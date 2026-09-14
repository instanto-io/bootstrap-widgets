package org.gwtbootstrap3.extras.select.client;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2016 GwtBootstrap3
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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.ScriptInjector;

/**
 * @author godi
 */
public class SelectEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
        declareBootstrapGlobal();
        ScriptInjector.fromString(SelectClientBundle.INSTANCE.select().getText())
            .setWindow(ScriptInjector.TOP_WINDOW).inject();
    }

    /**
     * Makes sure a {@code bootstrap} global exists before the plugin is evaluated.
     *
     * <p>bootstrap-select 1.14 supports Bootstrap 3 and 5 from one file, and finds the
     * dropdown class with {@code window.Dropdown || bootstrap.Dropdown}. On a Bootstrap 5
     * page that global is there. On a Bootstrap 3 page it is not -- and because it is an
     * undeclared identifier rather than a missing property, reading it throws a
     * ReferenceError instead of giving undefined. That happens while the plugin is still
     * being defined, so nothing is defined at all: selectpicker never registers and every
     * Select on the page stays an unstyled select element.</p>
     *
     * <p>An empty object is enough, and is what the author's own expression assumes it
     * can fall back through. The plugin reads the value it produces in exactly two
     * places: a version probe already wrapped in a try/catch, and a branch guarded by
     * {@code 4 < major}, which Bootstrap 3 never takes. Nothing else is affected, and a
     * page that really does have Bootstrap 5 loaded keeps its own global.</p>
     */
    private static native void declareBootstrapGlobal() /*-{
        $wnd.bootstrap = $wnd.bootstrap || {};
    }-*/;
}
