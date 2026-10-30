# Bootstrap Widgets

Bootstrap Widgets brings Bootstrap 3 and Bootstrap 5 components to Java
applications built with GWT or TeaVM. The Bootstrap 3 API preserves the
original `org.gwtbootstrap3.*` packages for existing applications. The
Bootstrap 5 API uses `io.instanto.bootstrap5.*` and follows Bootstrap 5's
markup and interaction model.

The TeaVM variants compile the corresponding GWT widget sources. This lets
an application move to TeaVM while keeping its widget code where the APIs
match. Bootstrap 5 is a migration path, so layouts and older Bootstrap 3
concepts may need deliberate changes.

| Track | Best fit |
| --- | --- |
| GWT Bootstrap 3 | Maintain an existing GwtBootstrap3 application. |
| TeaVM Bootstrap 3 | Compile that widget API with TeaVM. |
| GWT Bootstrap 5 | Build a GWT application with Bootstrap 5 components. |
| TeaVM Bootstrap 5 | Use the Bootstrap 5 API with TeaVM. |

## Bootstrap 5 API continuity

The Bootstrap 5 track is designed to preserve the GwtBootstrap3 programming
model as much as possible. Where a Bootstrap 3 widget had a familiar Java API,
composition pattern, or lifecycle, the Bootstrap 5 port tries to keep the same
shape and usage patterns rather than reinventing the library from scratch. This
keeps migration work smaller for existing GWT applications and makes the
bootstrap3-to-bootstrap5 transition more predictable.

That said, Bootstrap 5 is not a literal clone of the Bootstrap 3 API. Some
differences are unavoidable because the underlying HTML, CSS classes,
JavaScript plugin model, and interaction semantics changed in Bootstrap 5. In
particular:

- markup and class names follow Bootstrap 5 conventions rather than Bootstrap 3
  ones
- some widgets are reworked to align with Bootstrap 5's native APIs and DOM
  structure
- plugin-based extras that relied on jQuery or legacy Bootstrap 3 behavior are
  either rewritten, wrapped differently, or intentionally scoped to the parts
  that still map cleanly
- newer Bootstrap 5 features and naming conventions may appear, even when the
  older widget patterns are retained

In other words, the goal is compatibility of intent and developer workflow, not
a byte-for-byte API transplant. Where behavior or markup diverges, the project
documentation and showcase examples call that out explicitly.

## Explore the showcases

