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
package io.instanto.bootstrap5.extras.markdown.client.ui;

import com.google.gwt.dom.client.TextAreaElement;

/**
 * The textarea selection API, which this GWT release does not expose from Java.
 *
 * <p>Its own class because the implementation is JSNI, which the TeaVM backend cannot
 * compile. That backend excludes this file and supplies the same API through
 * {@code @JSBody}, the arrangement the widget library already uses for its own seams.</p>
 */
final class TextAreaSelection {

    private TextAreaSelection() {
    }

    static native int start(TextAreaElement element) /*-{
        return element.selectionStart | 0;
    }-*/;

    static native int end(TextAreaElement element) /*-{
        return element.selectionEnd | 0;
    }-*/;

    /** Focuses the element and selects {@code length} characters from {@code start}. */
    static native void focusAndSelect(TextAreaElement element, int start, int length) /*-{
        element.focus();
        element.setSelectionRange(start, start + length);
    }-*/;
}
