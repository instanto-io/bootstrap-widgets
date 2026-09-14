# Native Bootstrap Components

Toast, Offcanvas and Placeholder are part of the core Bootstrap 5 library, not the extras
artifact. Their Java sources are shared by GWT and TeaVM.

## Composition

- `Toast` contains `ToastHeader` and `ToastBody`. Set autohide, delay and animation before
  showing. `ToastContainer` is a polite live region and stacks ordinary child toasts.
  Its `notify(title, message, delay)` convenience method removes a notification after hiding.
- `Offcanvas` contains `OffcanvasHeader` and `OffcanvasBody`. The header supplies its
  accessible title. Start, end, top and bottom placements, backdrop, body scrolling and
  Escape dismissal are configurable. Focus returns to the opener on hiding.
- `Placeholder` uses Bootstrap's column widths and sizes. `PlaceholderContainer` supplies
  glow or wave animation. Skeletons are decorative; provide loading status outside them.

The lifecycle base extends the established `Div`/`ComplexWidget` hierarchy and uses the
existing Bootstrap component and event bridges. Show, shown, hide and hidden use the same
Java event types as Collapse. Show and hide can be cancelled. Text setters return `void`,
and headers implement `HasText` and `IsClosable`. There is no parallel TeaVM widget API.

Mount widgets through an ordinary panel before showing them. Mount notification live
regions before adding messages. Bootstrap caches plugin options: changing an option while
visible or transitioning is rejected; wait for hidden before changing it. Removing a
widget finishes pending transitions and disposes its plugin, timers and overlay state.

TeaVM applications serve the library JAR's `META-INF/bootstrap5-assets/` tree and set
`Bootstrap5Resources.setAssetBase(...)` before initialisation. Use
`Bootstrap5.initialise(ready)` to start the application after its module resources are usable.
GWT performs this initialisation through the inherited module.

## Showcase

Native visual widgets are under Components; native interactive widgets are under Interactive.
Third-party libraries are under Integrations. `#cards`, `#dialogs` and the other existing
routes remain valid. The new routes are `#toasts`, `#offcanvas` and `#placeholders` on both
showcases. Source examples accompany each page.

Tom Select, Tabulator, SortableJS, GridStack and PhotoSwipe have separate integration
pages and shared Java wrappers in the extras artifact. See [Bootstrap 5 integrations](BOOTSTRAP5-INTEGRATIONS.md)
for the supported APIs and remaining coverage.

## Tests

`native-feedback.feature` and `Bootstrap5NativeFeedbackSteps` exercise mounted widgets
in Chromium through Cucumber Tea, TeaVMTestRunner and mockatcha-dom. The scenarios cover
events, cancellation, dismissal, timers, reattachment, keyboard behaviour, focus, overlay
cleanup and placeholder markup. They use the packaged module loader, with no test-owned
Bootstrap script injection. These are direct TeaVM widget tests, not cross-compiler frame
tests or a visual-regression suite.
