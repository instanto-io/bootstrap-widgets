package io.instanto.bootstrap.testing.bootstrap3;

import org.gwtbootstrap3.client.Bootstrap3;
import org.gwtbootstrap3.client.Bootstrap3Resources;

/** Uses the test server's classpath resource endpoint for the published library assets. */
final class Bootstrap3TestRuntime {
    private Bootstrap3TestRuntime() {
    }

    static void initialise() {
        Bootstrap3Resources.setBase("/resources/META-INF/bootstrap3-assets/css/");
        boolean[] ready = {false};
        Bootstrap3.initialise(() -> ready[0] = true);
        long deadline = System.currentTimeMillis() + 10000;
        while (!ready[0] && System.currentTimeMillis() < deadline) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw new AssertionError("Interrupted waiting for Bootstrap 3", interrupted);
            }
        }
        if (!ready[0]) {
            throw new AssertionError("Bootstrap 3 dependencies did not become ready within 10 seconds");
        }
    }
}
