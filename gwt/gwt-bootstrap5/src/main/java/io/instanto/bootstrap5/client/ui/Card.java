/*
 * #%L
 * GWT Bootstrap
 * %%
 * Copyright (C) 2026 Carl Stainton
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

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.html.Div;

public class Card extends Div {

    private final Div body = new Div();

    public Card() {
        setStyleName("card shadow-sm");
        body.addStyleName("card-body");
        add(body);
    }

    public void setTitle(String title) {
        body.insert(new HTML("<h2 class=\"h5 card-title\">" + escape(title) + "</h2>"), 0);
    }

    public void addBody(HTML html) {
        body.add(html);
    }

    public void addBody(Widget widget) {
        body.add(widget);
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
