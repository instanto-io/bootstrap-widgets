# Test Architecture

How the tests in this repository fit together, and what each layer is for.

The library is one set of widget sources compiled by two very different compilers.
Almost every testing decision here follows from that: the question is rarely "does
this widget work" but "does it work the same way on both backends".

The compiled showcase suite also visits each Bootstrap 5 section and checks that
only that section is constructed. Gallery is checked against the assembled static
site: TeaVM 0.16's test resource servlet serves SVG resources without
`image/svg+xml`, which prevents those images from decoding in the framed suite.
Do not treat passing navigation assertions as a substitute for checking the
gallery's three images on both GWT and TeaVM with a normal static server.

## The layers

| Layer | Where it runs | Tests | What it proves |
|---|---|---|---|
| GWT JVM contracts | JVM, against real `gwt-user` | 5 | what GWT actually does |
| TeaVM compat contracts | JVM, against the compat layer | 11 | the compat layer agrees |
| Browser-bound contracts | Chrome | 4 | the parts that need a DOM |
| TeaVM widget tests | Chrome, TeaVM-compiled | 17 | widgets behave once compiled |
| UiBinder processor tests | in-process `javac` | 12 | templates generate correct Java |
| Browser behaviour tests | Chrome, both backends | 72 scenarios | the two backends agree |

## 1. Contracts: pinning down what GWT does

The compat layer reimplements `com.google.gwt.*` on top of TeaVM. The risk is that
it drifts from the real thing in ways nobody notices until an application breaks.

`testing/bootstrap-widget-contracts` holds the contracts themselves — plain
assertions about GWT behaviour, written once:

- `JvmSafeGwtContracts` — behaviour that can be checked without a browser
- `BrowserBoundGwtContracts` — behaviour that needs a real DOM
- `ContractAssertions`, `ContractRuntime` — the shared plumbing

Two modules then run the same contracts against different implementations:

- `gwt/gwt-user-jvm-contract-tests` runs them against **real gwt-user**
- `teavm/teavm-gwt-compat-contracts` runs them against **our compat layer**

That is the differential: a contract that passes on gwt-user and fails on compat is
a divergence, and it is reported as one. Adding a contract is how you record a piece
of GWT behaviour you have come to depend on.

## 2. TeaVM widget tests

`teavm/teavm-bootstrap-widget-tests/{bootstrap3,bootstrap5}` are compiled **by
TeaVM into JavaScript** and run in Chrome through `teavm-junit`. They are not JVM
tests; the whole point is to exercise the compiled artefact.

- `*ControlBehaviourTest` — how form controls actually behave once compiled
- `*KnownSeamsTest` — that each seam's two halves stay in step
- `ValidationRegressionTest` and `ValidationLifecycleTest` in both generations —
  rule ordering, message arguments, callback-driven membership changes, stale
  subscriptions, nested validation and failure recovery. A rule-list change takes
  effect on the next validation pass; a group skips removed fields and validates
  newly added fields as part of adding them.
- Bootstrap 5 `PluginLifecycleTest` and `ModalLifecycleTest` — delayed readiness,
  detach during initialization, failure cleanup, retained editor/control values,
  obsolete callbacks, and modal transitions/body scroll restoration. These use
  the real packaged plugins as well as a deterministic delayed-loader probe.

- `ConstructorLifecycleTest` in both generations and Bootstrap 5
  `ExtrasConstructorLifecycleTest` — subclasses whose fields are unavailable
  during base construction, private initialization paths, form target ownership,
  and theme subscriptions across attachment/removal/remounting.
- GWT Bootstrap 3 `DatePickerValidationLifecycleFixture` — date and date-time pickers
  retain their popup-aware blur validation after moving customization out of
  constructor-time setup methods.

Validation mixin customization now belongs in `onValidationBlur()` and
`onValidationValueChange()`, which run when events arrive. Constructors bind the
handlers privately. The protected `setupBlurValidation()` and
`setupValueChangeValidation()` methods still register additional handlers when
called explicitly; overriding them no longer replaces the constructor's bindings.
Both owned Bootstrap 3 date-picker implementations use the new event-time hook.

