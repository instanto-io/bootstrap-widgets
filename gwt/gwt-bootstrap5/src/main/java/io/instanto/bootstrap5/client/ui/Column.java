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

import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.constants.ColumnOffset;
import io.instanto.bootstrap5.client.ui.constants.ColumnPull;
import io.instanto.bootstrap5.client.ui.constants.ColumnPush;
import io.instanto.bootstrap5.client.ui.constants.ColumnSize;
import io.instanto.bootstrap5.client.ui.html.Div;

public class Column extends Div {

    private static final String SEPARATOR = "[, ]+";

    public Column() {
        this(12);
    }

    public Column(int span) {
        setStyleName("col-" + boundedSpan(span));
    }

    public Column(final ColumnSize size, final Widget firstWidget, final Widget... otherWidgets) {
        this(size);
        add(firstWidget);
        for (final Widget widget : otherWidgets) {
            add(widget);
        }
    }

    public Column(final ColumnSize firstSize, final ColumnSize... otherSizes) {
        setSize(firstSize, otherSizes);
    }

    @UiConstructor
    public Column(final String size) {
        setSize(size);
    }

    public void setMediumSpan(int span) {
        addStyleName("col-md-" + boundedSpan(span));
    }

    public void setSize(final ColumnSize firstSize, final ColumnSize... otherSizes) {
        addEnumVarargsValues(new ColumnSize[]{firstSize}, ColumnSize.class, true);
        addEnumVarargsValues(otherSizes, ColumnSize.class, false);
    }

    public void setSize(final String sizes) {
        addEnumStringValues(sizes, ColumnSize.class, true);
    }

    public void addSize(final ColumnSize... sizes) {
        addEnumVarargsValues(sizes, ColumnSize.class, false);
    }

    public void addSize(final String sizes) {
        addEnumStringValues(sizes, ColumnSize.class, false);
    }

    public void setPull(final ColumnPull... pulls) {
        addEnumVarargsValues(pulls, ColumnPull.class, true);
    }

    public void setPull(final String pulls) {
        addEnumStringValues(pulls, ColumnPull.class, true);
    }

    public void addPull(final ColumnPull... pulls) {
        addEnumVarargsValues(pulls, ColumnPull.class, false);
    }

    public void addPull(final String pulls) {
        addEnumStringValues(pulls, ColumnPull.class, false);
    }

    public void setPush(final ColumnPush... pushes) {
        addEnumVarargsValues(pushes, ColumnPush.class, true);
    }

    public void setPush(final String pushes) {
        addEnumStringValues(pushes, ColumnPush.class, true);
    }

    public void addPush(final ColumnPush... pushes) {
        addEnumVarargsValues(pushes, ColumnPush.class, false);
    }

    public void addPush(final String pushes) {
        addEnumStringValues(pushes, ColumnPush.class, false);
    }

    public void setOffset(final ColumnOffset... offsets) {
        addEnumVarargsValues(offsets, ColumnOffset.class, true);
    }

    public void setOffset(final String offsets) {
        addEnumStringValues(offsets, ColumnOffset.class, true);
    }

    public void addOffset(final ColumnOffset... offsets) {
        addEnumVarargsValues(offsets, ColumnOffset.class, false);
    }

    public void addOffset(final String offsets) {
        addEnumStringValues(offsets, ColumnOffset.class, false);
    }

    private <E extends Enum<? extends Style.HasCssName>> void addEnumVarargsValues(final E[] values,
                                                                                   final Class<E> enumClass,
                                                                                   final boolean clearOld) {
        if (clearOld) {
            removeStyleNames(enumClass);
        }
        for (final E value : values) {
            addStyleName(cssName(value));
        }
    }

    private <E extends Enum<? extends Style.HasCssName>> void addEnumStringValues(final String values,
                                                                                  final Class<E> enumClass,
                                                                                  final boolean clearOld) {
        if (clearOld) {
            removeStyleNames(enumClass);
        }
        if (values == null || values.trim().isEmpty()) {
            return;
        }
        final String[] valuesSplit = values.split(SEPARATOR);
        for (final String value : valuesSplit) {
            for (final E constant : enumClass.getEnumConstants()) {
                if (value.equalsIgnoreCase(constant.name())) {
                    addStyleName(cssName(constant));
                }
            }
        }
    }

    private <E extends Enum<? extends Style.HasCssName>> void removeStyleNames(final Class<E> enumClass) {
        for (final E constant : enumClass.getEnumConstants()) {
            removeStyleName(cssName(constant));
        }
    }

    private String cssName(final Enum<? extends Style.HasCssName> value) {
        final String cssName = ((Style.HasCssName) value).getCssName();
        if (value instanceof ColumnSize) {
            return cssName.replace("col-xs-", "col-");
        }
        if (value instanceof ColumnOffset) {
            return cssName.replace("col-xs-offset-", "offset-")
                    .replace("col-sm-offset-", "offset-sm-")
                    .replace("col-md-offset-", "offset-md-")
                    .replace("col-lg-offset-", "offset-lg-");
        }
        return cssName;
    }

    private int boundedSpan(int span) {
        return Math.max(1, Math.min(12, span));
    }
}
