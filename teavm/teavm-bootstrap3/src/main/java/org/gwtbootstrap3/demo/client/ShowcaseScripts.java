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
package org.gwtbootstrap3.demo.client;

import org.teavm.jso.JSBody;

/**
 * The TeaVM half of the showcase-scripts seam.
 *
 * <p>prettify.js is served next to the stylesheets and fetched by URL, rather than
 * compiled into the output by a ClientBundle as it is under GWT. That difference is
 * visible: the script is still loading while the first page is built, so the widgets
 * that ask to be highlighted ask before anything can answer. The handler is therefore
 * attached before the element is inserted -- afterwards is a race the script can
 * already have won -- and highlights whatever is on the page once it arrives. Pages
 * built later find the function and highlight themselves.</p>
 */
final class ShowcaseScripts {

    private ShowcaseScripts() {
    }

    /** Loads the same showcase styles and syntax highlighter declared for GWT. */
    static void inject() {
        // This also restores demo.css: its page spacing is separate from Bootstrap.
        ResourcesResources.ensureInjected();
        ResourcesResources.whenReady(ShowcaseScripts::isReady, ShowcaseScripts::highlight);
    }

    @JSBody(script = "return typeof window.prettyPrint === 'function';")
    private static native boolean isReady();

    @JSBody(script = "if (typeof window.prettyPrint === 'function') window.prettyPrint();")
    private static native void highlight();
}