Theme switchers subscribe only while attached. They synchronize the current theme
on attachment, including after theme changes made while detached.


`PreconditionsRegressionTest` in both TeaVM generations checks explicit wrapper
preconditions, validation dependencies, size bounds and constructor-safe size
initialization. The GWT Bootstrap 3 `OptionPreconditionsFixture` also checks
assertion-disabled behavior, calendar argument bounds, view-format mappings and
calendar subclasses whose fields are not initialized during base construction.
The fixture inherits `FullCalendarNoResources`: these option-object tests do not
need the calendar runtime. A separate check ensures this module injects no
calendar/jQuery UI plugins, which could otherwise replace Bootstrap's tooltip.

`AttachmentRegressionFixture` exercises the packaged Bootstrap 3 tags-input and
typeahead plugins: deferred setup after detach/reconfigure, exactly-once event
forwarding, preservation of unrelated listeners, and values across detach and
remount. Its pagination cases insert unrelated children and retain obsolete links
after rebuild/clear, checking both page meaning and callback ownership.

Tags-input constructors that accept an existing element now leave it unattached.
Use `TagsInput.wrap(element)` / `MVTagsInput.wrap(element)` for immediate attachment
after construction, or call `attachToDocument()` after constructing a subclass.
Normal panel attachment still starts the lifecycle. The explicit method requires
the element to be in the document and registers window-close cleanup.

Calendar format constructors now accept custom defaults through their map argument;
`getDefaultOptions()` / `populateDefaultOptions()` overrides no longer run during
construction. `AgendaOptions.newInstance()` remains callable after construction.
Size validators likewise initialize privately rather than invoking overridden
setters. Maximum size zero permits empty values; negative and null maxima are
rejected even when assertions are disabled, without changing the previous limit.

`ElementConstructorLifecycleTest` now guards subclass element access during
initialization across both generations, verifies explicit button elements, and
checks Bootstrap 5 choice-button events across repeated attachment cycles. The
compiled GWT `CoreConstructorFixture` also covers button factories and all three
data-grid initialization paths. `ExtrasConstructorFixture` covers the legacy
calendar, gallery, gallery image and both markdown editor modes, including a
second gallery instance and markdown input callbacks after detach/remount.

Button subclasses that previously overrode `createElement()` must pass their
custom element to the new protected constructor instead. The old method remains
deprecated for source compatibility but is no longer a constructor hook. Initial
styles, text and owned children are initialized without calling subclass element,
style or insertion hooks; ordinary runtime setters remain available. These tests
guard the reported paths, not every possible override in the underlying GWT APIs.

`CalendarLoaderFixture` exercises the packaged FullCalendar resources: loading
must preserve Bootstrap tooltip/button and datepicker plugin identities, preserve
Moment and live drag/resize instances across repeated calls, inject styles once,
and leave actual calendar rendering and drag/resize initialization working. It
uses the minimal pinned jQuery UI interaction bundle described in
`THIRD-PARTY-ASSETS.md`.

These are hand-written. A previous generated "reachability" suite was removed: it
asserted that a widget survives dead-code elimination, which is a tautology, since
the widget survived only because the test referenced it.

## 3. Behaviour specifications

The largest and most useful layer. It exists because "the two backends agree" is a
claim about observable behaviour in a browser, not about Java.

```
features/*.feature          13 files, 72 scenarios   what should be true
   |
   |  (by hand — nothing executes the .feature files)
   v
run-browser-behaviour-tests.mjs         62 implemented, by spec id
   |
   v
/fixtures/gwt-bootstrap3/index.html     54 fixtures
/fixtures/gwt-bootstrap5/index.html     53 fixtures
```

**The specs** are Gherkin, one file per widget area. Every scenario has an id
(`SEL-001`, `BGR-003`) which is how the harness finds its implementation.

