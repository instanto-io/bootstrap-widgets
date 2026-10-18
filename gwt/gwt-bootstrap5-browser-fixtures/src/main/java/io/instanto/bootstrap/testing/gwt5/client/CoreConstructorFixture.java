package io.instanto.bootstrap.testing.gwt5.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import io.instanto.bootstrap5.client.ui.AnchorButton;
import io.instanto.bootstrap5.client.ui.Button;
import io.instanto.bootstrap5.client.ui.SubmitButton;
import io.instanto.bootstrap5.client.ui.gwt.DataGrid;

final class CoreConstructorFixture {
  static void run() {
    String name = Window.Location.getParameter("case");
    if (name == null || !name.contains("CTOR-CORE-")) return;
    try {
      if (name.contains("CTOR-CORE-001")) {
        class Probe extends Button {
          final Object ready = new Object();

          @Override
          public Element getElement() {
            check(ready != null, "Early button element hook");
            return super.getElement();
          }

          @Override
          protected com.google.gwt.dom.client.Element createElement() {
            throw new IllegalStateException("Early button factory hook");
          }
        }
        check("BUTTON".equals(new Probe().getElement().getTagName()), "Button tag lost");
        class AnchorProbe extends AnchorButton {
          final Object ready = new Object();

          @Override
          public Element getElement() {
            check(ready != null, "Early anchor element hook");
            return super.getElement();
          }
        }
        check("A".equals(new AnchorProbe().getElement().getTagName()), "Anchor tag lost");
        check(
            "submit".equals(new SubmitButton().getElement().getAttribute("type")),
            "Submit type lost");
        class CustomButton extends Button {
          CustomButton() {
            super(Document.get().createAnchorElement());
          }
        }
        check("A".equals(new CustomButton().getElement().getTagName()), "Explicit element ignored");
      } else if (name.contains("CTOR-CORE-002")) {
        class Probe extends DataGrid<String> {
          final Object ready = new Object();

          Probe() {
            super(5, (com.google.gwt.view.client.ProvidesKey<String>) null);
          }

          Probe(Resources resources) {
            super(5, resources);
          }

          Probe(Resources resources, boolean loading) {
            super(5, resources, null, null);
          }

          @Override
          public TableSectionElement getTableBodyElement() {
            check(ready != null, "Early table body hook");
            return super.getTableBodyElement();
          }
        }
        DataGrid.Resources resources = GWT.create(DataGrid.Resources.class);
        for (Probe grid :
            new Probe[] {new Probe(), new Probe(resources), new Probe(resources, false)}) {
          check(
              grid.getTableBodyElement().getParentElement().hasClassName("table"),
              "Initial table style lost");
        }
      }
      Document.get().getBody().setAttribute("data-attachment-result", "ok");
    } catch (Throwable failure) {
      Document.get().getBody().setAttribute("data-attachment-result", "FAILED: " + failure);
    }
  }

  private static void check(boolean condition, String message) {
    if (!condition) throw new IllegalStateException(message);
  }
}
