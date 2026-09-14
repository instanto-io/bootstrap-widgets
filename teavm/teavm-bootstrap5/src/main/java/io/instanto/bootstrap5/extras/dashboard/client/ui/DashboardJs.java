package io.instanto.bootstrap5.extras.dashboard.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;
import org.teavm.jso.*;
import io.instanto.bootstrap5.extras.dashboard.client.DashboardResources;

final class DashboardJs {
    private DashboardJs() {}
    @JSFunctor private interface Callback extends JSObject { void call(String data); }
    static void whenReady(Runnable action) { DashboardResources.whenReady(DashboardJs::isReady, action); }
    @JSBody(script="return typeof window.GridStack !== 'undefined';")
    static native boolean isReady();
    static JavaScriptObject create(Element el, boolean enabled, PluginCallback changed) { return JavaScriptObject.of(createNative(el.unwrap(), enabled, (Callback) changed::onEvent)); }
    @JSBody(params={"el","enabled","changed"}, script="var p=window.GridStack.init({column:12,cellHeight:80,margin:8,staticGrid:!enabled},el);p.on('change',function(){changed(JSON.stringify(p.save(false)));});return p;")
    private static native JSObject createNative(JSObject el, boolean enabled, Callback changed);
    static void add(JavaScriptObject p, Element el) { addNative(p.unwrap(), el.unwrap()); }
    @JSBody(params={"p","el"}, script="p.makeWidget(el);")
    private static native void addNative(JSObject p, JSObject el);
    static void remove(JavaScriptObject p, Element el) { removeNative(p.unwrap(), el.unwrap()); }
    @JSBody(params={"p","el"}, script="p.removeWidget(el,false);")
    private static native void removeNative(JSObject p, JSObject el);
    static String layout(JavaScriptObject p) { return layoutNative(p.unwrap()); }
    @JSBody(params={"p"}, script="return JSON.stringify(p.save(false));")
    private static native String layoutNative(JSObject p);
    static void restore(JavaScriptObject p, String json) { restoreNative(p.unwrap(), json); }
    @JSBody(params={"p","json"}, script="p.load(JSON.parse(json),false);")
    private static native void restoreNative(JSObject p, String json);
    static void enabled(JavaScriptObject p, boolean enabled) { enabledNative(p.unwrap(), enabled); }
    @JSBody(params={"p","enabled"}, script="p.setStatic(!enabled);")
    private static native void enabledNative(JSObject p, boolean enabled);
    static void destroy(JavaScriptObject p) { destroyNative(p.unwrap()); }
    @JSBody(params={"p"}, script="p.destroy(false);")
    private static native void destroyNative(JSObject p);
}
