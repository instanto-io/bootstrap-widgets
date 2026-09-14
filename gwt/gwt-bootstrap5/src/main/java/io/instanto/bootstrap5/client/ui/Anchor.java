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

import java.util.List;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.base.HasBadge;
import io.instanto.bootstrap5.client.ui.base.HasDataTarget;
import io.instanto.bootstrap5.client.ui.base.HasDataToggle;
import io.instanto.bootstrap5.client.ui.base.HasHref;
import io.instanto.bootstrap5.client.ui.base.HasIcon;
import io.instanto.bootstrap5.client.ui.base.HasIconPosition;
import io.instanto.bootstrap5.client.ui.base.HasPull;
import io.instanto.bootstrap5.client.ui.base.HasTarget;
import io.instanto.bootstrap5.client.ui.base.HasTargetHistoryToken;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.base.mixin.DataTargetMixin;
import io.instanto.bootstrap5.client.ui.base.mixin.DataToggleMixin;
import io.instanto.bootstrap5.client.ui.base.mixin.FocusableMixin;
import io.instanto.bootstrap5.client.ui.base.mixin.IconTextMixin;
import io.instanto.bootstrap5.client.ui.base.mixin.PullMixin;
import io.instanto.bootstrap5.client.ui.constants.BadgePosition;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;
import io.instanto.bootstrap5.client.ui.constants.IconFlip;
import io.instanto.bootstrap5.client.ui.constants.IconPosition;
import io.instanto.bootstrap5.client.ui.constants.IconRotate;
import io.instanto.bootstrap5.client.ui.constants.IconSize;
import io.instanto.bootstrap5.client.ui.constants.IconType;
import io.instanto.bootstrap5.client.ui.constants.Pull;
import io.instanto.bootstrap5.client.ui.constants.Toggle;

