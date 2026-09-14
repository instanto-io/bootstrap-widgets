package io.instanto.bootstrap5.showcase.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * The UiBinder template, embedded as text so the showcase can display the file it actually
 * renders from. Hand-copied snippets drift from the code beside them; this one cannot.
 */
public interface ProbeSource extends ClientBundle {

    ProbeSource INSTANCE = GWT.create(ProbeSource.class);

    @Source("UiBinderProbe.ui.xml")
    TextResource template();
}
