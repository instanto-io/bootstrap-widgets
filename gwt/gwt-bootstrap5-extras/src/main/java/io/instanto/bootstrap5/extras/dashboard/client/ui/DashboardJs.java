package io.instanto.bootstrap5.extras.dashboard.client.ui;

import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.NativeJson;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;
import io.instanto.bootstrap5.extras.dashboard.client.DashboardResources;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/** GridStack, reached through JsInterop so GWT and TeaVM compile this one source. */
final class DashboardJs {
    private DashboardJs() {}

    static void whenReady(Runnable action) { DashboardResources.whenReady(DashboardJs::isReady, action); }

    static boolean isReady() { return Js.global().get("GridStack") != null; }

    static GridStack create(Element el, boolean enabled, PluginCallback changed) {
        JsPropertyMap<Object> opts = JsPropertyMap.of();
        opts.set("column", 12d);
        opts.set("cellHeight", 80d);
        opts.set("margin", 8d);
        opts.set("staticGrid", !enabled);
        GridStack p = GridStack.init(opts, el);
        p.on("change", () -> changed.onEvent(layout(p)));
        return p;
    }

    static void add(GridStack p, Element el) { p.makeWidget(el); }

    static void remove(GridStack p, Element el) { p.removeWidget(el, false); }

    static String layout(GridStack p) { return NativeJson.stringify(p.save(false)); }

    static void restore(GridStack p, String json) { p.load(NativeJson.parse(json), false); }

    static void enabled(GridStack p, boolean enabled) { p.setStatic(!enabled); }

    static void destroy(GridStack p) { p.destroy(false); }

    @JsFunction interface Listener { void handle(); }

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "GridStack")
    static class GridStack {
        static native GridStack init(JsPropertyMap<Object> options, Element element);
        native void on(String event, Listener listener);
        native void makeWidget(Element element);
        native void removeWidget(Element element, boolean removeDom);
        native Object save(boolean saveContent);
        native void load(Object layout, boolean addAndRemove);
        native void setStatic(boolean staticGrid);
        native void destroy(boolean removeDom);
    }
}
