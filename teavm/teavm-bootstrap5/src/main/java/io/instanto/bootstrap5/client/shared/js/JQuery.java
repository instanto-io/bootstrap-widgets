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
package io.instanto.bootstrap5.client.shared.js;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * TeaVM stand-in for the jQuery bridge.
 *
 * <p>The GWT class is a JsInterop native type, which TeaVM cannot compile, and
 * Bootstrap 5 dropped its jQuery dependency — so the plugin entry points here map to
 * Bootstrap 5's own JavaScript API where an equivalent exists, and to a no-op where
 * Bootstrap 5 removed the concept.</p>
 *
 * <p>This file exists because two call sites in the shared source still route through
 * jQuery: {@code HTMLMixin.setHTML} and {@code AbstractButton}'s button-state call.
 * Both are Bootstrap 3 leftovers; once they are replaced on the GWT side this class
 * can go.</p>
 */
public class JQuery {

    private final Element element;

    private JQuery(final Element element) {
        this.element = element;
    }

    public static JQuery jQuery(final Element element) {
        return new JQuery(element);
    }

    public static JQuery jQuery(final String selector) {
        final HTMLElement found = query(selector);
        return new JQuery(found == null ? Document.get().createDivElement() : new Element(found));
    }

    /** Bootstrap 5 removed the button plugin; state is set through classes instead. */
    public JQuery button(final String method) {
        return this;
    }

    public JQuery html(final String htmlString) {
        element.setInnerHTML(htmlString == null ? "" : htmlString);
        return this;
    }

    public JQuery on(final String events, final EventHandler function) {
        if (events != null && function != null) {
            for (final String event : events.trim().split("\\s+")) {
                element.unwrap().addEventListener(event,
                        (Event e) -> function.callEventHandler(new com.google.gwt.user.client.Event(e)));
            }
        }
        return this;
    }

    /** Listener removal needs the original reference, which this shim does not retain. */
    public JQuery off(final String events) {
        return this;
    }

    public JQuery alert(final String method) {
        return component("Alert", method);
    }

    public JQuery carousel(final String method) {
        return component("Carousel", method);
    }

    public JQuery carousel(final int slideNumber) {
        invokeWithInt("Carousel", element.unwrap(), "to", slideNumber);
        return this;
    }

    public JQuery collapse(final String method) {
        return component("Collapse", method);
    }

    public JQuery modal(final String method) {
        return component("Modal", method);
    }

    public JQuery popover() {
        return component("Popover", null);
    }

    public JQuery popover(final String method) {
        return component("Popover", method);
    }

    public JQuery scrollspy(final String method) {
        return component("ScrollSpy", method);
    }

    public JQuery tab(final String method) {
        return component("Tab", method);
    }

    public JQuery tooltip() {
        return component("Tooltip", null);
    }

    public JQuery tooltip(final String method) {
        return component("Tooltip", method);
    }

    private JQuery component(final String name, final String method) {
        if (method == null) {
            create(name, element.unwrap());
        } else {
            invoke(name, element.unwrap(), method);
        }
        return this;
    }

    @JSBody(params = {"selector"}, script = "return document.querySelector(selector);")
    private static native HTMLElement query(String selector);

    @JSBody(params = {"name", "el"}, script =
            "if (window.bootstrap && window.bootstrap[name]) {"
            + " window.bootstrap[name].getOrCreateInstance(el); }")
    private static native void create(String name, HTMLElement el);

    @JSBody(params = {"name", "el", "method"}, script =
            "if (window.bootstrap && window.bootstrap[name]) {"
            + " var i = window.bootstrap[name].getOrCreateInstance(el);"
            + " if (i && typeof i[method] === 'function') { i[method](); } }")
    private static native void invoke(String name, HTMLElement el, String method);

    @JSBody(params = {"name", "el", "method", "arg"}, script =
            "if (window.bootstrap && window.bootstrap[name]) {"
            + " var i = window.bootstrap[name].getOrCreateInstance(el);"
            + " if (i && typeof i[method] === 'function') { i[method](arg); } }")
    private static native void invokeWithInt(String name, HTMLElement el, String method, int arg);
}
