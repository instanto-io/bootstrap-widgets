/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui.constants;

public enum PlaceholderSize implements Size, com.google.gwt.dom.client.Style.HasCssName {
    DEFAULT(""), EXTRA_SMALL("placeholder-xs"), SMALL("placeholder-sm"), LARGE("placeholder-lg");
    private final String style;
    PlaceholderSize(String style) { this.style = style; }
    @Override public String getCssName() { return style; }
}
