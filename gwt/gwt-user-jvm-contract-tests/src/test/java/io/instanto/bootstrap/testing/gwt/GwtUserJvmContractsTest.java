package io.instanto.bootstrap.testing.gwt;

import org.junit.Test;

import io.instanto.gwt.testing.api.JvmSafeGwtContracts;

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
