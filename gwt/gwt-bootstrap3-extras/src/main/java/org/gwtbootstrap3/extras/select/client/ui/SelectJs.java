package org.gwtbootstrap3.extras.select.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;

/** Native boundary for the shared select widgets. */
final class SelectJs {
    static native com.google.gwt.core.client.JsArrayNumber numbers() /*-{ return []; }-*/;
    interface Events { void event(String name); }
    static void whenReady(Runnable action) {
        if (!ready()) new org.gwtbootstrap3.extras.select.client.SelectEntryPoint().onModuleLoad();
        captureDefaults();
        action.run();
    }
    static native boolean ready() /*-{ return !!($wnd.jQuery && $wnd.jQuery.fn.selectpicker); }-*/;
    static native void prepare() /*-{ $wnd.bootstrap = $wnd.bootstrap || {}; }-*/;
    static native void captureDefaults() /*-{ var p=$wnd.jQuery.fn.selectpicker; if(!p.__gbmDefaults) { p.__gbmDefaults=$wnd.jQuery.extend({},p.Constructor.DEFAULTS); p.__gbmLocaleDefaults=$wnd.jQuery.extend({},p.defaults); } }-*/;
    static native void restoreDefaults() /*-{ var p=$wnd.jQuery.fn.selectpicker; p.Constructor.DEFAULTS=$wnd.jQuery.extend({},p.__gbmDefaults); p.defaults=$wnd.jQuery.extend({},p.__gbmLocaleDefaults); }-*/;
    static native JavaScriptObject options(String icons, String tick) /*-{ return {iconBase:icons,tickIcon:tick}; }-*/;
    static native JsArrayString strings() /*-{ return []; }-*/;
    static native void initialize(Element e, JavaScriptObject options) /*-{ $wnd.jQuery(e).selectpicker(options); }-*/;
    static native void command(Element e, String command) /*-{ var jq=$wnd.jQuery(e);
        if(command==='refresh') { for(var i=0;i<e.attributes.length;i++) { var a=e.attributes[i]; if(a.name.indexOf('data-')===0) jq.removeData(a.name.slice(5)); } }
        jq.selectpicker(command); }-*/;
    static native String value(Element e) /*-{ return $wnd.jQuery(e).val(); }-*/;
    static native JsArrayString values(Element e) /*-{ return $wnd.jQuery(e).val() || []; }-*/;
    static native void value(Element e, String value) /*-{ e.__gbmSelectSilent=true; try { $wnd.jQuery(e).selectpicker('val',value); } finally { e.__gbmSelectSilent=false; } }-*/;
    static native void value(Element e, JsArrayString value) /*-{ e.__gbmSelectSilent=true; try { $wnd.jQuery(e).selectpicker('val',value); } finally { e.__gbmSelectSilent=false; } }-*/;
    static native void unbind(Element e) /*-{ $wnd.jQuery(e).off('.gbmSelect'); }-*/;
    static native void bind(Element e, Events callback) /*-{
        $wnd.jQuery(e).on('loaded.bs.select.gbmSelect changed.bs.select.gbmSelect show.bs.select.gbmSelect shown.bs.select.gbmSelect hide.bs.select.gbmSelect hidden.bs.select.gbmSelect rendered.bs.select.gbmSelect refreshed.bs.select.gbmSelect', function(event) { if(e.__gbmSelectSilent && event.type==='changed') return; callback.@org.gwtbootstrap3.extras.select.client.ui.SelectJs.Events::event(Ljava/lang/String;)(event.type); });
    }-*/;
    static native void countText(JavaScriptObject options, CountSelectedTextHandler handler) /*-{
        if (!handler) { delete options.countSelectedText; return; }
        options.countSelectedText = function(a,b) { return handler.@org.gwtbootstrap3.extras.select.client.ui.CountSelectedTextHandler::getCountSelectedText(II)(a,b); };
    }-*/;
    static native void maxText(JavaScriptObject options, MaxOptionsTextHandler handler) /*-{
        if (!handler) { delete options.maxOptionsText; return; }
        options.maxOptionsText = function(a,b) { return [handler.@org.gwtbootstrap3.extras.select.client.ui.MaxOptionsTextHandler::getMaxSelectOptionsText(I)(a),handler.@org.gwtbootstrap3.extras.select.client.ui.MaxOptionsTextHandler::getMaxGroupOptionsText(I)(b)]; };
    }-*/;
}
