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

import jsinterop.annotations.JsFunction;
import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.base.HasId;
import org.gwtbootstrap3.client.ui.base.mixin.IdMixin;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
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

    /** In Classic mode: segmented Write / Preview controls. */
    private final Div modeGroup = new Div();
    private final Button writeButton = new Button("Write");
    private final Button previewButton = new Button("Preview");

    /** Segmented control for switching between Rich Text (WYSIWYG) and Markdown (Classic). */
    private final Div modeSwitcherGroup = new Div();
    private final Button wysiwygButton = new Button("Rich Text");
    private final Button classicButton = new Button("Markdown");

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
        super.getElement().addClassName("gbm-markdown-editor");

        // Toolbar
        toolbar.addStyleName("btn-toolbar");
        toolbar.getElement().getStyle().setProperty("display", "flex");
        toolbar.getElement().getStyle().setProperty("justifyContent", "space-between");
        toolbar.getElement().getStyle().setProperty("alignItems", "center");
        toolbar.getElement().getStyle().setProperty("flexWrap", "wrap");
        toolbar.getElement().getStyle().setProperty("gap", "6px");
        toolbar.getElement().getStyle().setProperty("marginBottom", "8px");

        final Div leftSection = new Div();
        leftSection.getElement().getStyle().setProperty("display", "inline-flex");
        leftSection.getElement().getStyle().setProperty("alignItems", "center");
        leftSection.getElement().getStyle().setProperty("flexWrap", "wrap");
        leftSection.getElement().getStyle().setProperty("gap", "6px");

        // Classic mode Write / Preview segmented controls
        modeGroup.addStyleName("btn-group");
        modeGroup.addStyleName("gbm-markdown-tabs");
        modeGroup.getElement().getStyle().setProperty("marginRight", "8px");

        writeButton.setSize(ButtonSize.SMALL);
        writeButton.setIcon(IconType.PENCIL);
        writeButton.setTitle("Edit Markdown source");
        writeButton.addStyleName("gbm-tab-btn");
        writeButton.addStyleName("gbm-tab-write");
        preventMouseDownBlur(writeButton.getElement());
        writeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                showEditor();
            }
        });
        modeGroup.add(writeButton);

        previewButton.setSize(ButtonSize.SMALL);
        previewButton.setIcon(IconType.EYE);
        previewButton.setTitle("Preview rendered Markdown");
        previewButton.addStyleName("gbm-tab-btn");
        previewButton.addStyleName("gbm-tab-preview");
        preventMouseDownBlur(previewButton.getElement());
        previewButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                showRendered();
            }
        });
        modeGroup.add(previewButton);

        leftSection.add(modeGroup);

        syntaxButtons = syntaxButtons();
        leftSection.add(syntaxButtons);
        toolbar.add(leftSection);

        // Right section: Rich Text vs Markdown segmented mode switcher
        modeSwitcherGroup.addStyleName("btn-group");
        modeSwitcherGroup.addStyleName("gbm-editor-mode-toggle");

        wysiwygButton.setSize(ButtonSize.SMALL);
        wysiwygButton.setIcon(IconType.PENCIL);
        wysiwygButton.setTitle("Switch to Rich Text (WYSIWYG) mode");
        wysiwygButton.addStyleName("gbm-mode-btn");
        wysiwygButton.addStyleName("gbm-mode-wysiwyg");
        preventMouseDownBlur(wysiwygButton.getElement());
        wysiwygButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                setEditorMode(EditorMode.WYSIWYG);
            }
        });
        modeSwitcherGroup.add(wysiwygButton);

        classicButton.setSize(ButtonSize.SMALL);
        classicButton.setIcon(IconType.CODE);
        classicButton.setTitle("Switch to Markdown source mode");
        classicButton.addStyleName("gbm-mode-btn");
        classicButton.addStyleName("gbm-mode-classic");
        preventMouseDownBlur(classicButton.getElement());
        classicButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                setEditorMode(EditorMode.CLASSIC);
            }
        });
        modeSwitcherGroup.add(classicButton);

        toolbar.add(modeSwitcherGroup);

        addInitialChild(toolbar);

        // Visual Editor (WYSIWYG)
        visualEditor.addStyleName("form-control");
        visualEditor.addStyleName("gbm-markdown-visual-editor");
        visualEditor.getElement().setAttribute("contenteditable", "true");
        visualEditor.getElement().setAttribute("role", "textbox");
        visualEditor.getElement().setAttribute("aria-multiline", "true");
        visualEditor.getElement().getStyle().setProperty("minHeight", "180px");
        visualEditor.getElement().getStyle().setProperty("height", "auto");
        visualEditor.getElement().getStyle().setProperty("overflowY", "auto");
        addInitialChild(visualEditor);

        // Classic Textarea
        textArea.setVisibleLines(8);
        textArea.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(final KeyUpEvent event) {
                ValueChangeEvent.fire(MarkdownEditor.this, getValue());
            }
        });
        addInitialChild(textArea);

        // Preview Panel
        preview.addStyleName("well");
        preview.setVisible(false);
        addInitialChild(preview);

        // Markdown Syntax Reference Help
        help.setVisible(false);
        addInitialChild(help);

        applyEditorMode("");
    }

    public MarkdownEditor(final String markdown) {
        this(EditorMode.WYSIWYG);
        applyValue(markdown);
    }

    public MarkdownEditor(final String markdown, final EditorMode initialMode) {
        this(initialMode);
        applyValue(markdown);
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
            this.editing = true;
        }
        applyEditorMode();
        fireEditorModeChange(this.editorMode);
    }

    public void toggleEditorMode() {
        setEditorMode(this.editorMode == EditorMode.WYSIWYG ? EditorMode.CLASSIC : EditorMode.WYSIWYG);
    }

    public void setShowModeSwitcher(final boolean show) {
        this.showModeSwitcher = show;
        modeSwitcherGroup.setVisible(show);
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
        applyEditorMode(editorMode == EditorMode.CLASSIC && !editing ? getValue() : "");
    }

    private void applyEditorMode(String previewValue) {
        modeSwitcherGroup.setVisible(showModeSwitcher);
        if (editorMode == EditorMode.WYSIWYG) {
            visualEditor.setVisible(true);
            textArea.setVisible(false);
            preview.setVisible(false);
            modeGroup.setVisible(false);
            syntaxButtons.setVisible(true);
            help.setVisible(false);

            wysiwygButton.getElement().setAttribute("aria-pressed", "true");
            classicButton.getElement().setAttribute("aria-pressed", "false");
            wysiwygButton.setType(ButtonType.PRIMARY);
            wysiwygButton.addStyleName("active");
            classicButton.setType(ButtonType.DEFAULT);
            classicButton.removeStyleName("active");
            refreshToolbarState(visualEditor.getElement());
        } else {
            clearToolbarState(syntaxButtons.getElement());
            visualEditor.setVisible(false);
            modeGroup.setVisible(true);

            wysiwygButton.setType(ButtonType.DEFAULT);
            wysiwygButton.removeStyleName("active");
            wysiwygButton.getElement().setAttribute("aria-pressed", "false");
            classicButton.getElement().setAttribute("aria-pressed", "true");
            classicButton.setType(ButtonType.PRIMARY);
            classicButton.addStyleName("active");

            if (editing) {
                textArea.setVisible(true);
                preview.setVisible(false);
                syntaxButtons.setVisible(true);
                help.setVisible(helpVisible);

                writeButton.getElement().setAttribute("aria-pressed", "true");
                previewButton.getElement().setAttribute("aria-pressed", "false");
                writeButton.setType(ButtonType.PRIMARY);
                writeButton.addStyleName("active");
                previewButton.setType(ButtonType.DEFAULT);
                previewButton.removeStyleName("active");
            } else {
                preview.setMarkdown(previewValue);
                textArea.setVisible(false);
                preview.setVisible(true);
                syntaxButtons.setVisible(false);
                help.setVisible(false);

                writeButton.setType(ButtonType.DEFAULT);
                writeButton.removeStyleName("active");
                writeButton.getElement().setAttribute("aria-pressed", "false");
                previewButton.getElement().setAttribute("aria-pressed", "true");
                previewButton.setType(ButtonType.PRIMARY);
                previewButton.addStyleName("active");
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
        }, "**", "**", "bold text", "command:bold"));
        group.add(syntaxButton(IconType.ITALIC, "Italic", new Runnable() {
            @Override
            public void run() {
                execFormat("italic");
            }
        }, "*", "*", "italic text", "command:italic"));
        group.add(syntaxButton(IconType.STRIKETHROUGH, "Strikethrough", new Runnable() {
            @Override
            public void run() {
                execFormat("strikeThrough");
            }
        }, "~~", "~~", "struck text", "command:strikeThrough"));
        group.add(syntaxButton(IconType.HEADER, "Heading", new Runnable() {
            @Override
            public void run() {
                execFormatBlock("<h3>");
            }
        }, "\n### ", "", "Heading", "block:h3"));
        group.add(syntaxButton(IconType.CODE, "Code", new Runnable() {
            @Override
            public void run() {
                execCodeFormat(visualEditor.getElement());
            }
        }, "`", "`", "code", "ancestor:code"));
        group.add(syntaxButton(IconType.LINK, "Link", new Runnable() {
            @Override
            public void run() {
                execLinkFormat(visualEditor.getElement());
            }
        }, "[", "](https://)", "text", "ancestor:a"));
        group.add(syntaxButton(IconType.LIST_UL, "Bulleted list", new Runnable() {
            @Override
            public void run() {
                execFormat("insertUnorderedList");
            }
        }, "\n- ", "", "item", "command:insertUnorderedList"));
        group.add(syntaxButton(IconType.LIST_OL, "Numbered list", new Runnable() {
            @Override
            public void run() {
                execFormat("insertOrderedList");
            }
        }, "\n1. ", "", "item", "command:insertOrderedList"));
        group.add(syntaxButton(IconType.CHECK_SQUARE_O, "Task list", new Runnable() {
            @Override
            public void run() {
                insertTaskList(visualEditor.getElement());
            }
        }, "\n- [ ] ", "", "task", "ancestor:ul.task-list"));
        group.add(syntaxButton(IconType.TABLE, "Table", new Runnable() {
            @Override
            public void run() {
                insertTable(visualEditor.getElement());
            }
        }, "\n| Column | Column |\n| --- | --- |\n| ", " |  |\n", "cell", "ancestor:table"));
        group.add(syntaxButton(IconType.QUOTE_LEFT, "Quote", new Runnable() {
            @Override
            public void run() {
                execFormatBlock("<blockquote>");
            }
        }, "\n> ", "", "quote", "block:blockquote"));
        return group;
    }

    private Button syntaxButton(final IconType icon, final String title, final Runnable wysiwygAction,
            final String before, final String after, final String placeholder, final String stateQuery) {
        final Button button = new Button("");
        button.setSize(ButtonSize.SMALL);
        button.setIcon(icon);
        button.setTitle(title);
        button.addStyleName("gbm-format-btn");
        button.getElement().setAttribute("aria-pressed", "false");
        button.getElement().setAttribute("data-gbm-format-state", stateQuery);
        preventMouseDownBlur(button.getElement());
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (editorMode == EditorMode.WYSIWYG) {
                    if (wysiwygAction != null) {
                        wysiwygAction.run();
                        refreshToolbarState(visualEditor.getElement());
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
        applyValue(value);
        if (fireEvents) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    private void applyValue(final String value) {
        final String md = value == null ? "" : value;
        textArea.setValue(md);
        final String html = Markdown.toHtml(md);
        visualEditor.getElement().setInnerHTML(html);
        if (editorMode == EditorMode.CLASSIC && !editing) {
            preview.setMarkdown(md);
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
        writeButton.setEnabled(enabled);
        previewButton.setEnabled(enabled);
        wysiwygButton.setEnabled(enabled);
        classicButton.setEnabled(enabled);
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

    /** Notified on every input in the visual editor. */
    @JsFunction
    interface InputListener {
        void onInput();
    }

    private InputListener inputListener;

    @Override
    protected void onLoad() {
        super.onLoad();
        inputListener = this::onVisualEditorInput;
        initInputListener(visualEditor.getElement(), inputListener);
        initToolbarStateTracking(visualEditor.getElement(), syntaxButtons.getElement());
    }

    @Override
    protected void onUnload() {
        removeToolbarStateTracking(visualEditor.getElement());
        if (inputListener != null) {
            removeInputListener(visualEditor.getElement(), inputListener);
            inputListener = null;
        }
        super.onUnload();
    }

    private static native void initInputListener(Element el, InputListener listener) /*-{
        el.addEventListener('input', listener);
    }-*/;

    private static native void initToolbarStateTracking(Element editor, Element toolbar) /*-{
        var refresh = function() {
            var buttons = toolbar.querySelectorAll('[data-gbm-format-state]');
            var selection = $wnd.getSelection ? $wnd.getSelection() : null;
            var node = selection && selection.anchorNode ? selection.anchorNode : null;
            if (node && node.nodeType === 3) {
                node = node.parentNode;
            }
            var insideEditor = !!node && (node === editor || editor.contains(node));

            for (var i = 0; i < buttons.length; i++) {
                var button = buttons[i];
                var query = button.getAttribute('data-gbm-format-state') || '';
                var active = false;

                if (insideEditor && query.indexOf('command:') === 0) {
                    try {
                        active = !!$doc.queryCommandState(query.substring(8));
                    } catch (ignored) {
                        active = false;
                    }
                } else if (insideEditor && query.indexOf('block:') === 0) {
                    try {
                        var block = String($doc.queryCommandValue('formatBlock') || '').toLowerCase();
                        block = block.replace(/[<>]/g, '');
                        active = block === query.substring(6).toLowerCase();
                    } catch (ignored) {
                        active = false;
                    }
                } else if (insideEditor && query.indexOf('ancestor:') === 0 && node.closest) {
                    var ancestor = node.closest(query.substring(9));
                    active = !!ancestor && editor.contains(ancestor);
                }

                if (active) {
                    button.classList.add('active');
                } else {
                    button.classList.remove('active');
                }
                button.setAttribute('aria-pressed', active ? 'true' : 'false');
            }
        };

        editor.__gbmRefreshToolbarState = refresh;
        var events = ['keyup', 'mouseup', 'input', 'focus'];
        for (var i = 0; i < events.length; i++) {
            editor.addEventListener(events[i], refresh);
        }
        refresh();
    }-*/;

    private static native void removeToolbarStateTracking(Element editor) /*-{
        var refresh = editor.__gbmRefreshToolbarState;
        var events = ['keyup', 'mouseup', 'input', 'focus'];
        for (var i = 0; i < events.length; i++) {
            editor.removeEventListener(events[i], refresh);
        }
        if (editor.__gbmToolbarFrame) $wnd.cancelAnimationFrame(editor.__gbmToolbarFrame);
        if (editor.__gbmToolbarTimer) $wnd.clearTimeout(editor.__gbmToolbarTimer);
        editor.__gbmToolbarFrame = null;
        editor.__gbmToolbarTimer = null;
        editor.__gbmRefreshToolbarState = null;
    }-*/;

    private static native void refreshToolbarState(Element editor) /*-{
        if (editor.__gbmRefreshToolbarState) {
            editor.__gbmRefreshToolbarState();
            if ($wnd.requestAnimationFrame) {
                if (editor.__gbmToolbarFrame) $wnd.cancelAnimationFrame(editor.__gbmToolbarFrame);
                editor.__gbmToolbarFrame = $wnd.requestAnimationFrame(editor.__gbmRefreshToolbarState);
            } else {
                if (editor.__gbmToolbarTimer) $wnd.clearTimeout(editor.__gbmToolbarTimer);
                editor.__gbmToolbarTimer = $wnd.setTimeout(editor.__gbmRefreshToolbarState, 0);
            }
        }
    }-*/;

    private static native void clearToolbarState(Element toolbar) /*-{
        var buttons = toolbar.querySelectorAll('[data-gbm-format-state]');
        for (var i = 0; i < buttons.length; i++) {
            buttons[i].classList.remove('active');
            buttons[i].setAttribute('aria-pressed', 'false');
        }
    }-*/;

    private static native void removeInputListener(Element el, InputListener listener) /*-{
        el.removeEventListener('input', listener);
    }-*/;
}
