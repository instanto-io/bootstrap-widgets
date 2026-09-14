package io.instanto.bootstrap5.extras.gallery.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;

final class GalleryJs {
    private GalleryJs() {}
    static void whenReady(Runnable action) {
        if (!isReady()) throw new IllegalStateException("Inherit the Gallery module to load PhotoSwipeLightbox");
        action.run();
    }
    static native boolean isReady() /*-{ return typeof $wnd.PhotoSwipeLightbox !== 'undefined'; }-*/;
    static native JavaScriptObject create(Element el, PluginCallback changed) /*-{
        var p=new $wnd.PhotoSwipeLightbox({gallery:el,children:'a',pswpModule:$wnd.PhotoSwipe});p.on('change',function(){changed.@io.instanto.bootstrap5.extras.base.client.PluginCallback::onEvent(Ljava/lang/String;)(String(p.pswp.currIndex));});p.on("openingAnimationEnd",function(){if(p.__closeRequested&&p.pswp)p.pswp.close();});p.init();return p;
    }-*/;
    static native void open(JavaScriptObject p, int index) /*-{
        p.__closeRequested=false;p.loadAndOpen(index);
    }-*/;
    static native void close(JavaScriptObject p) /*-{
        p.__closeRequested=true;if(p.pswp)p.pswp.close();
    }-*/;
    static native void destroy(JavaScriptObject p) /*-{
        p.destroy();
    }-*/;
}
