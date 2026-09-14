@p0 @functional @widget @teavm5 @browser @javascript @skip-jvm
Feature: Native notifications and sliding panels
  Bootstrap owns transitions, dismissal and focus management.
  The Java widgets expose the same lifecycle events on both compilation targets.

  Scenario: Show and dismiss a persistent toast
    Given fixture "native/toast" is mounted
    When the toast is shown
    And its close button is clicked
    Then the toast lifecycle is show shown hide hidden

  Scenario: Cancel showing a toast
    Given fixture "native/toast" is mounted
    When a show handler cancels the toast
    Then the toast stays hidden

  Scenario: Cancel hiding a toast
    Given fixture "native/toast" is mounted
    When the toast is shown
    And a hide handler cancels dismissal
    Then the toast remains shown

  Scenario: Independent notification timers
    Given fixture "native/notifications" is mounted
    When two notifications with different delays are sent
    Then only the longer lived notification remains
    And eventually the notification region is empty

  Scenario: Reattach a toast without duplicate event delivery
    Given fixture "native/toast" is mounted
    When the toast is detached during showing and reattached
    And the toast is shown
    And its close button is clicked
    Then the toast lifecycle is show shown hide hidden

  Scenario: Offcanvas closes with Escape and restores focus
    Given fixture "native/offcanvas" is mounted
    When the panel is opened from its button
    And Escape is pressed inside the panel
    Then the panel is hidden and opening focus is restored

  Scenario: Offcanvas closes from its dismiss button
    Given fixture "native/offcanvas" is mounted
    When the panel is opened from its button
    And its panel close button is clicked
    Then the panel is hidden and opening focus is restored

  Scenario: Offcanvas backdrop dismisses the panel
    Given fixture "native/offcanvas" is mounted
    When the panel is opened from its button
    And its backdrop is clicked
    Then the panel is hidden and opening focus is restored

  Scenario: Offcanvas keyboard option is respected
    Given fixture "native/offcanvas" is mounted
    And panel keyboard dismissal is disabled
    When the panel is opened from its button
    And Escape is pressed inside the panel
    Then the panel remains shown

  Scenario: Removing an opening panel releases its backdrop and scroll lock
    Given fixture "native/offcanvas" is mounted
    When the panel is removed during opening
    Then no backdrop or body scroll lock remains

  Scenario: Removing a shown panel releases its backdrop and scroll lock
    Given fixture "native/offcanvas" is mounted
    When the panel is opened from its button
    And the panel is removed
    Then no backdrop or body scroll lock remains

  @markup
  Scenario: Placeholder options replace rather than accumulate styles
    Given fixture "native/placeholders" is mounted
    When its placeholder size and animation are changed
    Then the skeleton is decorative and has only its final styles
