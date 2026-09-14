package io.instanto.bootstrap5.extras.sortable.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;
import org.teavm.jso.*;
import io.instanto.bootstrap5.extras.sortable.client.SortableResources;

final class SortableJs {
    private SortableJs() {}
    @JSFunctor private interface Callback extends JSObject { void call(String data); }
    static void whenReady(Runnable action) { SortableResources.whenReady(SortableJs::isReady, action); }
    @JSBody(script="return typeof window.Sortable !== 'undefined';")
    static native boolean isReady();
    static JavaScriptObject create(Element el, String group, boolean enabled, PluginCallback moved) { return JavaScriptObject.of(createNative(el.unwrap(), group, enabled, (Callback) moved::onEvent)); }
    @JSBody(params={"el","group","enabled","moved"}, script="return new window.Sortable(el,{group:group||undefined,animation:150,disabled:!enabled,draggable:'.list-group-item',onEnd:function(e){moved(e.to.getAttribute('data-sortable-owner')+':'+e.oldDraggableIndex+':'+e.newDraggableIndex);}});")
    private static native JSObject createNative(JSObject el, String group, boolean enabled, Callback moved);
    static void enabled(JavaScriptObject p, boolean enabled) { enabledNative(p.unwrap(), enabled); }
    @JSBody(params={"p","enabled"}, script="p.option('disabled',!enabled);")
    private static native void enabledNative(JSObject p, boolean enabled);
    static void destroy(JavaScriptObject p) { destroyNative(p.unwrap()); }
    @JSBody(params={"p"}, script="p.destroy();")
    private static native void destroyNative(JSObject p);
}
