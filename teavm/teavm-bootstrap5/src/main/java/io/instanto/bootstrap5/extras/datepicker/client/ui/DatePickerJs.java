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

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

import io.instanto.bootstrap5.extras.datepicker.client.DatePickerResources;

/**
 * TeaVM implementation of the Tempus Dominus seam.
 *
 * <p>Identical in package and API to the Bootstrap 5 class of the same name; it exists
 * separately only because that one is JSNI, which TeaVM cannot compile.</p>
 */
final class DatePickerJs {

    /** Notified when the picked date changes; -1 means cleared. */
    interface ChangeHandler {
        void onDateChange(double millis);
    }

    /** The shape @JSBody can call back into. */
    private interface JsChange extends JSObject {
        void onDateChange(double millis);
    }

    private DatePickerJs() {
    }

    @JSBody(script = "return typeof window.tempusDominus !== 'undefined';")
    static native boolean isReady();

    /** Fetches Tempus Dominus and its stylesheet, once. */
    /**
     * Runs an action once the date picker is usable.
     *
     * <p>The module fetches the library by URL, so this is genuinely asynchronous. The
     * presence test is passed in so a page that already loaded the date picker itself is
     * recognised and nothing is fetched at all.</p>
     */
    static void whenReady(final Runnable action) {
        DatePickerResources.whenReady(DatePickerJs::isReady, action);
    }

    static void ensureResources() {
        DatePickerResources.ensureInjected();
    }

    static JavaScriptObject create(final Element element, final String format,
            final boolean sideBySide, final boolean showClear, final boolean showClose) {
        return JavaScriptObject.of(createPicker(element.unwrap(), format, sideBySide,
                showClear, showClose));
    }

    static void bindChange(final JavaScriptObject picker, final ChangeHandler handler) {
        if (picker != null && handler != null) {
            bind(picker.unwrap(), (JsChange) handler::onDateChange);
        }
    }

    static void invoke(final JavaScriptObject picker, final String method) {
        if (picker != null) {
            call(picker.unwrap(), method);
        }
    }

    static double readValue(final JavaScriptObject picker) {
        return picker == null ? -1 : read(picker.unwrap());
    }

    static void writeValue(final JavaScriptObject picker, final double millis) {
        if (picker != null) {
            write(picker.unwrap(), millis);
        }
    }

    static void dispose(final JavaScriptObject picker) {
        if (picker != null) {
            call(picker.unwrap(), "dispose");
        }
    }

    @JSBody(params = {"el", "format", "sideBySide", "showClear", "showClose"}, script =
            "var options = { display: { sideBySide: sideBySide,"
            + " buttons: { today: true, clear: showClear, close: showClose },"
            + " theme: 'auto' } };"
            + "if (format) { options.localization = { format: format }; }"
            + "return new window.tempusDominus.TempusDominus(el, options);")
    private static native JSObject createPicker(Object el, String format, boolean sideBySide,
            boolean showClear, boolean showClose);

    @JSBody(params = {"picker", "handler"}, script =
            "picker.subscribe(window.tempusDominus.Namespace.events.change, function (e) {"
            + "  handler.onDateChange(e && e.date ? e.date.valueOf() : -1);"
            + "});")
    private static native void bind(JSObject picker, JsChange handler);

    @JSBody(params = {"picker", "method"}, script =
            "if (typeof picker[method] === 'function') { picker[method](); }")
    private static native void call(JSObject picker, String method);

    @JSBody(params = {"picker"}, script =
            "var dates = picker.dates.picked;"
            + "return dates && dates.length ? dates[0].valueOf() : -1;")
    private static native double read(JSObject picker);

    @JSBody(params = {"picker", "millis"}, script =
            "if (millis < 0) { picker.dates.clear(); }"
            + "else { picker.dates.setValue(new window.tempusDominus.DateTime(millis)); }")
    private static native void write(JSObject picker, double millis);
}
