/*
 * #%L
 * GWT Bootstrap
 * %%
 * Copyright (C) 2026 Carl Stainton
 * %%
 * Reimplements, over TeaVM's JSO libraries, part of the GWT client API. Class,
 * method and package names follow GWT (https://github.com/gwtproject/gwt),
 * Copyright (C) The GWT Project Authors, licensed under the Apache License,
 * Version 2.0. No GWT source is included.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.instanto.bootstrap5.client.ui.base;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.Registration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bridges Bootstrap's custom events onto widget events.
 *
 * <p>Same contract as the GWT class of this name; the listener bookkeeping is kept in
 * Java here rather than on a property of the element, because TeaVM cannot hold a Java
 * lambda on a DOM node the way the JSNI version does.</p>
 */
public final class BootstrapEventBridge {

    private static final Map<Element, Map<String, Registration>> REGISTRATIONS = new HashMap<>();

    private BootstrapEventBridge() {
    }

    /** Binds {@code handler} to {@code eventName}, replacing any previous binding. */
    public static void bind(final Element element, final String eventName,
            final BootstrapEventHandler handler) {
        if (element == null || eventName == null || handler == null) {
            return;
        }
        final Map<String, Registration> forElement =
                REGISTRATIONS.computeIfAbsent(element, key -> new HashMap<>());
        final Registration previous = forElement.remove(eventName);
        if (previous != null) {
            previous.dispose();
        }
        final EventListener<Event> listener = event -> handler.onEvent(new NativeEvent(event));
        forElement.put(eventName, element.unwrap().onEvent(eventName, listener));
    }

    /** Removes the binding for a single event name. */
    public static void unbind(final Element element, final String eventName) {
        final Map<String, Registration> forElement = REGISTRATIONS.get(element);
        if (forElement == null) {
            return;
        }
        final Registration registration = forElement.remove(eventName);
        if (registration != null) {
            registration.dispose();
        }
        if (forElement.isEmpty()) {
            REGISTRATIONS.remove(element);
        }
    }

    public static void unbindAll(final Element element) {
        final Map<String, Registration> forElement = REGISTRATIONS.get(element);
        if (forElement == null) {
            return;
        }
        REGISTRATIONS.remove(element);
        for (final Registration registration : new ArrayList<>(forElement.values())) {
            registration.dispose();
        }
    }
}
