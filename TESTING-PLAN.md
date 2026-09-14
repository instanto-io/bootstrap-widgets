# GWT and TeaVM Widget Testing Plan

## Status

This document records the agreed direction for testing the Bootstrap 3 and
Bootstrap 5 widget tracks under both GWT and TeaVM.

The selected GWT compatibility reference strategy is ordinary JVM JUnit for
plain-Java `gwt-user` contracts and `GWTTestCase` only for contracts that need
GWT compilation. Cucumber Tea generates the latter once the browser-bound
corpus is large enough to justify generation. The first browser-bound
contracts may use small handwritten wrappers so useful tests do not wait for
cross-repository tooling.

A TeaVM-hosted runner that executes GWT test code inside a frame is
deliberately deferred. Frames test already rendered applications in Chromium;
they are the primary behavioural test path for GWT widgets and Bootstrap
JavaScript.

### Implementation status

The first executable tranche is now in the repository:

- The shared-source UiBinder annotation processor has twelve compiler-level
  contracts covering generated source compilation, typed construction,
  inherited setters, fields, handlers, service registration and negative
  diagnostics. Cross-compiler runtime fixtures remain open.
- Phase 0 core reachability is complete. Generated source contains a direct
  construction path and an independently reported TeaVM test for every core
  export in its inventory: 121 Bootstrap 3 fixtures and 131 Bootstrap 5
  fixtures. Extending the same enforcement across every optional extra and
  theme remains open.
- Phase 1 fixture/API enforcement is complete for the core, Markdown and first
  Bootstrap 3 optional-extra contracts. The reviewed corpus contains 64 P0 and
  8 P1 scenarios with stable
  IDs, fixture IDs, Bootstrap 3 showcase references, explicit subject,
  contract and execution classification, and a four-target matrix.
- Phase 2 has a substantial rendered Chromium tranche for both compiled GWT
  generations. One hundred and twenty-seven mobile-touch tests execute 69
  canonical scenarios covering buttons, button groups, dropdowns, forms,
  collapse/accordion, lifecycle, input groups, SuggestBox, overlays, tabs,
  required runtime assets and themes. Source-map integrity is enforced by the
  build. The same feature text is not yet Cucumber Tea executable.
- Phase 3 is partial. Dedicated Bootstrap 3 and Bootstrap 5 GWT fixture
  applications expose those canonical fixture IDs and readiness markers;
  equivalent TeaVM fixture hosts and integration into all four narrative
  showcases remain open.
- Phase 4 has a temporary dependency-free Chrome DevTools Protocol executor
  with real touch input, browser exception/log/network diagnostics and
  target/case filtering. The planned Cucumber Tea frame host and
  document-scoped Mockatcha API remain open.
- Phase 6 has five JVM-safe contracts against real `gwt-user`, four
  browser-bound contracts through `GWTTestCase`, and eleven contracts against
  `teavm-gwt-compat` in Chromium.
- Phase 9 has an initial deployment gate. CI assembles the four showcases with
  local vendored JavaScript, renders each in Chromium, and passes that exact
  artifact to the Pages workflow for the same successful run.

The current Maven matrix executes 301 tests: 12 processor contracts, 5 JVM
reference contracts, 4 GWT browser reference contracts, 11 TeaVM compatibility
contracts, 128 Bootstrap 3 TeaVM tests and 141 Bootstrap 5 TeaVM tests. CI
additionally executes 127 compiled-GWT mobile-touch tests. Phase 5, generated
Cucumber Tea glue, the remaining Phase 3/4/6 work, the expanded behaviour
matrix, pinned Chrome for Testing, structural snapshots and accessibility
gating remain open.

Current rendered-browser coverage follows the priorities below:

| Area | Executable against compiled GWT 3 and 5 | Highest-priority gaps |
| --- | --- | --- |
| Controls | All button, button-group, form, input-group and SuggestBox scenarios, including labels, focus, values, validation, selection and cancelled submission | Equivalent TeaVM execution and remaining P1 widget families |
| Bootstrap JavaScript | All dropdown, collapse/accordion, overlay and tab scenarios, including event order, disposal and detach/remount | Carousel and optional-extras behaviour |
| Lifecycle/resources | All lifecycle scenarios, required runtime assets, source maps and theme replacement/dark-state/persistence | Missing-provider execution and equivalent TeaVM fixture hosts |

## Decisions

1. Gherkin feature files describe shared widget behaviour.
2. Cucumber Tea generates the executable test cases.
3. GWT widget behaviour runs as a rendered application in the Chromium frame
   suite.
4. TeaVM Java contracts run through `TeaVMTestRunner` in Chromium.
5. `mockatcha-dom` drives widgets mounted by TeaVM tests and applications
   rendered inside same-origin frames.
6. Rendered-interface tests remain independent of the technology that produced
   the page.
7. `teavm-gwt-compat` receives a separate conformance suite that is also used
   by the Bootstrap widgets as a representative consumer.
8. Browser-bound real-`gwt-user` compatibility reference contracts run
   through `GWTTestCase`; generated wrappers are the target state, not a
   prerequisite for the first contracts.
9. Playwright is not required for the primary test suites. It remains an
   optional deployment-level tool if the frame host cannot cover a future
   browser or hosting requirement.
10. A plain `TeaVMTestRunner` reachability suite is delivered before Gherkin,
    frame hosting or generated GWT support.
11. GWT and TeaVM markup for the same Bootstrap generation is structurally
    diffed after normalization; deliberate differences are approved explicitly.
12. TeaVM classpaths mechanically reject `gwt-user` and `gwt-dev` rather than
    relying on developer discipline.
13. The Phase 0 reachability source contains concrete fixture construction and
    one generated test method per fixture. Reflection, `ServiceLoader`,
    classpath scanning and runtime name-to-class lookup are prohibited.
14. Compatibility contracts that run safely on the JVM execute directly
    against `gwt-user`; `GWTTestCase` is reserved for contracts that genuinely
    require GWT compilation or browser emulation.
15. Cross-compiler structural comparison and per-compiler markup regression
    snapshots are separate test families. Neither is allowed to stand in for
    the other.

## Goals

- Execute the same behavioural specifications against GWT and TeaVM widgets.
- Detect compile-time, Java API, event, lifecycle, DOM and Bootstrap JavaScript
  regressions.
- Exercise every exported widget and important optional code path under both
  compilers.
- Compare `teavm-gwt-compat` behaviour with real `gwt-user` behaviour.
- Test GWT, TeaVM and third-party rendered interfaces through one DOM-facing
  test API.
- Fail CI on browser exceptions, missing assets, incomplete service bindings
  and inaccessible markup.
- Produce a readable feature-to-target coverage report.
- Fail when an exported widget has no fixture or compiler reachability test.
- Detect unapproved structural differences between GWT and TeaVM markup for
  the same Bootstrap generation.
- Detect unintended markup changes that affect both compilers equally.
- Preserve the original upstream GwtBootstrap3 showcase behaviour as the
  initial normative functional specification, rather than deriving
  expectations from this fork or the Bootstrap 5 port.

## Non-goals

