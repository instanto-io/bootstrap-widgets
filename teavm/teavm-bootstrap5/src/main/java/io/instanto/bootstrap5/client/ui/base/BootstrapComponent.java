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
package io.instanto.bootstrap5.client.ui.base;

import com.google.gwt.dom.client.Element;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * TeaVM implementation of the Bootstrap component seam.
 *
 * <p>Same contract as the GWT class of this name, reached through {@code @JSBody} rather
 * than JSNI. Because every widget calls Bootstrap through this one class, the widgets
 * themselves are shared source between the GWT and TeaVM builds.</p>
 */
public final class BootstrapComponent {

    private BootstrapComponent() {
    }

    public static void create(final Element element, final String component) {
        createInstance(component, element.unwrap());
    }

    public static void createCarousel(final Element element, final int interval,
            final String pause, final boolean wrap) {
        newCarousel(element.unwrap(), interval, pause, wrap);
    }

    public static void createPlugin(final Element element, final String component,
            final boolean animation, final boolean html, final String placement,
            final String trigger, final int showDelay, final int hideDelay,
            final String container, final String selector, final String boundary,
            final int padding, final String title, final String template, final String content) {
        newPlugin(element.unwrap(), component, animation, html, placement, trigger, showDelay,
                hideDelay, container, selector, boundary, padding, title, template, content);
    }

    public static void call(final Element element, final String component, final String method) {
        invoke(component, element.unwrap(), method);
    }

    public static void call(final Element element, final String component, final String method,
            final int argument) {
        invokeWithInt(component, element.unwrap(), method, argument);
    }

    public static void createCollapse(final Element element, final boolean toggle) {
        newCollapse(element.unwrap(), toggle);
    }

    public static void callCollapse(final Element element, final String method) {
        invokeCollapse(element.unwrap(), method);
    }

    public static void dispose(final Element element, final String component) {
        disposeInstance(component, element.unwrap());
    }

    public static void hideOtherModals(final Element current) {
        hideOthers(current.unwrap());
    }

    public static boolean isLoaded() {
        return bootstrapPresent();
    }

    @JSBody(params = {"name", "el"}, script =
            "if (window.bootstrap && window.bootstrap[name]) {"
            + " window.bootstrap[name].getOrCreateInstance(el); }")
    private static native void createInstance(String name, HTMLElement el);

    @JSBody(params = {"el", "interval", "pause", "wrap"}, script =
            "if (!window.bootstrap || !window.bootstrap.Carousel) { return; }"
            + " var existing = window.bootstrap.Carousel.getInstance(el);"
            + " if (existing) { existing.dispose(); }"
            + " new window.bootstrap.Carousel(el,"
            + "   {interval: interval, pause: pause || false, wrap: wrap});")
    private static native void newCarousel(HTMLElement el, int interval, String pause, boolean wrap);

    @JSBody(params = {"el", "component", "animation", "html", "placement", "trigger",
            "showDelay", "hideDelay", "container", "selector", "boundary", "padding",
            "title", "template", "content"}, script =
            "if (!window.bootstrap || !window.bootstrap[component]) { return; }"
            + " var plugin = window.bootstrap[component];"
            + " var existing = plugin.getInstance(el);"
            + " if (existing) { existing.dispose(); }"
            + " var options = {animation: animation, html: html, placement: placement,"
            + "   trigger: trigger, delay: {show: showDelay, hide: hideDelay},"
            + "   title: title, template: template};"
            + " if (container) { options.container = container; }"
            + " if (selector) { options.selector = selector; }"
            + " if (boundary) { options.boundary = boundary; }"
            + " if (content != null) { options.content = content; }"
            + " if (padding > 0) {"
            + "   options.popperConfig = function(defaultConfig) {"
            + "     var modifiers = defaultConfig.modifiers || [];"
            + "     for (var i = 0; i < modifiers.length; i++) {"
            + "       if (modifiers[i].name === 'preventOverflow') {"
            + "         modifiers[i].options = modifiers[i].options || {};"
            + "         modifiers[i].options.padding = padding; } }"
            + "     return defaultConfig; }; }"
            + " new plugin(el, options);")
    private static native void newPlugin(HTMLElement el, String component, boolean animation,
            boolean html, String placement, String trigger, int showDelay, int hideDelay,
            String container, String selector, String boundary, int padding, String title,
            String template, String content);

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

