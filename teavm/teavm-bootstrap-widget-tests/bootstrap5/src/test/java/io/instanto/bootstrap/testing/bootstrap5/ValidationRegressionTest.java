package io.instanto.bootstrap.testing.bootstrap5;

import static org.junit.Assert.*;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.web.bindery.event.shared.HandlerRegistration;
import io.instanto.bootstrap5.client.ui.TextBox;
import io.instanto.bootstrap5.client.ui.base.mixin.DefaultValidatorMixin;
import io.instanto.bootstrap5.client.ui.form.validator.AbstractValidator;
import io.instanto.bootstrap5.client.ui.form.validator.GroupValidator;
import io.instanto.bootstrap5.client.ui.form.validator.MessageFormat;
import io.instanto.bootstrap5.client.ui.form.validator.ValidationChangedEvent;
import io.instanto.bootstrap5.client.ui.form.validator.ValidationMessages;
import io.instanto.bootstrap5.client.ui.form.validator.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class ValidationRegressionTest {
  @Test
  public void validatesWithoutAnErrorDisplayHandler() {
    DefaultValidatorMixin<TextBox, String> validation =
        new DefaultValidatorMixin<>(new TextBox(), null);
    validation.addValidator(new InvalidValidator());
    assertFalse(validation.validate(false));
  }

  @Test
  public void removingThenAddingValidatorsDoesNotLoseEqualPriorityRules() {
    DefaultValidatorMixin<TextBox, String> validation =
        new DefaultValidatorMixin<>(new TextBox(), null);
    FirstValidator first = new FirstValidator();
    List<String> calls = new ArrayList<>();
    validation.addValidator(first);
    validation.addValidator(new SecondValidator(calls));
    assertTrue(validation.removeValidator(first));
    validation.addValidator(new ThirdValidator(calls));
    validation.validate(false);
    assertEquals(List.of("second", "third"), calls);
  }

  @Test
  public void duplicateFieldsHaveOnlyOneRegistrationAndRemovalUnsubscribes() {
    TrackingField field = new TrackingField();
    GroupValidator group = new GroupValidator();
    group.add(field);
    group.add(field);
    assertEquals(1, field.registrations);
    assertTrue(group.remove(field));
    assertEquals(0, field.registrations);
    assertFalse(group.remove(field));
  }

  @Test
  public void removingAnInvalidFieldUpdatesGroupState() {
    TrackingField field = new TrackingField();
    field.result = false;
    GroupValidator group = new GroupValidator();
    List<Boolean> changes = new ArrayList<>();
    group.addValidationChangedHandler(event -> changes.add(event.isValid()));
    group.add(field);
    assertFalse(group.validate());
    group.remove(field);
    assertEquals(List.of(false, true), changes);
  }

  @Test
  public void groupUsesReturnedValidityEvenWhenTheFieldDoesNotFireEvents() {
    TrackingField field = new TrackingField();
    GroupValidator group = new GroupValidator();
    group.add(field);
    field.result = false;
    assertFalse(group.validate());
  }

  @Test
  public void messageArgumentsCannotBeChangedByTheCaller() {
    Object[] arguments = {"before"};
    MessageValidator validator = new MessageValidator(arguments);
    String expected = validator.getInvalidMessage(ValidationMessages.Keys.DECIMAL_MIN);
    arguments[0] = "after";
    assertEquals(expected, validator.getInvalidMessage(ValidationMessages.Keys.DECIMAL_MIN));
  }

  @Test
  public void messageArgumentsAndOverrideRejectNullWithoutDependingOnAssertions() {
    assertThrows(NullPointerException.class, () -> new MessageValidator((Object[]) null));
    assertThrows(NullPointerException.class, () -> new MessageValidator((String) null));
  }

  @Test
  public void messageSubstitutionTreatsDollarSignsAndBackslashesAsText() {
    assertEquals("Value $5\\path", MessageFormat.format("Value {1}", "$5\\path"));
  }

  private static final class MessageValidator extends AbstractValidator<String> {
    MessageValidator(Object[] arguments) {
      super(ValidationMessages.Keys.DECIMAL_MIN, arguments);
    }

    MessageValidator(String override) {
      super(override);
    }

    @Override
    public boolean isValid(String value) {
      return false;
    }

    @Override
    public int getPriority() {
      return Priority.MEDIUM;
    }
  }

  @Test
  public void validatorChangesTakeEffectOnTheNextPassWithoutBreakingIteration() {
    DefaultValidatorMixin<TextBox, String> validation =
        new DefaultValidatorMixin<>(new TextBox(), null);
    List<String> calls = new ArrayList<>();
    SecondValidator second = new SecondValidator(calls);
    validation.addValidator(
        new FirstValidator() {
          boolean changed;

          @Override
          public List<EditorError> validate(Editor<String> editor, String value) {
            if (!changed) {
              changed = true;
              validation.removeValidator(second);
              validation.addValidator(new ThirdValidator(calls));
            }
            return Collections.emptyList();
          }
        });
    validation.addValidator(second);
    assertTrue(validation.validate());
    assertEquals(List.of("second"), calls);
    calls.clear();
    assertTrue(validation.validate());
    assertEquals(List.of("third"), calls);
  }

  private static class TrackingField extends TextBox {
    boolean result = true;
    int registrations;

    @Override
    public boolean validate(boolean show) {
      return result;
    }

    @Override
    public HandlerRegistration addValidationChangedHandler(
        ValidationChangedEvent.ValidationChangedHandler handler) {
      registrations++;
      HandlerRegistration registration = super.addValidationChangedHandler(handler);
      return () -> {
        registrations--;
        registration.removeHandler();
      };
    }
  }

  private static class FirstValidator implements Validator<String> {
    @Override
    public int getPriority() {
      return Priority.MEDIUM;
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
      return Collections.emptyList();
    }
  }

  private static final class SecondValidator extends FirstValidator {
    private final List<String> calls;

    SecondValidator(List<String> calls) {
      this.calls = calls;
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
      calls.add("second");
      return Collections.emptyList();
    }
  }

  private static final class ThirdValidator extends FirstValidator {
    private final List<String> calls;

    ThirdValidator(List<String> calls) {
      this.calls = calls;
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
      calls.add("third");
      return Collections.emptyList();
    }
  }

  private static final class InvalidValidator extends FirstValidator {
    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
      return Collections.singletonList(null);
    }
  }
}
