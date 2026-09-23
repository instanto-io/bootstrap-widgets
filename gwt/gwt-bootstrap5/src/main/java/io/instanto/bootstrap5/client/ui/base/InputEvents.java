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
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;
import jsinterop.base.Js;

/**
 * The DOM {@code input} event, which this GWT release has no {@code InputEvent} type for.
 *
 * <p>Reached through JsInterop, so GWT and TeaVM compile this one source. A widget wanting the
 * event implements {@link Handler} rather than touching the DOM itself.
 */
public final class InputEvents {

  /** Notified on every {@code input} event, which for a range fires while dragging. */
  public interface Handler {
    void onInput();
  }

  private InputEvents() {}

  /** Calls {@code handler} whenever {@code element} raises {@code input}. */
  public static void listen(final Element element, final Handler handler) {
    if (element == null || handler == null) {
      return;
    }
    Js.<EventTarget>uncheckedCast(Js.asAny(element))
        .addEventListener("input", event -> handler.onInput());
  }

  @JsFunction
  interface Listener {
    void handle(Any event);
  }

  @JsType(isNative = true)
  interface EventTarget {
    void addEventListener(String type, Listener listener);
  }
}
