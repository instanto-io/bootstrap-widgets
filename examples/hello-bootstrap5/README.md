# Hello Bootstrap 5 on TeaVM

This is a complete TeaVM application with Java code, an HTML page and a Maven
build. It uses installed library JARs and can be built on its own.

Use JDK 21 and Maven. First build and install Bootstrap Widgets and its shared
compatibility libraries, or set up access to their Maven repositories.
From this directory:

```sh
mvn clean package
jwebserver -b 127.0.0.1 -p 8080 -d target/site
```

Open [the application](http://127.0.0.1:8080/), enter a name and press **Greet**.
The build copies the widget library's CSS, fonts and scripts into
`target/site/assets/bootstrap5/`. `Bootstrap5.initialise` loads them before
showing the form.

Install `gwt-resources-compat-maven-plugin:0.1.0-SNAPSHOT` from
[teavm-compat](https://github.com/instanto-io/teavm-compat/tree/main/gwt-resources-compat-maven-plugin)
and use current widget JARs. The widget library
includes a file named `META-INF/teavm-assets.properties` in its JAR when it is
built. In Bootstrap 5, that file contains:

```properties
source=META-INF/bootstrap5-assets
target=assets/bootstrap5
```

When you build this application, `stage-assets` reads the file and copies the
contents of the JAR's `source` folder into `target/site/assets/bootstrap5/`.
The library supplies these instructions; you do not need to create the file.
Older widget JARs need rebuilding to include it.

To put the whole application in another folder served by your web server, run:

```sh
mvn package -Dsite.directory=/path/to/webroot
```

This puts the compiled JavaScript, HTML page and library files in that folder.
`Bootstrap5Resources.setAssetBase("assets/bootstrap5/")` tells the browser to
find the library files beside the HTML page. For example, an application at
`https://example.com/my-app/` loads them from
`https://example.com/my-app/assets/bootstrap5/`. If you serve the files elsewhere,
set their browser address before calling `Bootstrap5.initialise`.

Your deployment process can then upload the complete folder or make it available
through your web server. Maven's `deploy` command publishes Java packages to a
Maven repository; `package` is enough to prepare this application.

Copy this example to start your own application. If you rename the Java entry
point, update TeaVM's `mainClass` setting in the POM too.
