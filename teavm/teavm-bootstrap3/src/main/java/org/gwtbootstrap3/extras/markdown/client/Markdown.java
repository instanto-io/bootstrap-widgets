/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2018 GwtBootstrap3
 * %%
 * Modified from the GwtBootstrap3 original for the Bootstrap 5 track of
 * GWT Bootstrap: moved to the org.gwtbootstrap3 namespace and re-targeted
 * at Bootstrap 5 markup, class names and JavaScript APIs.
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
package org.gwtbootstrap3.extras.markdown.client;

import org.teavm.jso.JSBody;

/**
 * The TeaVM half of the markdown seam.
 *
 * <p>Same behaviour as the GWT class it replaces, reaching the parser through JSBody
 * rather than JSNI. The table classes are Bootstrap 3's, matching what a flexmark
 * server sets, so a preview is the markup the server sends rather than merely
 * something similar.</p>
 *
 * <p>GWT compiles marked and DOMPurify into the module through a ClientBundle. There
 * is no such thing here, so they are fetched by URL and this reports when they have
 * arrived rather than leaving a panel to poll for them.</p>
 */
public final class Markdown {

    private Markdown() {
    }

    /** Runs an action once the parser is usable, immediately if it already is. */
    public static void whenReady(final Runnable action) {
        MarkdownResources.whenReady(Markdown::isReady, action);
    }

    /** Starts fetching the parser if that has not already begun. */
    public static void ensureResources() {
        MarkdownResources.ensureInjected();
    }

    @JSBody(script = "if (window.marked && window.marked.setOptions) {"
            + " window.marked.setOptions({ gfm: true, breaks: false }); }")
    public static native void configure();

    public static String toHtml(final String markdown) {
        return markdown == null || markdown.isEmpty() ? "" : render(markdown);
    }

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
            + " '<table class=\"table table-striped table-condensed table-bordered\">');")
    private static native String render(String markdown);
}
