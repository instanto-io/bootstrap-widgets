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

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLElement;

import io.instanto.bootstrap5.extras.slider.client.SliderResources;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * TeaVM implementation of the noUiSlider seam.
 *
 * <p>Identical in package and API to the Bootstrap 5 class of the same name; it exists
 * separately only because that one is JSNI, which TeaVM cannot compile.</p>
 */
final class SliderJs {

    /** Notified as a handle moves. */
    interface UpdateHandler {
        void onUpdate(double value);
    }

    /** The shape @JSBody can call back into. */
    private interface JsUpdate extends JSObject {
        void onUpdate(double value);
    }

    private SliderJs() {
    }


    /** Fetches the library this seam wraps, once. */
    /**
     * Runs an action once noUiSlider is usable.
     *
     * <p>The module fetches the library by URL, so this is genuinely asynchronous. The
     * presence test is passed in so a page that already loaded noUiSlider itself is
     * recognised and nothing is fetched at all.</p>
     */
    static void whenReady(final Runnable action) {
        SliderResources.whenReady(SliderJs::isReady, action);
    }

    static void ensureResources() {
        SliderResources.ensureInjected();
    }

    /** Whether noUiSlider has finished loading. */
    @JSBody(script = "return typeof window.noUiSlider !== 'undefined';")
    static native boolean isReady();

    static JavaScriptObject create(final Element element, final double min, final double max,
            final double step, final double start, final double end, final boolean range,
            final boolean tooltips, final boolean pips) {
        return JavaScriptObject.of(
                createSlider(element.unwrap(), min, max, step, start, end, range, tooltips, pips));
    }

    static void bindChange(final JavaScriptObject slider, final UpdateHandler handler) {
        if (slider == null || handler == null) {
            return;
        }
        bind(slider.unwrap(), (JsUpdate) handler::onUpdate);
    }

    static double readValue(final JavaScriptObject slider, final int handle) {
        return slider == null ? 0 : read(slider.unwrap(), handle);
    }

    static void applyValues(final JavaScriptObject slider, final double lower, final double upper) {
        if (slider != null) {
            apply(slider.unwrap(), lower, upper);
        }
    }

    static void applyEnabled(final Element element, final boolean enabled) {
        if (element == null) {
            return;
        }
        final HTMLElement target = element.unwrap();
        if (enabled) {
            target.removeAttribute("disabled");
        } else {
            target.setAttribute("disabled", "true");
        }
    }

    static void destroy(final JavaScriptObject slider) {
        if (slider != null) {
            destroySlider(slider.unwrap());
        }
    }

    @JSBody(params = {"el", "min", "max", "step", "start", "end", "range", "tooltips", "pips"},
            script = "var options = {"
            + "  start: range ? [start, end] : [start],"
            + "  connect: range ? true : 'lower',"
            + "  step: step,"
            + "  range: { min: min, max: max },"
            + "  tooltips: tooltips"
            + "};"
            + "if (pips) { options.pips = { mode: 'count', values: 5, density: 4 }; }"
            + "window.noUiSlider.create(el, options);"
            + "return el.noUiSlider;")
    private static native JSObject createSlider(HTMLElement el, double min, double max, double step,
            double start, double end, boolean range, boolean tooltips, boolean pips);

    @JSBody(params = {"slider", "handler"}, script =
            "slider.on('update', function (values, handle) {"
            + "  handler.onUpdate(parseFloat(values[handle]));"
            + "});")
    private static native void bind(JSObject slider, JsUpdate handler);

    /** Same duck-typing as the GWT side: noUiSlider returns a string for one handle. */
    @JSBody(params = {"slider", "handle"}, script =
            "var v = slider.get();"
            + "var isArray = v != null && typeof v !== 'string' && typeof v.length === 'number';"
            + "return parseFloat(isArray ? v[handle] : v);")
    private static native double read(JSObject slider, int handle);

    @JSBody(params = {"slider", "lower", "upper"}, script =
            "var current = slider.get();"
            + "var isArray = current != null && typeof current !== 'string'"
            + "        && typeof current.length === 'number';"
            + "slider.set(isArray ? [lower, upper] : lower);")
    private static native void apply(JSObject slider, double lower, double upper);

    @JSBody(params = {"slider"}, script =
            "if (typeof slider.destroy === 'function') { slider.destroy(); }")
    private static native void destroySlider(JSObject slider);
}
