package io.instanto.bootstrap5.client.ui.base.mixin;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2015 GwtBootstrap3
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

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import io.instanto.bootstrap5.client.ui.form.error.ErrorHandler;
import io.instanto.bootstrap5.client.ui.form.validator.HasValidators;
import io.instanto.bootstrap5.client.ui.form.validator.ValidationChangedEvent;
import io.instanto.bootstrap5.client.ui.form.validator.ValidationChangedEvent.ValidationChangedHandler;
import io.instanto.bootstrap5.client.ui.form.validator.Validator;
import io.instanto.bootstrap5.client.ui.form.validator.ValidatorWrapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract validator mixin. Contains all of the validation logic.
 *
 * @param <W> the generic type
 * @param <V> the value type
 * @author Steven Jardine
 */
public class DefaultValidatorMixin<W extends Widget & HasValue<V> & Editor<V>, V>
    implements HasValidators<V> {

  protected ErrorHandler errorHandler;

  private EventBus eventBus;

  private W inputWidget;

  private Boolean valid = null;

  private boolean validateOnBlur;

  protected Set<ValidatorWrapper<V>> validators = new LinkedHashSet<ValidatorWrapper<V>>();

  /**
   * Instantiates a new abstract validator mixin.
   *
   * @param inputWidget the input widget
   * @param errorHandler the error handler
   */
  public DefaultValidatorMixin(W inputWidget, ErrorHandler errorHandler) {
    this.inputWidget = inputWidget;
    this.errorHandler = errorHandler;
    eventBus = new SimpleEventBus();

    bindBlurValidation();
    bindValueChangeValidation();
  }

  /**
   * Registers an additional blur handler; construction uses a private binding path. Override
   * onValidationBlur() to customize handling after construction.
   */
  protected HandlerRegistration setupBlurValidation() {
    return bindBlurValidation();
  }

  private HandlerRegistration bindBlurValidation() {
    return inputWidget.addDomHandler(
        new BlurHandler() {
          @Override
          public void onBlur(BlurEvent event) {
            onValidationBlur();
          }
        },
        BlurEvent.getType());
  }

  /**
   * Registers an additional value handler; construction uses a private binding path. Override
   * onValidationValueChange() to customize handling after construction.
   */
  protected HandlerRegistration setupValueChangeValidation() {
    return bindValueChangeValidation();
  }

  private HandlerRegistration bindValueChangeValidation() {
    return inputWidget.addHandler(
        new ValueChangeHandler<V>() {
          @Override
          public void onValueChange(ValueChangeEvent<V> event) {
            onValidationValueChange();
          }
        },
        ValueChangeEvent.getType());
  }

  /** Called for blur events, after the input and mixin have been constructed. */
  protected void onValidationBlur() {
    validate(validateOnBlur);
  }

  /** Called for value changes, after the input and mixin have been constructed. */
  protected void onValidationValueChange() {
    validate(false);
  }

  @Override
  public HandlerRegistration addValidationChangedHandler(ValidationChangedHandler handler) {
    return eventBus.addHandler(ValidationChangedEvent.getType(), handler);
  }

  /** {@inheritDoc} */
  @Override
  public void addValidator(Validator<V> validator) {
    // Equality enforces one validator per type; ordering must not also decide membership.
    if (validators.add(new ValidatorWrapper<V>(validator, validators.size()))) {
      List<ValidatorWrapper<V>> ordered = new ArrayList<>(validators);
      // The stable sort preserves insertion order among equal priorities, including after removal.
      ordered.sort(Comparator.comparing(ValidatorWrapper::getPriority));
      validators.clear();
      validators.addAll(ordered);
    }
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    eventBus.fireEvent(event);
  }

  /**
   * @return the inputWidget
   */
  public W getInputWidget() {
    return inputWidget;
  }

  /** {@inheritDoc} */
  @Override
  public boolean getValidateOnBlur() {
    return validateOnBlur;
  }

  /** {@inheritDoc} */
  @Override
  public boolean removeValidator(Validator<V> validator) {
    for (ValidatorWrapper<V> wrapper : validators) {
      if (wrapper.getValidator().equals(validator)) {
        return validators.remove(wrapper);
      }
    }
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public void reset() {
    if (errorHandler != null) {
      errorHandler.clearErrors();
    }
  }

  /**
   * Sets the error handler.
   *
   * @param errorHandler the new error handler
   */
  public void setErrorHandler(ErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
  }

  /** {@inheritDoc} */
  @Override
  public void setValidateOnBlur(boolean vob) {
    validateOnBlur = vob;
  }

  /** {@inheritDoc} */
  @Override
  public void setValidators(@SuppressWarnings("unchecked") Validator<V>... newValidators) {
    validators.clear();
    for (Validator<V> validator : newValidators) {
      addValidator(validator);
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean validate() {
    return validate(true);
  }

  /** {@inheritDoc} */
  @Override
  public boolean validate(boolean show) {
    Boolean oldValid = valid;
    boolean currentValid = true;
    List<EditorError> errors = null;
    if (!validators.isEmpty()) {
      V value = inputWidget.getValue();
      // A validator may edit the rule set; apply those changes on the next pass.
      for (ValidatorWrapper<V> wrapper : new ArrayList<>(validators)) {
        List<EditorError> result = wrapper.getValidator().validate(inputWidget, value);
        if (result != null && !result.isEmpty()) {
          currentValid = false;
          if (show && errorHandler != null) {
            if (errors == null) {
              errors = new ArrayList<>();
            }
            errors.addAll(result);
          }
        }
      }
    }
    if (show && errorHandler != null) {
      if (errors == null) {
        errorHandler.clearErrors();
      } else {
        errorHandler.showErrors(errors);
      }
    }
    valid = currentValid;
    if (oldValid == null || oldValid.booleanValue() != currentValid) {
      eventBus.fireEvent(new ValidationChangedEvent(currentValid));
    }
    return valid;
  }
}
