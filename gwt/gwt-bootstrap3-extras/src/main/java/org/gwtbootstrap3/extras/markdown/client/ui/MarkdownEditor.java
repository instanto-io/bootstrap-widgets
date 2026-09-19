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

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.base.HasId;
import org.gwtbootstrap3.client.ui.base.mixin.IdMixin;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.extras.markdown.client.Markdown;

import com.google.gwt.dom.client.Element;
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
 * An editor supporting both Modern WYSIWYG (live rendered HTML editing where raw Markdown
 * is completely hidden) and Classic mode (raw Markdown text editing with preview).
 *
 * <p>The stored value is clean GitHub Flavoured Markdown. In WYSIWYG mode, content is
 * edited in a rich contenteditable area and converted to/from Markdown via Turndown and Marked.
 * In Classic mode, content is edited in a standard textarea with Markdown syntax buttons and preview.</p>
 */
public class MarkdownEditor extends Div implements HasEnabled, HasId, HasValue<String>,
        HasValueChangeHandlers<String> {

    public enum EditorMode {
        WYSIWYG,
        CLASSIC
    }

    public interface EditorModeChangeHandler {
        void onEditorModeChange(EditorMode mode);
    }

    private final IdMixin<MarkdownEditor> idMixin = new IdMixin<MarkdownEditor>(this);

    private final Div toolbar = new Div();

    private final TextArea textArea = new TextArea();

    private final Div visualEditor = new Div();

    private final MarkdownPanel preview = new MarkdownPanel();

    private final MarkdownHelp help = new MarkdownHelp();

    /** In Classic mode: toggles between writing Markdown and previewing rendered output. */
    private final Button modeButton = new Button();

    /** Switches between WYSIWYG and Classic editor modes. */
    private final Button editorModeButton = new Button();

    private final Div syntaxButtons;

    private EditorMode editorMode = EditorMode.WYSIWYG;

    private boolean editing = true;

    /** Whether the reference should show while editing in Classic mode; never shows in WYSIWYG. */
    private boolean helpVisible = false;

    private boolean showModeSwitcher = true;

    private final List<EditorModeChangeHandler> editorModeChangeHandlers = new ArrayList<EditorModeChangeHandler>();

    public MarkdownEditor() {
        this(EditorMode.WYSIWYG);
    }

    public MarkdownEditor(final EditorMode initialMode) {
        this.editorMode = initialMode != null ? initialMode : EditorMode.WYSIWYG;
        addStyleName("gbm-markdown-editor");

        // Toolbar
        toolbar.addStyleName("btn-toolbar");
        toolbar.getElement().getStyle().setProperty("display", "flex");
        toolbar.getElement().getStyle().setProperty("justifyContent", "space-between");
        toolbar.getElement().getStyle().setProperty("alignItems", "center");
        toolbar.getElement().getStyle().setProperty("marginBottom", "8px");

        final Div leftSection = new Div();
        leftSection.getElement().getStyle().setProperty("display", "inline-flex");
        leftSection.getElement().getStyle().setProperty("alignItems", "center");

        final Div modeGroup = new Div();
        modeGroup.addStyleName("btn-group");
        modeGroup.getElement().getStyle().setProperty("marginRight", "8px");
        modeGroup.add(modeToggle());
        leftSection.add(modeGroup);

        syntaxButtons = syntaxButtons();
        leftSection.add(syntaxButtons);
        toolbar.add(leftSection);

        final Div rightSection = new Div();
        rightSection.addStyleName("btn-group");
        rightSection.add(editorModeToggle());
        toolbar.add(rightSection);

        add(toolbar);

        // Visual Editor (WYSIWYG)
        visualEditor.addStyleName("form-control");
        visualEditor.addStyleName("gbm-markdown-visual-editor");
        visualEditor.getElement().setAttribute("contenteditable", "true");
        visualEditor.getElement().setAttribute("role", "textbox");
        visualEditor.getElement().setAttribute("aria-multiline", "true");
        visualEditor.getElement().getStyle().setProperty("minHeight", "180px");
        visualEditor.getElement().getStyle().setProperty("height", "auto");
        visualEditor.getElement().getStyle().setProperty("overflowY", "auto");
        initInputListener(visualEditor.getElement());
        add(visualEditor);

        // Classic Textarea
        textArea.setVisibleLines(8);
        textArea.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(final KeyUpEvent event) {
                ValueChangeEvent.fire(MarkdownEditor.this, getValue());
            }
        });
        add(textArea);

        // Preview Panel
        preview.addStyleName("well");
        preview.setVisible(false);
        add(preview);

        // Markdown Syntax Reference Help
        help.setVisible(false);
        add(help);

        applyEditorMode();
    }

    public MarkdownEditor(final String markdown) {
        this(EditorMode.WYSIWYG);
        setValue(markdown);
    }

    public MarkdownEditor(final String markdown, final EditorMode initialMode) {
        this(initialMode);
        setValue(markdown);
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

    private Button editorModeToggle() {
        editorModeButton.setSize(ButtonSize.SMALL);
        editorModeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                toggleEditorMode();
            }
        });
        return editorModeButton;
    }

    public EditorMode getEditorMode() {
        return editorMode;
    }

    public void setEditorMode(final EditorMode mode) {
        if (this.editorMode == mode) {
            return;
        }
        if (mode == EditorMode.WYSIWYG) {
            final String md = textArea.getText() != null ? textArea.getText() : textArea.getValue();
            final String html = Markdown.toHtml(md == null ? "" : md);
            visualEditor.getElement().setInnerHTML(html);
            this.editorMode = EditorMode.WYSIWYG;
        } else {
            final String html = visualEditor.getElement().getInnerHTML();
            final String md = Markdown.toMarkdown(html);
            textArea.setValue(md);
            this.editorMode = EditorMode.CLASSIC;
        }
        applyEditorMode();
        fireEditorModeChange(this.editorMode);
    }

    public void toggleEditorMode() {
        setEditorMode(this.editorMode == EditorMode.WYSIWYG ? EditorMode.CLASSIC : EditorMode.WYSIWYG);
    }

    public void setShowModeSwitcher(final boolean show) {
        this.showModeSwitcher = show;
        editorModeButton.setVisible(show);
    }

    public void addEditorModeChangeHandler(final EditorModeChangeHandler handler) {
        if (handler != null) {
            editorModeChangeHandlers.add(handler);
        }
    }

    private void fireEditorModeChange(final EditorMode mode) {
        for (final EditorModeChangeHandler handler : editorModeChangeHandlers) {
            handler.onEditorModeChange(mode);
        }
    }

    private void applyEditorMode() {
        editorModeButton.setVisible(showModeSwitcher);
        if (editorMode == EditorMode.WYSIWYG) {
            visualEditor.setVisible(true);
            textArea.setVisible(false);
            preview.setVisible(false);
            modeButton.setVisible(false);
            syntaxButtons.setVisible(true);
            help.setVisible(false);
            editorModeButton.setIcon(IconType.CODE);
            editorModeButton.setText("Classic");
            editorModeButton.setTitle("Switch to Classic Markdown mode");
        } else {
            visualEditor.setVisible(false);
            modeButton.setVisible(true);
            editorModeButton.setIcon(IconType.DESKTOP);
            editorModeButton.setText("WYSIWYG");
            editorModeButton.setTitle("Switch to Visual WYSIWYG mode");
            if (editing) {
                textArea.setVisible(true);
                preview.setVisible(false);
                syntaxButtons.setVisible(true);
                help.setVisible(helpVisible);
                setModeButton(IconType.EYE, "Preview rendered markdown");
            } else {
                preview.setMarkdown(getValue());
                textArea.setVisible(false);
                preview.setVisible(true);
                syntaxButtons.setVisible(false);
                help.setVisible(false);
                setModeButton(IconType.PENCIL, "Write / edit markdown");
            }
        }
    }

    private Div syntaxButtons() {
        final Div group = new Div();
        group.addStyleName("btn-group");
        group.add(syntaxButton(IconType.BOLD, "Bold", new Runnable() {
            @Override
            public void run() {
                execFormat("bold");
            }
        }, "**", "**", "bold text"));
        group.add(syntaxButton(IconType.ITALIC, "Italic", new Runnable() {
            @Override
            public void run() {
                execFormat("italic");
            }
        }, "*", "*", "italic text"));
        group.add(syntaxButton(IconType.STRIKETHROUGH, "Strikethrough", new Runnable() {
            @Override
            public void run() {
                execFormat("strikeThrough");
            }
        }, "~~", "~~", "struck text"));
        group.add(syntaxButton(IconType.HEADER, "Heading", new Runnable() {
            @Override
            public void run() {
                execFormatBlock("<h3>");
            }
        }, "\n### ", "", "Heading"));
        group.add(syntaxButton(IconType.CODE, "Code", new Runnable() {
            @Override
            public void run() {
                execCodeFormat(visualEditor.getElement());
            }
        }, "`", "`", "code"));
        group.add(syntaxButton(IconType.LINK, "Link", new Runnable() {
            @Override
            public void run() {
                execLinkFormat(visualEditor.getElement());
            }
        }, "[", "](https://)", "text"));
        group.add(syntaxButton(IconType.LIST_UL, "Bulleted list", new Runnable() {
            @Override
            public void run() {
                execFormat("insertUnorderedList");
            }
        }, "\n- ", "", "item"));
        group.add(syntaxButton(IconType.LIST_OL, "Numbered list", new Runnable() {
            @Override
            public void run() {
                execFormat("insertOrderedList");
            }
        }, "\n1. ", "", "item"));
        group.add(syntaxButton(IconType.CHECK_SQUARE_O, "Task list", new Runnable() {
            @Override
            public void run() {
                insertTaskList(visualEditor.getElement());
            }
        }, "\n- [ ] ", "", "task"));
        group.add(syntaxButton(IconType.TABLE, "Table", new Runnable() {
            @Override
            public void run() {
                insertTable(visualEditor.getElement());
            }
        }, "\n| Column | Column |\n| --- | --- |\n| ", " |  |\n", "cell"));
        group.add(syntaxButton(IconType.QUOTE_LEFT, "Quote", new Runnable() {
            @Override
            public void run() {
                execFormatBlock("<blockquote>");
            }
        }, "\n> ", "", "quote"));
        return group;
    }

    private Button syntaxButton(final IconType icon, final String title, final Runnable wysiwygAction,
            final String before, final String after, final String placeholder) {
        final Button button = new Button("");
        button.setSize(ButtonSize.SMALL);
        button.setIcon(icon);
        button.setTitle(title);
        preventMouseDownBlur(button.getElement());
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (editorMode == EditorMode.WYSIWYG) {
                    if (wysiwygAction != null) {
                        wysiwygAction.run();
                        ValueChangeEvent.fire(MarkdownEditor.this, getValue());
                    }
                } else {
                    wrapSelection(before, after, placeholder);
                }
            }
        });
        return button;
    }

    /** Wraps the selection in Classic mode, or inserts a placeholder when nothing is selected. */
    private void wrapSelection(final String before, final String after, final String placeholder) {
        final TextAreaElement element = textArea.getElement().cast();
        final int start = TextAreaSelection.start(element);
        final int end = TextAreaSelection.end(element);
        final String value = getValue();
        final String selected = start == end ? placeholder : value.substring(start, end);
        setValue(value.substring(0, start) + before + selected + after + value.substring(end), true);
        TextAreaSelection.focusAndSelect(element, start + before.length(), selected.length());
    }

    /** Shows the editor for typing. */
    public void showEditor() {
        editing = true;
        applyEditorMode();
    }

    /** Shows the rendered Markdown in Classic mode. */
    public void showRendered() {
        editing = false;
        applyEditorMode();
    }

    /** True while the active input (visual editor or textarea) is showing. */
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

    /** Shows or hides the syntax reference beneath the editor while editing in Classic mode. */
    public void setHelpVisible(final boolean visible) {
        helpVisible = visible;
        help.setVisible(visible && editing && editorMode == EditorMode.CLASSIC);
    }

    public boolean isHelpVisible() {
        return helpVisible;
    }

    public void setVisibleLines(final int lines) {
        textArea.setVisibleLines(lines);
        final int px = Math.max(120, lines * 22);
        visualEditor.getElement().getStyle().setProperty("minHeight", px + "px");
    }

    public void setPlaceholder(final String placeholder) {
        textArea.setPlaceholder(placeholder);
        if (placeholder != null && !placeholder.isEmpty()) {
            visualEditor.getElement().setAttribute("data-placeholder", placeholder);
        } else {
            visualEditor.getElement().removeAttribute("data-placeholder");
        }
    }

    /** The Markdown rendered as sanitised HTML. */
    public String getHTML() {
        return Markdown.toHtml(getValue());
    }

    @Override
    public String getValue() {
        if (editorMode == EditorMode.WYSIWYG) {
            final String html = visualEditor.getElement().getInnerHTML();
            return Markdown.toMarkdown(html);
        } else {
            final String value = textArea.getText();
            return value == null ? "" : value;
        }
    }

    @Override
    public void setValue(final String value) {
        setValue(value, false);
    }

    @Override
    public void setValue(final String value, final boolean fireEvents) {
        final String md = value == null ? "" : value;
        textArea.setValue(md);
        final String html = Markdown.toHtml(md);
        visualEditor.getElement().setInnerHTML(html);
        if (editorMode == EditorMode.CLASSIC && !editing) {
            preview.setMarkdown(md);
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
        visualEditor.getElement().setAttribute("contenteditable", enabled ? "true" : "false");
        modeButton.setEnabled(enabled);
        editorModeButton.setEnabled(enabled);
    }

    @Override
    public void setId(final String id) {
        idMixin.setId(id);
    }

    @Override
    public String getId() {
        return idMixin.getId();
    }

    private void onVisualEditorInput() {
        ValueChangeEvent.fire(MarkdownEditor.this, getValue());
    }

    private static native void preventMouseDownBlur(Element el) /*-{
        el.addEventListener('mousedown', function(e) {
            e.preventDefault();
        });
    }-*/;

    private static native void execFormat(String command) /*-{
        $doc.execCommand(command, false, null);
    }-*/;

    private static native void execFormatBlock(String tag) /*-{
        $doc.execCommand('formatBlock', false, tag);
    }-*/;

    private static native void execLinkFormat(Element visualEditorEl) /*-{
        var url = $wnd.prompt("Enter link URL:", "https://");
        if (url && url.trim().length > 0) {
            $doc.execCommand("createLink", false, url.trim());
        }
    }-*/;

    private static native void execCodeFormat(Element visualEditorEl) /*-{
        var sel = $wnd.getSelection();
        if (!sel || !sel.rangeCount) return;
        var range = sel.getRangeAt(0);
        var parent = sel.anchorNode ? (sel.anchorNode.nodeType === 3 ? sel.anchorNode.parentNode : sel.anchorNode) : null;
        if (parent && parent.closest && parent.closest('code')) {
            var codeEl = parent.closest('code');
            var text = codeEl.textContent;
            var tn = $doc.createTextNode(text);
            codeEl.parentNode.replaceChild(tn, codeEl);
            return;
        }
        var selectedText = range.toString();
        if (!selectedText) {
            var codeEl = $doc.createElement('code');
            codeEl.textContent = 'code';
            range.insertNode(codeEl);
            range.selectNodeContents(codeEl);
            sel.removeAllRanges();
            sel.addRange(range);
        } else {
            var codeEl = $doc.createElement('code');
            codeEl.textContent = selectedText;
            range.deleteContents();
            range.insertNode(codeEl);
        }
    }-*/;

    private static native void insertTaskList(Element visualEditorEl) /*-{
        var html = '<ul class="task-list" style="list-style-type: none; padding-left: 0;"><li class="task-list-item"><input type="checkbox"> Task item</li></ul>';
        if (!$doc.execCommand('insertHTML', false, html)) {
            var sel = $wnd.getSelection();
            if (sel && sel.rangeCount) {
                var range = sel.getRangeAt(0);
                range.deleteContents();
                var div = $doc.createElement('div');
                div.innerHTML = html;
                var frag = $doc.createDocumentFragment(), node;
                while ((node = div.firstChild)) {
                    frag.appendChild(node);
                }
                range.insertNode(frag);
            }
        }
    }-*/;

    private static native void insertTable(Element visualEditorEl) /*-{
        var html = '<table class="table table-striped table-condensed table-bordered"><thead><tr><th>Header 1</th><th>Header 2</th></tr></thead><tbody><tr><td>Cell 1</td><td>Cell 2</td></tr></tbody></table><p><br></p>';
        if (!$doc.execCommand('insertHTML', false, html)) {
            var sel = $wnd.getSelection();
            if (sel && sel.rangeCount) {
                var range = sel.getRangeAt(0);
                range.deleteContents();
                var div = $doc.createElement('div');
                div.innerHTML = html;
                var frag = $doc.createDocumentFragment(), node;
                while ((node = div.firstChild)) {
                    frag.appendChild(node);
                }
                range.insertNode(frag);
            }
        }
    }-*/;

    private native void initInputListener(Element el) /*-{
        var self = this;
        el.addEventListener('input', function() {
            self.@org.gwtbootstrap3.extras.markdown.client.ui.MarkdownEditor::onVisualEditorInput()();
        });
    }-*/;
}

