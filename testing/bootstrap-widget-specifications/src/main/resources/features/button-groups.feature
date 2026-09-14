@p0 @widget @gwt3 @teavm3 @gwt5 @teavm5
Feature: Button group composition
  Button groups preserve child ownership and group-level state while using the
  native layout idioms of each Bootstrap generation.

  @api-contract @dom-contract
  Scenario: BGR-001 Button group preserves insertion order
    Given fixture "behaviour/button-group/basic" is constructed
    Given Bootstrap 3 showcase route "buttonGroups" section "Basic example" defines the baseline
    When three labelled buttons are added to the group
    Then the group contains three children in insertion order
    And the button elements appear in the same order

  @api-contract @dom-contract
  Scenario: BGR-002 Group size is applied without changing child values
    Given fixture "behaviour/button-group/sizes" is constructed
    Given Bootstrap 3 showcase route "buttonGroups" section "Sizing" defines the baseline
    When the button group size changes from large to small
    Then only the small group size class is present
    And every child button retains its value

  @browser @layout @style-contract @dom-contract
  Scenario: BGR-003 Vertical group stacks its buttons
    Given fixture "behaviour/button-group/vertical" is mounted
    Given Bootstrap 3 showcase route "buttonGroups" section "Vertical variation" defines the baseline
    When the group is configured as vertical
    Then the vertical group class is present
    And each button starts below the preceding button

  @browser @javascript @functional @dom-contract
  Scenario: BGR-004 Nested dropdown remains a child of its group
    Given fixture "behaviour/button-group/nested-dropdown" is mounted
    Given Bootstrap 3 showcase route "buttonGroups" section "Nesting" defines the baseline
    When the nested dropdown is opened
    Then the dropdown menu is visible
    And the dropdown remains inside the owning button group
    And sibling buttons remain actionable

  @api-contract @dom-contract
  Scenario: BGR-005 Removing a grouped button removes its element
    Given fixture "behaviour/button-group/removal" is constructed
    Given Bootstrap 3 showcase route "buttonGroups" section "Basic example" defines the baseline
    When the middle button is removed
    Then the group contains two children
    And the removed button has no widget parent
    And the removed button element is absent from the group
