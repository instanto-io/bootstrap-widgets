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
package io.instanto.widgets.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

/**
 * Which handler interface an event belongs to, and how a widget takes one.
 *
 * <p>Nothing is listed here. The widget is asked: of its {@code addXHandler} methods,
 * the one wanted is whichever takes an interface whose single method takes this event.
 * A table would have to name every event in the library and the extras, and would be
 * wrong again the moment somebody wrote a widget of their own.</p>
 */
final class Handlers {

    /** A resolved wiring: the adder to call, the interface to implement, its method. */
    static final class Binding {
        final String adder;
        final String handler;
        final String method;
        final String parameter;

        Binding(final String adder, final String handler, final String method,
                final String parameter) {
            this.adder = adder;
            this.handler = handler;
            this.method = method;
            this.parameter = parameter;
        }
    }

    private Handlers() {
    }

    /**
     * Finds how {@code widget} takes a handler for {@code event}, or null.
     *
     * <p>Type arguments are resolved against the widget, so a TextBox yields
     * {@code ValueChangeHandler<String>} rather than the raw interface, and the
     * generated method signature is an exact override.</p>
     */
    static Binding resolve(final ProcessingEnvironment env, final TypeMirror widget,
            final TypeMirror event) {
        if (widget == null || widget.getKind() != TypeKind.DECLARED) {
            return null;
        }
        final DeclaredType owner = (DeclaredType) widget;
        final String wanted = env.getTypeUtils().erasure(event).toString();
        for (final ExecutableElement method : ElementFilter.methodsIn(
                env.getElementUtils().getAllMembers((TypeElement) owner.asElement()))) {
            final String name = method.getSimpleName().toString();
            if (!name.startsWith("add") || !name.endsWith("Handler")
                    || method.getParameters().size() != 1
                    || !method.getModifiers().contains(Modifier.PUBLIC)) {
                continue;
            }
            final TypeMirror parameter = ((ExecutableType) env.getTypeUtils()
                    .asMemberOf(owner, method)).getParameterTypes().get(0);
            if (parameter.getKind() != TypeKind.DECLARED) {
                continue;
            }
            final DeclaredType handler = (DeclaredType) parameter;
            final ExecutableElement single = singleMethod(env, (TypeElement) handler.asElement());
            if (single == null || single.getParameters().size() != 1) {
                continue;
            }
            final TypeMirror taken = ((ExecutableType) env.getTypeUtils()
                    .asMemberOf(handler, single)).getParameterTypes().get(0);
            if (!env.getTypeUtils().erasure(taken).toString().equals(wanted)) {
                continue;
            }
            return new Binding(name, handler.toString(),
                    single.getSimpleName().toString(), taken.toString());
        }
        return null;
    }

    /** The one method a handler interface declares, ignoring anything from Object. */
    private static ExecutableElement singleMethod(final ProcessingEnvironment env,
            final TypeElement handler) {
        ExecutableElement found = null;
        for (final ExecutableElement method : ElementFilter.methodsIn(
                env.getElementUtils().getAllMembers(handler))) {
            if (!method.getModifiers().contains(Modifier.ABSTRACT)
                    || method.getEnclosingElement().toString().equals("java.lang.Object")) {
                continue;
            }
            if (found != null) {
                return null;
            }
            found = method;
        }
        return found;
    }

    /** The ui:field names a @UiHandler annotation lists. */
    static List<String> namedFields(final AnnotationMirror mirror) {
        final List<String> names = new ArrayList<>();
        for (final Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
                : mirror.getElementValues().entrySet()) {
            if (!entry.getKey().getSimpleName().contentEquals("value")) {
                continue;
            }
            final Object value = entry.getValue().getValue();
            if (value instanceof List) {
                for (final Object item : (List<?>) value) {
                    names.add(String.valueOf(((AnnotationValue) item).getValue()));
                }
            } else {
                names.add(String.valueOf(value));
            }
        }
        return names;
    }
}
