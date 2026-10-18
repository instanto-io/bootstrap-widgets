package io.instanto.bootstrap.testing.bootstrap5;

import static org.junit.Assert.*;

import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.dashboard.client.ui.DashboardTile;
import io.instanto.bootstrap5.extras.datepicker.client.ui.DatePicker;
import io.instanto.bootstrap5.extras.slider.client.ui.Slider;
import io.instanto.bootstrap5.extras.sortable.client.ui.SortableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class ExtrasConstructorLifecycleTest {
  @Test
  public void dashboardConstructionDoesNotReadSubclassElement() {
    class Probe extends DashboardTile {
      private final Object initialized = new Object();

      Probe() {
        super("tile", 1, 2, 3, 4);
      }

      @Override
      public Element getElement() {
        assertNotNull("Element override ran before subclass initialization", initialized);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertEquals("3", widget.getElement().getAttribute("gs-w"));
    assertSame(widget, widget.getContent().getParent());
  }

  @Test
  public void datePickerConstructionDoesNotReadSubclassElement() {
    class Probe extends DatePicker {
      private final Object initialized = new Object();

      Probe() {
        super("Pick a date");
      }

      @Override
      public Element getElement() {
        assertNotNull("Element override ran before subclass initialization", initialized);
        return super.getElement();
      }

      @Override
      public void setPlaceholder(String value) {
        assertNotNull(initialized);
        super.setPlaceholder(value);
      }
    }
    Probe widget = new Probe();
    assertEquals("Pick a date", widget.getPlaceholder());
    assertEquals(2, widget.getWidgetCount());
  }

  @Test
  public void sliderConstructionDoesNotReadSubclassElement() {
    class Probe extends Slider {
      private final Object initialized = new Object();

      Probe() {
        super(10, 30);
      }

      @Override
      public Element getElement() {
        assertNotNull("Element override ran before subclass initialization", initialized);
        return super.getElement();
      }

      @Override
      public void setMin(double value) {
        assertNotNull(initialized);
        super.setMin(value);
      }

      @Override
      public void setMax(double value) {
        assertNotNull(initialized);
        super.setMax(value);
      }

      @Override
      public void setValue(double value) {
        assertNotNull(initialized);
        super.setValue(value);
      }
    }
    Probe widget = new Probe();
    assertEquals(10, widget.getValue(), 0);
    assertEquals(30, widget.getMax(), 0);
  }

  @Test
  public void sortableConstructionDoesNotReadSubclassElement() {
    class Probe extends SortableList {
      private final Object initialized = new Object();

      Probe() {
        super();
      }

      @Override
      public Element getElement() {
        assertNotNull("Element override ran before subclass initialization", initialized);
        return super.getElement();
      }
    }
    Probe widget = new Probe();
    assertTrue(widget.getElement().getClassName().contains("list-group"));
  }
}
