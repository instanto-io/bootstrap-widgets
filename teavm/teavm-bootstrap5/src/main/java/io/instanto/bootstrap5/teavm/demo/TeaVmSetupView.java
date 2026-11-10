package io.instanto.bootstrap5.teavm.demo;

import io.instanto.bootstrap5.showcase.client.SetupView;

/** Consumer instructions for the TeaVM build; demonstrations remain shared. */
public final class TeaVmSetupView extends SetupView {
  public TeaVmSetupView() {
    super(
        new String[][] {
          {
            "Maven",
            "Use the TeaVM artifact. Do not also add gwt-user.",
            "<dependency>\n  <groupId>io.instanto</groupId>\n  <artifactId>teavm-bootstrap5</artifactId>\n  <version>1.0-SNAPSHOT</version>\n</dependency>"
          },
          {
            "Application entry point",
            "Create and attach widgets in the ready callback.",
            "public static void main(String[] args) {\n    Bootstrap5.initialise(() -> {\n        RootPanel.get().add(new Button(\"Save\"));\n    });\n}"
          },
          {
            "Bundled assets",
            "Add the gwt-resources-compat-maven-plugin's stage-assets goal: it copies the JAR's assets into your site under assets/bootstrap5/, where initialisation looks for them. Initialisation loads the bundled CSS and scripts; do not add individual plugin script tags.",
            "// Only if the assets are published somewhere else, before initialisation:\nBootstrap5Resources.setAssetBase(\"static/bootstrap5/\");"
          },
          {
            "UiBinder support",
            "Keep Owner.ui.xml beside its Java owner. Enable widget-processor in Maven's annotationProcessorPaths and include the templates as resources. It generates the binder and its factory during javac; GWT.create(Binder.class) uses that factory on TeaVM. The example below uses the same template and handlers.",
            "<annotationProcessorPaths>\n  <path>\n    <groupId>io.instanto</groupId>\n    <artifactId>widget-processor</artifactId>\n    <version>1.0-SNAPSHOT</version>\n  </path>\n</annotationProcessorPaths>"
          }
        });
  }
}
