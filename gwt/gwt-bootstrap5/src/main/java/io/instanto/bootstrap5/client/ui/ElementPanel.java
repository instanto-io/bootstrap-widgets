/*
 * #%L
 * GWT Bootstrap
 * %%
 * Copyright (C) 2026 Carl Stainton
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

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.base.ComplexWidget;
import io.instanto.bootstrap5.client.ui.base.HasDataParent;
import io.instanto.bootstrap5.client.ui.base.HasDataTarget;
import io.instanto.bootstrap5.client.ui.base.HasDataToggle;
import io.instanto.bootstrap5.client.ui.base.mixin.DataParentMixin;
import io.instanto.bootstrap5.client.ui.base.mixin.DataTargetMixin;
import io.instanto.bootstrap5.client.ui.base.mixin.DataToggleMixin;
import io.instanto.bootstrap5.client.ui.constants.Attributes;
import io.instanto.bootstrap5.client.ui.constants.Toggle;

/*
 * HasWidgets MUST stay ahead of HasHTML in the implements clause below.
 *
 * UiBinder picks the first content parser it finds walking the type hierarchy. HasWidgets
 * is inherited from ComplexPanel, further up than this class, so without naming it here
 * the HasHTML parser wins and every template nesting a widget inside one of the seventy
 * widgets extending ElementPanel fails with "Not allowed in an HTML context". Reordering
 * or tidying this list silently breaks UiBinder with no compile error; the Setup section
 * of the showcase is what catches it. Bootstrap 3 avoids the problem by having its
 * containers extend Div, which is not HasHTML.
 */
class ElementPanel extends ComplexWidget implements HasWidgets, HasHTML, HasClickHandlers, HasDoubleClickHandlers, HasDataParent, HasDataTarget, HasDataToggle {

    private final DataParentMixin<ElementPanel> parentMixin = new DataParentMixin<ElementPanel>(this);
    private final DataTargetMixin<ElementPanel> targetMixin = new DataTargetMixin<ElementPanel>(this);
    private final DataToggleMixin<ElementPanel> toggleMixin = new DataToggleMixin<ElementPanel>(this);

    ElementPanel(String tagName) {
        setElement(Document.get().createElement(tagName));
    }

    @Override
    public void add(Widget child) {
        add(child, getElement());
    }

    public void insert(Widget child, int beforeIndex) {
        insert(child, getElement(), beforeIndex, true);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    @Override
    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
        return addDomHandler(handler, DoubleClickEvent.getType());
    }

    @Override
    public String getText() {
        return getElement().getInnerText();
    }

    @Override
    public void setText(String text) {
        getElement().setInnerText(text == null ? "" : text);
    }

    public String getHTML() {
        return getElement().getInnerHTML();
    }

    public void setHTML(String html) {
        getElement().setInnerHTML(html == null ? "" : html);
    }

    @Override
    public void setDataParent(String dataParent) {
        parentMixin.setDataParent(dataParent);
    }

    @Override
    public String getDataParent() {
        return parentMixin.getDataParent();
    }

    @Override
    public void setDataTargetWidget(Widget widget) {
        targetMixin.setDataTargetWidget(widget);
    }

    @Override
    public void setDataTargetWidgets(List<Widget> widgets) {
        targetMixin.setDataTargetWidgets(widgets);
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
    public void setDataToggle(Toggle toggle) {
        toggleMixin.setDataToggle(toggle);
    }

    public void setDataToggle(String toggle) {
        if (toggle == null || toggle.isEmpty()) {
            setDataToggle((Toggle) null);
        } else {
            getElement().setAttribute(Attributes.DATA_TOGGLE, toggle);
        }
    }

    @Override
    public Toggle getDataToggle() {
        return toggleMixin.getDataToggle();
    }
}
