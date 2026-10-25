package io.instanto.bootstrap5.extras.fullcalendar.client;

/**
 * How FullCalendarJS arrives on GWT: compiled into the application through {@link
 * FullCalendarClientBundle} and injected by {@link FullCalendarEntryPoint} before it runs.
 *
 * <p>TeaVM builds leave this class out. The resources plugin generates a {@code
 * FullCalendarResources} with the same methods there, which fetches the library by URL, so the
 * widget calls one API and each platform supplies its own loading.
 */
public final class FullCalendarResources {

  /** Whether the library is usable on the page. */
  public interface Presence {
    boolean isPresent();
  }

  private FullCalendarResources() {}

  /** Nothing to fetch: the entry point has already injected the library. */
  public static void ensureInjected() {}

  /**
   * Runs an action now if the library is on the page. If it is not, waiting will not help --
   * nothing else is going to load it -- so this says so.
   */
  public static void whenReady(final Presence presence, final Runnable action) {
    if (presence.isPresent()) {
      action.run();
    } else {
      throw new IllegalStateException("Inherit the FullCalendar module to load FullCalendar");
    }
  }

  /** Whether the library is usable now. */
  public static boolean isReady(final Presence presence) {
    return presence.isPresent();
  }
}
