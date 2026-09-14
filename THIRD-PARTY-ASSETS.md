# Third-party browser assets

This fork vendors browser JS/CSS resources inside the GWT modules so downstream consumers can continue to build with Maven only. npm is used only as a maintainer convenience for fetching pinned upstream release artifacts; it is not part of the consumer build path and is not required when applications build against the fork.

## Policy

- Keep vendored assets checked in with the Java/GWT sources.
- Pin exact upstream versions in this file and in `pom.xml` properties.
- Ship source maps for refreshed minified core assets where upstream publishes them.
- Do not add npm, node, or frontend package-manager execution to the Maven lifecycle.
- Prefer upstream release artifacts from npm only where the original project publishes its browser distribution there.
- When assets are refreshed, rebuild the fork and run the dependency audit profile before publishing.

## Bootstrap 3 Compatibility Assets

| Asset | Version | Source package | Notes |
| --- | --- | --- | --- |
| Bootstrap | 3.4.1 | `bootstrap` | Core framework for the GwtBootstrap3-compatible modules. |
| Bootstrap theme | 3.4.1 | `bootstrap` | Optional Bootstrap 3 theme CSS loaded by the compatibility module. |
| jQuery | 3.7.1 | `jquery` | Updated compatibility dependency for Bootstrap 3 and legacy widgets/plugins. |
| Font Awesome | 4.7.0 | `font-awesome` | Retained for the existing `IconType` API. |
| Bootbox | 6.0.4 | `bootbox` | Updated dialog layer used by extras. |
| bootstrap-datepicker | 1.10.1 | `bootstrap-datepicker` | Kept because public widgets are tied to this plugin. |
| bootstrap-datetimepicker | 2.4.4 | upstream gwtbootstrap3-extras | Newest version in upstream extras baseline. |
| bootstrap-select | 1.14.0-beta3 | `bootstrap-select` | Latest maintained jQuery line; not a pure Bootstrap 3 visual match. |
| bootstrap-slider | 11.0.2 | `bootstrap-slider` | Drop-in jQuery plugin update. |
| bootstrap-switch | 3.4.0 | `bootstrap-switch` | Last compatible line. |
| typeahead.js | 0.11.1 | `typeahead.js` | Last upstream release. |
| animate.css | 4.1.1 | `animate.css` | Uses `animate.compat.css` to preserve legacy class names. |
| Summernote | 0.9.1 | `summernote` | Current maintained browser distribution. |
| FullCalendar | 3.10.5 | `fullcalendar` | Last jQuery-compatible line; newer major versions are not drop-in. |
| jQuery UI | 1.13.3 | `jquery-ui-dist` | Used by FullCalendar drag/resize support. |
| Moment | 2.30.1 | `moment` | Used by FullCalendar 3.x. |
| Blueimp Gallery | 3.4.0 | `blueimp-gallery` | Core gallery plugin update; legacy Bootstrap image gallery adapter retained. |
| bootstrap-notify | 3.1.3 | `bootstrap-notify` | Latest available line, retained. |
| bootstrap-tagsinput | 0.8.0 | upstream gwtbootstrap3-extras | Newer npm package is older than the bundled upstream asset, retained. |

## Bootstrap 3 Theme Assets

Vendored in `gwt-bootstrap3-themes`, which is a separate artifact because the set
totals about 1.9 MB. Bootstrap 3 has no dark mode of its own; these supply one.

| Asset | Version | Source package | Licence | Notes |
| --- | --- | --- | --- | --- |
| Bootswatch | 3.4.1 | `bootswatch` | MIT | 16 complete Bootstrap 3.4.1 stylesheets. Four are dark: Cyborg, Darkly, Slate, Superhero. Licence text is kept at `gwt/gwt-bootstrap3-themes/BOOTSWATCH-LICENSE`. |

Each theme replaces `bootstrap.css` rather than layering over it, so switching swaps one
stylesheet link. Fourteen of the sixteen `@import` their webfonts from Google Fonts, as
they do upstream, which means a page using them reaches the network at runtime. Only
Cerulean and Slate do not, and are the ones to prefer where that matters -- Slate being
the only dark theme of the set that is self-contained.

## Bootstrap 5 Native Assets

The core Bootstrap 5 artifact is deliberately independent of Bootstrap 3 and jQuery.

