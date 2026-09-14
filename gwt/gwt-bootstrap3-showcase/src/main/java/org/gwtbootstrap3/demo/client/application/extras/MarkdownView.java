/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2015 GwtBootstrap3
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
package org.gwtbootstrap3.demo.client.application.extras;

import org.gwtbootstrap3.extras.markdown.client.ui.MarkdownEditor;
import org.gwtbootstrap3.extras.markdown.client.ui.MarkdownPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MarkdownView extends Composite {

    private static final String SAMPLE = "## Release notes\n\n"
            + "Stored as **Markdown**, exactly as typed.\n\n"
            + "- Rendered client side with the same dialect flexmark produces\n"
            + "- So the preview matches the server\n\n"
            + "- [x] task lists\n"
            + "- [ ] and tables\n\n"
            + "| Extra | Stores |\n| --- | --- |\n| Rich Text | HTML |\n| Markdown | Markdown |\n\n"
            + "> ~~Struck~~ text, `code`, and [links](https://commonmark.org).\n";

    interface Binder extends UiBinder<Widget, MarkdownView> {
    }


    private static final Binder BINDER = GWT.create(Binder.class);
    @UiField MarkdownEditor editor;

    @UiField MarkdownPanel rendered;

    public MarkdownView() {
        initWidget(BINDER.createAndBindUi(this));
        editor.setValue(SAMPLE);
        rendered.setMarkdown(SAMPLE);
        editor.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(final ValueChangeEvent<String> event) {
                rendered.setMarkdown(event.getValue());
            }
        });
    }
}
