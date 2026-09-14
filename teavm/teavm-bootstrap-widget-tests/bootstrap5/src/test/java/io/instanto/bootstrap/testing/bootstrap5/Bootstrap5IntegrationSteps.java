package io.instanto.bootstrap.testing.bootstrap5;

import static org.junit.Assert.*;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import io.instanto.bootstrap5.client.ui.Paragraph;
import io.instanto.bootstrap5.extras.base.client.PluginWidget;
import io.instanto.bootstrap5.extras.select.client.ui.SearchableSelect;
import io.instanto.bootstrap5.extras.select.client.ui.SelectOption;
import io.instanto.bootstrap5.extras.grid.client.ui.*;
import io.instanto.bootstrap5.extras.grid.client.ui.DataTable;
import io.instanto.bootstrap5.extras.sortable.client.ui.SortableList;
import io.instanto.bootstrap5.extras.dashboard.client.ui.*;
import io.instanto.bootstrap5.extras.gallery.client.ui.ImageGallery;
import io.instanto.cucumber.tea.*;
import io.instanto.webapp.testkit.dom.Dom;
import org.teavm.jso.dom.html.HTMLDocument;
import java.util.*;

@CucumberSuite("features/advanced-integrations.feature")
public class Bootstrap5IntegrationSteps {
    private RootPanel host;
    private PluginWidget widget;
    private SearchableSelect select;
    private DataTable grid;
    private Dashboard dashboard;
    private DashboardTile first;
    private DashboardTile second;
    private ImageGallery gallery;
    private int events;
    private AsyncCallback<List<SelectOption>> directoryResult;

    @BeforeScenario public void prepare() {
        Dom.reset();
        Bootstrap5TestRuntime.initialise();
        Dom.container().setAttribute("id", "integration-tests");
        host = RootPanel.get("integration-tests");
    }
    @AfterScenario public void cleanup() {
        if (host != null) host.clear();
        Dom.reset();
    }
    @Given("integration fixture {string} is mounted")
    public void fixture(String name) {
        switch (name) {
            case "remote-select":
            case "select":
                select = new SearchableSelect();
                select.setMultiple(true);
                select.setAllowCreate(true);
                select.addOption("java", "Java");
                select.addOption("sql", "SQL");
                select.addValueChangeHandler(event -> events++);
                if (name.equals("remote-select")) select.setDataProvider((query, result) -> directoryResult = result);
                widget = select;
                break;
            case "grid":
                grid = new DataTable();
                grid.setColumns(Arrays.asList(new GridColumn("Name", "name"), new GridColumn("City", "city")));
                Map<String, Object> london = new LinkedHashMap<>();
                london.put("name", "Ada"); london.put("city", "London");
                Map<String, Object> paris = new LinkedHashMap<>();
                paris.put("name", "Grace"); paris.put("city", "Paris");
                grid.setRows(Arrays.asList(london, paris));
                widget = grid;
                break;
            case "sortable":
                SortableList list = new SortableList();
                list.add(new Paragraph("First card"));
                list.add(new Paragraph("Second card"));
                widget = list;
                break;
            case "dashboard":
                dashboard = new Dashboard();
                first = new DashboardTile("one", 0, 0, 6, 2);
                second = new DashboardTile("two", 6, 0, 6, 2);
                first.add(new Paragraph("Summary"));
                second.add(new Paragraph("Details"));
                dashboard.add(first); dashboard.add(second);
                widget = dashboard;
                break;
            case "gallery":
                gallery = new ImageGallery();
                String image = "/resources/META-INF/bootstrap5-assets/images/gallery-mountains.svg";
                gallery.addImage(image, image, 960, 640, "Mountains");
                widget = gallery;
                break;
            default: throw new IllegalArgumentException(name);
        }
        widget.getElement().setAttribute("data-testid", "integrations/" + name);
        host.add(widget);
        ready();
        if (grid != null) Dom.waitFor(() -> assertEquals(2, Dom.findAll(".tabulator-row").size()));
    }
    private void ready() { Dom.waitFor(() -> assertTrue(widget.isReady()), 10000); }

