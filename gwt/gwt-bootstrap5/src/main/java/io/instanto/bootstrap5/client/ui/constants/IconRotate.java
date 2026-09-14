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
 * Icon rotation.
 *
 * <p>Neither Bootstrap 5 nor Bootstrap Icons defines rotation utilities, so
 * these classes are declared by this library in
 * {@code css/gwt-bootstrap5.cache.css}. They are namespaced
 * {@code gbm-icon-*} rather than {@code bi-*} so that they cannot collide with
 * an icon name — {@code bi-border}, for one, is a real Bootstrap icon.</p>
 */
public enum IconRotate implements Style.HasCssName {

    NONE(""),
    ROTATE_90("gbm-icon-rotate-90"),
    ROTATE_180("gbm-icon-rotate-180"),
    ROTATE_270("gbm-icon-rotate-270");

    private final String cssClass;

    private IconRotate(final String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public String getCssName() {
        return cssClass;
    }

    public static IconRotate fromStyleName(final String styleName) {
        return EnumHelper.fromStyleName(styleName, IconRotate.class, NONE);
    }
}
