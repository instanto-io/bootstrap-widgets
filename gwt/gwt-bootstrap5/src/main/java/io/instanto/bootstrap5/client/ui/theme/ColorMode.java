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

/**
 * Bootstrap 5's colour modes.
 *
 * <p>Bootstrap 5.3 carries light and dark in the one stylesheet, selected by the
 * {@code data-bs-theme} attribute, so switching is an attribute change rather than the
 * stylesheet swap the Bootstrap 3 track needs. {@link #AUTO} removes the attribute and
 * lets the CSS follow the operating system.</p>
 */
public enum ColorMode {

    /** Follow the operating system's preference. */
    AUTO(null),

    LIGHT("light"),

    DARK("dark");

    private final String attributeValue;

    ColorMode(final String attributeValue) {
        this.attributeValue = attributeValue;
    }

    /** The {@code data-bs-theme} value, or null for {@link #AUTO}. */
    public String getAttributeValue() {
        return attributeValue;
    }

    /** The mode for a {@code data-bs-theme} value; {@link #AUTO} when absent. */
    public static ColorMode fromAttributeValue(final String value) {
        if (value == null || value.isEmpty()) {
            return AUTO;
        }
        for (final ColorMode mode : values()) {
            if (value.equals(mode.attributeValue)) {
                return mode;
            }
        }
        return AUTO;
    }
}
