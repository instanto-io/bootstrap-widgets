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

import io.instanto.bootstrap5.client.ui.base.HasResponsiveness;
import io.instanto.bootstrap5.client.ui.base.HasSize;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.DeviceSize;
import io.instanto.bootstrap5.client.ui.constants.IconSize;
import io.instanto.bootstrap5.client.ui.constants.Styles;

import com.google.gwt.user.client.ui.Widget;

/**
 * Two icons drawn on top of one another.
 *
 * <p>Font Awesome provided {@code fa-stack} for this; Bootstrap Icons does not,
 * so the {@code gbm-icon-stack} classes are declared by this library in
 * {@code css/gwt-bootstrap5.cache.css} using position utilities.</p>
 */
public class IconStack extends ElementPanel implements HasSize<IconSize>, HasResponsiveness {

    public IconStack() {
        super("span");
        setStyleName(Styles.ICON_STACK);
    }

    /**
     * Adds {@code icon} to the stack, as the larger background icon when
     * {@code base} is true and as the smaller foreground icon otherwise.
     */
    public void add(final Icon icon, final boolean base) {
        icon.setStackBase(base);
        add(icon);
    }

    @Override
    public void add(final Widget child) {
        if (!(child instanceof Icon)) {
            throw new IllegalArgumentException("An IconStack can only have children that are of type Icon.");
        }
        super.add(child);
    }

    @Override
    public void setVisibleOn(final DeviceSize deviceSize) {
        StyleHelper.setVisibleOn(this, deviceSize);
    }

    @Override
    public void setHiddenOn(final DeviceSize deviceSize) {
        StyleHelper.setHiddenOn(this, deviceSize);
    }

    @Override
    public void setSize(final IconSize size) {
        StyleHelper.addUniqueEnumStyleName(this, IconSize.class, size == null ? IconSize.NONE : size);
    }

    @Override
    public IconSize getSize() {
        return IconSize.fromStyleName(getStyleName());
    }
}
