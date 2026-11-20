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
import io.instanto.bootstrap5.client.ui.constants.ButtonType;

public class DropDown extends ElementPanel {
  private static final String DROPDOWN = "dropdown";
  private static final String DROPUP = "dropup";
  private static final String DROPSTART = "dropstart";
  private static final String DROPEND = "dropend";

  private final Button toggle;
  private final DropDownMenu menu = new DropDownMenu();

  public DropDown() {
    this("Dropdown");
  }

  public DropDown(String text) {
    this("div", text);
  }

  protected DropDown(String tagName, String text) {
    super(tagName);
    addStyleName(DROPDOWN);
    toggle = new Button(text, ButtonType.DEFAULT);
    toggle.addStyleName("dropdown-toggle");
    toggle.getElement().setAttribute("data-bs-toggle", DROPDOWN);
    toggle.getElement().setAttribute("aria-expanded", "false");
    add(toggle);
    add(menu);
  }

  public Button getToggle() {
    return toggle;
  }

  public DropDownMenu getMenu() {
    return menu;
  }

  @Override
  public String getText() {
    return toggle.getText();
  }

  @Override
  public void setText(String text) {
    toggle.setText(text == null ? "" : text);
  }

  public void addItem(DropDownItem item) {
    menu.add(item);
  }

  public void addMenuWidget(Widget widget) {
    menu.add(widget);
  }

  public void setDropUp(boolean dropUp) {
    setStyleName(DROPUP, dropUp);
    setStyleName(DROPDOWN, !dropUp);
  }

  public void setDropStart(boolean dropStart) {
    setStyleName(DROPSTART, dropStart);
    if (dropStart) {
      removeStyleName(DROPDOWN);
      removeStyleName(DROPUP);
      removeStyleName(DROPEND);
    } else if (!getStyleName().contains(DROPEND) && !getStyleName().contains(DROPUP)) {
      addStyleName(DROPDOWN);
    }
  }

  public void setDropEnd(boolean dropEnd) {
    setStyleName(DROPEND, dropEnd);
    if (dropEnd) {
      removeStyleName(DROPDOWN);
      removeStyleName(DROPUP);
      removeStyleName(DROPSTART);
    } else if (!getStyleName().contains(DROPSTART) && !getStyleName().contains(DROPUP)) {
      addStyleName(DROPDOWN);
    }
  }

  public void setMenuEndAligned(boolean endAligned) {
    menu.setEndAligned(endAligned);
  }

  public void setMenuMatchToggleWidth(boolean matchToggleWidth) {
    if (matchToggleWidth) {
      menu.addStyleName("dropdown-menu-match-toggle");
    } else {
      menu.removeStyleName("dropdown-menu-match-toggle");
    }
  }
}
