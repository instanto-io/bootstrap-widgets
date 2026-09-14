/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui;

import io.instanto.bootstrap5.client.ui.base.HasSize;
import io.instanto.bootstrap5.client.ui.constants.PlaceholderSize;

/** Decorative loading skeleton. Add meaningful loading status outside the hidden skeleton. */
public class Placeholder extends ElementPanel implements HasSize<PlaceholderSize> {
    private int columns = 12;
    private PlaceholderSize size = PlaceholderSize.DEFAULT;

    public Placeholder() {
        super("span");
        addStyleName("placeholder col-12");
        getElement().setAttribute("aria-hidden", "true");
    }

    public Placeholder(int columns) { this(); setColumns(columns); }

    public void setColumns(int columns) {
        if (columns < 1 || columns > 12) throw new IllegalArgumentException("Columns must be between 1 and 12");
        removeStyleName("col-" + this.columns);
        this.columns = columns;
        addStyleName("col-" + columns);
    }

    public int getColumns() { return columns; }

    @Override public void setSize(PlaceholderSize size) {
        if (size == null) size = PlaceholderSize.DEFAULT;
        if (!this.size.getCssName().isEmpty()) removeStyleName(this.size.getCssName());
        this.size = size;
        if (!size.getCssName().isEmpty()) addStyleName(size.getCssName());
    }

    @Override public PlaceholderSize getSize() { return size; }
}
