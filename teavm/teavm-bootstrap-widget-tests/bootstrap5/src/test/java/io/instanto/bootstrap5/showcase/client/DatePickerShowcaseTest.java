package io.instanto.bootstrap5.showcase.client;

import static org.junit.Assert.*;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.Bootstrap5;
import io.instanto.bootstrap5.client.Bootstrap5Resources;
import io.instanto.bootstrap5.extras.datepicker.client.ui.DatePicker;
import io.instanto.webapp.testkit.dom.Dom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class DatePickerShowcaseTest {
  @Test
  public void showcaseReceivesDatesClearingAndChangesAfterRemount() {
    Dom.reset();
    Bootstrap5Resources.setAssetBase("/resources/META-INF/bootstrap5-assets/");
    boolean[] loaded = {false};
    Bootstrap5.initialise(() -> loaded[0] = true);
    Dom.waitFor(() -> assertTrue(loaded[0]), 10000);
    Dom.container().setAttribute("id", "datepicker5-showcase");
    RootPanel host = RootPanel.get("datepicker5-showcase");
    try {
      Widget page = new IntegrationPages().createIntegrationSections("datePicker");
      host.add(page);
      DatePicker picker = findPicker(page);
      assertNotNull(picker);
      Dom.waitFor(() -> assertTrue(picker.isReady()), 10000);
      HTMLElement input = Dom.container().querySelector("input[placeholder='Pick a date']").cast();
      HTMLElement echo = Dom.container().querySelector("#date-picker-value").cast();
      type(input, "2031-02-17");
      assertEquals("2031-02-17", echo.getAttribute("data-value"));
      assertEquals("true", echo.getAttribute("data-source-match"));
      assertTrue(echo.getTextContent().contains("Picked"));
      clickRemount();
      assertNull(Dom.container().querySelector("input[placeholder='Pick a date']"));
      clickRemount();
      Dom.waitFor(() -> assertTrue(picker.isReady()), 10000);
      type(input, "2031-11-02");
      assertEquals("2031-11-02", echo.getAttribute("data-value"));
      type(input, "");
      assertEquals("null", echo.getAttribute("data-value"));
      assertEquals("Cleared.", echo.getTextContent());
    } finally {
      host.clear();
      Dom.reset();
    }
  }

  private static DatePicker findPicker(Widget widget) {
    if (widget instanceof DatePicker) return (DatePicker) widget;
    if (widget instanceof HasWidgets) {
      for (Widget child : (HasWidgets) widget) {
        DatePicker picker = findPicker(child);
        if (picker != null) return picker;
      }
    }
    return null;
  }

  @JSBody(
      params = {"input", "value"},
      script = "input.value=value; input.dispatchEvent(new Event('change', {bubbles:true}));")
  private static native void type(HTMLElement input, String value);

  @JSBody(
      script =
          "Array.from(document.querySelectorAll('button')).find(b => b.textContent === 'Remove / restore date picker').click();")
  private static native void clickRemount();
}
