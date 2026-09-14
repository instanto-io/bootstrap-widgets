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

/** Bootstrap 5 popover preserving the GwtBootstrap3 controller API. */
public class Popover extends AbstractTooltip {

    private static final String TEMPLATE = "<div class=\"popover\" role=\"tooltip\">"
            + "<div class=\"popover-arrow\"></div><h3 class=\"popover-header\"></h3>"
            + "<div class=\"popover-body\"></div></div>";

    private String content;

    public Popover() {
        super("popover");
        setAlternateTemplate(TEMPLATE);
    }

    public Popover(String title) {
        this();
        setTitle(title);
    }

    public Popover(String title, String content) {
        this(title);
        setContent(content);
    }

    public Popover(Widget widget) {
        this();
        setWidget(widget);
    }

    public Popover(Widget widget, String title, String content) {
        this(widget);
        setTitle(title);
        setContent(content);
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
        reconfigure();
    }

    @Override
    public void init() {
        initializePlugin(getContent());
    }

    @Override
    protected void call(String method) {
        invokePlugin(method);
    }

    @Override
    protected String getBootstrapPluginName() {
        return "Popover";
    }
}
