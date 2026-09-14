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

import io.instanto.bootstrap5.client.ui.base.HasTabPosition;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.TabPosition;


public class TabPanel extends ElementPanel implements HasTabPosition {

    private final NavTabs tabs = new NavTabs();
    private final TabContent content = new TabContent();

    public TabPanel() {
        super("div");
        tabs.getElement().setAttribute("role", "tablist");
        add(tabs);
        add(content);
    }

    public NavTabs getTabs() {
        return tabs;
    }

    public TabContent getContent() {
        return content;
    }

    private TabPosition tabPosition = TabPosition.TOP;

    /**
     * Bootstrap 5 has no .tabs-left / .tabs-right / .tabs-below; the same
     * layouts come from flex ordering, which is what TabPosition now names.
     */
    @Override
    public void setTabPosition(final TabPosition tabPosition) {
        this.tabPosition = tabPosition == null ? TabPosition.TOP : tabPosition;
        StyleHelper.addUniqueEnumStyleName(this, TabPosition.class, this.tabPosition);
    }

    @Override
    public TabPosition getTabPosition() {
        return tabPosition;
    }

}
