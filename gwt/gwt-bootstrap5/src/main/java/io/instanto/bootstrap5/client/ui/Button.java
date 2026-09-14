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

import io.instanto.bootstrap5.client.ui.base.button.AbstractToggleButton;
import io.instanto.bootstrap5.client.ui.constants.Attributes;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;
import io.instanto.bootstrap5.client.ui.constants.IconType;
import io.instanto.bootstrap5.client.ui.constants.Toggle;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Button based on a {@code <button>} element. It preserves the GwtBootstrap3
 * widget contract while rendering Bootstrap 5 classes and behaviour.
 */
public class Button extends AbstractToggleButton {

    public Button() {
    }

    public Button(final String text) {
        setText(text);
    }

    public Button(final String text, final ClickHandler handler) {
        this(text);
        addClickHandler(handler);
    }

    public Button(final String text, final IconType iconType, final ClickHandler handler) {
        this(text, handler);
        setIcon(iconType);
    }

    public Button(final String text, final Variant variant) {
        this(text);
        setVariant(variant);
    }

    public Button(final String text, final ButtonType type) {
        super(type);
        setText(text);
    }

    public void setDataToggle(final String toggle) {
        if (toggle == null || toggle.isEmpty()) {
            setDataToggle((Toggle) null);
            return;
        }
        for (final Toggle candidate : Toggle.values()) {
            if (candidate.getToggle().equals(toggle)) {
                setDataToggle(candidate);
                return;
            }
        }
        getElement().setAttribute(Attributes.DATA_TOGGLE, toggle);
    }

    @Override
    protected Element createElement() {
        return Document.get().createPushButtonElement().cast();
    }
}
