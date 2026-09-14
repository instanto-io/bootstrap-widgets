package io.instanto.bootstrap5.extras.select.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;

final class SelectJs {
    private SelectJs() {}
    static void whenReady(Runnable action) {
        if (!isReady()) throw new IllegalStateException("Inherit the Select module to load TomSelect");
        action.run();
    }
    static native boolean isReady() /*-{ return typeof $wnd.TomSelect !== 'undefined'; }-*/;
    static native JavaScriptObject create(Element el, boolean tags, String placeholder, PluginCallback changed) /*-{
        return new $wnd.TomSelect(el.querySelector('select'), {create:tags,placeholder:placeholder,plugins:['remove_button'],onChange:function(){ changed.@io.instanto.bootstrap5.extras.base.client.PluginCallback::onEvent(Ljava/lang/String;)(JSON.stringify(this.items)); }});
    }-*/;
    static native void setValues(JavaScriptObject p, String json) /*-{
        p.setValue(JSON.parse(json), true);
    }-*/;
    static native String values(JavaScriptObject p) /*-{
        return JSON.stringify(p.items);
    }-*/;
    static native int count(JavaScriptObject p) /*-{
        return p.items.length;
    }-*/;
    static native String item(JavaScriptObject p, int i) /*-{
        return p.items[i];
    }-*/;
    static native void enabled(JavaScriptObject p, boolean enabled) /*-{
        if(enabled)p.enable();else p.disable();
    }-*/;
    static native void addOption(JavaScriptObject p, String value, String text) /*-{
        p.addOption({value:value,text:text});p.refreshOptions(false);
    }-*/;
    static native void remote(JavaScriptObject p, PluginCallback load) /*-{
        p.settings.load=function(query, callback){ var id=String(++p.__requestId); p.__callbacks[id]=callback; load.@io.instanto.bootstrap5.extras.base.client.PluginCallback::onEvent(Ljava/lang/String;)(JSON.stringify({id:id,query:query})); };p.__requestId=0;p.__callbacks=Object.create(null);
    }-*/;
    static native String requestId(String json) /*-{
        return JSON.parse(json).id;
    }-*/;
    static native String requestQuery(String json) /*-{
        return JSON.parse(json).query;
    }-*/;
    static native void complete(JavaScriptObject p, String id, String json) /*-{
        var callback=p.__callbacks[id];delete p.__callbacks[id];if(callback)callback(json===null?undefined:JSON.parse(json));
    }-*/;
    static native void destroy(JavaScriptObject p) /*-{
        p.destroy();
    }-*/;
}
