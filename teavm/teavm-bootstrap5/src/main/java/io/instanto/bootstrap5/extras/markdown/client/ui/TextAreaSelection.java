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

import org.teavm.jso.JSBody;

import com.google.gwt.dom.client.TextAreaElement;

/**
 * TeaVM implementation of the textarea selection seam.
 *
 * <p>Identical in package and API to the Bootstrap 5 class of the same name; it exists
 * separately only because that one is JSNI, which TeaVM cannot compile.</p>
 */
final class TextAreaSelection {

    private TextAreaSelection() {
    }

    static int start(final TextAreaElement element) {
        return element == null ? 0 : selectionStart(element.unwrap());
    }

    static int end(final TextAreaElement element) {
        return element == null ? 0 : selectionEnd(element.unwrap());
    }

    static void focusAndSelect(final TextAreaElement element, final int start, final int length) {
        if (element != null) {
            focusRange(element.unwrap(), start, start + length);
        }
    }

    @JSBody(params = {"el"}, script = "return el.selectionStart | 0;")
    private static native int selectionStart(Object el);

    @JSBody(params = {"el"}, script = "return el.selectionEnd | 0;")
    private static native int selectionEnd(Object el);

    @JSBody(params = {"el", "from", "to"}, script = "el.focus(); el.setSelectionRange(from, to);")
    private static native void focusRange(Object el, int from, int to);
}
