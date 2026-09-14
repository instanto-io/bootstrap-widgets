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
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.Event;
import com.google.web.bindery.event.shared.HandlerRegistration;
import io.instanto.bootstrap5.client.shared.event.AlertCloseEvent;
import io.instanto.bootstrap5.client.shared.event.AlertCloseHandler;
import io.instanto.bootstrap5.client.shared.event.AlertClosedEvent;
import io.instanto.bootstrap5.client.shared.event.AlertClosedHandler;
import io.instanto.bootstrap5.client.ui.base.BootstrapEventBridge;
import io.instanto.bootstrap5.client.ui.base.BootstrapEventHandler;
import io.instanto.bootstrap5.client.ui.base.HasType;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.AlertType;

public class Alert extends ElementPanel implements HasType<AlertType> {

    private final InlineLabel text = new InlineLabel();
    private final ElementPanel closeButton = new ElementPanel("button");
    private Variant variant;

    public Alert() {
        super("div");
        addStyleName("alert");
        getElement().setAttribute("role", "alert");
        closeButton.setStyleName("btn-close");
        closeButton.getElement().setAttribute("type", "button");
        closeButton.getElement().setAttribute("data-bs-dismiss", "alert");
        closeButton.getElement().setAttribute("aria-label", "Close");
        setVariant(Variant.WARNING);
        addCloseHandler(new AlertCloseHandler() {
            @Override
            public void onClose(AlertCloseEvent event) {
                removeFromParent();
            }
        });
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        BootstrapEventBridge.bind(getElement(), "close.bs.alert", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                fireEvent(new AlertCloseEvent(Event.as(event)));
            }
        });
        BootstrapEventBridge.bind(getElement(), "closed.bs.alert", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                fireEvent(new AlertClosedEvent(Event.as(event)));
            }
        });
    }

    @Override
    protected void onUnload() {
        BootstrapEventBridge.unbindAll(getElement());
        BootstrapComponent.dispose(getElement(), "Alert");
        super.onUnload();
    }

    public Alert(String text) {
        this();
        setText(text);
    }

    public Alert(String text, Variant variant) {
        this(text);
        setVariant(variant);
    }

    public Alert(String text, AlertType type) {
        this(text);
        setType(type);
    }

    @Override
    public String getText() {
        return text.getText();
    }

    @Override
    public void setText(String text) {
        String effectiveText = text == null ? "" : text;
        this.text.setText(effectiveText);
        if (effectiveText.isEmpty()) {
            this.text.removeFromParent();
        } else if (this.text.getParent() == null) {
            insert(this.text, 0);
        }
    }

    public void setVariant(Variant variant) {
        if (this.variant != null) {
            removeStyleName(styleName(this.variant));
        }
        this.variant = variant == null ? Variant.WARNING : variant;
        addStyleName(styleName(this.variant));
    }

    public Variant getVariant() {
        return variant;
    }

    @Override
    public void setType(AlertType type) {
        StyleHelper.addUniqueEnumStyleName(this, AlertType.class, type == null ? AlertType.DEFAULT : type);
    }

    @Override
    public AlertType getType() {
        return AlertType.fromStyleName(getStyleName());
    }

    public void setDismissible(boolean dismissible) {
        setStyleName("alert-dismissible", dismissible);
        if (dismissible) {
            if (closeButton.getParent() == null) {
                add(closeButton);
            }
        } else {
            closeButton.removeFromParent();
        }
    }

    public void setDismissable(boolean dismissable) {
        setDismissible(dismissable);
    }

    public boolean isDismissable() {
        return closeButton.getParent() != null;
    }

    public void setFade(boolean fade) {
        setStyleName("fade", fade);
        setStyleName("show", fade);
    }

    public boolean isFade() {
        return getStyleName().contains("fade");
    }

    public void close() {
        BootstrapComponent.call(getElement(), "Alert", "close");
    }

    public HandlerRegistration addCloseHandler(AlertCloseHandler handler) {
        return addHandler(handler, AlertCloseEvent.getType());
    }

    public HandlerRegistration addClosedHandler(AlertClosedHandler handler) {
        return addHandler(handler, AlertClosedEvent.getType());
    }

    private String styleName(Variant variant) {
        return "alert-" + variant.cssName();
    }


}
