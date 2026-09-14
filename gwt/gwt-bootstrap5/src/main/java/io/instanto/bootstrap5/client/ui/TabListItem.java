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
import io.instanto.bootstrap5.client.ui.constants.BadgePosition;
import io.instanto.bootstrap5.client.ui.constants.IconFlip;
import io.instanto.bootstrap5.client.ui.constants.IconPosition;
import io.instanto.bootstrap5.client.ui.constants.IconRotate;
import io.instanto.bootstrap5.client.ui.constants.IconSize;
import io.instanto.bootstrap5.client.ui.constants.IconType;
import com.google.gwt.user.client.ui.Focusable;


import io.instanto.bootstrap5.client.ui.base.BootstrapComponent;

import java.util.List;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import io.instanto.bootstrap5.client.shared.event.TabShowEvent;
import io.instanto.bootstrap5.client.shared.event.TabShowHandler;
import io.instanto.bootstrap5.client.shared.event.TabShownEvent;
import io.instanto.bootstrap5.client.shared.event.TabShownHandler;
import io.instanto.bootstrap5.client.ui.base.BootstrapEventBridge;
import io.instanto.bootstrap5.client.ui.base.BootstrapEventHandler;
import io.instanto.bootstrap5.client.ui.base.HasActive;
import io.instanto.bootstrap5.client.ui.base.HasDataTarget;
import io.instanto.bootstrap5.client.ui.base.HasHref;
import io.instanto.bootstrap5.client.ui.base.mixin.DataTargetMixin;
import io.instanto.bootstrap5.client.ui.constants.Toggle;

