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
package org.gwtbootstrap3.client.ui.theme;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.storage.client.Storage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The set of available themes and the one currently applied.
 *
 * <p>Switching replaces the {@code href} of a single stylesheet link, which the browser
 * swaps atomically -- so there is no flash of a half-styled page, and no accumulation of
 * stylesheets as the user changes their mind. The link is created on first use and
 * carries {@code id="gwtbootstrap3-theme"}; a page that already declares a link with that
 * id has it adopted instead, so a server-rendered initial theme survives startup without
 * a repaint.</p>
 *
 * <p>The chosen theme is remembered in {@code localStorage} when the browser allows it,
 * and restored by {@link #restore()}. Storage failures are ignored: a private window or
 * blocked site data leaves the default theme in place rather than breaking the page.</p>
 *
 * <p>Register the themes an application offers before using them:</p>
 *
 * <pre>{@code
 * Themes.register(BootswatchThemes.all());
 * Themes.restore();
 * }</pre>
 */
public final class Themes {

    /** The id of the stylesheet link this class owns. */
    public static final String LINK_ID = "gwtbootstrap3-theme";

    /** The id of the overlay link, used only by themes that have one. */
    public static final String OVERLAY_LINK_ID = "gwtbootstrap3-theme-overlay";

    private static final String STORAGE_KEY = "gwtbootstrap3.theme";
    private static final Map<String, Theme> REGISTRY = new LinkedHashMap<>();
    private static final List<ThemeChangeHandler> HANDLERS = new ArrayList<>();

    private static Theme current;

    private Themes() {
    }

    /** Adds a theme, replacing any already registered under the same name. */
    public static void register(final Theme theme) {
        if (theme != null) {
            REGISTRY.put(theme.getName(), theme);
        }
    }

    public static void register(final Iterable<Theme> themes) {
        if (themes != null) {
            for (final Theme theme : themes) {
                register(theme);
            }
        }
    }

    /** Every registered theme, in registration order. */
    public static List<Theme> getThemes() {
        return new ArrayList<>(REGISTRY.values());
    }

    public static Theme get(final String name) {
        return name == null ? null : REGISTRY.get(name);
    }

    /** The theme currently applied, or null when none has been. */
    public static Theme getCurrent() {
        return current;
    }

    /** True when the current theme has a dark background. */
    public static boolean isDark() {
        return current != null && current.isDark();
    }

    /** Applies a theme by name; does nothing when the name is not registered. */
    public static void apply(final String name) {
        apply(get(name));
    }

    /** Applies a theme and remembers the choice. */
    public static void apply(final Theme theme) {
        if (theme == null || theme.equals(current)) {
            return;
        }
        linkElement(LINK_ID, null).setAttribute("href", theme.getUrl());
        applyOverlay(theme.getOverlayUrl());
        current = theme;
        store(theme.getName());
        for (final ThemeChangeHandler handler : new ArrayList<>(HANDLERS)) {
            handler.onThemeChanged(theme);
        }
    }

    /**
     * Applies the remembered theme, or {@code fallback} when there is none.
     *
     * @return the theme applied, or null when neither was available
     */
    public static Theme restore(final Theme fallback) {
        final Theme remembered = get(read());
        final Theme theme = remembered == null ? fallback : remembered;
        apply(theme);
        return theme;
    }

    /** Applies the remembered theme, or the first registered one. */
    public static Theme restore() {
        final List<Theme> all = getThemes();
        return restore(all.isEmpty() ? null : all.get(0));
    }

    public static void addThemeChangeHandler(final ThemeChangeHandler handler) {
        if (handler != null) {
            HANDLERS.add(handler);
        }
    }

    public static void removeThemeChangeHandler(final ThemeChangeHandler handler) {
        HANDLERS.remove(handler);
    }

    /**
     * Sets or removes the overlay stylesheet. It is created after the main link so it
     * always wins the cascade, and removed outright when a theme has no overlay -- an
     * empty href would otherwise make the browser re-request the page.
     */
    private static void applyOverlay(final String overlayUrl) {
        final Element existing = Document.get().getElementById(OVERLAY_LINK_ID);
        if (overlayUrl == null || overlayUrl.isEmpty()) {
            if (existing != null) {
                existing.removeFromParent();
            }
            return;
        }
        linkElement(OVERLAY_LINK_ID, existing).setAttribute("href", overlayUrl);
    }

    /** The link with this id, adopting one already on the page or creating it. */
    private static Element linkElement(final String id, final Element known) {
        Element link = known == null ? Document.get().getElementById(id) : known;
        if (link == null) {
            link = Document.get().createElement("link");
            link.setId(id);
            link.setAttribute("rel", "stylesheet");
            Document.get().getHead().appendChild(link);
        }
        return link;
    }

    /**
     * Remembers the choice, when the browser allows it. A private window or blocked site
     * data simply means the choice is not remembered.
     */
    private static void store(final String name) {
        final Storage storage = Storage.getLocalStorageIfSupported();
        if (storage != null) {
            storage.setItem(STORAGE_KEY, name);
        }
    }

    private static String read() {
        final Storage storage = Storage.getLocalStorageIfSupported();
        return storage == null ? null : storage.getItem(STORAGE_KEY);
    }
}
