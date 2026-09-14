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

import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.Styles;


import com.google.gwt.user.client.ui.Widget;

public class Breadcrumbs extends ElementPanel {

    public Breadcrumbs() {
        super("ol");
        addStyleName("breadcrumb");
    }

    public Breadcrumbs(Widget... widgets) {
        this();
        for (Widget widget : widgets) {
            add(widget);
        }
    }

    @Override
    public void add(Widget child) {
        child.addStyleName("breadcrumb-item");
        super.add(child);
    }

    /**
     * Bootstrap 5 keeps .list-inline, and marks each child .list-inline-item.
     */
    public void setInline(final boolean inline) {
        setStyleName(Styles.LIST_INLINE, inline);
        for (final Widget child : getChildren()) {
            child.setStyleName(Styles.LIST_INLINE_ITEM, inline);
        }
    }

    public boolean isInline() {
        return StyleHelper.containsStyle(getStyleName(), Styles.LIST_INLINE);
    }

    public void setUnstyled(final boolean unstyled) {
        setStyleName(Styles.LIST_UNSTYLED, unstyled);
    }

    public boolean isUnstyled() {
        return StyleHelper.containsStyle(getStyleName(), Styles.LIST_UNSTYLED);
    }

}
