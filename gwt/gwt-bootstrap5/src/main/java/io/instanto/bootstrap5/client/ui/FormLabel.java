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


/** A semantic HTML label styled with Bootstrap 5's form-label class. */
public class FormLabel extends ElementPanel implements IsEditor<LeafValueEditor<String>> {

    private boolean showRequiredIndicator;
    private String text = "";
    private boolean html;

    public FormLabel() {
        super("label");
        addStyleName("form-label");
    }

    public FormLabel(String text) {
        this();
        setText(text);
    }

    @Override
    public void setText(String text) {
        this.text = text == null ? "" : text;
        html = false;
        render();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setHTML(String html) {
        this.text = html == null ? "" : html;
        this.html = true;
        render();
    }

    @Override
    public String getHTML() {
        return getElement().getInnerHTML();
    }

    public void setFor(String targetId) {
        if (targetId == null || targetId.isEmpty()) {
            getElement().removeAttribute("for");
        } else {
            getElement().setAttribute("for", targetId);
        }
    }

    public void setShowRequiredIndicator(boolean showRequiredIndicator) {
        this.showRequiredIndicator = showRequiredIndicator;
        render();
    }

    public boolean getShowRequiredIndicator() {
        return showRequiredIndicator;
    }

    private void render() {
        String value = html ? text : escape(text);
        if (showRequiredIndicator && !value.isEmpty()) {
            value += " <sup class=\"text-danger\">*</sup>";
        }
        getElement().setInnerHTML(value);
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
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
