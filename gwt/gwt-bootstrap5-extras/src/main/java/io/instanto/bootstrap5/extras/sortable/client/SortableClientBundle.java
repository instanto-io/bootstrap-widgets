package io.instanto.bootstrap5.extras.sortable.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
/** Pinned vendor scripts for the Sortable module. */
public interface SortableClientBundle extends ClientBundle {
    SortableClientBundle INSTANCE = GWT.create(SortableClientBundle.class);
    @Source("resource/js/sortable-1.15.7.min.js") TextResource script0();
}
