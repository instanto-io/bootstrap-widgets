/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2014 GwtBootstrap3
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
package org.gwtbootstrap3.extras.animate.client.ui;

import org.teavm.jso.JSBody;

import com.google.gwt.dom.client.Element;

/**
 * The TeaVM half of the animation seam.
 *
 * <p>The DOM does both of these itself. A one-shot listener is what jQuery's one()
 * provides, and every browser this library still supports fires the unprefixed
 * animationend, so the four prefixed names the GWT half also listens for are not
 * repeated here.</p>
 */
final class AnimateJs {

    private AnimateJs() {
    }

    static void removeOnEnd(final Element element, final String animation) {
        if (element != null) {
            removeClassOnEnd(element.unwrap(), animation);
        }
    }

    static void remove(final Element element, final String animation) {
        if (element != null) {
            removeClasses(element.unwrap(), animation);
        }
    }

    @JSBody(params = {"el", "animation"}, script =
            "el.addEventListener('animationend', function handler() {"
          + "  el.removeEventListener('animationend', handler);"
          + "  animation.split(/\\s+/).forEach(function (name) {"
          + "    if (name) { el.classList.remove(name); }"
          + "  });"
          + "});")
    private static native void removeClassOnEnd(Object el, String animation);

    @JSBody(params = {"el", "animation"}, script =
            "animation.split(/\\s+/).forEach(function (name) {"
          + "  if (name) { el.classList.remove(name); }"
          + "});")
    private static native void removeClasses(Object el, String animation);
}
