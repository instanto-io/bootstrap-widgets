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
package io.instanto.bootstrap5.themes.client;

import java.util.ArrayList;
import java.util.List;

import io.instanto.bootstrap5.client.ui.theme.Theme;

/**
 * The Bootswatch 5.3.8 stylesheets vendored by this module, as a catalogue of
 * {@link Theme} instances ready to hand to
 * {@link io.instanto.bootstrap5.client.ui.theme.Themes#register(Iterable)}.
 *
 * <p>Each is a complete replacement for bootstrap.css rather than an overlay,
 * so exactly one is active at a time. The dark flag is not a guess: it was
 * derived from each stylesheet's own {@code --bs-body-bg} and
 * {@code --bs-body-color}, a theme counting as dark when its text is lighter
 * than its ground. {@link io.instanto.bootstrap5.client.ui.theme.Themes} uses it to
 * set {@code data-bs-theme}, which is what Bootstrap 5.3's own components read.</p>
 *
 * <p>Bootswatch is MIT licensed; see BOOTSWATCH-LICENSE alongside these files.</p>
 */
public enum BootswatchThemes {

    BRITE("Brite", false),
    CERULEAN("Cerulean", false),
    COSMO("Cosmo", false),
    CYBORG("Cyborg", true),
    DARKLY("Darkly", true),
    FLATLY("Flatly", false),
    JOURNAL("Journal", false),
    LITERA("Litera", false),
    LUMEN("Lumen", false),
    LUX("Lux", false),
    MATERIA("Materia", false),
    MINTY("Minty", false),
    MORPH("Morph", false),
    PULSE("Pulse", false),
    QUARTZ("Quartz", true),
    SANDSTONE("Sandstone", false),
    SIMPLEX("Simplex", false),
    SKETCHY("Sketchy", false),
    SLATE("Slate", true),
    SOLAR("Solar", true),
    SPACELAB("Spacelab", false),
    SUPERHERO("Superhero", true),
    UNITED("United", false),
    VAPOR("Vapor", true),
    YETI("Yeti", false),
    ZEPHYR("Zephyr", false);

    private static final String VERSION = "5.3.8";

    private final String displayName;

    private final boolean dark;

    BootswatchThemes(final String displayName, final boolean dark) {
        this.displayName = displayName;
        this.dark = dark;
    }

    /** The stylesheet name, e.g. {@code flatly}. */
    public String getThemeName() {
        return name().toLowerCase();
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isDark() {
        return dark;
    }

    Theme at(final String basePath) {
        return new Theme(getThemeName(), displayName,
                basePath + "bootswatch-" + getThemeName() + "-" + VERSION + ".min.cache.css", dark);
    }

    /** Every vendored theme, served from this module's own resources. */
    public static List<Theme> all() {
        return all(com.google.gwt.core.client.GWT.getModuleBaseURL() + "css/");
    }

    /** Every vendored theme, served from {@code basePath}. */
    public static List<Theme> all(final String basePath) {
        final String base = normalise(basePath);
        final List<Theme> themes = new ArrayList<Theme>();
        for (final BootswatchThemes theme : values()) {
            themes.add(theme.at(base));
        }
        return themes;
    }

    /** Only the dark themes, served from {@code basePath}. */
    public static List<Theme> dark(final String basePath) {
        return filter(basePath, true);
    }

    /** Only the light themes, served from {@code basePath}. */
    public static List<Theme> light(final String basePath) {
        return filter(basePath, false);
    }

    /** The theme with this name, served from {@code basePath}, or null. */
    public static Theme byName(final String name, final String basePath) {
        if (name == null) {
            return null;
        }
        final String base = normalise(basePath);
        for (final BootswatchThemes theme : values()) {
            if (theme.getThemeName().equals(name.toLowerCase())) {
                return theme.at(base);
            }
        }
        return null;
    }

    private static List<Theme> filter(final String basePath, final boolean wantDark) {
        final String base = normalise(basePath);
        final List<Theme> themes = new ArrayList<Theme>();
        for (final BootswatchThemes theme : values()) {
            if (theme.dark == wantDark) {
                themes.add(theme.at(base));
            }
        }
        return themes;
    }

    private static String normalise(final String basePath) {
        if (basePath == null || basePath.isEmpty()) {
            return "";
        }
        return basePath.endsWith("/") ? basePath : basePath + "/";
    }
}
