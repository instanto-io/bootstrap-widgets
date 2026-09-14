@p0 @widget @gwt3 @teavm3 @gwt5 @teavm5 @skip-jvm
Feature: Form labels, values and submission
  Form controls preserve the GWT value and event contracts while producing
  accessible native form markup.

  @browser @accessibility @functional @dom-contract
  Scenario: FRM-001 Activating a checkbox label toggles its input once
    Given fixture "behaviour/form/checkbox-label" is mounted
    Given Bootstrap 3 showcase route "forms" section "Basic example" defines the baseline
    And the checkbox is unchecked
    When the user activates its label
    Then the checkbox is checked
    And one value change is reported with the checkbox as source
    And the label is associated with the checkbox

  @browser @accessibility @functional @dom-contract
  Scenario: FRM-002 Activating a radio label selects its input once
    Given fixture "behaviour/form/radio-label" is mounted
    Given Bootstrap 3 showcase route "forms" section "Inline form" defines the baseline
    And the radio is unselected
    When the user activates its label
    Then the radio is selected
    And one value change is reported with the radio as source
    And the label is associated with the radio

  @browser @accessibility @functional @dom-contract
  Scenario: FRM-003 Activating a text field label focuses its input
    Given fixture "behaviour/form/text-label" is mounted
    Given Bootstrap 3 showcase route "forms" section "Horizontal form" defines the baseline
    When the user activates the text field label
    Then the text field owns document focus
    And the label for attribute names the text field id

  @browser @functional
  Scenario: FRM-004 User input reports one value change with the widget as source
    Given fixture "behaviour/form/text-value" is mounted
    Given Bootstrap 3 showcase route "forms" section "Basic example" defines the baseline
    When the user replaces the text field value with "updated"
    And the control commits its change
    Then the text field value is "updated"
    And one value change is reported with the text field as source

  @api-contract
  Scenario: FRM-005 Explicit value assignment reports only when requested
    Given fixture "behaviour/form/values" is constructed
    Given Bootstrap 3 showcase route "forms" section "Basic example" defines the baseline
    When a text value is assigned without firing events
    Then no value change is reported
    When another text value is assigned and events are requested
    Then one value change is reported with the control as source

  @api-contract @accessibility @dom-contract
  Scenario: FRM-006 Validation state message and ARIA state agree
    Given fixture "behaviour/form/validation" is constructed
    Given Bootstrap 3 showcase route "formsWithValidation" section "Validation example" defines the baseline
    When the field is marked invalid with message "Required"
    Then the invalid framework class is present
    And the field has aria-invalid "true"
    And the validation message "Required" is present
    And the field description points to the validation message

  @browser @functional
  Scenario: FRM-007 List selection exposes one selected value
    Given fixture "behaviour/form/list-selection" is mounted
    Given Bootstrap 3 showcase route "forms" section "Horizontal form" defines the baseline
    When the user selects "Germany"
    Then the list value is "Germany"
    And exactly one option is selected
    And one value change is reported with the list as source

  @browser @functional
  Scenario: FRM-008 Radio controls sharing a name remain exclusive
    Given fixture "behaviour/form/radio-group" is mounted
    Given Bootstrap 3 showcase route "forms" section "Horizontal form" defines the baseline
    When the user selects each radio in sequence
    Then only the last radio remains selected
    And the group value is the last radio form value

  @browser @functional @dom-contract
  Scenario: FRM-009 Form submission can be cancelled before native submission
    Given fixture "behaviour/form/submission" is mounted
    Given Bootstrap 3 showcase route "forms" section "Basic example" defines the baseline
    And a submit handler cancels submission
    When the form is submitted
    Then one submit event is reported with the form as source
    And no native form submission occurs
    And the hidden target frame remains attached until the form is detached
