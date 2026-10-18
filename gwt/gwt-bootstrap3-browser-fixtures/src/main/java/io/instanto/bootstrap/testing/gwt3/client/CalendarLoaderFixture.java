package io.instanto.bootstrap.testing.gwt3.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import org.gwtbootstrap3.extras.fullcalendar.client.FullCalendarEntryPoint;

final class CalendarLoaderFixture {
  static void run() {
    String name = Window.Location.getParameter("case");
    if (name == null || !name.contains("CAL-LOAD-")) return;
    try {
      FullCalendarEntryPoint loader = new FullCalendarEntryPoint();
      JavaScriptObject previous = snapshot();
      loader.onModuleLoad();
      if (name.contains("001")) {
        checkUnrelatedPlugins(previous);
      } else if (name.contains("002")) {
        JavaScriptObject loaded = snapshot();
        JavaScriptObject interactions = createInteractions();
        try {
          loader.onModuleLoad();
          loader.onModuleLoad();
          checkRepeatedLoad(loaded, interactions);
        } finally {
          destroyInteractions(interactions);
        }
      } else if (name.contains("003")) {
        JavaScriptObject interactions = createInteractions();
        try {
          checkCalendar();
          checkInteractions(interactions);
        } finally {
          destroyInteractions(interactions);
        }
      }
      Document.get().getBody().setAttribute("data-attachment-result", "ok");
    } catch (Throwable failure) {
      Document.get().getBody().setAttribute("data-attachment-result", "FAILED: " + failure);
    }
  }

  private static native JavaScriptObject snapshot() /*-{
    var $ = $wnd.jQuery;
    return {tooltip: $.fn.tooltip, button: $.fn.button, datepicker: $.fn.datepicker,
      moment: $wnd.moment, calendar: $.fn.fullCalendar,
      draggable: $.fn.draggable, resizable: $.fn.resizable,
      styles: $doc.querySelectorAll('style').length};
  }-*/;

  private static native void checkUnrelatedPlugins(JavaScriptObject old) /*-{
    var $ = $wnd.jQuery;
    ['tooltip', 'button', 'datepicker'].forEach(function(name) {
      if ($.fn[name] !== old[name]) throw new Error('Calendar replaced existing ' + name);
    });
    if (old.moment && $wnd.moment !== old.moment) throw new Error('Calendar replaced existing Moment');
    var target = $('<button title="Still Bootstrap">probe</button>').appendTo('body');
    try {
      target.tooltip({animation: false, trigger: 'manual'}).tooltip('show');
      if (!target.data('bs.tooltip') || !$('.tooltip.in').length) throw new Error('Bootstrap tooltip stopped working');
    } finally { target.tooltip('destroy'); target.remove(); }
  }-*/;

  private static native JavaScriptObject createInteractions() /*-{
    var $ = $wnd.jQuery;
    var drag = $('<div>drag</div>').appendTo('body').draggable({axis: 'x'});
    var resize = $('<div>resize</div>').css({width: 120, height: 80}).appendTo('body').resizable({minWidth: 40});
    return {drag: drag, resize: resize, dragInstance: drag.draggable('instance'), resizeInstance: resize.resizable('instance')};
  }-*/;

  private static native void checkRepeatedLoad(JavaScriptObject old, JavaScriptObject items) /*-{
    var $ = $wnd.jQuery;
    ['tooltip', 'button', 'datepicker', 'draggable', 'resizable'].forEach(function(name) {
      if ($.fn[name] !== old[name]) throw new Error('Repeated load replaced ' + name);
    });
    if ($wnd.moment !== old.moment || $.fn.fullCalendar !== old.calendar) throw new Error('Repeated load replaced calendar dependencies');
    if ($doc.querySelectorAll('style').length !== old.styles) throw new Error('Repeated load duplicated styles');
    if (items.drag.draggable('instance') !== items.dragInstance || items.resize.resizable('instance') !== items.resizeInstance)
      throw new Error('Repeated load replaced live interaction instances');
  }-*/;

  private static native void checkInteractions(JavaScriptObject items) /*-{
    if (items.drag.draggable('option', 'axis') !== 'x' || items.resize.resizable('option', 'minWidth') !== 40)
      throw new Error('Interaction options lost');
    items.drag.draggable('disable'); items.resize.resizable('disable');
    if (!items.drag.draggable('option', 'disabled') || !items.resize.resizable('option', 'disabled'))
      throw new Error('Interactions do not respond to commands');
  }-*/;

  private static native void destroyInteractions(JavaScriptObject items) /*-{
    items.drag.draggable('destroy').remove(); items.resize.resizable('destroy').remove();
  }-*/;

  private static native void checkCalendar() /*-{
    var $ = $wnd.jQuery, calendar = $('<div>').appendTo('body');
    try {
      calendar.fullCalendar({defaultDate: '2026-10-18', editable: true,
        events: [{id: 'probe', title: 'Loader event', start: '2026-10-18'}]});
      if (calendar.fullCalendar('clientEvents', 'probe').length !== 1 || !calendar.find('.fc-event').length)
        throw new Error('Loaded calendar did not render its event');
      calendar.fullCalendar('changeView', 'agendaWeek');
      if (calendar.fullCalendar('getView').name !== 'agendaWeek') throw new Error('Calendar view command failed');
    } finally { calendar.fullCalendar('destroy'); calendar.remove(); }
  }-*/;
}
