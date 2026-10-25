package io.instanto.bootstrap.testing.bootstrap5;

import static org.junit.Assert.*;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.bootstrap5.extras.markdown.client.Markdown;
import io.instanto.bootstrap5.extras.markdown.client.ui.MarkdownEditor;
import io.instanto.webapp.testkit.dom.Dom;
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
    Bootstrap5TestRuntime.initialise();
    boolean[] ready = {false};
    Markdown.whenReady(() -> ready[0] = true);
    Dom.waitFor(() -> assertTrue(ready[0]), 10000);
    Dom.container().setAttribute("id", "markdown-parity");
    RootPanel host = RootPanel.get("markdown-parity");
    MarkdownEditor editor = new MarkdownEditor();
    try {
      host.add(editor);
      editor.setValue("**Original text**");
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
}
