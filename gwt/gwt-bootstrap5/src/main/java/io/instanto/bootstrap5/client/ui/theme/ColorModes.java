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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.storage.client.Storage;
import java.util.ArrayList;
import java.util.List;

/**
 * Applies Bootstrap 5's colour mode, and remembers the choice.
 *
 * <p>The counterpart to the Bootstrap 3 track's {@code Themes}: same shape, different
 * mechanism. Bootstrap 5.3 ships light and dark in one stylesheet, so this sets
 * {@code data-bs-theme} on the document element rather than swapping a stylesheet, and
 * needs no extra assets.</p>
 *
 * <p>The choice is remembered in {@code localStorage} where the browser allows it. A
 * browser that refuses storage keeps working, it just does not remember.</p>
 *
 * <pre>{@code
 * ColorModes.restore(ColorMode.AUTO);
 * ColorModes.apply(ColorMode.DARK);
 * }</pre>
 */
public final class ColorModes {

    /** The attribute Bootstrap 5 reads. */
    public static final String ATTRIBUTE = "data-bs-theme";

    private static final String STORAGE_KEY = "gwtbootstrap5.colorMode";
    private static final List<ColorModeChangeHandler> HANDLERS = new ArrayList<>();

    private ColorModes() {
    }

    /** The mode currently set on the document. */
    public static ColorMode getCurrent() {
        return ColorMode.fromAttributeValue(root().getAttribute(ATTRIBUTE));
    }

    /** True when the document is explicitly in dark mode. */
    public static boolean isDark() {
        return getCurrent() == ColorMode.DARK;
    }

    /** Applies a mode and remembers it. */
    public static void apply(final ColorMode mode) {
        final ColorMode effective = mode == null ? ColorMode.AUTO : mode;
        if (effective == ColorMode.AUTO) {
            root().removeAttribute(ATTRIBUTE);
        } else {
            root().setAttribute(ATTRIBUTE, effective.getAttributeValue());
        }
        store(effective.name());
        for (final ColorModeChangeHandler handler : new ArrayList<>(HANDLERS)) {
            handler.onColorModeChanged(effective);
        }
    }

    /** Switches between light and dark, treating AUTO as light. */
    public static void toggle() {
        apply(isDark() ? ColorMode.LIGHT : ColorMode.DARK);
    }

    /** Applies the remembered mode, or {@code fallback} when there is none. */
    public static ColorMode restore(final ColorMode fallback) {
        final String remembered = read();
        ColorMode mode = fallback;
        if (remembered != null) {
            try {
                mode = ColorMode.valueOf(remembered);
            } catch (final IllegalArgumentException stale) {
                mode = fallback;
            }
        }
        apply(mode);
        return mode;
    }

    public static void addColorModeChangeHandler(final ColorModeChangeHandler handler) {
        if (handler != null) {
            HANDLERS.add(handler);
        }
    }

    public static void removeColorModeChangeHandler(final ColorModeChangeHandler handler) {
        HANDLERS.remove(handler);
    }

    /** Bootstrap reads the attribute from the html element. */
    private static Element root() {
        return Document.get().getDocumentElement();
    }

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
