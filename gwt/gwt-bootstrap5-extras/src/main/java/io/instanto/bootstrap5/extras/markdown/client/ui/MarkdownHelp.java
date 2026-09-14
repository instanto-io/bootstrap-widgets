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
package io.instanto.bootstrap5.extras.markdown.client.ui;

import io.instanto.bootstrap5.client.ui.Anchor;
import io.instanto.bootstrap5.client.ui.html.Div;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * A Markdown syntax reference, shown inline rather than linked off-site.
 *
 * <p>Replaces the "We support Markdown" link that sends an author to
 * commonmark.org and loses their draft. The same syntax, collapsed until asked
 * for, covering what a flexmark server with the GFM extensions accepts.</p>
 */
public class MarkdownHelp extends Div {

    private final Div body = new Div();

    private boolean expanded;

    public MarkdownHelp() {
        addStyleName("gbm-markdown-help");

        final Anchor toggle = new Anchor("");
        toggle.setText("Markdown supported");
        toggle.addStyleName("small text-body-secondary text-decoration-none");
        toggle.setHref("javascript:;");
        toggle.getElement().setAttribute("role", "button");
        toggle.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                setExpanded(!expanded);
            }
        });

        body.addStyleName("border rounded p-2 mt-2 small");
        body.getElement().setInnerHTML(reference());
        body.setVisible(false);

        add(toggle);
        add(body);
    }

    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
        body.setVisible(expanded);
    }

    public boolean isExpanded() {
        return expanded;
    }

    private static String reference() {
        final String id = Document.get().createUniqueId();
        return "<div class='table-responsive'><table class='table table-sm mb-0'>"
                + "<thead><tr><th scope='col'>You write</th><th scope='col'>You get</th></tr></thead>"
                + "<tbody id='" + id + "'>"
                + row("**bold**", "<strong>bold</strong>")
                + row("*italic*", "<em>italic</em>")
                + row("~~struck~~", "<del>struck</del>")
                + row("`code`", "<code>code</code>")
                + row("# Heading", "<strong>Heading</strong>")
                + row("&gt; quote", "<span class='text-body-secondary'>quote</span>")
                + row("- item", "a bulleted list")
                + row("1. item", "a numbered list")
                + row("- [ ] task", "a task list")
                + row("[text](https://example.com)", "<a href='javascript:;'>text</a>")
                + row("![alt](image.png)", "an image")
                + row("| a | b |", "a table")
                + "</tbody></table></div>";
    }

    private static String row(final String source, final String result) {
        return "<tr><td><code>" + source.replace("<", "&lt;") + "</code></td><td>" + result + "</td></tr>";
    }
}
