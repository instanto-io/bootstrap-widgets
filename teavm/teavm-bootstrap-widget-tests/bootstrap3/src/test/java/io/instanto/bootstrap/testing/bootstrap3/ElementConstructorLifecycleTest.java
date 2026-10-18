package io.instanto.bootstrap.testing.bootstrap3;

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
    Bootstrap3TestRuntime.initialise();
  }

  @Test
  public void carouselDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.Carousel {
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
    assertEquals("carousel", widget.getElement().getAttribute("data-ride"));
  }

  @Test
  public void carouselInnerDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.CarouselInner {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("CarouselInner read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("listbox", widget.getElement().getAttribute("role"));
  }

  @Test
  public void checkBoxDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.CheckBox {
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
    assertTrue(widget.getElement().hasClassName("checkbox"));
  }

  @Test
  public void checkBoxButtonDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.CheckBoxButton {
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
    assertTrue(widget.getElement().hasClassName("btn"));
  }

  @Test
  public void dropDownMenuDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.DropDownMenu {
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
  public void iconStackDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.IconStack {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("IconStack read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertTrue(widget.getElement().hasClassName("fa-stack"));
  }

  @Test
  public void inlineCheckBoxDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.InlineCheckBox {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("InlineCheckBox read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertTrue(widget.getElement().hasClassName("checkbox-inline"));
  }

  @Test
  public void inlineHelpBlockDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.InlineHelpBlock {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("InlineHelpBlock read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertTrue(widget.getElement().hasClassName("help-block"));
  }

  @Test
  public void inlineRadioDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.InlineRadio {
      private final Object ready = new Object();

      Probe() {
        super("group");
      }

      @Override
      public Element getElement() {
        assertNotNull("InlineRadio read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertTrue(widget.getElement().hasClassName("radio-inline"));
  }

  @Test
  public void modalDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.Modal {
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
  public void navbarDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.Navbar {
      private final Object ready = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("Navbar read subclass state during construction", ready);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("navigation", widget.getElement().getAttribute("role"));
  }

  @Test
  public void radioDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.Radio {
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
    assertTrue(widget.getElement().hasClassName("radio"));
  }

  @Test
  public void radioButtonDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.RadioButton {
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
    assertTrue(widget.getElement().hasClassName("btn"));
  }

  @Test
  public void anchorListItemDoesNotReadSubclassElementBeforeInitialization() {
    class Probe extends org.gwtbootstrap3.client.ui.AnchorListItem {
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
    class Probe extends org.gwtbootstrap3.client.ui.base.button.CloseButton {
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
    class Probe extends org.gwtbootstrap3.client.ui.Button {
      @Override
      protected Element createElement() {
        throw new AssertionError("Subclass factory invoked during construction");
      }
    }
    class CustomButton extends org.gwtbootstrap3.client.ui.Button {
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
  public void markdownEditorBindsInputOnlyWhileAttached() {
    class Probe extends org.gwtbootstrap3.extras.markdown.client.ui.MarkdownEditor {
      private final Object ready = new Object();

      @Override
      public Element getElement() {
        assertNotNull("Early markdown element hook", ready);
        return super.getElement();
      }

      @Override
      public String getValue() {
        assertNotNull("Early markdown value hook", ready);
        return super.getValue();
      }
    }
    Dom.container().setAttribute("id", "markdown-constructor-lifecycle");
    com.google.gwt.user.client.ui.RootPanel host =
        com.google.gwt.user.client.ui.RootPanel.get("markdown-constructor-lifecycle");
    Probe editor = new Probe();
    int[] changes = {0};
    editor.addValueChangeHandler(event -> changes[0]++);
    try {
      for (int cycle = 0; cycle < 3; cycle++) {
        host.add(editor);
        dispatchVisualInput(editor.getElement().unwrap());
        assertEquals(cycle + 1, changes[0]);
        editor.removeFromParent();
        dispatchVisualInput(editor.getElement().unwrap());
        assertEquals(cycle + 1, changes[0]);
      }
    } finally {
      host.clear();
    }
  }

  @org.teavm.jso.JSBody(
      params = {"root"},
      script =
          "var visual = root.querySelector('[contenteditable]'); visual.innerHTML = '<p>Changed</p>'; visual.dispatchEvent(new Event('input', {bubbles: true}));")
  private static native void dispatchVisualInput(org.teavm.jso.dom.html.HTMLElement root);
}
