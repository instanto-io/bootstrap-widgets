package io.instanto.bootstrap.testing.bootstrap3;

import static io.instanto.webapp.testkit.dom.Expect.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.cucumber.tea.AfterScenario;
import io.instanto.cucumber.tea.BeforeScenario;
import io.instanto.cucumber.tea.CucumberSuite;
import io.instanto.cucumber.tea.Given;
import io.instanto.cucumber.tea.Then;
import io.instanto.cucumber.tea.When;
import io.instanto.webapp.testkit.dom.Dom;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.Radio;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.form.validator.BlankValidator;
import org.teavm.jso.dom.html.HTMLElement;

@CucumberSuite("features/forms.feature")
public class Bootstrap3FormSteps {
    private RootPanel host;
    private CheckBox checkBox;
    private Radio radio;
    private TextBox textBox;
    private FormLabel label;
    private FormGroup validationGroup;
    private HelpBlock validationMessage;
    private ListBox listBox;
    private Radio firstRadio;
    private Radio secondRadio;
    private FlowPanel radioGroup;
    private Form form;
    private int valueChanges;
    private int submitEvents;
    private int submitCompletions;
    private Object lastEventSource;

    @BeforeScenario
    public void createHost() {
        Dom.reset();
        Bootstrap3TestRuntime.initialise();
        Dom.container().setAttribute("id", "bootstrap3-form-test-host");
        host = RootPanel.get("bootstrap3-form-test-host");
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

    private void createFixture(String fixture, boolean mounted) {
        switch (fixture) {
            case "behaviour/form/checkbox-label":
                checkBox = new CheckBox("Accept");
                checkBox.setValue(false);
                checkBox.addValueChangeHandler(event -> recordValueChange(event.getSource()));
                mount(checkBox, mounted);
                return;
            case "behaviour/form/radio-label":
                radio = new Radio("form-radio-label", "Select");
                radio.setValue(false);
                radio.addValueChangeHandler(event -> recordValueChange(event.getSource()));
                mount(radio, mounted);
                return;
            case "behaviour/form/text-label":
                FormGroup labelGroup = new FormGroup();
                label = new FormLabel();
                label.setText("Account name");
                textBox = new TextBox();
                textBox.setId("form-text-label-control");
                label.setFor(textBox.getId());
                labelGroup.add(label);
                labelGroup.add(textBox);
                mount(labelGroup, mounted);
                return;
            case "behaviour/form/text-value":
                textBox = new TextBox();
                textBox.setValue("initial");
                textBox.addValueChangeHandler(event -> recordValueChange(event.getSource()));
                mount(textBox, mounted);
                return;
            case "behaviour/form/values":
                textBox = new TextBox();
                textBox.setValue("initial");
                textBox.addValueChangeHandler(event -> recordValueChange(event.getSource()));
                mount(textBox, mounted);
                return;
            case "behaviour/form/validation":
                createValidationFixture(mounted);
                return;
            case "behaviour/form/list-selection":
                listBox = new ListBox();
                listBox.addItem("France");
                listBox.addItem("Germany");
                listBox.addItem("Spain");
                listBox.addChangeHandler(event -> recordValueChange(event.getSource()));
                mount(listBox, mounted);
                return;
            case "behaviour/form/radio-group":
                createRadioGroupFixture(mounted);
                return;
            case "behaviour/form/submission":
                createSubmissionFixture(mounted);
                return;
            default:
                throw new IllegalArgumentException("Unknown fixture: " + fixture);
        }
    }

    private void createValidationFixture(boolean mounted) {
        validationGroup = new FormGroup();
        label = new FormLabel();
        label.setText("Required value");
        textBox = new TextBox();
        textBox.setId("form-validation-control");
        label.setFor(textBox.getId());
        textBox.setValidators(new BlankValidator<String>("Required"));
        validationMessage = new HelpBlock();
        validationMessage.getElement().setId("form-validation-message");
        validationGroup.add(label);
        validationGroup.add(textBox);
        validationGroup.add(validationMessage);
        mount(validationGroup, mounted);
    }

    private void createRadioGroupFixture(boolean mounted) {
        radioGroup = new FlowPanel();
        firstRadio = new Radio("form-radio-group", "First");
        firstRadio.setFormValue("first");
        secondRadio = new Radio("form-radio-group", "Second");
        secondRadio.setFormValue("second");
        firstRadio.addValueChangeHandler(event -> recordRadioGroupValue());
        secondRadio.addValueChangeHandler(event -> recordRadioGroupValue());
        radioGroup.add(firstRadio);
        radioGroup.add(secondRadio);
        mount(radioGroup, mounted);
    }

    private void createSubmissionFixture(boolean mounted) {
        form = new Form();
        form.addSubmitHandler(event -> {
            submitEvents++;
            lastEventSource = event.getSource();
            event.cancel();
        });
        form.addSubmitCompleteHandler(event -> submitCompletions++);
        form.add(new TextBox());
        mount(form, mounted);
    }

    private void mount(com.google.gwt.user.client.ui.Widget widget, boolean mounted) {
        widget.getElement().setAttribute("data-testid", widget.getClass().getSimpleName());
        if (mounted) {
            host.add(widget);
        }
    }

    private void recordValueChange(Object source) {
        valueChanges++;
        lastEventSource = source;
    }

    private void recordRadioGroupValue() {
        if (firstRadio.getValue()) {
            radioGroup.getElement().setAttribute("data-value", firstRadio.getFormValue());
        } else if (secondRadio.getValue()) {
            radioGroup.getElement().setAttribute("data-value", secondRadio.getFormValue());
        }
    }

    @Given("the checkbox is unchecked")
    public void checkboxIsUnchecked() {
        assertFalse(checkBox.getValue());
    }

    @When("the user activates its label")
    public void activateControlLabel() {
        com.google.gwt.user.client.ui.Widget control = checkBox != null ? checkBox : radio;
        Dom.click(Dom.within(element(control)).find("label"));
    }

    @Then("the checkbox is checked")
    public void checkboxIsChecked() {
        Dom.waitFor(() -> assertTrue(checkBox.getValue()));
    }

    @Then("one value change is reported with the checkbox as source")
    public void checkboxReportsOneChange() {
        assertEquals(1, valueChanges);
        assertSame(checkBox, lastEventSource);
    }

    @Then("the label is associated with the checkbox")
    public void checkboxLabelIsAssociated() {
        assertAssociatedLabel(checkBox);
    }

    @Given("the radio is unselected")
    public void radioIsUnselected() {
        assertFalse(radio.getValue());
    }

    @Then("the radio is selected")
    public void radioIsSelected() {
        Dom.waitFor(() -> assertTrue(radio.getValue()));
    }

    @Then("one value change is reported with the radio as source")
    public void radioReportsOneChange() {
        assertEquals(1, valueChanges);
        assertSame(radio, lastEventSource);
    }

    @Then("the label is associated with the radio")
    public void radioLabelIsAssociated() {
        assertAssociatedLabel(radio);
    }

    @When("the user activates the text field label")
    public void activateTextFieldLabel() {
        Dom.click(element(label));
    }

    @Then("the text field owns document focus")
    public void textFieldOwnsFocus() {
        expect(element(textBox)).toHaveFocus();
    }

    @Then("the label for attribute names the text field id")
    public void textLabelNamesControl() {
        assertEquals(textBox.getId(), label.getElement().getAttribute("for"));
    }

    @When("the user replaces the text field value with {string}")
    public void replaceTextFieldValue(String value) {
        Dom.type(element(textBox), value);
    }

    @When("the control commits its change")
    public void controlCommitsChange() {
        Dom.blur(element(textBox));
    }

    @Then("the text field value is {string}")
    public void textFieldValueIs(String value) {
        assertEquals(value, textBox.getValue());
    }

    @Then("one value change is reported with the text field as source")
    public void textFieldReportsOneChange() {
        assertEquals(1, valueChanges);
        assertSame(textBox, lastEventSource);
    }

    @When("a text value is assigned without firing events")
    public void setTextSilently() {
        textBox.setValue("silent", false);
    }

    @Then("no value change is reported")
    public void noValueChangeIsReported() {
        assertEquals(0, valueChanges);
    }

    @When("another text value is assigned and events are requested")
    public void setTextAndFire() {
        textBox.setValue("firing", true);
    }

    @Then("one value change is reported with the control as source")
    public void controlReportsOneChange() {
        assertEquals(1, valueChanges);
        assertSame(textBox, lastEventSource);
    }

    @When("the field is marked invalid with message {string}")
    public void markFieldInvalid(String message) {
        textBox.setValidators(new BlankValidator<String>(message));
        assertFalse(textBox.validate(true));
    }

    @Then("the invalid framework class is present")
    public void invalidFrameworkClassIsPresent() {
        expect(element(validationGroup)).toHaveClass("has-error");
    }

    @Then("the field has aria-invalid {string}")
    public void fieldHasInvalidState(String state) {
        expect(element(textBox)).toHaveAttribute("aria-invalid", state);
    }

    @Then("the validation message {string} is present")
    public void validationMessageIsPresent(String message) {
        expect(element(validationMessage)).toContainText(message);
    }

    @Then("the field description points to the validation message")
    public void fieldDescriptionNamesMessage() {
        assertAttributeContains(textBox.getElement().getAttribute("aria-describedby"),
                validationMessage.getElement().getId());
    }

    @When("the user selects {string}")
    public void selectListValue(String value) {
        Dom.select(element(listBox), value);
    }

    @Then("the list value is {string}")
    public void listValueIs(String value) {
        assertEquals(value, listBox.getValue(listBox.getSelectedIndex()));
    }

    @Then("exactly one option is selected")
    public void exactlyOneOptionIsSelected() {
        assertEquals(1, Dom.within(element(listBox)).findAll("option:checked").size());
    }

    @Then("one value change is reported with the list as source")
    public void listReportsOneChange() {
        assertEquals(1, valueChanges);
        assertSame(listBox, lastEventSource);
    }

    @When("the user selects each radio in sequence")
    public void selectEachRadioInSequence() {
        Dom.click(Dom.within(element(firstRadio)).find("label"));
        Dom.click(Dom.within(element(secondRadio)).find("label"));
    }

    @Then("only the last radio remains selected")
    public void onlyLastRadioIsSelected() {
        assertFalse(firstRadio.getValue());
        assertTrue(secondRadio.getValue());
    }

    @Then("the group value is the last radio form value")
    public void groupValueIsLastRadioValue() {
        assertEquals(secondRadio.getFormValue(), radioGroup.getElement().getAttribute("data-value"));
    }

    @Given("a submit handler cancels submission")
    public void submitHandlerCancelsSubmission() {
        assertNotNull(form);
    }

    @When("the form is submitted")
    public void submitForm() {
        form.submit();
    }

    @Then("one submit event is reported with the form as source")
    public void formReportsOneSubmitEvent() {
        assertEquals(1, submitEvents);
        assertSame(form, lastEventSource);
    }

    @Then("no native form submission occurs")
    public void noNativeSubmissionOccurs() {
        assertEquals(0, submitCompletions);
    }

    @Then("the hidden target frame remains attached until the form is detached")
    public void targetFrameFollowsFormLifecycle() {
        Element frame = findNamedFrame(form.getTarget());
        assertNotNull(frame);
        expect(frame.unwrap()).toBeInTheDocument();
        form.removeFromParent();
        assertNull(findNamedFrame(form.getTarget()));
    }

    private static void assertAssociatedLabel(com.google.gwt.user.client.ui.Widget control) {
        HTMLElement root = element(control);
        HTMLElement input = Dom.within(root).find("input");
        HTMLElement controlLabel = Dom.within(root).find("label");
        String controlId = input.getAttribute("id");
        boolean explicit = controlId != null && !controlId.isEmpty()
                && controlId.equals(controlLabel.getAttribute("for"));
        boolean nested = Dom.within(controlLabel).findOrNull("input") != null;
        assertTrue("Control must be nested by, or explicitly named by, its label", explicit || nested);
    }

    private static Element findNamedFrame(String name) {
        NodeList<Element> frames = Document.get().getBody().getElementsByTagName("iframe");
        for (int index = 0; index < frames.getLength(); index++) {
            Element frame = frames.getItem(index);
            if (name.equals(frame.getAttribute("name"))) {
                return frame;
            }
        }
        return null;
    }

    private static void assertAttributeContains(String attribute, String token) {
        for (String candidate : attribute.split("\\s+")) {
            if (candidate.equals(token)) {
                return;
            }
        }
        throw new AssertionError("Expected attribute to contain " + token + " but was " + attribute);
    }

    private static HTMLElement element(com.google.gwt.user.client.ui.Widget widget) {
        return widget.getElement().unwrap();
    }
}
