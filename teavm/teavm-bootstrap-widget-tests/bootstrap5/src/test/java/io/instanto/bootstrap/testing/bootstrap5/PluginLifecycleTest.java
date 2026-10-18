package io.instanto.bootstrap.testing.bootstrap5;

import static org.junit.Assert.*;

import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.bootstrap5.client.ui.Paragraph;
import io.instanto.bootstrap5.extras.base.client.PluginWidget;
import io.instanto.bootstrap5.extras.dashboard.client.ui.Dashboard;
import io.instanto.bootstrap5.extras.dashboard.client.ui.DashboardTile;
import io.instanto.bootstrap5.extras.datepicker.client.ui.DatePicker;
import io.instanto.bootstrap5.extras.markdown.client.ui.MarkdownEditor;
import io.instanto.bootstrap5.extras.richtext.client.ui.RichTextEditor;
import io.instanto.bootstrap5.extras.slider.client.ui.Slider;
import io.instanto.webapp.testkit.dom.Dom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
public class PluginLifecycleTest {
  private RootPanel host;

  @Before
  public void prepare() {
    Dom.reset();
    Bootstrap5TestRuntime.initialise();
    Dom.container().setAttribute("id", "plugin-lifecycle");
    host = RootPanel.get("plugin-lifecycle");
  }

  @After
  public void cleanup() {
    host.clear();
    Dom.reset();
  }

  @Test
  public void delayedReadinessFromAnEarlierMountCannotCreateAPlugin() {
    Probe probe = new Probe();
    host.add(probe);
    probe.removeFromParent();
    host.add(probe);
    probe.ready.get(0).run();
    assertEquals(0, probe.created);
    probe.ready.get(1).run();
    assertEquals(1, probe.created);
    probe.ready.get(1).run();
    assertEquals(1, probe.created);
    probe.removeFromParent();
    assertEquals(1, probe.destroyed);
  }

  @Test
  public void detachingDuringCreationDestroysTheReturnedPlugin() {
    Probe probe = new Probe();
    probe.duringCreate = probe::removeFromParent;
    host.add(probe);
    probe.ready.get(0).run();
    assertFalse(probe.isAttached());
    assertFalse(probe.isReady());
    assertEquals(1, probe.destroyed);
  }

  @Test
  public void failingAfterCreatePreservesTheFailureWhenDestructionAlsoFails() {
    Probe probe = new Probe();
    IllegalArgumentException original = new IllegalArgumentException("initialize");
    probe.after =
        () -> {
          throw original;
        };
    probe.destroyFailure = new IllegalStateException("destroy");
    host.add(probe);
    assertSame(
        original, assertThrows(IllegalArgumentException.class, () -> probe.ready.get(0).run()));
    assertEquals(1, original.getSuppressed().length);
    assertFalse(probe.isReady());
    probe.removeFromParent();
    assertEquals(1, probe.destroyed);
  }

  @Test
  public void detachingInsideAfterCreateDoesNotDestroyTheSamePluginTwice() {
    Probe probe = new Probe();
    probe.after =
        () -> {
          probe.removeFromParent();
          throw new IllegalArgumentException("after detach");
        };
    host.add(probe);
    assertThrows(IllegalArgumentException.class, () -> probe.ready.get(0).run());
    assertEquals(1, probe.destroyed);
    assertFalse(probe.isReady());
  }

  @Test
  public void richTextSurvivesRepeatedDetachWithoutAccumulatingToolbars() {
    RichTextEditor editor = new RichTextEditor();
    editor.setHTML("<p>Keep this text</p>");
    for (int cycle = 0; cycle < 3; cycle++) {
      host.add(editor);
      Dom.waitFor(
          () -> assertTrue(editor.getElement().getInnerHTML().contains("ql-editor")), 10000);
      assertTrue(
          editor.getHTML(), editor.getHTML().contains("Keep") && editor.getHTML().contains("text"));
      assertEquals(1, editor.getElement().unwrap().querySelectorAll(".ql-toolbar").getLength());
      editor.removeFromParent();
      assertTrue(
          "Detached content is retained",
          editor.getHTML().contains("Keep") && editor.getHTML().contains("text"));
      assertEquals(0, editor.getElement().unwrap().querySelectorAll(".ql-toolbar").getLength());
    }
  }

