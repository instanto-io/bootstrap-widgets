package io.instanto.bootstrap5.extras.base.client;

import io.instanto.bootstrap5.client.ui.html.Div;

/**
 * Common attachment lifecycle for extras backed by a native plugin.
 *
 * @param <P> the extra's handle on its plugin instance
 */
public abstract class PluginWidget<P> extends Div {
  private P plugin;
  private int generation;

  protected abstract void whenReady(Runnable action);

  protected abstract P createPlugin();

  protected abstract void destroyPlugin(P plugin);

  protected void captureState() {}

  protected void afterCreate() {}

  protected final P plugin() {
    return plugin;
  }

  public final boolean isReady() {
    return plugin != null;
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    final int request = ++generation;
    whenReady(
        () -> {
          if (isAttached() && request == generation && plugin == null) {
            P created = createPlugin();
            // Creation may synchronously detach the widget, or start another mount.
            if (!isAttached() || request != generation) {
              destroyPlugin(created);
              return;
            }
            plugin = created;
            try {
              afterCreate();
            } catch (RuntimeException | Error failure) {
              // afterCreate may already have detached and destroyed this instance.
              if (plugin == created) {
                plugin = null;
                try {
                  destroyPlugin(created);
                } catch (RuntimeException | Error cleanup) {
                  if (cleanup != failure) failure.addSuppressed(cleanup);
                }
              }
              throw failure;
            }
          }
        });
  }

  @Override
  protected void onUnload() {
    ++generation;
    try {
      P previous = plugin;
      if (previous != null) {
        try {
          captureState();
        } catch (RuntimeException | Error failure) {
          plugin = null;
          try {
            destroyPlugin(previous);
          } catch (RuntimeException | Error cleanup) {
            if (cleanup != failure) failure.addSuppressed(cleanup);
          }
          throw failure;
        }
        plugin = null;
        destroyPlugin(previous);
      }
    } finally {
      plugin = null;
      super.onUnload();
    }
  }

  protected final void requireDetached() {
    if (isAttached())
      throw new IllegalStateException("Configure this option before attaching the widget");
  }
}
