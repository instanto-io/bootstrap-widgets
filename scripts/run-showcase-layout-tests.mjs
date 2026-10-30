#!/usr/bin/env node
import assert from 'node:assert/strict';
import { spawn } from 'node:child_process';
import { mkdtempSync, rmSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join } from 'node:path';
import { CdpClient, findChrome, startStaticServer, reservePort, poll, stopBrowser } from './run-browser-behaviour-tests.mjs';

const server = await startStaticServer();
const origin = `http://127.0.0.1:${server.address().port}`;
const port = await reservePort();
const profile = mkdtempSync(join(tmpdir(), 'showcase-layout-'));
const browser = spawn(findChrome(), ['--headless=new', '--no-first-run', '--no-default-browser-check',
  '--no-sandbox', '--disable-dev-shm-usage', '--disable-gpu',
  `--remote-debugging-port=${port}`, `--user-data-dir=${profile}`, 'about:blank'], {stdio:'ignore'});
let cdp;
try {
  const target = await poll(async () => (await (await fetch(`http://127.0.0.1:${port}/json/list`)).json()).find(t => t.type === 'page'), 'Chrome');
  cdp = new CdpClient(target.webSocketDebuggerUrl);
  await cdp.connect();
  const errors = [];
  cdp.on('Runtime.exceptionThrown', ({exceptionDetails:e}) => errors.push(e.exception?.description || e.text));
  cdp.on('Network.responseReceived', ({response:r}) => { if (r.status >= 400) errors.push(`${r.status} ${r.url}`); });
  await Promise.all(['Page.enable','Runtime.enable','Network.enable'].map(method => cdp.send(method)));
  async function evaluate(expression) {
    const r = await cdp.send('Runtime.evaluate', {expression,returnByValue:true,awaitPromise:true});
    if (r.exceptionDetails) throw new Error(r.exceptionDetails.exception?.description || r.exceptionDetails.text);
    return r.result.value;
  }
  for (const width of [1440, 390]) {
    await cdp.send('Emulation.setDeviceMetricsOverride', {width,height:1000,deviceScaleFactor:1,mobile:false});
    for (const [generation, paths] of [[3,['/index.html','/teavm.html']], [5,['/bootstrap5/index.html','/teavm-bootstrap5.html']]]) {
      for (const route of ['home','buttons']) {
        const layouts = [];
        for (const path of paths) {
          errors.length = 0;
          await cdp.send('Page.navigate', {url:`${origin}${path}#${route}`});
          await poll(() => evaluate(`!!document.querySelector('h1, h2') && Array.from(document.querySelectorAll('link[rel="stylesheet"]')).every(link=>!!link.sheet)`), `${path} styles and heading`);
          await evaluate('document.fonts.ready.then(()=>true)');
          layouts.push(await evaluate(`(() => {
            const geometry = selector => {
              const e=document.querySelector(selector);
              if (!e) return null;
              const r=e.getBoundingClientRect(), s=getComputedStyle(e);
              return {x:r.x,y:r.y,width:r.width,height:r.height,fontSize:s.fontSize,marginTop:s.marginTop,padding:s.padding};
            };
            return {bodyPadding:getComputedStyle(document.body).paddingTop,
              heading:geometry('h1, h2'), navigation:geometry('.navbar'),
              pageHeader:geometry('.page-header'), code:geometry('pre')};
          })()`));
          assert.deepEqual(errors, [], `${path}: no failed resources or browser exceptions`);
        }
        // Compare rendered geometry and styles rather than compiler-specific DOM wrappers.
        for (const [key, expected] of Object.entries(layouts[0])) {
          const actual = layouts[1][key];
          if (expected && typeof expected === 'object') {
            assert.ok(actual, `Bootstrap ${generation} ${route}: missing ${key}`);
            for (const [property, value] of Object.entries(expected)) {
              if (typeof value === 'number') assert.ok(Math.abs(actual[property]-value) <= 1,
                `Bootstrap ${generation} ${route} ${width}px ${key}.${property}: GWT=${value}, TeaVM=${actual[property]}`);
              else assert.equal(actual[property], value, `${key}.${property}`);
            }
          } else assert.equal(actual, expected, `${key}: Bootstrap ${generation} ${route} at ${width}px`);
        }
        console.log(`ok - Bootstrap ${generation} ${route}: GWT/TeaVM layout matches at ${width}px`);
      }
    }
  }
} finally {
  await stopBrowser(browser, cdp);
  cdp?.close();
  server.close();
  rmSync(profile, {recursive:true,force:true,maxRetries:10,retryDelay:200});
}
