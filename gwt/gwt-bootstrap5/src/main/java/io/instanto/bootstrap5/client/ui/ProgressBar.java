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

import io.instanto.bootstrap5.client.ui.base.HasType;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.ProgressBarType;

public class ProgressBar extends ElementPanel implements HasType<ProgressBarType> {

    private Variant variant;

    public ProgressBar() {
        this(0);
    }

    public ProgressBar(int value) {
        super("div");
        addStyleName("progress-bar");
        getElement().setAttribute("role", "progressbar");
        setValue(value);
    }

    public ProgressBar(int value, ProgressBarType type) {
        this(value);
        setType(type);
    }

    public void setValue(int value) {
        int clamped = Math.max(0, Math.min(100, value));
        getElement().getStyle().setProperty("width", clamped + "%");
        getElement().setAttribute("aria-valuenow", String.valueOf(clamped));
        getElement().setAttribute("aria-valuemin", "0");
        getElement().setAttribute("aria-valuemax", "100");
        setText(clamped + "%");
    }

    public void setPercent(double percent) {
        double clamped = Math.max(0, Math.min(100, percent));
        getElement().getStyle().setProperty("width", clamped + "%");
        getElement().setAttribute("aria-valuenow", String.valueOf(clamped));
        getElement().setAttribute("aria-valuemin", "0");
        getElement().setAttribute("aria-valuemax", "100");
    }

    public double getPercent() {
        String width = getElement().getStyle().getWidth();
        if (width == null || !width.endsWith("%")) {
            return 0;
        }
        return Double.parseDouble(width.substring(0, width.length() - 1));
    }

    public void setVariant(Variant variant) {
        if (this.variant != null) {
            removeStyleName("bg-" + this.variant.cssName());
        }
        this.variant = variant;
        if (variant != null) {
            addStyleName("bg-" + variant.cssName());
        }
    }

    public Variant getVariant() {
        return variant;
    }

    @Override
    public void setType(ProgressBarType type) {
        StyleHelper.addUniqueEnumStyleName(this, ProgressBarType.class, type == null ? ProgressBarType.DEFAULT : type);
    }

    @Override
    public ProgressBarType getType() {
        return ProgressBarType.fromStyleName(getStyleName());
    }

    public void setSrOnly(boolean srOnly) {
        setStyleName("visually-hidden", srOnly);
    }

    public void setStriped(boolean striped) {
        setStyleName("progress-bar-striped", striped);
    }

    public void setAnimated(boolean animated) {
        setStyleName("progress-bar-animated", animated);
    }
}
