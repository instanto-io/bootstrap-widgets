package io.instanto.bootstrap5.client.ui;



/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2015 GwtBootstrap3
 * %%
 * Modified for the Bootstrap 5 track of GWT Bootstrap.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * #L%
 */

import com.google.gwt.user.client.ui.InlineLabel;
import io.instanto.bootstrap5.client.ui.constants.IconType;
import io.instanto.bootstrap5.client.ui.constants.Placement;

/** Inline validation help rendered through a Bootstrap 5 tooltip. */
public class TooltipHelpBlock extends Tooltip {

    private final Icon icon = new Icon();
    private final InlineLabel label = new InlineLabel();
    private final ElementPanel help = new ElementPanel("span");

    public TooltipHelpBlock() {
        help.addStyleName("form-text d-inline-flex align-items-center gap-1");
        help.add(icon);
        help.add(label);
        setWidget(help);
        setPlacement(Placement.RIGHT);
        addTooltipClassName("tooltip-danger");
        setIconType(IconType.EXCLAMATION_TRIANGLE);
    }

    public TooltipHelpBlock(String text) {
        this();
        setText(text);
    }

    public IconType getIconType() {
        return icon.getType();
    }

    public String getText() {
        return getTitle();
    }

    @Override
    public void setText(String text) {
        String effectiveText = text == null ? "" : text;
        label.setText(effectiveText);
        setTitle(effectiveText);
    }

    public void setIconType(IconType iconType) {
        icon.setType(iconType);
    }

    public void setHTML(final String html) {
        getWidget().getElement().setInnerHTML(html == null ? "" : html);
    }

    public String getHTML() {
        return getWidget().getElement().getInnerHTML();
    }

}
