/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui.base;

import com.google.gwt.event.shared.HandlerRegistration;
import io.instanto.bootstrap5.client.shared.event.HiddenEvent;
import io.instanto.bootstrap5.client.shared.event.HiddenHandler;
import io.instanto.bootstrap5.client.shared.event.HideEvent;
import io.instanto.bootstrap5.client.shared.event.HideHandler;
import io.instanto.bootstrap5.client.shared.event.ShowEvent;
import io.instanto.bootstrap5.client.shared.event.ShowHandler;
import io.instanto.bootstrap5.client.shared.event.ShownEvent;
import io.instanto.bootstrap5.client.shared.event.ShownHandler;
import io.instanto.bootstrap5.client.ui.html.Div;

/** Composable Bootstrap components with the same lifecycle events as Collapse. */
public abstract class AbstractVisibilityWidget extends Div {
    private final String component;
    private final String eventSuffix;

    protected AbstractVisibilityWidget(String component, String eventSuffix, String style) {
        this.component = component;
        this.eventSuffix = eventSuffix;
        addStyleName(style);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        BootstrapEventBridge.bind(getElement(), "show.bs." + eventSuffix,
                event -> fireEvent(new ShowEvent(event)));
        BootstrapEventBridge.bind(getElement(), "shown.bs." + eventSuffix,
                event -> fireEvent(new ShownEvent(event)));
        BootstrapEventBridge.bind(getElement(), "hide.bs." + eventSuffix,
                event -> fireEvent(new HideEvent(event)));
        BootstrapEventBridge.bind(getElement(), "hidden.bs." + eventSuffix,
                event -> fireEvent(new HiddenEvent(event)));
    }

    @Override
    protected void onUnload() {
        BootstrapEventBridge.unbindAll(getElement());
        BootstrapComponent.releaseVisibility(getElement(), component);
        super.onUnload();
    }

    /** Mount through a panel before showing, so event handlers and resources are ready. */
    public void show() {
        if (!isAttached()) {
            throw new IllegalStateException("Attach the widget before showing it");
        }
        if (!BootstrapComponent.isLoaded()) {
            throw new IllegalStateException("Bootstrap resources have not finished initialising");
        }
        BootstrapComponent.call(getElement(), component, "show");
    }

    public void hide() {
        if (isAttached()) BootstrapComponent.call(getElement(), component, "hide");
    }

    public void toggle() {
        if (isShown()) hide(); else show();
    }

    public boolean isShown() {
        return hasStyle("show") || hasStyle("showing");
    }

    private boolean hasStyle(String name) {
        return (" " + getStyleName() + " ").contains(" " + name + " ");
    }

    /** Bootstrap caches options at construction; changing them during display is unsafe. */
    protected void setOption(String name, String value) {
        if (isShown() || hasStyle("hiding")) {
            throw new IllegalStateException("Hide the widget and wait for hidden before changing options");
        }
        BootstrapComponent.dispose(getElement(), component);
        getElement().setAttribute("data-bs-" + name, value);
    }

    public HandlerRegistration addShowHandler(ShowHandler handler) {
        return addHandler(handler, ShowEvent.getType());
    }

    public HandlerRegistration addShownHandler(ShownHandler handler) {
        return addHandler(handler, ShownEvent.getType());
    }

    public HandlerRegistration addHideHandler(HideHandler handler) {
        return addHandler(handler, HideEvent.getType());
    }

    public HandlerRegistration addHiddenHandler(HiddenHandler handler) {
        return addHandler(handler, HiddenEvent.getType());
    }
}
