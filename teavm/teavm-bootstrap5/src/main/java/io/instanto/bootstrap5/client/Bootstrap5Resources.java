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
package io.instanto.bootstrap5.client;

import com.google.gwt.dom.client.Document;
import io.instanto.bootstrap5.client.ui.theme.Themes;
import com.google.gwt.dom.client.Element;

/**
 * Injects the library's own stylesheets on the TeaVM backend.
 *
 * <p>On GWT this class does not exist and is not needed: the module declares its
 * stylesheets with {@code <stylesheet src="..."/>} in {@code NoThemeResources.gwt.xml} and
 * the compiler injects them, so an application never writes a {@code <link>} for them.
 * TeaVM has no module system to do that, and the alternative -- asking every host page to
 * hand-wire the library's internals -- leaks mechanics the widgets exist to hide. So the
 * TeaVM backend injects the same set itself, from
 * {@link Bootstrap5#initialise()}.</p>
 *
 * <p>The default Bootstrap theme is installed unless the page has already selected one.
 * The icon font and the library's own rules are then layered on top. {@code Themes} owns
 * the theme link and can replace its target later.</p>
 */
public final class Bootstrap5Resources {

    private static final String THEME = "bootstrap-5.3.8.min.cache.css";
    private static final String ICONS = "bootstrap-icons-1.13.1.min.cache.css";
    private static final String LIBRARY = "gwt-bootstrap5.cache.css";
    private static final String ID_PREFIX = "bootstrap5-resource-";

    /**
     * Where everything this module ships is served from. The TeaVM build puts its
     * stylesheets, fonts and vendored scripts under one directory, so a single setting
     * covers the library and every extra: an application says where the module was
     * deployed and nothing else has to be told anything.
     */
    private static String assetBase = "";

    private static boolean injected;

    private Bootstrap5Resources() {
    }

    /**
     * Sets where this module's assets were deployed, which must be called before the
     * first initialisation to have any effect. Everything else is derived from it, so an extra
     * never needs configuring separately.
     */
    public static void setAssetBase(final String path) {
        assetBase = normalise(path);
    }

    /** Where this module's assets were deployed. */
    public static String getAssetBase() {
        return assetBase;
    }

    /** The directory stylesheets are served from. */
    public static String cssBase() {
        return assetBase + "css/";
    }

    /** The directory vendored scripts are served from. */
    public static String jsBase() {
        return assetBase + "js/";
    }

    private static String normalise(final String path) {
        return path == null || path.isEmpty() ? "" : path.endsWith("/") ? path : path + "/";
    }

    /** @deprecated use {@link #setAssetBase(String)} */
    @Deprecated
    public static void setBase(final String path) {
        setAssetBase(path.endsWith("css/") ? path.substring(0, path.length() - 4) : path);
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
        link(Themes.LINK_ID, cssBase() + THEME);
        link(ID_PREFIX + "icons", cssBase() + ICONS);
        link(ID_PREFIX + "library", cssBase() + LIBRARY);
    }

    /**
     * Injects a script once, by URL rather than by embedding it. GWT's ClientBundle
     * inlines the vendored JavaScript into the compiled output; TeaVM has no generator to
     * do that, and loading by URL keeps 74KB of parser and sanitiser out of the module.
     * Returns immediately if a script with this id is already present.
     */
    public static void script(final String id, final String src) {
        if (Document.get().getElementById(id) != null) {
            return;
        }
        final Element script = Document.get().createElement("script");
        script.setId(id);
        script.setAttribute("src", src);
        script.setAttribute("defer", "defer");
        Document.get().getHead().appendChild(script);
    }

    /**
     * Injects a stylesheet once, for an extra that has one. The library's own sheets go
     * through {@link #ensureInjected()}; this is for modules loaded on demand.
     */
    public static void stylesheet(final String id, final String href) {
        link(id, href);
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
