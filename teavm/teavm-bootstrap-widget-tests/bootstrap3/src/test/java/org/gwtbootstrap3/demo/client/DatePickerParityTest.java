package org.gwtbootstrap3.demo.client;

import static org.junit.Assert.*;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.webapp.testkit.dom.Dom;
import java.util.Date;
import org.gwtbootstrap3.client.Bootstrap3;
import org.gwtbootstrap3.client.Bootstrap3Resources;
import org.gwtbootstrap3.extras.datepicker.client.ui.DatePicker;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.constants.DatePickerDayOfWeek;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.constants.DatePickerLanguage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class DatePickerParityTest {
  private RootPanel host;

  @Before
  public void prepare() {
    Dom.reset();
    Bootstrap3Resources.setBase("/resources/META-INF/bootstrap3-assets/css/");
    boolean[] loaded = {false};
    Bootstrap3.initialise(() -> loaded[0] = true);
    Dom.waitFor(() -> assertTrue(loaded[0]), 10000);
    Dom.container().setAttribute("id", "datepicker-parity");
    host = RootPanel.get("datepicker-parity");
  }

  @After
  public void cleanup() {
    host.clear();
    Dom.reset();
  }

  @Test
  public void calendarValuesLimitsAndListenersSurviveReloadAndRemount() {
    DatePicker picker = new DatePicker();
    assertNull(picker.getValue());
    picker.show();
    picker.hide();
    Date winter = new Date(131, 1, 17);
    picker.setValue(winter);
    picker.setStartDate(new Date(131, 1, 1));
    picker.setEndDate(new Date(131, 1, 28));
    picker.setDaysOfWeekDisabled(DatePickerDayOfWeek.SUNDAY);
    int[] values = {0, 0};
    picker.addValueChangeHandler(
        event -> {
          assertSame(picker, event.getSource());
          values[0]++;
        });
    picker.addValueChangeHandler(event -> values[1]++);
    int[] events = new int[6];
    picker.addShowHandler(event -> events[0]++);
    picker.addHideHandler(event -> events[1]++);
    picker.addChangeDateHandler(event -> events[2]++);
    picker.addChangeMonthHandler(event -> events[3]++);
    picker.addChangeYearHandler(event -> events[4]++);
    picker.addClearDateHandler(event -> events[5]++);
    host.add(picker);
    picker.removeFromParent();
    host.add(picker);
    Dom.waitFor(() -> assertTrue(ready(picker.getElement().unwrap())), 10000);
    assertEquals(winter, picker.getValue());
    assertEquals("02/17/2031", picker.getBaseValue());
    assertTrue(hasEndDate(picker.getElement().unwrap()));
    picker.clearEndDate();
    assertFalse(hasEndDate(picker.getElement().unwrap()));
    picker.clearStartDate();
    picker.setDaysOfWeekDisabled((DatePickerDayOfWeek[]) null);
    picker.show();
    picker.hide();
    assertEquals(1, events[0]);
    assertEquals(1, events[1]);
    type(picker.getElement().unwrap(), "07/17/2031");
    assertEquals(new Date(131, 6, 17), picker.getValue());
    assertEquals(1, values[0]);
    assertEquals(values[0], values[1]);
    trigger(picker.getElement().unwrap(), "changeDate");
    assertEquals(1, values[0]);
    trigger(picker.getElement().unwrap(), "changeMonth");
    trigger(picker.getElement().unwrap(), "changeYear");
    assertEquals(1, events[3]);
    assertEquals(1, events[4]);
    picker.setValue(null, true);
    assertNull(picker.getValue());
    assertEquals(2, values[0]);
    picker.setValue(winter);
    picker.setLanguage(DatePickerLanguage.FR);
    picker.setFormat("dd/mm/yyyy");
    picker.reload();
    assertEquals("fr", language(picker.getElement().unwrap()));
    assertEquals("17/02/2031", picker.getBaseValue());
    for (int i = 0; i < 3; i++) {
      picker.removeFromParent();
      assertFalse(ready(picker.getElement().unwrap()));
      host.add(picker);
      Dom.waitFor(() -> assertTrue(ready(picker.getElement().unwrap())), 10000);
      assertEquals(winter, picker.getValue());
    }
    type(picker.getElement().unwrap(), "");
    assertNull(picker.getValue());
    trigger(picker.getElement().unwrap(), "clearDate");
    assertTrue(events[5] > 0);
    assertEquals(values[0], values[1]);
    assertTrue(events[2] > 0);
    picker.getTextBox().setValue("invalid");
    assertNull(picker.getValue());
  }

  @Test
  public void showcaseLogsTheJavaCallbacksIncludingClearing() {
    assertNull(ExtrasPages.forToken("unknown"));
    ShowcaseScripts.inject();
    host.add(ExtrasPages.forToken("datePicker"));
    Dom.waitFor(() -> assertEquals("70px", bodyPadding()), 10000);
    HTMLElement input = Dom.container().querySelector("#event-date-picker").cast();
    Dom.waitFor(() -> assertTrue(ready(input)), 10000);
    type(input, "02/17/2031");
    trigger(input, "changeDate");
    HTMLElement log = Dom.container().querySelector("#date-picker-events").cast();
    assertEquals("2031-02-17", log.getAttribute("data-value"));
    assertEquals("true", log.getAttribute("data-source-match"));
    trigger(input, "changeMonth");
    trigger(input, "changeYear");
    clickButton("Add/Remove");
    assertNull(Dom.container().querySelector("#event-date-picker"));
    clickButton("Add/Remove");
    Dom.waitFor(() -> assertTrue(ready(input)), 10000);
    clickButton("Set End Date");
    clickButton("Clear End Date");
    clickButton("Set Start Date");
    clickButton("Clear Start Date");
    type(input, "11/02/2031");
    assertEquals("2031-11-02", log.getAttribute("data-value"));
    type(input, "");
    trigger(input, "clearDate");
    assertEquals("null", log.getAttribute("data-value"));
    assertTrue(log.getTextContent().contains("Clear Date Event Fired! (null)"));
  }

  @JSBody(script = "return getComputedStyle(document.body).paddingTop;")
  private static native String bodyPadding();

  @JSBody(
      params = "text",
      script =
          "Array.from(document.querySelectorAll('button')).find(b => b.textContent === text).click();")
  private static native void clickButton(String text);

  @JSBody(
      params = "input",
      script = "return !!(window.jQuery && window.jQuery(input).data('datepicker'));")
  private static native boolean ready(HTMLElement input);

  @JSBody(
      params = "input",
      script =
          "return Number.isFinite(window.jQuery(input).data('datepicker').o.endDate.getTime ? window.jQuery(input).data('datepicker').o.endDate.getTime() : window.jQuery(input).data('datepicker').o.endDate);")
  private static native boolean hasEndDate(HTMLElement input);

  @JSBody(params = "input", script = "return window.jQuery(input).data('datepicker').o.language;")
  private static native String language(HTMLElement input);

  @JSBody(
      params = {"input", "value"},
      script =
          "input.value=value; window.jQuery(input).datepicker('update'); input.dispatchEvent(new Event('change', {bubbles:true}));")
  private static native void type(HTMLElement input, String value);

  @JSBody(
      params = {"input", "name"},
      script = "window.jQuery(input).trigger(name);")
  private static native void trigger(HTMLElement input, String name);
}
