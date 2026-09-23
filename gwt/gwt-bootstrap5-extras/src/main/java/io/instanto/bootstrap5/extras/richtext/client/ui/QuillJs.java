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
package io.instanto.bootstrap5.extras.richtext.client.ui;

import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.NativeJson;
import io.instanto.bootstrap5.extras.richtext.client.RichTextResources;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/**
 * Quill, reached through JsInterop so GWT and TeaVM compile this one source.
 *
 * <p>The change callback is an interface rather than a back-reference into the widget, so the
 * widget stays plain Java; it is adapted to a JavaScript function here.
 */
final class QuillJs {

  /** Notified when the text changes. */
  interface ChangeHandler {
    void onTextChange();
  }

  /** Quill's toolbar groups, as its toolbar module takes them. */
  private static final String FULL_TOOLBAR =
      "[[{\"header\":[1,2,3,false]}],"
          + "[\"bold\",\"italic\",\"underline\",\"strike\"],"
          + "[{\"color\":[]},{\"background\":[]}],"
          + "[{\"list\":\"ordered\"},{\"list\":\"bullet\"}],"
          + "[{\"align\":[]}],"
          + "[\"blockquote\",\"code-block\",\"link\"],"
          + "[\"clean\"]]";

  private static final String BASIC_TOOLBAR =
      "[[\"bold\",\"italic\",\"underline\",\"strike\"],"
          + "[{\"list\":\"ordered\"},{\"list\":\"bullet\"}],"
          + "[\"link\",\"clean\"]]";

  private QuillJs() {}

  /** Runs an action once Quill is usable, immediately if it already is. */
  static void whenReady(final Runnable action) {
    RichTextResources.whenReady(QuillJs::isReady, action);
  }

  /** Starts loading Quill if that has not already begun. */
  static void ensureResources() {
    RichTextResources.ensureInjected();
  }

  /** Whether Quill has finished loading. */
  static boolean isReady() {
    return Js.global().get("Quill") != null;
  }

  static Quill create(final Element element, final String toolbarSpec, final String placeholder) {
    final Object toolbar;
    if ("none".equals(toolbarSpec)) {
      toolbar = Boolean.FALSE;
    } else if ("full".equals(toolbarSpec)) {
      toolbar = NativeJson.parse(FULL_TOOLBAR);
    } else {
      toolbar = NativeJson.parse(BASIC_TOOLBAR);
    }
    final JsPropertyMap<Object> options =
        JsPropertyMap.<Object>of(
            "theme",
            "snow",
            "placeholder",
            placeholder,
            "modules",
            JsPropertyMap.<Object>of("toolbar", toolbar));
    return new Quill(Js.asAny(element), options);
  }

  static void bindChange(final Quill quill, final ChangeHandler handler) {
    quill.on("text-change", handler::onTextChange);
  }

  static String readHtml(final Quill quill) {
    return "function".equals(Js.typeof(Js.asPropertyMap(quill).get("getSemanticHTML")))
        ? quill.getSemanticHTML()
        : Js.asString(Js.asPropertyMap(quill.getRoot()).get("innerHTML"));
  }

  static void writeHtml(final Quill quill, final String html) {
    quill.setContents(
        quill.getClipboard().convert(JsPropertyMap.<Object>of("html", html)), "silent");
  }

  static String readText(final Quill quill) {
    return quill.getText();
  }

  static void applyEnabled(final Quill quill, final boolean enabled) {
    quill.enable(enabled);
  }

  static void applyPlaceholder(final Quill quill, final String placeholder) {
    Js.<Root>uncheckedCast(quill.getRoot()).setAttribute("data-placeholder", placeholder);
  }

  @JsFunction
  interface Listener {
    void handle();
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Quill")
  static class Quill {
    Quill(Any element, JsPropertyMap<Object> options) {}

    native void on(String event, Listener listener);

    native String getSemanticHTML();

    native String getText();

    native void setContents(Any delta, String source);

    native void enable(boolean enabled);

    @JsProperty(name = "root")
    native Any getRoot();

    @JsProperty(name = "clipboard")
    native Clipboard getClipboard();
  }

  /** Quill's clipboard module, which turns HTML into a delta. */
  @JsType(isNative = true)
  interface Clipboard {
    Any convert(JsPropertyMap<Object> source);
  }

  /** The editable element Quill renders into. */
  @JsType(isNative = true)
  interface Root {
    void setAttribute(String name, String value);
  }
}
