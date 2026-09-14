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
package org.gwtbootstrap3.teavm.dom;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.xml.Node;

/**
 * Small TeaVM DOM wrapper matching the shape needed by the future Bootstrap DOM seam.
 */
public final class TeaVmDomElement {

    private final HTMLElement element;

    public TeaVmDomElement(final HTMLElement element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
        this.element = element;
    }

    public static TeaVmDomElement create(final String tagName) {
        return new TeaVmDomElement(HTMLDocument.current().createElement(tagName));
    }

    public static TeaVmDomElement byId(final String id) {
        return wrapNullable(HTMLDocument.current().getElementById(id));
    }

    public static TeaVmDomElement query(final String selector) {
        return wrapNullable(HTMLDocument.current().querySelector(selector));
    }

    public HTMLElement unwrap() {
        return element;
    }

    public String getAttribute(final String name) {
        return element.getAttribute(name);
    }

    public void setAttribute(final String name, final String value) {
        element.setAttribute(name, value);
    }

    public void removeAttribute(final String name) {
        element.removeAttribute(name);
    }

    public void addClass(final String className) {
        element.getClassList().add(className);
    }

    public void removeClass(final String className) {
        element.getClassList().remove(className);
    }

    public boolean hasClass(final String className) {
        return element.getClassList().contains(className);
    }

    public void setClassName(final String className) {
        element.setClassName(className == null ? "" : className);
    }

    public void setVisible(final boolean visible) {
        element.setHidden(!visible);
    }

    public void setText(final String text) {
        element.setTextContent(text);
    }

    public String getText() {
        return element.getTextContent();
    }

    public void setHtml(final String html) {
        element.setInnerHTML(html == null ? "" : html);
    }

    public String getHtml() {
        return element.getInnerHTML();
    }

    public void clear() {
        element.clear();
    }

    public void appendChild(final TeaVmDomElement child) {
        element.appendChild((Node) child.unwrap());
    }

    public void removeFromParent() {
        final Node parent = element.getParentNode();
        if (parent != null) {
            parent.removeChild((Node) element);
        }
    }

    private static TeaVmDomElement wrapNullable(final HTMLElement element) {
        return element == null ? null : new TeaVmDomElement(element);
    }
}
