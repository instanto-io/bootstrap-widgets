package io.instanto.bootstrap.testing.bootstrap5;

import io.instanto.bootstrap5.client.Bootstrap5;
import io.instanto.bootstrap5.client.Bootstrap5Resources;
import io.instanto.bootstrap5.client.ui.html.Div;
import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.webapp.testkit.dom.Dom;
import static io.instanto.webapp.testkit.dom.Expect.expect;
import static org.junit.Assert.assertTrue;

/** Uses the same packaged module loader as an application, including its stylesheets. */
final class Bootstrap5TestRuntime {
    private Bootstrap5TestRuntime() { }

    static void initialise() {
        Bootstrap5Resources.setAssetBase("/resources/META-INF/bootstrap5-assets/");
        boolean[] ready = {false};
        Bootstrap5.initialise(() -> ready[0] = true);
        Dom.waitFor(() -> assertTrue("Bootstrap module did not become ready", ready[0]), 10000);
        // Script readiness does not imply that the browser has finished applying CSS.
        Div probe = new Div();
        probe.addStyleName("d-none");
        RootPanel.get().add(probe);
        try {
            Dom.waitFor(() -> expect(probe.getElement().unwrap()).toBeHidden(), 10000);
        } finally {
            probe.removeFromParent();
        }
    }
}
