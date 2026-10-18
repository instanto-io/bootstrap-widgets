package io.instanto.bootstrap.testing.gwt3.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import java.util.HashMap;
import java.util.Map;
import org.gwtbootstrap3.client.ui.SimpleCheckBox;
import org.gwtbootstrap3.client.ui.SimpleRadioButton;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.*;

final class OptionPreconditionsFixture {
  static void run() {
    String name = Window.Location.getParameter("case");
    if (name == null) return;
    try {
      if (name.contains("PRE-001")) {
        rejected(() -> SimpleCheckBox.wrap(Document.get().createCheckInputElement()));
        rejected(() -> SimpleRadioButton.wrap(Document.get().createRadioInputElement("group")));
        rejected(() -> TextArea.wrap(Document.get().createTextAreaElement()));
      } else if (name.contains("PRE-002")) {
        DayNames days = new DayNames();
        MonthNames months = new MonthNames();
        rejected(() -> days.localized(new String[6], new String[7]));
        rejected(() -> days.localized(new String[7], null));
        rejected(() -> months.localize(new String[11], new String[12]));
        rejected(() -> months.localize(new String[12], new String[13]));
        AgendaOptions agenda = new AgendaOptions();
        rejected(() -> agenda.setSlotDuration(-1));
        rejected(() -> agenda.setSnapDuration(61));
        agenda.setSlotDuration(0);
        agenda.setSnapDuration(60);
        GeneralDisplay display = new GeneralDisplay();
        rejected(() -> display.setHiddenDays(0, 7));
        display.setHiddenDays(0, 6);
      } else if (name.contains("CAL-001")) {
        check(
            "ddd".equals(property(new ColumnFormat().toJavaScript(), "columnFormat", "month")),
            "Column default was not stored under the view key");
        check(
            "h:mm{ - h:mm}"
                .equals(property(new TimeFormat().toJavaScript(), "timeFormat", "agenda")),
            "Time default was not stored under the view key");
        check(
            "MMMM yyyy".equals(property(new TitleFormat().toJavaScript(), "titleFormat", "month")),
            "Title default was not stored under the view key");
        Map<ViewOptionHash, String> options = new HashMap<>();
        options.put(ViewOptionHash.week, "custom");
        check(
            "custom"
                .equals(property(new ColumnFormat(options).toJavaScript(), "columnFormat", "week")),
            "Column custom format lost");
        check(
            "custom"
                .equals(
                    property(
                        new TimeFormat("fallback", options).toJavaScript(), "timeFormat", "week")),
            "Time custom format lost");
        check(
            "custom"
                .equals(property(new TitleFormat(options).toJavaScript(), "titleFormat", "week")),
            "Title custom format lost");
      } else if (name.contains("CAL-003")) {
        check(
            !calendarResourcesLoaded(),
            "NoResources module injected calendar or jQuery UI plugins");
      } else if (name.contains("CAL-002")) {
        class Agenda extends AgendaOptions {
          final Object ready = new Object();

          @Override
          public void newInstance() {
            check(ready != null, "Agenda override ran in constructor");
            super.newInstance();
          }
        }
        class Columns extends ColumnFormat {
          final Object ready = new Object();

          @Override
          protected Map<ViewOptionHash, String> getDefaultOptions() {
            check(ready != null, "Column override ran in constructor");
            return super.getDefaultOptions();
          }
        }
        class Times extends TimeFormat {
          final Object ready = new Object();

          @Override
          protected Map<ViewOptionHash, String> getDefaultOptions() {
            check(ready != null, "Time override ran in constructor");
            return super.getDefaultOptions();
          }
        }
        class Titles extends TitleFormat {
          final Object ready = new Object();

          @Override
          protected Map<ViewOptionHash, String> getDefaultOptions() {
            check(ready != null, "Title override ran in constructor");
            return super.getDefaultOptions();
          }
        }
        new Agenda().newInstance();
        new Columns();
        new Times();
        new Titles();
      } else return;
      result("ok");
    } catch (Throwable failure) {
      result("FAILED: " + failure);
    }
  }

  private static void rejected(Runnable action) {
    try {
      action.run();
    } catch (IllegalArgumentException expected) {
      return;
    }
    throw new IllegalStateException("Invalid argument was accepted");
  }

  private static void check(boolean value, String message) {
    if (!value) throw new IllegalStateException(message);
  }

  private static void result(String value) {
    Document.get().getBody().setAttribute("data-attachment-result", value);
  }

  private static native boolean calendarResourcesLoaded() /*-{
    return !!($wnd.jQuery.fullCalendar || ($wnd.jQuery.ui && $wnd.jQuery.ui.tooltip));
  }-*/;

  private static native String property(
      JavaScriptObject object, String group, String key) /*-{ return object[group][key]; }-*/;
}
