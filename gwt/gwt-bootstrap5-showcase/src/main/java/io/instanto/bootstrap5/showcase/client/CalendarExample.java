package io.instanto.bootstrap5.showcase.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.extras.fullcalendar.client.ui.FullCalendar;

/** Interactive scheduling example shared by GWT and TeaVM. */
final class CalendarExample {
  static Widget create() {
    FlowPanel content = new FlowPanel(), tools = new FlowPanel(), mount = new FlowPanel();
    tools.setStyleName("d-flex flex-wrap gap-2 mb-3");
    Label status = new Label("Select dates, click an event, or drag and resize an appointment.");
    status.getElement().setId("calendar-status");
    status.setStyleName("alert alert-info mt-3");
    FullCalendar calendar = new FullCalendar();
    calendar.setInitialDate("2026-10-12");
    calendar.addEvent(
        "planning", "Planning session", "2026-10-12T10:00:00", "2026-10-12T11:30:00", false);
    calendar.addEvent(
        "review", "Design review", "2026-10-14T14:00:00", "2026-10-14T15:00:00", false);
    calendar.addEvent("release", "Release window", "2026-10-19", "2026-10-21", true);
    calendar.setListener(
        (action, id, title, start, end) ->
            status.setText(action + ": " + (title.isEmpty() ? start : title + " · " + start)));
    Button add = new Button("Add appointment");
    add.getElement().setId("calendar-add");
    final int[] sequence = {0};
    add.addClickHandler(
        e -> {
          sequence[0]++;
          calendar.addEvent(
              "new-" + sequence[0],
              "New appointment " + sequence[0],
              "2026-10-15T09:00:00",
              "2026-10-15T10:00:00",
              false);
          status.setText(calendar.getEventCount() + " events");
        });
    tools.add(add);
    Button week = new Button("Week view");
    week.getElement().setId("calendar-week");
    week.addClickHandler(e -> calendar.changeView("timeGridWeek"));
    tools.add(week);
    Button reset = new Button("October 2026");
    reset.getElement().setId("calendar-reset");
    reset.addClickHandler(
        e -> {
          calendar.goToDate("2026-10-12");
          calendar.changeView("dayGridMonth");
        });
    tools.add(reset);
    Button recreate = new Button("Detach / reattach");
    recreate.getElement().setId("calendar-recreate");
    recreate.addClickHandler(
        e -> {
          mount.remove(calendar);
          mount.add(calendar);
          status.setText("Calendar reattached with " + calendar.getEventCount() + " events");
        });
    tools.add(recreate);
    mount.add(calendar);
    content.add(tools);
    content.add(mount);
    content.add(status);
    return ShowcasePanels.panel(
        "Team calendar",
        content,
        "FullCalendar calendar = new FullCalendar();\ncalendar.setInitialDate(\"2026-10-12\");\ncalendar.addEvent(\"planning\", \"Planning session\",\n    \"2026-10-12T10:00:00\", \"2026-10-12T11:30:00\", false);\ncalendar.setListener((action, id, title, start, end) -> { /* update model */ });");
  }
}
