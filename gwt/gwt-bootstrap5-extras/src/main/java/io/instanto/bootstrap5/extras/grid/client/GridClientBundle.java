package io.instanto.bootstrap5.extras.grid.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
/** Pinned vendor scripts for the Grid module. */
public interface GridClientBundle extends ClientBundle {
    GridClientBundle INSTANCE = GWT.create(GridClientBundle.class);
    @Source("resource/js/tabulator-6.5.2.min.js") TextResource script0();
}
