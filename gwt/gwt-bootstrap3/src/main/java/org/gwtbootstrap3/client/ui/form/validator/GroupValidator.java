package org.gwtbootstrap3.client.ui.form.validator;

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

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import org.gwtbootstrap3.client.ui.form.validator.ValidationChangedEvent.ValidationChangedHandler;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Useful for validating a group of fields that implement the {@link HasValidators} interface.
 *
 * @author Steven Jardine
 */
public class GroupValidator implements ValidationChangedEvent.HasValidationChangedHandlers {

  private final SimpleEventBus eventBus = new SimpleEventBus();
  private final Map<HasValidators<?>, FieldState> fields = new LinkedHashMap<>();
  private int validationDepth;
  private Boolean groupValid;

  /** Adds a field once, including when validation or notification calls back into this group. */
  public <T extends Widget & HasValidators<?>> void add(final T field) {
    if (fields.containsKey(field)) return;
    FieldState state = new FieldState(field);
    fields.put(field, state);
    validationDepth++;
    try {
      state.registration = field.addValidationChangedHandler(event -> {
        // A queued event from a removed registration must not resurrect it, even after re-add.
        if (fields.get(field) == state) {
          state.valid = event.isValid();
          updateStateAndNotify();
        }
      });
      if (fields.get(field) == state) {
        validateField(state, false);
      } else {
        unsubscribe(state);
      }
    } catch (RuntimeException | Error failure) {
      if (fields.get(field) == state) fields.remove(field);
      try {
        unsubscribe(state);
      } catch (RuntimeException | Error cleanup) {
        if (cleanup != failure) failure.addSuppressed(cleanup);
      }
      throw failure;
    } finally {
      validationDepth--;
    }
    if (field.isAttached() || groupValid != null) updateStateAndNotify();
  }

  @Override
  public HandlerRegistration addValidationChangedHandler(ValidationChangedHandler handler) {
    return eventBus.addHandler(ValidationChangedEvent.getType(), handler);
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    eventBus.fireEvent(event);
  }

  /** Removes membership before unsubscribing, so callbacks cannot reinsert a removed field. */
  public <T extends Widget & HasValidators<?>> boolean remove(final T field) {
    FieldState state = fields.remove(field);
    if (state == null) return false;
    unsubscribe(state);
    if (groupValid != null) updateStateAndNotify();
    return true;
  }

  private static void unsubscribe(FieldState state) {
    HandlerRegistration registration = state.registration;
    state.registration = null;
    if (registration != null) registration.removeHandler();
  }

  private boolean currentValidity() {
    for (FieldState state : fields.values()) {
      if (!state.valid) return false;
    }
    return true;
  }

  private void updateStateAndNotify() {
    if (validationDepth != 0) return;
    boolean valid = currentValidity();
    if (groupValid == null || groupValid.booleanValue() != valid) {
      groupValid = valid;
      eventBus.fireEvent(new ValidationChangedEvent(valid));
    }
  }

  public boolean validate() {
    return validate(true);
  }

  /**
   * Validates the fields present at the start of this pass. Removed fields are skipped and newly
   * added fields validate in add(). Nested passes skip the field already being validated and
   * publish changes only when the outermost pass finishes.
   */
  public boolean validate(boolean show) {
    validationDepth++;
    try {
      // Validators can add/remove fields synchronously; iterating the live map is unsafe.
      for (FieldState state : new ArrayList<>(fields.values())) {
        if (fields.get(state.field) == state) validateField(state, show);
      }
    } finally {
      validationDepth--;
    }
    updateStateAndNotify();
    return currentValidity();
  }

  private void validateField(FieldState state, boolean show) {
    if (state.validating) return;
    state.validating = true;
    try {
      boolean valid = state.field.validate(show);
      if (fields.get(state.field) == state) state.valid = valid;
    } finally {
      state.validating = false;
    }
  }

  private static final class FieldState {
    final HasValidators<?> field;
    boolean valid = true;
    boolean validating;
    HandlerRegistration registration;

    FieldState(HasValidators<?> field) {
      this.field = field;
    }
  }
}
