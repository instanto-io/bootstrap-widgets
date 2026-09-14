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
package io.instanto.bootstrap5.client.ui;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.ui.client.adapters.HasTextEditor;


public class Legend extends ElementPanel implements IsEditor<LeafValueEditor<String>> {

    public Legend() {
        this("");
    }

    public Legend(String text) {
        super("legend");
        setText(text);
    }

    private LeafValueEditor<String> editor;

    /**
     * Bootstrap 3 got this from AbstractTextWidget. The Bootstrap 5 widget is a
     * panel rather than a leaf so that it can hold an icon or nested markup, so
     * the editor is composed in rather than inherited.
     */
    @Override
    public LeafValueEditor<String> asEditor() {
        if (editor == null) {
            editor = HasTextEditor.of(this);
        }
        return editor;
    }

}
