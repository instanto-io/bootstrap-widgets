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
package io.instanto.bootstrap5.extras.richtext.client.ui;

import io.instanto.bootstrap5.client.ui.base.HasId;
import io.instanto.bootstrap5.client.ui.base.mixin.IdMixin;
import io.instanto.bootstrap5.client.ui.html.Div;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasHTML;

/**
 * A rich text editor, backed by Quill 2.
 *
 * <p>This replaces the Summernote extra rather than porting it. Summernote is a
 * jQuery plugin and the only Bootstrap 3 extra with no jQuery-free counterpart;
 * Quill has no dependencies, ships its own toolbar, and is what the widget wraps
 * here.</p>
 *
 * <p>The value is HTML. {@link #getHTML()} returns the editor's semantic HTML,
 * which is what you would store; {@link #setHTML(String)} replaces the
 * contents.</p>
 */
public class RichTextEditor extends Div implements HasHTML, HasEnabled, HasId,
        HasValueChangeHandlers<String> {

    /** The toolbars Quill ships with. */
    public enum Toolbar {
        /** Bold, italic, underline, strike, lists and a link. */
        BASIC,
        /** Adds headings, colours, alignment, quotes, code and clearing. */
        FULL,
        /** No toolbar; the editor is plain but still rich. */
        NONE
    }

    private final IdMixin<RichTextEditor> idMixin = new IdMixin<RichTextEditor>(this);

    private final Div surface = new Div();

    private Toolbar toolbar = Toolbar.BASIC;

    private String placeholder = "";

    private String pendingHtml;

    private boolean enabled = true;

    private JavaScriptObject quill;

    public RichTextEditor() {
        addStyleName("gbm-richtext");
        add(surface);
    }

    public RichTextEditor(final Toolbar toolbar) {
        this();
        setToolbar(toolbar);
    }

    /** Must be set before the editor is attached. */
    public void setToolbar(final Toolbar toolbar) {
        this.toolbar = toolbar == null ? Toolbar.BASIC : toolbar;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setPlaceholder(final String placeholder) {
        this.placeholder = placeholder == null ? "" : placeholder;
        if (quill != null) {
            QuillJs.applyPlaceholder(quill, this.placeholder);
        }
    }

    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        initialise();
    }

    /**
     * Builds the editor, waiting for Quill if it has not arrived yet. The GWT module
     * injects Quill as inline script text before the application runs, so the first
     * attempt succeeds there; the TeaVM backend fetches it by URL, and an editor attached
     * during startup would otherwise stay an empty div.
     */
    /**
     * Builds the widget once Quill is usable.
     *
     * <p>Which backend this is no longer matters here. GWT compiles the library into the
     * module, so the action runs immediately; TeaVM fetches it and runs the action when
     * it arrives. Either way this asks the module instead of polling for a global to
     * appear, and a module that cannot load reports it rather than letting the widget
     * wait out a timeout and give up in silence.</p>
     */
    private void initialise() {
        QuillJs.whenReady(new Runnable() {
            @Override
            public void run() {
                if (isAttached()) {
                    build();
                }
            }
        });
    }

    private void build() {
        quill = QuillJs.create(surface.getElement(), toolbarSpec(), placeholder);
        QuillJs.bindChange(quill, new QuillJs.ChangeHandler() {
            @Override
            public void onTextChange() {
                RichTextEditor.this.onTextChange();
            }
        });

        if (pendingHtml != null) {
            QuillJs.writeHtml(quill, pendingHtml);
            pendingHtml = null;
        }
        if (!enabled) {
            setEnabled(false);
        }
    }

    @Override
    protected void onUnload() {
        quill = null;
        super.onUnload();
    }

    @Override
    public String getHTML() {
        return quill == null ? (pendingHtml == null ? "" : pendingHtml) : QuillJs.readHtml(quill);
    }

    @Override
    public void setHTML(final String html) {
        final String value = html == null ? "" : html;
        if (quill == null) {
            pendingHtml = value;
        } else {
            QuillJs.writeHtml(quill, value);
        }
    }

    @Override
    public String getText() {
        return quill == null ? "" : QuillJs.readText(quill);
    }

    @Override
    public void setText(final String text) {
        setHTML(text == null ? "" : text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        if (quill != null) {
            QuillJs.applyEnabled(quill, enabled);
        }
    }

    @Override
    public void setId(final String id) {
        idMixin.setId(id);
    }

    @Override
    public String getId() {
        return idMixin.getId();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /** Called from the Quill text-change subscription. */
    void onTextChange() {
        ValueChangeEvent.fire(this, getHTML());
    }

    private String toolbarSpec() {
        switch (toolbar) {
            case NONE:
                return "none";
            case FULL:
                return "full";
            case BASIC:
            default:
                return "basic";
        }
    }
}
