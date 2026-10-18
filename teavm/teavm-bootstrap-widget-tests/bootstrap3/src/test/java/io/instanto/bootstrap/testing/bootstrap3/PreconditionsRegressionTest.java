package io.instanto.bootstrap.testing.bootstrap3;

import static org.junit.Assert.*;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import org.gwtbootstrap3.client.ui.SimpleCheckBox;
import org.gwtbootstrap3.client.ui.SimpleRadioButton;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.form.error.DefaultErrorHandler;
import org.gwtbootstrap3.client.ui.form.validator.FieldMatchValidator;
import org.gwtbootstrap3.client.ui.form.validator.SizeValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class PreconditionsRegressionTest {
  @Test
  public void wrappingDetachedElementsFailsExplicitly() {
    assertThrows(
        IllegalArgumentException.class,
        () -> SimpleCheckBox.wrap(Document.get().createCheckInputElement()));
    assertThrows(
        IllegalArgumentException.class,
        () -> SimpleRadioButton.wrap(Document.get().createRadioInputElement("group")));
    assertThrows(
        IllegalArgumentException.class,
        () -> TextArea.wrap(Document.get().createTextAreaElement()));
  }

  @Test
  public void wrappingWrongElementTypesDoesNotAttachThem() {
    Element element = Document.get().createDivElement();
    Document.get().getBody().appendChild(element);
    try {
      assertThrows(IllegalArgumentException.class, () -> SimpleCheckBox.wrap(element));
      assertThrows(IllegalArgumentException.class, () -> SimpleRadioButton.wrap(element));
      assertThrows(IllegalArgumentException.class, () -> TextArea.wrap(element));
    } finally {
      element.removeFromParent();
    }
  }

  @Test
  public void wrappingWrongInputTypesIsRejected() {
    Element element = Document.get().createTextInputElement();
    Document.get().getBody().appendChild(element);
    try {
      assertThrows(IllegalArgumentException.class, () -> SimpleCheckBox.wrap(element));
      assertThrows(IllegalArgumentException.class, () -> SimpleRadioButton.wrap(element));
    } finally {
      element.removeFromParent();
    }
  }

  @Test
  public void wrappingAValidElementStillSupportsCleanup() {
    Element element = Document.get().createCheckInputElement();
    Document.get().getBody().appendChild(element);
    SimpleCheckBox box = SimpleCheckBox.wrap(element);
    try {
      assertTrue(box.isAttached());
      box.setValue(true);
      assertTrue(box.getValue());
    } finally {
      RootPanel.detachNow(box);
      element.removeFromParent();
    }
    assertFalse(box.isAttached());
  }

  @Test
  public void nullValidationDependenciesFailExplicitly() {
    assertThrows(NullPointerException.class, () -> new DefaultErrorHandler(null));
    assertThrows(
        NullPointerException.class, () -> new FieldMatchValidator<String>(null, "invalid"));
    assertThrows(
        NullPointerException.class, () -> new FieldMatchValidator<String>(null, new Object[0]));
  }

  @Test
  public void invalidSizeMaximumIsRejectedWithoutChangingThePreviousLimit() {
    SizeValidator<String> validator = new SizeValidator<>(0, 3);
    assertThrows(IllegalArgumentException.class, () -> validator.setMax(-1));
    assertTrue(validator.isValid("abc"));
    assertFalse(validator.isValid("abcd"));
    assertThrows(NullPointerException.class, () -> validator.setMax(null));
    assertTrue(validator.isValid("abc"));
  }

  @Test
  public void zeroMaximumAcceptsOnlyEmptyValues() {
    SizeValidator<String> validator = new SizeValidator<>(0, 0);
    assertTrue(validator.isValid(""));
    assertFalse(validator.isValid("a"));
  }

  @Test
  public void sizeConstructionDoesNotInvokeSubclassSetters() {
    class Probe extends SizeValidator<String> {
      final Object ready = new Object();

      Probe() {
        super(0, 3);
      }

      @Override
      public void setMin(Integer value) {
        assertNotNull(ready);
        super.setMin(value);
      }

      @Override
      public void setMax(Integer value) {
        assertNotNull(ready);
        super.setMax(value);
      }
    }
    Probe value = new Probe();
    assertTrue(value.isValid("abc"));
    value.setMax(1);
    assertFalse(value.isValid("abc"));
  }
}
