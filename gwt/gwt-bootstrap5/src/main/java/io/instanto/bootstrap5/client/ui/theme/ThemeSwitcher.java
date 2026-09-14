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
package io.instanto.bootstrap5.client.ui.theme;

import java.util.List;

import io.instanto.bootstrap5.client.ui.DropDownItem;
import io.instanto.bootstrap5.client.ui.ListDropDown;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * A navbar dropdown listing every theme registered with {@link Themes}.
 *
 * <p>Same shape as the Bootstrap 3 switcher, built on the Bootstrap 5 dropdown:
 * {@link ListDropDown} owns its toggle and menu, so items are added through it
 * rather than assembled by hand.</p>
 */
public class ThemeSwitcher extends ListDropDown {

    private final String label;

    public ThemeSwitcher() {
        this("Theme");
    }

    public ThemeSwitcher(final String label) {
        super(label);
        this.label = label;
        rebuild();
        Themes.addThemeChangeHandler(new ThemeChangeHandler() {
            @Override
            public void onThemeChanged(final Theme theme) {
                markActive();
                updateToggleText();
            }
        });
    }

    /** Rebuilds the menu from the current contents of the {@link Themes} registry. */
    public void rebuild() {
        getMenu().clear();
        for (final Theme theme : Themes.getThemes()) {
            final DropDownItem item = new DropDownItem();
            item.setText(theme.getDisplayName());
            item.setHref("javascript:;");
            item.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent event) {
                    Themes.apply(theme);
                }
            });
            addItem(item);
        }
        markActive();
        updateToggleText();
    }

    private void markActive() {
        final Theme current = Themes.getCurrent();
        final List<Theme> themes = Themes.getThemes();
        int index = 0;
        for (final Widget child : getMenu()) {
            if (child instanceof DropDownItem && index < themes.size()) {
                child.setStyleName("active", themes.get(index).equals(current));
            }
            index++;
        }
    }

    private void updateToggleText() {
        final Theme current = Themes.getCurrent();
        getToggle().setText(current == null ? label : label + ": " + current.getDisplayName());
    }
}
