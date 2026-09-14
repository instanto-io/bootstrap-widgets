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

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import io.instanto.bootstrap5.extras.richtext.client.RichTextResources;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * TeaVM implementation of the Quill seam.
 *
 * <p>Identical in package and API to the Bootstrap 5 class of the same name; it exists
 * separately only because that one is JSNI, which TeaVM cannot compile.</p>
 */
final class QuillJs {

    /** Notified on every text change. */
    interface ChangeHandler {
        void onTextChange();
    }

    /** The shape @JSBody can call back into. */
    private interface JsChange extends JSObject {
        void onTextChange();
    }

    private QuillJs() {
    }


    /** Fetches the library this seam wraps, once. */
    /**
     * Runs an action once Quill is usable.
     *
     * <p>The module fetches the library by URL, so this is genuinely asynchronous. The
     * presence test is passed in so a page that already loaded Quill itself is
     * recognised and nothing is fetched at all.</p>
     */
    static void whenReady(final Runnable action) {
        RichTextResources.whenReady(QuillJs::isReady, action);
    }

    static void ensureResources() {
        RichTextResources.ensureInjected();
    }

    /** Whether Quill has finished loading. */
    @JSBody(script = "return typeof window.Quill !== 'undefined';")
    static native boolean isReady();

    static JavaScriptObject create(final Element element, final String toolbarSpec,
            final String placeholder) {
        return JavaScriptObject.of(createQuill(element.unwrap(), toolbarSpec, placeholder));
    }

    static void bindChange(final JavaScriptObject quill, final ChangeHandler handler) {
        if (quill != null && handler != null) {
            bind(quill.unwrap(), (JsChange) handler::onTextChange);
        }
    }

    static String readHtml(final JavaScriptObject quill) {
        return quill == null ? "" : html(quill.unwrap());
    }

    static void writeHtml(final JavaScriptObject quill, final String value) {
        if (quill != null) {
            setHtml(quill.unwrap(), value == null ? "" : value);
        }
    }

    static String readText(final JavaScriptObject quill) {
        return quill == null ? "" : text(quill.unwrap());
    }

    static void applyEnabled(final JavaScriptObject quill, final boolean enabled) {
        if (quill != null) {
            enable(quill.unwrap(), enabled);
        }
    }

    static void applyPlaceholder(final JavaScriptObject quill, final String placeholder) {
        if (quill != null) {
            placeholderOf(quill.unwrap(), placeholder == null ? "" : placeholder);
        }
    }

    @JSBody(params = {"el", "toolbarSpec", "placeholder"}, script =
            "var toolbar;"
            + "if (toolbarSpec === 'none') { toolbar = false; }"
            + "else if (toolbarSpec === 'full') {"
            + "  toolbar = ["
            + "    [{ header: [1, 2, 3, false] }],"
            + "    ['bold', 'italic', 'underline', 'strike'],"
            + "    [{ color: [] }, { background: [] }],"
            + "    [{ list: 'ordered' }, { list: 'bullet' }],"
            + "    [{ align: [] }],"
            + "    ['blockquote', 'code-block', 'link'],"
            + "    ['clean']"
            + "  ];"
            + "} else {"
            + "  toolbar = ["
            + "    ['bold', 'italic', 'underline', 'strike'],"
            + "    [{ list: 'ordered' }, { list: 'bullet' }],"
            + "    ['link', 'clean']"
            + "  ];"
            + "}"
            + "return new window.Quill(el, {"
            + "  theme: 'snow', placeholder: placeholder, modules: { toolbar: toolbar }"
            + "});")
    private static native JSObject createQuill(Object el, String toolbarSpec, String placeholder);

    @JSBody(params = {"quill", "handler"}, script =
            "quill.on('text-change', function () { handler.onTextChange(); });")
    private static native void bind(JSObject quill, JsChange handler);

    @JSBody(params = {"quill"}, script =
            "return typeof quill.getSemanticHTML === 'function'"
            + " ? quill.getSemanticHTML() : quill.root.innerHTML;")
    private static native String html(JSObject quill);

    @JSBody(params = {"quill", "value"}, script =
            "quill.setContents(quill.clipboard.convert({ html: value }), 'silent');")
    private static native void setHtml(JSObject quill, String value);

    @JSBody(params = {"quill"}, script = "return quill.getText();")
    private static native String text(JSObject quill);

    @JSBody(params = {"quill", "enabled"}, script = "quill.enable(enabled);")
    private static native void enable(JSObject quill, boolean enabled);

    @JSBody(params = {"quill", "placeholder"}, script =
            "quill.root.setAttribute('data-placeholder', placeholder);")
    private static native void placeholderOf(JSObject quill, String placeholder);
}
