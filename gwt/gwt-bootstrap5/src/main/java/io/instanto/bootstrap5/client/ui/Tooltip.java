package io.instanto.bootstrap5.client.ui;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2015 GwtBootstrap3
 * %%
 * Modified for the Bootstrap 5 track of GWT Bootstrap.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * #L%
 */

import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.base.AbstractTooltip;

/** Bootstrap 5 tooltip preserving the GwtBootstrap3 controller API. */
public class Tooltip extends AbstractTooltip {

    public Tooltip() {
        super("tooltip");
    }

    public Tooltip(String title) {
        this();
        setTitle(title);
    }

    public Tooltip(Widget widget) {
        this();
        setWidget(widget);
    }

    public Tooltip(Widget widget, String title) {
        this(widget);
        setTitle(title);
    }

    @Override
    public void init() {
        initializePlugin(null);
    }

    @Override
    protected void call(String method) {
        invokePlugin(method);
    }

    @Override
    protected String getBootstrapPluginName() {
        return "Tooltip";
    }
}
