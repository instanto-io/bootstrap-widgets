package io.instanto.bootstrap5.extras.gallery.client.ui;

import com.google.gwt.dom.client.Element;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;
import io.instanto.bootstrap5.extras.gallery.client.GalleryResources;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/** PhotoSwipe's lightbox, reached through JsInterop so GWT and TeaVM compile this one source. */
final class GalleryJs {
  private GalleryJs() {}

  /** A lightbox, and whether a close was asked for while it was still opening. */
  static final class Handle {
    private final Lightbox lightbox;
    private boolean closeRequested;

    private Handle(Lightbox lightbox) {
      this.lightbox = lightbox;
    }
  }

  static void whenReady(Runnable action) {
    GalleryResources.whenReady(GalleryJs::isReady, action);
  }

  static boolean isReady() {
    return Js.global().get("PhotoSwipeLightbox") != null;
  }

  static Handle create(Element el, PluginCallback changed) {
    JsPropertyMap<Object> opts = JsPropertyMap.of();
    opts.set("gallery", Js.asAny(el));
    opts.set("children", "a");
    opts.set("pswpModule", Js.global().get("PhotoSwipe"));
    Handle p = new Handle(new Lightbox(opts));
    p.lightbox.on(
        "change", () -> changed.onEvent(String.valueOf(p.lightbox.getPswp().getCurrIndex())));
    p.lightbox.on(
        "openingAnimationEnd",
        () -> {
          if (p.closeRequested) closeOpen(p);
        });
    p.lightbox.init();
    return p;
  }

  static void open(Handle p, int index) {
    p.closeRequested = false;
    p.lightbox.loadAndOpen(index);
  }

  static void close(Handle p) {
    p.closeRequested = true;
    closeOpen(p);
  }

  static void destroy(Handle p) {
    p.lightbox.destroy();
  }

  private static void closeOpen(Handle p) {
    Viewer viewer = p.lightbox.getPswp();
    if (Js.isTruthy(viewer)) viewer.close();
  }

  @JsFunction
  interface Listener {
    void handle();
  }

  /** The open viewer, present only while the lightbox shows it. */
  @JsType(isNative = true)
  interface Viewer {
    @JsProperty
    int getCurrIndex();

    void close();
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "PhotoSwipeLightbox")
  static class Lightbox {
    Lightbox(JsPropertyMap<Object> options) {}

    native void on(String event, Listener listener);

    native void init();

    native void loadAndOpen(int index);

    native void destroy();

    @JsProperty(name = "pswp")
    native Viewer getPswp();
  }
}
