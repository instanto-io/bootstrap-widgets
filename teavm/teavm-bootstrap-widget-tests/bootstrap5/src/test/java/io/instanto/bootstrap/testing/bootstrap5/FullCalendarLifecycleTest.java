package io.instanto.bootstrap.testing.bootstrap5;

import static org.junit.Assert.*;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.bootstrap5.extras.fullcalendar.client.ui.FullCalendar;
import io.instanto.webapp.testkit.dom.Dom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class FullCalendarLifecycleTest {
  @Test
  public void preservesDatesEventsAndCallbacksAcrossRemounts() {
    Dom.reset();
    Bootstrap5TestRuntime.initialise();
    Dom.container().setAttribute("id", "calendar-lifecycle");
    RootPanel host = RootPanel.get("calendar-lifecycle");
    FullCalendar calendar = new FullCalendar();
    calendar.setInitialDate("2026-10-12");
    calendar.addEvent("timed", "Planning", "2026-10-12T10:00:00", "2026-10-12T11:00:00", false);
    calendar.addEvent("all-day", "Release", "2026-10-19", "2026-10-21", true);
    int[] clicks = {0};
    calendar.setListener(
        (action, id, title, start, end) -> {
          if (action.equals("click")) {
            clicks[0]++;
            assertEquals("timed", id);
            assertTrue(start.startsWith("2026-10-12T10:00:00"));
          }
        });
    try {
      host.add(calendar);
      Dom.waitFor(
          () -> assertEquals("true", calendar.getElement().getAttribute("data-calendar-ready")),
          10000);
      assertEquals(2, calendar.getEventCount());
      calendar.changeView("timeGridWeek");
      for (int i = 0; i < 3; i++) {
        host.remove(calendar);
        host.add(calendar);
        Dom.waitFor(
            () -> assertEquals("true", calendar.getElement().getAttribute("data-calendar-ready")),
            10000);
        assertEquals(2, calendar.getEventCount());
        assertTrue(calendar.getElement().getInnerText().contains("Planning"));
        clickPlanning(calendar.getElement().unwrap());
        assertEquals(i + 1, clicks[0]);
      }
      calendar.changeView("dayGridMonth");
      assertTrue(calendar.getElement().getInnerText().contains("Release"));
      calendar.removeEvent("timed");
      assertEquals(1, calendar.getEventCount());
      calendar.clearEvents();
      host.remove(calendar);
      host.add(calendar);
      Dom.waitFor(
          () -> assertEquals("true", calendar.getElement().getAttribute("data-calendar-ready")),
          10000);
      assertEquals(0, calendar.getEventCount());
    } finally {
      host.clear();
      Dom.reset();
    }
  }

  @JSBody(
      params = "root",
      script =
          "var e=Array.from(root.querySelectorAll('[role=button],button,a')).find(function(e){return"
              + " e.textContent.includes('Planning');});if(!e)throw new Error('Missing Planning"
              + " event');e.click();")
  private static native void clickPlanning(HTMLElement root);
}
