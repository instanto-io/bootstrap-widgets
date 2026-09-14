#!/usr/bin/env node

import assert from "node:assert/strict";
import { spawn, spawnSync } from "node:child_process";
import { createServer } from "node:http";
import { createServer as createNetServer } from "node:net";
import { existsSync, mkdtempSync, readFileSync, rmSync, statSync } from "node:fs";
import { tmpdir } from "node:os";
import { extname, join, resolve, sep } from "node:path";
import { fileURLToPath } from "node:url";

const root = resolve(fileURLToPath(new URL("..", import.meta.url)));
const pages = resolve(process.env.PAGES_OUTPUT_DIR || join(root, "showcase-site/target/pages"));
const fixtureTargets = [
  { name: "GWT3", generation: 3, path: "/fixtures/gwt-bootstrap3/index.html" },
  { name: "GWT5", generation: 5, path: "/fixtures/gwt-bootstrap5/index.html" },
].filter((target) => !process.env.BROWSER_TEST_TARGET || target.name === process.env.BROWSER_TEST_TARGET);
const timeoutMs = Number(process.env.BROWSER_TEST_TIMEOUT_MS || 15000);
const caseFilter = process.env.BROWSER_TEST_CASE;

function findChrome() {
  const candidates = [
    process.env.CHROME_BIN,
    "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
    "google-chrome",
    "google-chrome-stable",
    "chromium",
    "chromium-browser",
  ].filter(Boolean);
  for (const candidate of candidates) {
    if (candidate.includes(sep) && existsSync(candidate)) {
      return candidate;
    }
    const located = spawnSync("sh", ["-c", `command -v ${candidate}`], { encoding: "utf8" });
    if (located.status === 0 && located.stdout.trim()) {
      return located.stdout.trim();
    }
  }
  throw new Error("Chrome or Chromium is required for browser behaviour tests");
}

function mimeType(path) {
  return {
    ".css": "text/css",
    ".html": "text/html",
    ".js": "text/javascript",
    ".json": "application/json",
    ".map": "application/json",
    ".png": "image/png",
    ".svg": "image/svg+xml",
    ".woff": "font/woff",
    ".woff2": "font/woff2",
  }[extname(path)] || "application/octet-stream";
}

function startStaticServer() {
  const server = createServer((request, response) => {
    try {
      const pathname = decodeURIComponent(new URL(request.url, "http://127.0.0.1").pathname);
      let path = resolve(pages, `.${pathname}`);
      if (path !== pages && !path.startsWith(`${pages}${sep}`)) {
        throw new Error("Path escaped the Pages root");
      }
      if (statSync(path).isDirectory()) {
        path = join(path, "index.html");
      }
      response.writeHead(200, { "Content-Type": mimeType(path), "Cache-Control": "no-store" });
      response.end(readFileSync(path));
    } catch (error) {
      response.writeHead(404, { "Content-Type": "text/plain" });
      response.end(String(error));
    }
  });
  return new Promise((resolveServer) => {
    server.listen(0, "127.0.0.1", () => resolveServer(server));
  });
}

function reservePort() {
  return new Promise((resolvePort, reject) => {
    const server = createNetServer();
    server.once("error", reject);
    server.listen(0, "127.0.0.1", () => {
      const { port } = server.address();
      server.close((error) => error ? reject(error) : resolvePort(port));
    });
  });
}

async function poll(operation, description, timeout = timeoutMs) {
  const deadline = Date.now() + timeout;
  let lastError;
  while (Date.now() < deadline) {
    try {
      const value = await operation();
      if (value) {
        return value;
      }
    } catch (error) {
      lastError = error;
    }
    await new Promise((resolveWait) => setTimeout(resolveWait, 50));
  }
  throw new Error(`Timed out waiting for ${description}${lastError ? `: ${lastError.message}` : ""}`);
}

function signalAndWait(child, signal, timeout) {
  if (child.exitCode !== null || child.signalCode !== null) {
    return Promise.resolve(true);
  }
  return new Promise((resolveExit) => {
    let timer;
    const onExit = () => {
      clearTimeout(timer);
      resolveExit(true);
    };
    child.once("exit", onExit);
    if (signal) child.kill(signal);
    timer = setTimeout(() => {
      child.off("exit", onExit);
      resolveExit(false);
    }, timeout);
  });
}

async function stopBrowser(browser, cdp) {
  if (cdp) {
    try {
      await cdp.send("Browser.close");
      if (await signalAndWait(browser, null, 5000)) return;
    } catch {
      // A disconnected browser still needs the process shutdown fallback.
    }
  }
  if (await signalAndWait(browser, "SIGTERM", 5000)) return;
  if (await signalAndWait(browser, "SIGKILL", 2000)) return;
  browser.unref();
}

class CdpClient {
  constructor(url) {
    this.url = url;
    this.nextId = 1;
    this.pending = new Map();
    this.handlers = new Map();
  }

  async connect() {
    this.socket = new WebSocket(this.url);
    await new Promise((resolveOpen, reject) => {
      this.socket.addEventListener("open", resolveOpen, { once: true });
      this.socket.addEventListener("error", reject, { once: true });
    });
    this.socket.addEventListener("message", (event) => this.receive(JSON.parse(event.data)));
  }

  receive(message) {
    if (message.id) {
      const pending = this.pending.get(message.id);
      if (!pending) return;
      this.pending.delete(message.id);
      if (message.error) pending.reject(new Error(message.error.message));
      else pending.resolve(message.result);
      return;
    }
    for (const handler of this.handlers.get(message.method) || []) {
      handler(message.params || {});
    }
  }

  on(method, handler) {
    const handlers = this.handlers.get(method) || [];
    handlers.push(handler);
    this.handlers.set(method, handlers);
  }

  send(method, params = {}) {
    const id = this.nextId++;
    return new Promise((resolveCommand, reject) => {
      const timer = setTimeout(() => {
        if (this.pending.delete(id)) {
          reject(new Error(`Timed out waiting for Chrome response to ${method}`));
        }
      }, timeoutMs);
      this.pending.set(id, {
        resolve: (value) => {
          clearTimeout(timer);
          resolveCommand(value);
        },
        reject: (error) => {
          clearTimeout(timer);
          reject(error);
        },
      });
      try {
        this.socket.send(JSON.stringify({ id, method, params }));
      } catch (error) {
        this.pending.delete(id);
        clearTimeout(timer);
        reject(error);
      }
    });
  }

  close() {
    this.socket.close();
  }
}

