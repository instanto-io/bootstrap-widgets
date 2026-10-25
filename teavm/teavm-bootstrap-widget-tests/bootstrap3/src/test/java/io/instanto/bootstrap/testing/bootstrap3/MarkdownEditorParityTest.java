package io.instanto.bootstrap.testing.bootstrap3;

import static org.junit.Assert.*;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.webapp.testkit.dom.Dom;
import org.gwtbootstrap3.extras.markdown.client.Markdown;
import org.gwtbootstrap3.extras.markdown.client.ui.MarkdownEditor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class MarkdownEditorParityTest {
  @Test
  public void explicitModesPreserveContentStateAndLifecycle() {
    Dom.reset();
    Bootstrap3TestRuntime.initialise();
    boolean[] ready = {false};
    Markdown.whenReady(() -> ready[0] = true);
    Dom.waitFor(() -> assertTrue(ready[0]), 10000);
    Dom.container().setAttribute("id", "markdown-parity");
    RootPanel host = RootPanel.get("markdown-parity");
    MarkdownEditor editor = new MarkdownEditor();
    try {
      host.add(editor);
      editor.setValue("**Original text**");
      editor.setEditorMode(MarkdownEditor.EditorMode.CLASSIC);
      click(editor.getElement().unwrap(), ".gbm-tab-preview");
      assertFalse(editor.isEditing());
      assertEquals(
          "true", attribute(editor.getElement().unwrap(), ".gbm-tab-preview", "aria-pressed"));
      assertTrue(editor.getValue().contains("Original text"));
      click(editor.getElement().unwrap(), ".gbm-tab-write");
      assertTrue(editor.isEditing());
      assertEquals(
          "true", attribute(editor.getElement().unwrap(), ".gbm-tab-write", "aria-pressed"));
      editor.setEnabled(false);
      click(editor.getElement().unwrap(), ".gbm-tab-preview");
      assertTrue(editor.isEditing());
      editor.setEnabled(true);

      click(editor.getElement().unwrap(), ".gbm-mode-wysiwyg");
      selectBold(editor.getElement().unwrap());
      assertEquals(
          "true",
          attribute(
              editor.getElement().unwrap(),
              "[data-gbm-format-state='command:bold']",
              "aria-pressed"));
      for (int cycle = 0; cycle < 3; cycle++) {
        editor.removeFromParent();
        assertFalse(tracking(editor.getElement().unwrap()));
        host.add(editor);
        assertTrue(tracking(editor.getElement().unwrap()));
        selectBold(editor.getElement().unwrap());
        assertEquals(
            "true",
            attribute(
                editor.getElement().unwrap(),
                "[data-gbm-format-state='command:bold']",
                "aria-pressed"));
      }
      click(editor.getElement().unwrap(), ".gbm-mode-classic");
      assertTrue(editor.isEditing());
      assertEquals(
          "false",
          attribute(
              editor.getElement().unwrap(),
              "[data-gbm-format-state='command:bold']",
              "aria-pressed"));
      assertEquals("3.4.17", purifierVersion());
      assertFalse(Markdown.toHtml("<img src=x onerror=alert(1)>").contains("onerror"));
    } finally {
      host.clear();
      Dom.reset();
    }
  }

  @JSBody(
      params = {"root", "selector"},
      script = "root.querySelector(selector).click();")
  private static native void click(HTMLElement root, String selector);

  @JSBody(
      params = {"root", "selector", "name"},
      script = "return root.querySelector(selector).getAttribute(name);")
  private static native String attribute(HTMLElement root, String selector, String name);

  @JSBody(script = "return window.DOMPurify.version;")
  private static native String purifierVersion();

  @JSBody(
      params = {"root"},
      script =
          "var e=root.querySelector('[contenteditable]'); e.innerHTML='<p><strong>Bold text</strong></p>'; e.focus(); var r=document.createRange(); r.selectNodeContents(e.querySelector('strong')); var s=window.getSelection(); s.removeAllRanges(); s.addRange(r); e.dispatchEvent(new Event('keyup')); ")
  private static native void selectBold(HTMLElement root);

  @JSBody(
      params = {"root"},
      script = "return !!root.querySelector('[contenteditable]').__gbmRefreshToolbarState;")
  private static native boolean tracking(HTMLElement root);
}
