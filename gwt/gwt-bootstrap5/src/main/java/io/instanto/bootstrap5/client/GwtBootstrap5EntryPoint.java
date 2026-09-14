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
package io.instanto.bootstrap5.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.ScriptInjector;

/** Injects Bootstrap 5 JavaScript when the host page has not already supplied it. */
public class GwtBootstrap5EntryPoint implements EntryPoint {

    private native boolean isBootstrapLoaded() /*-{
        return typeof $wnd.bootstrap !== 'undefined' && typeof $wnd.bootstrap.Modal !== 'undefined';
    }-*/;

    @Override
    public void onModuleLoad() {
        if (!isBootstrapLoaded()) {
            ScriptInjector.fromString(GwtBootstrap5ClientBundle.INSTANCE.bootstrap().getText())
                    .setWindow(ScriptInjector.TOP_WINDOW)
                    .inject();
        }
    }
}
