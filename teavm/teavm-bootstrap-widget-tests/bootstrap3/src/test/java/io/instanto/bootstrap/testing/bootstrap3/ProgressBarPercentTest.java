package io.instanto.bootstrap.testing.bootstrap3;

import org.gwtbootstrap3.client.ui.ProgressBar;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;
import static org.junit.Assert.assertEquals;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class ProgressBarPercentTest {
    @Test
    public void unsetAndNonPercentageWidthsReturnZero() {
        ProgressBar bar = new ProgressBar();
        assertEquals(0, bar.getPercent(), 0);
        bar.getElement().getStyle().setProperty("width", "20px");
        assertEquals(0, bar.getPercent(), 0);
    }

    @Test
    public void percentageWidthRoundTrips() {
        ProgressBar bar = new ProgressBar();
        bar.setPercent(37.5);
        assertEquals(37.5, bar.getPercent(), 0);
        bar.setPercent(0);
        assertEquals(0, bar.getPercent(), 0);
        bar.setPercent(100);
        assertEquals(100, bar.getPercent(), 0);
    }
}
