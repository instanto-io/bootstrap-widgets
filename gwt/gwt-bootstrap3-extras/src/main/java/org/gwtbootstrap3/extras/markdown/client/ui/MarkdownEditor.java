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
package org.gwtbootstrap3.extras.markdown.client.ui;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.base.HasId;
import org.gwtbootstrap3.client.ui.base.mixin.IdMixin;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Div;

import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;

/**
 * An editor for applications whose stored format is Markdown.
 *
 * <p>Deliberately not a rich text editor. A rich text editor keeps HTML, so an
 * application storing Markdown has to convert on every save, which is lossy and
 * loses exactly the constructs a flexmark server understands -- task lists and
 * tables among them. This keeps the Markdown as the author typed it, and adds
 * what a bare textarea lacks: a toolbar that inserts the syntax, a preview
 * rendered with the same dialect the server uses, and the reference to hand
 * rather than a link away to another site.</p>
 *
 * <p>The value is Markdown. {@link #getValue()} returns exactly what is in the
 * textarea; {@link #getHTML()} renders it.</p>
 */
public class MarkdownEditor extends Div implements HasEnabled, HasId, HasValue<String>,
        HasValueChangeHandlers<String> {

    private final IdMixin<MarkdownEditor> idMixin = new IdMixin<MarkdownEditor>(this);

    private final Div toolbar = new Div();

    private final TextArea textArea = new TextArea();

    private final MarkdownPanel preview = new MarkdownPanel();

    private final MarkdownHelp help = new MarkdownHelp();

    /** The one control that changes mode: a pencil to edit, an eye to read. */
    private final Button modeButton = new Button();

    private final Div syntaxButtons = syntaxButtons();

    private boolean editing;

    /** Whether the reference should show while editing; it never shows while reading. */
    private boolean helpVisible = true;

    public MarkdownEditor() {
        addStyleName("gbm-markdown-editor");

        // Bootstrap 3 has no flex utilities; btn-toolbar is its row-of-groups idiom.
        toolbar.addStyleName("btn-toolbar");
        toolbar.add(modeToggle());
        toolbar.add(syntaxButtons);
        add(toolbar);

        textArea.setVisibleLines(8);
        textArea.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(final KeyUpEvent event) {
                ValueChangeEvent.fire(MarkdownEditor.this, getValue());
            }
        });
        add(textArea);

        preview.addStyleName("well");
        preview.setVisible(false);
        add(preview);

        add(help);
        // Nothing to read yet, so an empty editor opens ready to type.
        showEditor();
    }

    public MarkdownEditor(final String markdown) {
        this();
        setValue(markdown);
        // There is something to read now, so start by reading it.
        showRendered();
    }

    private Button modeToggle() {
        modeButton.setSize(ButtonSize.SMALL);
        modeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (editing) {
                    showRendered();
                } else {
                    showEditor();
                }
            }
        });
        return modeButton;
    }

    private void setModeButton(final IconType icon, final String title) {
        modeButton.setIcon(icon);
        modeButton.setTitle(title);
    }

    private Div syntaxButtons() {
        final Div group = new Div();
        group.addStyleName("btn-group");
        group.add(syntaxButton(IconType.BOLD, "Bold", "**", "**", "bold text"));
        group.add(syntaxButton(IconType.ITALIC, "Italic", "*", "*", "italic text"));
        group.add(syntaxButton(IconType.STRIKETHROUGH, "Strikethrough", "~~", "~~", "struck text"));
        group.add(syntaxButton(IconType.CODE, "Code", "`", "`", "code"));
        group.add(syntaxButton(IconType.LINK, "Link", "[", "](https://)", "text"));
        group.add(syntaxButton(IconType.LIST_UL, "Bulleted list", "\n- ", "", "item"));
        group.add(syntaxButton(IconType.LIST_OL, "Numbered list", "\n1. ", "", "item"));
        group.add(syntaxButton(IconType.CHECK_SQUARE_O, "Task list", "\n- [ ] ", "", "task"));
        group.add(syntaxButton(IconType.TABLE, "Table",
                "\n| Column | Column |\n| --- | --- |\n| ", " |  |\n", "cell"));
        group.add(syntaxButton(IconType.QUOTE_LEFT, "Quote", "\n> ", "", "quote"));
        return group;
    }

    private Button syntaxButton(final IconType icon, final String title, final String before,
            final String after, final String placeholder) {
        final Button button = new Button("");
        button.setSize(ButtonSize.SMALL);
        button.setIcon(icon);
        button.setTitle(title);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                wrapSelection(before, after, placeholder);
            }
        });
        return button;
    }

    /** Wraps the selection, or inserts a placeholder when nothing is selected. */
    private void wrapSelection(final String before, final String after, final String placeholder) {
        final TextAreaElement element = textArea.getElement().cast();
        final int start = TextAreaSelection.start(element);
        final int end = TextAreaSelection.end(element);
        final String value = getValue();
        final String selected = start == end ? placeholder : value.substring(start, end);
        setValue(value.substring(0, start) + before + selected + after + value.substring(end), true);
        TextAreaSelection.focusAndSelect(element, start + before.length(), selected.length());
    }

    /** Shows the textarea and the syntax buttons, with an eye to go back to reading. */
    public void showEditor() {
        editing = true;
        textArea.setVisible(true);
        preview.setVisible(false);
        syntaxButtons.setVisible(true);
        help.setVisible(helpVisible);
        setModeButton(IconType.EYE, "Done editing");
    }

    /**
     * Shows the rendered Markdown, with a pencil to start editing. The syntax buttons and
     * the reference go with the textarea, because neither means anything while reading.
     */
    public void showRendered() {
        editing = false;
        preview.setMarkdown(getValue());
        textArea.setVisible(false);
        preview.setVisible(true);
        syntaxButtons.setVisible(false);
        help.setVisible(false);
        setModeButton(IconType.PENCIL, "Edit");
    }

    /** True while the textarea is showing. */
    public boolean isEditing() {
        return editing;
    }

    /** @deprecated use {@link #showEditor()} */
    @Deprecated
    public void showWrite() {
        showEditor();
    }

    /** @deprecated use {@link #showRendered()} */
    @Deprecated
    public void showPreview() {
        showRendered();
    }

    /** @deprecated use {@link #isEditing()} */
    @Deprecated
    public boolean isPreviewing() {
        return !editing;
    }

    /** Shows or hides the syntax reference beneath the editor while editing. */
    public void setHelpVisible(final boolean visible) {
        helpVisible = visible;
        help.setVisible(visible && editing);
    }

    public boolean isHelpVisible() {
        return helpVisible;
    }

    public void setVisibleLines(final int lines) {
        textArea.setVisibleLines(lines);
    }

    public void setPlaceholder(final String placeholder) {
        textArea.setPlaceholder(placeholder);
    }

    /** The Markdown rendered as sanitised HTML. */
    public String getHTML() {
        return org.gwtbootstrap3.extras.markdown.client.Markdown.toHtml(getValue());
    }

    @Override
    public String getValue() {
        final String value = textArea.getValue();
        return value == null ? "" : value;
    }

    @Override
    public void setValue(final String value) {
        setValue(value, false);
    }

    @Override
    public void setValue(final String value, final boolean fireEvents) {
        textArea.setValue(value == null ? "" : value);
        if (!editing) {
            preview.setMarkdown(getValue());
        }
        if (fireEvents) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isEnabled() {
        return textArea.isEnabled();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        textArea.setEnabled(enabled);
    }

    @Override
    public void setId(final String id) {
        idMixin.setId(id);
    }

    @Override
    public String getId() {
        return idMixin.getId();
    }

}
