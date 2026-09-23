package org.gwtbootstrap3.extras.select.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsFunction;
import org.gwtbootstrap3.client.Bootstrap3;
import org.gwtbootstrap3.extras.select.client.SelectClientBundle;
import org.gwtbootstrap3.extras.select.client.SelectResourcesResources;

/** Native boundary for the shared select widgets. */
final class SelectJs {
    static native com.google.gwt.core.client.JsArrayNumber numbers() /*-{ return []; }-*/;
    @JsFunction interface Events { void event(String name); }
    @JsFunction interface CountText { String text(int selected, int total); }
    @JsFunction interface LimitText { String text(int limit); }
    static void whenReady(Runnable action) {
        Bootstrap3.initialise(() -> {
            SelectResourcesResources.ensureInjected();
            if (!ready()) {
                // The plugin needs the bootstrap global and triggerNative while it is being
                // defined, so prepare before injecting it and again for a page that had it.
                prepare();
                ScriptInjector.fromString(SelectClientBundle.INSTANCE.select().getText())
                        .setWindow(ScriptInjector.TOP_WINDOW).inject();
            }
            prepare();
            captureDefaults();
            action.run();
        });
    }
    static native boolean ready() /*-{ return !!($wnd.jQuery && $wnd.jQuery.fn.selectpicker); }-*/;
    static native void prepare() /*-{
        $wnd.bootstrap = $wnd.bootstrap || {};
        if ($wnd.jQuery && !$wnd.jQuery.fn.triggerNative) {
            $wnd.jQuery.fn.triggerNative = function(e) {
                var t, i = this[0];
                if (i && i.dispatchEvent) {
                    try { t = new Event(e, { bubbles: true }); }
                    catch(err) { t = document.createEvent('Event'); t.initEvent(e, true, false); }
                    i.dispatchEvent(t);
                }
            };
        }
    }-*/;
    static native void captureDefaults() /*-{ var p=$wnd.jQuery.fn.selectpicker; if(!p.__gbmDefaults) { p.__gbmDefaults=$wnd.jQuery.extend({},p.Constructor.DEFAULTS); p.__gbmLocaleDefaults=$wnd.jQuery.extend({},p.defaults); } }-*/;
    static native void restoreDefaults() /*-{ var p=$wnd.jQuery.fn.selectpicker; p.Constructor.DEFAULTS=$wnd.jQuery.extend({},p.__gbmDefaults); p.defaults=$wnd.jQuery.extend({},p.__gbmLocaleDefaults); }-*/;
    static native JavaScriptObject options(String icons, String tick) /*-{ return {iconBase:icons,tickIcon:tick}; }-*/;
    static native JsArrayString strings() /*-{ return []; }-*/;
    static native void initialize(Element e, JavaScriptObject options) /*-{ $wnd.jQuery(e).selectpicker(options); }-*/;
    static native void command(Element e, String command) /*-{
        var jq = $wnd.jQuery(e);
        if (command === 'refresh') {
            for (var i = 0; i < e.attributes.length; i++) {
                var a = e.attributes[i];
                if (a.name.indexOf('data-') === 0) jq.removeData(a.name.slice(5));
            }
            var sp = jq.data('selectpicker');
            if (sp && sp.selectpicker && sp.selectpicker.main) {
                sp.selectpicker.main.data = [];
                sp.selectpicker.main.elements = [];
                sp.selectpicker.main.optionQueue = document.createDocumentFragment();
                sp.selectpicker.view = {};
                sp.selectpicker.optionValuesDataMap = {};
            }
        }
        jq.selectpicker(command);
    }-*/;
    static native String value(Element e) /*-{ return $wnd.jQuery(e).val(); }-*/;
    static native JsArrayString values(Element e) /*-{ return $wnd.jQuery(e).val() || []; }-*/;
    static native void value(Element e, String value) /*-{ e.__gbmSelectSilent=true; try { $wnd.jQuery(e).selectpicker('val',value); } finally { e.__gbmSelectSilent=false; } }-*/;
    static native void value(Element e, JsArrayString value) /*-{ e.__gbmSelectSilent=true; try { $wnd.jQuery(e).selectpicker('val',value); } finally { e.__gbmSelectSilent=false; } }-*/;
    static native void unbind(Element e) /*-{ $wnd.jQuery(e).off('.gbmSelect'); }-*/;
    static native void bind(Element e, Events callback) /*-{
        $wnd.jQuery(e).on('loaded.bs.select.gbmSelect changed.bs.select.gbmSelect show.bs.select.gbmSelect shown.bs.select.gbmSelect hide.bs.select.gbmSelect hidden.bs.select.gbmSelect rendered.bs.select.gbmSelect refreshed.bs.select.gbmSelect', function(event) { if(e.__gbmSelectSilent && event.type==='changed') return; callback(event.type); });
    }-*/;
    static void countText(JavaScriptObject options, CountSelectedTextHandler handler) {
        countTextNative(options, handler == null ? null : handler::getCountSelectedText);
    }
    private static native void countTextNative(JavaScriptObject options, CountText text) /*-{
        if (!text) { delete options.countSelectedText; return; }
        options.countSelectedText = function(a,b) { return text(a,b); };
    }-*/;
    static void maxText(JavaScriptObject options, MaxOptionsTextHandler handler) {
        if (handler == null) maxTextNative(options, null, null);
        else maxTextNative(options, handler::getMaxSelectOptionsText, handler::getMaxGroupOptionsText);
    }
    private static native void maxTextNative(JavaScriptObject options, LimitText select, LimitText group) /*-{
        if (!select) { delete options.maxOptionsText; return; }
        options.maxOptionsText = function(a,b) { return [select(a), group(b)]; };
    }-*/;
}
