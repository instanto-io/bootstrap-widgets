@p0 @widget @gwt3 @teavm3 @gwt5 @teavm5 @skip-jvm
Feature: Dropdown direction and lifecycle
  Dropdowns retain their public composition model while each Bootstrap
  generation owns the plugin behaviour and positioning.

  @browser @javascript @accessibility @functional @dom-contract
  Scenario: DRP-001 Dropdown opens and reports expanded state
    Given fixture "behaviour/dropdown/basic" is mounted
    Given Bootstrap 3 showcase route "dropdowns" section "Basic example" defines the baseline
    And the dropdown menu is closed
    When the user activates the dropdown toggle
    Then the dropdown menu is visible
    And the toggle has aria-expanded "true"
    And one show event occurs before one shown event

  @browser @javascript @accessibility @functional @dom-contract
  Scenario: DRP-002 Dropdown closes on outside activation
    Given fixture "behaviour/dropdown/basic" is mounted
    Given Bootstrap 3 showcase route "dropdowns" section "Basic example" defines the baseline
    And the dropdown menu is open
    When the user activates the document outside the dropdown
    Then the dropdown menu is hidden
    And the toggle has aria-expanded "false"
    And one hide event occurs before one hidden event

  @browser @javascript @accessibility @functional @dom-contract
  Scenario: DRP-003 Dropdown closes on Escape and returns focus
    Given fixture "behaviour/dropdown/basic" is mounted
    Given Bootstrap 3 showcase route "dropdowns" section "Basic example" defines the baseline
    And the dropdown menu is open
    When the user presses Escape in the menu
    Then the dropdown menu is hidden
    And the dropdown toggle owns document focus

  @browser @javascript @functional
  Scenario: DRP-004 Disabled dropdown item cannot activate
    Given fixture "behaviour/dropdown/disabled-item" is mounted
    Given Bootstrap 3 showcase route "dropdowns" section "Disabled menu items" defines the baseline
    When the user activates the disabled menu item
    Then the disabled item action is not reported
    And the current location is unchanged

  @browser @javascript @functional
  Scenario: DRP-005 Split dropdown keeps its primary action independent
    Given fixture "behaviour/dropdown/split-button" is mounted
    Given Bootstrap 3 showcase route "buttonDropdowns" section "Split button dropdowns" defines the baseline
    When the user activates the primary button
    Then one primary action is reported
    And the dropdown menu remains closed
    When the user activates the adjacent toggle
    Then the dropdown menu is visible
    And no additional primary action is reported

  @browser @javascript @layout @style-contract @functional @dom-contract
  Scenario: DRP-006 Dropup menu is positioned above its toggle
    Given fixture "behaviour/dropdown/dropup" is mounted
    Given Bootstrap 3 showcase route "buttonDropdowns" section "Dropup variation" defines the baseline
    When the user opens the dropup
    Then the menu is visible
    And the menu bottom is no lower than the toggle top

  @browser @javascript @layout @style-contract @functional @dom-contract
  Scenario: DRP-007 Constrained dropdown menu aligns with its owning button
    Given fixture "behaviour/dropdown/aligned-width" is mounted
    Given Bootstrap 3 showcase route "buttonDropdowns" section "Button dropdown sizing" defines the baseline
    When the user opens the constrained dropdown
    Then the menu is visible
    And the menu width equals the owning button group width
    And no menu item overflows horizontally
