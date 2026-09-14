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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * The Tempus Dominus API.
 *
 * <p>Its own class because the implementation is JSNI, which the TeaVM backend cannot
 * compile; that backend excludes this file and supplies the same API through
 * {@code @JSBody}. The change callback is an interface rather than a back-reference into
 * the widget, because JSNI can call a Java method by signature and {@code @JSBody}
 * cannot.</p>
 */
final class DatePickerJs {

    /** Notified when the picked date changes; -1 means cleared. */
    interface ChangeHandler {
        void onDateChange(double millis);
    }

    private DatePickerJs() {
    }

    /** Whether Tempus Dominus has finished loading. */
    static native boolean isReady() /*-{
        return typeof $wnd.tempusDominus !== "undefined";
    }-*/;

    /** Asks for the library; a no-op on GWT, where the entry point already injected it. */
    /**
     * Runs an action once the date picker is usable.
     *
     * <p>The GWT module injects the library as script text before the application runs,
     * so by the time a widget attaches it is there. If it is not, waiting will not help
     * -- nothing else is going to load it -- so this says so rather than failing later
     * inside the library.</p>
     */
    static void whenReady(final Runnable action) {
        ensureResources();
        if (isReady()) {
            action.run();
        } else {
            com.google.gwt.core.client.GWT.log(
                    "DatePicker: the date picker is not on the page; the module did not load it");
        }
    }

    static void ensureResources() {
    }

    static native JavaScriptObject create(Element element, String format, boolean sideBySide,
            boolean showClear, boolean showClose) /*-{
        var options = {
            display: {
                sideBySide: sideBySide,
                buttons: { today: true, clear: showClear, close: showClose },
                theme: "auto"
            }
        };
        if (format) {
            options.localization = { format: format };
        }
        return new $wnd.tempusDominus.TempusDominus(element, options);
    }-*/;

    static native void bindChange(JavaScriptObject picker, ChangeHandler handler) /*-{
        picker.subscribe($wnd.tempusDominus.Namespace.events.change, function (e) {
            var d = e && e.date ? e.date.valueOf() : -1;
            handler.@io.instanto.bootstrap5.extras.datepicker.client.ui.DatePickerJs.ChangeHandler::onDateChange(D)(d);
        });
    }-*/;

    static native void invoke(JavaScriptObject picker, String method) /*-{
        if (typeof picker[method] === "function") {
            picker[method]();
        }
    }-*/;

    static native double readValue(JavaScriptObject picker) /*-{
        var dates = picker.dates.picked;
        return dates && dates.length ? dates[0].valueOf() : -1;
    }-*/;

    static native void writeValue(JavaScriptObject picker, double millis) /*-{
        if (millis < 0) {
            picker.dates.clear();
        } else {
            picker.dates.setValue(new $wnd.tempusDominus.DateTime(millis));
        }
    }-*/;

    static native void dispose(JavaScriptObject picker) /*-{
        if (typeof picker.dispose === "function") {
            picker.dispose();
        }
    }-*/;
}