- Pixel-identical Bootstrap 3 and Bootstrap 5 output.
- Inspecting arbitrary cross-origin websites from a test frame.
- Reimplementing Playwright, Selenium or a general browser automation system.
- Making `mockatcha-dom` compile under GWT.
- Using `GWTTestCase` for ordinary rendered widget or Bootstrap plugin tests.

## Test Families

| Family | Executor | Subject | Assertions | GWTTestCase |
| --- | --- | --- | --- | --- |
| GWT rendered widget behaviour | Cucumber Tea frame suite | Compiled GWT showcase fixture pages | Frame-scoped `mockatcha-dom` | No |
| TeaVM widget reachability and behaviour | `TeaVMTestRunner`: plain JUnit in Phase 0, then Cucumber Tea | Every exported fixture compiled with `teavm-gwt-compat` | Construction, Java API, DOM, events and lifecycle | No |
| Framed rendered-interface tests | `TeaVMTestRunner` in Chromium | GWT, TeaVM or third-party application | Frame-scoped `mockatcha-dom` | No |
| Structural compiler differential | Chromium frame suite plus snapshot normalizer | GWT and TeaVM rendering of the same fixture | Normalized `outerHTML` | No |
| Per-compiler markup regression | Chromium frame suite plus committed snapshots | Each compiler target independently | Normalized `outerHTML` against its previous approved output | No |
| JVM compatibility reference | Ordinary JUnit on the JVM | JVM-safe `gwt-user` contracts | Safe HTML, event bus, values, scheduling and other plain-Java semantics | No |
| Browser-bound compatibility conformance | `GWTTestCase` reference and TeaVM compatibility runners | DOM/native `gwt-user` versus `teavm-gwt-compat` | Shared browser-bound Java contracts | Reference side only |
| Deployment smoke | Chromium frame host | Assembled Pages artifact | Startup, routes, assets and console | No |

## Proposed Modules

### Cucumber Tea repository

| Module or area | Responsibility |
| --- | --- |
| `cucumber-tea` | Portable annotations, hooks and scenario execution |
| `cucumber-tea-codegen` | Add generated `GWTTestCase` support while retaining the existing TeaVM target |
| `cucumber-tea-browser-testkit` | Same-origin application hosting, frame lifecycle and browser diagnostics |
| `cucumber-tea-examples` | Demonstrate TeaVM and generated GWT suites from the same feature |

The browser testkit name is provisional. It should be a separate module if it
would otherwise add DOM or server concerns to the small Cucumber Tea runtime.

### Mockatcha repository

`mockatcha-dom` remains TeaVM/browser based. It gains support for scoping
queries and interactions to a same-origin child document. No
`mockatcha-gwt-dom` module is planned initially.

### Bootstrap Widgets repository

The repository layout is part of the test isolation model:

| Path | Responsibility |
| --- | --- |
| `pom.xml` | Root `bootstrap-widgets` reactor and common build policy |
| `widget-processor` | Shared-source UiBinder generation and its compiler-level contract tests |
| `gwt/pom.xml` | GWT libraries, themes, extras, showcases and GWT-only tests |
| `teavm/pom.xml` | TeaVM compatibility runtime, TeaVM widget builds and TeaVM-only tests |
| `testing/pom.xml` | Future cross-target specifications, fixture metadata and assembled-site tests |

The root reactor includes `testing/` for the shared specifications, fixture
metadata and compatibility contracts.
Compiler-bound tests stay in their compiler sub-reactor; no test module may
bridge `gwt/` and `teavm/` by putting both runtime implementations on one
classpath.

| Proposed path | Responsibility |
| --- | --- |
| `testing/bootstrap-widget-specifications` | Shared `.feature` resources and coverage metadata |
| `testing/bootstrap-widget-fixtures` | Compiler-neutral fixture IDs, metadata and renderable-example contracts |
| `testing/bootstrap-widget-contracts` | Portable Java contracts used by compatibility runners |
| `gwt/gwt-user-jvm-contract-tests` | Fast ordinary-JUnit reference tests for contracts that need no GWT compile or browser |
| `gwt/gwt-bootstrap-widget-tests` | Browser-bound real-`gwt-user` compatibility reference tests only |
| `teavm/teavm-bootstrap-widget-tests` | Generated TeaVM tests and concrete fixture factories for Bootstrap 3 and Bootstrap 5 |
| `testing/bootstrap-showcase-browser-tests` | Framed tests against all compiled showcases and the assembled Pages site |
| `teavm/teavm-gwt-compat-contracts` | TeaVM execution of the shared GWT API conformance corpus |

These can begin as fewer integration-test modules and be split only when build
ordering or dependency isolation requires it. In particular, GWT and TeaVM
must never see both `gwt-user` and `teavm-gwt-compat` implementations of the
same `com.google.gwt.*` classes on one compiler classpath. The `teavm/`
sub-reactor enforces this by banning both the current `org.gwtproject` and
legacy `com.google.gwt` coordinates for `gwt-user` and `gwt-dev`.

The GWT showcases at `gwt/gwt-bootstrap3-showcase` and
`gwt/gwt-bootstrap5-showcase` compile with Thomas Broyer's
`net.ltgt.gwt.maven:gwt-maven-plugin`. TeaVM tests must not invoke that plugin
or inherit a GWT application lifecycle.

## Shared Feature Corpus

Feature files live in a small resource artifact so every runner reads the same
text. They describe behaviour rather than implementation classes or generated
markup.

Example:

```gherkin
Feature: Toggle buttons

  Scenario: activating a toggle button
    Given fixture "toggle-button/basic" is mounted
    When the user activates the button
    Then the button value is true
    And the button is visibly active
    And the button has aria-pressed "true"
    And one value change is reported
```

### Tag vocabulary

Tags describe independent axes. A subject tag says what is exercised, a
contract tag says what is normative, and an execution tag says what machinery
the assertion genuinely requires. Do not use an execution tag as a synonym
for a contract.

Subject tags:

| Tag | Meaning |
| --- | --- |
| `@widget` | Exercises a widget through the shared fixture/harness vocabulary; portable to another widget library |
| `@compat` | Exercises a `teavm-gwt-compat` implementation or its conformance with `gwt-user` |

Contract tags:

| Tag | Meaning |
| --- | --- |
| `@functional` | Verifies observable runtime behaviour through a mounted fixture, including interaction, event ordering and lifecycle effects |
| `@api-contract` | Verifies the public programming contract without mounting the widget, such as setters, getters, values or requested event delivery |
| `@dom-contract` | Makes element structure, ordering, attributes, ARIA, class names or declared inline styles normative |
| `@style-contract` | Makes stylesheet rules or resolved visual style normative |
| `@artifact-contract` | Makes a packaged or generated artifact normative, such as a script, stylesheet, source map or service descriptor |

Execution and concern tags:

| Tag | Meaning |
| --- | --- |
| `@browser` | Requires a real browser engine rather than a JVM, parser or synthetic DOM |
| `@javascript` | Requires native Bootstrap JavaScript/plugin behaviour and therefore also declares `@browser` |
| `@layout` | Requires computed style or geometry and therefore also declares `@browser` |
| `@accessibility` | Checks an accessibility concern such as roles, labels, focus or ARIA; it does not by itself require a browser |

