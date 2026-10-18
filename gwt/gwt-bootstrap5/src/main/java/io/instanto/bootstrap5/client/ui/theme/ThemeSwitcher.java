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

import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.DropDownItem;
import io.instanto.bootstrap5.client.ui.ListDropDown;
import java.util.List;

/**
 * A navbar dropdown listing every theme registered with {@link Themes}.
 *
 * <p>Same shape as the Bootstrap 3 switcher, built on the Bootstrap 5 dropdown: {@link
 * ListDropDown} owns its toggle and menu, so items are added through it rather than assembled by
 * hand.
 */
public class ThemeSwitcher extends ListDropDown {

  private final String label;
  private final ThemeChangeHandler themeHandler =
      theme -> {
        if (isAttached()) {
          markActive();
          updateToggleText();
        }
      };

  public ThemeSwitcher() {
    this("Theme");
  }

  public ThemeSwitcher(final String label) {
    super(label);
    this.label = label;
    rebuildMenu();
  }

  /** Rebuilds the menu from the current contents of the {@link Themes} registry. */
  public void rebuild() {
    rebuildMenu();
  }

  private void rebuildMenu() {
    super.getMenu().clear();
    for (final Theme theme : Themes.getThemes()) {
      final DropDownItem item = new DropDownItem();
      item.setText(theme.getDisplayName());
      item.setHref("javascript:;");
      item.addClickHandler(event -> Themes.apply(theme));
      super.getMenu().add(item);
    }
    markActive();
    updateToggleText();
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    Themes.addThemeChangeHandler(themeHandler);
    markActive();
    updateToggleText();
  }

  @Override
  protected void onUnload() {
    Themes.removeThemeChangeHandler(themeHandler);
    super.onUnload();
  }

  private void markActive() {
    final Theme current = Themes.getCurrent();
    final List<Theme> themes = Themes.getThemes();
    int index = 0;
    for (final Widget child : super.getMenu()) {
      if (child instanceof DropDownItem && index < themes.size()) {
        child.setStyleName("active", themes.get(index).equals(current));
      }
      index++;
    }
  }

  private void updateToggleText() {
    final Theme current = Themes.getCurrent();
    super.getToggle().setText(current == null ? label : label + ": " + current.getDisplayName());
  }
}
