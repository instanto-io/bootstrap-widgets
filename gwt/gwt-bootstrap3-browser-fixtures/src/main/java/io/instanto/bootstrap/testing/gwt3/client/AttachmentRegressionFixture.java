package io.instanto.bootstrap.testing.gwt3.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.Collection;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.extras.tagsinput.client.ui.MVTagsInput;
import org.gwtbootstrap3.extras.tagsinput.client.ui.TagsInput;
import org.gwtbootstrap3.extras.typeahead.client.base.Dataset;

final class AttachmentRegressionFixture {
  static void run() {
    String name = Window.Location.getParameter("case");
    if (name == null) return;
    if (name.contains("TAG-001")) guarded(() -> constructors());
    if (name.contains("TAG-002")) guarded(() -> remount());
    if (name.contains("TAG-003")) guarded(() -> retainedValues());
    if (name.contains("PAG-001")) guarded(() -> paginationMutation());
    if (name.contains("PAG-002")) guarded(() -> stalePagination());
  }

  private static void guarded(Runnable action) {
    try {
      action.run();
    } catch (Throwable failure) {
      result("FAILED: " + failure.toString());
    }
  }

  private static void result(String value) {
    Document.get().getBody().setAttribute("data-attachment-result", value);
  }

  private static void check(boolean condition, String message) {
    if (!condition) throw new IllegalStateException(message);
  }

  private static void constructors() {
    Element input = Document.get().createTextInputElement();
    Element select = Document.get().createSelectElement();
    select.setAttribute("multiple", "multiple");
    Document.get().getBody().appendChild(input);
    Document.get().getBody().appendChild(select);
    try {
      SingleProbe one = new SingleProbe(input);
      MultiProbe many = new MultiProbe(select);
      check(!one.isAttached() && !many.isAttached(), "Constructors attached wrappers");
      one.attach();
      many.attach();
      one.attach();
      many.attach();
      check(one.loads == 1 && many.loads == 1, "Explicit attachment must load once");
      RootPanel.detachNow(one);
      RootPanel.detachNow(many);
      TagsInput wrappedOne = TagsInput.wrap(input);
      MVTagsInput wrappedMany = MVTagsInput.wrap(select);
      check(wrappedOne.isAttached() && wrappedMany.isAttached(), "Factories did not attach");
      RootPanel.detachNow(wrappedOne);
      RootPanel.detachNow(wrappedMany);
      result("ok");
    } finally {
      input.removeFromParent();
      select.removeFromParent();
    }
  }

  private static class SingleProbe extends TagsInput {
    private final Object initialized = new Object();
    int loads;

    SingleProbe(Element element) {
      super(element);
    }

    @Override
    protected void onLoad() {
      check(initialized != null, "Single wrapper loaded before construction completed");
      loads++;
      super.onLoad();
    }

    @Override
    public void setDatasets(Collection<? extends Dataset<String>> data) {
      check(initialized != null, "Single dataset override called in constructor");
      super.setDatasets(data);
    }

    void attach() {
      attachToDocument();
    }
  }

  private static class MultiProbe extends MVTagsInput {
    private final Object initialized = new Object();
    int loads;

    MultiProbe(Element element) {
      super(element);
    }

    @Override
    protected void onLoad() {
      check(initialized != null, "Multi wrapper loaded before construction completed");
      loads++;
      super.onLoad();
    }

    @Override
    public void setDatasets(Collection<? extends Dataset<String>> data) {
      check(initialized != null, "Multi dataset override called in constructor");
      super.setDatasets(data);
    }

    void attach() {
      attachToDocument();
    }
  }

