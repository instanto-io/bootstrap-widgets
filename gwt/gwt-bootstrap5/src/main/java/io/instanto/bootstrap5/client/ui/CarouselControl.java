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
import io.instanto.bootstrap5.client.ui.constants.IconType;
import io.instanto.bootstrap5.client.ui.constants.Styles;


public class CarouselControl extends ElementPanel implements HasHref {

    /**
     * A control with neither direction nor target set yet, which is what UiBinder needs:
     * it constructs a widget with no arguments and then applies the attributes. Bootstrap
     * 3's CarouselControl has the same no-argument constructor, so a template written
     * against that track carries over.
     */
    public CarouselControl() {
        this("", true);
    }

    public CarouselControl(String targetId, boolean previous) {
        super("button");
        setStyleName(previous ? "carousel-control-prev" : "carousel-control-next");
        getElement().setAttribute("type", "button");
        getElement().setAttribute("data-bs-target", "#" + targetId);
        getElement().setAttribute("data-bs-slide", previous ? "prev" : "next");
        setHTML("<span class=\"" + (previous ? "carousel-control-prev-icon" : "carousel-control-next-icon") + "\" aria-hidden=\"true\"></span>"
                + "<span class=\"visually-hidden\">" + (previous ? "Previous" : "Next") + "</span>");
    }

    private boolean previous;

    private String targetId = "";

    /** The id of the carousel this control drives, without the leading hash. */
    public void setTargetId(final String targetId) {
        this.targetId = targetId == null ? "" : targetId;
        getElement().setAttribute("data-bs-target", "#" + this.targetId);
    }

    public String getTargetId() {
        return targetId;
    }

    public void setPrev(final boolean prev) {
        setDirection(prev);
    }

    public boolean isPrev() {
        return previous;
    }

    public void setNext(final boolean next) {
        setDirection(!next);
    }

    public boolean isNext() {
        return !previous;
    }

    /**
     * Bootstrap 5 addresses the carousel with data-bs-target rather than href,
     * so this sets both and href is kept only for source compatibility.
     */
    @Override
    public void setHref(final String href) {
        this.targetId = href == null ? "" : (href.startsWith("#") ? href.substring(1) : href);
        getElement().setAttribute("data-bs-target", "#" + this.targetId);
    }

    @Override
    public String getHref() {
        return "#" + targetId;
    }

    /**
     * Bootstrap 5 draws the control with its own carousel-control-*-icon span
     * rather than a glyph from the icon font, so a custom IconType replaces
     * that span's content.
     */
    public void setIconType(final IconType iconType) {
        if (iconType == null) {
            return;
        }
        getElement().setInnerHTML("<i class=\"" + Styles.ICON + " " + iconType.getCssName() + "\" aria-hidden=\"true\"></i>"
                + "<span class=\"visually-hidden\">" + (previous ? "Previous" : "Next") + "</span>");
    }

    private void setDirection(final boolean prev) {
        this.previous = prev;
        setStyleName("carousel-control-prev", prev);
        setStyleName("carousel-control-next", !prev);
        getElement().setAttribute("data-bs-slide", prev ? "prev" : "next");
    }

}
