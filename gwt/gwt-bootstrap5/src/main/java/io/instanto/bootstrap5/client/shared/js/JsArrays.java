package io.instanto.bootstrap5.client.shared.js;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsArrayLike;

/** JavaScript arrays for plugin options. */
public final class JsArrays {
    private JsArrays() { }

    /** A JavaScript array holding {@code values}, converted as JsPropertyMap.set converts them. */
    public static JsArrayLike<Object> of(Object... values) {
        JsArrayLike<Object> array = Js.uncheckedCast(new NativeArray());
        for (int i = 0; i < values.length; i++) array.setAt(i, values[i]);
        return array;
    }

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Array")
    private static class NativeArray { }
}
