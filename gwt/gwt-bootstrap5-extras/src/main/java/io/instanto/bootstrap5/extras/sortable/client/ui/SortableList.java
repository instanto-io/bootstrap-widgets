package io.instanto.bootstrap5.extras.sortable.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.extras.base.client.PluginWidget;
import java.util.*;

/** Sortable widget container. Lists with the same group accept each other's items. */
public class SortableList extends PluginWidget implements HasEnabled, HasValueChangeHandlers<List<Widget>> {
    private static final Map<String, SortableList> ATTACHED = new HashMap<>();
    private static int nextOwner;
    private final String owner = "sortable-" + (++nextOwner);
    private String group = "";
    private boolean enabled = true;

    public SortableList() {
        setStyleName("list-group");
        getElement().setAttribute("data-sortable-owner", owner);
    }
    public void setGroup(String group) { requireDetached(); this.group = group == null ? "" : group; }
    public String getGroup() { return group; }
    @Override public void add(Widget child) { child.addStyleName("list-group-item"); super.add(child); }
    @Override public void insert(Widget child, int index) { child.addStyleName("list-group-item"); super.insert(child, index); }
    public List<Widget> getItems() {
        List<Widget> items = new ArrayList<>();
        for (int i = 0; i < getWidgetCount(); i++) items.add(getWidget(i));
        return items;
    }
    @Override public boolean isEnabled() { return enabled; }
    @Override public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (isReady()) SortableJs.enabled(plugin(), enabled);
    }
    @Override public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<Widget>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
    @Override protected void whenReady(Runnable action) { SortableJs.whenReady(action); }
    @Override protected JavaScriptObject createPlugin() {
        return SortableJs.create(getElement(), group, enabled, this::moved);
    }
    @Override protected void afterCreate() { ATTACHED.put(owner, this); }
    private void moved(String detail) {
        String[] parts = detail.split(":");
        SortableList target = ATTACHED.get(parts[0]);
        int from = Integer.parseInt(parts[1]);
        int to = Integer.parseInt(parts[2]);
        if (target == null || (target == this && from == to)) return;
        // Sortable moves the DOM first. Reconcile GWT ownership and child order as well.
        Widget item = getWidget(from);
        remove(item);
        target.insert(item, to);
        ValueChangeEvent.fire(this, getItems());
        if (target != this) ValueChangeEvent.fire(target, target.getItems());
    }
    @Override protected void destroyPlugin(JavaScriptObject plugin) {
        ATTACHED.remove(owner);
        SortableJs.destroy(plugin);
    }
}
