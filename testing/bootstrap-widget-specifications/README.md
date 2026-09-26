# Behaviour specifications

This module packages the shared Gherkin specifications used by the GWT and
TeaVM runners. The original upstream GwtBootstrap3 project and showcase from
which this repository was forked are the functional compatibility baseline;
the refreshed Bootstrap 3 implementation is a target of those specifications,
not their source of truth.

Every scenario declares:

- a stable specification ID;
- the fixture it exercises;
- whether the fixture is constructed, mounted or selected;
- the Bootstrap 3 showcase route and section from which the expectation came;
- subject, contract and execution tags;
- applicability to GWT Bootstrap 3, TeaVM Bootstrap 3, GWT Bootstrap 5 and
  TeaVM Bootstrap 5.

Each target is explicit. Use `@gwt3`, `@teavm3`, `@gwt5` or `@teavm5` when
the scenario is required there, and the corresponding `@unsupported-gwt3`,
`@unsupported-teavm3`, `@unsupported-gwt5` or `@unsupported-teavm5` when the
library deliberately does not supply that feature. Omitting a target is an
inventory error, not an implicit unsupported declaration.

`@dom-contract` and `@style-contract` do not imply `@browser`: detached markup
and static stylesheet rules can be inspected without a browser engine.

The feature files are the source of truth. The generated inventory and its Maven
check were removed: they validated tag spelling while nothing compared the scenarios
with the browser tests that implement them (see [TEST-ARCHITECTURE.md](../../TEST-ARCHITECTURE.md)).
