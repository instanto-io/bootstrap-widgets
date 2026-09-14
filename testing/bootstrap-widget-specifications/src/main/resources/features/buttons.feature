@p0 @widget @gwt3 @teavm3 @gwt5 @teavm5 @skip-jvm
Feature: Button state and selection
  Bootstrap 3 showcase controls define the consumer-visible button contract.

  @browser @accessibility @functional @dom-contract
  Scenario: BTN-001 Toggle button activates once
    Given fixture "behaviour/toggle-button/basic" is mounted
    Given Bootstrap 3 showcase route "buttons" section "Toggle buttons" defines the baseline
    And the toggle button is inactive
    When the user activates the toggle button
    Then the toggle button is active
    And the toggle button has the active state class
    And the toggle button has aria-pressed "true"
    And one click is reported with the toggle button as source

  @browser @accessibility @functional @dom-contract
  Scenario: BTN-002 Toggle button deactivates on its second activation
    Given fixture "behaviour/toggle-button/basic" is mounted
    Given Bootstrap 3 showcase route "buttons" section "Toggle buttons" defines the baseline
    And the toggle button is active
    When the user activates the toggle button
    Then the toggle button is inactive
    And the toggle button does not have the active state class
    And the toggle button has aria-pressed "false"
    And one click is reported with the toggle button as source

  @api-contract
  Scenario: BTN-003 Checkbox button value change can suppress its event
    Given fixture "behaviour/check-box-button/basic" is constructed
    Given Bootstrap 3 showcase route "buttonGroups" section "Checkbox button group" defines the baseline
    When the checkbox button value is set to true without firing events
    Then the checkbox button value is true
    And no value change is reported
    When the checkbox button value is set to false and events are requested
    Then one value change is reported with the checkbox button as source

  @browser @accessibility @functional @dom-contract
  Scenario: BTN-004 Disabled toggle button ignores user activation
    Given fixture "behaviour/toggle-button/disabled" is mounted
    Given Bootstrap 3 showcase route "buttons" section "Disabled state" defines the baseline
    And the toggle button is disabled
    When the user activates the toggle button
    Then the toggle button is inactive
    And the toggle button does not have the active state class
    And no click or value change is reported

  @browser @functional
  Scenario: BTN-005 Checkbox buttons retain independent selections
    Given fixture "behaviour/check-box-buttons/independent" is mounted
    Given Bootstrap 3 showcase route "buttonGroups" section "Checkbox button group" defines the baseline
    When the user activates the first checkbox button
    And the user activates the third checkbox button
    Then the first and third checkbox button values are true
    And the second checkbox button value is false
    And each changed checkbox button reports one value change

  @browser @accessibility @functional @dom-contract
  Scenario: BTN-006 Radio buttons enforce one selection within their group
    Given fixture "behaviour/radio-buttons/exclusive" is mounted
    Given Bootstrap 3 showcase route "buttonGroups" section "Radio button group" defines the baseline
    And the first radio button is selected
    When the user activates the second radio button
    Then only the second radio button is selected
    And the first radio button has aria-pressed "false"
    And the second radio button has aria-pressed "true"
    And one value change is reported with the second radio button as source

  @api-contract @accessibility @dom-contract
  Scenario: BTN-007 Loading state replaces and then restores button content
    Given fixture "behaviour/button/loading" is constructed
    Given Bootstrap 3 showcase route "buttons" section "Loading state" defines the baseline
    And the button text is "Save"
    And the loading text is "Saving..."
    When loading state is started
    Then the button text is "Saving..."
    And the button is disabled
    And the button has aria-busy "true"
    When loading state is reset
    Then the button text is "Save"
    And the button is enabled
    And the button has no aria-busy attribute

  @api-contract @dom-contract
  Scenario: BTN-008 Button types map to their framework classes
    Given fixture "behaviour/button/types" is constructed
    Given Bootstrap 3 showcase route "buttons" section "Options" defines the baseline
    When every supported button type is assigned
    Then each button reports the assigned type
    And each button has exactly one matching framework type class
    And no type falls through to the default class

  @api-contract @dom-contract
  Scenario: BTN-009 Button sizes remain mutually exclusive
    Given fixture "behaviour/button/sizes" is constructed
    Given Bootstrap 3 showcase route "buttons" section "Sizes" defines the baseline
    When a button changes from large to small
    Then the button reports the small size
    And the small size class is present
    And the large size class is absent

  @browser @functional
  Scenario: BTN-010 Repeated checkbox activation reports one event per transition
    Given fixture "behaviour/check-box-button/repeated" is mounted
    Given Bootstrap 3 showcase route "buttonGroups" section "Checkbox button group" defines the baseline
    And the checkbox button value is false
    When the user activates the checkbox button twice
    Then the checkbox button value is false
    And two value changes are reported with the checkbox button as source

  @browser @functional
  Scenario: BTN-011 Disabled checkbox button ignores user activation
    Given fixture "behaviour/check-box-button/disabled" is mounted
    Given Bootstrap 3 showcase route "buttonGroups" section "Checkbox button group" defines the baseline
    And the checkbox button is disabled
    When the user activates the checkbox button
    Then the checkbox button value is false
    And no value change is reported

  @browser @functional
  Scenario: BTN-012 Reactivating the selected radio does not report a value change
    Given fixture "behaviour/radio-buttons/exclusive" is mounted
    Given Bootstrap 3 showcase route "buttonGroups" section "Radio button group" defines the baseline
    And the first radio button is selected
    When the user activates the first radio button
    Then only the first radio button is selected
    And no radio value change is reported
