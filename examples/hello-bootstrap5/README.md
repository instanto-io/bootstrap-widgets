# Hello Bootstrap 5 on TeaVM

This small application shows a Bootstrap 5 form compiled from Java with TeaVM.
Open [the Java entry point](src/main/java) and
[the host page](src/site) to see how the form starts. The
[TeaVM Bootstrap 5 guide](../../teavm/teavm-bootstrap5/README.md) explains
initialisation, themes and UiBinder.

The widget library carries its CSS, scripts and fonts in its JAR. A file named
`META-INF/teavm-assets.properties` tells the
[resource plugin](https://github.com/instanto-io/teavm-compat/tree/main/gwt-resources-compat-maven-plugin)
where to place them in the website folder. This example serves them from
`assets/bootstrap5/`. The Java entry point uses
`Bootstrap5Resources.setAssetBase("assets/bootstrap5/")` to find them.

Copy this example to begin an application. Keep the generated JavaScript,
host page and asset folder together when serving it.
