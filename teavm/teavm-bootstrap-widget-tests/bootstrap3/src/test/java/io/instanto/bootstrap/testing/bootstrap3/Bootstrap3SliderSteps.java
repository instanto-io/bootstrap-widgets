package io.instanto.bootstrap.testing.bootstrap3;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.cucumber.tea.*;
import io.instanto.webapp.testkit.dom.Dom;
import org.gwtbootstrap3.extras.slider.client.ui.*;
import org.teavm.jso.dom.html.HTMLElement;
import java.util.Arrays;
import static org.junit.Assert.*;

@CucumberSuite("features/slider.feature")
public class Bootstrap3SliderSteps {
    private RootPanel host;
    private Slider slider;
    private RangeSlider range;
    private int changes;
    private Object source;
    @BeforeScenario public void prepare() {
        Dom.reset(); Bootstrap3TestRuntime.initialise();
        Dom.container().setAttribute("id", "slider-test-host");
        host = RootPanel.get("slider-test-host");
    }
    @AfterScenario public void cleanup() { host.clear(); Dom.reset(); }
    private HTMLElement handle() { return Dom.container().querySelector(".slider-handle"); }
    @Given("a numeric slider from zero to ten is ready") public void initial() {
        slider = new Slider(0, 10, 4);
        slider.setFormatter(value -> "Units " + value.intValue());
        slider.addValueChangeHandler(e -> { changes++; source = e.getSource(); });
        host.add(slider);
        Dom.waitFor(() -> assertNotNull(handle()));
    }
    @Then("its value is four and its tooltip says Units 4") public void value() {
        assertEquals(4, slider.getValue(), 0);
        assertEquals("Units 4", Dom.container().querySelector(".tooltip-inner").getTextContent());
    }
    @When("the slider API changes the value silently and then with notification") public void change() {
        slider.setValue(5.0); assertEquals(0, changes);
        slider.setValue(6.0, true); slider.setValue(6.0, true);
    }
    @Then("the slider has sent exactly one value change with itself as source") public void event() {
        assertEquals(1, changes); assertSame(slider, source); assertEquals(6, slider.getValue(), 0);
    }
    @When("the slider is disabled and enabled") public void disable() {
        slider.setEnabled(false); assertFalse(slider.isEnabled());
        assertTrue(slider.getStyleElement().hasClassName("slider-disabled"));
        slider.setEnabled(true);
    }
    @Then("the slider is enabled again") public void enabled() {
        assertTrue(slider.isEnabled()); assertFalse(slider.getStyleElement().hasClassName("slider-disabled"));
    }
    @When("the slider value is changed and it is reattached") public void reattach() {
        slider.setValue(8.0); slider.removeFromParent(); host.add(slider);
        Dom.waitFor(() -> assertNotNull(handle()));
    }
    @Then("there is one slider with the changed value") public void retained() {
        assertEquals(8, slider.getValue(), 0);
        assertEquals(1, Dom.container().querySelectorAll(".slider").getLength());
    }
    @Given("a range slider with tick labels is ready") public void ranged() {
        range = new RangeSlider(0, 10, new Range(2, 8));
        range.setTicks(Arrays.asList(0.0, 5.0, 10.0));
        range.setTicksLabels(Arrays.asList("Low", "Middle", "High"));
        host.add(range); Dom.waitFor(() -> assertNotNull(handle()));
    }
    @Then("both range endpoints and tick labels are available") public void endpoints() {
        assertEquals(2, range.getValue().getMinValue(), 0);
        assertEquals(8, range.getValue().getMaxValue(), 0);
        assertEquals(Arrays.asList("Low", "Middle", "High"), range.getTicksLabels());
        assertEquals(3, Dom.container().querySelectorAll(".slider-tick-label").getLength());
    }
    @When("the user presses the right arrow on its handle") public void key() { Dom.focus(handle()); Dom.press(handle(), "ArrowRight"); }
    @Then("its value increases by one") public void increment() { assertEquals(5, slider.getValue(), 0); }
}
