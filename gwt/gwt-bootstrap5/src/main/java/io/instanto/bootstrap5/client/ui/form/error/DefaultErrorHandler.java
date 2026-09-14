package io.instanto.bootstrap5.client.ui.form.error;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2015 GwtBootstrap3
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

import java.util.List;

import io.instanto.bootstrap5.client.ui.HelpBlock;
import io.instanto.bootstrap5.client.ui.base.HasValidationState;
import io.instanto.bootstrap5.client.ui.constants.ValidationState;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the default {@link ErrorHandler} implementation. The assumption is that every {@link ValueBoxBase}
 * instance will have a {@link HasValidationState} parent. If there is a {@link HelpBlock} that is a child of
 * the {@link HasValidationState} parent then error messages will be displayed in the {@link HelpBlock}.
 *
 * Example:
 *
 * <pre>{@code
 * <b:FormGroup>
 *      <b:FormLabel for="username">User</b:FormLabel>
 *      <b:TextBox b:id="username" ui:field="username" />
 *      <b:HelpBlock iconType="EXCLAMATION" />
 * </b:FormGroup>
 * }</pre>
 *
 * @author Steven Jardine
 */
public class DefaultErrorHandler implements ErrorHandler {

    private boolean initialized = false;

    private final Widget inputWidget;

    private HelpBlock feedback = null;

    private boolean feedbackIsOurs = false;

    private HasValidationState validationStateParent = null;

    public DefaultErrorHandler(final Widget widget) {
        super();
        assert widget != null;
        this.inputWidget = widget;
        this.inputWidget.addAttachHandler(new Handler() {
            @Override
            public void onAttachOrDetach(final AttachEvent event) {
                init();
            }
        });
    }

    @Override
    public void cleanup() {
        if (feedbackIsOurs && feedback != null) {
            feedback.removeFromParent();
            feedback = null;
            feedbackIsOurs = false;
        }
    }

    @Override
    public void clearErrors() {
        inputWidget.getElement().removeAttribute("aria-invalid");
        removeValidationDescription();
        if (validationStateParent != null) {
            validationStateParent.setValidationState(ValidationState.NONE);
        }
        if (feedback != null) {
            feedback.clearError();
        }
    }

    @Override
    public void showErrors(final List<EditorError> errors) {
        init();
        final boolean hasErrors = errors != null && !errors.isEmpty();

        if (validationStateParent != null) {
            validationStateParent.setValidationState(hasErrors ? ValidationState.ERROR : ValidationState.NONE);
        }

        if (!hasErrors) {
            if (feedback != null) {
                feedback.clearError();
            }
            return;
        }

        final StringBuilder message = new StringBuilder();
        for (int i = 0; i < errors.size(); i++) {
            if (i > 0) {
                message.append("; ");
            }
            message.append(errors.get(i).getMessage());
        }

        ensureFeedback();
        if (feedback != null) {
            feedback.setError(message.toString());
        }
        inputWidget.getElement().setAttribute("aria-invalid", "true");
        associateValidationDescription();
    }

    /**
     * Walks up to the nearest {@link HasValidationState} -- in practice the
     * {@link FormGroup} -- and remembers any {@link HelpBlock} already inside
     * it to carry the message.
     */
    public void init() {
        if (initialized) {
            return;
        }
        Widget parent = inputWidget.getParent();
        while (parent != null) {
            if (parent instanceof HasValidationState) {
                validationStateParent = (HasValidationState) parent;
                feedback = findHelpBlock(parent);
                feedbackIsOurs = false;
                break;
            }
            parent = parent.getParent();
        }
        if (inputWidget.isAttached() || validationStateParent != null) {
            initialized = true;
        }
    }

    /**
     * Bootstrap 5 shows a validation message from a .invalid-feedback element
     * next to the control. Bootstrap 3 code was not obliged to declare one --
     * without a HelpBlock the message simply went nowhere -- so one is created
     * on demand here and removed again on cleanup.
     */
    private void ensureFeedback() {
        if (feedback != null || validationStateParent == null) {
            return;
        }
        if (!(validationStateParent instanceof HasWidgets)) {
            return;
        }
        feedback = new HelpBlock();
        feedbackIsOurs = true;
        ((HasWidgets) validationStateParent).add(feedback);
    }

    private HelpBlock findHelpBlock(final Widget widget) {
        if (widget instanceof HelpBlock) {
            return (HelpBlock) widget;
        }
        if (widget instanceof HasWidgets) {
            for (final Widget child : (HasWidgets) widget) {
                final HelpBlock found = findHelpBlock(child);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private void associateValidationDescription() {
        if (feedback == null) {
            return;
        }
        String messageId = feedback.getElement().getId();
        if (messageId == null || messageId.isEmpty()) {
            messageId = Document.get().createUniqueId();
            feedback.getElement().setId(messageId);
        }
        String describedBy = inputWidget.getElement().getAttribute("aria-describedby");
        if (!(" " + describedBy + " ").contains(" " + messageId + " ")) {
            inputWidget.getElement().setAttribute("aria-describedby",
                    describedBy == null || describedBy.trim().isEmpty()
                            ? messageId : describedBy.trim() + " " + messageId);
        }
    }

    private void removeValidationDescription() {
        if (feedback == null) {
            return;
        }
        String messageId = feedback.getElement().getId();
        String describedBy = inputWidget.getElement().getAttribute("aria-describedby");
        if (messageId == null || messageId.isEmpty() || describedBy == null || describedBy.isEmpty()) {
            return;
        }
        StringBuilder remaining = new StringBuilder();
        for (String id : describedBy.trim().split("\\s+")) {
            if (!messageId.equals(id)) {
                if (remaining.length() > 0) {
                    remaining.append(' ');
                }
                remaining.append(id);
            }
        }
        if (remaining.length() == 0) {
            inputWidget.getElement().removeAttribute("aria-describedby");
        } else {
            inputWidget.getElement().setAttribute("aria-describedby", remaining.toString());
        }
    }
}
