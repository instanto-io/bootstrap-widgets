/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui;

import io.instanto.bootstrap5.client.ui.base.AbstractVisibilityWidget;

/** A Bootstrap toast. Compose with ToastHeader and ToastBody; setters retain void contracts. */
public class Toast extends AbstractVisibilityWidget {
    private boolean autoHide = true;
    private boolean animation = true;
    private int delay = 5000;

    public Toast() {
        super("Toast", "toast", "toast");
        getElement().setAttribute("role", "status");
        getElement().setAttribute("aria-atomic", "true");
    }

    public Toast(String title, String message) {
        this();
        add(new ToastHeader(title));
        add(new ToastBody(message));
    }

    public void setAutoHide(boolean autoHide) {
        setOption("autohide", String.valueOf(autoHide));
        this.autoHide = autoHide;
    }

    public boolean isAutoHide() { return autoHide; }

    public void setAnimation(boolean animation) {
        setOption("animation", String.valueOf(animation));
        this.animation = animation;
    }

    public boolean isAnimation() { return animation; }

    public void setDelay(int delay) {
        if (delay < 0) throw new IllegalArgumentException("Delay must not be negative");
        setOption("delay", String.valueOf(delay));
        this.delay = delay;
    }

    public int getDelay() { return delay; }
}
