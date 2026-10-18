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

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.Alert;
import io.instanto.bootstrap5.client.ui.Anchor;
import io.instanto.bootstrap5.client.ui.AnchorButton;
import io.instanto.bootstrap5.client.ui.AnchorListItem;
import io.instanto.bootstrap5.client.ui.Badge;
import io.instanto.bootstrap5.client.ui.Breadcrumbs;
import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.client.ui.ButtonGroup;
import io.instanto.bootstrap5.client.ui.ButtonToolBar;
import io.instanto.bootstrap5.client.ui.CheckBoxButton;
import io.instanto.bootstrap5.client.ui.Column;
import io.instanto.bootstrap5.client.ui.Divider;
import io.instanto.bootstrap5.client.ui.DropDown;
import io.instanto.bootstrap5.client.ui.DropDownHeader;
import io.instanto.bootstrap5.client.ui.DropDownItem;
import io.instanto.bootstrap5.client.ui.FormGroup;
import io.instanto.bootstrap5.client.ui.FormLabel;
import io.instanto.bootstrap5.client.ui.Heading;
import io.instanto.bootstrap5.client.ui.HelpBlock;
import io.instanto.bootstrap5.client.ui.Icon;
import io.instanto.bootstrap5.client.ui.IconStack;
import io.instanto.bootstrap5.client.ui.Image;
import io.instanto.bootstrap5.client.ui.Input;
import io.instanto.bootstrap5.client.ui.InputGroup;
import io.instanto.bootstrap5.client.ui.InputGroupAddon;
import io.instanto.bootstrap5.client.ui.InputGroupButton;
import io.instanto.bootstrap5.client.ui.Jumbotron;
import io.instanto.bootstrap5.client.ui.Label;
import io.instanto.bootstrap5.client.ui.Lead;
import io.instanto.bootstrap5.client.ui.LinkedGroup;
import io.instanto.bootstrap5.client.ui.LinkedGroupItem;
import io.instanto.bootstrap5.client.ui.LinkedGroupItemText;
import io.instanto.bootstrap5.client.ui.ListDropDown;
import io.instanto.bootstrap5.client.ui.ListGroup;
import io.instanto.bootstrap5.client.ui.ListGroupItem;
import io.instanto.bootstrap5.client.ui.ListItem;
import io.instanto.bootstrap5.client.ui.MediaBody;
import io.instanto.bootstrap5.client.ui.MediaList;
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
import io.instanto.bootstrap5.client.ui.NavbarText;
import io.instanto.bootstrap5.client.ui.PageHeader;
import io.instanto.bootstrap5.client.ui.Pager;
import io.instanto.bootstrap5.client.ui.Pagination;
import io.instanto.bootstrap5.client.ui.Panel;
import io.instanto.bootstrap5.client.ui.PanelBody;
import io.instanto.bootstrap5.client.ui.PanelFooter;
import io.instanto.bootstrap5.client.ui.PanelHeader;
import io.instanto.bootstrap5.client.ui.Paragraph;
import io.instanto.bootstrap5.client.ui.Progress;
import io.instanto.bootstrap5.client.ui.ProgressBar;
import io.instanto.bootstrap5.client.ui.Row;
import io.instanto.bootstrap5.client.ui.SuggestBox;
import io.instanto.bootstrap5.client.ui.ThumbnailLink;
import io.instanto.bootstrap5.client.ui.ThumbnailPanel;
import io.instanto.bootstrap5.client.ui.VerticalButtonGroup;
import io.instanto.bootstrap5.client.ui.Well;
import io.instanto.bootstrap5.client.ui.constants.AlertType;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;
import io.instanto.bootstrap5.client.ui.constants.Emphasis;
import io.instanto.bootstrap5.client.ui.constants.IconFlip;
import io.instanto.bootstrap5.client.ui.constants.IconRotate;
import io.instanto.bootstrap5.client.ui.constants.IconSize;
import io.instanto.bootstrap5.client.ui.constants.IconType;
import io.instanto.bootstrap5.client.ui.constants.InputGroupSize;
import io.instanto.bootstrap5.client.ui.constants.LabelType;
import io.instanto.bootstrap5.client.ui.constants.ListGroupItemType;
import io.instanto.bootstrap5.client.ui.constants.PaginationSize;
import io.instanto.bootstrap5.client.ui.constants.PanelType;
import io.instanto.bootstrap5.client.ui.constants.ProgressBarType;
import io.instanto.bootstrap5.client.ui.constants.ProgressType;
import io.instanto.bootstrap5.client.ui.html.Div;
import java.util.Arrays;

