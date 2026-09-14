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
package org.gwtbootstrap3.client.ui.theme;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.ListDropDown;
import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import java.util.List;

/**
 * A navbar dropdown listing the registered themes.
 *
 * <p>Drop it into a {@code NavbarNav}. It lists whatever {@link Themes} holds at the
 * moment it is constructed, and ticks the active entry -- following later changes too,
 * so a theme applied from elsewhere still shows here.</p>
 */
public class ThemeSwitcher extends ListDropDown {

    private final DropDownMenu menu = new DropDownMenu();
    private final AnchorButton toggle = new AnchorButton();
    private final String label;

    public ThemeSwitcher() {
        this("Theme");
    }

    public ThemeSwitcher(final String label) {
        this.label = label;
        toggle.setDataToggle(Toggle.DROPDOWN);
        toggle.setText(label);
        toggle.setToggleCaret(true);
        add(toggle);
        add(menu);
        rebuild();
        Themes.addThemeChangeHandler(theme -> {
            markActive();
            updateToggleText();
        });
    }

    /** Rebuilds the list from the themes currently registered. */
    public void rebuild() {
        menu.clear();
        for (final Theme theme : Themes.getThemes()) {
            final AnchorListItem item = new AnchorListItem(theme.getDisplayName());
            item.addClickHandler(event -> Themes.apply(theme));
            menu.add(item);
        }
        markActive();
        updateToggleText();
    }

    private void markActive() {
        final Theme current = Themes.getCurrent();
        final List<Theme> themes = Themes.getThemes();
        int index = 0;
        for (final com.google.gwt.user.client.ui.Widget child : menu) {
            if (child instanceof AnchorListItem && index < themes.size()) {
                ((AnchorListItem) child).setActive(themes.get(index).equals(current));
            }
            index++;
        }
    }

    private void updateToggleText() {
        final Theme current = Themes.getCurrent();
        toggle.setText(current == null ? label : label + ": " + current.getDisplayName());
    }
}
