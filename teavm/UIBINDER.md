# UiBinder with TeaVM

Both TeaVM widget libraries support UiBinder templates through `widget-processor`.
It turns templates into Java during `javac`; TeaVM compiles that Java along with
your application. No browser-side XML parser or GWT compiler is needed.

## Configure the application build

Use the TeaVM widget dependency, not `gwt-user`. Add the processor to your existing
Maven compiler configuration:

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <configuration>
    <annotationProcessorPaths>
      <path>
        <groupId>io.instanto</groupId>
        <artifactId>widget-processor</artifactId>
        <version>1.0-SNAPSHOT</version>
      </path>
    </annotationProcessorPaths>
  </configuration>
</plugin>
```

Keep other processors if your application already uses them. Under `<build>`,
include templates as resources so the processor can find them before compilation:

```xml
<resources>
  <resource>
    <directory>src/main/resources</directory>
  </resource>
  <resource>
    <directory>src/main/java</directory>
    <includes>
      <include>**/*.ui.xml</include>
    </includes>
  </resource>
</resources>
```

Keep resources referenced by templates available too. Preserve generated
`META-INF/services` entries when packaging or shading the application: the
compatibility layer's `GWT.create` uses them to find the generated binder.

## Write the view

Put `ExampleView.java` and `ExampleView.ui.xml` in the same package. Declare a nested
interface named `Binder` extending `UiBinder<Widget, ExampleView>`, obtain it with
`GWT.create(Binder.class)` and pass `binder.createAndBindUi(this)` to the composite's
`initWidget`. Use `ui:field` and `@UiField` to bind widgets, and `@UiHandler` to
connect their events.

Working examples compiled by the showcases:

- Bootstrap 3: [ButtonsView.java](../gwt/gwt-bootstrap3-showcase/src/main/java/org/gwtbootstrap3/demo/client/application/css/ButtonsView.java)
  and [ButtonsView.ui.xml](../gwt/gwt-bootstrap3-showcase/src/main/java/org/gwtbootstrap3/demo/client/application/css/ButtonsView.ui.xml).
- Bootstrap 5: [UiBinderDemo.java](../gwt/gwt-bootstrap5-showcase/src/main/java/io/instanto/bootstrap5/showcase/client/UiBinderDemo.java)
  and [UiBinderDemo.ui.xml](../gwt/gwt-bootstrap5-showcase/src/main/java/io/instanto/bootstrap5/showcase/client/UiBinderDemo.ui.xml).

Create and attach the view through `RootPanel` inside the library's
`Bootstrap3.initialise(...)` or `Bootstrap5.initialise(...)` callback. UiBinder
constructs widgets; it does not replace resource loading or widget attachment.
The libraries bundle their stylesheets and scripts as described in their READMEs.

## Current limits

The processor supports the template features used by the shared showcases, not
every GWT UiBinder feature. It supports widget composition, setter attributes,
enum values, fields, handlers, constructors, `ui:style` and `ui:with`.
Elements such as `ui:msg`, `ui:image`, `ui:data` and `ui:attribute` are not supported
and produce a compile-time error. Follow the `Owner.ui.xml` naming convention;
do not assume alternate template names work.

If compilation warns that no template was found, check its package and resource
configuration. That warning leaves no generated binder for TeaVM to use. A clean
build should produce both `Owner_BinderImpl.java` and the corresponding
`META-INF/services/...Owner$Binder` descriptor.