    @JSBody(params = {"el", "toggle"}, script =
            "if (window.bootstrap && window.bootstrap.Collapse) {"
            + " window.bootstrap.Collapse.getOrCreateInstance(el, {toggle: toggle}); }")
    private static native void newCollapse(HTMLElement el, boolean toggle);

    @JSBody(params = {"el", "method"}, script =
            "if (window.bootstrap && window.bootstrap.Collapse) {"
            + " var i = window.bootstrap.Collapse.getOrCreateInstance(el, {toggle: false});"
            + " if (i && typeof i[method] === 'function') { i[method](); } }")
    private static native void invokeCollapse(HTMLElement el, String method);

    @JSBody(params = {"name", "el"}, script =
            "if (window.bootstrap && window.bootstrap[name]) {"
            + " var i = window.bootstrap[name].getInstance(el);"
            + " if (i && typeof i.dispose === 'function') { i.dispose(); } }")
    private static native void disposeInstance(String name, HTMLElement el);

    @JSBody(params = {"current"}, script =
            "if (!window.bootstrap || !window.bootstrap.Modal) { return; }"
            + " var open = document.querySelectorAll('.modal.show');"
            + " for (var i = 0; i < open.length; i++) {"
            + "   if (open[i] !== current) {"
            + "     var m = window.bootstrap.Modal.getInstance(open[i]);"
            + "     if (m) { m.hide(); } } }")
    private static native void hideOthers(HTMLElement current);

    @JSBody(script = "return !!(window.bootstrap && window.bootstrap.Modal);")
    private static native boolean bootstrapPresent();
    public static void releaseVisibility(Element element, String component) { releaseVisibilityNative(element.unwrap(), component); }
    public static void rememberFocus(Element element) { rememberFocusNative(element.unwrap()); }

    @JSBody(params = {"el", "name"}, script =
            "if (!window.bootstrap || !window.bootstrap[name]) return;\n"
             + "var instance = window.bootstrap[name].getInstance(el);\n"
             + "if (!instance) return;\n"
             + "// Complete pending transitions before disposing: Bootstrap's dispose alone does not\n"
             + "// cancel transition callbacks or restore an Offcanvas scroll lock.\n"
             + "var finish = function() { el.dispatchEvent(new el.ownerDocument.defaultView.Event('transitionend')); };\n"
             + "if (el.classList.contains('showing')) finish();\n"
             + "if (el.classList.contains('show')) instance.hide();\n"
             + "if (el.classList.contains('hiding') || el.classList.contains('showing')) finish();\n"
             + "instance.dispose();\n"
             + "el.classList.remove('show', 'showing', 'hiding');\n"
             + "if (el.__bootstrapRestoreFocus) el.__bootstrapRestoreFocus();\n")
    private static native void releaseVisibilityNative(HTMLElement el, String name);

    @JSBody(params = {"el"}, script =
            "if (el.__bootstrapReturnFocus) return;\n"
             + "var previous = el.ownerDocument.activeElement;\n"
             + "if (!previous || el.contains(previous)) return;\n"
             + "el.__bootstrapReturnFocus = previous;\n"
             + "var restore = function() {\n"
             + "    el.removeEventListener('hidden.bs.offcanvas', restore);\n"
             + "    el.__bootstrapReturnFocus = null;\n"
             + "    el.__bootstrapRestoreFocus = null;\n"
             + "    if (previous.isConnected && typeof previous.focus === 'function') previous.focus();\n"
             + "};\n"
             + "el.__bootstrapRestoreFocus = restore;\n"
             + "el.addEventListener('hidden.bs.offcanvas', restore);\n")
    private static native void rememberFocusNative(HTMLElement el);
}
