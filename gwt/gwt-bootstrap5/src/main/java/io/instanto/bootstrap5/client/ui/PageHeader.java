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

import io.instanto.bootstrap5.client.ui.base.HasSubText;
import io.instanto.bootstrap5.client.ui.html.Small;

public class PageHeader extends ElementPanel implements HasSubText {

    public PageHeader() {
        super("div");
        setStyleName("pb-2 mt-4 mb-4 border-bottom");
    }

    private final Small subText = new Small();

    /**
     * Bootstrap 3 styled heading subtext with .small; Bootstrap 5 needs the
     * muted colour spelled out, so the element also carries
     * .text-body-secondary.
     */
    @Override
    public void setSubText(final String subText) {
        this.subText.setText(" " + (subText == null ? "" : subText));
        this.subText.addStyleName("text-body-secondary");
        add(this.subText);
    }

    @Override
    public String getSubText() {
        return subText.getText();
    }

}
