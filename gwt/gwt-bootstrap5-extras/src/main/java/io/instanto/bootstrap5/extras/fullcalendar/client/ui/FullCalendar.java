package io.instanto.bootstrap5.extras.fullcalendar.client.ui;

import io.instanto.bootstrap5.extras.base.client.PluginWidget;
import java.util.ArrayList;
import java.util.List;

/** FullCalendar 7 with its official Bootstrap 5 theme. Configure before attaching. */
public class FullCalendar extends PluginWidget<FullCalendarJs.Calendar> {
  @FunctionalInterface
  public interface Listener {
    void onEvent(String action, String id, String title, String start, String end);
  }

  private String date, view = "dayGridMonth";
  private boolean editable = true;
  private Listener listener = (a, id, t, s, e) -> {};
  private final List<FullCalendarJs.EventInput> events = new ArrayList<>();

  public FullCalendar() {
    getElement().setAttribute("data-fullcalendar-widget", "true");
  }

  public void setInitialDate(String date) {
    requireDetached();
    this.date = date;
  }

  public void setInitialView(String view) {
    requireDetached();
    this.view = view;
  }

  public void setEditable(boolean value) {
    requireDetached();
    editable = value;
  }

  public void setListener(Listener listener) {
    this.listener = listener == null ? (a, id, t, s, e) -> {} : listener;
  }

  public void addEvent(String id, String title, String start, String end, boolean allDay) {
    FullCalendarJs.EventInput event = FullCalendarJs.event(id, title, start, end, allDay);
    if (plugin() != null) plugin().addEvent(event);
    else events.add(event);
  }

  public void removeEvent(String id) {
    if (plugin() == null) throw new IllegalStateException("Calendar must be attached");
    FullCalendarJs.Event e = plugin().getEventById(id);
    if (e != null) e.remove();
  }

  public void clearEvents() {
    events.clear();
    if (plugin() != null) plugin().removeAllEvents();
  }

  public int getEventCount() {
    return plugin() == null ? events.size() : plugin().getEvents().length;
  }

  public void next() {
    if (plugin() != null) plugin().next();
  }

  public void previous() {
    if (plugin() != null) plugin().prev();
  }

  public void today() {
    if (plugin() != null) plugin().today();
  }

  public void changeView(String view) {
    this.view = view;
    if (plugin() != null) plugin().changeView(view);
  }

  public void goToDate(String date) {
    this.date = date;
    if (plugin() != null) plugin().gotoDate(date);
  }

  public void updateSize() {
    if (plugin() != null) plugin().updateSize();
  }

  @Override
  protected void whenReady(Runnable action) {
    FullCalendarJs.whenReady(action);
  }

  @Override
  protected FullCalendarJs.Calendar createPlugin() {
    return FullCalendarJs.create(
        getElement(),
        date,
        view,
        editable,
        events.toArray(new FullCalendarJs.EventInput[0]),
        (a, id, t, s, e) -> listener.onEvent(a, id, t, s, e));
  }

  @Override
  protected void afterCreate() {
    plugin().render();
    getElement().setAttribute("data-calendar-ready", "true");
  }

  @Override
  protected void captureState() {
    date = plugin().getDate().toISOString();
    view = plugin().getView().getType();
    events.clear();
    for (FullCalendarJs.Event e : plugin().getEvents())
      events.add(
          FullCalendarJs.event(
              e.getId(), e.getTitle(), e.getStartStr(), e.getEndStr(), e.getAllDay()));
  }

  @Override
  protected void destroyPlugin(FullCalendarJs.Calendar p) {
    p.destroy();
    getElement().removeAttribute("data-calendar-ready");
  }
}