  @Test
  public void detachedRichTextDoesNotDeliverChangesFromAnOldPlugin() {
    RichTextEditor editor = new RichTextEditor();
    int[] events = {0};
    editor.addValueChangeHandler(event -> events[0]++);
    host.add(editor);
    Dom.waitFor(() -> assertTrue(editor.getElement().getInnerHTML().contains("ql-editor")), 10000);
    JSObject old = editorPlugin(editor.getElement().unwrap());
    editor.removeFromParent();
    emitChange(old);
    assertEquals(0, events[0]);
  }

  @JSBody(
      params = "element",
      script = "return Quill.find(element.querySelector('.ql-container')); ")
  private static native JSObject editorPlugin(HTMLElement element);

  @JSBody(params = "editor", script = "editor.emitter.emit('text-change');")
  private static native void emitChange(JSObject editor);

  @Test
  public void sliderKeepsUserSelectedValueAcrossDetach() {
    Slider slider = new Slider();
    host.add(slider);
    Dom.waitFor(
        () -> assertTrue(slider.getElement().getInnerHTML().contains("noUi-handle")), 10000);
    moveSlider(slider.getElement().unwrap(), 42);
    assertEquals(42, slider.getValue(), 0.001);
    slider.removeFromParent();
    assertEquals(42, slider.getValue(), 0.001);
    host.add(slider);
    Dom.waitFor(
        () -> assertTrue(slider.getElement().getInnerHTML().contains("noUi-handle")), 10000);
    assertEquals(42, slider.getValue(), 0.001);
  }

  @Test
  public void datePickerKeepsValuesSetBeforeMountAndAfterDetach() {
    DatePicker picker = new DatePicker();
    Date date = new Date(1700000000000L);
    picker.setValue(date);
    assertEquals(date, picker.getValue());
    host.add(picker);
    Dom.waitFor(() -> assertTrue(picker.isReady()), 10000);
    assertEquals(date, picker.getValue());
    int[] changes = {0};
    picker.addValueChangeHandler(event -> changes[0]++);
    picker.setValue(new Date(date.getTime() + 60000), false);
    assertEquals(0, changes[0]);
    picker.setValue(date, true);
    assertEquals(1, changes[0]);
    picker.removeFromParent();
    assertEquals(date, picker.getValue());
    host.add(picker);
    Dom.waitFor(() -> assertTrue(picker.isReady()), 10000);
    assertEquals(date, picker.getValue());
    assertEquals(1, changes[0]);
  }

  @JSBody(
      params = {"element", "value"},
      script = "element.noUiSlider.set(value);")
  private static native void moveSlider(HTMLElement element, double value);

  @Test
  public void dashboardInsertionEnforcesTheSameRulesAsAdd() {
    Dashboard dashboard = new Dashboard();
    assertThrows(IllegalArgumentException.class, () -> dashboard.insert(new Paragraph("wrong"), 0));
    dashboard.add(new DashboardTile("one", 0, 0, 6, 2));
    assertThrows(
        IllegalArgumentException.class,
        () -> dashboard.insert(new DashboardTile("one", 0, 0, 6, 2), 0));
    assertEquals(1, dashboard.getWidgetCount());
  }

  @Test
  public void markdownConstructionDoesNotInvokeUninitializedSubclassState() {
    MarkdownProbe editor = new MarkdownProbe();
    assertTrue(editor.isEditing());
    assertEquals(0, editor.calls[0]);
    editor.showEditor();
    assertEquals(1, editor.calls[0]);
  }

  private static final class MarkdownProbe extends MarkdownEditor {
    final int[] calls = {0};

    @Override
    public void showEditor() {
      calls[0]++;
      super.showEditor();
    }
  }

  @Test
  public void dashboardRejectsBoundsThatOverflowAnInteger() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new DashboardTile("overflow", 1, 0, Integer.MAX_VALUE, 1));
  }

  private static final class Probe extends PluginWidget<Object> {
    final List<Runnable> ready = new ArrayList<>();
    int created;
    int destroyed;
    Runnable duringCreate;
    Runnable after;
    RuntimeException destroyFailure;

    @Override
    protected void whenReady(Runnable action) {
      ready.add(action);
    }

    @Override
    protected Object createPlugin() {
      created++;
      if (duringCreate != null) duringCreate.run();
      return new Object();
    }

    @Override
    protected void afterCreate() {
      if (after != null) after.run();
    }

    @Override
    protected void destroyPlugin(Object plugin) {
      assertNotNull(plugin);
      destroyed++;
      if (destroyFailure != null) throw destroyFailure;
    }
  }
}
