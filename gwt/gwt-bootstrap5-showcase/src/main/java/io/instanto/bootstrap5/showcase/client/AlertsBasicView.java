package io.instanto.bootstrap5.showcase.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.uibinder.client.UiBinder;

import io.instanto.bootstrap5.client.ui.Panel;

/** The Alerts "Basic" panel, declared in UiBinder. */
public class AlertsBasicView extends UiBinderPanel {

    interface Binder extends UiBinder<Panel, AlertsBasicView> { }

    interface Source extends ClientBundle {
        @com.google.gwt.resources.client.ClientBundle.Source("AlertsBasicView.ui.xml")
        TextResource template();
    }

    private static final Binder BINDER = GWT.create(Binder.class);
    private static final Source SOURCE = GWT.create(Source.class);

    public AlertsBasicView() {
        init(BINDER.createAndBindUi(this), SOURCE.template());
    }
}
