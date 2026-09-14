package io.instanto.bootstrap5.extras.grid.client.ui;

import java.util.LinkedHashMap;
import java.util.Map;

public final class GridColumn {
    private final String title;
    private final String field;
    private boolean editable;
    public GridColumn(String title, String field) {
        if (title == null || field == null || field.isEmpty()) throw new IllegalArgumentException("Title and field are required");
        this.title = title; this.field = field;
    }
    public String getTitle() { return title; }
    public String getField() { return field; }
    public void setEditable(boolean editable) { this.editable = editable; }
    public boolean isEditable() { return editable; }
    Map<String, Object> data() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("title", title); result.put("field", field); result.put("formatter", "plaintext");
        if (editable) result.put("editor", "input");
        return result;
    }
}
