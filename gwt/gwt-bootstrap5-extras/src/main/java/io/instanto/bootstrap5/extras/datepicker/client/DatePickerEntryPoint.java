/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2018 GwtBootstrap3
 * %%
 * Modified from the GwtBootstrap3 original for the Bootstrap 5 track of
 * GWT Bootstrap: moved to the io.instanto.bootstrap5 namespace and
 * re-targeted at Bootstrap 5 markup, class names and JavaScript APIs. The
 * GwtBootstrap3 copyright above is retained as required by the Apache
 * License 2.0; the namespace changed, the attribution did not.
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
package io.instanto.bootstrap5.extras.datepicker.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.ScriptInjector;

/**
 * Injects Popper, then Tempus Dominus, in that order.
 *
 * <p>Tempus Dominus reads {@code window.Popper} when it opens its popup and only
 * falls back to a dynamic import if the global is missing, which a compiled GWT
 * application cannot resolve. Bootstrap 5's bundle keeps its own Popper private,
 * so the global is supplied here. Both injections leave an existing copy
 * alone.</p>
 */
public class DatePickerEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
        if (!isPopperLoaded()) {
            ScriptInjector.fromString(DatePickerClientBundle.INSTANCE.popper().getText())
                    .setWindow(ScriptInjector.TOP_WINDOW)
                    .inject();
        }
        if (!isTempusDominusLoaded()) {
            ScriptInjector.fromString(DatePickerClientBundle.INSTANCE.tempusDominus().getText())
                    .setWindow(ScriptInjector.TOP_WINDOW)
                    .inject();
        }
    }

    private static native boolean isPopperLoaded() /*-{
        return typeof $wnd.Popper !== "undefined";
    }-*/;

    private static native boolean isTempusDominusLoaded() /*-{
        return typeof $wnd.tempusDominus !== "undefined";
    }-*/;
}
