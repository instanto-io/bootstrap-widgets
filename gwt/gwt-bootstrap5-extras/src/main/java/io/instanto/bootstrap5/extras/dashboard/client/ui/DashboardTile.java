package io.instanto.bootstrap5.extras.dashboard.client.ui;

import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.html.Div;

/** A dashboard tile containing ordinary widgets; its key identifies saved layout entries. */
public class DashboardTile extends Div {
    private final Div content = new Div();
    private final String key;
    public DashboardTile(String key, int x, int y, int width, int height) {
        if (key == null || key.isEmpty()) throw new IllegalArgumentException("Tile key is required");
        if (x < 0 || y < 0 || width < 1 || height < 1 || x + width > 12)
            throw new IllegalArgumentException("Tile must fit the twelve-column grid");
        this.key = key;
        setStyleName("grid-stack-item");
        getElement().setAttribute("gs-id", key);
        getElement().setAttribute("gs-x", String.valueOf(x));
        getElement().setAttribute("gs-y", String.valueOf(y));
        getElement().setAttribute("gs-w", String.valueOf(width));
        getElement().setAttribute("gs-h", String.valueOf(height));
        content.setStyleName("grid-stack-item-content card p-3");
        super.add(content);
    }
    public String getKey() { return key; }
    public Div getContent() { return content; }
    @Override public void add(Widget child) { content.add(child); }
}
