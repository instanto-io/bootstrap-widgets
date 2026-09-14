package io.instanto.bootstrap5.extras.gallery.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import io.instanto.bootstrap5.client.ui.Anchor;
import io.instanto.bootstrap5.client.ui.Image;
import io.instanto.bootstrap5.extras.base.client.PluginWidget;

/** Thumbnail links with a zoomable, touch-friendly PhotoSwipe viewer. */
public class ImageGallery extends PluginWidget implements HasSelectionHandlers<Integer> {
    public ImageGallery() { setStyleName("d-flex flex-wrap gap-3"); }
    public void addImage(String source, String thumbnail, int width, int height, String alternativeText) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Image dimensions must be positive");
        checkUrl(source);
        checkUrl(thumbnail);
        Anchor link = new Anchor(source);
        link.getElement().setAttribute("data-pswp-width", String.valueOf(width));
        link.getElement().setAttribute("data-pswp-height", String.valueOf(height));
        Image image = new Image(thumbnail);
        image.getElement().setAttribute("alt", alternativeText == null ? "" : alternativeText);
        image.setWidth("160px");
        image.addStyleName("rounded");
        link.add(image);
        add(link);
    }
    private static void checkUrl(String url) {
        if (url == null || url.trim().isEmpty()) throw new IllegalArgumentException("Image URL is required");
        String lower = url.trim().toLowerCase(java.util.Locale.ROOT);
        if (lower.contains(":") && !lower.startsWith("https://") && !lower.startsWith("http://"))
            throw new IllegalArgumentException("Use a relative or HTTP(S) image URL");
    }
    public void open(int index) {
        if (index < 0 || index >= getWidgetCount()) throw new IndexOutOfBoundsException("Invalid image index");
        if (!isReady()) throw new IllegalStateException("Gallery is not attached");
        GalleryJs.open(plugin(), index);
    }
    public void close() { if (isReady()) GalleryJs.close(plugin()); }
    @Override public HandlerRegistration addSelectionHandler(SelectionHandler<Integer> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }
    @Override protected void whenReady(Runnable action) { GalleryJs.whenReady(action); }
    @Override protected JavaScriptObject createPlugin() {
        return GalleryJs.create(getElement(), index -> SelectionEvent.fire(this, Integer.valueOf(index)));
    }
    @Override protected void destroyPlugin(JavaScriptObject plugin) { GalleryJs.destroy(plugin); }
}
