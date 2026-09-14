@p0 @widget @gwt3
Feature: Bootstrap 3 select enhancement
  Bootstrap Select remains a Bootstrap 3 optional extra until an equivalent
  widget is deliberately supplied for the other targets.

  @browser @javascript @style-contract @dom-contract
  Scenario: SEL-001 Select initializes against the Bootstrap 3 runtime
    Given fixture "behaviour/bootstrap-select/basic" is mounted
    Given Bootstrap 3 showcase route "select" section "Basic" defines the baseline
    When the Bootstrap Select plugin initializes
    Then the native select is wrapped by one Bootstrap Select container
    And the container reports Bootstrap 3 mode
    And its toggle uses the Bootstrap 3 default button class

  @browser @javascript @functional @dom-contract
  Scenario: SEL-002 Select opens from touch input
    Given fixture "behaviour/bootstrap-select/basic" is mounted
    Given Bootstrap 3 showcase route "select" section "Basic" defines the baseline
    When the user activates the visible select toggle
    Then its menu is open and visible
    And its toggle reports the expanded state

  @browser @javascript @functional @dom-contract
  Scenario: SEL-003 Selecting an item updates the widget value once
    Given fixture "behaviour/bootstrap-select/basic" is mounted
    Given Bootstrap 3 showcase route "select" section "Basic" defines the baseline
    When the user opens the select and activates its second item
    Then the widget value is the second item value
    And the visible toggle reports the second item text
    And one value change is reported with the select as source
