package org.gwtbootstrap3.extras.toggleswitch.client;

/**
 * How Bootstrap Switch arrives on GWT: the {@link ToggleSwitchEntryPoint} injects it.
 *
 * <p>TeaVM builds leave this class out. The resources plugin generates a {@code ToggleSwitchResourcesResources}
 * with the same methods there, which fetches the library by URL, so the widget calls one API
 * and each platform supplies its own loading.</p>
 */
public final class ToggleSwitchResourcesResources {

    /** Whether the library is usable on the page. */
    public interface Presence {
        boolean isPresent();
    }

    private ToggleSwitchResourcesResources() {
    }

    /** Nothing to fetch: an inherited module's entry point has already injected it. */
    public static void ensureInjected() {
    }

    /**
     * Runs an action once the library is usable. If the module was not inherited, its entry
     * point injects the library here, synchronously, and the action runs straight after.
     */
    public static void whenReady(final Presence presence, final Runnable action) {
        if (!presence.isPresent()) {
            new ToggleSwitchEntryPoint().onModuleLoad();
        }
        action.run();
    }

    /** Whether the library is usable now. */
    public static boolean isReady(final Presence presence) {
        return presence.isPresent();
    }
}
