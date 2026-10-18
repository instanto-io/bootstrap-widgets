package io.instanto.bootstrap.testing.bootstrap5;

import static org.junit.Assert.*;

import com.google.gwt.dom.client.Element;
import io.instanto.webapp.testkit.dom.Dom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class ElementConstructorLifecycleTest {
  @Before
  public void prepare() {
    Dom.reset();
    Bootstrap5TestRuntime.initialise();
  }

  @Test
  public void alertDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.Alert {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("Alert read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("alert", widget.getElement().getAttribute("role"));
  }

  @Test
  public void buttonGroupDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.ButtonGroup {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("ButtonGroup read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("group", widget.getElement().getAttribute("role"));
  }

  @Test
  public void buttonToolBarDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.ButtonToolBar {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("ButtonToolBar read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("toolbar", widget.getElement().getAttribute("role"));
  }

  @Test
  public void carouselDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.Carousel {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("Carousel read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("carousel", widget.getElement().getAttribute("data-bs-ride"));
  }

  @Test
  public void carouselControlDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.CarouselControl {
      private final Object ready = new Object();

      Probe() {
        super("target", false);
      }

      @Override
      public Element getElement() {
        assertNotNull("CarouselControl read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("#target", widget.getElement().getAttribute("data-bs-target"));
  }

  @Test
  public void carouselIndicatorDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.CarouselIndicator {
      private final Object ready = new Object();

      Probe() {
        super("target", 2);
      }

      @Override
      public Element getElement() {
        assertNotNull("CarouselIndicator read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("Slide 3", widget.getElement().getAttribute("aria-label"));
  }

  @Test
  public void checkBoxDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.CheckBox {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("CheckBox read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertTrue(widget.getElement().hasClassName("form-check"));
  }

  @Test
  public void checkBoxButtonDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.CheckBoxButton {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("CheckBoxButton read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertTrue(widget.getElement().hasClassName("d-inline-block"));
  }

  @Test
  public void dropDownMenuDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.DropDownMenu {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("DropDownMenu read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("menu", widget.getElement().getAttribute("role"));
  }

  @Test
  public void modalDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.Modal {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("Modal read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("dialog", widget.getElement().getAttribute("role"));
  }

  @Test
  public void navbarCollapseButtonDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.NavbarCollapseButton {
      private final Object ready = new Object();

      Probe() {
        super("target");
      }

      @Override
      public Element getElement() {
        assertNotNull("NavbarCollapseButton read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("#target", widget.getElement().getAttribute("data-bs-target"));
  }

  @Test
  public void navbarFormDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.NavbarForm {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("NavbarForm read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("search", widget.getElement().getAttribute("role"));
  }

  @Test
  public void offcanvasDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.Offcanvas {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("Offcanvas read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("-1", widget.getElement().getAttribute("tabindex"));
  }

  @Test
  public void placeholderDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.Placeholder {
      private final Object ready = new Object();

      Probe() {
        super(5);
      }

      @Override
      public Element getElement() {
        assertNotNull("Placeholder read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("true", widget.getElement().getAttribute("aria-hidden"));
  }

  @Test
  public void placeholderContainerDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.PlaceholderContainer {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("PlaceholderContainer read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("true", widget.getElement().getAttribute("aria-hidden"));
  }

  @Test
  public void progressBarDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.ProgressBar {
      private final Object ready = new Object();

      Probe() {
        super(40);
      }

      @Override
      public Element getElement() {
        assertNotNull("ProgressBar read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("40", widget.getElement().getAttribute("aria-valuenow"));
  }

  @Test
  public void radioDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.Radio {
      private final Object ready = new Object();

      Probe() {
        super("group");
      }

      @Override
      public Element getElement() {
        assertNotNull("Radio read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertTrue(widget.getElement().hasClassName("form-check"));
  }

  @Test
  public void radioButtonDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.RadioButton {
      private final Object ready = new Object();

      Probe() {
        super("group");
      }

      @Override
      public Element getElement() {
        assertNotNull("RadioButton read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertTrue(widget.getElement().hasClassName("d-inline-block"));
  }

  @Test
  public void submitButtonDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.SubmitButton {
      private final Object ready = new Object();

      Probe() {
        super("Save");
      }

      @Override
      public Element getElement() {
        assertNotNull("SubmitButton read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("submit", widget.getElement().getAttribute("type"));
  }

  @Test
  public void tabPaneDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.TabPane {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("TabPane read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("tabpanel", widget.getElement().getAttribute("role"));
  }

  @Test
  public void toastDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.Toast {
      private final Object ready = new Object();

      Probe() {
        super("Title", "Message");
      }

      @Override
      public Element getElement() {
        assertNotNull("Toast read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("status", widget.getElement().getAttribute("role"));
  }

  @Test
  public void toastContainerDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.ToastContainer {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("ToastContainer read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("polite", widget.getElement().getAttribute("aria-live"));
  }

  @Test
  public void verticalButtonGroupDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.VerticalButtonGroup {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("VerticalButtonGroup read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("group", widget.getElement().getAttribute("role"));
  }

  @Test
  public void anchorListItemDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.AnchorListItem {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("AnchorListItem read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("li", widget.getElement().getTagName().toLowerCase());
  }

  @Test
  public void closeButtonDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends io.instanto.bootstrap5.client.ui.base.button.CloseButton {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("CloseButton read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("button", widget.getElement().getTagName().toLowerCase());
  }

  @Test
  public void buttonFactoryIsReplacedByExplicitElementConstruction() {
    class Probe extends io.instanto.bootstrap5.client.ui.Button {
      @Override
      protected Element createElement() {
        throw new AssertionError("Subclass factory invoked during construction");
      }
    }
    class CustomButton extends io.instanto.bootstrap5.client.ui.Button {
      CustomButton() {
        super(com.google.gwt.dom.client.Document.get().createAnchorElement());
      }
    }
    assertEquals("BUTTON", new Probe().getElement().getTagName().toUpperCase());
    CustomButton custom = new CustomButton();
    assertEquals("A", custom.getElement().getTagName().toUpperCase());
    custom.setText("Custom");
    assertEquals("Custom", custom.getText());
  }

  @Test
  public void choiceButtonChangeHandlersSurviveRemountsWithoutDuplicates() {
    Dom.container().setAttribute("id", "choice-constructor-lifecycle");
    com.google.gwt.user.client.ui.RootPanel host =
        com.google.gwt.user.client.ui.RootPanel.get("choice-constructor-lifecycle");
    try {
      for (io.instanto.bootstrap5.client.ui.CheckBox choice :
          new io.instanto.bootstrap5.client.ui.CheckBox[] {
            new io.instanto.bootstrap5.client.ui.CheckBoxButton(),
            new io.instanto.bootstrap5.client.ui.RadioButton("constructor-choice")
          }) {
        int[] changes = {0};
        choice.addChangeHandler(event -> changes[0]++);
        for (int cycle = 0; cycle < 3; cycle++) {
          host.add(choice);
          choice.setValue(false);
          Dom.click(Dom.within(choice.getElement().unwrap()).find("label"));
          assertTrue(choice.getValue());
          assertTrue(
              Dom.within(choice.getElement().unwrap())
                  .find("label")
                  .getAttribute("class")
                  .contains("active"));
          assertEquals(cycle + 1, changes[0]);
          choice.removeFromParent();
        }
      }
    } finally {
      host.clear();
    }
  }

  @Test
  public void typedAlertConstructionRemovesTheDefaultWarningStyle() {
    for (io.instanto.bootstrap5.client.ui.constants.AlertType type :
        io.instanto.bootstrap5.client.ui.constants.AlertType.values()) {
      io.instanto.bootstrap5.client.ui.Alert alert =
          new io.instanto.bootstrap5.client.ui.Alert("Message", type);
      assertEquals("Message", alert.getText());
      assertEquals(type, alert.getType());
      assertEquals(
          type == io.instanto.bootstrap5.client.ui.constants.AlertType.WARNING,
          alert.getElement().hasClassName("alert-warning"));
    }
  }
}