Scope tags:

| Tag | Meaning |
| --- | --- |
| `@gwt3`, `@teavm3`, `@gwt5`, `@teavm5` | Compiler and Bootstrap-generation targets to which the scenario applies |
| `@unsupported-gwt3`, `@unsupported-teavm3`, `@unsupported-gwt5`, `@unsupported-teavm5` | Explicitly records that a target does not supply the scenario; every target must be required or unsupported |
| `@bootstrap3`, `@bootstrap5` | Generation-only scope for specifications that are independent of compiler |
| `@p0`, `@p1`, `@p2`, `@p3` | Execution priority, from release-critical core behaviour to optional extras |

The same feature may have compiler-specific glue where construction differs.
The assertion-bearing contract classes should remain shared wherever both
compilers expose the same API.

Every scenario declares at least one contract tag. `@api` and `@rendered` are
deliberately not part of this vocabulary: `@api` conflated API semantics with
access to a live widget, while `@rendered` overlapped both fixture setup and
browser execution. The inventory validator rejects both legacy tags and also
requires `@javascript` and `@layout` scenarios to declare `@browser`.

Markup does not imply a browser. A parser, a JVM DOM implementation or a
detached widget element can inspect element names, order, classes, attributes,
ARIA and inline styles under `@dom-contract`. Static inspection of selectors
and declarations in a CSS asset uses `@style-contract` without `@browser`.
Computed cascade, inherited values, pseudo-elements and viewport-dependent
styles add `@browser`; geometry also adds `@layout`. Examples:

| Inspection | Tags |
| --- | --- |
| Detached widget emits the expected classes and ARIA | `@widget @dom-contract` |
| CSS contains the required selector and declaration | `@style-contract` |
| Source map names source files from the source artifact | `@artifact-contract` |
| Browser resolves the expected colour or visibility | `@style-contract @browser` |
| Dropdown width equals its button width | `@widget @functional @dom-contract @style-contract @layout @browser` |

### Fixture identity

The fixture catalogue is the shared noun used by direct and framed scenarios.
Each fixture has a stable path and, where it is rendered, a stable
`data-testid`, for example `toggle-button/basic`. Setup wording records the
required fixture state:

```gherkin
Given fixture "toggle-button/basic" is constructed
Given fixture "toggle-button/basic" is mounted
Given fixture "resources/source-maps" is selected
```

`constructed` creates the subject without attaching it and is required for a
widget `@api-contract`. It also supports detached `@dom-contract` checks.
`mounted` attaches the widget to a render-capable harness; this can be a
synthetic DOM and does not itself imply `@browser`. `selected` identifies a
non-widget fixture or artifact without implying construction or rendering.

For a direct TeaVM test, the fixture catalogue performs the requested setup.
For a frame test, `mounted` navigates to or locates the same fixture in the
compiled showcase. Only examples selected for frame coverage must use
catalogue fixtures. Existing narrative showcase sections remain handwritten
and may host or reference those fixtures rather than being rewritten as
generated catalogue views.

The catalogue is not merely coverage bookkeeping. A static reference from the
catalogue makes TeaVM's reachability analysis compile and link the widget and
the optional paths exercised by that fixture. Missing native implementations,
stale service providers and invalid compatibility casts therefore become build
or browser failures instead of dormant code.

That static reference is mandatory and concrete. The generated fixture
registry contains direct `new` expressions (or direct calls to named factory
methods whose bodies contain those expressions). It must not discover fixture
classes through reflection, `ServiceLoader`, classpath scanning, annotations,
resource names or runtime class lookup. Those mechanisms may enumerate names
but do not make TeaVM reach the fixture constructors.

Generate one test method per fixture rather than one test that loops over the
registry. This preserves TeaVM reachability while giving every widget an
independent result; one constructor or linkage failure cannot mask all fixtures
that follow it. A registry loop may be used only for inventory consistency
checks, never as the executable reachability suite.

### Generated fixture source

Where a fixture applies to both compilers, its owner class and `.ui.xml`
template are shared source. The GWT build uses the normal GWT generator and the
TeaVM build uses the `widget-processor` annotation processor. A handwritten
Java copy of that shared layout is not a fixture implementation and must not
become one.

TeaVM may also own widgets, owners and UiBinder templates that have no GWT
counterpart. Those templates use the same annotation processor and participate
in TeaVM reachability, behaviour and markup-regression tests, but not in the
GWT-versus-TeaVM structural differential. GWT-only fixtures follow the
corresponding rule. Target applicability in the fixture catalogue determines
which generated harnesses include each fixture.

The compiler launchers remain thin and target-specific. Each generated test
method directly constructs its fixture owner, after which the generated binder
contains direct widget constructor calls. This preserves TeaVM reachability
without using reflection or runtime fixture discovery. Service descriptors may
select a generated binder, but they must not be used to discover the fixture
catalogue itself.

The processor has its own test boundary, independent of widget behaviour:

1. Compile representative owner classes and templates with the JDK compiler.
2. Compile the generated binder in the same task, rather than only comparing
   generated text.
3. Verify typed constructors, inherited setters, enum conversion, nested
   composition, `ui:field`, `@UiHandler` and binder service registration.
4. Verify failures identify unsupported attributes, event types, missing
   fields and malformed templates at compile time.
5. Exercise generated binders under both GWT and TeaVM in browser fixtures.

Focused generated-source assertions protect important reachability and wiring
statements. They are not full-file golden snapshots; runtime structural
snapshots remain responsible for detecting emitted markup changes.

To support the complete fixture corpus, the processor and its build integration
must provide:

- Reusable annotation-processor configuration for both TeaVM Bootstrap
  generations and downstream TeaVM applications, not only this repository's
  Bootstrap 5 module.
- Stable fixture attributes, including `data-testid`, ARIA and ordinary DOM
  attributes, without requiring a widget setter for every attribute.
- `@UiField(provided = true)`, `@UiFactory`, `@UiChild`, `IsWidget` and
  non-`HasWidgets` child composition so tests can supply collaborators and
  exercise real widget composition paths.
- `@UiTemplate`, inherited binder and inherited handler support so fixture
  owners are not constrained to one nested-interface convention.
- HTML-namespace elements, DOM `ui:field` references and mixed text/widget
  content for structural and accessibility fixtures.
- UiBinder expressions and resources needed by real templates, including
  owner fields, `ui:with`, messages, styles and safe HTML.
- Deterministic generated binder selection and direct generated fixture
  factories. Service descriptors may bind a known interface but may not be the
  only mechanism that makes a fixture reachable.
- Offline, external-entity-safe XML parsing and source-positioned diagnostics
  for malformed or unsupported templates.

### Authoritative widget inventory

Generate the expected fixture list independently from each module's exported
public API so Bootstrap 3 and Bootstrap 5 can evolve without being treated as
contract-equivalent. The build compares:

