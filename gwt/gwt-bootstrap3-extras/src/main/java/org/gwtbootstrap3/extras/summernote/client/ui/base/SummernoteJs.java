package org.gwtbootstrap3.extras.summernote.client.ui.base;
import com.google.gwt.core.client.*;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteImageUploadEvent.ImageFile;
final class SummernoteJs {
    interface Events { void event(String name); void key(boolean down, NativeEvent event); void images(JsArray<ImageFile> files); }
    static void whenReady(Runnable action) {
        if (!ready()) new org.gwtbootstrap3.extras.summernote.client.SummernoteEntryPoint().onModuleLoad();
        action.run();
    }
    static native boolean ready() /*-{ return !!($wnd.jQuery && $wnd.jQuery.fn.summernote); }-*/;
    static native JavaScriptObject options() /*-{ return {}; }-*/;
    static native JsArrayString strings() /*-{ return []; }-*/;
    static native JsArrayMixed mixed() /*-{ return []; }-*/;
    static native JsArrayMixed group(String name, JsArrayString buttons) /*-{ return [name,buttons]; }-*/;
    static native void toolbarVisible(JavaScriptObject o, boolean visible) /*-{ if(visible) delete o.toolbar; else o.toolbar=false; }-*/;
    static native void command(Element e, String command) /*-{ $wnd.jQuery(e).summernote(command); }-*/;
    static native void code(Element e, String code) /*-{ $wnd.jQuery(e).summernote('code',code); }-*/;
    static native String code(Element e) /*-{ return $wnd.jQuery(e).summernote('code'); }-*/;
    static native boolean empty(Element e) /*-{ return $wnd.jQuery(e).summernote('isEmpty'); }-*/;
    static native void images(Element e, JsArray<ImageFile> files) /*-{ $wnd.jQuery(e).summernote('insertImages',files); }-*/;
    static native void property(JavaScriptObject o, String key, String value) /*-{ o[key]=value; }-*/;
    static native void property(JavaScriptObject o, String key, boolean value) /*-{ o[key]=value; }-*/;
    static native void property(JavaScriptObject o, String key, int value) /*-{ o[key]=value; }-*/;
    static native void property(JavaScriptObject o, String key, JsArrayString value) /*-{ o[key]=value; }-*/;
    static native void property(JavaScriptObject o, String key, JsArrayMixed value) /*-{ o[key]=value; }-*/;
    static native void initialize(Element e, JavaScriptObject options, Events callback, boolean upload) /*-{
        var notify=function(name){ callback.@org.gwtbootstrap3.extras.summernote.client.ui.base.SummernoteJs.Events::event(Ljava/lang/String;)(name); };
        var key=function(down,event){ callback.@org.gwtbootstrap3.extras.summernote.client.ui.base.SummernoteJs.Events::key(ZLcom/google/gwt/dom/client/NativeEvent;)(down,event); };
        var images=function(files){ callback.@org.gwtbootstrap3.extras.summernote.client.ui.base.SummernoteJs.Events::images(Lcom/google/gwt/core/client/JsArray;)(files); };
        var o=$wnd.jQuery.extend({},options); o.callbacks={};
        ['init','enter','focus','blur','paste','change'].forEach(function(name) { o.callbacks['on'+name.charAt(0).toUpperCase()+name.slice(1)]=function(){ notify(name); }; });
        o.callbacks.onKeydown=function(e){ key(true,e.originalEvent || e); };
        o.callbacks.onKeyup=function(e){ key(false,e.originalEvent || e); };
        if(upload) o.callbacks.onImageUpload=function(files){ images(files); };
        $wnd.jQuery(e).summernote(o);
    }-*/;
    static native void hint(JavaScriptObject o,String match,HintHandler handler) /*-{
        o.hint={match:new RegExp(match),search:function(word,callback){callback(handler.@org.gwtbootstrap3.extras.summernote.client.ui.base.HintHandler::onSearch(Ljava/lang/String;)(word));},template:function(word){return handler.@org.gwtbootstrap3.extras.summernote.client.ui.base.HintHandler::getTemplate(Ljava/lang/String;)(word);},content:function(word){return handler.@org.gwtbootstrap3.extras.summernote.client.ui.base.HintHandler::getContent(Ljava/lang/String;)(word);}};
    }-*/;
}