| Asset | Version | Source package | Licence | Loaded by |
| --- | --- | --- | --- | --- |
| Bootstrap | 5.3.8 | `bootstrap` | MIT | `io.instanto.bootstrap5.GwtBootstrap5` loads the CSS and bundled JavaScript. `io.instanto.bootstrap5.GwtBootstrap5NoTheme` omits only the CSS so an application can supply or switch the stylesheet itself. |
| Bootstrap Icons | 1.13.1 | `bootstrap-icons` | MIT | Both Bootstrap 5 GWT modules load the icon CSS and local font files. |

These files live under `gwt/gwt-bootstrap5/src/main/java/io/instanto/bootstrap5/client/resource`.
They must not be loaded by `gwt-bootstrap3` or used as a hidden compatibility
layer for the Bootstrap 3 widgets.

### Bootstrap 5 Theme Assets

Themes are kept in the separate `gwt-bootstrap5-themes` artifact. An application
inherits `io.instanto.bootstrap5.GwtBootstrap5NoTheme` and
`io.instanto.bootstrap5.themes.GwtBootstrap5Themes`, then selects Bootstrap or a Bootswatch
theme through the theme API. A selected theme replaces `bootstrap.css`; it is not added
on top of it.

| Asset | Version | Source package | Licence | Notes |
| --- | --- | --- | --- | --- |
| Bootswatch | 5.3.8 | `bootswatch` | MIT | 26 complete Bootstrap 5.3.8 stylesheets. Seven are dark: Cyborg, Darkly, Quartz, Slate, Solar, Superhero and Vapor. Licence text is kept at `gwt/gwt-bootstrap5-themes/BOOTSWATCH-LICENSE`. |

Twenty of the Bootswatch 5 stylesheets import webfonts from Google Fonts and therefore
make a network request when used. The remaining six use no remote font import.

### Optional Bootstrap 5 Extras

The Bootstrap 5 extras artifact contains third-party components that are not required by
the core widgets. Inheriting an extra's GWT module loads only that extra's resources.

| Asset | Version | Source package | Licence | Used by |
| --- | --- | --- | --- | --- |
| Tempus Dominus | 6.10.4 | `@eonasdan/tempus-dominus` | MIT | `io.instanto.bootstrap5.extras.datepicker.DatePicker`; replaces the Bootstrap 3 date and date-time picker plugins without requiring jQuery. |
| Popper | 2.11.8 | `@popperjs/core` | MIT | The DatePicker module exposes the global Popper API required by Tempus Dominus. Bootstrap's bundled private copy cannot satisfy that dependency. |
| Quill | 2.0.3 | `quill` | BSD-3-Clause | Rich text editor, replacing the Summernote extra. No jQuery, ships its own toolbar. Licence text at `gwt/gwt-bootstrap5-extras/QUILL-LICENSE`. |
| noUiSlider | 15.8.1 | `nouislider` | MIT | Slider for what a native range input cannot do: two handles, non-linear scales, tooltips, pips. No dependencies. Licence text at `gwt/gwt-bootstrap5-extras/NOUISLIDER-LICENSE`. |
| marked | 18.0.11 | `marked` | MIT | Markdown parser, configured for GitHub Flavoured Markdown so a client-side preview matches what flexmark-java renders with its tables, strikethrough and task list extensions. No dependencies. |
| DOMPurify | 3.4.14 | `dompurify` | MPL-2.0 OR Apache-2.0 | Sanitises rendered Markdown before it reaches the DOM. marked does not sanitise, by design, and Markdown permits raw HTML. |
| Tom Select | 2.6.2 | `tom-select` | Apache-2.0 | Searchable selects, multiple selection, tags and asynchronous options; Bootstrap 5 stylesheet. |
| Tabulator | 6.5.2 | `tabulator-tables` | MIT | Editable data grids, grouping, tree rows and pagination; Bootstrap 5 stylesheet. |
| SortableJS | 1.15.7 | `sortablejs` | MIT | List reordering and moving widgets between lists. |
| GridStack | 13.2.0 | `gridstack` | MIT | Responsive, draggable and resizable dashboard tiles. |
| PhotoSwipe | 5.4.4 | `photoswipe` | MIT | Image lightbox, zoom and swipe navigation; UMD core and lightbox distributions. |

The five integration distributions come from their official npm release archives. Their
licences are kept in `gwt/gwt-bootstrap5-extras/*-LICENSE` and bundled under each
module's `client/resource/licenses/` directory. Available upstream maps accompany the
vendored files. The build does not run npm or retrieve assets from a CDN.

Programmatic alert, confirm and prompt dialogs use the core `Dialogs` and `Modal`
widgets. They do not vendor Bootbox or add jQuery to the Bootstrap 5 track.
