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

import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.base.HasJustified;
import io.instanto.bootstrap5.client.ui.base.HasStacked;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.Styles;

public class Nav extends ElementPanel implements HasJustified, HasStacked {

    public Nav() {
        super("ul");
        addStyleName("nav");
    }

    public void addItem(Widget child) {
        ElementPanel item = new ElementPanel("li");
        item.addStyleName("nav-item");
        item.add(child);
        add(item);
    }

    public Anchor addLink(String text, String href) {
        Anchor link = new Anchor(text, href);
        link.addStyleName("nav-link");
        addItem(link);
        return link;
    }

    @Override
    public void setJustified(final boolean justified) {
        setStyleName(Styles.NAV_JUSTIFIED, justified);
    }

    @Override
    public boolean isJustified() {
        return StyleHelper.containsStyle(getStyleName(), Styles.NAV_JUSTIFIED);
    }

    /** Bootstrap 5 has no .nav-stacked; a stacked nav is a flex column. */
    @Override
    public void setStacked(final boolean stacked) {
        setStyleName(Styles.NAV_STACKED, stacked);
    }

    @Override
    public boolean isStacked() {
        return StyleHelper.containsStyle(getStyleName(), Styles.NAV_STACKED);
    }

    public void setInline(final boolean inline) {
        setStyleName(Styles.LIST_INLINE, inline);
    }

    public boolean isInline() {
        return StyleHelper.containsStyle(getStyleName(), Styles.LIST_INLINE);
    }

    public void setUnstyled(final boolean unstyled) {
        setStyleName(Styles.LIST_UNSTYLED, unstyled);
    }

    public boolean isUnstyled() {
        return StyleHelper.containsStyle(getStyleName(), Styles.LIST_UNSTYLED);
    }

}
