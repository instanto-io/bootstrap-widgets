package io.instanto.bootstrap5.client.ui.base;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2015 GwtBootstrap3
 * %%
 * Modified for Bootstrap 5 without changing the GwtBootstrap3 composition API.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * #L%
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

import io.instanto.bootstrap5.client.shared.event.HiddenEvent;
import io.instanto.bootstrap5.client.shared.event.HiddenHandler;
import io.instanto.bootstrap5.client.shared.event.HideEvent;
import io.instanto.bootstrap5.client.shared.event.HideHandler;
import io.instanto.bootstrap5.client.shared.event.InsertedEvent;
import io.instanto.bootstrap5.client.shared.event.ShowEvent;
import io.instanto.bootstrap5.client.shared.event.ShowHandler;
import io.instanto.bootstrap5.client.shared.event.ShownEvent;
import io.instanto.bootstrap5.client.shared.event.ShownHandler;
import io.instanto.bootstrap5.client.ui.constants.Placement;
import io.instanto.bootstrap5.client.ui.constants.Trigger;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;

/** Common Bootstrap 5 implementation for tooltip and popover controllers. */
public abstract class AbstractTooltip implements IsWidget, HasWidgets, HasOneWidget, HasId, HasHover {

    private static final String DEFAULT_TEMPLATE =
            "<div class=\"{0}\" role=\"tooltip\"><div class=\"{1}\"></div><div class=\"{2}\"></div></div>";

    private final String eventNamespace;
    private boolean animated = true;
    private boolean html;
    private Placement placement = Placement.TOP;
    private Trigger trigger = Trigger.HOVER;
    private String title = "";
    private int hideDelayMs;
    private int showDelayMs;
    private String container;
    private String selector;
    private String viewportSelector = "body";
    private int viewportPadding;
    private String tooltipClassNames = "tooltip";
    private String tooltipArrowClassNames = "tooltip-arrow";
    private String tooltipInnerClassNames = "tooltip-inner";
    private String alternateTemplate;
    private String id;
    private boolean initialized;
    private boolean showing;
    private HandlerRegistration attachRegistration;
    protected Widget widget;

    public AbstractTooltip(String eventNamespace) {
        this.eventNamespace = eventNamespace;
    }

    public AbstractTooltip(String eventNamespace, String title) {
        this(eventNamespace);
        setTitle(title);
    }

    public AbstractTooltip(String eventNamespace, Widget widget) {
        this(eventNamespace);
        setWidget(widget);
    }

    public AbstractTooltip(String eventNamespace, Widget widget, String title) {
        this(eventNamespace, widget);
        setTitle(title);
    }

    @Override
    public void add(Widget child) {
        if (widget != null) {
            throw new IllegalStateException("Can only contain one child widget");
        }
        setWidget(child);
    }

    public HandlerRegistration addHiddenHandler(HiddenHandler handler) {
        requireWidget();
        return widget.addHandler(handler, HiddenEvent.getType());
    }

    public HandlerRegistration addHideHandler(HideHandler handler) {
        requireWidget();
        return widget.addHandler(handler, HideEvent.getType());
    }

    public HandlerRegistration addShowHandler(ShowHandler handler) {
        requireWidget();
        return widget.addHandler(handler, ShowEvent.getType());
    }

    public HandlerRegistration addShownHandler(ShownHandler handler) {
        requireWidget();
        return widget.addHandler(handler, ShownEvent.getType());
    }

    public void addTooltipArrowClassName(String className) {
        tooltipArrowClassNames = appendClass(tooltipArrowClassNames, className);
        reconfigure();
    }

    public void addTooltipClassName(String className) {
        tooltipClassNames = appendClass(tooltipClassNames, className);
        reconfigure();
    }

