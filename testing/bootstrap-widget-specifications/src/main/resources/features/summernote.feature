@p0 @widget @teavm3 @browser @javascript @functional @skip-jvm
Feature: Summernote editing
  Scenario: SUM-001 Initial HTML and init event
    Given a Summernote editor with initial HTML is ready
    Then its initial HTML is preserved and initialization is reported once

  Scenario: SUM-002 API replacement and clearing
    Given a Summernote editor with initial HTML is ready
    When its HTML is replaced and then cleared
    Then the editor is empty and changes identify the editor

  Scenario: SUM-003 Read-only state is reversible
    Given a Summernote editor with initial HTML is ready
    When Summernote is disabled and enabled
    Then its editable region is enabled again

  Scenario: SUM-004 Reattachment retains content
    Given a Summernote editor with initial HTML is ready
    When Summernote is removed and reattached
    Then its content survives without duplicate editor controls

  Scenario: SUM-005 Custom toolbar and code view
    Given Summernote with a custom toolbar is ready
    When the user opens the HTML source view
    Then the editor displays its source text

  Scenario: SUM-006 Locale and reconfiguration
    Given a French Summernote editor is ready
    When its height is reconfigured
    Then its French toolbar and content remain available
