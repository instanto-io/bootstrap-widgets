package io.instanto.bootstrap5.showcase.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import io.instanto.bootstrap5.client.ui.Heading;
import io.instanto.bootstrap5.client.ui.Panel;
import io.instanto.bootstrap5.client.ui.PanelBody;
import io.instanto.bootstrap5.client.ui.PanelFooter;
import io.instanto.bootstrap5.client.ui.PanelHeader;
import io.instanto.bootstrap5.client.ui.Pre;

/** Setup instructions supplied by the GWT showcase. */
public class SetupView extends FlowPanel {
    public SetupView() {
        this(new String[][] {
            {"Maven", "Add the widget library to your GWT application.",
                "<dependency>\n  <groupId>io.instanto</groupId>\n  <artifactId>gwt-bootstrap5</artifactId>\n  <version>1.0-SNAPSHOT</version>\n</dependency>"},
            {"GWT Module", "Inherit the widget module in your application's .gwt.xml file.",
                "<inherits name=\"io.instanto.bootstrap5.GwtBootstrap5\"/>"}
        });
    }

    protected SetupView(String[][] sections) {
        for (String[] section : sections) {
            addSection(section[0], section[1], section[2]);
        }
    }

    protected final void addSection(String title, String description, String code) {
        Panel panel = new Panel();
        panel.addStyleName("mb-4");
        PanelHeader header = new PanelHeader();
        header.add(new Heading(3, title));
        PanelBody body = new PanelBody();
        body.add(new Label(description));
        PanelFooter footer = new PanelFooter();
        Pre pre = new Pre(code);
        pre.addStyleName("mb-0 small");
        footer.add(pre);
        panel.add(header);
        panel.add(body);
        panel.add(footer);
        add(panel);
    }
}
