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
package io.instanto.bootstrap5.extras.slider.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * The noUiSlider API.
 *
 * <p>Its own class because the implementation is JSNI, which the TeaVM backend cannot
 * compile. That backend excludes this file and supplies the same API through
 * {@code @JSBody}, the arrangement the widget library already uses for its seams. The
 * update callback is an interface rather than a back-reference into the widget, because
 * JSNI can call a Java method by signature and {@code @JSBody} cannot.</p>
 */
final class SliderJs {

    /** Notified as a handle moves. */
    interface UpdateHandler {
        void onUpdate(double value);
    }

    private SliderJs() {
    }


    /**
     * Ensures the library this seam wraps has been asked for. On GWT the module's entry
     * point has already injected it, so this does nothing; the TeaVM implementation of
     * this class fetches it. Widgets call this rather than the application, so adding a
     * widget is all it takes to get the widget working.
     */
    /**
     * Runs an action once noUiSlider is usable.
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
                    "Slider: noUiSlider is not on the page; the module did not load it");
        }
    }

    static void ensureResources() {
    }

    /** Whether noUiSlider has finished loading. */
    static native boolean isReady() /*-{
        return typeof $wnd.noUiSlider !== "undefined";
    }-*/;

    static native JavaScriptObject create(Element element, double min, double max, double step,
            double start, double end, boolean range, boolean tooltips, boolean pips) /*-{
        var options = {
            start: range ? [start, end] : [start],
            connect: range ? true : "lower",
            step: step,
            range: { min: min, max: max },
            tooltips: tooltips
        };
        if (pips) {
            options.pips = { mode: "count", values: 5, density: 4 };
        }
        $wnd.noUiSlider.create(element, options);
        return element.noUiSlider;
    }-*/;

    static native void bindChange(JavaScriptObject slider, UpdateHandler handler) /*-{
        slider.on("update", function (values, handle) {
            handler.@io.instanto.bootstrap5.extras.slider.client.ui.SliderJs.UpdateHandler::onUpdate(D)(
                    parseFloat(values[handle]));
        });
    }-*/;

    /**
     * noUiSlider returns a string for one handle and an array of strings for two.
     * "instanceof Array" is unreliable from compiled GWT, where the array can come from
     * another realm; a false result there silently parsed "120.00,880.00" as 120, so both
     * handles read the same. Duck-typing the array is what actually holds.
     */
    static native double readValue(JavaScriptObject slider, int handle) /*-{
        var v = slider.get();
        var isArray = v != null && typeof v !== "string" && typeof v.length === "number";
        return parseFloat(isArray ? v[handle] : v);
    }-*/;

    static native void applyValues(JavaScriptObject slider, double lower, double upper) /*-{
        var current = slider.get();
        var isArray = current != null && typeof current !== "string"
                && typeof current.length === "number";
        slider.set(isArray ? [lower, upper] : lower);
    }-*/;

    static native void applyEnabled(Element element, boolean enabled) /*-{
        if (enabled) {
            element.removeAttribute("disabled");
        } else {
            element.setAttribute("disabled", true);
        }
    }-*/;

    static native void destroy(JavaScriptObject slider) /*-{
        if (typeof slider.destroy === "function") {
            slider.destroy();
        }
    }-*/;
}
