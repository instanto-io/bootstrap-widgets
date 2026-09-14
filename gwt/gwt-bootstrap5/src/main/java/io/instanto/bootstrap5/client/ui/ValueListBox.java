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

import java.util.List;

import io.instanto.bootstrap5.client.ui.base.HasId;
import io.instanto.bootstrap5.client.ui.base.HasPlaceholder;
import io.instanto.bootstrap5.client.ui.base.HasResponsiveness;
import io.instanto.bootstrap5.client.ui.base.HasSize;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.base.mixin.BlankValidatorMixin;
import io.instanto.bootstrap5.client.ui.base.mixin.ErrorHandlerMixin;
import io.instanto.bootstrap5.client.ui.base.mixin.IdMixin;
import io.instanto.bootstrap5.client.ui.constants.DeviceSize;
import io.instanto.bootstrap5.client.ui.constants.InputSize;
import io.instanto.bootstrap5.client.ui.form.error.ErrorHandler;
import io.instanto.bootstrap5.client.ui.form.error.ErrorHandlerType;
import io.instanto.bootstrap5.client.ui.form.error.HasErrorHandler;
import io.instanto.bootstrap5.client.ui.form.validator.HasBlankValidator;
import io.instanto.bootstrap5.client.ui.form.validator.HasValidators;
import io.instanto.bootstrap5.client.ui.form.validator.ValidationChangedEvent.ValidationChangedHandler;
import io.instanto.bootstrap5.client.ui.form.validator.Validator;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A {@link com.google.gwt.user.client.ui.ValueListBox} styled for Bootstrap 5.
 *
 * <p>Bootstrap 3 styled a select with {@code form-control}; Bootstrap 5 gives
 * selects their own {@code form-select} class, which is what the underlying
 * {@link ListBox} applies. Validation puts {@code is-invalid} on the select and
 * shows the message from a sibling {@code invalid-feedback} element.</p>
 *
 * @param <T> the value type
 */
public class ValueListBox<T> extends com.google.gwt.user.client.ui.ValueListBox<T> implements HasName, HasId,
        HasPlaceholder, HasResponsiveness, HasSize<InputSize>, HasEditorErrors<T>, HasErrorHandler,
        HasValidators<T>, HasBlankValidator<T> {

    private final ErrorHandlerMixin<T> errorHandlerMixin = new ErrorHandlerMixin<T>(this);

    private final IdMixin<ValueListBox<T>> idMixin = new IdMixin<ValueListBox<T>>(this);

    private final BlankValidatorMixin<ValueListBox<T>, T> validatorMixin =
            new BlankValidatorMixin<ValueListBox<T>, T>(this, errorHandlerMixin.getErrorHandler());

    public ValueListBox() {
        super();
    }

    public ValueListBox(final Renderer<? super T> renderer) {
        super(renderer);
    }

    public ValueListBox(final Renderer<? super T> renderer, final ProvidesKey<T> keyProvider) {
        super(renderer, keyProvider);
    }

    /** Renders through {@link ListBox}, so the select carries form-select. */
    @Override
    protected void initWidget(final com.google.gwt.user.client.ui.Widget widget) {
        super.initWidget(new ListBox());
    }

    @Override
    public void setName(final String name) {
        getElement().setAttribute("name", name == null ? "" : name);
    }

    @Override
    public String getName() {
        return getElement().getAttribute("name");
    }

    /**
     * Kept for parity with the Bootstrap 3 widget. A select has no placeholder
     * attribute in HTML, so this sets it without visible effect; Bootstrap 5
     * spells the same intent as a disabled, selected first option.
     */
    @Override
    public void setPlaceholder(final String placeholder) {
        getElement().setAttribute("placeholder", placeholder == null ? "" : placeholder);
    }

    @Override
    public String getPlaceholder() {
        return getElement().getAttribute("placeholder");
    }

    @Override
    public void setId(final String id) {
        idMixin.setId(id);
    }

    @Override
    public String getId() {
        return idMixin.getId();
    }

    @Override
    public void setSize(final InputSize size) {
        StyleHelper.addUniqueEnumStyleName(this, InputSize.class, size == null ? InputSize.DEFAULT : size);
    }

    @Override
    public InputSize getSize() {
        return InputSize.fromStyleName(getStyleName());
    }

    @Override
    public void setVisibleOn(final DeviceSize deviceSize) {
        StyleHelper.setVisibleOn(this, deviceSize);
    }

    @Override
    public void setHiddenOn(final DeviceSize deviceSize) {
        StyleHelper.setHiddenOn(this, deviceSize);
    }

    // ---- validation --------------------------------------------------------

    @Override
    public void setErrorHandler(final ErrorHandler handler) {
        validatorMixin.setErrorHandler(handler);
        errorHandlerMixin.setErrorHandler(handler);
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return errorHandlerMixin.getErrorHandler();
    }

    @Override
    public void setErrorHandlerType(final ErrorHandlerType type) {
        validatorMixin.setErrorHandler(errorHandlerMixin.getErrorHandler());
        errorHandlerMixin.setErrorHandlerType(type);
    }

    @Override
    public ErrorHandlerType getErrorHandlerType() {
        return errorHandlerMixin.getErrorHandlerType();
    }

    @Override
    public void showErrors(final List<EditorError> errors) {
        errorHandlerMixin.showErrors(errors);
    }

    @Override
    public HandlerRegistration addValidationChangedHandler(final ValidationChangedHandler handler) {
        return validatorMixin.addValidationChangedHandler(handler);
    }

    @Override
    public void addValidator(final Validator<T> validator) {
        validatorMixin.addValidator(validator);
    }

    @Override
    public boolean removeValidator(final Validator<T> validator) {
        return validatorMixin.removeValidator(validator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValidators(final Validator<T>... validators) {
        validatorMixin.setValidators(validators);
    }

    @Override
    public boolean getValidateOnBlur() {
        return validatorMixin.getValidateOnBlur();
    }

    @Override
    public void setValidateOnBlur(final boolean validateOnBlur) {
        validatorMixin.setValidateOnBlur(validateOnBlur);
    }

    @Override
    public boolean getAllowBlank() {
        return validatorMixin.getAllowBlank();
    }

    @Override
    public void setAllowBlank(final boolean allowBlank) {
        validatorMixin.setAllowBlank(allowBlank);
    }

    @Override
    public void reset() {
        validatorMixin.reset();
    }

    @Override
    public boolean validate() {
        return validatorMixin.validate();
    }

    @Override
    public boolean validate(final boolean show) {
        return validatorMixin.validate(show);
    }
}
