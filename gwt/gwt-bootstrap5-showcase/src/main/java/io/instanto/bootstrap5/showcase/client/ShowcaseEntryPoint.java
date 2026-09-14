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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import io.instanto.bootstrap5.extras.markdown.client.ui.MarkdownPanel;
import io.instanto.bootstrap5.extras.markdown.client.ui.MarkdownEditor;
import io.instanto.bootstrap5.extras.slider.client.ui.Slider;
import io.instanto.bootstrap5.client.ui.Range;
import io.instanto.bootstrap5.extras.richtext.client.ui.RichTextEditor;
import io.instanto.bootstrap5.client.ui.Dialogs;
import io.instanto.bootstrap5.extras.datepicker.client.ui.DatePicker;
import com.google.gwt.i18n.client.DateTimeFormat;
import io.instanto.bootstrap5.client.ui.Affix;
import io.instanto.bootstrap5.client.ui.ScrollSpy;
import com.google.gwt.core.client.GWT;
import io.instanto.bootstrap5.client.ui.theme.StandardThemes;
import io.instanto.bootstrap5.client.ui.theme.ColorModes;
import io.instanto.bootstrap5.client.ui.theme.ThemeSwitcher;
import io.instanto.bootstrap5.client.ui.theme.Themes;
import io.instanto.bootstrap5.themes.client.BootswatchThemes;
import io.instanto.bootstrap5.client.ui.NavbarText;
import io.instanto.bootstrap5.client.ui.constants.ListGroupItemType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import io.instanto.bootstrap5.client.shared.event.ModalHiddenEvent;
import io.instanto.bootstrap5.client.shared.event.ModalHiddenHandler;
import io.instanto.bootstrap5.client.shared.event.ModalHideEvent;
import io.instanto.bootstrap5.client.shared.event.ModalHideHandler;
import io.instanto.bootstrap5.client.shared.event.ModalShowEvent;
import io.instanto.bootstrap5.client.shared.event.ModalShowHandler;
import io.instanto.bootstrap5.client.shared.event.ModalShownEvent;
import io.instanto.bootstrap5.client.shared.event.ModalShownHandler;
import io.instanto.bootstrap5.client.shared.event.TabShowEvent;
import io.instanto.bootstrap5.client.shared.event.TabShowHandler;
import io.instanto.bootstrap5.client.shared.event.TabShownEvent;
import io.instanto.bootstrap5.client.shared.event.TabShownHandler;
import io.instanto.bootstrap5.client.ui.ModalSize;
import io.instanto.bootstrap5.client.ui.IconStack;
import io.instanto.bootstrap5.client.ui.html.Div;
import io.instanto.bootstrap5.client.ui.constants.Emphasis;
import io.instanto.bootstrap5.client.ui.constants.IconFlip;
import io.instanto.bootstrap5.client.ui.constants.IconRotate;
import io.instanto.bootstrap5.client.ui.constants.Placement;
import io.instanto.bootstrap5.client.ui.constants.Trigger;
import io.instanto.bootstrap5.client.ui.Abbreviation;
import io.instanto.bootstrap5.client.ui.Alert;
import io.instanto.bootstrap5.client.ui.Anchor;
import io.instanto.bootstrap5.client.ui.AnchorButton;
import io.instanto.bootstrap5.client.ui.AnchorListItem;
import io.instanto.bootstrap5.client.ui.Badge;
import io.instanto.bootstrap5.client.ui.BlockQuote;
import io.instanto.bootstrap5.client.ui.Breadcrumbs;
import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.client.ui.ButtonGroup;
import io.instanto.bootstrap5.client.ui.ButtonToolBar;
import io.instanto.bootstrap5.client.ui.Caption;
import io.instanto.bootstrap5.client.ui.Carousel;
import io.instanto.bootstrap5.client.ui.CarouselCaption;
import io.instanto.bootstrap5.client.ui.CarouselControl;
import io.instanto.bootstrap5.client.ui.CarouselIndicator;
import io.instanto.bootstrap5.client.ui.CarouselIndicators;
import io.instanto.bootstrap5.client.ui.CarouselSlide;
import io.instanto.bootstrap5.client.ui.CheckBox;
import io.instanto.bootstrap5.client.ui.CheckBoxButton;
import io.instanto.bootstrap5.client.ui.Code;
import io.instanto.bootstrap5.client.ui.Collapse;
import io.instanto.bootstrap5.client.ui.Column;
import io.instanto.bootstrap5.client.ui.Container;
import io.instanto.bootstrap5.client.ui.Description;
import io.instanto.bootstrap5.client.ui.DescriptionData;
import io.instanto.bootstrap5.client.ui.DescriptionTitle;
import io.instanto.bootstrap5.client.ui.Divider;
import io.instanto.bootstrap5.client.ui.DoubleBox;
import io.instanto.bootstrap5.client.ui.DropDown;
import io.instanto.bootstrap5.client.ui.DropDownHeader;
import io.instanto.bootstrap5.client.ui.DropDownItem;
import io.instanto.bootstrap5.client.ui.FieldSet;
import io.instanto.bootstrap5.client.ui.Form;
import io.instanto.bootstrap5.client.ui.FormControlStatic;
import io.instanto.bootstrap5.client.ui.FormGroup;
import io.instanto.bootstrap5.client.ui.FormLabel;
import io.instanto.bootstrap5.client.ui.HelpBlock;
import io.instanto.bootstrap5.client.ui.Heading;
import io.instanto.bootstrap5.client.ui.Icon;
import io.instanto.bootstrap5.client.ui.Image;
import io.instanto.bootstrap5.client.ui.ImageAnchor;
import io.instanto.bootstrap5.client.ui.InlineCheckBox;
import io.instanto.bootstrap5.client.ui.InlineHelpBlock;
import io.instanto.bootstrap5.client.ui.InlineRadio;
import io.instanto.bootstrap5.client.ui.Input;
import io.instanto.bootstrap5.client.ui.InputGroup;
import io.instanto.bootstrap5.client.ui.InputGroupAddon;
import io.instanto.bootstrap5.client.ui.InputGroupButton;
import io.instanto.bootstrap5.client.ui.IntegerBox;
import io.instanto.bootstrap5.client.ui.Jumbotron;
import io.instanto.bootstrap5.client.ui.Label;
import io.instanto.bootstrap5.client.ui.Lead;
import io.instanto.bootstrap5.client.ui.Legend;
import io.instanto.bootstrap5.client.ui.LinkedGroup;
import io.instanto.bootstrap5.client.ui.LinkedGroupItem;
import io.instanto.bootstrap5.client.ui.LinkedGroupItemText;
import io.instanto.bootstrap5.client.ui.ListBox;
import io.instanto.bootstrap5.client.ui.ListDropDown;
import io.instanto.bootstrap5.client.ui.ListGroup;
import io.instanto.bootstrap5.client.ui.ListGroupItem;
import io.instanto.bootstrap5.client.ui.ListItem;
import io.instanto.bootstrap5.client.ui.LongBox;
import io.instanto.bootstrap5.client.ui.MediaBody;
import io.instanto.bootstrap5.client.ui.MediaList;
import io.instanto.bootstrap5.client.ui.Modal;
import io.instanto.bootstrap5.client.ui.ModalFooter;
import io.instanto.bootstrap5.client.ui.NavPills;
import io.instanto.bootstrap5.client.ui.NavTabs;
import io.instanto.bootstrap5.client.ui.Navbar;
import io.instanto.bootstrap5.client.ui.NavbarBrand;
import io.instanto.bootstrap5.client.ui.NavbarButton;
import io.instanto.bootstrap5.client.ui.NavbarCollapse;
import io.instanto.bootstrap5.client.ui.NavbarCollapseButton;
import io.instanto.bootstrap5.client.ui.NavbarForm;
import io.instanto.bootstrap5.client.ui.NavbarHeader;
import io.instanto.bootstrap5.client.ui.NavbarLink;
import io.instanto.bootstrap5.client.ui.PageHeader;
import io.instanto.bootstrap5.client.ui.Pager;
import io.instanto.bootstrap5.client.ui.Pagination;
import io.instanto.bootstrap5.client.ui.Panel;
import io.instanto.bootstrap5.client.ui.PanelBody;
import io.instanto.bootstrap5.client.ui.PanelCollapse;
import io.instanto.bootstrap5.client.ui.PanelFooter;
import io.instanto.bootstrap5.client.ui.PanelGroup;
import io.instanto.bootstrap5.client.ui.PanelHeader;
import io.instanto.bootstrap5.client.ui.Paragraph;
import io.instanto.bootstrap5.client.ui.Popover;
import io.instanto.bootstrap5.client.ui.Pre;
import io.instanto.bootstrap5.client.ui.Progress;
import io.instanto.bootstrap5.client.ui.ProgressBar;
import io.instanto.bootstrap5.client.ui.Radio;
import io.instanto.bootstrap5.client.ui.RadioButton;
import io.instanto.bootstrap5.client.ui.Row;
import io.instanto.bootstrap5.client.ui.SimpleCheckBox;
import io.instanto.bootstrap5.client.ui.SimpleRadioButton;
import io.instanto.bootstrap5.client.ui.StringRadioGroup;
import io.instanto.bootstrap5.client.ui.SubmitButton;
import io.instanto.bootstrap5.client.ui.SuggestBox;
import io.instanto.bootstrap5.client.ui.TabContent;
import io.instanto.bootstrap5.client.ui.TabListItem;
import io.instanto.bootstrap5.client.ui.TabPane;
import io.instanto.bootstrap5.client.ui.TabPanel;
import io.instanto.bootstrap5.client.ui.TextArea;
import io.instanto.bootstrap5.client.ui.TextBox;
import io.instanto.bootstrap5.client.ui.ThumbnailLink;
import io.instanto.bootstrap5.client.ui.ThumbnailPanel;
import io.instanto.bootstrap5.client.ui.Tooltip;
import io.instanto.bootstrap5.client.ui.TooltipHelpBlock;
import io.instanto.bootstrap5.client.ui.ValueListBox;
import io.instanto.bootstrap5.client.ui.Variant;
import io.instanto.bootstrap5.client.ui.constants.AlertType;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;
import io.instanto.bootstrap5.client.ui.constants.ColumnOffset;
import io.instanto.bootstrap5.client.ui.constants.ColumnSize;
import io.instanto.bootstrap5.client.ui.constants.IconSize;
import io.instanto.bootstrap5.client.ui.constants.IconType;
import io.instanto.bootstrap5.client.ui.constants.ImageType;
import io.instanto.bootstrap5.client.ui.constants.InputGroupSize;
import io.instanto.bootstrap5.client.ui.constants.LabelType;
import io.instanto.bootstrap5.client.ui.constants.PaginationSize;
import io.instanto.bootstrap5.client.ui.constants.PanelType;
import io.instanto.bootstrap5.client.ui.constants.ProgressBarType;
import io.instanto.bootstrap5.client.ui.constants.ProgressType;
import io.instanto.bootstrap5.client.ui.constants.Toggle;
import io.instanto.bootstrap5.client.ui.VerticalButtonGroup;
import io.instanto.bootstrap5.client.ui.Well;

public class ShowcaseEntryPoint implements EntryPoint {

    private final java.util.function.Supplier<Widget> setupFactory;

    public ShowcaseEntryPoint() {
        this(SetupView::new);
    }

    public ShowcaseEntryPoint(java.util.function.Supplier<Widget> setupFactory) {
        this.setupFactory = setupFactory;
    }

    private static final String IMG_WIDE = "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='640' height='240'%3E%3Crect width='100%25' height='100%25' fill='rgb(13,110,253)'/%3E%3Ctext x='50%25' y='50%25' fill='white' text-anchor='middle' dominant-baseline='middle' font-family='sans-serif' font-size='32'%3EBootstrap 5%3C/text%3E%3C/svg%3E";
    private static final String IMG_THUMB = "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='320' height='180'%3E%3Crect width='100%25' height='100%25' fill='rgb(25,135,84)'/%3E%3Ctext x='50%25' y='50%25' fill='white' text-anchor='middle' dominant-baseline='middle' font-family='sans-serif' font-size='24'%3EThumbnail%3C/text%3E%3C/svg%3E";
    private static final String[] CSS_SECTIONS = {"buttons", "code", "forms", "gridSystem", "images", "responsiveUtilities", "tables", "typography"};
    private static final String[] CSS_LABELS = {"Buttons", "Code", "Forms", "Grid System", "Images", "Responsive Utilities", "Tables", "Typography"};
    private static final String[] COMPONENT_SECTIONS = {"cards", "placeholders", "alerts", "badges", "breadcrumbs", "buttonDropdowns", "buttonGroups", "dropdowns", "icons", "inputGroups", "jumbotron", "labels", "listGroup", "mediaObjects", "navbar", "navs", "pageHeader", "pagination", "panels", "progressBars", "suggestBox", "thumbnails", "wells"};
    private static final String[] COMPONENT_LABELS = {"Cards", "Placeholders", "Alerts", "Badges", "Breadcrumbs", "Button Dropdowns", "Button Groups", "Dropdowns", "Icons", "Input Groups", "Jumbotron", "Labels", "List Group", "Media Objects", "Navbar", "Navs", "Page Header", "Pagination", "Panels", "Progress Bars", "SuggestBox", "Thumbnails", "Wells"};
    private static final String[] JS_SECTIONS = {"toasts", "offcanvas", "dialogs", "affix", "carousel", "collapse", "modals", "popover", "scrollspy", "tabs", "tooltips"};
    private static final String[] JS_LABELS = {"Toasts", "Offcanvas", "Dialogs", "Affix", "Carousel", "Collapse", "Modals", "Popover", "ScrollSpy", "Tabs", "Tooltips"};
    private static final String[] INTEGRATION_SECTIONS = {"datePicker", "richText", "markdown", "slider", "searchableSelect", "dataGrid", "sortableLists", "dashboard", "gallery", "unsupportedExtras"};
    private static final String[] INTEGRATION_LABELS = {"DatePicker", "Rich Text", "Markdown", "Slider", "Searchable Select", "Data Grid", "Sortable Lists", "Dashboard", "Gallery", "About Integrations"};

    static {
        // The showcase inherits GwtBootstrap5NoTheme, so nothing else claims the
        // stylesheet: the switcher owns it. Stock Bootstrap first, so it heads the
        // menu and is what an unrecognised or absent stored choice falls back to.
        Themes.register(StandardThemes.all());
        Themes.register(BootswatchThemes.all());
        Themes.restore(StandardThemes.bootstrap(GWT.getModuleBaseURL() + "css/"));
    }