**The fixtures** are hand-written GWT applications — `gwt-bootstrap3-browser-fixtures`
and `gwt-bootstrap5-browser-fixtures` — that mount one small arrangement of widgets
per fixture id, tagged with `data-testid`. They are compiled by GWT and served as
part of the site.

**The harness** (`run-browser-behaviour-tests.mjs`) serves the assembled site,
launches Chrome, and runs the assertions for each spec id against each target.

**The gap.** The specs are documentation, not executable: nothing reads a `.feature`
file, and the harness mirrors scenario ids by convention. Ten of the 72 scenarios
have no implementation — `BTN-001/002`, `COL-001/002/003`, `DRP-001/002`,
`RES-001/002/004`. A check worth having would compare the two sets; a previous
script instead validated tag spelling on every build and never noticed.

## 4. Structural guards

Checks of the repository's shape rather than of behaviour. Both run in every
Maven build:

- **Source seams** — each TeaVM module's `verify-*-source-seams` enforcer execution
  requires every GWT file its compiler excludes to exist, so an exclusion cannot
  outlive the file it was written for, and requires the TeaVM halves of the seams
  that remain. Most seams are gone: the TeaVM builds compile the GWT sources
  themselves, through teavm-compat's `adapt-sources`.
- **`ElementPanelInterfacesTest`** — `ElementPanel` declares `HasWidgets`
  immediately before `HasHTML`, which is how UiBinder knows its children are
  widgets.

## Markdown editor parity

`MarkdownEditorParityTest` runs in Chromium for both TeaVM generations, while
`MARKDOWN-PARITY` in the compiled GWT browser fixtures covers the same behaviour
on GWT3 and GWT5: Write/Preview controls, retained content, disabled controls,
pressed state and DOMPurify 3.4.17 sanitization. The Bootstrap 3 cases also exercise
Rich Text/Markdown switching, formatting selection state, and three detach/remount
cycles to check listener cleanup and reattachment. Bootstrap 5 remains a Markdown
source editor and therefore has no rich-text selection contract.

## FullCalendar lifecycle

`FullCalendarLifecycleTest` checks timed and all-day events, ISO date callbacks,
three detach/remount cycles without duplicate callbacks, and event removal and
clearing in the TeaVM Bootstrap 5 browser runtime. The shared widget is also
compiled and exercised in the native GWT Bootstrap 5 showcase.

## Where things run

| | local `mvn install` | CI |
|---|---|---|
| Contracts, widget tests, processor tests | yes | yes |
| Browser behaviour tests | no | yes |
| Structural guards | yes | yes |
| GWT compilation of the showcases | **no** | yes |

Two of these are worth knowing about:

- The GWT compiler does **not** run during `mvn install`. The showcase modules are
  packaged as `war`, so `gwt:compile` only runs when invoked explicitly, as CI does.
  A green local build does not mean the GWT compile succeeds.
- The behaviour tests need the assembled site and Chrome, so they are a CI step
  rather than part of the reactor.

## Scripts

| Script | Lines | Runs |
|---|---|---|
| `run-browser-behaviour-tests.mjs` | 2093 | CI |
| `smoke-showcase-pages.sh` | 78 | CI |

No script gates a local build, and nothing in the build or CI needs a python
interpreter. Both run in CI only. `build-tools/SourceMaps.java` is not a CI script: the
JDK runs it as part of packaging each showcase application, and it makes their source
maps usable from the published site and fails when a mapped source is missing.

## Static analysis

`mvn verify` runs SpotBugs at maximum effort and retains low-priority findings in
XML, HTML and SARIF reports under each module's `target/spotbugs`. The build fails
on high-priority findings. `spotbugs-exclude.xml` lists the public GWT adapter names
that deliberately match their superclass names; it does not exclude whole packages
or mutable widget APIs. The `.mvn` root marker makes the filter path work when a
module is built with `mvn -f ...` as well as from the reactor root.

The `widget-processor` artifact delegates to the owned `gwt-uibinder-processor`
dependency. Its compiler tests remain here to check compatibility for widget consumers;
processor implementation changes and their unit tests belong in `teavm-compat`.
