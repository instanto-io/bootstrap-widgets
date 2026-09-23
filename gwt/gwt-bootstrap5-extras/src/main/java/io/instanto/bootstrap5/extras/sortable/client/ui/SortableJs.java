package io.instanto.bootstrap5.extras.sortable.client.ui;

import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;
import io.instanto.bootstrap5.extras.sortable.client.SortableResources;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/** SortableJS, reached through JsInterop so GWT and TeaVM compile this one source. */
final class SortableJs {
    private SortableJs() {}

    static void whenReady(Runnable action) { SortableResources.whenReady(SortableJs::isReady, action); }

    static boolean isReady() { return Js.global().get("Sortable") != null; }

    static Sortable create(Element el, String group, boolean enabled, PluginCallback moved) {
        Options options = Js.uncheckedCast(JsPropertyMap.of());
        JsPropertyMap<Object> opts = Js.asPropertyMap(options);
        if (group != null && !group.isEmpty()) opts.set("group", group);
        opts.set("animation", 150d);
        opts.set("disabled", !enabled);
        opts.set("draggable", ".list-group-item");
        options.setOnEnd(e -> moved.onEvent(
                e.getTo().getAttribute("data-sortable-owner") + ":" + e.getOldDraggableIndex() + ":" + e.getNewDraggableIndex()));
        return new Sortable(Js.asAny(el), opts);
    }

    static void enabled(Sortable p, boolean enabled) { p.option("disabled", !enabled); }

    static void destroy(Sortable p) { p.destroy(); }

    @JsFunction interface EndListener { void handle(SortEvent event); }

    /**
     * The options that take callbacks. A typed property, not JsPropertyMap.set, because
     * TeaVM makes a lambda a JavaScript function only where its target type says so.
     */
    @JsType(isNative = true)
    interface Options {
        @JsProperty void setOnEnd(EndListener listener);
    }

    /** The event SortableJS passes to onEnd. */
    @JsType(isNative = true)
    interface SortEvent {
        @JsProperty NativeElement getTo();
        @JsProperty int getOldDraggableIndex();
        @JsProperty int getNewDraggableIndex();
    }

    @JsType(isNative = true)
    interface NativeElement { String getAttribute(String name); }

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Sortable")
    static class Sortable {
        Sortable(jsinterop.base.Any element, JsPropertyMap<Object> options) {}
        native void option(String name, Object value);
        native void destroy();
    }
}
