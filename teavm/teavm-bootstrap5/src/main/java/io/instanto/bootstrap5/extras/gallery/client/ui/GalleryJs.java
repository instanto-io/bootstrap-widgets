package io.instanto.bootstrap5.extras.gallery.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;
import org.teavm.jso.*;
import io.instanto.bootstrap5.extras.gallery.client.GalleryResources;

final class GalleryJs {
    private GalleryJs() {}
    @JSFunctor private interface Callback extends JSObject { void call(String data); }
    static void whenReady(Runnable action) { GalleryResources.whenReady(GalleryJs::isReady, action); }
    @JSBody(script="return typeof window.PhotoSwipeLightbox !== 'undefined';")
    static native boolean isReady();
    static JavaScriptObject create(Element el, PluginCallback changed) { return JavaScriptObject.of(createNative(el.unwrap(), (Callback) changed::onEvent)); }
    @JSBody(params={"el","changed"}, script="var p=new window.PhotoSwipeLightbox({gallery:el,children:'a',pswpModule:window.PhotoSwipe});p.on('change',function(){changed(String(p.pswp.currIndex));});p.on(\"openingAnimationEnd\",function(){if(p.__closeRequested&&p.pswp)p.pswp.close();});p.init();return p;")
    private static native JSObject createNative(JSObject el, Callback changed);
    static void open(JavaScriptObject p, int index) { openNative(p.unwrap(), index); }
    @JSBody(params={"p","index"}, script="p.__closeRequested=false;p.loadAndOpen(index);")
    private static native void openNative(JSObject p, int index);
    static void close(JavaScriptObject p) { closeNative(p.unwrap()); }
    @JSBody(params={"p"}, script="p.__closeRequested=true;if(p.pswp)p.pswp.close();")
    private static native void closeNative(JSObject p);
    static void destroy(JavaScriptObject p) { destroyNative(p.unwrap()); }
    @JSBody(params={"p"}, script="p.destroy();")
    private static native void destroyNative(JSObject p);
}