- [GWT Bootstrap 3](https://instanto-io.github.io/bootstrap-widgets/)
- [GWT Bootstrap 5](https://instanto-io.github.io/bootstrap-widgets/bootstrap5/)
- [TeaVM Bootstrap 3](https://instanto-io.github.io/bootstrap-widgets/teavm.html)
- [TeaVM Bootstrap 5](https://instanto-io.github.io/bootstrap-widgets/teavm-bootstrap5.html)

The GWT and TeaVM showcases use the same widget and example sources where
possible, making the runtime differences visible. Check
[Bootstrap 5 coverage](BOOTSTRAP5-PORTING.md) and the
[extras inventory](EXTRAS-INVENTORY.md) when choosing a component.

## Start with a Bootstrap 5 button

Initialise the browser resources before adding widgets. This example uses a
TeaVM entry point and attaches a button through `RootPanel` so its lifecycle
runs normally:

```java
package example;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.bootstrap5.client.Bootstrap5;
import io.instanto.bootstrap5.client.Bootstrap5Resources;
import io.instanto.bootstrap5.client.ui.Button;

public final class HelloBootstrap {
    public static void main(String[] args) {
        Bootstrap5Resources.setAssetBase("assets/bootstrap5/");
        Bootstrap5.initialise(() -> {
            RootPanel.get().add(new Button("Hello Bootstrap"));
        });
    }
}
```

The [Hello Bootstrap application](examples/hello-bootstrap5) includes the host
page and browser assets. The
[TeaVM Bootstrap 5 guide](teavm/teavm-bootstrap5/README.md) continues with
themes, integrations and UiBinder.

## React to input

Inside the ready callback, replace the button with a text box, button and
result label. Import `TextBox` and `Label` from
`io.instanto.bootstrap5.client.ui`:

```java
TextBox name = new TextBox();
name.setPlaceholder("Your name");
Button greet = new Button("Greet");
Label result = new Label("Ready");
greet.addClickHandler(event -> result.setText("Hello " + name.getValue()));
RootPanel.get().add(name);
RootPanel.get().add(greet);
RootPanel.get().add(result);
```

To make the form responsive, import `Container`, `Row` and `Column` from the same
package and replace the three root additions with a column:

```java
Container container = new Container();
Row row = new Row();
Column column = new Column(12);
column.setMediumSpan(6);
column.add(name);
column.add(greet);
column.add(result);
row.add(column);
container.add(row);
RootPanel.get().add(container);
```

## Use the Bootstrap 3 API

The Bootstrap 3 track keeps the familiar widget packages and composition
model. Its TeaVM variant also supports the shared Select, Slider, ToggleSwitch
and Summernote examples. The
[TeaVM Bootstrap 3 guide](teavm/teavm-bootstrap3/README.md) shows its
initialisation, themes and integrations.

Both TeaVM tracks use shared compatibility libraries for GWT client APIs,
UiBinder and browser resources. The compatibility layer covers the APIs these
widgets use; the [compatibility library](https://github.com/instanto-io/teavm-compat)
documents its supported surface separately.

## Added work beyond the original GwtBootstrap3 portfolio

This fork goes beyond the original GwtBootstrap3 widget catalogue with the
Bootstrap 5-native widget set, TeaVM compatibility layers and showcase work,
plus new extras such as FullCalendar and the bundled asset inventory used by the
modern browser builds. Those additions are part of this project’s independent
distribution rather than a direct re-export of the upstream portfolio.

## FullCalendar

The Bootstrap 5 extras now include `io.instanto.bootstrap5.extras.fullcalendar.FullCalendar`.
It wraps FullCalendar **7.1.3** and its official **Bootstrap 5 theme plugin**, using
one Java widget implementation for GWT and TeaVM. Both upstream distributions are
MIT licensed; pinned assets and license files are recorded in
[THIRD-PARTY-ASSETS.md](THIRD-PARTY-ASSETS.md).

Inherit the module in a GWT application:

```xml
<inherits name="io.instanto.bootstrap5.extras.fullcalendar.FullCalendar"/>
```

Configure `io.instanto.bootstrap5.extras.fullcalendar.client.ui.FullCalendar`
before attaching it. The current Java surface covers initial date/view,
editable events, event addition/removal, navigation, date selection and event
click/change callbacks. Its native calendar is destroyed on detach and its
current view/date/events are restored on reattachment. Callbacks report ISO date
strings, preserving all-day date-only values. This is a focused wrapper, not a
binding for every FullCalendar setting or its separately licensed premium views.

Open **Integrations → FullCalendar** in either Bootstrap 5 showcase. The example
includes month/week/day/list views, appointments, selection, drag/resize,
Java callbacks and a detach/reattach control.

The Bootstrap 3 GWT API remains on FullCalendar **3.10.5**. Its TeaVM showcase now
has a live **Extras → FullCalendar** page using that same runtime and Moment
2.31.0. This is a scoped TeaVM showcase bridge; the entire legacy GWT3 FullCalendar
API has not been ported. The Bootstrap 5 wrapper is a separate modern API, not a
drop-in upgrade of the jQuery-based Bootstrap 3 wrapper.

## Upstream credits

The original Java widgets, extras and showcase are the work of the
[GwtBootstrap3 contributors](https://github.com/gwtbootstrap3/gwtbootstrap3),
including the [extras](https://github.com/gwtbootstrap3/gwtbootstrap3-extras) and
[demo](https://github.com/gwtbootstrap3/gwtbootstrap3-demo) projects. Our Bootstrap 5
widgets are also derived from that work. The browser components and styles come
from [Bootstrap](https://github.com/twbs/bootstrap); the Bootstrap 3 libraries also
use [jQuery](https://github.com/jquery/jquery).

[GWT](https://github.com/gwtproject/gwt) provides the original Java browser APIs,
and [TeaVM](https://github.com/konsoletyper/teavm), by Alexey Andreev and its
contributors, makes the TeaVM builds possible. Instanto maintains the adaptations,
compatibility integration and build tooling. This is an independent distribution.

See [NOTICE](NOTICE) for source attribution and modifications, [LICENSE](LICENSE)
for the Java licence, and the [asset inventory](THIRD-PARTY-ASSETS.md) for bundled
plugins, themes, fonts and icons, which retain their own licences.

## Support the projects

Like Bootstrap? Please [support the Bootstrap team](https://github.com/sponsors/twbs).
GwtBootstrap3 is a separate, archived project; the Bootstrap sponsorship supports
the underlying browser framework.

Using the TeaVM build? Please [support TeaVM](https://github.com/sponsors/konsoletyper).

Want to see this port and more TeaVM libraries maintained? Please [sponsor this port](https://github.com/sponsors/instanto-io).
