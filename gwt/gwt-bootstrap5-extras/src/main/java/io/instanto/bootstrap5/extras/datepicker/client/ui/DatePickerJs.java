/*
 * #%L
 * GWT Bootstrap
 * %%
 * Copyright (C) 2026 Carl Stainton
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

import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.datepicker.client.DatePickerResources;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsArrayLike;
import jsinterop.base.JsPropertyMap;

/**
 * Tempus Dominus, reached through JsInterop so GWT and TeaVM compile this one source.
 *
 * <p>The change callback is an interface rather than a back-reference into the widget, so
 * the widget stays plain Java; it is adapted to a JavaScript function here.</p>
 */
final class DatePickerJs {

    /** Notified when the picked date changes; {@code -1} when it is cleared. */
    interface ChangeHandler {
        void onDateChange(double millis);
    }

    private DatePickerJs() {
    }

    /** Whether Tempus Dominus has finished loading. */
    static boolean isReady() {
        return Js.global().get("tempusDominus") != null;
    }

    /** Runs an action once the date picker is usable, immediately if it already is. */
    static void whenReady(final Runnable action) {
        DatePickerResources.whenReady(DatePickerJs::isReady, action);
    }

    /** Starts loading the date picker if that has not already begun. */
    static void ensureResources() {
        DatePickerResources.ensureInjected();
    }

    static TempusDominus create(final Element element, final String format, final boolean sideBySide,
            final boolean showClear, final boolean showClose) {
        final JsPropertyMap<Object> buttons = JsPropertyMap.<Object>of("today", true, "clear", showClear, "close", showClose);
        final JsPropertyMap<Object> display = JsPropertyMap.<Object>of("sideBySide", sideBySide, "buttons", buttons, "theme", "auto");
        final JsPropertyMap<Object> options = JsPropertyMap.<Object>of("display", display);
        if (format != null && !format.isEmpty()) {
            options.set("localization", JsPropertyMap.<Object>of("format", format));
        }
        return new TempusDominus(Js.asAny(element), options);
    }

    static void bindChange(final TempusDominus picker, final ChangeHandler handler) {
        final String change = Js.asString(Js.global().nestedGet("tempusDominus.Namespace.events.change"));
        picker.subscribe(change, event -> {
            final Object date = Js.isTruthy(event) ? Js.asPropertyMap(event).get("date") : null;
            handler.onDateChange(Js.isTruthy(date) ? Js.<DateTime>uncheckedCast(date).valueOf() : -1);
        });
    }

    /** Calls {@code method} on the picker if it has one, as show, hide and toggle. */
    static void invoke(final TempusDominus picker, final String method) {
        final Object function = Js.asPropertyMap(picker).get(method);
        if ("function".equals(Js.typeof(function))) {
            Js.<Function>uncheckedCast(function).call(Js.asAny(picker));
        }
    }

    static double readValue(final TempusDominus picker) {
        final JsArrayLike<Object> dates = picker.getDates().getPicked();
        return Js.isTruthy(dates) && dates.getLength() > 0
                ? Js.<DateTime>uncheckedCast(dates.getAt(0)).valueOf() : -1;
    }

    static void writeValue(final TempusDominus picker, final double millis) {
        if (millis < 0) {
            picker.getDates().clear();
        } else {
            picker.getDates().setValue(new DateTime(millis));
        }
    }

    static void dispose(final TempusDominus picker) {
        if ("function".equals(Js.typeof(Js.asPropertyMap(picker).get("dispose")))) {
            picker.dispose();
        }
    }

    @JsFunction
    interface ChangeListener {
        void onChange(Any event);
    }

    /** A JavaScript function, called with a receiver. */
    @JsType(isNative = true)
    interface Function {
        void call(Any receiver);
    }

    @JsType(isNative = true, namespace = "tempusDominus", name = "TempusDominus")
    static class TempusDominus {
        TempusDominus(Any element, JsPropertyMap<Object> options) {
        }

        native void subscribe(String event, ChangeListener listener);

        @JsProperty(name = "dates")
        native Dates getDates();

        native void dispose();
    }

    /** The picker's selected dates. */
    @JsType(isNative = true)
    interface Dates {
        @JsProperty
        JsArrayLike<Object> getPicked();

        void clear();

        void setValue(DateTime date);
    }

    @JsType(isNative = true, namespace = "tempusDominus", name = "DateTime")
    static class DateTime {
        DateTime(double millis) {
        }

        native double valueOf();
    }
}