```text
exported widgets - registered fixtures = uncovered widgets
registered fixtures - exported widgets = stale fixtures
```

Run the check for Bootstrap 3, Bootstrap 5, extras and themes. A newly exported
widget without a fixture fails the build. The coverage report is a view of
this enforced inventory, not a manually maintained cross-version parity list.

## GWT Compatibility Reference Support

`GWTTestCase` is used only to establish reference behaviour for APIs supplied
by real `gwt-user`. It is not the primary GWT widget runner and does not test
Bootstrap JavaScript or showcase rendering. Those responsibilities belong to
the Chromium frame suite.

### JVM-safe reference contracts

First classify each shared compatibility contract by runtime requirement.
Contracts that do not touch the DOM, JSNI, deferred binding or browser globals
run as ordinary JUnit tests on the JVM with the real `gwt-user` jar. This is the
fastest and most reliable reference path and avoids HtmlUnit entirely.

The initial JVM tranche covers:

- Safe HTML escaping, concatenation and safe-URI semantics.
- `SimpleEventBus` and `HandlerManager` ordering, source identity, handler
  removal and mutation during dispatch.
- Value semantics, null handling, explicit event firing and event suppression
  where the implementation has no DOM dependency.
- Deferred/finally scheduling order and cancellation where the selected GWT
  implementation is JVM-runnable.
- Plain Java collections, range and selection models used by the compatibility
  layer.

Each contract is proved JVM-safe by execution, not assumed from its package.
If it reaches a native method, deferred binding or browser state, classify it
as browser-bound instead of adding a JVM shim.

### Browser-bound reference contracts

Use `GWTTestCase` only for contracts that need real GWT compilation: DOM and
specialized element factories, widget attachment, browser event dispatch,
focus, native/JSO behaviour, `GWT.create()`, resources and i18n. HtmlUnit is a
reference for the GWT API result, not a general modern browser. If the
observable behaviour depends on browser layout, current DOM event behaviour
or Bootstrap JavaScript, move it to the Chromium frame suite.

Begin the compatibility work with a small number of handwritten
`GWTTestCase` wrappers around shared contract methods. Add generated wrappers
when repetition makes generation valuable. This keeps the first useful test
off the critical path of a Cucumber Tea code-generator change.

### Processor target

The target state adds `gwt` to the accepted `cucumber.tea.runner` values. The
generated class uses JUnit 3 conventions required by GWT:

- Extend a Cucumber Tea GWT base derived from `GWTTestCase`.
- Generate public `test...()` methods instead of JUnit 4 `@Test` methods.
- Generate `getModuleName()` from suite configuration.
- Execute Cucumber Tea before hooks, steps and after hooks inside each test.
- Preserve undefined, ambiguous and duplicate-step failures at generation
  time.
- Keep TeaVM-only script annotations out of GWT output.

Proposed suite declaration:

```java
@CucumberSuite(
    value = "features/buttons.feature",
    gwtModule = "io.instanto.bootstrap5.tests.Bootstrap5Tests")
public final class Bootstrap5ButtonSteps {
    // Step definitions
}
```

Proposed generated shape:

```java
public final class Bootstrap5ButtonStepsCucumberTeaTest
        extends CucumberTeaGwtTestCase {

    @Override
    public String getModuleName() {
        return "io.instanto.bootstrap5.tests.Bootstrap5Tests";
    }

    public void testActivatingAToggleButton() throws Throwable {
        // Generated scenario orchestration
    }
}
```

### GWT module

Create a small GWT compatibility-reference module. It inherits the GWT test
module and includes the shared compatibility contract source. Entry points are
not assumed to run in `GWTTestCase`; contracts construct only the reference
objects they need.

### Execution

Use the existing `gwt-maven-plugin:test` goal with GWT's default HtmlUnit run
style only for the real-`gwt-user` compatibility reference. If a contract
depends on browser behaviour HtmlUnit does not implement reliably, move its
observable part to the Chromium frame suite rather than teaching the reference
suite about Bootstrap or modern browser plugins.

### Asynchronous behaviour

The GWT reference tranche remains synchronous. Later support can adapt
`delayTestFinish()` and `finishTest()` behind a Cucumber Tea asynchronous step
contract. Do not add polling or blocking emulation to ordinary generated
steps.

## Immediate TeaVM Reachability

Phase 0 is a plain JUnit suite executed by `TeaVMTestRunner`. It deliberately
has no dependency on Gherkin, Cucumber Tea code generation, frame hosting or
changes in another repository. Its purpose is to put every exported TeaVM
widget on the linker path immediately. It is the initial delivery form of the
direct TeaVM widget family, not a second permanent family.

Before adding fixtures, enforce the TeaVM classpath rule with Maven Enforcer.
The guard belongs here because a leaked `gwt-user` jar creates misleading
compatibility failures in the same work Phase 0 is intended to diagnose.

Generate one JUnit test method for every fixture registered in the catalogue.
Each generated method statically invokes a named registry factory containing a
direct construction expression, for example:

```java
@Test
public void toggleButtonBasic() {
    verify(Fixtures.toggleButtonBasic());
}

// In Fixtures: no reflection, service lookup or dynamic class loading.
public static Fixture toggleButtonBasic() {
    return new ToggleButtonBasicFixture();
}
```

Every independently reported test must:

1. Construct the widget and any required resource or service binding.
2. Mount it in a fresh document container.
3. Assert a fixture-specific DOM marker or minimal rendered contract.
4. Exercise any optional path declared by the fixture.
5. Detach it and assert that the container and parent relationship are clean.

The test fails on JavaScript exceptions, linkage failures, missing native
implementations, stale service descriptors and invalid compatibility casts.
This suite is intentionally small enough to land before the shared feature
corpus. Later phases reuse its fixtures and contract methods. As each assertion
is covered by the Cucumber Tea runner, remove the standalone orchestration so
the two forms do not become duplicate suites.

At minimum, Phase 0 must contain regression fixtures that would catch:

- A `META-INF/services` entry naming a class that no longer exists.
- `cast()`-based narrowing that cannot work against TeaVM's real Java element
  classes.
- A reached native or JavaScript seam with no TeaVM implementation.
- An enum value that renders an incorrect Bootstrap class through a default
  switch branch.

## Direct TeaVM Widget Tests

Cucumber Tea already generates JUnit 4 tests using `TeaVMTestRunner`. These
tests compile the reached application and test code to JavaScript and execute
it in Chromium. This is the mature form of the Phase 0 reachability suite, not
an additional layer of equivalent tests.

Direct widget scenarios must:

1. Create a fresh `mockatcha-dom` container before each scenario.
2. Construct the widget through a statically referenced fixture catalogue.
3. Mount it using the shared GWT-compatible widget API.
4. Drive the rendered control with `mockatcha-dom`.
5. Assert Java values, event counts and rendered state.
6. Detach the widget and reset the container after each scenario.

The fixture catalogue is important because TeaVM reachability analysis only
compiles code reached by a test. Every exported widget needs at least a
construction, mount and detach scenario, and optional paths need explicit
references. Examples include continuous range input, modal events, validators,
resource lookup and `GWT.create()` implementations.

