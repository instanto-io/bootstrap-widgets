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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.Column;
import io.instanto.bootstrap5.client.ui.Container;
import io.instanto.bootstrap5.client.ui.DropDownItem;
import io.instanto.bootstrap5.client.ui.Heading;
import io.instanto.bootstrap5.client.ui.Jumbotron;
import io.instanto.bootstrap5.client.ui.ListDropDown;
import io.instanto.bootstrap5.client.ui.Navbar;
import io.instanto.bootstrap5.client.ui.NavbarBrand;
import io.instanto.bootstrap5.client.ui.NavbarCollapse;
import io.instanto.bootstrap5.client.ui.NavbarCollapseButton;
import io.instanto.bootstrap5.client.ui.NavbarLink;
import io.instanto.bootstrap5.client.ui.Paragraph;
import io.instanto.bootstrap5.client.ui.Row;
import io.instanto.bootstrap5.client.ui.theme.ColorModes;
import io.instanto.bootstrap5.client.ui.theme.StandardThemes;
import io.instanto.bootstrap5.client.ui.theme.ThemeSwitcher;
import io.instanto.bootstrap5.client.ui.theme.Themes;
import io.instanto.bootstrap5.themes.client.BootswatchThemes;

public class ShowcaseEntryPoint implements EntryPoint {

  private final CssPages cssPages = new CssPages();
  private final ComponentPages componentPages = new ComponentPages();
  private final JavaScriptPages javaScriptPages = new JavaScriptPages();
  private final IntegrationPages integrationPages = new IntegrationPages();
  private final java.util.function.Supplier<Widget> setupFactory;

  public ShowcaseEntryPoint() {
    this(SetupView::new);
  }

  public ShowcaseEntryPoint(java.util.function.Supplier<Widget> setupFactory) {
    this.setupFactory = setupFactory;
  }

