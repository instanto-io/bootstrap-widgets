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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
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
import io.instanto.bootstrap5.client.ui.Affix;
import io.instanto.bootstrap5.client.ui.Anchor;
import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.client.ui.Carousel;
import io.instanto.bootstrap5.client.ui.CarouselCaption;
import io.instanto.bootstrap5.client.ui.CarouselControl;
import io.instanto.bootstrap5.client.ui.CarouselIndicator;
import io.instanto.bootstrap5.client.ui.CarouselIndicators;
import io.instanto.bootstrap5.client.ui.CarouselSlide;
import io.instanto.bootstrap5.client.ui.Collapse;
import io.instanto.bootstrap5.client.ui.Column;
import io.instanto.bootstrap5.client.ui.Dialogs;
import io.instanto.bootstrap5.client.ui.Heading;
import io.instanto.bootstrap5.client.ui.ListGroup;
import io.instanto.bootstrap5.client.ui.ListGroupItem;
import io.instanto.bootstrap5.client.ui.Modal;
import io.instanto.bootstrap5.client.ui.ModalFooter;
import io.instanto.bootstrap5.client.ui.ModalSize;
import io.instanto.bootstrap5.client.ui.NavPills;
import io.instanto.bootstrap5.client.ui.Panel;
import io.instanto.bootstrap5.client.ui.PanelBody;
import io.instanto.bootstrap5.client.ui.PanelCollapse;
import io.instanto.bootstrap5.client.ui.PanelGroup;
import io.instanto.bootstrap5.client.ui.PanelHeader;
import io.instanto.bootstrap5.client.ui.Paragraph;
import io.instanto.bootstrap5.client.ui.Popover;
import io.instanto.bootstrap5.client.ui.Row;
import io.instanto.bootstrap5.client.ui.ScrollSpy;
import io.instanto.bootstrap5.client.ui.TabContent;
import io.instanto.bootstrap5.client.ui.TabListItem;
import io.instanto.bootstrap5.client.ui.TabPane;
import io.instanto.bootstrap5.client.ui.TabPanel;
import io.instanto.bootstrap5.client.ui.Tooltip;
import io.instanto.bootstrap5.client.ui.TooltipHelpBlock;
import io.instanto.bootstrap5.client.ui.Well;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;
import io.instanto.bootstrap5.client.ui.constants.Placement;
import io.instanto.bootstrap5.client.ui.constants.Trigger;
import io.instanto.bootstrap5.client.ui.html.Div;
import java.util.ArrayList;
import java.util.List;

/** Creates only the requested showcase section. */
final class JavaScriptPages {
  private final List<Modal> pageModals = new ArrayList<>();

  void clearModals() {
    for (Modal modal : pageModals) modal.removeFromParent();
    pageModals.clear();
  }

  Row createJavaScriptSections(RootPanel root, String token) {
    Row row = row();
    Column column = fullColumn(row);
    switch (token) {
      case "toasts":
        {
          addPageHeader(column, "toasts", "Toasts", "notifications and independent stacks");
          column.add(
              panel(
                  "Show, dismiss and auto-hide",
                  NativeComponentsExamples.toasts(),
                  "ToastContainer notifications = new ToastContainer();\n// Mount the live region before sending a notification.\nnotifications.notify(\"Saved\", \"Your changes were saved.\", 5000);"));
          break;
        }
      case "offcanvas":
        {
          addPageHeader(column, "offcanvas", "Offcanvas", "sliding detail and filter panels");
          column.add(
              panel(
                  "Placement, backdrop and keyboard",
                  NativeComponentsExamples.offcanvas(),
                  "Offcanvas panel = new Offcanvas();\npanel.setPlacement(OffcanvasPlacement.END);\npanel.add(new OffcanvasHeader(\"Details\"));\npanel.add(new OffcanvasBody(\"Panel content\"));\n// Mount, then show from a button's click handler.\npanel.show();"));
          break;
        }
      case "dialogs":
        {
          addPageHeader(column, "dialogs", "Dialogs", "alert, confirm and prompt");
          column.add(dialogsPanel());
          break;
        }
      case "affix":
        {
          addPageHeader(column, "affix", "Affix", "sticky positioning");
          column.add(affixPanel());
          break;
        }
      case "carousel":
        {
          addPageHeader(column, "carousel", "Carousel", null);
          column.add(
              panel(
                  "Basic",
                  carousel(),
                  "Carousel carousel = new Carousel();\ncarousel.addSlide(...);"));
          column.add(carouselCaptionsPanel());
          column.add(carouselOptionsPanel());
          break;
        }
      case "collapse":
        {
          addPageHeader(column, "collapse", "Collapse", null);
          column.add(collapsePanel());
          column.add(accordionPanel());
          break;
        }
      case "modals":
        {
          addPageHeader(column, "modals", "Modals", null);
          column.add(modalPanel(root));
          column.add(modalSizesPanel(root));
          column.add(modalOnlyOnePanel(root));
          column.add(modalEventsPanel(root));
          break;
        }
      case "popover":
        {
          addPageHeader(column, "popover", "Popover", null);
          column.add(popoverPlacementsPanel());
          column.add(popoverTriggersPanel());
          column.add(popoverOptionsPanel());
          break;
        }
      case "scrollspy":
        {
          addPageHeader(column, "scrollspy", "ScrollSpy", null);
          column.add(scrollSpyPanel());
          break;
        }
      case "tabs":
        {
          addPageHeader(column, "tabs", "Tabs", null);
          column.add(tabsPanel());
          column.add(tabsFadePanel());
          column.add(tabsPillsPanel());
          column.add(tabsEventsPanel());
          break;
        }
      case "tooltips":
        {
          addPageHeader(column, "tooltips", "Tooltips", null);
          column.add(tooltipPlacementsPanel());
          column.add(tooltipTriggersPanel());
          column.add(tooltipOptionsPanel());
          break;
        }
      default:
        throw new IllegalArgumentException("Unknown section: " + token);
    }
    return row;
  }

