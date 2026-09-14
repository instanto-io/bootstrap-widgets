package io.instanto.bootstrap5.extras.grid.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import io.instanto.bootstrap5.extras.base.client.*;
import java.util.*;

/** Tabulator table with editing, grouping, tree rows, local paging and remote scroll loading. */
public class DataTable extends PluginWidget implements HasValueChangeHandlers<String> {
    private String columns = "[]";
    private String rows = "[]";
    private String group = "";
    private String remote = "";
    private boolean tree;
    private int pageSize;
    public DataTable() { setHeight("320px"); }
    public void setColumns(List<GridColumn> columns) {
        requireDetached();
        List<Map<String, Object>> data = new ArrayList<>();
        for (GridColumn column : columns) data.add(column.data());
        this.columns = Json.encode(data);
    }
    public void setRows(List<? extends Map<String, ?>> rows) { requireDetached(); this.rows = Json.encode(rows); }
    /** Current rows, including edits. Tree children use Tabulator's _children field. */
    public String getRowsJson() { return isReady() ? GridJs.data(plugin()) : rows; }
    public void setGroupBy(String field) { requireDetached(); group = field == null ? "" : field; }
    public void setTree(boolean tree) { requireDetached(); this.tree = tree; }
    public void setPageSize(int size) { requireDetached(); if (size < 0) throw new IllegalArgumentException("Negative page size"); pageSize = size; }
    /** Server returns Tabulator's remote pagination response (last_page and data). */
    public void setRemoteUrl(String url) { requireDetached(); remote = url == null ? "" : url; }
    public void filter(String field, String value) {
        if (!isReady()) throw new IllegalStateException("Table is not attached");
        GridJs.filter(plugin(), field, value);
    }
    @Override public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
    @Override protected void whenReady(Runnable action) { GridJs.whenReady(action); }
    @Override protected JavaScriptObject createPlugin() {
        return GridJs.create(getElement(), columns, rows, group, tree, pageSize, remote,
                data -> { rows = data; ValueChangeEvent.fire(this, data); });
    }
    @Override protected void captureState() { rows = getRowsJson(); }
    @Override protected void destroyPlugin(JavaScriptObject plugin) { GridJs.destroy(plugin); }
}
