package io.instanto.bootstrap5.client.ui;

import static org.junit.Assert.*;

import io.instanto.bootstrap5.client.ui.base.helper.EnumHelper;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.ButtonType;
import org.junit.Test;

public class StyleHelperTest {
  @Test
  public void matchesWholeTokensAcrossWhitespace() {
    assertTrue(StyleHelper.containsStyle("  btn\tbtn-primary\nactive\r\f", "active"));
    assertTrue(StyleHelper.containsStyle("btn-primary", "btn-primary"));
    assertFalse(StyleHelper.containsStyle("btn-primary", "btn"));
    assertFalse(StyleHelper.containsStyle("btn btn-primary", "btn btn-primary"));
    assertFalse(StyleHelper.containsStyle("  btn", ""));
    assertFalse(StyleHelper.containsStyle(null, "btn"));
    assertFalse(StyleHelper.containsStyle("btn", null));
  }

  @Test
  public void enumLookupUsesCompleteCssClassNames() {
    assertEquals(
        ButtonType.PRIMARY,
        EnumHelper.fromStyleName("btn btn-primary active", ButtonType.class, null));
    assertNull(EnumHelper.fromStyleName("btn-primary-outline", ButtonType.class, null));
  }
}
