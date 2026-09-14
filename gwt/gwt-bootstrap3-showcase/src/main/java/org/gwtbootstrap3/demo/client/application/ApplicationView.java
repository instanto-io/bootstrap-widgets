package org.gwtbootstrap3.demo.client.application;

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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.NavbarCollapse;

/**
 * @author Joshua Godi
 */
public class ApplicationView extends Composite {

    static {
        // the switcher owns the Bootstrap stylesheet: the module inherits NoTheme so
        // nothing else declares one, and these are the sheets it chooses between
        // stock Bootstrap first, so there is always a way back to the familiar look
        org.gwtbootstrap3.client.ui.theme.Themes.register(
                org.gwtbootstrap3.client.ui.theme.StandardThemes.all());
        org.gwtbootstrap3.client.ui.theme.Themes.register(
                org.gwtbootstrap3.themes.client.BootswatchThemes.all());
        org.gwtbootstrap3.client.ui.theme.Themes.restore(
                org.gwtbootstrap3.client.ui.theme.StandardThemes.bootstrap(
                        com.google.gwt.core.client.GWT.getModuleBaseURL() + "css/"));
    }


    @UiField
    SimplePanel contentContainer;
    @UiField
    NavbarCollapse navbarCollapse;
    @UiField
    org.gwtbootstrap3.client.ui.AnchorListItem otherRuntime;

    /** The nav menu, so a page change can collapse it. */
    public NavbarCollapse getNavbarCollapse() {
        return navbarCollapse;
    }

    interface Binder extends UiBinder<Widget, ApplicationView> {
    }


    private static final Binder BINDER = GWT.create(Binder.class);
    public ApplicationView() {
        initWidget(BINDER.createAndBindUi(this));
        if ("teavm".equals(GWT.getModuleName())) {
            otherRuntime.setText("Bootstrap 3 Showcase (GWT)");
            otherRuntime.setHref("./");
        }
    }

    /** Shows a page in the shell. This was the framework's slot protocol. */
    public void setContent(final IsWidget content) {
        contentContainer.setWidget(content);
    }
}
