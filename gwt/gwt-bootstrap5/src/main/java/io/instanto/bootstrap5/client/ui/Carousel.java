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

import io.instanto.bootstrap5.client.ui.base.BootstrapComponent;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import io.instanto.bootstrap5.client.shared.event.CarouselSlidEvent;
import io.instanto.bootstrap5.client.shared.event.CarouselSlidHandler;
import io.instanto.bootstrap5.client.shared.event.CarouselSlideEvent;
import io.instanto.bootstrap5.client.shared.event.CarouselSlideHandler;
import io.instanto.bootstrap5.client.ui.base.BootstrapEventBridge;
import io.instanto.bootstrap5.client.ui.base.BootstrapEventHandler;

public class Carousel extends ElementPanel {

    public static final String HOVER = "hover";
    public static final String CAROUSEL = "carousel";
    public static final String CYCLE = "cycle";
    public static final String PAUSE = "pause";
    public static final String PREV = "prev";
    public static final String NEXT = "next";

    private final CarouselInner inner = new CarouselInner();
    private int interval = 5000;
    private String pause = HOVER;
    private boolean wrap = true;

    public Carousel() {
        super("div");
        setStyleName("carousel slide");
        getElement().setAttribute("data-bs-ride", "carousel");
        add(inner);
    }

    public CarouselInner getInner() {
        return inner;
    }

    public void setInterval(int intervalMs) {
        interval = intervalMs;
        reconfigureIfAttached();
    }

    public void setPause(String pause) {
        this.pause = pause;
        reconfigureIfAttached();
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
        reconfigureIfAttached();
    }

    public void addSlide(CarouselSlide slide) {
        inner.add(slide);
    }

    public void cycleCarousel() {
        BootstrapComponent.call(getElement(), "Carousel", "cycle");
    }

    public void pauseCarousel() {
        BootstrapComponent.call(getElement(), "Carousel", "pause");
    }

    public void goToPrev() {
        BootstrapComponent.call(getElement(), "Carousel", "prev");
    }

    public void goToNext() {
        BootstrapComponent.call(getElement(), "Carousel", "next");
    }

    public void jumpToSlide(int index) {
        BootstrapComponent.call(getElement(), "Carousel", "to", index);
    }

    public HandlerRegistration addSlideHandler(CarouselSlideHandler handler) {
        return addHandler(handler, CarouselSlideEvent.getType());
    }

    public HandlerRegistration addSlidHandler(CarouselSlidHandler handler) {
        return addHandler(handler, CarouselSlidEvent.getType());
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        BootstrapEventBridge.bind(getElement(), "slide.bs.carousel", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                fireEvent(new CarouselSlideEvent(Carousel.this, Event.as(event)));
            }
        });
        BootstrapEventBridge.bind(getElement(), "slid.bs.carousel", new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                fireEvent(new CarouselSlidEvent(Carousel.this, Event.as(event)));
            }
        });
        BootstrapComponent.createCarousel(getElement(), interval, pause, wrap);
    }

    @Override
    protected void onUnload() {
        BootstrapEventBridge.unbindAll(getElement());
        BootstrapComponent.dispose(getElement(), "Carousel");
        super.onUnload();
    }

    private void reconfigureIfAttached() {
        if (isAttached()) {
            BootstrapComponent.createCarousel(getElement(), interval, pause, wrap);
        }
    }

    /** Bootstrap's Carousel options, as a JavaScript object. */
}
