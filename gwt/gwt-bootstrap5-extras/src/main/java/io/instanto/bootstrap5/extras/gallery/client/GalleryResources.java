package io.instanto.bootstrap5.extras.gallery.client;

/**
 * How PhotoSwipe arrives on GWT: compiled into the application through {@link GalleryClientBundle}
 * and injected by {@link GalleryEntryPoint} before it runs.
 *
 * <p>TeaVM builds leave this class out. The resources plugin generates a {@code GalleryResources}
 * with the same methods there, which fetches the library by URL, so the widget calls one API and
 * each platform supplies its own loading.
 */
public final class GalleryResources {

  /** Whether the library is usable on the page. */
  public interface Presence {
    boolean isPresent();
  }

  private GalleryResources() {}

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
      throw new IllegalStateException("Inherit the Gallery module to load PhotoSwipeLightbox");
    }
  }

  /** Whether the library is usable now. */
  public static boolean isReady(final Presence presence) {
    return presence.isPresent();
  }
}
