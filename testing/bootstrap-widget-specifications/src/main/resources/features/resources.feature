@p0 @gwt3 @teavm3 @gwt5 @teavm5
Feature: Runtime resources and providers
  Required providers and debug assets fail explicitly rather than remaining
  dormant until a consumer reaches them.

  @api-contract @browser
  Scenario: RES-001 Deferred binding resolves every declared fixture provider
    Given fixture "behaviour/resources/providers" is selected
    Given Bootstrap 3 showcase route "setup" section "Requirements" defines the baseline
    When each fixture path that uses deferred binding is constructed
    Then every declared provider resolves to an assignable implementation
    And no provider lookup returns null

  @api-contract @browser
  Scenario: RES-002 Missing provider reports its requested contract
    Given fixture "behaviour/resources/missing-provider" is selected
    Given Bootstrap 3 showcase route "setup" section "Requirements" defines the baseline
    When a deliberately unregistered deferred binding contract is requested
    Then construction fails before the fixture is mounted
    And the failure names the requested contract
    And the failure explains that no implementation was registered

  @artifact-contract @browser
  Scenario: RES-003 Showcase loads all required scripts and styles
    Given fixture "behaviour/resources/showcase-assets" is mounted
    Given Bootstrap 3 showcase route "home" section "Component examples" defines the baseline
    When the showcase readiness marker is reached
    Then every required script and stylesheet request succeeded
    And the expected Bootstrap runtime is available
    And no browser error or unhandled rejection was recorded

  @artifact-contract
  Scenario: RES-004 Published compiler output includes usable source maps
    Given fixture "behaviour/resources/source-maps" is selected
    Given Bootstrap 3 showcase route "setup" section "Requirements" defines the baseline
    When the compiled application and library debug artifacts are inspected
    Then every published JavaScript bundle names an existing source map
    And every source map names source files supplied by the source artifact
    And no source map refers to a workstation-only path
