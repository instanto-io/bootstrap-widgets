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

import io.instanto.bootstrap5.client.ui.base.HasBadge;
import io.instanto.bootstrap5.client.ui.base.HasIcon;
import io.instanto.bootstrap5.client.ui.base.HasIconPosition;
import io.instanto.bootstrap5.client.ui.base.mixin.IconTextMixin;
import io.instanto.bootstrap5.client.ui.constants.BadgePosition;
import io.instanto.bootstrap5.client.ui.constants.IconFlip;
import io.instanto.bootstrap5.client.ui.constants.IconPosition;
import io.instanto.bootstrap5.client.ui.constants.IconRotate;
import io.instanto.bootstrap5.client.ui.constants.IconSize;
import io.instanto.bootstrap5.client.ui.constants.IconType;
import com.google.gwt.user.client.ui.HasText;

/**
 * The text or icon segment of an {@link InputGroup}.
 *
 * <p>Carries the same icon and badge surface as the Bootstrap 3 addon, through
 * {@link IconTextMixin}. Bootstrap 5 renders it as a single
 * {@code span.input-group-text} rather than wrapping buttons separately, so the
 * mixin composes onto this widget rather than arriving through a shared
 * superclass.</p>
 */
public class InputGroupAddon extends ElementPanel implements HasText, HasIcon, HasIconPosition, HasBadge {

    private final IconTextMixin<InputGroupAddon> iconTextMixin = new IconTextMixin<InputGroupAddon>(this);

    public InputGroupAddon() {
        this("");
    }

    public InputGroupAddon(final String text) {
        super("span");
        addStyleName("input-group-text");
        iconTextMixin.addTextWidgetToParent();
        setText(text);
    }

    @Override
    public void setText(final String text) {
        iconTextMixin.setText(text);
    }

    @Override
    public String getText() {
        return iconTextMixin.getText();
    }

    @Override
    public void setIcon(final IconType iconType) {
        iconTextMixin.setIcon(iconType);
    }

    @Override
    public IconType getIcon() {
        return iconTextMixin.getIcon();
    }

    @Override
    public void setIconPosition(final IconPosition iconPosition) {
        iconTextMixin.setIconPosition(iconPosition);
    }

    @Override
    public IconPosition getIconPosition() {
        return iconTextMixin.getIconPosition();
    }

    @Override
    public void setIconSize(final IconSize iconSize) {
        iconTextMixin.setIconSize(iconSize);
    }

    @Override
    public IconSize getIconSize() {
        return iconTextMixin.getIconSize();
    }

    @Override
    public void setIconFlip(final IconFlip iconFlip) {
        iconTextMixin.setIconFlip(iconFlip);
    }

    @Override
    public IconFlip getIconFlip() {
        return iconTextMixin.getIconFlip();
    }

    @Override
    public void setIconRotate(final IconRotate iconRotate) {
        iconTextMixin.setIconRotate(iconRotate);
    }

    @Override
    public IconRotate getIconRotate() {
        return iconTextMixin.getIconRotate();
    }

    @Override
    public void setIconBordered(final boolean iconBordered) {
        iconTextMixin.setIconBordered(iconBordered);
    }

    @Override
    public boolean isIconBordered() {
        return iconTextMixin.isIconBordered();
    }

    @Override
    public void setIconInverse(final boolean iconInverse) {
        iconTextMixin.setIconInverse(iconInverse);
    }

    @Override
    public boolean isIconInverse() {
        return iconTextMixin.isIconInverse();
    }

    @Override
    public void setIconSpin(final boolean iconSpin) {
        iconTextMixin.setIconSpin(iconSpin);
    }

    @Override
    public boolean isIconSpin() {
        return iconTextMixin.isIconSpin();
    }

    @Override
    public void setIconPulse(final boolean iconPulse) {
        iconTextMixin.setIconPulse(iconPulse);
    }

    @Override
    public boolean isIconPulse() {
        return iconTextMixin.isIconPulse();
    }

    @Override
    public void setIconFixedWidth(final boolean iconFixedWidth) {
        iconTextMixin.setIconFixedWidth(iconFixedWidth);
    }

    @Override
    public boolean isIconFixedWidth() {
        return iconTextMixin.isIconFixedWidth();
    }

    @Override
    public void setIconColor(final String iconColor) {
        iconTextMixin.setIconColor(iconColor);
    }

    @Override
    public void setBadgeText(final String badgeText) {
        iconTextMixin.setBadgeText(badgeText);
    }

    @Override
    public String getBadgeText() {
        return iconTextMixin.getBadgeText();
    }

    @Override
    public void setBadgePosition(final BadgePosition badgePosition) {
        iconTextMixin.setBadgePosition(badgePosition);
    }

    @Override
    public BadgePosition getBadgePosition() {
        return iconTextMixin.getBadgePosition();
    }
}
