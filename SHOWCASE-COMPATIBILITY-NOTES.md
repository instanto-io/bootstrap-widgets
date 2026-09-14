# Showcase Compatibility Notes

Original reference: https://gwtbootstrap3.github.io/gwtbootstrap3-demo/

## Current Target

The `gwt-bootstrap3-showcase` module is the GwtBootstrap3 compatibility showcase. It should render and behave like the original Bootstrap 3 showcase while running on GWT 2.13.1, Bootstrap 3.4.1, and jQuery 3.7.1.

## Architecture Decision

An earlier Bootstrap 5 shim experiment showed that forcing Bootstrap 5 under the Bootstrap 3 widget API creates avoidable mismatches:

| Area | Why the shim was fragile |
| --- | --- |
| Navbar | Bootstrap 3 emits `data-toggle`; Bootstrap 5 expects `data-bs-toggle`. |
| Dropdowns | Bootstrap 3 uses explicit `.caret` markup; Bootstrap 5 adds its own pseudo-caret. |
| Dropups | Bootstrap 3 relies on legacy CSS placement; Bootstrap 5 uses Popper-oriented behaviour. |
| Pagination | Bootstrap 3 emits `ul.pagination > li > a`; Bootstrap 5 expects `.page-item` and `.page-link`. |
| Buttons | Bootstrap 3 exposes jQuery button states such as `button('loading')`; Bootstrap 5 removed that API. |

The conclusion is that `org.gwtbootstrap3` should remain a Bootstrap 3-compatible API. Bootstrap 5 should have a separate module, package namespace, templates, and showcase.

## Compatibility Checks

The GwtBootstrap3-compatible showcase should be checked against the original demo for:

- Navbar collapse on mobile.
- Dropdown and dropup direction.
- Pagination structure and sizing.
- Toggle button state.
- Loading button state.
- Panels, wells, labels, alerts, tabs, modals, and Glyphicons.

The Bootstrap 5-native showcase should be compared against Bootstrap 5 expectations, not Bootstrap 3 pixel parity.
