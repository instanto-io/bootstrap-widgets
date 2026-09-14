package io.instanto.bootstrap5.extras.base.client;

/** Callback at the native plugin boundary. Widgets translate it to GWT events. */
public interface PluginCallback {
    void onEvent(String data);
}
