@p0 @widget @gwt3 @teavm3 @gwt5 @teavm5
Feature: Modal tooltip and popover overlays
  Overlay widgets preserve event order, focus and disposal while delegating
  visible behaviour to the native Bootstrap plugin implementation.

  @browser @javascript @accessibility @functional @dom-contract
  Scenario: OVL-001 Modal opens with an accessible dialog
    Given fixture "behaviour/modal/basic" is mounted
    Given Bootstrap 3 showcase route "modals" section "Basic" defines the baseline
    When the user activates the modal target
    Then the modal is visible
    And the modal has dialog semantics
    And the modal label points to its visible title
    And one show event occurs before one shown event

  @browser @javascript @accessibility @functional @dom-contract
  Scenario: OVL-002 Modal closes from its dismiss button and restores focus
    Given fixture "behaviour/modal/basic" is mounted
    Given Bootstrap 3 showcase route "modals" section "Basic" defines the baseline
    And the modal is open
    When the user activates the modal dismiss button
    Then the modal and backdrop are hidden
    And one hide event occurs before one hidden event
    And the modal target owns document focus

  @browser @javascript @accessibility @functional @dom-contract
  Scenario: OVL-003 Keyboard-enabled modal closes on Escape
    Given fixture "behaviour/modal/keyboard" is mounted
    Given Bootstrap 3 showcase route "modals" section "Basic" defines the baseline
    And the keyboard-enabled modal is open
    When the user presses Escape
    Then the modal is hidden
    And the modal backdrop is removed

  @browser @javascript @functional @dom-contract
  Scenario: OVL-004 Opening an exclusive modal hides the previous modal
    Given fixture "behaviour/modal/exclusive" is mounted
    Given Bootstrap 3 showcase route "modals" section "Only One Modal Active" defines the baseline
    And the first modal is open
    When the user opens the exclusive second modal
    Then the second modal is visible
    And the first modal is hidden
    And only one modal backdrop remains

  @browser @javascript @layout @style-contract @functional @dom-contract
  Scenario: OVL-005 Tooltip honours trigger placement and delays
    Given fixture "behaviour/tooltip/options" is mounted
    Given Bootstrap 3 showcase route "tooltips" section "Four directions" defines the baseline
    When a bottom click tooltip with show and hide delays is initialized
    And the user activates its target
    Then the tooltip becomes visible below the target after its show delay
    And the tooltip content matches its configured title
    And one show event occurs before one shown event

  @browser @javascript @functional @dom-contract
  Scenario: OVL-006 Tooltip disposal removes generated markup and handlers
    Given fixture "behaviour/tooltip/disposal" is mounted
    Given Bootstrap 3 showcase route "tooltips" section "Four directions" defines the baseline
    And the tooltip is visible
    When the tooltip is destroyed and its target is detached
    Then no tooltip markup remains in the document
    When the target is mounted and activated again
    Then no stale tooltip handler runs

  @browser @javascript @functional @dom-contract
  Scenario: OVL-007 Popover renders title and HTML content
    Given fixture "behaviour/popover/html" is mounted
    Given Bootstrap 3 showcase route "popover" section "Four directions" defines the baseline
    When the user activates the popover target
    Then the popover is visible
    And its title is rendered as text
    And its trusted HTML content is rendered as HTML
    And one show event occurs before one shown event

  @browser @javascript @functional @dom-contract
  Scenario: OVL-008 Popover can be shown hidden and toggled through the API
    Given fixture "behaviour/popover/programmatic" is mounted
    Given Bootstrap 3 showcase route "popover" section "Four directions" defines the baseline
    When the popover is shown through the widget API
    Then the popover is visible
    When the popover is toggled through the widget API
    Then the popover is hidden
    When the popover is shown and then hidden through the widget API
    Then the final popover state is hidden
