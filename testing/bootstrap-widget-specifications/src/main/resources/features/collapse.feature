@p0 @widget @gwt3 @teavm3 @gwt5 @teavm5
Feature: Collapse and accordion behaviour
  Collapsible content exposes stable events and accessible expanded state.

  @browser @javascript @accessibility @functional @dom-contract
  Scenario: COL-001 Collapse opens from its target control
    Given fixture "behaviour/collapse/basic" is mounted
    Given Bootstrap 3 showcase route "collapse" section "Collapse via Button target" defines the baseline
    And the collapse is hidden
    When the user activates the collapse target
    Then the collapse is visible
    And the target has aria-expanded "true"
    And the target aria-controls names the collapse id

  @browser @javascript @accessibility @functional @dom-contract
  Scenario: COL-002 Collapse closes from its target control
    Given fixture "behaviour/collapse/basic" is mounted
    Given Bootstrap 3 showcase route "collapse" section "Collapse via Button target" defines the baseline
    And the collapse is visible
    When the user activates the collapse target
    Then the collapse is hidden
    And the target has aria-expanded "false"

  @browser @javascript @functional
  Scenario: COL-003 Collapse reports ordered transition events
    Given fixture "behaviour/collapse/events" is mounted
    Given Bootstrap 3 showcase route "collapse" section "Collapse via Button target" defines the baseline
    When the collapse is shown and then hidden
    Then show occurs before shown
    And hide occurs before hidden
    And each transition event is reported exactly once with the collapse as source

  @browser @javascript @functional @dom-contract
  Scenario: COL-004 Accordion keeps only one panel open
    Given fixture "behaviour/collapse/accordion" is mounted
    Given Bootstrap 3 showcase route "collapse" section "Accordion Example using PanelCollapse" defines the baseline
    And the first accordion panel is open
    When the user opens the second accordion panel
    Then the second accordion panel is open
    And the first accordion panel is closed
    And the third accordion panel remains closed

  @browser @javascript @functional
  Scenario: COL-005 Detached collapse removes transition handlers
    Given fixture "behaviour/collapse/detach" is mounted
    Given Bootstrap 3 showcase route "collapse" section "Collapse via Button target" defines the baseline
    When the collapse is detached and mounted again
    And the user activates its target once
    Then one show event and one shown event are reported
    And no handler from the first mount is invoked
