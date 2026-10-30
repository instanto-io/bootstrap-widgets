package org.gwtbootstrap3.extras.datepicker.client.ui.base;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2016 GwtBootstrap3
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gwtbootstrap3.client.shared.event.HideEvent;
import org.gwtbootstrap3.client.shared.event.HideHandler;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.base.HasId;
import org.gwtbootstrap3.client.ui.base.HasPlaceholder;
import org.gwtbootstrap3.client.ui.base.HasResponsiveness;
import org.gwtbootstrap3.client.ui.base.ValueBoxBase;
import org.gwtbootstrap3.client.ui.base.helper.StyleHelper;
import org.gwtbootstrap3.client.ui.base.mixin.BlankValidatorMixin;
import org.gwtbootstrap3.client.ui.base.mixin.ErrorHandlerMixin;
import org.gwtbootstrap3.client.ui.constants.DeviceSize;
import org.gwtbootstrap3.client.ui.form.error.ErrorHandler;
import org.gwtbootstrap3.client.ui.form.error.ErrorHandlerType;
import org.gwtbootstrap3.client.ui.form.error.HasErrorHandler;
import org.gwtbootstrap3.client.ui.form.validator.HasBlankValidator;
import org.gwtbootstrap3.client.ui.form.validator.HasValidators;
import org.gwtbootstrap3.client.ui.form.validator.ValidationChangedEvent.ValidationChangedHandler;
import org.gwtbootstrap3.client.ui.form.validator.Validator;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.constants.DatePickerDayOfWeek;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.constants.DatePickerLanguage;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.constants.DatePickerMinView;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.constants.DatePickerPosition;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.constants.HasDateTimePickerHandlers;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeDateEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeDateHandler;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeMonthEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeMonthHandler;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeYearEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ChangeYearHandler;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ClearDateEvent;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.events.ClearDateHandler;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.dom.client.Document;
import org.gwtbootstrap3.extras.datepicker.client.DatePickerResourcesResources;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.constants.HasDatePickerOptions;

/**
 * @author Joshua Godi
 * @author Steven Jardine
 */
