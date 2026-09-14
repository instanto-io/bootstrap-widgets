/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui;

import io.instanto.bootstrap5.client.ui.base.AbstractVisibilityWidget;
import io.instanto.bootstrap5.client.ui.base.BootstrapComponent;
import io.instanto.bootstrap5.client.ui.constants.OffcanvasPlacement;
import com.google.gwt.user.client.ui.Widget;

/** Bootstrap's sliding dialog, composed from OffcanvasHeader and OffcanvasBody. */
public class Offcanvas extends AbstractVisibilityWidget {
    private OffcanvasPlacement placement = OffcanvasPlacement.START;
    private boolean keyboard = true;
    private boolean scroll;
    private boolean backdrop = true;

    public Offcanvas() {
        super("Offcanvas", "offcanvas", "offcanvas");
        addStyleName(placement.getCssName());
        getElement().setAttribute("tabindex", "-1");
        getElement().setAttribute("aria-label", "Details");
    }

    @Override
    public void add(Widget child) {
        super.add(child);
        if (child instanceof OffcanvasHeader) {
            getElement().setAttribute("aria-labelledby", ((OffcanvasHeader) child).getTitleId());
            getElement().removeAttribute("aria-label");
        }
    }

    @Override
    public void show() {
        super.show();
        if (isShown()) BootstrapComponent.rememberFocus(getElement());
    }

    public void setPlacement(OffcanvasPlacement placement) {
        if (placement == null) throw new IllegalArgumentException("Placement is required");
        if (isShown()) throw new IllegalStateException("Hide the panel before changing placement");
        removeStyleName(this.placement.getCssName());
        this.placement = placement;
        addStyleName(placement.getCssName());
    }

    public OffcanvasPlacement getPlacement() { return placement; }
    public void setKeyboard(boolean keyboard) { setOption("keyboard", String.valueOf(keyboard)); this.keyboard = keyboard; }
    public boolean isKeyboard() { return keyboard; }
    public void setScroll(boolean scroll) { setOption("scroll", String.valueOf(scroll)); this.scroll = scroll; }
    public boolean isScroll() { return scroll; }
    public void setBackdrop(boolean backdrop) { setOption("backdrop", String.valueOf(backdrop)); this.backdrop = backdrop; }
    public boolean isBackdrop() { return backdrop; }
}
