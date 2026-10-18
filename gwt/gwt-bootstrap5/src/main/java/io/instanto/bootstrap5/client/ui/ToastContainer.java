/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui;

import io.instanto.bootstrap5.client.ui.html.Div;

/**
 * Mount this live region before announcing notifications. Ordinary add() also accepts reusable
 * toasts.
 */
public class ToastContainer extends Div {
  public ToastContainer() {
    io.instanto.bootstrap5.client.ui.base.helper.StyleHelper.addStyleNames(
        super.getElement(), "toast-container");
    super.getElement().setAttribute("aria-live", "polite");
    super.getElement().setAttribute("aria-atomic", "false");
    super.getElement().setAttribute("aria-relevant", "additions text");
  }

  /** Adds an independently timed notification, removing it after it is dismissed. */
  public Toast notify(String title, String message, int delay) {
    if (!isAttached()) throw new IllegalStateException("Mount the notification region first");
    Toast toast = new Toast(title, message);
    toast.setDelay(delay);
    toast.addHiddenHandler(event -> toast.removeFromParent());
    add(toast);
    toast.show();
    return toast;
  }
}
