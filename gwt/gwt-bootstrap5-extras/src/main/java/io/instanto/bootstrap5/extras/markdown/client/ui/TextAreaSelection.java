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
package io.instanto.bootstrap5.extras.markdown.client.ui;

import com.google.gwt.dom.client.TextAreaElement;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;

/**
 * The text-area selection API, which GWT's TextAreaElement does not expose. Reached through
 * JsInterop so GWT and TeaVM compile this one source.
 */
final class TextAreaSelection {

  private TextAreaSelection() {}

  static int start(final TextAreaElement element) {
    return view(element).getSelectionStart();
  }

  static int end(final TextAreaElement element) {
    return view(element).getSelectionEnd();
  }

  static void focusAndSelect(final TextAreaElement element, final int start, final int length) {
    final Selectable area = view(element);
    area.focus();
    area.setSelectionRange(start, start + length);
  }

  private static Selectable view(final TextAreaElement element) {
    return Js.uncheckedCast(Js.asAny(element));
  }

  /** The native text area, as far as selection goes. */
  @JsType(isNative = true)
  interface Selectable {
    @JsProperty
    int getSelectionStart();

    @JsProperty
    int getSelectionEnd();

    void focus();

    void setSelectionRange(int start, int end);
  }
}
