package io.instanto.bootstrap5.extras.base.client;

import java.util.Collection;
import java.util.Map;

/** Encodes data at the plugin boundary without exposing Java objects to native JavaScript. */
public final class Json {
    private Json() { }
    public static String encode(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return quote((String) value);
        if (value instanceof Boolean) return value.toString();
        if (value instanceof Number) {
            double n = ((Number) value).doubleValue();
            if (Double.isNaN(n) || Double.isInfinite(n)) throw new IllegalArgumentException("Non-finite JSON number");
            return value.toString();
        }
        StringBuilder result = new StringBuilder();
        if (value instanceof Map) {
            result.append('{');
            boolean comma = false;
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                if (!(entry.getKey() instanceof String)) throw new IllegalArgumentException("JSON keys must be strings");
                if (comma) result.append(',');
                comma = true;
                result.append(quote((String) entry.getKey())).append(':').append(encode(entry.getValue()));
            }
            return result.append('}').toString();
        }
        if (value instanceof Collection) {
            result.append('[');
            boolean comma = false;
            for (Object item : (Collection<?>) value) {
                if (comma) result.append(',');
                comma = true;
                result.append(encode(item));
            }
            return result.append(']').toString();
        }
        throw new IllegalArgumentException("Unsupported JSON value: " + value.getClass().getName());
    }
    private static String quote(String text) {
        StringBuilder result = new StringBuilder("\"");
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"' || c == '\\') result.append('\\').append(c);
            else if (c < 32 || c == '\u2028' || c == '\u2029') {
                String hex = Integer.toHexString(c);
                result.append("\\u");
                for (int pad = hex.length(); pad < 4; pad++) result.append('0');
                result.append(hex);
            } else result.append(c);
        }
        return result.append('"').toString();
    }
}
