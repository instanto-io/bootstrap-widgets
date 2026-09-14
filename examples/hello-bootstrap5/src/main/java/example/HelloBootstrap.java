package example;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.bootstrap5.client.Bootstrap5;
import io.instanto.bootstrap5.client.Bootstrap5Resources;
import io.instanto.bootstrap5.client.ui.*;

public final class HelloBootstrap {
    public static void main(String[] args) {
        Bootstrap5Resources.setAssetBase("assets/bootstrap5/");
        Bootstrap5.initialise(() -> {
            TextBox name = new TextBox();
            name.setPlaceholder("Your name");
            Button greet = new Button("Greet");
            Label result = new Label("Ready");
            greet.addClickHandler(event -> result.setText("Hello " + name.getValue()));
            Container container = new Container();
            Row row = new Row();
            Column column = new Column(12);
            column.setMediumSpan(6);
            column.add(name);
            column.add(greet);
            column.add(result);
            row.add(column);
            container.add(row);
            RootPanel.get().add(container);
        });
    }
}
