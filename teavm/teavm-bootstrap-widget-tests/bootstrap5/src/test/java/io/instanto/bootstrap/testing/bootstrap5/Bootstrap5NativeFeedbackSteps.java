package io.instanto.bootstrap.testing.bootstrap5;

import static org.junit.Assert.*;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.bootstrap5.client.ui.*;
import io.instanto.bootstrap5.client.ui.constants.PlaceholderSize;
import io.instanto.cucumber.tea.*;
import io.instanto.webapp.testkit.dom.Dom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.teavm.jso.dom.html.HTMLDocument;

@CucumberSuite("features/native-feedback.feature")
public class Bootstrap5NativeFeedbackSteps {
    private RootPanel host;
    private Toast toast;
    private ToastContainer notifications;
    private Offcanvas panel;
    private Button open;
    private Placeholder placeholder;
    private PlaceholderContainer skeleton;
    private final List<String> events = new ArrayList<>();
    private String previousOverflow;

    @BeforeScenario
    public void prepare() {
        Dom.reset();
        Bootstrap5TestRuntime.initialise();
        Dom.container().setAttribute("id", "native-component-tests");
        host = RootPanel.get("native-component-tests");
        previousOverflow = HTMLDocument.current().getBody().getStyle().getPropertyValue("overflow");
    }

    @AfterScenario
    public void cleanup() {
        if (host != null) host.clear();
        Dom.reset();
    }

    @Given("fixture {string} is mounted")
    public void mount(String fixture) {
        if (fixture.equals("native/toast")) {
            toast = new Toast("Saved", "Message with <safe> text");
            toast.setAutoHide(false);
            toast.setId("native-toast");
            toast.addShowHandler(event -> events.add("show"));
            toast.addShownHandler(event -> events.add("shown"));
            toast.addHideHandler(event -> events.add("hide"));
            toast.addHiddenHandler(event -> events.add("hidden"));
            host.add(toast);
        } else if (fixture.equals("native/notifications")) {
            notifications = new ToastContainer();
            host.add(notifications);
        } else if (fixture.equals("native/offcanvas")) {
            panel = new Offcanvas();
            panel.setId("native-panel");
            panel.add(new OffcanvasHeader("Filters"));
            panel.add(new OffcanvasBody("Details"));
            open = new Button("Open filters");
            open.addClickHandler(event -> panel.show());
            host.add(open);
            host.add(panel);
        } else if (fixture.equals("native/placeholders")) {
            placeholder = new Placeholder(8);
            skeleton = new PlaceholderContainer();
            skeleton.add(placeholder);
            host.add(skeleton);
        } else {
            throw new IllegalArgumentException(fixture);
        }
    }

    @When("the toast is shown")
    public void showToast() {
        toast.show();
        Dom.waitFor(() -> assertTrue(events.contains("shown")));
    }

    @When("its close button is clicked")
    public void closeToast() {
        Dom.click(Dom.container().querySelector("#native-toast .btn-close"));
        Dom.waitFor(() -> assertTrue(events.contains("hidden")));
    }

    @Then("the toast lifecycle is show shown hide hidden")
    public void lifecycle() {
        assertEquals(Arrays.asList("show", "shown", "hide", "hidden"), events);
        assertFalse(toast.isShown());
    }

    @When("a show handler cancels the toast")
    public void cancelShow() {
        toast.addShowHandler(event -> event.preventDefault());
        toast.show();
    }

    @Then("the toast stays hidden")
    public void hiddenToast() { assertFalse(toast.isShown()); assertEquals(Arrays.asList("show"), events); }

    @When("a hide handler cancels dismissal")
    public void cancelHide() { toast.addHideHandler(event -> event.preventDefault()); toast.hide(); }

    @Then("the toast remains shown")
    public void shownToast() { assertTrue(toast.isShown()); assertEquals(Arrays.asList("show", "shown", "hide"), events); }

    @When("two notifications with different delays are sent")
    public void notifications() {
        assertEquals("polite", notifications.getElement().getAttribute("aria-live"));
        notifications.notify("First", "Short", 50);
        notifications.notify("Second", "Long", 1200);
    }

    @Then("only the longer lived notification remains")
    public void oneNotification() {
        Dom.waitFor(() -> assertEquals(1, notifications.getWidgetCount()));
        assertTrue(notifications.getElement().getInnerText().contains("Second"));
    }

    @Then("eventually the notification region is empty")
    public void empty() { Dom.waitFor(() -> assertEquals(0, notifications.getWidgetCount()), 5000); }

    @When("the toast is detached during showing and reattached")
    public void reattachToast() {
        toast.show();
        toast.removeFromParent();
        events.clear();
        host.add(toast);
    }

    @Given("panel keyboard dismissal is disabled")
    public void disableKeyboard() { panel.setKeyboard(false); }

    @When("the panel is opened from its button")
    public void openPanel() {
        Dom.focus(open.getElement().unwrap());
        Dom.click(open.getElement().unwrap());
        Dom.waitFor(() -> {
            assertTrue(panel.getElement().hasClassName("show"));
            assertFalse(panel.getElement().hasClassName("showing"));
        });
    }

    @When("Escape is pressed inside the panel")
    public void escape() { Dom.press(panel.getElement().unwrap(), "Escape"); }

    @When("its panel close button is clicked")
    public void closePanel() { Dom.click(Dom.container().querySelector("#native-panel .btn-close")); }

    @When("its backdrop is clicked")
    public void backdrop() {
        Dom.fire(HTMLDocument.current().querySelector(".offcanvas-backdrop"), "mousedown");
    }

    @Then("the panel is hidden and opening focus is restored")
    public void hiddenPanel() {
        Dom.waitFor(() -> {
            assertFalse(panel.isShown());
            assertFalse(panel.getElement().hasClassName("hiding"));
            assertEquals(open.getElement().unwrap(), HTMLDocument.current().getActiveElement());
        });
    }

    @Then("the panel remains shown")
    public void shownPanel() { assertTrue(panel.isShown()); }

    @When("the panel is removed during opening")
    public void removeOpening() { panel.show(); panel.removeFromParent(); }

    @When("the panel is removed")
    public void remove() { panel.removeFromParent(); }

    @Then("no backdrop or body scroll lock remains")
    public void released() {
        Dom.waitFor(() -> {
            assertNull(HTMLDocument.current().querySelector(".offcanvas-backdrop"));
            assertEquals(previousOverflow, HTMLDocument.current().getBody().getStyle().getPropertyValue("overflow"));
        });
    }

    @When("its placeholder size and animation are changed")
    public void configureSkeleton() {
        skeleton.setAnimation(PlaceholderContainer.Animation.GLOW);
        skeleton.setAnimation(PlaceholderContainer.Animation.WAVE);
        placeholder.setSize(PlaceholderSize.LARGE);
        placeholder.setSize(PlaceholderSize.SMALL);
        placeholder.setColumns(6);
    }

    @Then("the skeleton is decorative and has only its final styles")
    public void skeleton() {
        assertEquals("true", skeleton.getElement().getAttribute("aria-hidden"));
        assertEquals("true", placeholder.getElement().getAttribute("aria-hidden"));
        assertTrue(skeleton.getElement().hasClassName("placeholder-wave"));
        assertFalse(skeleton.getElement().hasClassName("placeholder-glow"));
        assertTrue(placeholder.getElement().hasClassName("placeholder-sm"));
        assertFalse(placeholder.getElement().hasClassName("placeholder-lg"));
        assertTrue(placeholder.getElement().hasClassName("col-6"));
        assertFalse(placeholder.getElement().hasClassName("col-8"));
    }
}
