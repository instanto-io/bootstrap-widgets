package io.instanto.bootstrap5.extras.base.client;

import com.google.gwt.core.client.JavaScriptObject;
import io.instanto.bootstrap5.client.ui.html.Div;

/** Common attachment lifecycle for extras backed by a native plugin. */
public abstract class PluginWidget extends Div {
    private JavaScriptObject plugin;
    private int generation;

    protected abstract void whenReady(Runnable action);
    protected abstract JavaScriptObject createPlugin();
    protected abstract void destroyPlugin(JavaScriptObject plugin);
    protected void captureState() { }
    protected void afterCreate() { }
    protected final JavaScriptObject plugin() { return plugin; }
    public final boolean isReady() { return plugin != null; }

    @Override protected void onLoad() {
        super.onLoad();
        final int request = ++generation;
        whenReady(() -> {
            if (isAttached() && request == generation && plugin == null) {
                plugin = createPlugin();
                try {
                    afterCreate();
                } catch (RuntimeException failure) {
                    try { destroyPlugin(plugin); } finally { plugin = null; }
                    throw failure;
                }
            }
        });
    }

    @Override protected void onUnload() {
        ++generation;
        try {
            if (plugin != null) {
                try { captureState(); } finally { destroyPlugin(plugin); }
            }
        } finally {
            plugin = null;
            super.onUnload();
        }
    }

    protected final void requireDetached() {
        if (isAttached()) throw new IllegalStateException("Configure this option before attaching the widget");
    }
}
