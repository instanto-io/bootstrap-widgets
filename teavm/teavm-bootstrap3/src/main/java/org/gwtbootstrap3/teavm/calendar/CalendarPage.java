package org.gwtbootstrap3.teavm.calendar;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import org.gwtbootstrap3.client.ui.Button;

/** Live Bootstrap 3 calendar showcase, including native lifecycle behavior. */
public final class CalendarPage extends FlowPanel {
  public CalendarPage() {
    add(
        new HTML(
            "<h2>FullCalendar <small>Bootstrap 3 scheduling</small></h2><p>Select dates, click"
                + " events, or drag and resize appointments.</p>"));
    FlowPanel tools = new FlowPanel(), mount = new FlowPanel();
    tools.getElement().getStyle().setProperty("marginBottom", "15px");
    Label status = new Label("Ready");
    status.getElement().setId("calendar-status");
    status.setStyleName("alert alert-info");
    CalendarWidget calendar = new CalendarWidget();
    calendar.setListener((a, t, d) -> status.setText(a + ": " + t + " " + d));
    final int[] n = {0};
    Button add = new Button("Add appointment");
    add.getElement().setId("calendar-add");
    add.addClickHandler(
        e -> {
          calendar.addAppointment(Integer.toString(++n[0]));
          status.setText(calendar.getEventCount() + " events");
        });
    tools.add(add);
    Button week = new Button("Week view");
    week.getElement().setId("calendar-week");
    week.addClickHandler(e -> calendar.week());
    tools.add(week);
    Button reset = new Button("October 2026");
    reset.getElement().setId("calendar-reset");
    reset.addClickHandler(e -> calendar.reset());
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
    add(tools);
    mount.add(calendar);
    add(mount);
    add(status);
  }
}
