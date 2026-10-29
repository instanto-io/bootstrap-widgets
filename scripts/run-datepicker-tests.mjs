#!/usr/bin/env node
import assert from 'node:assert/strict';
import { spawn } from 'node:child_process';
import { mkdtempSync, rmSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join } from 'node:path';
import { CdpClient, findChrome, startStaticServer, reservePort, poll, stopBrowser } from './run-browser-behaviour-tests.mjs';

const targets = [
  { name: 'GWT3', generation: 3, path: '/index.html' },
  { name: 'TeaVM3', generation: 3, path: '/teavm.html' },
  { name: 'GWT5', generation: 5, path: '/bootstrap5/index.html' },
  { name: 'TeaVM5', generation: 5, path: '/teavm-bootstrap5.html' },
].filter(t => !process.env.BROWSER_TEST_TARGET || t.name === process.env.BROWSER_TEST_TARGET);

assert.ok(targets.length, 'BROWSER_TEST_TARGET must name a supported showcase');
const server = await startStaticServer();
const origin = `http://127.0.0.1:${server.address().port}`;
const port = await reservePort();
const profile = mkdtempSync(join(tmpdir(), 'datepicker-browser-'));
const browser = spawn(findChrome(), ['--headless=new', '--no-first-run', '--no-default-browser-check',
  '--no-sandbox', '--disable-dev-shm-usage', '--disable-gpu', '--disable-background-networking',
  `--remote-debugging-port=${port}`, `--user-data-dir=${profile}`, 'about:blank'], {stdio: 'ignore'});
