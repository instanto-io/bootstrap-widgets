package io.instanto.bootstrap.testing.bootstrap5;

import static org.junit.Assert.*;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.bootstrap5.client.ui.Modal;
import io.instanto.webapp.testkit.dom.Dom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.jso.JSBody;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class ModalLifecycleTest {
  @Test
  public void removingAnOpenModalRestoresBodyAndAllowsRemount() {
    checkDetach(false);
  }

  @Test
  public void removingDuringTheOpeningTransitionCancelsPendingCallbacks() {
    checkDetach(true);
  }

  private void checkDetach(boolean animated) {
    Bootstrap5TestRuntime.initialise();
    Modal modal = new Modal();
    modal.setFade(animated);
    int[] shown = {0};
    modal.addShownHandler(event -> shown[0]++);
    String previousStyle = HTMLDocument.current().getBody().getAttribute("style");
    try {
      for (int cycle = 0; cycle < 2; cycle++) {
        RootPanel.get().add(modal);
        modal.show();
        if (!animated) assertEquals(cycle + 1, shown[0]);
        modal.removeFromParent();
        assertFalse(HTMLDocument.current().getBody().getClassList().contains("modal-open"));
        assertNull(HTMLDocument.current().querySelector(".modal-backdrop"));
        assertFalse(modal.getElement().hasClassName("show"));
        assertNotEquals(
            "hidden", HTMLDocument.current().getBody().getStyle().getPropertyValue("overflow"));
        boolean[] settled = {false};
        Window.setTimeout(() -> settled[0] = true, 400);
        Dom.waitFor(() -> assertTrue(settled[0]));
        if (animated) assertEquals(0, shown[0]);
      }
    } finally {
      if (modal.isAttached()) modal.removeFromParent();
      restoreBody(previousStyle);
    }
  }

  @JSBody(
      params = "style",
      script =
          "document.body.classList.remove('modal-open');"
              + " if(style===null) document.body.removeAttribute('style'); else document.body.setAttribute('style',style);"
              + " document.querySelectorAll('.modal-backdrop').forEach(e=>e.remove());")
  private static native void restoreBody(String style);
}
