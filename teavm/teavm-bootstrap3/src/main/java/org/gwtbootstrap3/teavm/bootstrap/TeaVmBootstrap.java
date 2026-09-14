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
package org.gwtbootstrap3.teavm.bootstrap;

import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;

/** Thin TeaVM calls into Bootstrap 3's jQuery plugin API. */
public final class TeaVmBootstrap {

    private TeaVmBootstrap() {
    }

    @JSBody(params = {"element"}, script = "if (window.jQuery) { window.jQuery(element).modal('show'); }")
    public static native void showModal(HTMLElement element);

    @JSBody(params = {"element"}, script = "if (window.jQuery) { window.jQuery(element).modal('hide'); }")
    public static native void hideModal(HTMLElement element);
}
