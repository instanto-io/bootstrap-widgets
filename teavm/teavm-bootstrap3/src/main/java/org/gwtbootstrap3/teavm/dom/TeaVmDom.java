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

/**
 * TeaVM entry point for DOM operations used by the experimental backend.
 */
public final class TeaVmDom {

    private TeaVmDom() {
    }

    public static TeaVmDomElement createElement(final String tagName) {
        return TeaVmDomElement.create(tagName);
    }

    public static TeaVmDomElement getElementById(final String id) {
        return TeaVmDomElement.byId(id);
    }

    public static TeaVmDomElement querySelector(final String selector) {
        return TeaVmDomElement.query(selector);
    }
}
