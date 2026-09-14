@p0 @widget @gwt3 @teavm3 @gwt5 @teavm5 @skip-jvm
Feature: Widget lifecycle
  Every exported widget must support predictable parent, attachment and
  handler lifecycle across repeated use.

  @browser @functional @dom-contract
  Scenario: LIF-001 Widget mounts with one parent and one DOM parent
    Given fixture "behaviour/lifecycle/basic" is mounted
    Given Bootstrap 3 showcase route "home" section "Component examples" defines the baseline
    Then the widget reports the fixture host as its parent
    And the widget reports itself attached
    And its element has exactly one DOM parent
    And its stable fixture id is present

  @browser @functional @dom-contract
  Scenario: LIF-002 Widget detaches cleanly
    Given fixture "behaviour/lifecycle/basic" is mounted
    Given Bootstrap 3 showcase route "home" section "Component examples" defines the baseline
    When the widget is removed from its parent
    Then the widget has no parent
    And the widget reports itself detached
    And its element has no DOM parent

  @browser @functional @dom-contract
  Scenario: LIF-003 Widget can be mounted again after detach
    Given fixture "behaviour/lifecycle/remount" is mounted
    Given Bootstrap 3 showcase route "home" section "Component examples" defines the baseline
    When the widget is detached and mounted in a fresh host
    Then the fresh host is the widget parent
    And one new attach event is reported
    And the previous host remains empty

  @browser @functional
  Scenario: LIF-004 Repeated mounting does not duplicate handlers
    Given fixture "behaviour/lifecycle/handlers" is mounted
    Given Bootstrap 3 showcase route "buttons" section "Options" defines the baseline
    When the interactive widget is detached and mounted three times
    And the user activates it once
    Then its action handler runs exactly once
    And no detached host receives an event

  @browser @javascript @functional @dom-contract
  Scenario: LIF-005 Plugin widget disposes document elements on detach
    Given fixture "behaviour/lifecycle/plugin" is mounted
    Given Bootstrap 3 showcase route "tooltips" section "Four directions" defines the baseline
    And the plugin has created document-level markup
    When the plugin widget is detached
    Then its document-level markup is removed
    And remounting creates exactly one replacement element
