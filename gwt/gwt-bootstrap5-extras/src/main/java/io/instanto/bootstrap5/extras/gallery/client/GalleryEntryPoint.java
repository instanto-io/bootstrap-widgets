package io.instanto.bootstrap5.extras.gallery.client;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.ScriptInjector;
/** Loads bundled scripts once before application entry points run. */
public final class GalleryEntryPoint implements EntryPoint {
    @Override public void onModuleLoad() {
        if (isLoaded()) return;
        ScriptInjector.fromString(GalleryClientBundle.INSTANCE.script0().getText())
                .setWindow(ScriptInjector.TOP_WINDOW).inject();
        ScriptInjector.fromString(GalleryClientBundle.INSTANCE.script1().getText())
                .setWindow(ScriptInjector.TOP_WINDOW).inject();
    }
    private static native boolean isLoaded() /*-{ return typeof $wnd.PhotoSwipeLightbox !== 'undefined'; }-*/;
}
