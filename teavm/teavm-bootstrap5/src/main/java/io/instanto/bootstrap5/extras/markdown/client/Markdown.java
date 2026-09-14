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
package io.instanto.bootstrap5.extras.markdown.client;

import org.teavm.jso.JSBody;

/**
 * TeaVM implementation of the Markdown renderer.
 *
 * <p>Identical in package and API to the Bootstrap 5 class of the same name; it exists
 * separately only because that one is entirely JSNI, which TeaVM cannot compile. The
 * behaviour is the same: GitHub Flavoured Markdown through marked, sanitised through
 * DOMPurify because Markdown permits raw HTML and marked does not sanitise, and the
 * Bootstrap table classes applied so a preview matches what a flexmark server produces.</p>
 */
public final class Markdown {

    private Markdown() {
    }

    /** Fetches the parser and sanitiser, once. */
    /**
     * Runs an action once the parser is usable.
     *
     * <p>The module fetches the library by URL, so this is genuinely asynchronous. The
     * presence test is passed in so a page that already loaded the parser itself is
     * recognised and nothing is fetched at all.</p>
     */
    public static void whenReady(final Runnable action) {
        MarkdownResources.whenReady(Markdown::isReady, action);
    }

    public static void ensureResources() {
        MarkdownResources.ensureInjected();
    }

    /** Applies the GFM options. Called once the scripts have loaded. */
    @JSBody(script = "if (window.marked && window.marked.setOptions) {"
            + " window.marked.setOptions({ gfm: true, breaks: false }); }")
    public static native void configure();

    /** Renders {@code markdown} to sanitised HTML. */
    public static String toHtml(final String markdown) {
        return markdown == null || markdown.isEmpty() ? "" : render(markdown);
    }

    /** Whether the parser and sanitiser have finished loading. */
    @JSBody(script = "return typeof window.marked !== 'undefined'"
            + " && typeof window.DOMPurify !== 'undefined';")
    public static native boolean isReady();

    @JSBody(params = {"markdown"}, script =
            "if (typeof window.marked === 'undefined') { return markdown; }"
            + "var parse = window.marked.parse || window.marked;"
            + "var html = parse(markdown, { gfm: true, breaks: false });"
            + "if (typeof window.DOMPurify !== 'undefined') {"
            + "  html = window.DOMPurify.sanitize(html, { USE_PROFILES: { html: true } });"
            + "}"
            + "return html.replace(/<table>/g,"
            + " '<table class=\"table table-striped table-bordered\">');")
    private static native String render(String markdown);
}
