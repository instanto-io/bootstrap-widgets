# TeaVM Bootstrap 5

Bootstrap 5 widgets for a Java web application compiled with TeaVM.

You write ordinary GWT widget code. TeaVM compiles it to JavaScript instead of the GWT
compiler. Nothing about your code has to know the difference.

## 1. Add the dependency

```xml
<dependency>
  <groupId>io.instanto</groupId>
  <artifactId>teavm-bootstrap5</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

Do not also add `gwt-user`. This library brings its own implementation of the GWT
classes it needs, and having both gives you two of everything.

## 2. Write your main

```java
public final class MyApp {
    public static void main(String[] args) {
        Bootstrap5.initialise(() -> {
            Button save = new Button("Save");
            save.setType(ButtonType.PRIMARY);
            save.addClickHandler(event -> Window.alert("Saved"));
            RootPanel.get().add(save);
        });
    }
}
```

Two lines matter.

`Bootstrap5.initialise(ready)` loads the library's stylesheets and Bootstrap's own
JavaScript through the generated module loader. Build your widgets in the callback.
Repeated calls share resource loading; each callback runs when the scripts are usable.

`RootPanel.get().add(...)` puts a widget on the page. `RootPanel.get()` is the
document body. If your widgets belong inside an element that is already there, name
it instead:

```java
RootPanel.get("editor").add(new Container());
```

## 3. Publish the bundled assets

Publish the library JAR's `META-INF/bootstrap5-assets/` tree alongside your application,
preserving its directories. For a different location, set
`Bootstrap5Resources.setAssetBase("assets/bootstrap5/")` before initialisation.
No per-script HTML declarations are needed:

```html
<body>
  <script src="your-app.js"></script>
</body>
```

If the host already supplies the compatible Bootstrap runtime, it is reused. The
no-argument `initialise()` starts loading but does not wait; use the callback before
constructing widgets that depend on the scripts.

That is the whole setup. Everything below is optional.

## Choosing a theme

A default theme is applied for you, so widgets look right without any of this. To
offer alternatives:

```java
Themes.register(StandardThemes.all());
Themes.register(BootswatchThemes.all());
Themes.restore();
```

Twenty-six Bootswatch themes ship in this artifact. If your page already declares a
`<link id="bootstrap5-theme">`, that link is used and switching replaces its `href`,
so a server-rendered starting theme survives startup without a flash.

## The richer widgets

The integrations include a slider, date picker, rich text editor and Markdown editor:

```java
Slider slider = new Slider(0, 100);
slider.setValue(25);
RootPanel.get().add(slider);
```

Each fetches its JavaScript library when first used and builds itself when it arrives,
so there is nothing to declare on the page. If a library cannot be loaded, the console
says which module and which file, rather than the widget quietly staying blank.

## One thing that will catch you out

Add widgets through `RootPanel`. Do not append their elements yourself:

```java
someElement.appendChild(widget.getElement());   // don't
```

That puts the markup on the page but never tells the widget it was attached. Its
`onLoad` never runs, and that is where a tooltip binds to its element and a slider
builds itself. The widget looks right and does nothing.

## UiBinder templates

If you prefer markup to Java for laying out a screen, UiBinder templates work here.
Keep `Owner.ui.xml` beside its Java owner and use `UiBinder`, `@UiField`,
`@UiHandler` and `GWT.create(Binder.class)` as in the shared showcase.
The `widget-processor` annotation processor generates Java and a service descriptor
during compilation; the compatibility layer uses these to construct the binder.
The showcase already configures this; a separate application needs the processor too.

See [UiBinder setup](../UIBINDER.md) for the Maven configuration, template packaging
and current limits. The shared [UiBinderDemo.java](../../gwt/gwt-bootstrap5-showcase/src/main/java/io/instanto/bootstrap5/showcase/client/UiBinderDemo.java)
and [UiBinderDemo.ui.xml](../../gwt/gwt-bootstrap5-showcase/src/main/java/io/instanto/bootstrap5/showcase/client/UiBinderDemo.ui.xml)
demonstrate field binding and click handlers. Attach the resulting view through
`RootPanel` inside the `Bootstrap5.initialise(...)` callback.

## Try it

The [TeaVM showcase](https://cstainton.github.io/bootstrap-widgets/teavm-bootstrap5.html)
is this library running in a browser, beside the
[GWT showcase](https://cstainton.github.io/bootstrap-widgets/bootstrap5/) built from
the same source. Comparing the two is the point: where they differ, the compatibility
layer is wrong.

If you want to read a working application:

- [`SharedShowcaseApp`](src/main/java/io/instanto/bootstrap5/teavm/demo/SharedShowcaseApp.java)
  is the entry point, and it is a handful of lines.
- [`ShowcaseEntryPoint`](../../gwt/gwt-bootstrap5-showcase/src/main/java/io/instanto/bootstrap5/showcase/client/ShowcaseEntryPoint.java)
  is the showcase itself — around three thousand lines of ordinary widget code,
  compiled unchanged by both compilers.

## How it works, if you are curious

This module compiles the same source as
[`gwt-bootstrap5`](../../gwt/gwt-bootstrap5), its themes and its extras, against
[`teavm-gwt-compat`](../teavm-gwt-compat), which reimplements the parts of
`com.google.gwt.*` the widgets use.

`initialise()` exists because GWT has a module system and TeaVM does not. In a GWT
application, a `.gwt.xml` file declares the stylesheets a module needs and the
generated bootstrap injects them before your code runs. Nothing does that here, so one
call stands in for it.
