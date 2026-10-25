package io.instanto.bootstrap.testing.gwt3.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import org.gwtbootstrap3.extras.markdown.client.Markdown;
import org.gwtbootstrap3.extras.markdown.client.ui.MarkdownEditor;

final class MarkdownEditorParityFixture {
  static void run() {
    String name = Window.Location.getParameter("case");
    if (name == null || !name.contains("MARKDOWN-PARITY")) return;
    RootPanel host = RootPanel.get("fixtures");
    MarkdownEditor editor = new MarkdownEditor();
    try {
      host.add(editor);
      editor.setValue("**Original text**");
      editor.setEditorMode(MarkdownEditor.EditorMode.CLASSIC);
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

      click(editor.getElement(), ".gbm-mode-wysiwyg");
      selectBold(editor.getElement());
      assertEquals(
          "true",
          attribute(editor.getElement(), "[data-gbm-format-state='command:bold']", "aria-pressed"));
      for (int cycle = 0; cycle < 3; cycle++) {
        editor.removeFromParent();
        assertFalse(tracking(editor.getElement()));
        host.add(editor);
        assertTrue(tracking(editor.getElement()));
        selectBold(editor.getElement());
        assertEquals(
            "true",
            attribute(
                editor.getElement(), "[data-gbm-format-state='command:bold']", "aria-pressed"));
      }
      click(editor.getElement(), ".gbm-mode-classic");
      assertTrue(editor.isEditing());
      assertEquals(
          "false",
          attribute(editor.getElement(), "[data-gbm-format-state='command:bold']", "aria-pressed"));
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

  private static native void selectBold(Element root) /*-{
    var e=root.querySelector('[contenteditable]'); e.innerHTML='<p><strong>Bold text</strong></p>'; e.focus();
    var r=$doc.createRange(); r.selectNodeContents(e.querySelector('strong')); var s=$wnd.getSelection(); s.removeAllRanges(); s.addRange(r); e.dispatchEvent(new Event('keyup'));
  }-*/;

  private static native boolean tracking(
      Element
          root) /*-{ return !!root.querySelector('[contenteditable]').__gbmRefreshToolbarState; }-*/;
}
