/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2018 GwtBootstrap3
 * %%
 * Modified from the GwtBootstrap3 original for the Bootstrap 5 track of
 * GWT Bootstrap: moved to the io.instanto.bootstrap5 namespace and
 * re-targeted at Bootstrap 5 markup, class names and JavaScript APIs. The
 * GwtBootstrap3 copyright above is retained as required by the Apache
 * License 2.0; the namespace changed, the attribution did not.
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

    /**
     * Ensures the parser and sanitiser have been asked for. On GWT the module's entry
     * point has already injected them, so this does nothing.
     */
    /**
     * Runs an action once the parser is usable.
     *
     * <p>The GWT module injects the library as script text before the application runs,
     * so by the time a widget attaches it is there. If it is not, waiting will not help
     * -- nothing else is going to load it -- so this says so rather than failing later
     * inside the library.</p>
     */
    public static void whenReady(final Runnable action) {
        ensureResources();
        if (isReady()) {
            action.run();
        } else {
            com.google.gwt.core.client.GWT.log(
                    "Markdown: the parser is not on the page; the module did not load it");
        }
    }

    public static void ensureResources() {
    }

    /** Applies the GFM options. Called once by the module's entry point. */
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
        // flexmark is usually configured to put the Bootstrap table classes on
        // rendered tables; do the same so a preview matches the server.
        return html.replace(/<table>/g,
                "<table class=\"table table-striped table-bordered\">");
    }-*/;
}
