package org.gwtbootstrap3.extras.toggleswitch.client.ui.base;

import com.google.gwt.dom.client.Element;
import org.gwtbootstrap3.client.Bootstrap3;
import org.gwtbootstrap3.extras.toggleswitch.client.ToggleSwitchResourcesResources;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/** TeaVM bridge to the vendored Bootstrap Switch plugin. */
final class ToggleSwitchJs {
    @JSFunctor
    interface ChangeHandler extends JSObject {
        void changed(boolean value);
    }

    private ToggleSwitchJs() { }

    static void whenReady(Runnable action) {
        Bootstrap3.initialise(() -> ToggleSwitchResourcesResources.whenReady(ToggleSwitchJs::isReady, action));
    }

    @JSBody(script = "return !!(window.jQuery && window.jQuery.fn.bootstrapSwitch);")
    private static native boolean isReady();

    static void init(Element element, ChangeHandler handler) {
        initNative(element.unwrap(), handler);
    }

    @JSBody(params = {"element", "handler"}, script =
            "var options = {};"
            + " for (var i = 0; i < element.attributes.length; i++) {"
            + " var attr = element.attributes[i];"
            + " if (attr.name.indexOf('data-') === 0) {"
            + " var key = attr.name.substring(5).replace(/-([a-z])/g, function(_, c) { return c.toUpperCase(); });"
            + " options[key] = attr.value === 'true' ? true : attr.value === 'false' ? false : attr.value;"
            + " }}"
            + " window.jQuery(element).bootstrapSwitch(options).on('switchChange.bootstrapSwitch.widget',"
            + " function(e, state) { handler(state); });")
    private static native void initNative(JSObject element, ChangeHandler handler);

    static void destroy(Element element) { destroyNative(element.unwrap()); }

    @JSBody(params = "element", script = "window.jQuery(element).off('.widget').bootstrapSwitch('destroy');")
    private static native void destroyNative(JSObject element);

    static void command(Element element, String command, String value) {
        commandString(element.unwrap(), command, value);
    }

    @JSBody(params = {"element", "command", "value"}, script = "window.jQuery(element).bootstrapSwitch(command, value);")
    private static native void commandString(JSObject element, String command, String value);

    static void command(Element element, String command, boolean value) {
        commandBoolean(element.unwrap(), command, value);
    }

    @JSBody(params = {"element", "command", "value"}, script = "window.jQuery(element).bootstrapSwitch(command, value);")
    private static native void commandBoolean(JSObject element, String command, boolean value);

    static String stringValue(Element element, String command) { return stringValueNative(element.unwrap(), command); }

    @JSBody(params = {"element", "command"}, script = "return String(window.jQuery(element).bootstrapSwitch(command));")
    private static native String stringValueNative(JSObject element, String command);

    static boolean booleanValue(Element element, String command) { return booleanValueNative(element.unwrap(), command); }

    @JSBody(params = {"element", "command"}, script = "return !!window.jQuery(element).bootstrapSwitch(command);")
    private static native boolean booleanValueNative(JSObject element, String command);

    static void state(Element element, boolean value, boolean skip) { stateNative(element.unwrap(), value, skip); }

    @JSBody(params = {"element", "value", "skip"}, script = "window.jQuery(element).bootstrapSwitch('state', value, skip);")
    private static native void stateNative(JSObject element, boolean value, boolean skip);

    static boolean state(Element element) { return booleanValue(element, "state"); }
}