/** Creates only the requested showcase section. */
final class ComponentPages {
  private static final IconType[] ICON_GALLERY = {
    IconType.ARCHIVE,
    IconType.ARROW_DOWN,
    IconType.ARROW_LEFT,
    IconType.ARROW_RIGHT,
    IconType.ARROW_UP,
    IconType.BATTERY,
    IconType.BELL,
    IconType.BOOKMARK,
    IconType.BUG,
    IconType.CALENDAR,
    IconType.CAMERA,
    IconType.CHECK2_CIRCLE,
    IconType.CLOUD,
    IconType.CODE,
    IconType.COMPASS,
    IconType.CREDIT_CARD,
    IconType.DATABASE,
    IconType.DOWNLOAD,
    IconType.ENVELOPE,
    IconType.EYE,
    IconType.FILE_EARMARK,
    IconType.FUNNEL,
    IconType.FLAG,
    IconType.FOLDER,
    IconType.GEAR,
    IconType.GIFT,
    IconType.GLOBE,
    IconType.HEART,
    IconType.HOUSE,
    IconType.IMAGE,
    IconType.INBOX,
    IconType.INFO_CIRCLE,
    IconType.KEY,
    IconType.LINK_45DEG,
    IconType.LIST,
    IconType.LOCK,
    IconType.MAP,
    IconType.PAPERCLIP,
    IconType.PENCIL,
    IconType.PERSON,
    IconType.PLAY,
    IconType.PLUS,
    IconType.QUESTION_CIRCLE,
    IconType.SEARCH,
    IconType.SEND,
    IconType.SHARE,
    IconType.SHIELD,
    IconType.SLIDERS,
    IconType.STAR,
    IconType.TABLE,
    IconType.TAG,
    IconType.TELEPHONE,
    IconType.TERMINAL,
    IconType.TRASH,
    IconType.TROPHY,
    IconType.TRUCK,
    IconType.UMBRELLA,
    IconType.UPLOAD,
    IconType.WIFI,
    IconType.WRENCH
  };

