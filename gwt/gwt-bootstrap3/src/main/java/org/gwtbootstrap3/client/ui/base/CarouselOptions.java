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
 * Initialises Bootstrap's carousel plugin with its options.
 *
 * <p>Extracted from {@code Carousel} unchanged, for the same reason as
 * {@link TooltipOptions}: the TeaVM backend cannot compile a native JsInterop
 * method, so it excludes this class and supplies the same API through
 * {@code @JSBody}.</p>
 */
public final class CarouselOptions {

    private CarouselOptions() {
    }

    @JsMethod
    public static native void carousel(Element e, int interval, String pause, boolean wrap);
}
