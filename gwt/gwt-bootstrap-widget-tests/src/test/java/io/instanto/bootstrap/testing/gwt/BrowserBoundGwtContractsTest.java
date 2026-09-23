package io.instanto.bootstrap.testing.gwt;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import io.instanto.gwt.testing.api.BrowserBoundGwtContracts;

public class BrowserBoundGwtContractsTest extends GWTTestCase {
  @Override
  public String getModuleName() {
    return "io.instanto.bootstrap.testing.gwt.BootstrapWidgetContractsTest";
  }

  public void testSpecializedElementFactoriesAndNarrowingRetainTheirTypes() {
    BrowserBoundGwtContracts.specializedElementFactoriesAndNarrowingRetainTheirTypes();
  }

  public void testWidgetLifecycleReportsAttachThenDetachAndClearsParent() {
    BrowserBoundGwtContracts.widgetLifecycleReportsAttachThenDetachAndClearsParent();
  }

  public void testDependentStyleNamesFollowThePrimaryStyle() {
    BrowserBoundGwtContracts.dependentStyleNamesFollowThePrimaryStyle();
  }

  public void testCheckboxValueBridgeUsesTheSubclassExtensionPointOnce() {
    BrowserBoundGwtContracts.checkboxValueBridgeUsesTheSubclassExtensionPointOnce();
  }

  public void testrootPanelsAreCachedPerElement() {
    final Label host = new Label();
    host.getElement().setId("contract-root-host");
    RootPanel.get().add(host);
    try {
      final RootPanel panel = RootPanel.get("contract-root-host");
      assertNotNull(panel);
      assertSame(panel, RootPanel.get("contract-root-host"));
      panel.add(new Label("kept"));
      assertEquals(1, RootPanel.get("contract-root-host").getWidgetCount());
    } finally {
      host.removeFromParent();
    }
  }

  public void testaReplacedElementGetsItsOwnRootPanel() {
    final Label first = new Label();
    first.getElement().setId("contract-replaced-host");
    RootPanel.get().add(first);
    final RootPanel before = RootPanel.get("contract-replaced-host");
    first.removeFromParent();

    final Label second = new Label();
    second.getElement().setId("contract-replaced-host");
    RootPanel.get().add(second);
    try {
      assertNotSame(before, RootPanel.get("contract-replaced-host"));
    } finally {
      second.removeFromParent();
    }
  }

  public void testaWrappingPanelIsDetachedWhenThePageCloses() {
    final Label host = new Label();
    host.getElement().setId("contract-detach-host");
    RootPanel.get().add(host);
    try {
      final RootPanel panel = RootPanel.get("contract-detach-host");
      assertTrue(RootPanel.isInDetachList(panel));
      assertTrue(panel.isAttached());
      RootPanel.detachNow(panel);
      assertFalse(RootPanel.isInDetachList(panel));
      assertFalse(panel.isAttached());
    } finally {
      host.removeFromParent();
    }
  }
}
