/*
 * #%L
 * GWT Bootstrap
 * %%
 * Copyright (C) 2026 Carl Stainton
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.instanto.bootstrap5.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class DropDownItem extends ElementPanel {

    private final Anchor anchor = new Anchor();

    public DropDownItem() {
        super("li");
        anchor.addStyleName("dropdown-item");
        super.add(anchor);
    }

    public DropDownItem(String text, String href) {
        this();
        setText(text);
        setHref(href);
    }

    @Override
    public String getText() {
        return anchor.getText();
    }

    @Override
    public void setText(String text) {
        anchor.setText(text == null ? "" : text);
    }

    public void setHref(String href) {
        anchor.setHref(href == null ? "#" : href);
    }

    public String getHref() {
        return anchor.getHref();
    }

    @Override
    public void add(Widget child) {
        anchor.add(child);
    }

    public void setActive(boolean active) {
        anchor.setStyleName("active", active);
        anchor.getElement().setAttribute("aria-current", active ? "true" : "false");
    }

    public void setDisabled(boolean disabled) {
        anchor.setStyleName("disabled", disabled);
        anchor.getElement().setAttribute("aria-disabled", disabled ? "true" : "false");
        anchor.getElement().setAttribute("tabindex", disabled ? "-1" : "0");
    }

    public boolean isDisabled() {
        return "true".equals(anchor.getElement().getAttribute("aria-disabled"));
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (Event.ONCLICK == DOM.eventGetType(event) && isDisabled()) {
            event.preventDefault();
            event.stopPropagation();
            return;
        }
        super.onBrowserEvent(event);
    }
}
