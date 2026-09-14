@p0 @widget @teavm3 @browser @javascript @functional @skip-jvm
Feature: Bootstrap 3 toggle switches
  Checkbox and radio switches retain the upstream widget value and event contracts.

  Scenario: TSW-001 A checkbox switch can be switched on and off
    Given a checkbox switch is ready
    When the user switches it on and off
    Then the switch is off and two value changes were reported

  Scenario: TSW-002 Programmatic changes honour the event flag
    Given a checkbox switch is ready
    When its value is assigned with and without event notification
    Then only the requested value change was reported

  Scenario: TSW-003 Disabled and read-only switches reject user changes
    Given a checkbox switch is ready
    When the user tries to change it while disabled and then read-only
    Then its value and event count are unchanged

  Scenario: TSW-004 Configuration made before attachment survives initialization
    Given a switch is configured before attachment
    When it is attached and its plugin is ready
    Then its value texts size and enabled state match the configuration

  Scenario: TSW-005 Reattachment keeps a single control and handler
    Given a checkbox switch is ready
    When the switch is removed and attached again
    Then one plugin control remains and one user change is reported

  Scenario: TSW-006 Radio switches share exclusive selection
    Given two radio switches in the same group are ready
    When the user selects each radio switch in turn
    Then only the second radio switch is selected

  Scenario: TSW-007 A detached switch does not initialise later
    Given a switch is attached and immediately removed
    When another switch finishes initializing
    Then the removed switch has no plugin wrapper

  Scenario: TSW-008 Live options survive reattachment
    Given a switch is configured before attachment
    When it is attached and its plugin is ready
    And its texts and size are changed before reattachment
    Then the updated texts and size are retained
