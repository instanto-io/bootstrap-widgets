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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasHTML;
import io.instanto.bootstrap5.client.ui.base.mixin.IdMixin;
import io.instanto.bootstrap5.client.ui.html.Div;
import io.instanto.bootstrap5.extras.base.client.PluginWidget;

/**
 * A rich text editor, backed by Quill 2.
 *
 * <p>This replaces the Summernote extra rather than porting it. Summernote is a jQuery plugin and
 * the only Bootstrap 3 extra with no jQuery-free counterpart; Quill has no dependencies, ships its
 * own toolbar, and is what the widget wraps here.
 *
 * <p>The value is HTML. {@link #getHTML()} returns the editor's semantic HTML, which is what you
 * would store; {@link #setHTML(String)} replaces the contents.
 */
public class RichTextEditor extends PluginWidget<QuillJs.Handle>
    implements HasHTML, HasEnabled, HasValueChangeHandlers<String> {

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
    if (plugin() != null) {
      QuillJs.applyPlaceholder(plugin().editor, this.placeholder);
    }
  }

  public String getPlaceholder() {
    return placeholder;
  }

  @Override
  protected void whenReady(Runnable action) {
    QuillJs.whenReady(action);
  }

  @Override
  protected QuillJs.Handle createPlugin() {
    return new QuillJs.Handle(QuillJs.create(surface.getElement(), toolbarSpec(), placeholder));
  }

  @Override
  protected void afterCreate() {
    QuillJs.Handle current = plugin();
    current.listener =
        QuillJs.bindChange(
            current.editor,
            () -> {
              if (plugin() == current && isAttached()) onTextChange();
            });
    if (pendingHtml != null) {
      QuillJs.writeHtml(current.editor, pendingHtml);
      pendingHtml = null;
    }
    QuillJs.applyEnabled(current.editor, enabled);
  }

  @Override
  protected void captureState() {
    pendingHtml = QuillJs.readHtml(plugin().editor);
  }

  @Override
  protected void destroyPlugin(QuillJs.Handle previous) {
    QuillJs.release(previous);
    if (plugin() == null) {
      surface.getElement().setInnerHTML("");
      surface.removeStyleName("ql-container");
      surface.removeStyleName("ql-snow");
    }
  }

  @Override
  public String getHTML() {
    return plugin() == null
        ? (pendingHtml == null ? "" : pendingHtml)
        : QuillJs.readHtml(plugin().editor);
  }

  @Override
  public void setHTML(final String html) {
    final String value = html == null ? "" : html;
    if (plugin() == null) {
      pendingHtml = value;
    } else {
      QuillJs.writeHtml(plugin().editor, value);
    }
  }

  @Override
  public String getText() {
    if (plugin() != null) return QuillJs.readText(plugin().editor);
    Element content = Document.get().createDivElement();
    content.setInnerHTML(pendingHtml == null ? "" : pendingHtml);
    return content.getInnerText();
  }

  @Override
  public void setText(final String text) {
    setHTML(
        text == null ? "" : text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
    if (plugin() != null) {
      QuillJs.applyEnabled(plugin().editor, enabled);
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
