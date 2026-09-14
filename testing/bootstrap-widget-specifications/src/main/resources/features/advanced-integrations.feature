@p1 @functional @widget @teavm5 @browser @javascript @skip-jvm
Feature: Advanced integration widgets
  Integrations load their bundled resources through the library's module loader.
  Selection, layout and child ownership survive removing and reattaching widgets.

  Scenario: Choose an option through the searchable dropdown
    Given integration fixture "select" is mounted
    When SQL is chosen in the dropdown
    Then the selection is SQL and one value event was delivered

  Scenario: Programmatic selection follows HasValue semantics
    Given integration fixture "select" is mounted
    When selection is set silently and then changed with notification
    Then exactly one selection event was delivered

  Scenario: Tags survive detach and reattach
    Given integration fixture "select" is mounted
    When a new tag is selected and the integration is reattached
    Then the new tag remains selected

  Scenario: Disable a searchable selection
    Given integration fixture "select" is mounted
    When the selection is disabled
    Then the dropdown input is disabled

  Scenario: Select an asynchronously loaded option
    Given integration fixture "remote-select" is mounted
    When a directory search is started
    And the directory result arrives
    Then the remote option can be selected

  Scenario: Ignore an asynchronous response from an old attachment
    Given integration fixture "remote-select" is mounted
    When a directory search is started
    And the integration is detached and reattached
    And the directory result arrives
    Then the old response does not change the selection

  Scenario: Filter the data grid
    Given integration fixture "grid" is mounted
    When the data grid is filtered to London
    Then only the London row is displayed

  Scenario: Dashboard layout restores existing tiles
    Given integration fixture "dashboard" is mounted
    When the dashboard tiles are rearranged through saved layout data
    Then both original tile widgets remain owned by the dashboard

  Scenario: Dashboard removal preserves the remaining tile
    Given integration fixture "dashboard" is mounted
    When a dashboard tile is removed
    Then the remaining tile is still owned by the dashboard

  Scenario: Gallery opens and closes
    Given integration fixture "gallery" is mounted
    When the gallery is opened and closed
    Then no image viewer remains open

  Scenario Outline: Integrations release their plugins and can be reattached
    Given integration fixture "<fixture>" is mounted
    When the integration is detached and reattached
    Then one ready integration remains

    Examples:
      | fixture   |
      | select    |
      | grid      |
      | sortable  |
      | dashboard |
      | gallery   |
