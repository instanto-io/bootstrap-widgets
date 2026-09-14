/*
 * #%L
 * GWT Bootstrap
 * %%
 * Copyright (C) 2026 Carl Stainton
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.gwtbootstrap3.teavm.demo;

import org.gwtbootstrap3.client.Bootstrap3;
import org.gwtbootstrap3.client.Bootstrap3Resources;
import org.gwtbootstrap3.demo.client.GwtBootstrap3DemoEntryPoint;

/**
 * The GWT showcase, run on TeaVM.
 *
 * <p>Not a port: this is the same GwtBootstrap3DemoEntryPoint the GWT build compiles,
 * reached through the same onModuleLoad GWT calls, with its pages built from the same
 * UiBinder templates. All this class supplies is what GWT's module system would have --
 * where the assets live, and the call to start.</p>
 *
 * <p>Forty-one of the fifty-five pages are here. The rest need gwt-bootstrap3-extras,
 * which is not ported; ExtrasPages is where that shows.</p>
 */
public final class SharedShowcaseApp {

    private SharedShowcaseApp() {
    }

    public static void main(final String[] args) {
        // The site publishes the module's generated asset tree beside its JavaScript.
        Bootstrap3Resources.setBase("teavm/css/");
        // What GWT's bootstrap does before the application's own entry point: run the
        // library module's initialisation first, so its stylesheets are on the page.
        Bootstrap3.initialise(() -> new GwtBootstrap3DemoEntryPoint(TeaVmSetupView::new).onModuleLoad());
    }
}
