package io.instanto.bootstrap5.client.ui;

import static org.junit.Assert.assertEquals;

import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasWidgets;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * UiBinder decides how to parse an element's children from the first matching interface a
 * widget declares. ElementPanel's children are widgets, so HasWidgets has to come before
 * HasHTML, or UiBinder treats the children as HTML text.
 */
public class ElementPanelInterfacesTest {
  @Test
  public void declaresHasWidgetsImmediatelyBeforeHasHtml() {
    List<Class<?>> declared = Arrays.asList(ElementPanel.class.getInterfaces());
    int widgets = declared.indexOf(HasWidgets.class);
    int html = declared.indexOf(HasHTML.class);
    assertEquals("ElementPanel must declare HasWidgets immediately before HasHTML: " + declared, widgets + 1, html);
  }
}
