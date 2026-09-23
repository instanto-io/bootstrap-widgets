# Showcase Architecture

How the showcase applications are built, and how one set of sources produces both a
GWT and a TeaVM version of the same site.

## The idea

There is one showcase per Bootstrap generation, and each is compiled twice — once by
GWT and once by TeaVM. Not ported twice: compiled twice, from the same Java. If the
two versions of a page differ, that is a bug in the compatibility layer, which is
what makes the showcase useful as more than a demo.

## Where the source lives

All widget and showcase source lives under `gwt/`:

```
gwt/gwt-bootstrap3            widgets
gwt/gwt-bootstrap3-themes     themes
gwt/gwt-bootstrap3-extras     extras (jQuery plugins; GWT only)
gwt/gwt-bootstrap3-showcase   the showcase application
```

and the same shape for Bootstrap 5.

The TeaVM modules own almost no source of their own. `teavm/teavm-bootstrap3` adds
the GWT modules' `src/main/java` as extra source roots and compiles them itself:

| TeaVM module | compiles source from |
|---|---|
| `teavm-bootstrap3` | `gwt-bootstrap3`, `-themes`, `-showcase` |
| `teavm-bootstrap5` | `gwt-bootstrap5`, `-themes`, `-extras`, `-showcase` |

`gwt-bootstrap3-extras` is absent from that list deliberately: it reaches the browser
through 353 JSNI methods across 52 files, which only the GWT compiler understands.

## Seams

A seam is the smallest file where the two backends must differ. The same fully
qualified class exists twice — once under `gwt/`, once under `teavm/` — and each
build excludes the other's copy through the compiler plugin's `<excludes>`.

```
gwt/gwt-bootstrap5-extras/.../ui/SliderJs.java      JSNI      compiled by GWT
teavm/teavm-bootstrap5/.../ui/SliderJs.java         @JSBody   compiled by TeaVM
```

There are 6 seam pairs on the Bootstrap 3 track and 8 on Bootstrap 5. They fall into
three kinds:

- **Browser access** — JSNI on one side, `@JSBody` on the other (`SliderJs`, `QuillJs`,
  `DatePickerJs`, `InputEvents`).
- **Resource loading** — GWT compiles a script into the module; TeaVM fetches it by
  URL (`ShowcaseScripts`, `Markdown`).
- **Unavailable features** — `ExtrasPages` returns the 14 extras-backed showcase pages
  on GWT and `null` on TeaVM, which is how the entry point itself stays shared.

The TeaVM builds' `verify-*-source-seams` enforcer rules require every excluded file
to exist and the TeaVM half of each remaining seam, so a seam cannot be left half-made.

## The two showcases

**Bootstrap 3** — one entry point routing on the history token, 55 pages. 41 are
ordinary widget code and build for both backends. The other 14 need the extras and
are reached through the `ExtrasPages` seam, so the TeaVM version is short those pages
and complete otherwise. GWT Platform's MVP and GIN were removed: both reach their
wiring through deferred binding, which TeaVM has no equivalent for, so no application
using them can be compiled for that backend.

**Bootstrap 5** — a single ~3,000-line entry point that builds each page on demand
from a token. It runs on both backends unmodified.

## Machinery TeaVM needs that GWT provides itself

GWT's module system does several things at compile time that TeaVM has no equivalent
for. Each is replaced by something explicit.

| GWT mechanism | TeaVM replacement |
|---|---|
| UiBinder generator | `widget-processor`, an annotation processor |
| `GWT.create` deferred binding | `ServiceLoader` and `META-INF/services` |
| `<stylesheet>`, ClientBundle scripts | `gwt-resources-compat-maven-plugin` |
| module readiness | `ScriptModule` |

**UiBinder.** GWT reaches its generator through deferred binding. The same work runs
inside `javac` as an annotation processor, which has the advantage of a real type
model rather than an approximation of one. A module with no templates generates
nothing, so this costs an application that does not use UiBinder exactly nothing.

Text-only `ClientBundle` interfaces also get generated providers, including nested
source bundles used to display templates. Their service descriptors use binary
names (`View$Source`), and missing source resources fail compilation rather than
leaving `GWT.create` to fail when a page is opened.

The Bootstrap 5 UiBinder example is a routed page shared by both compilers. The
HTML hosts do not append extra navigation or demonstrations outside the shared
application. Both hosts load the same showcase spacing stylesheet.

**Module resources.** `gwt-resources-compat-maven-plugin` reads the same `.gwt.xml` and
ClientBundle declarations the GWT build reads, and writes one class per module that
loads the same files by URL, plus the assets themselves.

**Readiness.** GWT has no way for a module to say its initialisation is still in
flight — `onModuleLoad` returns `void`. `ScriptModule` adds that: scripts in a module
load in the order declared, a module that fails to load says so once, and a library
already on the page is recognised without fetching a second copy. Widgets ask
`whenReady(...)` instead of polling for a global to appear.

## Assembling the site

After assembly, run `mvn -f testing/showcase-browser-tests/pom.xml test`. These
Java/Gherkin tests use Mockatcha's same-origin application frames and
`TeaVMTestRunner` to check the built GWT and TeaVM pages, without initialising
widgets or loading their assets from the harness. CI runs them before publication.

`showcase-site` collects everything into one directory of static pages.

```
unpack-applications        the four GWT war artefacts (showcases + fixture pages)
unpack-api-documentation   javadoc
copy-site-assets           TeaVM output, stylesheets, fonts, themes, vendored js
alias-bootstrap3-showcase  showcase.html alias
verify-assembled-site      enforcer: required files must exist
```

The result:

| Page | What it is |
|---|---|
| `index.html` | landing page |
| `GwtBootstrap3Demo.html` | Bootstrap 3 showcase, GWT |
| `teavm.html` | Bootstrap 3 showcase, TeaVM |
| `bootstrap5/` | Bootstrap 5 showcase, GWT |
| `teavm-bootstrap5.html` | Bootstrap 5 showcase, TeaVM |
| `fixtures/gwt-bootstrap3/`, `fixtures/gwt-bootstrap5/` | fixture pages for the behaviour tests |

## Two things that surprise people

**`mvn install` does not run the GWT compiler.** The showcase modules are packaged as
`war`, so `gwt:compile` runs only when invoked explicitly:

```
mvn -f gwt/gwt-bootstrap3-showcase/pom.xml -Dgwt.forceCompilation=true gwt:compile
```

A green reactor build says nothing about whether the GWT compile succeeds. CI runs
it as a separate step for each of the four applications.

**Adding a source root needs a clean.** `build-helper`'s `add-source` is evaluated at
`generate-sources`; running `mvn install` without `clean` after changing it will
silently compile the previous set of sources.
