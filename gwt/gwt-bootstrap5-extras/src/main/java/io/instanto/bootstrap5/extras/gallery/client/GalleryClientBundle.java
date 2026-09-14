package io.instanto.bootstrap5.extras.gallery.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
/** Pinned vendor scripts for the Gallery module. */
public interface GalleryClientBundle extends ClientBundle {
    GalleryClientBundle INSTANCE = GWT.create(GalleryClientBundle.class);
    @Source("resource/js/photoswipe-5.4.4.min.js") TextResource script0();
    @Source("resource/js/photoswipe-lightbox-5.4.4.min.js") TextResource script1();
}
