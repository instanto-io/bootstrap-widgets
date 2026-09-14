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

import com.google.gwt.dom.client.Element;

/**
 * The two calls the animation helper makes into the browser.
 *
 * <p>A seam. Both are reached through jQuery here, because that is what the Bootstrap 3
 * extras already require; the TeaVM half uses the DOM directly, since jQuery is a page
 * concern there and neither call needs it.</p>
 */
final class AnimateJs {

    private AnimateJs() {
    }

    /** Removes the animation class once the animation finishes, once only. */
    static native void removeOnEnd(Element element, String animation) /*-{
        var elem = $wnd.jQuery(element);
        elem.one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend',
                { elem: elem }, function (event) {
            event.data.elem.removeClass(animation);
        });
    }-*/;

    /** Removes the animation class now, stopping the animation. */
    static native void remove(Element element, String animation) /*-{
        $wnd.jQuery(element).removeClass(animation);
    }-*/;
}
