/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2018 GwtBootstrap3
 * %%
 * Modified from the GwtBootstrap3 original for the Bootstrap 5 track of
 * GWT Bootstrap.
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

import io.instanto.bootstrap5.client.ui.base.AbstractButtonGroup;
import io.instanto.bootstrap5.client.ui.constants.Styles;

import com.google.gwt.user.client.ui.Widget;

/** Vertical Bootstrap 5 button group retaining the Bootstrap 3 API. */
public class VerticalButtonGroup extends AbstractButtonGroup {

    public VerticalButtonGroup() {
        super(Styles.BTN_GROUP_VERTICAL);
        getElement().setAttribute("role", "group");
    }

    public void addButton(Widget button) {
        add(button);
    }
}
