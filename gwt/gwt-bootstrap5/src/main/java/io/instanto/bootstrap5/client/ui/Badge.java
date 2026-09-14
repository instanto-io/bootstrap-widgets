/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2018 GwtBootstrap3
 * %%
 * Modified from the GwtBootstrap3 original for the Bootstrap 5 track of
 * GWT Bootstrap: moved to the io.instanto.bootstrap5 namespace and
 * re-targeted at Bootstrap 5 markup, class names and JavaScript APIs. The
 * GwtBootstrap3 copyright above is retained as required by the Apache
 * License 2.0; the namespace changed, the attribution did not.
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
package io.instanto.bootstrap5.client.ui;

import io.instanto.bootstrap5.client.ui.base.HasType;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.LabelType;

public class Badge extends ElementPanel implements HasType<LabelType> {

    private Variant variant;

    public Badge() {
        super("span");
        setVariant(Variant.SECONDARY);
    }

    public Badge(String text) {
        this();
        setText(text);
    }

    public Badge(String text, Variant variant) {
        this(text);
        setVariant(variant);
    }

    public Badge(String text, LabelType type) {
        this(text);
        setType(type);
    }

    public void setVariant(Variant variant) {
        if (this.variant != null) {
            removeStyleName(styleName(this.variant));
        }
        this.variant = variant == null ? Variant.SECONDARY : variant;
        addStyleName("badge");
        addStyleName(styleName(this.variant));
    }

    public Variant getVariant() {
        return variant;
    }

    @Override
    public void setType(LabelType type) {
        StyleHelper.addUniqueEnumStyleName(this, LabelType.class, type == null ? LabelType.DEFAULT : type);
    }

    @Override
    public LabelType getType() {
        return LabelType.fromStyleName(getStyleName());
    }

    public void setPill(boolean pill) {
        setStyleName("rounded-pill", pill);
    }

    private String styleName(Variant variant) {
        return "text-bg-" + variant.cssName();
    }
}
