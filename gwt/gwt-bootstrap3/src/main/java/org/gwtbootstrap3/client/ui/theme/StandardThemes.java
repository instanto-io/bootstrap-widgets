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

import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stock Bootstrap, as themes the switcher can select.
 *
 * <p>These are the stylesheets this module already vendors, so they need no extra
 * artifact. Registering them gives a user somewhere to go back to when the other themes
 * on offer are all restyled.</p>
 *
 * <p>{@link #BOOTSTRAP} is Bootstrap 3.4.1 on its own. {@link #BOOTSTRAP_THEME} adds the
 * optional lookalike theme as an overlay, which is what {@code GwtBootstrap3Theme}
 * inherits at compile time -- the same pairing, chosen at runtime instead.</p>
 */
public final class StandardThemes {

    /** Plain Bootstrap 3.4.1. */
    public static final String BOOTSTRAP = "bootstrap";

    /** Bootstrap 3.4.1 with the optional lookalike theme layered on top. */
    public static final String BOOTSTRAP_THEME = "bootstrap-theme";

    private StandardThemes() {
    }

    /** Both stock themes, served from the GWT module base. */
    public static List<Theme> all() {
        return all(GWT.getModuleBaseURL() + "css/");
    }

    /** Both stock themes, served from {@code basePath}. */
    public static List<Theme> all(final String basePath) {
        return new ArrayList<>(Arrays.asList(bootstrap(basePath), bootstrapTheme(basePath)));
    }

    /** Plain Bootstrap, served from {@code basePath}. */
    public static Theme bootstrap(final String basePath) {
        return new Theme(BOOTSTRAP, "Bootstrap", base(basePath), null, false);
    }

    /** Bootstrap with the lookalike theme overlaid, served from {@code basePath}. */
    public static Theme bootstrapTheme(final String basePath) {
        return new Theme(BOOTSTRAP_THEME, "Bootstrap (theme)", base(basePath),
                normalise(basePath) + "bootstrap-theme-3.4.1.min.cache.css", false);
    }

    private static String base(final String basePath) {
        return normalise(basePath) + "bootstrap-3.4.1.min.cache.css";
    }

    private static String normalise(final String basePath) {
        if (basePath == null || basePath.isEmpty()) {
            return "";
        }
        return basePath.endsWith("/") ? basePath : basePath + "/";
    }
}
