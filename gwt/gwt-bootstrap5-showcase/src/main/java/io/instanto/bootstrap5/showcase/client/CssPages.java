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
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import io.instanto.bootstrap5.client.ui.Abbreviation;
import io.instanto.bootstrap5.client.ui.Anchor;
import io.instanto.bootstrap5.client.ui.BlockQuote;
import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.client.ui.Caption;
import io.instanto.bootstrap5.client.ui.CheckBox;
import io.instanto.bootstrap5.client.ui.CheckBoxButton;
import io.instanto.bootstrap5.client.ui.Code;
import io.instanto.bootstrap5.client.ui.Column;
import io.instanto.bootstrap5.client.ui.Description;
import io.instanto.bootstrap5.client.ui.DescriptionData;
import io.instanto.bootstrap5.client.ui.DescriptionTitle;
import io.instanto.bootstrap5.client.ui.DoubleBox;
import io.instanto.bootstrap5.client.ui.FieldSet;
import io.instanto.bootstrap5.client.ui.Form;
import io.instanto.bootstrap5.client.ui.FormControlStatic;
import io.instanto.bootstrap5.client.ui.FormGroup;
import io.instanto.bootstrap5.client.ui.FormLabel;
import io.instanto.bootstrap5.client.ui.Heading;
import io.instanto.bootstrap5.client.ui.HelpBlock;
import io.instanto.bootstrap5.client.ui.Icon;
import io.instanto.bootstrap5.client.ui.Image;
import io.instanto.bootstrap5.client.ui.ImageAnchor;
import io.instanto.bootstrap5.client.ui.InlineCheckBox;
import io.instanto.bootstrap5.client.ui.InlineHelpBlock;
import io.instanto.bootstrap5.client.ui.InlineRadio;
import io.instanto.bootstrap5.client.ui.IntegerBox;
import io.instanto.bootstrap5.client.ui.Lead;
import io.instanto.bootstrap5.client.ui.Legend;
import io.instanto.bootstrap5.client.ui.ListBox;
import io.instanto.bootstrap5.client.ui.LongBox;
import io.instanto.bootstrap5.client.ui.PanelBody;
import io.instanto.bootstrap5.client.ui.Paragraph;
import io.instanto.bootstrap5.client.ui.Pre;
import io.instanto.bootstrap5.client.ui.Radio;
import io.instanto.bootstrap5.client.ui.RadioButton;
import io.instanto.bootstrap5.client.ui.Range;
import io.instanto.bootstrap5.client.ui.Row;
import io.instanto.bootstrap5.client.ui.SimpleCheckBox;
import io.instanto.bootstrap5.client.ui.SimpleRadioButton;
import io.instanto.bootstrap5.client.ui.StringRadioGroup;
import io.instanto.bootstrap5.client.ui.SubmitButton;
import io.instanto.bootstrap5.client.ui.SuggestBox;
import io.instanto.bootstrap5.client.ui.TextArea;
import io.instanto.bootstrap5.client.ui.TextBox;
import io.instanto.bootstrap5.client.ui.ValueListBox;
import io.instanto.bootstrap5.client.ui.Variant;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;
import io.instanto.bootstrap5.client.ui.constants.ColumnOffset;
import io.instanto.bootstrap5.client.ui.constants.ColumnSize;
import io.instanto.bootstrap5.client.ui.constants.IconType;
import io.instanto.bootstrap5.client.ui.constants.ImageType;
import io.instanto.bootstrap5.client.ui.constants.Toggle;
import io.instanto.bootstrap5.client.ui.html.Div;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/** Creates only the requested showcase section. */
final class CssPages {
  Row createCssSections(String token) {
    Row row = row();
    Column column = fullColumn(row);
    switch (token) {
      case "buttons":
        {
          addPageHeader(column, "buttons", "Buttons", "styled, states, icons...");
          column.add(buttonsBasicPanel());
          column.add(buttonSizesPanel());
          column.add(buttonStatesPanel());
          column.add(buttonCompositionPanel());
          break;
        }
      case "code":
        {
          addPageHeader(column, "code", "Code", null);
          column.add(codeVariantsPanel());
          column.add(
              panel(
                  "Inline and block code",
                  inline(new Code("Code"), new Pre("Preformatted text")),
                  "new Code(\"Code\");\nnew Pre(\"Preformatted text\");"));
          break;
        }
      case "forms":
        {
          addPageHeader(column, "forms", "Forms", null);
          column.add(formsPanel());
          column.add(rangePanel());
          column.add(formValidationPanel());
          column.add(formAdaptersPanel());
          break;
        }
      case "gridSystem":
        {
          addPageHeader(column, "gridSystem", "Grid System", null);
          column.add(gridPanel());
          column.add(gridNestingPanel());
          column.add(gridBreakpointsPanel());
          break;
        }
      case "images":
        {
          addPageHeader(column, "images", "Images", null);
          Image rounded = new Image(IMG_THUMB);
          rounded.setType(ImageType.ROUNDED);
          Image circle = new Image(IMG_THUMB);
          circle.setType(ImageType.CIRCLE);
          Image thumbnail = new Image(IMG_THUMB);
          thumbnail.setType(ImageType.THUMBNAIL);
          column.add(
              panel(
                  "Basic",
                  inline(rounded, circle, thumbnail, new ImageAnchor("#", IMG_THUMB)),
                  "Image rounded = new Image(url);\nrounded.setType(ImageType.ROUNDED);\nimage.setType(ImageType.CIRCLE);\nimage.setType(ImageType.THUMBNAIL);"));
          column.add(imagesResponsivePanel());
          column.add(imagesFigurePanel());
          break;
        }
      case "responsiveUtilities":
        {
          addPageHeader(column, "responsiveUtilities", "Responsive Utilities", null);
          column.add(responsiveUtilitiesTablePanel());
          column.add(
              panel(
                  "Bootstrap 5 utilities",
                  new HTML(
                      "<p class='d-none d-md-block'>Visible on medium screens and wider.</p><p class='d-md-none'>Visible below medium screens.</p>"),
                  "Bootstrap 5 responsive display utilities: d-none, d-md-block, d-md-none."));
          break;
        }
      case "tables":
        {
          addPageHeader(column, "tables", "Tables", "plain markup and GWT cell widgets");
          column.add(tableBasicPanel());
          column.add(tableVariantsPanel());
          column.add(tableContextualPanel());
          column.add(tableResponsivePanel());
          column.add(cellTablePanel());
          break;
        }
      case "typography":
        {
          addPageHeader(column, "typography", "Typography", null);
          column.add(typographyHeadingsPanel());
          column.add(typographyBodyCopyPanel());
          column.add(typographyInlineElementsPanel());
          column.add(typographyAlignmentPanel());
          column.add(typographyBlockQuotePanel());
          column.add(typographyListsPanel());
          column.add(typographyPanel());
          break;
        }
      default:
        throw new IllegalArgumentException("Unknown section: " + token);
    }
    return row;
  }

