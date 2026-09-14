package io.instanto.bootstrap5.client.ui.constants;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 GwtBootstrap3
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

import io.instanto.bootstrap5.client.ui.base.helper.EnumHelper;

import com.google.gwt.dom.client.Style;

/**
 * Icon sizes, expressed with the stock Bootstrap 5 font-size utilities.
 *
 * <p>Bootstrap 3 sized icons with the Font Awesome step classes
 * ({@code fa-lg}, {@code fa-2x} … {@code fa-5x}). Bootstrap Icons ships no size
 * classes of its own — its documentation sizes icons with {@code font-size} —
 * so these constants map onto Bootstrap's own {@code fs-*} scale and need no
 * stylesheet from this library. The scale runs 1rem to 2.5rem; for anything
 * larger, set {@code font-size} directly.</p>
 */
public enum IconSize implements Size, Style.HasCssName {

    NONE(""),
    SMALL("fs-6"),
    MEDIUM("fs-5"),
    LARGE("fs-4"),
    X_LARGE("fs-3"),
    XX_LARGE("fs-2"),
    XXX_LARGE("fs-1");

    private final String cssClass;

    private IconSize(final String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public String getCssName() {
        return cssClass;
    }

    public static IconSize fromStyleName(final String styleName) {
        return EnumHelper.fromStyleName(styleName, IconSize.class, NONE);
    }
}
