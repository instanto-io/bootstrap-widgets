package io.instanto.bootstrap5.extras.fullcalendar.client.ui;

import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.fullcalendar.client.FullCalendarResources;
import jsinterop.annotations.*;
import jsinterop.base.*;

/** Typed native boundary shared by the GWT and TeaVM builds. */
final class FullCalendarJs {
  private FullCalendarJs() {}

  static void whenReady(Runnable r) {
    FullCalendarResources.whenReady(() -> Js.global().get("FullCalendar") != null, r);
  }

  static Calendar create(
      Element element,
      String date,
      String view,
      boolean editable,
      EventInput[] events,
      FullCalendar.Listener listener) {
    Options options = Js.uncheckedCast(JsPropertyMap.of());
    JsPropertyMap<Object> values = Js.asPropertyMap(options);
    if (date != null) values.set("initialDate", date);
    values.set("initialView", view);
    values.set("editable", editable);
    values.set("selectable", true);
    values.set("height", "auto");
    values.set("events", events);
    values.set(
        "headerToolbar",
        JsPropertyMap.of(
            "left",
            "prev,next today",
            "center",
            "title",
            "right",
            "dayGridMonth,timeGridWeek,timeGridDay,listWeek"));
    options.setEventClick(info -> emit(listener, "click", info.getEvent()));
    options.setEventChange(info -> emit(listener, "change", info.getEvent()));
    options.setDateClick(info -> listener.onEvent("date", "", "", info.getDateStr(), ""));
    options.setSelect(
        info -> listener.onEvent("select", "", "", info.getStartStr(), info.getEndStr()));
    return new Calendar(Js.asAny(element), options);
  }

  private static void emit(FullCalendar.Listener l, String action, Event event) {
    l.onEvent(action, event.getId(), event.getTitle(), event.getStartStr(), event.getEndStr());
  }

  static EventInput event(String id, String title, String start, String end, boolean allDay) {
    JsPropertyMap<Object> o = JsPropertyMap.of();
    o.set("id", id);
    o.set("title", title);
    o.set("start", start);
    if (end != null && !end.isEmpty()) o.set("end", end);
    o.set("allDay", allDay);
    return Js.uncheckedCast(o);
  }

  @JsFunction
  interface EventHandler {
    void handle(EventInfo info);
  }

  @JsFunction
  interface DateHandler {
    void handle(DateInfo info);
  }

  @JsFunction
  interface SelectHandler {
    void handle(SelectInfo info);
  }

  @JsType(isNative = true)
  interface Options {
    @JsProperty
    void setEventClick(EventHandler h);

    @JsProperty
    void setEventChange(EventHandler h);

    @JsProperty
    void setDateClick(DateHandler h);

    @JsProperty
    void setSelect(SelectHandler h);
  }

  @JsType(isNative = true)
  interface EventInfo {
    @JsProperty
    Event getEvent();
  }

  @JsType(isNative = true)
  interface DateInfo {
    @JsProperty
    String getDateStr();
  }

  @JsType(isNative = true)
  interface SelectInfo {
    @JsProperty
    String getStartStr();

    @JsProperty
    String getEndStr();
  }

  @JsType(isNative = true)
  interface DateValue {
    String toISOString();
  }

  @JsType(isNative = true)
  interface View {
    @JsProperty
    String getType();
  }

  @JsType(isNative = true)
  interface EventInput {}

  @JsType(isNative = true)
  interface Event {
    @JsProperty
    String getId();

    @JsProperty
    String getTitle();

    @JsProperty
    String getStartStr();

    @JsProperty
    String getEndStr();

    @JsProperty
    boolean getAllDay();

    void remove();
  }

  @JsType(isNative = true, namespace = "FullCalendar", name = "Calendar")
  static class Calendar {
    Calendar(Any element, Options options) {}

    public native void render();

    public native void destroy();

    public native void next();

    public native void prev();

    public native void today();

    public native void changeView(String view);

    public native void gotoDate(String date);

    public native void updateSize();

    public native Event addEvent(EventInput event);

    public native Event getEventById(String id);

    public native Event[] getEvents();

    public native void removeAllEvents();

    public native DateValue getDate();

    @JsProperty
    public native View getView();
  }
}
