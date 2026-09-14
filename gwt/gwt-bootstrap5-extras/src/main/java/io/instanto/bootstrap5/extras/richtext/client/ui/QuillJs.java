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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * The Quill API.
 *
 * <p>Its own class because the implementation is JSNI, which the TeaVM backend cannot
 * compile. That backend excludes this file and supplies the same API through
 * {@code @JSBody}. The change callback is an interface rather than a back-reference into
 * the widget, because JSNI can call a Java method by signature and {@code @JSBody}
 * cannot.</p>
 */
final class QuillJs {

    /** Notified on every text change. */
    interface ChangeHandler {
        void onTextChange();
    }

    private QuillJs() {
    }


    /**
     * Ensures the library this seam wraps has been asked for. On GWT the module's entry
     * point has already injected it, so this does nothing; the TeaVM implementation of
     * this class fetches it. Widgets call this rather than the application, so adding a
     * widget is all it takes to get the widget working.
     */
    /**
     * Runs an action once Quill is usable.
     *
     * <p>The GWT module injects the library as script text before the application runs,
     * so by the time a widget attaches it is there. If it is not, waiting will not help
     * -- nothing else is going to load it -- so this says so rather than failing later
     * inside the library.</p>
     */
    static void whenReady(final Runnable action) {
        ensureResources();
        if (isReady()) {
            action.run();
        } else {
            com.google.gwt.core.client.GWT.log(
                    "RichText: Quill is not on the page; the module did not load it");
        }
    }

    static void ensureResources() {
    }

    /** Whether Quill has finished loading. */
    static native boolean isReady() /*-{
        return typeof $wnd.Quill !== "undefined";
    }-*/;

    static native JavaScriptObject create(Element element, String toolbarSpec,
            String placeholder) /*-{
        var toolbar;
        if (toolbarSpec === "none") {
            toolbar = false;
        } else if (toolbarSpec === "full") {
            toolbar = [
                [{ header: [1, 2, 3, false] }],
                ["bold", "italic", "underline", "strike"],
                [{ color: [] }, { background: [] }],
                [{ list: "ordered" }, { list: "bullet" }],
                [{ align: [] }],
                ["blockquote", "code-block", "link"],
                ["clean"]
            ];
        } else {
            toolbar = [
                ["bold", "italic", "underline", "strike"],
                [{ list: "ordered" }, { list: "bullet" }],
                ["link", "clean"]
            ];
        }
        return new $wnd.Quill(element, {
            theme: "snow",
            placeholder: placeholder,
            modules: { toolbar: toolbar }
        });
    }-*/;

    static native void bindChange(JavaScriptObject quill, ChangeHandler handler) /*-{
        quill.on("text-change", function () {
            handler.@io.instanto.bootstrap5.extras.richtext.client.ui.QuillJs.ChangeHandler::onTextChange()();
        });
    }-*/;

    static native String readHtml(JavaScriptObject quill) /*-{
        return typeof quill.getSemanticHTML === "function"
            ? quill.getSemanticHTML() : quill.root.innerHTML;
    }-*/;

    static native void writeHtml(JavaScriptObject quill, String html) /*-{
        quill.setContents(quill.clipboard.convert({ html: html }), "silent");
    }-*/;

    static native String readText(JavaScriptObject quill) /*-{
        return quill.getText();
    }-*/;

    static native void applyEnabled(JavaScriptObject quill, boolean enabled) /*-{
        quill.enable(enabled);
    }-*/;

    static native void applyPlaceholder(JavaScriptObject quill, String placeholder) /*-{
        quill.root.setAttribute("data-placeholder", placeholder);
    }-*/;
}
