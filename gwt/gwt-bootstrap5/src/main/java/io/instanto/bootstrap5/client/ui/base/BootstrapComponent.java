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
import io.instanto.bootstrap5.client.shared.js.JsArrays;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsArrayLike;
import jsinterop.base.JsPropertyMap;

/**
 * Calls into Bootstrap's own JavaScript components.
 *
 * <p>The counterpart to {@link BootstrapEventBridge}: that one brings Bootstrap's events out, this
 * one takes method calls in. Keeping every call in one place means the widgets reach no JavaScript
 * of their own. The calls go through JsInterop, so GWT and TeaVM compile this one source.
 *
 * <p>Every call is a no-op when Bootstrap's bundle or the named component is absent.
 */
public final class BootstrapComponent {
  private static final String RETURN_FOCUS = "__bootstrapReturnFocus";
  private static final String RESTORE_FOCUS = "__bootstrapRestoreFocus";

  private BootstrapComponent() {}

  /** Creates the component instance for {@code element} without invoking anything. */
  public static void create(final Element element, final String component) {
    final Component type = component(component);
    if (type != null) {
      type.getOrCreateInstance(Js.asAny(element));
    }
  }

  /** Creates a Carousel with explicit options, replacing any instance already there. */
  public static void createCarousel(
      final Element element, final int interval, final String pause, final boolean wrap) {
    final Component type = component("Carousel");
    if (type == null) {
      return;
    }
    disposeExisting(type, element);
    final JsPropertyMap<Object> options = JsPropertyMap.of();
    options.set("interval", (double) interval);
    options.set("pause", isEmpty(pause) ? (Object) Boolean.FALSE : pause);
    options.set("wrap", wrap);
    construct(type, element, options);
  }

  /**
   * Creates a Tooltip- or Popover-family plugin with explicit options, replacing any instance
   * already on the element.
   */
  public static void createPlugin(
      final Element element,
      final String component,
      final boolean animation,
      final boolean html,
      final String placement,
      final String trigger,
      final int showDelay,
      final int hideDelay,
      final String container,
      final String selector,
      final String boundary,
      final int padding,
      final String title,
      final String template,
      final String content) {
    final Component type = component(component);
    if (type == null) {
      return;
    }
    disposeExisting(type, element);
    final PluginOptions typed = Js.uncheckedCast(JsPropertyMap.of());
    final JsPropertyMap<Object> options = Js.asPropertyMap(typed);
    options.set("animation", animation);
    options.set("html", html);
    options.set("placement", placement);
    options.set("trigger", trigger);
    options.set(
        "delay", JsPropertyMap.<Object>of("show", (double) showDelay, "hide", (double) hideDelay));
    options.set("title", title);
    options.set("template", template);
    if (!isEmpty(container)) {
      options.set("container", container);
    }
    if (!isEmpty(selector)) {
      options.set("selector", selector);
    }
    if (!isEmpty(boundary)) {
      options.set("boundary", boundary);
    }
    if (content != null) {
      options.set("content", content);
    }
    if (padding > 0) {
      typed.setPopperConfig(defaultConfig -> withOverflowPadding(defaultConfig, padding));
    }
    construct(type, element, options);
  }

  /** Invokes a no-argument method on the component instance for {@code element}. */
  public static void call(final Element element, final String component, final String method) {
    final Component type = component(component);
    if (type != null) {
      invoke(type.getOrCreateInstance(Js.asAny(element)), method);
    }
  }

  /** Invokes a single-int-argument method, such as the carousel's {@code to}. */
  public static void call(
      final Element element, final String component, final String method, final int argument) {
    final Component type = component(component);
    if (type == null) {
      return;
    }
    final Any instance = type.getOrCreateInstance(Js.asAny(element));
    final Function function = method(instance, method);
    if (function != null) {
      function.call(instance, argument);
    }
  }

  /**
   * Creates the component with options, as {@link #create(Element, String)} does, but taking {@code
   * toggle: false} -- which Collapse needs so that constructing it does not immediately toggle the
   * panel.
   */
  public static void createCollapse(final Element element, final boolean toggle) {
    final Component type = component("Collapse");
    if (type != null) {
      type.getOrCreateInstance(Js.asAny(element), JsPropertyMap.<Object>of("toggle", toggle));
    }
  }

