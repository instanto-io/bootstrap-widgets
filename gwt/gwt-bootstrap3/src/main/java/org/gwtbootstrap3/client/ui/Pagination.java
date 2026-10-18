package org.gwtbootstrap3.client.ui;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2014 GwtBootstrap3
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

import org.gwtbootstrap3.client.ui.base.HasPaginationSize;
import org.gwtbootstrap3.client.ui.base.HasResponsiveness;
import org.gwtbootstrap3.client.ui.base.helper.StyleHelper;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.PaginationSize;
import org.gwtbootstrap3.client.ui.constants.Styles;
import org.gwtbootstrap3.client.ui.html.UnorderedList;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Widget;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Support for Bootstrap pagination (http://getbootstrap.com/components/#pagination)
 *
 * @author Joshua Godi
 */
public class Pagination extends UnorderedList implements HasResponsiveness, HasPaginationSize {

    private PageBinding binding;

    private static final class PageBinding {
        final SimplePager pager;
        final Map<AnchorListItem, Integer> pages = new LinkedHashMap<>();
        AnchorListItem previous;
        AnchorListItem next;

        PageBinding(SimplePager pager) { this.pager = pager; }
    }

    @Override
    public void clear() {
        binding = null;
        super.clear();
    }

    @Override
    public boolean remove(Widget child) {
        boolean removed = super.remove(child);
        if (removed && binding != null) {
            binding.pages.remove(child);
            if (binding.previous == child) binding.previous = null;
            if (binding.next == child) binding.next = null;
        }
        return removed;
    }

    public Pagination() {
        setStyleName(Styles.PAGINATION);
    }

    public Pagination(final PaginationSize paginationSize) {
        this();
        setPaginationSize(paginationSize);
    }

    @Override
    public void setPaginationSize(final PaginationSize paginationSize) {
        StyleHelper.addUniqueEnumStyleName(this, PaginationSize.class, paginationSize);
    }

    @Override
    public PaginationSize getPaginationSize() {
        return PaginationSize.fromStyleName(getStyleName());
    }

    public AnchorListItem addPreviousLink() {
        final AnchorListItem listItem = new AnchorListItem();
        listItem.setIcon(IconType.ANGLE_DOUBLE_LEFT);
        insert(listItem, 0);
        return listItem;
    }

    public AnchorListItem addNextLink() {
        final AnchorListItem listItem = new AnchorListItem();
        listItem.setIcon(IconType.ANGLE_DOUBLE_RIGHT);
        add(listItem);
        return listItem;
    }

    /**
     * This will help to rebuild the Pagination based on the data inside the SimplePager passed in.
     * <p/>
     * Make sure to all this after adding/remove data from any of the grid to ensure that this stays
     * current with the SimplePager.
     * <p/>
     * ex.
     * dataProvider.getList().addAll(newData);
     * pagination.rebuild(mySimplePager);
     *
     * @param pager the SimplePager of the CellTable/DataGrid
     */
    public void rebuild(final SimplePager pager) {
        Objects.requireNonNull(pager, "pager");
        clear();
        if (pager.getPageCount() == 0) return;

        final PageBinding current = new PageBinding(pager);
        binding = current;
        final AnchorListItem previous = addPreviousLink();
        current.previous = previous;
        previous.addClickHandler(event -> {
            if (!owns(current, previous) || !pager.hasPreviousPage()) return;
            pager.previousPage();
            updatePaginationState(current);
        });
        for (int i = 0; i < pager.getPageCount(); i++) {
            final int pageIndex = i;
            final AnchorListItem page = new AnchorListItem(String.valueOf(i + 1));
            current.pages.put(page, pageIndex);
            page.addClickHandler(event -> {
                if (!owns(current, page)) return;
                pager.setPage(pageIndex);
                updatePaginationState(current);
            });
            add(page);
        }
        final AnchorListItem next = addNextLink();
        current.next = next;
        next.addClickHandler(event -> {
            if (!owns(current, next) || !pager.hasNextPage()) return;
            pager.nextPage();
            updatePaginationState(current);
        });
        updatePaginationState(current);
    }

    private boolean owns(PageBinding current, AnchorListItem link) {
        return binding == current && link.getParent() == this;
    }

    private void updatePaginationState(PageBinding current) {
        if (binding != current) return;
        if (current.previous != null) current.previous.setEnabled(current.pager.hasPreviousPage());
        if (current.next != null) current.next.setEnabled(current.pager.hasNextPage());
        for (Map.Entry<AnchorListItem, Integer> page : current.pages.entrySet()) {
            page.getKey().setActive(page.getValue() == current.pager.getPage());
        }
    }
}
