/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import io.instanto.bootstrap5.client.ui.html.Div;

public class ToastHeader extends Div implements HasText, IsClosable {
    private final InlineLabel title = new InlineLabel();
    private final Button close = new Button();

    public ToastHeader() {
        addStyleName("toast-header");
        title.setStyleName("fw-bold me-auto");
        close.setStyleName("btn-close");
        close.getElement().setAttribute("aria-label", "Close");
        close.getElement().setAttribute("data-bs-dismiss", "toast");
        add(title);
        add(close);
    }

    public ToastHeader(String text) { this(); setText(text); }
    @Override public void setText(String text) { title.setText(text); }
    @Override public String getText() { return title.getText(); }
    @Override public void setClosable(boolean closable) { close.setVisible(closable); }
    @Override public boolean isClosable() { return close.isVisible(); }
}