  /** Invokes a method on a Collapse instance created with {@code toggle: false}. */
  public static void callCollapse(final Element element, final String method) {
    final Component type = component("Collapse");
    if (type != null) {
      invoke(
          type.getOrCreateInstance(Js.asAny(element), JsPropertyMap.<Object>of("toggle", false)),
          method);
    }
  }

  /** Disposes the component instance for {@code element}, if one exists. */
  public static void dispose(final Element element, final String component) {
    final Component type = component(component);
    if (type != null) {
      invoke(type.getInstance(Js.asAny(element)), "dispose");
    }
  }

  /** Hides every open modal except {@code current}. */
  public static void hideOtherModals(final Element current) {
    final Component modal = component("Modal");
    if (modal == null) {
      return;
    }
    final JsArrayLike<Object> open = document().querySelectorAll(".modal.show");
    final Any self = Js.asAny(current);
    for (int i = 0; i < open.getLength(); i++) {
      final Any candidate = Js.asAny(open.getAt(i));
      if (!Js.isTripleEqual(candidate, self)) {
        final Any instance = modal.getInstance(candidate);
        if (Js.isTruthy(instance)) {
          Js.<Instance>uncheckedCast(instance).hide();
        }
      }
    }
  }

  /** True when Bootstrap's JavaScript bundle is present on the page. */
  public static boolean isLoaded() {
    return component("Modal") != null;
  }

  public static void releaseVisibility(final Element el, final String name) {
    final Component type = component(name);
    if (type == null) {
      return;
    }
    final Any instance = type.getInstance(Js.asAny(el));
    if (!Js.isTruthy(instance)) {
      return;
    }
    // Complete pending transitions before disposing: Bootstrap's dispose alone does not
    // cancel transition callbacks or restore an Offcanvas scroll lock.
    if (el.hasClassName("showing")) {
      finishTransition(el);
    }
    if (el.hasClassName("show")) {
      Js.<Instance>uncheckedCast(instance).hide();
    }
    if (el.hasClassName("hiding") || el.hasClassName("showing")) {
      finishTransition(el);
    }
    Js.<Instance>uncheckedCast(instance).dispose();
    el.removeClassName("show");
    el.removeClassName("showing");
    el.removeClassName("hiding");
    final Object restore = Js.asPropertyMap(el).get(RESTORE_FOCUS);
    if (Js.isTruthy(restore)) {
      Js.<Function>uncheckedCast(restore).call(Js.asAny(el));
    }
  }

  public static void rememberFocus(final Element el) {
    final JsPropertyMap<Object> props = Js.asPropertyMap(el);
    if (Js.isTruthy(props.get(RETURN_FOCUS))) {
      return;
    }
    final Node node = Js.uncheckedCast(Js.asAny(el));
    final Any previous = node.getOwnerDocument().getActiveElement();
    if (!Js.isTruthy(previous) || node.contains(previous)) {
      return;
    }
    props.set(RETURN_FOCUS, previous);
    final Slot slot = Js.uncheckedCast(JsPropertyMap.of());
    slot.setListener(
        event -> {
          node.removeEventListener("hidden.bs.offcanvas", slot.getListener());
          props.set(RETURN_FOCUS, null);
          props.set(RESTORE_FOCUS, null);
          final JsPropertyMap<Object> element = Js.asPropertyMap(previous);
          if (Js.isTruthy(element.get("isConnected"))
              && "function".equals(Js.typeof(element.get("focus")))) {
            Js.<Node>uncheckedCast(previous).focus();
          }
        });
    final Listener restore = slot.getListener();
    props.set(RESTORE_FOCUS, restore);
    node.addEventListener("hidden.bs.offcanvas", restore);
  }

  /** Bootstrap's component class called {@code name}, or null when it is not loaded. */
  private static Component component(final String name) {
    final Object bootstrap = Js.global().get("bootstrap");
    if (!Js.isTruthy(bootstrap)) {
      return null;
    }
    final Object type = Js.asPropertyMap(bootstrap).get(name);
    return Js.isTruthy(type) ? Js.uncheckedCast(type) : null;
  }

