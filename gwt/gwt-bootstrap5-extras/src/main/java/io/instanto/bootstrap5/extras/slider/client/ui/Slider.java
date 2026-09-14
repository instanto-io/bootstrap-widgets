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
package io.instanto.bootstrap5.extras.slider.client.ui;

import io.instanto.bootstrap5.client.ui.base.HasId;
import io.instanto.bootstrap5.client.ui.base.mixin.IdMixin;
import io.instanto.bootstrap5.client.ui.html.Div;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;

/**
 * A slider with the capabilities a native range input does not have, backed by
 * noUiSlider 15.
 *
 * <p>For a plain single value, prefer {@link io.instanto.bootstrap5.client.ui.Range}:
 * Bootstrap 5 styles the native control and nothing needs shipping. This is for
 * two handles, non-linear scales, tooltips and pips. GwtBootstrap3 wrapped
 * bootstrap-slider, a jQuery plugin; noUiSlider has no dependencies.</p>
 *
 * <p>A slider with one handle reports through {@link #getValue()}; one with two
 * reports through {@link #getLowerValue()} and {@link #getUpperValue()}.</p>
 */
public class Slider extends Div implements HasEnabled, HasId, HasValueChangeHandlers<Double> {

    private final IdMixin<Slider> idMixin = new IdMixin<Slider>(this);

    private double min = 0;

    private double max = 100;

    private double step = 1;

    private double start = 0;

    private double end = 100;

    private boolean range;

    private boolean tooltips;

    private boolean pips;

    private boolean enabled = true;




    private JavaScriptObject slider;

    public Slider() {
        addStyleName("gbm-slider");
        getElement().getStyle().setProperty("margin", "1.5rem 0.5rem");
    }

    public Slider(final double min, final double max) {
        this();
        setMin(min);
        setMax(max);
        setValue(min);
    }

    /** Two handles rather than one. Set before attach. */
    public void setRange(final boolean range) {
        this.range = range;
    }

    public boolean isRange() {
        return range;
    }

    /** Shows the value above each handle. Set before attach. */
    public void setTooltips(final boolean tooltips) {
        this.tooltips = tooltips;
    }

    /** Draws a scale beneath the track. Set before attach. */
    public void setPips(final boolean pips) {
        this.pips = pips;
    }

    public void setMin(final double min) {
        this.min = min;
    }

    public double getMin() {
        return min;
    }

    public void setMax(final double max) {
        this.max = max;
    }

    public double getMax() {
        return max;
    }

    public void setStep(final double step) {
        this.step = step;
    }

    public double getStep() {
        return step;
    }

    /** The handle position of a single-handle slider. */
    public void setValue(final double value) {
        start = value;
        if (slider != null) {
            SliderJs.applyValues(slider, value, value);
        }
    }

    public double getValue() {
        return slider == null ? start : SliderJs.readValue(slider, 0);
    }

    /** The handle positions of a two-handle slider. */
    public void setValues(final double lower, final double upper) {
        start = lower;
        end = upper;
        if (slider != null) {
            SliderJs.applyValues(slider, lower, upper);
        }
    }

    public double getLowerValue() {
        return slider == null ? start : SliderJs.readValue(slider, 0);
    }

    public double getUpperValue() {
        return slider == null ? end : SliderJs.readValue(slider, 1);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        initialise();
    }

    /**
     * Builds the slider once noUiSlider is usable.
     *
     * <p>Which backend this is does not matter here any more. GWT compiles the library
     * into the module, so the action runs immediately; TeaVM fetches it by URL and runs
     * the action when it arrives. Either way the widget asks the module rather than
     * polling for a global to appear, and a module that fails to load reports it instead
     * of letting the widget wait out a timeout and give up silently.</p>
     */
    private void initialise() {
        SliderJs.whenReady(new Runnable() {
            @Override
            public void run() {
                if (isAttached()) {
                    build();
                }
            }
        });
    }

    private void build() {
        slider = SliderJs.create(getElement(), min, max, step, start, end, range, tooltips, pips);
        SliderJs.bindChange(slider, new SliderJs.UpdateHandler() {
            @Override
            public void onUpdate(final double value) {
                onSliderUpdate(value);
            }
        });
        if (!enabled) {
            SliderJs.applyEnabled(getElement(), false);
        }
    }

    @Override
    protected void onUnload() {
        if (slider != null) {
            SliderJs.destroy(slider);
            slider = null;
        }
        super.onUnload();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        if (slider != null) {
            SliderJs.applyEnabled(getElement(), enabled);
        }
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
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Double> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /** Called from the noUiSlider update subscription. */
    void onSliderUpdate(final double value) {
        ValueChangeEvent.fire(this, value);
    }
}
