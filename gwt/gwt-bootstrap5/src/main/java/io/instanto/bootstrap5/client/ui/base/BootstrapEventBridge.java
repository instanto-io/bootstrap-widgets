package io.instanto.bootstrap5.client.ui.base;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/**
 * Bridges Bootstrap's native custom events into GWT widget events.
 *
 * <p>Reached through JsInterop, so GWT and TeaVM compile this one source. Each element keeps its
 * listeners on a property, one per event name, so binding again replaces the listener and unbinding
 * removes the exact function that was added.
 */
public final class BootstrapEventBridge {
  private static final String LISTENERS = "__gwtBootstrapModernListeners";

  private BootstrapEventBridge() {}

  public static void bind(
      final Element element, final String eventName, final BootstrapEventHandler handler) {
    if (element == null || eventName == null || handler == null) {
      return;
    }
    final JsPropertyMap<Object> listeners = listeners(element, true);
    if (listeners.has(eventName)) {
      target(element).removeEventListener(eventName, Js.uncheckedCast(listeners.get(eventName)));
    }
    final Listener listener = function(event -> handler.onEvent(Events.assign(event)));
    listeners.set(eventName, listener);
    target(element).addEventListener(eventName, listener);
  }

  public static void unbind(final Element element, final String eventName) {
    final JsPropertyMap<Object> listeners = listeners(element, false);
    if (listeners == null || !listeners.has(eventName)) {
      return;
    }
    target(element).removeEventListener(eventName, Js.uncheckedCast(listeners.get(eventName)));
    listeners.delete(eventName);
  }

  public static void unbindAll(final Element element) {
    final JsPropertyMap<Object> listeners = listeners(element, false);
    if (listeners == null) {
      return;
    }
    final EventTarget target = target(element);
    listeners.forEach(
        eventName ->
            target.removeEventListener(eventName, Js.uncheckedCast(listeners.get(eventName))));
    Js.asPropertyMap(element).delete(LISTENERS);
  }

  private static JsPropertyMap<Object> listeners(final Element element, final boolean create) {
    if (element == null) {
      return null;
    }
    final JsPropertyMap<Object> node = Js.asPropertyMap(element);
    if (!node.has(LISTENERS) && create) {
      node.set(LISTENERS, JsPropertyMap.of());
    }
    return node.has(LISTENERS) ? Js.asPropertyMap(node.get(LISTENERS)) : null;
  }

  private static EventTarget target(final Element element) {
    return Js.uncheckedCast(Js.asAny(element));
  }

  /**
   * The listener as the JavaScript function the DOM holds. Passing it through a typed property
   * turns the lambda into a function once, so the same function is added, stored and later removed;
   * TeaVM would otherwise convert it afresh on each pass.
   */
  private static Listener function(final Listener listener) {
    final Slot slot = Js.uncheckedCast(JsPropertyMap.of());
    slot.setListener(listener);
    return slot.getListener();
  }

  @JsFunction
  interface Listener {
    void handle(Any event);
  }

  @JsType(isNative = true)
  interface Slot {
    @JsProperty
    void setListener(Listener listener);

    @JsProperty
    Listener getListener();
  }

  @JsType(isNative = true)
  interface EventTarget {
    void addEventListener(String type, Listener listener);

    void removeEventListener(String type, Listener listener);
  }

  /**
   * Views a JavaScript event as GWT's NativeEvent. {@code Object.assign} with no sources returns
   * its target unchanged; declaring its result as NativeEvent lets the TeaVM boundary wrap the
   * event, while on GWT it is the event itself.
   */
  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
  static class Events {
    static native NativeEvent assign(Any event);
  }
}
