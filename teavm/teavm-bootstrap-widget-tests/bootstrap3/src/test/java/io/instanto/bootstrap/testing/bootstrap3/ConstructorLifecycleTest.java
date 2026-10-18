package io.instanto.bootstrap.testing.bootstrap3;

import static org.junit.Assert.*;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.HandlerRegistration;
import io.instanto.webapp.testkit.dom.Dom;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.base.form.AbstractForm;
import org.gwtbootstrap3.client.ui.base.mixin.DefaultValidatorMixin;
import org.gwtbootstrap3.client.ui.theme.Theme;
import org.gwtbootstrap3.client.ui.theme.ThemeSwitcher;
import org.gwtbootstrap3.client.ui.theme.Themes;
import org.junit.*;
import org.junit.runner.RunWith;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class ConstructorLifecycleTest {
  private RootPanel host;

  @Before
  public void prepare() {
    Dom.reset();
    Bootstrap3TestRuntime.initialise();
    Dom.container().setAttribute("id", "constructor-lifecycle");
    host = RootPanel.get("constructor-lifecycle");
  }

  @After
  public void cleanup() {
    host.clear();
    Dom.reset();
  }

  @Test
  public void themeConstructionDoesNotCallSubclassRebuild() {
    SafeThemeSwitcher widget = new SafeThemeSwitcher();
    assertEquals(0, widget.calls);
    widget.rebuild();
    assertEquals(1, widget.calls);
  }

  private static class SafeThemeSwitcher extends ThemeSwitcher {
    private final Object initialized = new Object();
    int calls;

    @Override
    public void rebuild() {
      assertNotNull("Subclass fields must exist before rebuild", initialized);
      calls++;
      super.rebuild();
    }
  }

  @Test
  public void themeSubscriptionFollowsAttachmentAndResynchronizesOnRemount() {
    String stylesheet = Document.get().getElementById(Themes.LINK_ID).getAttribute("href");
    Theme first = new Theme("ctor-first", "First", stylesheet, false);
    Theme second = new Theme("ctor-second", "Second", stylesheet, false);
    Themes.register(first);
    Themes.register(second);
    Themes.apply(first);
    ThemeSwitcher widget = new ThemeSwitcher("Theme");
    String initial = widget.getElement().getInnerText();
    Themes.apply(second);
    assertEquals(
        "Unattached widgets must not receive global callbacks",
        initial,
        widget.getElement().getInnerText());
    for (int i = 0; i < 2; i++) {
      host.add(widget);
      assertTrue(widget.getElement().getInnerText().contains("Theme: Second"));
      Themes.apply(first);
      assertTrue(widget.getElement().getInnerText().contains("Theme: First"));
      widget.removeFromParent();
      String detached = widget.getElement().getInnerText();
      Themes.apply(second);
      assertEquals(detached, widget.getElement().getInnerText());
    }
  }

  @Test
  public void validatorConstructionDoesNotCallSubclassSetupButStillBindsEvents() {
    TextBox input = new TextBox();
    SafeValidation validation = new SafeValidation(input);
    assertEquals(0, validation.setupCalls);
    ValueChangeEvent.fire(input, "changed");
    assertEquals(1, validation.validations);
    DomEvent.fireNativeEvent(Document.get().createBlurEvent(), input);
    assertEquals(1, validation.blurs);
    validation.setupBlurValidation().removeHandler();
    assertEquals(1, validation.setupCalls);
  }

  private static class SafeValidation extends DefaultValidatorMixin<TextBox, String> {
    private final Object initialized = new Object();
    int setupCalls, validations, blurs;

    SafeValidation(TextBox input) {
      super(input, null);
    }

    @Override
    public HandlerRegistration setupBlurValidation() {
      assertNotNull("Subclass fields must exist before blur setup", initialized);
      setupCalls++;
      return super.setupBlurValidation();
    }

    @Override
    protected HandlerRegistration setupValueChangeValidation() {
      assertNotNull("Subclass fields must exist before value setup", initialized);
      setupCalls++;
      return super.setupValueChangeValidation();
    }

    @Override
    protected void onValidationBlur() {
      assertNotNull(initialized);
      blurs++;
      super.onValidationBlur();
    }

    @Override
    public boolean validate(boolean show) {
      validations++;
      return true;
    }
  }

  @Test
  public void formConstructionDoesNotReadSubclassTargetAndTargetsAreUnique() {
    SafeForm first = new SafeForm();
    SafeForm second = new SafeForm();
    assertFalse(first.getTarget().isEmpty());
    assertNotEquals(first.getTarget(), second.getTarget());
  }

  @Test
  public void formDoesNotOverwriteAnExistingTargetWhenCreatingItsIframe() {
    com.google.gwt.dom.client.FormElement element = Document.get().createFormElement();
    element.setTarget("existing-target");
    class WrappedForm extends AbstractForm {
      WrappedForm(boolean createFrame) {
        super(element, createFrame);
      }
    }
    assertThrows(IllegalArgumentException.class, () -> new WrappedForm(true));
    assertEquals("existing-target", element.getTarget());
    assertEquals("existing-target", new WrappedForm(false).getTarget());
  }

  private static class SafeForm extends AbstractForm {
    private final Object initialized = new Object();

    @Override
    public String getTarget() {
      assertNotNull("Subclass fields must exist before reading target", initialized);
      return super.getTarget();
    }
  }

  @Test
  public void tabConstructionDoesNotCallSubclassActiveGetter() {
    class Probe extends org.gwtbootstrap3.client.ui.TabListItem {
      private final Object initialized = new Object();

      @Override
      public boolean isActive() {
        assertNotNull(initialized);
        return super.isActive();
      }
    }
    Probe tab = new Probe();
    assertFalse(tab.isActive());
    assertEquals("false", tab.getElement().getFirstChildElement().getAttribute("aria-selected"));
  }
}
