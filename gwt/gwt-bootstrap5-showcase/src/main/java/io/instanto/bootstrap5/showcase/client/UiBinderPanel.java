package io.instanto.bootstrap5.showcase.client;

import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.Composite;

import io.instanto.bootstrap5.client.ui.Panel;
import io.instanto.bootstrap5.client.ui.PanelFooter;
import io.instanto.bootstrap5.client.ui.Pre;

/**
 * A showcase panel whose widgets are declared in a UiBinder template rather than built in
 * Java, and whose code sample is that template's own text.
 *
 * <p>Two reasons for these to exist. Bootstrap 3's showcase is entirely UiBinder while
 * Bootstrap 5's was entirely programmatic, which is why UiBinder was broken on the
 * Bootstrap 5 track for its whole life without anyone noticing; keeping real templates in
 * this showcase means a regression fails the build. And reading the sample out of the
 * template removes the drift that the hand-copied Bootstrap 3 snippets suffer from.</p>
 */
public abstract class UiBinderPanel extends Composite {

    protected void init(final Panel panel, final TextResource source) {
        final PanelFooter footer = new PanelFooter();
        final Pre pre = new Pre(source.getText().trim());
        pre.addStyleName("mb-0 small");
        footer.add(pre);
        panel.add(footer);
        initWidget(panel);
    }
}
