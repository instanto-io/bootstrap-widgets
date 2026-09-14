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

import com.google.gwt.dom.client.Element;

final class GwtBootstrapDomElement implements BootstrapDomElement {

    private final Element element;

    GwtBootstrapDomElement(Element element) {
        this.element = element;
    }

    @Override
    public void addClass(String className) {
        element.addClassName(className);
    }

    @Override
    public void removeClass(String className) {
        element.removeClassName(className);
    }

    @Override
    public boolean hasClass(String className) {
        return element.hasClassName(className);
    }

    @Override
    public String getAttribute(String name) {
        return element.getAttribute(name);
    }

    @Override
    public void setAttribute(String name, String value) {
        element.setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        element.removeAttribute(name);
    }

    @Override
    public Object unwrap() {
        return element;
    }
}
