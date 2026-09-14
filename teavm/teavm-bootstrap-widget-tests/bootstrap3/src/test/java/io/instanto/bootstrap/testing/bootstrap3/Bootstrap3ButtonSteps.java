package io.instanto.bootstrap.testing.bootstrap3;

import static io.instanto.webapp.testkit.dom.Expect.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
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
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBoxButton;
import org.gwtbootstrap3.client.ui.RadioButton;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.teavm.jso.dom.html.HTMLElement;

@CucumberSuite(
        value = {"features/buttons.feature", "features/widget-lifecycle.feature"})
public class Bootstrap3ButtonSteps {
    private RootPanel host;
    private Button button;
    private CheckBoxButton checkBoxButton;
    private CheckBoxButton[] checkBoxButtons;
    private RadioButton[] radioButtons;
    private final List<Button> typeButtons = new ArrayList<>();
    private int clicks;
    private int valueChanges;
    private Object lastEventSource;
    private int[] checkBoxChanges;
    private int radioChanges;
    private Object lastRadioEventSource;
    private String loadingText;
    private FlowPanel fixtureHost;
    private FlowPanel previousHost;
    private Widget lifecycleWidget;
    private Tooltip tooltip;
    private int attachEvents;
    private int actionEvents;

    @BeforeScenario
    public void createHost() {
        Dom.reset();
        Bootstrap3TestRuntime.initialise();
        Dom.container().setAttribute("id", "bootstrap3-widget-test-host");
        host = RootPanel.get("bootstrap3-widget-test-host");
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
        if (fixture.startsWith("behaviour/lifecycle/")) {
            createLifecycleFixture(fixture, mount);
            return;
        }
        if (fixture.startsWith("behaviour/toggle-button/")) {
            button = new Button("Toggle");
            button.setDataToggle(Toggle.BUTTON);
            if (fixture.endsWith("disabled")) {
                button.setEnabled(false);
            }
            button.addClickHandler(event -> {
                clicks++;
                lastEventSource = event.getSource();
            });
            mount(button, mount);
            return;
        }
        if (fixture.startsWith("behaviour/check-box-button/")) {
            checkBoxButton = new CheckBoxButton("Choice");
            if (fixture.endsWith("disabled")) {
                checkBoxButton.setEnabled(false);
            }
            checkBoxButton.addValueChangeHandler(event -> {
                valueChanges++;
                lastEventSource = event.getSource();
            });
            mount(checkBoxButton, mount);
            return;
        }
        if (fixture.equals("behaviour/check-box-buttons/independent")) {
            checkBoxButtons = new CheckBoxButton[] {
                new CheckBoxButton("One"), new CheckBoxButton("Two"), new CheckBoxButton("Three")
            };
            checkBoxChanges = new int[checkBoxButtons.length];
            for (int i = 0; i < checkBoxButtons.length; i++) {
                final int index = i;
                checkBoxButtons[i].addValueChangeHandler(event -> checkBoxChanges[index]++);
                mount(checkBoxButtons[i], mount);
            }
            return;
        }
        if (fixture.equals("behaviour/radio-buttons/exclusive")) {
            radioButtons = new RadioButton[] {
                new RadioButton("fixture-radio", "One"),
                new RadioButton("fixture-radio", "Two")
            };
            radioButtons[0].setValue(true);
            for (RadioButton radio : radioButtons) {
                radio.addValueChangeHandler(event -> {
                    radioChanges++;
                    lastRadioEventSource = event.getSource();
                });
                mount(radio, mount);
            }
            return;
        }
        if (fixture.equals("behaviour/button/loading") || fixture.equals("behaviour/button/sizes")) {
            button = new Button("Save");
            loadingText = "Saving...";
            button.setDataLoadingText(loadingText);
            mount(button, mount);
            return;
        }
        if (fixture.equals("behaviour/button/types")) {
            for (ButtonType type : ButtonType.values()) {
                Button typed = new Button(type.name());
                typed.setType(type);
                typeButtons.add(typed);
                mount(typed, mount);
            }
            return;
        }
        throw new IllegalArgumentException("Unknown fixture: " + fixture);
    }

