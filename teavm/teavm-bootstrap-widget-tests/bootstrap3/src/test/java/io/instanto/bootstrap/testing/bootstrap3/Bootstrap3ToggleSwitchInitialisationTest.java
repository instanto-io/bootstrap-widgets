package io.instanto.bootstrap.testing.bootstrap3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.junit.EachTestCompiledSeparately;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

import static org.junit.Assert.assertEquals;

/** Forces the asynchronous path independently of the functional suite's plugin cache. */
@RunWith(TeaVMTestRunner.class)
@SkipJVM
@EachTestCompiledSeparately
public class Bootstrap3ToggleSwitchInitialisationTest {
    @Test
    public void detachedWidgetIsNotInitializedWhenItsScriptArrives() {
        Bootstrap3ToggleSwitchSteps steps = new Bootstrap3ToggleSwitchSteps();
        steps.prepare();
        try {
            assertEquals(0, HTMLDocument.current()
                    .querySelectorAll("script[src*='bootstrap-switch']").getLength());
            steps.removeEarly();
            steps.finishLoading();
            steps.noOrphan();
            assertEquals(1, HTMLDocument.current()
                    .querySelectorAll("script[src*='bootstrap-switch']").getLength());
        } finally {
            steps.cleanup();
        }
    }
}
