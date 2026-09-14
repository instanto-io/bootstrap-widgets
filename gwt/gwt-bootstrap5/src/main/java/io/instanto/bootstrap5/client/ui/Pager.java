/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2018 GwtBootstrap3
 * %%
 * Modified from the GwtBootstrap3 original for the Bootstrap 5 track of
 * GWT Bootstrap: moved to the io.instanto.bootstrap5 namespace and
 * re-targeted at Bootstrap 5 markup, class names and JavaScript APIs. The
 * GwtBootstrap3 copyright above is retained as required by the Apache
 * License 2.0; the namespace changed, the attribution did not.
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

import io.instanto.bootstrap5.client.ui.constants.IconPosition;
import io.instanto.bootstrap5.client.ui.constants.IconSize;
import io.instanto.bootstrap5.client.ui.constants.IconType;
import io.instanto.bootstrap5.client.ui.constants.Styles;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class Pager extends Pagination {

    private final AnchorListItem previous;
    private final AnchorListItem next;

    public Pager() {
        previous = new AnchorListItem("Previous", "#");
        next = new AnchorListItem("Next", "#");
        add(previous);
        add(next);
    }

    public void setAlignToSides(boolean alignToSides) {
        setStyleName("justify-content-between", alignToSides);
        previous.setStyleName(Styles.PREVIOUS, alignToSides);
        next.setStyleName(Styles.NEXT, alignToSides);
    }

    public HandlerRegistration addPreviousClickHandler(final ClickHandler clickHandler) {
        return previous.addClickHandler(clickHandler);
    }

    public HandlerRegistration addNextClickHandler(final ClickHandler clickHandler) {
        return next.addClickHandler(clickHandler);
    }

    public void setPreviousText(final String text) {
        previous.setText(text);
    }

    public void setPreviousIcon(final IconType icon) {
        previous.setIcon(icon);
    }

    public void setPreviousIconSize(final IconSize iconSize) {
        previous.setIconSize(iconSize);
    }

    public void setPreviousEnabled(final boolean enabled) {
        previous.setEnabled(enabled);
    }

    public void setPreviousVisible(final boolean visible) {
        previous.setVisible(visible);
    }

    public void setNextText(final String text) {
        next.setText(text);
    }

    public void setNextIcon(final IconType icon) {
        next.setIcon(icon);
        next.setIconPosition(IconPosition.RIGHT);
    }

    public void setNextIconSize(final IconSize iconSize) {
        next.setIconSize(iconSize);
    }

    public void setNextEnabled(final boolean enabled) {
        next.setEnabled(enabled);
    }

    public void setNextVisible(final boolean visible) {
        next.setVisible(visible);
    }
}
