package io.instanto.bootstrap5.extras.sortable.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;

final class SortableJs {
    private SortableJs() {}
    static void whenReady(Runnable action) {
        if (!isReady()) throw new IllegalStateException("Inherit the Sortable module to load Sortable");
        action.run();
    }
    static native boolean isReady() /*-{ return typeof $wnd.Sortable !== 'undefined'; }-*/;
    static native JavaScriptObject create(Element el, String group, boolean enabled, PluginCallback moved) /*-{
        return new $wnd.Sortable(el,{group:group||undefined,animation:150,disabled:!enabled,draggable:'.list-group-item',onEnd:function(e){moved.@io.instanto.bootstrap5.extras.base.client.PluginCallback::onEvent(Ljava/lang/String;)(e.to.getAttribute('data-sortable-owner')+':'+e.oldDraggableIndex+':'+e.newDraggableIndex);}});
    }-*/;
    static native void enabled(JavaScriptObject p, boolean enabled) /*-{
        p.option('disabled',!enabled);
    }-*/;
    static native void destroy(JavaScriptObject p) /*-{
        p.destroy();
    }-*/;
}
