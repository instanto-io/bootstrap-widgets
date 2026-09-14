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

import io.instanto.bootstrap5.client.ui.base.AbstractListItem;
import io.instanto.bootstrap5.client.ui.constants.Styles;
import io.instanto.bootstrap5.client.ui.html.Text;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;

public class ListItem extends AbstractListItem implements HasWidgets, HasText, HasClickHandlers {

    private Text text = null;

    public ListItem() {
    }

    public ListItem(final String text) {
        this();
        setText(text);
    }

    @Override
    public HandlerRegistration addClickHandler(final ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    @Override
    public String getText() {
        return text == null ? "" : text.getText();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        if (getParent() instanceof MediaList) {
            addStyleName(Styles.MEDIA);
        }
    }

    @Override
    public void setText(final String text) {
        if (this.text == null) {
            this.text = new Text(text == null ? "" : text);
            add(this.text);
        } else {
            this.text.setText(text == null ? "" : text);
        }
    }
}