public class DatePickerBase extends Composite implements HasDateTimePickerHandlers, HasEnabled, HasId, HasResponsiveness,
        HasVisibility, HasPlaceholder, HasDatePickerOptions, HasName, HasValue<Date>, LeafValueEditor<Date>,
        HasEditorErrors<Date>, HasErrorHandler, HasValidators<Date>, HasBlankValidator<Date> {

    static class DatePickerValidatorMixin extends BlankValidatorMixin<DatePickerBase, Date> {

        private boolean showing = false;

        public void setShowing(boolean showing) {
            this.showing = showing;
        }

        public DatePickerValidatorMixin(DatePickerBase inputWidget, ErrorHandler errorHandler) {
            super(inputWidget, errorHandler);
        }

        @Override
        protected void onValidationBlur() {
            getInputWidget().validate(!showing && getValidateOnBlur());
        }

    }

    // Check http://www.gwtproject.org/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.html
    // for more information on syntax
    private static final Map<Character, Character> DATE_TIME_FORMAT_MAP = new HashMap<Character, Character>();

    static {
        DATE_TIME_FORMAT_MAP.put('m', 'M'); // months
    }

    private final DatePickerEvents pickerEvents = new DatePickerEvents(this);
    private final TextBox textBox;
    private DateTimeFormat dateTimeFormat;
    private static final DateTimeFormat CALENDAR_DATE = DateTimeFormat.getFormat("yyyy-MM-dd");
    private LeafValueEditor<Date> editor;
    private final ErrorHandlerMixin<Date> errorHandlerMixin;
    private final DatePickerValidatorMixin validatorMixin;

    /**
     * DEFAULT values
     */
    private String format = "mm/dd/yyyy";
    private DatePickerDayOfWeek weekStart = DatePickerDayOfWeek.SUNDAY;
    private DatePickerDayOfWeek[] daysOfWeekDisabled = {};
    private boolean autoClose = false;
    private DatePickerMinView startView = DatePickerMinView.DAY;
    private DatePickerMinView minView = DatePickerMinView.DAY;

    private boolean showTodayButton = false;
    private boolean showClearButton = false;
    private boolean highlightToday = false;
    private boolean keyboardNavigation = true;
    private boolean forceParse = true;

    private DatePickerMinView viewSelect = DatePickerMinView.DAY;

    private Widget container = null;
    private DatePickerLanguage language = DatePickerLanguage.EN;
    private DatePickerPosition position = DatePickerPosition.AUTO;

    private Date lastValue;
    private boolean settingValue;
    private boolean configured;
    private int configurationVersion;
    private String startDate;
    private String endDate;

    public DatePickerBase() {
        textBox = new TextBox();
        initWidget(textBox);
        errorHandlerMixin = new ErrorHandlerMixin<Date>(this);
        validatorMixin = new DatePickerValidatorMixin(this, errorHandlerMixin.getErrorHandler());
        setFormat(format);
        textBox.addValueChangeHandler(event -> fireValueChange());
    }

    public void setContainer(final Widget container) {
        this.container = container;
    }

    public Widget getContainer() {
        return container;
    }

    public TextBox getTextBox() {
        return textBox;
    }

    public void setAlignment(final ValueBoxBase.TextAlignment align) {
        textBox.setAlignment(align);
    }

    /** {@inheritDoc} */
    @Override
    public void setPlaceholder(final String placeHolder) {
        textBox.setPlaceholder(placeHolder);
    }

    /** {@inheritDoc} */
    @Override
    public String getPlaceholder() {
        return textBox.getPlaceholder();
    }

    public void setReadOnly(final boolean readOnly) {
        textBox.setReadOnly(readOnly);
    }

    public boolean isReadOnly() {
        return textBox.isReadOnly();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEnabled() {
        return textBox.isEnabled();
    }

    /** {@inheritDoc} */
    @Override
    public void setEnabled(final boolean enabled) {
        textBox.setEnabled(enabled);
    }

    /** {@inheritDoc} */
    @Override
    public void setId(final String id) {
        textBox.setId(id);
    }

    /** {@inheritDoc} */
    @Override
    public String getId() {
        return textBox.getId();
    }

    /** {@inheritDoc} */
    @Override
    public void setName(final String name) {
        textBox.setName(name);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return textBox.getName();
    }

    /** {@inheritDoc} */
    @Override
    public void setVisibleOn(final DeviceSize deviceSize) {
        StyleHelper.setVisibleOn(this, deviceSize);
    }

    /** {@inheritDoc} */
    @Override
    public void setHiddenOn(final DeviceSize deviceSize) {
        StyleHelper.setHiddenOn(this, deviceSize);
    }

    /** {@inheritDoc} */
    @Override
    public void setLanguage(final DatePickerLanguage language) {
        this.language = language;

        // Locale scripts are injected after the picker library has loaded in configure().
    }

    /** {@inheritDoc} */
    @Override
    public DatePickerLanguage getLanguage() {
        return language;
    }

    /** {@inheritDoc} */
    @Override
    public void setPosition(final DatePickerPosition position) {
        this.position = position;
    }

    /** {@inheritDoc} */
    @Override
    public DatePickerPosition getPosition() {
        return position;
    }

    /**
     * Call this whenever changing any settings: minView, startView, format, etc. If you are changing
     * format and date value, the updates must take in such order:
     * <p/>
     * locales.cache.1.4.0. DateTimePicker.reload()
     * 2. DateTimePicker.setValue(newDate); // Date newDate.
     * <p/>
     * Otherwise date value is not updated.
     */
    public void reload() {
        configure();
    }

    public void show() {
        if (configured) show(getElement());
    }

    public void hide() {
        if (configured) hide(getElement());
    }

    /** {@inheritDoc} */
    @Override
    public void setAutoClose(final boolean autoClose) {
        this.autoClose = autoClose;
    }

    /** {@inheritDoc} */
    @Override
    public void onShow(final Event e) {
        validatorMixin.setShowing(true);
        pickerEvents.show(e);
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addShowHandler(final ShowHandler showHandler) {
        return addHandler(showHandler, ShowEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onHide(final Event e) {
        validatorMixin.setShowing(false);
        validate(getValidateOnBlur());
        pickerEvents.hide(e);
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addHideHandler(final HideHandler hideHandler) {
        return addHandler(hideHandler, HideEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onChangeDate(final Event e) {
        fireValueChange();
        pickerEvents.changeDate(e);
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addChangeDateHandler(final ChangeDateHandler changeDateHandler) {
        return addHandler(changeDateHandler, ChangeDateEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onChangeYear(final Event e) {
        pickerEvents.changeYear(e);
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addChangeYearHandler(final ChangeYearHandler changeYearHandler) {
        return addHandler(changeYearHandler, ChangeYearEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onChangeMonth(final Event e) {
        pickerEvents.changeMonth(e);
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addChangeMonthHandler(final ChangeMonthHandler changeMonthHandler) {
        return addHandler(changeMonthHandler, ChangeMonthEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void onClearDate(final Event e) {
        fireValueChange();
        pickerEvents.clearDate(e);
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addClearDateHandler(final ClearDateHandler clearDateHandler) {
        return addHandler(clearDateHandler, ClearDateEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void setDaysOfWeekDisabled(final DatePickerDayOfWeek... daysOfWeekDisabled) {
        String days = toDaysOfWeekDisabledString(daysOfWeekDisabled);
        if (configured) setDaysOfWeekDisabled(getElement(), days);
    }

    /** {@inheritDoc} */
    @Override
    public void setEndDate(final Date endDate) {
        // The plugin expects the currently configured display format.
        setEndDate(formatLocalDate(endDate));
    }

    /** {@inheritDoc} */
    @Override
    public void setEndDate(final String endDate) {
        // The plugin expects the currently configured display format.
        this.endDate = endDate;
        if (configured) setEndDate(getElement(), endDate);
    }

    /** {@inheritDoc} */
    @Override
    public void clearEndDate() {
        setEndDate((String) null);
    }

    /** {@inheritDoc} */
    @Override
    public void setForceParse(final boolean forceParse) {
        this.forceParse = forceParse;
    }

    /** {@inheritDoc} */
    @Override
    public void setHighlightToday(final boolean highlightToday) {
        this.highlightToday = highlightToday;
    }

    /** {@inheritDoc} */
    @Override
    public void setHasKeyboardNavigation(final boolean hasKeyboardNavigation) {
        this.keyboardNavigation = hasKeyboardNavigation;
    }

    /** {@inheritDoc} */
    @Override
    public void setMinView(final DatePickerMinView datePickerMinView) {
        this.minView = datePickerMinView;

        // We keep the view select the same as the min view
        if (viewSelect != minView) {
            setViewSelect(datePickerMinView);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setShowTodayButton(final boolean showTodayButton) {
        this.showTodayButton = showTodayButton;
    }

    /** {@inheritDoc} */
    @Override
    public void setShowClearButton(boolean showClearbutton) {
        this.showClearButton = showClearbutton;
    }

    /** {@inheritDoc} */
    @Override
    public void setStartDate(final Date startDate) {
        // The plugin expects the currently configured display format.
        setStartDate(formatLocalDate(startDate));
    }

    /** {@inheritDoc} */
    @Override
    public void setStartDate(final String startDate) {
        // The plugin expects the currently configured display format.
        this.startDate = startDate;
        if (configured) setStartDate(getElement(), startDate);
    }

    /** {@inheritDoc} */
    @Override
    public void clearStartDate() {
        setStartDate((String) null);
    }

    /** {@inheritDoc} */
    @Override
    public void setStartView(final DatePickerMinView datePickerMinView) {
        this.startView = datePickerMinView;
    }

    /** {@inheritDoc} */
    @Override
    public void setViewSelect(final DatePickerMinView datePickerMinView) {
        this.viewSelect = datePickerMinView;

        // We keep the min view the same as the view select
        if (viewSelect != minView) {
            setMinView(datePickerMinView);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setWeekStart(final DatePickerDayOfWeek weekStart) {
        this.weekStart = weekStart;
    }

    /**
     * Convert GWT date format to bootstrap date format
     *
     * @param format date format using GWT notation
     * @return date format using bootstrap notation
     */
    private static String toBootstrapDateFormat(final String format) {
        String bootstrap_format = format;

        // Replace long day name "EEEE" with "DD"
        bootstrap_format = bootstrap_format.replace("EEEE", "DD");
        // Replace short day name "EE" with "DD"
        bootstrap_format = bootstrap_format.replaceAll("E{1,3}", "D");
        // If there are at least 3 Ms there is month name in wording
        if (bootstrap_format.contains("MMM")) {
            // Replace long date month "MMMM" with "MM"
            bootstrap_format = bootstrap_format.replace("MMMM", "MM");
            // Replace month name "MMM" with "M"
            bootstrap_format = bootstrap_format.replace("MMM", "M");
        }
        else {
            // Replace month number with leading 0 "MM" with "mm"
            bootstrap_format = bootstrap_format.replace("MM", "mm");
            // Replace month number "M" with "m"
            bootstrap_format = bootstrap_format.replace("M", "m");
        }
        if (!bootstrap_format.contains("yy")) {
            // Replace full year format "y" with "yyyy"
            bootstrap_format = bootstrap_format.replace("y", "yyyy");
        }

        return bootstrap_format;
    }

    /**
     * Sets format of the date using GWT notation
     *
     * @param format date format in GWT notation
     */
    public void setGWTFormat(final String format) {
        this.format = toBootstrapDateFormat(format);

        // Get the old value
        final Date oldValue = getValue();

        // Make the new DateTimeFormat
        this.dateTimeFormat = DateTimeFormat.getFormat(format);

        if (oldValue != null) {
            setValue(oldValue);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setFormat(final String format) {
        this.format = format;

        // Get the old value
        final Date oldValue = getValue();

        // Make the new DateTimeFormat
        setDateTimeFormat(format);

        if (oldValue != null) {
            setValue(oldValue);
        }
    }

    private void setDateTimeFormat(final String format) {
        final StringBuilder fb = new StringBuilder(format);
        for (int i = 0; i < fb.length(); i++) {
            if (DATE_TIME_FORMAT_MAP.containsKey(fb.charAt(i))) {
                fb.setCharAt(i, DATE_TIME_FORMAT_MAP.get(fb.charAt(i)));
            }
        }

        this.dateTimeFormat = DateTimeFormat.getFormat(fb.toString());
    }

    /** {@inheritDoc} */
    @Override
    public Date getValue() {
        try {
            if (dateTimeFormat == null || textBox.getValue() == null || textBox.getValue().isEmpty()) return null;
            // A date picker represents a local calendar day. TeaVM's shared formatter may
            // use UTC, so preserve its parsed calendar fields rather than its time zone.
            String[] day = CALENDAR_DATE.format(dateTimeFormat.parse(textBox.getValue())).split("-");
            return new Date(Integer.parseInt(day[0]) - 1900,
                    Integer.parseInt(day[1]) - 1, Integer.parseInt(day[2]));
        } catch (final Exception e) {
            return null;
        }
    }

    private String formatLocalDate(Date value) {
        // Give the formatter the same calendar fields in its own time zone. This keeps
        // the date-only widget independent of the compiler's default time-zone setting.
        int month = value.getMonth() + 1;
        int date = value.getDate();
        String day = (value.getYear() + 1900) + "-" + (month < 10 ? "0" : "") + month
                + "-" + (date < 10 ? "0" : "") + date;
        return dateTimeFormat.format(CALENDAR_DATE.parse(day));
    }

    private void fireValueChange() {
        if (settingValue) return;
        Date value = getValue();
        Date oldValue = lastValue;
        lastValue = value;
        ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
    }

    public String getBaseValue() {
        return textBox.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Date> dateValueChangeHandler) {
        return addHandler(dateValueChangeHandler, ValueChangeEvent.getType());
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final Date value) {
        setValue(value, false);
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final Date value, final boolean fireEvents) {
        errorHandlerMixin.clearErrors();
        textBox.setValue(value != null ? formatLocalDate(value) : null);
        lastValue = getValue();
        boolean previous = settingValue;
        settingValue = true;
        try {
            if (configured) update(textBox.getElement());
        } finally {
            settingValue = previous;
        }

        if (fireEvents) {
            ValueChangeEvent.fire(DatePickerBase.this, value);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void onLoad() {
        super.onLoad();
        configure();

        // With the new update the parent must have position: relative for positioning to work
        if (getElement().getParentElement() != null) {
            getElement().getParentElement().getStyle().setPosition(Style.Position.RELATIVE);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void onUnload() {
        configurationVersion++;
        if (configured) remove(getElement());
        configured = false;
        validatorMixin.setShowing(false);
        super.onUnload();
    }

    protected void configure() {
        final int version = ++configurationVersion;
        DatePickerResourcesResources.whenReady(
                DatePickerBase::isPickerReady, () -> {
                    if (!isAttached() || version != configurationVersion) return;
                    if (language.getJs() != null) {
                        ScriptInjector.fromString(language.getJs().getText())
                                .setWindow(ScriptInjector.TOP_WINDOW).inject();
                    }
                    configure(this, container == null ? getParent() : container);
                    configured = true;
                    setStartDate(getElement(), startDate);
                    setEndDate(getElement(), endDate);
                });
    }

    private static native boolean isPickerReady() /*-{
        return !!($wnd.jQuery && $wnd.jQuery.fn.datepicker);
    }-*/;

    protected void configure(final Widget w, final Widget container) {
        w.getElement().setAttribute("data-date-format", format);

        // If configuring not for the first time, datepicker must be removed first.
        this.remove(w.getElement());

        configure(w.getElement(), container == null ? Document.get().getBody() : container.getElement(), format, weekStart.getValue(), toDaysOfWeekDisabledString(daysOfWeekDisabled), autoClose,
                startView.getValue(), minView.getValue(), showTodayButton, showClearButton, highlightToday, keyboardNavigation, forceParse, viewSelect.getValue(),
                language.getCode(), position.getPosition());
    }

    protected void execute(final String cmd) {
        execute(getElement(), cmd);
    }

    private native void execute(Element e, String cmd) /*-{
        $wnd.jQuery(e).datepicker(cmd);
    }-*/;

    private native void remove(Element e) /*-{
        $wnd.jQuery(e).off('.gwtBootstrapDatePicker');
        if ($wnd.jQuery(e).data('datepicker')) $wnd.jQuery(e).datepicker('destroy');
    }-*/;

    private native void show(Element e) /*-{
        $wnd.jQuery(e).datepicker('show');
    }-*/;

    private native void hide(Element e) /*-{
        $wnd.jQuery(e).datepicker('hide');
    }-*/;

    private native void update(Element e) /*-{
        $wnd.jQuery(e).datepicker('update');
    }-*/;

    private native void setStartDate(Element e, String startDate) /*-{
        $wnd.jQuery(e).datepicker('setStartDate', startDate);
    }-*/;

    private native void setEndDate(Element e, String endDate) /*-{
        $wnd.jQuery(e).datepicker('setEndDate', endDate);
    }-*/;

    private native void setDaysOfWeekDisabled(Element e, String daysOfWeekDisabled) /*-{
        $wnd.jQuery(e).datepicker('setDaysOfWeekDisabled', daysOfWeekDisabled);
    }-*/;

    /**
     * Compatibility hook for existing subclasses. The historical parameter list is retained;
     * the plugin bridge itself receives one options object.
     */
    @SuppressWarnings("java:S107") // Preserve the protected extension API used by downstream widgets.
    protected void configure(Element e, Element p, String format, int weekStart, String daysOfWeekDisabled, boolean autoClose, int startView,
                             int minViewMode, boolean todayBtn, boolean clearBtn, boolean highlightToday, boolean keyboardNavigation, boolean forceParse, int viewSelect, String language,
                             String orientation) {
        jsinterop.base.JsPropertyMap<Object> options = jsinterop.base.JsPropertyMap.of();
        options.set("format", format);
        options.set("language", language);
        options.set("weekStart", weekStart);
        options.set("daysOfWeekDisabled", daysOfWeekDisabled);
        options.set("autoclose", autoClose);
        options.set("startView", startView);
        options.set("minViewMode", minViewMode);
        options.set("todayBtn", todayBtn ? "linked" : Boolean.FALSE);
        options.set("clearBtn", clearBtn);
        options.set("todayHighlight", highlightToday);
        options.set("keyboardNavigation", keyboardNavigation);
        options.set("forceParse", forceParse);
        options.set("orientation", orientation);
        configureNative(e, p, options, this::dispatchPickerEvent);
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

    private static native void configureNative(Element e, Element p, jsinterop.base.JsPropertyMap<Object> options,
                                                PickerEvents callback) /*-{
        options.container = p;
        $wnd.jQuery(e).datepicker(options)
            .on('show.gwtBootstrapDatePicker hide.gwtBootstrapDatePicker changeDate.gwtBootstrapDatePicker changeYear.gwtBootstrapDatePicker changeMonth.gwtBootstrapDatePicker clearDate.gwtBootstrapDatePicker',
                function (event) { callback(event.type, event); });
    }-*/;

    protected String toDaysOfWeekDisabledString(final DatePickerDayOfWeek... datePickerDayOfWeeks) {
        this.daysOfWeekDisabled = datePickerDayOfWeeks;

        final StringBuilder builder = new StringBuilder();

        if (datePickerDayOfWeeks != null) {
            int i = 0;
            for (final DatePickerDayOfWeek dayOfWeek : datePickerDayOfWeeks) {
                builder.append(dayOfWeek.getValue());

                i++;
                if (i < datePickerDayOfWeeks.length) {
                    builder.append(",");
                }
            }
        }
        return builder.toString();
    }

    /** {@inheritDoc} */
    @Override
    public com.google.web.bindery.event.shared.HandlerRegistration addValidationChangedHandler(ValidationChangedHandler handler) {
        return validatorMixin.addValidationChangedHandler(handler);
    }

    /** {@inheritDoc} */
    @Override
    public boolean getAllowBlank() {
        return validatorMixin.getAllowBlank();
    }

    /** {@inheritDoc} */
    @Override
    public void setAllowBlank(boolean allowBlank) {
        validatorMixin.setAllowBlank(allowBlank);
    }

    /** {@inheritDoc} */
    @Override
    public void addValidator(Validator<Date> validator) {
        validatorMixin.addValidator(validator);
    }

    /** {@inheritDoc} */
    @Override
    public boolean getValidateOnBlur() {
        return validatorMixin.getValidateOnBlur();
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeValidator(Validator<Date> validator) {
        return validatorMixin.removeValidator(validator);
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        validatorMixin.reset();
    }

    /** {@inheritDoc} */
    @Override
    public void setValidateOnBlur(boolean validateOnBlur) {
        validatorMixin.setValidateOnBlur(validateOnBlur);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public void setValidators(Validator<Date>... validators) {
        validatorMixin.setValidators(validators);
    }

    /** {@inheritDoc} */
    @Override
    public boolean validate() {
        return validatorMixin.validate();
    }

    /** {@inheritDoc} */
    @Override
    public boolean validate(boolean show) {
        return validatorMixin.validate(show);
    }

    /** {@inheritDoc} */
    @Override
    public ErrorHandler getErrorHandler() {
        return errorHandlerMixin.getErrorHandler();
    }

    /** {@inheritDoc} */
    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        errorHandlerMixin.setErrorHandler(errorHandler);
        validatorMixin.setErrorHandler(errorHandler);
    }

    /** {@inheritDoc} */
    @Override
    public ErrorHandlerType getErrorHandlerType() {
        return errorHandlerMixin.getErrorHandlerType();
    }

    /** {@inheritDoc} */
    @Override
    public void setErrorHandlerType(ErrorHandlerType errorHandlerType) {
        errorHandlerMixin.setErrorHandlerType(errorHandlerType);
    }

    /** {@inheritDoc} */
    @Override
    public void showErrors(List<EditorError> errors) {
        errorHandlerMixin.showErrors(errors);
    }

}
