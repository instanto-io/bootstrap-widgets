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
 * @author Joshua Godi
 */
public enum Responsiveness implements Type, Style.HasCssName {
    DEFAULT(""),
    VISIBLE_PRINT("d-print-block"),
    VISIBLE_XS("d-block d-sm-none"),
    VISIBLE_SM("d-none d-sm-block d-md-none"),
    VISIBLE_MD("d-none d-md-block d-lg-none"),
    VISIBLE_LG("d-none d-lg-block"),
    HIDDEN_PRINT("d-print-none"),
    HIDDEN_XS("d-none d-sm-block"),
    HIDDEN_SM("d-sm-none"),
    HIDDEN_MD("d-md-none"),
    HIDDEN_LG("d-lg-none");

    private final String cssClass;

    private Responsiveness(final String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public String getCssName() {
        return cssClass;
    }

    public static Responsiveness fromStyleName(final String styleName) {
        return EnumHelper.fromStyleName(styleName, Responsiveness.class, DEFAULT);
    }
}
