package io.instanto.bootstrap.testing.bootstrap3;

import static io.instanto.webapp.testkit.dom.Expect.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.cucumber.tea.AfterScenario;
import io.instanto.cucumber.tea.BeforeScenario;
import io.instanto.cucumber.tea.CucumberSuite;
import io.instanto.cucumber.tea.Given;
import io.instanto.cucumber.tea.Then;
import io.instanto.cucumber.tea.When;
import io.instanto.webapp.testkit.dom.Dom;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.InputGroupSize;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Node;
import org.teavm.jso.dom.xml.NodeList;

/**
 * Input group composition on Bootstrap 3.
 *
 * <p>Bootstrap 3 is the reference for these behaviours: the same feature file drives
 * the Bootstrap 5 steps, so anything asserted here is what the port has to reproduce.
 * The assertions are therefore about what the widgets do -- addon order, independent
 * activation, one reported size -- rather than about markup only this generation
 * emits.</p>
 *
 * <p>Layout assertions use browser geometry after the fixture is mounted. This keeps
 * the scenario at widget level while verifying that the packaged stylesheet makes
 * the segments adjoin, share a height, and consume the complete group width.</p>
 */
@CucumberSuite(
        value = {"features/input-groups.feature"})
public class Bootstrap3InputGroupSteps {

    private static final String PREFIX = "£";
    private static final String SUFFIX = ".00";

    private RootPanel host;
    private InputGroup group;
    private TextBox control;
    private InputGroupAddon prefix;
    private InputGroupAddon suffix;
    private InputGroupButton buttonAddon;
    private Button addonButton;
    private int buttonActions;

    /**
     * Starts the library the way an application does.
     *
     * <p>The same two steps the showcase takes before it builds anything: initialise
     * the library, then add through {@code RootPanel}. Reaching straight for the panel
     * would exercise widgets without the resources and plugin checks an application
     * receives.</p>
     */
    @BeforeScenario
    public void createHost() {
        Dom.reset();
        Bootstrap3TestRuntime.initialise();
        Dom.container().setAttribute("id", "bootstrap3-input-group-host");
        host = RootPanel.get("bootstrap3-input-group-host");
    }

    @AfterScenario
    public void removeFixture() {
        if (host != null) {
            host.clear();
        }
        Dom.reset();
    }

    @Given("fixture {string} is mounted")
    public void mountedFixture(String fixture) {
        createFixture(fixture, true);
    }

    @Given("fixture {string} is constructed")
    public void constructedFixture(String fixture) {
        createFixture(fixture, false);
    }

    @Given("Bootstrap 3 showcase route {string} section {string} defines the baseline")
    public void referenceShowcaseDefinesBaseline(String route, String section) {
        assertFalse(route.isEmpty());
        assertFalse(section.isEmpty());
    }

    private void createFixture(String fixture, boolean mount) {
        group = new InputGroup();
        control = new TextBox();
        control.getElement().setAttribute("data-testid", "input-group-control");

        if (fixture.equals("behaviour/input-group/text-addons")) {
            prefix = new InputGroupAddon();
            prefix.setText(PREFIX);
            suffix = new InputGroupAddon();
            suffix.setText(SUFFIX);
        } else if (fixture.equals("behaviour/input-group/button-addon")) {
            addonButton = new Button("Go");
            addonButton.addClickHandler(event -> buttonActions++);
            buttonAddon = new InputGroupButton();
            buttonAddon.add(addonButton);
            group.add(control);
            group.add(buttonAddon);
            mount(group, mount);
            return;
        } else if (!fixture.equals("behaviour/input-group/sizing")) {
            throw new IllegalArgumentException("Unknown fixture: " + fixture);
        }

        if (prefix != null) {
            group.add(prefix);
        }
        group.add(control);
        if (suffix != null) {
            group.add(suffix);
        }
        mount(group, mount);
    }

    private void mount(Widget widget, boolean mounted) {
        widget.getElement().setAttribute("data-testid", "input-group");
        if (mounted) {
            host.add(widget);
        }
    }

    // ING-001 -----------------------------------------------------------------

    @When("text addons are added around the control")
    public void addonsAreAddedAroundTheControl() {
        assertEquals(3, group.getWidgetCount());
    }

    @Then("the prefix addon precedes the text control")
    public void prefixPrecedesControl() {
        assertTrue(group.getWidgetIndex(prefix) < group.getWidgetIndex(control));
        assertEquals(PREFIX, prefix.getText());
        assertTrue("the prefix addon does not precede the control in the DOM",
                domIndex(prefix) < domIndex(control));
    }

    @Then("the suffix addon follows the text control")
    public void suffixFollowsControl() {
        assertTrue(group.getWidgetIndex(suffix) > group.getWidgetIndex(control));
        assertEquals(SUFFIX, suffix.getText());
        assertTrue("the suffix addon does not follow the control in the DOM",
                domIndex(control) < domIndex(suffix));
    }

    @Then("the text control remains editable")
    public void controlRemainsEditable() {
        assertFalse(control.isReadOnly());
        expect(element(control)).toBeEnabled();
    }

    // ING-002 -----------------------------------------------------------------

