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
 * This slider takes as value a range with a min value and a max value.
 *
 * @author Xiaodong SUN
 */
public class RangeSlider extends SliderBase<Range> {

    /**
     * Creates a range slider.
     */
    public RangeSlider() {
        setRange(true);
    }

    /**
     * Creates a range slider with min, max, and range value.
     *
     * @param min
     * @param max
     * @param range
     */
    public RangeSlider(final double min, final double max, final Range range) {
        this();
        setMin(min);
        setMax(max);
        setValue(range);
    }

    /**
     * Creates a range slider with min, max, and range value.<br>
     * <br>
     * Useful for UiBinder.
     *
     * @param min
     * @param max
     * @param value
     */
    @UiConstructor
    public RangeSlider(final double min, final double max, final String value) {
        this(min, max, Range.fromString(value));
    }

    @Override
    protected void setValue(Element e, Range value) { SliderJs.value(e, value.toString()); }

    @Override
    protected Range getValue(Element e) { return convertValue(SliderJs.value(e)); }

    @Override
    protected void setFormatterOption(JavaScriptObject options) { SliderJs.formatter(options, value -> formatTooltip(convertValue(value))); }

    @Override
    protected void setFormatter(Element e) { SliderJs.formatter(e, value -> formatTooltip(convertValue(value))); }

    @Override
    protected String format(Range value) {
        return value.getMinValue() + " : " + value.getMaxValue();
    }

    @Override
    protected Range convertValue(String value) {
        return Range.fromString(value);
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