let cdp;
let failures = 0;
try {
  const target = await poll(async () => (await (await fetch(`http://127.0.0.1:${port}/json/list`)).json()).find(t => t.type === 'page'), 'Chrome');
  cdp = new CdpClient(target.webSocketDebuggerUrl);
  await cdp.connect();
  const errors = [];
  let delayedScript;
  cdp.on('Fetch.requestPaused', event => { delayedScript = event.requestId; });
  cdp.on('Page.javascriptDialogOpening', ({message}) => {
    errors.push(`Unexpected dialog: ${message}`);
    cdp.send('Page.handleJavaScriptDialog', {accept: false}).catch(() => {});
  });
  cdp.on('Runtime.exceptionThrown', ({exceptionDetails: e}) => errors.push(e.exception?.description || e.text));
  cdp.on('Network.responseReceived', ({response: r}) => { if (r.status >= 400) errors.push(`${r.status} ${r.url}`); });
  await Promise.all(['Page.enable', 'Runtime.enable', 'Network.enable'].map(method => cdp.send(method)));
  await cdp.send('Emulation.setTimezoneOverride', {timezoneId: process.env.BROWSER_TEST_TIMEZONE || 'Europe/London'});
  await cdp.send('Emulation.setDeviceMetricsOverride', {width: 1440, height: 1100, deviceScaleFactor: 1, mobile: false});
  const evaluate = async expression => {
    const r = await cdp.send('Runtime.evaluate', {expression, returnByValue: true, awaitPromise: true});
    if (r.exceptionDetails) throw new Error(r.exceptionDetails.exception?.description || r.exceptionDetails.text);
    return r.result.value;
  };
  const wait = (expression, label) => poll(() => evaluate(`!!(${expression})`), label);
  const q = selector => `document.querySelector(${JSON.stringify(selector)})`;
  async function click(selector) {
    await wait(`${q(selector)} && ${q(selector)}.getClientRects().length`, `visible ${selector}`);
    const point = await evaluate(`(() => { const e=${q(selector)}; e.scrollIntoView({block:'center'}); const r=e.getBoundingClientRect(); return {x:r.x+r.width/2,y:r.y+r.height/2}; })()`);
    await cdp.send('Input.dispatchMouseEvent', {type:'mousePressed',button:'left',clickCount:1,...point});
    await cdp.send('Input.dispatchMouseEvent', {type:'mouseReleased',button:'left',clickCount:1,...point});
  }
  async function button(text) {
    await evaluate(`Array.from(document.querySelectorAll('button')).find(e=>e.textContent===${JSON.stringify(text)}).id='datepicker-test-action'`);
    await click('#datepicker-test-action');
    await evaluate(`document.querySelector('#datepicker-test-action').removeAttribute('id')`);
  }
  async function type(selector, text) {
    await click(selector);
    await evaluate(`${q(selector)}.select()`);
    if (!text) {
      await cdp.send('Input.dispatchKeyEvent', {type:'keyDown',key:'Backspace',code:'Backspace',windowsVirtualKeyCode:8});
      await cdp.send('Input.dispatchKeyEvent', {type:'keyUp',key:'Backspace',code:'Backspace',windowsVirtualKeyCode:8});
    } else {
      await cdp.send('Input.insertText', {text});
      await cdp.send('Input.dispatchKeyEvent', {type:'keyUp',key:text.at(-1),windowsVirtualKeyCode:text.charCodeAt(text.length-1)});
    }
    await cdp.send('Input.dispatchKeyEvent', {type:'keyDown',key:'Tab',code:'Tab',windowsVirtualKeyCode:9});
    await cdp.send('Input.dispatchKeyEvent', {type:'keyUp',key:'Tab',code:'Tab',windowsVirtualKeyCode:9});
  }
  for (const t of targets) {
    errors.length = 0;
    try {
      const input = t.generation === 3 ? '#event-date-picker' : 'input[placeholder="Pick a date"]';
      const result = t.generation === 3 ? '#date-picker-events' : '#date-picker-value';
      if (t.name === 'TeaVM3') {
        delayedScript = undefined;
        await cdp.send('Fetch.enable', {patterns:[{urlPattern:'*bootstrap-datepicker-1.10.1.min.cache.js',requestStage:'Request'}]});
      }
      await cdp.send('Page.navigate', {url: `${origin}${t.path}?date-test=${Date.now()}#datePicker`});
      await cdp.send('Page.bringToFront');
      await wait(`${q(input)} && ${q(result)}`, `${t.name} datepicker page`);
      if (t.name === 'TeaVM3') {
        await poll(() => delayedScript, 'the datepicker resource request');
        await evaluate(`window.__detachedDatePicker=${q(input)}; true`);
        await button('Add/Remove');
        assert.equal(await evaluate(`!!${q(input)}`), false);
        await cdp.send('Fetch.continueRequest', {requestId:delayedScript});
        await cdp.send('Fetch.disable');
        await wait(`window.jQuery?.fn.datepicker`, 'delayed datepicker library');
        assert.equal(await evaluate(`!!window.jQuery(window.__detachedDatePicker).data('datepicker')`), false, 'late resource callback cannot initialize a detached picker');
        await button('Add/Remove');
      }
      if (t.generation === 3) {
        const today = await evaluate(`(() => { const d=new Date(); return [d.getMonth()+1,d.getDate()].map(n=>String(n).padStart(2,'0')).join('/')+'/'+d.getFullYear(); })()`);
        assert.equal(await evaluate(`${q(input)}.value`), today, 'value provided before UiBinder and attachment is preserved');
      }
      const count = () => evaluate(`Number(${q(result)}.dataset.changeCount || 0)`);
      async function verify(iso, before) {
        await wait(`${q(result)}.dataset.value === ${JSON.stringify(iso)}`, `${t.name} Java getValue ${iso}`);
        assert.equal(await evaluate(`${q(result)}.dataset.sourceMatch`), 'true');
        assert.equal(await count(), before + 1, 'exactly one event per listener');
        if (t.generation === 3) {
          if (iso !== 'null') {
            const expected = await evaluate(`new Date(${JSON.stringify(iso)} + 'T00:00:00').getTime()`);
            assert.equal(await evaluate(`Number(${q(result)}.dataset.time)`), expected, 'Java date is local midnight');
          }
          const entries = await evaluate(`Array.from(${q(result)}.querySelectorAll('p')).filter(e=>e.textContent.startsWith('Value Changed:')).map(e=>e.textContent)`);
          assert.equal(entries.length, await count(), 'second listener receives the same number of events');
          assert.equal(entries.at(-1), `Value Changed: ${iso}`);
        } else {
          const text = await evaluate(`${q(result)}.textContent`);
          assert.ok(text.includes(iso === 'null' ? 'Cleared.' : iso), `Java event value: ${text}`);
        }
      }
      async function choose() {
        if (t.generation === 3) {
          await wait(`window.jQuery && window.jQuery(${q(input)}).data('datepicker')`, 'datepicker loaded');
          await click(input);
        } else {
          const toggle = 'input[placeholder="Pick a date"] + [data-td-toggle]';
          // Rendering the Java widget precedes the asynchronous plugin download.
          // Wait for Tempus Dominus to bind its native toggle before clicking it.
          const {result: handle} = await cdp.send('Runtime.evaluate', {expression:q(toggle)});
          try {
            await poll(async () => {
              const {listeners} = await cdp.send('DOMDebugger.getEventListeners', {objectId:handle.objectId});
              return listeners.some(listener => listener.type === 'click');
            }, 'Tempus Dominus toggle binding');
          } finally {
            await cdp.send('Runtime.releaseObject', {objectId:handle.objectId});
          }
          await click(toggle);
        }
        const day = t.generation === 3 ? '.datepicker-days td.day:not(.old):not(.new):not(.active):not(.disabled)'
          : '.tempus-dominus-widget [data-action="selectDay"]:not(.old):not(.new):not(.active):not(.disabled)';
        await wait(`${q(day)} && ${q(day)}.getClientRects().length`, 'calendar day');
        // Tempus Dominus can select today when opened. Count only the deliberate day selection.
        const before = await count();
        await click(day);
        const value = await evaluate(`${q(input)}.value`);
        const iso = t.generation === 3 ? `${value.slice(6)}-${value.slice(0,2)}-${value.slice(3,5)}` : value;
        await verify(iso, before);
        if (t.generation === 5) await click('.tempus-dominus-widget [data-action="close"]');
      }
      await choose();
      let before = await count();
      await type(input, t.generation === 3 ? '02/17/2031' : '2031-02-17');
      await verify('2031-02-17', before);
      before = await count();
      await type(input, t.generation === 3 ? '07/17/2031' : '2031-07-17');
      await verify('2031-07-17', before);
      before = await count();
      await type(input, '');
      await verify('null', before);
      if (t.generation === 3) {
        before = await count();
        await type(input, '02/17/2031');
        await verify('2031-02-17', before);
        await evaluate(`window.__externalDateChanges=0; window.jQuery(${q(input)}).on('changeDate.applicationProbe', ()=>window.__externalDateChanges++); true`);
        for (let n=0;n<3;n++) {
          await button('Add/Remove');
          assert.equal(await evaluate(`!!${q(input)}`), false);
          await button('Add/Remove');
          await wait(`${q(input)} && window.jQuery(${q(input)}).data('datepicker')`, 'remounted datepicker');
          assert.equal(await evaluate(`${q(input)}.value`), '02/17/2031');
        }
        await choose();
        assert.equal(await evaluate('window.__externalDateChanges'), 1, 'remount preserves application listeners');
        // Language is a Java setter, not a data attribute; identify its configured input.
        await evaluate(`Array.from(document.querySelectorAll('input')).find(e=>window.jQuery(e).data('datepicker')?.o.language==='fr').id='french-date-picker'`);
        await click('#french-date-picker');
        await wait(`Array.from(document.querySelectorAll('.datepicker-dropdown .dow')).some(e=>e.textContent==='l')`, 'French weekday labels');
        await type('#methods-date-picker', '01/15/2015');
        await button('Set Start Date');
        await button('Set End Date');
        await button('Show');
        const disabled = day => evaluate(`Array.from(document.querySelectorAll('.datepicker-days td.day:not(.old):not(.new)')).find(e=>e.textContent===${JSON.stringify(String(day))}).classList.contains('disabled')`);
        assert.equal(await disabled(1), true);
        assert.equal(await disabled(15), false);
        assert.equal(await disabled(31), true);
        await button('Clear End Date');
        await button('Show');
        assert.equal(await disabled(1), true, 'clearing the end leaves the start limit intact');
        assert.equal(await disabled(31), false, 'clearing the end allows later dates');
        await button('Clear Start Date');
        await button('Show');
        assert.equal(await disabled(1), false);
        await button('Disable Sundays');
        await button('Set Format');
        await button('Show');
        assert.equal(await evaluate(`${q('#methods-date-picker')}.value`), '15.01.2015');
        assert.equal(await disabled(4), true, 'disabled weekdays survive reload');
        await button('Enable Sundays');
        await button('Show');
        assert.equal(await disabled(4), false);
        await button('Hide');
      }
      if (t.generation === 5) {
        before = await count();
        await type(input, '2031-07-17');
        await verify('2031-07-17', before);
        for (let n=0;n<3;n++) {
          await button('Remove / restore date picker');
          assert.equal(await evaluate(`!!${q(input)}`), false);
          await button('Remove / restore date picker');
          await wait(`${q(input)}?.value === '2031-07-17'`, 'restored Bootstrap 5 value');
        }
        await choose();
      }
      assert.deepEqual(errors, [], 'no browser errors');
      console.log(`ok - ${t.name}: open, select, Java values and events, typed format, clear, remount${t.generation===3?', French locale, limits, reload':''}`);
    } catch (e) {
      await cdp.send('Fetch.disable');
      failures++;
      console.error(await evaluate(`({inputs:Array.from(document.querySelectorAll('input')).map(e=>({id:e.id,value:e.value})), events:document.querySelector('#date-picker-events')?.outerHTML, value:document.querySelector('#date-picker-value')?.outerHTML})`));
      console.error(`not ok - ${t.name}: ${e.stack || e}\n${errors.join('\n')}`);
    }
  }
} finally {
  await stopBrowser(browser, cdp);
  cdp?.close();
  server.close();
  rmSync(profile, {recursive:true,force:true,maxRetries:10,retryDelay:200});
}
if (failures) process.exitCode = 1;