public class Anchor extends ElementPanel implements HasEnabled, HasHref, HasDataTarget, HasDataToggle,
        HasIcon, HasIconPosition, HasBadge, HasTarget, HasTargetHistoryToken, HasPull, Focusable {

    private String targetHistoryToken;
    private Variant buttonVariant;
    private boolean outline;
    private final DataTargetMixin<Anchor> targetMixin = new DataTargetMixin<Anchor>(this);
    private final DataToggleMixin<Anchor> toggleMixin = new DataToggleMixin<Anchor>(this);
    private final FocusableMixin<Anchor> focusableMixin = new FocusableMixin<Anchor>(this);
    private final IconTextMixin<Anchor> iconTextMixin = new IconTextMixin<Anchor>(this);
    private final PullMixin<Anchor> pullMixin = new PullMixin<Anchor>(this);

    public Anchor() {
        this("#");
    }

    public Anchor(String href) {
        super("a");
        setHref(href);
        iconTextMixin.addTextWidgetToParent();
    }

    public Anchor(String text, String href) {
        this(href);
        setText(text);
    }

    @Override
    public void setHref(String href) {
        AnchorElement.as(getElement()).setHref(href == null ? "#" : href);
    }

    @Override
    public String getHref() {
        return AnchorElement.as(getElement()).getHref();
    }

    @Override
    public void setDataTargetWidgets(List<Widget> widgets) {
        targetMixin.setDataTargetWidgets(widgets);
    }

    @Override
    public void setDataTargetWidget(Widget widget) {
        targetMixin.setDataTargetWidget(widget);
    }

    @Override
    public void setDataTarget(String dataTarget) {
        targetMixin.setDataTarget(dataTarget);
    }

    @Override
    public String getDataTarget() {
        return targetMixin.getDataTarget();
    }

    @Override
    public void setTarget(String target) {
        getElement().setAttribute("target", target == null ? "" : target);
    }

    @Override
    public String getTarget() {
        return getElement().getAttribute("target");
    }

    @Override
    public void setTargetHistoryToken(String targetHistoryToken) {
        this.targetHistoryToken = targetHistoryToken;
        setHref(targetHistoryToken == null ? "#" : "#" + History.encodeHistoryToken(targetHistoryToken));
    }

    @Override
    public String getTargetHistoryToken() {
        return targetHistoryToken;
    }

    @Override
    public void setText(String text) {
        iconTextMixin.setText(text);
    }

    @Override
    public String getText() {
        return iconTextMixin.getText();
    }

    @Override
    public void setIcon(IconType iconType) {
        iconTextMixin.setIcon(iconType);
    }

    @Override
    public IconType getIcon() {
        return iconTextMixin.getIcon();
    }

    @Override
    public void setIconPosition(IconPosition iconPosition) {
        iconTextMixin.setIconPosition(iconPosition);
    }

    @Override
    public IconPosition getIconPosition() {
        return iconTextMixin.getIconPosition();
    }

    @Override
    public void setIconSize(IconSize iconSize) {
        iconTextMixin.setIconSize(iconSize);
    }

    @Override
    public IconSize getIconSize() {
        return iconTextMixin.getIconSize();
    }

    @Override
    public void setIconFlip(IconFlip iconFlip) {
        iconTextMixin.setIconFlip(iconFlip);
    }

    @Override
    public IconFlip getIconFlip() {
        return iconTextMixin.getIconFlip();
    }

    @Override
    public void setIconRotate(IconRotate iconRotate) {
        iconTextMixin.setIconRotate(iconRotate);
    }

    @Override
    public IconRotate getIconRotate() {
        return iconTextMixin.getIconRotate();
    }

    @Override
    public void setIconBordered(boolean iconBordered) {
        iconTextMixin.setIconBordered(iconBordered);
    }

    @Override
    public boolean isIconBordered() {
        return iconTextMixin.isIconBordered();
    }

    @Override
    public void setIconInverse(boolean iconInverse) {
        iconTextMixin.setIconInverse(iconInverse);
    }

    @Override
    public boolean isIconInverse() {
        return iconTextMixin.isIconInverse();
    }

    @Override
    public void setIconSpin(boolean iconSpin) {
        iconTextMixin.setIconSpin(iconSpin);
    }

    @Override
    public boolean isIconSpin() {
        return iconTextMixin.isIconSpin();
    }

    @Override
    public void setIconPulse(boolean iconPulse) {
        iconTextMixin.setIconPulse(iconPulse);
    }

    @Override
    public boolean isIconPulse() {
        return iconTextMixin.isIconPulse();
    }

    @Override
    public void setIconFixedWidth(boolean iconFixedWidth) {
        iconTextMixin.setIconFixedWidth(iconFixedWidth);
    }

    @Override
    public boolean isIconFixedWidth() {
        return iconTextMixin.isIconFixedWidth();
    }

    @Override
    public void setIconColor(String iconColor) {
        iconTextMixin.setIconColor(iconColor);
    }

    @Override
    public void setBadgeText(String badgeText) {
        iconTextMixin.setBadgeText(badgeText);
    }

    @Override
    public String getBadgeText() {
        return iconTextMixin.getBadgeText();
    }

    @Override
    public void setBadgePosition(BadgePosition badgePosition) {
        iconTextMixin.setBadgePosition(badgePosition);
    }

    @Override
    public BadgePosition getBadgePosition() {
        return iconTextMixin.getBadgePosition();
    }

    @Override
    public void setDataToggle(Toggle toggle) {
        toggleMixin.setDataToggle(toggle);
        setStyleName("dropdown-toggle", toggle == Toggle.DROPDOWN);
    }

    public void setDataToggle(String toggle) {
        if (toggle == null || toggle.isEmpty()) {
            setDataToggle((Toggle) null);
            return;
        }
        getElement().setAttribute("data-bs-toggle", toggle);
        setStyleName("dropdown-toggle", "dropdown".equals(toggle));
    }

    @Override
    public Toggle getDataToggle() {
        return toggleMixin.getDataToggle();
    }

    @Override
    public int getTabIndex() {
        return focusableMixin.getTabIndex();
    }

    @Override
    public void setTabIndex(int index) {
        focusableMixin.setTabIndex(index);
    }

    @Override
    public void setAccessKey(char key) {
        focusableMixin.setAccessKey(key);
    }

    @Override
    public void setFocus(boolean focused) {
        focusableMixin.setFocus(focused);
    }

    @Override
    public void setEnabled(boolean enabled) {
        setStyleName("disabled", !enabled);
        getElement().setAttribute("aria-disabled", enabled ? "false" : "true");
        if (!enabled) {
            getElement().setAttribute("tabindex", "-1");
        } else {
            getElement().removeAttribute("tabindex");
        }
    }

    @Override
    public boolean isEnabled() {
        return !"true".equals(getElement().getAttribute("aria-disabled"));
    }

    @Override
    public void setPull(Pull pull) {
        pullMixin.setPull(pull);
    }

    @Override
    public Pull getPull() {
        return pullMixin.getPull();
    }

    public void setButtonVariant(Variant variant) {
        if (buttonVariant != null) {
            removeStyleName(buttonStyle(buttonVariant, outline));
        }
        buttonVariant = variant == null ? Variant.PRIMARY : variant;
        addStyleName("btn");
        addStyleName(buttonStyle(buttonVariant, outline));
    }

    public void setButtonType(ButtonType type) {
        addStyleName("btn");
        StyleHelper.addUniqueEnumStyleName(this, ButtonType.class, type == null ? ButtonType.DEFAULT : type);
    }

    public void setOutline(boolean outline) {
        if (this.outline != outline) {
            if (buttonVariant != null) {
                removeStyleName(buttonStyle(buttonVariant, this.outline));
            }
            this.outline = outline;
            if (buttonVariant != null) {
                addStyleName(buttonStyle(buttonVariant, this.outline));
            }
        }
    }

    private String buttonStyle(Variant variant, boolean outline) {
        return outline ? "btn-outline-" + variant.cssName() : "btn-" + variant.cssName();
    }
}
