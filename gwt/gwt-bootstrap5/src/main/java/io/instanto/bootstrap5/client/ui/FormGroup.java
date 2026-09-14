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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.base.HasSize;
import io.instanto.bootstrap5.client.ui.base.HasValidationState;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.FormGroupSize;
import io.instanto.bootstrap5.client.ui.constants.ValidationState;

public class FormGroup extends ElementPanel implements HasSize<FormGroupSize>, HasValidationState {

    private FormLabel label;
    private Element controlElement;

    public FormGroup() {
        super("div");
        addStyleName("mb-3");
    }

    @Override
    public void add(Widget child) {
        super.add(child);
        if (child instanceof FormLabel) {
            label = (FormLabel) child;
        } else {
            Element candidate = formControlElement(child);
            if (candidate != null && controlElement == null) {
                controlElement = candidate;
            }
        }
        associateLabelAndControl();
    }

    @Override
    public void setSize(FormGroupSize size) {
        StyleHelper.addUniqueEnumStyleName(this, FormGroupSize.class, size);
    }

    @Override
    public FormGroupSize getSize() {
        return FormGroupSize.fromStyleName(getStyleName());
    }

    /**
     * Bootstrap 3 put .has-error on the group and let descendant selectors
     * colour everything inside it. Bootstrap 5 puts .is-invalid on the control
     * itself, so that is where the state goes; the group keeps the property so
     * calling code and the error handler do not have to know the difference.
     */
    @Override
    public void setValidationState(ValidationState state) {
        this.validationState = state == null ? ValidationState.NONE : state;
        if (controlElement == null) {
            return;
        }
        for (ValidationState candidate : ValidationState.values()) {
            if (!candidate.getCssName().isEmpty()) {
                controlElement.removeClassName(candidate.getCssName());
            }
        }
        if (!this.validationState.getCssName().isEmpty()) {
            controlElement.addClassName(this.validationState.getCssName());
        }
    }

    @Override
    public ValidationState getValidationState() {
        return validationState;
    }

    /** The input, textarea or select this group labels, or null if it has none. */
    public Element getControlElement() {
        return controlElement;
    }

    private ValidationState validationState = ValidationState.NONE;

    private Element formControlElement(Widget child) {
        // No widget needs special-casing here. A SuggestBox is a Composite built
        // with initWidget(box), so its own element already is the <input>; the
        // tag check below picks it up like any other control. Reaching inside it
        // was what made this cast to a type the Bootstrap TextBox never extended.
        Element element = child.getElement();
        String tagName = element.getTagName();
        if ("input".equalsIgnoreCase(tagName)
                || "textarea".equalsIgnoreCase(tagName)
                || "select".equalsIgnoreCase(tagName)) {
            return element;
        }
        return null;
    }

    private void associateLabelAndControl() {
        if (label == null || controlElement == null) {
            return;
        }
        if (controlElement.getId() == null || controlElement.getId().isEmpty()) {
            controlElement.setId(Document.get().createUniqueId());
        }
        label.setFor(controlElement.getId());
    }
}
