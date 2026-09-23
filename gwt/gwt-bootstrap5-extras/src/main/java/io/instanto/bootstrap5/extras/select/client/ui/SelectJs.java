package io.instanto.bootstrap5.extras.select.client.ui;

import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.NativeJson;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;
import io.instanto.bootstrap5.extras.select.client.SelectResources;
import java.util.HashMap;
import java.util.Map;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsArrayLike;
import jsinterop.base.JsPropertyMap;

/** Tom Select, reached through JsInterop so GWT and TeaVM compile this one source. */
final class SelectJs {
    private SelectJs() {}

    /** A Tom Select instance and the remote searches waiting for their results. */
    static final class Handle {
        private TomSelect select;
        private int requestId;
        private final Map<String, LoadCallback> callbacks = new HashMap<>();
    }

    static void whenReady(Runnable action) { SelectResources.whenReady(SelectJs::isReady, action); }

    static boolean isReady() { return Js.global().get("TomSelect") != null; }

    static Handle create(Element el, boolean tags, String placeholder, PluginCallback changed) {
        Handle handle = new Handle();
        Settings settings = Js.uncheckedCast(JsPropertyMap.of());
        JsPropertyMap<Object> opts = Js.asPropertyMap(settings);
        opts.set("create", tags);
        opts.set("placeholder", placeholder);
        opts.set("plugins", NativeJson.parse("[\"remove_button\"]"));
        settings.setOnChange(() -> changed.onEvent(values(handle)));
        NativeElement element = Js.uncheckedCast(Js.asAny(el));
        handle.select = new TomSelect(element.querySelector("select"), opts);
        return handle;
    }

    static void setValues(Handle p, String json) { p.select.setValue(NativeJson.parse(json), true); }

    static String values(Handle p) { return NativeJson.stringify(p.select.getItems()); }

    static int count(Handle p) { return p.select.getItems().getLength(); }

    static String item(Handle p, int i) { return Js.asString(p.select.getItems().getAt(i)); }

    static void enabled(Handle p, boolean enabled) {
        if (enabled) p.select.enable();
        else p.select.disable();
    }

    static void addOption(Handle p, String value, String text) {
        p.select.addOption(JsPropertyMap.<Object>of("value", value, "text", text));
        p.select.refreshOptions(false);
    }

    static void remote(Handle p, PluginCallback load) {
        p.requestId = 0;
        p.callbacks.clear();
        p.select.getSettings().setLoad((query, callback) -> {
            String id = String.valueOf(++p.requestId);
            p.callbacks.put(id, callback);
            load.onEvent(NativeJson.stringify(JsPropertyMap.<Object>of("id", id, "query", query)));
        });
    }

    static String requestId(String json) { return Js.asString(Js.asPropertyMap(NativeJson.parse(json)).get("id")); }

    static String requestQuery(String json) { return Js.asString(Js.asPropertyMap(NativeJson.parse(json)).get("query")); }

    static void complete(Handle p, String id, String json) {
        LoadCallback callback = p.callbacks.remove(id);
        if (callback != null) callback.accept(Js.asAny(json == null ? Js.undefined() : NativeJson.parse(json)));
    }

    static void destroy(Handle p) { p.select.destroy(); }

    @JsFunction interface Listener { void handle(); }
    @JsFunction interface LoadCallback { void accept(Any results); }
    @JsFunction interface Loader { void load(String query, LoadCallback callback); }

    @JsType(isNative = true)
    interface NativeElement { Any querySelector(String selectors); }

    /**
     * The settings that take callbacks. Typed properties, not JsPropertyMap.set, because
     * TeaVM makes a lambda a JavaScript function only where its target type says so.
     */
    @JsType(isNative = true)
    interface Settings {
        @JsProperty void setOnChange(Listener listener);
        @JsProperty void setLoad(Loader loader);
    }

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "TomSelect")
    static class TomSelect {
        TomSelect(Any element, JsPropertyMap<Object> settings) {}
        @JsProperty(name = "items") native JsArrayLike<Object> getItems();
        @JsProperty(name = "settings") native Settings getSettings();
        native void setValue(Object value, boolean silent);
        native void enable();
        native void disable();
        native void addOption(JsPropertyMap<Object> option);
        native void refreshOptions(boolean triggerDropdown);
        native void destroy();
    }
}