    @Given("the grouped text control contains {string}")
    public void groupedControlContains(String text) {
        control.setText(text);
        expect(element(control)).toHaveValue(text);
    }

    @When("the user activates the addon button")
    public void activateAddonButton() {
        Dom.click(element(addonButton));
    }

    @Then("one button action is reported")
    public void oneButtonActionIsReported() {
        Dom.waitFor(() -> assertEquals(1, buttonActions));
    }

    @Then("the grouped text control still contains {string}")
    public void groupedControlStillContains(String text) {
        expect(element(control)).toHaveValue(text);
    }

    @Then("the button remains a child of the button addon")
    public void buttonRemainsInsideAddon() {
        assertSame(buttonAddon, addonButton.getParent());
        expect(element(buttonAddon)).toContainElement(element(addonButton));
    }

    // ING-003 -----------------------------------------------------------------

    @Then("the input group is rendered")
    public void inputGroupIsRendered() {
        expect(element(group)).toBeVisible();
        assertTrue("the input group has no rendered width", width(element(group)) > 0);
        assertTrue("the input group has no rendered height", height(element(group)) > 0);
    }

    @Then("adjacent segment edges meet without a layout gap")
    public void adjacentSegmentEdgesMeet() {
        assertAdjoins(element(prefix), element(control));
        assertAdjoins(element(control), element(suffix));
    }

    @Then("all segments share one vertical extent")
    public void allSegmentsShareOneVerticalExtent() {
        assertSameVerticalExtent(element(group), element(prefix));
        assertSameVerticalExtent(element(group), element(control));
        assertSameVerticalExtent(element(group), element(suffix));
    }

    @Then("the text control consumes the remaining group width")
    public void controlConsumesRemainingWidth() {
        double remaining = width(element(group))
                - width(element(prefix)) - width(element(suffix));
        double actual = width(element(control));
        assertTrue("control width " + actual + "px differs from remaining width "
                + remaining + "px", Math.abs(actual - remaining) <= 1);
    }

    // ING-004 -----------------------------------------------------------------

    @When("the input group size is set to large")
    public void setSizeLarge() {
        group.setSize(InputGroupSize.LARGE);
    }

    @Then("the widget reports large as its size")
    public void sizeIsLarge() {
        assertEquals(InputGroupSize.LARGE, group.getSize());
    }

    @Then("exactly one large input-group class is present")
    public void oneLargeClassIsPresent() {
        expect(element(group)).toHaveClass(InputGroupSize.LARGE.getCssName());
        assertEquals(1, countSizeClasses());
    }

    @When("the input group size is reset to default")
    public void resetSizeToDefault() {
        group.setSize(InputGroupSize.DEFAULT);
    }

    @Then("the widget reports default as its size")
    public void sizeIsDefault() {
        assertEquals(InputGroupSize.DEFAULT, group.getSize());
    }

    @Then("no stale large input-group class remains")
    public void noStaleLargeClass() {
        expect(element(group)).not().toHaveClass(InputGroupSize.LARGE.getCssName());
    }

    private int countSizeClasses() {
        int count = 0;
        for (InputGroupSize candidate : InputGroupSize.values()) {
            String name = candidate.getCssName();
            if (name != null && !name.isEmpty() && hasClass(group, name)) {
                count++;
            }
        }
        return count;
    }

    // -------------------------------------------------------------------------

    /**
     * Where a widget's element sits among the group's children.
     *
     * <p>Read from the element's own children rather than through a document scope,
     * because ING-001 asserts this on a group that has deliberately not been mounted:
     * addon order is a property of the widget, not of having been attached.</p>
     */
    private int domIndex(Widget widget) {
        HTMLElement target = element(widget);
        NodeList<? extends Node> children = element(group).getChildNodes();
        int index = 0;
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.get(i);
            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (child == target) {
                return index;
            }
            index++;
        }
        throw new AssertionError("the group does not contain " + widget.getClass().getSimpleName());
    }

    private static void assertAdjoins(HTMLElement left, HTMLElement right) {
        double gap = left(right) - (left(left) + width(left));
        assertTrue("a gap of " + gap + "px separates two segments of one control",
                Math.abs(gap) <= 1.0);
    }

    private static void assertSameVerticalExtent(HTMLElement expected, HTMLElement actual) {
        double topDifference = top(actual) - top(expected);
        double heightDifference = height(actual) - height(expected);
        assertTrue("segment top differs by " + topDifference + "px",
                Math.abs(topDifference) <= 1.0);
        assertTrue("segment height differs by " + heightDifference + "px",
                Math.abs(heightDifference) <= 1.0);
    }

    private static HTMLElement element(Widget widget) {
        return widget.getElement().unwrap();
    }

    private static boolean hasClass(Widget widget, String className) {
        String classes = " " + widget.getStyleName() + " ";
        return classes.contains(" " + className + " ");
    }

    private static double width(HTMLElement element) {
        return element.getBoundingClientRect().getWidth();
    }

    private static double height(HTMLElement element) {
        return element.getBoundingClientRect().getHeight();
    }

    private static double top(HTMLElement element) {
        return element.getBoundingClientRect().getTop();
    }

    private static double left(HTMLElement element) {
        return element.getBoundingClientRect().getLeft();
    }
}
