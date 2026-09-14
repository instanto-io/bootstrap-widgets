package io.instanto.bootstrap5.client.ui;

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

import io.instanto.bootstrap5.client.ui.base.HasActive;
import io.instanto.bootstrap5.client.ui.base.HasIcon;
import io.instanto.bootstrap5.client.ui.base.HasIconPosition;
import io.instanto.bootstrap5.client.ui.base.HasSize;
import io.instanto.bootstrap5.client.ui.base.HasType;
import io.instanto.bootstrap5.client.ui.constants.ButtonSize;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;
import io.instanto.bootstrap5.client.ui.constants.IconFlip;
import io.instanto.bootstrap5.client.ui.constants.IconPosition;
import io.instanto.bootstrap5.client.ui.constants.IconRotate;
import io.instanto.bootstrap5.client.ui.constants.IconSize;
import io.instanto.bootstrap5.client.ui.constants.IconType;
import io.instanto.bootstrap5.client.ui.constants.Styles;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;

/**
 * Button representing a radio button used within a {@link ButtonGroup} that has
 * toggle set to {@code Toogle.BUTTONS}.
 * <p/>
 * If you are looking for a classic radio button see {@link RadioButton}.
 *
 * @author Sven Jacobs
 */
public class RadioButton extends Radio implements HasActive,
        HasType<ButtonType>, HasSize<ButtonSize>, HasIcon, HasIconPosition {

    private final LabelElement buttonLabel = Document.get().createLabelElement();
    private IconPosition iconPosition = IconPosition.LEFT;
    private Icon icon;
    private ButtonType type;
    private boolean outline;


    /**
     * Creates a new radio associated with a particular group, and initialized
     * with the given HTML label. All radio buttons associated with the same
     * group name belong to a mutually-exclusive set.
     *
     * Radio buttons are grouped by their name attribute, so changing their name
     * using the setName() method will also change their associated group.
     *
     * @param name
     *            the group name with which to associate the radio button
     * @param label
     *            this radio button's html label
     */
    public RadioButton(String name, SafeHtml label) {
        this(name, label.asString(), true);
    }

    /**
     * @see #RadioButtonToggle(String, SafeHtml)
     *
     * @param name
     *            the group name with which to associate the radio button
     * @param label
     *            this radio button's html label
     * @param dir
     *            the text's direction. Note that {@code DEFAULT} means
     *            direction should be inherited from the widget's parent
     *            element.
     */
    public RadioButton(String name, SafeHtml label, Direction dir) {
        this(name);
        setHTML(label, dir);
    }

    /**
     * @see #RadioButtonToggle(String, SafeHtml)
     *
     * @param name
     *            the group name with which to associate the radio button
     * @param label
     *            this radio button's html label
     * @param directionEstimator
     *            A DirectionEstimator object used for automatic direction
     *            adjustment. For convenience,
     *            {@link #DEFAULT_DIRECTION_ESTIMATOR} can be used.
     */
    public RadioButton(String name, SafeHtml label, DirectionEstimator directionEstimator) {
        this(name);
        setDirectionEstimator(directionEstimator);
        setHTML(label.asString());
    }

    /**
     * Creates a new radio associated with a particular group, and initialized
     * with the given HTML label. All radio buttons associated with the same
     * group name belong to a mutually-exclusive set.
     *
     * Radio buttons are grouped by their name attribute, so changing their name
     * using the setName() method will also change their associated group.
     *
     * @param name
     *            the group name with which to associate the radio button
     * @param label
     *            this radio button's label
     */
    public RadioButton(String name, String label) {
        this(name);
        setText(label);
    }

    /**
     * @see #RadioButtonToggle(String, SafeHtml)
     *
     * @param name
     *            the group name with which to associate the radio button
     * @param label
     *            this radio button's label
     * @param dir
     *            the text's direction. Note that {@code DEFAULT} means
     *            direction should be inherited from the widget's parent
     *            element.
     */
    public RadioButton(String name, String label, Direction dir) {
        this(name);
        setText(label, dir);
    }

    /**
     * @see #RadioButtonToggle(String, SafeHtml)
     *
     * @param name
     *            the group name with which to associate the radio button
     * @param label
     *            this radio button's label
     * @param directionEstimator
     *            A DirectionEstimator object used for automatic direction
     *            adjustment. For convenience,
     *            {@link #DEFAULT_DIRECTION_ESTIMATOR} can be used.
     */
    public RadioButton(String name, String label, DirectionEstimator directionEstimator) {
        this(name);
        setDirectionEstimator(directionEstimator);
        setText(label);
    }

    /**
     * Creates a new radio button associated with a particular group, and
     * initialized with the given label (optionally treated as HTML). All radio
     * buttons associated with the same group name belong to a
     * mutually-exclusive set.
     *
     * Radio buttons are grouped by their name attribute, so changing their name
     * using the setName() method will also change their associated group.
     *
     * @param name
     *            name the group with which to associate the radio button
     * @param label
     *            this radio button's label
     * @param asHTML
     *            <code>true</code> to treat the specified label as HTML
     */
    public RadioButton(String name, String label, boolean asHTML) {
        this(name);
        if (asHTML) {
            setHTML(label);
        } else {
            setText(label);
        }
    }

    @UiConstructor
    public RadioButton(String name) {
        this(Document.get().createRadioInputElement(name));
    }

    protected RadioButton(InputElement element) {
        super(DOM.createSpan(), element);

        setStyleName("d-inline-block");
        inputElem.setClassName("btn-check");
        inputElem.setAttribute("autocomplete", "off");
        inputElem.setId(Document.get().createUniqueId());
        buttonLabel.setClassName(Styles.BTN);
        buttonLabel.setHtmlFor(inputElem.getId());
        setType(ButtonType.DEFAULT);

        getElement().appendChild(inputElem);
        buttonLabel.appendChild(labelElem);
        getElement().appendChild(buttonLabel);
        addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                updatePressedState();
            }
        });
    }

    @Override
    protected void ensureDomEventHandlers() {
        super.ensureDomEventHandlers();
    }

    @Override
    public void setSize(ButtonSize size) {
        for (ButtonSize candidate : ButtonSize.values()) {
            if (!candidate.getCssName().isEmpty()) {
                buttonLabel.removeClassName(candidate.getCssName());
            }
        }
        if (size != null && !size.getCssName().isEmpty()) {
            buttonLabel.addClassName(size.getCssName());
        }
    }

    @Override
    public ButtonSize getSize() {
        return ButtonSize.fromStyleName(buttonLabel.getClassName());
    }

    @Override
    public void setType(ButtonType type) {
        if (this.type != null) {
            buttonLabel.removeClassName(buttonStyle(this.type, outline));
        }
        this.type = type == null ? ButtonType.DEFAULT : type;
        buttonLabel.addClassName(buttonStyle(this.type, outline));
    }

    @Override
    public ButtonType getType() {
        return type == null ? ButtonType.DEFAULT : type;
    }

    public void setOutline(boolean outline) {
        ButtonType currentType = getType();
        buttonLabel.removeClassName(buttonStyle(currentType, this.outline));
        this.outline = outline;
        buttonLabel.addClassName(buttonStyle(currentType, this.outline));
    }

    public boolean isOutline() {
        return outline;
    }

    @Override
    public void setActive(boolean active) {
        setValue(active);
    }

    @Override
    public boolean isActive() {
        return getValue();
    }

    @Override
    public void setValue(Boolean value, boolean fireEvents) {
        super.setValue(value, fireEvents);
        updatePressedState();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            buttonLabel.removeClassName("disabled");
        } else {
            buttonLabel.addClassName("disabled");
        }
        buttonLabel.setAttribute("aria-disabled", Boolean.toString(!enabled));
    }

    @Override
    public void setIconPosition(IconPosition iconPosition) {
        this.iconPosition = iconPosition;
        render();
    }

    @Override
    public IconPosition getIconPosition() {
        return iconPosition;
    }

    @Override
    public void setIcon(IconType iconType) {
        getActualIcon().setType(iconType);
    }

    @Override
    public IconType getIcon() {
        return getActualIcon().getType();
    }

    @Override
    public void setIconSize(IconSize iconSize) {
        getActualIcon().setSize(iconSize);
    }

    @Override
    public IconSize getIconSize() {
        return getActualIcon().getSize();
    }

    @Override
    public void setIconFlip(IconFlip iconFlip) {
        getActualIcon().setFlip(iconFlip);
    }

    @Override
    public IconFlip getIconFlip() {
        return getActualIcon().getFlip();
    }

    @Override
    public void setIconRotate(IconRotate iconRotate) {
        getActualIcon().setRotate(iconRotate);
    }

    @Override
    public IconRotate getIconRotate() {
        return getActualIcon().getRotate();
    }

    @Override
    public void setIconBordered(boolean iconBordered) {
        getActualIcon().setBorder(iconBordered);
    }

    @Override
    public boolean isIconBordered() {
        return getActualIcon().isBorder();
    }

    /** {@inheritDoc} */
    @Override
    public void setIconInverse(final boolean iconInverse) {
        getActualIcon().setInverse(iconInverse);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isIconInverse() {
        return getActualIcon().isInverse();
    }

    @Override
    public void setIconSpin(boolean iconSpin) {
        getActualIcon().setSpin(iconSpin);
    }

    @Override
    public boolean isIconSpin() {
        return getActualIcon().isSpin();
    }

    @Override
    public void setIconPulse(boolean iconPulse) {
        getActualIcon().setPulse(iconPulse);
    }

    @Override
    public boolean isIconPulse() {
        return getActualIcon().isPulse();
    }

    @Override
    public void setIconFixedWidth(boolean iconFixedWidth) {
        getActualIcon().setFixedWidth(iconFixedWidth);
    }

    @Override
    public boolean isIconFixedWidth() {
        return getActualIcon().isFixedWidth();
    }

    private Icon getActualIcon() {
        if (icon == null) {
            icon = new Icon();
            render();
        }
        return icon;
    }

    private void render() {
        if (iconPosition == IconPosition.LEFT) {
            buttonLabel.insertBefore(icon.getElement(), labelElem);
        } else {
            buttonLabel.appendChild(icon.getElement());
        }
    }

    @Override
    public void setIconColor(String iconColor) {
        getActualIcon().setColor(iconColor);
    }

    private static String buttonStyle(ButtonType type, boolean outline) {
        String cssName = type.getCssName();
        return outline && type != ButtonType.LINK
                ? "btn-outline-" + cssName.substring("btn-".length()) : cssName;
    }

    /**
     * Mirrors the checked state onto the label as .active.
     *
     * <p>Bootstrap 5 styles {@code .btn.active} with the very same rule as
     * {@code .btn-check:checked + .btn}, so this changes nothing about how the
     * button looks. What it changes is how the browser finds out. Relying on the
     * sibling combinator alone leaves the label's style dependent on a state
     * change on another element, and WebKit does not reliably invalidate that on
     * touch: a toggle would stay looking pressed after being switched off until
     * a scroll forced a repaint. Setting a class on the label is a direct
     * mutation, which always invalidates.</p>
     *
     * <p>This is what GwtBootstrap3 did through ActiveMixin; it was lost when
     * the widget moved to the btn-check markup.</p>
     */
    private void updatePressedState() {
        if (!isAttached()) {
            setLabelActive(buttonLabel, Boolean.TRUE.equals(getValue()));
            return;
        }

        // Selecting a radio unchecks its peer inputs without dispatching change
        // events to them. Refresh every same-name label from native input state.
        final NodeList<com.google.gwt.dom.client.Element> radios = Document.get().getBody()
                .getElementsByTagName("input");
        for (int i = 0; i < radios.getLength(); i++) {
            final com.google.gwt.dom.client.Element radio = radios.getItem(i);
            if (!"radio".equals(radio.getAttribute("type"))
                    || !getName().equals(radio.getAttribute("name"))) {
                continue;
            }
            final com.google.gwt.dom.client.Element label = radio.getNextSiblingElement();
            if (label != null && label.hasClassName(Styles.BTN)) {
                setLabelActive(label, InputElement.as(radio).isChecked());
            }
        }
    }

    private static void setLabelActive(com.google.gwt.dom.client.Element label, boolean active) {
        if (active) {
            label.addClassName("active");
        } else {
            label.removeClassName("active");
        }
        // aria-pressed is not valid on a label; the input already conveys state.
        label.removeAttribute("aria-pressed");
    }

}
