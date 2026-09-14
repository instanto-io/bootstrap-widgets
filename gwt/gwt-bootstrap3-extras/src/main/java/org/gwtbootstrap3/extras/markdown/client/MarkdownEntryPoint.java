/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2018 GwtBootstrap3
 * %%
 * Modified from the GwtBootstrap3 original for the Bootstrap 5 track of
 * GWT Bootstrap: moved to the org.gwtbootstrap3 namespace and re-targeted
 * at Bootstrap 5 markup, class names and JavaScript APIs.
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
package org.gwtbootstrap3.extras.markdown.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.ScriptInjector;

/** Injects marked and DOMPurify unless the page already provides them. */
public class MarkdownEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
        if (!isMarkedLoaded()) {
            ScriptInjector.fromString(MarkdownClientBundle.INSTANCE.marked().getText())
                    .setWindow(ScriptInjector.TOP_WINDOW)
                    .inject();
        }
        if (!isPurifyLoaded()) {
            ScriptInjector.fromString(MarkdownClientBundle.INSTANCE.domPurify().getText())
                    .setWindow(ScriptInjector.TOP_WINDOW)
                    .inject();
        }
        Markdown.configure();
    }

    private static native boolean isMarkedLoaded() /*-{
        return typeof $wnd.marked !== "undefined";
    }-*/;

    private static native boolean isPurifyLoaded() /*-{
        return typeof $wnd.DOMPurify !== "undefined";
    }-*/;
}
