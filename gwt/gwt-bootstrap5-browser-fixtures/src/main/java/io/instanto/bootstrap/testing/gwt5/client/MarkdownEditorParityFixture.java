package io.instanto.bootstrap.testing.gwt5.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.bootstrap5.extras.markdown.client.Markdown;
import io.instanto.bootstrap5.extras.markdown.client.ui.MarkdownEditor;

final class MarkdownEditorParityFixture {
  static void run() {
    String name = Window.Location.getParameter("case");
    if (name == null || !name.contains("MARKDOWN-PARITY")) return;
    RootPanel host = RootPanel.get("fixtures");
    MarkdownEditor editor = new MarkdownEditor();
    try {
      host.add(editor);
      editor.setValue("**Original text**");
      click(editor.getElement(), ".gbm-tab-preview");
      assertFalse(editor.isEditing());
      assertEquals("true", attribute(editor.getElement(), ".gbm-tab-preview", "aria-pressed"));
      assertTrue(editor.getValue().contains("Original text"));
      click(editor.getElement(), ".gbm-tab-write");
      assertTrue(editor.isEditing());
      assertEquals("true", attribute(editor.getElement(), ".gbm-tab-write", "aria-pressed"));
      editor.setEnabled(false);
      click(editor.getElement(), ".gbm-tab-preview");
      assertTrue(editor.isEditing());
      editor.setEnabled(true);
      assertEquals("3.4.17", purifierVersion());
      assertFalse(Markdown.toHtml("<img src=x onerror=alert(1)>").contains("onerror"));
      Document.get().getBody().setAttribute("data-markdown-parity", "ok");
    } catch (Throwable failure) {
      Document.get().getBody().setAttribute("data-markdown-parity", failure.toString());
    } finally {
      host.clear();
    }
  }

  private static void assertTrue(boolean value) {
    if (!value) throw new AssertionError("Expected true");
  }

  private static void assertFalse(boolean value) {
    assertTrue(!value);
  }

  private static void assertEquals(String expected, String actual) {
    if (!expected.equals(actual)) throw new AssertionError(expected + " != " + actual);
  }

  private static native void click(
      Element root, String selector) /*-{ root.querySelector(selector).click(); }-*/;

  private static native String attribute(
      Element root,
      String selector,
      String name) /*-{ return root.querySelector(selector).getAttribute(name); }-*/;

  private static native String purifierVersion() /*-{ return $wnd.DOMPurify.version; }-*/;
}
