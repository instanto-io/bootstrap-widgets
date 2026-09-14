# Bootstrap 5 integrations

Five optional extras add selection, data grids, sortable lists, dashboards and image
viewing. They use the existing `Div`/`ComplexWidget` hierarchy, ordinary child widgets
and GWT event handlers. GWT and TeaVM compile the same widget sources; only the native
plugin bridges differ.

## Components

| Widget | Supports | Showcase route |
| --- | --- | --- |
| `SearchableSelect` | Single/multiple selection, tags, disabled state, asynchronous option provider, `HasValue<List<String>>` | `#searchableSelect` |
| `DataTable` | Plain-text columns, editable cells, grouping, `_children` tree rows, local pagination, remote scroll loading, filtering | `#dataGrid` |
| `SortableList` | Ordinary widget children, reordering, cross-list groups, disabled state, ordered child events | `#sortableLists` |
| `Dashboard` / `DashboardTile` | Twelve-column responsive layout, dragging, resizing, ordinary tile contents, layout save/restore, locked layout | `#dashboard` |
| `ImageGallery` | Thumbnail links, full-size images with known dimensions, zoom, touch/keyboard navigation, selection events, open/close | `#gallery` |

The showcase uses local sample images. The asynchronous selection demo simulates a
directory lookup; it does not call a remote service.

## Loading

GWT applications depend on `io.instanto:gwt-bootstrap5-extras:1.0-SNAPSHOT` and inherit
the modules they use:

```xml
<inherits name="io.instanto.bootstrap5.extras.select.Select"/>
<inherits name="io.instanto.bootstrap5.extras.grid.Grid"/>
<inherits name="io.instanto.bootstrap5.extras.sortable.Sortable"/>
<inherits name="io.instanto.bootstrap5.extras.dashboard.Dashboard"/>
<inherits name="io.instanto.bootstrap5.extras.gallery.Gallery"/>
```

TeaVM applications use `teavm-bootstrap5`. Serve its bundled `META-INF/bootstrap5-assets/`
tree and configure the asset base before calling `Bootstrap5.initialise(...)`. Each extra
uses the generated module loader on attachment. Do not manually add vendor script tags
or add `gwt-user` to the TeaVM classpath.

Attach widgets through `RootPanel` or another panel. Native plugin instances become
available after resources load; `isReady()` reports this. Removing a widget destroys its
plugin and invalidates pending initialisation callbacks. Reattachment creates a fresh
instance. Selection, grid edits and dashboard layout are retained.

## API details

Configure structural options before attaching. Changing enabled state and selection is
supported while attached. `SearchableSelect.setValue(value)` is silent; its overload with
`true` fires one event only if the value changed. Add an option before selecting its value,
unless tag creation is enabled. Remote providers complete an `AsyncCallback`; responses
from a previous attachment are ignored. This is a callback contract, not GWT RPC transport.

Grid rows are Java maps. Columns use plain text rather than interpreting cell content as
HTML. Edits are available through `getRowsJson()` and its value-change event. Configure
tree children under `_children`. `setRemoteUrl` expects Tabulator's remote pagination
response, including `last_page` and `data`. The table has a bounded height for scroll
loading; change it with `setHeight`. Initial rows and columns are configured before
attachment. Filters can be set once the native plugin is ready.

Sortable lists with the same non-empty group accept each other's children. After a drag,
both DOM order and Java widget ownership are reconciled; affected lists each report their
ordered children. This matters when applications subsequently remove or update a card.

Dashboard tile keys must be unique within the dashboard. Saved layouts contain positions
and sizes, not widget contents. Restoring a layout updates existing tiles only; applications
remain responsible for creating their contents. Image URLs may be relative or HTTP(S),
and image dimensions must be positive.

## Tests and limits

`advanced-integrations.feature` runs through Java Cucumber Tea steps, mockatcha-dom and
TeaVMTestRunner in Chromium. It covers actual dropdown selection, HasValue event semantics,
tag preservation, disabled selection, filtering, dashboard ownership and layout restoration,
gallery open/close, and detach/reattach for all five plugins. Assets come from the built
library, not test-owned injection.

This is the first wrapper tranche, not the entire vendor API. Remote selection currently
loads one result set per query, not incremental scroll pages. Grid column formatters,
remote authentication configuration and richer row-editing APIs remain to be exposed.
Pointer/touch drag behaviour, remote paging against a server and cross-compiler visual
comparison still need dedicated tests. The browser scenarios above run the TeaVM widgets;
the GWT showcase is also compiler-checked.
