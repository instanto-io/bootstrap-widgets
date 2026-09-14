@browser @javascript @functional @skip-jvm
Feature: The compiled showcase replaces pages and keeps navigation usable
  The application starts itself inside a same-origin frame. Tests do not initialise widgets or assets.

  Scenario Outline: Setup instructions match the compiler used by the showcase
    Given the showcase "<application>#setup" is open
    Then setup includes "<expected>" but not "<excluded>"
    And the application has no startup errors
    Examples:
      | application           | expected                                         | excluded                   |
      | index.html            | org.gwtbootstrap3.GwtBootstrap3                   | Bootstrap3.initialise      |
      | bootstrap5/index.html | <artifactId>gwt-bootstrap5</artifactId>            | Bootstrap5.initialise      |
      | teavm.html            | <artifactId>teavm-bootstrap3</artifactId>           | org.gwtbootstrap3.GwtBootstrap3 |
      | teavm-bootstrap5.html | <artifactId>teavm-bootstrap5</artifactId>           | <artifactId>gwt-bootstrap5</artifactId> |

  Scenario Outline: TeaVM setup explains template generation and asset initialisation
    Given the showcase "<application>#setup" is open
    Then setup includes "widget-processor" but not "Support for IE8"
    And setup includes "annotationProcessorPaths" but not "GWT Module"
    And setup includes "<initialise>" but not "Bootstrap2 Look-a-like Setup"
    And setup includes "<assets>" but not "Respond module"
    And the application has no startup errors
    Examples:
      | application           | initialise          | assets                      |
      | teavm.html            | Bootstrap3.initialise | META-INF/bootstrap3-assets/ |
      | teavm-bootstrap5.html | Bootstrap5.initialise | META-INF/bootstrap5-assets/ |

  Scenario Outline: Navigation remains readable after restoring a dark theme
    Given the showcase "<application>" is open
    When I choose theme "<theme>"
    Then the navbar uses "dark" mode with brand colour "rgb(255, 255, 255)"
    When I reopen the showcase at width <width>
    Then the navbar uses "dark" mode with brand colour "rgb(255, 255, 255)"
    When I select "Components" then "Alerts"
    Then section "alerts" is visible
    When I choose theme "Bootstrap"
    Then the navbar uses "light" mode with brand colour "rgb(0, 0, 0)"
    And the application has no startup errors
    Examples:
      | application           | theme  | width |
      | teavm-bootstrap5.html  | Darkly | 1440  |
      | teavm-bootstrap5.html  | Darkly | 390   |
      | teavm-bootstrap5.html  | Slate  | 1440  |
      | teavm-bootstrap5.html  | Slate  | 390   |
      | bootstrap5/index.html | Darkly | 1440  |
      | bootstrap5/index.html | Darkly | 390   |
      | bootstrap5/index.html | Slate  | 1440  |
      | bootstrap5/index.html | Slate  | 390   |

  Scenario Outline: Component routes render after selecting another page
    Given the showcase "<application>" is open
    When I select "Interactive" then "Toasts"
    And I select "Components" then "<page>"
    Then section "<section>" is visible
    And no standalone UiBinder demo remains
    And the application has no startup errors
    Examples:
      | application           | page             | section         |
      | teavm-bootstrap5.html | Alerts           | alerts          |
      | teavm-bootstrap5.html | Badges           | badges          |
      | teavm-bootstrap5.html | Dropdowns        | dropdowns       |
      | teavm-bootstrap5.html | Button Dropdowns | buttonDropdowns |
      | bootstrap5/index.html| Alerts           | alerts          |
      | bootstrap5/index.html| Badges           | badges          |
      | bootstrap5/index.html| Dropdowns        | dropdowns       |
      | bootstrap5/index.html| Button Dropdowns | buttonDropdowns |

  Scenario Outline: UiBinder handlers work only on their own page
    Given the showcase "<application>" is open
    When I open UiBinder and click the counter twice
    Then the bound field reports two clicks
    When I select "Components" then "Alerts"
    Then section "alerts" is visible
    And no standalone UiBinder demo remains
    And the UiBinder source template is displayed as text
    Examples:
      | application            |
      | teavm-bootstrap5.html  |
      | bootstrap5/index.html  |

  Scenario Outline: Dropdown examples open and close in the compiled application
    Given the showcase "<application>" is open
    When I select "Components" then "Button Dropdowns"
    Then section "buttonDropdowns" is visible
    When I toggle example "<toggle>" twice
    Then the application has no startup errors
    Examples:
      | application           | toggle    |
      | teavm-bootstrap5.html | Action    |
      | teavm-bootstrap5.html | Dropup    |
      | teavm-bootstrap5.html | Dropstart |
      | teavm-bootstrap5.html | Dropend   |
      | bootstrap5/index.html | Action    |
      | bootstrap5/index.html | Dropup    |
      | bootstrap5/index.html | Dropstart |
      | bootstrap5/index.html | Dropend   |

  Scenario Outline: Cross-build navigation appears once and stays inside the published site
    Given the showcase "<application>" is open
    When I open Other Builds
    Then there are exactly three other build links within the site
    And the application has no startup errors
    Examples:
      | application            |
      | teavm-bootstrap5.html  |
      | bootstrap5/index.html  |
      | teavm.html             |
      | index.html             |
