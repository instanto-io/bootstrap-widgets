package io.instanto.bootstrap.testing.bootstrap3;

import org.gwtbootstrap3.client.Bootstrap3;
import org.gwtbootstrap3.client.Bootstrap3Resources;
import org.gwtbootstrap3.client.TeaVmBootstrap3EntryPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.EachTestCompiledSeparately;
import org.teavm.junit.TeaVMTestRunner;
import org.teavm.jso.dom.html.HTMLDocument;
import io.instanto.teavm.module.ScriptModule;

import static org.junit.Assert.*;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
@EachTestCompiledSeparately
public class Bootstrap3InitialisationTest {
    @Test
    public void loadsVendoredDependenciesBeforeCallingApplication() throws InterruptedException {
        assertFalse(TeaVmBootstrap3EntryPoint.isJQueryLoaded());
        assertFalse(TeaVmBootstrap3EntryPoint.isBootstrapLoaded());
        Bootstrap3Resources.setBase("/resources/META-INF/bootstrap3-assets/css/");
        int[] callbacks = {0};
        Bootstrap3.initialise(() -> callbacks[0]++);
        Bootstrap3.initialise(() -> callbacks[0]++);
        long deadline = System.currentTimeMillis() + 10000;
        while (callbacks[0] != 2 && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }
        assertEquals("Both callers must be notified", 2, callbacks[0]);
        assertTrue(TeaVmBootstrap3EntryPoint.isJQueryLoaded());
        assertTrue(TeaVmBootstrap3EntryPoint.isBootstrapLoaded());
        Bootstrap3.initialise(() -> callbacks[0]++);
        assertEquals(3, callbacks[0]);
        assertEquals(1, HTMLDocument.current().querySelectorAll(
                "script[src$='jquery-3.7.1.min.cache.js']").getLength());
        assertEquals(1, HTMLDocument.current().querySelectorAll(
                "script[src$='bootstrap-3.4.1.min.cache.js']").getLength());
    }

    @Test
    public void reusesJQueryWhenOnlyBootstrapIsMissing() throws InterruptedException {
        ScriptModule jquery = ScriptModule.named("host-jquery")
                .script("/resources/org/gwtbootstrap3/client/resource/js/jquery-3.7.1.min.cache.js");
        jquery.ensureLoaded();
        long deadline = System.currentTimeMillis() + 10000;
        while (!jquery.isReady() && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }
        assertTrue("The host must supply jQuery first", jquery.isReady());
        assertTrue(TeaVmBootstrap3EntryPoint.isJQueryLoaded());
        assertFalse(TeaVmBootstrap3EntryPoint.isBootstrapLoaded());

        Bootstrap3TestRuntime.initialise();

        assertTrue(TeaVmBootstrap3EntryPoint.isBootstrapLoaded());
        assertEquals(1, HTMLDocument.current().querySelectorAll(
                "script[src$='jquery-3.7.1.min.cache.js']").getLength());
    }
}
