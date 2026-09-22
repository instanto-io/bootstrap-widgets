# Bootstrap Widgets

Bootstrap Widgets provides Bootstrap 3 and Bootstrap 5 widget libraries for GWT and TeaVM.

The Bootstrap 3 build is a maintained replacement for
[GwtBootstrap3](https://github.com/gwtbootstrap3/gwtbootstrap3). It keeps the original Java packages,
GWT module names, markup, and behaviour while updating the toolchain and browser dependencies. The
Bootstrap 5 build provides a migration path with native Bootstrap 5 markup and JavaScript behaviour.

| Track | Purpose | Java packages |
|---|---|---|
| GWT Bootstrap 3 | Drop-in maintenance build for existing applications | `org.gwtbootstrap3.*` |
| GWT Bootstrap 5 | Bootstrap 5-native migration target | `io.instanto.bootstrap5.*` |
| TeaVM Bootstrap 3 | Bootstrap 3 widgets compiled from the GWT sources | `org.gwtbootstrap3.*` |
| TeaVM Bootstrap 5 | Bootstrap 5 widgets compiled from the GWT sources | `io.instanto.bootstrap5.*` |

Current core versions are GWT 2.13.1, TeaVM 0.15.0, Bootstrap 3.4.1, Bootstrap 5.3.8, and jQuery
3.7.1. Bootstrap 5 does not use jQuery.

## Showcases

- [GWT Bootstrap 3](https://cstainton.github.io/bootstrap-widgets/)
- [GWT Bootstrap 5](https://cstainton.github.io/bootstrap-widgets/bootstrap5/)
- [TeaVM Bootstrap 3](https://cstainton.github.io/bootstrap-widgets/teavm.html)
- [TeaVM Bootstrap 5](https://cstainton.github.io/bootstrap-widgets/teavm-bootstrap5.html)

The GWT and TeaVM showcases use the same widget and showcase sources where possible. This makes
differences between the compilers visible instead of hiding them behind separate demos.

The Bootstrap 5 showcase groups native widgets under **Components** and **Interactive**.
Cards, dialogs, [toasts](https://cstainton.github.io/bootstrap-widgets/bootstrap5/#toasts),
[offcanvas panels](https://cstainton.github.io/bootstrap-widgets/bootstrap5/#offcanvas) and
[loading placeholders](https://cstainton.github.io/bootstrap-widgets/bootstrap5/#placeholders)
belong there. **Integrations** contains third-party editors, date pickers and sliders.
Existing showcase routes and Maven artifact names are unchanged.

## Choosing An Artifact

All artifacts use the `io.instanto` group ID and currently publish as `1.0-SNAPSHOT`.

| Runtime | Bootstrap 3 | Bootstrap 5 |
|---|---|---|
| GWT | `gwt-bootstrap3` | `gwt-bootstrap5` |
| GWT extras | `gwt-bootstrap3-extras` | `gwt-bootstrap5-extras` |
| GWT themes | `gwt-bootstrap3-themes` | `gwt-bootstrap5-themes` |
| TeaVM | `teavm-bootstrap3` | `teavm-bootstrap5` |

Use `gwt-bootstrap3` when updating an existing GwtBootstrap3 application. Only the Maven group ID
and version need to change. Do not put the original GwtBootstrap3 artifact and this replacement on
the same classpath because both contain `org.gwtbootstrap3.*` classes.

Use `gwt-bootstrap5` for new Bootstrap 5 code or when migrating an application. Its API follows the
same widget composition and event-handling model where that still fits Bootstrap 5, but it is not a
drop-in replacement. Templates, styles, and removed Bootstrap 3 concepts may need changes. See
[BOOTSTRAP5-PORTING.md](BOOTSTRAP5-PORTING.md) for current coverage.

TeaVM artifacts have separate names because they use TeaVM libraries and `gwt-user-compat` instead
of `gwt-user`. The build rejects `gwt-user` and `gwt-dev` on TeaVM module classpaths.

## Start a TeaVM application

Use JDK 21, Maven and a browser. The example below chooses Bootstrap 5. For the
Bootstrap 3 API and its jQuery prerequisite, follow the corresponding
[TeaVM Bootstrap 3 guide](teavm/teavm-bootstrap3/README.md).

### 1. Choose the library

```xml
<dependency>
  <groupId>io.instanto</groupId>
  <artifactId>teavm-bootstrap5</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
  <groupId>org.teavm</groupId>
  <artifactId>teavm-classlib</artifactId>
  <version>0.15.0</version>
</dependency>
```

The widget dependency supplies the shared GWT compatibility runtime. Configure
Maven package access as described below, or install the libraries locally first.
The application needs the TeaVM artifacts on its classpath, without `gwt-user`.

### 2. Set up the application

Copy the [standalone Hello Bootstrap example](examples/hello-bootstrap5) for a
complete Maven build, Java entry point and HTML page following the steps below.

Configure `org.teavm:teavm-maven-plugin:0.15.0` with the `compile` goal in
`prepare-package`, `example.HelloBootstrap` as `mainClass`, `target/site` as
`targetDirectory`, and `app.js` as `targetFileName`.

Add this Maven step to copy the widget library's browser files:

The plugin is maintained in
[teavm-compat](https://github.com/instanto-io/teavm-compat/tree/main/gwt-resources-compat-maven-plugin).
Install it from that repository first, or follow its guide to configure Maven
plugin downloads from GitHub Packages.

```xml
<plugin>
  <groupId>io.instanto</groupId>
  <artifactId>gwt-resources-compat-maven-plugin</artifactId>
  <version>0.1.0-SNAPSHOT</version>
  <executions>
    <execution><goals><goal>stage-assets</goal></goals></execution>
  </executions>
  <configuration>
    <outputDirectory>${project.build.directory}/site</outputDirectory>
  </configuration>
</plugin>
```

When you run `mvn package`, this copies the CSS, fonts and scripts into
`assets/bootstrap5/` inside the website folder. The widget library's build puts
`META-INF/teavm-assets.properties` in its JAR. That file names the folder to copy
from (`source`) and the folder to create inside your website (`target`). Your
application build reads these instructions; you do not need to write the file.
The folder structure stays intact so stylesheets can still find their fonts and
images. Use current widget JARs that include this file.

The standalone example uses one setting, `site.directory`, for the compiled
JavaScript, library files and HTML page. To put them in another folder served by
your web server, run `mvn package -Dsite.directory=/path/to/webroot`.
The [file-copying guide](https://github.com/instanto-io/teavm-compat/blob/main/gwt-resources-compat-maven-plugin/README.md#stage-assets-for-an-application)
explains the settings and how to tell the browser where to find the files.

Use Maven's resources plugin to copy this `index.html` into the same folder:

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Hello Bootstrap</title>
</head>
<body>
  <script src="app.js"></script>
  <script>main();</script>
</body>
</html>
```

The Java entry point loads the CSS and scripts before creating widgets:

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

### 3. React to input

Inside the ready callback, replace the single button with this form. Add imports
for `io.instanto.bootstrap5.client.ui.TextBox` and `Label`:

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

### 4. Compose a layout

Import `Container`, `Row` and `Column` from the same widget package. Replace the
three `RootPanel` additions with a responsive column:

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

Build with `mvn clean package`, serve `target/site` with
`jwebserver -b 127.0.0.1 -p 8080 -d target/site`, then open
[your application](http://127.0.0.1:8080/). Add widgets through `RootPanel` or a
parent widget so their setup and cleanup code runs when they are added or removed.

The [Bootstrap 5 usage guide](teavm/teavm-bootstrap5/README.md) continues with
themes, integrations and UiBinder; the
[showcase launcher](teavm/teavm-bootstrap5/src/main/java/io/instanto/bootstrap5/teavm/demo/SharedShowcaseApp.java)
and its [POM](teavm/teavm-bootstrap5/pom.xml) show how the full showcase is built.

## TeaVM Support

The TeaVM builds compile the corresponding GWT widget sources. They do not maintain a second copy of
the widget API. `gwt-user-compat` implements the part of the GWT client API used by those sources and
provides TeaVM-backed DOM, events, widgets, history, scheduling, and resource support.

UiBinder templates are supported through the `widget-processor` annotation processor. It generates
ordinary Java during compilation, including widget construction, fields, handlers, constructors,
enum attributes, and the template features used by the shared showcases.

The shared `gwt-resources-compat-maven-plugin` from `teavm-compat` reads GWT module and ClientBundle declarations and generates TeaVM
resource loaders. Scripts load in declaration order, expose a readiness result, report failures, and
are not loaded again when the host application already provides them.

This compatibility layer covers what these widget libraries currently use; it is not a complete
replacement for all of GWT. TeaVM Bootstrap 3 now includes the shared Select, Slider,
ToggleSwitch and Summernote showcase pages. See [EXTRAS-INVENTORY.md](EXTRAS-INVENTORY.md)
for the remaining integrations. Bootstrap 5 integrations use explicit JavaScript seams that
both compilers can implement.

## Using published snapshots

Add the package repository to the consuming build:

```xml
<repository>
  <id>forgejo</id>
  <url>https://packages.instanto.io/api/packages/instanto-io/maven</url>
  <releases><enabled>false</enabled></releases>
  <snapshots><enabled>true</enabled></snapshots>
</repository>
```

Then add the artifact for the track you want, for example:

```xml
<dependency>
  <groupId>io.instanto</groupId>
  <artifactId>gwt-bootstrap3</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

## Public dependency repositories

Bootstrap Widgets uses `cucumber-tea`, `gherkin-tea` and `mockatcha-dom` as test dependencies.
They are published in GitHub Packages under their companion repositories. Public source
repositories make the code accessible, but do not remove Maven registry authentication.

## Build

CI uses Java 21; the libraries target Java 17 bytecode.

Build and install the complete reactor:

```bash
mvn -DskipTests install
```

Compile either GWT showcase directly:

```bash
mvn -f gwt/gwt-bootstrap3-showcase/pom.xml -DskipTests -Dgwt.forceCompilation=true gwt:compile
mvn -f gwt/gwt-bootstrap5-showcase/pom.xml -DskipTests -Dgwt.forceCompilation=true gwt:compile
```

Compile either TeaVM library and showcase:

```bash
mvn -f teavm/teavm-bootstrap3/pom.xml -DskipTests package
mvn -f teavm/teavm-bootstrap5/pom.xml -DskipTests package
```

Published library artifacts include source JARs. GWT core and extras artifacts also include
Javadoc JARs, which are published with the showcases. The GWT and TeaVM showcase builds publish
JavaScript source maps and the corresponding Java source trees.

## Tests

The Java/Gherkin widget suites currently need the `0.1.0-SNAPSHOT` builds of
`cucumber-tea`, `cucumber-tea-codegen`, `gherkin-tea` and `mockatcha-dom`, either installed
locally or downloaded from their current GitHub Packages repositories. CI uses a token with package read access;
private companion repositories also require access to those repositories.

For CI, store a classic token with `read:packages` scope in the repository's Actions
secret `PACKAGES_READ_TOKEN`. It is used only for the companion Maven registries;
the built-in `GITHUB_TOKEN` remains in use for this repository. Renew the package
token before its expiry. Do not add token values to POMs, workflows or source control.

After installing the reactor, run the tests against its packaged artifacts:

```bash
mvn -pl :gwt-user-jvm-contract-tests,:gwt-bootstrap-widget-tests,:teavm-bootstrap3-tests,:teavm-bootstrap5-tests test
```

Do not add `-am` to this test-only command: during an un-packaged reactor build Maven can
substitute GWT class directories for source-classifier artifacts on TeaVM's classpath.
Use `install` for whole-reactor builds and the command above for subsequent verification.

The test suite includes:

- shared Gherkin behaviour specifications based on the original GwtBootstrap3 showcase;
- API contracts run against both `gwt-user` and `gwt-user-compat`;
- compiled GWT and TeaVM widget fixtures;
- real-browser showcase smoke tests and mobile touch tests.

[TESTING-PLAN.md](TESTING-PLAN.md) describes the test families, tags, coverage rules, and remaining
work.

## Repository Layout

| Path | Contents |
|---|---|
| `gwt/` | Bootstrap 3 and 5 libraries, extras, themes, showcases, fixtures, and GWT tests |
| `teavm/` | GWT compatibility layer, Bootstrap 3 and 5 TeaVM builds, and TeaVM tests |
| `widget-processor/` | UiBinder annotation processor used by TeaVM builds |
| `testing/` | Shared behaviour specifications, fixture identities, and API contracts |
| `showcase-site/` | GitHub Pages assembly for all four showcases |

Third-party browser assets and their versions are listed in
[THIRD-PARTY-ASSETS.md](THIRD-PARTY-ASSETS.md).

## Shared compatibility libraries

TeaVM's GWT client adapter is now maintained and published independently in
[teavm-compat](https://github.com/instanto-io/teavm-compat). This build imports its BOM
and consumes `gwt-user-compat`; portable API assertions come from
`gwt-api`. The old coordinates remain relocation POMs during migration.
Configure Maven server `github-teavm-compat` with package read access. No sibling
checkout is needed for published versions. For an unpublished rename, follow the
[local installation instructions](gwt/gwt-user-jvm-contract-tests/README.md).
Compatibility fixes and shared API assertions belong in that repository;
Bootstrap-specific widget adapters and scenarios remain here.

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

## Shared build parent

For local builds, install the shared parent from a sibling `instanto-poms`
checkout with `mvn -f ../instanto-poms/pom.xml install`. Release instructions
are in `instanto-poms/RELEASING.md`.
