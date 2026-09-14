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
package io.instanto.bootstrap5.client.ui.theme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;

/**
 * The stylesheet this module ships, offered as a {@link Theme} so an
 * application can always get back to stock Bootstrap.
 *
 * <p>Bootstrap 3 had two of these -- the base stylesheet and the optional
 * bootstrap-theme overlay. Bootstrap 5 dropped the overlay, so there is one.</p>
 */
public final class StandardThemes {

    public static final String BOOTSTRAP = "bootstrap";

    private StandardThemes() {
    }

    /** Stock Bootstrap, served from this module's own resources. */
    public static List<Theme> all() {
        return all(GWT.getModuleBaseURL() + "css/");
    }

    public static List<Theme> all(final String basePath) {
        return new ArrayList<Theme>(Arrays.asList(bootstrap(basePath)));
    }

    public static Theme bootstrap(final String basePath) {
        return new Theme(BOOTSTRAP, "Bootstrap", base(basePath), null, false);
    }

    private static String base(final String basePath) {
        return normalise(basePath) + "bootstrap-5.3.8.min.cache.css";
    }

    private static String normalise(final String basePath) {
        if (basePath == null || basePath.isEmpty()) {
            return "";
        }
        return basePath.endsWith("/") ? basePath : basePath + "/";
    }
}
