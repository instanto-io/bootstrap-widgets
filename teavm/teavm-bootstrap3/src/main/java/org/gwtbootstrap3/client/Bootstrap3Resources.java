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
package org.gwtbootstrap3.client;

import com.google.gwt.dom.client.Document;
import org.gwtbootstrap3.client.ui.theme.Themes;
import com.google.gwt.dom.client.Element;

/**
 * Injects the library's own stylesheets on the TeaVM backend.
 *
 * <p>On GWT this class does not exist and is not needed: the module declares its
 * stylesheets with {@code <stylesheet src="..."/>} in {@code NoThemeResources.gwt.xml} and
 * the compiler injects them. TeaVM has no module system to do that, and asking every host
 * page to hand-wire the library's internals leaks mechanics the widgets exist to hide, so
 * the TeaVM backend injects the same set itself from
 * {@link Bootstrap3#initialise()}.</p>
 *
 * <p>The default Bootstrap theme is installed unless the page has already selected one.
 * Font Awesome and the library's own rules are then layered on top. {@code Themes} owns
 * the theme link and can replace its target later.</p>
 */
public final class Bootstrap3Resources {

    private static final String THEME = "bootstrap-3.4.1.min.cache.css";
    private static final String FONT_AWESOME = "font-awesome-4.7.0.min.cache.css";
    private static final String LIBRARY = "gwt-bootstrap3.cache.css";
    private static final String ID_PREFIX = "gwtbootstrap3-resource-";

    private static String base = "css/";
    private static boolean injected;

    private Bootstrap3Resources() {
    }

    /**
     * Sets where the library's stylesheets are served from, which must be called before
     * the first initialisation to have any effect. Defaults to {@code css/}.
     */
    public static void setBase(final String path) {
        base = path == null || path.isEmpty() ? "" : path.endsWith("/") ? path : path + "/";
    }

    /** The current base path. */
    public static String getBase() {
        return base;
    }

    /** Injects the stylesheets once; further calls do nothing. */
    public static void ensureInjected() {
        if (injected) {
            return;
        }
        injected = true;
        // Bootstrap itself, unless the page has already said which theme it wants.
        // Themes owns a link with this id and swaps its href to change theme; creating
        // it here means the widgets are styled from initialisation rather than from
        // the first theme switch, and a page that declares its own link keeps it.
        link(Themes.LINK_ID, base + THEME);
        link(ID_PREFIX + "font-awesome", base + FONT_AWESOME);
        link(ID_PREFIX + "library", base + LIBRARY);
    }

    /** Where this library's stylesheets are served from. */
    public static String cssBase() {
        return base;
    }

    /** Where this library's scripts are served from, beside the stylesheets. */
    public static String jsBase() {
        return scriptBase();
    }

    /** Where scripts served alongside the stylesheets live. */
    public static String scriptBase() {
        return base.endsWith("css/") ? base.substring(0, base.length() - 4) + "js/" : base;
    }

    private static void link(final String id, final String href) {
        if (Document.get().getElementById(id) != null) {
            return;
        }
        final Element link = Document.get().createElement("link");
        link.setId(id);
        link.setAttribute("rel", "stylesheet");
        link.setAttribute("href", href);
        Document.get().getHead().appendChild(link);
    }
}
