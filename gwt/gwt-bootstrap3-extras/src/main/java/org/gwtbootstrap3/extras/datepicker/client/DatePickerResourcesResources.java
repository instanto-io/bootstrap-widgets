package org.gwtbootstrap3.extras.datepicker.client;

/**
 * How bootstrap-datepicker arrives on GWT: the {@link DatePickerEntryPoint} injects it.
 *
 * <p>TeaVM builds leave this class out. The resources plugin generates a {@code DatePickerResourcesResources}
 * with the same methods there, which fetches the library by URL, so the widget calls one API
 * and each platform supplies its own loading.</p>
 */
public final class DatePickerResourcesResources {

    /** Whether the library is usable on the page. */
    public interface Presence {
        boolean isPresent();
    }

    private DatePickerResourcesResources() {
    }

    /** Nothing to fetch: an inherited module's entry point has already injected it. */
    public static void ensureInjected() {
        // GWT already injects the library through DatePickerEntryPoint.
    }

    /**
     * Runs an action once the library is usable. If the module was not inherited, its entry
     * point injects the library here, synchronously, and the action runs straight after.
     */
    public static void whenReady(final Presence presence, final Runnable action) {
        if (!presence.isPresent()) {
            new DatePickerEntryPoint().onModuleLoad();
        }
        action.run();
    }

    /** Whether the library is usable now. */
    public static boolean isReady(final Presence presence) {
        return presence.isPresent();
    }
}