    @Override
    public void onModuleLoad() {
        RootPanel root = RootPanel.get("showcase");
        if (root == null) {
            root = RootPanel.get();
        }

        root.add(createNavbar());

        Container container = new Container();
        container.addStyleName("gbm-showcase");
        root.add(container);

        final RootPanel modalRoot = root;
        final Container content = container;
        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                renderToken(content, modalRoot, event.getValue());
            }
        });
        renderToken(content, modalRoot, History.getToken());
    }

    /**
     * Demo modals have to live outside the content container, so clearing the
     * container does not remove them. Without this they accumulate: every visit
     * to the JavaScript section left another set behind.
     */
    private final List<Modal> pageModals = new ArrayList<Modal>();

    private void renderToken(Container content, RootPanel root, String token) {
        for (Modal modal : pageModals) {
            modal.removeFromParent();
        }
        pageModals.clear();
        content.clear();
        content.add(createPage(root, normalizeToken(token)));
    }

    private String normalizeToken(String token) {
        return token == null || token.isEmpty() ? "home" : token;
    }

    private Widget createPage(RootPanel root, String token) {
        if ("home".equals(token)) {
            return createHome();
        }
        if ("setup".equals(token)) {
            return createSetup();
        }
        if ("uiBinder".equals(token)) {
            return panel("UiBinder", new UiBinderDemo(),
                    "@UiField Button counter;\n@UiHandler(\"counter\")\nvoid onCounterClick(ClickEvent event) { ... }");
        }
        if (contains(CSS_SECTIONS, token)) {
            Row row = createCssSections();
            filterSections(row, token);
            return row;
        }
        if (contains(COMPONENT_SECTIONS, token)) {
            Row row = createComponentSections();
            filterSections(row, token);
            return row;
        }
        if (contains(JS_SECTIONS, token)) {
            Row row = createJavaScriptSections(root);
            filterSections(row, token);
            return row;
        }
        if (contains(INTEGRATION_SECTIONS, token)) {
            Row row = createIntegrationSections();
            filterSections(row, token);
            return row;
        }
        return createHome();
    }

    private boolean contains(String[] values, String value) {
        for (String candidate : values) {
            if (candidate.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private void filterSections(Row row, String token) {
        for (Widget rowChild : row) {
            if (!(rowChild instanceof HasWidgets)) {
                continue;
            }
            boolean visible = false;
            for (Widget sectionChild : (HasWidgets) rowChild) {
                if (sectionChild instanceof PageHeader) {
                    visible = token.equals(sectionChild.getElement().getAttribute("data-section"));
                }
                sectionChild.setVisible(visible);
            }
        }
    }

    private Navbar createNavbar() {
        Navbar navbar = new Navbar();
        navbar.addStyleName("sticky-top");
        // Navbar colour variables require a local mode, not just the document's mode.
        navbar.getElement().setAttribute(ColorModes.ATTRIBUTE, ColorModes.isDark() ? "dark" : "light");
        ColorModes.addColorModeChangeHandler(mode -> navbar.getElement().setAttribute(
                ColorModes.ATTRIBUTE, ColorModes.isDark() ? "dark" : "light"));
        NavbarCollapseButton navbarCollapseButton = new NavbarCollapseButton("navbar-collapse");
        NavbarCollapse navbarCollapse = new NavbarCollapse();
        navbarCollapse.getElement().setId("navbar-collapse");
        navbar.getContainer().add(new NavbarBrand("Bootstrap", "#home"));
        navbar.getContainer().add(navbarCollapseButton);
        navbarCollapse.add(navbar.getNav());
        navbar.getContainer().add(navbarCollapse);
        navbar.getNav().add(new NavbarLink("Setup", "#setup"));
        navbar.getNav().add(new NavbarLink("UiBinder", "#uiBinder"));
        navbar.getNav().add(dropdown("CSS", CSS_LABELS, CSS_SECTIONS));
        navbar.getNav().add(dropdown("Components", COMPONENT_LABELS, COMPONENT_SECTIONS));
        navbar.getNav().add(dropdown("Interactive", JS_LABELS, JS_SECTIONS));
        navbar.getNav().add(dropdown("Integrations", INTEGRATION_LABELS, INTEGRATION_SECTIONS));
        String docsBase = "teavm".equals(GWT.getModuleName()) ? "bootstrap5/" : "";
        navbar.getNav().add(dropdown("View Javadoc",
                new String[] {"Core API", "Integrations API"},
                new String[] {docsBase + "apidocs/index.html", docsBase + "extras-apidocs/index.html"}));
        boolean teaVm = "teavm".equals(GWT.getModuleName());
        navbar.getNav().add(dropdown("Other Builds",
                new String[] {"Bootstrap 3 Showcase (GWT)", "Bootstrap 3 Showcase (TeaVM)",
                        teaVm ? "Bootstrap 5 Showcase (GWT)" : "Bootstrap 5 Showcase (TeaVM)"},
                teaVm ? new String[] {"./", "./teavm.html", "bootstrap5/"}
                        : new String[] {"../", "../teavm.html", "../teavm-bootstrap5.html"}));
        navbar.getNav().add(new NavbarLink("Fork on GitHub", "https://github.com/cstainton/bootstrap-widgets"));
        navbar.getNav().add(new ThemeSwitcher());
        return navbar;
    }

    private ListDropDown dropdown(String text, String[] labels, String[] hrefs) {
        ListDropDown dropDown = new ListDropDown(text);
        for (int i = 0; i < labels.length; i++) {
            String href = hrefs[i].startsWith("http") || hrefs[i].contains("/") ? hrefs[i] : "#" + hrefs[i];
            dropDown.addItem(new DropDownItem(labels[i], href));
        }
        return dropDown;
    }

    private Widget createHome() {
        Row row = row();
        Column column = fullColumn();
        Jumbotron jumbotron = new Jumbotron();
        jumbotron.getElement().setId("home");
        jumbotron.add(new Heading(1, "Bootstrap Showcase"));
        jumbotron.add(new Paragraph("Explore Bootstrap components, layouts and interactive behaviour."));
        jumbotron.add(new Paragraph("Components and Interactive cover native Bootstrap capabilities. Integrations adds editors, advanced selection, data grids, sortable lists, dashboards and image galleries. Each page includes working examples and Java code."));
        column.add(jumbotron);
        row.add(column);
        return row;
    }

    private Widget createSetup() {
        Row row = section("setup", "Setup", "Application setup");
        Column column = fullColumn(row);
        column.add(setupFactory.get());
        column.add(panel("UiBinder", uiBinderProbe(), uiBinderSource()));
        return row;
    }

    private Row createCssSections() {
        Row row = row();
        Column column = fullColumn(row);
        addPageHeader(column, "buttons", "Buttons", "styled, states, icons...");
        column.add(buttonsBasicPanel());
        column.add(buttonSizesPanel());
        column.add(buttonStatesPanel());
        column.add(buttonCompositionPanel());

        addPageHeader(column, "code", "Code", null);
        column.add(codeVariantsPanel());
        column.add(panel("Inline and block code", inline(new Code("Code"), new Pre("Preformatted text")), "new Code(\"Code\");\nnew Pre(\"Preformatted text\");"));

        addPageHeader(column, "forms", "Forms", null);
        column.add(formsPanel());
        column.add(rangePanel());
        column.add(formValidationPanel());
        column.add(formAdaptersPanel());

        addPageHeader(column, "gridSystem", "Grid System", null);
        column.add(gridPanel());
        column.add(gridNestingPanel());
        column.add(gridBreakpointsPanel());

        addPageHeader(column, "images", "Images", null);
        Image rounded = new Image(IMG_THUMB);
        rounded.setType(ImageType.ROUNDED);
        Image circle = new Image(IMG_THUMB);
        circle.setType(ImageType.CIRCLE);
        Image thumbnail = new Image(IMG_THUMB);
        thumbnail.setType(ImageType.THUMBNAIL);
        column.add(panel("Basic", inline(rounded, circle, thumbnail, new ImageAnchor("#", IMG_THUMB)), "Image rounded = new Image(url);\nrounded.setType(ImageType.ROUNDED);\nimage.setType(ImageType.CIRCLE);\nimage.setType(ImageType.THUMBNAIL);"));
        column.add(imagesResponsivePanel());
        column.add(imagesFigurePanel());

        addPageHeader(column, "responsiveUtilities", "Responsive Utilities", null);
        column.add(responsiveUtilitiesTablePanel());
        column.add(panel("Bootstrap 5 utilities", new HTML("<p class='d-none d-md-block'>Visible on medium screens and wider.</p><p class='d-md-none'>Visible below medium screens.</p>"), "Bootstrap 5 responsive display utilities: d-none, d-md-block, d-md-none."));

        addPageHeader(column, "tables", "Tables", "plain markup and GWT cell widgets");
        column.add(tableBasicPanel());
        column.add(tableVariantsPanel());
        column.add(tableContextualPanel());
        column.add(tableResponsivePanel());
        column.add(cellTablePanel());

        addPageHeader(column, "typography", "Typography", null);
        column.add(typographyHeadingsPanel());
        column.add(typographyBodyCopyPanel());
        column.add(typographyInlineElementsPanel());
        column.add(typographyAlignmentPanel());
        column.add(typographyBlockQuotePanel());
        column.add(typographyListsPanel());
        column.add(typographyPanel());
        return row;
    }

    private Row createComponentSections() {
        Row row = row();
        Column column = fullColumn(row);
        addPageHeader(column, "cards", "Cards", null);
        column.add(cardHeaderFooterPanel());
        column.add(cardVariantsPanel());
        column.add(panel("Bootstrap 5 native", sampleCard(), "Card card = new Card();"));
        addPageHeader(column, "placeholders", "Placeholders", "loading skeletons");
        column.add(panel("Static, glow and wave", NativeComponentsExamples.placeholders(),
                "PlaceholderContainer skeleton = new PlaceholderContainer();\nskeleton.setAnimation(PlaceholderContainer.Animation.GLOW);\nskeleton.add(new Placeholder(8));"));

        addPageHeader(column, "alerts", "Alerts", null);
        column.add(new AlertsBasicView());
        column.add(alertDismissiblePanel());
        column.add(alertLinksPanel());

        addPageHeader(column, "badges", "Badges", null);
        column.add(new BadgesView());
        column.add(labelBadgeMigrationPanel());

        addPageHeader(column, "breadcrumbs", "Breadcrumbs", null);
        column.add(breadcrumbsPanel());

        addPageHeader(column, "buttonDropdowns", "Button Dropdowns", null);
        column.add(buttonDropdownBasicPanel());
        column.add(buttonDropdownSplitPanel());
        column.add(buttonDropdownDirectionsPanel());
        column.add(buttonDropdownSizesPanel());
        column.add(buttonDropdownMenuContentPanel());

        addPageHeader(column, "buttonGroups", "Button Groups", null);
        column.add(buttonGroupsPanel());
        column.add(buttonGroupToolbarPanel());

        addPageHeader(column, "dropdowns", "Dropdowns", null);
        column.add(dropdownsPanel());

        addPageHeader(column, "icons", "Icons", "Bootstrap Icons replace Font Awesome");
        column.add(iconTypePanel());
        column.add(iconBasicPanel());
        column.add(iconSizesPanel());
        column.add(iconEmphasisPanel());
        column.add(iconAnimationPanel());
        column.add(iconRotateFlipPanel());
        column.add(iconStackPanel());
        column.add(iconFixedWidthPanel());
        column.add(iconGalleryPanel());

        addPageHeader(column, "inputGroups", "Input Groups", null);
        column.add(inputGroupsPanel());
        column.add(inputGroupVariantsPanel());

        addPageHeader(column, "jumbotron", "Jumbotron", null);
        column.add(jumbotronPanel());

        addPageHeader(column, "labels", "Labels", null);
        column.add(labelsPanel());
        column.add(labelBadgeMigrationPanel());

        addPageHeader(column, "listGroup", "List Group", null);
        column.add(listGroupPanel());
        column.add(listGroupContextualPanel());
        column.add(listGroupLinkedPanel());

        addPageHeader(column, "mediaObjects", "Media Objects", null);
        column.add(mediaPanel());
        column.add(mediaFlexPanel());

        addPageHeader(column, "navbar", "Navbar", null);
        column.add(navbarPanel());
        column.add(navbarCollapsePanel());
        column.add(navbarDarkPanel());

        addPageHeader(column, "navs", "Navs", null);
        column.add(navsPanel());
        column.add(navStackedPanel());
        column.add(navJustifiedPanel());
        column.add(navDropdownPanel());
        column.add(navIconsPanel());

        addPageHeader(column, "pageHeader", "Page Header", null);
        PageHeader pageHeader = new PageHeader();
        pageHeader.add(new Heading(2, "Page Header"));
        column.add(panel("Basic", pageHeader, "PageHeader pageHeader = new PageHeader();"));
        column.add(pageHeaderVariantsPanel());

        addPageHeader(column, "pagination", "Pagination", null);
        column.add(paginationPanel());
        column.add(pagerPanel());

        addPageHeader(column, "panels", "Panels", null);
        column.add(panel("Basic", basicPanelExample(), "Panel panel = new Panel();\npanel.add(new PanelHeader(\"Panel Header\"));\npanel.add(new PanelBody());\npanel.add(new PanelFooter(\"Panel Footer\"));"));
        column.add(panel("Contextual Classes", inline(panelExample(PanelType.INFO), panelExample(PanelType.DANGER), panelExample(PanelType.SUCCESS)), "new Panel(PanelType.INFO);\nnew Panel(PanelType.DANGER);\nnew Panel(PanelType.SUCCESS);"));

        addPageHeader(column, "progressBars", "Progress Bars", null);
        column.add(progressBasicPanel());
        column.add(progressStripedPanel());
        column.add(progressAnimatedPanel());
        column.add(progressStackedPanel());

        addPageHeader(column, "suggestBox", "SuggestBox", null);
        column.add(suggestBoxPanel());

        addPageHeader(column, "thumbnails", "Thumbnails", null);
        column.add(thumbnailsPanel());
        column.add(thumbnailLinksPanel());
        column.add(thumbnailCustomContentPanel());

        addPageHeader(column, "wells", "Wells", null);
        column.add(wellsPanel());
        column.add(wellMigrationPanel());
        return row;
    }

    private Row createJavaScriptSections(RootPanel root) {
        Row row = row();
        Column column = fullColumn(row);
        addPageHeader(column, "toasts", "Toasts", "notifications and independent stacks");
        column.add(panel("Show, dismiss and auto-hide", NativeComponentsExamples.toasts(),
                "ToastContainer notifications = new ToastContainer();\n// Mount the live region before sending a notification.\nnotifications.notify(\"Saved\", \"Your changes were saved.\", 5000);"));
        addPageHeader(column, "offcanvas", "Offcanvas", "sliding detail and filter panels");
        column.add(panel("Placement, backdrop and keyboard", NativeComponentsExamples.offcanvas(),
                "Offcanvas panel = new Offcanvas();\npanel.setPlacement(OffcanvasPlacement.END);\npanel.add(new OffcanvasHeader(\"Details\"));\npanel.add(new OffcanvasBody(\"Panel content\"));\n// Mount, then show from a button's click handler.\npanel.show();"));
        addPageHeader(column, "dialogs", "Dialogs", "alert, confirm and prompt");
        column.add(dialogsPanel());

        addPageHeader(column, "affix", "Affix", "sticky positioning");
        column.add(affixPanel());

        addPageHeader(column, "carousel", "Carousel", null);
        column.add(panel("Basic", carousel(), "Carousel carousel = new Carousel();\ncarousel.addSlide(...);"));
        column.add(carouselCaptionsPanel());
        column.add(carouselOptionsPanel());

        addPageHeader(column, "collapse", "Collapse", null);
        column.add(collapsePanel());
        column.add(accordionPanel());

        addPageHeader(column, "modals", "Modals", null);
        column.add(modalPanel(root));
        column.add(modalSizesPanel(root));
        column.add(modalOnlyOnePanel(root));
        column.add(modalEventsPanel(root));

        addPageHeader(column, "popover", "Popover", null);
        column.add(popoverPlacementsPanel());
        column.add(popoverTriggersPanel());
        column.add(popoverOptionsPanel());

        addPageHeader(column, "scrollspy", "ScrollSpy", null);
        column.add(scrollSpyPanel());

        addPageHeader(column, "tabs", "Tabs", null);
        column.add(tabsPanel());
        column.add(tabsFadePanel());
        column.add(tabsPillsPanel());
        column.add(tabsEventsPanel());

        addPageHeader(column, "tooltips", "Tooltips", null);
        column.add(tooltipPlacementsPanel());
        column.add(tooltipTriggersPanel());
        column.add(tooltipOptionsPanel());
        return row;
    }

    private Row createIntegrationSections() {
        Row row = row();
        Column column = fullColumn(row);

        addPageHeader(column, "datePicker", "DatePicker", "Tempus Dominus 6");
        column.add(datePickerPanel());

        addPageHeader(column, "richText", "Rich Text", "Quill 2");
        column.add(richTextPanel());

        addPageHeader(column, "markdown", "Markdown", "for applications that store Markdown");
        column.add(markdownPanel());

        addPageHeader(column, "slider", "Slider", "noUiSlider 15");
        column.add(sliderPanel());

        addPageHeader(column, "searchableSelect", "Searchable Select", "search, tags and asynchronous options");
        column.add(panel("Selection and tags", IntegrationExamples.selects(),
                "SearchableSelect tags = new SearchableSelect();\ntags.setMultiple(true);\ntags.setAllowCreate(true);\ntags.addOption(\"java\", \"Java\");\ntags.addValueChangeHandler(event -> { /* event.getValue() */ });"));
        addPageHeader(column, "dataGrid", "Data Grid", "editing, grouping and paging");
        column.add(panel("Editable team directory", IntegrationExamples.grid(),
                "DataTable table = new DataTable();\ntable.setColumns(columns);\ntable.setRows(rows);\ntable.setGroupBy(\"team\");\ntable.setPageSize(5);"));
        addPageHeader(column, "sortableLists", "Sortable Lists", "drag cards between columns");
        column.add(panel("Work board", IntegrationExamples.sortable(),
                "SortableList todo = new SortableList();\ntodo.setGroup(\"work\");\ntodo.add(new Paragraph(\"Review changes\"));\ntodo.addValueChangeHandler(event -> { /* ordered child widgets */ });"));
        addPageHeader(column, "dashboard", "Dashboard", "move and resize tiles");
        column.add(panel("Custom layout", IntegrationExamples.dashboard(),
                "Dashboard dashboard = new Dashboard();\nDashboardTile tile = new DashboardTile(\"summary\", 0, 0, 6, 2);\ntile.add(new Paragraph(\"Summary\"));\ndashboard.add(tile);\n// dashboard.getLayoutJson() stores positions, not widget contents."));
        addPageHeader(column, "gallery", "Gallery", "open an image, zoom or swipe");
        column.add(panel("Image viewer", IntegrationExamples.gallery(),
                "ImageGallery gallery = new ImageGallery();\ngallery.addImage(\"landscape.svg\", \"landscape.svg\", 960, 640, \"Landscape\");"));
        addPageHeader(column, "unsupportedExtras", "About Integrations", "optional components");
        column.add(panel("Loading integrations", new HTML(
                "<p>Each integration bundles its own scripts and styles. Enable only the modules your application uses. "
                + "Widgets release plugin listeners when removed, and can be attached again.</p>"),
                "<inherits name=\"io.instanto.bootstrap5.extras.select.Select\"/>"));
        return row;
    }

    private Widget buttonsBasicPanel() {
        return panel("Basic", inline(new Button("Default", ButtonType.DEFAULT), new Button("Primary", ButtonType.PRIMARY), new Button("Success", ButtonType.SUCCESS), new Button("Info", ButtonType.INFO), new Button("Warning", ButtonType.WARNING), new Button("Danger", ButtonType.DANGER), linkButton("Link")), "new Button(\"Primary\", ButtonType.PRIMARY);" );
    }

    private Widget buttonSizesPanel() {
        Button medium = new Button("Default / medium", ButtonType.PRIMARY);
        Button small = new Button("Small", ButtonType.PRIMARY);
        small.setSmall(true);
        Button large = new Button("Large", ButtonType.PRIMARY);
        large.setLarge(true);
        return panel("Sizes", inline(small, medium, large, note("Bootstrap 5's medium size is the default button size; only small and large require modifier classes.")), "button.setSize(ButtonSize.SMALL);\nbutton.setSize(ButtonSize.DEFAULT);\nbutton.setSize(ButtonSize.LARGE);" );
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
        loading.addClickHandler(new ClickHandler() {
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
        return panel("States", inline(enabled, disabled, toggle, loading, block), "button.setDataToggle(Toggle.BUTTON);\nbutton.setActive(true);\nbutton.setLoadingText(\"Loading...\");\nbutton.state().loading();\nbutton.state().reset();\nbutton.toggle();\nbutton.setBlock(true);" );
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
        return panel("Composition and checkbox buttons", inline(button, checkBoxButton, initiallyChecked, disabled,
                radioA, radioB), "button.setIcon(IconType.STAR);\nbutton.setBadgeText(\"1\");\nCheckBoxButton checkBox = new CheckBoxButton(\"CheckBoxButton\");\ncheckBox.setOutline(true);\ncheckBox.setValue(true);\nRadioButton radio = new RadioButton(\"choice\", \"Radio A\");" );
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
        return panel("Basic", form,
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

        group.add(new HelpBlock("These are the bare inputs, with no form-check wrapper or label of their own."));

        Div inlineHelpRow = new Div();
        inlineHelpRow.add(new InlineHTML("<span class='form-text'>An InlineHelpBlock follows text on the same line:</span>"));
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
        emailGroup.add(new HelpBlock("Required. The message replaces this text while the field is invalid."));
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
        cities.addAll(Arrays.asList("Aberdeen", "Belfast", "Bristol", "Cardiff", "Dublin", "Edinburgh", "Glasgow",
                "Leeds", "Liverpool", "London", "Manchester", "Newcastle", "Norwich", "Oxford", "Sheffield", "York"));
        final SuggestBox city = new SuggestBox(cities);
        city.setPlaceholder("Start typing");
        city.setAllowBlank(false);
        cityGroup.add(city);
        form.add(cityGroup);

        Button validate = new Button("Validate", ButtonType.PRIMARY);
        validate.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                form.validate(true);
            }
        });
        Button reset = new Button("Reset", ButtonType.DEFAULT);
        reset.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                form.reset();
            }
        });
        form.add(inline(validate, reset));

        return panel("Validation", form,
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
        return panel("Value adapters", inline(integerBox, doubleBox, longBox, listBox, valueListBox, radioGroup), "new IntegerBox();\nnew DoubleBox();\nnew LongBox();\nnew ValueListBox<String>();\nnew StringRadioGroup(\"group\");");
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
        stringSized.add(new HTML("<div class='p-3 text-bg-success rounded'>new Column(\"XS_12 MD_6\")</div>"));
        Column offset = new Column(ColumnSize.XS_12);
        offset.addSize(ColumnSize.MD_3);
        offset.setOffset(ColumnOffset.MD_3);
        offset.add(new HTML("<div class='p-3 text-bg-warning rounded'>MD_3 with MD_3 offset</div>"));
        compatibilityGrid.add(stringSized);
        compatibilityGrid.add(offset);
        return panel("Basic and Compatibility", stacked(grid, compatibilityGrid), "Column column = new Column(12);\ncolumn.setMediumSpan(6);\nnew Column(\"XS_12 MD_6\");\ncolumn.addSize(ColumnSize.MD_3);\ncolumn.setOffset(ColumnOffset.MD_3);");
    }

    private Widget alertsBasicPanel() {
        return panel("Basic", stacked(
                alert(AlertType.SUCCESS),
                alert(AlertType.INFO),
                alert(AlertType.WARNING),
                alert(AlertType.DANGER)),
                "new Alert(\"Title Description\", AlertType.SUCCESS);\nnew Alert(\"Title Description\", AlertType.INFO);\nnew Alert(\"Title Description\", AlertType.WARNING);\nnew Alert(\"Title Description\", AlertType.DANGER);");
    }

    private Widget alertDismissiblePanel() {
        Alert alert = alert(AlertType.SUCCESS);
        alert.setDismissible(true);
        return panel("Dismissible with Handlers", alert, "Alert alert = new Alert(\"Title Description\", AlertType.SUCCESS);\nalert.setDismissible(true);\nalert.addCloseHandler(...);\nalert.addClosedHandler(...);");
    }

    private Widget alertLinksPanel() {
        Alert alert = alert(AlertType.SUCCESS);
        alert.setDismissible(true);
        alert.add(new Anchor("Link", "#"));
        return panel("Links Inside", alert, "Alert alert = new Alert(\"Title Description\", AlertType.SUCCESS);\nalert.setDismissible(true);\nalert.add(new Anchor(\"Link\", \"#\"));");
    }

    private Alert alert(AlertType type) {
        Alert alert = new Alert("", type);
        alert.add(new HTML("<strong>Title</strong> Description"));
        return alert;
    }

    private Widget badgesPanel() {
        Badge primary = new Badge("Primary", LabelType.PRIMARY);
        Badge success = new Badge("Success", LabelType.SUCCESS);
        Badge warning = new Badge("Warning", LabelType.WARNING);
        Badge danger = new Badge("Danger", LabelType.DANGER);
        Badge pill = new Badge("Pill badge", LabelType.SUCCESS);
        pill.setPill(true);

        Anchor inbox = new Anchor("Inbox", "#");
        inbox.setBadgeText("12");

        return panel("Variants, pills and links", stacked(
                inline(primary, success, warning, danger, pill),
                inline(new Heading(4, "Heading with badge "), new Badge("New", LabelType.PRIMARY)),
                inbox),
                "new Badge(\"Primary\", LabelType.PRIMARY);\nbadge.setPill(true);\nanchor.setBadgeText(\"12\");");
    }

    private Widget breadcrumbsPanel() {
        Breadcrumbs basic = new Breadcrumbs(
                new AnchorListItem("Home", "#home"),
                new ListItem("Current"));

        Breadcrumbs nested = new Breadcrumbs(
                new AnchorListItem("Projects", "#home"),
                new AnchorListItem("GWT Bootstrap", "#setup"),
                new ListItem("Bootstrap 5"));
        nested.getElement().setAttribute("aria-label", "breadcrumb");

        return panel("Basic and multi-level", stacked(basic, nested),
                "new Breadcrumbs(new AnchorListItem(\"Home\", \"#home\"), new ListItem(\"Current\"));");
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
        return panel("Basic", inline(new Heading(2, "Heading"), new Lead("Lead text"), new Paragraph("Paragraph text"), abbreviation, quote, description, fieldSet, new Caption("Caption helper")), "new Heading(2, \"Heading\");\nnew Lead(\"Lead text\");\nnew BlockQuote();");
    }

    private Widget buttonGroupsPanel() {
        ButtonGroup group = new ButtonGroup();
        group.addButton(new Button("Button 1", ButtonType.DEFAULT));
        group.addButton(new Button("Button 2", ButtonType.DEFAULT));
        group.addButton(new Button("Button 3", ButtonType.DEFAULT));
        ButtonGroup large = new ButtonGroup();
        large.setLarge(true);
        large.addButton(new Button("Left", ButtonType.DEFAULT));
        large.addButton(new Button("Middle", ButtonType.DEFAULT));
        large.addButton(new Button("Right", ButtonType.DEFAULT));
        ButtonGroup small = new ButtonGroup();
        small.setSmall(true);
        small.addButton(new Button("Left", ButtonType.DEFAULT));
        small.addButton(new Button("Middle", ButtonType.DEFAULT));
        small.addButton(new Button("Right", ButtonType.DEFAULT));
        VerticalButtonGroup vertical = new VerticalButtonGroup();
        vertical.addButton(new Button("Top", ButtonType.DEFAULT));
        vertical.addButton(new Button("Bottom", ButtonType.DEFAULT));
        ButtonToolBar toolbar = new ButtonToolBar();
        toolbar.addGroup(group);
        return panel("Basic and Sizing", stacked(inline(toolbar, vertical), inline(large, small)), "ButtonGroup group = new ButtonGroup();\ngroup.addButton(new Button(\"Button 1\"));\nlarge.setLarge(true);\nsmall.setSmall(true);\nButtonToolBar toolbar = new ButtonToolBar();");
    }

    private Widget dropdownsPanel() {
        DropDown dropDown = new DropDown("Click to toggle dropdown");
        dropDown.setMenuMatchToggleWidth(true);
        dropDown.addMenuWidget(new DropDownHeader("Header 1"));
        DropDownItem first = new DropDownItem("Action 1", "#");
        first.add(new Icon(IconType.CAMERA));
        dropDown.addItem(first);
        dropDown.addMenuWidget(new Divider());
        DropDownItem disabled = new DropDownItem("Action 2 (disabled)", "#");
        disabled.setDisabled(true);
        dropDown.addItem(disabled);
        DropDown endAligned = new DropDown("End aligned");
        endAligned.setMenuEndAligned(true);
        endAligned.setMenuMatchToggleWidth(true);
        endAligned.addItem(new DropDownItem("Action", "#"));
        endAligned.addItem(new DropDownItem("Another action", "#"));
        DropDown dropUp = new DropDown("Dropup");
        dropUp.setDropUp(true);
        dropUp.setMenuMatchToggleWidth(true);
        dropUp.addItem(new DropDownItem("Action", "#"));
        dropUp.addItem(new DropDownItem("Another action", "#"));
        return panel("Basic", inline(dropDown, endAligned, dropUp), "DropDown dropDown = new DropDown(\"Click to toggle dropdown\");\ndropDown.addItem(new DropDownItem(\"Action\", \"#\"));\ndropDown.setMenuEndAligned(true);\ndropDown.setDropUp(true);\ndropDown.setMenuMatchToggleWidth(true);");
    }

    private Widget inputGroupsPanel() {
        InputGroup inputGroup = new InputGroup();
        inputGroup.add(new InputGroupAddon("@"));
        Input input = new Input("text");
        input.setPlaceholder("Username");
        inputGroup.add(input);
        inputGroup.add(new InputGroupButton(new Button("Go", ButtonType.PRIMARY)));
        InputGroup large = new InputGroup();
        large.setSize(InputGroupSize.LARGE);
        large.add(new InputGroupAddon("Large"));
        Input largeInput = new Input("text");
        largeInput.setPlaceholder("InputGroupSize.LARGE");
        large.add(largeInput);
        InputGroup small = new InputGroup();
        small.setSmall(true);
        small.add(new InputGroupAddon("Small"));
        Input smallInput = new Input("text");
        smallInput.setPlaceholder("setSmall(true)");
        small.add(smallInput);
        return panel("Basic and Sizing", stacked(inputGroup, large, small), "InputGroup inputGroup = new InputGroup();\ninputGroup.add(new InputGroupAddon(\"@\"));\ninputGroup.setSize(InputGroupSize.LARGE);\ninputGroup.setSmall(true);");
    }

    private Widget listGroupPanel() {
        ListGroup group = new ListGroup();
        group.add(new ListGroupItem("Plain list-group-item"));
        ListGroupItem active = new ListGroupItem("Active item");
        active.setActive(true);
        group.add(active);
        LinkedGroup linked = new LinkedGroup();
        LinkedGroupItem linkedItem = new LinkedGroupItem("LinkedGroupItem", "#");
        linkedItem.add(new LinkedGroupItemText("Linked item body text"));
        linked.add(linkedItem);
        return panel("Basic", inline(group, linked), "new ListGroup();\nnew LinkedGroup();");
    }

    private Widget mediaPanel() {
        MediaList mediaList = new MediaList();
        MediaBody mediaBody = new MediaBody();
        mediaBody.add(new Heading(4, "Media heading"));
        mediaBody.add(new Paragraph("Bootstrap 5 uses flex utilities for the old media object pattern."));
        mediaList.add(new Image(IMG_THUMB));
        mediaList.add(mediaBody);
        return panel("Basic", mediaList, "MediaList mediaList = new MediaList();\nmediaList.add(new MediaBody());");
    }

    private Widget navbarPanel() {
        Navbar navbar = new Navbar();
        navbar.getContainer().add(new NavbarBrand("Brand", "#"));
        navbar.getNav().add(new NavbarLink("Link", "#"));
        NavbarHeader header = new NavbarHeader();
        header.add(new NavbarButton("NavbarButton"));
        NavbarForm form = new NavbarForm();
        Input input = new Input("search");
        input.setPlaceholder("NavbarForm");
        form.add(input);
        navbar.getContainer().add(header);
        navbar.getContainer().add(form);
        return panel("Basic", navbar, "Navbar navbar = new Navbar();\nnavbar.getContainer().add(new NavbarBrand(...));");
    }

    private Widget navsPanel() {
        NavTabs tabs = new NavTabs();
        tabs.addLink("Home", "#").addStyleName("active");
        tabs.addLink("Profile", "#");
        NavPills pills = new NavPills();
        pills.addLink("Home", "#").addStyleName("active");
        pills.addLink("Profile", "#");
        return panel("Tabs and pills", inline(tabs, pills), "new NavTabs();\nnew NavPills();");
    }

    private Widget paginationPanel() {
        Pagination pagination = pagination(PaginationSize.NONE);
        Pagination small = pagination(PaginationSize.SMALL);
        Pagination large = pagination(PaginationSize.LARGE);
        return panel("Basic", stacked(pagination, small, large), "Pagination pagination = new Pagination();\npagination.setPaginationSize(PaginationSize.SMALL);\npagination.setPaginationSize(PaginationSize.LARGE);");
    }

    private Widget pagerPanel() {
        Pager pager = new Pager();
        Pager custom = new Pager();
        custom.setPreviousText("Older");
        custom.setNextText("Newer");
        Pager aligned = new Pager();
        aligned.setPreviousText("Older");
        aligned.setNextText("Newer");
        aligned.setAlignToSides(true);
        Pager iconPager = new Pager();
        iconPager.setPreviousIcon(IconType.CHEVRON_LEFT);
        iconPager.setNextIcon(IconType.CHEVRON_RIGHT);
        iconPager.setPreviousIconSize(IconSize.LARGE);
        iconPager.setNextIconSize(IconSize.LARGE);
        return panel("Pager", stacked(pager, custom, aligned, iconPager), "new Pager();\npager.setPreviousText(\"Older\");\npager.setNextText(\"Newer\");\npager.setAlignToSides(true);\npager.setPreviousIcon(IconType.CHEVRON_LEFT);\npager.setNextIcon(IconType.CHEVRON_RIGHT);");
    }

    private Pagination pagination(PaginationSize size) {
        Pagination pagination = new Pagination();
        pagination.setPaginationSize(size);
        pagination.addPreviousLink();
        AnchorListItem active = new AnchorListItem("1", "#");
        active.setActive(true);
        pagination.add(active);
        pagination.add(new AnchorListItem("2", "#"));
        pagination.addNextLink();
        return pagination;
    }

    private Widget progressBasicPanel() {
        return panel("Basic", stacked(
                progress(40, ProgressBarType.SUCCESS),
                progress(20, ProgressBarType.INFO),
                progress(60, ProgressBarType.WARNING),
                progress(80, ProgressBarType.DANGER)),
                "new ProgressBar(40, ProgressBarType.SUCCESS);\nnew ProgressBar(20, ProgressBarType.INFO);\nnew ProgressBar(60, ProgressBarType.WARNING);\nnew ProgressBar(80, ProgressBarType.DANGER);");
    }

    private Widget progressStripedPanel() {
        return panel("Striped", stacked(
                stripedProgress(40, ProgressBarType.SUCCESS),
                stripedProgress(20, ProgressBarType.INFO),
                stripedProgress(60, ProgressBarType.WARNING),
                stripedProgress(80, ProgressBarType.DANGER)),
                "Progress progress = new Progress();\nprogress.setType(ProgressType.STRIPED);");
    }

    private Widget progressAnimatedPanel() {
        Progress progress = stripedProgress(40, ProgressBarType.SUCCESS);
        progress.setActive(true);
        return panel("Animated", stacked(
                new Paragraph("The diagonal stripes move continuously unless the device requests reduced motion."),
                progress),
                "Progress progress = new Progress();\nprogress.setType(ProgressType.STRIPED);\nprogress.setActive(true);");
    }

    private Widget progressStackedPanel() {
        Progress progress = new Progress();
        ProgressBar success = new ProgressBar();
        success.setPercent(35);
        success.setType(ProgressBarType.SUCCESS);
        ProgressBar warning = new ProgressBar();
        warning.setPercent(20);
        warning.setType(ProgressBarType.WARNING);
        ProgressBar danger = new ProgressBar();
        danger.setPercent(10);
        danger.setType(ProgressBarType.DANGER);
        progress.add(success);
        progress.add(warning);
        progress.add(danger);
        return panel("Stacked", progress, "Progress progress = new Progress();\nprogress.add(new ProgressBar(...));\nprogress.add(new ProgressBar(...));");
    }

    private Progress progress(int percent, ProgressBarType type) {
        Progress progress = new Progress();
        ProgressBar bar = new ProgressBar();
        bar.setPercent(percent);
        bar.setText(percent + "%");
        bar.setType(type);
        progress.add(bar);
        return progress;
    }

    private Progress stripedProgress(int percent, ProgressBarType type) {
        Progress progress = progress(percent, type);
        progress.setType(ProgressType.STRIPED);
        return progress;
    }

    private Widget thumbnailsPanel() {
        ThumbnailPanel panel = new ThumbnailPanel();
        panel.addBody(new Image(IMG_THUMB));
        panel.addBody(new Paragraph("ThumbnailPanel maps to Card."));
        ThumbnailLink link = new ThumbnailLink("#");
        link.add(new Image(IMG_THUMB));
        return panel("Basic", inline(panel, link), "new ThumbnailPanel();\nnew ThumbnailLink(\"#\");");
    }

    private Widget labelsPanel() {
        return panel("Basic", inline(
                new Label("Default", LabelType.DEFAULT),
                new Label("Primary", LabelType.PRIMARY),
                new Label("Success", LabelType.SUCCESS),
                new Label("Info", LabelType.INFO),
                new Label("Warning", LabelType.WARNING),
                new Label("Danger", LabelType.DANGER)),
                "new Label(\"Primary\", LabelType.PRIMARY);");
    }

    private Widget wellsPanel() {
        Well standard = new Well();
        standard.add(new HTML("<span>Look, I am in a well!</span>"));
        Well large = new Well();
        large.addStyleName("p-5");
        large.add(new HTML("<span>Large well mapping</span>"));
        Well small = new Well();
        small.addStyleName("p-2");
        small.add(new HTML("<span>Small well mapping</span>"));
        return panel("Default and Optional Classes", stacked(standard, large, small), "new Well();\nwell.addStyleName(\"p-5\");\nwell.addStyleName(\"p-2\");");
    }

    private Widget basicPanelExample() {
        Panel panel = new Panel();
        panel.add(new PanelHeader("Panel Header"));
        PanelBody body = new PanelBody();
        body.add(new HTML("<strong>Panel Body</strong>"));
        panel.add(body);
        panel.add(new PanelFooter("Panel Footer"));
        return panel;
    }

    private Widget panelExample(PanelType type) {
        Panel panel = new Panel(type);
        panel.add(new PanelHeader("Panel Header"));
        PanelBody body = new PanelBody();
        body.add(new HTML("<strong>Panel Body</strong>"));
        panel.add(body);
        panel.add(new PanelFooter("Panel Footer"));
        return panel;
    }

    private Widget collapsePanel() {
        Collapse collapse = new Collapse();
        collapse.getElement().setId("collapseExample");
        collapse.add(new Well());
        Button button = new Button("Toggle collapse", ButtonType.PRIMARY);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                collapse.toggle();
            }
        });
        return panel("Basic", inline(button, collapse), "Collapse collapse = new Collapse();\ncollapse.toggle();");
    }

    private Widget accordionPanel() {
        PanelGroup accordion = new PanelGroup();
        accordion.getElement().setId("accordion");
        accordion.add(accordionItem("collapseOne", "Collapse Group #1", true));
        accordion.add(accordionItem("collapseTwo", "Collapse Group #2", false));
        accordion.add(accordionItem("collapseThree", "Collapse Group #3", false));
        return panel("Accordion Example using PanelCollapse", accordion, "PanelGroup accordion = new PanelGroup();\naccordion.add(panelWithPanelCollapse(...));");
    }

    private Widget accordionItem(String id, String title, boolean open) {
        Panel panel = new Panel();
        PanelHeader header = new PanelHeader();
        Anchor anchor = new Anchor(title, "#" + id);
        anchor.setDataToggle("collapse");
        anchor.getElement().setAttribute("data-bs-target", "#" + id);
        header.add(anchor);
        panel.add(header);
        PanelCollapse collapse = new PanelCollapse();
        collapse.getElement().setId(id);
        collapse.setIn(open);
        // Bootstrap 5 reads data-bs-parent from the collapsing element, not the toggle.
        collapse.getElement().setAttribute("data-bs-parent", "#accordion");
        PanelBody body = new PanelBody();
        body.add(new Paragraph("I am the content of " + title + "."));
        collapse.add(body);
        panel.add(collapse);
        return panel;
    }

    private Widget modalPanel(RootPanel root) {
        Modal modal = new Modal();
        modal.getElement().setId("exampleModal");
        modal.setTitle("Cupcake ipsum");
        modal.addToBody(new Paragraph("Modal body..."));
        ModalFooter footer = new ModalFooter();
        footer.add(new Button("Do something", ButtonType.PRIMARY));
        Button close = new Button("Close", ButtonType.DEFAULT);
        close.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                modal.hide();
            }
        });
        footer.add(close);
        modal.addFooter(footer);
        root.add(modal);
        pageModals.add(modal);
        Button show = new Button("Click to show modal", ButtonType.PRIMARY);
        show.setLarge(true);
        show.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                modal.show();
            }
        });
        return panel("Basic", show, "Modal modal = new Modal();\nmodal.show();\nmodal.hide();");
    }

    private Widget tabsPanel() {
        TabPanel tabPanel = new TabPanel();
        TabListItem firstTab = new TabListItem("First", "firstPane");
        firstTab.setActive(true);
        tabPanel.getTabs().add(firstTab);
        tabPanel.getTabs().add(new TabListItem("Second", "secondPane"));
        TabPane firstPane = new TabPane();
        firstPane.getElement().setId("firstPane");
        firstPane.setActive(true);
        firstPane.add(new Paragraph("First tab content"));
        TabPane secondPane = new TabPane();
        secondPane.getElement().setId("secondPane");
        secondPane.add(new Paragraph("Second tab content"));
        TabContent content = tabPanel.getContent();
        content.add(firstPane);
        content.add(secondPane);
        return panel("Basic", tabPanel, "TabPanel tabPanel = new TabPanel();\ntabPanel.getTabs().add(new TabListItem(...));");
    }

    private Widget carousel() {
        String carouselId = "showcaseCarousel";
        Carousel carousel = new Carousel();
        carousel.getElement().setId(carouselId);
        CarouselIndicators indicators = new CarouselIndicators();
        CarouselIndicator firstIndicator = new CarouselIndicator(carouselId, 0);
        firstIndicator.setActive(true);
        indicators.addIndicator(firstIndicator);
        indicators.addIndicator(new CarouselIndicator(carouselId, 1));
        carousel.insert(indicators, 0);
        CarouselSlide firstSlide = new CarouselSlide(new HTML("<div class='d-flex align-items-center justify-content-center text-bg-primary rounded' style='height: 12rem;'>First slide</div>"));
        firstSlide.setActive(true);
        CarouselCaption caption = new CarouselCaption();
        caption.add(new Heading(5, "First slide"));
        firstSlide.add(caption);
        carousel.addSlide(firstSlide);
        carousel.addSlide(new CarouselSlide(new HTML("<div class='d-flex align-items-center justify-content-center text-bg-success rounded' style='height: 12rem;'>Second slide</div>")));
        carousel.add(new CarouselControl(carouselId, true));
        carousel.add(new CarouselControl(carouselId, false));
        return carousel;
    }

    private Widget samplePanel() {
        Panel panel = new Panel(PanelType.PRIMARY);
        panel.add(new PanelHeader("Panel heading"));
        PanelBody body = new PanelBody();
        body.add(new Paragraph("Panel body"));
        panel.add(body);
        panel.add(new PanelFooter("Panel footer"));
        PanelGroup group = new PanelGroup();
        PanelCollapse collapse = new PanelCollapse();
        collapse.add(new Paragraph("PanelCollapse wrapper"));
        group.add(collapse);
        panel.add(group);
        return panel;
    }

    private Widget sampleCard() {
        io.instanto.bootstrap5.client.ui.Card card = new io.instanto.bootstrap5.client.ui.Card();
        card.setTitle("Card title");
        card.addBody(new Paragraph("Bootstrap 5 card content."));
        card.addBody(new AnchorButton("Card action", "#", ButtonType.PRIMARY));
        return card;
    }

    private Widget linkButton(String text) {
        Anchor link = new Anchor(text, "#");
        link.addStyleName("btn btn-link");
        return link;
    }

    private Row section(String id, String title, String subText) {
        Row row = row();
        Column column = fullColumn(row);
        addPageHeader(column, id, title, subText);
        return row;
    }

    private Row row() {
        Row row = new Row();
        row.setStyleName("row");
        return row;
    }

    private Column fullColumn() {
        Column column = new Column(12);
        return column;
    }

    private Column fullColumn(Row row) {
        Column column = fullColumn();
        row.add(column);
        return column;
    }

    private void addPageHeader(Column column, String id, String title, String subText) {
        PageHeader header = new PageHeader();
        header.getElement().setAttribute("data-section", id);
        Heading heading = new Heading(2, title);
        if (subText != null && !subText.isEmpty()) {
            heading.add(new InlineHTML(" <small class='text-body-secondary'>" + subText + "</small>"));
        }
        header.add(heading);
        column.add(header);
    }

    private static final IconType[] ICON_GALLERY = {IconType.ARCHIVE, IconType.ARROW_DOWN, IconType.ARROW_LEFT,
            IconType.ARROW_RIGHT, IconType.ARROW_UP, IconType.BATTERY, IconType.BELL, IconType.BOOKMARK,
            IconType.BUG, IconType.CALENDAR, IconType.CAMERA, IconType.CHECK2_CIRCLE, IconType.CLOUD, IconType.CODE,
            IconType.COMPASS, IconType.CREDIT_CARD, IconType.DATABASE, IconType.DOWNLOAD, IconType.ENVELOPE,
            IconType.EYE, IconType.FILE_EARMARK, IconType.FUNNEL, IconType.FLAG, IconType.FOLDER, IconType.GEAR,
            IconType.GIFT, IconType.GLOBE, IconType.HEART, IconType.HOUSE, IconType.IMAGE, IconType.INBOX,
            IconType.INFO_CIRCLE, IconType.KEY, IconType.LINK_45DEG, IconType.LIST, IconType.LOCK, IconType.MAP,
            IconType.PAPERCLIP, IconType.PENCIL, IconType.PERSON, IconType.PLAY, IconType.PLUS,
            IconType.QUESTION_CIRCLE, IconType.SEARCH, IconType.SEND, IconType.SHARE, IconType.SHIELD,
            IconType.SLIDERS, IconType.STAR, IconType.TABLE, IconType.TAG, IconType.TELEPHONE, IconType.TERMINAL,
            IconType.TRASH, IconType.TROPHY, IconType.TRUCK, IconType.UMBRELLA, IconType.UPLOAD, IconType.WIFI,
            IconType.WRENCH};

    private Widget iconBasicPanel() {
        return panel("Basic Use", inline(new Icon(IconType.STAR), new Icon(IconType.HEART),
                new Icon(IconType.ENVELOPE), new Icon(IconType.GEAR), new Icon(IconType.CLOUD),
                new Icon(IconType.CAMERA), new Icon(IconType.CREDIT_CARD), new Icon("check2-circle")),
                "new Icon(IconType.STAR);\nnew Icon(\"check2-circle\"); // or name it directly");
    }

    private Widget iconSizesPanel() {
        IconSize[] sizes = {IconSize.SMALL, IconSize.MEDIUM, IconSize.LARGE, IconSize.X_LARGE, IconSize.XX_LARGE,
                IconSize.XXX_LARGE};
        Widget[] icons = new Widget[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            Icon icon = new Icon(IconType.STAR);
            icon.setSize(sizes[i]);
            icons[i] = icon;
        }
        return panel("Sizes", inline(icons),
                "Icon icon = new Icon(IconType.STAR);\nicon.setSize(IconSize.XX_LARGE);\n\n// IconSize maps onto Bootstrap's own fs-* utilities, so it\n// needs no stylesheet from this library. Bootstrap 3 used the\n// Font Awesome steps fa-lg and fa-2x .. fa-5x.");
    }

    private Widget iconAnimationPanel() {
        Icon spin = new Icon(IconType.GEAR);
        spin.setSize(IconSize.X_LARGE);
        spin.setSpin(true);
        Icon pulse = new Icon(IconType.GEAR);
        pulse.setSize(IconSize.X_LARGE);
        pulse.setPulse(true);
        Button saving = new Button("Saving", ButtonType.PRIMARY);
        saving.setIcon(IconType.ARROW_REPEAT);
        saving.setIconSpin(true);
        return panel("Animation", inline(spin, pulse, saving),
                "icon.setSpin(true);\nicon.setPulse(true);\n\nbutton.setIcon(IconType.ARROW_REPEAT);\nbutton.setIconSpin(true);\n\n// Bootstrap Icons ships glyphs only, so gwt-bootstrap5.css\n// declares the animation. It honours prefers-reduced-motion.");
    }

    private Widget iconRotateFlipPanel() {
        IconRotate[] rotations = {IconRotate.NONE, IconRotate.ROTATE_90, IconRotate.ROTATE_180, IconRotate.ROTATE_270};
        IconFlip[] flips = {IconFlip.HORIZONTAL, IconFlip.VERTICAL};
        Widget[] icons = new Widget[rotations.length + flips.length];
        for (int i = 0; i < rotations.length; i++) {
            Icon icon = new Icon(IconType.SIGNPOST_2);
            icon.setSize(IconSize.X_LARGE);
            icon.setRotate(rotations[i]);
            icons[i] = icon;
        }
        for (int i = 0; i < flips.length; i++) {
            Icon icon = new Icon(IconType.SIGNPOST_2);
            icon.setSize(IconSize.X_LARGE);
            icon.setFlip(flips[i]);
            icons[rotations.length + i] = icon;
        }
        return panel("Rotated & Flipped", inline(icons),
                "icon.setRotate(IconRotate.ROTATE_90);\nicon.setFlip(IconFlip.HORIZONTAL);");
    }

    private Widget iconStackPanel() {
        IconStack stack = new IconStack();
        stack.add(new Icon(IconType.CIRCLE_FILL), true);
        Icon glyph = new Icon(IconType.TERMINAL);
        glyph.setInverse(true);
        stack.add(glyph, false);

        IconStack flagged = new IconStack();
        flagged.add(new Icon(IconType.SQUARE), true);
        flagged.add(new Icon(IconType.CHECK), false);

        Icon bordered = new Icon(IconType.STAR);
        bordered.setSize(IconSize.LARGE);
        bordered.setBorder(true);
        return panel("Stacked & Bordered Icons", inline(stack, flagged, bordered),
                "IconStack stack = new IconStack();\nstack.add(new Icon(IconType.CIRCLE_FILL), true);  // background\nstack.add(new Icon(IconType.TERMINAL), false);    // foreground\n\nicon.setBorder(true);");
    }

    private Widget iconFixedWidthPanel() {
        IconType[] types = {IconType.ENVELOPE, IconType.CALENDAR, IconType.TRASH, IconType.GEAR};
        String[] labels = {"Inbox", "Calendar", "Trash", "Settings"};
        ListGroup list = new ListGroup();
        for (int i = 0; i < types.length; i++) {
            Icon icon = new Icon(types[i]);
            icon.setFixedWidth(true);
            ListGroupItem item = new ListGroupItem();
            item.add(icon);
            item.add(new InlineHTML(" " + labels[i]));
            list.add(item);
        }
        return panel("Fixed Width Icons", list,
                "Icon icon = new Icon(IconType.ENVELOPE);\nicon.setFixedWidth(true); // aligns icons down a list");
    }

    private Widget iconEmphasisPanel() {
        Emphasis[] emphases = {Emphasis.DEFAULT, Emphasis.MUTED, Emphasis.PRIMARY, Emphasis.SUCCESS, Emphasis.INFO,
                Emphasis.WARNING, Emphasis.DANGER};
        Widget[] icons = new Widget[emphases.length];
        for (int i = 0; i < emphases.length; i++) {
            Icon icon = new Icon(IconType.INFO_CIRCLE);
            icon.setSize(IconSize.X_LARGE);
            icon.setEmphasis(emphases[i]);
            icons[i] = icon;
        }
        return panel("Contextual Colours", inline(icons),
                "icon.setEmphasis(Emphasis.DANGER); // maps onto the Bootstrap 5 text-* utilities");
    }

    private Widget iconTypePanel() {
        return panel("Migrating icons from Bootstrap 3", new HTML(
                "<p>Bootstrap&nbsp;3 shipped Font&nbsp;Awesome; Bootstrap&nbsp;5 ships"
                + " <a href='https://icons.getbootstrap.com/'>Bootstrap&nbsp;Icons</a>. The two name their icons"
                + " differently, so <code>IconType</code> has been re-based on the Bootstrap&nbsp;Icons vocabulary:"
                + " 2078 constants, each emitting its own <code>bi-*</code> class.</p>"
                + "<p>Around 200 Font&nbsp;Awesome&nbsp;4 names coincide with a Bootstrap&nbsp;Icons name and survive"
                + " unchanged — <code>STAR</code>, <code>HEART</code>, <code>ENVELOPE</code>, <code>GEAR</code>,"
                + " <code>CALENDAR</code>. The rest are gone, and code using one will not compile. That is on"
                + " purpose: a missing constant is a compile error a developer can act on, where keeping it would"
                + " render nothing or an approximation of the icon they asked for. Common renames:</p>"
                + "<div class='table-responsive'><table class='table table-sm'>"
                + "<thead><tr><th scope='col'>Bootstrap 3</th><th scope='col'>Bootstrap 5</th></tr></thead><tbody>"
                + "<tr><td><code>IconType.HOME</code></td><td><code>IconType.HOUSE</code></td></tr>"
                + "<tr><td><code>IconType.USER</code></td><td><code>IconType.PERSON</code></td></tr>"
                + "<tr><td><code>IconType.COG</code>, <code>COGS</code></td><td><code>IconType.GEAR</code>, <code>GEAR_WIDE_CONNECTED</code></td></tr>"
                + "<tr><td><code>IconType.TIMES</code>, <code>REMOVE</code></td><td><code>IconType.X</code></td></tr>"
                + "<tr><td><code>IconType.ANGLE_LEFT</code></td><td><code>IconType.CHEVRON_LEFT</code></td></tr>"
                + "<tr><td><code>IconType.REFRESH</code></td><td><code>IconType.ARROW_CLOCKWISE</code></td></tr>"
                + "<tr><td><code>IconType.WARNING</code></td><td><code>IconType.EXCLAMATION_TRIANGLE</code></td></tr>"
                + "<tr><td><code>IconType.PICTURE_O</code></td><td><code>IconType.IMAGE</code></td></tr>"
                + "<tr><td><code>IconType.SHOPPING_CART</code></td><td><code>IconType.CART</code></td></tr>"
                + "<tr><td><code>IconType.BARS</code>, <code>NAVICON</code></td><td><code>IconType.LIST</code></td></tr>"
                + "</tbody></table></div>"
                + "<p class='mb-0'>The modifier vocabulary moved with it. <code>IconSize</code> now names"
                + " Bootstrap's own <code>fs-*</code> utilities and <code>Emphasis</code> its <code>text-*</code>"
                + " utilities, so neither needs a stylesheet from this library. Spin, pulse, rotate, flip, fixed"
                + " width, border and stacking have no Bootstrap&nbsp;5 equivalent, so"
                + " <code>gwt-bootstrap5.css</code> declares them under a <code>gbm-icon-*</code>"
                + " namespace — deliberately not <code>bi-*</code>, since <code>bi-border</code> is a real"
                + " Bootstrap icon.</p>"),
                "// Bootstrap 3\nnew Icon(IconType.STAR);          // fa-star\nicon.setSize(IconSize.TIMES2);    // fa-2x\n\n// Bootstrap 5\nnew Icon(IconType.STAR);          // bi bi-star\nicon.setSize(IconSize.XX_LARGE);  // fs-2");
    }

    private Widget iconGalleryPanel() {
        Row grid = new Row();
        grid.setStyleName("row row-cols-2 row-cols-sm-3 row-cols-md-4 row-cols-lg-6 g-2 text-center");
        for (IconType type : ICON_GALLERY) {
            Column cell = new Column(2);
            cell.setStyleName("col");
            Div box = new Div();
            box.addStyleName("border rounded py-2 h-100");
            Icon icon = new Icon(type);
            icon.setSize(IconSize.LARGE);
            box.add(icon);
            box.add(new HTML("<small class='text-body-secondary d-block text-truncate'>" + type.name() + "</small>"));
            cell.add(box);
            grid.add(cell);
        }
        return panel("Available Icons (" + ICON_GALLERY.length + " of 2078)", grid,
                "for (IconType type : IconType.values()) {\n    add(new Icon(type));\n}");
    }

    private Widget navStackedPanel() {
        NavPills stacked = new NavPills();
        stacked.addStyleName("flex-column");
        stacked.addLink("Home", "#navs").addStyleName("active");
        stacked.addLink("Profile", "#navs");
        stacked.addLink("Messages", "#navs");
        Column narrow = new Column(4);
        narrow.add(stacked);
        Row wrapper = row();
        wrapper.add(narrow);
        return panel("Stacked Pills", wrapper,
                "NavPills pills = new NavPills();\npills.addStyleName(\"flex-column\"); // Bootstrap 5 replaces nav-stacked");
    }

    private Widget navJustifiedPanel() {
        NavTabs justified = new NavTabs();
        justified.addStyleName("nav-justified");
        justified.addLink("Home", "#navs").addStyleName("active");
        justified.addLink("Profile", "#navs");
        justified.addLink("Messages", "#navs");
        NavPills filled = new NavPills();
        filled.addStyleName("nav-fill");
        filled.addLink("Home", "#navs").addStyleName("active");
        filled.addLink("Profile", "#navs");
        filled.addLink("Messages", "#navs");
        PanelBody body = new PanelBody();
        body.add(justified);
        body.add(new HTML("<div class='mt-3'></div>"));
        body.add(filled);
        return panel("Justified and Filled", body,
                "tabs.addStyleName(\"nav-justified\"); // equal-width, full-width\npills.addStyleName(\"nav-fill\"); // proportional widths");
    }

    private Widget navDropdownPanel() {
        NavTabs tabs = new NavTabs();
        tabs.addLink("Home", "#navs").addStyleName("active");
        ListDropDown menu = new ListDropDown("Dropdown");
        menu.addItem(new DropDownItem("Action", "#navs"));
        menu.addItem(new DropDownItem("Another action", "#navs"));
        menu.addMenuWidget(new Divider());
        menu.addItem(new DropDownItem("Separated link", "#navs"));
        tabs.add(menu);
        tabs.addLink("Messages", "#navs");
        return panel("Using Dropdowns", tabs,
                "NavTabs tabs = new NavTabs();\nListDropDown menu = new ListDropDown(\"Dropdown\");\nmenu.addItem(new DropDownItem(\"Action\", \"#\"));\ntabs.add(menu);");
    }

    private Widget navIconsPanel() {
        NavPills pills = new NavPills();
        IconType[] types = {IconType.HOUSE, IconType.PERSON, IconType.ENVELOPE};
        String[] labels = {"Home", "Profile", "Messages"};
        for (int i = 0; i < types.length; i++) {
            Anchor link = pills.addLink(labels[i], "#navs");
            link.setIcon(types[i]);
            link.setIconFixedWidth(true);
            if (i == 0) {
                link.addStyleName("active");
            }
        }
        return panel("Navs With Icons And Fixed Width", pills,
                "Anchor link = pills.addLink(\"Home\", \"#\");\nlink.setIcon(IconType.HOUSE);\nlink.setIconFixedWidth(true);");
    }

    private Widget buttonDropdownBasicPanel() {
        DropDown primary = menuDropDown("Action", ButtonType.PRIMARY);
        DropDown secondary = menuDropDown("Another action", ButtonType.DEFAULT);
        DropDown danger = menuDropDown("Danger", ButtonType.DANGER);
        return panel("Basic", inline(primary, secondary, danger),
                "DropDown dropDown = new DropDown(\"Action\");\ndropDown.getToggle().setType(ButtonType.PRIMARY);\ndropDown.addItem(new DropDownItem(\"First\", \"#\"));");
    }

    private Widget buttonDropdownSplitPanel() {
        Widget[] groups = new Widget[3];
        ButtonType[] types = {ButtonType.PRIMARY, ButtonType.SUCCESS, ButtonType.DANGER};
        String[] labels = {"Primary", "Success", "Danger"};
        for (int i = 0; i < types.length; i++) {
            groups[i] = splitDropDown(labels[i], types[i]);
        }
        return panel("Split", inline(groups),
                "ButtonGroup group = new ButtonGroup();\ngroup.add(new Button(\"Primary\", ButtonType.PRIMARY));\nDropDown split = new DropDown(\"\");\nsplit.getToggle().addStyleName(\"dropdown-toggle-split\");\ngroup.add(split);");
    }

    private Widget buttonDropdownDirectionsPanel() {
        DropDown up = menuDropDown("Dropup", ButtonType.DEFAULT);
        up.setDropUp(true);
        DropDown start = menuDropDown("Dropstart", ButtonType.DEFAULT);
        start.setDropStart(true);
        DropDown end = menuDropDown("Dropend", ButtonType.DEFAULT);
        end.setDropEnd(true);
        DropDown aligned = menuDropDown("End-aligned menu", ButtonType.DEFAULT);
        aligned.setMenuEndAligned(true);
        return panel("Directions and Alignment", inline(up, start, end, aligned),
                "dropDown.setDropUp(true);\ndropDown.setDropStart(true);\ndropDown.setDropEnd(true);\ndropDown.setMenuEndAligned(true);");
    }

    private Widget buttonDropdownSizesPanel() {
        DropDown large = menuDropDown("Large", ButtonType.PRIMARY);
        large.getToggle().setLarge(true);
        DropDown normal = menuDropDown("Normal", ButtonType.PRIMARY);
        DropDown small = menuDropDown("Small", ButtonType.PRIMARY);
        small.getToggle().setSmall(true);
        DropDown outline = menuDropDown("Outline", ButtonType.PRIMARY);
        outline.getToggle().setOutline(true);
        return panel("Sizes and Outline", inline(large, normal, small, outline),
                "dropDown.getToggle().setLarge(true);\ndropDown.getToggle().setSmall(true);\ndropDown.getToggle().setOutline(true);");
    }

    private Widget buttonDropdownMenuContentPanel() {
        DropDown dropDown = new DropDown("Menu content");
        dropDown.getToggle().setType(ButtonType.DEFAULT);
        dropDown.addMenuWidget(new DropDownHeader("Header"));
        dropDown.addItem(new DropDownItem("Action", "#buttonDropdowns"));
        DropDownItem disabled = new DropDownItem("Disabled action", "#buttonDropdowns");
        disabled.addStyleName("disabled");
        dropDown.addItem(disabled);
        dropDown.addMenuWidget(new Divider());
        dropDown.addMenuWidget(new DropDownHeader("Second group"));
        dropDown.addItem(new DropDownItem("Separated link", "#buttonDropdowns"));
        return panel("Headers, Dividers and Disabled Items", inline(dropDown),
                "dropDown.addMenuWidget(new DropDownHeader(\"Header\"));\ndropDown.addItem(new DropDownItem(\"Action\", \"#\"));\ndropDown.addMenuWidget(new Divider());");
    }

    private DropDown menuDropDown(String text, ButtonType type) {
        DropDown dropDown = new DropDown(text);
        dropDown.getToggle().setType(type);
        dropDown.addItem(new DropDownItem("First", "#buttonDropdowns"));
        dropDown.addItem(new DropDownItem("Second", "#buttonDropdowns"));
        dropDown.addMenuWidget(new Divider());
        dropDown.addItem(new DropDownItem("Third", "#buttonDropdowns"));
        return dropDown;
    }

    private Widget splitDropDown(String text, ButtonType type) {
        ButtonGroup group = new ButtonGroup();
        Button action = new Button(text, type);
        group.addButton(action);
        DropDown split = new DropDown("");
        split.removeStyleName("dropdown");
        split.getToggle().setType(type);
        split.getToggle().addStyleName("dropdown-toggle-split");
        split.getToggle().getElement().setAttribute("aria-label", text + " menu");
        split.addItem(new DropDownItem("First", "#buttonDropdowns"));
        split.addItem(new DropDownItem("Second", "#buttonDropdowns"));
        group.add(split);
        return group;
    }

    private Widget jumbotronPanel() {
        Jumbotron jumbotron = new Jumbotron();
        jumbotron.add(new Heading(1, "Hello, world!"));
        jumbotron.add(new Lead("This is a simple hero unit, a simple jumbotron-style component for calling extra attention to featured content."));
        jumbotron.add(new HTML("<hr class='my-4'/>"));
        jumbotron.add(new Paragraph("It uses utility classes for typography and spacing to space content out within the larger container."));
        Button learnMore = new Button("Learn more", ButtonType.PRIMARY);
        learnMore.setLarge(true);
        jumbotron.add(learnMore);
        return panel("Basic", jumbotron,
                "Jumbotron jumbotron = new Jumbotron();\njumbotron.add(new Heading(1, \"Hello, world!\"));\njumbotron.add(new Lead(\"...\"));\n\n// Bootstrap 5 dropped the .jumbotron class; the widget now\n// renders the documented p-5 mb-4 bg-body-tertiary rounded-3 utilities.");
    }

    private Widget suggestBoxPanel() {
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        oracle.addAll(Arrays.asList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado",
                "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa",
                "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
                "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey",
                "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon",
                "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah",
                "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"));
        SuggestBox suggestBox = new SuggestBox(oracle);
        suggestBox.getElement().setAttribute("placeholder", "Start typing a state name");
        FormGroup group = new FormGroup();
        FormLabel label = new FormLabel("Where do you live?");
        group.add(label);
        group.add(suggestBox);
        group.add(new HelpBlock("The suggestion popup is styled with the Bootstrap 5 dropdown-menu classes."));
        return panel("Basic", group,
                "MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();\noracle.addAll(Arrays.asList(\"Alabama\", \"Alaska\", ...));\nSuggestBox suggestBox = new SuggestBox(oracle);");
    }

    private Widget thumbnailLinksPanel() {
        Row grid = row();
        grid.addStyleName("g-3");
        for (int i = 0; i < 3; i++) {
            Column cell = new Column(4);
            ThumbnailLink link = new ThumbnailLink("#thumbnails");
            link.add(new Image(IMG_THUMB));
            cell.add(link);
            grid.add(cell);
        }
        return panel("As Links", grid,
                "ThumbnailLink link = new ThumbnailLink(\"#\");\nlink.add(new Image(url));");
    }

    private Widget thumbnailCustomContentPanel() {
        Row grid = row();
        grid.addStyleName("g-3");
        for (int i = 0; i < 3; i++) {
            Column cell = new Column(4);
            ThumbnailPanel thumbnail = new ThumbnailPanel();
            thumbnail.addBody(new Image(IMG_THUMB));
            thumbnail.addBody(new Heading(4, "Heading"));
            thumbnail.addBody(new Paragraph("Content here..."));
            Button primary = new Button("Button", ButtonType.PRIMARY);
            Button secondary = new Button("Button", ButtonType.DEFAULT);
            thumbnail.addBody(inline(primary, secondary));
            cell.add(thumbnail);
            grid.add(cell);
        }
        return panel("Custom Content", grid,
                "ThumbnailPanel thumbnail = new ThumbnailPanel();\nthumbnail.addBody(new Image(url));\nthumbnail.addBody(new Heading(4, \"Heading\"));\nthumbnail.addBody(new Paragraph(\"Content here...\"));");
    }

    private Widget tableBasicPanel() {
        return panel("Basic", new HTML("<table class='table'>"
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
        String body = "<tbody><tr><th scope='row'>1</th><td>gwt-bootstrap3</td></tr>"
                + "<tr><th scope='row'>2</th><td>gwt-bootstrap5</td></tr></tbody>";
        String[] classes = {"table table-striped", "table table-bordered", "table table-hover", "table table-sm",
                "table table-dark", "table table-striped-columns"};
        String[] labels = {"table-striped", "table-bordered", "table-hover", "table-sm (was table-condensed)",
                "table-dark", "table-striped-columns (new in 5.3)"};
        PanelBody wrapper = new PanelBody();
        for (int i = 0; i < classes.length; i++) {
            wrapper.add(new HTML("<h3 class='h6 text-body-secondary mt-3'><code>" + labels[i] + "</code></h3>"
                    + "<table class='" + classes[i] + "'>" + head + body + "</table>"));
        }
        return panel("Variants", wrapper,
                "// TableType.CONDENSED is Bootstrap 3 wording; Bootstrap 5 renames it table-sm.\n<table class=\"table table-striped\">...</table>");
    }

    private Widget tableContextualPanel() {
        String[] variants = {"primary", "secondary", "success", "danger", "warning", "info", "light", "dark"};
        StringBuilder rows = new StringBuilder();
        rows.append("<tr><td>default</td><td>No contextual class</td></tr>");
        for (String variant : variants) {
            rows.append("<tr class='table-").append(variant).append("'><td>table-").append(variant)
                    .append("</td><td>Contextual row</td></tr>");
        }
        return panel("Contextual Classes", new HTML("<table class='table'>"
                + "<thead><tr><th scope='col'>Class</th><th scope='col'>Meaning</th></tr></thead>"
                + "<tbody>" + rows + "</tbody></table>"),
                "// Bootstrap 3 used .active/.success/.info/.warning/.danger on rows.\n// Bootstrap 5 uses .table-* with the full theme palette.\n<tr class=\"table-success\">...</tr>");
    }

    private Widget tableResponsivePanel() {
        StringBuilder header = new StringBuilder();
        StringBuilder cells = new StringBuilder();
        for (int i = 1; i <= 12; i++) {
            header.append("<th scope='col'>Heading ").append(i).append("</th>");
            cells.append("<td>Cell ").append(i).append("</td>");
        }
        return panel("Responsive", new HTML("<div class='table-responsive'><table class='table table-bordered'>"
                + "<thead><tr>" + header + "</tr></thead><tbody><tr>" + cells + "</tr><tr>" + cells
                + "</tr></tbody></table></div>"),
                "<div class=\"table-responsive\">\n  <table class=\"table\">...</table>\n</div>");
    }

    private Widget cellTablePanel() {
        List<Release> releases = Arrays.asList(
                new Release("Bootstrap", "3.4.1", "2019-02-13"),
                new Release("Bootstrap", "5.3.8", "2025-08-19"),
                new Release("jQuery", "3.7.1", "2023-08-28"),
                new Release("GWT", "2.13.1", "2025-11-05"),
                new Release("TeaVM", "0.15.0", "2025-08-13"));

        CellTable<Release> table = new CellTable<Release>();
        table.addStyleName("table table-striped table-hover align-middle");
        table.setWidth("100%");
        table.setEmptyTableWidget(new HTML("<p class='text-body-secondary'>No releases.</p>"));

        TextColumn<Release> project = new TextColumn<Release>() {
            @Override
            public String getValue(Release release) {
                return release.project;
            }
        };
        project.setSortable(true);
        TextColumn<Release> version = new TextColumn<Release>() {
            @Override
            public String getValue(Release release) {
                return release.version;
            }
        };
        TextColumn<Release> released = new TextColumn<Release>() {
            @Override
            public String getValue(Release release) {
                return release.released;
            }
        };
        released.setSortable(true);
        table.addColumn(project, "Project");
        table.addColumn(version, "Version");
        table.addColumn(released, "Released");

        ListDataProvider<Release> provider = new ListDataProvider<Release>(new ArrayList<Release>(releases));
        provider.addDataDisplay(table);

        final List<Release> rows = provider.getList();
        ColumnSortEvent.ListHandler<Release> sortHandler = new ColumnSortEvent.ListHandler<Release>(rows);
        sortHandler.setComparator(project, new Comparator<Release>() {
            @Override
            public int compare(Release left, Release right) {
                return left.project.compareTo(right.project);
            }
        });
        sortHandler.setComparator(released, new Comparator<Release>() {
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
        selection.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Release selected = ((SingleSelectionModel<Release>) event.getSource()).getSelectedObject();
                echo.setHTML(selected == null ? "<p class='text-body-secondary mb-0'>Nothing selected.</p>"
                        : "<p class='mb-0'>Selected <strong>" + selected.project + " " + selected.version
                                + "</strong></p>");
            }
        });

        Div responsive = new Div();
        responsive.addStyleName("table-responsive");
        responsive.add(table);
        PanelBody body = new PanelBody();
        body.add(responsive);
        body.add(echo);
        return panel("Cell Table", body,
                "CellTable<Release> table = new CellTable<Release>();\ntable.addStyleName(\"table table-striped table-hover\");\ntable.addColumn(project, \"Project\");\n\nListDataProvider<Release> provider = new ListDataProvider<Release>(releases);\nprovider.addDataDisplay(table);\ntable.addColumnSortHandler(sortHandler);\ntable.setSelectionModel(new SingleSelectionModel<Release>());");
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

    private Widget typographyHeadingsPanel() {
        PanelBody body = new PanelBody();
        for (int level = 1; level <= 6; level++) {
            body.add(new Heading(level, "h" + level + ". Bootstrap heading"));
        }
        Heading withSub = new Heading(2, "h2. Heading ");
        withSub.add(new InlineHTML("<small class='text-body-secondary'>with muted secondary text</small>"));
        body.add(withSub);
        Heading withIcon = new Heading(3, "h3. Heading with icon ");
        withIcon.add(new Icon(IconType.STAR));
        body.add(withIcon);
        body.add(new HTML("<p class='display-4'>Display heading</p>"));
        return panel("Headings", body,
                "new Heading(1, \"h1. Bootstrap heading\");\n\nHeading withSub = new Heading(2, \"h2. Heading \");\nwithSub.add(new InlineHTML(\"<small class='text-body-secondary'>...</small>\"));\n\n// Bootstrap 3's .text-muted becomes .text-body-secondary in 5.3.");
    }

    private Widget typographyBodyCopyPanel() {
        PanelBody body = new PanelBody();
        body.add(new Lead("Lead text stands out from the surrounding paragraphs."));
        body.add(new Paragraph("A standard paragraph. Bootstrap 5 keeps a 1rem base font size and a 1.5 line height, "
                + "so blocks of running text render at a comfortable measure without further styling."));
        body.add(new Paragraph("A second paragraph, so the default bottom margin between blocks is visible."));
        return panel("Body Copy", body, "new Lead(\"Lead text...\");\nnew Paragraph(\"A standard paragraph.\");");
    }

    private Widget typographyInlineElementsPanel() {
        Abbreviation abbreviation = new Abbreviation("GWT");
        abbreviation.setTitle("Google Web Toolkit");
        PanelBody body = new PanelBody();
        body.add(new HTML("<p>You can use the mark tag to <mark>highlight</mark> text.</p>"
                + "<p><del>This line of text is meant to be treated as deleted text.</del></p>"
                + "<p><s>This line of text is meant to be treated as no longer accurate.</s></p>"
                + "<p><ins>This line of text is meant to be treated as an addition.</ins></p>"
                + "<p><u>This line of text will render as underlined.</u></p>"
                + "<p><small>This line of text is meant to be treated as fine print.</small></p>"
                + "<p><strong>This line rendered as bold text.</strong></p>"
                + "<p><em>This line rendered as italicised text.</em></p>"));
        body.add(new Paragraph("Abbreviations carry a title attribute:"));
        body.add(inline(abbreviation, new Code("Code"), new Pre("Preformatted text")));
        return panel("Inline Text Elements", body,
                "Abbreviation abbreviation = new Abbreviation(\"GWT\");\nabbreviation.setTitle(\"Google Web Toolkit\");\nnew Code(\"Code\");\nnew Pre(\"Preformatted text\");");
    }

    private Widget typographyAlignmentPanel() {
        return panel("Alignment and Transform", new HTML(
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
        quote.add(new HTML("<footer class='blockquote-footer'>Someone famous in <cite title='Source Title'>Source Title</cite></footer>"));
        BlockQuote reversed = new BlockQuote();
        reversed.addStyleName("text-end");
        reversed.add(new Paragraph("A quote aligned to the end of the line."));
        reversed.add(new HTML("<footer class='blockquote-footer'>Someone famous in <cite title='Source Title'>Source Title</cite></footer>"));
        PanelBody body = new PanelBody();
        body.add(quote);
        body.add(reversed);
        return panel("Blockquotes", body,
                "BlockQuote quote = new BlockQuote();\nquote.add(new Paragraph(\"...\"));\n\n// Bootstrap 3's .blockquote-reverse becomes .text-end in Bootstrap 5.\nreversed.addStyleName(\"text-end\");");
    }

    private Widget typographyListsPanel() {
        Description description = new Description();
        description.add(new DescriptionTitle("Description lists"));
        description.add(new DescriptionData("A description list is perfect for defining terms."));
        description.add(new DescriptionTitle("Horizontal"));
        description.add(new DescriptionData("Bootstrap 5 builds horizontal description lists from grid classes rather than .dl-horizontal."));
        PanelBody body = new PanelBody();
        body.add(new HTML("<ul><li>Unordered list item</li><li>Another item</li></ul>"
                + "<ol><li>Ordered list item</li><li>Another item</li></ol>"
                + "<ul class='list-unstyled'><li>Unstyled list item</li><li>Another item</li></ul>"
                + "<ul class='list-inline'><li class='list-inline-item'>Inline</li><li class='list-inline-item'>list</li><li class='list-inline-item'>items</li></ul>"));
        body.add(description);
        return panel("Lists", body,
                "Description description = new Description();\ndescription.add(new DescriptionTitle(\"Term\"));\ndescription.add(new DescriptionData(\"Definition\"));\n\n// Bootstrap 5 marks inline list children with .list-inline-item.");
    }

    private Widget carouselCaptionsPanel() {
        String carouselId = "showcaseCaptionCarousel";
        Carousel carousel = new Carousel();
        carousel.getElement().setId(carouselId);
        CarouselIndicators indicators = new CarouselIndicators();
        String[] tones = {"primary", "success", "info"};
        for (int i = 0; i < tones.length; i++) {
            CarouselIndicator indicator = new CarouselIndicator(carouselId, i);
            indicator.setActive(i == 0);
            indicators.addIndicator(indicator);
        }
        carousel.insert(indicators, 0);
        for (int i = 0; i < tones.length; i++) {
            CarouselSlide slide = new CarouselSlide(new HTML("<div class='d-flex align-items-center "
                    + "justify-content-center text-bg-" + tones[i] + " rounded' style='height: 14rem;'>Slide "
                    + (i + 1) + "</div>"));
            slide.setActive(i == 0);
            CarouselCaption caption = new CarouselCaption();
            caption.add(new Heading(5, "Slide " + (i + 1)));
            caption.add(new Paragraph("Caption!"));
            slide.add(caption);
            carousel.addSlide(slide);
        }
        carousel.add(new CarouselControl(carouselId, true));
        carousel.add(new CarouselControl(carouselId, false));
        return panel("With Captions", carousel,
                "CarouselSlide slide = new CarouselSlide(content);\nCarouselCaption caption = new CarouselCaption();\ncaption.add(new Heading(5, \"Slide 1\"));\nslide.add(caption);\ncarousel.addSlide(slide);");
    }

    private Widget carouselOptionsPanel() {
        final String carouselId = "showcaseOptionsCarousel";
        final Carousel carousel = new Carousel();
        carousel.getElement().setId(carouselId);
        carousel.setInterval(1500);
        carousel.setWrap(true);
        carousel.setPause("hover");
        String[] tones = {"warning", "danger"};
        for (int i = 0; i < tones.length; i++) {
            CarouselSlide slide = new CarouselSlide(new HTML("<div class='d-flex align-items-center "
                    + "justify-content-center text-bg-" + tones[i] + " rounded' style='height: 10rem;'>Slide "
                    + (i + 1) + " — 1.5s interval</div>"));
            slide.setActive(i == 0);
            carousel.addSlide(slide);
        }
        carousel.add(new CarouselControl(carouselId, true));
        carousel.add(new CarouselControl(carouselId, false));

        Button previous = new Button("Previous", ButtonType.DEFAULT);
        previous.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                carousel.goToPrev();
            }
        });
        Button next = new Button("Next", ButtonType.DEFAULT);
        next.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                carousel.goToNext();
            }
        });
        Button pause = new Button("Pause", ButtonType.DEFAULT);
        pause.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                carousel.pauseCarousel();
            }
        });
        Button cycle = new Button("Cycle", ButtonType.DEFAULT);
        cycle.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                carousel.cycleCarousel();
            }
        });
        PanelBody body = new PanelBody();
        body.add(carousel);
        body.add(inline(previous, next, pause, cycle));
        return panel("Options and Programmatic Control", body,
                "carousel.setInterval(1500);\ncarousel.setWrap(true);\ncarousel.setPause(\"hover\");\n\ncarousel.goToPrev();\ncarousel.goToNext();\ncarousel.pauseCarousel();\ncarousel.cycleCarousel();");
    }

    private Widget modalSizesPanel(RootPanel root) {
        ModalSize[] sizes = {ModalSize.SMALL, ModalSize.DEFAULT, ModalSize.LARGE};
        String[] labels = {"Small", "Default", "Large"};
        Widget[] buttons = new Widget[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            final Modal modal = new Modal();
            modal.setTitle(labels[i] + " modal");
            modal.setSize(sizes[i]);
            modal.addToBody(new Paragraph("This modal was created with ModalSize." + sizes[i].name() + "."));
            ModalFooter footer = new ModalFooter();
            footer.add(closeButton(modal));
            modal.addFooter(footer);
            root.add(modal);
        pageModals.add(modal);
            Button open = new Button(labels[i], ButtonType.PRIMARY);
            open.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    modal.show();
                }
            });
            buttons[i] = open;
        }
        return panel("Sizes", inline(buttons),
                "Modal modal = new Modal();\nmodal.setSize(ModalSize.LARGE);");
    }

    private Widget modalOnlyOnePanel(RootPanel root) {
        final Modal first = new Modal();
        first.setTitle("First modal");
        first.setHideOtherModals(true);
        first.addToBody(new Paragraph("Opening the second modal hides this one, because both set "
                + "setHideOtherModals(true)."));
        final Modal second = new Modal();
        second.setTitle("Second modal");
        second.setHideOtherModals(true);
        second.addToBody(new Paragraph("The first modal was hidden when this one opened."));

        Button openSecond = new Button("Open the second modal", ButtonType.PRIMARY);
        openSecond.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                second.show();
            }
        });
        ModalFooter firstFooter = new ModalFooter();
        firstFooter.add(openSecond);
        firstFooter.add(closeButton(first));
        first.addFooter(firstFooter);
        ModalFooter secondFooter = new ModalFooter();
        secondFooter.add(closeButton(second));
        second.addFooter(secondFooter);
        root.add(first);
        root.add(second);
        pageModals.add(first);
        pageModals.add(second);

        Button open = new Button("Open the first modal", ButtonType.PRIMARY);
        open.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                first.show();
            }
        });
        return panel("Only One Modal Active", inline(open),
                "modal.setHideOtherModals(true);");
    }

    private Widget modalEventsPanel(RootPanel root) {
        final Modal modal = new Modal();
        modal.setTitle("Events");
        modal.addToBody(new Paragraph("Show, shown, hide and hidden are logged below."));
        ModalFooter footer = new ModalFooter();
        footer.add(closeButton(modal));
        modal.addFooter(footer);
        root.add(modal);
        pageModals.add(modal);

        final ListGroup log = new ListGroup();
        final HTML empty = new HTML("<p class='text-body-secondary mb-0'>No events yet.</p>");
        final PanelBody logBody = new PanelBody();
        logBody.add(empty);
        logBody.add(log);

        modal.addShowHandler(new ModalShowHandler() {
            @Override
            public void onShow(ModalShowEvent event) {
                logEvent(log, empty, "show");
            }
        });
        modal.addShownHandler(new ModalShownHandler() {
            @Override
            public void onShown(ModalShownEvent event) {
                logEvent(log, empty, "shown");
            }
        });
        modal.addHideHandler(new ModalHideHandler() {
            @Override
            public void onHide(ModalHideEvent event) {
                logEvent(log, empty, "hide");
            }
        });
        modal.addHiddenHandler(new ModalHiddenHandler() {
            @Override
            public void onHidden(ModalHiddenEvent event) {
                logEvent(log, empty, "hidden");
            }
        });

        Button open = new Button("Open and watch the log", ButtonType.PRIMARY);
        open.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                modal.show();
            }
        });
        Button clear = new Button("Clear Log", ButtonType.DEFAULT);
        clear.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                log.clear();
                empty.setVisible(true);
            }
        });
        PanelBody body = new PanelBody();
        body.add(inline(open, clear));
        body.add(logBody);
        return panel("Events", body,
                "modal.addShowHandler(...);\nmodal.addShownHandler(...);\nmodal.addHideHandler(...);\nmodal.addHiddenHandler(...);");
    }

    private void logEvent(ListGroup log, HTML empty, String name) {
        empty.setVisible(false);
        log.add(new ListGroupItem(name));
    }

    private Button closeButton(final Modal modal) {
        Button close = new Button("Close", ButtonType.DEFAULT);
        close.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                modal.hide();
            }
        });
        return close;
    }

    private Widget tooltipPlacementsPanel() {
        Placement[] placements = {Placement.TOP, Placement.LEFT, Placement.BOTTOM, Placement.RIGHT};
        Widget[] widgets = new Widget[placements.length];
        for (int i = 0; i < placements.length; i++) {
            Button button = new Button("I have a Tooltip! (" + placements[i].name().toLowerCase() + ")",
                    ButtonType.DEFAULT);
            Tooltip tooltip = new Tooltip(button, "Tooltip on " + placements[i].name().toLowerCase());
            tooltip.setPlacement(placements[i]);
            widgets[i] = tooltip.asWidget();
        }
        return panel("Basic", inline(widgets),
                "Tooltip tooltip = new Tooltip(button, \"Tooltip text\");\ntooltip.setPlacement(Placement.TOP);");
    }

    private Widget tooltipTriggersPanel() {
        Trigger[] triggers = {Trigger.HOVER, Trigger.CLICK, Trigger.FOCUS};
        Widget[] widgets = new Widget[triggers.length + 1];
        for (int i = 0; i < triggers.length; i++) {
            Button button = new Button("I have a Tooltip! (on " + triggers[i].name().toLowerCase() + ")",
                    ButtonType.DEFAULT);
            Tooltip tooltip = new Tooltip(button, "Triggered on " + triggers[i].name().toLowerCase());
            tooltip.setTrigger(triggers[i]);
            widgets[i] = tooltip.asWidget();
        }
        Button manualTarget = new Button("I have a Tooltip! (on manual)", ButtonType.DEFAULT);
        final Tooltip manual = new Tooltip(manualTarget, "Shown and hidden from code");
        manual.setTrigger(Trigger.MANUAL);
        Button show = new Button("Show", ButtonType.PRIMARY);
        show.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                manual.show();
            }
        });
        Button hide = new Button("Hide", ButtonType.PRIMARY);
        hide.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                manual.hide();
            }
        });
        PanelBody manualGroup = new PanelBody();
        manualGroup.setStyleName("gbm-inline-demo");
        manualGroup.add(manual.asWidget());
        manualGroup.add(show);
        manualGroup.add(hide);
        widgets[triggers.length] = manualGroup;
        return panel("Triggers", inline(widgets),
                "tooltip.setTrigger(Trigger.CLICK);\n\ntooltip.setTrigger(Trigger.MANUAL);\ntooltip.show();\ntooltip.hide();");
    }

    private Widget tooltipOptionsPanel() {
        Button delayed = new Button("Delayed 500ms / 250ms", ButtonType.DEFAULT);
        Tooltip delayedTooltip = new Tooltip(delayed, "Appeared after a delay");
        delayedTooltip.setShowDelayMs(500);
        delayedTooltip.setHideDelayMs(250);

        Button htmlTarget = new Button("HTML content", ButtonType.DEFAULT);
        Tooltip htmlTooltip = new Tooltip(htmlTarget, "<em>Emphasised</em> and <strong>bold</strong>");
        htmlTooltip.setIsHtml(true);

        Button unanimated = new Button("No fade animation", ButtonType.DEFAULT);
        Tooltip unanimatedTooltip = new Tooltip(unanimated, "No fade");
        unanimatedTooltip.setIsAnimated(false);

        TooltipHelpBlock helpBlock = new TooltipHelpBlock("TooltipHelpBlock");
        return panel("Options", inline(delayedTooltip.asWidget(), htmlTooltip.asWidget(),
                unanimatedTooltip.asWidget(), helpBlock.asWidget()),
                "tooltip.setShowDelayMs(500);\ntooltip.setHideDelayMs(250);\ntooltip.setIsHtml(true);\ntooltip.setIsAnimated(false);");
    }

    private Widget popoverPlacementsPanel() {
        Placement[] placements = {Placement.TOP, Placement.LEFT, Placement.BOTTOM, Placement.RIGHT};
        Widget[] widgets = new Widget[placements.length];
        for (int i = 0; i < placements.length; i++) {
            Button button = new Button("I have a Popover! (" + placements[i].name().toLowerCase() + ")",
                    ButtonType.DEFAULT);
            Popover popover = new Popover(button, "Popover on " + placements[i].name().toLowerCase(),
                    "And here is some content to go with it.");
            popover.setPlacement(placements[i]);
            widgets[i] = popover.asWidget();
        }
        return panel("Basic", inline(widgets),
                "Popover popover = new Popover(button, \"Title\", \"Content\");\npopover.setPlacement(Placement.TOP);");
    }

    private Widget popoverTriggersPanel() {
        Trigger[] triggers = {Trigger.HOVER, Trigger.CLICK, Trigger.FOCUS};
        Widget[] widgets = new Widget[triggers.length + 1];
        for (int i = 0; i < triggers.length; i++) {
            Button button = new Button("I have a Popover! (on " + triggers[i].name().toLowerCase() + ")",
                    ButtonType.DEFAULT);
            Popover popover = new Popover(button, "Trigger", "Triggered on " + triggers[i].name().toLowerCase());
            popover.setTrigger(triggers[i]);
            widgets[i] = popover.asWidget();
        }
        Button manualTarget = new Button("I have a Popover! (on manual)", ButtonType.DEFAULT);
        final Popover manual = new Popover(manualTarget, "Manual", "Shown and hidden from code");
        manual.setTrigger(Trigger.MANUAL);
        Button show = new Button("Show", ButtonType.PRIMARY);
        show.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                manual.show();
            }
        });
        Button hide = new Button("Hide", ButtonType.PRIMARY);
        hide.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                manual.hide();
            }
        });
        PanelBody manualGroup = new PanelBody();
        manualGroup.setStyleName("gbm-inline-demo");
        manualGroup.add(manual.asWidget());
        manualGroup.add(show);
        manualGroup.add(hide);
        widgets[triggers.length] = manualGroup;
        return panel("Triggers", inline(widgets),
                "popover.setTrigger(Trigger.CLICK);\n\npopover.setTrigger(Trigger.MANUAL);\npopover.show();\npopover.hide();");
    }

    private Widget popoverOptionsPanel() {
        Button htmlTarget = new Button("HTML content", ButtonType.DEFAULT);
        Popover html = new Popover(htmlTarget, "Rich content",
                "<p>Popovers accept <strong>markup</strong> when <code>setIsHtml(true)</code>.</p>");
        html.setIsHtml(true);

        Button delayed = new Button("Delayed 400ms", ButtonType.DEFAULT);
        Popover delayedPopover = new Popover(delayed, "Delayed", "Appeared after 400ms");
        delayedPopover.setShowDelayMs(400);

        Button contained = new Button("Attached to body", ButtonType.DEFAULT);
        Popover containedPopover = new Popover(contained, "Container", "Rendered as a child of document.body");
        containedPopover.setContainer("body");

        return panel("Options", inline(html.asWidget(), delayedPopover.asWidget(), containedPopover.asWidget()),
                "popover.setIsHtml(true);\npopover.setShowDelayMs(400);\npopover.setContainer(\"body\");");
    }

    private Widget tabsFadePanel() {
        TabPanel tabPanel = new TabPanel();
        String[] labels = {"Tab 4", "Tab 5", "Tab 6"};
        for (int i = 0; i < labels.length; i++) {
            String paneId = "fadePane" + i;
            TabListItem tab = new TabListItem(labels[i], paneId);
            tab.setActive(i == 0);
            tabPanel.getTabs().add(tab);
            TabPane pane = new TabPane();
            pane.getElement().setId(paneId);
            pane.setFade(true);
            pane.setActive(i == 0);
            pane.setIn(i == 0);
            pane.add(new Paragraph("Content of " + labels[i] + ", faded in and out."));
            tabPanel.getContent().add(pane);
        }
        return panel("Fading Content In/Out", tabPanel,
                "TabPane pane = new TabPane();\npane.setFade(true);\npane.setIn(true); // for the initially active pane");
    }

    private Widget tabsPillsPanel() {
        TabPanel tabPanel = new TabPanel();
        tabPanel.getTabs().removeStyleName("nav-tabs");
        tabPanel.getTabs().addStyleName("nav-pills");
        String[] labels = {"Pill 1", "Pill 2", "Pill 3"};
        for (int i = 0; i < labels.length; i++) {
            String paneId = "pillPane" + i;
            TabListItem tab = new TabListItem(labels[i], paneId);
            tab.setActive(i == 0);
            tabPanel.getTabs().add(tab);
            TabPane pane = new TabPane();
            pane.getElement().setId(paneId);
            pane.setActive(i == 0);
            pane.add(new Paragraph("Content of " + labels[i] + "."));
            tabPanel.getContent().add(pane);
        }
        return panel("Pills Instead of Tabs", tabPanel,
                "tabPanel.getTabs().removeStyleName(\"nav-tabs\");\ntabPanel.getTabs().addStyleName(\"nav-pills\");");
    }

    private Widget tabsEventsPanel() {
        TabPanel tabPanel = new TabPanel();
        final ListGroup log = new ListGroup();
        final HTML empty = new HTML("<p class='text-body-secondary mb-0'>Switch tabs to see show/shown events.</p>");
        String[] labels = {"Alpha", "Beta", "Gamma"};
        for (int i = 0; i < labels.length; i++) {
            String paneId = "eventPane" + i;
            TabListItem tab = new TabListItem(labels[i], paneId);
            tab.setActive(i == 0);
            final String name = labels[i];
            tab.addShowHandler(new TabShowHandler() {
                @Override
                public void onShow(TabShowEvent event) {
                    logEvent(log, empty, "show: " + name);
                }
            });
            tab.addShownHandler(new TabShownHandler() {
                @Override
                public void onShown(TabShownEvent event) {
                    logEvent(log, empty, "shown: " + name);
                }
            });
            tabPanel.getTabs().add(tab);
            TabPane pane = new TabPane();
            pane.getElement().setId(paneId);
            pane.setActive(i == 0);
            pane.add(new Paragraph("Content of " + labels[i] + "."));
            tabPanel.getContent().add(pane);
        }
        Button clear = new Button("Clear Log", ButtonType.DEFAULT);
        clear.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                log.clear();
                empty.setVisible(true);
            }
        });
        PanelBody body = new PanelBody();
        body.add(tabPanel);
        body.add(inline(clear));
        body.add(empty);
        body.add(log);
        return panel("Events", body,
                "tab.addShowHandler(...);\ntab.addShownHandler(...);\ntab.showTab(); // switch programmatically");
    }

    private Widget listGroupContextualPanel() {
        ListGroup group = new ListGroup();
        ListGroupItemType[] types = {ListGroupItemType.DEFAULT, ListGroupItemType.SUCCESS, ListGroupItemType.INFO,
                ListGroupItemType.WARNING, ListGroupItemType.DANGER};
        for (ListGroupItemType type : types) {
            ListGroupItem item = new ListGroupItem("ListGroupItemType." + type.name());
            item.setType(type);
            group.add(item);
        }
        ListGroup withBadges = new ListGroup();
        String[] labels = {"Inbox", "Drafts", "Sent"};
        String[] counts = {"14", "2", "108"};
        for (int i = 0; i < labels.length; i++) {
            ListGroupItem item = new ListGroupItem();
            item.addStyleName("d-flex justify-content-between align-items-center");
            item.add(new InlineHTML(labels[i]));
            Badge badge = new Badge(counts[i], LabelType.PRIMARY);
            badge.setPill(true);
            item.add(badge);
            withBadges.add(item);
        }
        return panel("Contextual Classes and Badges", stacked(group, withBadges),
                "ListGroupItem item = new ListGroupItem(\"Warning\");\nitem.setType(ListGroupItemType.WARNING);\n\nitem.addStyleName(\"d-flex justify-content-between align-items-center\");\nitem.add(pillBadge);");
    }

    private Widget listGroupLinkedPanel() {
        LinkedGroup linked = new LinkedGroup();
        String[] titles = {"First item", "Second item", "Third item"};
        for (int i = 0; i < titles.length; i++) {
            LinkedGroupItem item = new LinkedGroupItem(titles[i], "#listGroup");
            item.setActive(i == 0);
            item.add(new LinkedGroupItemText("Supporting body text for " + titles[i].toLowerCase() + "."));
            linked.add(item);
        }
        LinkedGroup disabled = new LinkedGroup();
        LinkedGroupItem enabledItem = new LinkedGroupItem("Enabled link", "#listGroup");
        LinkedGroupItem disabledItem = new LinkedGroupItem("Disabled link", "#listGroup");
        disabledItem.setDisabled(true);
        disabled.add(enabledItem);
        disabled.add(disabledItem);
        return panel("Linked Groups", stacked(linked, disabled),
                "LinkedGroupItem item = new LinkedGroupItem(\"First item\", \"#\");\nitem.setActive(true);\nitem.add(new LinkedGroupItemText(\"Supporting body text.\"));\nitem.setDisabled(true);");
    }

    private Widget navbarDarkPanel() {
        Navbar navbar = new Navbar();
        navbar.setDark(true);
        navbar.getContainer().add(new NavbarBrand("Dark brand", "#navbar"));
        navbar.getNav().add(new NavbarLink("Home", "#navbar"));
        navbar.getNav().add(new NavbarLink("Profile", "#navbar"));
        navbar.getContainer().add(new NavbarText("Signed in as Carl"));
        return panel("Dark Navbar", navbar,
                "Navbar navbar = new Navbar();\nnavbar.setDark(true); // Bootstrap 5.3 also honours data-bs-theme=\"dark\"\nnavbar.getContainer().add(new NavbarText(\"Signed in as Carl\"));");
    }

    private Widget navbarCollapsePanel() {
        Navbar navbar = new Navbar();
        navbar.getContainer().add(new NavbarBrand("Collapsing", "#navbar"));
        NavbarCollapseButton toggle = new NavbarCollapseButton("demo-navbar-collapse");
        navbar.getContainer().add(toggle);
        NavbarCollapse collapse = new NavbarCollapse();
        collapse.getElement().setId("demo-navbar-collapse");
        collapse.add(navbar.getNav());
        navbar.getNav().add(new NavbarLink("Home", "#navbar"));
        navbar.getNav().add(new NavbarLink("Link", "#navbar"));
        ListDropDown menu = new ListDropDown("Dropdown");
        menu.addItem(new DropDownItem("Action", "#navbar"));
        menu.addItem(new DropDownItem("Another action", "#navbar"));
        navbar.getNav().add(menu);
        NavbarForm form = new NavbarForm();
        Input search = new Input("search");
        search.setPlaceholder("Search");
        form.add(search);
        form.add(new Button("Go", ButtonType.PRIMARY));
        collapse.add(form);
        navbar.getContainer().add(collapse);
        return panel("Collapsing Navbar", navbar,
                "NavbarCollapseButton toggle = new NavbarCollapseButton(\"navbar-id\");\nNavbarCollapse collapse = new NavbarCollapse();\ncollapse.getElement().setId(\"navbar-id\");\ncollapse.add(navbar.getNav());");
    }

    private Widget imagesResponsivePanel() {
        Image fluid = new Image(IMG_THUMB);
        fluid.addStyleName("img-fluid");
        Div box = new Div();
        box.addStyleName("border rounded p-2");
        box.add(fluid);
        return panel("Responsive Images", box,
                "// Bootstrap 3 used .img-responsive.\nimage.addStyleName(\"img-fluid\");");
    }

    private Widget imagesFigurePanel() {
        return panel("Figures", new HTML("<figure class='figure'>"
                + "<img src='" + IMG_THUMB + "' class='figure-img img-fluid rounded' alt='Placeholder'/>"
                + "<figcaption class='figure-caption'>A caption for the image above.</figcaption></figure>"),
                "<figure class=\"figure\">\n  <img class=\"figure-img img-fluid rounded\" .../>\n  <figcaption class=\"figure-caption\">...</figcaption>\n</figure>");
    }

    private Widget cardVariantsPanel() {
        Row grid = row();
        grid.addStyleName("g-3");
        String[] tones = {"primary", "success", "warning"};
        for (String tone : tones) {
            Column cell = new Column(4);
            io.instanto.bootstrap5.client.ui.Card card = new io.instanto.bootstrap5.client.ui.Card();
            card.addStyleName("text-bg-" + tone);
            card.setTitle("text-bg-" + tone);
            card.addBody(new Paragraph("Bootstrap 5.3 colour helper applied to the whole card."));
            cell.add(card);
            grid.add(cell);
        }
        return panel("Contextual Cards", grid,
                "Card card = new Card();\ncard.addStyleName(\"text-bg-primary\");");
    }

    private Widget cardHeaderFooterPanel() {
        io.instanto.bootstrap5.client.ui.Card card = new io.instanto.bootstrap5.client.ui.Card();
        card.insert(new HTML("<div class='card-header'>Featured</div>"), 0);
        card.setTitle("Card with a header and footer");
        card.addBody(new Paragraph("A card body sits between the header and footer."));
        card.addBody(new AnchorButton("Go somewhere", "#cards", ButtonType.PRIMARY));
        card.add(new HTML("<div class='card-footer text-body-secondary'>Two days ago</div>"));
        return panel("Header and Footer", card,
                "card.insert(new HTML(\"<div class='card-header'>Featured</div>\"), 0);\ncard.add(new HTML(\"<div class='card-footer'>Two days ago</div>\"));");
    }

    private Widget inputGroupVariantsPanel() {
        InputGroup checkbox = new InputGroup();
        checkbox.add(new HTML("<span class='input-group-text'><input class='form-check-input mt-0' type='checkbox' aria-label='Checkbox for the following text input'/></span>"));
        Input checkboxInput = new Input("text");
        checkboxInput.setPlaceholder("Checkbox addon");
        checkbox.add(checkboxInput);

        InputGroup segments = new InputGroup();
        segments.add(new InputGroupAddon("$"));
        Input amount = new Input("text");
        amount.setPlaceholder("Amount");
        segments.add(amount);
        segments.add(new InputGroupAddon(".00"));

        InputGroup withDropDown = new InputGroup();
        DropDown menu = new DropDown("Currency");
        menu.getToggle().setType(ButtonType.DEFAULT);
        menu.addItem(new DropDownItem("GBP", "#inputGroups"));
        menu.addItem(new DropDownItem("EUR", "#inputGroups"));
        withDropDown.add(menu);
        Input amountTwo = new Input("text");
        amountTwo.setPlaceholder("Amount");
        withDropDown.add(amountTwo);

        return panel("Checkboxes, Multiple Addons and Dropdowns", stacked(checkbox, segments, withDropDown),
                "InputGroup group = new InputGroup();\ngroup.add(new InputGroupAddon(\"$\"));\ngroup.add(input);\ngroup.add(new InputGroupAddon(\".00\"));\n\ngroup.add(dropDown); // Bootstrap 5 drops the input-group-btn wrapper");
    }

    private Widget gridBreakpointsPanel() {
        return panel("Breakpoints", new HTML("<div class='table-responsive'><table class='table table-sm'>"
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
        return panel("Nesting", outer,
                "Column left = new Column(12);\nleft.setMediumSpan(8);\nleft.add(nestedRow);");
    }

    private Widget buttonGroupToolbarPanel() {
        ButtonToolBar toolbar = new ButtonToolBar();
        toolbar.addStyleName("gap-2");
        for (int g = 0; g < 3; g++) {
            ButtonGroup group = new ButtonGroup();
            int buttons = g == 0 ? 4 : (g == 1 ? 2 : 1);
            for (int i = 1; i <= buttons; i++) {
                group.addButton(new Button(String.valueOf(g * 4 + i), ButtonType.DEFAULT));
            }
            toolbar.addGroup(group);
        }
        ButtonGroup checkboxes = new ButtonGroup();
        String[] labels = {"Left", "Middle", "Right"};
        for (String label : labels) {
            CheckBoxButton toggle = new CheckBoxButton(label);
            toggle.setOutline(true);
            toggle.setType(ButtonType.PRIMARY);
            checkboxes.add(toggle);
        }
        return panel("Toolbars and Toggle Groups", stacked(toolbar, checkboxes),
                "ButtonToolBar toolbar = new ButtonToolBar();\ntoolbar.addGroup(group);\n\nCheckBoxButton toggle = new CheckBoxButton(\"Left\");\ntoggle.setOutline(true);");
    }

    private Widget codeVariantsPanel() {
        return panel("Variables, User Input and Sample Output", new HTML(
                "<p>Inline code: <code>&lt;section&gt;</code></p>"
                + "<p>A variable: <var>y</var> = <var>m</var><var>x</var> + <var>b</var></p>"
                + "<p>User input: press <kbd><kbd>ctrl</kbd> + <kbd>,</kbd></kbd> to open preferences.</p>"
                + "<p>Sample output: <samp>This text is meant to be treated as sample output.</samp></p>"
                + "<pre class='p-3 bg-body-tertiary rounded'><code>Row row = new Row();\nrow.add(new Column(12));</code></pre>"),
                "new Code(\"Code\");\nnew Pre(\"Preformatted text\");\n\n// var, kbd and samp have no widget; use HTML directly.");
    }

    private Widget mediaFlexPanel() {
        Div media = new Div();
        media.addStyleName("d-flex align-items-start mb-3");
        Image avatar = new Image(IMG_THUMB);
        avatar.addStyleName("me-3 rounded");
        Div body = new Div();
        body.addStyleName("flex-grow-1");
        body.add(new Heading(5, "Media heading"));
        body.add(new Paragraph("Bootstrap 5 removed the .media component. The same layout is now built from the"
                + " flex utilities, which is what MediaList and MediaBody emit."));
        media.add(avatar);
        media.add(body);
        return panel("Bootstrap 5 Flex Equivalent", media,
                "// Bootstrap 3\n<div class=\"media\"><div class=\"media-body\">...</div></div>\n\n// Bootstrap 5\n<div class=\"d-flex align-items-start\">\n  <img class=\"me-3\"/>\n  <div class=\"flex-grow-1\">...</div>\n</div>");
    }

    private Widget pageHeaderVariantsPanel() {
        PageHeader withSub = new PageHeader();
        Heading heading = new Heading(2, "Example page header ");
        heading.add(new InlineHTML("<small class='text-body-secondary'>Subtext for the header</small>"));
        withSub.add(heading);
        return panel("With Subtext", withSub,
                "PageHeader header = new PageHeader();\nHeading heading = new Heading(2, \"Example page header \");\nheading.add(new InlineHTML(\"<small class='text-body-secondary'>Subtext</small>\"));\nheader.add(heading);\n\n// Bootstrap 5 dropped .page-header; the widget emits a\n// bottom-bordered block built from utilities.");
    }

    private Widget labelBadgeMigrationPanel() {
        Widget[] pills = new Widget[6];
        LabelType[] types = {LabelType.DEFAULT, LabelType.PRIMARY, LabelType.SUCCESS, LabelType.INFO,
                LabelType.WARNING, LabelType.DANGER};
        for (int i = 0; i < types.length; i++) {
            Badge badge = new Badge(types[i].name(), types[i]);
            badge.setPill(true);
            pills[i] = badge;
        }
        return panel("Labels became Badges", stacked(inline(pills), new HTML(
                "<p class='mb-0'>Bootstrap 3 had two components, <code>.label</code> and <code>.badge</code>."
                + " Bootstrap 5 keeps only <code>.badge</code>, so the <code>Label</code> widget now renders a badge."
                + " <code>LabelType</code> is retained for source compatibility and maps onto the"
                + " <code>text-bg-*</code> helpers.</p>")),
                "new Label(\"Primary\", LabelType.PRIMARY);  // renders <span class=\"badge text-bg-primary\">\nBadge badge = new Badge(\"Primary\", LabelType.PRIMARY);\nbadge.setPill(true);                    // .rounded-pill");
    }

    private Widget wellMigrationPanel() {
        return panel("Bootstrap 5 mapping", new HTML(
                "<p class='mb-0'>Bootstrap 5 removed <code>.well</code>. The widget renders the documented"
                + " replacement — a padded, rounded block on the tertiary background — and"
                + " <code>.well-lg</code> / <code>.well-sm</code> become the padding utilities"
                + " <code>p-5</code> and <code>p-2</code>.</p>"),
                "Well well = new Well();\nwell.addStyleName(\"p-5\"); // was .well-lg\nwell.addStyleName(\"p-2\"); // was .well-sm");
    }

    private Widget responsiveUtilitiesTablePanel() {
        return panel("Display Utilities", new HTML("<div class='table-responsive'><table class='table table-sm'>"
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

    private Widget affixPanel() {
        Div demo = new Div();
        demo.addStyleName("border rounded p-2");
        demo.getElement().getStyle().setProperty("maxHeight", "180px");
        demo.getElement().getStyle().setProperty("overflowY", "auto");

        Div sticky = new Div();
        sticky.addStyleName("bg-body-tertiary border-bottom py-2 mb-2");
        sticky.add(new InlineHTML("<strong>I stay put while the box scrolls.</strong>"));
        Affix.affix(sticky);
        demo.add(sticky);

        for (int i = 1; i <= 12; i++) {
            demo.add(new Paragraph("Scrolling content, line " + i + "."));
        }

        return panel("Sticky positioning", demo,
                "Div header = new Div();\nAffix.affix(header);          // adds .sticky-top\nAffix.affix(header, 56);      // .sticky-top with a top offset\n\n// Bootstrap 4 removed the affix plugin and Bootstrap 5 has no\n// JavaScript for this at all: .sticky-top is position: sticky.\n// The widget is kept so Bootstrap 3 code still compiles, and it\n// now applies that class rather than calling a plugin.");
    }

    private Widget scrollSpyPanel() {
        String navId = "showcaseSpyNav";

        NavPills spyNav = new NavPills();
        spyNav.getElement().setId(navId);
        spyNav.addStyleName("flex-column");
        spyNav.addLink("First", "#spy-first");
        spyNav.addLink("Second", "#spy-second");
        spyNav.addLink("Third", "#spy-third");

        Div scroller = new Div();
        scroller.addStyleName("border rounded p-3");
        scroller.getElement().setId("showcaseSpyBody");
        scroller.getElement().getStyle().setProperty("maxHeight", "200px");
        scroller.getElement().getStyle().setProperty("overflowY", "auto");
        String[] ids = {"spy-first", "spy-second", "spy-third"};
        String[] titles = {"First", "Second", "Third"};
        for (int i = 0; i < ids.length; i++) {
            Heading heading = new Heading(5, titles[i]);
            heading.getElement().setId(ids[i]);
            scroller.add(heading);
            for (int line = 1; line <= 5; line++) {
                scroller.add(new Paragraph(titles[i] + " section, line " + line + "."));
            }
        }

        Row row = row();
        Column navColumn = new Column(4);
        navColumn.add(spyNav);
        Column bodyColumn = new Column(8);
        bodyColumn.add(scroller);
        row.add(navColumn);
        row.add(bodyColumn);

        ScrollSpy.scrollSpy(scroller, "#" + navId);

        return panel("Basic", row,
                "NavPills nav = new NavPills();\nnav.getElement().setId(\"spy-nav\");\n\nScrollSpy.scrollSpy(scrollingBody, \"#spy-nav\");\n\n// Bootstrap 5 spells the attributes data-bs-spy and\n// data-bs-target, which is what the widget writes.");
    }

    private Widget dialogsPanel() {
        final HTML echo = new HTML("<p class='text-body-secondary mb-0'>No dialog answered yet.</p>");

        Button alert = new Button("Alert", ButtonType.PRIMARY);
        alert.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Dialogs.alert("Drawn with the Modal widget. No jQuery, no third-party script.");
            }
        });

        Button confirm = new Button("Confirm", ButtonType.DEFAULT);
        confirm.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Dialogs.confirm("Are you sure?", new Dialogs.ConfirmCallback() {
                    @Override
                    public void callback(boolean confirmed) {
                        echo.setHTML("<p class='mb-0'>Confirm answered <strong>" + confirmed + "</strong></p>");
                    }
                });
            }
        });

        Button prompt = new Button("Prompt", ButtonType.DEFAULT);
        prompt.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Dialogs.prompt("What is your name?", new Dialogs.PromptCallback() {
                    @Override
                    public void callback(String value) {
                        echo.setHTML(value == null
                                ? "<p class='mb-0'>Prompt cancelled.</p>"
                                : "<p class='mb-0'>Prompt answered <strong>" + escapeHtml(value) + "</strong></p>");
                    }
                });
            }
        });

        PanelBody body = new PanelBody();
        body.add(inline(alert, confirm, prompt));
        body.add(echo);
        return panel("Alert, confirm and prompt", body,
                "Dialogs.alert(\"...\");\nDialogs.confirm(\"Are you sure?\", confirmed -> { ... });\nDialogs.prompt(\"What is your name?\", value -> { ... });\n\n// Bootstrap 3 got these from Bootbox, a jQuery plugin.\n// Modal already draws them, so they are built on it and\n// the library needs no jQuery at all.");
    }

    private static String escapeHtml(String value) {
        return value == null ? "" : value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private Widget datePickerPanel() {
        final HTML echo = new HTML("<p class='text-body-secondary mb-0'>Nothing picked yet.</p>");

        FormGroup dateGroup = new FormGroup();
        dateGroup.add(new FormLabel("Date"));
        final DatePicker date = new DatePicker("Pick a date");
        date.setFormat("yyyy-MM-dd");
        date.addValueChangeHandler(new ValueChangeHandler<java.util.Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<java.util.Date> event) {
                echo.setHTML(event.getValue() == null
                        ? "<p class='mb-0'>Cleared.</p>"
                        : "<p class='mb-0'>Picked <strong>" + DateTimeFormat.getFormat("yyyy-MM-dd")
                                .format(event.getValue()) + "</strong></p>");
            }
        });
        dateGroup.add(date);

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
        return panel("Basic", body,
                "DatePicker date = new DatePicker(\"Pick a date\");\ndate.setFormat(\"yyyy-MM-dd\");\ndate.addValueChangeHandler(event -> { ... });\n\nDatePicker dateTime = new DatePicker();\ndateTime.setFormat(\"yyyy-MM-dd HH:mm\");\ndateTime.setSideBySide(true);\n\n// Tempus Dominus 6 replaces the two Bootstrap 3 pickers.\n// It targets Bootstrap 5 and needs no jQuery.");
    }

    private Widget rangePanel() {
        final HTML echo = new HTML("<p class='text-body-secondary mb-0'>Volume: 50</p>");

        FormGroup group = new FormGroup();
        group.add(new FormLabel("Volume"));
        final Range volume = new Range(0, 100);
        volume.setValue(50d);
        volume.setContinuous(true);
        volume.addValueChangeHandler(new ValueChangeHandler<Double>() {
            @Override
            public void onValueChange(ValueChangeEvent<Double> event) {
                echo.setHTML("<p class='mb-0'>Volume: <strong>"
                        + (long) event.getValue().doubleValue() + "</strong></p>");
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
        return panel("Range", body,
                "Range volume = new Range(0, 100);\nvolume.setValue(50d);\nvolume.setContinuous(true);   // report while dragging\nvolume.addValueChangeHandler(event -> { ... });\n\nRange steps = new Range(0, 10);\nsteps.setStep(2);\n\n// Bootstrap 3 wrapped bootstrap-slider, a jQuery plugin.\n// Bootstrap 5 styles the native input with .form-range, so\n// for a single value there is nothing to ship.");
    }

    private Widget richTextPanel() {
        final HTML echo = new HTML("<p class='text-body-secondary mb-0'>Nothing typed yet.</p>");

        final RichTextEditor editor = new RichTextEditor(RichTextEditor.Toolbar.FULL);
        editor.setPlaceholder("Write something...");
        editor.setHTML("<p>Quill replaces the Summernote extra. "
                + "<strong>No jQuery</strong>, and it brings its own toolbar.</p>");
        editor.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                echo.setHTML("<p class='mb-0 text-body-secondary'>"
                        + event.getValue().length() + " characters of HTML</p>");
            }
        });

        Button read = new Button("Read the HTML", ButtonType.PRIMARY);
        read.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Dialogs.alert(editor.getHTML());
            }
        });

        PanelBody body = new PanelBody();
        body.add(editor);
        body.add(inline(read));
        body.add(echo);
        return panel("Editor", body,
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
        between.addValueChangeHandler(new ValueChangeHandler<Double>() {
            @Override
            public void onValueChange(ValueChangeEvent<Double> event) {
                echo.setHTML("<p class='mb-0'>Between <strong>"
                        + (long) between.getLowerValue() + "</strong> and <strong>"
                        + (long) between.getUpperValue() + "</strong></p>");
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
        body.add(new HTML("<p class='text-body-secondary'>For a plain single value prefer"
                + " <code>Range</code> on the Forms page &mdash; Bootstrap 5 styles the native control and"
                + " nothing needs shipping. This is for what that cannot do.</p>"));
        body.add(rangeGroup);
        body.add(echo);
        body.add(pipGroup);
        return panel("Beyond a native range input", body,
                "Slider between = new Slider(0, 1000);\nbetween.setRange(true);          // two handles\nbetween.setValues(200, 800);\nbetween.setTooltips(true);\nbetween.addValueChangeHandler(event -> {\n    between.getLowerValue(); between.getUpperValue();\n});\n\nSlider scaled = new Slider(0, 100);\nscaled.setPips(true);            // a scale beneath the track\n\n// Bootstrap 3 wrapped bootstrap-slider, a jQuery plugin.\n// noUiSlider has no dependencies at all.");
    }

    private Widget markdownPanel() {
        final String sample = "## Release notes\n\n"
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
        editor.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                rendered.setMarkdown(event.getValue());
            }
        });

        PanelBody editorBody = new PanelBody();
        editorBody.add(new HTML("<p class='text-body-secondary'>A textarea that keeps the Markdown as typed,"
                + " with a toolbar that inserts the syntax, a Preview tab, and the reference to hand rather"
                + " than a link away to another site.</p>"));
        editorBody.add(editor);
        editorBody.add(new HTML("<p class='form-label mt-3 mb-1'>Rendered live from the editor above</p>"));
        editorBody.add(rendered);

        return panel("Editor and renderer", editorBody,
                "MarkdownEditor editor = new MarkdownEditor(source);\neditor.setPlaceholder(\"Write Markdown...\");\nString markdown = editor.getValue();   // as typed\nString html = editor.getHTML();        // rendered\n\nMarkdownPanel view = new MarkdownPanel(source);\nview.setMarkdown(updated);\n\n// Separate from the Rich Text extra on purpose. A rich text\n// editor stores HTML, so an application whose format is\n// Markdown would convert on every save and lose exactly the\n// constructs a flexmark server understands: task lists and\n// tables. Rendering is sanitised with DOMPurify, because\n// Markdown permits raw HTML and marked does not sanitise.");
    }

    /** The template's own text, so what is shown cannot drift from what is rendered. */
    private String uiBinderSource() {
        return ProbeSource.INSTANCE.template().getText().trim();
    }

    private Widget uiBinderProbe() {
        return new UiBinderProbe();
    }

    private Panel panel(String title, Widget bodyWidget, String code) {
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

    private Widget inline(Widget... widgets) {
        PanelBody wrapper = new PanelBody();
        wrapper.setStyleName("gbm-inline-demo");
        for (Widget widget : widgets) {
            wrapper.add(widget);
        }
        return wrapper;
    }

    private Widget stacked(Widget... widgets) {
        PanelBody wrapper = new PanelBody();
        wrapper.setStyleName("gbm-stacked-demo");
        for (Widget widget : widgets) {
            wrapper.add(widget);
        }
        return wrapper;
    }

    private Widget note(String text) {
        return new HTML("<span class='text-body-secondary'>" + text + "</span>");
    }
}
