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

import io.instanto.bootstrap5.client.ui.base.TextBoxBase;
import io.instanto.bootstrap5.client.ui.constants.Styles;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.ui.RootPanel;

public class TextArea extends TextBoxBase {

    public static TextArea wrap(final Element element) {
        assert Document.get().getBody().isOrHasChild(element);

        final TextArea textArea = new TextArea(element);
        textArea.onAttach();
        RootPanel.detachOnWindowClose(textArea);
        return textArea;
    }

    public TextArea() {
        super(Document.get().createTextAreaElement());
        setStyleName(Styles.FORM_CONTROL);
    }

    public TextArea(String text) {
        this();
        setText(text);
    }

    protected TextArea(final Element element) {
        super(element.<Element>cast());
        TextAreaElement.as(element);
        element.addClassName(Styles.FORM_CONTROL);
    }

    public int getCharacterWidth() {
        return getTextAreaElement().getCols();
    }

    @Override
    public int getCursorPos() {
        return getImpl().getTextAreaCursorPos(getElement());
    }

    @Override
    public int getSelectionLength() {
        return getImpl().getTextAreaSelectionLength(getElement());
    }

    public int getVisibleLines() {
        return getTextAreaElement().getRows();
    }

    public void setCharacterWidth(final int width) {
        getTextAreaElement().setCols(width);
    }

    public void setVisibleLines(final int lines) {
        getTextAreaElement().setRows(lines);
    }

    private TextAreaElement getTextAreaElement() {
        return TextAreaElement.as(getElement());
    }

    public void clear() {
        super.setValue(null);
    }
}