    @When("SQL is chosen in the dropdown")
    public void choose() {
        Dom.click(Dom.find(".ts-control input"));
        Dom.waitFor(() -> assertNotNull(Dom.findOrNull(".ts-dropdown [data-value='sql']")));
        Dom.click(Dom.find(".ts-dropdown [data-value='sql']"));
    }
    @Then("the selection is SQL and one value event was delivered")
    public void sqlSelected() { assertEquals(Arrays.asList("sql"), select.getValue()); assertEquals(1, events); }
    @When("selection is set silently and then changed with notification")
    public void programmatic() {
        select.setValue(Arrays.asList("java"));
        assertEquals(0, events);
        select.setValue(Arrays.asList("sql"), true);
        select.setValue(Arrays.asList("sql"), true);
    }
    @Then("exactly one selection event was delivered")
    public void oneEvent() { assertEquals(1, events); }
    @When("a new tag is selected and the integration is reattached")
    public void newTag() { select.setValue(Arrays.asList("new-skill")); reattach(); }
    @Then("the new tag remains selected")
    public void tagPreserved() { assertEquals(Arrays.asList("new-skill"), select.getValue()); }
    @When("the selection is disabled")
    public void disable() { select.setEnabled(false); }
    @Then("the dropdown input is disabled")
    public void disabled() { assertFalse(select.isEnabled()); assertTrue(Dom.find(".ts-control input").hasAttribute("disabled")); }

    @When("a directory search is started")
    public void searchDirectory() {
        Dom.type(Dom.find(".ts-control input"), "remote");
        Dom.waitFor(() -> assertNotNull(directoryResult));
    }
    @When("the directory result arrives")
    public void directoryResult() { directoryResult.onSuccess(Arrays.asList(new SelectOption("remote", "Remote colleague"))); }
    @Then("the remote option can be selected")
    public void chooseRemote() {
        Dom.waitFor(() -> assertNotNull(Dom.findOrNull(".ts-dropdown [data-value='remote']")));
        Dom.click(Dom.find(".ts-dropdown [data-value='remote']"));
        assertEquals(Arrays.asList("remote"), select.getValue());
        assertEquals(1, events);
    }
    @Then("the old response does not change the selection")
    public void staleResponse() {
        assertTrue(select.getValue().isEmpty());
        assertNull(Dom.findOrNull(".ts-dropdown [data-value='remote']"));
        assertEquals(0, events);
    }

    @When("the data grid is filtered to London")
    public void filter() { grid.filter("city", "London"); }
    @Then("only the London row is displayed")
    public void filtered() {
        Dom.waitFor(() -> assertEquals(1, Dom.findAll(".tabulator-row").size()));
        assertTrue(Dom.find(".tabulator-row").getTextContent().contains("London"));
    }
    @When("the dashboard tiles are rearranged through saved layout data")
    public void layout() { dashboard.setLayoutJson("[{\"id\":\"one\",\"x\":6,\"y\":0,\"w\":6,\"h\":2},{\"id\":\"two\",\"x\":0,\"y\":0,\"w\":6,\"h\":2}]"); }
    @Then("both original tile widgets remain owned by the dashboard")
    public void tiles() {
        assertEquals(2, dashboard.getWidgetCount());
        assertSame(dashboard, first.getParent()); assertSame(dashboard, second.getParent());
        assertEquals("6", first.getElement().getAttribute("gs-x"));
    }
    @When("a dashboard tile is removed")
    public void removeTile() { assertTrue(dashboard.remove(first)); }
    @Then("the remaining tile is still owned by the dashboard")
    public void remaining() { assertEquals(1, dashboard.getWidgetCount()); assertSame(dashboard, second.getParent()); assertNull(first.getParent()); }
    @When("the gallery is opened and closed")
    public void view() {
        gallery.open(0);
        Dom.waitFor(() -> assertNotNull(HTMLDocument.current().querySelector(".pswp--open")));
        gallery.close();
    }
    @Then("no image viewer remains open")
    public void closed() { Dom.waitFor(() -> assertNull(HTMLDocument.current().querySelector(".pswp--open"))); }

    @When("the integration is detached and reattached")
    public void reattach() {
        widget.removeFromParent();
        assertFalse(widget.isReady());
        host.add(widget);
        ready();
    }
    @Then("one ready integration remains")
    public void oneIntegration() { assertEquals(1, host.getWidgetCount()); assertTrue(widget.isReady()); }
}
