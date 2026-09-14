# Shared GWT API tests

This module and `../gwt-bootstrap-widget-tests` use
`io.instanto:gwt-api` from `Instanto-io/teavm-compat`. The Java package is
`io.instanto.gwt.testing.api`; the native GWT module is
`io.instanto.gwt.testing.api.GwtApi`.

Until the renamed artifact is published, install it and the updated BOM from
the matching compatibility checkout before building these tests:

```sh
# In the teavm-compat checkout, using JDK 21
mvn -pl gwt-api,teavm-compat-bom -am install
# In the bootstrap-widgets checkout
mvn -f gwt/gwt-user-jvm-contract-tests/pom.xml test
mvn -f gwt/gwt-bootstrap-widget-tests/pom.xml verify
```

The same shared artifact contains the portable contracts exercised by Material.
These are test dependencies; the package move does not change widget runtime APIs.
