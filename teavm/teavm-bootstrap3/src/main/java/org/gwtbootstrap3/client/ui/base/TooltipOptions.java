/*
 * #%L
 * GWT Bootstrap
 * %%
 * Copyright (C) 2013 - 2018 GwtBootstrap3
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
package org.gwtbootstrap3.client.ui.base;

import org.teavm.jso.JSBody;

import com.google.gwt.dom.client.Element;

/**
 * TeaVM implementation of the tooltip and popover option writer.
 *
 * <p>Identical in package and API to the Bootstrap 3 class of the same name; it exists
 * separately only because that one declares its methods as native JsInterop, which TeaVM
 * cannot compile. Bootstrap 3 keeps a live options object on the element under the jQuery
 * data key the widget was constructed with, so writing to it is all these need to do.</p>
 */
final class TooltipOptions {

    private TooltipOptions() {
    }

    static void updateBool(final String dataTarget, final Element e, final String option,
            final boolean value) {
        setOption(dataTarget, e == null ? null : e.unwrap(), option, value);
    }

    static void updateDelay(final String dataTarget, final Element e, final int showDelay,
            final int hideDelay) {
        setDelay(dataTarget, e == null ? null : e.unwrap(), showDelay, hideDelay);
    }

    static void updateString(final String dataTarget, final Element e, final String option,
            final String value) {
        setOption(dataTarget, e == null ? null : e.unwrap(), option, value);
    }

    static void updateViewport(final String dataTarget, final Element e, final String selector,
            final int padding) {
        setViewport(dataTarget, e == null ? null : e.unwrap(), selector, padding);
    }

    @JSBody(params = {"key", "el", "option", "value"}, script =
            "if (!el || !window.jQuery) { return; }"
            + "var data = window.jQuery(el).data(key);"
            + "if (data && data.options) { data.options[option] = value; }")
    private static native void setOption(String key, Object el, String option, Object value);

    @JSBody(params = {"key", "el", "showDelay", "hideDelay"}, script =
            "if (!el || !window.jQuery) { return; }"
            + "var data = window.jQuery(el).data(key);"
            + "if (data && data.options) { data.options.delay = { show: showDelay, hide: hideDelay }; }")
    private static native void setDelay(String key, Object el, int showDelay, int hideDelay);

    @JSBody(params = {"key", "el", "selector", "padding"}, script =
            "if (!el || !window.jQuery) { return; }"
            + "var data = window.jQuery(el).data(key);"
            + "if (data && data.options) { data.options.viewport = { selector: selector, padding: padding }; }")
    private static native void setViewport(String key, Object el, String selector, int padding);
}
