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

import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.extras.markdown.client.Markdown;

/**
 * Displays Markdown as rendered HTML.
 *
 * <p>For applications that store Markdown and want to show it without a round
 * trip to a server-side renderer. The output is sanitised; see
 * {@link Markdown}.</p>
 */
public class MarkdownPanel extends Div {

    private String markdown = "";

    public MarkdownPanel() {
        addStyleName("gbm-markdown");
    }

    public MarkdownPanel(final String markdown) {
        this();
        setMarkdown(markdown);
    }

    public void setMarkdown(final String markdown) {
        this.markdown = markdown == null ? "" : markdown;
        // The source meanwhile: an honest fallback, and what stays on screen if the
        // parser never arrives. On GWT it is replaced in the same turn, because the
        // module compiles the parser in and whenReady runs immediately.
        getElement().setInnerText(this.markdown);
        Markdown.whenReady(new Runnable() {
            @Override
            public void run() {
                getElement().setInnerHTML(Markdown.toHtml(MarkdownPanel.this.markdown));
            }
        });
    }

    /** The Markdown source, as given. */
    public String getMarkdown() {
        return markdown;
    }

    /** The rendered HTML currently displayed. */
    public String getHTML() {
        return getElement().getInnerHTML();
    }
}