  private static final String[] CSS_SECTIONS = {
    "buttons",
    "code",
    "forms",
    "gridSystem",
    "images",
    "responsiveUtilities",
    "tables",
    "typography"
  };
  private static final String[] CSS_LABELS = {
    "Buttons",
    "Code",
    "Forms",
    "Grid System",
    "Images",
    "Responsive Utilities",
    "Tables",
    "Typography"
  };
  private static final String[] COMPONENT_SECTIONS = {
    "cards",
    "placeholders",
    "alerts",
    "badges",
    "breadcrumbs",
    "buttonDropdowns",
    "buttonGroups",
    "dropdowns",
    "icons",
    "inputGroups",
    "jumbotron",
    "labels",
    "listGroup",
    "mediaObjects",
    "navbar",
    "navs",
    "pageHeader",
    "pagination",
    "panels",
    "progressBars",
    "suggestBox",
    "thumbnails",
    "wells"
  };
  private static final String[] COMPONENT_LABELS = {
    "Cards",
    "Placeholders",
    "Alerts",
    "Badges",
    "Breadcrumbs",
    "Button Dropdowns",
    "Button Groups",
    "Dropdowns",
    "Icons",
    "Input Groups",
    "Jumbotron",
    "Labels",
    "List Group",
    "Media Objects",
    "Navbar",
    "Navs",
    "Page Header",
    "Pagination",
    "Panels",
    "Progress Bars",
    "SuggestBox",
    "Thumbnails",
    "Wells"
  };
  private static final String[] JS_SECTIONS = {
    "toasts",
    "offcanvas",
    "dialogs",
    "affix",
    "carousel",
    "collapse",
    "modals",
    "popover",
    "scrollspy",
    "tabs",
    "tooltips"
  };
  private static final String[] JS_LABELS = {
    "Toasts",
    "Offcanvas",
    "Dialogs",
    "Affix",
    "Carousel",
    "Collapse",
    "Modals",
    "Popover",
    "ScrollSpy",
    "Tabs",
    "Tooltips"
  };
  private static final String[] INTEGRATION_SECTIONS = {
    "datePicker",
    "richText",
    "markdown",
    "slider",
    "searchableSelect",
    "dataGrid",
    "sortableLists",
    "dashboard",
    "gallery",
    "unsupportedExtras"
  };
  private static final String[] INTEGRATION_LABELS = {
    "DatePicker",
    "Rich Text",
    "Markdown",
    "Slider",
    "Searchable Select",
    "Data Grid",
    "Sortable Lists",
    "Dashboard",
    "Gallery",
    "About Integrations"
  };

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
    History.addValueChangeHandler(
        new ValueChangeHandler<String>() {
          @Override
          public void onValueChange(ValueChangeEvent<String> event) {
            renderToken(content, modalRoot, event.getValue());
          }
        });
    renderToken(content, modalRoot, History.getToken());
  }

  private void renderToken(Container content, RootPanel root, String token) {
    javaScriptPages.clearModals();
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
      return panel(
          "UiBinder",
          new UiBinderDemo(),
          "@UiField Button counter;\n@UiHandler(\"counter\")\nvoid onCounterClick(ClickEvent event) { ... }");
    }
    if (contains(CSS_SECTIONS, token)) {
      return cssPages.createCssSections(token);
    }
    if (contains(COMPONENT_SECTIONS, token)) {
      return componentPages.createComponentSections(token);
    }
    if (contains(JS_SECTIONS, token)) {
      return javaScriptPages.createJavaScriptSections(root, token);
    }
    if (contains(INTEGRATION_SECTIONS, token)) {
      return integrationPages.createIntegrationSections(token);
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

  private Navbar createNavbar() {
    Navbar navbar = new Navbar();
    navbar.addStyleName("sticky-top");
    // Navbar colour variables require a local mode, not just the document's mode.
    navbar.getElement().setAttribute(ColorModes.ATTRIBUTE, ColorModes.isDark() ? "dark" : "light");
    ColorModes.addColorModeChangeHandler(
        mode ->
            navbar
                .getElement()
                .setAttribute(ColorModes.ATTRIBUTE, ColorModes.isDark() ? "dark" : "light"));
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
    navbar
        .getNav()
        .add(
            dropdown(
                "View Javadoc",
                new String[] {"Core API", "Integrations API"},
                new String[] {
                  docsBase + "apidocs/index.html", docsBase + "extras-apidocs/index.html"
                }));
    boolean teaVm = "teavm".equals(GWT.getModuleName());
    navbar
        .getNav()
        .add(
            dropdown(
                "Other Builds",
                new String[] {
                  "Bootstrap 3 Showcase (GWT)",
                  "Bootstrap 3 Showcase (TeaVM)",
                  teaVm ? "Bootstrap 5 Showcase (GWT)" : "Bootstrap 5 Showcase (TeaVM)"
                },
                teaVm
                    ? new String[] {"./", "./teavm.html", "bootstrap5/"}
                    : new String[] {"../", "../teavm.html", "../teavm-bootstrap5.html"}));
    navbar
        .getNav()
        .add(new NavbarLink("Fork on GitHub", "https://github.com/instanto-io/bootstrap-widgets"));
    navbar.getNav().add(new ThemeSwitcher());
    return navbar;
  }

  private ListDropDown dropdown(String text, String[] labels, String[] hrefs) {
    ListDropDown dropDown = new ListDropDown(text);
    for (int i = 0; i < labels.length; i++) {
      String href =
          hrefs[i].startsWith("http") || hrefs[i].contains("/") ? hrefs[i] : "#" + hrefs[i];
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
    jumbotron.add(
        new Paragraph("Explore Bootstrap components, layouts and interactive behaviour."));
    jumbotron.add(
        new Paragraph(
            "Components and Interactive cover native Bootstrap capabilities. Integrations adds editors, advanced selection, data grids, sortable lists, dashboards and image galleries. Each page includes working examples and Java code."));
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

  /** The template's own text, so what is shown cannot drift from what is rendered. */
  private String uiBinderSource() {
    return ProbeSource.INSTANCE.template().getText().trim();
  }

  private Widget uiBinderProbe() {
    return new UiBinderProbe();
  }

  private Row section(String id, String title, String subText) {
    Row row = row();
    Column column = fullColumn(row);
    addPageHeader(column, id, title, subText);
    return row;
  }
}
