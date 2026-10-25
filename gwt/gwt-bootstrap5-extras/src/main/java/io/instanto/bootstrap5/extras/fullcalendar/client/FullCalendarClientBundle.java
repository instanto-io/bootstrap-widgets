package io.instanto.bootstrap5.extras.fullcalendar.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/** Pinned vendor scripts for the FullCalendar module. */
public interface FullCalendarClientBundle extends ClientBundle {
  FullCalendarClientBundle INSTANCE = GWT.create(FullCalendarClientBundle.class);

  @Source("resource/js/fullcalendar-7.1.3.global.js")
  TextResource script0();

  @Source("resource/js/fullcalendar-bootstrap5-7.1.3.global.js")
  TextResource script1();
}
