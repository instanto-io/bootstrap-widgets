/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2018 GwtBootstrap3
 * %%
 * Modified from the GwtBootstrap3 original for the Bootstrap 5 track of
 * GWT Bootstrap: moved to the io.instanto.bootstrap5 namespace and
 * re-targeted at Bootstrap 5 markup, class names and JavaScript APIs. The
 * GwtBootstrap3 copyright above is retained as required by the Apache
 * License 2.0; the namespace changed, the attribution did not.
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
package io.instanto.bootstrap5.extras.datepicker.client.ui;

import java.util.Date;

import io.instanto.bootstrap5.client.ui.TextBox;
import io.instanto.bootstrap5.client.ui.base.HasId;
import io.instanto.bootstrap5.client.ui.base.HasPlaceholder;
import io.instanto.bootstrap5.client.ui.base.HasResponsiveness;
import io.instanto.bootstrap5.client.ui.constants.DeviceSize;
import io.instanto.bootstrap5.client.ui.html.Div;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasValue;

/**
 * A date picker backed by Tempus Dominus 6.
 *
 * <p>This is a replacement rather than a port. GwtBootstrap3 wrapped
 * bootstrap-datepicker and bootstrap-datetimepicker, both of which render
 * Bootstrap 3 markup, and the latter has been unmaintained for years. Tempus
 * Dominus is the maintained successor, targets Bootstrap 5 and needs no jQuery.
 * The property names that carry over -- placeholder, id, name, enabled, the
 * responsive helpers, {@code show()} and {@code hide()} -- keep the Bootstrap 3
 * spelling, so most calling code reads the same.</p>
 *
 * <p>The widget renders the input-group wrapper Tempus Dominus expects:</p>
 *
 * <pre>
 * &lt;div class="input-group" id="..." data-td-target-input="nearest"&gt;
 *   &lt;input type="text" class="form-control"&gt;
 *   &lt;span class="input-group-text" data-td-target="#..." data-td-toggle="datetimepicker"&gt;
 * &lt;/div&gt;
 * </pre>
 */
public class DatePicker extends Div implements HasEnabled, HasId, HasName, HasPlaceholder,
        HasResponsiveness, HasValue<Date>, HasValueChangeHandlers<Date> {

    private final TextBox input = new TextBox();

    private final Div toggle = new Div();

    private final String wrapperId;

    private JavaScriptObject picker;

    private String format;

    private boolean sideBySide;

    private boolean showClear = true;

    private boolean showClose = true;

    public DatePicker() {
        wrapperId = Document.get().createUniqueId();
        addStyleName("input-group");
        getElement().setId(wrapperId);
        getElement().setAttribute("data-td-target-input", "nearest");
        getElement().setAttribute("data-td-target-toggle", "nearest");

        input.getElement().setAttribute("data-td-target", "#" + wrapperId);
        add(input);

        toggle.setStyleName("input-group-text");
        toggle.getElement().setAttribute("data-td-target", "#" + wrapperId);
        toggle.getElement().setAttribute("data-td-toggle", "datetimepicker");
        toggle.getElement().setInnerHTML("<i class=\"bi bi-calendar\"></i>");
        add(toggle);
    }

    public DatePicker(final String placeholder) {
        this();
        setPlaceholder(placeholder);
    }

    /** The text box the picker writes into. */
    public TextBox getTextBox() {
        return input;
    }

    /**
     * The display and parse format, in Tempus Dominus terms, e.g. {@code yyyy-MM-dd}.
     * Must be set before attach to take effect.
     */
    public void setFormat(final String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    /** Shows the date and clock panels together rather than switching between them. */
    public void setSideBySide(final boolean sideBySide) {
        this.sideBySide = sideBySide;
    }

    public void setShowClear(final boolean showClear) {
        this.showClear = showClear;
    }

    public void setShowClose(final boolean showClose) {
        this.showClose = showClose;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        initialise();
    }

    /**
     * Builds the picker, waiting for Tempus Dominus if it has not arrived yet. The GWT
     * module injects it as inline script text before the application runs; the TeaVM
     * backend fetches it by URL, and a picker attached during startup would otherwise
     * stay an inert input.
     */
    /**
     * Builds the widget once the date picker is usable.
     *
     * <p>Which backend this is no longer matters here. GWT compiles the library into the
     * module, so the action runs immediately; TeaVM fetches it and runs the action when
     * it arrives. Either way this asks the module instead of polling for a global to
     * appear, and a module that cannot load reports it rather than letting the widget
     * wait out a timeout and give up in silence.</p>
     */
    private void initialise() {
        DatePickerJs.whenReady(new Runnable() {
            @Override
            public void run() {
                if (isAttached()) {
                    build();
                }
            }
        });
    }

    private void build() {
        picker = DatePickerJs.create(getElement(), format, sideBySide, showClear, showClose);
        DatePickerJs.bindChange(picker, new DatePickerJs.ChangeHandler() {
            @Override
            public void onDateChange(final double millis) {
                onPickerChange(millis);
            }
        });
    }

    @Override
    protected void onUnload() {
        if (picker != null) {
            DatePickerJs.dispose(picker);
            picker = null;
        }
        super.onUnload();
    }

    public void show() {
        if (picker != null) {
            DatePickerJs.invoke(picker, "show");
        }
    }

    public void hide() {
        if (picker != null) {
            DatePickerJs.invoke(picker, "hide");
        }
    }

    public void toggle() {
        if (picker != null) {
            DatePickerJs.invoke(picker, "toggle");
        }
    }

    public void clear() {
        setValue(null, true);
    }

    @Override
    public Date getValue() {
        final double millis = picker == null ? -1 : DatePickerJs.readValue(picker);
        return millis < 0 ? null : new Date((long) millis);
    }

    @Override
    public void setValue(final Date value) {
        setValue(value, false);
    }

    @Override
    public void setValue(final Date value, final boolean fireEvents) {
        if (picker != null) {
            DatePickerJs.writeValue(picker, value == null ? -1 : value.getTime());
        }
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Date> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /** Called from the Tempus Dominus change subscription. */
    void onPickerChange(final double millis) {
        ValueChangeEvent.fire(this, millis < 0 ? null : new Date((long) millis));
    }

    // ---- the familiar Bootstrap 3 surface -----------------------------------

    @Override
    public void setPlaceholder(final String placeholder) {
        input.setPlaceholder(placeholder);
    }

    @Override
    public String getPlaceholder() {
        return input.getPlaceholder();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        input.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return input.isEnabled();
    }

    @Override
    public void setName(final String name) {
        input.setName(name);
    }

    @Override
    public String getName() {
        return input.getName();
    }

    @Override
    public void setId(final String id) {
        input.setId(id);
    }

    @Override
    public String getId() {
        return input.getId();
    }

    @Override
    public void setVisibleOn(final DeviceSize deviceSize) {
        io.instanto.bootstrap5.client.ui.base.helper.StyleHelper.setVisibleOn(this, deviceSize);
    }

    @Override
    public void setHiddenOn(final DeviceSize deviceSize) {
        io.instanto.bootstrap5.client.ui.base.helper.StyleHelper.setHiddenOn(this, deviceSize);
    }

    // ---- Tempus Dominus ------------------------------------------------------
}
