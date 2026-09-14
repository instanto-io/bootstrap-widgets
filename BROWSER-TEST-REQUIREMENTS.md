# Browser Test Requirements

What we need in order to write the browser behaviour tests in Java.

## What we want

Test code written in Java, compiled by TeaVM, running in Chrome under
`TeaVMTestRunner`. The application under test — a compiled GWT showcase or a
compiled TeaVM showcase — is loaded in a same-origin iframe, and the test drives and
inspects it through frame-scoped `mockatcha-dom`.

The same tests must work against both compilers. A compiled showcase is just widgets
on a DOM; nothing about the test should care which compiler produced it. That is the
whole point — the tests exist to prove the two produce the same behaviour.

```
TeaVMTestRunner page  (Java, compiled to JS)
`-- same-origin iframe
    |-- GWT Bootstrap 3 showcase
    |-- GWT Bootstrap 5 showcase
    |-- TeaVM Bootstrap 3 showcase
    `-- TeaVM Bootstrap 5 showcase
```

## Why we are asking

The tests currently live in a 2,104-line JavaScript file. Of that, 461 lines are
plumbing written by hand before the first test: a Chrome DevTools Protocol client
over a raw WebSocket, a static file server, Chrome binary discovery across six
candidate paths, port reservation and process lifecycle handling. The remaining
1,600 lines are tests written as DOM code inside JavaScript strings.

All of that reimplements, less well, what `mockatcha-dom` and `TeaVMTestRunner`
already do. We would like to delete it.

## What already works

- `TeaVMTestRunner` runs Java tests compiled to JavaScript in Chrome in this
  repository today (`teavm.junit.js.runner=browser-chrome`).
- `mockatcha-dom` provides `Dom`, `DomScope`, `Expect`, `DomTest` and `DomRule`,
  with `within(HTMLElement)` scoping, returning TeaVM `HTMLElement` values.
- `cucumber-tea` provides `@Specification`, `@Given`, `@When`, `@Then`,
  `@BeforeScenario`, `@AfterScenario`, `DataTable` and a codegen processor.
- The applications are already built and assembled into one directory tree.

## What is required

### 1. Serve a whole directory from the test page's origin

TeaVMTestRunner already serves classpath resources under `/resources/`. Direct
widget tests use `/resources/META-INF/bootstrap3-assets/` for the library JAR's
bundled assets. They do not need per-file `@ServeJS` or `@AttachJavaScript`
annotations: the module initializer loads the dependencies. The separate directory
hosting requirement below concerns compiled application outputs used by frame tests.

A compiled GWT application cannot be hosted file by file: its bootstrap script loads
generated permutations, stylesheets, images and other resources by relative path, so
the whole directory has to be reachable at a stable base URL on the same origin as
the test page.

The direct TeaVM widget tests use the same startup path as an application. They call
`Bootstrap3.initialise()` or `Bootstrap5.initialise()` once, then add fixtures through
the compatibility library's `RootPanel`. Initialisation installs the default Bootstrap
theme and the library's additional stylesheets. Bootstrap 5 also installs its bundled
runtime; Bootstrap 3 loads its vendored jQuery and Bootstrap scripts in order.
Use `Bootstrap3.initialise(ready)` to wait before constructing fixtures. The host
serves the bundled files; script attachment is performed by the module loader.

The test must not inject those resources itself. It must serve the complete packaged
asset tree at the base path configured for the library so the URLs selected by
initialisation resolve normally. With that in place, a direct test can inspect applied
styles as well as markup and behaviour. `RootPanel` matters independently: appending a
widget element by hand bypasses GWT attachment and therefore skips `onLoad()` and any
plugin setup performed there.

So: a way to say "serve this directory at this path". The directory is a build
output, so its location is not a compile-time constant — this probably wants Maven
or system-property configuration, or an annotation naming a logical application whose
root is configured in the build. Please do not require a hard-coded workstation
path.

#### Resource identity and version conflicts

A test application can contain several widget libraries, each with generated module
initialisation and transitive browser assets. Maven normally selects one version of a
given Java artifact, but differently named artifacts can still package incompatible
versions of the same logical browser library. Exact URL or DOM-id deduplication is not
enough: different URLs may install the same global API, and a generic presence check may
accept an incompatible version that another module loaded first.

Module initialisation therefore needs an explicit logical resource identity and expected
version. The browser harness must exercise these cases:

1. the same logical resource and version requested by two modules loads once and shares
   one readiness result;
2. different versions of the same global script or stylesheet fail with a diagnostic
   before the second version is installed;
