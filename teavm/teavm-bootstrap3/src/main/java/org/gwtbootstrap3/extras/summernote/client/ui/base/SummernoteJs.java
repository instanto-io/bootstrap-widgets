package org.gwtbootstrap3.extras.summernote.client.ui.base;
import com.google.gwt.core.client.*;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import org.gwtbootstrap3.extras.summernote.client.event.SummernoteImageUploadEvent.ImageFile;
import org.teavm.jso.*;
final class SummernoteJs {
    interface Events { void event(String name); void key(boolean down, NativeEvent event); void images(JsArray<ImageFile> files); }
    @JSFunctor interface Notify extends JSObject { void event(String name); }
    @JSFunctor interface Key extends JSObject { void key(boolean down, org.teavm.jso.dom.events.Event event); }
    @JSFunctor interface Images extends JSObject { void images(JSObject files); }
    @JSFunctor interface Search extends JSObject { JSObject search(String word); }
    @JSFunctor interface Content extends JSObject { JSObject content(String word); }
    @JSFunctor interface Text extends JSObject { String text(String word); }
    static void whenReady(Runnable action) {
        org.gwtbootstrap3.client.Bootstrap3.initialise(() -> {
            org.gwtbootstrap3.extras.summernote.client.SummernoteResourcesResources.ensureInjected();
            if (!ready()) ScriptInjector.fromString(org.gwtbootstrap3.extras.summernote.client.SummernoteClientBundle.INSTANCE.summernote().getText()).setWindow(ScriptInjector.TOP_WINDOW).inject();
            action.run();
        });
    }
    @JSBody(params={}, script="return !!(window.jQuery && window.jQuery.fn.summernote);")
    static native boolean ready();
    static JavaScriptObject options() { return JavaScriptObject.of(optionsNative0()); }
    @JSBody(params={}, script="return {};")
    private static native JSObject optionsNative0();
    static JsArrayString strings() { return JsArrayString.of(stringsNative1()); }
    @JSBody(params={}, script="return [];")
    private static native JSObject stringsNative1();
    static JsArrayMixed mixed() { return JsArrayMixed.of(mixedNative2()); }
    @JSBody(params={}, script="return [];")
    private static native JSObject mixedNative2();
    static JsArrayMixed group(String name, JsArrayString buttons) { return JsArrayMixed.of(groupNative3(name, buttons.unwrap())); }
    @JSBody(params={"name","buttons"}, script="return [name,buttons];")
    private static native JSObject groupNative3(String name, JSObject buttons);
    static void toolbarVisible(JavaScriptObject o, boolean visible) { toolbarVisibleNative4(o.unwrap(), visible); }
    @JSBody(params={"o","visible"}, script="if(visible) delete o.toolbar; else o.toolbar=false;")
    private static native void toolbarVisibleNative4(JSObject o, boolean visible);
    static void command(Element e, String command) { commandNative5(e.unwrap(), command); }
    @JSBody(params={"e","command"}, script="window.jQuery(e).summernote(command);")
    private static native void commandNative5(JSObject e, String command);
    static void code(Element e, String code) { codeNative6(e.unwrap(), code); }
    @JSBody(params={"e","code"}, script="window.jQuery(e).summernote('code',code);")
    private static native void codeNative6(JSObject e, String code);
    static String code(Element e) { return codeNative7(e.unwrap()); }
    @JSBody(params={"e"}, script="return window.jQuery(e).summernote('code');")
    private static native String codeNative7(JSObject e);
    static boolean empty(Element e) { return emptyNative8(e.unwrap()); }
    @JSBody(params={"e"}, script="return window.jQuery(e).summernote('isEmpty');")
    private static native boolean emptyNative8(JSObject e);
    static void images(Element e, JsArray<ImageFile> files) { imagesNative9(e.unwrap(), files.unwrap()); }
    @JSBody(params={"e","files"}, script="window.jQuery(e).summernote('insertImages',files);")
    private static native void imagesNative9(JSObject e, JSObject files);
    static void property(JavaScriptObject o, String key, String value) { propertyNative10(o.unwrap(), key, value); }
    @JSBody(params={"o","key","value"}, script="o[key]=value;")
    private static native void propertyNative10(JSObject o, String key, String value);
    static void property(JavaScriptObject o, String key, boolean value) { propertyNative11(o.unwrap(), key, value); }
    @JSBody(params={"o","key","value"}, script="o[key]=value;")
    private static native void propertyNative11(JSObject o, String key, boolean value);
    static void property(JavaScriptObject o, String key, int value) { propertyNative12(o.unwrap(), key, value); }
    @JSBody(params={"o","key","value"}, script="o[key]=value;")
    private static native void propertyNative12(JSObject o, String key, int value);
    static void property(JavaScriptObject o, String key, JsArrayString value) { propertyNative13(o.unwrap(), key, value.unwrap()); }
    @JSBody(params={"o","key","value"}, script="o[key]=value;")
    private static native void propertyNative13(JSObject o, String key, JSObject value);
    static void property(JavaScriptObject o, String key, JsArrayMixed value) { propertyNative14(o.unwrap(), key, value.unwrap()); }
    @JSBody(params={"o","key","value"}, script="o[key]=value;")
    private static native void propertyNative14(JSObject o, String key, JSObject value);
    static void initialize(Element e, JavaScriptObject options, Events callback, boolean upload) {
        initializeNative(e.unwrap(),options.unwrap(),callback::event,(down,event)->callback.key(down,new NativeEvent(event)),files->callback.images(new JsArray<>(files,ImageFile::new)),upload);
    }
    @JSBody(params={"e","options","notify","key","images","upload"},script="var o=window.jQuery.extend({},options); o.callbacks={};\n        ['init','enter','focus','blur','paste','change'].forEach(function(name) { o.callbacks['on'+name.charAt(0).toUpperCase()+name.slice(1)]=function(){ notify(name); }; });\n        o.callbacks.onKeydown=function(e){ key(true,e.originalEvent || e); };\n        o.callbacks.onKeyup=function(e){ key(false,e.originalEvent || e); };\n        if(upload) o.callbacks.onImageUpload=function(files){ images(files); };\n        window.jQuery(e).summernote(o);")
    private static native void initializeNative(JSObject e,JSObject options,Notify notify,Key key,Images images,boolean upload);
    static void hint(JavaScriptObject options,String match,HintHandler handler) {
        hintNative(options.unwrap(),match,word->{ JsArrayString result=strings(); for(String item:handler.onSearch(word)) result.push(item); return result.unwrap(); },handler::getTemplate,word->handler.getContent(word).unwrap());
    }
    @JSBody(params={"o","match","search","template","content"},script="o.hint={match:new RegExp(match),search:function(word,callback){callback(search(word));},template:template,content:content};")
    private static native void hintNative(JSObject o,String match,Search search,Text template,Content content);
}
