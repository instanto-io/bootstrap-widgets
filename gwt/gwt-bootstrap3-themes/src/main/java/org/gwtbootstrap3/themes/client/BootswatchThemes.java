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

package org.gwtbootstrap3.themes.client;

import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.gwtbootstrap3.client.ui.theme.Theme;

/**
 * The Bootswatch themes this artifact vendors.
 *
 * <p>Each is a complete Bootstrap 3.4.1 stylesheet, so switching replaces the sheet
 * rather than layering over it. Four are dark: Cyborg, Darkly, Slate and Superhero.</p>
 *
 * <p>Fourteen of the sixteen pull their webfonts from Google Fonts with an
 * {@code @import}, as they do upstream, so a page using them reaches the network at
 * runtime. Only Cerulean and Slate are self-contained; Slate is the only dark theme of
 * the set that is. A page that must not reach the network should use one of those two,
 * or serve the fonts itself.</p>
 *
 * <p>{@link #all()} points at the GWT module base, which is where the GWT build serves
 * them from. A TeaVM page serving them elsewhere uses {@link #all(String)}:</p>
 *
 * <pre>{@code
 * Themes.register(BootswatchThemes.all("themes/"));
 * Themes.restore(BootswatchThemes.FLATLY);
 * }</pre>
 */
public final class BootswatchThemes {

    /** A theme's identity, independent of where its stylesheet is served from. */
    private static final class Entry {

        private final String name;
        private final String displayName;
        private final boolean dark;

        Entry(final String name, final String displayName, final boolean dark) {
            this.name = name;
            this.displayName = displayName;
            this.dark = dark;
        }

        Theme at(final String basePath) {
            return new Theme(name, displayName,
                    basePath + "bootswatch-" + name + "-3.4.1.min.cache.css", dark);
        }
    }

    private static final List<Entry> ENTRIES = Arrays.asList(
            new Entry("cerulean", "Cerulean", false),
            new Entry("cosmo", "Cosmo", false),
            new Entry("cyborg", "Cyborg", true),
            new Entry("darkly", "Darkly", true),
            new Entry("flatly", "Flatly", false),
            new Entry("journal", "Journal", false),
            new Entry("lumen", "Lumen", false),
            new Entry("paper", "Paper", false),
            new Entry("readable", "Readable", false),
            new Entry("sandstone", "Sandstone", false),
            new Entry("simplex", "Simplex", false),
            new Entry("slate", "Slate", true),
            new Entry("spacelab", "Spacelab", false),
            new Entry("superhero", "Superhero", true),
            new Entry("united", "United", false),
            new Entry("yeti", "Yeti", false),
            new Entry("__none__", "", false));

    private BootswatchThemes() {
    }

    /** Every vendored theme, served from the GWT module base. */
    public static List<Theme> all() {
        return all(GWT.getModuleBaseURL() + "css/");
    }

    /** Every vendored theme, served from {@code basePath}. */
    public static List<Theme> all(final String basePath) {
        final String base = normalise(basePath);
        final List<Theme> themes = new ArrayList<>();
        for (final Entry entry : ENTRIES) {
            if (!"__none__".equals(entry.name)) {
                themes.add(entry.at(base));
            }
        }
        return themes;
    }

    /** Only the dark themes, served from {@code basePath}. */
    public static List<Theme> dark(final String basePath) {
        return filter(all(basePath), true);
    }

    /** Only the light themes, served from {@code basePath}. */
    public static List<Theme> light(final String basePath) {
        return filter(all(basePath), false);
    }

    /** The theme with this name, served from {@code basePath}, or null. */
    public static Theme byName(final String name, final String basePath) {
        for (final Theme theme : all(basePath)) {
            if (theme.getName().equals(name)) {
                return theme;
            }
        }
        return null;
    }

    private static List<Theme> filter(final List<Theme> themes, final boolean dark) {
        final List<Theme> matching = new ArrayList<>();
        for (final Theme theme : themes) {
            if (theme.isDark() == dark) {
                matching.add(theme);
            }
        }
        return matching;
    }

    private static String normalise(final String basePath) {
        if (basePath == null || basePath.isEmpty()) {
            return "";
        }
        return basePath.endsWith("/") ? basePath : basePath + "/";
    }
}
