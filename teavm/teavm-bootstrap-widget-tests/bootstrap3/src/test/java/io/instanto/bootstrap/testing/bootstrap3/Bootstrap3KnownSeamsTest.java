package io.instanto.bootstrap.testing.bootstrap3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.gwtbootstrap3.client.ui.Form;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.RootPanel;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class Bootstrap3KnownSeamsTest {
    @Test
    public void formDeferredBindingCreatesAndRemovesItsTargetFrame() {
        Form form = new Form();
        String target = form.getTarget();

        assertTrue(target.startsWith("GWTBootstrap3_AbstractForm_"));
        assertFalse(hasNamedFrame(target));

        RootPanel.get().add(form);
        assertTrue(hasNamedFrame(target));

        form.removeFromParent();
        assertFalse(hasNamedFrame(target));
    }

    private static boolean hasNamedFrame(String name) {
        NodeList<Element> frames = Document.get().getBody().getElementsByTagName("iframe");
        for (int index = 0; index < frames.getLength(); index++) {
            if (name.equals(frames.getItem(index).getAttribute("name"))) {
                return true;
            }
        }
        return false;
    }
}
