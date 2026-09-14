# Extras Inventory

The extras module is now based on latest upstream `gwtbootstrap3-extras` source, with browser assets refreshed where a drop-in or near drop-in path exists. npm is only a maintainer-side source for fetching pinned release assets; consumers build from checked-in resources with Maven only.

| Extra | Fork asset version | State | Notes |
| --- | --- | --- | --- |
| Bootbox | `6.0.4` | Updated | Uses `bootbox.all.min.js` so locales remain available. |
| DatePicker | `1.10.1` | Updated | Preserves existing Java API and locale injection model. |
| DateTimePicker | `2.4.4` | Updated to latest upstream extras baseline | Plugin remains legacy, but source/API now match upstream latest. |
| Select | `1.14.0-beta3` | Updated | Bootstrap 3-compatible jQuery plugin. Shared GWT/TeaVM API; locale aliases preserve bundle methods. |
| ToggleSwitch | `3.4.0` | Patched | Initial class filter corrected to retain non-null state/size classes. Shared by GWT and TeaVM. |
| Animate | `4.1.1` | Updated | Uses `animate.compat.css` to preserve legacy class names. |
| FullCalendar | `3.10.5` | Updated | Last jQuery-compatible line. Newer major versions are not drop-in. |
| jQuery UI | `1.13.3` | Updated | Required by FullCalendar 3 drag/resize support. |
| Moment | `2.30.1` | Updated | Required by FullCalendar 3. |
| Summernote | `0.9.1` | Shared GWT/TeaVM API | Bootstrap 3 retains its jQuery editor. Bootstrap 5 uses Quill. |
| Typeahead | `0.11.1` | Updated | Last upstream release; includes compatibility CSS. |
| TagsInput | `0.8.0` | Retained | No clearly newer compatible upstream release. Consider replacement later. |
| Notify | `3.1.3` | Retained | Latest available line. Consider Bootstrap 5 toast-backed implementation later. |
| Slider | `11.0.2` | Updated | Drop-in plugin update. |
| Card | `1.0.1` | Retained | Project-local CSS helper, not an external runtime plugin. |
| Gallery | Blueimp `3.4.0`, Bootstrap image gallery `3.4.2` | Partially updated | Blueimp core updated; legacy Bootstrap image gallery adapter retained. |
| CacheManifest | upstream latest | Retained | Source-only helper from upstream extras. |

## Validation Gates

### TeaVM Bootstrap 3 Port

The TeaVM port currently packages Animate, Card, Markdown, ToggleSwitch, Slider, Select and Summernote inside
`teavm-bootstrap3`. Other extras are not yet available on that backend; the asset
versions above describe the GWT extras, not TeaVM implementation coverage.

ToggleSwitch and ToggleSwitchRadio compile from the shared GWT widget source using
backend-specific plugin bridges. Their CSS and JavaScript are bundled from the
resource declarations, and the original Toggle Switch showcase page is included.
Java/Cucumber Tea scenarios cover selection, event flags, disabled/read-only state,
configuration and reattachment. An isolated TeaVMTestRunner test covers removal
while the plugin script is still loading.

Slider and RangeSlider preserve numeric/range values, formatter callbacks and slider events.
Select and MultipleSelect preserve option groups, search, value-change events and locale
selection. Locale scripts are bundled as text resources and injected only when selected.
Summernote preserves its editor API, toolbar, hints, image callbacks and locales. Its
shared showcase uses bundled emoji hints so that loading it does not call GitHub.
Quill remains the editor for the Bootstrap 5 tracks.

Java/Cucumber Tea browser tests cover these ports, including Slider arrow keys and
dropdown Escape handling. They use mockatcha-dom's keyboard-event normalization,
which supplies both modern key fields and legacy keyCode/which values.

### Local Asset Patch

`bootstrap-switch-3.4.0.min.cache.js` has one local correction: the initial wrapper
class filter uses `null != F`, not `null == F`. The latter retained undefined class
names and discarded `on`/`off`, size, disabled and inverse classes. Keep this fix when
refreshing the vendored asset unless the replacement already corrects it.

### Build Checks

- `mvn -DskipTests package`
- `mvn -f gwt/gwt-bootstrap3-showcase/pom.xml -DskipTests -Dgwt.forceCompilation=true gwt:compile`
- `mvn -Pdependency-audit -Dmaven.test.skip=true -DskipTests verify`
- Browser smoke test of the showcase, especially modal, dropdown, datepicker, select, summernote, and gallery pages.
