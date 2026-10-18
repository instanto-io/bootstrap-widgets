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
package io.instanto.bootstrap5.showcase.client;

import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.Column;
import io.instanto.bootstrap5.client.ui.Heading;
import io.instanto.bootstrap5.client.ui.PageHeader;
import io.instanto.bootstrap5.client.ui.Panel;
import io.instanto.bootstrap5.client.ui.PanelBody;
import io.instanto.bootstrap5.client.ui.PanelFooter;
import io.instanto.bootstrap5.client.ui.PanelHeader;
import io.instanto.bootstrap5.client.ui.Pre;
import io.instanto.bootstrap5.client.ui.Row;

/** Small layout helpers shared by the showcase page factories. */
final class ShowcasePanels {
  private ShowcasePanels() {}

  static final String IMG_WIDE =
      "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='640' height='240'%3E%3Crect width='100%25' height='100%25' fill='rgb(13,110,253)'/%3E%3Ctext x='50%25' y='50%25' fill='white' text-anchor='middle' dominant-baseline='middle' font-family='sans-serif' font-size='32'%3EBootstrap 5%3C/text%3E%3C/svg%3E";
  static final String IMG_THUMB =
      "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='320' height='180'%3E%3Crect width='100%25' height='100%25' fill='rgb(25,135,84)'/%3E%3Ctext x='50%25' y='50%25' fill='white' text-anchor='middle' dominant-baseline='middle' font-family='sans-serif' font-size='24'%3EThumbnail%3C/text%3E%3C/svg%3E";

  static Row row() {
    Row row = new Row();
    row.setStyleName("row");
    return row;
  }

  static Column fullColumn() {
    Column column = new Column(12);
    return column;
  }

  static Column fullColumn(Row row) {
    Column column = fullColumn();
    row.add(column);
    return column;
  }

  static void addPageHeader(Column column, String id, String title, String subText) {
    PageHeader header = new PageHeader();
    header.getElement().setAttribute("data-section", id);
    Heading heading = new Heading(2, title);
    if (subText != null && !subText.isEmpty()) {
      heading.add(new InlineHTML(" <small class='text-body-secondary'>" + subText + "</small>"));
    }
    header.add(heading);
    column.add(header);
  }

  static Panel panel(String title, Widget bodyWidget, String code) {
    Panel panel = new Panel();
    panel.addStyleName("mb-4");
    PanelHeader header = new PanelHeader();
    header.add(new Heading(3, title));
    PanelBody body = new PanelBody();
    body.addStyleName("gbm-example-body");
    body.add(bodyWidget);
    PanelFooter footer = new PanelFooter();
    Pre pre = new Pre(code);
    pre.addStyleName("mb-0 small");
    footer.add(pre);
    panel.add(header);
    panel.add(body);
    panel.add(footer);
    return panel;
  }

  static Widget inline(Widget... widgets) {
    PanelBody wrapper = new PanelBody();
    wrapper.setStyleName("gbm-inline-demo");
    for (Widget widget : widgets) {
      wrapper.add(widget);
    }
    return wrapper;
  }

  static Widget stacked(Widget... widgets) {
    PanelBody wrapper = new PanelBody();
    wrapper.setStyleName("gbm-stacked-demo");
    for (Widget widget : widgets) {
      wrapper.add(widget);
    }
    return wrapper;
  }
}
