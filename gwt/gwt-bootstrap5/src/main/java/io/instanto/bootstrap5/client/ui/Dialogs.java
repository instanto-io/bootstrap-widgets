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

import io.instanto.bootstrap5.client.shared.event.ModalHiddenEvent;
import io.instanto.bootstrap5.client.shared.event.ModalHiddenHandler;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Alert, confirm and prompt dialogs, built on {@link Modal}.
 *
 * <p>GwtBootstrap3 got these from Bootbox, a jQuery plugin. Bootstrap 5 has its
 * own JavaScript and no jQuery bridge, so wrapping Bootbox meant shipping jQuery
 * and re-registering Bootstrap's components as jQuery plugins to satisfy it.
 * That is a lot of machinery for three dialogs that {@link Modal} can already
 * draw, so they are built here instead and the library stays jQuery-free.</p>
 *
 * <p>Each dialog is created on demand, shown, and removed from the DOM once it
 * has closed.</p>
 */
public final class Dialogs {

    /** Notified when an alert is dismissed. */
    public interface SimpleCallback {
        void callback();
    }

    /** Notified with the answer to a confirm. */
    public interface ConfirmCallback {
        void callback(boolean confirmed);
    }

    /** Notified with the entered text, or null if the prompt was cancelled. */
    public interface PromptCallback {
        void callback(String value);
    }

    private static String okLabel = "OK";

    private static String cancelLabel = "Cancel";

    private Dialogs() {
    }

    /** Sets the button labels used by every dialog, for applications that translate. */
    public static void setLabels(final String ok, final String cancel) {
        okLabel = ok == null ? "OK" : ok;
        cancelLabel = cancel == null ? "Cancel" : cancel;
    }

    public static void alert(final String message) {
        alert(message, null);
    }

    public static void alert(final String message, final SimpleCallback callback) {
        final Modal modal = dialog(message);
        final ModalFooter footer = new ModalFooter();
        footer.add(closingButton(okLabel, ButtonType.PRIMARY, modal));
        modal.addFooter(footer);
        modal.addHiddenHandler(new ModalHiddenHandler() {
            @Override
            public void onHidden(final ModalHiddenEvent event) {
                if (callback != null) {
                    callback.callback();
                }
            }
        });
        open(modal);
    }

    public static void confirm(final String message, final ConfirmCallback callback) {
        final Modal modal = dialog(message);
        final boolean[] confirmed = new boolean[1];

        final Button ok = new Button(okLabel, ButtonType.PRIMARY);
        ok.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                confirmed[0] = true;
                modal.hide();
            }
        });

        final ModalFooter footer = new ModalFooter();
        footer.add(closingButton(cancelLabel, ButtonType.DEFAULT, modal));
        footer.add(ok);
        modal.addFooter(footer);

        modal.addHiddenHandler(new ModalHiddenHandler() {
            @Override
            public void onHidden(final ModalHiddenEvent event) {
                if (callback != null) {
                    callback.callback(confirmed[0]);
                }
            }
        });
        open(modal);
    }

    public static void prompt(final String message, final PromptCallback callback) {
        prompt(message, "", callback);
    }

    public static void prompt(final String message, final String initialValue,
            final PromptCallback callback) {
        final Modal modal = dialog(message);
        final boolean[] accepted = new boolean[1];

        final TextBox input = new TextBox();
        input.setValue(initialValue == null ? "" : initialValue);
        modal.addToBody(input);

        final Button ok = new Button(okLabel, ButtonType.PRIMARY);
        ok.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                accepted[0] = true;
                modal.hide();
            }
        });
        input.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(final KeyUpEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    accepted[0] = true;
                    modal.hide();
                }
            }
        });

        final ModalFooter footer = new ModalFooter();
        footer.add(closingButton(cancelLabel, ButtonType.DEFAULT, modal));
        footer.add(ok);
        modal.addFooter(footer);

        modal.addShownHandler(new io.instanto.bootstrap5.client.shared.event.ModalShownHandler() {
            @Override
            public void onShown(final io.instanto.bootstrap5.client.shared.event.ModalShownEvent event) {
                input.setFocus(true);
            }
        });
        modal.addHiddenHandler(new ModalHiddenHandler() {
            @Override
            public void onHidden(final ModalHiddenEvent event) {
                if (callback != null) {
                    callback.callback(accepted[0] ? input.getValue() : null);
                }
            }
        });
        open(modal);
    }

    private static Modal dialog(final String message) {
        final Modal modal = new Modal();
        modal.setRemoveOnHide(true);
        modal.addToBody(new Paragraph(message == null ? "" : message));
        return modal;
    }

    private static Button closingButton(final String text, final ButtonType type, final Modal modal) {
        final Button button = new Button(text, type);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                modal.hide();
            }
        });
        return button;
    }

    private static void open(final Modal modal) {
        RootPanel.get().add(modal);
        modal.show();
    }
}
