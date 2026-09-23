package io.instanto.bootstrap5.extras.grid.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import io.instanto.bootstrap5.extras.base.client.NativeJson;
import io.instanto.bootstrap5.extras.base.client.PluginCallback;
import io.instanto.bootstrap5.extras.grid.client.GridResources;
import java.util.ArrayList;
import java.util.List;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsArrayLike;
import jsinterop.base.JsPropertyMap;

/** Tabulator, reached through JsInterop so GWT and TeaVM compile this one source. */
final class GridJs {
  private GridJs() {}

  /** A table and the calls waiting for Tabulator to finish building it. */
  static final class Handle {
    private final Tabulator table;
    private boolean built;
    private final List<Runnable> pending = new ArrayList<>();

    private Handle(Tabulator table) {
      this.table = table;
    }

    private void whenBuilt(Runnable call) {
      if (built) call.run();
      else pending.add(call);
    }
  }

  static void whenReady(Runnable action) {
    GridResources.whenReady(GridJs::isReady, action);
  }

  static boolean isReady() {
    return Js.global().get("Tabulator") != null;
  }

  static Handle create(
      Element el,
      String columns,
      String data,
      String group,
      boolean tree,
      int pageSize,
      String remote,
      PluginCallback changed) {
    JsArrayLike<Object> columnList = Js.uncheckedCast(NativeJson.parse(columns));
    for (int i = 0; i < columnList.getLength(); i++) {
      Column column = Js.uncheckedCast(columnList.getAt(i));
      String title = column.getTitle();
      column.setTitleFormatter(() -> text(title));
    }
    Options options = Js.uncheckedCast(JsPropertyMap.of());
    JsPropertyMap<Object> opts = Js.asPropertyMap(options);
    opts.set("columns", columnList);
    opts.set("data", NativeJson.parse(data));
    opts.set("layout", "fitColumns");
    opts.set("dataTree", tree);
    if (!group.isEmpty()) {
      opts.set("groupBy", group);
      options.setGroupHeader((value, count) -> text(value + " (" + count + ")"));
    }
    if (pageSize > 0) {
      opts.set("pagination", true);
      // A boxed Integer is a Java object on GWT; Tabulator needs a number.
      opts.set("paginationSize", (double) pageSize);
    }
    if (!remote.isEmpty()) {
      opts.set("ajaxURL", remote);
      opts.set("progressiveLoad", "scroll");
      opts.delete("pagination");
      opts.delete("paginationSize");
      opts.delete("data");
    }
    Handle handle = new Handle(new Tabulator(Js.asAny(el), opts));
    handle.table.on(
        "tableBuilt",
        () -> {
          handle.built = true;
          List<Runnable> calls = new ArrayList<>(handle.pending);
          handle.pending.clear();
          for (Runnable call : calls) call.run();
        });
    handle.table.on("cellEdited", () -> changed.onEvent(data(handle)));
    return handle;
  }

  static String data(Handle p) {
    return NativeJson.stringify(p.table.getData());
  }

  static void filter(Handle p, String field, String value) {
    p.whenBuilt(
        () -> {
          if (value != null && !value.isEmpty()) p.table.setFilter(field, "like", value);
          else p.table.clearFilter();
        });
  }

  static void destroy(Handle p) {
    p.table.destroy();
  }

  /** A span holding {@code text}; Tabulator inserts it, so the text is never parsed as HTML. */
  private static Any text(String text) {
    SpanElement span = Document.get().createSpanElement();
    span.setInnerText(text);
    return Js.asAny(span);
  }

  @JsFunction
  interface Listener {
    void handle();
  }

  @JsFunction
  interface TitleFormatter {
    Any format();
  }

  @JsFunction
  interface GroupHeader {
    Any format(String value, int count);
  }

  /*
   * Callbacks go through typed properties rather than JsPropertyMap.set: TeaVM turns a
   * lambda into a JavaScript function only where the target type is the functional
   * interface, so an Object-typed map value would carry the Java object instead.
   */

  /** A column definition, as parsed from the widget's JSON. */
  @JsType(isNative = true)
  interface Column {
    @JsProperty
    String getTitle();

    @JsProperty
    void setTitleFormatter(TitleFormatter formatter);
  }

  /** The table options that take callbacks. */
  @JsType(isNative = true)
  interface Options {
    @JsProperty
    void setGroupHeader(GroupHeader header);
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Tabulator")
  static class Tabulator {
    Tabulator(Any element, JsPropertyMap<Object> options) {}

    native void on(String event, Listener listener);

    native Object getData();

    native void setFilter(String field, String type, String value);

    native void clearFilter();

    native void destroy();
  }
}
