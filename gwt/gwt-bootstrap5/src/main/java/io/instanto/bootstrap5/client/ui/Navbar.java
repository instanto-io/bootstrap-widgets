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

import io.instanto.bootstrap5.client.ui.base.HasType;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.NavbarPosition;
import io.instanto.bootstrap5.client.ui.constants.NavbarType;


public class Navbar extends ElementPanel implements HasType<NavbarType> {

    private final Container container = new Container();
    private final NavbarNav nav = new NavbarNav();

    public Navbar() {
        super("nav");
        setStyleName("navbar navbar-expand-xl bg-body-tertiary border-bottom");
        container.addStyleName("py-0");
        container.add(nav);
        add(container);
    }

    public Container getContainer() {
        return container;
    }

    public NavbarNav getNav() {
        return nav;
    }

    public void setDark(boolean dark) {
        setStyleName(dark ? "navbar navbar-expand-xl navbar-dark bg-dark" : "navbar navbar-expand-xl bg-body-tertiary border-bottom");
    }

    /**
     * Bootstrap 5 dropped .navbar-default and .navbar-inverse in favour of
     * data-bs-theme and the background utilities; NavbarType names the
     * Bootstrap 5 equivalents so the Bootstrap 3 spelling still works.
     */
    @Override
    public void setType(final NavbarType type) {
        setDark(NavbarType.INVERSE == type);
    }

    @Override
    public NavbarType getType() {
        return StyleHelper.containsStyle(getStyleName(), "navbar-dark") ? NavbarType.INVERSE : NavbarType.DEFAULT;
    }

    public void setPosition(final NavbarPosition position) {
        StyleHelper.addUniqueEnumStyleName(this, NavbarPosition.class,
                position == null ? NavbarPosition.DEFAULT : position);
    }

    public NavbarPosition getPosition() {
        return NavbarPosition.fromStyleName(getStyleName());
    }

}
