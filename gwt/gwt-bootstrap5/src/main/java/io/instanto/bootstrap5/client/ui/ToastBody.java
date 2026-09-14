/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui;

/** A toast body that accepts text, HTML or child widgets like the existing body widgets. */
public class ToastBody extends ElementPanel {
    public ToastBody() { super("div"); addStyleName("toast-body"); }
    public ToastBody(String text) { this(); setText(text); }
}
