package io.instanto.bootstrap5.extras.select.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;
import org.teavm.jso.*;
import io.instanto.bootstrap5.extras.select.client.SelectResources;

final class SelectJs {
    private SelectJs() {}
    @JSFunctor private interface Callback extends JSObject { void call(String data); }
    static void whenReady(Runnable action) { SelectResources.whenReady(SelectJs::isReady, action); }
    @JSBody(script="return typeof window.TomSelect !== 'undefined';")
    static native boolean isReady();
    static JavaScriptObject create(Element el, boolean tags, String placeholder, PluginCallback changed) { return JavaScriptObject.of(createNative(el.unwrap(), tags, placeholder, (Callback) changed::onEvent)); }
    @JSBody(params={"el","tags","placeholder","changed"}, script="return new window.TomSelect(el.querySelector('select'), {create:tags,placeholder:placeholder,plugins:['remove_button'],onChange:function(){ changed(JSON.stringify(this.items)); }});")
    private static native JSObject createNative(JSObject el, boolean tags, String placeholder, Callback changed);
    static void setValues(JavaScriptObject p, String json) { setValuesNative(p.unwrap(), json); }
    @JSBody(params={"p","json"}, script="p.setValue(JSON.parse(json), true);")
    private static native void setValuesNative(JSObject p, String json);
    static String values(JavaScriptObject p) { return valuesNative(p.unwrap()); }
    @JSBody(params={"p"}, script="return JSON.stringify(p.items);")
    private static native String valuesNative(JSObject p);
    static int count(JavaScriptObject p) { return countNative(p.unwrap()); }
    @JSBody(params={"p"}, script="return p.items.length;")
    private static native int countNative(JSObject p);
    static String item(JavaScriptObject p, int i) { return itemNative(p.unwrap(), i); }
    @JSBody(params={"p","i"}, script="return p.items[i];")
    private static native String itemNative(JSObject p, int i);
    static void enabled(JavaScriptObject p, boolean enabled) { enabledNative(p.unwrap(), enabled); }
    @JSBody(params={"p","enabled"}, script="if(enabled)p.enable();else p.disable();")
    private static native void enabledNative(JSObject p, boolean enabled);
    static void addOption(JavaScriptObject p, String value, String text) { addOptionNative(p.unwrap(), value, text); }
    @JSBody(params={"p","value","text"}, script="p.addOption({value:value,text:text});p.refreshOptions(false);")
    private static native void addOptionNative(JSObject p, String value, String text);
    static void remote(JavaScriptObject p, PluginCallback load) { remoteNative(p.unwrap(), (Callback) load::onEvent); }
    @JSBody(params={"p","load"}, script="p.settings.load=function(query, callback){ var id=String(++p.__requestId); p.__callbacks[id]=callback; load(JSON.stringify({id:id,query:query})); };p.__requestId=0;p.__callbacks=Object.create(null);")
    private static native void remoteNative(JSObject p, Callback load);
    static String requestId(String json) { return requestIdNative(json); }
    @JSBody(params={"json"}, script="return JSON.parse(json).id;")
    private static native String requestIdNative(String json);
    static String requestQuery(String json) { return requestQueryNative(json); }
    @JSBody(params={"json"}, script="return JSON.parse(json).query;")
    private static native String requestQueryNative(String json);
    static void complete(JavaScriptObject p, String id, String json) { completeNative(p.unwrap(), id, json); }
    @JSBody(params={"p","id","json"}, script="var callback=p.__callbacks[id];delete p.__callbacks[id];if(callback)callback(json===null?undefined:JSON.parse(json));")
    private static native void completeNative(JSObject p, String id, String json);
    static void destroy(JavaScriptObject p) { destroyNative(p.unwrap()); }
    @JSBody(params={"p"}, script="p.destroy();")
    private static native void destroyNative(JSObject p);
}
