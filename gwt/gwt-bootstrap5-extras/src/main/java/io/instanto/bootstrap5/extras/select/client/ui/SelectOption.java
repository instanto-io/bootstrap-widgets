package io.instanto.bootstrap5.extras.select.client.ui;

public final class SelectOption {
    private final String value;
    private final String text;
    public SelectOption(String value, String text) {
        if (value == null || text == null) throw new IllegalArgumentException("Option value and text are required");
        this.value = value;
        this.text = text;
    }
    public String getValue() { return value; }
    public String getText() { return text; }
}
