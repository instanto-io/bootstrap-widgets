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
package io.instanto.bootstrap5.extras.datepicker.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasValue;
import io.instanto.bootstrap5.client.ui.TextBox;
import io.instanto.bootstrap5.client.ui.base.HasPlaceholder;
import io.instanto.bootstrap5.client.ui.constants.DeviceSize;
import io.instanto.bootstrap5.client.ui.html.Div;
import io.instanto.bootstrap5.extras.base.client.PluginWidget;
import java.util.Date;

/**
 * A date picker backed by Tempus Dominus 6.
 *
 * <p>This is a replacement rather than a port. GwtBootstrap3 wrapped bootstrap-datepicker and
 * bootstrap-datetimepicker, both of which render Bootstrap 3 markup, and the latter has been
 * unmaintained for years. Tempus Dominus is the maintained successor, targets Bootstrap 5 and needs
 * no jQuery. The property names that carry over -- placeholder, id, name, enabled, the responsive
 * helpers, {@code show()} and {@code hide()} -- keep the Bootstrap 3 spelling, so most calling code
 * reads the same.
 *
 * <p>The widget renders the input-group wrapper Tempus Dominus expects:
 *
 * <pre>
 * &lt;div class="input-group" id="..." data-td-target-input="nearest"&gt;
 *   &lt;input type="text" class="form-control"&gt;
 *   &lt;span class="input-group-text" data-td-target="#..." data-td-toggle="datetimepicker"&gt;
 * &lt;/div&gt;
 * </pre>
 */
public class DatePicker extends PluginWidget<DatePickerJs.TempusDominus>
    implements HasEnabled, HasName, HasPlaceholder, HasValue<Date> {

  private final TextBox input = new TextBox();

  private final Div toggle = new Div();

  private final String wrapperId;

  private double pendingMillis = -1;
  private boolean hasPendingValue;
  private boolean settingValue;
  private int callbackVersion;

  private String format;

  private boolean sideBySide;

  private boolean showClear = true;

  private boolean showClose = true;

  public DatePicker() {
    wrapperId = Document.get().createUniqueId();
    Element element = super.getElement();
    element.addClassName("input-group");
    element.setId(wrapperId);
    element.setAttribute("data-td-target-input", "nearest");
    element.setAttribute("data-td-target-toggle", "nearest");

    input.getElement().setAttribute("data-td-target", "#" + wrapperId);
    super.add(input, element);

    toggle.setStyleName("input-group-text");
    toggle.getElement().setAttribute("data-td-target", "#" + wrapperId);
    toggle.getElement().setAttribute("data-td-toggle", "datetimepicker");
    toggle.getElement().setInnerHTML("<i class=\"bi bi-calendar\"></i>");
    super.add(toggle, element);
  }

  public DatePicker(final String placeholder) {
    this();
    input.setPlaceholder(placeholder);
  }

  /** The text box the picker writes into. */
  public TextBox getTextBox() {
    return input;
  }

  /**
   * The display and parse format, in Tempus Dominus terms, e.g. {@code yyyy-MM-dd}. Must be set
   * before attach to take effect.
   */
  public void setFormat(final String format) {
    this.format = format;
  }

  public String getFormat() {
    return format;
  }

  /** Shows the date and clock panels together rather than switching between them. */
  public void setSideBySide(final boolean sideBySide) {
    this.sideBySide = sideBySide;
  }

  public void setShowClear(final boolean showClear) {
    this.showClear = showClear;
  }

  public void setShowClose(final boolean showClose) {
    this.showClose = showClose;
  }

  @Override
  protected void whenReady(Runnable action) {
    DatePickerJs.whenReady(action);
  }

  @Override
  protected DatePickerJs.TempusDominus createPlugin() {
    return DatePickerJs.create(getElement(), format, sideBySide, showClear, showClose);
  }

  @Override
  protected void afterCreate() {
    DatePickerJs.TempusDominus current = plugin();
    // A Java generation token also works when TeaVM wraps the generic plugin handle.
    final int version = ++callbackVersion;
    if (hasPendingValue) DatePickerJs.writeValue(current, pendingMillis);
    DatePickerJs.bindChange(
        current,
        millis -> {
          if (callbackVersion == version && isAttached() && !settingValue) onPickerChange(millis);
        });
  }

  @Override
  protected void captureState() {
    ++callbackVersion;
    pendingMillis = DatePickerJs.readValue(plugin());
    hasPendingValue = true;
  }

  @Override
  protected void destroyPlugin(DatePickerJs.TempusDominus previous) {
    DatePickerJs.dispose(previous);
  }

  public void show() {
    if (plugin() != null) {
      DatePickerJs.invoke(plugin(), "show");
    }
  }

  public void hide() {
    if (plugin() != null) {
      DatePickerJs.invoke(plugin(), "hide");
    }
  }

  public void toggle() {
    if (plugin() != null) {
      DatePickerJs.invoke(plugin(), "toggle");
    }
  }

  public void clear() {
    setValue(null, true);
  }

  @Override
  public Date getValue() {
    final double millis = plugin() == null ? pendingMillis : DatePickerJs.readValue(plugin());
    return millis < 0 ? null : new Date((long) millis);
  }

  @Override
  public void setValue(final Date value) {
    setValue(value, false);
  }

  @Override
  public void setValue(final Date value, final boolean fireEvents) {
    pendingMillis = value == null ? -1 : value.getTime();
    hasPendingValue = true;
    if (plugin() != null) {
      boolean previous = settingValue;
      settingValue = true;
      try {
        DatePickerJs.writeValue(plugin(), pendingMillis);
      } finally {
        settingValue = previous;
      }
    }
    if (fireEvents) {
      ValueChangeEvent.fire(this, value);
    }
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Date> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /** Called from the Tempus Dominus change subscription. */
  void onPickerChange(final double millis) {
    pendingMillis = millis;
    hasPendingValue = true;
    ValueChangeEvent.fire(this, millis < 0 ? null : new Date((long) millis));
  }

  // ---- the familiar Bootstrap 3 surface -----------------------------------

  @Override
  public void setPlaceholder(final String placeholder) {
    input.setPlaceholder(placeholder);
  }

  @Override
  public String getPlaceholder() {
    return input.getPlaceholder();
  }

  @Override
  public void setEnabled(final boolean enabled) {
    input.setEnabled(enabled);
  }

  @Override
  public boolean isEnabled() {
    return input.isEnabled();
  }

  @Override
  public void setName(final String name) {
    input.setName(name);
  }

  @Override
  public String getName() {
    return input.getName();
  }

  @Override
  public void setId(final String id) {
    input.setId(id);
  }

  @Override
  public String getId() {
    return input.getId();
  }

  @Override
  public void setVisibleOn(final DeviceSize deviceSize) {
    io.instanto.bootstrap5.client.ui.base.helper.StyleHelper.setVisibleOn(this, deviceSize);
  }

  @Override
  public void setHiddenOn(final DeviceSize deviceSize) {
    io.instanto.bootstrap5.client.ui.base.helper.StyleHelper.setHiddenOn(this, deviceSize);
  }

  // ---- Tempus Dominus ------------------------------------------------------
}
