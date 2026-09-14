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

import io.instanto.bootstrap5.client.ui.base.HasHref;
import io.instanto.bootstrap5.client.ui.base.HasTargetHistoryToken;
import io.instanto.bootstrap5.client.ui.base.button.AbstractToggleButton;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

/**
 * An anchor styled and behaving as a button.
 *
 * <p>Extends {@link AbstractToggleButton}, so it has the whole button surface:
 * {@code setActive}, {@code toggle()}, {@code state()} with its loading text,
 * sizes, block layout, the mouse handlers and {@code setDataToggle}. Rendering
 * it as an anchor rather than a button is the only difference from
 * {@link Button}.</p>
 */
public class AnchorButton extends AbstractToggleButton implements HasHref, HasTargetHistoryToken {

    private static final String EMPTY_HREF = "#";

    private static final HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

    private String targetHistoryToken;

    public AnchorButton() {
        this(ButtonType.DEFAULT);
    }

    public AnchorButton(final ButtonType type) {
        super(type);
        setHref(EMPTY_HREF);
        sinkEvents(Event.ONCLICK);
    }

    public AnchorButton(final String text) {
        this(text, EMPTY_HREF, ButtonType.PRIMARY);
    }

    public AnchorButton(final String text, final String href) {
        this(text, href, ButtonType.PRIMARY);
    }

    public AnchorButton(final String text, final String href, final ButtonType type) {
        this(type);
        setText(text);
        setHref(href);
    }

    public AnchorButton(final String text, final String href, final Variant variant) {
        this(text, href, ButtonType.DEFAULT);
        setVariant(variant);
    }

    @Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        if (getTargetHistoryToken() != null
                && DOM.eventGetType(event) == Event.ONCLICK && impl.handleAsClick(event)) {
            History.newItem(getTargetHistoryToken());
            event.preventDefault();
        }
    }

    @Override
    public void setTargetHistoryToken(final String targetHistoryToken) {
        this.targetHistoryToken = targetHistoryToken;
        if (targetHistoryToken != null) {
            getAnchorElement().setHref("#" + History.encodeHistoryToken(targetHistoryToken));
        }
    }

    @Override
    public String getTargetHistoryToken() {
        return targetHistoryToken;
    }

    @Override
    public void setHref(final String href) {
        this.targetHistoryToken = null;
        getAnchorElement().setHref(href == null ? EMPTY_HREF : href);
    }

    @Override
    public String getHref() {
        return getAnchorElement().getHref();
    }

    /** Kept for source compatibility with the Anchor-based AnchorButton. */
    public void setButtonType(final ButtonType type) {
        setType(type);
    }

    /** Kept for source compatibility with the Anchor-based AnchorButton. */
    public void setButtonVariant(final Variant variant) {
        setVariant(variant);
    }

    @Override
    protected Element createElement() {
        return Document.get().createAnchorElement();
    }

    private AnchorElement getAnchorElement() {
        return AnchorElement.as(getElement());
    }
}
