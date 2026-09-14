package io.instanto.bootstrap5.extras.grid.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;
import org.teavm.jso.*;
import io.instanto.bootstrap5.extras.grid.client.GridResources;

final class GridJs {
    private GridJs() {}
    @JSFunctor private interface Callback extends JSObject { void call(String data); }
    static void whenReady(Runnable action) { GridResources.whenReady(GridJs::isReady, action); }
    @JSBody(script="return typeof window.Tabulator !== 'undefined';")
    static native boolean isReady();
    static JavaScriptObject create(Element el, String columns, String data, String group, boolean tree, int pageSize, String remote, PluginCallback changed) { return JavaScriptObject.of(createNative(el.unwrap(), columns, data, group, tree, pageSize, remote, (Callback) changed::onEvent)); }
    @JSBody(params={"el","columns","data","group","tree","pageSize","remote","changed"}, script="var opts={columns:JSON.parse(columns),data:JSON.parse(data),layout:'fitColumns',dataTree:tree};opts.columns.forEach(function(c){c.titleFormatter=function(){var span=el.ownerDocument.createElement('span');span.textContent=c.title;return span;};});if(group){opts.groupBy=group;opts.groupHeader=function(value,count){var span=el.ownerDocument.createElement('span');span.textContent=value+' ('+count+')';return span;};}if(pageSize>0){opts.pagination=true;opts.paginationSize=pageSize;}if(remote){opts.ajaxURL=remote;opts.progressiveLoad='scroll';delete opts.pagination;delete opts.paginationSize;delete opts.data;}var p=new window.Tabulator(el,opts);p.__ready=false;p.__pending=[];p.on('tableBuilt',function(){p.__ready=true;p.__pending.splice(0).forEach(function(f){f();});});p.on('cellEdited',function(){changed(JSON.stringify(p.getData()));});return p;")
    private static native JSObject createNative(JSObject el, String columns, String data, String group, boolean tree, int pageSize, String remote, Callback changed);
    static String data(JavaScriptObject p) { return dataNative(p.unwrap()); }
    @JSBody(params={"p"}, script="return JSON.stringify(p.getData());")
    private static native String dataNative(JSObject p);
    static void filter(JavaScriptObject p, String field, String value) { filterNative(p.unwrap(), field, value); }
    @JSBody(params={"p","field","value"}, script="var apply=function(){if(value)p.setFilter(field,'like',value);else p.clearFilter();};if(p.__ready)apply();else p.__pending.push(apply);")
    private static native void filterNative(JSObject p, String field, String value);
    static void destroy(JavaScriptObject p) { destroyNative(p.unwrap()); }
    @JSBody(params={"p"}, script="p.destroy();")
    private static native void destroyNative(JSObject p);
}
