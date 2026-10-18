package io.instanto.bootstrap.testing.gwt3.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.FullCalendar;
import org.gwtbootstrap3.extras.fullcalendar.client.ui.ViewOption;
import org.gwtbootstrap3.extras.gallery.client.ui.Gallery;
import org.gwtbootstrap3.extras.gallery.client.ui.GalleryImage;
import org.gwtbootstrap3.extras.markdown.client.ui.MarkdownEditor;

final class ExtrasConstructorFixture {
  static void run() {
    String name = Window.Location.getParameter("case");
    if (name == null || !name.contains("CTOR-EXTRA-")) return;
    try {
      if (name.contains("CTOR-EXTRA-001")) {
        class Probe extends FullCalendar {
          final Object ready = new Object();

          Probe() {
            super("constructor-calendar", ViewOption.month, true);
          }

          @Override
          public Element getElement() {
            check(ready != null, "Early calendar element hook");
            return super.getElement();
          }
        }
        check("constructor-calendar".equals(new Probe().getElement().getId()), "Calendar id lost");
      } else if (name.contains("CTOR-EXTRA-002")) {
        class Probe extends Gallery {
          final Object ready = new Object();

          @Override
          public Element getElement() {
            check(ready != null, "Early gallery element hook");
            return super.getElement();
          }
        }
        check(new Probe().getElement() != null, "Gallery element missing");
      } else if (name.contains("CTOR-EXTRA-003")) {
        class Probe extends GalleryImage {
          final Object ready = new Object();

          Probe() {
            super("data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs=");
          }

          @Override
          public Element getElement() {
            check(ready != null, "Early image element hook");
            return super.getElement();
          }

          @Override
          public void add(Widget widget) {
            check(ready != null, "Early image insertion hook");
            super.add(widget);
          }

          @Override
          public void setHref(String value) {
            check(ready != null, "Early image href hook");
            super.setHref(value);
          }
        }
        Probe image = new Probe();
        check(
            image.getWidgetCount() == 1 && image.getHref().startsWith("data:"),
            "Image initialization lost");
      } else if (name.contains("CTOR-EXTRA-004")) {
        class Probe extends MarkdownEditor {
          final Object ready = new Object();

          Probe(EditorMode mode) {
            super("Initial **text**", mode);
          }

          @Override
          public Element getElement() {
            check(ready != null, "Early markdown element hook");
            return super.getElement();
          }

          @Override
          public String getValue() {
            check(ready != null, "Early markdown value hook");
            return super.getValue();
          }

          @Override
          public void setValue(String value) {
            check(ready != null, "Early markdown setter hook");
            super.setValue(value);
          }

          @Override
          public void setValue(String value, boolean fire) {
            check(ready != null, "Early markdown event setter hook");
            super.setValue(value, fire);
          }
        }
        for (MarkdownEditor.EditorMode mode : MarkdownEditor.EditorMode.values()) {
          Probe editor = new Probe(mode);
          check(editor.getValue().contains("Initial"), "Initial markdown lost");
          editor.setValue("Changed");
          check(editor.getValue().contains("Changed"), "Post-construction setter lost");
        }
      } else if (name.contains("CTOR-EXTRA-006")) {
        MarkdownEditor editor = new MarkdownEditor("Initial");
        int[] changes = {0};
        editor.addValueChangeHandler(event -> changes[0]++);
        com.google.gwt.user.client.ui.RootPanel host =
            com.google.gwt.user.client.ui.RootPanel.get("fixtures");
        for (int cycle = 0; cycle < 3; cycle++) {
          host.add(editor);
          dispatchVisualInput(editor.getElement());
          check(changes[0] == cycle + 1, "Input callback missing or duplicated after attachment");
          editor.removeFromParent();
          dispatchVisualInput(editor.getElement());
          check(changes[0] == cycle + 1, "Detached editor still receives input callbacks");
        }
      } else if (name.contains("CTOR-EXTRA-005")) {
        Gallery first = new Gallery();
        Gallery second = new Gallery();
        second.setTitle("Shared controls");
        second.setShowPrevButton(false);
        second.setShowNextButton(false);
        second.setShowCloseButton(true);
        second.setShowIndicator(true);
        check(
            "Shared controls".equals(first.getTitle())
                && "Shared controls".equals(second.getTitle()),
            "Gallery controls were not initialized for every instance");
      }
      Document.get().getBody().setAttribute("data-attachment-result", "ok");
    } catch (Throwable failure) {
      Document.get().getBody().setAttribute("data-attachment-result", "FAILED: " + failure);
    }
  }

  private static native void dispatchVisualInput(Element root) /*-{
    var visual = root.querySelector('[contenteditable]');
    visual.innerHTML = '<p>Changed</p>';
    visual.dispatchEvent(new $wnd.Event('input', {bubbles: true}));
  }-*/;

  private static void check(boolean condition, String message) {
    if (!condition) throw new IllegalStateException(message);
  }
}
