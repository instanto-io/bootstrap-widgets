package io.instanto.bootstrap.testing.bootstrap3;

import static org.junit.Assert.*;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.HandlerRegistration;
import java.util.ArrayList;
import java.util.List;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.form.validator.GroupValidator;
import org.gwtbootstrap3.client.ui.form.validator.ValidationChangedEvent;
import org.gwtbootstrap3.client.ui.form.validator.ValidationChangedEvent.ValidationChangedHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class ValidationLifecycleTest {
  @Test
  public void attachedAddCanBeRemovedByItsGroupNotification() {
    Field field = new Field();
    GroupValidator group = new GroupValidator();
    RootPanel.get().add(field);
    try {
      group.addValidationChangedHandler(event -> assertTrue(group.remove(field)));
      group.add(field);
      assertEquals(0, field.handlers.size());
      assertFalse(group.remove(field));
    } finally {
      field.removeFromParent();
    }
  }

  @Test
  public void reentrantAddDoesNotCreateASecondSubscription() {
    Field field = new Field();
    GroupValidator group = new GroupValidator();
    RootPanel.get().add(field);
    try {
      group.addValidationChangedHandler(event -> group.add(field));
      group.add(field);
      assertEquals(1, field.handlers.size());
      group.remove(field);
      assertTrue(field.handlers.isEmpty());
    } finally {
      field.removeFromParent();
    }
  }

  @Test
  public void aFieldCanRemoveItselfAndTheNextFieldDuringValidation() {
    Field first = new Field();
    Field next = new Field();
    GroupValidator group = new GroupValidator();
    group.add(first);
    group.add(next);
    next.calls = 0;
    first.result = false;
    first.action =
        () -> {
          assertTrue(group.remove(first));
          assertTrue(group.remove(next));
        };
    assertTrue(group.validate());
    assertEquals(0, next.calls);
    assertTrue(first.handlers.isEmpty());
    assertTrue(next.handlers.isEmpty());
  }

  @Test
  public void nestedValidationDoesNotPublishAnIncompleteOuterPass() {
    Field first = new Field();
    Field second = new Field();
    GroupValidator group = new GroupValidator();
    group.add(first);
    group.add(second);
    List<Boolean> changes = new ArrayList<>();
    group.addValidationChangedHandler(event -> changes.add(event.isValid()));
    second.result = false;
    first.action =
        () -> {
          group.validate(false);
          assertTrue("No notification before the outer pass completes", changes.isEmpty());
        };
    assertFalse(group.validate());
    assertEquals(List.of(false), changes);
  }

  @Test
  public void anOldCallbackCannotChangeAReaddedFieldsState() {
    Field field = new Field();
    GroupValidator group = new GroupValidator();
    group.add(field);
    ValidationChangedHandler stale = field.handlers.get(0);
    group.validate();
    group.remove(field);
    group.add(field);
    List<Boolean> changes = new ArrayList<>();
    group.addValidationChangedHandler(event -> changes.add(event.isValid()));
    stale.onValidationChanged(new ValidationChangedEvent(false));
    assertTrue(changes.isEmpty());
    assertEquals(1, field.handlers.size());
  }

  @Test
  public void fieldChangesAreObservedBeforeExplicitGroupValidation() {
    Field field = new Field();
    GroupValidator group = new GroupValidator();
    group.add(field);
    List<Boolean> changes = new ArrayList<>();
    group.addValidationChangedHandler(event -> changes.add(event.isValid()));
    field.emit(false);
    assertEquals(List.of(false), changes);
  }

  @Test
  public void failedInitialValidationDoesNotLeaveARegistration() {
    Field field = new Field();
    GroupValidator group = new GroupValidator();
    field.action =
        () -> {
          throw new IllegalArgumentException("bad validator");
        };
    assertThrows(IllegalArgumentException.class, () -> group.add(field));
    assertTrue(field.handlers.isEmpty());
    assertFalse(group.remove(field));
    group.add(field);
    assertEquals(1, field.handlers.size());
  }

  @Test
  public void failedValidationRestoresEventDelivery() {
    Field field = new Field();
    GroupValidator group = new GroupValidator();
    group.add(field);
    group.validate();
    field.action =
        () -> {
          throw new IllegalArgumentException("bad validator");
        };
    assertThrows(IllegalArgumentException.class, group::validate);
    List<Boolean> changes = new ArrayList<>();
    group.addValidationChangedHandler(event -> changes.add(event.isValid()));
    field.emit(false);
    assertEquals(List.of(false), changes);
  }

  @Test
  public void failedInitialValidationAfterSelfRemovalUnsubscribesOnlyOnce() {
    Field field = new Field();
    GroupValidator group = new GroupValidator();
    field.action =
        () -> {
          assertTrue(group.remove(field));
          throw new IllegalArgumentException("removed");
        };
    assertThrows(IllegalArgumentException.class, () -> group.add(field));
    assertEquals(1, field.unsubscriptions);
    assertTrue(field.handlers.isEmpty());
  }

  private static final class Field extends TextBox {
    final List<ValidationChangedHandler> handlers = new ArrayList<>();
    boolean result = true;
    int calls;
    int unsubscriptions;
    Runnable action;

    @Override
    public boolean validate(boolean show) {
      calls++;
      Runnable next = action;
      action = null;
      if (next != null) next.run();
      return result;
    }

    @Override
    public HandlerRegistration addValidationChangedHandler(ValidationChangedHandler handler) {
      handlers.add(handler);
      return () -> {
        unsubscriptions++;
        handlers.remove(handler);
      };
    }

    void emit(boolean value) {
      result = value;
      for (ValidationChangedHandler handler : new ArrayList<>(handlers)) {
        handler.onValidationChanged(new ValidationChangedEvent(value));
      }
    }
  }
}
