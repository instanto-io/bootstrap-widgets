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

import static io.instanto.bootstrap5.showcase.client.ShowcasePanels.*;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.client.ui.Column;
import io.instanto.bootstrap5.client.ui.Dialogs;
import io.instanto.bootstrap5.client.ui.FormGroup;
import io.instanto.bootstrap5.client.ui.FormLabel;
import io.instanto.bootstrap5.client.ui.PanelBody;
import io.instanto.bootstrap5.client.ui.Row;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;
import io.instanto.bootstrap5.extras.datepicker.client.ui.DatePicker;
import io.instanto.bootstrap5.extras.markdown.client.ui.MarkdownEditor;
import io.instanto.bootstrap5.extras.markdown.client.ui.MarkdownPanel;
import io.instanto.bootstrap5.extras.richtext.client.ui.RichTextEditor;
import io.instanto.bootstrap5.extras.slider.client.ui.Slider;

/** Creates only the requested showcase section. */
final class IntegrationPages {
  Row createIntegrationSections(String token) {
    Row row = row();
    Column column = fullColumn(row);

    switch (token) {
      case "fullCalendar":
        {
          addPageHeader(column, "fullCalendar", "FullCalendar", "Bootstrap 5 scheduling");
          column.add(CalendarExample.create());
          break;
        }
      case "datePicker":
        {
          addPageHeader(column, "datePicker", "DatePicker", "Tempus Dominus 6");
          column.add(datePickerPanel());
          break;
        }
      case "richText":
        {
          addPageHeader(column, "richText", "Rich Text", "Quill 2");
          column.add(richTextPanel());
          break;
        }
      case "markdown":
        {
          addPageHeader(column, "markdown", "Markdown", "for applications that store Markdown");
          column.add(markdownPanel());
          break;
        }
      case "slider":
        {
          addPageHeader(column, "slider", "Slider", "noUiSlider 15");
          column.add(sliderPanel());
          break;
        }
      case "searchableSelect":
        {
          addPageHeader(
              column,
              "searchableSelect",
              "Searchable Select",
              "search, tags and asynchronous options");
          column.add(
              panel(
                  "Selection and tags",
                  IntegrationExamples.selects(),
                  "SearchableSelect tags = new SearchableSelect();\ntags.setMultiple(true);\ntags.setAllowCreate(true);\ntags.addOption(\"java\", \"Java\");\ntags.addValueChangeHandler(event -> { /* event.getValue() */ });"));
          break;
        }
      case "dataGrid":
        {
          addPageHeader(column, "dataGrid", "Data Grid", "editing, grouping and paging");
          column.add(
              panel(
                  "Editable team directory",
                  IntegrationExamples.grid(),
                  "DataTable table = new DataTable();\ntable.setColumns(columns);\ntable.setRows(rows);\ntable.setGroupBy(\"team\");\ntable.setPageSize(5);"));
          break;
        }
      case "sortableLists":
        {
          addPageHeader(column, "sortableLists", "Sortable Lists", "drag cards between columns");
          column.add(
              panel(
                  "Work board",
                  IntegrationExamples.sortable(),
                  "SortableList todo = new SortableList();\ntodo.setGroup(\"work\");\ntodo.add(new Paragraph(\"Review changes\"));\ntodo.addValueChangeHandler(event -> { /* ordered child widgets */ });"));
          break;
        }
      case "dashboard":
        {
          addPageHeader(column, "dashboard", "Dashboard", "move and resize tiles");
          column.add(
              panel(
                  "Custom layout",
                  IntegrationExamples.dashboard(),
                  "Dashboard dashboard = new Dashboard();\nDashboardTile tile = new DashboardTile(\"summary\", 0, 0, 6, 2);\ntile.add(new Paragraph(\"Summary\"));\ndashboard.add(tile);\n// dashboard.getLayoutJson() stores positions, not widget contents."));
          break;
        }
      case "gallery":
        {
          addPageHeader(column, "gallery", "Gallery", "open an image, zoom or swipe");
          column.add(
              panel(
                  "Image viewer",
                  IntegrationExamples.gallery(),
                  "ImageGallery gallery = new ImageGallery();\ngallery.addImage(\"landscape.svg\", \"landscape.svg\", 960, 640, \"Landscape\");"));
          break;
        }
      case "unsupportedExtras":
        {
          addPageHeader(column, "unsupportedExtras", "About Integrations", "optional components");
          column.add(
              panel(
                  "Loading integrations",
                  new HTML(
                      "<p>Each integration bundles its own scripts and styles. Enable only the modules your application uses. "
                          + "Widgets release plugin listeners when removed, and can be attached again.</p>"),
                  "<inherits name=\"io.instanto.bootstrap5.extras.select.Select\"/>"));
          break;
        }
      default:
        throw new IllegalArgumentException("Unknown section: " + token);
    }
    return row;
  }

