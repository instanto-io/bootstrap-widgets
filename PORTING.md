# Porting Notes

## Source Baseline

This fork is based on the latest upstream GwtBootstrap3 projects rather than the old `0.9.4` jar snapshot:

- `gwtbootstrap3` upstream `master`: `66b482b4ec5db54931ff76c4a66454ea4d39637f`
- `gwtbootstrap3-extras` upstream `master`: `3b4c82f547f39fbfcf9d44b4b912ea3fdd61c13b`
- `gwtbootstrap3-demo` upstream `master`: `d4fc399914ac386220fc351dbd707592afa34224`

Upstream still declares old `com.google.gwt` GWT `2.8.0`. This fork deliberately targets `org.gwtproject:gwt-user` and `org.gwtproject:gwt-dev` `2.13.1` to match current GWT usage.

## Compatibility Rules

- Keep public package names and GWT module names stable for drop-in GwtBootstrap3 consumers.
- Preserve Bootstrap 3 widget semantics in `gwt-bootstrap3` and `gwt-bootstrap3-extras`.
- Prefer resource and dependency maintenance over Java API churn in the Bootstrap 3 compatibility modules.
- Do not leak Bootstrap 5, Domino, Elemental2, or TeaVM types into the existing `org.gwtbootstrap3` API.
- Use separate modules for Bootstrap 5-native widgets and for TeaVM experiments.

## Bootstrap 3 Compatibility Strategy

The compatibility modules remain Bootstrap 3 modules. They use Bootstrap 3.4.1, Bootstrap theme CSS, Font Awesome 4.7.0, and jQuery 3.7.1 while retaining the original GwtBootstrap3 Java API and emitted Bootstrap 3 markup.

This is the safest path for existing applications because templates using Bootstrap 3 class names, `data-toggle` attributes, button state calls, dropdown/dropup behaviour, pagination markup, panels, wells, labels, and Glyphicons continue to mean what they meant in the original library.

## Bootstrap 5 Strategy

Bootstrap 5 is implemented as a separate migration track, not as a hidden replacement inside the Bootstrap 3 compatibility artifact.

The current shape is:

1. `gwt-bootstrap5`: Bootstrap 5-native GWT widgets under `io.instanto.bootstrap5.*`.
2. `gwt-bootstrap5-showcase`: a Bootstrap 5-native showcase that exercises Bootstrap 5 markup, class names, data attributes, and interaction patterns.
3. `BOOTSTRAP5-PORTING.md`: an inventory tracking Bootstrap 5 coverage against the existing GwtBootstrap3 widget catalogue.

Planned follow-up work:

1. Continue porting Bootstrap 3 widget concepts into Bootstrap 5-native equivalents where Bootstrap 5 still has the concept.
2. Replace Bootstrap 3 style names and markup with Bootstrap 5 style names and markup in the Bootstrap 5 modules.
3. Replace jQuery plugin calls with Bootstrap 5 JavaScript APIs or plain DOM behaviour in the Bootstrap 5 modules.
4. Allow breaking API changes where Bootstrap 5 removed or redesigned Bootstrap 3 behaviour.
5. Add explicit migration helpers only where they clarify the move from Bootstrap 3 to Bootstrap 5.
6. Grow `teavm-bootstrap5` alongside the Bootstrap 5 GWT surface.
7. Add `teavm-bootstrap5-showcase` once the Bootstrap 5 TeaVM surface is less skeletal.

The Bootstrap 5 track should not carry Bootstrap 3 CSS compatibility classes or jQuery as a long-term runtime requirement. During porting, a widget is considered Bootstrap 5-native only when its generated classes, data attributes, and interactive behaviour match Bootstrap 5 expectations.

## GWT And TeaVM Outputs

This repository keeps separate buildable outputs:

1. Bootstrap 3 GWT compatibility: `gwt-bootstrap3`, `gwt-bootstrap3-extras`, and `gwt-bootstrap3-showcase`.
2. Bootstrap 5 GWT native: `gwt-bootstrap5` and `gwt-bootstrap5-showcase`.
3. Shared TeaVM compatibility: `teavm-gwt-compat`, a small GWT client API subset used by both TeaVM tracks.
4. Bootstrap 3 TeaVM experiment: `teavm-bootstrap3`, compiled by TeaVM to JavaScript as part of its Maven build.
5. Bootstrap 5 TeaVM experiment: `teavm-bootstrap5`, compiled by TeaVM to JavaScript as part of its Maven build.

## TeaVM Direction

The TeaVM modules are currently widget backend prototypes, not complete replacements for the GWT implementation. They share `teavm-gwt-compat` for a small GWT client surface, but do not depend on UiBinder; TeaVM widgets are constructed directly through Java APIs and lightweight DOM adapters. The intended route is:

1. Keep stabilising the internal DOM/plugin abstraction.
2. Move direct JSNI/jQuery calls behind small interfaces.
3. Provide TeaVM JSO-backed implementations for those interfaces.
4. Share widget behaviour above that adapter where possible.
5. Add same-package compatibility facades only when the backend contracts are stable enough to avoid a divergent second library.

A broad Elemental2-on-TeaVM compatibility layer is not the preferred first move. A small WebIDL-driven set of TeaVM JSO bindings may be useful later, but only for browser APIs actually needed by this library.
