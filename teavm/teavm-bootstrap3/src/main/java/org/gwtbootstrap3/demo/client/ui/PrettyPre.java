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
package org.gwtbootstrap3.demo.client.ui;

import org.gwtbootstrap3.client.ui.Pre;
import org.gwtbootstrap3.demo.client.ui.constants.Styles;
import org.teavm.jso.JSBody;

/**
 * A {@code pre} the syntax highlighter has run over.
 *
 * <p>The TeaVM half of a seam. The GWT file is identical but for the one call into the
 * page, which it makes through JSNI; this one makes it through JSBody. The highlighter
 * may not have loaded when a page is built, so a missing prettyPrint is not an error
 * here -- the script is fetched by URL on this backend rather than compiled in.</p>
 */
public class PrettyPre extends Pre {

    public PrettyPre() {
        addStyleName(Styles.PRETTYPRINT);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        prettyPrint();
    }

    @JSBody(script = "if (typeof window.prettyPrint === 'function') { window.prettyPrint(); }")
    private static native void prettyPrint();
}
