package io.instanto.bootstrap5.showcase.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.client.ui.Paragraph;
import io.instanto.bootstrap5.client.ui.html.Div;
import io.instanto.bootstrap5.extras.select.client.ui.*;
import io.instanto.bootstrap5.extras.grid.client.ui.*;
import io.instanto.bootstrap5.extras.sortable.client.ui.SortableList;
import io.instanto.bootstrap5.extras.dashboard.client.ui.*;
import io.instanto.bootstrap5.extras.gallery.client.ui.ImageGallery;
import java.util.*;

/** Live integration examples shared by both showcase builds. */
final class IntegrationExamples {
    private IntegrationExamples() { }
    static Widget selects() {
        Div examples = stack();
        SearchableSelect tags = new SearchableSelect();
        tags.setMultiple(true);
        tags.setAllowCreate(true);
        tags.setPlaceholder("Choose skills or type a new one");
        tags.addOption("java", "Java");
        tags.addOption("sql", "SQL");
        tags.addOption("css", "CSS");
        tags.setValue(Arrays.asList("java"));
        Paragraph status = new Paragraph("Selected: java");
        tags.addValueChangeHandler(event -> status.setText("Selected: " + event.getValue()));
        examples.add(new Paragraph("Skills: type to search, press Enter to create a tag, or remove a selection."));
        examples.add(tags);
        examples.add(status);
        Button disable = new Button("Enable / disable selection");
        disable.addClickHandler(event -> tags.setEnabled(!tags.isEnabled()));
        examples.add(disable);

        SearchableSelect remote = new SearchableSelect();
        remote.setPlaceholder("Search the team directory");
        remote.setDataProvider((query, result) -> new Timer() {
            @Override public void run() {
                List<SelectOption> matches = new ArrayList<>();
                for (String name : Arrays.asList("Ada Lovelace", "Grace Hopper", "Alan Turing", "Margaret Hamilton")) {
                    if (name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                        matches.add(new SelectOption(name, name));
                }
                result.onSuccess(matches);
            }
        }.schedule(250));
        examples.add(new Paragraph("Asynchronous options: this example simulates a directory lookup with a short delay."));
        examples.add(remote);
        return examples;
    }

    static Widget grid() {
        Div examples = stack();
        DataTable table = new DataTable();
        GridColumn name = new GridColumn("Name", "name");
        name.setEditable(true);
        table.setColumns(Arrays.asList(name, new GridColumn("Team", "team"), new GridColumn("Location", "location")));
        List<Map<String, ?>> rows = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", i + 1);
            row.put("name", "Colleague " + (i + 1));
            row.put("team", i % 2 == 0 ? "Engineering" : "Operations");
            row.put("location", i % 3 == 0 ? "London" : "Paris");
            rows.add(row);
        }
        table.setRows(rows);
        table.setGroupBy("team");
        table.setPageSize(5);
        Paragraph status = new Paragraph("Click a name to edit it. Team headings expand and collapse.");
        table.addValueChangeHandler(event -> status.setText("Changes captured in the table's row data."));
        examples.add(status);
        examples.add(table);
        Button filter = new Button("Show London");
        filter.addClickHandler(event -> table.filter("location", "London"));
        Button clear = new Button("Clear filter");
        clear.addClickHandler(event -> table.filter("location", ""));
        examples.add(filter);
        examples.add(clear);
        return examples;
    }

    static Widget sortable() {
        Div examples = stack();
        Div board = new Div();
        board.setStyleName("row g-3");
        Paragraph status = new Paragraph("Drag cards within a column or across to the other column.");
        for (String title : Arrays.asList("To do", "Reviewed")) {
            Div column = new Div();
            column.setStyleName("col-md-6");
            column.add(new Paragraph(title));
            SortableList list = new SortableList();
            list.setGroup("showcase-work");
            list.getElement().getStyle().setProperty("minHeight", "120px");
            list.add(new Paragraph(title.equals("To do") ? "Review changes" : "Update documentation"));
            list.add(new Paragraph(title.equals("To do") ? "Run tests" : "Check accessibility"));
            list.addValueChangeHandler(event -> status.setText(title + ": " + event.getValue().size() + " cards"));
            column.add(list);
            board.add(column);
        }
        examples.add(status);
        examples.add(board);
        return examples;
    }

    static Widget dashboard() {
        Div examples = stack();
        Dashboard dashboard = new Dashboard();
        DashboardTile summary = new DashboardTile("summary", 0, 0, 6, 2);
        summary.add(new Paragraph("Summary: drag this tile or resize its lower corner."));
        DashboardTile activity = new DashboardTile("activity", 6, 0, 6, 2);
        activity.add(new Paragraph("Recent activity: tiles contain ordinary widgets."));
        dashboard.add(summary);
        dashboard.add(activity);
        Paragraph status = new Paragraph("Changes can be saved as layout data and restored later.");
        String[] saved = {null};
        Button save = new Button("Save layout");
        save.addClickHandler(event -> { saved[0] = dashboard.getLayoutJson(); status.setText("Layout saved for this session."); });
        Button restore = new Button("Restore layout");
        restore.addClickHandler(event -> {
            if (saved[0] != null) { dashboard.setLayoutJson(saved[0]); status.setText("Saved layout restored."); }
        });
        Button lock = new Button("Lock / unlock layout");
        lock.addClickHandler(event -> dashboard.setEnabled(!dashboard.isEnabled()));
        examples.add(status);
        examples.add(dashboard);
        examples.add(save);
        examples.add(restore);
        examples.add(lock);
        return examples;
    }

    static Widget gallery() {
        Div examples = stack();
        ImageGallery gallery = new ImageGallery();
        for (String scene : Arrays.asList("mountains", "coast", "city")) {
            String url = GWT.getModuleBaseURL() + "images/gallery-" + scene + ".svg";
            gallery.addImage(url, url, 960, 640, scene);
        }
        Paragraph status = new Paragraph("Select a thumbnail. Swipe between images, zoom in, or use the arrow keys.");
        gallery.addSelectionHandler(event -> status.setText("Viewing image " + (event.getSelectedItem() + 1)));
        examples.add(status);
        examples.add(gallery);
        return examples;
    }

    private static Div stack() {
        Div result = new Div();
        result.setStyleName("d-flex flex-column gap-3");
        return result;
    }
}
