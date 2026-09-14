/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.HasText;
import io.instanto.bootstrap5.client.ui.html.Div;

public class OffcanvasHeader extends Div implements HasText, IsClosable {
    private final Heading title = new Heading(2);
    private final Button close = new Button();

    public OffcanvasHeader() {
        addStyleName("offcanvas-header");
        title.setStyleName("offcanvas-title h5");
        title.getElement().setId(Document.get().createUniqueId());
        close.setStyleName("btn-close");
        close.getElement().setAttribute("aria-label", "Close");
        close.getElement().setAttribute("data-bs-dismiss", "offcanvas");
        add(title);
        add(close);
    }

    public OffcanvasHeader(String text) { this(); setText(text); }
    public String getTitleId() { return title.getElement().getId(); }
    @Override public void setText(String text) { title.setText(text); }
    @Override public String getText() { return title.getText(); }
    @Override public void setClosable(boolean closable) { close.setVisible(closable); }
    @Override public boolean isClosable() { return close.isVisible(); }
}
