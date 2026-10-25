package io.instanto.bootstrap5.extras.fullcalendar.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.ScriptInjector;
import jsinterop.base.Js;

/** Loads bundled scripts once before application entry points run. */
public final class FullCalendarEntryPoint implements EntryPoint {
  private static boolean injected;

  @Override
  public void onModuleLoad() {
    if (injected) return;
    if (!isLoaded()) {
      ScriptInjector.fromString(FullCalendarClientBundle.INSTANCE.script0().getText())
          .setWindow(ScriptInjector.TOP_WINDOW)
          .inject();
    }
    ScriptInjector.fromString(FullCalendarClientBundle.INSTANCE.script1().getText())
        .setWindow(ScriptInjector.TOP_WINDOW)
        .inject();
    injected = true;
  }

  private static boolean isLoaded() {
    return Js.global().get("FullCalendar") != null;
  }
}