  private static void remount() {
    Div host = new Div();
    RootPanel.get().add(host);
    TagsInput widget = new TagsInput();
    int[] changes = {0};
    int[] added = {0};
    widget.addValueChangeHandler(event -> changes[0]++);
    widget.addItemAddedHandler(event -> added[0]++);
    observeExternal(widget.getElement());
    host.add(widget);
    widget.removeFromParent();
    host.add(widget);
    widget.reconfigure();
    widget.reconfigure();
    new Timer() {
      @Override
      public void run() {
        guarded(
            () -> {
              check(
                  typeaheadCount(host.getElement()) == 1,
                  "Expected one typeahead after queued remount/reconfigure");
              widget.add("one");
              check(
                  changes[0] == 1 && added[0] == 1,
                  "Duplicate forwarded events: " + changes[0] + "/" + added[0]);
              widget.removeFromParent();
              check(typeaheadCount(host.getElement()) == 0, "Typeahead DOM survived detach");
              int before = changes[0];
              triggerExternal(widget.getElement());
              check(changes[0] == before, "Detached widget forwarded an event");
              check(
                  externalCount(widget.getElement()) == 1,
                  "Cleanup removed another owner's listener");
              host.removeFromParent();
              result("ok");
            });
      }
    }.schedule(30);
  }

  private static native int typeaheadCount(
      Element root) /*-{ return root.querySelectorAll('.twitter-typeahead').length; }-*/;

  private static native void observeExternal(Element element) /*-{
    element.externalChanges = 0;
    $wnd.jQuery(element).on('change.externalRegression', function() { element.externalChanges++; });
  }-*/;

  private static native void triggerExternal(
      Element
          element) /*-{ element.externalChanges = 0; $wnd.jQuery(element).trigger('change'); }-*/;

  private static native int externalCount(
      Element element) /*-{ return element.externalChanges; }-*/;

  private static void retainedValues() {
    Div host = new Div();
    RootPanel.get().add(host);
    TagsInput single = new TagsInput();
    MVTagsInput multi = new MVTagsInput();
    host.add(single);
    host.add(multi);
    single.add("one");
    multi.add("two");
    single.removeFromParent();
    multi.removeFromParent();
    check("one".equals(single.getValue()), "Single value lost on detach");
    check(
        multi.getValue().size() == 1 && "two".equals(multi.getValue().get(0)),
        "Multi value lost on detach");
    single.setValue("new");
    multi.setValue("new");
    check(
        "new".equals(single.getValue()), "Detached single setValue appended instead of replacing");
    check(
        multi.getValue().size() == 1 && "new".equals(multi.getValue().get(0)),
        "Detached multi setValue appended instead of replacing");
    host.add(single);
    host.add(multi);
    check("new".equals(single.getValue()) && multi.getValue().size() == 1, "Value lost on remount");
    host.clear();
    host.removeFromParent();
    result("ok");
  }

  private static class Pager extends SimplePager {
    int page;

    @Override
    public int getPageCount() {
      return 3;
    }

    @Override
    public int getPage() {
      return page;
    }

    @Override
    public boolean hasPreviousPage() {
      return page > 0;
    }

    @Override
    public boolean hasNextPage() {
      return page < 2;
    }

    @Override
    public void setPage(int value) {
      page = value;
    }

    @Override
    public void nextPage() {
      page++;
    }

    @Override
    public void previousPage() {
      page--;
    }
  }

  private static void click(AnchorListItem item) {
    DomEvent.fireNativeEvent(
        Document.get().createClickEvent(1, 0, 0, 0, 0, false, false, false, false),
        item.getWidget(0));
  }

  private static void paginationMutation() {
    Pager pager = new Pager();
    Pagination pages = new Pagination();
    pages.rebuild(pager);
    AnchorListItem second = (AnchorListItem) pages.getWidget(2);
    AnchorListItem third = (AnchorListItem) pages.getWidget(3);
    AnchorListItem next = (AnchorListItem) pages.getWidget(4);
    pages.insert(new Div(), 1);
    pages.remove(pages.getWidget(0));
    click(third);
    check(
        pager.page == 2 && third.isActive() && !second.isActive() && !next.isEnabled(),
        "Custom children changed page meanings");
    result("ok");
  }

  private static void stalePagination() {
    Pager first = new Pager();
    Pager second = new Pager();
    Pagination pages = new Pagination();
    pages.rebuild(first);
    AnchorListItem oldNext = (AnchorListItem) pages.getWidget(4);
    pages.rebuild(second);
    click(oldNext);
    check(first.page == 0 && second.page == 0, "Removed pager link still navigated");
    AnchorListItem currentNext = (AnchorListItem) pages.getWidget(4);
    pages.clear();
    click(currentNext);
    check(second.page == 0, "Cleared pager link still navigated");
    result("ok");
  }
}
