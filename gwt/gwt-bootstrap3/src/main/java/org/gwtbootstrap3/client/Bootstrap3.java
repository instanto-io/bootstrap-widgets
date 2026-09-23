package org.gwtbootstrap3.client;

/**
 * Makes Bootstrap's scripts ready before an extra that depends on them.
 *
 * <p>On GWT the module's entry points inject jQuery and Bootstrap before the application
 * runs, so there is nothing to wait for. TeaVM builds leave this class out and supply one
 * that loads them on demand, so an extra calls the same method on both.</p>
 */
public final class Bootstrap3 {

    private Bootstrap3() {
    }

    /** Nothing to do: the module has already injected Bootstrap. */
    public static void initialise() {
    }

    /** Runs {@code ready} now; Bootstrap is already on the page. */
    public static void initialise(final Runnable ready) {
        ready.run();
    }
}