async function main() {
  for (const target of fixtureTargets) {
    if (!existsSync(join(pages, target.path))) {
      throw new Error(`Missing assembled ${target.name} browser fixtures under ${pages}`);
    }
  }

  const server = await startStaticServer();
  const appPort = server.address().port;
  const debugPort = await reservePort();
  const initialUrl = `http://127.0.0.1:${appPort}${fixtureTargets[0].path}`;
  const profile = mkdtempSync(join(tmpdir(), "bootstrap-widget-browser-"));
  const browser = spawn(findChrome(), [
    "--headless=new",
    "--no-sandbox",
    "--disable-dev-shm-usage",
    "--disable-gpu",
    "--disable-background-networking",
    `--remote-debugging-port=${debugPort}`,
    `--user-data-dir=${profile}`,
    initialUrl,
  ], { stdio: ["ignore", "ignore", "pipe"] });
  let browserStderr = "";
  browser.stderr.on("data", (chunk) => { browserStderr += chunk.toString(); });

  let cdp;
  try {
    const target = await poll(async () => {
      const response = await fetch(`http://127.0.0.1:${debugPort}/json/list`);
      if (!response.ok) return null;
      const targets = await response.json();
      return targets.find((candidate) => candidate.type === "page" && candidate.url.startsWith(initialUrl));
    }, "the Chrome DevTools target");

    cdp = new CdpClient(target.webSocketDebuggerUrl);
    await cdp.connect();
    const diagnostics = [];
    cdp.on("Runtime.exceptionThrown", ({ exceptionDetails }) => {
      const exception = exceptionDetails?.exception;
      const frames = exceptionDetails?.stackTrace?.callFrames || [];
      const stack = frames.map((frame) =>
        `${frame.functionName || "<anonymous>"} (${frame.url}:${frame.lineNumber + 1}:${frame.columnNumber + 1})`
      ).join("\n  at ");
      diagnostics.push(`Uncaught exception: ${exception?.description || exception?.value
        || exceptionDetails?.text || "unknown"}${stack ? `\n  at ${stack}` : ""}`);
    });
    cdp.on("Log.entryAdded", ({ entry }) => {
      if (entry?.level === "error") diagnostics.push(`Browser log: ${entry.text}`);
    });
    cdp.on("Network.responseReceived", ({ response }) => {
      if (response?.status >= 400) diagnostics.push(`HTTP ${response.status}: ${response.url}`);
    });
    cdp.on("Network.loadingFailed", ({ errorText, type }) => {
      if (errorText === "net::ERR_ABORTED") return;
      diagnostics.push(`Network failure (${type}): ${errorText}`);
    });

    await Promise.all([
      cdp.send("Page.enable"),
      cdp.send("Runtime.enable"),
      cdp.send("Log.enable"),
      cdp.send("Network.enable"),
    ]);
    await cdp.send("Emulation.setDeviceMetricsOverride", {
      width: 390,
      height: 1200,
      deviceScaleFactor: 3,
      mobile: true,
    });
    await cdp.send("Emulation.setTouchEmulationEnabled", { enabled: true, maxTouchPoints: 5 });

    async function evaluate(expression) {
      const response = await cdp.send("Runtime.evaluate", {
        expression,
        awaitPromise: true,
        returnByValue: true,
      });
      if (response.exceptionDetails) {
        throw new Error(response.exceptionDetails.exception?.description || response.exceptionDetails.text);
      }
      return response.result.value;
    }

    async function waitFor(expression, description, timeout) {
      return poll(async () => {
        try {
          return await evaluate(expression);
        } catch {
          return false;
        }
      }, description, timeout);
    }

    async function dispatchTouchTap(x, y) {
      await cdp.send("Input.dispatchTouchEvent", {
        type: "touchStart",
        touchPoints: [{ x, y, radiusX: 1, radiusY: 1, force: 1, id: 1 }],
      });
      await new Promise((resolveWait) => setTimeout(resolveWait, 50));
      await cdp.send("Input.dispatchTouchEvent", {
        type: "touchEnd",
        touchPoints: [],
      });
    }

    async function freshPage(fixtureUrl, name) {
      diagnostics.length = 0;
      await cdp.send("Storage.clearDataForOrigin", {
        origin: new URL(fixtureUrl).origin,
        storageTypes: "local_storage",
      });
      const caseUrl = `${fixtureUrl}?case=${encodeURIComponent(name)}&t=${Date.now()}`;
      await cdp.send("Page.navigate", { url: caseUrl });
      await waitFor(
        `new URL(location.href).searchParams.get('case') === ${JSON.stringify(name)}
          && document.body && document.body.dataset.fixturesReady === 'true'
          && window.__bootstrapWidgetFixturesReady === true`,
        "the GWT fixtures to become ready",
      );
      assert.ok(await evaluate("navigator.maxTouchPoints > 0"), "Chrome touch emulation was not active");
      // Keep independent cases outside Chrome's double-tap recognition window.
      await new Promise((resolveWait) => setTimeout(resolveWait, 400));
    }

    async function tapSelector(selector, description, allowPointerPassThrough = false) {
      await evaluate(`new Promise((resolve) => {
        const element = document.querySelector(${JSON.stringify(selector)});
        if (!element) throw new Error(${JSON.stringify(`Missing ${description}`)});
        element.scrollIntoView({block: 'center', inline: 'center', behavior: 'instant'});
        requestAnimationFrame(() => {
          const rect = element.getBoundingClientRect();
          if (rect.top < 0 || rect.bottom > window.innerHeight) {
            window.scrollBy(0, rect.top + rect.height / 2 - window.innerHeight / 2);
          }
          requestAnimationFrame(() => resolve(true));
        });
      })`);
      const point = await evaluate(`(() => {
        const element = document.querySelector(${JSON.stringify(selector)});
        const hitTarget = element.matches('button, a, input, label')
          ? element
          : element.querySelector('label, button, a, input') || element;
        let rect = hitTarget.getBoundingClientRect();
        if (rect.top < 0 || rect.bottom > window.innerHeight
            || rect.left < 0 || rect.right > window.innerWidth) {
          hitTarget.scrollIntoView({block: 'center', inline: 'center', behavior: 'instant'});
          rect = hitTarget.getBoundingClientRect();
        }
        const x = rect.left + rect.width / 2;
        const y = rect.top + rect.height / 2;
        const hit = document.elementFromPoint(x, y);
        const expectedTestId = element.getAttribute('data-testid');
        const hitTestId = hit && hit.closest('[data-testid]')
          ? hit.closest('[data-testid]').getAttribute('data-testid') : null;
        return {
          x,
          y,
          rect: {left: rect.left, top: rect.top, width: rect.width, height: rect.height},
          viewport: {width: window.innerWidth, height: window.innerHeight},
          scroll: {x: window.scrollX, y: window.scrollY},
          hitStack: document.elementsFromPoint(x, y).slice(0, 5).map((candidate) => ({
            tag: candidate.tagName,
            className: candidate.className,
            testId: candidate.getAttribute && candidate.getAttribute('data-testid')
          })),
          selectedTargetHit: Boolean(hit && (
            hit === hitTarget || hitTarget.contains(hit) || hit.contains(hitTarget)
              || (expectedTestId && expectedTestId === hitTestId)
          )),
          hitTestId
        };
      })()`);
      if (!allowPointerPassThrough) {
        assert.equal(point.selectedTargetHit, true,
          `Touch point for ${description} was obscured: ${JSON.stringify(point)}`);
      }
      await dispatchTouchTap(point.x, point.y);
      await new Promise((resolveWait) => setTimeout(resolveWait, 100));
    }

    async function tap(testId, allowPointerPassThrough = false) {
      const selector = `[data-testid=${JSON.stringify(testId)}]`;
      await tapSelector(selector, `fixture ${testId}`, allowPointerPassThrough);
    }

    async function tapOutside() {
      const point = await evaluate("({x: document.documentElement.clientWidth - 8, y: window.innerHeight - 8})");
      await dispatchTouchTap(point.x, point.y);
      await new Promise((resolveWait) => setTimeout(resolveWait, 100));
    }

    async function pressKey(key, code, keyCode) {
      const keyEvent = {
        key,
        code,
        windowsVirtualKeyCode: keyCode,
        nativeVirtualKeyCode: keyCode,
      };
      await cdp.send("Input.dispatchKeyEvent", { type: "rawKeyDown", ...keyEvent });
      await cdp.send("Input.dispatchKeyEvent", { type: "keyUp", ...keyEvent });
      await new Promise((resolveWait) => setTimeout(resolveWait, 100));
    }

    async function replaceText(testId, value) {
      await tap(testId);
      await evaluate(`document.querySelector('[data-testid=' +
        ${JSON.stringify(JSON.stringify(testId))} + ']').select()`);
      await cdp.send("Input.insertText", { text: value });
      await pressKey("Tab", "Tab", 9);
    }

    async function enterSuggestQuery(testId, value) {
      await tap(testId);
      await evaluate(`(() => {
        const input = document.querySelector('[data-testid=' +
          ${JSON.stringify(JSON.stringify(testId))} + ']');
        input.value = ${JSON.stringify(value)};
        const event = new KeyboardEvent('keyup', {
          key: ${JSON.stringify(value.slice(-1) || "Backspace")},
          code: 'KeyU',
          bubbles: true,
          cancelable: true
        });
        Object.defineProperty(event, 'keyCode', {value: ${value ? 85 : 8}});
        Object.defineProperty(event, 'which', {value: ${value ? 85 : 8}});
        input.dispatchEvent(event);
      })()`);
      await new Promise((resolveWait) => setTimeout(resolveWait, 150));
    }

    async function state(testId) {
      return evaluate(`(() => {
        const element = document.querySelector('[data-testid=' + ${JSON.stringify(JSON.stringify(testId))} + ']');
        const input = element && element.querySelector('input');
        const paintTarget = input ? element.querySelector('label') || element : element;
        const paint = paintTarget && getComputedStyle(paintTarget);
        return element && {
          active: element.classList.contains('active') || Boolean(element.querySelector('label.active')),
          open: element.classList.contains('open') || element.classList.contains('show')
            || Boolean(element.querySelector('.dropdown-toggle.show, .dropdown-menu.show')),
          shown: element.classList.contains('in') || element.classList.contains('show'),
          disabled: element.hasAttribute('disabled'),
          ariaPressed: element.getAttribute('aria-pressed'),
          ariaExpanded: element.getAttribute('aria-expanded'),
          ariaBusy: element.getAttribute('aria-busy'),
          clickCount: Number(element.dataset.clickCount || 0),
          changeCount: Number(element.dataset.changeCount || 0),
          value: element.dataset.value,
          checked: input ? input.checked : undefined,
          backgroundColor: paint ? paint.backgroundColor : undefined,
          text: element.textContent.trim(),
          events: element.dataset.eventOrder || ''
        };
      })()`);
    }

    function testsFor(target) {
      const tests = [];
      const test = (id, name, body) => tests.push({ name: `${target.name}-TOUCH-${id} ${name}`, body });

      test("BTN-001/002", "toggle receives one click per activation and toggles twice", async () => {
        await tap("behaviour/toggle-button/basic");
        let result = await state("behaviour/toggle-button/basic");
        assert.equal(result.active, true);
        assert.equal(result.ariaPressed, "true");
        assert.equal(result.clickCount, 1);
        const activeBackground = result.backgroundColor;
        await tap("behaviour/toggle-button/basic");
        result = await state("behaviour/toggle-button/basic");
        assert.equal(result.active, false);
        assert.equal(result.ariaPressed, "false");
        assert.equal(result.clickCount, 2);
        assert.notEqual(result.backgroundColor, activeBackground,
          "inactive touch paint must differ from active paint");
      });

      test("BTN-003", "checkbox value assignment honours the fire-events flag", async () => {
        await tap("behaviour/check-box-button/set-silent");
        let result = await state("behaviour/check-box-button/basic");
        assert.equal(result.checked, true, JSON.stringify(result));
        assert.equal(result.value, "true");
        assert.equal(result.changeCount, 0);

        await tap("behaviour/check-box-button/set-firing");
        result = await state("behaviour/check-box-button/basic");
        assert.equal(result.checked, false);
        assert.equal(result.value, "false");
        assert.equal(result.changeCount, 1);
      });

      test("BTN-004", "disabled toggle suppresses touch activation", async () => {
      await tap("behaviour/toggle-button/disabled", true);
      const result = await state("behaviour/toggle-button/disabled");
      assert.equal(result.active, false);
      assert.equal(result.clickCount, 0);
    });

      test("BTN-005", "checkbox buttons retain independent touch selections", async () => {
      await tap("behaviour/check-box-button/first");
      await tap("behaviour/check-box-button/third");
      const first = await state("behaviour/check-box-button/first");
      const second = await state("behaviour/check-box-button/second");
      const third = await state("behaviour/check-box-button/third");
      assert.equal(first.active, true);
      assert.equal(first.checked, true);
      assert.equal(first.value, "true");
      assert.equal(first.changeCount, 1);
      assert.equal(first.clickCount, 1);
      assert.equal(second.active, false);
      assert.equal(second.checked, false);
      assert.equal(second.value, "false");
      assert.equal(second.changeCount, 0);
      assert.equal(second.clickCount, 0);
      assert.equal(third.active, true);
      assert.equal(third.checked, true);
      assert.equal(third.value, "true");
      assert.equal(third.changeCount, 1);
      assert.equal(third.clickCount, 1);
    });

      test("BTN-006", "radio button remains exclusive and reports once", async () => {
      await tap("behaviour/radio-button/second");
      await waitFor(
        "document.querySelector('[data-testid=\"behaviour/radio-button/second\"]').dataset.value === 'true'",
        "the deferred GWT radio value handler",
      );
      const first = await state("behaviour/radio-button/first");
      const second = await state("behaviour/radio-button/second");
      assert.equal(first.active, false);
      assert.equal(first.checked, false);
      assert.equal(first.value, "false");
      assert.equal(second.active, true);
      assert.equal(second.checked, true);
      assert.equal(second.value, "true");
      assert.equal(second.changeCount, 1);
      assert.equal(second.clickCount, 1);
    });

      test("DRP-001/002", "dropdown opens and closes from touch", async () => {
        await evaluate(`(() => {
        window.__dropdownEvents = [];
        const dropdown = document.querySelector('[data-testid="behaviour/dropdown/basic"]');
        const events = ['show', 'shown', 'hide', 'hidden'];
        if (${target.generation} === 3) {
          for (const event of events) {
            window.jQuery(dropdown).on(event + '.bs.dropdown', () => window.__dropdownEvents.push(event));
          }
        } else {
          for (const event of events) {
            dropdown.addEventListener(event + '.bs.dropdown', () => window.__dropdownEvents.push(event));
          }
        }
      })()`);
      await tap("behaviour/dropdown/toggle");
      let dropdown = await state("behaviour/dropdown/basic");
      let toggle = await state("behaviour/dropdown/toggle");
      assert.equal(dropdown.open, true);
      assert.equal(toggle.ariaExpanded, "true");
      assert.deepEqual(await evaluate("window.__dropdownEvents"), ["show", "shown"]);
      await tapOutside();
      dropdown = await state("behaviour/dropdown/basic");
      toggle = await state("behaviour/dropdown/toggle");
      assert.equal(dropdown.open, false);
      assert.equal(toggle.ariaExpanded, "false");
      assert.deepEqual(await evaluate("window.__dropdownEvents"), ["show", "shown", "hide", "hidden"]);
    });

      test("DRP-003", "Escape closes the dropdown and restores toggle focus", async () => {
        await tap("behaviour/dropdown/toggle");
        await waitFor(
          `document.querySelector('[data-testid="behaviour/dropdown/basic"]').classList.contains('open')
            || document.querySelector('[data-testid="behaviour/dropdown/basic"]').classList.contains('show')
            || Boolean(document.querySelector('[data-testid="behaviour/dropdown/basic"] .dropdown-menu.show'))`,
          "the dropdown to open",
        );
        await evaluate(`(() => {
          const item = document.querySelector('[data-testid="behaviour/dropdown/action"]');
          const focusTarget = item.matches('a, button, input') ? item : item.querySelector('a, button, input');
          focusTarget.focus();
        })()`);
        await pressKey("Escape", "Escape", 27);
        const dropdown = await state("behaviour/dropdown/basic");
        const focusedFixture = await evaluate(`document.activeElement.closest('[data-testid]')
          && document.activeElement.closest('[data-testid]').getAttribute('data-testid')`);
        assert.equal(dropdown.open, false);
        assert.equal(focusedFixture, "behaviour/dropdown/toggle");
      });

      test("DRP-004", "disabled dropdown item suppresses action and navigation", async () => {
        const locationBefore = await evaluate("window.location.href");
        await tap("behaviour/dropdown/toggle");
        await tap("behaviour/dropdown/disabled-item");
        const disabled = await state("behaviour/dropdown/disabled-item");
        assert.equal(disabled.clickCount, 0);
        assert.equal(await evaluate("window.location.href"), locationBefore);
      });

      test("DRP-005", "split dropdown keeps its primary action independent", async () => {
        await tap("behaviour/dropdown/split-primary");
        const primary = await state("behaviour/dropdown/split-primary");
        assert.equal(primary.clickCount, 1);
        assert.equal(await evaluate(`getComputedStyle(document.querySelector(
          '[data-testid="behaviour/dropdown/split-menu"]'
        )).display`), "none");

        await tap("behaviour/dropdown/split-toggle");
        await waitFor(
          `getComputedStyle(document.querySelector(
            '[data-testid="behaviour/dropdown/split-menu"]'
          )).display !== 'none'`,
          "the split dropdown menu to open",
        );
        assert.equal((await state("behaviour/dropdown/split-primary")).clickCount, 1);
      });

      test("DRP-006", "dropup menu is positioned above its toggle", async () => {
        await tap("behaviour/dropdown/dropup-toggle");
        await waitFor(
          `getComputedStyle(document.querySelector(
            '[data-testid="behaviour/dropdown/dropup-menu"]'
          )).display !== 'none'`,
          "the dropup menu to open",
        );
        const geometry = await evaluate(`(() => {
          const toggle = document.querySelector('[data-testid="behaviour/dropdown/dropup-toggle"]')
            .getBoundingClientRect();
          const menu = document.querySelector('[data-testid="behaviour/dropdown/dropup-menu"]')
            .getBoundingClientRect();
          return {menuBottom: menu.bottom, toggleTop: toggle.top};
        })()`);
        assert.ok(geometry.menuBottom <= geometry.toggleTop + 1, JSON.stringify(geometry));
      });

      test("DRP-007", "constrained dropdown menu matches its owning group", async () => {
        await tap("behaviour/dropdown/aligned-width-toggle");
        await waitFor(
          `getComputedStyle(document.querySelector(
            '[data-testid="behaviour/dropdown/aligned-width-menu"]'
          )).display !== 'none'`,
          "the constrained dropdown menu to open",
        );
        const geometry = await evaluate(`(() => {
          const group = document.querySelector('[data-testid="behaviour/dropdown/aligned-width"]')
            .getBoundingClientRect();
          const menuElement = document.querySelector(
            '[data-testid="behaviour/dropdown/aligned-width-menu"]'
          );
          const menu = menuElement.getBoundingClientRect();
          return {
            groupWidth: group.width,
            menuWidth: menu.width,
            overflow: Array.from(menuElement.children).some(
              (item) => item.scrollWidth > item.clientWidth + 1
            )
          };
        })()`);
        assert.ok(Math.abs(geometry.menuWidth - geometry.groupWidth) <= 1, JSON.stringify(geometry));
        assert.equal(geometry.overflow, false);
      });

      test("FRM-001", "checkbox label toggles its input once", async () => {
        await tap("behaviour/form/checkbox-label");
        const result = await state("behaviour/form/checkbox-label");
        assert.equal(result.checked, true);
        assert.equal(result.value, "true");
        assert.equal(result.changeCount, 1);
        const semantics = await evaluate(`(() => {
          const fixture = document.querySelector('[data-testid="behaviour/form/checkbox-label"]');
          const input = fixture.querySelector('input');
          const label = fixture.querySelector('label');
          return {
            associated: label.contains(input) || (Boolean(input.id) && label.htmlFor === input.id),
            sourceMatch: fixture.dataset.sourceMatch
          };
        })()`);
        assert.equal(semantics.associated, true);
        assert.equal(semantics.sourceMatch, "true");
      });

      test("FRM-002", "radio label selects its input once", async () => {
        await tap("behaviour/form/radio-label");
        const result = await state("behaviour/form/radio-label");
        assert.equal(result.checked, true);
        assert.equal(result.value, "true");
        assert.equal(result.changeCount, 1);
        const semantics = await evaluate(`(() => {
          const fixture = document.querySelector('[data-testid="behaviour/form/radio-label"]');
          const input = fixture.querySelector('input');
          const label = fixture.querySelector('label');
          return {
            associated: label.contains(input) || (Boolean(input.id) && label.htmlFor === input.id),
            sourceMatch: fixture.dataset.sourceMatch
          };
        })()`);
        assert.equal(semantics.associated, true);
        assert.equal(semantics.sourceMatch, "true");
      });

      test("FRM-003", "text field label transfers focus to its control", async () => {
        await tap("behaviour/form/text-label/label");
        const result = await evaluate(`(() => {
          const label = document.querySelector('[data-testid="behaviour/form/text-label/label"]');
          const control = document.querySelector('[data-testid="behaviour/form/text-label/control"]');
          return {
            focused: document.activeElement === control,
            labelFor: label.htmlFor,
            controlId: control.id
          };
        })()`);
        assert.equal(result.focused, true);
        assert.ok(result.controlId);
        assert.equal(result.labelFor, result.controlId);
      });

      test("FRM-004", "committed user text reports one widget value event", async () => {
        await replaceText("behaviour/form/text-value", "updated");
        await waitFor(
          `document.querySelector('[data-testid="behaviour/form/text-value"]')
            .dataset.changeCount === '1'`,
          "the text value-change event",
        );
        const result = await evaluate(`(() => {
          const input = document.querySelector('[data-testid="behaviour/form/text-value"]');
          return {
            inputValue: input.value,
            reportedValue: input.dataset.value,
            changeCount: input.dataset.changeCount,
            sourceMatch: input.dataset.sourceMatch
          };
        })()`);
        assert.equal(result.inputValue, "updated");
        assert.equal(result.reportedValue, "updated");
        assert.equal(result.changeCount, "1");
        assert.equal(result.sourceMatch, "true");
      });

      test("FRM-005", "programmatic text assignment honours the fire-events flag", async () => {
        await tap("behaviour/form/values/set-silent");
        let result = await state("behaviour/form/values");
        assert.equal(result.value, "silent");
        assert.equal(result.changeCount, 0);

        await tap("behaviour/form/values/set-firing");
        result = await state("behaviour/form/values");
        assert.equal(result.value, "firing");
        assert.equal(result.changeCount, 1);
        assert.equal(await evaluate(`document.querySelector(
          '[data-testid="behaviour/form/values"]'
        ).dataset.sourceMatch`), "true");
      });

      test("FRM-006", "validation state message and ARIA state agree", async () => {
        await tap("behaviour/form/validation/action");
        await waitFor(
          `document.querySelector('[data-testid="behaviour/form/validation/message"]')
            .textContent.trim() === 'Required'`,
          "the validation message",
        );
        const result = await evaluate(`(() => {
          const group = document.querySelector('[data-testid="behaviour/form/validation"]');
          const control = document.querySelector('[data-testid="behaviour/form/validation/control"]');
          const message = document.querySelector('[data-testid="behaviour/form/validation/message"]');
          return {
            invalidClass: group.classList.contains('has-error') || control.classList.contains('is-invalid'),
            ariaInvalid: control.getAttribute('aria-invalid'),
            describedBy: (control.getAttribute('aria-describedby') || '').split(/\\s+/).filter(Boolean),
            messageId: message.id,
            messageVisible: message.getClientRects().length > 0,
            valid: group.dataset.validationResult
          };
        })()`);
        assert.equal(result.invalidClass, true);
        assert.equal(result.ariaInvalid, "true");
        assert.ok(result.messageId);
        assert.ok(result.describedBy.includes(result.messageId), JSON.stringify(result));
        assert.equal(result.messageVisible, true);
        assert.equal(result.valid, "false");

        await tap("behaviour/form/validation/clear");
        const cleared = await evaluate(`(() => {
          const group = document.querySelector('[data-testid="behaviour/form/validation"]');
          const control = document.querySelector('[data-testid="behaviour/form/validation/control"]');
          const message = document.querySelector('[data-testid="behaviour/form/validation/message"]');
          return {
            invalidClass: group.classList.contains('has-error') || control.classList.contains('is-invalid'),
            ariaInvalid: control.getAttribute('aria-invalid'),
            describedBy: control.getAttribute('aria-describedby'),
            message: message.textContent.trim(),
            valid: group.dataset.validationResult
          };
        })()`);
        assert.equal(cleared.invalidClass, false);
        assert.equal(cleared.ariaInvalid, null);
        assert.equal(cleared.describedBy, null);
        assert.equal(cleared.message, "");
        assert.equal(cleared.valid, "true");
      });

      test("FRM-007", "list selection exposes one selected value", async () => {
        await evaluate(`(() => {
          const select = document.querySelector('[data-testid="behaviour/form/list-selection"]');
          select.selectedIndex = 1;
          select.dispatchEvent(new Event('change', {bubbles: true}));
        })()`);
        const result = await evaluate(`(() => {
          const select = document.querySelector('[data-testid="behaviour/form/list-selection"]');
          return {
            value: select.dataset.value,
            selected: Array.from(select.options).filter((option) => option.selected).length,
            changeCount: select.dataset.changeCount,
            sourceMatch: select.dataset.sourceMatch
          };
        })()`);
        assert.equal(result.value, "Germany");
        assert.equal(result.selected, 1);
        assert.equal(result.changeCount, "1");
        assert.equal(result.sourceMatch, "true");
      });

      test("FRM-008", "radio controls with one name remain exclusive", async () => {
        await tap("behaviour/form/radio-group/first");
        await tap("behaviour/form/radio-group/second");
        const first = await state("behaviour/form/radio-group/first");
        const second = await state("behaviour/form/radio-group/second");
        const group = await state("behaviour/form/radio-group");
        assert.equal(first.checked, false);
        assert.equal(second.checked, true);
        assert.equal(group.value, "second");
      });

      test("FRM-009", "cancelled form submission reports once without navigating", async () => {
        const before = await evaluate(`(() => {
          const form = document.querySelector('[data-testid="behaviour/form/submission"]');
          const frameName = form.dataset.frameName;
          return {
            url: window.location.href,
            frameName,
            frameAttached: Array.from(document.querySelectorAll('iframe'))
              .some((frame) => frame.name === frameName)
          };
        })()`);
        assert.ok(before.frameName);
        assert.equal(before.frameAttached, true);
        await tap("behaviour/form/submission/action");
        const after = await evaluate(`(() => {
          const form = document.querySelector('[data-testid="behaviour/form/submission"]');
          return {
            url: window.location.href,
            submitCount: form.dataset.submitCount,
            sourceMatch: form.dataset.sourceMatch,
            frameAttached: Array.from(document.querySelectorAll('iframe'))
              .some((frame) => frame.name === form.dataset.frameName)
          };
        })()`);
        assert.equal(after.url, before.url);
        assert.equal(after.submitCount, "1");
        assert.equal(after.sourceMatch, "true");
        assert.equal(after.frameAttached, true);
      });

      test("COL-001/002/003", "collapse reports one ordered transition each way", async () => {
      await evaluate(`(() => {
        window.__collapseInputEvents = [];
        const toggle = document.querySelector('[data-testid="behaviour/collapse/toggle"]');
        for (const event of ['touchstart', 'touchend', 'pointerdown', 'pointerup', 'click']) {
          toggle.addEventListener(event, () => window.__collapseInputEvents.push(event), true);
        }
      })()`);
      await tap("behaviour/collapse/toggle");
      await waitFor(
        "document.querySelector('[data-testid=\"behaviour/collapse/basic\"]').classList.contains('in') || document.querySelector('[data-testid=\"behaviour/collapse/basic\"]').classList.contains('show')",
        "the collapse show transition",
      );
      let collapse = await state("behaviour/collapse/basic");
      let toggle = await state("behaviour/collapse/toggle");
      assert.equal(collapse.events, "show,shown");
      assert.equal(toggle.ariaExpanded, "true");
      assert.equal(toggle.clickCount, 1);
      await new Promise((resolveWait) => setTimeout(resolveWait, 500));
      await tap("behaviour/collapse/toggle");
      try {
        await waitFor(
          "!document.querySelector('[data-testid=\"behaviour/collapse/basic\"]').classList.contains('in') && !document.querySelector('[data-testid=\"behaviour/collapse/basic\"]').classList.contains('show') && !document.querySelector('[data-testid=\"behaviour/collapse/basic\"]').classList.contains('collapsing')",
          "the collapse hide transition",
        );
      } catch (error) {
        const failedCollapse = await state("behaviour/collapse/basic");
        const failedToggle = await state("behaviour/collapse/toggle");
        const inputEvents = await evaluate("window.__collapseInputEvents");
        const toggleHtml = await evaluate("document.querySelector('[data-testid=\"behaviour/collapse/toggle\"]').outerHTML");
        throw new Error(`${error.message}\nCollapse: ${JSON.stringify(failedCollapse)}\nToggle: ${JSON.stringify(failedToggle)}\nInput events: ${JSON.stringify(inputEvents)}\nToggle HTML: ${toggleHtml}`);
      }
      collapse = await state("behaviour/collapse/basic");
      toggle = await state("behaviour/collapse/toggle");
      assert.equal(collapse.events, "show,shown,hide,hidden");
      assert.equal(toggle.ariaExpanded, "false");
      assert.equal(toggle.clickCount, 2);
    });

      test("COL-004", "accordion keeps only one panel open", async () => {
        await tap("behaviour/collapse/accordion/second-toggle");
        await waitFor(
          `document.querySelector('[data-testid="behaviour/collapse/accordion/second-panel"]')
            .classList.contains(${target.generation === 3 ? "'in'" : "'show'"})`,
          "the second accordion panel to open",
        );
        await waitFor(
          `(() => {
            const panel = document.querySelector(
              '[data-testid="behaviour/collapse/accordion/first-panel"]'
            );
            return !panel.classList.contains('in')
              && !panel.classList.contains('show')
              && !panel.classList.contains('collapsing');
          })()`,
          "the first accordion panel to close",
        );
        const result = await evaluate(`(() => {
          const fixture = document.querySelector(
            '[data-testid="behaviour/collapse/accordion"]'
          );
          const shown = (name) => {
            const panel = fixture.querySelector(
              '[data-testid="behaviour/collapse/accordion/' + name + '-panel"]'
            );
            return panel.classList.contains('in') || panel.classList.contains('show');
          };
          return {
            first: shown('first'),
            second: shown('second'),
            third: shown('third'),
            firstExpanded: fixture.querySelector(
              '[data-testid="behaviour/collapse/accordion/first-toggle"]'
            ).getAttribute('aria-expanded'),
            secondExpanded: fixture.querySelector(
              '[data-testid="behaviour/collapse/accordion/second-toggle"]'
            ).getAttribute('aria-expanded')
          };
        })()`);
        assert.equal(result.first, false);
        assert.equal(result.second, true);
        assert.equal(result.third, false);
        assert.equal(result.firstExpanded, "false");
        assert.equal(result.secondExpanded, "true");
      });

      test("COL-005", "detached collapse does not retain transition handlers", async () => {
        await tap("behaviour/collapse/detach/remount");
        assert.equal(await evaluate(`document.querySelector(
          '[data-testid="behaviour/collapse/detach"]'
        ).dataset.remounted`), "true");
        await tap("behaviour/collapse/detach/toggle");
        await waitFor(
          `document.querySelector('[data-testid="behaviour/collapse/detach/panel"]')
            .classList.contains(${target.generation === 3 ? "'in'" : "'show'"})`,
          "the remounted collapse to open",
        );
        const result = await state("behaviour/collapse/detach/panel");
        assert.equal(result.events, "show,shown");
        assert.equal(await evaluate(`document.querySelector(
          '[data-testid="behaviour/collapse/detach/panel"]'
        ).parentElement.dataset.testid`), "behaviour/collapse/detach/host");
      });

      test("LIF-001", "widget mounts with one framework and DOM parent", async () => {
        const result = await evaluate(`(() => {
          const state = document.querySelector('[data-testid="behaviour/lifecycle/basic"]');
          const host = state.querySelector('[data-testid="behaviour/lifecycle/basic/host"]');
          const widget = host.querySelector('[data-testid="behaviour/lifecycle/basic/widget"]');
          return {
            parentMatch: state.dataset.parentMatch,
            attached: state.dataset.attached,
            domParent: state.dataset.domParent,
            stableId: widget && widget.dataset.testid,
            hostChildren: host.children.length
          };
        })()`);
        assert.equal(result.parentMatch, "true");
        assert.equal(result.attached, "true");
        assert.equal(result.domParent, "true");
        assert.equal(result.stableId, "behaviour/lifecycle/basic/widget");
        assert.equal(result.hostChildren, 1);
      });

      test("LIF-002", "widget detaches cleanly", async () => {
        await tap("behaviour/lifecycle/basic/remove");
        const result = await evaluate(`(() => {
          const state = document.querySelector('[data-testid="behaviour/lifecycle/basic"]');
          const host = state.querySelector('[data-testid="behaviour/lifecycle/basic/host"]');
          return {
            parentMatch: state.dataset.parentMatch,
            attached: state.dataset.attached,
            domParent: state.dataset.domParent,
            mountedWidget: document.querySelector(
              '[data-testid="behaviour/lifecycle/basic/widget"]'
            ),
            hostChildren: host.children.length
          };
        })()`);
        assert.equal(result.parentMatch, "false");
        assert.equal(result.attached, "false");
        assert.equal(result.domParent, "false");
        assert.equal(result.mountedWidget, null);
        assert.equal(result.hostChildren, 0);
      });

      test("LIF-003", "widget remounts in a fresh host with one new attach", async () => {
        await tap("behaviour/lifecycle/remount/action");
        const result = await evaluate(`(() => {
          const state = document.querySelector('[data-testid="behaviour/lifecycle/remount"]');
          const oldHost = state.querySelector(
            '[data-testid="behaviour/lifecycle/remount/old-host"]'
          );
          const freshHost = state.querySelector(
            '[data-testid="behaviour/lifecycle/remount/fresh-host"]'
          );
          const widget = freshHost.querySelector(
            '[data-testid="behaviour/lifecycle/remount/widget"]'
          );
          return {
            parentMatch: state.dataset.parentMatch,
            newAttachCount: state.dataset.newAttachCount,
            oldHostChildren: oldHost.children.length,
            freshHostChildren: freshHost.children.length,
            widgetPresent: Boolean(widget)
          };
        })()`);
        assert.equal(result.parentMatch, "true");
        assert.equal(result.newAttachCount, "1");
        assert.equal(result.oldHostChildren, 0);
        assert.equal(result.freshHostChildren, 1);
        assert.equal(result.widgetPresent, true);
      });

      test("LIF-004", "repeated mounting preserves one action and current host", async () => {
        await evaluate(`(() => {
          window.__lifecycleHostClicks = {first: 0, second: 0};
          document.querySelector('[data-testid="behaviour/lifecycle/handlers/first-host"]')
            .addEventListener('click', () => window.__lifecycleHostClicks.first++);
          document.querySelector('[data-testid="behaviour/lifecycle/handlers/second-host"]')
            .addEventListener('click', () => window.__lifecycleHostClicks.second++);
        })()`);
        await tap("behaviour/lifecycle/handlers/remount");
        await tap("behaviour/lifecycle/handlers/widget");
        const result = await evaluate(`(() => {
          const state = document.querySelector('[data-testid="behaviour/lifecycle/handlers"]');
          const firstHost = state.querySelector(
            '[data-testid="behaviour/lifecycle/handlers/first-host"]'
          );
          const secondHost = state.querySelector(
            '[data-testid="behaviour/lifecycle/handlers/second-host"]'
          );
          const widget = state.querySelector('[data-testid="behaviour/lifecycle/handlers/widget"]');
          return {
            clickCount: widget.dataset.clickCount,
            currentHost: state.dataset.currentHost,
            firstHostChildren: firstHost.children.length,
            secondHostContainsWidget: secondHost.contains(widget),
            hostClicks: window.__lifecycleHostClicks
          };
        })()`);
        assert.equal(result.clickCount, "1");
        assert.equal(result.currentHost, "second");
        assert.equal(result.firstHostChildren, 0);
        assert.equal(result.secondHostContainsWidget, true);
        assert.deepEqual(result.hostClicks, {first: 0, second: 1});
      });

      test("LIF-005", "plugin markup is disposed and recreated once after remount", async () => {
        await tap("behaviour/lifecycle/plugin/show");
        await waitFor(
          "document.querySelectorAll('.tooltip.in, .tooltip.show').length === 1",
          "the lifecycle tooltip to open",
        );
        await tap("behaviour/lifecycle/plugin/detach");
        await waitFor(
          "document.querySelectorAll('.tooltip').length === 0",
          "the detached tooltip markup to be removed",
        );
        assert.equal(await evaluate(`document.querySelector(
          '[data-testid="behaviour/lifecycle/plugin/target"]'
        )`), null);
        await tap("behaviour/lifecycle/plugin/remount");
        await waitFor(
          "document.querySelectorAll('.tooltip.in, .tooltip.show').length === 1",
          "one replacement tooltip to open",
        );
        assert.equal(await evaluate("document.querySelectorAll('.tooltip').length"), 1);
      });

      test("ING-001", "text addons retain order around an editable input", async () => {
        await replaceText("behaviour/input-group/text-addons/control", "editable");
        const result = await evaluate(`(() => {
          const group = document.querySelector('[data-testid="behaviour/input-group/text-addons"]');
          return {
            children: Array.from(group.children).map((child) => child.dataset.testid),
            value: group.querySelector(
              '[data-testid="behaviour/input-group/text-addons/control"]'
            ).value
          };
        })()`);
        assert.deepEqual(result.children, [
          "behaviour/input-group/text-addons/prefix",
          "behaviour/input-group/text-addons/control",
          "behaviour/input-group/text-addons/suffix",
        ]);
        assert.equal(result.value, "editable");
      });

      test("ING-002", "button addon activates independently of its input", async () => {
        await tap("behaviour/input-group/button-addon/action");
        const result = await evaluate(`(() => {
          const action = document.querySelector(
            '[data-testid="behaviour/input-group/button-addon/action"]'
          );
          const addon = document.querySelector(
            '[data-testid="behaviour/input-group/button-addon/container"]'
          );
          const control = document.querySelector(
            '[data-testid="behaviour/input-group/button-addon/control"]'
          );
          return {
            clicks: action.dataset.clickCount,
            value: control.value,
            child: action.parentElement === addon
          };
        })()`);
        assert.equal(result.clicks, "1");
        assert.equal(result.value, "original");
        assert.equal(result.child, true);
      });

      test("ING-003", "input-group segments render as one contiguous control", async () => {
        const result = await evaluate(`(() => {
          const group = document.querySelector('[data-testid="behaviour/input-group/text-addons"]');
          const prefix = group.children[0].getBoundingClientRect();
          const control = group.children[1].getBoundingClientRect();
          const suffix = group.children[2].getBoundingClientRect();
          const groupRect = group.getBoundingClientRect();
          return {
            prefixGap: Math.abs(prefix.right - control.left),
            suffixGap: Math.abs(control.right - suffix.left),
            topSpread: Math.max(prefix.top, control.top, suffix.top)
              - Math.min(prefix.top, control.top, suffix.top),
            bottomSpread: Math.max(prefix.bottom, control.bottom, suffix.bottom)
              - Math.min(prefix.bottom, control.bottom, suffix.bottom),
            controlWidth: control.width,
            consumedWidth: prefix.width + control.width + suffix.width,
            groupWidth: groupRect.width
          };
        })()`);
        assert.ok(result.prefixGap <= 1, JSON.stringify(result));
        assert.ok(result.suffixGap <= 1, JSON.stringify(result));
        assert.ok(result.topSpread <= 1, JSON.stringify(result));
        assert.ok(result.bottomSpread <= 1, JSON.stringify(result));
        assert.ok(result.controlWidth > 0, JSON.stringify(result));
        assert.ok(Math.abs(result.consumedWidth - result.groupWidth) <= 2, JSON.stringify(result));
      });

      test("ING-004", "input-group size reports and removes one framework state", async () => {
        await tap("behaviour/input-group/sizing/large");
        let result = await evaluate(`(() => {
          const group = document.querySelector('[data-testid="behaviour/input-group/sizing"]');
          return {
            reported: group.dataset.reportedSize,
            largeClasses: Array.from(group.classList).filter(
              (name) => name === 'input-group-lg'
            ).length
          };
        })()`);
        assert.equal(result.reported, "LARGE");
        assert.equal(result.largeClasses, 1);
        await tap("behaviour/input-group/sizing/default");
        result = await evaluate(`(() => {
          const group = document.querySelector('[data-testid="behaviour/input-group/sizing"]');
          return {
            reported: group.dataset.reportedSize,
            large: group.classList.contains('input-group-lg')
          };
        })()`);
        assert.equal(result.reported, "DEFAULT");
        assert.equal(result.large, false);
      });

      test("SUG-001", "suggest box filters and positions matching suggestions", async () => {
        await enterSuggestQuery("behaviour/suggest-box/basic", "Uni");
        await waitFor(
          `Array.from(document.querySelectorAll('.dropdown-menu')).some((menu) =>
            menu.getClientRects().length > 0 && menu.querySelector('.item, .dropdown-item'))`,
          "the matching suggestion popup",
        );
        const result = await evaluate(`(() => {
          const input = document.querySelector('[data-testid="behaviour/suggest-box/basic"]');
          const popup = Array.from(document.querySelectorAll('.dropdown-menu')).find((menu) =>
            menu.getClientRects().length > 0 && menu.querySelector('.item, .dropdown-item'));
          const inputRect = input.getBoundingClientRect();
          const popupRect = popup.getBoundingClientRect();
          const popupStyle = getComputedStyle(popup);
          const items = Array.from(popup.querySelectorAll(
            ${target.generation === 5 ? "'.dropdown-item'" : "'.item'"}
          ))
            .map((item) => item.textContent.trim()).filter(Boolean);
          return {
            items,
            widthDifference: Math.abs(inputRect.width - popupRect.width),
            beneath: popupRect.top >= inputRect.bottom - 2,
            boxSizing: popupStyle.boxSizing,
            declaredWidth: popup.style.width,
            horizontalPadding: parseFloat(popupStyle.paddingLeft)
              + parseFloat(popupStyle.paddingRight),
            horizontalBorder: parseFloat(popupStyle.borderLeftWidth)
              + parseFloat(popupStyle.borderRightWidth),
            popupTag: popup.tagName,
            popupClass: popup.className,
            parentTag: popup.parentElement && popup.parentElement.tagName,
            parentClass: popup.parentElement && popup.parentElement.className,
            parentWidth: popup.parentElement && popup.parentElement.style.width,
            grandparentWidth: popup.parentElement && popup.parentElement.parentElement
              && popup.parentElement.parentElement.style.width
          };
        })()`);
        assert.deepEqual(result.items, ["United Kingdom", "United States"]);
        assert.ok(result.widthDifference <= 3, JSON.stringify(result));
        assert.equal(result.beneath, true);
      });

      test("SUG-002", "keyboard selection commits one oracle suggestion", async () => {
        await enterSuggestQuery("behaviour/suggest-box/basic", "Uni");
        await waitFor(
          `Array.from(document.querySelectorAll('.dropdown-menu')).some((menu) =>
            menu.getClientRects().length > 0 && menu.querySelector('.item, .dropdown-item'))`,
          "suggestions before keyboard selection",
        );
        await pressKey("Enter", "Enter", 13);
        await waitFor(
          `document.querySelector('[data-testid="behaviour/suggest-box/basic"]')
            .dataset.suggestionCount === '1'`,
          "one suggestion event",
        );
        const result = await evaluate(`(() => {
          const input = document.querySelector('[data-testid="behaviour/suggest-box/basic"]');
          return {
            value: input.value,
            selected: input.dataset.selectedValue,
            count: input.dataset.suggestionCount,
            sourceMatch: input.dataset.sourceMatch,
            visiblePopups: Array.from(document.querySelectorAll('.dropdown-menu')).filter((menu) =>
              menu.getClientRects().length > 0 && menu.querySelector('.item, .dropdown-item')
            ).length
          };
        })()`);
        assert.equal(result.value, "United Kingdom");
        assert.equal(result.selected, "United Kingdom");
        assert.equal(result.count, "1");
        assert.equal(result.sourceMatch, "true");
        assert.equal(result.visiblePopups, 0);
      });

      test("SUG-003", "query without matches closes the suggestion popup", async () => {
        await enterSuggestQuery("behaviour/suggest-box/basic", "Uni");
        await waitFor(
          `Array.from(document.querySelectorAll('.dropdown-menu')).some((menu) =>
            menu.getClientRects().length > 0 && menu.querySelector('.item, .dropdown-item'))`,
          "matching suggestions before the no-match query",
        );
        await enterSuggestQuery("behaviour/suggest-box/basic", "zzzz");
        await waitFor(
          `!Array.from(document.querySelectorAll('.dropdown-menu')).some((menu) =>
            menu.getClientRects().length > 0 && menu.querySelector('.item, .dropdown-item'))`,
          "the no-match query to close suggestions",
        );
        assert.equal(await evaluate(`document.querySelector(
          '[data-testid="behaviour/suggest-box/basic"]'
        ).dataset.suggestionCount`), "0");
      });

      test("SUG-004", "suggestion popup follows detach and remount lifecycle", async () => {
        await enterSuggestQuery("behaviour/suggest-box/lifecycle/control", "United K");
        await waitFor(
          `Array.from(document.querySelectorAll('.dropdown-menu')).some((menu) =>
            menu.getClientRects().length > 0 && menu.querySelector('.item, .dropdown-item'))`,
          "the lifecycle suggestion popup",
        );
        await tap("behaviour/suggest-box/lifecycle/detach");
        await waitFor(
          `!Array.from(document.querySelectorAll('.dropdown-menu')).some((menu) =>
            menu.getClientRects().length > 0 && menu.querySelector('.item, .dropdown-item'))`,
          "detaching the suggest box to remove its popup",
        );
        await tap("behaviour/suggest-box/lifecycle/remount");
        await enterSuggestQuery("behaviour/suggest-box/lifecycle/control", "Uni");
        await waitFor(
          `Array.from(document.querySelectorAll('.dropdown-menu')).filter((menu) =>
            menu.getClientRects().length > 0 && menu.querySelector('.item, .dropdown-item')
          ).length === 1`,
          "one suggestion popup after remount",
        );
        await pressKey("Enter", "Enter", 13);
        await waitFor(
          `document.querySelector('[data-testid="behaviour/suggest-box/lifecycle/control"]')
            .dataset.suggestionCount === '1'`,
          "one suggestion event after remount",
        );
      });

      test("RES-003", "compiled fixture loads its Bootstrap scripts and styles", async () => {
        const result = await evaluate(`(() => ({
          bootstrapRuntime: ${target.generation === 3
            ? "Boolean(window.jQuery && window.jQuery.fn && window.jQuery.fn.modal)"
            : "Boolean(window.bootstrap && window.bootstrap.Modal)"},
          themeLinks: document.querySelectorAll('#${target.generation === 3
            ? "gwtbootstrap3-theme"
            : "bootstrap5-theme"}').length,
          themeLoaded: Array.from(document.styleSheets).some((sheet) =>
            sheet.ownerNode && sheet.ownerNode.id === '${target.generation === 3
              ? "gwtbootstrap3-theme"
              : "bootstrap5-theme"}')
        }))()`);
        assert.equal(result.bootstrapRuntime, true);
        assert.equal(result.themeLinks, 1);
        assert.equal(result.themeLoaded, true);
      });

      test("THM-001", "standard theme replaces the previous stylesheet", async () => {
        const linkId = target.generation === 3 ? "gwtbootstrap3-theme" : "bootstrap5-theme";
        await tap("behaviour/themes/switcher/flatly");
        await waitFor(
          `document.getElementById('${linkId}').href.includes('bootswatch-flatly')`,
          "the Flatly theme stylesheet",
        );
        // The href changes before the stylesheet loads and moves the next touch target.
        await waitFor(
          `(() => { const link = document.getElementById('${linkId}');
            return link.sheet && link.sheet.href === link.href; })()`,
          "the Flatly stylesheet to finish applying",
        );
        const previousUrl = await evaluate(`document.getElementById('${linkId}').href`);
        assert.match(previousUrl, /bootswatch-flatly/);

        await tap("behaviour/themes/switcher/standard");
        const standardVersion = target.generation === 3 ? "bootstrap-3.4.1" : "bootstrap-5.3.8";
        await waitFor(
          `document.getElementById('${linkId}').href.includes(${JSON.stringify(standardVersion)})`,
          "the standard Bootstrap theme stylesheet",
        );
        const result = await evaluate(`(() => {
          const links = Array.from(document.querySelectorAll('#${linkId}'));
          return {
            count: links.length,
            href: links[0] && links[0].href,
            previousPresent: Array.from(document.querySelectorAll('link[rel="stylesheet"]'))
              .some((link) => link.href === ${JSON.stringify(previousUrl)})
          };
        })()`);
        assert.equal(result.count, 1);
        assert.match(result.href, new RegExp(`bootstrap-${target.generation === 3 ? "3\\.4\\.1" : "5\\.3\\.8"}`));
        assert.equal(result.previousPresent, false);
      });

      test("THM-002", "Bootswatch selection updates current theme metadata", async () => {
        await tap("behaviour/themes/switcher/flatly");
        const result = await evaluate(`(() => {
          const fixture = document.querySelector('[data-testid="behaviour/themes/switcher"]');
          const link = document.getElementById('${target.generation === 3
            ? "gwtbootstrap3-theme"
            : "bootstrap5-theme"}');
          return {
            current: fixture.dataset.currentTheme,
            recordedUrl: fixture.dataset.themeUrl,
            href: link && link.href
          };
        })()`);
        assert.equal(result.current, "flatly");
        assert.match(result.href, /bootswatch-flatly/);
        assert.equal(result.recordedUrl, result.href);
      });

      test("THM-003", "dark theme reporting follows the selected stylesheet", async () => {
        await tap("behaviour/themes/dark/darkly");
        let result = await evaluate(`(() => {
          const fixture = document.querySelector('[data-testid="behaviour/themes/dark"]');
          return {
            current: fixture.dataset.currentTheme,
            dark: fixture.dataset.dark,
            colorMode: document.documentElement.getAttribute('data-bs-theme')
          };
        })()`);
        assert.equal(result.current, "darkly");
        assert.equal(result.dark, "true");
        if (target.generation === 5) assert.equal(result.colorMode, "dark");

        await tap("behaviour/themes/dark/flatly");
        result = await evaluate(`(() => {
          const fixture = document.querySelector('[data-testid="behaviour/themes/dark"]');
          return {
            current: fixture.dataset.currentTheme,
            dark: fixture.dataset.dark,
            colorMode: document.documentElement.getAttribute('data-bs-theme')
          };
        })()`);
        assert.equal(result.current, "flatly");
        assert.equal(result.dark, "false");
        if (target.generation === 5) assert.equal(result.colorMode, "light");
      });

      test("THM-004", "theme selection survives an application reload", async () => {
        const linkId = target.generation === 3 ? "gwtbootstrap3-theme" : "bootstrap5-theme";
        await tap("behaviour/themes/persistence/united");
        await cdp.send("Page.reload", { ignoreCache: true });
        await waitFor(
          "document.body && document.body.dataset.fixturesReady === 'true' && window.__bootstrapWidgetFixturesReady === true",
          "the reloaded theme fixture",
        );
        const result = await evaluate(`(() => {
          const fixture = document.querySelector('[data-testid="behaviour/themes/persistence"]');
          const links = Array.from(document.querySelectorAll('#${linkId}'));
          return {
            current: fixture.dataset.currentTheme,
            count: links.length,
            href: links[0] && links[0].href
          };
        })()`);
        assert.equal(result.current, "united");
        assert.equal(result.count, 1);
        assert.match(result.href, /bootswatch-united/);
      });

      test("OVL-001", "modal opens with semantics and ordered events", async () => {
        await tap("behaviour/modal/basic/target");
        await waitFor(
          `document.querySelector('[data-testid="behaviour/modal/basic/dialog"]')
            .classList.contains(${target.generation === 3 ? "'in'" : "'show'"})`,
          "the basic modal to open",
        );
        const result = await evaluate(`(() => {
          const modal = document.querySelector('[data-testid="behaviour/modal/basic/dialog"]');
          const title = modal.querySelector('.modal-title');
          const labelledBy = modal.getAttribute('aria-labelledby');
          return {
            visible: getComputedStyle(modal).display !== 'none',
            role: modal.getAttribute('role'),
            labelledBy,
            titleId: title && title.id,
            titleVisible: Boolean(title && title.getClientRects().length),
            events: modal.dataset.eventOrder,
            backdrops: document.querySelectorAll('.modal-backdrop').length
          };
        })()`);
        assert.equal(result.visible, true);
        assert.equal(result.role, "dialog");
        assert.ok(result.titleId);
        assert.equal(result.labelledBy, result.titleId);
        assert.equal(result.titleVisible, true);
        assert.equal(result.events, "show,shown");
        assert.equal(result.backdrops, 1);
      });

      test("OVL-002", "modal dismiss restores target focus and cleans its backdrop", async () => {
        await tap("behaviour/modal/basic/target");
        await waitFor(
          `document.querySelector('[data-testid="behaviour/modal/basic/dialog"]')
            .classList.contains(${target.generation === 3 ? "'in'" : "'show'"})`,
          "the modal to open before dismissal",
        );
        await tap("behaviour/modal/basic/dismiss");
        await waitFor(
          `(() => {
            const modal = document.querySelector(
              '[data-testid="behaviour/modal/basic/dialog"]'
            );
            return !modal.classList.contains('in')
              && !modal.classList.contains('show')
              && document.querySelectorAll('.modal-backdrop').length === 0;
          })()`,
          "the modal and backdrop to close",
        );
        const result = await evaluate(`(() => {
          const modal = document.querySelector('[data-testid="behaviour/modal/basic/dialog"]');
          const target = document.querySelector('[data-testid="behaviour/modal/basic/target"]');
          return {
            visible: getComputedStyle(modal).display !== 'none',
            events: modal.dataset.eventOrder,
            focused: document.activeElement === target,
            bodyModalOpen: document.body.classList.contains('modal-open')
          };
        })()`);
        assert.equal(result.visible, false);
        assert.equal(result.events, "show,shown,hide,hidden");
        assert.equal(result.focused, true);
        assert.equal(result.bodyModalOpen, false);
      });

      test("OVL-003", "keyboard-enabled modal closes on Escape", async () => {
        await tap("behaviour/modal/keyboard/target");
        await waitFor(
          `document.querySelector('[data-testid="behaviour/modal/keyboard/dialog"]')
            .classList.contains(${target.generation === 3 ? "'in'" : "'show'"})`,
          "the keyboard modal to open",
        );
        await pressKey("Escape", "Escape", 27);
        await waitFor(
          `(() => {
            const modal = document.querySelector(
              '[data-testid="behaviour/modal/keyboard/dialog"]'
            );
            return !modal.classList.contains('in')
              && !modal.classList.contains('show')
              && document.querySelectorAll('.modal-backdrop').length === 0;
          })()`,
          "Escape to close the modal and remove its backdrop",
        );
      });

      test("OVL-004", "exclusive modal hides the previous modal", async () => {
        await tap("behaviour/modal/exclusive/open-first");
        await waitFor(
          `document.querySelector('[data-testid="behaviour/modal/exclusive/first"]')
            .classList.contains(${target.generation === 3 ? "'in'" : "'show'"})`,
          "the first exclusive modal to open",
        );
        await tap("behaviour/modal/exclusive/open-second");
        await waitFor(
          `(() => {
            const first = document.querySelector(
              '[data-testid="behaviour/modal/exclusive/first"]'
            );
            const second = document.querySelector(
              '[data-testid="behaviour/modal/exclusive/second"]'
            );
            return !first.classList.contains('in')
              && !first.classList.contains('show')
              && (second.classList.contains('in') || second.classList.contains('show'));
          })()`,
          "the exclusive modal transition",
        );
        assert.equal(await evaluate("document.querySelectorAll('.modal-backdrop').length"), 1);
      });

      test("OVL-005", "tooltip honours click trigger, bottom placement and delay", async () => {
        const started = Date.now();
        await tap("behaviour/tooltip/options");
        await waitFor(
          "document.querySelectorAll('.tooltip.in, .tooltip.show').length === 1",
          "the delayed tooltip to open",
        );
        await waitFor(
          `document.querySelector('[data-testid="behaviour/tooltip/options"]')
            .dataset.eventOrder === 'show,shown'`,
          "the delayed tooltip shown event",
        );
        const elapsed = Date.now() - started;
        const result = await evaluate(`(() => {
          const target = document.querySelector('[data-testid="behaviour/tooltip/options"]');
          const tooltip = document.querySelector('.tooltip.in, .tooltip.show');
          const targetRect = target.getBoundingClientRect();
          const tooltipRect = tooltip.getBoundingClientRect();
          return {
            content: tooltip.querySelector('.tooltip-inner').textContent.trim(),
            below: tooltipRect.top >= targetRect.bottom - 2,
            events: target.dataset.eventOrder,
            describedBy: target.getAttribute('aria-describedby'),
            tooltipId: tooltip.id
          };
        })()`);
        assert.ok(elapsed >= 80, `Tooltip appeared too early after ${elapsed}ms`);
        assert.equal(result.content, "Delayed bottom tooltip");
        assert.equal(result.below, true);
        assert.equal(result.events, "show,shown");
        assert.ok(result.tooltipId);
        assert.equal(result.describedBy, result.tooltipId);
      });

      test("OVL-006", "tooltip disposal removes generated markup and stale handlers", async () => {
        await tap("behaviour/tooltip/disposal/target");
        await waitFor(
          "document.querySelectorAll('.tooltip.in, .tooltip.show').length === 1",
          "the disposable tooltip to open",
        );
        await tap("behaviour/tooltip/disposal/destroy");
        await waitFor(
          "document.querySelectorAll('.tooltip').length === 0",
          "tooltip destruction to remove generated markup",
        );
        assert.equal(await evaluate(`document.querySelector(
          '[data-testid="behaviour/tooltip/disposal/target"]'
        )`), null);
        await tap("behaviour/tooltip/disposal/remount");
        await tap("behaviour/tooltip/disposal/target");
        await waitFor(
          "document.querySelectorAll('.tooltip.in, .tooltip.show').length === 1",
          "the remounted tooltip to open",
        );
        await waitFor(
          `document.querySelector('[data-testid="behaviour/tooltip/disposal/target"]')
            .dataset.eventOrder === 'show,shown,show,shown'`,
          "the remounted tooltip shown event",
        );
        const result = await state("behaviour/tooltip/disposal/target");
        assert.equal(result.events, "show,shown,show,shown");
        assert.equal(await evaluate("document.querySelectorAll('.tooltip').length"), 1);
      });

      test("OVL-007", "popover renders text title and trusted HTML content", async () => {
        await tap("behaviour/popover/html");
        await waitFor(
          "document.querySelectorAll('.popover.in, .popover.show').length === 1",
          "the HTML popover to open",
        );
        await waitFor(
          `document.querySelector('[data-testid="behaviour/popover/html"]')
            .dataset.eventOrder === 'show,shown'`,
          "the HTML popover shown event",
        );
        const result = await evaluate(`(() => {
          const target = document.querySelector('[data-testid="behaviour/popover/html"]');
          const popover = document.querySelector('.popover.in, .popover.show');
          const title = popover.querySelector('.popover-title, .popover-header');
          const body = popover.querySelector('.popover-content, .popover-body');
          return {
            title: title && title.textContent.trim(),
            body: body && body.textContent.trim(),
            trustedElement: Boolean(body && body.querySelector('strong')),
            events: target.dataset.eventOrder
          };
        })()`);
        assert.equal(result.title, "Trusted title");
        assert.equal(result.body, "Trusted content");
        assert.equal(result.trustedElement, true);
        assert.equal(result.events, "show,shown");
      });

      test("OVL-008", "popover supports show, toggle and hide through its API", async () => {
        await tap("behaviour/popover/programmatic/show");
        await waitFor(
          "document.querySelectorAll('.popover.in, .popover.show').length === 1",
          "the programmatic popover to show",
        );
        await tap("behaviour/popover/programmatic/toggle");
        await waitFor(
          "document.querySelectorAll('.popover.in, .popover.show').length === 0",
          "the programmatic popover to hide after toggle",
        );
        await tap("behaviour/popover/programmatic/show-hide");
        await waitFor(
          "document.querySelectorAll('.popover.in, .popover.show').length === 0",
          "the final programmatic popover state to be hidden",
        );
      });

      test("TAB-001", "touch activation selects one tab and its associated pane", async () => {
        await tap("behaviour/tabs/basic/second-tab");
        await waitFor(
          `document.querySelector('[data-testid="behaviour/tabs/basic/second-pane"]')
            .classList.contains('active')`,
          "the second basic tab pane to become active",
        );
        const result = await evaluate(`(() => {
          const fixture = document.querySelector('[data-testid="behaviour/tabs/basic"]');
          const tabs = Array.from(fixture.querySelectorAll('[role="tab"]'));
          const panes = Array.from(fixture.querySelectorAll('[role="tabpanel"], .tab-pane'));
          const active = (tab) => tab.classList.contains('active')
            || tab.parentElement.classList.contains('active');
          return {
            activeTabs: tabs.filter(active).map((tab) => tab.dataset.testid),
            activePanes: panes.filter((pane) => pane.classList.contains('active'))
              .map((pane) => pane.dataset.testid),
            visiblePanes: panes.filter((pane) => getComputedStyle(pane).display !== 'none')
              .map((pane) => pane.dataset.testid),
            firstSelected: tabs[0].getAttribute('aria-selected'),
            secondSelected: tabs[1].getAttribute('aria-selected')
          };
        })()`);
        assert.deepEqual(result.activeTabs, ["behaviour/tabs/basic/second-tab"]);
        assert.deepEqual(result.activePanes, ["behaviour/tabs/basic/second-pane"]);
        assert.deepEqual(result.visiblePanes, ["behaviour/tabs/basic/second-pane"]);
        assert.equal(result.firstSelected, "false");
        assert.equal(result.secondSelected, "true");
      });

      test("TAB-002", "disabled tab cannot change selection or emit transition events", async () => {
        await evaluate(`(() => {
          window.__tabEvents = [];
          const fixture = document.querySelector('[data-testid="behaviour/tabs/disabled"]');
          const tabs = fixture.querySelectorAll('[role="tab"]');
          const names = ['hide', 'show', 'hidden', 'shown'];
          for (const tab of tabs) {
            for (const name of names) {
              if (${target.generation} === 3) {
                window.jQuery(tab).on(name + '.bs.tab.fixture',
                  () => window.__tabEvents.push(name));
              } else {
                tab.addEventListener(name + '.bs.tab', () => window.__tabEvents.push(name));
              }
            }
          }
        })()`);
        // Bootstrap 5 removes the disabled link from pointer hit-testing. A real
        // touch therefore lands on its parent and must remain inert.
        await tap("behaviour/tabs/disabled/third-tab", target.generation === 5);
        const result = await evaluate(`(() => {
          const fixture = document.querySelector('[data-testid="behaviour/tabs/disabled"]');
          const first = fixture.querySelector('[data-testid="behaviour/tabs/disabled/first-tab"]');
          const firstPane = fixture.querySelector(
            '[data-testid="behaviour/tabs/disabled/first-pane"]'
          );
          return {
            firstActive: first.classList.contains('active')
              || first.parentElement.classList.contains('active'),
            paneActive: firstPane.classList.contains('active'),
            paneVisible: getComputedStyle(firstPane).display !== 'none',
            widgetEvents: fixture.dataset.eventOrder,
            nativeEvents: window.__tabEvents
          };
        })()`);
        assert.equal(result.firstActive, true);
        assert.equal(result.paneActive, true);
        assert.equal(result.paneVisible, true);
        assert.equal(result.widgetEvents, "");
        assert.deepEqual(result.nativeEvents, []);
      });

      test("TAB-003", "programmatic selection matches the native tab transition contract", async () => {
        await evaluate(`(() => {
          window.__tabEvents = [];
          const fixture = document.querySelector('[data-testid="behaviour/tabs/programmatic"]');
          const tabs = fixture.querySelectorAll('[role="tab"]');
          const names = ['hide', 'show', 'hidden', 'shown'];
          for (const tab of tabs) {
            for (const name of names) {
              if (${target.generation} === 3) {
                window.jQuery(tab).on(name + '.bs.tab.fixture',
                  () => window.__tabEvents.push(name));
              } else {
                tab.addEventListener(name + '.bs.tab', () => window.__tabEvents.push(name));
              }
            }
          }
        })()`);
        await tap("behaviour/tabs/programmatic/show-second");
        await waitFor(
          `document.querySelector('[data-testid="behaviour/tabs/programmatic/second-pane"]')
            .classList.contains('active')`,
          "the programmatically selected tab pane",
        );
        const result = await evaluate(`(() => {
          const fixture = document.querySelector('[data-testid="behaviour/tabs/programmatic"]');
          const second = fixture.querySelector(
            '[data-testid="behaviour/tabs/programmatic/second-tab"]'
          );
          return {
            active: second.classList.contains('active')
              || second.parentElement.classList.contains('active'),
            nativeEvents: window.__tabEvents,
            widgetEvents: fixture.dataset.eventOrder
          };
        })()`);
        assert.equal(result.active, true);
        assert.deepEqual(result.nativeEvents, ["hide", "show", "hidden", "shown"]);
        assert.equal(result.widgetEvents, "show,shown");
      });

      test("TAB-004", "fading panes finish with one visible active pane", async () => {
        await tap("behaviour/tabs/fade/second-tab");
        await waitFor(
          `(() => {
            const pane = document.querySelector('[data-testid="behaviour/tabs/fade/second-pane"]');
            return pane.classList.contains('active')
              && (pane.classList.contains('in') || pane.classList.contains('show'));
          })()`,
          "the second fading tab pane transition",
        );
        const result = await evaluate(`(() => {
          const first = document.querySelector('[data-testid="behaviour/tabs/fade/first-pane"]');
          const second = document.querySelector('[data-testid="behaviour/tabs/fade/second-pane"]');
          const visible = (pane) => pane.classList.contains('in') || pane.classList.contains('show');
          return {
            firstActive: first.classList.contains('active'),
            firstVisible: visible(first),
            secondActive: second.classList.contains('active'),
            secondVisible: visible(second),
            secondFades: second.classList.contains('fade')
          };
        })()`);
        assert.equal(result.firstActive, false);
        assert.equal(result.firstVisible, false);
        assert.equal(result.secondActive, true);
        assert.equal(result.secondVisible, true);
        assert.equal(result.secondFades, true);
      });

      test("TAB-005", "positioned tab sets retain local pane targets", async () => {
        for (const position of ["left", "right", "below"]) {
          await tap(`behaviour/tabs/positions/${position}/second-tab`);
          await waitFor(
            `document.querySelector('[data-testid="behaviour/tabs/positions/${position}/second-pane"]')
              .classList.contains('active')`,
            `the ${position} second tab pane`,
          );
        }
        const result = await evaluate(`(() => {
          const positions = ['left', 'right', 'below'];
          return positions.map((position) => {
            const fixture = document.querySelector(
              '[data-testid="behaviour/tabs/positions/' + position + '"]'
            );
            const tab = fixture.querySelector(
              '[data-testid="behaviour/tabs/positions/' + position + '/second-tab"]'
            );
            const pane = fixture.querySelector(
              '[data-testid="behaviour/tabs/positions/' + position + '/second-pane"]'
            );
            const target = tab.getAttribute('data-target')
              || tab.getAttribute('data-bs-target') || tab.getAttribute('href');
            return {
              position,
              paneActive: pane.classList.contains('active'),
              targetIsLocal: fixture.querySelector(target) === pane
            };
          });
        })()`);
        for (const item of result) {
          assert.equal(item.paneActive, true, JSON.stringify(item));
          assert.equal(item.targetIsLocal, true, JSON.stringify(item));
        }
      });

      test("BTN-007", "loading state starts and restores after one touch", async () => {
        await tap("behaviour/button/loading");
        await waitFor(
          "document.querySelector('[data-testid=\"behaviour/button/loading\"]').textContent.trim() === 'Saving...'",
          "the deferred loading state",
        );
        let result = await state("behaviour/button/loading");
        assert.equal(result.text, "Saving...");
        assert.equal(result.disabled, true);
        assert.equal(result.ariaBusy, "true");
        assert.equal(result.clickCount, 1);
        await waitFor(
          "document.querySelector('[data-testid=\"behaviour/button/loading\"]').textContent.trim() === 'Save'",
          "the loading state reset",
        );
        result = await state("behaviour/button/loading");
        assert.equal(result.text, "Save");
        assert.equal(result.disabled, false);
        assert.equal(result.ariaBusy, null);
        assert.equal(result.clickCount, 1);
      });

      test("BTN-008", "button types round-trip without framework-class fallthrough", async () => {
        const buttons = await evaluate(`Array.from(
          document.querySelector('[data-testid="behaviour/button/types"]').children
        ).map((button) => ({
          assigned: button.dataset.assignedType,
          reported: button.dataset.reportedType,
          expectedClass: button.dataset.expectedClass,
          typeClasses: Array.from(button.classList).filter((name) => name.startsWith('btn-'))
        }))`);
        assert.ok(buttons.length >= 7);
        for (const button of buttons) {
          assert.equal(button.reported, button.assigned, `${button.assigned} did not round-trip`);
          assert.deepEqual(button.typeClasses, [button.expectedClass],
            `${button.assigned} did not map exclusively to ${button.expectedClass}`);
        }
      });

      test("BTN-009", "button size replacement removes the previous size", async () => {
        let result = await state("behaviour/button/sizes");
        assert.equal(result.text, "Sized");
        assert.equal(await evaluate(`document.querySelector(
          '[data-testid="behaviour/button/sizes"]'
        ).dataset.reportedSize`), "LARGE");
        assert.equal(await evaluate(`document.querySelector(
          '[data-testid="behaviour/button/sizes"]'
        ).classList.contains('btn-lg')`), true);

        await tap("behaviour/button/sizes/change");
        assert.equal(await evaluate(`document.querySelector(
          '[data-testid="behaviour/button/sizes"]'
        ).dataset.reportedSize`), "SMALL");
        assert.equal(await evaluate(`document.querySelector(
          '[data-testid="behaviour/button/sizes"]'
        ).classList.contains('btn-sm')`), true);
        assert.equal(await evaluate(`document.querySelector(
          '[data-testid="behaviour/button/sizes"]'
        ).classList.contains('btn-lg')`), false);
      });

      test("BGR-001", "button group preserves rendered insertion order", async () => {
        const labels = await evaluate(`Array.from(document.querySelector(
          '[data-testid="behaviour/button-group/basic"]'
        ).children).map((button) => button.textContent.trim())`);
        assert.deepEqual(labels, ["First", "Second", "Third"]);
      });

      test("BGR-002", "group size changes without changing child values", async () => {
        const selector = '[data-testid="behaviour/button-group/sizes"]';
        const before = await evaluate(`(() => {
          const group = document.querySelector(${JSON.stringify(selector)});
          return {
            size: group.dataset.reportedSize,
            large: group.classList.contains('btn-group-lg'),
            values: Array.from(group.children).map((child) => child.dataset.value)
          };
        })()`);
        assert.equal(before.size, "LARGE");
        assert.equal(before.large, true);
        assert.deepEqual(before.values, ["true", "false", "true"]);

        await tap("behaviour/button-group/sizes/change");
        const after = await evaluate(`(() => {
          const group = document.querySelector(${JSON.stringify(selector)});
          return {
            size: group.dataset.reportedSize,
            large: group.classList.contains('btn-group-lg'),
            small: group.classList.contains('btn-group-sm'),
            values: Array.from(group.children).map((child) => child.dataset.value)
          };
        })()`);
        assert.equal(after.size, "SMALL");
        assert.equal(after.large, false);
        assert.equal(after.small, true);
        assert.deepEqual(after.values, ["true", "false", "true"]);
      });

      test("BGR-003", "vertical button group stacks each child", async () => {
        const result = await evaluate(`(() => {
          const group = document.querySelector('[data-testid="behaviour/button-group/vertical"]');
          const tops = Array.from(group.children).map((child) => child.getBoundingClientRect().top);
          return {
            vertical: group.classList.contains('btn-group-vertical'),
            ordered: tops.every((top, index) => index === 0 || top > tops[index - 1])
          };
        })()`);
        assert.equal(result.vertical, true);
        assert.equal(result.ordered, true);
      });

      test("BGR-004", "nested dropdown remains owned and leaves siblings actionable", async () => {
        await tap("behaviour/button-group/nested-dropdown/toggle");
        await waitFor(
          `getComputedStyle(document.querySelector(
            '[data-testid="behaviour/button-group/nested-dropdown/menu"]'
          )).display !== 'none'`,
          "the nested dropdown menu to open",
        );
        const composition = await evaluate(`(() => {
          const group = document.querySelector('[data-testid="behaviour/button-group/nested-dropdown"]');
          const menu = document.querySelector('[data-testid="behaviour/button-group/nested-dropdown/menu"]');
          return {
            ownsMenu: menu.parentElement === group,
            menuVisible: getComputedStyle(menu).display !== 'none'
          };
        })()`);
        assert.equal(composition.ownsMenu, true);
        assert.equal(composition.menuVisible, true);

        if (target.generation === 3) {
          // Bootstrap 3 inserts a mobile backdrop; the first touch dismisses it.
          await tap("behaviour/button-group/nested-dropdown/sibling", true);
          await new Promise((resolveWait) => setTimeout(resolveWait, 500));
        }
        await tap("behaviour/button-group/nested-dropdown/sibling");
        const sibling = await state("behaviour/button-group/nested-dropdown/sibling");
        assert.equal(sibling.clickCount, 1);
      });

      test("BGR-005", "removing a grouped button clears DOM and widget ownership", async () => {
        await tap("behaviour/button-group/removal/action");
        const result = await evaluate(`(() => {
          const group = document.querySelector('[data-testid="behaviour/button-group/removal"]');
          return {
            childCount: group.children.length,
            removedPresent: Boolean(document.querySelector(
              '[data-testid="behaviour/button-group/removal/middle"]'
            )),
            parentNull: group.dataset.removedParentNull
          };
        })()`);
        assert.equal(result.childCount, 2);
        assert.equal(result.removedPresent, false);
        assert.equal(result.parentNull, "true");
      });

      if (target.generation === 3) {
        test("SEL-001", "Bootstrap Select initializes in Bootstrap 3 mode", async () => {
          const result = await evaluate(`(() => {
            const select = document.querySelector(
              '[data-testid="behaviour/bootstrap-select/basic/control"]'
            );
            const plugin = select && select.closest('.bootstrap-select');
            const toggle = plugin && plugin.querySelector('.dropdown-toggle');
            return {
              pluginRegistered: Boolean(window.jQuery && window.jQuery.fn.selectpicker),
              bootstrapVersion: window.jQuery.fn.dropdown.Constructor.VERSION,
              containers: document.querySelectorAll(
                '[data-testid="behaviour/bootstrap-select/basic"] .bootstrap-select'
              ).length,
              bootstrap3Mode: Boolean(plugin && plugin.classList.contains('bs3')),
              defaultButton: Boolean(toggle && toggle.classList.contains('btn-default')),
              bootstrap3Toggle: toggle && toggle.getAttribute('data-toggle')
            };
          })()`);
          assert.equal(result.pluginRegistered, true, JSON.stringify(result));
          assert.match(result.bootstrapVersion, /^3\./);
          assert.equal(result.containers, 1);
          assert.equal(result.bootstrap3Mode, true);
          assert.equal(result.defaultButton, true);
          assert.equal(result.bootstrap3Toggle, "dropdown");
        });

        test("SEL-002", "Bootstrap Select opens from mobile touch", async () => {
          const root = '[data-testid="behaviour/bootstrap-select/basic"]';
          await tapSelector(`${root} .dropdown-toggle`, "Bootstrap Select toggle");
          await waitFor(
            `document.querySelector(${JSON.stringify(`${root} .bootstrap-select`)})
              .classList.contains('open')`,
            "the Bootstrap Select menu to open",
          );
          const result = await evaluate(`(() => {
            const root = document.querySelector(${JSON.stringify(root)});
            const plugin = root.querySelector('.bootstrap-select');
            const toggle = plugin.querySelector('.dropdown-toggle');
            const menu = plugin.querySelector('.dropdown-menu');
            return {
              open: plugin.classList.contains('open'),
              expanded: toggle.getAttribute('aria-expanded'),
              visible: menu.getClientRects().length > 0 && getComputedStyle(menu).display !== 'none'
            };
          })()`);
          assert.equal(result.open, true);
          assert.equal(result.expanded, "true");
          assert.equal(result.visible, true);
        });

        test("SEL-003", "Bootstrap Select reports one selected value from touch", async () => {
          const root = '[data-testid="behaviour/bootstrap-select/basic"]';
          await tapSelector(`${root} .dropdown-toggle`, "Bootstrap Select toggle");
          await waitFor(
            `document.querySelector(${JSON.stringify(`${root} .bootstrap-select`)})
              .classList.contains('open')`,
            "the Bootstrap Select menu to open",
          );
          await tapSelector(`${root} .dropdown-menu li:nth-child(2) a`,
            "Bootstrap Select second item");
          await waitFor(
            `document.querySelector(${JSON.stringify(root)}).dataset.changeCount === '1'`,
            "one Bootstrap Select value change",
          );
          const result = await evaluate(`(() => {
            const root = document.querySelector(${JSON.stringify(root)});
            const toggle = root.querySelector('.bootstrap-select .dropdown-toggle');
            return {
              value: root.dataset.value,
              changes: root.dataset.changeCount,
              sourceMatch: root.dataset.sourceMatch,
              text: toggle.textContent.trim()
            };
          })()`);
          assert.equal(result.value, "ketchup");
          assert.equal(result.changes, "1");
          assert.equal(result.sourceMatch, "true");
          assert.match(result.text, /Ketchup/);
        });
      }

      return tests;
    }

    let total = 0;
    let failures = 0;
    for (const fixtureTarget of fixtureTargets) {
      const fixtureUrl = `http://127.0.0.1:${appPort}${fixtureTarget.path}`;
      const tests = testsFor(fixtureTarget);
      const selectedTests = caseFilter ? tests.filter((test) => test.name.includes(caseFilter)) : tests;
      const failuresBeforeTarget = failures;
      for (const current of selectedTests) {
        total++;
        await freshPage(fixtureUrl, current.name);
        try {
          await current.body();
          if (diagnostics.length) throw new Error(diagnostics.join("\n"));
          console.log(`ok - ${current.name}`);
        } catch (error) {
          failures++;
          console.error(`not ok - ${current.name}`);
          console.error(error.stack || error);
          if (diagnostics.length) console.error(diagnostics.join("\n"));
        }
      }
      const targetFailures = failures - failuresBeforeTarget;
      console.log(`${selectedTests.length - targetFailures}/${selectedTests.length} compiled ${fixtureTarget.name} touch tests passed`);
    }
    console.log(`${total - failures}/${total} compiled GWT mobile touch tests passed`);
    if (failures) process.exitCode = 1;
  } finally {
    await stopBrowser(browser, cdp);
    cdp?.close();
    server.close();
    rmSync(profile, { recursive: true, force: true, maxRetries: 10, retryDelay: 200 });
    if (process.exitCode && browserStderr) {
      console.error(browserStderr);
    }
  }
}

main().catch((error) => {
  console.error(error.stack || error);
  process.exitCode = 1;
});
