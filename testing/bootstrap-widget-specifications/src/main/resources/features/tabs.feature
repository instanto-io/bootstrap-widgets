@p0 @widget @gwt3 @teavm3 @gwt5 @teavm5
Feature: Tab selection and events
  Tabs synchronize the selected list item, pane visibility and accessibility
  state for both user and programmatic selection.

  @browser @javascript @accessibility @functional @dom-contract
  Scenario: TAB-001 Activating a tab selects its pane
    Given fixture "behaviour/tabs/basic" is mounted
    Given Bootstrap 3 showcase route "tabs" section "Basic" defines the baseline
    And the first tab and pane are active
    When the user activates the second tab
    Then only the second tab is active
    And only the second pane is visible and active
    And the second tab has aria-selected "true"
    And the first tab has aria-selected "false"

  @browser @javascript @functional
  Scenario: TAB-002 Disabled tab cannot be selected
    Given fixture "behaviour/tabs/disabled" is mounted
    Given Bootstrap 3 showcase route "tabs" section "Basic" defines the baseline
    When the user activates the disabled third tab
    Then the first tab remains active
    And the first pane remains visible
    And no tab transition event is reported

  @browser @javascript @functional
  Scenario: TAB-003 Programmatic tab selection matches user selection
    Given fixture "behaviour/tabs/programmatic" is mounted
    Given Bootstrap 3 showcase route "tabs" section "Force Show Tabs" defines the baseline
    When the second tab is shown through the widget API
    Then only the second tab and pane are active
    And the same hide show hidden and shown event sequence is reported as for user activation

  @browser @javascript @functional @dom-contract
  Scenario: TAB-004 Fading pane becomes visible after transition
    Given fixture "behaviour/tabs/fade" is mounted
    Given Bootstrap 3 showcase route "tabs" section "Fading Content In/Out" defines the baseline
    When the user activates the second tab
    Then the second pane receives its fade and visible state classes
    And the first pane is not visible after the transition completes

  @browser @javascript @layout @style-contract @functional @dom-contract
  Scenario: TAB-005 Positioned tab sets preserve pane association
    Given fixture "behaviour/tabs/positions" is mounted
    Given Bootstrap 3 showcase route "tabs" section "Positions" defines the baseline
    When each left right and below tab set selects its second tab
    Then each second pane is visible in its own tab set
    And no tab controls a pane in another tab set
