@p0 @gwt3 @teavm3 @gwt5 @teavm5
Feature: Theme selection and replacement
  Theme switching replaces the active stylesheet and accurately reports the
  selected theme metadata.

  @functional @dom-contract
  Scenario: THM-001 Selecting a standard theme replaces the active stylesheet
    Given fixture "behaviour/themes/switcher" is mounted
    Given Bootstrap 3 showcase route "home" section "Theme selector" defines the baseline
    And one theme stylesheet is active
    When the standard theme is selected
    Then exactly one theme stylesheet is active
    And its URL identifies the standard theme
    And the previous theme stylesheet is absent

  @api-contract @dom-contract
  Scenario: THM-002 Selecting a Bootswatch theme updates current theme metadata
    Given fixture "behaviour/themes/switcher" is constructed
    Given Bootstrap 3 showcase route "home" section "Theme selector" defines the baseline
    When a supported Bootswatch theme is selected
    Then exactly one theme stylesheet is active
    And the reported current theme is the selected theme
    And the stylesheet URL belongs to the selected theme

  @api-contract
  Scenario: THM-003 Dark theme reporting matches the selected stylesheet
    Given fixture "behaviour/themes/dark" is constructed
    Given Bootstrap 3 showcase route "home" section "Theme selector" defines the baseline
    When a dark theme is selected
    Then the theme service reports dark mode
    When a light theme is selected
    Then the theme service does not report dark mode

  @browser @functional @dom-contract
  Scenario: THM-004 Theme selection survives application restart
    Given fixture "behaviour/themes/persistence" is mounted
    Given Bootstrap 3 showcase route "home" section "Theme selector" defines the baseline
    When a non-default theme is selected
    And the application fixture is destroyed and recreated
    Then the selected theme is restored
    And exactly one matching theme stylesheet is active
