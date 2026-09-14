@p1 @widget @gwt3 @teavm3 @gwt5 @teavm5 @skip-jvm
Feature: Input group composition
  Input groups preserve addon order, control sizing and nested button behaviour
  while using the native structure of their Bootstrap generation.

  @dom-contract
  Scenario: ING-001 Text addons retain their order around the input
    Given fixture "behaviour/input-group/text-addons" is constructed
    Given Bootstrap 3 showcase route "inputGroups" section "Basic" defines the baseline
    When text addons are added around the control
    Then the prefix addon precedes the text control
    And the suffix addon follows the text control
    And the text control remains editable

  @browser @functional @dom-contract
  Scenario: ING-002 A button addon activates independently of its text control
    Given fixture "behaviour/input-group/button-addon" is mounted
    Given Bootstrap 3 showcase route "inputGroups" section "Button Addons" defines the baseline
    And the grouped text control contains "original"
    When the user activates the addon button
    Then one button action is reported
    And the grouped text control still contains "original"
    And the button remains a child of the button addon

  @browser @layout @style-contract @dom-contract
  Scenario: ING-003 Grouped segments form one contiguous control
    Given fixture "behaviour/input-group/text-addons" is mounted
    Given Bootstrap 3 showcase route "inputGroups" section "Basic" defines the baseline
    When the input group is rendered
    Then adjacent segment edges meet without a layout gap
    And all segments share one vertical extent
    And the text control consumes the remaining group width

  @api-contract @dom-contract
  Scenario: ING-004 Input group size has one reported framework state
    Given fixture "behaviour/input-group/sizing" is constructed
    Given Bootstrap 3 showcase route "inputGroups" section "Basic" defines the baseline
    When the input group size is set to large
    Then the widget reports large as its size
    And exactly one large input-group class is present
    When the input group size is reset to default
    Then the widget reports default as its size
    And no stale large input-group class remains
