package org.gwtbootstrap3.demo.client.application.css;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 GwtBootstrap3
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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Button;

/**
 * @author Joshua Godi
 */
public class ButtonsView extends Composite {

    interface Binder extends UiBinder<Widget, ButtonsView> {
    }


    private static final Binder BINDER = GWT.create(Binder.class);
    @UiField
    Button button;

    public ButtonsView() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @UiHandler("button")
    public void onButtonClick(final ClickEvent event) {
        button.state().loading();

        new Timer() {
            @Override
            public void run() {
                button.state().reset();
            }
        }.schedule(2000);
    }
}
