/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.showcase.client;

import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.*;
import io.instanto.bootstrap5.client.ui.constants.OffcanvasPlacement;
import io.instanto.bootstrap5.client.ui.constants.PlaceholderSize;
import io.instanto.bootstrap5.client.ui.html.Div;

/** Shared examples: the GWT and TeaVM showcases compile these same widgets. */
final class NativeComponentsExamples {
    private NativeComponentsExamples() { }

    static Widget toasts() {
        Div demo = new Div();
        Div controls = new Div();
        controls.setStyleName("d-flex flex-wrap gap-2 mb-3");
        ToastContainer notifications = new ToastContainer();
        notifications.setId("native-notifications");
        notifications.addStyleName("position-static mt-3");
        Toast reusable = new Toast("Notification", "This toast stays until you dismiss it.");
        reusable.setAutoHide(false);
        notifications.add(reusable);
        InlineLabel status = new InlineLabel("No toast events yet.");
        reusable.addShownHandler(event -> status.setText("Toast shown"));
        reusable.addHiddenHandler(event -> status.setText("Toast hidden"));
        Button show = new Button("Show notification", Variant.PRIMARY);
        show.addClickHandler(event -> reusable.show());
        Button hide = new Button("Hide notification", Variant.SECONDARY);
        hide.addClickHandler(event -> reusable.hide());
        Button add = new Button("Add timed notification", Variant.SUCCESS);
        final int[] count = {0};
        add.addClickHandler(event -> notifications.notify("Saved " + ++count[0],
                "Each notification closes independently after five seconds.", 5000));
        controls.add(show);
        controls.add(hide);
        controls.add(add);
        demo.add(controls);
        demo.add(status);
        demo.add(notifications);
        return demo;
    }

    static Widget offcanvas() {
        Div demo = new Div();
        Div controls = new Div();
        controls.setStyleName("d-flex flex-wrap gap-2 mb-3");
        InlineLabel status = new InlineLabel("Choose a panel. Escape, the backdrop or Close dismisses it.");
        for (OffcanvasPlacement placement : OffcanvasPlacement.values()) {
            Offcanvas drawer = new Offcanvas();
            drawer.setId("native-offcanvas-" + placement.name().toLowerCase());
            drawer.setPlacement(placement);
            drawer.add(new OffcanvasHeader("Details: " + placement.name().toLowerCase()));
            OffcanvasBody body = new OffcanvasBody();
            body.add(new Paragraph("This panel uses Bootstrap's backdrop, focus management and keyboard behaviour."));
            TextBox search = new TextBox();
            search.setPlaceholder("Filter records");
            search.getElement().setAttribute("aria-label", "Filter records");
            body.add(search);
            drawer.add(body);
            drawer.addShownHandler(event -> status.setText("Panel shown: " + placement.name().toLowerCase()));
            drawer.addHiddenHandler(event -> status.setText("Panel hidden; focus returned to the opening button."));
            Button open = new Button("Open " + placement.name().toLowerCase(), Variant.PRIMARY);
            open.addClickHandler(event -> drawer.show());
            controls.add(open);
            demo.add(drawer);
        }
        Offcanvas scrolling = new Offcanvas();
        scrolling.setScroll(true);
        scrolling.setBackdrop(false);
        scrolling.add(new OffcanvasHeader("Page scrolling allowed"));
        scrolling.add(new OffcanvasBody("No backdrop. Scroll the page or close this panel with Escape."));
        Button openScrolling = new Button("Without backdrop", Variant.SECONDARY);
        openScrolling.addClickHandler(event -> scrolling.show());
        controls.add(openScrolling);
        demo.add(scrolling);
        demo.add(controls);
        demo.add(status);
        return demo;
    }

    static Widget placeholders() {
        Div demo = new Div();
        demo.setStyleName("row g-3");
        for (PlaceholderContainer.Animation animation : PlaceholderContainer.Animation.values()) {
            Div column = new Div();
            column.setStyleName("col-12 col-md-4");
            column.add(new Heading(3, animation.name().toLowerCase()));
            PlaceholderContainer skeleton = new PlaceholderContainer();
            skeleton.setAnimation(animation);
            skeleton.addStyleName("border rounded p-3");
            Placeholder heading = new Placeholder(8);
            heading.setSize(PlaceholderSize.LARGE);
            heading.addStyleName("mb-3");
            skeleton.add(heading);
            for (int width : new int[] {12, 10, 7}) {
                Placeholder line = new Placeholder(width);
                line.addStyleName("mb-2");
                skeleton.add(line);
            }
            column.add(skeleton);
            column.add(new Paragraph("Loading content..."));
            demo.add(column);
        }
        return demo;
    }
}