  Row createComponentSections(String token) {
    Row row = row();
    Column column = fullColumn(row);
    switch (token) {
      case "cards":
        {
          addPageHeader(column, "cards", "Cards", null);
          column.add(cardHeaderFooterPanel());
          column.add(cardVariantsPanel());
          column.add(panel("Bootstrap 5 native", sampleCard(), "Card card = new Card();"));
          break;
        }
      case "placeholders":
        {
          addPageHeader(column, "placeholders", "Placeholders", "loading skeletons");
          column.add(
              panel(
                  "Static, glow and wave",
                  NativeComponentsExamples.placeholders(),
                  "PlaceholderContainer skeleton = new PlaceholderContainer();\nskeleton.setAnimation(PlaceholderContainer.Animation.GLOW);\nskeleton.add(new Placeholder(8));"));
          break;
        }
      case "alerts":
        {
          addPageHeader(column, "alerts", "Alerts", null);
          column.add(new AlertsBasicView());
          column.add(alertDismissiblePanel());
          column.add(alertLinksPanel());
          break;
        }
      case "badges":
        {
          addPageHeader(column, "badges", "Badges", null);
          column.add(new BadgesView());
          column.add(labelBadgeMigrationPanel());
          break;
        }
      case "breadcrumbs":
        {
          addPageHeader(column, "breadcrumbs", "Breadcrumbs", null);
          column.add(breadcrumbsPanel());
          break;
        }
      case "buttonDropdowns":
        {
          addPageHeader(column, "buttonDropdowns", "Button Dropdowns", null);
          column.add(buttonDropdownBasicPanel());
          column.add(buttonDropdownSplitPanel());
          column.add(buttonDropdownDirectionsPanel());
          column.add(buttonDropdownSizesPanel());
          column.add(buttonDropdownMenuContentPanel());
          break;
        }
      case "buttonGroups":
        {
          addPageHeader(column, "buttonGroups", "Button Groups", null);
          column.add(buttonGroupsPanel());
          column.add(buttonGroupToolbarPanel());
          break;
        }
      case "dropdowns":
        {
          addPageHeader(column, "dropdowns", "Dropdowns", null);
          column.add(dropdownsPanel());
          break;
        }
      case "icons":
        {
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
          break;
        }
      case "inputGroups":
        {
          addPageHeader(column, "inputGroups", "Input Groups", null);
          column.add(inputGroupsPanel());
          column.add(inputGroupVariantsPanel());
          break;
        }
      case "jumbotron":
        {
          addPageHeader(column, "jumbotron", "Jumbotron", null);
          column.add(jumbotronPanel());
          break;
        }
      case "labels":
        {
          addPageHeader(column, "labels", "Labels", null);
          column.add(labelsPanel());
          column.add(labelBadgeMigrationPanel());
          break;
        }
      case "listGroup":
        {
          addPageHeader(column, "listGroup", "List Group", null);
          column.add(listGroupPanel());
          column.add(listGroupContextualPanel());
          column.add(listGroupLinkedPanel());
          break;
        }
      case "mediaObjects":
        {
          addPageHeader(column, "mediaObjects", "Media Objects", null);
          column.add(mediaPanel());
          column.add(mediaFlexPanel());
          break;
        }
      case "navbar":
        {
          addPageHeader(column, "navbar", "Navbar", null);
          column.add(navbarPanel());
          column.add(navbarCollapsePanel());
          column.add(navbarDarkPanel());
          break;
        }
      case "navs":
        {
          addPageHeader(column, "navs", "Navs", null);
          column.add(navsPanel());
          column.add(navStackedPanel());
          column.add(navJustifiedPanel());
          column.add(navDropdownPanel());
          column.add(navIconsPanel());
          break;
        }
      case "pageHeader":
        {
          addPageHeader(column, "pageHeader", "Page Header", null);
          PageHeader pageHeader = new PageHeader();
          pageHeader.add(new Heading(2, "Page Header"));
          column.add(panel("Basic", pageHeader, "PageHeader pageHeader = new PageHeader();"));
          column.add(pageHeaderVariantsPanel());
          break;
        }
      case "pagination":
        {
          addPageHeader(column, "pagination", "Pagination", null);
          column.add(paginationPanel());
          column.add(pagerPanel());
          break;
        }
      case "panels":
        {
          addPageHeader(column, "panels", "Panels", null);
          column.add(
              panel(
                  "Basic",
                  basicPanelExample(),
                  "Panel panel = new Panel();\npanel.add(new PanelHeader(\"Panel Header\"));\npanel.add(new PanelBody());\npanel.add(new PanelFooter(\"Panel Footer\"));"));
          column.add(
              panel(
                  "Contextual Classes",
                  inline(
                      panelExample(PanelType.INFO),
                      panelExample(PanelType.DANGER),
                      panelExample(PanelType.SUCCESS)),
                  "new Panel(PanelType.INFO);\nnew Panel(PanelType.DANGER);\nnew Panel(PanelType.SUCCESS);"));
          break;
        }
      case "progressBars":
        {
          addPageHeader(column, "progressBars", "Progress Bars", null);
          column.add(progressBasicPanel());
          column.add(progressStripedPanel());
          column.add(progressAnimatedPanel());
          column.add(progressStackedPanel());
          break;
        }
      case "suggestBox":
        {
          addPageHeader(column, "suggestBox", "SuggestBox", null);
          column.add(suggestBoxPanel());
          break;
        }
      case "thumbnails":
        {
          addPageHeader(column, "thumbnails", "Thumbnails", null);
          column.add(thumbnailsPanel());
          column.add(thumbnailLinksPanel());
          column.add(thumbnailCustomContentPanel());
          break;
        }
      case "wells":
        {
          addPageHeader(column, "wells", "Wells", null);
          column.add(wellsPanel());
          column.add(wellMigrationPanel());
          break;
        }
      default:
        throw new IllegalArgumentException("Unknown section: " + token);
    }
    return row;
  }

  private Widget alertDismissiblePanel() {
    Alert alert = alert(AlertType.SUCCESS);
    alert.setDismissible(true);
    return panel(
        "Dismissible with Handlers",
        alert,
        "Alert alert = new Alert(\"Title Description\", AlertType.SUCCESS);\nalert.setDismissible(true);\nalert.addCloseHandler(...);\nalert.addClosedHandler(...);");
  }

