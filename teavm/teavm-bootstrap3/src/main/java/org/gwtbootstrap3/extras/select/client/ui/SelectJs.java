package org.gwtbootstrap3.extras.select.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/** Native boundary for the shared select widgets. */
final class SelectJs {
    static com.google.gwt.core.client.JsArrayNumber numbers() {
        return com.google.gwt.core.client.JsArrayNumber.of(stringsNative1());
    }
    interface Events { void event(String name); }
    @JSFunctor private interface EventCallback extends JSObject { void event(String name); }
    @JSFunctor private interface CountCallback extends JSObject { String text(int selected, int total); }
    @JSFunctor private interface MaxCallback extends JSObject { String text(int count); }
    static void whenReady(Runnable action) {
        org.gwtbootstrap3.client.Bootstrap3.initialise(() -> {
            org.gwtbootstrap3.extras.select.client.SelectResourcesResources.ensureInjected();
            if (!ready()) {
                prepare();
                com.google.gwt.core.client.ScriptInjector.fromString(
                    org.gwtbootstrap3.extras.select.client.SelectClientBundle.INSTANCE.select().getText())
                    .setWindow(com.google.gwt.core.client.ScriptInjector.TOP_WINDOW).inject();
            }
            captureDefaults();
            action.run();
        });
    }
    @JSBody(params = {}, script = "return !!(window.jQuery && window.jQuery.fn.selectpicker);")
    static native boolean ready();
    @JSBody(params = {}, script = "window.bootstrap = window.bootstrap || {};")
    static native void prepare();
    @JSBody(params = {}, script = "var p=window.jQuery.fn.selectpicker; if(!p.__gbmDefaults) { p.__gbmDefaults=window.jQuery.extend({},p.Constructor.DEFAULTS); p.__gbmLocaleDefaults=window.jQuery.extend({},p.defaults); }")
    static native void captureDefaults();
    @JSBody(params = {}, script = "var p=window.jQuery.fn.selectpicker; p.Constructor.DEFAULTS=window.jQuery.extend({},p.__gbmDefaults); p.defaults=window.jQuery.extend({},p.__gbmLocaleDefaults);")
    static native void restoreDefaults();
    static JavaScriptObject options(String icons, String tick) { return JavaScriptObject.of(optionsNative0(icons, tick)); }
    @JSBody(params = {"icons", "tick"}, script = "return {iconBase:icons,tickIcon:tick};")
    private static native JSObject optionsNative0(String icons, String tick);
    static JsArrayString strings() { return JsArrayString.of(stringsNative1()); }
    @JSBody(params = {}, script = "return [];")
    private static native JSObject stringsNative1();
    static void initialize(Element e, JavaScriptObject options) { initializeNative2(e.unwrap(), options.unwrap()); }
    @JSBody(params = {"e", "options"}, script = "window.jQuery(e).selectpicker(options);")
    private static native void initializeNative2(JSObject e, JSObject options);
    static void command(Element e, String command) { commandNative3(e.unwrap(), command); }
    @JSBody(params = {"e", "command"}, script = "var jq=window.jQuery(e);\n        if(command==='refresh') { for(var i=0;i<e.attributes.length;i++) { var a=e.attributes[i]; if(a.name.indexOf('data-')===0) jq.removeData(a.name.slice(5)); } }\n        jq.selectpicker(command);")
    private static native void commandNative3(JSObject e, String command);
    static String value(Element e) { return valueNative4(e.unwrap()); }
    @JSBody(params = {"e"}, script = "return window.jQuery(e).val();")
    private static native String valueNative4(JSObject e);
    static JsArrayString values(Element e) { return JsArrayString.of(valuesNative5(e.unwrap())); }
    @JSBody(params = {"e"}, script = "return window.jQuery(e).val() || [];")
    private static native JSObject valuesNative5(JSObject e);
    static void value(Element e, String value) { valueNative6(e.unwrap(), value); }
    @JSBody(params = {"e", "value"}, script = "e.__gbmSelectSilent=true; try { window.jQuery(e).selectpicker('val',value); } finally { e.__gbmSelectSilent=false; }")
    private static native void valueNative6(JSObject e, String value);
    static void value(Element e, JsArrayString value) { valueNative7(e.unwrap(), value.unwrap()); }
    @JSBody(params = {"e", "value"}, script = "e.__gbmSelectSilent=true; try { window.jQuery(e).selectpicker('val',value); } finally { e.__gbmSelectSilent=false; }")
    private static native void valueNative7(JSObject e, JSObject value);
    static void unbind(Element e) { unbindNative8(e.unwrap()); }
    @JSBody(params = {"e"}, script = "window.jQuery(e).off('.gbmSelect');")
    private static native void unbindNative8(JSObject e);
    static void bind(Element e, Events callback) { bindNative(e.unwrap(), callback::event); }
    @JSBody(params={"e","callback"}, script="window.jQuery(e).on('loaded.bs.select.gbmSelect changed.bs.select.gbmSelect show.bs.select.gbmSelect shown.bs.select.gbmSelect hide.bs.select.gbmSelect hidden.bs.select.gbmSelect rendered.bs.select.gbmSelect refreshed.bs.select.gbmSelect', function(event) { if(e.__gbmSelectSilent && event.type==='changed') return; callback(event.type); });")
    private static native void bindNative(JSObject e, EventCallback callback);
    static void countText(JavaScriptObject options, CountSelectedTextHandler handler) {
        countNative(options.unwrap(), handler == null ? null : handler::getCountSelectedText);
    }
    @JSBody(params={"options","callback"}, script="if(callback) options.countSelectedText=callback; else delete options.countSelectedText;")
    private static native void countNative(JSObject options, CountCallback callback);
    static void maxText(JavaScriptObject options, MaxOptionsTextHandler handler) {
        maxNative(options.unwrap(), handler == null ? null : handler::getMaxSelectOptionsText, handler == null ? null : handler::getMaxGroupOptionsText);
    }
    @JSBody(params={"options","all","group"}, script="if(all) options.maxOptionsText=function(a,b){ return [all(a),group(b)]; }; else delete options.maxOptionsText;")
    private static native void maxNative(JSObject options, MaxCallback all, MaxCallback group);
}
