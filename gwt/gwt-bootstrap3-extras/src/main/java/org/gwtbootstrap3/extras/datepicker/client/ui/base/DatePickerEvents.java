package org.gwtbootstrap3.extras.datepicker.client.ui.base;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Event;
import org.gwtbootstrap3.client.shared.event.HideEvent;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeDateEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeMonthEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeYearEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ClearDateEvent;

/** Creates typed events while retaining the picker as their source. */
final class DatePickerEvents {
    private final HasHandlers source;

    DatePickerEvents(HasHandlers source) {
        this.source = source;
    }

    void show(Event event) {
        source.fireEvent(new ShowEvent(event));
    }

    void hide(Event event) {
        source.fireEvent(new HideEvent(event));
    }

    void changeDate(Event event) {
        source.fireEvent(new ChangeDateEvent(event));
    }

    void changeMonth(Event event) {
        source.fireEvent(new ChangeMonthEvent(event));
    }

    void changeYear(Event event) {
        source.fireEvent(new ChangeYearEvent(event));
    }

    void clearDate(Event event) {
        source.fireEvent(new ClearDateEvent(event));
    }
}
