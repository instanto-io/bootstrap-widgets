package io.instanto.bootstrap.testing.showcase;

import static io.instanto.webapp.testkit.dom.Expect.expect;
import static org.junit.Assert.*;

import io.instanto.cucumber.tea.AfterScenario;
import io.instanto.cucumber.tea.BeforeScenario;
import io.instanto.cucumber.tea.CucumberSuite;
import io.instanto.cucumber.tea.Given;
import io.instanto.cucumber.tea.Then;
import io.instanto.cucumber.tea.When;
import io.instanto.webapp.testkit.dom.Dom;
import io.instanto.webapp.testkit.dom.DomScope;
import io.instanto.webapp.testkit.app.FramedApplication;
import org.teavm.jso.dom.html.HTMLAnchorElement;
import org.teavm.jso.dom.html.HTMLElement;

@CucumberSuite("features/showcase-navigation.feature")
public class ShowcaseNavigationSteps {
    private FramedApplication app;
    private String application;
    private String selectedTheme;

    @BeforeScenario public void reset() { Dom.reset(); }

    @AfterScenario public void close() {
        try { if (app != null) app.close(); } finally { Dom.reset(); }
    }

    @Given("the showcase {string} is open")
    public void open(String application) {
        this.application = application;
        app = FramedApplication.open("/resources/applications/showcase/" + application, 1440, 900)
                .awaitReady(page -> page.findByTextOrNull("Other Builds") != null, 20000);
    }

    private DomScope page() { return app.page(); }

    @Then("setup includes {string} but not {string}")
    public void setupText(String expected, String excluded) {
        Dom.waitFor(() -> {
            String text = page().root().getTextContent();
            assertTrue("Missing setup instruction: " + expected, text.contains(expected));
            assertFalse("Setup contains instructions for another runtime: " + excluded, text.contains(excluded));
        });
    }

    @When("I choose theme {string}")
    public void chooseTheme(String theme) {
        selectedTheme = theme;
        HTMLElement toggle = page().find("#showcase > nav .dropdown:last-child > button");
        Dom.click(toggle);
        DomScope menu = page().within((HTMLElement) toggle.getParentNode());
        Dom.waitFor(() -> expect(menu.findByRole("link", theme)).toBeVisible());
        Dom.click(menu.findByRole("link", theme));
    }

    @When("I reopen the showcase at width {int}")
    public void reopen(int width) {
        app.close();
        open(application);
        app.resize(width, 900);
        if (width < 1200) {
            HTMLElement toggle = page().find("nav .navbar-toggler");
            Dom.waitFor(() -> expect(toggle).toBeVisible());
            Dom.click(toggle);
            Dom.waitFor(() -> { expect(page().find("nav .navbar-collapse")).toHaveClass("show"); });
            HTMLElement components = page().findByText("Components");
            Dom.click(toggle);
            Dom.waitFor(() -> { expect(components).toBeHidden(); });
            Dom.click(toggle);
            Dom.waitFor(() -> { expect(page().find("nav .navbar-collapse")).toHaveClass("show"); });
        }
        Dom.waitFor(() -> expect(page().findByText("Components")).toBeVisible());
    }

    @Then("the navbar uses {string} mode with brand colour {string}")
    public void navbarTheme(String mode, String colour) {
        Dom.waitFor(() -> {
            expect(page().find("#showcase > nav")).toHaveAttribute("data-bs-theme", mode);
            expect(page().find("#showcase > nav .navbar-brand")).toHaveStyle("color", colour);
            expect(page().findByText("Components")).toBeVisible();
            if ("dark".equals(mode)) {
                expect(page().findByText("Components")).toHaveStyle("color",
                        "Darkly".equals(selectedTheme) ? "rgba(255, 255, 255, 0.6)" : "rgba(255, 255, 255, 0.55)");
            }
        });
    }

    @When("I select {string} then {string}")
    public void select(String category, String destination) {
        Dom.click(page().findByText(category));
        Dom.waitFor(() -> expect(page().findByText(destination)).toBeVisible());
        Dom.click(page().findByText(destination));
    }

    @Then("section {string} is visible")
    public void visibleSection(String section) {
        Dom.waitFor(() -> {
            expect(page().find("[data-section='" + section + "']")).toBeVisible();
            if (!section.equals("toasts")) {
                HTMLElement old = page().findOrNull("[data-section='toasts']");
                if (old != null) expect(old).toBeHidden();
            }
        });
    }

    @When("I open UiBinder and click the counter twice")
    public void counter() {
        Dom.click(page().findByText("UiBinder"));
        Dom.waitFor(() -> expect(page().findByTestId("counter")).toBeVisible());
        Dom.click(page().findByTestId("counter"));
        Dom.click(page().findByTestId("counter"));
    }

    @Then("the bound field reports two clicks")
    public void counterValue() {
        expect(page().findByRole("textbox", "Bound with ui:field")).toHaveValue("the handler ran 2 times");
    }

    @When("I toggle example {string} twice")
    public void toggleExample(String label) {
        HTMLElement toggle = page().findByRole("button", label);
        HTMLElement menu = page().within((HTMLElement) toggle.getParentNode()).find(".dropdown-menu");
        Dom.click(toggle);
        Dom.waitFor(() -> {
            expect(toggle).toHaveAttribute("aria-expanded", "true");
            expect(menu).toBeVisible();
        });
        Dom.click(toggle);
        Dom.waitFor(() -> {
            expect(toggle).toHaveAttribute("aria-expanded", "false");
            expect(menu).toBeHidden();
        });
    }

    @Then("no standalone UiBinder demo remains")
    public void noPersistentDemo() { assertNull(page().findByTestIdOrNull("counter")); }

    @Then("the UiBinder source template is displayed as text")
    public void sourceText() {
        HTMLElement section = page().find("[data-section='alerts']");
        DomScope content = page().within((HTMLElement) section.getParentNode());
        assertTrue(content.findAll("pre").stream().anyMatch(pre ->
                pre.getTextContent().contains("<b:Alert type=\"SUCCESS\"")));
    }

    @When("I open Other Builds")
    public void otherBuilds() {
        assertEquals(1, page().findAllByText("Other Builds").size());
        Dom.click(page().findByText("Other Builds"));
    }

    @Then("there are exactly three other build links within the site")
    public void buildLinks() {
        Dom.waitFor(() -> {
            int links = 0;
            for (HTMLElement element : page().findAll("a")) {
                if (!element.getTextContent().contains("Showcase (")) continue;
                expect(element).toBeVisible();
                String href = ((HTMLAnchorElement) element).getHref();
                assertTrue(href, href.contains("/resources/applications/showcase/"));
                links++;
            }
            assertEquals(3, links);
        });
    }

    @Then("the application has no startup errors")
    public void healthy() { app.assertHealthy(); }
}
