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

/**
 * A Bootstrap stylesheet that can be swapped at runtime.
 *
 * <p>A theme is a complete replacement for {@code bootstrap.css}, not an overlay, so
 * switching means pointing one stylesheet link at a different URL. {@link #isDark()}
 * lets an application react to the change -- picking a matching syntax highlighter or
 * chart palette, say -- without hard-coding theme names.</p>
 */
public class Theme {

    private final String name;
    private final String displayName;
    private final String url;
    private final String overlayUrl;
    private final boolean dark;

    public Theme(final String name, final String displayName, final String url,
            final boolean dark) {
        this(name, displayName, url, null, dark);
    }

    /**
     * @param overlayUrl a second stylesheet layered over {@code url}, or null. Stock
     *                   Bootstrap needs this: its optional theme is an overlay on
     *                   bootstrap.css rather than a replacement for it.
     */
    public Theme(final String name, final String displayName, final String url,
            final String overlayUrl, final boolean dark) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name must not be empty");
        }
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("url must not be empty");
        }
        this.name = name;
        this.displayName = displayName == null ? name : displayName;
        this.url = url;
        this.overlayUrl = overlayUrl;
        this.dark = dark;
    }

    /** The stable identifier, used when the choice is stored and restored. */
    public String getName() {
        return name;
    }

    /** The label to show a user. */
    public String getDisplayName() {
        return displayName;
    }

    public String getUrl() {
        return url;
    }

    /** A stylesheet layered over {@link #getUrl()}, or null when there is none. */
    public String getOverlayUrl() {
        return overlayUrl;
    }

    /** True when this theme has a dark background. */
    public boolean isDark() {
        return dark;
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof Theme && name.equals(((Theme) other).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