public class TabListItem extends ElementPanel implements HasActive, HasEnabled, HasHref, HasDataTarget,
        HasIcon, HasIconPosition, HasBadge, Focusable {

    private final Anchor anchor = new Anchor();
    private final DataTargetMixin<Anchor> targetMixin = new DataTargetMixin<Anchor>(anchor);

    public TabListItem() {
        super("li");
        addStyleName("nav-item");
        anchor.addStyleName("nav-link");
        anchor.getElement().setAttribute("data-bs-toggle", "tab");
        anchor.getElement().setAttribute("role", "tab");
        super.add(anchor);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        BootstrapEventBridge.bind(anchor.getElement(), "show.bs.tab", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                fireEvent(new TabShowEvent(TabListItem.this, Event.as(event)));
            }
        });
        BootstrapEventBridge.bind(anchor.getElement(), "shown.bs.tab", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                fireEvent(new TabShownEvent(TabListItem.this, Event.as(event)));
            }
        });
    }

    @Override
    protected void onUnload() {
        BootstrapEventBridge.unbindAll(anchor.getElement());
        BootstrapComponent.dispose(anchor.getElement(), "Tab");
        super.onUnload();
    }

    public TabListItem(String text, String targetId) {
        this();
        setText(text);
        setTarget(targetId);
    }

    @Override
    public void setText(String text) {
        anchor.setText(text == null ? "" : text);
    }

    @Override
    public String getText() {
        return anchor.getText();
    }

    public void setTarget(String targetId) {
        String id = targetId == null ? "" : targetId;
        setDataTarget("#" + id);
    }

    @Override
    public void setHref(String href) {
        setDataTarget(href == null ? "#" : href);
    }

    @Override
    public String getHref() {
        return getDataTarget();
    }

    @Override
    public void setDataTargetWidgets(List<Widget> widgets) {
        targetMixin.setDataTargetWidgets(widgets);
        anchor.setHref(targetMixin.getDataTarget());
    }

    @Override
    public void setDataTargetWidget(Widget widget) {
        targetMixin.setDataTargetWidget(widget);
        anchor.setHref(targetMixin.getDataTarget());
    }

    @Override
    public void setDataTarget(String dataTarget) {
        targetMixin.setDataTarget(dataTarget);
        anchor.setHref(dataTarget == null ? "#" : dataTarget);
    }

    @Override
    public String getDataTarget() {
        return targetMixin.getDataTarget();
    }

    @Override
    public void add(Widget child) {
        anchor.add(child);
    }

    @Override
    public void setActive(boolean active) {
        anchor.setStyleName("active", active);
        anchor.getElement().setAttribute("aria-selected", active ? "true" : "false");
    }

    @Override
    public boolean isActive() {
        return anchor.getStyleName().contains("active");
    }

    @Override
    public void setEnabled(boolean enabled) {
        anchor.setEnabled(enabled);
        anchor.setStyleName("disabled", !enabled);
        anchor.getElement().setAttribute("aria-disabled", enabled ? "false" : "true");
        anchor.setDataToggle(enabled ? Toggle.TAB : null);
    }

    @Override
    public boolean isEnabled() {
        return anchor.isEnabled();
    }

    public void showTab() {
        BootstrapComponent.call(anchor.getElement(), "Tab", "show");
    }

    public void showTab(boolean fireEvents) {
        showTab();
    }

    public HandlerRegistration addShowHandler(TabShowHandler handler) {
        return addHandler(handler, TabShowEvent.getType());
    }

    public HandlerRegistration addShownHandler(TabShownHandler handler) {
        return addHandler(handler, TabShownEvent.getType());
    }

    public String getHTML() {
        return anchor.getHTML();
    }

    public void setHTML(String html) {
        anchor.setHTML(html);
    }



    // ---- icon, badge, focus -------------------------------------------------
    // Bootstrap 3 got these by extending AnchorListItem. The Bootstrap 5 item
    // owns its anchor outright -- it carries data-bs-toggle="tab" and the event
    // bridging -- so the same surface is delegated to that anchor instead.

    @Override
    public void setIcon(final IconType iconType) {
        anchor.setIcon(iconType);
    }

    @Override
    public IconType getIcon() {
        return anchor.getIcon();
    }

    @Override
    public void setIconPosition(final IconPosition iconPosition) {
        anchor.setIconPosition(iconPosition);
    }

    @Override
    public IconPosition getIconPosition() {
        return anchor.getIconPosition();
    }

    @Override
    public void setIconSize(final IconSize iconSize) {
        anchor.setIconSize(iconSize);
    }

    @Override
    public IconSize getIconSize() {
        return anchor.getIconSize();
    }

    @Override
    public void setIconFlip(final IconFlip iconFlip) {
        anchor.setIconFlip(iconFlip);
    }

    @Override
    public IconFlip getIconFlip() {
        return anchor.getIconFlip();
    }

    @Override
    public void setIconRotate(final IconRotate iconRotate) {
        anchor.setIconRotate(iconRotate);
    }

    @Override
    public IconRotate getIconRotate() {
        return anchor.getIconRotate();
    }

    @Override
    public void setIconBordered(final boolean iconBordered) {
        anchor.setIconBordered(iconBordered);
    }

    @Override
    public boolean isIconBordered() {
        return anchor.isIconBordered();
    }

    @Override
    public void setIconInverse(final boolean iconInverse) {
        anchor.setIconInverse(iconInverse);
    }

    @Override
    public boolean isIconInverse() {
        return anchor.isIconInverse();
    }

    @Override
    public void setIconSpin(final boolean iconSpin) {
        anchor.setIconSpin(iconSpin);
    }

    @Override
    public boolean isIconSpin() {
        return anchor.isIconSpin();
    }

    @Override
    public void setIconPulse(final boolean iconPulse) {
        anchor.setIconPulse(iconPulse);
    }

    @Override
    public boolean isIconPulse() {
        return anchor.isIconPulse();
    }

    @Override
    public void setIconFixedWidth(final boolean iconFixedWidth) {
        anchor.setIconFixedWidth(iconFixedWidth);
    }

    @Override
    public boolean isIconFixedWidth() {
        return anchor.isIconFixedWidth();
    }

    @Override
    public void setIconColor(final String iconColor) {
        anchor.setIconColor(iconColor);
    }

    @Override
    public void setBadgeText(final String badgeText) {
        anchor.setBadgeText(badgeText);
    }

    @Override
    public String getBadgeText() {
        return anchor.getBadgeText();
    }

    @Override
    public void setBadgePosition(final BadgePosition badgePosition) {
        anchor.setBadgePosition(badgePosition);
    }

    @Override
    public BadgePosition getBadgePosition() {
        return anchor.getBadgePosition();
    }

    @Override
    public int getTabIndex() {
        return anchor.getTabIndex();
    }

    @Override
    public void setTabIndex(final int index) {
        anchor.setTabIndex(index);
    }

    @Override
    public void setAccessKey(final char key) {
        anchor.setAccessKey(key);
    }

    @Override
    public void setFocus(final boolean focused) {
        anchor.setFocus(focused);
    }

    public void setTargetHistoryToken(final String targetHistoryToken) {
        anchor.setTargetHistoryToken(targetHistoryToken);
    }

    public String getTargetHistoryToken() {
        return anchor.getTargetHistoryToken();
    }

    public String getTarget() {
        return anchor.getTarget();
    }

}