  private Widget datePickerPanel() {
    final HTML echo = new HTML("<p class='text-body-secondary mb-0'>Nothing picked yet.</p>");

    FormGroup dateGroup = new FormGroup();
    dateGroup.add(new FormLabel("Date"));
    final DatePicker date = new DatePicker("Pick a date");
    date.setFormat("yyyy-MM-dd");
    echo.getElement().setId("date-picker-value");
    final int[] changes = {0};
    date.addValueChangeHandler(
        event -> {
          echo.getElement().setAttribute("data-change-count", String.valueOf(++changes[0]));
          echo.getElement()
              .setAttribute("data-source-match", String.valueOf(event.getSource() == date));
          echo.getElement()
              .setAttribute(
                  "data-value",
                  date.getValue() == null ? "null" : formatLocalDate(date.getValue()));
        });
    date.addValueChangeHandler(
        new ValueChangeHandler<java.util.Date>() {
          @Override
          public void onValueChange(ValueChangeEvent<java.util.Date> event) {
            echo.setHTML(
                event.getValue() == null
                    ? "<p class='mb-0'>Cleared.</p>"
                    : "<p class='mb-0'>Picked <strong>"
                        + formatLocalDate(event.getValue())
                        + "</strong></p>");
          }
        });
    dateGroup.add(date);
    Button remount = new Button("Remove / restore date picker");
    remount.addClickHandler(
        event -> {
          if (date.isAttached()) date.removeFromParent();
          else dateGroup.add(date);
        });
    dateGroup.add(remount);

    FormGroup dateTimeGroup = new FormGroup();
    dateTimeGroup.add(new FormLabel("Date and time, side by side"));
    DatePicker dateTime = new DatePicker("Pick a moment");
    dateTime.setFormat("yyyy-MM-dd HH:mm");
    dateTime.setSideBySide(true);
    dateTimeGroup.add(dateTime);

    PanelBody body = new PanelBody();
    body.add(dateGroup);
    body.add(dateTimeGroup);
    body.add(echo);
    return panel(
        "Basic",
        body,
        "DatePicker date = new DatePicker(\"Pick a date\");\ndate.setFormat(\"yyyy-MM-dd\");\ndate.addValueChangeHandler(event -> { ... });\n\nDatePicker dateTime = new DatePicker();\ndateTime.setFormat(\"yyyy-MM-dd HH:mm\");\ndateTime.setSideBySide(true);\n\n// Tempus Dominus 6 replaces the two Bootstrap 3 pickers.\n// It targets Bootstrap 5 and needs no jQuery.");
  }

  private static String formatLocalDate(java.util.Date date) {
    int month = date.getMonth() + 1;
    int day = date.getDate();
    return (date.getYear() + 1900)
        + "-"
        + (month < 10 ? "0" : "")
        + month
        + "-"
        + (day < 10 ? "0" : "")
        + day;
  }

