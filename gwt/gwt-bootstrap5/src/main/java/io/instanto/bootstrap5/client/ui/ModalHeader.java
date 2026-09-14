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

import io.instanto.bootstrap5.client.ui.base.HasDataSpy;
import io.instanto.bootstrap5.client.ui.base.mixin.DataSpyMixin;
import io.instanto.bootstrap5.client.ui.constants.Spy;


import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.HTML;

public class ModalHeader extends ElementPanel implements HasDataSpy {

    private final HTML titleWidget = new HTML();
    private final HTML closeButton = new HTML("<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");

    public ModalHeader() {
        super("div");
        addStyleName("modal-header");
        titleWidget.addStyleName("modal-title h5");
        titleWidget.getElement().setId(Document.get().createUniqueId());
        add(titleWidget);
    }

    public ModalHeader(String title) {
        this();
        setTitle(title);
        addCloseButton();
    }

    public void setTitle(String title) {
        titleWidget.setHTML(escape(title));
    }

    public String getTitleId() {
        return titleWidget.getElement().getId();
    }

    public void addCloseButton() {
        setClosable(true);
    }

    public void setClosable(boolean closable) {
        if (closable) {
            if (closeButton.getParent() == null) {
                add(closeButton);
            }
        } else {
            closeButton.removeFromParent();
        }
    }

    public boolean isClosable() {
        return closeButton.getParent() != null;
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    private final DataSpyMixin<ModalHeader> dataSpyMixin = new DataSpyMixin<ModalHeader>(this);

    @Override
    public void setDataSpy(final Spy spy) {
        dataSpyMixin.setDataSpy(spy);
    }

    @Override
    public Spy getDataSpy() {
        return dataSpyMixin.getDataSpy();
    }

}
