package io.instanto.bootstrap5.extras.richtext.client;

import com.google.gwt.core.client.GWT;

/**
 * How Quill arrives on GWT: compiled into the application through
 * {@link RichTextClientBundle} and injected by {@link RichTextEntryPoint} before it runs.
 *
 * <p>TeaVM builds leave this class out. The resources plugin generates a
 * {@code RichTextResources} with the same methods there, which fetches the library by URL,
 * so the widget calls one API and each platform supplies its own loading.</p>
 */
public final class RichTextResources {

    /** Whether the library is usable on the page. */
    public interface Presence {
        boolean isPresent();
    }

    private RichTextResources() {
    }

    /** Nothing to fetch: the entry point has already injected the library. */
    public static void ensureInjected() {
    }

    /**
     * Runs an action now if the library is on the page. If it is not, waiting will not
     * help -- nothing else is going to load it -- so this says so.
     */
    public static void whenReady(final Presence presence, final Runnable action) {
        if (presence.isPresent()) {
            action.run();
        } else {
            GWT.log("RichText: Quill is not on the page; the module did not load it");
        }
    }

    /** Whether the library is usable now. */
    public static boolean isReady(final Presence presence) {
        return presence.isPresent();
    }
}
