package org.gwtbootstrap3.extras.slider.client.ui;

import org.gwtbootstrap3.extras.slider.client.ui.base.SliderJs;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2015 GwtBootstrap3
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

import org.gwtbootstrap3.extras.slider.client.ui.base.SliderBase;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Event;

/**
 * This slider simply takes a numeric value.
 *
 * @author Xiaodong SUN
 */
public class Slider extends SliderBase<Double> {

    /**
     * Creates a numerical slider.
     */
    public Slider() {
        setRange(false);
    }

    /**
     * Creates a numerical slider with min, max, and value.
     *
     * @param min
     * @param max
     * @param value
     */
    @UiConstructor
    public Slider(final double min, final double max, final double value) {
        this();
        setMin(min);
        setMax(max);
        setValue(value);
    }

    @Override
    protected void setValue(Element e, Double value) { SliderJs.value(e, value.toString()); }

    @Override
    protected Double getValue(Element e) { return convertValue(SliderJs.value(e)); }

    @Override
    protected void setFormatterOption(JavaScriptObject options) { SliderJs.formatter(options, value -> formatTooltip(convertValue(value))); }

    @Override
    protected void setFormatter(Element e) { SliderJs.formatter(e, value -> formatTooltip(convertValue(value))); }

    @Override
    protected String format(Double value) {
        return value.toString();
    }

    @Override
    protected Double convertValue(String value) {
        if (value == null || value.isEmpty())
            return null;
        return Double.valueOf(value);
    }

    @Override
    protected void onSlide(Event event) { fireSlideEvent(convertValue(SliderJs.eventValue(event, false))); }

    @Override
    protected void onSlideStart(Event event) { fireSlideStartEvent(convertValue(SliderJs.eventValue(event, false))); }

    @Override
    protected void onSlideStop(Event event) { fireSlideStopEvent(convertValue(SliderJs.eventValue(event, false))); }

    @Override
    protected void onSlideChange(Event event) { fireChangeEvent(convertValue(SliderJs.eventValue(event, true))); }

}
