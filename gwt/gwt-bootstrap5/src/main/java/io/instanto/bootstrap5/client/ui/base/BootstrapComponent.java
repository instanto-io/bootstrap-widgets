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
package io.instanto.bootstrap5.client.ui.base;

import com.google.gwt.dom.client.Element;

/**
 * Calls into Bootstrap's own JavaScript components.
 *
 * <p>The counterpart to {@link BootstrapEventBridge}: that one brings Bootstrap's events
 * out, this one takes method calls in. Keeping every call in one place means the widgets
 * hold no JSNI of their own, so a build that cannot compile JSNI -- TeaVM -- can supply
 * its own implementation of this class and share every widget unchanged.</p>
 *
 * <p>Every call is a no-op when Bootstrap's bundle or the named component is absent.</p>
 */
public final class BootstrapComponent {

    private BootstrapComponent() {
    }

    /** Creates the component instance for {@code element} without invoking anything. */
    public static native void create(Element element, String component) /*-{
        if ($wnd.bootstrap && $wnd.bootstrap[component]) {
            $wnd.bootstrap[component].getOrCreateInstance(element);
        }
    }-*/;


    /** Creates a Carousel with explicit options, replacing any instance already there. */
    public static native void createCarousel(Element element, int interval, String pause,
            boolean wrap) /*-{
        if (!$wnd.bootstrap || !$wnd.bootstrap.Carousel) {
            return;
        }
        var existing = $wnd.bootstrap.Carousel.getInstance(element);
        if (existing) {
            existing.dispose();
        }
        new $wnd.bootstrap.Carousel(element, {
            interval: interval,
            pause: pause || false,
            wrap: wrap
        });
    }-*/;

    /**
     * Creates a Tooltip- or Popover-family plugin with explicit options, replacing any
     * instance already on the element.
     */
    public static native void createPlugin(Element element, String component, boolean animation,
            boolean html, String placement, String trigger, int showDelay, int hideDelay,
            String container, String selector, String boundary, int padding, String title,
            String template, String content) /*-{
        if (!$wnd.bootstrap || !$wnd.bootstrap[component]) {
            return;
        }
        var plugin = $wnd.bootstrap[component];
        var existing = plugin.getInstance(element);
        if (existing) {
            existing.dispose();
        }
        var options = {
            animation: animation,
            html: html,
            placement: placement,
            trigger: trigger,
            delay: {show: showDelay, hide: hideDelay},
            title: title,
            template: template
        };
        if (container) options.container = container;
        if (selector) options.selector = selector;
        if (boundary) options.boundary = boundary;
        if (content != null) options.content = content;
        if (padding > 0) {
            options.popperConfig = function(defaultConfig) {
                var modifiers = defaultConfig.modifiers || [];
                for (var i = 0; i < modifiers.length; i++) {
                    if (modifiers[i].name === "preventOverflow") {
                        modifiers[i].options = modifiers[i].options || {};
                        modifiers[i].options.padding = padding;
                    }
                }
                return defaultConfig;
            };
        }
        new plugin(element, options);
    }-*/;

    /** Invokes a no-argument method on the component instance for {@code element}. */
    public static native void call(Element element, String component, String method) /*-{
        if ($wnd.bootstrap && $wnd.bootstrap[component]) {
            var instance = $wnd.bootstrap[component].getOrCreateInstance(element);
            if (instance && typeof instance[method] === "function") {
                instance[method]();
            }
        }
    }-*/;

    /** Invokes a single-int-argument method, such as the carousel's {@code to}. */
    public static native void call(Element element, String component, String method,
            int argument) /*-{
        if ($wnd.bootstrap && $wnd.bootstrap[component]) {
            var instance = $wnd.bootstrap[component].getOrCreateInstance(element);
            if (instance && typeof instance[method] === "function") {
                instance[method](argument);
            }
        }
    }-*/;

    /**
     * Creates the component with options, as {@link #create(Element, String,
     * JavaScriptObject)} does, but taking {@code toggle: false} -- which Collapse needs
     * so that constructing it does not immediately toggle the panel.
     */
    public static native void createCollapse(Element element, boolean toggle) /*-{
        if ($wnd.bootstrap && $wnd.bootstrap.Collapse) {
            $wnd.bootstrap.Collapse.getOrCreateInstance(element, {toggle: toggle});
        }
    }-*/;

    /** Invokes a method on a Collapse instance created with {@code toggle: false}. */
    public static native void callCollapse(Element element, String method) /*-{
        if ($wnd.bootstrap && $wnd.bootstrap.Collapse) {
            var instance = $wnd.bootstrap.Collapse.getOrCreateInstance(element, {toggle: false});
            if (instance && typeof instance[method] === "function") {
                instance[method]();
            }
        }
    }-*/;

    /** Disposes the component instance for {@code element}, if one exists. */
    public static native void dispose(Element element, String component) /*-{
        if ($wnd.bootstrap && $wnd.bootstrap[component]) {
            var instance = $wnd.bootstrap[component].getInstance(element);
            if (instance && typeof instance.dispose === "function") {
                instance.dispose();
            }
        }
    }-*/;

    /** Hides every open modal except {@code current}. */
    public static native void hideOtherModals(Element current) /*-{
        if (!$wnd.bootstrap || !$wnd.bootstrap.Modal) {
            return;
        }
        var open = $doc.querySelectorAll(".modal.show");
        for (var i = 0; i < open.length; i++) {
            if (open[i] !== current) {
                var instance = $wnd.bootstrap.Modal.getInstance(open[i]);
                if (instance) {
                    instance.hide();
                }
            }
        }
    }-*/;

    /** True when Bootstrap's JavaScript bundle is present on the page. */
    public static native boolean isLoaded() /*-{
        return !!($wnd.bootstrap && $wnd.bootstrap.Modal);
    }-*/;
    public static native void releaseVisibility(Element el, String name) /*-{
        if (!$wnd.bootstrap || !$wnd.bootstrap[name]) return;
        var instance = $wnd.bootstrap[name].getInstance(el);
        if (!instance) return;
        // Complete pending transitions before disposing: Bootstrap's dispose alone does not
        // cancel transition callbacks or restore an Offcanvas scroll lock.
        var finish = function() { el.dispatchEvent(new el.ownerDocument.defaultView.Event('transitionend')); };
        if (el.classList.contains('showing')) finish();
        if (el.classList.contains('show')) instance.hide();
        if (el.classList.contains('hiding') || el.classList.contains('showing')) finish();
        instance.dispose();
        el.classList.remove('show', 'showing', 'hiding');
        if (el.__bootstrapRestoreFocus) el.__bootstrapRestoreFocus();
    }-*/;

    public static native void rememberFocus(Element el) /*-{
        if (el.__bootstrapReturnFocus) return;
        var previous = el.ownerDocument.activeElement;
        if (!previous || el.contains(previous)) return;
        el.__bootstrapReturnFocus = previous;
        var restore = function() {
            el.removeEventListener('hidden.bs.offcanvas', restore);
            el.__bootstrapReturnFocus = null;
            el.__bootstrapRestoreFocus = null;
            if (previous.isConnected && typeof previous.focus === 'function') previous.focus();
        };
        el.__bootstrapRestoreFocus = restore;
        el.addEventListener('hidden.bs.offcanvas', restore);
    }-*/;
}
