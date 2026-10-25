package org.gwtbootstrap3.teavm.calendar;

import com.google.gwt.user.client.ui.FlowPanel;
import org.gwtbootstrap3.extras.fullcalendar.client.FullCalendarResourcesResources;
import org.teavm.jso.*;

/** TeaVM host for the Bootstrap 3 FullCalendar runtime; preserves data across remounts. */
public final class CalendarWidget extends FlowPanel {
  @FunctionalInterface
  public interface Listener {
    void event(String action, String title, String date);
  }

  private JSObject instance, state;
  private int generation;
  private Listener listener = (a, t, d) -> {};

  public CalendarWidget() {
    getElement().setAttribute("data-fullcalendar-widget", "true");
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    int request = ++generation;
    FullCalendarResourcesResources.whenReady(
        CalendarWidget::available,
        () -> {
          if (isAttached() && generation == request && instance == null) {
            instance = create(getElement().unwrap(), state, (a, t, d) -> listener.event(a, t, d));
            getElement().setAttribute("data-calendar-ready", "true");
          }
        });
  }

  @Override
  protected void onUnload() {
    ++generation;
    if (instance != null) {
      state = snapshot(instance);
      destroy(instance);
      instance = null;
    }
    getElement().removeAttribute("data-calendar-ready");
    super.onUnload();
  }

  public void addAppointment(String id) {
    if (instance != null) add(instance, id);
  }

  public void week() {
    if (instance != null) command(instance, "changeView", "agendaWeek");
  }

  public void reset() {
    if (instance != null) {
      command(instance, "gotoDate", "2026-10-12");
      command(instance, "changeView", "month");
    }
  }

  public int getEventCount() {
    return instance == null ? 0 : count(instance);
  }

  @JSFunctor
  private interface Callback extends JSObject {
    void fire(String action, String title, String date);
  }

  @JSBody(script = "return !!(window.jQuery && jQuery.fn.fullCalendar);")
  private static native boolean available();

  @JSBody(
      params = {"el", "state", "callback"},
      script =
          "var e=jQuery(el);var"
              + " opts={defaultDate:state?state.date:'2026-10-12',defaultView:state?state.view:'month',height:'auto',editable:true,selectable:true,header:{left:'prev,next"
              + " today',center:'title',right:'month,agendaWeek,agendaDay,listWeek'},events:state?state.events:[{id:'planning',title:'Planning"
              + " session',start:'2026-10-12T10:00:00',end:'2026-10-12T11:30:00'},{id:'review',title:'Design"
              + " review',start:'2026-10-14T14:00:00',end:'2026-10-14T15:00:00'},{id:'release',title:'Release"
              + " window',start:'2026-10-19',end:'2026-10-21',allDay:true}],eventClick:function(event){callback('click',event.title,event.start.format());},eventDrop:function(event){callback('change',event.title,event.start.format());},eventResize:function(event){callback('resize',event.title,event.start.format());},select:function(start,end){callback('select','',start.format());}};e.fullCalendar(opts);return"
              + " e;")
  private static native JSObject create(JSObject el, JSObject state, Callback callback);

  @JSBody(
      params = "e",
      script =
          "return"
              + " {date:e.fullCalendar('getDate').format(),view:e.fullCalendar('getView').name,events:e.fullCalendar('clientEvents').map(function(x){return"
              + " {id:x.id,title:x.title,start:x.start.format(),end:x.end?x.end.format():null,allDay:x.allDay};})};")
  private static native JSObject snapshot(JSObject e);

  @JSBody(params = "e", script = "e.fullCalendar('destroy');")
  private static native void destroy(JSObject e);

  @JSBody(
      params = {"e", "c", "a"},
      script = "e.fullCalendar(c,a);")
  private static native void command(JSObject e, String c, String a);

  @JSBody(
      params = {"e", "id"},
      script =
          "e.fullCalendar('renderEvent',{id:id,title:'New appointment"
              + " '+id,start:'2026-10-15T09:00:00',end:'2026-10-15T10:00:00'},true);")
  private static native void add(JSObject e, String id);

  @JSBody(params = "e", script = "return e.fullCalendar('clientEvents').length;")
  private static native int count(JSObject e);
}