  private Widget alertLinksPanel() {
    Alert alert = alert(AlertType.SUCCESS);
    alert.setDismissible(true);
    alert.add(new Anchor("Link", "#"));
    return panel(
        "Links Inside",
        alert,
        "Alert alert = new Alert(\"Title Description\", AlertType.SUCCESS);\nalert.setDismissible(true);\nalert.add(new Anchor(\"Link\", \"#\"));");
  }

  private Alert alert(AlertType type) {
    Alert alert = new Alert("", type);
    alert.add(new HTML("<strong>Title</strong> Description"));
    return alert;
  }

  private Widget breadcrumbsPanel() {
    Breadcrumbs basic =
        new Breadcrumbs(new AnchorListItem("Home", "#home"), new ListItem("Current"));

    Breadcrumbs nested =
        new Breadcrumbs(
            new AnchorListItem("Projects", "#home"),
            new AnchorListItem("GWT Bootstrap", "#setup"),
            new ListItem("Bootstrap 5"));
    nested.getElement().setAttribute("aria-label", "breadcrumb");

    return panel(
        "Basic and multi-level",
        stacked(basic, nested),
        "new Breadcrumbs(new AnchorListItem(\"Home\", \"#home\"), new ListItem(\"Current\"));");
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
    return panel(
        "Basic and Sizing",
        stacked(inline(toolbar, vertical), inline(large, small)),
        "ButtonGroup group = new ButtonGroup();\ngroup.addButton(new Button(\"Button 1\"));\nlarge.setLarge(true);\nsmall.setSmall(true);\nButtonToolBar toolbar = new ButtonToolBar();");
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
    return panel(
        "Basic",
        inline(dropDown, endAligned, dropUp),
        "DropDown dropDown = new DropDown(\"Click to toggle dropdown\");\ndropDown.addItem(new DropDownItem(\"Action\", \"#\"));\ndropDown.setMenuEndAligned(true);\ndropDown.setDropUp(true);\ndropDown.setMenuMatchToggleWidth(true);");
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
    return panel(
        "Basic and Sizing",
        stacked(inputGroup, large, small),
        "InputGroup inputGroup = new InputGroup();\ninputGroup.add(new InputGroupAddon(\"@\"));\ninputGroup.setSize(InputGroupSize.LARGE);\ninputGroup.setSmall(true);");
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
    mediaBody.add(
        new Paragraph("Bootstrap 5 uses flex utilities for the old media object pattern."));
    mediaList.add(new Image(IMG_THUMB));
    mediaList.add(mediaBody);
    return panel(
        "Basic",
        mediaList,
        "MediaList mediaList = new MediaList();\nmediaList.add(new MediaBody());");
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
    return panel(
        "Basic",
        navbar,
        "Navbar navbar = new Navbar();\nnavbar.getContainer().add(new NavbarBrand(...));");
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
    return panel(
        "Basic",
        stacked(pagination, small, large),
        "Pagination pagination = new Pagination();\npagination.setPaginationSize(PaginationSize.SMALL);\npagination.setPaginationSize(PaginationSize.LARGE);");
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
    return panel(
        "Pager",
        stacked(pager, custom, aligned, iconPager),
        "new Pager();\npager.setPreviousText(\"Older\");\npager.setNextText(\"Newer\");\npager.setAlignToSides(true);\npager.setPreviousIcon(IconType.CHEVRON_LEFT);\npager.setNextIcon(IconType.CHEVRON_RIGHT);");
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
    return panel(
        "Basic",
        stacked(
            progress(40, ProgressBarType.SUCCESS),
            progress(20, ProgressBarType.INFO),
            progress(60, ProgressBarType.WARNING),
            progress(80, ProgressBarType.DANGER)),
        "new ProgressBar(40, ProgressBarType.SUCCESS);\nnew ProgressBar(20, ProgressBarType.INFO);\nnew ProgressBar(60, ProgressBarType.WARNING);\nnew ProgressBar(80, ProgressBarType.DANGER);");
  }

  private Widget progressStripedPanel() {
    return panel(
        "Striped",
        stacked(
            stripedProgress(40, ProgressBarType.SUCCESS),
            stripedProgress(20, ProgressBarType.INFO),
            stripedProgress(60, ProgressBarType.WARNING),
            stripedProgress(80, ProgressBarType.DANGER)),
        "Progress progress = new Progress();\nprogress.setType(ProgressType.STRIPED);");
  }