Bootstrap JavaScript and styles should be attached through Cucumber Tea test
configuration. Add a `CucumberStyle` facility parallel to `CucumberScript` if
styles cannot be loaded cleanly through the browser testkit.

## Framed Rendered-interface Tests

### Purpose

The frame suite treats the subject as a web application. Its implementation
may be GWT, TeaVM, React or another renderer. Test code stays in the parent
TeaVM test document and communicates with the child through the DOM.

```text
TeaVMTestRunner page
`-- same-origin iframe
    |-- GWT Bootstrap 3 showcase
    |-- GWT Bootstrap 5 showcase
    |-- TeaVM Bootstrap 3 showcase
    |-- TeaVM Bootstrap 5 showcase
    `-- another locally packaged interface
```

### Static application hosting

The test host must expose complete compiled application directories from the
same origin as the TeaVM test page. A single-file `ServeJS` mechanism is not
enough for a GWT application because its bootstrap script loads generated
permutations and resources by relative path.

Add configuration equivalent to:

```java
@CucumberWebApplication(
    name = "gwt-bootstrap5",
    root = "${gwt.bootstrap5.showcase.output}",
    startPage = "index.html")
```

Maven configuration may be preferable for build-directory paths that are not
compile-time constants. The annotation or generated metadata should refer to a
logical application name, not hard-code a workstation path.

### Frame lifecycle

The browser testkit provides:

```java
FramedApplication app = applications.open("gwt-bootstrap5", "#buttons");
DomScope page = app.awaitReady().page();
```

It must support:

- Frame creation and removal per scenario.
- Configurable route, query string, width and height.
- Load and readiness timeouts.
- A standard readiness marker or predicate.
- Frame-scoped roles, labels, text, values and test IDs.
- Computed style and geometry queries.
- Child `error` and `unhandledrejection` capture.
- Useful DOM, URL and console diagnostics on failure.
- Failure when a required asset returns an error.

### Same-origin boundary

The browser prevents a parent test from inspecting a cross-origin frame.
Third-party rendered interfaces are testable when their built output is copied
under the local test host or when an approved same-origin test proxy is used.
Remote production websites are not made inspectable by CORS and are outside
this mechanism.

## Structural Compiler Differential

For each fixture supported by both compilers, render it under GWT and TeaVM
for the same Bootstrap generation and compare normalized `outerHTML`. This is
separate from behavioural scenarios: equal behaviour does not prove equal
composition, classes, attributes or child ordering.

Normalization may remove only values known to be volatile, such as generated
identifier suffixes and compiler bookkeeping attributes. It must not remove
element names, meaningful classes, ARIA attributes, Bootstrap state classes,
child order or visible content. Identifier values are normalized consistently
across their references so ID presence and `for`/ARIA topology remain part of
the comparison. The normalizer itself needs unit tests.

The default expectation is equality. A necessary compiler-specific difference
is stored as a reviewed snapshot beside the fixture with a short reason. A
missing snapshot, stale snapshot or newly divergent fixture fails the build.
Bootstrap 3 and Bootstrap 5 are not structurally diffed against each other.

This family specifically guards against compatibility-only `gwt-*` classes,
different generated-label wiring and silent mappings such as a light button
falling through to `btn-secondary`.

It does not guard against a shared-source markup regression. If one change
makes GWT and TeaVM emit the same new but incorrect markup, the differential
still passes because it compares the two compilers at one commit. Cross-
compiler snapshots record only approved differences; they are not historical
baselines.

Maintain a second committed normalized snapshot for each compiler target to
cover that case. A fixture therefore has two independent checks:

1. GWT output versus TeaVM output at the current commit, with approved
   compiler differences.
2. Each compiler's current output versus its own last approved snapshot.

Review and update per-compiler snapshots only when the markup change is
intentional. Bootstrap 3 and Bootstrap 5 retain separate baselines because
their native markup is expected to differ.

## Mockatcha DOM Changes

`mockatcha-dom` remains the browser-facing API for direct TeaVM and frame
tests. Add document-rooted scoping without changing its existing default
document behaviour.

The public computed-style and geometry contract, including tolerances,
cross-document handling and acceptance tests, is defined in
[`MOCKATCHA-DOM-STYLE-LAYOUT-REQUIREMENTS.md`](MOCKATCHA-DOM-STYLE-LAYOUT-REQUIREMENTS.md).

Candidate API:

```java
DomScope currentPage = Dom.page();
FrameScope frame = Dom.frame("#subject");
DomScope subjectPage = frame.awaitLoaded().page();
```

Required work:

1. Allow `DomScope` query operations to use either an element or document root.
2. Add safe access to a same-origin iframe document.
3. Add frame readiness and cleanup support.
4. Keep all interactions scoped so selectors cannot accidentally match the
   test runner page.
5. Add tests for two frames containing identical IDs.
6. Add tests for load failure, readiness timeout and child exceptions.
7. Produce a clear cross-origin error instead of leaking a browser exception.

`mockatcha-dom` is not used inside generated `GWTTestCase` code. GWT Java
contracts use GWT widget and DOM APIs directly. The rendered GWT application is
still driven by `mockatcha-dom` because the driver runs in the parent TeaVM
test page.

## TeaVM GWT Compatibility Contracts

### Purpose

`teavm-gwt-compat` currently exists primarily to compile GWT-oriented widget
source under TeaVM. Its behaviour should be checked against real GWT rather
than inferred from successful compilation.

### Contract arrangement

Write shared contract classes against `com.google.gwt.*` API names. Compile
and execute them through three isolated paths:

