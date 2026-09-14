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
package org.gwtbootstrap3.demo.client;

import com.google.gwt.user.client.ui.Widget;

/**
 * The TeaVM half of the extras page registry.
 *
 * <p>Only shared pages whose widget dependencies have been ported are reachable here.
 * A null result lets the entry point use its normal route handling.</p>
 */
final class ExtrasPages {

    private ExtrasPages() {
    }

    /** Returns a shared showcase page when its dependencies have been ported. */
    static Widget forToken(final String token) {
        if (org.gwtbootstrap3.demo.client.place.NameTokens.SUMMERNOTE.equals(token)) {
            return new org.gwtbootstrap3.demo.client.application.extras.SummernoteView();
        }
        if (org.gwtbootstrap3.demo.client.place.NameTokens.SELECT.equals(token)) {
            return new org.gwtbootstrap3.demo.client.application.extras.BootstrapSelectView();
        }
        if (org.gwtbootstrap3.demo.client.place.NameTokens.SLIDER.equals(token)) {
            return new org.gwtbootstrap3.demo.client.application.extras.SliderView();
        }
        if (org.gwtbootstrap3.demo.client.place.NameTokens.TOGGLESWITCH.equals(token)) {
            return new org.gwtbootstrap3.demo.client.application.extras.ToggleSwitchView();
        }
        return null;
    }
}
