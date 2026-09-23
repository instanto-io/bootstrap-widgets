package io.instanto.bootstrap.testing.bootstrap5;

import static io.instanto.webapp.testkit.dom.Expect.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.client.ui.ButtonGroup;
import io.instanto.bootstrap5.client.ui.DropDown;
import io.instanto.bootstrap5.client.ui.DropDownItem;
import io.instanto.bootstrap5.client.ui.DropDownMenu;
import io.instanto.bootstrap5.client.ui.base.BootstrapEventBridge;
import io.instanto.bootstrap5.client.ui.constants.Toggle;
import io.instanto.cucumber.tea.AfterScenario;
import io.instanto.cucumber.tea.BeforeScenario;
import io.instanto.cucumber.tea.CucumberSuite;
import io.instanto.cucumber.tea.Given;
import io.instanto.cucumber.tea.Then;
import io.instanto.cucumber.tea.When;
import io.instanto.webapp.testkit.dom.Dom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.teavm.jso.browser.Location;
import org.teavm.jso.dom.html.HTMLElement;

@CucumberSuite(value = "features/dropdowns.feature")
public class Bootstrap5DropDownSteps {
  private RootPanel host;
  private DropDown dropDown;
  private Button toggle;
  private DropDownMenu menu;
  private DropDownItem action;
  private DropDownItem disabledItem;
  private Button outside;
  private Button splitPrimary;
  private Button splitToggle;
  private DropDownMenu splitMenu;
  private final List<String> events = new ArrayList<>();
  private int disabledActions;
  private int primaryActions;
  private String recordedLocation;

  @BeforeScenario
  public void createHost() {
    Dom.reset();
    Bootstrap5TestRuntime.initialise();
    Dom.container().setAttribute("id", "bootstrap5-dropdown-test-host");
    host = RootPanel.get("bootstrap5-dropdown-test-host");
  }

  @AfterScenario
  public void removeFixture() {
    if (dropDown != null) {
      BootstrapEventBridge.unbindAll(dropDown.getElement());
    }
    if (host != null) {
      host.clear();
    }
    Dom.reset();
  }

  @Given("fixture {string} is mounted")
  public void mountedFixture(String fixture) {
    if ("behaviour/dropdown/dropup".equals(fixture)) {
      createBasicDropDown();
      dropDown.setDropUp(true);
      // Leave room above the toggle so Popper does not flip the menu below it.
      dropDown.setMarginTop(200);
      return;
    }
    if ("behaviour/dropdown/aligned-width".equals(fixture)) {
      createBasicDropDown();
      dropDown.setWidth("240px");
      toggle.setWidth("100%");
      dropDown.setMenuMatchToggleWidth(true);
      return;
    }
    if ("behaviour/dropdown/basic".equals(fixture)
        || "behaviour/dropdown/disabled-item".equals(fixture)) {
      createBasicDropDown();
      recordedLocation = Location.current().getFullURL();
      return;
    }
    if ("behaviour/dropdown/split-button".equals(fixture)) {
      createSplitDropDown();
      return;
    }
    throw new IllegalArgumentException("Unknown fixture: " + fixture);
  }

  @Given("Bootstrap 3 showcase route {string} section {string} defines the baseline")
  public void referenceShowcaseDefinesBaseline(String route, String section) {
    assertFalse(route.isEmpty());
    assertFalse(section.isEmpty());
  }

  private void createBasicDropDown() {
    FlowPanel fixture = new FlowPanel();
    dropDown = tagged(new DropDown("Touch menu"), "behaviour/dropdown/basic");
    toggle = tagged(dropDown.getToggle(), "behaviour/dropdown/toggle");
    menu = tagged(dropDown.getMenu(), "behaviour/dropdown/menu");
    action = tagged(new DropDownItem("Action", "#"), "behaviour/dropdown/action");
    disabledItem =
        tagged(
            new DropDownItem("Disabled", "#disabled-action"), "behaviour/dropdown/disabled-item");
    disabledItem.setDisabled(true);
    disabledItem.addClickHandler(event -> disabledActions++);
    dropDown.addItem(action);
    dropDown.addItem(disabledItem);
    bindEvent("show");
    bindEvent("shown");
    bindEvent("hide");
    bindEvent("hidden");

    outside = tagged(new Button("Outside"), "behaviour/dropdown/outside");
    fixture.add(dropDown);
    fixture.add(outside);
    host.add(fixture);
  }

  private void bindEvent(String name) {
    BootstrapEventBridge.bind(
        dropDown.getElement(), name + ".bs.dropdown", event -> events.add(name));
  }

  private void createSplitDropDown() {
    ButtonGroup group = tagged(new ButtonGroup(), "behaviour/dropdown/split-button");
    splitPrimary = tagged(new Button("Primary action"), "behaviour/dropdown/split-primary");
    splitPrimary.addClickHandler(event -> primaryActions++);
    splitToggle = tagged(new Button(), "behaviour/dropdown/split-toggle");
    splitToggle.setDataToggle(Toggle.DROPDOWN);
    splitToggle.addStyleName("dropdown-toggle-split");
    splitToggle.getElement().setAttribute("aria-label", "Primary action menu");
    splitMenu = tagged(new DropDownMenu(), "behaviour/dropdown/split-menu");
    splitMenu.add(new DropDownItem("Menu action", "#"));
    group.add(splitPrimary);
    group.add(splitToggle);
    group.add(splitMenu);
    host.add(group);
  }

