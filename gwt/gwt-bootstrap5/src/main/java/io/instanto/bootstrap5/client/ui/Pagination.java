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

import io.instanto.bootstrap5.client.ui.constants.Styles;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.base.HasPaginationSize;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.PaginationSize;

public class Pagination extends ElementPanel implements HasPaginationSize {

    public Pagination() {
        super("ul");
        addStyleName("pagination");
    }

    public void setLarge(boolean large) {
        setStyleName("pagination-lg", large);
    }

    public void setSmall(boolean small) {
        setStyleName("pagination-sm", small);
    }

    @Override
    public void setPaginationSize(PaginationSize paginationSize) {
        StyleHelper.addUniqueEnumStyleName(this, PaginationSize.class, paginationSize == null ? PaginationSize.NONE : paginationSize);
    }

    @Override
    public PaginationSize getPaginationSize() {
        return PaginationSize.fromStyleName(getStyleName());
    }

    @Override
    public void add(Widget child) {
        decoratePageItem(child);
        super.add(child);
    }

    public AnchorListItem addPreviousLink() {
        AnchorListItem listItem = pageListItem("«");
        insert(listItem, 0);
        return listItem;
    }

    public AnchorListItem addNextLink() {
        AnchorListItem listItem = pageListItem("»");
        add(listItem);
        return listItem;
    }

    public void rebuild(final SimplePager pager) {
        clear();

        if (pager.getPageCount() == 0) {
            return;
        }

        final AnchorListItem previous = addPreviousLink();
        previous.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                pager.previousPage();
                updatePaginationState(pager);
            }
        });
        previous.setEnabled(pager.hasPreviousPage());

        for (int i = 0; i < pager.getPageCount(); i++) {
            final int pageIndex = i;
            final AnchorListItem page = pageListItem(String.valueOf(i + 1));
            page.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    pager.setPage(pageIndex);
                    updatePaginationState(pager);
                }
            });
            page.setActive(i == pager.getPage());
            add(page);
        }

        final AnchorListItem next = addNextLink();
        next.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                pager.nextPage();
                updatePaginationState(pager);
            }
        });
        next.setEnabled(pager.hasNextPage());
    }

    private AnchorListItem pageListItem(String text) {
        AnchorListItem item = new AnchorListItem(text, "#");
        decoratePageItem(item);
        return item;
    }

    private void decoratePageItem(Widget child) {
        if (child instanceof PageItem) {
            return;
        }
        child.addStyleName("page-item");
        if (child instanceof AnchorListItem) {
            ((AnchorListItem) child).getAnchor().addStyleName("page-link");
        }
    }

    private void updatePaginationState(final SimplePager pager) {
        for (int i = 0; i < getWidgetCount(); i++) {
            Widget widget = getWidget(i);
            if (!(widget instanceof AnchorListItem)) {
                continue;
            }
            AnchorListItem item = (AnchorListItem) widget;
            if (i == 0) {
                item.setEnabled(pager.hasPreviousPage());
            } else if (i == getWidgetCount() - 1) {
                item.setEnabled(pager.hasNextPage());
            } else {
                item.setActive(i - 1 == pager.getPage());
            }
        }
    }

    /**
     * Bootstrap 5 keeps .list-inline, and marks each child .list-inline-item.
     */
    public void setInline(final boolean inline) {
        setStyleName(Styles.LIST_INLINE, inline);
        for (final Widget child : getChildren()) {
            child.setStyleName(Styles.LIST_INLINE_ITEM, inline);
        }
    }

    public boolean isInline() {
        return StyleHelper.containsStyle(getStyleName(), Styles.LIST_INLINE);
    }

    public void setUnstyled(final boolean unstyled) {
        setStyleName(Styles.LIST_UNSTYLED, unstyled);
    }

    public boolean isUnstyled() {
        return StyleHelper.containsStyle(getStyleName(), Styles.LIST_UNSTYLED);
    }

}