3. unrelated resources from different modules coexist;
4. a host-supplied global is accepted only when its compatible version can be verified;
5. Bootstrap major versions are treated as conflicting global CSS unless an application
   deliberately isolates them in separate documents.

This is an application-composition contract, not a reason for Cucumber Tea, Mockatcha or
individual steps to choose assets. They continue to observe the modules' own
initialisation behaviour.

### 2. Open an application in a frame and know when it is ready

Something equivalent to:

```java
FramedApplication app = applications.open("gwt-bootstrap3", "#buttons");
DomScope page = app.awaitReady().page();
```

It needs to support:

- creating and removing the frame per scenario
- configurable route, query string, width and height
- load and readiness timeouts
- a readiness marker or predicate

The readiness point matters more than it sounds. A GWT application is **not** ready
when the iframe's `load` event fires — the bootstrap script then selects a
permutation, fetches it, and runs the entry point afterwards. Waiting on `load`
gives a blank page.

### 3. Scope every query to the frame

`mockatcha-dom` already has `within(HTMLElement)`. What we need confirmed, or added,
is that it works when the element belongs to the **child document** rather than the
test document. Every query in these tests is against the frame's contents.

### 4. Input synthesis, including touch

Tests must click, tap, type into and change controls inside the frame.

Touch is not optional. Several of the real defects found in this library were
touch-only: radio buttons that did not select, a toggle button that only repainted
after a scroll, a dropdown that would not open. A mouse-only harness would have
found none of them. It also needs to behave the same on Chrome under macOS and
Linux, because the existing harness needed specific work to stop touch tests being
flaky across the two.

### 5. Fail the test when the child does

Uncaught errors and unhandled promise rejections inside the frame must fail the
scenario that caused them. If a GWT module throws while bootstrapping, the test
should say so; at the moment that class of failure is indistinguishable from a
timeout.

### 6. Computed style and geometry

Some assertions are about applied CSS and layout rather than DOM structure, so we
need to read computed style and element geometry inside the frame.

`mockatcha-dom` already implements exact computed-style assertions. The missing
public geometry, comparison and frame-window semantics are specified in
[`MOCKATCHA-DOM-STYLE-LAYOUT-REQUIREMENTS.md`](MOCKATCHA-DOM-STYLE-LAYOUT-REQUIREMENTS.md).
The subject must load resources exactly as it does in normal use. GWT does that through
its module bootstrap. TeaVM tests call the library initialiser before adding fixtures
through `RootPanel`; the host serves the packaged assets that initialiser references.
Cucumber Tea and Mockatcha must not select or inject Bootstrap stylesheets on the
subject's behalf. An application using a deliberate no-theme module remains responsible
for choosing its theme in the usual way.

### 7. Tell us whether `.feature` files are executed

`cucumber-tea` has the step annotations and a codegen processor, so this may already
be answered — we just do not know which.

It matters. We have 13 feature files and 72 scenarios. Nothing currently executes
them: they are documentation, and the JavaScript harness mirrors their scenario ids
by convention. Ten scenarios have no implementation at all and nothing noticed —
`BTN-001`, `BTN-002`, `COL-001`, `COL-002`, `COL-003`, `DRP-001`, `DRP-002`,
`RES-001`, `RES-002`, `RES-004`.

If features are executed and bound to Java step definitions, that gap becomes a
visible failure and the specifications become worth maintaining. If they are not,
please say so plainly and we will treat them as documentation and stop implying
otherwise.

Our own target tags (`@gwt3`, `@teavm3`, `@gwt5`, `@teavm5`) record which compiler
targets a scenario covers. They are ours, and we are not asking for anything from
the framework to support them.

## What this repository will provide

- The applications, already assembled to `showcase-site/target/pages`: a GWT and a
  TeaVM showcase for each Bootstrap generation.
- Browser fixture pages with stable `data-testid` values — 54 for Bootstrap 3, 53
  for Bootstrap 5 — each mounting one small arrangement of widgets.
- The port of the 62 currently implemented tests from JavaScript to Java.
- Deletion of `scripts/run-browser-behaviour-tests.mjs` once the port lands.

## Constraints

- The test page and the application must share an origin.
- No `GWTTestCase`. The whole approach treats the compiled application as an opaque
  web page, which is what lets one test cover both compilers.
- Chrome is the browser, pinned in CI.

## Summary

We are not asking for a new testing framework. We are asking for the frame hosting
and frame-scoped driving described in the testing plan, so that browser tests can be
ordinary Java using `mockatcha-dom` instead of hand-written JavaScript talking to
Chrome over a socket.
