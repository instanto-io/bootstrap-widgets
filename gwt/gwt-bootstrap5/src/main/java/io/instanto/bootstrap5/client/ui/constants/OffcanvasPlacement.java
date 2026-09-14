/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui.constants;

public enum OffcanvasPlacement implements Type, com.google.gwt.dom.client.Style.HasCssName {
    START("offcanvas-start"), END("offcanvas-end"), TOP("offcanvas-top"), BOTTOM("offcanvas-bottom");
    private final String style;
    OffcanvasPlacement(String style) { this.style = style; }
    @Override public String getCssName() { return style; }
}
