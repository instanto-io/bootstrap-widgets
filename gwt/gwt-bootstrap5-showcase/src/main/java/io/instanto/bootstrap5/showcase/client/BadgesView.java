package io.instanto.bootstrap5.showcase.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.uibinder.client.UiBinder;

import io.instanto.bootstrap5.client.ui.Panel;

/** The Badges panel, declared in UiBinder. */
public class BadgesView extends UiBinderPanel {

    interface Binder extends UiBinder<Panel, BadgesView> { }

    interface Source extends ClientBundle {
        @com.google.gwt.resources.client.ClientBundle.Source("BadgesView.ui.xml")
        TextResource template();
    }

    private static final Binder BINDER = GWT.create(Binder.class);
    private static final Source SOURCE = GWT.create(Source.class);

    public BadgesView() {
        init(BINDER.createAndBindUi(this), SOURCE.template());
    }
}
