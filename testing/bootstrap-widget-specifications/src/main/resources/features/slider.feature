@p0 @widget @teavm3 @browser @javascript @functional @skip-jvm
Feature: Numeric and range sliders
  Scenario: SLD-001 Initial value and formatter
    Given a numeric slider from zero to ten is ready
    Then its value is four and its tooltip says Units 4

  Scenario: SLD-002 Value changes respect the event flag
    Given a numeric slider from zero to ten is ready
    When the slider API changes the value silently and then with notification
    Then the slider has sent exactly one value change with itself as source

  Scenario: SLD-003 Disabled state is reversible
    Given a numeric slider from zero to ten is ready
    When the slider is disabled and enabled
    Then the slider is enabled again

  Scenario: SLD-004 Reattachment retains live value
    Given a numeric slider from zero to ten is ready
    When the slider value is changed and it is reattached
    Then there is one slider with the changed value

  Scenario: SLD-005 Range values and tick arrays
    Given a range slider with tick labels is ready
    Then both range endpoints and tick labels are available

  Scenario: SLD-006 Keyboard changes the numeric value
    Given a numeric slider from zero to ten is ready
    When the user presses the right arrow on its handle
    Then its value increases by one
