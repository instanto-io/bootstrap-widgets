package io.instanto.bootstrap.testing.bootstrap3;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.cucumber.tea.*;
import io.instanto.webapp.testkit.dom.Dom;
import org.gwtbootstrap3.extras.select.client.ui.*;
import org.teavm.jso.dom.html.HTMLElement;
import java.util.Arrays;
import static org.junit.Assert.*;

@CucumberSuite("features/select.feature")
public class Bootstrap3SelectSteps {
    private RootPanel host;
    private Select select;
    private MultipleSelect multiple;
    private Option alpha, bravo, charlie;
    private int changes;
    private Object source;
    @BeforeScenario public void prepare() {
        Dom.reset(); Bootstrap3TestRuntime.initialise();
        Dom.container().setAttribute("id", "select-test-host"); host = RootPanel.get("select-test-host");
    }
    @AfterScenario public void cleanup() { host.clear(); Dom.reset(); }
    private Option option(String label) { Option o = new Option(); o.setText(label); o.setValue(label); return o; }
    private void options(SelectBase<?> target) {
        alpha=option("Alpha"); bravo=option("Bravo"); charlie=option("Charlie");
        target.add(alpha); target.add(bravo); target.add(charlie);
    }
    private HTMLElement button() { return Dom.container().querySelector("button.dropdown-toggle"); }
    private void attach(SelectBase<?> target) {
        target.addValueChangeHandler(e -> { changes++; source=e.getSource(); });
        host.add(target); Dom.waitFor(() -> assertNotNull(button()));
    }
    @Given("a select with three options is ready") public void initial() {
        select = new Select(); options(select); select.setValue("Alpha"); attach(select);
    }
    @Then("its options and selected item correspond to the widget objects") public void identities() {
        assertEquals(3,select.getItemCount()); assertSame(alpha,select.getItem(0));
        assertEquals(Arrays.asList(alpha,bravo,charlie),select.getItems()); assertSame(alpha,select.getSelectedItem());
    }
    @When("its selection is changed silently then with notification") public void change() {
        select.setValue("Bravo"); assertEquals(0,changes);
        select.setValue("Charlie",true); select.setValue("Charlie",true);
    }
    @Then("exactly one select value change identifies the widget") public void event() {
        assertEquals(1,changes); assertSame(select,source); assertEquals("Charlie",select.getValue());
    }
    private HTMLElement choice(String text) {
        var links=Dom.container().querySelectorAll(".dropdown-menu a");
        for(int i=0;i<links.getLength();i++) {
            HTMLElement link=(HTMLElement)links.item(i);
            if(text.equals(link.getTextContent().trim())) return link;
        }
        return null;
    }
    @When("the user opens the menu and chooses Bravo") public void choose() {
        Dom.click(button()); Dom.waitFor(() -> assertNotNull(choice("Bravo"))); Dom.click(choice("Bravo"));
    }
    @Then("Bravo is selected and a value change is reported") public void chosen() {
        assertEquals("Bravo",select.getValue()); assertEquals(1,changes); assertSame(select,source);
    }
    @Given("a searchable select with three options is ready") public void searchable() {
        select=new Select(); select.setLiveSearch(true); options(select); attach(select);
    }
    @When("the user searches for Bravo") public void search() {
        Dom.click(button()); Dom.type(Dom.container().querySelector(".bs-searchbox input"),"Bravo");
    }
    @Then("the menu offers Bravo but not Alpha") public void filtered() {
        Dom.waitFor(() -> { assertNotNull(choice("Bravo")); assertNull(choice("Alpha")); });
    }
    @Given("a multiple select with a count formatter is ready") public void multi() {
        multiple=new MultipleSelect(); options(multiple); multiple.setCountSelectedTextFormat(0);
        multiple.setCountSelectedTextHandler((count,total)-> "Chosen " + count + " of " + total);
        attach(multiple);
    }
    @When("Alpha and Bravo are selected") public void several() { multiple.setValue(Arrays.asList("Alpha","Bravo")); }
    @Then("both values are selected and the custom count is shown") public void severalValues() {
        assertEquals(Arrays.asList("Alpha","Bravo"),multiple.getValue());
        assertTrue(button().getTextContent(), button().getTextContent().contains("Chosen 2 of 3"));
        assertEquals(0,changes);
    }
    @Given("a French and an English empty select are ready") public void locales() {
        multiple=new MultipleSelect(); multiple.setLanguage(SelectLanguage.FR); attach(multiple);
        select=new Select(); attach(select);
    }
    @Then("each displays its own empty selection text") public void localised() {
        var buttons=Dom.container().querySelectorAll("button.dropdown-toggle");
        assertEquals(2,buttons.getLength());
        assertTrue(buttons.item(0).getTextContent().contains("Aucune"));
        assertTrue(buttons.item(1).getTextContent(),buttons.item(1).getTextContent().contains("Nothing selected"));
    }
    @When("Bravo is removed and the select is reattached") public void reattach() {
        select.remove(bravo); select.refresh(); select.removeFromParent(); host.add(select);
        Dom.waitFor(() -> assertNotNull(button()));
    }
    @Then("its remaining option identities and controls are correct") public void retained() {
        assertEquals(Arrays.asList(alpha,charlie),select.getItems());
        assertEquals(1,Dom.container().querySelectorAll("button.dropdown-toggle").getLength());
    }
    @When("the select is disabled") public void disable() { select.setEnabled(false); }
    @Then("its button is disabled and cannot open the menu") public void disabled() {
        assertFalse(select.isEnabled()); assertEquals("true",button().getAttribute("aria-disabled"));
        Dom.click(button());
        assertEquals("false",button().getAttribute("aria-expanded"));
    }
}
