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

import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.client.shared.js.JsArrays;
import io.instanto.bootstrap5.extras.slider.client.SliderResources;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsArrayLike;
import jsinterop.base.JsPropertyMap;

/**
 * The noUiSlider API, reached through JsInterop so GWT and TeaVM compile this one source.
 *
 * <p>The update callback is an interface rather than a back-reference into the widget, so the
 * widget stays plain Java; it is adapted to a JavaScript function here.
 */
final class SliderJs {

  /** Notified as a handle moves. */
  interface UpdateHandler {
    void onUpdate(double value);
  }

  private SliderJs() {}

  /** Runs an action once noUiSlider is usable, immediately if it already is. */
  static void whenReady(final Runnable action) {
    SliderResources.whenReady(SliderJs::isReady, action);
  }

  /** Starts loading noUiSlider if that has not already begun. */
  static void ensureResources() {
    SliderResources.ensureInjected();
  }

  /** Whether noUiSlider has finished loading. */
  static boolean isReady() {
    return Js.global().get("noUiSlider") != null;
  }

  static Api create(
      final Element element,
      final double min,
      final double max,
      final double step,
      final double start,
      final double end,
      final boolean range,
      final boolean tooltips,
      final boolean pips) {
    final JsPropertyMap<Object> options = JsPropertyMap.of();
    options.set("start", range ? JsArrays.of(start, end) : JsArrays.of(start));
    options.set("connect", range ? (Object) Boolean.TRUE : "lower");
    options.set("step", step);
    options.set("range", JsPropertyMap.<Object>of("min", min, "max", max));
    options.set("tooltips", tooltips);
    if (pips) {
      options.set("pips", JsPropertyMap.<Object>of("mode", "count", "values", 5d, "density", 4d));
    }
    NoUiSlider.create(element, options);
    return Js.<Target>uncheckedCast(Js.asAny(element)).getNoUiSlider();
  }

  static void bindChange(final Api slider, final UpdateHandler handler) {
    slider.on(
        "update",
        (values, handle) -> handler.onUpdate(NativeNumber.parseFloat(values.getAt(handle))));
  }

  /**
   * noUiSlider returns a string for one handle and an array of strings for two. Telling them apart
   * by the Java type holds on both platforms, where "instanceof Array" did not: from compiled GWT
   * the array can come from another realm.
   */
  static double readValue(final Api slider, final int handle) {
    final Object v = slider.get();
    return NativeNumber.parseFloat(isSingle(v) ? v : Js.asArrayLike(v).getAt(handle));
  }

  static void applyValues(final Api slider, final double lower, final double upper) {
    slider.set(isSingle(slider.get()) ? (Object) lower : JsArrays.of(lower, upper));
  }

  static void applyEnabled(final Element element, final boolean enabled) {
    if (enabled) {
      element.removeAttribute("disabled");
    } else {
      element.setAttribute("disabled", "true");
    }
  }

  static void destroy(final Api slider) {
    if ("function".equals(Js.typeof(Js.asPropertyMap(slider).get("destroy")))) {
      slider.destroy();
    }
  }

  private static boolean isSingle(final Object value) {
    return value == null || value instanceof String;
  }

  @JsFunction
  interface UpdateListener {
    void onUpdate(JsArrayLike<Object> values, int handle);
  }

  /** The element noUiSlider was created on, which then carries the slider. */
  @JsType(isNative = true)
  interface Target {
    @JsProperty
    Api getNoUiSlider();
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "noUiSlider")
  static class NoUiSlider {
    static native void create(Element target, JsPropertyMap<Object> options);
  }

  /** A slider, as noUiSlider attaches it to its element. */
  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
  static class Api {
    native void on(String event, UpdateListener listener);

    native Object get();

    native void set(Object value);

    native void destroy();
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Number")
  static class NativeNumber {
    static native double parseFloat(Object value);
  }
}
