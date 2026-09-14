package io.instanto.bootstrap5.showcase.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;

import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.client.ui.PanelBody;
import io.instanto.bootstrap5.client.ui.TextBox;

/** Shared UiBinder example showing field binding and click handlers. */
public class UiBinderDemo extends Composite {

    interface Binder extends UiBinder<PanelBody, UiBinderDemo> { }

    private static final Binder BINDER = GWT.create(Binder.class);

    @UiField Button counter;
    @UiField TextBox box;

    private int clicks;

    public UiBinderDemo() {
        initWidget(BINDER.createAndBindUi(this));
        counter.getElement().setAttribute("data-testid", "counter");
        counter.getElement().setAttribute("aria-label", "Click counter");
    }

    @UiHandler("counter")
    void onCounterClick(ClickEvent event) {
        clicks++;
        counter.setText("Clicked " + clicks + " times");
        box.setValue("the handler ran " + clicks + " times");
    }
}
