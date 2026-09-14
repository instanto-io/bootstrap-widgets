package io.instanto.bootstrap5.showcase.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.client.ui.CheckBox;
import io.instanto.bootstrap5.client.ui.TextBox;

/** Proves the Bootstrap 5 widgets are usable from a UiBinder template. */
public class UiBinderProbe extends Composite {

    interface Binder extends UiBinder<Widget, UiBinderProbe> { }

    private static final Binder BINDER = GWT.create(Binder.class);

    @UiField Button counter;
    @UiField TextBox box;
    @UiField CheckBox check;
    @UiField io.instanto.bootstrap5.client.ui.Carousel carousel;

    private int clicks;

    public UiBinderProbe() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @UiHandler("counter")
    void onCounterClick(ClickEvent event) {
        clicks++;
        counter.setText("Clicked " + clicks + " times");
        box.setValue("counter is at " + clicks);
        check.setValue(clicks % 2 == 0);
    }
}
