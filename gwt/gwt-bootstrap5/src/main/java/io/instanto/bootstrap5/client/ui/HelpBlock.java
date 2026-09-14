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

import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.IconType;


import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.ui.client.adapters.HasTextEditor;


public class HelpBlock extends ElementPanel implements IsEditor<LeafValueEditor<String>> {

    public HelpBlock() {
        this("");
    }

    public HelpBlock(String text) {
        super("div");
        addStyleName("form-text");
        setText(text);
    }

    /**
     * Shows {@code message} as a Bootstrap 5 validation message.
     *
     * <p>Bootstrap 3 styled these with .help-block inside a .has-error group.
     * Bootstrap 5 uses .invalid-feedback, which is hidden unless it follows a
     * sibling carrying .is-invalid; .d-block is Bootstrap's own escape hatch for
     * showing it when the control is not a direct sibling, which is the case
     * whenever the control is wrapped, as it is in a SuggestBox.</p>
     */
    public void setError(String message) {
        boolean hasError = message != null && !message.isEmpty();
        if (hasError && !erroring) {
            helpText = getText();
        }
        erroring = hasError;
        setText(hasError ? message : helpText);
        setStyleName("form-text", !hasError);
        setStyleName("invalid-feedback", hasError);
        setStyleName("d-block", hasError);
    }

    /** Restores the help text this block carried before the error. */
    public void clearError() {
        setError("");
    }

    private String helpText = "";

    private boolean erroring;


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


    private IconType iconType;

    /**
     * Bootstrap 3 put a contextual icon inside the help block. Bootstrap 5 has
     * no equivalent component, so the icon is prepended to the text.
     */
    public void setIconType(final IconType iconType) {
        this.iconType = iconType;
        if (icon != null) {
            icon.removeFromParent();
            icon = null;
        }
        if (iconType != null) {
            icon = new Icon(iconType);
            icon.addStyleName("me-1");
            insert(icon, 0);
        }
    }

    public IconType getIconType() {
        return iconType;
    }

    public boolean isError() {
        return StyleHelper.containsStyle(getStyleName(), "invalid-feedback");
    }

    private Icon icon;

}
