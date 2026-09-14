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

import io.instanto.bootstrap5.client.ui.base.BootstrapComponent;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import io.instanto.bootstrap5.client.shared.event.ModalHiddenEvent;
import io.instanto.bootstrap5.client.shared.event.ModalHiddenHandler;
import io.instanto.bootstrap5.client.shared.event.ModalHideEvent;
import io.instanto.bootstrap5.client.shared.event.ModalHideHandler;
import io.instanto.bootstrap5.client.shared.event.ModalShowEvent;
import io.instanto.bootstrap5.client.shared.event.ModalShowHandler;
import io.instanto.bootstrap5.client.shared.event.ModalShownEvent;
import io.instanto.bootstrap5.client.shared.event.ModalShownHandler;
import io.instanto.bootstrap5.client.ui.base.BootstrapEventBridge;
import io.instanto.bootstrap5.client.ui.base.BootstrapEventHandler;
import io.instanto.bootstrap5.client.ui.constants.ModalBackdrop;

public class Modal extends ElementPanel implements IsClosable {

    private final ElementPanel dialog = new ElementPanel("div");
    private final ElementPanel content = new ElementPanel("div");
    private ModalHeader header = new ModalHeader();
    private final ModalBody body = new ModalBody();
    private ModalSize size = ModalSize.DEFAULT;
    private boolean hideOtherModals;
    private boolean removeOnHide;

    public Modal() {
        super("div");
        addStyleName("modal");
        getElement().setAttribute("role", "dialog");
        getElement().setAttribute("aria-modal", "true");
        getElement().setAttribute("aria-labelledby", header.getTitleId());
        getElement().setAttribute("tabindex", "-1");
        dialog.addStyleName("modal-dialog");
        content.addStyleName("modal-content");
        content.add(header);
        content.add(body);
        dialog.add(content);
        super.add(dialog);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        BootstrapEventBridge.bind(getElement(), "show.bs.modal", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                onShow(Event.as(event));
            }
        });
        BootstrapEventBridge.bind(getElement(), "shown.bs.modal", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                onShown(Event.as(event));
            }
        });
        BootstrapEventBridge.bind(getElement(), "hide.bs.modal", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                onHide(Event.as(event));
            }
        });
        BootstrapEventBridge.bind(getElement(), "hidden.bs.modal", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                onHidden(Event.as(event));
            }
        });
    }

    @Override
    protected void onUnload() {
        BootstrapEventBridge.unbindAll(getElement());
        BootstrapComponent.dispose(getElement(), "Modal");
        super.onUnload();
    }

    public void setTitle(String title) {
        header.setTitle(title);
    }

    public void addToBody(Widget child) {
        body.add(child);
    }

    public void addHeader(ModalHeader header) {
        this.header.removeFromParent();
        this.header = header == null ? new ModalHeader() : header;
        getElement().setAttribute("aria-labelledby", this.header.getTitleId());
        content.insert(this.header, 0);
    }

    public void addFooter(ModalFooter footer) {
        content.add(footer);
    }

    @Override
    public void add(Widget child) {
        if (child instanceof ModalHeader) {
            addHeader((ModalHeader) child);
        } else if (child instanceof ModalFooter) {
            addFooter((ModalFooter) child);
        } else if (child instanceof ModalBody) {
            body.removeFromParent();
            content.add(child);
        } else {
            addToBody(child);
        }
    }

    public void setClosable(boolean closable) {
        header.setClosable(closable);
    }

    public boolean isClosable() {
        return header.isClosable();
    }

    public void setFade(boolean fade) {
        setStyleName("fade", fade);
    }

    public void setWidth(String width) {
        dialog.setWidth(width);
    }

    public void setSize(ModalSize size) {
        if (this.size != null && !this.size.cssName().isEmpty()) {
            dialog.removeStyleName(this.size.cssName());
        }
        this.size = size == null ? ModalSize.DEFAULT : size;
        if (!this.size.cssName().isEmpty()) {
            dialog.addStyleName(this.size.cssName());
        }
    }

    public ModalSize getSize() {
        return size;
    }

    public void setDataBackdrop(String backdrop) {
        if (backdrop == null || backdrop.isEmpty()) {
            getElement().removeAttribute("data-bs-backdrop");
        } else {
            getElement().setAttribute("data-bs-backdrop", backdrop);
        }
    }

    public void setDataBackdrop(ModalBackdrop backdrop) {
        setDataBackdrop(backdrop == null ? null : backdrop.getBackdrop());
    }

    public void setDataKeyboard(boolean keyboard) {
        getElement().setAttribute("data-bs-keyboard", Boolean.toString(keyboard));
    }

    public void setRemoveOnHide(boolean removeOnHide) {
        this.removeOnHide = removeOnHide;
    }

    public void setHideOtherModals(boolean hideOtherModals) {
        this.hideOtherModals = hideOtherModals;
    }

    public void show() {
        if (!isAttached()) {
            RootPanel.get().add(this);
        }
        BootstrapComponent.call(getElement(), "Modal", "show");
    }

    public void hide() {
        BootstrapComponent.call(getElement(), "Modal", "hide");
    }

    public void toggle() {
        BootstrapComponent.call(getElement(), "Modal", "toggle");
    }

    public HandlerRegistration addShowHandler(ModalShowHandler handler) {
        return addHandler(handler, ModalShowEvent.getType());
    }

    public HandlerRegistration addShownHandler(ModalShownHandler handler) {
        return addHandler(handler, ModalShownEvent.getType());
    }

    public HandlerRegistration addHideHandler(ModalHideHandler handler) {
        return addHandler(handler, ModalHideEvent.getType());
    }

    public HandlerRegistration addHiddenHandler(ModalHiddenHandler handler) {
        return addHandler(handler, ModalHiddenEvent.getType());
    }

    protected void onShow(Event event) {
        if (hideOtherModals) {
            BootstrapComponent.hideOtherModals(getElement());
        }
        fireEvent(new ModalShowEvent(this, event));
    }

    protected void onShown(Event event) {
        fireEvent(new ModalShownEvent(this, event));
    }

    protected void onHide(Event event) {
        fireEvent(new ModalHideEvent(this, event));
    }

    protected void onHidden(Event event) {
        fireEvent(new ModalHiddenEvent(this, event));
        if (removeOnHide) {
            removeFromParent();
        }
    }





}
