package io.instanto.bootstrap5.extras.grid.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;

final class GridJs {
    private GridJs() {}
    static void whenReady(Runnable action) {
        if (!isReady()) throw new IllegalStateException("Inherit the Grid module to load Tabulator");
        action.run();
    }
    static native boolean isReady() /*-{ return typeof $wnd.Tabulator !== 'undefined'; }-*/;
    static native JavaScriptObject create(Element el, String columns, String data, String group, boolean tree, int pageSize, String remote, PluginCallback changed) /*-{
        var opts={columns:JSON.parse(columns),data:JSON.parse(data),layout:'fitColumns',dataTree:tree};opts.columns.forEach(function(c){c.titleFormatter=function(){var span=el.ownerDocument.createElement('span');span.textContent=c.title;return span;};});if(group){opts.groupBy=group;opts.groupHeader=function(value,count){var span=el.ownerDocument.createElement('span');span.textContent=value+' ('+count+')';return span;};}if(pageSize>0){opts.pagination=true;opts.paginationSize=pageSize;}if(remote){opts.ajaxURL=remote;opts.progressiveLoad='scroll';delete opts.pagination;delete opts.paginationSize;delete opts.data;}var p=new $wnd.Tabulator(el,opts);p.__ready=false;p.__pending=[];p.on('tableBuilt',function(){p.__ready=true;p.__pending.splice(0).forEach(function(f){f();});});p.on('cellEdited',function(){changed.@io.instanto.bootstrap5.extras.base.client.PluginCallback::onEvent(Ljava/lang/String;)(JSON.stringify(p.getData()));});return p;
    }-*/;
    static native String data(JavaScriptObject p) /*-{
        return JSON.stringify(p.getData());
    }-*/;
    static native void filter(JavaScriptObject p, String field, String value) /*-{
        var apply=function(){if(value)p.setFilter(field,'like',value);else p.clearFilter();};if(p.__ready)apply();else p.__pending.push(apply);
    }-*/;
    static native void destroy(JavaScriptObject p) /*-{
        p.destroy();
    }-*/;
}
