/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui;

public class OffcanvasBody extends ElementPanel {
    public OffcanvasBody() { super("div"); addStyleName("offcanvas-body"); }
    public OffcanvasBody(String text) { this(); setText(text); }
}