```text
Shared GWT API contract source
|-- JVM reference: gwt-user + ordinary JUnit (plain-Java contracts)
|-- GWT reference: gwt-user + generated GWTTestCase (browser-bound contracts)
`-- TeaVM implementation: teavm-gwt-compat + TeaVMTestRunner (both sets)
```

The shared contract artifact treats `gwt-user` as a compile-time/provided API.
The TeaVM runner must exclude it and supply `teavm-gwt-compat`. The GWT runner
must not include `teavm-gwt-compat`.

Classify contracts explicitly in source metadata as `JVM_SAFE` or
`BROWSER_BOUND`. The JVM reference suite rejects a browser-bound contract
rather than silently skipping it. The TeaVM suite runs both classifications in
Chromium, while the reference side chooses ordinary JUnit or `GWTTestCase` from
that classification.

Enforce this separation in every TeaVM module with Maven Enforcer
`bannedDependencies`. Ban both historical and current coordinates for
`gwt-user` and `gwt-dev`, including transitive dependencies:

```text
com.google.gwt:gwt-user
com.google.gwt:gwt-dev
org.gwtproject:gwt-user
org.gwtproject:gwt-dev
```

Also run duplicate-class detection for `com/google/gwt/**` where the build
layout permits it. A failure must print the dependency paths that introduced
the forbidden artifact. This turns compiler classpath isolation into a build
property rather than a convention.

### Initial compatibility areas

| Area | Representative contracts |
| --- | --- |
| Widget lifecycle | parent assignment, attach, detach, removal and handler order |
| Panels | add, insert, remove, clear, child ordering and indexed access |
| Values | `HasValue`, change suppression, explicit event firing and null handling |
| Events | bubbling, handler registration, handler removal and source identity |
| Controls | text, enabled, visible, focus, checkbox, radio and list selection |
| DOM | attributes, classes, properties, text, HTML, IDs and event dispatch |
| Scheduling | deferred commands, finally commands, timers and cancellation |
| Safe HTML | escaping, concatenation and safe URI handling |
| Deferred binding | successful registration and useful missing-binding failures |
| Data views | providers, ranges, sorting, selection and cell updates |
| History and storage | value changes, token behaviour and unavailable storage |
| JSO narrowing | `cast()` and `as()` behaviour where GWT uses a JavaScript-object no-op but TeaVM uses real Java types |
| Element factories | concrete return types from `getHead()`, input factories and other specialized element creation methods |
| Style names | primary style names, dependent style derivation, replacement and removal |
| Native seams | every JSNI, native or `@JsMethod` member reached by shared widget source has a TeaVM implementation |
| Resources and i18n | bundle lookup, generated resources, message substitution and missing-provider diagnostics |

Ordinary unit tests should cover low-level edge cases. Gherkin should cover
behavioural contracts that are useful to a library consumer; it should not be
used merely to restate every getter and setter.

### Bootstrap as a consumer test

The Bootstrap TeaVM suites provide a second level of compatibility evidence.
They exercise composition, event handling, resource lookup and DOM behaviour
through a substantial GWT-oriented library rather than isolated compatibility
classes.

## Bootstrap Behaviour Inventory

The first full inventory should cover:

| Group | Behaviour |
| --- | --- |
| Basic widgets | construction, text/HTML, style, visibility, enabled state |
| Buttons | types, sizes, groups, loading state, toggle and checkbox buttons |
| Forms | labels, values, validation, radio groups, input groups and submission |
| Navigation | links, breadcrumbs, pagination, navs, tabs and navbar collapse |
| Overlays | dropdown, dropup, modal, tooltip, popover and disposal |
| Dynamic widgets | collapse, accordion, carousel, progress and range input |
| Data widgets | suggest box, list box, cell list, cell table, tree and data grid |
| Layout | grid, responsive utilities, media objects, images and containers |
| Accessibility | names, roles, focus, keyboard control and ARIA state |
| Lifecycle | repeated mounting, route changes, detach and event unbinding |
| Resources | Bootstrap assets, icons, themes, service providers and source maps |
| Extras | rich text, slider, date picker and Markdown wrappers, including third-party script lifecycle |
| Themes | stylesheet replacement, active-theme reporting and dark-mode metadata |

Bootstrap 3 and Bootstrap 5 should share semantics where practical. Visual
assertions should allow the frameworks' native styling to differ. GWT and
TeaVM output for the same Bootstrap generation should be held to a closer
structural and behavioural standard.

Extras and themes are part of the enforced inventory rather than optional
manual demonstrations. Each vendored theme must have a fixture that applies
it, verifies that the existing theme link was replaced rather than appended,
and checks that the reported current theme and dark flag match the stylesheet
actually loaded.

## Functional Specification Baseline

The original upstream GwtBootstrap3 project and its showcase are the normative
behavioural source for the first corpus. This is the project from which the
Bootstrap 3 sources in this repository were forked, not the refreshed fork
itself. Its demo sections, actions, event logs and compositions are a
battle-hardened description of what an existing consumer expects. Bootstrap 5
and TeaVM scenarios inherit those functional expectations where applicable,
while their API and DOM contracts may deliberately differ. An exception is
recorded in the target matrix; it is not inferred from whatever the current
port happens to do.

Before implementing Cucumber Tea glue, extract and check in a showcase
inventory with one record per demonstrated behaviour:

```text
route + section -> fixture ID -> setup -> user action -> observable result
                -> event count/order -> DOM/ARIA contract -> target matrix
```

The inventory is reviewed against the Bootstrap 3 showcase Java and UiBinder
source and, where interaction matters, against the running showcase. The full
feature files are then written from that inventory at the outset. Scenarios may
temporarily be tagged as not implemented by a target, but their expected
behaviour must not be left as prose to be invented while writing step code.

Every interactive scenario specifies all relevant observables:

- Public Java value before and after the action.
- Visible state and Bootstrap state class or data attribute.
- ARIA state, focus and keyboard result where applicable.
- Exact event count, source and ordering.
- Disabled/read-only behaviour and repeated activation.
- Detach/disposal result and absence of stale handlers or document elements.
- Target applicability across GWT Bootstrap 3, TeaVM Bootstrap 3, GWT
  Bootstrap 5 and TeaVM Bootstrap 5.

### Specification priorities

Priority is based on consumer impact and on defects already found in the port,
not on ease of implementation.

| Priority | Feature files | Scenarios fixed before runner work begins |
| --- | --- | --- |
| P0 controls | `buttons.feature`, `button-groups.feature`, `forms.feature`, `bootstrap-select.feature` | Click/disabled handling; type and size mapping; loading text and restoration; toggle value, active class and `aria-pressed`; checkbox-button independent values; radio-button exclusivity; label activation; text/list/check/radio value changes; explicit versus suppressed events; validation state and submit behaviour; Bootstrap Select initialization, touch opening and value propagation in Bootstrap 3 mode |
| P0 JavaScript | `dropdowns.feature`, `collapse.feature`, `tabs.feature`, `overlays.feature` | Open/close from mouse and keyboard; outside-click and Escape; split buttons; disabled items; dropup direction; menu/button alignment; collapse events and ARIA; accordion exclusivity; tab pane selection and events; modal show/hide, backdrop, Escape, focus and lifecycle; tooltip/popover trigger, placement and disposal |
| P0 lifecycle | `widget-lifecycle.feature`, `resources.feature`, `themes.feature` | Construction, mount, attach, detach and remount; handler cleanup; missing service/resource diagnostics; source-map presence; standard and Bootswatch selection; one stylesheet replaced rather than accumulated; persistence, current-theme and dark-theme reporting |
| P1 navigation | `navigation.feature`, `pagination.feature`, `carousel.feature` | Anchor target/history behaviour; breadcrumb composition; nav/tab/dropdown nesting; active and disabled pages; previous/next boundaries; navbar collapse; carousel indicators, captions, wrap, interval, slide events and controls |
| P1 values/data | `input-groups.feature`, `suggest-box.feature`, `data-widgets.feature`, `progress-range.feature` | Text/icon/button addons; sizing and validation propagation; suggestion filtering, keyboard selection and oracle lifecycle; list/cell/table/tree selection, ranges and sorting; progress types, stripes, animation and stacking; range input/change events and bounds |
| P1 composition | `alerts.feature`, `list-groups.feature`, `panels.feature`, `media-thumbnails.feature` | Dismiss and close events; links and contextual state; badges; linked/custom list content; panel/card header/body/footer composition; wells/jumbotrons; media alignment; linked and custom-content thumbnails |
| P2 presentation | `grid-responsive.feature`, `tables.feature`, `typography.feature`, `icons-images.feature`, `accessibility.feature` | Grid breakpoints, offsets and visibility; responsive tables and CellTable; headings and inline text helpers; icon size/spin/stack; responsive and shaped images; names, roles, labels, focus order, keyboard control and ARIA topology |
| P3 extras | `datepicker.feature`, `datetime-picker.feature`, `select-tags-typeahead.feature`, `slider-switch.feature` | Value and format; language; position; auto-close; today; min/max or disabled dates; method calls and event order; select refresh; scalar/object tags and suggestions; category rendering; slider values; toggle-switch state and events |
| P3 rich/plugins | `markdown.feature`, `summernote.feature`, `dialogs-notifications.feature`, `calendar-gallery.feature`, `animate-offline-card.feature` | Markdown value/render lifecycle; editor code, clear, enable/disable, toolbar, hints and events; alert/confirm/prompt/custom dialog callbacks; notification lifecycle; calendar events/localization/background events; gallery controls; animation completion; offline transitions; card composition |

### Required first feature definitions

The first implemented slice is intentionally narrower than the complete corpus
but its specifications are not abbreviated. At minimum it contains these named
scenarios before Cucumber Tea integration starts:

```gherkin
Feature: Toggle and selection buttons

  Scenario: Toggle button activates once
  Scenario: Toggle button deactivates on its second activation
  Scenario: Programmatic value change can suppress its event
  Scenario: Disabled toggle button ignores user activation
  Scenario: Checkbox buttons retain independent selections
  Scenario: Radio buttons enforce one selection within their group
  Scenario: Loading state replaces and then restores button content

Feature: Dropdown direction and lifecycle

  Scenario: Dropdown opens and reports expanded state
  Scenario: Dropdown closes on outside activation and Escape
  Scenario: Disabled dropdown item cannot activate
  Scenario: Split dropdown keeps its primary action independent
  Scenario: Dropup menu is positioned above its toggle
  Scenario: A constrained dropdown menu aligns with its owning button

Feature: Form labels and values

  Scenario: Activating a checkbox label toggles its input once
  Scenario: Activating a radio label selects its input once
  Scenario: Activating a text field label focuses its input
  Scenario: User input reports one value change with the widget as source
  Scenario: Explicit value assignment reports only when requested
  Scenario: Validation state, message and ARIA state agree

Feature: Theme switching

  Scenario: Selecting a theme replaces the current stylesheet link
  Scenario: Selecting several themes never accumulates stylesheet links
  Scenario: The selected theme survives application reload
  Scenario: Current theme and dark flag describe the active stylesheet
```

These scenarios are derived from and first checked against the original
upstream GwtBootstrap3 showcase. The refreshed GWT Bootstrap 3 showcase then
becomes the first executable target before the same steps are enabled for
Bootstrap 5 or either TeaVM target. A target cannot be marked supported until
every applicable scenario passes; class inventory or successful construction
alone is not functional coverage.

## Build and CI

### Browser reproducibility

CI uses a pinned Chromium or Chrome for Testing revision for
`TeaVMTestRunner`, frame tests and structural snapshots. It must not silently
follow the browser preinstalled on the runner image. Browser upgrades are
explicit dependency changes: run the full matrix, inspect geometry and
snapshot changes, then update the pin deliberately.

### Pull requests

1. Enforce compiler classpath isolation and fixture-to-public-API coverage.
2. Build the root reactor without globally skipping tests; this traverses
   `gwt/`, `teavm/` and, once introduced, `testing/`.
3. Run the plain TeaVM reachability suite for both Bootstrap generations.
4. Run Cucumber Tea parser and existing code-generation unit tests.
5. Run JVM-safe `gwt-user` reference contracts, browser-bound GWT reference
   contracts and both corresponding TeaVM compatibility sets.
6. Run direct TeaVM core widget scenarios.
7. Build all four showcases and run the core frame matrix.
8. Run normalized GWT-versus-TeaVM structural comparisons.
9. Compare each compiler target with its committed markup-regression
   snapshots.

Accessibility checks begin as a published, non-blocking report so existing
defects can be baselined without making the suite unusable. Once the baseline
is clean, the pull-request build rejects new violations and the main build
requires a clean report.

### Main branch

1. Run the full framed matrix for Bootstrap 3 and Bootstrap 5 under both
   compilers.
2. Run Bootstrap JavaScript and accessibility scenarios.
3. Assemble the exact GitHub Pages artifact.
4. Run startup, route, asset and console checks against that artifact before
   deployment.
5. Publish scenario and target coverage as a workflow artifact.

### GitHub Pages publication

Pages must deploy the exact artifact produced by a successful verification
job for the same commit. Prefer one workflow that builds, tests and uploads
the artifact once, or pass the verified artifact to a separate deployment job.
Do not rebuild it independently with `-DskipTests`; that can publish output
which was never exercised. A separate Pages workflow is acceptable only when
it is triggered from successful CI for the same commit and consumes that
CI artifact.

### Scheduled full run

1. Exercise every widget fixture and optional path.
2. Run mobile, tablet and desktop frame sizes.
3. Record screenshots for selected stable pages.
4. Archive page HTML and browser diagnostics for failures.
5. Report untested widgets, contracts and targets.

## Delivery Sequence

### Phase 0: Reach every TeaVM widget now

1. Test `widget-processor` generation and diagnostics with compiler-level
   positive and negative fixtures.
2. Add Maven Enforcer bans for all historical and current `gwt-user` and
   `gwt-dev` coordinates to every module under `teavm/`.
3. Add in-repository Bootstrap 3 and Bootstrap 5 fixture registries whose
   factory bodies contain concrete `new` expressions under
   `teavm/teavm-bootstrap3` and `teavm/teavm-bootstrap5`. Prohibit reflection,
   `ServiceLoader`, scanning and runtime class lookup.
4. Generate one plain JUnit method per fixture under `TeaVMTestRunner`; do not
   execute reachability through a loop over dynamic registry entries.
5. Construct, mount, minimally inspect and detach every fixture independently.
6. Add explicit paths for service providers, generated UiBinder owners, range
   input, tooltip options and
   every currently known native seam.

Exit condition: the stale service descriptor, invalid element narrowing,
unimplemented native method and silent button-type fallback defects described
in this plan all fail a specifically named test, and a leaked `gwt-user`
dependency fails before producing misleading compiler errors. No Cucumber
Tea, frame or external-repository change is required to run it.

### Phase 1: Make the fixture catalogue authoritative

1. Move the initial registry behind stable fixture IDs and `data-testid`
   values.
2. Generate each module's exported-widget inventory directly from its compiled public API.
3. Fail on uncovered exports and stale fixtures.
4. Extract the Bootstrap 3 showcase behaviour inventory and write the complete
   prioritized feature corpus and four-target applicability matrix.
5. Require every frame-targeted scenario to identify its fixture and existing
   showcase route/section.

Exit condition: adding a public widget without a reachable fixture fails the
build, and the functional expectations exist as reviewed Gherkin before glue
or target-specific assertions are written.

### Phase 2: Add shared behavioural features

1. Add the shared feature-resource module containing the already reviewed P0
   specifications.
2. Move Phase 0 fixture orchestration into Cucumber Tea TeaVM scenarios while
   retaining the shared fixtures and contract methods.
3. Implement the P0 control scenarios for toggle button, checkbox button,
   radio button, loading state, labels, values and event semantics.
4. Implement attach/detach, repeated-mount, handler-cleanup and missing-binding
   scenarios.
5. Remove standalone assertions once their generated scenario is stable.

Exit condition: the direct suite detects controls that render but do not
toggle, emit the wrong value event or leak handlers after detach.

### Phase 3: Expose catalogue fixtures in the showcases

1. Add stable fixture hosts to `gwt/gwt-bootstrap3-showcase`,
   `gwt/gwt-bootstrap5-showcase`, `teavm/teavm-bootstrap3` and
   `teavm/teavm-bootstrap5` only for scenarios selected for frame coverage.
2. Share fixture owner and UiBinder sources between compiler targets. Generate
   their binder implementation through the native GWT generator or
   `widget-processor`; keep only the launcher target-specific.
3. Keep narrative text, source examples and non-tested demonstrations in the
   handwritten showcase pages.
4. Add a machine-readable fixture index and readiness marker to each
   showcase.
5. Fail when a frame-targeted fixture is absent from any target marked as
   supported.

Exit condition: all P0 frame fixtures are addressable by the same stable IDs
in the four showcases without converting the showcases into generated views
or removing their existing demonstrations.

### Phase 4: Add the frame host and rendered GWT tests

1. Serve complete static application directories from the test origin.
2. Load catalogue fixtures in same-origin frames.
3. Add document-rooted Mockatcha queries.
4. Add readiness, asset, console and error handling.
5. Run shared rendered scenarios against GWT and TeaVM showcases.

Exit condition: the same fixture scenario drives both compiler outputs in
Chromium and a blank showcase, failed Bootstrap plugin or child exception
fails with the frame URL and browser diagnostics.

### Phase 5: Add structural and regression comparisons

1. Capture `outerHTML` for each shared GWT and TeaVM fixture.
2. Implement and unit-test narrow volatile-value normalization.
3. Add reviewed snapshots for necessary differences.
4. Fail on missing, stale or unapproved differences.
5. Commit a separate normalized baseline for each compiler target and compare
   each target with its own prior approved output.

Exit condition: additional `gwt-*` classes, different label/ID composition or
an incorrect Bootstrap enum-to-class mapping cannot pass merely because click
behaviour still works, and an identical bad change in both compiler outputs is
caught by the per-compiler regression baselines.

### Phase 6: Add compatibility conformance

1. Create shared lifecycle, value, event, Safe HTML, scheduling and DOM
   contracts, each classified as JVM-safe or browser-bound.
2. Run JVM-safe contracts against real `gwt-user` through ordinary JUnit.
3. Run only the browser-bound reference set against real `gwt-user` through
   handwritten `GWTTestCase` wrappers.
4. Run both contract sets against `teavm-gwt-compat` through
   `TeaVMTestRunner`.
5. Add JSO narrowing, element factories, style names, native seams and
   resources/i18n.
6. Resolve or explicitly document semantic differences.

Exit condition: `cast()`/`as()` divergence, an incorrectly generalized
element factory, dependent-style drift or a missing bundle provider fails an
executable comparison rather than waiting for a consuming widget to expose it,
and plain-Java contracts do not pay the GWT compile or HtmlUnit cost.

### Phase 7: Generate GWT reference wrappers

1. Add the Cucumber Tea `gwt` generator target when the reference corpus is
   large enough for generation to remove meaningful duplication.
2. Generate `GWTTestCase` wrappers from the shared compatibility features.
3. Preserve the synchronous boundary and generation-time diagnostics.
4. Report both reference and TeaVM results in Maven output.

Exit condition: a new synchronous compatibility scenario can target both
implementations without a second handwritten orchestration class. This phase
does not move rendered widget behaviour into HtmlUnit.

### Phase 8: Expand the full behaviour matrix

1. Implement P1 navigation, data, value and composition feature files in
   priority order.
2. Implement P2 presentation, accessibility and responsive-layout feature
   files.
3. Implement P3 extras and third-party plugin feature files.
4. Cover every vendored theme and theme replacement lifecycle.
5. Reconcile each result with the Bootstrap 3 showcase baseline and record
   deliberate Bootstrap 5 semantic replacements in the target matrix.
6. Pin the CI browser and introduce accessibility gating after its baseline is
   clean.

Exit condition: every exported widget and extra has reachability coverage,
every interactive widget has a behavioural scenario, and a third-party script
or theme update that breaks initialization or replacement fails the matrix.

### Phase 9: Gate publication

1. Replace JavaScript-file existence checks with executable tests.
2. Build and test the assembled Pages directory once.
3. Deploy that exact verified artifact without a `-DskipTests` rebuild.
4. Fail publication on startup, route, asset or browser-console errors.
5. Publish the enforced coverage matrix and diagnostics.

Exit condition: a showcase cannot be published merely because its compiler
produced a JavaScript file, and Pages cannot deploy a different artifact from
the one CI verified.

## Deferred Option: TeaVM-hosted GWT Contract Runner

A future runner could generate a GWT test application, execute contracts in a
frame and report structured results to the TeaVM parent through `postMessage`.
That could remove `GWTTestCase` and run all contracts in Chromium.

It is intentionally deferred because it requires generated GWT entry points,
cross-frame result transport, assertion serialization, timeout handling and
source-location reporting. The generated `GWTTestCase` path is smaller and
provides an established GWT reference implementation now.

## Definition of Done

- Every exported widget is represented by the authoritative fixture catalogue
  and reached by its own statically constructed plain TeaVM test method.
- One shared feature corpus runs against GWT and TeaVM rendered applications.
- JVM-safe compatibility contracts run directly against real `gwt-user`, and
  only browser-bound contracts use `GWTTestCase`; generated wrappers are used
  once that smaller corpus justifies generation.
- Direct TeaVM tests use `mockatcha-dom` in Chromium.
- Framed tests use `mockatcha-dom` against GWT, TeaVM and packaged third-party
  interfaces.
- `teavm-gwt-compat` is tested against real GWT behaviour.
- TeaVM builds reject `gwt-user`, `gwt-dev` and duplicate compatibility classes.
- GWT and TeaVM fixtures for one Bootstrap generation pass normalized
  structural comparison or carry an approved difference.
- Each compiler target passes its independent committed markup-regression
  snapshots, including when both compiler outputs change identically.
- Extras and themes have reachability, initialization and lifecycle coverage.
- Every interactive widget has an observable behavioural scenario.
- The scenario expectations and four-target applicability matrix are reviewed
  against the Bootstrap 3 showcase before target glue is implemented.
- Browser exceptions and missing assets fail the build.
- The assembled showcase site is tested before publication.
- Pages deploys the exact artifact that passed verification.
- The CI report identifies untested behaviours and targets; missing widget
  fixtures fail the build.