    private void createLifecycleFixture(String fixture, boolean mounted) {
        fixtureHost = new FlowPanel();
        fixtureHost.getElement().setAttribute("data-testid", fixture + "/host");
        host.add(fixtureHost);
        Button target = new Button("Lifecycle");
        target.getElement().setId(fixture);
        target.getElement().setAttribute("data-testid", fixture);
        target.addAttachHandler(event -> {
            if (event.isAttached()) {
                attachEvents++;
            }
        });
        target.addClickHandler(event -> actionEvents++);
        lifecycleWidget = target;
        if (fixture.endsWith("plugin")) {
            tooltip = new Tooltip(target, "Lifecycle detail");
            tooltip.setContainer("#bootstrap3-widget-test-host");
        }
        if (mounted) {
            fixtureHost.add(target);
        }
        if (tooltip != null) {
            tooltip.show();
            Dom.waitFor(() -> assertEquals(1, Dom.findAll(".tooltip").size()));
        }
    }

    private void mount(com.google.gwt.user.client.ui.Widget widget, boolean mounted) {
        widget.getElement().setAttribute("data-testid", widget.getClass().getSimpleName());
        if (mounted) {
            host.add(widget);
        }
    }

    @Given("the toggle button is inactive")
    public void toggleButtonIsInactive() {
        assertFalse(button.isActive());
    }

    @Given("the toggle button is active")
    public void toggleButtonIsActive() {
        if (clicks == 0) {
            button.setActive(true);
        }
        assertTrue(button.isActive());
    }

    @Given("the toggle button is disabled")
    public void toggleButtonIsDisabled() {
        expect(element(button)).toBeDisabled();
    }

    @When("the user activates the toggle button")
    public void activateToggleButton() {
        Dom.click(element(button));
    }

    @Then("the toggle button has the active state class")
    public void toggleHasActiveClass() {
        expect(element(button)).toHaveClass("active");
    }

    @Then("the toggle button does not have the active state class")
    public void toggleDoesNotHaveActiveClass() {
        expect(element(button)).not().toHaveClass("active");
    }

    @Then("the toggle button has aria-pressed {string}")
    public void toggleHasPressedState(String state) {
        expect(element(button)).toHaveAttribute("aria-pressed", state);
    }

    @Then("one click is reported with the toggle button as source")
    public void oneToggleClickIsReported() {
        assertEquals(1, clicks);
        assertSame(button, lastEventSource);
    }

    @Then("no click or value change is reported")
    public void noControlEventIsReported() {
        assertEquals(0, clicks);
        assertEquals(0, valueChanges);
    }

    @When("the checkbox button value is set to true without firing events")
    public void setCheckboxSilently() {
        checkBoxButton.setValue(true, false);
    }

    @Then("the checkbox button value is true")
    public void checkboxValueIsTrue() {
        assertTrue(checkBoxButton.getValue());
    }

    @Then("no value change is reported")
    public void noValueChangeIsReported() {
        assertEquals(0, valueChanges);
    }

    @When("the checkbox button value is set to false and events are requested")
    public void setCheckboxAndFire() {
        checkBoxButton.setValue(false, true);
    }

    @Then("one value change is reported with the checkbox button as source")
    public void oneCheckboxValueChangeIsReported() {
        assertEquals(1, valueChanges);
        assertSame(checkBoxButton, lastEventSource);
    }

    @Given("the checkbox button value is false")
    public void checkboxButtonIsFalse() {
        assertFalse(checkBoxButton.getValue());
    }

    @Given("the checkbox button is disabled")
    public void checkboxButtonIsDisabled() {
        assertFalse(checkBoxButton.isEnabled());
    }

    @When("the user activates the checkbox button")
    public void activateCheckboxButton() {
        Dom.click(element(checkBoxButton));
    }

    @When("the user activates the checkbox button twice")
    public void activateCheckboxButtonTwice() {
        Dom.click(element(checkBoxButton));
        Dom.waitFor(() -> assertTrue(checkBoxButton.getValue()));
        Dom.click(element(checkBoxButton));
        Dom.waitFor(() -> assertFalse(checkBoxButton.getValue()));
    }

    @Then("two value changes are reported with the checkbox button as source")
    public void twoCheckboxValueChangesAreReported() {
        Dom.waitFor(() -> assertEquals(2, valueChanges));
        assertSame(checkBoxButton, lastEventSource);
    }

    @When("the user activates the first checkbox button")
    public void activateFirstCheckbox() {
        Dom.click(element(checkBoxButtons[0]));
    }

    @When("the user activates the third checkbox button")
    public void activateThirdCheckbox() {
        Dom.click(element(checkBoxButtons[2]));
    }

    @Then("the first and third checkbox button values are true")
    public void firstAndThirdCheckboxesAreSelected() {
        Dom.waitFor(() -> {
            assertTrue(checkBoxButtons[0].getValue());
            assertTrue(checkBoxButtons[2].getValue());
        });
    }