  private Widget richTextPanel() {
    final HTML echo = new HTML("<p class='text-body-secondary mb-0'>Nothing typed yet.</p>");

    final RichTextEditor editor = new RichTextEditor(RichTextEditor.Toolbar.FULL);
    editor.setPlaceholder("Write something...");
    editor.setHTML(
        "<p>Quill replaces the Summernote extra. "
            + "<strong>No jQuery</strong>, and it brings its own toolbar.</p>");
    editor.addValueChangeHandler(
        new ValueChangeHandler<String>() {
          @Override
          public void onValueChange(ValueChangeEvent<String> event) {
            echo.setHTML(
                "<p class='mb-0 text-body-secondary'>"
                    + event.getValue().length()
                    + " characters of HTML</p>");
          }
        });

    Button read = new Button("Read the HTML", ButtonType.PRIMARY);
    read.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            Dialogs.alert(editor.getHTML());
          }
        });

    PanelBody body = new PanelBody();
    body.add(editor);
    body.add(inline(read));
    body.add(echo);
    return panel(
        "Editor",
        body,
        "RichTextEditor editor = new RichTextEditor(Toolbar.FULL);\neditor.setPlaceholder(\"Write something...\");\neditor.setHTML(\"<p>Hello</p>\");\neditor.addValueChangeHandler(event -> { ... });\n\nString html = editor.getHTML();\n\n// Summernote is the one Bootstrap 3 extra with no jQuery-free\n// counterpart, so it is replaced rather than ported. Quill has\n// no dependencies and ships a UMD build ScriptInjector can load.");
  }

  private Widget sliderPanel() {
    final HTML echo = new HTML("<p class='text-body-secondary mb-0'>Drag a handle.</p>");

    FormGroup rangeGroup = new FormGroup();
    rangeGroup.add(new FormLabel("Two handles, with tooltips"));
    final Slider between = new Slider(0, 1000);
    between.setRange(true);
    between.setValues(200, 800);
    between.setStep(10);
    between.setTooltips(true);
    between.addValueChangeHandler(
        new ValueChangeHandler<Double>() {
          @Override
          public void onValueChange(ValueChangeEvent<Double> event) {
            echo.setHTML(
                "<p class='mb-0'>Between <strong>"
                    + (long) between.getLowerValue()
                    + "</strong> and <strong>"
                    + (long) between.getUpperValue()
                    + "</strong></p>");
          }
        });
    rangeGroup.add(between);

    FormGroup pipGroup = new FormGroup();
    pipGroup.add(new FormLabel("One handle, with a scale"));
    Slider scaled = new Slider(0, 100);
    scaled.setValue(40);
    scaled.setPips(true);
    pipGroup.add(scaled);

    PanelBody body = new PanelBody();
    body.add(
        new HTML(
            "<p class='text-body-secondary'>For a plain single value prefer"
                + " <code>Range</code> on the Forms page &mdash; Bootstrap 5 styles the native control and"
                + " nothing needs shipping. This is for what that cannot do.</p>"));
    body.add(rangeGroup);
    body.add(echo);
    body.add(pipGroup);
    return panel(
        "Beyond a native range input",
        body,
        "Slider between = new Slider(0, 1000);\nbetween.setRange(true);          // two handles\nbetween.setValues(200, 800);\nbetween.setTooltips(true);\nbetween.addValueChangeHandler(event -> {\n    between.getLowerValue(); between.getUpperValue();\n});\n\nSlider scaled = new Slider(0, 100);\nscaled.setPips(true);            // a scale beneath the track\n\n// Bootstrap 3 wrapped bootstrap-slider, a jQuery plugin.\n// noUiSlider has no dependencies at all.");
  }

  private Widget markdownPanel() {
    final String sample =
        "## Release notes\n\n"
            + "The **rich text** editor and this one are *separate* extras.\n\n"
            + "- Rich text keeps HTML\n"
            + "- This keeps ~~HTML~~ Markdown, exactly as typed\n\n"
            + "- [x] renders task lists\n"
            + "- [ ] and tables\n\n"
            + "| Extra | Stores |\n| --- | --- |\n| Rich Text | HTML |\n| Markdown | Markdown |\n\n"
            + "> Preview uses the same dialect a flexmark server renders.\n";

    final MarkdownEditor editor = new MarkdownEditor(sample);
    editor.setPlaceholder("Write Markdown...");
    editor.setVisibleLines(10);

    final MarkdownPanel rendered = new MarkdownPanel();
    rendered.addStyleName("border rounded p-3 mt-3");
    rendered.setMarkdown(sample);
    editor.addValueChangeHandler(
        new ValueChangeHandler<String>() {
          @Override
          public void onValueChange(ValueChangeEvent<String> event) {
            rendered.setMarkdown(event.getValue());
          }
        });

    PanelBody editorBody = new PanelBody();
    editorBody.add(
        new HTML(
            "<p class='text-body-secondary'>A textarea that keeps the Markdown as typed,"
                + " with a toolbar that inserts the syntax, a Preview tab, and the reference to hand rather"
                + " than a link away to another site.</p>"));
    editorBody.add(editor);
    editorBody.add(
        new HTML("<p class='form-label mt-3 mb-1'>Rendered live from the editor above</p>"));
    editorBody.add(rendered);

    return panel(
        "Editor and renderer",
        editorBody,
        "MarkdownEditor editor = new MarkdownEditor(source);\neditor.setPlaceholder(\"Write Markdown...\");\nString markdown = editor.getValue();   // as typed\nString html = editor.getHTML();        // rendered\n\nMarkdownPanel view = new MarkdownPanel(source);\nview.setMarkdown(updated);\n\n// Separate from the Rich Text extra on purpose. A rich text\n// editor stores HTML, so an application whose format is\n// Markdown would convert on every save and lose exactly the\n// constructs a flexmark server understands: task lists and\n// tables. Rendering is sanitised with DOMPurify, because\n// Markdown permits raw HTML and marked does not sanitise.");
  }
}
