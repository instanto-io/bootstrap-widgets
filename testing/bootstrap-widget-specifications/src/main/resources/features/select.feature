@p0 @widget @teavm3 @browser @javascript @functional @skip-jvm
Feature: Searchable select menus
  Scenario: SEL-001 Option identity and initial selection
    Given a select with three options is ready
    Then its options and selected item correspond to the widget objects

  Scenario: SEL-002 API updates respect the event flag
    Given a select with three options is ready
    When its selection is changed silently then with notification
    Then exactly one select value change identifies the widget

  Scenario: SEL-003 Menu selection updates the value
    Given a select with three options is ready
    When the user opens the menu and chooses Bravo
    Then Bravo is selected and a value change is reported

  Scenario: SEL-004 Search filters available choices
    Given a searchable select with three options is ready
    When the user searches for Bravo
    Then the menu offers Bravo but not Alpha

  Scenario: SEL-005 Multiple selection and count callback
    Given a multiple select with a count formatter is ready
    When Alpha and Bravo are selected
    Then both values are selected and the custom count is shown

  Scenario: SEL-006 Locale does not leak between instances
    Given a French and an English empty select are ready
    Then each displays its own empty selection text

  Scenario: SEL-007 Reattachment and removal
    Given a select with three options is ready
    When Bravo is removed and the select is reattached
    Then its remaining option identities and controls are correct

  Scenario: SEL-008 Disabled menu
    Given a select with three options is ready
    When the select is disabled
    Then its button is disabled and cannot open the menu
