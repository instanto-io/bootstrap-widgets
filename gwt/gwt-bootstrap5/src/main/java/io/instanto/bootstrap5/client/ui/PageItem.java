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

import com.google.gwt.user.client.ui.Widget;

public class PageItem extends ElementPanel {

    private final Anchor link = new Anchor();

    public PageItem() {
        super("li");
        addStyleName("page-item");
        link.addStyleName("page-link");
        super.add(link);
    }

    public PageItem(String text, String href) {
        this();
        setText(text);
        setHref(href);
    }

    @Override
    public String getText() {
        return link.getText();
    }

    @Override
    public void setText(String text) {
        link.setText(text == null ? "" : text);
    }

    public void setHref(String href) {
        link.setHref(href == null ? "#" : href);
    }

    public String getHref() {
        return link.getHref();
    }

    @Override
    public void add(Widget child) {
        link.add(child);
    }

    public void setActive(boolean active) {
        setStyleName("active", active);
        if (active) {
            link.getElement().setAttribute("aria-current", "page");
        } else {
            link.getElement().removeAttribute("aria-current");
        }
    }

    public void setDisabled(boolean disabled) {
        setStyleName("disabled", disabled);
        link.getElement().setAttribute("aria-disabled", disabled ? "true" : "false");
        link.getElement().setAttribute("tabindex", disabled ? "-1" : "0");
    }
}
