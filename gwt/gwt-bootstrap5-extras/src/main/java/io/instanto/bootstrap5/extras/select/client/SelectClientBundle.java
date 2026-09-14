package io.instanto.bootstrap5.extras.select.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
/** Pinned vendor scripts for the Select module. */
public interface SelectClientBundle extends ClientBundle {
    SelectClientBundle INSTANCE = GWT.create(SelectClientBundle.class);
    @Source("resource/js/tom-select-2.6.2.min.js") TextResource script0();
}