  private Widget progressAnimatedPanel() {
    Progress progress = stripedProgress(40, ProgressBarType.SUCCESS);
    progress.setActive(true);
    return panel(
        "Animated",
        stacked(
            new Paragraph(
                "The diagonal stripes move continuously unless the device requests reduced motion."),
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
    return panel(
        "Stacked",
        progress,
        "Progress progress = new Progress();\nprogress.add(new ProgressBar(...));\nprogress.add(new ProgressBar(...));");
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
    return panel(
        "Basic",
        inline(
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
    return panel(
        "Default and Optional Classes",
        stacked(standard, large, small),
        "new Well();\nwell.addStyleName(\"p-5\");\nwell.addStyleName(\"p-2\");");
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

  private Widget sampleCard() {
    io.instanto.bootstrap5.client.ui.Card card = new io.instanto.bootstrap5.client.ui.Card();
    card.setTitle("Card title");
    card.addBody(new Paragraph("Bootstrap 5 card content."));
    card.addBody(new AnchorButton("Card action", "#", ButtonType.PRIMARY));
    return card;
  }

  private Widget iconBasicPanel() {
    return panel(
        "Basic Use",
        inline(
            new Icon(IconType.STAR),
            new Icon(IconType.HEART),
            new Icon(IconType.ENVELOPE),
            new Icon(IconType.GEAR),
            new Icon(IconType.CLOUD),
            new Icon(IconType.CAMERA),
            new Icon(IconType.CREDIT_CARD),
            new Icon("check2-circle")),
        "new Icon(IconType.STAR);\nnew Icon(\"check2-circle\"); // or name it directly");
  }

  private Widget iconSizesPanel() {
    IconSize[] sizes = {
      IconSize.SMALL,
      IconSize.MEDIUM,
      IconSize.LARGE,
      IconSize.X_LARGE,
      IconSize.XX_LARGE,
      IconSize.XXX_LARGE
    };
    Widget[] icons = new Widget[sizes.length];
    for (int i = 0; i < sizes.length; i++) {
      Icon icon = new Icon(IconType.STAR);
      icon.setSize(sizes[i]);
      icons[i] = icon;
    }
    return panel(
        "Sizes",
        inline(icons),
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
    return panel(
        "Animation",
        inline(spin, pulse, saving),
        "icon.setSpin(true);\nicon.setPulse(true);\n\nbutton.setIcon(IconType.ARROW_REPEAT);\nbutton.setIconSpin(true);\n\n// Bootstrap Icons ships glyphs only, so gwt-bootstrap5.css\n// declares the animation. It honours prefers-reduced-motion.");
  }

  private Widget iconRotateFlipPanel() {
    IconRotate[] rotations = {
      IconRotate.NONE, IconRotate.ROTATE_90, IconRotate.ROTATE_180, IconRotate.ROTATE_270
    };
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
    return panel(
        "Rotated & Flipped",
        inline(icons),
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
    return panel(
        "Stacked & Bordered Icons",
        inline(stack, flagged, bordered),
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
    return panel(
        "Fixed Width Icons",
        list,
        "Icon icon = new Icon(IconType.ENVELOPE);\nicon.setFixedWidth(true); // aligns icons down a list");
  }

  private Widget iconEmphasisPanel() {
    Emphasis[] emphases = {
      Emphasis.DEFAULT,
      Emphasis.MUTED,
      Emphasis.PRIMARY,
      Emphasis.SUCCESS,
      Emphasis.INFO,
      Emphasis.WARNING,
      Emphasis.DANGER
    };
    Widget[] icons = new Widget[emphases.length];
    for (int i = 0; i < emphases.length; i++) {
      Icon icon = new Icon(IconType.INFO_CIRCLE);
      icon.setSize(IconSize.X_LARGE);
      icon.setEmphasis(emphases[i]);
      icons[i] = icon;
    }
    return panel(
        "Contextual Colours",
        inline(icons),
        "icon.setEmphasis(Emphasis.DANGER); // maps onto the Bootstrap 5 text-* utilities");
  }

  private Widget iconTypePanel() {
    return panel(
        "Migrating icons from Bootstrap 3",
        new HTML(
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
      box.add(
          new HTML(
              "<small class='text-body-secondary d-block text-truncate'>"
                  + type.name()
                  + "</small>"));
      cell.add(box);
      grid.add(cell);
    }
    return panel(
        "Available Icons (" + ICON_GALLERY.length + " of 2078)",
        grid,
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
    return panel(
        "Stacked Pills",
        wrapper,
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
    return panel(
        "Justified and Filled",
        body,
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
    return panel(
        "Using Dropdowns",
        tabs,
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
    return panel(
        "Navs With Icons And Fixed Width",
        pills,
        "Anchor link = pills.addLink(\"Home\", \"#\");\nlink.setIcon(IconType.HOUSE);\nlink.setIconFixedWidth(true);");
  }

  private Widget buttonDropdownBasicPanel() {
    DropDown primary = menuDropDown("Action", ButtonType.PRIMARY);
    DropDown secondary = menuDropDown("Another action", ButtonType.DEFAULT);
    DropDown danger = menuDropDown("Danger", ButtonType.DANGER);
    return panel(
        "Basic",
        inline(primary, secondary, danger),
        "DropDown dropDown = new DropDown(\"Action\");\ndropDown.getToggle().setType(ButtonType.PRIMARY);\ndropDown.addItem(new DropDownItem(\"First\", \"#\"));");
  }

  private Widget buttonDropdownSplitPanel() {
    Widget[] groups = new Widget[3];
    ButtonType[] types = {ButtonType.PRIMARY, ButtonType.SUCCESS, ButtonType.DANGER};
    String[] labels = {"Primary", "Success", "Danger"};
    for (int i = 0; i < types.length; i++) {
      groups[i] = splitDropDown(labels[i], types[i]);
    }
    return panel(
        "Split",
        inline(groups),
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
    return panel(
        "Directions and Alignment",
        inline(up, start, end, aligned),
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
    return panel(
        "Sizes and Outline",
        inline(large, normal, small, outline),
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
    return panel(
        "Headers, Dividers and Disabled Items",
        inline(dropDown),
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
    jumbotron.add(
        new Lead(
            "This is a simple hero unit, a simple jumbotron-style component for calling extra attention to featured content."));
    jumbotron.add(new HTML("<hr class='my-4'/>"));
    jumbotron.add(
        new Paragraph(
            "It uses utility classes for typography and spacing to space content out within the larger container."));
    Button learnMore = new Button("Learn more", ButtonType.PRIMARY);
    learnMore.setLarge(true);
    jumbotron.add(learnMore);
    return panel(
        "Basic",
        jumbotron,
        "Jumbotron jumbotron = new Jumbotron();\njumbotron.add(new Heading(1, \"Hello, world!\"));\njumbotron.add(new Lead(\"...\"));\n\n// Bootstrap 5 dropped the .jumbotron class; the widget now\n// renders the documented p-5 mb-4 bg-body-tertiary rounded-3 utilities.");
  }

  private Widget suggestBoxPanel() {
    MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
    oracle.addAll(
        Arrays.asList(
            "Alabama",
            "Alaska",
            "Arizona",
            "Arkansas",
            "California",
            "Colorado",
            "Connecticut",
            "Delaware",
            "Florida",
            "Georgia",
            "Hawaii",
            "Idaho",
            "Illinois",
            "Indiana",
            "Iowa",
            "Kansas",
            "Kentucky",
            "Louisiana",
            "Maine",
            "Maryland",
            "Massachusetts",
            "Michigan",
            "Minnesota",
            "Mississippi",
            "Missouri",
            "Montana",
            "Nebraska",
            "Nevada",
            "New Hampshire",
            "New Jersey",
            "New Mexico",
            "New York",
            "North Carolina",
            "North Dakota",
            "Ohio",
            "Oklahoma",
            "Oregon",
            "Pennsylvania",
            "Rhode Island",
            "South Carolina",
            "South Dakota",
            "Tennessee",
            "Texas",
            "Utah",
            "Vermont",
            "Virginia",
            "Washington",
            "West Virginia",
            "Wisconsin",
            "Wyoming"));
    SuggestBox suggestBox = new SuggestBox(oracle);
    suggestBox.getElement().setAttribute("placeholder", "Start typing a state name");
    FormGroup group = new FormGroup();
    FormLabel label = new FormLabel("Where do you live?");
    group.add(label);
    group.add(suggestBox);
    group.add(
        new HelpBlock(
            "The suggestion popup is styled with the Bootstrap 5 dropdown-menu classes."));
    return panel(
        "Basic",
        group,
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
    return panel(
        "As Links",
        grid,
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
    return panel(
        "Custom Content",
        grid,
        "ThumbnailPanel thumbnail = new ThumbnailPanel();\nthumbnail.addBody(new Image(url));\nthumbnail.addBody(new Heading(4, \"Heading\"));\nthumbnail.addBody(new Paragraph(\"Content here...\"));");
  }

  private Widget listGroupContextualPanel() {
    ListGroup group = new ListGroup();
    ListGroupItemType[] types = {
      ListGroupItemType.DEFAULT,
      ListGroupItemType.SUCCESS,
      ListGroupItemType.INFO,
      ListGroupItemType.WARNING,
      ListGroupItemType.DANGER
    };
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
    return panel(
        "Contextual Classes and Badges",
        stacked(group, withBadges),
        "ListGroupItem item = new ListGroupItem(\"Warning\");\nitem.setType(ListGroupItemType.WARNING);\n\nitem.addStyleName(\"d-flex justify-content-between align-items-center\");\nitem.add(pillBadge);");
  }

  private Widget listGroupLinkedPanel() {
    LinkedGroup linked = new LinkedGroup();
    String[] titles = {"First item", "Second item", "Third item"};
    for (int i = 0; i < titles.length; i++) {
      LinkedGroupItem item = new LinkedGroupItem(titles[i], "#listGroup");
      item.setActive(i == 0);
      item.add(
          new LinkedGroupItemText(
              "Supporting body text for " + titles[i].toLowerCase(java.util.Locale.ROOT) + "."));
      linked.add(item);
    }
    LinkedGroup disabled = new LinkedGroup();
    LinkedGroupItem enabledItem = new LinkedGroupItem("Enabled link", "#listGroup");
    LinkedGroupItem disabledItem = new LinkedGroupItem("Disabled link", "#listGroup");
    disabledItem.setDisabled(true);
    disabled.add(enabledItem);
    disabled.add(disabledItem);
    return panel(
        "Linked Groups",
        stacked(linked, disabled),
        "LinkedGroupItem item = new LinkedGroupItem(\"First item\", \"#\");\nitem.setActive(true);\nitem.add(new LinkedGroupItemText(\"Supporting body text.\"));\nitem.setDisabled(true);");
  }

  private Widget navbarDarkPanel() {
    Navbar navbar = new Navbar();
    navbar.setDark(true);
    navbar.getContainer().add(new NavbarBrand("Dark brand", "#navbar"));
    navbar.getNav().add(new NavbarLink("Home", "#navbar"));
    navbar.getNav().add(new NavbarLink("Profile", "#navbar"));
    navbar.getContainer().add(new NavbarText("Signed in as Carl"));
    return panel(
        "Dark Navbar",
        navbar,
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
    return panel(
        "Collapsing Navbar",
        navbar,
        "NavbarCollapseButton toggle = new NavbarCollapseButton(\"navbar-id\");\nNavbarCollapse collapse = new NavbarCollapse();\ncollapse.getElement().setId(\"navbar-id\");\ncollapse.add(navbar.getNav());");
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
    return panel(
        "Contextual Cards",
        grid,
        "Card card = new Card();\ncard.addStyleName(\"text-bg-primary\");");
  }

  private Widget cardHeaderFooterPanel() {
    io.instanto.bootstrap5.client.ui.Card card = new io.instanto.bootstrap5.client.ui.Card();
    card.insert(new HTML("<div class='card-header'>Featured</div>"), 0);
    card.setTitle("Card with a header and footer");
    card.addBody(new Paragraph("A card body sits between the header and footer."));
    card.addBody(new AnchorButton("Go somewhere", "#cards", ButtonType.PRIMARY));
    card.add(new HTML("<div class='card-footer text-body-secondary'>Two days ago</div>"));
    return panel(
        "Header and Footer",
        card,
        "card.insert(new HTML(\"<div class='card-header'>Featured</div>\"), 0);\ncard.add(new HTML(\"<div class='card-footer'>Two days ago</div>\"));");
  }

  private Widget inputGroupVariantsPanel() {
    InputGroup checkbox = new InputGroup();
    checkbox.add(
        new HTML(
            "<span class='input-group-text'><input class='form-check-input mt-0' type='checkbox' aria-label='Checkbox for the following text input'/></span>"));
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

    return panel(
        "Checkboxes, Multiple Addons and Dropdowns",
        stacked(checkbox, segments, withDropDown),
        "InputGroup group = new InputGroup();\ngroup.add(new InputGroupAddon(\"$\"));\ngroup.add(input);\ngroup.add(new InputGroupAddon(\".00\"));\n\ngroup.add(dropDown); // Bootstrap 5 drops the input-group-btn wrapper");
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
    return panel(
        "Toolbars and Toggle Groups",
        stacked(toolbar, checkboxes),
        "ButtonToolBar toolbar = new ButtonToolBar();\ntoolbar.addGroup(group);\n\nCheckBoxButton toggle = new CheckBoxButton(\"Left\");\ntoggle.setOutline(true);");
  }

  private Widget mediaFlexPanel() {
    Div media = new Div();
    media.addStyleName("d-flex align-items-start mb-3");
    Image avatar = new Image(IMG_THUMB);
    avatar.addStyleName("me-3 rounded");
    Div body = new Div();
    body.addStyleName("flex-grow-1");
    body.add(new Heading(5, "Media heading"));
    body.add(
        new Paragraph(
            "Bootstrap 5 removed the .media component. The same layout is now built from the"
                + " flex utilities, which is what MediaList and MediaBody emit."));
    media.add(avatar);
    media.add(body);
    return panel(
        "Bootstrap 5 Flex Equivalent",
        media,
        "// Bootstrap 3\n<div class=\"media\"><div class=\"media-body\">...</div></div>\n\n// Bootstrap 5\n<div class=\"d-flex align-items-start\">\n  <img class=\"me-3\"/>\n  <div class=\"flex-grow-1\">...</div>\n</div>");
  }

  private Widget pageHeaderVariantsPanel() {
    PageHeader withSub = new PageHeader();
    Heading heading = new Heading(2, "Example page header ");
    heading.add(
        new InlineHTML("<small class='text-body-secondary'>Subtext for the header</small>"));
    withSub.add(heading);
    return panel(
        "With Subtext",
        withSub,
        "PageHeader header = new PageHeader();\nHeading heading = new Heading(2, \"Example page header \");\nheading.add(new InlineHTML(\"<small class='text-body-secondary'>Subtext</small>\"));\nheader.add(heading);\n\n// Bootstrap 5 dropped .page-header; the widget emits a\n// bottom-bordered block built from utilities.");
  }

  private Widget labelBadgeMigrationPanel() {
    Widget[] pills = new Widget[6];
    LabelType[] types = {
      LabelType.DEFAULT,
      LabelType.PRIMARY,
      LabelType.SUCCESS,
      LabelType.INFO,
      LabelType.WARNING,
      LabelType.DANGER
    };
    for (int i = 0; i < types.length; i++) {
      Badge badge = new Badge(types[i].name(), types[i]);
      badge.setPill(true);
      pills[i] = badge;
    }
    return panel(
        "Labels became Badges",
        stacked(
            inline(pills),
            new HTML(
                "<p class='mb-0'>Bootstrap 3 had two components, <code>.label</code> and <code>.badge</code>."
                    + " Bootstrap 5 keeps only <code>.badge</code>, so the <code>Label</code> widget now renders a badge."
                    + " <code>LabelType</code> is retained for source compatibility and maps onto the"
                    + " <code>text-bg-*</code> helpers.</p>")),
        "new Label(\"Primary\", LabelType.PRIMARY);  // renders <span class=\"badge text-bg-primary\">\nBadge badge = new Badge(\"Primary\", LabelType.PRIMARY);\nbadge.setPill(true);                    // .rounded-pill");
  }

  private Widget wellMigrationPanel() {
    return panel(
        "Bootstrap 5 mapping",
        new HTML(
            "<p class='mb-0'>Bootstrap 5 removed <code>.well</code>. The widget renders the documented"
                + " replacement — a padded, rounded block on the tertiary background — and"
                + " <code>.well-lg</code> / <code>.well-sm</code> become the padding utilities"
                + " <code>p-5</code> and <code>p-2</code>.</p>"),
        "Well well = new Well();\nwell.addStyleName(\"p-5\"); // was .well-lg\nwell.addStyleName(\"p-2\"); // was .well-sm");
  }
}
