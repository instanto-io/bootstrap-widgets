package org.gwtbootstrap3.extras.datepicker.client.ui.base;

import org.gwtbootstrap3.client.shared.event.HideEvent;
import org.gwtbootstrap3.client.shared.event.HideHandler;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.constants.HasDateTimePickerHandlers;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeDateEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeDateHandler;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeMonthEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeMonthHandler;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeYearEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeYearHandler;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ClearDateEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ClearDateHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;

/** Bridges plugin events to GWT handlers independently of date and validation state. */
public abstract class DatePickerEventSupport extends Composite implements HasDateTimePickerHandlers {
    protected abstract void pickerShown();
    protected abstract void pickerHidden();
    protected abstract void pickerValueChanged();

    /** {@inheritDoc} */
    @Override
    public void onShow(final Event e) {
        pickerShown();
        fireEvent(new ShowEvent(e));
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addShowHandler(final ShowHandler showHandler) {
        return addHandler(showHandler, ShowEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onHide(final Event e) {
        pickerHidden();
        fireEvent(new HideEvent(e));
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addHideHandler(final HideHandler hideHandler) {
        return addHandler(hideHandler, HideEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onChangeDate(final Event e) {
        pickerValueChanged();
        fireEvent(new ChangeDateEvent(e));
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addChangeDateHandler(final ChangeDateHandler changeDateHandler) {
        return addHandler(changeDateHandler, ChangeDateEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onChangeYear(final Event e) {
        fireEvent(new ChangeYearEvent(e));
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addChangeYearHandler(final ChangeYearHandler changeYearHandler) {
        return addHandler(changeYearHandler, ChangeYearEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onChangeMonth(final Event e) {
        fireEvent(new ChangeMonthEvent(e));
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addChangeMonthHandler(final ChangeMonthHandler changeMonthHandler) {
        return addHandler(changeMonthHandler, ChangeMonthEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onClearDate(final Event e) {
        pickerValueChanged();
        fireEvent(new ClearDateEvent(e));
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addClearDateHandler(final ClearDateHandler clearDateHandler) {
        return addHandler(clearDateHandler, ClearDateEvent.getType());
    }

    @jsinterop.annotations.JsFunction
    interface PickerEvents { void event(String name, jsinterop.base.Any event); }

    final void dispatchPickerEvent(String name, jsinterop.base.Any event) {
        Event nativeEvent = asEvent(event);
        switch (name) {
            case "show": onShow(nativeEvent); break;
            case "hide": onHide(nativeEvent); break;
            case "changeDate": onChangeDate(nativeEvent); break;
            case "changeYear": onChangeYear(nativeEvent); break;
            case "changeMonth": onChangeMonth(nativeEvent); break;
            case "clearDate": onClearDate(nativeEvent); break;
            default: break;
        }
    }

    private static native Event asEvent(jsinterop.base.Any event) /*-{ return event; }-*/;
}
