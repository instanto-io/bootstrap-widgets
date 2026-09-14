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

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import io.instanto.bootstrap5.client.ui.base.HasEmphasis;
import io.instanto.bootstrap5.client.ui.base.HasSize;
import io.instanto.bootstrap5.client.ui.base.HasType;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.Emphasis;
import io.instanto.bootstrap5.client.ui.constants.IconFlip;
import io.instanto.bootstrap5.client.ui.constants.IconRotate;
import io.instanto.bootstrap5.client.ui.constants.IconSize;
import io.instanto.bootstrap5.client.ui.constants.IconType;
import io.instanto.bootstrap5.client.ui.constants.Styles;

public class Icon extends ElementPanel implements HasType<IconType>, HasSize<IconSize>, HasEmphasis, HasClickHandlers {

    private String iconName = "";

    public Icon() {
        this("");
    }

    public Icon(String iconName) {
        super("i");
        addStyleName(Styles.ICON);
        setIconName(iconName);
    }

    @UiConstructor
    public Icon(IconType type) {
        super("i");
        addStyleName(Styles.ICON);
        setType(type);
    }

    /**
     * Names the icon, with or without the {@code bi-} prefix. This and
     * {@link #setType(IconType)} are the same setting; whichever is called last
     * wins, and the previous icon class is removed.
     */
    public void setIconName(String iconName) {
        String next = normalize(iconName);
        if (this.iconName.equals(next)) {
            return;
        }
        if (!this.iconName.isEmpty()) {
            removeStyleName(Styles.ICON_PREFIX + this.iconName);
        }
        this.iconName = next;
        if (!this.iconName.isEmpty()) {
            addStyleName(Styles.ICON_PREFIX + this.iconName);
        }
    }

    /** The icon name without the {@code bi-} prefix, or "" when none is set. */
    public String getIconName() {
        return iconName;
    }

    private String normalize(String iconName) {
        if (iconName == null) {
            return "";
        }
        String trimmed = iconName.trim();
        return trimmed.startsWith(Styles.ICON_PREFIX) ? trimmed.substring(Styles.ICON_PREFIX.length()) : trimmed;
    }

    @Override
    public void setType(IconType type) {
        setIconName(type == null ? "" : type.getIconName());
    }

    @Override
    public IconType getType() {
        return iconName.isEmpty() ? null : IconType.fromStyleName(Styles.ICON_PREFIX + iconName);
    }

    /**
     * Marks this icon as the larger background icon of an {@link IconStack}
     * when true, or the smaller foreground icon when false.
     */
    public void setStackBase(boolean base) {
        StyleHelper.toggleStyleName(this, base, Styles.ICON_STACK_BASE);
        StyleHelper.toggleStyleName(this, !base, Styles.ICON_STACK_TOP);
    }

    public boolean isStackBase() {
        return StyleHelper.containsStyle(getStyleName(), Styles.ICON_STACK_BASE);
    }

    public void setBorder(boolean border) {
        StyleHelper.toggleStyleName(this, border, Styles.ICON_BORDER);
    }

    public boolean isBorder() {
        return StyleHelper.containsStyle(getStyleName(), Styles.ICON_BORDER);
    }

    public void setFixedWidth(boolean fixedWidth) {
        StyleHelper.toggleStyleName(this, fixedWidth, Styles.ICON_FIXED_WIDTH);
    }

    public boolean isFixedWidth() {
        return StyleHelper.containsStyle(getStyleName(), Styles.ICON_FIXED_WIDTH);
    }

    public void setInverse(boolean inverse) {
        StyleHelper.toggleStyleName(this, inverse, Styles.ICON_INVERSE);
    }

    public boolean isInverse() {
        return StyleHelper.containsStyle(getStyleName(), Styles.ICON_INVERSE);
    }

    public void setSpin(boolean spin) {
        StyleHelper.toggleStyleName(this, spin, Styles.ICON_SPIN);
    }

    public boolean isSpin() {
        return StyleHelper.containsStyle(getStyleName(), Styles.ICON_SPIN);
    }

    public void setPulse(boolean pulse) {
        StyleHelper.toggleStyleName(this, pulse, Styles.ICON_PULSE);
    }

    public boolean isPulse() {
        return StyleHelper.containsStyle(getStyleName(), Styles.ICON_PULSE);
    }

    public void setRotate(IconRotate iconRotate) {
        StyleHelper.addUniqueEnumStyleName(this, IconRotate.class, iconRotate == null ? IconRotate.NONE : iconRotate);
    }

    public IconRotate getRotate() {
        return IconRotate.fromStyleName(getStyleName());
    }

    public void setFlip(IconFlip iconFlip) {
        StyleHelper.addUniqueEnumStyleName(this, IconFlip.class, iconFlip == null ? IconFlip.NONE : iconFlip);
    }

    public IconFlip getFlip() {
        return IconFlip.fromStyleName(getStyleName());
    }

    @Override
    public void setSize(IconSize iconSize) {
        StyleHelper.addUniqueEnumStyleName(this, IconSize.class, iconSize == null ? IconSize.NONE : iconSize);
    }

    @Override
    public IconSize getSize() {
        return IconSize.fromStyleName(getStyleName());
    }

    @Override
    public void setEmphasis(Emphasis emphasis) {
        StyleHelper.addUniqueEnumStyleName(this, Emphasis.class, emphasis);
    }

    @Override
    public Emphasis getEmphasis() {
        return Emphasis.fromStyleName(getStyleName());
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    /** The smaller foreground icon of an {@link IconStack}. */
    public void setStackTop(boolean top) {
        setStackBase(!top);
    }

    public boolean isStackTop() {
        return StyleHelper.containsStyle(getStyleName(), Styles.ICON_STACK_TOP);
    }

}
