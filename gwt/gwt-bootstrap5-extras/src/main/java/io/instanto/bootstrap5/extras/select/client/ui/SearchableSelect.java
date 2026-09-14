package io.instanto.bootstrap5.extras.select.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import io.instanto.bootstrap5.extras.base.client.Json;
import io.instanto.bootstrap5.extras.base.client.PluginWidget;
import java.util.*;

/** Searchable single/multiple selection, tags and asynchronous option loading through Tom Select. */
public class SearchableSelect extends PluginWidget implements HasValue<List<String>>, HasEnabled {
    public interface DataProvider {
        void load(String query, AsyncCallback<List<SelectOption>> result);
    }
    private final ListBox select = new ListBox();
    private List<String> values = new ArrayList<>();
    private boolean enabled = true;
    private boolean tags;
    private boolean multiple;
    private String placeholder = "Select an option";
    private DataProvider provider;
    private final Map<String, String> options = new LinkedHashMap<>();

    public SearchableSelect() { add(select); }
    public void setMultiple(boolean multiple) { requireDetached(); this.multiple = multiple; select.setMultipleSelect(multiple); }
    public boolean isMultiple() { return multiple; }
    public void setAllowCreate(boolean tags) { requireDetached(); this.tags = tags; }
    public boolean isAllowCreate() { return tags; }
    public void setPlaceholder(String placeholder) { requireDetached(); this.placeholder = placeholder == null ? "" : placeholder; }
    public String getPlaceholder() { return placeholder; }
    public void setDataProvider(DataProvider provider) { requireDetached(); this.provider = provider; }
    public void addOption(String value, String text) {
        new SelectOption(value, text);
        if (options.containsKey(value)) throw new IllegalArgumentException("Duplicate option: " + value);
        options.put(value, text);
        select.addItem(text, value);
        if (isReady()) SelectJs.addOption(plugin(), value, text);
    }
    @Override public List<String> getValue() {
        if (isReady()) {
            values = new ArrayList<>();
            for (int i = 0; i < SelectJs.count(plugin()); i++) values.add(SelectJs.item(plugin(), i));
        }
        return new ArrayList<>(values);
    }
    @Override public void setValue(List<String> value) { setValue(value, false); }
    @Override public void setValue(List<String> value, boolean fireEvents) {
        List<String> next = value == null ? new ArrayList<>() : new ArrayList<>(value);
        if (!multiple && next.size() > 1) throw new IllegalArgumentException("Single select accepts one value");
        if (next.contains(null) || new HashSet<>(next).size() != next.size()) throw new IllegalArgumentException("Invalid selection");
        for (String item : next) {
            if (!options.containsKey(item) && !tags) throw new IllegalArgumentException("Add the option before selecting: " + item);
        }
        for (String item : next) if (!options.containsKey(item)) addOption(item, item);
        List<String> previous = getValue();
        values = next;
        if (isReady()) SelectJs.setValues(plugin(), Json.encode(values));
        if (fireEvents && !previous.equals(getValue())) ValueChangeEvent.fire(this, getValue());
    }
    @Override public boolean isEnabled() { return enabled; }
    @Override public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        select.setEnabled(enabled);
        if (isReady()) SelectJs.enabled(plugin(), enabled);
    }
    @Override public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<String>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
    @Override protected void whenReady(Runnable action) { SelectJs.whenReady(action); }
    @Override protected JavaScriptObject createPlugin() {
        return SelectJs.create(getElement(), tags, placeholder, data -> ValueChangeEvent.fire(this, getValue()));
    }
    @Override protected void afterCreate() {
        SelectJs.setValues(plugin(), Json.encode(values));
        SelectJs.enabled(plugin(), enabled);
        if (provider != null) SelectJs.remote(plugin(), request -> {
            final JavaScriptObject current = plugin();
            final String id = SelectJs.requestId(request);
            AsyncCallback<List<SelectOption>> callback = new AsyncCallback<List<SelectOption>>() {
                private boolean completed;
                @Override public void onSuccess(List<SelectOption> options) {
                    if (completed || plugin() != current) return;
                    completed = true;
                    List<Map<String, String>> data = new ArrayList<>();
                    if (options != null) for (SelectOption option : options) {
                        SearchableSelect.this.options.put(option.getValue(), option.getText());
                        Map<String, String> row = new LinkedHashMap<>();
                        row.put("value", option.getValue()); row.put("text", option.getText()); data.add(row);
                    }
                    SelectJs.complete(current, id, Json.encode(data));
                }
                @Override public void onFailure(Throwable failure) {
                    if (completed || plugin() != current) return;
                    completed = true;
                    SelectJs.complete(current, id, null);
                    com.google.gwt.core.client.GWT.log("Option loading failed", failure);
                }
            };
            try { provider.load(SelectJs.requestQuery(request), callback); }
            catch (RuntimeException failure) { callback.onFailure(failure); }
        });
    }
    @Override protected void captureState() {
        values = getValue();
        for (String value : values) if (!options.containsKey(value)) options.put(value, value);
    }
    @Override protected void destroyPlugin(JavaScriptObject plugin) {
        SelectJs.destroy(plugin);
        // Tom Select restores the original select markup, which predates remote options and tags.
        select.clear();
        for (Map.Entry<String, String> option : options.entrySet()) select.addItem(option.getValue(), option.getKey());
    }
}
