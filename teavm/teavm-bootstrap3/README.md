# TeaVM Bootstrap 3

Bootstrap 3 widgets for a Java web application compiled with TeaVM.

You write ordinary GWT widget code. TeaVM compiles it to JavaScript instead of the GWT
compiler. Nothing about your code has to know the difference.

## 1. Add the dependency

```xml
<dependency>
  <groupId>io.instanto</groupId>
  <artifactId>teavm-bootstrap3</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

Do not also add `gwt-user`. This library brings its own implementation of the GWT
classes it needs, and having both gives you two of everything.

## 2. Write your main

```java
public final class MyApp {
    public static void main(String[] args) {
        Bootstrap3.initialise(() -> {
            Button save = new Button("Save");
            save.setType(ButtonType.PRIMARY);
            save.addClickHandler(event -> Window.alert("Saved"));
            RootPanel.get().add(save);
        });
    }
}
```

Two lines matter.

`Bootstrap3.initialise(ready)` adds the library's stylesheets and loads its vendored
jQuery and Bootstrap scripts in order. The callback runs when the scripts are usable;
construct and attach your widgets there. Existing host dependencies are reused.
Repeated calls share the same loading operation and each callback runs once.

`RootPanel.get().add(...)` puts a widget on the page. `RootPanel.get()` is the
document body. If your widgets belong inside an element that is already there, name
it instead:

```java
RootPanel.get("editor").add(new Panel());
```

## 3. Publish the bundled assets

The library JAR contains `META-INF/bootstrap3-assets/`, including CSS, JavaScript,
fonts and source maps. Publish the contents alongside your application, preserving
the `css/`, `js/` and `fonts/` directories. These files come from the GWT module's
public resources and ClientBundle declarations.

```html
<script src="your-app.js"></script>
```

If you publish them under another directory, call
`Bootstrap3Resources.setBase("assets/bootstrap3/css/")` before initialization.
The script directory is derived as `assets/bootstrap3/js/`. A failed script load
is reported on the console and the application callback does not run.

The no-argument `initialise()` starts loading and returns immediately. Use it only
when subsequent code does not require the scripts yet, or the host already loaded them.

That is the whole setup. Everything below is optional.

## Choosing a theme

A default theme is applied for you, so widgets look right without any of this. To
offer alternatives:

```java
Themes.register(StandardThemes.all());
Themes.register(BootswatchThemes.all());
Themes.restore();
```

Sixteen Bootswatch themes ship in this artifact. If your page already declares a
`<link id="gwtbootstrap3-theme">`, that link is used and switching replaces its
`href`, so a server-rendered starting theme survives startup without a flash.

## One thing that will catch you out

Add widgets through `RootPanel`. Do not append their elements yourself:

```java
someElement.appendChild(widget.getElement());   // don't
```

That puts the markup on the page but never tells the widget it was attached. Its
`onLoad` never runs, and that is where a tooltip binds to its element and a dropdown
registers its handlers. The widget looks right and does nothing.

## UiBinder templates

UiBinder is supported on TeaVM through the `widget-processor` annotation processor.
Keep `Owner.ui.xml` beside its Java owner and use `UiBinder`, `@UiField`,
`@UiHandler` and `GWT.create(Binder.class)` as in the shared showcase.
The processor generates Java and a service descriptor during compilation; the
compatibility layer uses these to construct the binder.

See [UiBinder setup](../UIBINDER.md) for the Maven configuration, template packaging
and current limits. The showcase already configures this; a separate application
needs the processor too.

The shared [ButtonsView.java](../../gwt/gwt-bootstrap3-showcase/src/main/java/org/gwtbootstrap3/demo/client/application/css/ButtonsView.java)
and [ButtonsView.ui.xml](../../gwt/gwt-bootstrap3-showcase/src/main/java/org/gwtbootstrap3/demo/client/application/css/ButtonsView.ui.xml)
are a working example. Attach the resulting view through `RootPanel` inside the
`Bootstrap3.initialise(...)` callback, just like a widget built in Java.

## Try it

The [TeaVM showcase](https://cstainton.github.io/bootstrap-widgets/teavm.html) is this
library running in a browser, beside the
[GWT showcase](https://cstainton.github.io/bootstrap-widgets/) built from the same
source. Comparing the two is the point: where they differ, the compatibility layer is
wrong.

If you want to read a working application:

- [`SharedShowcaseApp`](src/main/java/org/gwtbootstrap3/teavm/demo/SharedShowcaseApp.java)
  is the entry point, and it is four lines.
- [`GwtBootstrap3DemoEntryPoint`](../../gwt/gwt-bootstrap3-showcase/src/main/java/org/gwtbootstrap3/demo/client/GwtBootstrap3DemoEntryPoint.java)
  is the showcase itself — ordinary widget code, compiled unchanged by both compilers.

## Extras

The extras port includes Animate, Card, Markdown, ToggleSwitch, Slider, Select and
Summernote. Slider/RangeSlider, Select/MultipleSelect and the editor compile from the
shared GWT widget classes, with TeaVM bridges for native plugin operations. Their
original showcase pages are included.

Bootstrap 3 retains Summernote and jQuery. The Bootstrap 5 tracks use Quill instead.
Summernote supports custom toolbars, HTML operations, read-only mode, hints, locale
selection and image callbacks. The showcase uses a small bundled emoji catalogue
rather than fetching it from GitHub.

These extras currently ship inside teavm-bootstrap3, not a separate extras artifact.
Scripts, styles and locale text resources are bundled in the JAR. Widgets initialize
after their core dependencies, and destroy their plugin instance when detached.

Other extras, including FullCalendar and the date pickers, still need their native
browser calls ported. Bundling their assets alone does not port their widgets.

## How it works, if you are curious

This module compiles the same source as
[`gwt-bootstrap3`](../../gwt/gwt-bootstrap3) against
[`teavm-gwt-compat`](../teavm-gwt-compat), which reimplements the parts of
`com.google.gwt.*` the widgets use.

The module build reads resource declarations from `.gwt.xml` and ClientBundle
sources, bundles those assets, and generates TeaVM module loaders. `initialise()`
starts the core loader; extras use their generated loaders when needed. Applications
do not need to repeat the library's script and stylesheet declarations.
