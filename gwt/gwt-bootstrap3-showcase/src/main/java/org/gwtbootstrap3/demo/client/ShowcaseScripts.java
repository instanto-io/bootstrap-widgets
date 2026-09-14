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

import com.google.gwt.core.client.ScriptInjector;

/**
 * The scripts the showcase itself needs, as opposed to the library's.
 *
 * <p>A seam. GWT compiles prettify.js into the output through a ClientBundle, which is
 * a generator; TeaVM has no such thing, so its counterpart loads the same file over the
 * network. Only this class differs between the two, and the entry point does not know
 * which one it has.</p>
 */
final class ShowcaseScripts {

    private ShowcaseScripts() {
    }

    /** Injects the syntax highlighter the code samples are rendered with. */
    static void inject() {
        ScriptInjector.fromString(GwtBootstrap3DemoClientBundle.INSTANCE.prettify().getText())
                .setWindow(ScriptInjector.TOP_WINDOW)
                .inject();
    }
}