  private Widget buttonsBasicPanel() {
    return panel(
        "Basic",
        inline(
            new Button("Default", ButtonType.DEFAULT),
            new Button("Primary", ButtonType.PRIMARY),
            new Button("Success", ButtonType.SUCCESS),
            new Button("Info", ButtonType.INFO),
            new Button("Warning", ButtonType.WARNING),
            new Button("Danger", ButtonType.DANGER),
            linkButton("Link")),
        "new Button(\"Primary\", ButtonType.PRIMARY);");
  }

  private Widget buttonSizesPanel() {
    Button medium = new Button("Default / medium", ButtonType.PRIMARY);
    Button small = new Button("Small", ButtonType.PRIMARY);
    small.setSmall(true);
    Button large = new Button("Large", ButtonType.PRIMARY);
    large.setLarge(true);
    return panel(
        "Sizes",
        inline(
            small,
            medium,
            large,
            note(
                "Bootstrap 5's medium size is the default button size; only small and large require modifier classes.")),
        "button.setSize(ButtonSize.SMALL);\nbutton.setSize(ButtonSize.DEFAULT);\nbutton.setSize(ButtonSize.LARGE);");
  }

  private Widget buttonStatesPanel() {
    Button enabled = new Button("Enabled", ButtonType.PRIMARY);
    Button disabled = new Button("Disabled", ButtonType.PRIMARY);
    disabled.setEnabled(false);
    Button toggle = new Button("Toggle button", Variant.PRIMARY);
    toggle.setOutline(true);
    toggle.setDataToggle(Toggle.BUTTON);
    Button loading = new Button("Click me", ButtonType.PRIMARY);
    loading.setLoadingText("Loading...");
    loading.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            loading.setLoading(true);
            new Timer() {
              @Override
              public void run() {
                loading.state().reset();
              }
            }.schedule(5000);
          }
        });
    Button block = new Button("Block level button", ButtonType.PRIMARY);
    block.setBlock(true);
    return panel(
        "States",
        inline(enabled, disabled, toggle, loading, block),
        "button.setDataToggle(Toggle.BUTTON);\nbutton.setActive(true);\nbutton.setLoadingText(\"Loading...\");\nbutton.state().loading();\nbutton.state().reset();\nbutton.toggle();\nbutton.setBlock(true);");
  }

  private Widget buttonCompositionPanel() {
    Button button = new Button("With icon and badge", ButtonType.PRIMARY);
    button.setIcon(IconType.STAR);
    button.setBadgeText("1");
    CheckBoxButton checkBoxButton = new CheckBoxButton("CheckBoxButton");
    checkBoxButton.setType(ButtonType.PRIMARY);
    checkBoxButton.setOutline(true);
    CheckBoxButton initiallyChecked = new CheckBoxButton("Initially checked");
    initiallyChecked.setType(ButtonType.PRIMARY);
    initiallyChecked.setOutline(true);
    initiallyChecked.setValue(true);
    CheckBoxButton disabled = new CheckBoxButton("Disabled");
    disabled.setType(ButtonType.PRIMARY);
    disabled.setOutline(true);
    disabled.setEnabled(false);
    RadioButton radioA = new RadioButton("button-choice", "Radio A");
    radioA.setType(ButtonType.PRIMARY);
    radioA.setOutline(true);
    RadioButton radioB = new RadioButton("button-choice", "Radio B");
    radioB.setType(ButtonType.PRIMARY);
    radioB.setOutline(true);
    return panel(
        "Composition and checkbox buttons",
        inline(button, checkBoxButton, initiallyChecked, disabled, radioA, radioB),
        "button.setIcon(IconType.STAR);\nbutton.setBadgeText(\"1\");\nCheckBoxButton checkBox = new CheckBoxButton(\"CheckBoxButton\");\ncheckBox.setOutline(true);\ncheckBox.setValue(true);\nRadioButton radio = new RadioButton(\"choice\", \"Radio A\");");
  }

  private Widget formsPanel() {
    Form form = new Form();
    FormGroup nameGroup = new FormGroup();
    nameGroup.add(new FormLabel("TextBox"));
    nameGroup.add(new TextBox());
    nameGroup.add(new HelpBlock("A HelpBlock renders as Bootstrap 5 form-text."));
    FormGroup textAreaGroup = new FormGroup();
    textAreaGroup.add(new FormLabel("TextArea"));
    textAreaGroup.add(new TextArea());
    FormGroup listBoxGroup = new FormGroup();
    listBoxGroup.add(new FormLabel("ListBox"));
    ListBox labelledListBox = new ListBox();
    labelledListBox.addItem("Choose an option");
    labelledListBox.addItem("Another option");
    listBoxGroup.add(labelledListBox);
    form.add(nameGroup);
    form.add(textAreaGroup);
    form.add(listBoxGroup);

    Div checks = new Div();
    checks.addStyleName("mb-3");
    checks.add(new CheckBox("CheckBox"));
    checks.add(new Radio("form-radio", "Radio"));
    form.add(checks);

    Div inlineChecks = new Div();
    inlineChecks.addStyleName("mb-3");
    inlineChecks.add(new InlineCheckBox("InlineCheckBox"));
    inlineChecks.add(new InlineRadio("form-inline-radio", "InlineRadio"));
    form.add(inlineChecks);

    form.add(simpleControlsGroup());
    form.add(new SubmitButton("SubmitButton"));
    return panel(
        "Basic",
        form,
        "new Form();\nnew TextBox();\nnew CheckBox(\"CheckBox\");\nnew Radio(\"group\", \"Radio\");\nnew InlineRadio(\"group\", \"InlineRadio\");\n\n// Bootstrap 5 renders the input and the label as siblings\n// inside .form-check, not the input nested in the label.");
  }

  private Widget simpleControlsGroup() {
    Div group = new Div();
    group.addStyleName("mb-3");
    group.add(new HTML("<div class='form-label'>SimpleCheckBox and SimpleRadioButton</div>"));

    Div row = new Div();
    row.addStyleName("d-flex flex-wrap align-items-center gap-2 column-gap-3");
    SimpleCheckBox simpleCheckBox = new SimpleCheckBox();
    simpleCheckBox.getElement().setAttribute("aria-label", "SimpleCheckBox");
    SimpleRadioButton simpleRadio = new SimpleRadioButton("simple-radio-group");
    simpleRadio.getElement().setAttribute("aria-label", "SimpleRadioButton");
    row.add(simpleCheckBox);
    row.add(new InlineHTML("SimpleCheckBox"));
    row.add(simpleRadio);
    row.add(new InlineHTML("SimpleRadioButton"));
    group.add(row);

    group.add(
        new HelpBlock(
            "These are the bare inputs, with no form-check wrapper or label of their own."));

    Div inlineHelpRow = new Div();
    inlineHelpRow.add(
        new InlineHTML(
            "<span class='form-text'>An InlineHelpBlock follows text on the same line:</span>"));
    inlineHelpRow.add(new InlineHelpBlock("and here it is."));
    group.add(inlineHelpRow);
    return group;
  }

  private Widget formValidationPanel() {
    final Form form = new Form();

    final FormGroup emailGroup = new FormGroup();
    emailGroup.add(new FormLabel("Email address"));
    final TextBox email = new TextBox();
    email.setPlaceholder("you@example.com");
    email.setAllowBlank(false);
    emailGroup.add(email);
    emailGroup.add(
        new HelpBlock("Required. The message replaces this text while the field is invalid."));
    form.add(emailGroup);

    final FormGroup countryGroup = new FormGroup();
    countryGroup.add(new FormLabel("Country"));
    final ValueListBox<String> country = new ValueListBox<String>();
    country.setValue("");
    country.setAcceptableValues(Arrays.asList("", "United Kingdom", "Ireland", "France"));
    country.setAllowBlank(false);
    countryGroup.add(country);
    form.add(countryGroup);

    final FormGroup cityGroup = new FormGroup();
    cityGroup.add(new FormLabel("City"));
    MultiWordSuggestOracle cities = new MultiWordSuggestOracle();
    cities.addAll(
        Arrays.asList(
            "Aberdeen",
            "Belfast",
            "Bristol",
            "Cardiff",
            "Dublin",
            "Edinburgh",
            "Glasgow",
            "Leeds",
            "Liverpool",
            "London",
            "Manchester",
            "Newcastle",
            "Norwich",
            "Oxford",
            "Sheffield",
            "York"));
    final SuggestBox city = new SuggestBox(cities);
    city.setPlaceholder("Start typing");
    city.setAllowBlank(false);
    cityGroup.add(city);
    form.add(cityGroup);

    Button validate = new Button("Validate", ButtonType.PRIMARY);
    validate.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            form.validate(true);
          }
        });
    Button reset = new Button("Reset", ButtonType.DEFAULT);
    reset.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            form.reset();
          }
        });
    form.add(inline(validate, reset));

    return panel(
        "Validation",
        form,
        "TextBox email = new TextBox();\nemail.setAllowBlank(false);\n\nSuggestBox city = new SuggestBox(oracle);\ncity.setAllowBlank(false);\n\nform.validate(true);\nform.reset();\n\n// Bootstrap 5 puts is-invalid on the control and shows the\n// message from a sibling invalid-feedback element. The error\n// handler creates one if the FormGroup has no HelpBlock.");
  }

  private Widget formAdaptersPanel() {
    IntegerBox integerBox = new IntegerBox();
    integerBox.getElement().setAttribute("placeholder", "IntegerBox");
    DoubleBox doubleBox = new DoubleBox();
    doubleBox.getElement().setAttribute("placeholder", "DoubleBox");
    LongBox longBox = new LongBox();
    longBox.getElement().setAttribute("placeholder", "LongBox");
    ListBox listBox = new ListBox();
    listBox.addItem("ListBox");
    ValueListBox<String> valueListBox = new ValueListBox<String>();
    valueListBox.setAcceptableValues(Arrays.asList("ValueListBox A", "ValueListBox B"));
    StringRadioGroup radioGroup = new StringRadioGroup("string-radio-group");
    radioGroup.addRadio("a", "StringRadioGroup A");
    radioGroup.addRadio("b", "StringRadioGroup B");
    return panel(
        "Value adapters",
        inline(integerBox, doubleBox, longBox, listBox, valueListBox, radioGroup),
        "new IntegerBox();\nnew DoubleBox();\nnew LongBox();\nnew ValueListBox<String>();\nnew StringRadioGroup(\"group\");");
  }

  private Widget gridPanel() {
    Row grid = row();
    Column left = new Column(12);
    left.setMediumSpan(6);
    left.add(new HTML("<div class='p-3 text-bg-primary rounded'>.col-md-6</div>"));
    Column right = new Column(12);
    right.setMediumSpan(6);
    right.add(new HTML("<div class='p-3 text-bg-secondary rounded'>.col-md-6</div>"));
    grid.add(left);
    grid.add(right);
    Row compatibilityGrid = row();
    Column stringSized = new Column("XS_12 MD_6");
    stringSized.add(
        new HTML("<div class='p-3 text-bg-success rounded'>new Column(\"XS_12 MD_6\")</div>"));
    Column offset = new Column(ColumnSize.XS_12);
    offset.addSize(ColumnSize.MD_3);
    offset.setOffset(ColumnOffset.MD_3);
    offset.add(new HTML("<div class='p-3 text-bg-warning rounded'>MD_3 with MD_3 offset</div>"));
    compatibilityGrid.add(stringSized);
    compatibilityGrid.add(offset);
    return panel(
        "Basic and Compatibility",
        stacked(grid, compatibilityGrid),
        "Column column = new Column(12);\ncolumn.setMediumSpan(6);\nnew Column(\"XS_12 MD_6\");\ncolumn.addSize(ColumnSize.MD_3);\ncolumn.setOffset(ColumnOffset.MD_3);");
  }

  private Widget typographyPanel() {
    Description description = new Description();
    description.add(new DescriptionTitle("DescriptionTitle"));
    description.add(new DescriptionData("DescriptionData"));
    FieldSet fieldSet = new FieldSet();
    fieldSet.add(new Legend("Legend"));
    fieldSet.add(new FormControlStatic("FormControlStatic"));
    BlockQuote quote = new BlockQuote();
    quote.add(new Paragraph("BlockQuote maps to Bootstrap 5 blockquote styling."));
    Abbreviation abbreviation = new Abbreviation("GWT");
    abbreviation.setTitle("Google Web Toolkit");
    return panel(
        "Basic",
        inline(
            new Heading(2, "Heading"),
            new Lead("Lead text"),
            new Paragraph("Paragraph text"),
            abbreviation,
            quote,
            description,
            fieldSet,
            new Caption("Caption helper")),
        "new Heading(2, \"Heading\");\nnew Lead(\"Lead text\");\nnew BlockQuote();");
  }

  private Widget linkButton(String text) {
    Anchor link = new Anchor(text, "#");
    link.addStyleName("btn btn-link");
    return link;
  }

  private Widget tableBasicPanel() {
    return panel(
        "Basic",
        new HTML(
            "<table class='table'>"
                + "<caption>A list of the widget libraries in this repository</caption>"
                + "<thead><tr><th scope='col'>#</th><th scope='col'>Module</th><th scope='col'>Backing CSS</th></tr></thead>"
                + "<tbody>"
                + "<tr><th scope='row'>1</th><td>gwt-bootstrap3</td><td>Bootstrap 3.4.1</td></tr>"
                + "<tr><th scope='row'>2</th><td>gwt-bootstrap5</td><td>Bootstrap 5.3.8</td></tr>"
                + "<tr><th scope='row'>3</th><td>teavm-bootstrap3</td><td>Bootstrap 3.4.1</td></tr>"
                + "</tbody></table>"),
        "<table class=\"table\">\n  <caption>...</caption>\n  <thead>...</thead>\n  <tbody>...</tbody>\n</table>");
  }

  private Widget tableVariantsPanel() {
    String head = "<thead><tr><th scope='col'>#</th><th scope='col'>Module</th></tr></thead>";
    String body =
        "<tbody><tr><th scope='row'>1</th><td>gwt-bootstrap3</td></tr>"
            + "<tr><th scope='row'>2</th><td>gwt-bootstrap5</td></tr></tbody>";
    String[] classes = {
      "table table-striped",
      "table table-bordered",
      "table table-hover",
      "table table-sm",
      "table table-dark",
      "table table-striped-columns"
    };
    String[] labels = {
      "table-striped",
      "table-bordered",
      "table-hover",
      "table-sm (was table-condensed)",
      "table-dark",
      "table-striped-columns (new in 5.3)"
    };
    PanelBody wrapper = new PanelBody();
    for (int i = 0; i < classes.length; i++) {
      wrapper.add(
          new HTML(
              "<h3 class='h6 text-body-secondary mt-3'><code>"
                  + labels[i]
                  + "</code></h3>"
                  + "<table class='"
                  + classes[i]
                  + "'>"
                  + head
                  + body
                  + "</table>"));
    }
    return panel(
        "Variants",
        wrapper,
        "// TableType.CONDENSED is Bootstrap 3 wording; Bootstrap 5 renames it table-sm.\n<table class=\"table table-striped\">...</table>");
  }

  private Widget tableContextualPanel() {
    String[] variants = {
      "primary", "secondary", "success", "danger", "warning", "info", "light", "dark"
    };
    StringBuilder rows = new StringBuilder();
    rows.append("<tr><td>default</td><td>No contextual class</td></tr>");
    for (String variant : variants) {
      rows.append("<tr class='table-")
          .append(variant)
          .append("'><td>table-")
          .append(variant)
          .append("</td><td>Contextual row</td></tr>");
    }
    return panel(
        "Contextual Classes",
        new HTML(
            "<table class='table'>"
                + "<thead><tr><th scope='col'>Class</th><th scope='col'>Meaning</th></tr></thead>"
                + "<tbody>"
                + rows
                + "</tbody></table>"),
        "// Bootstrap 3 used .active/.success/.info/.warning/.danger on rows.\n// Bootstrap 5 uses .table-* with the full theme palette.\n<tr class=\"table-success\">...</tr>");
  }

  private Widget tableResponsivePanel() {
    StringBuilder header = new StringBuilder();
    StringBuilder cells = new StringBuilder();
    for (int i = 1; i <= 12; i++) {
      header.append("<th scope='col'>Heading ").append(i).append("</th>");
      cells.append("<td>Cell ").append(i).append("</td>");
    }
    return panel(
        "Responsive",
        new HTML(
            "<div class='table-responsive'><table class='table table-bordered'>"
                + "<thead><tr>"
                + header
                + "</tr></thead><tbody><tr>"
                + cells
                + "</tr><tr>"
                + cells
                + "</tr></tbody></table></div>"),
        "<div class=\"table-responsive\">\n  <table class=\"table\">...</table>\n</div>");
  }

  private Widget cellTablePanel() {
    List<Release> releases =
        Arrays.asList(
            new Release("Bootstrap", "3.4.1", "2019-02-13"),
            new Release("Bootstrap", "5.3.8", "2025-08-19"),
            new Release("jQuery", "3.7.1", "2023-08-28"),
            new Release("GWT", "2.13.1", "2025-11-05"),
            new Release("TeaVM", "0.15.0", "2025-08-13"));

    CellTable<Release> table = new CellTable<Release>();
    table.addStyleName("table table-striped table-hover align-middle");
    table.setWidth("100%");
    table.setEmptyTableWidget(new HTML("<p class='text-body-secondary'>No releases.</p>"));

    TextColumn<Release> project =
        new TextColumn<Release>() {
          @Override
          public String getValue(Release release) {
            return release.project;
          }
        };
    project.setSortable(true);
    TextColumn<Release> version =
        new TextColumn<Release>() {
          @Override
          public String getValue(Release release) {
            return release.version;
          }
        };
    TextColumn<Release> released =
        new TextColumn<Release>() {
          @Override
          public String getValue(Release release) {
            return release.released;
          }
        };
    released.setSortable(true);
    table.addColumn(project, "Project");
    table.addColumn(version, "Version");
    table.addColumn(released, "Released");

    ListDataProvider<Release> provider =
        new ListDataProvider<Release>(new ArrayList<Release>(releases));
    provider.addDataDisplay(table);

    final List<Release> rows = provider.getList();
    ColumnSortEvent.ListHandler<Release> sortHandler =
        new ColumnSortEvent.ListHandler<Release>(rows);
    sortHandler.setComparator(
        project,
        new Comparator<Release>() {
          @Override
          public int compare(Release left, Release right) {
            return left.project.compareTo(right.project);
          }
        });
    sortHandler.setComparator(
        released,
        new Comparator<Release>() {
          @Override
          public int compare(Release left, Release right) {
            return left.released.compareTo(right.released);
          }
        });
    table.addColumnSortHandler(sortHandler);
    table.getColumnSortList().push(project);

    SingleSelectionModel<Release> selection = new SingleSelectionModel<Release>();
    table.setSelectionModel(selection);
    final HTML echo = new HTML("<p class='text-body-secondary mb-0'>Click a row to select it.</p>");
    selection.addSelectionChangeHandler(
        new SelectionChangeEvent.Handler() {
          @Override
          public void onSelectionChange(SelectionChangeEvent event) {
            Release selected =
                ((SingleSelectionModel<Release>) event.getSource()).getSelectedObject();
            echo.setHTML(
                selected == null
                    ? "<p class='text-body-secondary mb-0'>Nothing selected.</p>"
                    : "<p class='mb-0'>Selected <strong>"
                        + selected.project
                        + " "
                        + selected.version
                        + "</strong></p>");
          }
        });

    Div responsive = new Div();
    responsive.addStyleName("table-responsive");
    responsive.add(table);
    PanelBody body = new PanelBody();
    body.add(responsive);
    body.add(echo);
    return panel(
        "Cell Table",
        body,
        "CellTable<Release> table = new CellTable<Release>();\ntable.addStyleName(\"table table-striped table-hover\");\ntable.addColumn(project, \"Project\");\n\nListDataProvider<Release> provider = new ListDataProvider<Release>(releases);\nprovider.addDataDisplay(table);\ntable.addColumnSortHandler(sortHandler);\ntable.setSelectionModel(new SingleSelectionModel<Release>());");
  }

  private Widget typographyHeadingsPanel() {
    PanelBody body = new PanelBody();
    for (int level = 1; level <= 6; level++) {
      body.add(new Heading(level, "h" + level + ". Bootstrap heading"));
    }
    Heading withSub = new Heading(2, "h2. Heading ");
    withSub.add(
        new InlineHTML("<small class='text-body-secondary'>with muted secondary text</small>"));
    body.add(withSub);
    Heading withIcon = new Heading(3, "h3. Heading with icon ");
    withIcon.add(new Icon(IconType.STAR));
    body.add(withIcon);
    body.add(new HTML("<p class='display-4'>Display heading</p>"));
    return panel(
        "Headings",
        body,
        "new Heading(1, \"h1. Bootstrap heading\");\n\nHeading withSub = new Heading(2, \"h2. Heading \");\nwithSub.add(new InlineHTML(\"<small class='text-body-secondary'>...</small>\"));\n\n// Bootstrap 3's .text-muted becomes .text-body-secondary in 5.3.");
  }

  private Widget typographyBodyCopyPanel() {
    PanelBody body = new PanelBody();
    body.add(new Lead("Lead text stands out from the surrounding paragraphs."));
    body.add(
        new Paragraph(
            "A standard paragraph. Bootstrap 5 keeps a 1rem base font size and a 1.5 line height, "
                + "so blocks of running text render at a comfortable measure without further styling."));
    body.add(
        new Paragraph(
            "A second paragraph, so the default bottom margin between blocks is visible."));
    return panel(
        "Body Copy",
        body,
        "new Lead(\"Lead text...\");\nnew Paragraph(\"A standard paragraph.\");");
  }

  private Widget typographyInlineElementsPanel() {
    Abbreviation abbreviation = new Abbreviation("GWT");
    abbreviation.setTitle("Google Web Toolkit");
    PanelBody body = new PanelBody();
    body.add(
        new HTML(
            "<p>You can use the mark tag to <mark>highlight</mark> text.</p>"
                + "<p><del>This line of text is meant to be treated as deleted text.</del></p>"
                + "<p><s>This line of text is meant to be treated as no longer accurate.</s></p>"
                + "<p><ins>This line of text is meant to be treated as an addition.</ins></p>"
                + "<p><u>This line of text will render as underlined.</u></p>"
                + "<p><small>This line of text is meant to be treated as fine print.</small></p>"
                + "<p><strong>This line rendered as bold text.</strong></p>"
                + "<p><em>This line rendered as italicised text.</em></p>"));
    body.add(new Paragraph("Abbreviations carry a title attribute:"));
    body.add(inline(abbreviation, new Code("Code"), new Pre("Preformatted text")));
    return panel(
        "Inline Text Elements",
        body,
        "Abbreviation abbreviation = new Abbreviation(\"GWT\");\nabbreviation.setTitle(\"Google Web Toolkit\");\nnew Code(\"Code\");\nnew Pre(\"Preformatted text\");");
  }

  private Widget typographyAlignmentPanel() {
    return panel(
        "Alignment and Transform",
        new HTML(
            "<p class='text-start'>Start aligned text.</p>"
                + "<p class='text-center'>Centre aligned text.</p>"
                + "<p class='text-end'>End aligned text.</p>"
                + "<p class='text-lowercase'>LOWERCASED TEXT.</p>"
                + "<p class='text-uppercase'>Uppercased text.</p>"
                + "<p class='text-capitalize'>capitalised text.</p>"),
        "// Bootstrap 3 used text-left / text-right.\n// Bootstrap 5 is direction-aware: text-start / text-end.\n<p class=\"text-start\">...</p>");
  }

  private Widget typographyBlockQuotePanel() {
    BlockQuote quote = new BlockQuote();
    quote.add(new Paragraph("A well-known quote, contained in a blockquote element."));
    quote.add(
        new HTML(
            "<footer class='blockquote-footer'>Someone famous in <cite title='Source Title'>Source Title</cite></footer>"));
    BlockQuote reversed = new BlockQuote();
    reversed.addStyleName("text-end");
    reversed.add(new Paragraph("A quote aligned to the end of the line."));
    reversed.add(
        new HTML(
            "<footer class='blockquote-footer'>Someone famous in <cite title='Source Title'>Source Title</cite></footer>"));
    PanelBody body = new PanelBody();
    body.add(quote);
    body.add(reversed);
    return panel(
        "Blockquotes",
        body,
        "BlockQuote quote = new BlockQuote();\nquote.add(new Paragraph(\"...\"));\n\n// Bootstrap 3's .blockquote-reverse becomes .text-end in Bootstrap 5.\nreversed.addStyleName(\"text-end\");");
  }

  private Widget typographyListsPanel() {
    Description description = new Description();
    description.add(new DescriptionTitle("Description lists"));
    description.add(new DescriptionData("A description list is perfect for defining terms."));
    description.add(new DescriptionTitle("Horizontal"));
    description.add(
        new DescriptionData(
            "Bootstrap 5 builds horizontal description lists from grid classes rather than .dl-horizontal."));
    PanelBody body = new PanelBody();
    body.add(
        new HTML(
            "<ul><li>Unordered list item</li><li>Another item</li></ul>"
                + "<ol><li>Ordered list item</li><li>Another item</li></ol>"
                + "<ul class='list-unstyled'><li>Unstyled list item</li><li>Another item</li></ul>"
                + "<ul class='list-inline'><li class='list-inline-item'>Inline</li><li class='list-inline-item'>list</li><li class='list-inline-item'>items</li></ul>"));
    body.add(description);
    return panel(
        "Lists",
        body,
        "Description description = new Description();\ndescription.add(new DescriptionTitle(\"Term\"));\ndescription.add(new DescriptionData(\"Definition\"));\n\n// Bootstrap 5 marks inline list children with .list-inline-item.");
  }

  private Widget imagesResponsivePanel() {
    Image fluid = new Image(IMG_THUMB);
    fluid.addStyleName("img-fluid");
    Div box = new Div();
    box.addStyleName("border rounded p-2");
    box.add(fluid);
    return panel(
        "Responsive Images",
        box,
        "// Bootstrap 3 used .img-responsive.\nimage.addStyleName(\"img-fluid\");");
  }

  private Widget imagesFigurePanel() {
    return panel(
        "Figures",
        new HTML(
            "<figure class='figure'>"
                + "<img src=\""
                + IMG_THUMB
                + "\" class='figure-img img-fluid rounded' alt='Placeholder'/>"
                + "<figcaption class='figure-caption'>A caption for the image above.</figcaption></figure>"),
        "<figure class=\"figure\">\n  <img class=\"figure-img img-fluid rounded\" .../>\n  <figcaption class=\"figure-caption\">...</figcaption>\n</figure>");
  }

  private Widget gridBreakpointsPanel() {
    return panel(
        "Breakpoints",
        new HTML(
            "<div class='table-responsive'><table class='table table-sm'>"
                + "<thead><tr><th scope='col'>Infix</th><th scope='col'>Minimum width</th>"
                + "<th scope='col'>ColumnSize prefix</th></tr></thead><tbody>"
                + "<tr><td><em>none</em></td><td>0</td><td><code>XS_*</code></td></tr>"
                + "<tr><td><code>sm</code></td><td>576px</td><td><code>SM_*</code></td></tr>"
                + "<tr><td><code>md</code></td><td>768px</td><td><code>MD_*</code></td></tr>"
                + "<tr><td><code>lg</code></td><td>992px</td><td><code>LG_*</code></td></tr>"
                + "<tr><td><code>xl</code></td><td>1200px</td><td><code>XL_*</code></td></tr>"
                + "<tr><td><code>xxl</code></td><td>1400px</td><td>—</td></tr>"
                + "</tbody></table></div>"
                + "<p class='mb-0'>Bootstrap 3 stopped at <code>lg</code>. Bootstrap 5 adds <code>xxl</code>, and"
                + " renames the container query breakpoints; <code>ColumnSize.XS_*</code> still maps to the"
                + " infix-less <code>.col-*</code> classes.</p>"),
        "new Column(ColumnSize.XS_12);          // .col-12\ncolumn.addSize(ColumnSize.MD_6);       // .col-md-6");
  }

  private Widget gridNestingPanel() {
    Row outer = row();
    Column left = new Column(12);
    left.setMediumSpan(8);
    left.add(new HTML("<div class='p-3 text-bg-primary rounded mb-2'>.col-md-8</div>"));
    Row inner = row();
    for (int i = 0; i < 2; i++) {
      Column nested = new Column(6);
      nested.add(new HTML("<div class='p-3 text-bg-info rounded'>nested .col-6</div>"));
      inner.add(nested);
    }
    left.add(inner);
    Column right = new Column(12);
    right.setMediumSpan(4);
    right.add(new HTML("<div class='p-3 text-bg-secondary rounded'>.col-md-4</div>"));
    outer.add(left);
    outer.add(right);
    return panel(
        "Nesting",
        outer,
        "Column left = new Column(12);\nleft.setMediumSpan(8);\nleft.add(nestedRow);");
  }

  private Widget codeVariantsPanel() {
    return panel(
        "Variables, User Input and Sample Output",
        new HTML(
            "<p>Inline code: <code>&lt;section&gt;</code></p>"
                + "<p>A variable: <var>y</var> = <var>m</var><var>x</var> + <var>b</var></p>"
                + "<p>User input: press <kbd><kbd>ctrl</kbd> + <kbd>,</kbd></kbd> to open preferences.</p>"
                + "<p>Sample output: <samp>This text is meant to be treated as sample output.</samp></p>"
                + "<pre class='p-3 bg-body-tertiary rounded'><code>Row row = new Row();\nrow.add(new Column(12));</code></pre>"),
        "new Code(\"Code\");\nnew Pre(\"Preformatted text\");\n\n// var, kbd and samp have no widget; use HTML directly.");
  }

  private Widget responsiveUtilitiesTablePanel() {
    return panel(
        "Display Utilities",
        new HTML(
            "<div class='table-responsive'><table class='table table-sm'>"
                + "<thead><tr><th scope='col'>Bootstrap 3</th><th scope='col'>Bootstrap 5</th></tr></thead><tbody>"
                + "<tr><td><code>.hidden-xs</code></td><td><code>.d-none .d-sm-block</code></td></tr>"
                + "<tr><td><code>.visible-xs</code></td><td><code>.d-block .d-sm-none</code></td></tr>"
                + "<tr><td><code>.hidden-md</code></td><td><code>.d-md-none .d-lg-block</code></td></tr>"
                + "<tr><td><code>.visible-print-block</code></td><td><code>.d-print-block</code></td></tr>"
                + "<tr><td><code>.pull-left</code></td><td><code>.float-start</code></td></tr>"
                + "<tr><td><code>.pull-right</code></td><td><code>.float-end</code></td></tr>"
                + "<tr><td><code>.center-block</code></td><td><code>.mx-auto .d-block</code></td></tr>"
                + "</tbody></table></div>"),
        "// Responsiveness and DeviceSize are kept for source compatibility\n// and emit the Bootstrap 5 d-* utilities.");
  }

  private Widget rangePanel() {
    final HTML echo = new HTML("<p class='text-body-secondary mb-0'>Volume: 50</p>");

    FormGroup group = new FormGroup();
    group.add(new FormLabel("Volume"));
    final Range volume = new Range(0, 100);
    volume.setValue(50d);
    volume.setContinuous(true);
    volume.addValueChangeHandler(
        new ValueChangeHandler<Double>() {
          @Override
          public void onValueChange(ValueChangeEvent<Double> event) {
            echo.setHTML(
                "<p class='mb-0'>Volume: <strong>"
                    + (long) event.getValue().doubleValue()
                    + "</strong></p>");
          }
        });
    group.add(volume);

    FormGroup stepped = new FormGroup();
    stepped.add(new FormLabel("Stepped, 0 to 10 in twos"));
    Range steps = new Range(0, 10);
    steps.setStep(2);
    steps.setValue(4d);
    stepped.add(steps);

    FormGroup disabled = new FormGroup();
    disabled.add(new FormLabel("Disabled"));
    Range off = new Range();
    off.setValue(30d);
    off.setEnabled(false);
    disabled.add(off);

    PanelBody body = new PanelBody();
    body.add(group);
    body.add(echo);
    body.add(stepped);
    body.add(disabled);
    return panel(
        "Range",
        body,
        "Range volume = new Range(0, 100);\nvolume.setValue(50d);\nvolume.setContinuous(true);   // report while dragging\nvolume.addValueChangeHandler(event -> { ... });\n\nRange steps = new Range(0, 10);\nsteps.setStep(2);\n\n// Bootstrap 3 wrapped bootstrap-slider, a jQuery plugin.\n// Bootstrap 5 styles the native input with .form-range, so\n// for a single value there is nothing to ship.");
  }

  private Widget note(String text) {
    return new HTML("<span class='text-body-secondary'>" + text + "</span>");
  }

  private static class Release {
    private final String project;
    private final String version;
    private final String released;

    Release(String project, String version, String released) {
      this.project = project;
      this.version = version;
      this.released = released;
    }
  }
}