    @Then("the second checkbox button value is false")
    public void secondCheckboxIsNotSelected() {
        assertFalse(checkBoxButtons[1].getValue());
    }

    @Then("each changed checkbox button reports one value change")
    public void changedCheckboxesReportOnce() {
        Dom.waitFor(() -> assertTrue(Arrays.equals(new int[] {1, 0, 1}, checkBoxChanges)));
    }

    @Given("the first radio button is selected")
    public void firstRadioIsSelected() {
        assertTrue(radioButtons[0].getValue());
    }

    @When("the user activates the second radio button")
    public void activateSecondRadio() {
        Dom.click(element(radioButtons[1]));
    }

    @When("the user activates the first radio button")
    public void activateFirstRadio() {
        Dom.click(element(radioButtons[0]));
    }

    @Then("only the second radio button is selected")
    public void onlySecondRadioIsSelected() {
        Dom.waitFor(() -> {
            assertFalse(radioButtons[0].getValue());
            assertTrue(radioButtons[1].getValue());
        });
    }

    @Then("only the first radio button is selected")
    public void onlyFirstRadioIsSelected() {
        assertTrue(radioButtons[0].getValue());
        assertFalse(radioButtons[1].getValue());
    }

    @Then("one value change is reported with the second radio button as source")
    public void secondRadioReportsOneValueChange() {
        Dom.waitFor(() -> assertEquals(1, radioChanges));
        assertSame(radioButtons[1], lastRadioEventSource);
    }

    @Then("no radio value change is reported")
    public void noRadioValueChangeIsReported() {
        assertEquals(0, radioChanges);
    }

    @Then("the first radio button has aria-pressed {string}")
    public void firstRadioHasPressedState(String state) {
        assertEquals(state, Boolean.toString(radioButtons[0].getValue()));
    }

    @Then("the second radio button has aria-pressed {string}")
    public void secondRadioHasPressedState(String state) {
        assertEquals(state, Boolean.toString(radioButtons[1].getValue()));
    }

    @Given("the button text is {string}")
    public void buttonTextIs(String text) {
        assertEquals(text, button.getText());
    }

    @Given("the loading text is {string}")
    public void loadingTextIs(String text) {
        assertEquals(text, loadingText);
    }

    @When("loading state is started")
    public void startLoading() {
        button.state().loading();
    }

    @When("loading state is reset")
    public void resetLoading() {
        button.state().reset();
    }

    @Then("the button is disabled")
    public void buttonIsDisabled() {
        assertFalse(button.isEnabled());
    }

    @Then("the button is enabled")
    public void buttonIsEnabled() {
        assertTrue(button.isEnabled());
    }

    @Then("the button has aria-busy {string}")
    public void buttonHasBusyState(String state) {
        assertEquals(state, button.getElement().getAttribute("aria-busy"));
    }

    @Then("the button has no aria-busy attribute")
    public void buttonHasNoBusyState() {
        assertFalse(button.getElement().hasAttribute("aria-busy"));
    }

    @When("every supported button type is assigned")
    public void everyTypeIsAssigned() {
        assertEquals(ButtonType.values().length, typeButtons.size());
    }

    @Then("each button reports the assigned type")
    public void eachButtonReportsItsType() {
        for (int i = 0; i < typeButtons.size(); i++) {
            assertEquals(ButtonType.values()[i], typeButtons.get(i).getType());
        }
    }

    @Then("each button has exactly one matching framework type class")
    public void eachButtonHasOneTypeClass() {
        for (int i = 0; i < typeButtons.size(); i++) {
            ButtonType assigned = ButtonType.values()[i];
            expect(element(typeButtons.get(i))).toHaveClass(assigned.getCssName());
            assertEquals(1, countTypeClasses(typeButtons.get(i)));
        }
    }

    @Then("no type falls through to the default class")
    public void noTypeFallsThrough() {
        for (int i = 0; i < typeButtons.size(); i++) {
            ButtonType assigned = ButtonType.values()[i];
            if (assigned != ButtonType.DEFAULT) {
                expect(element(typeButtons.get(i))).not().toHaveClass(ButtonType.DEFAULT.getCssName());
            }
        }
    }

    private int countTypeClasses(Button typed) {
        int count = 0;
        for (ButtonType candidate : ButtonType.values()) {
            if (hasClass(typed, candidate.getCssName())) {
                count++;
            }
        }
        return count;
    }

    @When("a button changes from large to small")
    public void changeButtonSize() {
        button.setSize(ButtonSize.LARGE);
        button.setSize(ButtonSize.SMALL);
    }

