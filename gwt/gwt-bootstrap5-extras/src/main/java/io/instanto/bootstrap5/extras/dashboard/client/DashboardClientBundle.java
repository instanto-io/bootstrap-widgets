package io.instanto.bootstrap5.extras.dashboard.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
/** Pinned vendor scripts for the Dashboard module. */
public interface DashboardClientBundle extends ClientBundle {
    DashboardClientBundle INSTANCE = GWT.create(DashboardClientBundle.class);
    @Source("resource/js/gridstack-13.2.0.js") TextResource script0();
}
