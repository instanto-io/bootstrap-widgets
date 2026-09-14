package io.instanto.bootstrap5.teavm.demo;

import com.google.gwt.core.client.GWT;

import io.instanto.bootstrap5.client.Bootstrap5;
import io.instanto.bootstrap5.client.Bootstrap5Resources;
import io.instanto.bootstrap5.showcase.client.ShowcaseEntryPoint;

/**
 * The GWT showcase, run on TeaVM.
 *
 * <p>Not a port: this is the same ShowcaseEntryPoint the GWT build compiles, reached
 * through the same onModuleLoad GWT calls. The only thing this class supplies is what
 * GWT's module system would have -- where the assets live, and the call to start.</p>
 */
public final class SharedShowcaseApp {

    private SharedShowcaseApp() {
    }

    public static void main(final String[] args) {
        GWT.setModuleBaseURL("teavm5/");
        Bootstrap5Resources.setAssetBase("teavm5/");
        // What GWT's bootstrap does before the application's own entry point: run the
        // library module's initialisation first, so its stylesheets are on the page.
        Bootstrap5.initialise(() -> {
            new ShowcaseEntryPoint(TeaVmSetupView::new).onModuleLoad();
        });
    }

}
