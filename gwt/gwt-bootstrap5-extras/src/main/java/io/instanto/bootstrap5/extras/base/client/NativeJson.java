package io.instanto.bootstrap5.extras.base.client;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/** The browser's JSON, for data a plugin reads or writes as JavaScript values. */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "JSON")
public final class NativeJson {
    private NativeJson() { }
    public static native Object parse(String json);
    public static native String stringify(Object value);
}