    public void addTooltipInnerClassName(String className) {
        tooltipInnerClassNames = appendClass(tooltipInnerClassNames, className);
        reconfigure();
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void clear() {
        destroy();
        if (attachRegistration != null) {
            attachRegistration.removeHandler();
            attachRegistration = null;
        }
        widget = null;
    }

    public void destroy() {
        if (widget != null) {
            unbindEvents(widget.getElement());
            BootstrapComponent.dispose(widget.getElement(), getBootstrapPluginName());
        }
        initialized = false;
        showing = false;
    }

    public String getAlternateTemplate() {
        return alternateTemplate;
    }

    @Override
    public String getContainer() {
        return container;
    }

    @Override
    public int getHideDelayMs() {
        return hideDelayMs;
    }

    @Override
    public String getId() {
        return widget == null ? id : widget.getElement().getId();
    }

    @Override
    public Placement getPlacement() {
        return placement;
    }

    public String getSelector() {
        return selector;
    }

    @Override
    public int getShowDelayMs() {
        return showDelayMs;
    }

    public String getTitle() {
        return title;
    }

    public String getTooltipArrowClassNames() {
        return tooltipArrowClassNames;
    }

    public String getTooltipClassNames() {
        return tooltipClassNames;
    }

    public String getTooltipInnerClassNames() {
        return tooltipInnerClassNames;
    }

    @Override
    public Trigger getTrigger() {
        return trigger;
    }

    public int getViewportPadding() {
        return viewportPadding;
    }

    public String getViewportSelector() {
        return viewportSelector;
    }

    @Override
    public Widget getWidget() {
        return widget;
    }

    public void hide() {
        call("hide");
    }

    public abstract void init();

    @Override
    public boolean isAnimated() {
        return animated;
    }

    @Override
    public boolean isHtml() {
        return html;
    }

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public Iterator<Widget> iterator() {
        return new Iterator<Widget>() {
            private boolean available = widget != null;
            private Widget returned;

            @Override
            public boolean hasNext() {
                return available;
            }

            @Override
            public Widget next() {
                if (!available || widget == null) {
                    throw new NoSuchElementException();
                }
                available = false;
                returned = widget;
                return returned;
            }

            @Override
            public void remove() {
                if (returned != null) {
                    AbstractTooltip.this.remove(returned);
                }
            }
        };
    }

    public void reconfigure() {
        if (initialized && widget != null && widget.isAttached()) {
            init();
        }
    }

    public void recreate() {
        recreate(300);
    }

    public void recreate(int delay) {
        destroy();
        new Timer() {
            @Override
            public void run() {
                if (widget != null && widget.isAttached()) {
                    init();
                }
            }
        }.schedule(Math.max(0, delay));
    }

    @Override
    public boolean remove(Widget candidate) {
        if (candidate != widget) {
            return false;
        }
        clear();
        return true;
    }

    public void setAlternateTemplate(String alternateTemplate) {
        this.alternateTemplate = alternateTemplate;
        reconfigure();
    }

    @Override
    public void setContainer(String container) {
        this.container = container;
        reconfigure();
    }

    @Override
    public void setHideDelayMs(int hideDelayMs) {
        this.hideDelayMs = Math.max(0, hideDelayMs);
        reconfigure();
    }

    public void setHtml(SafeHtml html) {
        setIsHtml(true);
        setTitle(html == null ? "" : html.asString());
    }

    @Override
    public void setId(String id) {
        this.id = id;
        if (widget != null) {
            widget.getElement().setId(id == null ? "" : id);
        }
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public void setIsAnimated(boolean animated) {
        this.animated = animated;
        reconfigure();
    }

    @Override
    public void setIsHtml(boolean html) {
        this.html = html;
        reconfigure();
    }

    @Override
    public void setPlacement(Placement placement) {
        this.placement = placement == null || placement == Placement.DEFAULT ? Placement.TOP : placement;
        reconfigure();
    }

    public void setSelector(String selector) {
        this.selector = selector;
        reconfigure();
    }

    @Override
    public void setShowDelayMs(int showDelayMs) {
        this.showDelayMs = Math.max(0, showDelayMs);
        reconfigure();
    }

    /** @deprecated use {@link #setTitle(String)}. */
    @Deprecated
    public void setText(String text) {
        setTitle(text);
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
        reconfigure();
    }

    public void setTooltipArrowClassNames(String classNames) {
        tooltipArrowClassNames = classNames;
        reconfigure();
    }

    public void setTooltipClassNames(String classNames) {
        tooltipClassNames = classNames;
        reconfigure();
    }

    public void setTooltipInnerClassNames(String classNames) {
        tooltipInnerClassNames = classNames;
        reconfigure();
    }

    @Override
    public void setTrigger(Trigger trigger) {
        this.trigger = trigger == null || trigger == Trigger.DEFAULT ? Trigger.HOVER : trigger;
        reconfigure();
    }

    public void setViewportPadding(int viewportPadding) {
        this.viewportPadding = Math.max(0, viewportPadding);
        reconfigure();
    }

    public void setViewportSelector(String viewportSelector) {
        this.viewportSelector = viewportSelector;
        reconfigure();
    }

    @Override
    public void setWidget(IsWidget widget) {
        setWidget(widget == null ? null : widget.asWidget());
    }

    @Override
    public void setWidget(Widget newWidget) {
        if (newWidget == widget) {
            return;
        }
        clear();
        if (newWidget != null) {
            newWidget.removeFromParent();
        }
        widget = newWidget;
        if (widget == null) {
            return;
        }
        if (id != null) {
            widget.getElement().setId(id);
        }
        attachRegistration = widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached()) {
                    init();
                } else {
                    destroy();
                }
            }
        });
        if (widget.isAttached()) {
            init();
        }
    }

    public void show() {
        call("show");
    }

    public void toggle() {
        call("toggle");
    }

    @Override
    public String toString() {
        return widget == null ? super.toString() : widget.toString();
    }

    protected abstract void call(String method);

    protected abstract String getBootstrapPluginName();

    protected final void initializePlugin(String content) {
        requireWidget();
        Element element = widget.getElement();
        applyDataAttributes(element, content);
        bindEvents(element);
        BootstrapComponent.createPlugin(element, getBootstrapPluginName(), animated, html,
                placement.getCssName(), trigger.getCssName(), showDelayMs, hideDelayMs,
                container, selector, viewportSelector, viewportPadding, title,
                prepareTemplate(), content);
        initialized = true;
    }

    protected final void invokePlugin(String method) {
        if (widget != null) {
            BootstrapComponent.call(widget.getElement(), getBootstrapPluginName(), method);
        }
    }

    protected void updateTitleWhenShowing() {
        if (showing) {
            init();
            show();
        }
    }

    protected String prepareTemplate() {
        if (alternateTemplate != null) {
            return alternateTemplate;
        }
        return DEFAULT_TEMPLATE.replace("{0}", tooltipClassNames)
                .replace("{1}", tooltipArrowClassNames)
                .replace("{2}", tooltipInnerClassNames);
    }

    private void bindEvents(Element element) {
        bindEvent(element, "show.bs." + eventNamespace, new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                widget.fireEvent(new ShowEvent(Event.as(event)));
            }
        });
        bindEvent(element, "shown.bs." + eventNamespace, new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                showing = true;
                widget.fireEvent(new ShownEvent(Event.as(event)));
            }
        });
        bindEvent(element, "hide.bs." + eventNamespace, new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                widget.fireEvent(new HideEvent(Event.as(event)));
            }
        });
        bindEvent(element, "hidden.bs." + eventNamespace, new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                showing = false;
                widget.fireEvent(new HiddenEvent(Event.as(event)));
            }
        });
        bindEvent(element, "inserted.bs." + eventNamespace, new BootstrapEventHandler() {
            @Override
            public void onEvent(NativeEvent event) {
                widget.fireEvent(new InsertedEvent(Event.as(event)));
            }
        });
    }

    private void applyDataAttributes(Element element, String content) {
        element.setAttribute("data-bs-toggle", eventNamespace);
        element.setAttribute("data-bs-title", title);
        element.setAttribute("title", title);
        if (content != null) {
            element.setAttribute("data-bs-content", content);
        }
    }

    private void requireWidget() {
        if (widget == null) {
            throw new IllegalStateException("Tooltip requires a widget");
        }
    }

    private static String appendClass(String current, String className) {
        if (className == null || className.trim().isEmpty()) {
            return current;
        }
        return current == null || current.isEmpty() ? className.trim() : current + " " + className.trim();
    }

    private static void bindEvent(Element element, String eventName, BootstrapEventHandler handler) {
        BootstrapEventBridge.bind(element, eventName, handler);
    }

    private void unbindEvents(Element element) {
        BootstrapEventBridge.unbind(element, "show.bs." + eventNamespace);
        BootstrapEventBridge.unbind(element, "shown.bs." + eventNamespace);
        BootstrapEventBridge.unbind(element, "hide.bs." + eventNamespace);
        BootstrapEventBridge.unbind(element, "hidden.bs." + eventNamespace);
        BootstrapEventBridge.unbind(element, "inserted.bs." + eventNamespace);
    }
}
