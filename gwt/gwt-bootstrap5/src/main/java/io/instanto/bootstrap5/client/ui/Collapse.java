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
import com.google.gwt.event.shared.HandlerRegistration;
import io.instanto.bootstrap5.client.shared.event.HiddenEvent;
import io.instanto.bootstrap5.client.shared.event.HiddenHandler;
import io.instanto.bootstrap5.client.shared.event.HideEvent;
import io.instanto.bootstrap5.client.shared.event.HideHandler;
import io.instanto.bootstrap5.client.shared.event.ShowEvent;
import io.instanto.bootstrap5.client.shared.event.ShowHandler;
import io.instanto.bootstrap5.client.shared.event.ShownEvent;
import io.instanto.bootstrap5.client.shared.event.ShownHandler;
import io.instanto.bootstrap5.client.ui.base.BootstrapEventBridge;
import io.instanto.bootstrap5.client.ui.base.BootstrapEventHandler;

public class Collapse extends ElementPanel {

    private boolean toggle = true;

    public Collapse() {
        super("div");
        addStyleName("collapse");
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (toggle) {
            setShown(true);
        }
        BootstrapEventBridge.bind(getElement(), "show.bs.collapse", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                fireEvent(new ShowEvent(event));
            }
        });
        BootstrapEventBridge.bind(getElement(), "shown.bs.collapse", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                fireEvent(new ShownEvent(event));
            }
        });
        BootstrapEventBridge.bind(getElement(), "hide.bs.collapse", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                fireEvent(new HideEvent(event));
            }
        });
        BootstrapEventBridge.bind(getElement(), "hidden.bs.collapse", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                fireEvent(new HiddenEvent(event));
            }
        });
    }

    @Override
    protected void onUnload() {
        BootstrapEventBridge.unbindAll(getElement());
        BootstrapComponent.dispose(getElement(), "Collapse");
        super.onUnload();
    }

    public void setShown(boolean shown) {
        setStyleName("show", shown);
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public void setIn(boolean in) {
        setShown(in);
    }

    public boolean isShown() {
        return getStyleName().contains("show");
    }

    public boolean isHidden() {
        return !isShown();
    }

    public boolean isCollapsing() {
        return getStyleName().contains("collapsing");
    }

    public HandlerRegistration addShowHandler(ShowHandler handler) {
        return addHandler(handler, ShowEvent.getType());
    }

    public HandlerRegistration addShownHandler(ShownHandler handler) {
        return addHandler(handler, ShownEvent.getType());
    }

    public HandlerRegistration addHideHandler(HideHandler handler) {
        return addHandler(handler, HideEvent.getType());
    }

    public HandlerRegistration addHiddenHandler(HiddenHandler handler) {
        return addHandler(handler, HiddenEvent.getType());
    }

    public void show() {
        BootstrapComponent.callCollapse(getElement(), "show");
    }

    public void hide() {
        BootstrapComponent.callCollapse(getElement(), "hide");
    }

    public void toggle() {
        BootstrapComponent.callCollapse(getElement(), "toggle");
    }




}
