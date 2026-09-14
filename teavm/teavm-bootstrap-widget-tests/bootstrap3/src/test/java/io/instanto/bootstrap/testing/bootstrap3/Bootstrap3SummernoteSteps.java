package io.instanto.bootstrap.testing.bootstrap3;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.cucumber.tea.*;
import io.instanto.webapp.testkit.dom.Dom;
import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;
import org.gwtbootstrap3.extras.summernote.client.ui.base.*;
import org.teavm.jso.dom.html.HTMLElement;
import static org.junit.Assert.*;

@CucumberSuite("features/summernote.feature")
public class Bootstrap3SummernoteSteps {
    private RootPanel host;
    private Summernote editor;
    private int initialized, changes;
    private Object source;
    @BeforeScenario public void prepare() {
        Dom.reset(); Bootstrap3TestRuntime.initialise();
        Dom.container().setAttribute("id","summernote-test-host"); host=RootPanel.get("summernote-test-host");
    }
    @AfterScenario public void cleanup() { host.clear(); Dom.reset(); }
    private HTMLElement editable() { return Dom.container().querySelector(".note-editable"); }
    private void create() {
        editor=new Summernote(); editor.setCode("<p>Hello <b>world</b></p>");
        editor.addSummernoteInitHandler(e -> initialized++);
        editor.addSummernoteChangeHandler(e -> { changes++; source=e.getSource(); });
    }
    private void attach() { host.add(editor); Dom.waitFor(() -> assertNotNull(editable())); }
    @Given("a Summernote editor with initial HTML is ready") public void initial() { create(); attach(); }
    @Then("its initial HTML is preserved and initialization is reported once") public void html() {
        assertTrue(editor.getCode().contains("<b>world</b>")); assertEquals(1,initialized);
    }
    @When("its HTML is replaced and then cleared") public void replace() {
        editor.setCode("<p>Replacement</p>"); assertTrue(editor.getCode().contains("Replacement"));
        assertFalse(editor.getCode().contains("world")); editor.clear();
    }
    @Then("the editor is empty and changes identify the editor") public void empty() {
        assertTrue(editor.isEmpty()); assertTrue(changes>0); assertSame(editor,source);
    }
    @When("Summernote is disabled and enabled") public void disable() {
        editor.setEnabled(false); assertFalse(editor.isEnabled());
        assertEquals("false",editable().getAttribute("contenteditable")); editor.setEnabled(true);
    }
    @Then("its editable region is enabled again") public void enabled() {
        assertTrue(editor.isEnabled()); assertEquals("true",editable().getAttribute("contenteditable"));
    }
    @When("Summernote is removed and reattached") public void reattach() {
        editor.removeFromParent(); assertTrue(editor.getCode().contains("world")); attach();
    }
    @Then("its content survives without duplicate editor controls") public void retained() {
        assertTrue(editor.getCode().contains("world"));
        assertEquals(1,Dom.container().querySelectorAll(".note-editor").getLength()); assertEquals(2,initialized);
    }
    @Given("Summernote with a custom toolbar is ready") public void toolbar() {
        create(); editor.setToolbar(new Toolbar().addGroup(ToolbarButton.BOLD,ToolbarButton.CODE_VIEW)); attach();
        assertEquals(2,Dom.container().querySelectorAll(".note-toolbar button").getLength());
    }
    @When("the user opens the HTML source view") public void codeview() { Dom.click(Dom.container().querySelector("button.btn-codeview")); }
    @Then("the editor displays its source text") public void source() {
        assertNotNull(Dom.container().querySelector(".note-editor.codeview"));
        assertTrue(((org.teavm.jso.dom.html.HTMLTextAreaElement)Dom.container().querySelector(".note-codable")).getValue().contains("world"));
    }
    @Given("a French Summernote editor is ready") public void french() { create(); editor.setLanguage(SummernoteLanguage.FR_FR); attach(); }
    @When("its height is reconfigured") public void reconfigure() { editor.setDefaultHeight(180); editor.reconfigure(); }
    @Then("its French toolbar and content remain available") public void configured() {
        assertTrue(editor.getCode().contains("world"));
        assertEquals("180px",editable().getStyle().getPropertyValue("height"));
        assertTrue(Dom.container().querySelector("button.note-btn-bold").getAttribute("data-original-title").contains("Gras"));
    }
}
