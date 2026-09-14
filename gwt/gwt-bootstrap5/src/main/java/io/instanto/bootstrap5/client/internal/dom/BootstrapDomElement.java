/*
 * #%L
 * GWT Bootstrap
 * %%
 * Copyright (C) 2026 Carl Stainton
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
package io.instanto.bootstrap5.client.internal.dom;

/**
 * Minimal DOM surface used by the Bootstrap integration layer.
 *
 * Keep this deliberately small: GWT code can back it with Elemental2, and a future
 * TeaVM build can back the same contract with TeaVM JSO bindings generated from WebIDL.
 */
public interface BootstrapDomElement {

    void addClass(String className);

    void removeClass(String className);

    boolean hasClass(String className);

    String getAttribute(String name);

    void setAttribute(String name, String value);

    void removeAttribute(String name);

    Object unwrap();
}
