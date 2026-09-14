package io.instanto.bootstrap5.client.ui.base;

import com.google.gwt.dom.client.Element;

/** Bridges Bootstrap's native custom events into GWT widget events. */
public final class BootstrapEventBridge {

    private BootstrapEventBridge() {
    }

    public static native void bind(Element element, String eventName, BootstrapEventHandler handler) /*-{
        var listeners = element.__gwtBootstrapModernListeners;
        if (!listeners) {
            listeners = element.__gwtBootstrapModernListeners = {};
        }
        if (listeners[eventName]) {
            element.removeEventListener(eventName, listeners[eventName]);
        }
        var listener = $entry(function(event) {
            handler.@io.instanto.bootstrap5.client.ui.base.BootstrapEventHandler::onEvent(Lcom/google/gwt/dom/client/NativeEvent;)(event);
        });
        listeners[eventName] = listener;
        element.addEventListener(eventName, listener);
    }-*/;

    public static native void unbind(Element element, String eventName) /*-{
        var listeners = element.__gwtBootstrapModernListeners;
        if (!listeners || !listeners[eventName]) {
            return;
        }
        element.removeEventListener(eventName, listeners[eventName]);
        delete listeners[eventName];
    }-*/;

    public static native void unbindAll(Element element) /*-{
        var listeners = element.__gwtBootstrapModernListeners;
        if (!listeners) {
            return;
        }
        for (var eventName in listeners) {
            if (Object.prototype.hasOwnProperty.call(listeners, eventName)) {
                element.removeEventListener(eventName, listeners[eventName]);
            }
        }
        delete element.__gwtBootstrapModernListeners;
    }-*/;
}