  @Given("the dropdown menu is closed")
  public void dropDownIsClosed() {
    expect(element(menu)).toBeHidden();
  }

  @Given("the dropdown menu is open")
  public void dropDownIsOpen() {
    if (!menu.getStyleName().contains("show")) {
      Dom.click(element(toggle));
    }
    Dom.waitFor(() -> expect(element(menu)).toBeVisible());
  }

  @When("the user activates the dropdown toggle")
  public void activateDropDownToggle() {
    Dom.click(element(toggle));
  }

  @When("the user activates the document outside the dropdown")
  public void activateOutsideDropDown() {
    Dom.click(element(outside));
  }

  @Then("the dropdown menu is visible")
  public void dropDownMenuIsVisible() {
    Dom.waitFor(() -> expect(element(activeMenu())).toBeVisible());
  }

  @Then("the dropdown menu is hidden")
  public void dropDownMenuIsClosed() {
    Dom.waitFor(() -> expect(element(menu)).toBeHidden());
  }

  @Then("the toggle has aria-expanded {string}")
  public void toggleHasExpandedState(String state) {
    expect(element(toggle)).toHaveAttribute("aria-expanded", state);
  }

  @Then("one show event occurs before one shown event")
  public void showEventsAreReportedInOrder() {
    assertEquals(Arrays.asList("show", "shown"), events);
  }

  @Given("keyboard focus is on the first dropdown item")
  public void focusFirstDropDownItem() {
    Dom.focus(anchorOf(action));
  }

  @When("the user presses Escape in the menu")
  public void pressEscape() {
    Dom.focus(anchorOf(action));
    Dom.press(anchorOf(action), "Escape");
  }

  @Then("one hide event occurs before one hidden event")
  public void hideEventsAreReportedInOrder() {
    assertEquals(Arrays.asList("show", "shown", "hide", "hidden"), events);
  }

  @Then("the dropdown toggle owns document focus")
  public void toggleOwnsFocus() {
    expect(element(toggle)).toHaveFocus();
  }

  @Given("the current location is recorded")
  public void recordCurrentLocation() {
    recordedLocation = Location.current().getFullURL();
  }

  @When("the user activates the disabled menu item")
  public void activateDisabledItem() {
    Dom.click(anchorOf(disabledItem));
  }

  @Then("the disabled item action is not reported")
  public void noItemActionIsReported() {
    assertEquals(0, disabledActions);
  }

  @Then("the current location is unchanged")
  public void locationIsUnchanged() {
    assertEquals(recordedLocation, Location.current().getFullURL());
  }

  @Given("the split primary action is idle")
  public void splitPrimaryIsIdle() {
    assertEquals(0, primaryActions);
  }

  @When("the user activates the primary button")
  public void activateSplitPrimary() {
    Dom.click(element(splitPrimary));
  }

  @Then("one primary action is reported")
  public void onePrimaryActionIsReported() {
    assertEquals(1, primaryActions);
  }

  @Then("the dropdown menu remains closed")
  public void splitMenuRemainsClosed() {
    expect(element(splitMenu)).toBeHidden();
  }

  @When("the user activates the adjacent toggle")
  public void activateSplitToggle() {
    Dom.click(element(splitToggle));
  }

  @Then("the split dropdown menu is visible")
  public void splitMenuIsVisible() {
    Dom.waitFor(() -> expect(element(splitMenu)).toBeVisible());
  }

  @Then("no additional primary action is reported")
  public void primaryActionCountRemainsOne() {
    assertEquals(1, primaryActions);
  }

  @When("the user opens the dropup")
  public void openDropup() {
    Dom.click(element(toggle));
  }

  @Then("the menu is visible")
  public void menuIsVisible() {
    Dom.waitFor(() -> expect(element(activeMenu())).toBeVisible());
  }

  @Then("the menu bottom is no lower than the toggle top")
  public void menuIsAboveToggle() {
    Dom.waitFor(
        () ->
            assertTrue(
                "the dropup menu extends below its toggle",
                element(activeMenu()).getBoundingClientRect().getBottom()
                    <= element(toggle).getBoundingClientRect().getTop() + 1));
  }

  @When("the user opens the constrained dropdown")
  public void openConstrainedDropdown() {
    Dom.click(element(toggle));
  }

  @Then("the menu width equals the owning button group width")
  public void menuMatchesOwnerWidth() {
    Dom.waitFor(
        () -> {
          double menuWidth = element(activeMenu()).getBoundingClientRect().getWidth();
          double ownerWidth = element(dropDown).getBoundingClientRect().getWidth();
          assertTrue(
              "menu width " + menuWidth + "px differs from owner width " + ownerWidth + "px",
              Math.abs(menuWidth - ownerWidth) <= 1);
        });
  }

  @Then("no menu item overflows horizontally")
  public void menuItemsDoNotOverflow() {
    HTMLElement activeMenu = element(activeMenu());
    assertTrue(
        "dropdown menu content overflows horizontally",
        activeMenu.getScrollWidth() <= activeMenu.getClientWidth() + 1);
  }

  private DropDownMenu activeMenu() {
    return splitMenu == null ? menu : splitMenu;
  }

  private static HTMLElement anchorOf(Widget widget) {
    return Dom.within(element(widget)).find("a");
  }

  private static <T extends Widget> T tagged(T widget, String testId) {
    widget.getElement().setAttribute("data-testid", testId);
    return widget;
  }

  private static HTMLElement element(Widget widget) {
    return widget.getElement().unwrap();
  }
}
