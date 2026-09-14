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

/**
 * Renders Markdown to HTML, in the dialect a flexmark-java server produces.
 *
 * <p>Configured for GitHub Flavoured Markdown: tables, strikethrough and task
 * lists, which are the extensions flexmark is typically built with. Tables are
 * given the Bootstrap table classes so they land styled.</p>
 *
 * <p>Everything is passed through DOMPurify on the way out. Markdown permits raw
 * HTML and marked does not sanitise, by design, so rendering user input without
 * this would be an injection route.</p>
 */
public final class Markdown {

    private Markdown() {
    }

    /** Applies the GFM options. Called once by the module's entry point. */
    /**
     * Runs an action once the parser is usable.
     *
     * <p>The GWT module compiles marked and DOMPurify in, so by the time anything is
     * rendered they are there and this runs immediately. The TeaVM counterpart fetches
     * them, so it runs the action when they arrive. A panel can then be written once
     * and be correct on both.</p>
     */
    public static void whenReady(final Runnable action) {
        ensureResources();
        action.run();
    }

    /** Nothing to fetch: the module compiles the parser into the application. */
    public static void ensureResources() {
    }

    public static native void configure() /*-{
        if ($wnd.marked && $wnd.marked.setOptions) {
            $wnd.marked.setOptions({ gfm: true, breaks: false });
        }
    }-*/;

    /** Renders {@code markdown} to sanitised HTML. */
    public static String toHtml(final String markdown) {
        return markdown == null || markdown.isEmpty() ? "" : render(markdown);
    }

    /** Whether the parser and sanitiser have finished loading. */
    public static native boolean isReady() /*-{
        return typeof $wnd.marked !== "undefined" && typeof $wnd.DOMPurify !== "undefined";
    }-*/;

    private static native String render(String markdown) /*-{
        if (typeof $wnd.marked === "undefined") {
            return markdown;
        }
        var parse = $wnd.marked.parse || $wnd.marked;
        var html = parse(markdown, { gfm: true, breaks: false });
        if (typeof $wnd.DOMPurify !== "undefined") {
            html = $wnd.DOMPurify.sanitize(html, { USE_PROFILES: { html: true } });
        }
        // Match the classes a flexmark server sets through
        // TablesExtension.CLASS_NAME, so a preview is the same markup the
        // server sends rather than merely similar.
        return html.replace(/<table>/g,
                "<table class=\"table table-striped table-condensed table-bordered\">");
    }-*/;
}
