package org.gwtbootstrap3.teavm.demo;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.PageHeader;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Pre;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;

/** Consumer instructions for the TeaVM build; demonstrations remain shared. */
public final class TeaVmSetupView extends FlowPanel {
    public TeaVmSetupView() {
        PageHeader title = new PageHeader();
        title.setText("Project Setup");
        add(title);
        section("Maven", "Use the TeaVM artifact. Do not also add gwt-user.",
                "<dependency>\n  <groupId>io.instanto</groupId>\n  <artifactId>teavm-bootstrap3</artifactId>\n  <version>1.0-SNAPSHOT</version>\n</dependency>");
        section("Application entry point", "Create and attach widgets in the ready callback.",
                "public static void main(String[] args) {\n    Bootstrap3.initialise(() -> {\n        RootPanel.get().add(new Button(\"Save\"));\n    });\n}");
        section("Bundled assets", "Publish the JAR's META-INF/bootstrap3-assets/ contents alongside your application, preserving the css, js and fonts directories. Initialisation loads the bundled CSS, jQuery and Bootstrap in order; no individual script tags are needed.",
                "// If assets are published at a different location, set this before initialisation:\nBootstrap3Resources.setBase(\"assets/bootstrap3/css/\");");
        section("UiBinder support", "Keep Owner.ui.xml beside its Java owner. Enable widget-processor in Maven's annotationProcessorPaths and include the templates as resources. It generates the binder and its factory during javac; GWT.create(Binder.class) uses that factory on TeaVM. The showcase pages use these shared templates.",
                "<annotationProcessorPaths>\n  <path>\n    <groupId>io.instanto</groupId>\n    <artifactId>widget-processor</artifactId>\n    <version>1.0-SNAPSHOT</version>\n  </path>\n</annotationProcessorPaths>");
        section("Themes", "Register themes and restore the user's choice after initialisation.",
                "Themes.register(StandardThemes.all());\nThemes.register(BootswatchThemes.all());\nThemes.restore();");
    }

    private void section(String title, String description, String code) {
        Panel panel = new Panel();
        PanelHeader header = new PanelHeader();
        header.add(new Heading(HeadingSize.H3, title));
        PanelBody body = new PanelBody();
        body.add(new Label(description));
        Pre pre = new Pre();
        pre.setText(code);
        body.add(pre);
        panel.add(header);
        panel.add(body);
        add(panel);
    }
}
