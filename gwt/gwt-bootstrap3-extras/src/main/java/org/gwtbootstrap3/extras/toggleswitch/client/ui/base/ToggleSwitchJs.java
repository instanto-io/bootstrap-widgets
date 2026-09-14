package org.gwtbootstrap3.extras.toggleswitch.client.ui.base;

import com.google.gwt.dom.client.Element;
import org.gwtbootstrap3.extras.toggleswitch.client.ToggleSwitchEntryPoint;

/** Browser calls shared by checkbox and radio switches. */
final class ToggleSwitchJs {
    interface ChangeHandler {
        void changed(boolean value);
    }

    private ToggleSwitchJs() { }

    static void whenReady(Runnable action) {
        if (!isReady()) {
            new ToggleSwitchEntryPoint().onModuleLoad();
        }
        action.run();
    }

    private static native boolean isReady() /*-{
        return !!($wnd.jQuery && $wnd.jQuery.fn.bootstrapSwitch);
    }-*/;

    static native void init(Element element, ChangeHandler handler) /*-{
        // Explicit options bypass jQuery's stale data cache after reattachment.
        var options = {};
        for (var i = 0; i < element.attributes.length; i++) {
            var attr = element.attributes[i];
            if (attr.name.indexOf('data-') === 0) {
                var key = attr.name.substring(5).replace(/-([a-z])/g, function(_, c) { return c.toUpperCase(); });
                options[key] = attr.value === 'true' ? true : attr.value === 'false' ? false : attr.value;
            }
        }
        $wnd.jQuery(element).bootstrapSwitch(options).on('switchChange.bootstrapSwitch.widget', function(e, state) {
            handler.@org.gwtbootstrap3.extras.toggleswitch.client.ui.base.ToggleSwitchJs$ChangeHandler::changed(Z)(state);
        });
    }-*/;

    static native void destroy(Element element) /*-{
        $wnd.jQuery(element).off('.widget').bootstrapSwitch('destroy');
    }-*/;

    static native void command(Element element, String command, String value) /*-{
        $wnd.jQuery(element).bootstrapSwitch(command, value);
    }-*/;

    static native void command(Element element, String command, boolean value) /*-{
        $wnd.jQuery(element).bootstrapSwitch(command, value);
    }-*/;

    static native String stringValue(Element element, String command) /*-{
        return String($wnd.jQuery(element).bootstrapSwitch(command));
    }-*/;

    static native boolean booleanValue(Element element, String command) /*-{
        return !!$wnd.jQuery(element).bootstrapSwitch(command);
    }-*/;

    static native void state(Element element, boolean value, boolean skip) /*-{
        $wnd.jQuery(element).bootstrapSwitch('state', value, skip);
    }-*/;

    static native boolean state(Element element) /*-{
        return $wnd.jQuery(element).bootstrapSwitch('state');
    }-*/;
}
