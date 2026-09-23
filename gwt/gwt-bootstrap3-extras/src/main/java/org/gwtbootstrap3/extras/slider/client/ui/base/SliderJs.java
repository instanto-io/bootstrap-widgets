package org.gwtbootstrap3.extras.slider.client.ui.base;

import jsinterop.annotations.JsFunction;
import jsinterop.base.Any;
import org.gwtbootstrap3.client.Bootstrap3;
import org.gwtbootstrap3.extras.slider.client.SliderResourcesResources;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;

/** Native boundary for scalar and range sliders. */
public final class SliderJs {
    private SliderJs() {}
    @JsFunction public interface Formatter { String format(String value); }
    public interface Events { void event(String name, Event event); }
    public static void whenReady(Runnable action) { Bootstrap3.initialise(() -> SliderResourcesResources.whenReady(SliderJs::ready, action)); }
    public static native boolean ready() /*-{ return typeof $wnd.Slider === "function"; }-*/;
    public static native JavaScriptObject options() /*-{ return {}; }-*/;
    public static native JsArrayNumber numbers() /*-{ return []; }-*/;
    public static native JsArrayString strings() /*-{ return []; }-*/;
    public static native JsArrayNumber parseNumbers(String json) /*-{ return JSON.parse(json); }-*/;
    public static native JsArrayString parseStrings(String json) /*-{ return JSON.parse(json); }-*/;
    public static native void init(Element e, JavaScriptObject options) /*-{ e.__gbmSlider = new $wnd.Slider(e, options); }-*/;
    public static native void command(Element e, String command) /*-{ e.__gbmSlider[command](); if(command === "destroy") delete e.__gbmSlider; }-*/;
    public static native boolean enabled(Element e) /*-{ return e.__gbmSlider.isEnabled(); }-*/;
    /** The slider's own element, as the user.client Element its callers cast it to. */
    public static native com.google.gwt.user.client.Element element(Element e) /*-{ return e.__gbmSlider.getElement(); }-*/;
    public static native String value(Element e) /*-{ return JSON.stringify(e.__gbmSlider.getValue()); }-*/;
    public static native void value(Element e, String json) /*-{ e.__gbmSlider.setValue(JSON.parse(json)); }-*/;
    public static native void attribute(Element e, String attr, String value) /*-{ e.__gbmSlider.setAttribute(attr, value); }-*/;
    public static native void attribute(Element e, String attr, boolean value) /*-{ e.__gbmSlider.setAttribute(attr, value); }-*/;
    public static native void attribute(Element e, String attr, double value) /*-{ e.__gbmSlider.setAttribute(attr, value); }-*/;
    public static native void attribute(Element e, String attr, JsArrayNumber value) /*-{ e.__gbmSlider.setAttribute(attr, value); }-*/;
    public static native void attribute(Element e, String attr, JsArrayString value) /*-{ e.__gbmSlider.setAttribute(attr, value); }-*/;
    public static native String stringAttribute(Element e, String attr) /*-{ return e.__gbmSlider.getAttribute(attr); }-*/;
    public static native boolean booleanAttribute(Element e, String attr) /*-{ return e.__gbmSlider.getAttribute(attr); }-*/;
    public static native double doubleAttribute(Element e, String attr) /*-{ return e.__gbmSlider.getAttribute(attr); }-*/;
    public static native JsArrayNumber numberArrayAttribute(Element e, String attr) /*-{ return e.__gbmSlider.getAttribute(attr); }-*/;
    public static native JsArrayString stringArrayAttribute(Element e, String attr) /*-{ return e.__gbmSlider.getAttribute(attr); }-*/;
    public static native String eventValue(Event event, boolean changed) /*-{ return JSON.stringify(changed ? event.value.newValue : event.value); }-*/;
    public static native void formatter(JavaScriptObject options, Formatter callback) /*-{ options.formatter = function(value) { return callback(JSON.stringify(value)); }; }-*/;
    public static native void formatter(Element e, Formatter callback) /*-{ e.__gbmSlider.setAttribute("formatter", function(value) { return callback(JSON.stringify(value)); }); }-*/;
    /** Delivers slider events to {@code callback}, as the GWT {@link Event} each one is. */
    public static void bind(Element e, Events callback) { bindNative(e, (name, event) -> callback.event(name, asEvent(event))); }
    /** The browser event, which GWT's Event is on both platforms. */
    private static native Event asEvent(Any event) /*-{ return event; }-*/;
    @JsFunction interface NativeEvents { void event(String name, Any event); }
    private static native void bindNative(Element e, NativeEvents callback) /*-{ $wnd.jQuery(e).on('slide.gbmSlider slideStart.gbmSlider slideStop.gbmSlider change.gbmSlider slideEnabled.gbmSlider slideDisabled.gbmSlider', function(event) { callback(event.type, event); }); }-*/;
    public static native void unbind(Element e) /*-{ $wnd.jQuery(e).off('.gbmSlider'); }-*/;
}
