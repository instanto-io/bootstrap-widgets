package org.gwtbootstrap3.extras.slider.client.ui.base;

import com.google.gwt.core.client.*;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import org.teavm.jso.*;
import org.teavm.jso.dom.html.HTMLElement;

/** TeaVM implementation of the slider native boundary. */
public final class SliderJs {
    private SliderJs() {}
    public interface Formatter { String format(String value); }
    public interface Events { void event(String name, Event event); }
    @JSFunctor interface JsFormatter extends JSObject { String format(String value); }
    @JSFunctor interface JsEvents extends JSObject { void event(String name, org.teavm.jso.dom.events.Event event); }
    public static void whenReady(Runnable action) { org.gwtbootstrap3.client.Bootstrap3.initialise(() -> org.gwtbootstrap3.extras.slider.client.SliderResourcesResources.whenReady(SliderJs::ready, action)); }
    public static boolean ready() { return readyNative0(); }
    @JSBody(params = {}, script = "return typeof window.Slider === \"function\";")
    private static native boolean readyNative0();
    public static JavaScriptObject options() { return JavaScriptObject.of(optionsNative1()); }
    @JSBody(params = {}, script = "return {};")
    private static native JSObject optionsNative1();
    public static JsArrayNumber numbers() { return JsArrayNumber.of(numbersNative2()); }
    @JSBody(params = {}, script = "return [];")
    private static native JSObject numbersNative2();
    public static JsArrayString strings() { return JsArrayString.of(stringsNative3()); }
    @JSBody(params = {}, script = "return [];")
    private static native JSObject stringsNative3();
    public static JsArrayNumber parseNumbers(String json) { return JsArrayNumber.of(parseNumbersNative4(json)); }
    @JSBody(params = {"json"}, script = "return JSON.parse(json);")
    private static native JSObject parseNumbersNative4(String json);
    public static JsArrayString parseStrings(String json) { return JsArrayString.of(parseStringsNative5(json)); }
    @JSBody(params = {"json"}, script = "return JSON.parse(json);")
    private static native JSObject parseStringsNative5(String json);
    public static void init(Element e, JavaScriptObject options) { initNative6(e.unwrap(), options.unwrap()); }
    @JSBody(params = {"e", "options"}, script = "e.__gbmSlider = new window.Slider(e, options);")
    private static native void initNative6(JSObject e, JSObject options);
    public static void command(Element e, String command) { commandNative7(e.unwrap(), command); }
    @JSBody(params = {"e", "command"}, script = "e.__gbmSlider[command](); if(command === \"destroy\") delete e.__gbmSlider;")
    private static native void commandNative7(JSObject e, String command);
    public static boolean enabled(Element e) { return enabledNative8(e.unwrap()); }
    @JSBody(params = {"e"}, script = "return e.__gbmSlider.isEnabled();")
    private static native boolean enabledNative8(JSObject e);
    public static Element element(Element e) { return new com.google.gwt.user.client.Element((HTMLElement) elementNative9(e.unwrap())); }
    @JSBody(params = {"e"}, script = "return e.__gbmSlider.getElement();")
    private static native JSObject elementNative9(JSObject e);
    public static String value(Element e) { return valueNative10(e.unwrap()); }
    @JSBody(params = {"e"}, script = "return JSON.stringify(e.__gbmSlider.getValue());")
    private static native String valueNative10(JSObject e);
    public static void value(Element e, String json) { valueNative11(e.unwrap(), json); }
    @JSBody(params = {"e", "json"}, script = "e.__gbmSlider.setValue(JSON.parse(json));")
    private static native void valueNative11(JSObject e, String json);
    public static void attribute(Element e, String attr, String value) { attributeNative12(e.unwrap(), attr, value); }
    @JSBody(params = {"e", "attr", "value"}, script = "e.__gbmSlider.setAttribute(attr, value);")
    private static native void attributeNative12(JSObject e, String attr, String value);
    public static void attribute(Element e, String attr, boolean value) { attributeNative13(e.unwrap(), attr, value); }
    @JSBody(params = {"e", "attr", "value"}, script = "e.__gbmSlider.setAttribute(attr, value);")
    private static native void attributeNative13(JSObject e, String attr, boolean value);
    public static void attribute(Element e, String attr, double value) { attributeNative14(e.unwrap(), attr, value); }
    @JSBody(params = {"e", "attr", "value"}, script = "e.__gbmSlider.setAttribute(attr, value);")
    private static native void attributeNative14(JSObject e, String attr, double value);
    public static void attribute(Element e, String attr, JsArrayNumber value) { attributeNative15(e.unwrap(), attr, value.unwrap()); }
    @JSBody(params = {"e", "attr", "value"}, script = "e.__gbmSlider.setAttribute(attr, value);")
    private static native void attributeNative15(JSObject e, String attr, JSObject value);
    public static void attribute(Element e, String attr, JsArrayString value) { attributeNative16(e.unwrap(), attr, value.unwrap()); }
    @JSBody(params = {"e", "attr", "value"}, script = "e.__gbmSlider.setAttribute(attr, value);")
    private static native void attributeNative16(JSObject e, String attr, JSObject value);
    public static String stringAttribute(Element e, String attr) { return stringAttributeNative17(e.unwrap(), attr); }
    @JSBody(params = {"e", "attr"}, script = "return e.__gbmSlider.getAttribute(attr);")
    private static native String stringAttributeNative17(JSObject e, String attr);
    public static boolean booleanAttribute(Element e, String attr) { return booleanAttributeNative18(e.unwrap(), attr); }
    @JSBody(params = {"e", "attr"}, script = "return e.__gbmSlider.getAttribute(attr);")
    private static native boolean booleanAttributeNative18(JSObject e, String attr);
    public static double doubleAttribute(Element e, String attr) { return doubleAttributeNative19(e.unwrap(), attr); }
    @JSBody(params = {"e", "attr"}, script = "return e.__gbmSlider.getAttribute(attr);")
    private static native double doubleAttributeNative19(JSObject e, String attr);
    public static JsArrayNumber numberArrayAttribute(Element e, String attr) { return JsArrayNumber.of(numberArrayAttributeNative20(e.unwrap(), attr)); }
    @JSBody(params = {"e", "attr"}, script = "return e.__gbmSlider.getAttribute(attr);")
    private static native JSObject numberArrayAttributeNative20(JSObject e, String attr);
    public static JsArrayString stringArrayAttribute(Element e, String attr) { return JsArrayString.of(stringArrayAttributeNative21(e.unwrap(), attr)); }
    @JSBody(params = {"e", "attr"}, script = "return e.__gbmSlider.getAttribute(attr);")
    private static native JSObject stringArrayAttributeNative21(JSObject e, String attr);
    public static String eventValue(Event event, boolean changed) { return eventValueNative22(event.unwrap(), changed); }
    @JSBody(params = {"event", "changed"}, script = "return JSON.stringify(changed ? event.value.newValue : event.value);")
    private static native String eventValueNative22(JSObject event, boolean changed);
    public static void formatter(JavaScriptObject options, Formatter callback) { formatterNative23(options.unwrap(), (JsFormatter) callback::format); }
    @JSBody(params = {"options", "callback"}, script = "options.formatter = function(value) { return callback(JSON.stringify(value)); };")
    private static native void formatterNative23(JSObject options, JsFormatter callback);
    public static void formatter(Element e, Formatter callback) { formatterNative24(e.unwrap(), (JsFormatter) callback::format); }
    @JSBody(params = {"e", "callback"}, script = "e.__gbmSlider.setAttribute(\"formatter\", function(value) { return callback(JSON.stringify(value)); });")
    private static native void formatterNative24(JSObject e, JsFormatter callback);
    public static void bind(Element e, Events callback) { bindNative25(e.unwrap(), (JsEvents) (name, event) -> callback.event(name, new Event(event))); }
    @JSBody(params = {"e", "callback"}, script = "window.jQuery(e).on('slide.gbmSlider slideStart.gbmSlider slideStop.gbmSlider change.gbmSlider slideEnabled.gbmSlider slideDisabled.gbmSlider', function(event) { callback(event.type, event); });")
    private static native void bindNative25(JSObject e, JsEvents callback);
    public static void unbind(Element e) { unbindNative26(e.unwrap()); }
    @JSBody(params = {"e"}, script = "window.jQuery(e).off('.gbmSlider');")
    private static native void unbindNative26(JSObject e);
}
