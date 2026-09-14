package io.instanto.bootstrap5.client.ui.base.button;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 GwtBootstrap3
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

import java.util.List;

import io.instanto.bootstrap5.client.ui.base.ComplexWidget;
import io.instanto.bootstrap5.client.ui.base.HasActive;
import io.instanto.bootstrap5.client.ui.base.HasDataTarget;
import io.instanto.bootstrap5.client.ui.base.HasSize;
import io.instanto.bootstrap5.client.ui.base.HasType;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.base.mixin.ActiveMixin;
import io.instanto.bootstrap5.client.ui.base.mixin.DataTargetMixin;
import io.instanto.bootstrap5.client.ui.base.mixin.EnabledMixin;
import io.instanto.bootstrap5.client.ui.base.mixin.FocusableMixin;
import io.instanto.bootstrap5.client.ui.constants.Attributes;
import io.instanto.bootstrap5.client.ui.constants.ButtonDismiss;
import io.instanto.bootstrap5.client.ui.constants.ButtonSize;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;
import io.instanto.bootstrap5.client.ui.constants.Styles;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.Variant;

/**
 * Abstract base class for different kinds of buttons.
 *
 * @author Sven Jacobs
 * @author Joshua Godi
 */
public abstract class AbstractButton extends ComplexWidget implements HasEnabled, HasActive, HasType<ButtonType>,
        HasSize<ButtonSize>, HasDataTarget, HasClickHandlers, Focusable, HasAllMouseHandlers {

    public class ButtonStateHandler {
        private ButtonStateHandler() {
        }

        public void loading() {
            setLoading(true);
        }

        public void reset() {
            setLoading(false);
        }

        /**
         * Resets button to specified text state.
         *
         * @param state Text state
         */
        public void reset(final String state) {
            setButtonText(state);
            normalText = state == null ? "" : state;
            setEnabled(true);
            getElement().removeAttribute("aria-busy");
            loading = false;
        }
    }

    private final ButtonStateHandler buttonStateHandler = new ButtonStateHandler();
    private final DataTargetMixin<AbstractButton> targetMixin = new DataTargetMixin<AbstractButton>(this);
    private final ActiveMixin<AbstractButton> activeMixin = new ActiveMixin<AbstractButton>(this);
    private final FocusableMixin<AbstractButton> focusableMixin = new FocusableMixin<AbstractButton>(this);
    private final EnabledMixin<AbstractButton> enabledMixin = new EnabledMixin<AbstractButton>(this);
    private String normalText = "";
    private String loadingText;
    private boolean loading;
    private ButtonType type = ButtonType.DEFAULT;

    /**
     * Creates button with DEFAULT type.
     */
    protected AbstractButton() {
        this(ButtonType.DEFAULT);
    }

    protected AbstractButton(final ButtonType type) {
        setElement(createElement());
        setStyleName(Styles.BTN);
        setType(type);
    }

    @Override
    public boolean isActive() {
        return activeMixin.isActive();
    }

    @Override
    public void setActive(final boolean active) {
        activeMixin.setActive(active);
        getElement().setAttribute("aria-pressed", Boolean.toString(active));
    }

    @Override
    public void setEnabled(final boolean enabled) {
        enabledMixin.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return enabledMixin.isEnabled();
    }

    @Override
    public HandlerRegistration addClickHandler(final ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    /**
     * Sets type of button.
     *
     * @param type Type of button
     */
    @Override
    public void setType(final ButtonType type) {
        removeVariantStyles();
        final ButtonType effective = type == null ? ButtonType.DEFAULT : type;
        this.type = effective;
        StyleHelper.addUniqueEnumStyleName(this, ButtonType.class, effective);
        setVariantField(variantFor(effective));
    }

    @Override
    public ButtonType getType() {
        return type;
    }

    /**
     * Sets size of button.
     *
     * @param size Size of button
     */
    @Override
    public void setSize(final ButtonSize size) {
        StyleHelper.addUniqueEnumStyleName(this, ButtonSize.class, size);
    }

    @Override
    public ButtonSize getSize() {
        return ButtonSize.fromStyleName(getStyleName());
    }

    @Override
    public void setDataTargetWidgets(final List<Widget> widgets) {
        targetMixin.setDataTargetWidgets(widgets);
    }

    @Override
    public void setDataTargetWidget(final Widget widget) {
        targetMixin.setDataTargetWidget(widget);
    }

    @Override
    public void setDataTarget(final String dataTarget) {
        targetMixin.setDataTarget(dataTarget);
    }

    @Override
    public String getDataTarget() {
        return targetMixin.getDataTarget();
    }

    @Override
    public int getTabIndex() {
        return focusableMixin.getTabIndex();
    }

    @Override
    public void setAccessKey(final char key) {
        focusableMixin.setAccessKey(key);
    }

    @Override
    public void setFocus(final boolean focused) {
        focusableMixin.setFocus(focused);
    }

    @Override
    public void setTabIndex(final int index) {
        focusableMixin.setTabIndex(index);
    }

    @Override
    public HandlerRegistration addMouseDownHandler(final MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseMoveHandler(final MouseMoveHandler handler) {
        return addDomHandler(handler, MouseMoveEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseUpHandler(final MouseUpHandler handler) {
        return addDomHandler(handler, MouseUpEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseWheelHandler(final MouseWheelHandler handler) {
        return addDomHandler(handler, MouseWheelEvent.getType());
    }

    /**
     * Makes button a block level element that spawns full width of parent.
     *
     * @param block True for block level element
     */
    public void setBlock(final boolean block) {
        setStyleName("d-block", block);
        setStyleName("w-100", block);
    }

    /**
     * Sets dismiss type of button.
     * <p/>
     * If button is inside a
     * {@link io.instanto.bootstrap5.client.ui.Modal} and dismiss type is
     * {@code MODAL} the button will act as the dismiss (close) button for this
     * Modal. Same goes for {@link io.instanto.bootstrap5.client.ui.Alert}
     * and dismiss type {@code ALERT}.
     *
     * @param dismiss Dismiss type of button
     * @see io.instanto.bootstrap5.client.ui.Modal
     * @see io.instanto.bootstrap5.client.ui.Alert
     */
    public void setDataDismiss(final ButtonDismiss dismiss) {
        if (dismiss != null) {
            getElement().setAttribute(Attributes.DATA_DISMISS, dismiss.getDismiss());
        } else {
            getElement().removeAttribute(Attributes.DATA_DISMISS);
        }
    }

    public void setDataLoadingText(final String loadingText) {
        this.loadingText = loadingText;
        if (loadingText != null) {
            getElement().setAttribute(Attributes.DATA_LOADING_TEXT, loadingText);
        } else {
            getElement().removeAttribute(Attributes.DATA_LOADING_TEXT);
        }
    }

    public void setLoadingText(final String loadingText) {
        setDataLoadingText(loadingText);
    }

    public void toggle() {
        setActive(!isActive());
    }

    public ButtonStateHandler state() {
        return buttonStateHandler;
    }

    public void click() {
        final NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
        DomEvent.fireNativeEvent(event, this);
    }

    protected abstract Element createElement();

    public String getLoadingText() {
        return loadingText;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(final boolean loading) {
        if (loading) {
            if (!this.loading) {
                normalText = getButtonText();
            }
            if (loadingText != null && !loadingText.isEmpty()) {
                setButtonText(loadingText);
            }
            setEnabled(false);
            getElement().setAttribute("aria-busy", "true");
        } else {
            setButtonText(normalText);
            setEnabled(true);
            getElement().removeAttribute("aria-busy");
        }
        this.loading = loading;
    }

    private String getButtonText() {
        return this instanceof HasText ? ((HasText) this).getText() : getElement().getInnerText();
    }

    private void setButtonText(String text) {
        if (this instanceof HasText) {
            ((HasText) this).setText(text == null ? "" : text);
        } else {
            getElement().setInnerText(text == null ? "" : text);
        }
    }

    private Variant variant;
    private boolean outline;

    /**
     * Bootstrap 5 names button colours btn-primary .. btn-dark plus btn-link,
     * with a btn-outline-* counterpart for each. {@link Variant} is that
     * vocabulary; {@link ButtonType} is the Bootstrap 3 spelling kept for
     * source compatibility, and setting one keeps the other in step.
     */
    public void setVariant(final Variant variant) {
        removeVariantStyles();
        this.variant = variant == null ? Variant.SECONDARY : variant;
        this.type = typeFor(this.variant);
        addStyleName(variantStyleName(this.variant, outline));
    }

    public Variant getVariant() {
        return variant == null ? Variant.SECONDARY : variant;
    }

    public void setOutline(final boolean outline) {
        if (this.outline == outline) {
            return;
        }
        removeVariantStyles();
        this.outline = outline;
        addStyleName(variantStyleName(getVariant(), outline));
    }

    public boolean isOutline() {
        return outline;
    }

    public void setLarge(final boolean large) {
        setStyleName("btn-lg", large);
    }

    public void setSmall(final boolean small) {
        setStyleName("btn-sm", small);
    }

    protected void removeVariantStyles() {
        for (final Variant candidate : Variant.values()) {
            removeStyleName(variantStyleName(candidate, false));
            removeStyleName(variantStyleName(candidate, true));
        }
    }

    protected static Variant variantFor(final ButtonType type) {
        if (type == null) {
            return Variant.SECONDARY;
        }
        switch (type) {
            case PRIMARY:  return Variant.PRIMARY;
            case SUCCESS:  return Variant.SUCCESS;
            case INFO:     return Variant.INFO;
            case WARNING:  return Variant.WARNING;
            case DANGER:   return Variant.DANGER;
            case LIGHT:    return Variant.LIGHT;
            case DARK:     return Variant.DARK;
            case LINK:     return Variant.LINK;
            case SECONDARY:
            case DEFAULT:
            default:       return Variant.SECONDARY;
        }
    }

    private static ButtonType typeFor(final Variant variant) {
        if (variant == null) {
            return ButtonType.SECONDARY;
        }
        switch (variant) {
            case PRIMARY:   return ButtonType.PRIMARY;
            case SUCCESS:   return ButtonType.SUCCESS;
            case INFO:      return ButtonType.INFO;
            case WARNING:   return ButtonType.WARNING;
            case DANGER:    return ButtonType.DANGER;
            case LIGHT:     return ButtonType.LIGHT;
            case DARK:      return ButtonType.DARK;
            case LINK:      return ButtonType.LINK;
            case SECONDARY:
            default:        return ButtonType.SECONDARY;
        }
    }

    protected static String variantStyleName(final Variant variant, final boolean outline) {
        final Variant effective = variant == null ? Variant.SECONDARY : variant;
        return outline ? "btn-outline-" + effective.cssName() : "btn-" + effective.cssName();
    }

    protected void setVariantField(final Variant variant) {
        this.variant = variant;
    }

}
