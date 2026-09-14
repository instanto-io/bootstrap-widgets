package io.instanto.bootstrap5.extras.dashboard.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.extras.base.client.PluginWidget;

/** Responsive dashboard with draggable/resizable tiles and serializable layout. */
public class Dashboard extends PluginWidget implements HasEnabled, HasValueChangeHandlers<String> {
    private boolean enabled = true;
    private String layout;
    public Dashboard() { setStyleName("grid-stack"); }
    @Override public void add(Widget child) {
        if (!(child instanceof DashboardTile)) throw new IllegalArgumentException("Expected a DashboardTile");
        DashboardTile tile = (DashboardTile) child;
        for (int i = 0; i < getWidgetCount(); i++) {
            if (((DashboardTile) getWidget(i)).getKey().equals(tile.getKey()))
                throw new IllegalArgumentException("Duplicate tile key: " + tile.getKey());
        }
        super.add(child);
        if (isReady()) DashboardJs.add(plugin(), child.getElement());
    }
    @Override public boolean remove(Widget child) {
        if (child.getParent() != this) return false;
        if (isReady()) DashboardJs.remove(plugin(), child.getElement());
        return super.remove(child);
    }
    public String getLayoutJson() { return isReady() ? DashboardJs.layout(plugin()) : layout; }
    /** Restore positions of existing tiles; does not create or remove Java widgets. */
    public void setLayoutJson(String layout) {
        if (layout == null) throw new IllegalArgumentException("Layout is required");
        if (isReady()) DashboardJs.restore(plugin(), layout);
        this.layout = layout;
    }
    @Override public boolean isEnabled() { return enabled; }
    @Override public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (isReady()) DashboardJs.enabled(plugin(), enabled);
    }
    @Override public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
    @Override protected void whenReady(Runnable action) { DashboardJs.whenReady(action); }
    @Override protected JavaScriptObject createPlugin() {
        return DashboardJs.create(getElement(), enabled, value -> {
            layout = value;
            ValueChangeEvent.fire(this, value);
        });
    }
    @Override protected void afterCreate() { if (layout != null) DashboardJs.restore(plugin(), layout); }
    @Override protected void captureState() { layout = getLayoutJson(); }
    @Override protected void destroyPlugin(JavaScriptObject plugin) { DashboardJs.destroy(plugin); }
}
