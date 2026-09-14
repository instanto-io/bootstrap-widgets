package io.instanto.bootstrap.testing.bootstrap5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.RootPanel;

import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.client.ui.Form;
import io.instanto.bootstrap5.client.ui.Range;
import io.instanto.bootstrap5.client.ui.Tooltip;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;
import io.instanto.bootstrap5.client.ui.constants.Placement;
import io.instanto.bootstrap5.client.ui.constants.Trigger;
import io.instanto.webapp.testkit.dom.Dom;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class Bootstrap5KnownSeamsTest {
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

    @Test
    public void rangeContinuousModeDeliversNativeInputEvents() {
        Range range = new Range();
        int[] events = {0};
        range.addValueChangeHandler(event -> events[0]++);
        range.setContinuous(true);
        range.setStep(0.5);
        range.setValue(42.5);

        RootPanel.get().add(range);
        Dom.fire(range.getElement().unwrap(), "input");

        assertEquals(1, events[0]);
        assertEquals(42.5, range.getValue(), 0.001);
        range.removeFromParent();
    }

    @Test
    public void tooltipOptionsReachTheTeaVmBootstrapSeam() {
        Button target = new Button("Details");
        Tooltip tooltip = new Tooltip(target, "More detail");
        tooltip.setPlacement(Placement.BOTTOM);
        tooltip.setTrigger(Trigger.CLICK);
        tooltip.setContainer("body");
        tooltip.setSelector(".detail-source");
        tooltip.setViewportSelector("main");
        tooltip.setViewportPadding(8);
        tooltip.setShowDelayMs(20);
        tooltip.setHideDelayMs(30);

        RootPanel.get().add(target);
        assertTrue(tooltip.isInitialized());
        assertEquals(Placement.BOTTOM, tooltip.getPlacement());
        assertEquals(Trigger.CLICK, tooltip.getTrigger());

        tooltip.show();
        tooltip.toggle();
        tooltip.hide();
        tooltip.destroy();
        assertFalse(tooltip.isInitialized());
        target.removeFromParent();
    }

    @Test
    public void lightButtonTypeMapsExactlyToBootstrapLight() {
        Button button = new Button("Light", ButtonType.LIGHT);

        assertEquals(ButtonType.LIGHT, button.getType());
        assertTrue(button.getStyleName().contains("btn-light"));
        assertFalse(button.getStyleName().contains("btn-secondary"));
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