  private static void disposeExisting(final Component type, final Element element) {
    final Any existing = type.getInstance(Js.asAny(element));
    if (Js.isTruthy(existing)) {
      Js.<Instance>uncheckedCast(existing).dispose();
    }
  }

  private static void construct(
      final Component type, final Element element, final JsPropertyMap<Object> options) {
    Reflect.construct(Js.asAny(type), JsArrays.of(Js.asAny(element), options));
  }

  private static void invoke(final Any instance, final String method) {
    final Function function = method(instance, method);
    if (function != null) {
      function.call(instance);
    }
  }

  /** The method called {@code name} on {@code instance}, when it has one. */
  private static Function method(final Any instance, final String name) {
    if (!Js.isTruthy(instance)) {
      return null;
    }
    final Object value = Js.asPropertyMap(instance).get(name);
    return "function".equals(Js.typeof(value)) ? Js.uncheckedCast(value) : null;
  }

  private static Any withOverflowPadding(final Any defaultConfig, final int padding) {
    final Object list = Js.asPropertyMap(defaultConfig).get("modifiers");
    if (Js.isTruthy(list)) {
      final JsArrayLike<Object> modifiers = Js.asArrayLike(list);
      for (int i = 0; i < modifiers.getLength(); i++) {
        final JsPropertyMap<Object> modifier = Js.asPropertyMap(modifiers.getAt(i));
        if ("preventOverflow".equals(modifier.get("name"))) {
          if (!Js.isTruthy(modifier.get("options"))) {
            modifier.set("options", JsPropertyMap.of());
          }
          Js.asPropertyMap(modifier.get("options")).set("padding", (double) padding);
        }
      }
    }
    return defaultConfig;
  }

  private static void finishTransition(final Element el) {
    final Object window =
        Js.asPropertyMap(Js.<Node>uncheckedCast(Js.asAny(el)).getOwnerDocument())
            .get("defaultView");
    final Any event =
        Reflect.construct(
            Js.asAny(Js.asPropertyMap(window).get("Event")), JsArrays.of("transitionend"));
    Js.<Node>uncheckedCast(Js.asAny(el)).dispatchEvent(event);
  }

  private static Document document() {
    return Js.uncheckedCast(Js.global().get("document"));
  }

  private static boolean isEmpty(final String value) {
    return value == null || value.isEmpty();
  }

  /** A Bootstrap component class, such as {@code bootstrap.Modal}. */
  @JsType(isNative = true)
  interface Component {
    Any getInstance(Any element);

    Any getOrCreateInstance(Any element);

    Any getOrCreateInstance(Any element, JsPropertyMap<Object> options);
  }

  /** A component instance. */
  @JsType(isNative = true)
  interface Instance {
    void hide();

    void dispose();
  }

  /** The options of a Tooltip-family plugin that take callbacks. */
  @JsType(isNative = true)
  interface PluginOptions {
    @JsProperty
    void setPopperConfig(PopperConfig config);
  }

  @JsFunction
  interface PopperConfig {
    Any apply(Any defaultConfig);
  }

  @JsFunction
  interface Listener {
    void handle(Any event);
  }

  /** Holds a listener as the JavaScript function the DOM keeps, so it can be removed. */
  @JsType(isNative = true)
  interface Slot {
    @JsProperty
    void setListener(Listener listener);

    @JsProperty
    Listener getListener();
  }

  /** A JavaScript function, called with a receiver. */
  @JsType(isNative = true)
  interface Function {
    void call(Any receiver);

    void call(Any receiver, double argument);
  }

  /** The DOM node methods these calls use. */
  @JsType(isNative = true)
  interface Node {
    @JsProperty
    Document getOwnerDocument();

    boolean contains(Any other);

    void focus();

    boolean dispatchEvent(Any event);

    void addEventListener(String type, Listener listener);

    void removeEventListener(String type, Listener listener);
  }

  @JsType(isNative = true)
  interface Document {
    @JsProperty
    Any getActiveElement();

    JsArrayLike<Object> querySelectorAll(String selectors);
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Reflect")
  static class Reflect {
    static native Any construct(Any target, JsArrayLike<Object> arguments);
  }
}
