package io.instanto.bootstrap5.teavm.demo;

import io.instanto.bootstrap5.showcase.client.SetupView;

/** Consumer instructions for the TeaVM build; demonstrations remain shared. */
public final class TeaVmSetupView extends SetupView {
    public TeaVmSetupView() {
        super(new String[][] {
            {"Maven", "Use the TeaVM artifact. Do not also add gwt-user.",
                "<dependency>\n  <groupId>io.instanto</groupId>\n  <artifactId>teavm-bootstrap5</artifactId>\n  <version>1.0-SNAPSHOT</version>\n</dependency>"},
            {"Application entry point", "Create and attach widgets in the ready callback.",
                "public static void main(String[] args) {\n    Bootstrap5.initialise(() -> {\n        RootPanel.get().add(new Button(\"Save\"));\n    });\n}"},
            {"Bundled assets", "Publish the JAR's META-INF/bootstrap5-assets/ contents alongside your application, preserving directories. Initialisation loads the bundled CSS and scripts; do not add individual plugin script tags.",
                "// If assets are published at a different location, set this before initialisation:\nBootstrap5Resources.setAssetBase(\"assets/bootstrap5/\");"},
            {"UiBinder support", "Keep Owner.ui.xml beside its Java owner. Enable widget-processor in Maven's annotationProcessorPaths and include the templates as resources. It generates the binder and its factory during javac; GWT.create(Binder.class) uses that factory on TeaVM. The example below uses the same template and handlers.",
                "<annotationProcessorPaths>\n  <path>\n    <groupId>io.instanto</groupId>\n    <artifactId>widget-processor</artifactId>\n    <version>1.0-SNAPSHOT</version>\n  </path>\n</annotationProcessorPaths>"}
        });
    }
}
