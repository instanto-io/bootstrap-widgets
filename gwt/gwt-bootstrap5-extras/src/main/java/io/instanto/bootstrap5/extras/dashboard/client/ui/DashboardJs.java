package io.instanto.bootstrap5.extras.dashboard.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;

final class DashboardJs {
    private DashboardJs() {}
    static void whenReady(Runnable action) {
        if (!isReady()) throw new IllegalStateException("Inherit the Dashboard module to load GridStack");
        action.run();
    }
    static native boolean isReady() /*-{ return typeof $wnd.GridStack !== 'undefined'; }-*/;
    static native JavaScriptObject create(Element el, boolean enabled, PluginCallback changed) /*-{
        var p=$wnd.GridStack.init({column:12,cellHeight:80,margin:8,staticGrid:!enabled},el);p.on('change',function(){changed.@io.instanto.bootstrap5.extras.base.client.PluginCallback::onEvent(Ljava/lang/String;)(JSON.stringify(p.save(false)));});return p;
    }-*/;
    static native void add(JavaScriptObject p, Element el) /*-{
        p.makeWidget(el);
    }-*/;
    static native void remove(JavaScriptObject p, Element el) /*-{
        p.removeWidget(el,false);
    }-*/;
    static native String layout(JavaScriptObject p) /*-{
        return JSON.stringify(p.save(false));
    }-*/;
    static native void restore(JavaScriptObject p, String json) /*-{
        p.load(JSON.parse(json),false);
    }-*/;
    static native void enabled(JavaScriptObject p, boolean enabled) /*-{
        p.setStatic(!enabled);
    }-*/;
    static native void destroy(JavaScriptObject p) /*-{
        p.destroy(false);
    }-*/;
}
