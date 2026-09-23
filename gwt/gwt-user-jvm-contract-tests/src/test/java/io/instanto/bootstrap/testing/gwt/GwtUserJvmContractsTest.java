package io.instanto.bootstrap.testing.gwt;

import io.instanto.gwt.testing.api.JvmSafeGwtContracts;
import org.junit.Test;

public class GwtUserJvmContractsTest {
  @Test
  public void safeHtmlEscapesAndPreservesTrustedFragments() {
    JvmSafeGwtContracts.safeHtmlEscapesAndPreservesTrustedFragments();
  }

  @Test
  public void handlerManagerPreservesOrderAndSource() {
    JvmSafeGwtContracts.handlerManagerPreservesOrderAndSource();
  }

  @Test
  public void handlerMutationIsDeferredUntilTheNextDispatch() {
    JvmSafeGwtContracts.handlerMutationIsDeferredUntilTheNextDispatch();
  }

  @Test
  public void removedHandlerDoesNotReceiveLaterEvents() {
    JvmSafeGwtContracts.removedHandlerDoesNotReceiveLaterEvents();
  }

  @Test
  public void valueChangesRespectExplicitEventSuppression() {
    JvmSafeGwtContracts.valueChangesRespectExplicitEventSuppression();
  }
}
