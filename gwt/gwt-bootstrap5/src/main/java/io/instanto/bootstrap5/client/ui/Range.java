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

import io.instanto.bootstrap5.client.ui.base.HasId;
import io.instanto.bootstrap5.client.ui.base.HasResponsiveness;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.base.mixin.IdMixin;
import io.instanto.bootstrap5.client.ui.constants.DeviceSize;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

import io.instanto.bootstrap5.client.ui.base.InputEvents;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasValue;

/**
 * A slider, as a native range input styled by Bootstrap 5.
 *
 * <p>GwtBootstrap3 got its slider from bootstrap-slider, a jQuery plugin.
 * Bootstrap 5 styles {@code <input type="range">} itself through
 * {@code .form-range}, so for a single value there is nothing to wrap and no
 * library to ship. Reach for a third-party slider only for what the native
 * control genuinely cannot do -- two handles, non-linear scales, pips.</p>
 *
 * <p>Value changes fire on {@code change}, so dragging reports once the handle
 * is released rather than on every pixel; {@link #setContinuous(boolean)}
 * switches to reporting throughout the drag.</p>
 */
public class Range extends FocusWidget implements HasId, HasName, HasResponsiveness,
        HasValue<Double>, com.google.gwt.event.logical.shared.HasValueChangeHandlers<Double> {

    private final IdMixin<Range> idMixin = new IdMixin<Range>(this);

    private boolean continuous;

    public Range() {
        final InputElement element = InputElement.as(Document.get().createElement("input"));
        element.setAttribute("type", "range");
        setElement(element);
        setStyleName("form-range");
        setMin(0);
        setMax(100);
        setStep(1);

        addDomHandler(new ChangeHandler() {
            @Override
            public void onChange(final ChangeEvent event) {
                ValueChangeEvent.fire(Range.this, getValue());
            }
        }, ChangeEvent.getType());
    }

    public Range(final double min, final double max) {
        this();
        setMin(min);
        setMax(max);
    }

    public void setMin(final double min) {
        input().setAttribute("min", trim(min));
    }

    public double getMin() {
        return parse(input().getAttribute("min"), 0);
    }

    public void setMax(final double max) {
        input().setAttribute("max", trim(max));
    }

    public double getMax() {
        return parse(input().getAttribute("max"), 100);
    }

    public void setStep(final double step) {
        input().setAttribute("step", trim(step));
    }

    public double getStep() {
        return parse(input().getAttribute("step"), 1);
    }

    /**
     * Reports value changes throughout the drag rather than only when the handle
     * is released.
     *
     * <p>This GWT has no {@code InputEvent} type, so the native {@code input}
     * event is listened for directly.</p>
     */
    public void setContinuous(final boolean continuous) {
        if (this.continuous == continuous) {
            return;
        }
        this.continuous = continuous;
        if (continuous) {
            InputEvents.listen(getElement(), this::onNativeInput);
        }
    }

    public boolean isContinuous() {
        return continuous;
    }

    /** Called from the native input listener while dragging. */
    private void onNativeInput() {
        if (continuous) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @Override
    public Double getValue() {
        return parse(input().getValue(), getMin());
    }

    @Override
    public void setValue(final Double value) {
        setValue(value, false);
    }

    @Override
    public void setValue(final Double value, final boolean fireEvents) {
        input().setValue(value == null ? trim(getMin()) : trim(value));
        if (fireEvents) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Double> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setName(final String name) {
        input().setName(name == null ? "" : name);
    }

    @Override
    public String getName() {
        return input().getName();
    }

    @Override
    public void setId(final String id) {
        idMixin.setId(id);
    }

    @Override
    public String getId() {
        return idMixin.getId();
    }

    @Override
    public void setVisibleOn(final DeviceSize deviceSize) {
        StyleHelper.setVisibleOn(this, deviceSize);
    }

    @Override
    public void setHiddenOn(final DeviceSize deviceSize) {
        StyleHelper.setHiddenOn(this, deviceSize);
    }

    private InputElement input() {
        return InputElement.as(getElement());
    }

    private static String trim(final double value) {
        return value == Math.rint(value) ? String.valueOf((long) value) : String.valueOf(value);
    }

    private static double parse(final String value, final double fallback) {
        if (value == null || value.isEmpty()) {
            return fallback;
        }
        try {
            return Double.parseDouble(value);
        } catch (final NumberFormatException e) {
            return fallback;
        }
    }
}
