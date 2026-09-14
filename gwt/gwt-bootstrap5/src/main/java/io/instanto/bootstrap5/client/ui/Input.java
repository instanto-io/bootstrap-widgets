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

import io.instanto.bootstrap5.client.ui.base.HasInputType;
import io.instanto.bootstrap5.client.ui.base.ValueBoxBase;
import io.instanto.bootstrap5.client.ui.constants.ElementTags;
import io.instanto.bootstrap5.client.ui.constants.InputType;
import io.instanto.bootstrap5.client.ui.constants.Styles;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.text.shared.testing.PassthroughParser;
import com.google.gwt.text.shared.testing.PassthroughRenderer;
import com.google.gwt.uibinder.client.UiConstructor;

public class Input extends ValueBoxBase<String> implements HasInputType {

    private static final String MIN = "min";
    private static final String MAX = "max";
    private static final String NAME = "name";

    public Input() {
        this(PassthroughRenderer.instance(), PassthroughParser.instance());
    }

    public Input(String type) {
        this();
        setType(type);
    }

    public Input(Renderer<String> renderer, Parser<String> parser) {
        super(Document.get().createElement(ElementTags.INPUT), renderer, parser);
        addStyleName(Styles.FORM_CONTROL);
    }

    @UiConstructor
    public Input(final InputType type) {
        this();
        setType(type);
    }

    public void setMin(final String min) {
        getElement().setAttribute(MIN, min == null ? "" : min);
    }

    public void setMax(final String max) {
        getElement().setAttribute(MAX, max == null ? "" : max);
    }

    public void setType(String type) {
        getElement().setAttribute(TYPE, type == null ? InputType.TEXT.getType() : type);
    }

    @Override
    public void setType(final InputType inputType) {
        setType(inputType == null ? InputType.TEXT.getType() : inputType.getType());
    }

    @Override
    public InputType getType() {
        String type = getElement().getAttribute(TYPE);
        for (InputType inputType : InputType.values()) {
            if (inputType.getType().equals(type)) {
                return inputType;
            }
        }
        return null;
    }

    public String getTypeName() {
        return getElement().getAttribute(TYPE);
    }

    public void setName(String name) {
        getElement().setAttribute(NAME, name == null ? "" : name);
    }

    public String getName() {
        return getElement().getAttribute(NAME);
    }

    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return addDomHandler(handler, ChangeEvent.getType());
    }
}
