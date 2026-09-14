/*
 * #%L
 * GWT Bootstrap
 * %%
 * Copyright (C) 2013 - 2018 GwtBootstrap3
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
package org.gwtbootstrap3.client.ui.base;

import org.teavm.jso.JSBody;

import com.google.gwt.dom.client.Element;

/**
 * TeaVM implementation of the carousel initialiser.
 *
 * <p>Identical in package and API to the Bootstrap 3 class of the same name; it exists
 * separately only because that one declares its method as native JsInterop, which TeaVM
 * cannot compile.</p>
 */
public final class CarouselOptions {

    private CarouselOptions() {
    }

    public static void carousel(final Element e, final int interval, final String pause,
            final boolean wrap) {
        init(e == null ? null : e.unwrap(), interval, pause, wrap);
    }

    @JSBody(params = {"el", "interval", "pause", "wrap"}, script =
            "if (!el || !window.jQuery) { return; }"
            + "window.jQuery(el).carousel({ interval: interval, pause: pause, wrap: wrap });")
    private static native void init(Object el, int interval, String pause, boolean wrap);
}
