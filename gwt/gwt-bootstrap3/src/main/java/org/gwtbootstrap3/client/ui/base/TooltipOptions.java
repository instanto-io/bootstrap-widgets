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

import com.google.gwt.dom.client.Element;

import jsinterop.annotations.JsMethod;

/**
 * Writes back a tooltip or popover option after the plugin has been initialised.
 *
 * <p>Extracted from {@link AbstractTooltip} unchanged, purely so the four native methods
 * sit in one file: the TeaVM backend cannot compile them and excludes this class,
 * supplying the same API through {@code @JSBody}, the arrangement {@code JQuery} already
 * uses. {@code dataTarget} is the jQuery data key the plugin stores its options under,
 * {@code bs.tooltip} or {@code bs.popover}.</p>
 */
final class TooltipOptions {

    private TooltipOptions() {
    }

    @JsMethod
    static native void updateBool(String dataTarget, Element e, String option, boolean value);

    @JsMethod
    static native void updateDelay(String dataTarget, Element e, int showDelay, int hideDelay);

    @JsMethod
    static native void updateString(String dataTarget, Element e, String option, String value);

    @JsMethod
    static native void updateViewport(String dataTarget, Element e, String selector, int padding);
}
