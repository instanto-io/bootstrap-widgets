# SpotBugs reports

SpotBugs runs automatically in `process-classes`, after Java compilation and
before tests, and therefore also during `package`, `verify`, `install`, and
`deploy`. A `compile`-only invocation stops before this phase.

Run the default reactor locally without running tests:

```sh
./mvnw -B -ntp --fail-at-end -DskipTests process-classes
```

Use `mvn` if this checkout has no Maven wrapper. Use the same Maven settings,
credentials and profiles as your normal build. Optional/profile-only reactors
must be selected explicitly with their usual `-P` or `-f` arguments.

Each Java module writes its own reports under `target/spotbugs/`:

- `${project.artifactId}-spotbugs.xml`: native SpotBugs findings.
- `${project.artifactId}-spotbugs.sarif`: findings for SARIF viewers.
- `${project.artifactId}-spotbugs.html`: readable HTML report.

A small Ant execution names the plugin-generated HTML report after the artifact.

Findings do not fail the build (the `check` goal is not bound). Analyzer errors
still fail it. POM-only modules and modules without compiled Java classes have
no bytecode to analyze. Reports are build outputs and are removed by `clean`.
To skip analysis explicitly, use `-Dspotbugs.skip=true`.

## First-tranche disposition

The reviewed same-simple-name superclass findings are public compatibility
wrappers/adapters. Preserve their API names and inheritance; no blanket rule
suppression is configured. After the initial parsing, FlowPanel and UiBinder
fixes, the local scan has 65 high flags, all in this naming category (formerly
83 high flags).

Primitive parsing is shared by the GWT and TeaVM builds. Attach current source
artifacts when rebuilding both backends together:

```sh
mvn -B -ntp -DskipTests process-classes source:jar-no-fork
```

Regression tests live in `widget-processor` (UiBinder member access),
`teavm-bootstrap3-tests` (`ProgressBarPercentTest`) and
`teavm-gwt-compat-contracts` (`FlowPanelInsertionTest`). The full feature suite
currently has unrelated undefined Cucumber steps; the four new browser checks
were also run independently against the rebuilt classes and passed.
