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
import io.instanto.bootstrap5.client.ui.constants.ListGroupItemType;

public class ListGroupItem extends ElementPanel implements HasType<ListGroupItemType> {

    private Variant variant;

    public ListGroupItem() {
        super("li");
        addStyleName("list-group-item");
    }

    public ListGroupItem(String text) {
        this();
        setText(text);
    }

    public ListGroupItem(String text, ListGroupItemType type) {
        this(text);
        setType(type);
    }

    public void setVariant(Variant variant) {
        if (this.variant != null) {
            removeStyleName(styleName(this.variant));
        }
        this.variant = variant;
        if (variant != null) {
            addStyleName(styleName(variant));
        }
    }

    public Variant getVariant() {
        return variant;
    }

    @Override
    public void setType(ListGroupItemType type) {
        StyleHelper.addUniqueEnumStyleName(this, ListGroupItemType.class, type == null ? ListGroupItemType.DEFAULT : type);
    }

    @Override
    public ListGroupItemType getType() {
        return ListGroupItemType.fromStyleName(getStyleName());
    }

    public void setActive(boolean active) {
        setStyleName("active", active);
        getElement().setAttribute("aria-current", active ? "true" : "false");
    }

    public boolean isActive() {
        return getStyleName().contains("active");
    }

    public void setDisabled(boolean disabled) {
        setStyleName("disabled", disabled);
        getElement().setAttribute("aria-disabled", disabled ? "true" : "false");
    }

    public void setEnabled(boolean enabled) {
        setDisabled(!enabled);
    }

    public boolean isEnabled() {
        return !"true".equals(getElement().getAttribute("aria-disabled"));
    }

    private String styleName(Variant variant) {
        return "list-group-item-" + variant.cssName();
    }
}
