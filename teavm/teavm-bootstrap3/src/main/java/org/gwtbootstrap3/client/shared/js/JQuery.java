/*
 * #%L
 * GWT Bootstrap
 * %%
 * Copyright (C) 2026 Carl Stainton
 * %%
 * Reimplements, over TeaVM's JSO libraries, part of the GWT client API. Class,
 * method and package names follow GWT (https://github.com/gwtproject/gwt),
 * Copyright (C) The GWT Project Authors, licensed under the Apache License,
 * Version 2.0. No GWT source is included.
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
package org.gwtbootstrap3.client.shared.js;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * TeaVM implementation of the jQuery bridge.
 *
 * <p>The GWT class is a JsInterop native type, which TeaVM cannot compile. Bootstrap 3
 * genuinely runs on jQuery, so this calls the real jQuery on the page rather than
 * emulating it; the plugin methods map one-to-one onto their jQuery counterparts.</p>
 *
 * <p>Calls are no-ops when jQuery or the relevant Bootstrap plugin is absent, so a page
 * that has not loaded them degrades rather than throwing.</p>
 */
public class JQuery {

    private final JSObject selection;

    private JQuery(final JSObject selection) {
        this.selection = selection;
    }

    public static JQuery jQuery(final Element element) {
        return new JQuery(selectElement(element.unwrap()));
    }

    public static JQuery jQuery(final String selector) {
        return new JQuery(selectSelector(selector));
    }

    public JQuery button(final String method) {
        return plugin("button", method);
    }

    public JQuery html(final String htmlString) {
        callHtml(selection, htmlString == null ? "" : htmlString);
        return this;
    }

    public JQuery on(final String events, final EventHandler function) {
        if (events != null && function != null) {
            bind(selection, events, event -> function.callEventHandler(new Event(event)));
        }
        return this;
    }

    public JQuery off(final String events) {
        unbind(selection, events);
        return this;
    }

    public JQuery alert(final String method) {
        return plugin("alert", method);
    }

    public JQuery carousel(final String method) {
        return plugin("carousel", method);
    }

    public JQuery carousel(final int slideNumber) {
        pluginWithInt(selection, "carousel", slideNumber);
        return this;
    }

    public JQuery collapse(final String method) {
        return plugin("collapse", method);
    }

    public JQuery modal(final String method) {
        return plugin("modal", method);
    }

    public JQuery popover() {
        return plugin("popover", null);
    }

    public JQuery popover(final String method) {
        return plugin("popover", method);
    }

    public JQuery scrollspy(final String method) {
        return plugin("scrollspy", method);
    }

    public JQuery tab(final String method) {
        return plugin("tab", method);
    }

    public JQuery tooltip() {
        return plugin("tooltip", null);
    }

    public JQuery tooltip(final String method) {
        return plugin("tooltip", method);
    }

    private JQuery plugin(final String name, final String method) {
        if (method == null) {
            invokePlugin(selection, name);
        } else {
            invokePluginWith(selection, name, method);
        }
        return this;
    }

    @JSBody(params = {"el"}, script = "return window.jQuery ? window.jQuery(el) : null;")
    private static native JSObject selectElement(HTMLElement el);

    @JSBody(params = {"selector"}, script = "return window.jQuery ? window.jQuery(selector) : null;")
    private static native JSObject selectSelector(String selector);

    @JSBody(params = {"sel", "name"}, script =
            "if (sel && typeof sel[name] === 'function') { sel[name](); }")
    private static native void invokePlugin(JSObject sel, String name);

    @JSBody(params = {"sel", "name", "method"}, script =
            "if (sel && typeof sel[name] === 'function') { sel[name](method); }")
    private static native void invokePluginWith(JSObject sel, String name, String method);

    @JSBody(params = {"sel", "name", "arg"}, script =
            "if (sel && typeof sel[name] === 'function') { sel[name](arg); }")
    private static native void pluginWithInt(JSObject sel, String name, int arg);

    @JSBody(params = {"sel", "html"}, script = "if (sel) { sel.html(html); }")
    private static native void callHtml(JSObject sel, String html);

    @JSBody(params = {"sel", "events", "fn"}, script = "if (sel) { sel.on(events, fn); }")
    private static native void bind(JSObject sel, String events, NativeListener fn);

    @JSBody(params = {"sel", "events"}, script = "if (sel) { sel.off(events); }")
    private static native void unbind(JSObject sel, String events);

    /** Function passed to jQuery's {@code on}. */
    @org.teavm.jso.JSFunctor
    private interface NativeListener extends JSObject {
        void handle(org.teavm.jso.dom.events.Event event);
    }
}
