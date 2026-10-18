package io.instanto.bootstrap.testing.gwt3.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.DatePickerBase;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.DateTimePickerBase;

/** GWT-only wrappers must preserve their customized blur behavior after constructor cleanup. */
final class DatePickerValidationLifecycleFixture {
  private DatePickerValidationLifecycleFixture() {}

  static void run() {
    DateProbe date = new DateProbe();
    date.setValidateOnBlur(true);
    DomEvent.fireNativeEvent(Document.get().createBlurEvent(), date);
    date.onShow(null);
    DomEvent.fireNativeEvent(Document.get().createBlurEvent(), date);
    date.onHide(null);
    Document.get().getBody().setAttribute("data-date-blur-validation", date.displays.toString());

    DateTimeProbe dateTime = new DateTimeProbe();
    dateTime.setValidateOnBlur(true);
    DomEvent.fireNativeEvent(Document.get().createBlurEvent(), dateTime);
    dateTime.onShow(null);
    DomEvent.fireNativeEvent(Document.get().createBlurEvent(), dateTime);
    dateTime.onHide(null);
    Document.get()
        .getBody()
        .setAttribute("data-date-time-blur-validation", dateTime.displays.toString());
  }

  private static class DateProbe extends DatePickerBase {
    final StringBuilder displays = new StringBuilder();

    @Override
    public boolean validate(boolean show) {
      displays.append(show).append(',');
      return true;
    }
  }

  private static class DateTimeProbe extends DateTimePickerBase {
    final StringBuilder displays = new StringBuilder();

    @Override
    public boolean validate(boolean show) {
      displays.append(show).append(',');
      return true;
    }
  }
}
