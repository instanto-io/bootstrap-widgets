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

import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

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
 *
 * <p>The libraries are reached through JsInterop, so this one source serves GWT and
 * TeaVM alike. Only how the scripts arrive differs, and that is
 * {@link MarkdownResources}'s concern.</p>
 */
public final class Markdown {

    private Markdown() {
    }

    /** Runs an action once the parser is usable, immediately if it already is. */
    public static void whenReady(final Runnable action) {
        MarkdownResources.whenReady(Markdown::isReady, action);
    }

    /** Starts loading the parser if that has not already begun. */
    public static void ensureResources() {
        MarkdownResources.ensureInjected();
    }

    /** Applies the GFM options. Called once the scripts have loaded. */
    public static void configure() {
        final Marked marked = marked();
        if (marked != null && Js.asPropertyMap(marked).has("setOptions")) {
            marked.setOptions(options());
        }
    }

    /** Renders {@code markdown} to sanitised HTML. */
    public static String toHtml(final String markdown) {
        return markdown == null || markdown.isEmpty() ? "" : render(markdown);
    }

    /** Whether the parser and sanitiser have finished loading. */
    public static boolean isReady() {
        return marked() != null && purifier() != null;
    }

    private static String render(final String markdown) {
        final Marked marked = marked();
        if (marked == null) {
            return markdown;
        }
        String html = marked.parse(markdown, options());
        final DomPurify purifier = purifier();
        if (purifier != null) {
            html = purifier.sanitize(html,
                    JsPropertyMap.<Object>of("USE_PROFILES", JsPropertyMap.of("html", true)));
        }
        // flexmark is usually configured to put the Bootstrap table classes on
        // rendered tables; do the same so a preview matches the server.
        return html.replace("<table>", "<table class=\"table table-striped table-bordered\">");
    }

    private static JsPropertyMap<Object> options() {
        return JsPropertyMap.<Object>of("gfm", true, "breaks", false);
    }

    private static Marked marked() {
        return Js.uncheckedCast(Js.global().get("marked"));
    }

    private static DomPurify purifier() {
        return Js.uncheckedCast(Js.global().get("DOMPurify"));
    }

    /** The {@code marked} global. */
    @JsType(isNative = true)
    interface Marked {
        String parse(String markdown, JsPropertyMap<Object> options);

        void setOptions(JsPropertyMap<Object> options);
    }

    /** The {@code DOMPurify} global. */
    @JsType(isNative = true)
    interface DomPurify {
        String sanitize(String html, JsPropertyMap<Object> options);
    }
}
