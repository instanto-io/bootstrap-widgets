package io.instanto.bootstrap.testing.bootstrap3;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.cucumber.tea.AfterScenario;
import io.instanto.cucumber.tea.BeforeScenario;
import io.instanto.cucumber.tea.CucumberSuite;
import io.instanto.cucumber.tea.Given;
import io.instanto.cucumber.tea.When;
import io.instanto.cucumber.tea.Then;
import io.instanto.webapp.testkit.dom.Dom;
import org.gwtbootstrap3.extras.toggleswitch.client.ui.ToggleSwitch;
import org.gwtbootstrap3.extras.toggleswitch.client.ui.ToggleSwitchRadio;
import org.gwtbootstrap3.extras.toggleswitch.client.ui.base.ToggleSwitchBase;
import org.gwtbootstrap3.extras.toggleswitch.client.ui.base.constants.SizeType;
import org.teavm.jso.dom.html.HTMLElement;

import static org.junit.Assert.*;

@CucumberSuite("features/toggle-switch.feature")
public class Bootstrap3ToggleSwitchSteps {
    private RootPanel host;
    private ToggleSwitchBase toggle;
    private ToggleSwitchRadio second;
    private int changes;
    private Object eventSource;

    @BeforeScenario
    public void prepare() {
        Dom.reset();
        Bootstrap3TestRuntime.initialise();
        Dom.container().setAttribute("id", "toggle-switch-test-host");
        host = RootPanel.get("toggle-switch-test-host");
    }

    @AfterScenario
    public void cleanup() {
        host.clear();
        Dom.reset();
    }

    private void observe() {
        toggle.addValueChangeHandler(event -> { changes++; eventSource = event.getSource(); });
    }

    private HTMLElement wrapper(ToggleSwitchBase control) {
        return control.getElement().getParentElement().getParentElement().unwrap();
    }

    private void ready(ToggleSwitchBase control) {
        Dom.waitFor(() -> assertTrue(wrapper(control).getClassName().contains("bootstrap-switch")));
    }

    private void choose(ToggleSwitchBase control, boolean on) {
        Dom.click(wrapper(control).querySelector(on ? ".bootstrap-switch-handle-off" : ".bootstrap-switch-handle-on"));
    }

    @Given("a checkbox switch is ready")
    public void checkbox() {
        toggle = new ToggleSwitch();
        toggle.setAnimate(false);
        observe();
        host.add(toggle);
        ready(toggle);
        assertTrue(wrapper(toggle).getClassName().contains("bootstrap-switch-off"));
    }

    @When("the user switches it on and off")
    public void toggleTwice() {
        choose(toggle, true);
        assertTrue(toggle.getValue());
        assertTrue(wrapper(toggle).getClassName().contains("bootstrap-switch-on"));
        choose(toggle, false);
    }

    @Then("the switch is off and two value changes were reported")
    public void twoChanges() {
        assertFalse(toggle.getValue());
        assertEquals(2, changes);
        assertSame(toggle, eventSource);
        assertTrue(wrapper(toggle).getClassName().contains("bootstrap-switch-off"));
    }

    @When("its value is assigned with and without event notification")
    public void programmatic() {
        toggle.setValue(true);
        assertTrue(toggle.getValue());
        assertEquals(0, changes);
        toggle.setValue(false, true);
        toggle.setValue(false, true);
    }

    @Then("only the requested value change was reported")
    public void oneRequestedChange() {
        assertFalse(toggle.getValue());
        assertEquals(1, changes);
        assertSame(toggle, eventSource);
    }

    @When("the user tries to change it while disabled and then read-only")
    public void disabledAndReadOnly() {
        toggle.setEnabled(false);
        choose(toggle, true);
        assertFalse(toggle.getValue());
        toggle.setEnabled(true);
        toggle.setReadOnly(true);
        choose(toggle, true);
    }

    @Then("its value and event count are unchanged")
    public void unchanged() {
        assertFalse(toggle.getValue());
        assertEquals(0, changes);
    }

    @Given("a switch is configured before attachment")
    public void configure() {
        toggle = new ToggleSwitch();
        toggle.setValue(true);
        toggle.setOnText("Yes");
        toggle.setOffText("No");
        toggle.setSize(SizeType.SMALL);
        toggle.setEnabled(false);
        toggle.setEnabled(true);
        toggle.setReadOnly(true);
        toggle.setReadOnly(false);
    }

    @When("it is attached and its plugin is ready")
    public void attach() {
        host.add(toggle);
        ready(toggle);
    }

    @Then("its value texts size and enabled state match the configuration")
    public void configuredValues() {
        assertTrue(toggle.getValue());
        assertTrue(toggle.isEnabled());
        assertFalse(toggle.isReadOnly());
        assertEquals("Yes", toggle.getOnText());
        assertEquals("No", toggle.getOffText());
        assertEquals(SizeType.SMALL, toggle.getSize());
    }

    @When("the switch is removed and attached again")
    public void reattach() {
        toggle.setValue(true);
        toggle.removeFromParent();
        assertEquals(0, Dom.container().querySelectorAll(".bootstrap-switch").getLength());
        host.add(toggle);
        ready(toggle);
        assertTrue(toggle.getValue());
    }

    @When("its texts and size are changed before reattachment")
    public void updateOptions() {
        toggle.setOnText("Enabled");
        toggle.setOffText("Disabled");
        toggle.setSize(SizeType.LARGE);
        toggle.setInverse(true);
        toggle.removeFromParent();
        host.add(toggle);
        ready(toggle);
    }

    @Then("the updated texts and size are retained")
    public void optionsRetained() {
        assertEquals("Enabled", toggle.getOnText());
        assertEquals("Disabled", toggle.getOffText());
        assertTrue(wrapper(toggle).getClassName(), wrapper(toggle).getClassName().contains("bootstrap-switch-large"));
        assertTrue(toggle.isInverse());
    }

    @Then("one plugin control remains and one user change is reported")
    public void oneWrapperAndHandler() {
        assertEquals(1, Dom.container().querySelectorAll(".bootstrap-switch").getLength());
        choose(toggle, false);
        assertEquals(1, changes);
        assertFalse(toggle.getValue());
    }

    @Given("two radio switches in the same group are ready")
    public void radios() {
        toggle = new ToggleSwitchRadio("choices");
        second = new ToggleSwitchRadio("choices");
        host.add(toggle);
        host.add(second);
        ready(toggle);
        ready(second);
    }

    @When("the user selects each radio switch in turn")
    public void selectRadios() {
        choose(toggle, true);
        assertTrue(toggle.getValue());
        choose(second, true);
    }

    @Then("only the second radio switch is selected")
    public void exclusive() {
        assertFalse(toggle.getValue());
        assertTrue(second.getValue());
    }

    @Given("a switch is attached and immediately removed")
    public void removeEarly() {
        toggle = new ToggleSwitch();
        host.add(toggle);
        toggle.removeFromParent();
    }

    @When("another switch finishes initializing")
    public void finishLoading() {
        ToggleSwitch other = new ToggleSwitch();
        host.add(other);
        ready(other);
    }

    @Then("the removed switch has no plugin wrapper")
    public void noOrphan() {
        assertNull(toggle.getElement().getParentElement());
        assertEquals(1, Dom.container().querySelectorAll(".bootstrap-switch").getLength());
    }
}