    @Then("the button reports the small size")
    public void buttonReportsSmallSize() {
        assertEquals(ButtonSize.SMALL, button.getSize());
    }

    @Then("the small size class is present")
    public void smallSizeClassIsPresent() {
        expect(element(button)).toHaveClass(ButtonSize.SMALL.getCssName());
    }

    @Then("the large size class is absent")
    public void largeSizeClassIsAbsent() {
        expect(element(button)).not().toHaveClass(ButtonSize.LARGE.getCssName());
    }

    private static HTMLElement element(com.google.gwt.user.client.ui.Widget widget) {
        return widget.getElement().unwrap();
    }

    private static boolean hasClass(com.google.gwt.user.client.ui.Widget widget, String className) {
        String classes = " " + widget.getStyleName() + " ";
        return classes.contains(" " + className + " ");
    }

    @Then("the widget reports the fixture host as its parent")
    public void widgetReportsFixtureHost() {
        assertSame(fixtureHost, lifecycleWidget.getParent());
    }

    @Then("the widget reports itself attached")
    public void widgetIsAttached() {
        assertTrue(lifecycleWidget.isAttached());
    }

    @Then("its element has exactly one DOM parent")
    public void widgetHasOneDomParent() {
        assertSame(fixtureHost.getElement().unwrap(), element(lifecycleWidget).getParentNode());
    }

    @Then("its stable fixture id is present")
    public void stableFixtureIdIsPresent() {
        assertFalse(lifecycleWidget.getElement().getId().isEmpty());
        expect(element(lifecycleWidget)).toHaveAttribute("data-testid", lifecycleWidget.getElement().getId());
    }

    @When("the widget is removed from its parent")
    public void removeWidgetFromParent() {
        lifecycleWidget.removeFromParent();
    }

    @Then("the widget has no parent")
    public void widgetHasNoParent() {
        assertNull(lifecycleWidget.getParent());
    }

    @Then("the widget reports itself detached")
    public void widgetIsDetached() {
        assertFalse(lifecycleWidget.isAttached());
    }

    @Then("its element has no DOM parent")
    public void widgetHasNoDomParent() {
        assertNull(element(lifecycleWidget).getParentNode());
    }

    @When("the widget is detached and mounted in a fresh host")
    public void remountInFreshHost() {
        previousHost = fixtureHost;
        lifecycleWidget.removeFromParent();
        fixtureHost = new FlowPanel();
        host.add(fixtureHost);
        fixtureHost.add(lifecycleWidget);
    }

    @Then("the fresh host is the widget parent")
    public void freshHostIsParent() {
        assertSame(fixtureHost, lifecycleWidget.getParent());
    }

    @Then("one new attach event is reported")
    public void oneNewAttachEventIsReported() {
        assertEquals(2, attachEvents);
    }

    @Then("the previous host remains empty")
    public void previousHostIsEmpty() {
        assertEquals(0, previousHost.getWidgetCount());
        expect(element(previousHost)).toBeEmpty();
    }

    @When("the interactive widget is detached and mounted three times")
    public void remountInteractiveWidgetThreeTimes() {
        for (int i = 0; i < 3; i++) {
            previousHost = fixtureHost;
            lifecycleWidget.removeFromParent();
            assertEquals(0, previousHost.getWidgetCount());
            fixtureHost = new FlowPanel();
            host.add(fixtureHost);
            fixtureHost.add(lifecycleWidget);
        }
    }

    @When("the user activates it once")
    public void activateLifecycleWidget() {
        Dom.click(element(lifecycleWidget));
    }

    @Then("its action handler runs exactly once")
    public void actionHandlerRunsOnce() {
        assertEquals(1, actionEvents);
    }

    @Then("no detached host receives an event")
    public void detachedHostsRemainEmpty() {
        assertEquals(0, previousHost.getWidgetCount());
    }

    @Given("the plugin has created document-level markup")
    public void pluginMarkupExists() {
        assertEquals(1, Dom.findAll(".tooltip").size());
    }

    @When("the plugin widget is detached")
    public void detachPluginWidget() {
        lifecycleWidget.removeFromParent();
    }

    @Then("its document-level markup is removed")
    public void pluginMarkupIsRemoved() {
        Dom.waitFor(() -> assertEquals(0, Dom.findAll(".tooltip").size()));
    }

    @Then("remounting creates exactly one replacement element")
    public void remountCreatesOnePluginElement() {
        fixtureHost.add(lifecycleWidget);
        tooltip.show();
        Dom.waitFor(() -> assertEquals(1, Dom.findAll(".tooltip").size()));
    }
}
