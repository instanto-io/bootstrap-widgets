# Test Architecture

How the tests in this repository fit together, and what each layer is for.

The library is one set of widget sources compiled by two very different compilers.
Almost every testing decision here follows from that: the question is rarely "does
this widget work" but "does it work the same way on both backends".

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

Two checks that are not tests of behaviour but of the repository's shape:

- **`check-module-layout.py`** — every TeaVM pom's `add-source` and `<excludes>`
  must be exactly the expected set, and every excluded GWT file must have a TeaVM
  counterpart that exists. This is what stops a half-made seam, where a file is
  excluded but nothing replaces it.
- **`check-showcase-samples.py`** — the showcase's hand-written code snippets must
  name widgets that exist and must be well-formed. They are maintained separately
  from the live examples beside them, so they drift.

## Where things run

| | local `mvn install` | CI |
|---|---|---|
| Contracts, widget tests, processor tests | yes | yes |
| Browser behaviour tests | no | yes |
| Structural guards | no | yes |
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
| `check-module-layout.py` | 148 | CI |
| `prepare-showcase-debug-artifacts.py` | 122 | CI |
| `check-showcase-samples.py` | 104 | CI |
| `smoke-showcase-pages.sh` | 78 | CI |

Nothing here gates a local build any more: `mvn install` no longer needs a python
interpreter. All six run in CI only.