  private Widget collapsePanel() {
    Collapse collapse = new Collapse();
    collapse.getElement().setId("collapseExample");
    collapse.add(new Well());
    Button button = new Button("Toggle collapse", ButtonType.PRIMARY);
    button.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            collapse.toggle();
          }
        });
    return panel(
        "Basic",
        inline(button, collapse),
        "Collapse collapse = new Collapse();\ncollapse.toggle();");
  }

  private Widget accordionPanel() {
    PanelGroup accordion = new PanelGroup();
    accordion.getElement().setId("accordion");
    accordion.add(accordionItem("collapseOne", "Collapse Group #1", true));
    accordion.add(accordionItem("collapseTwo", "Collapse Group #2", false));
    accordion.add(accordionItem("collapseThree", "Collapse Group #3", false));
    return panel(
        "Accordion Example using PanelCollapse",
        accordion,
        "PanelGroup accordion = new PanelGroup();\naccordion.add(panelWithPanelCollapse(...));");
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
    close.addClickHandler(
        new ClickHandler() {
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
    show.addClickHandler(
        new ClickHandler() {
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
    return panel(
        "Basic",
        tabPanel,
        "TabPanel tabPanel = new TabPanel();\ntabPanel.getTabs().add(new TabListItem(...));");
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
    CarouselSlide firstSlide =
        new CarouselSlide(
            new HTML(
                "<div class='d-flex align-items-center justify-content-center text-bg-primary rounded' style='height: 12rem;'>First slide</div>"));
    firstSlide.setActive(true);
    CarouselCaption caption = new CarouselCaption();
    caption.add(new Heading(5, "First slide"));
    firstSlide.add(caption);
    carousel.addSlide(firstSlide);
    carousel.addSlide(
        new CarouselSlide(
            new HTML(
                "<div class='d-flex align-items-center justify-content-center text-bg-success rounded' style='height: 12rem;'>Second slide</div>")));
    carousel.add(new CarouselControl(carouselId, true));
    carousel.add(new CarouselControl(carouselId, false));
    return carousel;
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
      CarouselSlide slide =
          new CarouselSlide(
              new HTML(
                  "<div class='d-flex align-items-center "
                      + "justify-content-center text-bg-"
                      + tones[i]
                      + " rounded' style='height: 14rem;'>Slide "
                      + (i + 1)
                      + "</div>"));
      slide.setActive(i == 0);
      CarouselCaption caption = new CarouselCaption();
      caption.add(new Heading(5, "Slide " + (i + 1)));
      caption.add(new Paragraph("Caption!"));
      slide.add(caption);
      carousel.addSlide(slide);
    }
    carousel.add(new CarouselControl(carouselId, true));
    carousel.add(new CarouselControl(carouselId, false));
    return panel(
        "With Captions",
        carousel,
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
      CarouselSlide slide =
          new CarouselSlide(
              new HTML(
                  "<div class='d-flex align-items-center "
                      + "justify-content-center text-bg-"
                      + tones[i]
                      + " rounded' style='height: 10rem;'>Slide "
                      + (i + 1)
                      + " — 1.5s interval</div>"));
      slide.setActive(i == 0);
      carousel.addSlide(slide);
    }
    carousel.add(new CarouselControl(carouselId, true));
    carousel.add(new CarouselControl(carouselId, false));

    Button previous = new Button("Previous", ButtonType.DEFAULT);
    previous.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            carousel.goToPrev();
          }
        });
    Button next = new Button("Next", ButtonType.DEFAULT);
    next.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            carousel.goToNext();
          }
        });
    Button pause = new Button("Pause", ButtonType.DEFAULT);
    pause.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            carousel.pauseCarousel();
          }
        });
    Button cycle = new Button("Cycle", ButtonType.DEFAULT);
    cycle.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            carousel.cycleCarousel();
          }
        });
    PanelBody body = new PanelBody();
    body.add(carousel);
    body.add(inline(previous, next, pause, cycle));
    return panel(
        "Options and Programmatic Control",
        body,
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
      modal.addToBody(
          new Paragraph("This modal was created with ModalSize." + sizes[i].name() + "."));
      ModalFooter footer = new ModalFooter();
      footer.add(closeButton(modal));
      modal.addFooter(footer);
      root.add(modal);
      pageModals.add(modal);
      Button open = new Button(labels[i], ButtonType.PRIMARY);
      open.addClickHandler(
          new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              modal.show();
            }
          });
      buttons[i] = open;
    }
    return panel(
        "Sizes", inline(buttons), "Modal modal = new Modal();\nmodal.setSize(ModalSize.LARGE);");
  }

  private Widget modalOnlyOnePanel(RootPanel root) {
    final Modal first = new Modal();
    first.setTitle("First modal");
    first.setHideOtherModals(true);
    first.addToBody(
        new Paragraph(
            "Opening the second modal hides this one, because both set "
                + "setHideOtherModals(true)."));
    final Modal second = new Modal();
    second.setTitle("Second modal");
    second.setHideOtherModals(true);
    second.addToBody(new Paragraph("The first modal was hidden when this one opened."));

    Button openSecond = new Button("Open the second modal", ButtonType.PRIMARY);
    openSecond.addClickHandler(
        new ClickHandler() {
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
    open.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            first.show();
          }
        });
    return panel("Only One Modal Active", inline(open), "modal.setHideOtherModals(true);");
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

    modal.addShowHandler(
        new ModalShowHandler() {
          @Override
          public void onShow(ModalShowEvent event) {
            logEvent(log, empty, "show");
          }
        });
    modal.addShownHandler(
        new ModalShownHandler() {
          @Override
          public void onShown(ModalShownEvent event) {
            logEvent(log, empty, "shown");
          }
        });
    modal.addHideHandler(
        new ModalHideHandler() {
          @Override
          public void onHide(ModalHideEvent event) {
            logEvent(log, empty, "hide");
          }
        });
    modal.addHiddenHandler(
        new ModalHiddenHandler() {
          @Override
          public void onHidden(ModalHiddenEvent event) {
            logEvent(log, empty, "hidden");
          }
        });

    Button open = new Button("Open and watch the log", ButtonType.PRIMARY);
    open.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            modal.show();
          }
        });
    Button clear = new Button("Clear Log", ButtonType.DEFAULT);
    clear.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            log.clear();
            empty.setVisible(true);
          }
        });
    PanelBody body = new PanelBody();
    body.add(inline(open, clear));
    body.add(logBody);
    return panel(
        "Events",
        body,
        "modal.addShowHandler(...);\nmodal.addShownHandler(...);\nmodal.addHideHandler(...);\nmodal.addHiddenHandler(...);");
  }

  private void logEvent(ListGroup log, HTML empty, String name) {
    empty.setVisible(false);
    log.add(new ListGroupItem(name));
  }

  private Button closeButton(final Modal modal) {
    Button close = new Button("Close", ButtonType.DEFAULT);
    close.addClickHandler(
        new ClickHandler() {
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
      Button button =
          new Button(
              "I have a Tooltip! (" + placements[i].name().toLowerCase(java.util.Locale.ROOT) + ")",
              ButtonType.DEFAULT);
      Tooltip tooltip =
          new Tooltip(
              button, "Tooltip on " + placements[i].name().toLowerCase(java.util.Locale.ROOT));
      tooltip.setPlacement(placements[i]);
      widgets[i] = tooltip.asWidget();
    }
    return panel(
        "Basic",
        inline(widgets),
        "Tooltip tooltip = new Tooltip(button, \"Tooltip text\");\ntooltip.setPlacement(Placement.TOP);");
  }

  private Widget tooltipTriggersPanel() {
    Trigger[] triggers = {Trigger.HOVER, Trigger.CLICK, Trigger.FOCUS};
    Widget[] widgets = new Widget[triggers.length + 1];
    for (int i = 0; i < triggers.length; i++) {
      Button button =
          new Button(
              "I have a Tooltip! (on "
                  + triggers[i].name().toLowerCase(java.util.Locale.ROOT)
                  + ")",
              ButtonType.DEFAULT);
      Tooltip tooltip =
          new Tooltip(
              button, "Triggered on " + triggers[i].name().toLowerCase(java.util.Locale.ROOT));
      tooltip.setTrigger(triggers[i]);
      widgets[i] = tooltip.asWidget();
    }
    Button manualTarget = new Button("I have a Tooltip! (on manual)", ButtonType.DEFAULT);
    final Tooltip manual = new Tooltip(manualTarget, "Shown and hidden from code");
    manual.setTrigger(Trigger.MANUAL);
    Button show = new Button("Show", ButtonType.PRIMARY);
    show.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            manual.show();
          }
        });
    Button hide = new Button("Hide", ButtonType.PRIMARY);
    hide.addClickHandler(
        new ClickHandler() {
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
    return panel(
        "Triggers",
        inline(widgets),
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
    return panel(
        "Options",
        inline(
            delayedTooltip.asWidget(),
            htmlTooltip.asWidget(),
            unanimatedTooltip.asWidget(),
            helpBlock.asWidget()),
        "tooltip.setShowDelayMs(500);\ntooltip.setHideDelayMs(250);\ntooltip.setIsHtml(true);\ntooltip.setIsAnimated(false);");
  }

  private Widget popoverPlacementsPanel() {
    Placement[] placements = {Placement.TOP, Placement.LEFT, Placement.BOTTOM, Placement.RIGHT};
    Widget[] widgets = new Widget[placements.length];
    for (int i = 0; i < placements.length; i++) {
      Button button =
          new Button(
              "I have a Popover! (" + placements[i].name().toLowerCase(java.util.Locale.ROOT) + ")",
              ButtonType.DEFAULT);
      Popover popover =
          new Popover(
              button,
              "Popover on " + placements[i].name().toLowerCase(java.util.Locale.ROOT),
              "And here is some content to go with it.");
      popover.setPlacement(placements[i]);
      widgets[i] = popover.asWidget();
    }
    return panel(
        "Basic",
        inline(widgets),
        "Popover popover = new Popover(button, \"Title\", \"Content\");\npopover.setPlacement(Placement.TOP);");
  }

  private Widget popoverTriggersPanel() {
    Trigger[] triggers = {Trigger.HOVER, Trigger.CLICK, Trigger.FOCUS};
    Widget[] widgets = new Widget[triggers.length + 1];
    for (int i = 0; i < triggers.length; i++) {
      Button button =
          new Button(
              "I have a Popover! (on "
                  + triggers[i].name().toLowerCase(java.util.Locale.ROOT)
                  + ")",
              ButtonType.DEFAULT);
      Popover popover =
          new Popover(
              button,
              "Trigger",
              "Triggered on " + triggers[i].name().toLowerCase(java.util.Locale.ROOT));
      popover.setTrigger(triggers[i]);
      widgets[i] = popover.asWidget();
    }
    Button manualTarget = new Button("I have a Popover! (on manual)", ButtonType.DEFAULT);
    final Popover manual = new Popover(manualTarget, "Manual", "Shown and hidden from code");
    manual.setTrigger(Trigger.MANUAL);
    Button show = new Button("Show", ButtonType.PRIMARY);
    show.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            manual.show();
          }
        });
    Button hide = new Button("Hide", ButtonType.PRIMARY);
    hide.addClickHandler(
        new ClickHandler() {
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
    return panel(
        "Triggers",
        inline(widgets),
        "popover.setTrigger(Trigger.CLICK);\n\npopover.setTrigger(Trigger.MANUAL);\npopover.show();\npopover.hide();");
  }

  private Widget popoverOptionsPanel() {
    Button htmlTarget = new Button("HTML content", ButtonType.DEFAULT);
    Popover html =
        new Popover(
            htmlTarget,
            "Rich content",
            "<p>Popovers accept <strong>markup</strong> when <code>setIsHtml(true)</code>.</p>");
    html.setIsHtml(true);

    Button delayed = new Button("Delayed 400ms", ButtonType.DEFAULT);
    Popover delayedPopover = new Popover(delayed, "Delayed", "Appeared after 400ms");
    delayedPopover.setShowDelayMs(400);

    Button contained = new Button("Attached to body", ButtonType.DEFAULT);
    Popover containedPopover =
        new Popover(contained, "Container", "Rendered as a child of document.body");
    containedPopover.setContainer("body");

    return panel(
        "Options",
        inline(html.asWidget(), delayedPopover.asWidget(), containedPopover.asWidget()),
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
    return panel(
        "Fading Content In/Out",
        tabPanel,
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
    return panel(
        "Pills Instead of Tabs",
        tabPanel,
        "tabPanel.getTabs().removeStyleName(\"nav-tabs\");\ntabPanel.getTabs().addStyleName(\"nav-pills\");");
  }

  private Widget tabsEventsPanel() {
    TabPanel tabPanel = new TabPanel();
    final ListGroup log = new ListGroup();
    final HTML empty =
        new HTML("<p class='text-body-secondary mb-0'>Switch tabs to see show/shown events.</p>");
    String[] labels = {"Alpha", "Beta", "Gamma"};
    for (int i = 0; i < labels.length; i++) {
      String paneId = "eventPane" + i;
      TabListItem tab = new TabListItem(labels[i], paneId);
      tab.setActive(i == 0);
      final String name = labels[i];
      tab.addShowHandler(
          new TabShowHandler() {
            @Override
            public void onShow(TabShowEvent event) {
              logEvent(log, empty, "show: " + name);
            }
          });
      tab.addShownHandler(
          new TabShownHandler() {
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
    clear.addClickHandler(
        new ClickHandler() {
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
    return panel(
        "Events",
        body,
        "tab.addShowHandler(...);\ntab.addShownHandler(...);\ntab.showTab(); // switch programmatically");
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

    return panel(
        "Sticky positioning",
        demo,
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

    return panel(
        "Basic",
        row,
        "NavPills nav = new NavPills();\nnav.getElement().setId(\"spy-nav\");\n\nScrollSpy.scrollSpy(scrollingBody, \"#spy-nav\");\n\n// Bootstrap 5 spells the attributes data-bs-spy and\n// data-bs-target, which is what the widget writes.");
  }

  private Widget dialogsPanel() {
    final HTML echo = new HTML("<p class='text-body-secondary mb-0'>No dialog answered yet.</p>");

    Button alert = new Button("Alert", ButtonType.PRIMARY);
    alert.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            Dialogs.alert("Drawn with the Modal widget. No jQuery, no third-party script.");
          }
        });

    Button confirm = new Button("Confirm", ButtonType.DEFAULT);
    confirm.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            Dialogs.confirm(
                "Are you sure?",
                new Dialogs.ConfirmCallback() {
                  @Override
                  public void callback(boolean confirmed) {
                    echo.setHTML(
                        "<p class='mb-0'>Confirm answered <strong>" + confirmed + "</strong></p>");
                  }
                });
          }
        });

    Button prompt = new Button("Prompt", ButtonType.DEFAULT);
    prompt.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            Dialogs.prompt(
                "What is your name?",
                new Dialogs.PromptCallback() {
                  @Override
                  public void callback(String value) {
                    echo.setHTML(
                        value == null
                            ? "<p class='mb-0'>Prompt cancelled.</p>"
                            : "<p class='mb-0'>Prompt answered <strong>"
                                + escapeHtml(value)
                                + "</strong></p>");
                  }
                });
          }
        });

    PanelBody body = new PanelBody();
    body.add(inline(alert, confirm, prompt));
    body.add(echo);
    return panel(
        "Alert, confirm and prompt",
        body,
        "Dialogs.alert(\"...\");\nDialogs.confirm(\"Are you sure?\", confirmed -> { ... });\nDialogs.prompt(\"What is your name?\", value -> { ... });\n\n// Bootstrap 3 got these from Bootbox, a jQuery plugin.\n// Modal already draws them, so they are built on it and\n// the library needs no jQuery at all.");
  }

  private static String escapeHtml(String value) {
    return value == null
        ? ""
        : value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }
}
