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

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Turns a {@code .ui.xml} template into the Java a UiBinder implementation would be.
 *
 * <p>GWT answers {@code GWT.create(SomeBinder.class)} with a generator reached through
 * deferred binding. TeaVM has no generator SPI, which is the only thing that stopped
 * UiBinder working there, because what a generator emits is ordinary Java. Running the
 * same step as an annotation processor puts it somewhere both compilers can use, and puts
 * it inside javac, which already knows the types.</p>
 *
 * <p>That last part matters. A UiBinder generator has to know whether a widget has a
 * {@code @UiConstructor} and what its parameters are, what type each setter takes, and
 * whether that type is an enum, following the hierarchy because a widget inherits most of
 * its setters. Those are questions about types, and {@code Elements} and {@code Types}
 * answer them exactly.</p>
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class UiBinderProcessor extends AbstractProcessor {

    private static final String UI_NS = "urn:ui:com.google.gwt.uibinder";
    private static final String IMPORT_PREFIX = "urn:import:";
    private static final String BINDER = "com.google.gwt.uibinder.client.UiBinder";

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment round) {
        if (round.processingOver()) {
            return false;
        }
        for (final Element element : round.getRootElements()) {
            new TextBundleGenerator(processingEnv).process(element);
            new StringConstantsGenerator(processingEnv).process(element);
            if (element.getKind() != ElementKind.CLASS) {
                continue;
            }
            final TypeElement owner = (TypeElement) element;
            final TypeElement binder = findBinder(owner);
            if (binder != null) {
                try {
                    generate(owner, binder);
                } catch (final Failure failure) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            failure.getMessage(), failure.element == null ? owner : failure.element);
                } catch (final Exception problem) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            "could not generate a UiBinder for " + owner.getSimpleName()
                                    + ": " + problem, owner);
                }
            }
        }
        return false;
    }

    /** The nested interface that extends UiBinder, if the owner declares one. */
    private TypeElement findBinder(final TypeElement owner) {
        for (final Element nested : owner.getEnclosedElements()) {
            if (nested.getKind() != ElementKind.INTERFACE) {
                continue;
            }
            final TypeElement candidate = (TypeElement) nested;
            for (final TypeMirror parent : candidate.getInterfaces()) {
                if (processingEnv.getTypeUtils().erasure(parent).toString().equals(BINDER)) {
                    return candidate;
                }
            }
        }
        return null;
    }

    static final class Failure extends RuntimeException {
        final Element element;

        Failure(final String message, final Element element) {
            super(message);
            this.element = element;
        }
    }

    private void generate(final TypeElement owner, final TypeElement binder) throws IOException {
        final String pkg = processingEnv.getElementUtils().getPackageOf(owner)
                .getQualifiedName().toString();
        final String simple = owner.getSimpleName().toString();
        final Document template = readTemplate(pkg, simple, owner);
        if (template == null) {
            return;
        }

        final Emitter emitter = new Emitter(processingEnv, owner);
        emitter.readStyles(template.getDocumentElement());
        emitter.readWith(template.getDocumentElement());
        rejectUnsupported(template.getDocumentElement(), owner);
        final Node root = firstElement(template.getDocumentElement());
        if (root == null) {
            throw new Failure(simple + ".ui.xml has no widget in it", owner);
        }
        final String rootVar = emitter.emit(root, null);

        final String impl = simple + "_BinderImpl";

        // Assembled whole before anything is created, because handlerWiring() can still
        // fail: a file opened and then abandoned half-written draws a second "reached end
        // of file" error from javac, which buries the diagnostic that actually explains it.
        final StringBuilder source = new StringBuilder();
        source.append("// Generated from ").append(simple).append(".ui.xml. Do not edit.\n");
        source.append("package ").append(pkg).append(";\n\n");
        source.append("/** UiBinder implementation for {@link ").append(simple).append("}. */\n");
        source.append("public class ").append(impl).append(" implements ")
              .append(simple).append(".Binder {\n\n");
        source.append("    @Override\n");
        source.append("    public ").append(emitter.rootType()).append(" createAndBindUi(final ")
              .append(simple).append(" owner) {\n");
        if (emitter.styles() != null) {
            source.append("        com.google.gwt.dom.client.StyleInjector.inject(\"")
                  .append(emitter.styles().replace("\\", "\\\\").replace("\"", "\\\"")
                          .replaceAll("\\s+", " ").trim())
                  .append("\");\n");
        }
        source.append(emitter.body());
        source.append(emitter.fieldAssignments());
        source.append(emitter.handlerWiring());
        source.append("        return ").append(rootVar).append(";\n");
        source.append("    }\n}\n");

        final JavaFileObject file = processingEnv.getFiler()
                .createSourceFile(pkg + "." + impl, owner);
        try (Writer out = file.openWriter()) {
            out.write(source.toString());
        }

        // so the compatibility layer's GWT.create finds it without deferred binding
        // ServiceLoader looks the interface up by its binary name, so a nested
        // interface is Owner$Binder rather than Owner.Binder. Getting this wrong
        // leaves GWT.create finding nothing and handing back null.
        final String binderBinary = processingEnv.getElementUtils()
                .getBinaryName(binder).toString();
        final FileObject service = processingEnv.getFiler().createResource(
                StandardLocation.CLASS_OUTPUT, "", "META-INF/services/" + binderBinary);
        try (Writer out = service.openWriter()) {
            out.write(pkg + "." + impl + "\n");
        }
    }

    /**
     * A parser that never reaches the network.
     *
     * <p>Every template opens with a DOCTYPE pointing at dl.google.com, which GWT does
     * not fetch and neither should a build: resolving it would put a network call in the
     * middle of compilation, failing offline and slowing it down otherwise. The entity
     * is not needed to read the document, so external entities are refused outright,
     * which also closes the usual XXE exposure.</p>
     */
    private javax.xml.parsers.DocumentBuilder parser() throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        final javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) ->
                new org.xml.sax.InputSource(new java.io.StringReader("")));
        return builder;
    }

    private Document readTemplate(final String pkg, final String simple, final Element owner) {
        final String name = simple + ".ui.xml";
        for (final StandardLocation where : new StandardLocation[] {
                StandardLocation.SOURCE_PATH, StandardLocation.CLASS_PATH,
                StandardLocation.CLASS_OUTPUT}) {
            try {
                final FileObject found = processingEnv.getFiler().getResource(where, pkg, name);
                try (InputStream stream = found.openInputStream()) {
                    return parser().parse(stream);
                }
            } catch (final Exception ignored) {
                // try the next location
            }
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                "no " + name + " found for " + simple + "; leaving its binder to GWT", owner);
        return null;
    }

    /**
     * Rejects any ui: element this generator does not implement.
     *
     * <p>GWT's UiBinder has a wider vocabulary than this: ui:msg for internationalised
     * text, ui:image and ui:data for bundled resources, ui:attribute. None appears in
     * any template here, so none is implemented. A template that used one would
     * otherwise be built as though the element were not there, and the difference would
     * show only at runtime; naming it at compile time is the whole point.
     */
    private void rejectUnsupported(final Node root, final TypeElement owner) {
        final NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (Emitter.UI_NS.equals(child.getNamespaceURI())) {
                final String name = child.getLocalName();
                if (!"style".equals(name) && !"with".equals(name)) {
                    throw new Failure("<ui:" + name
                            + "> is not supported by this generator", owner);
                }
            }
            rejectUnsupported(child, owner);
        }
    }

    static Node firstElement(final Node parent) {
        final NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (UI_NS.equals(child.getNamespaceURI())) {
                    final Node inner = firstElement(child);
                    if (inner != null) {
                        return inner;
                    }
                    continue;
                }
                return child;
            }
        }
        return null;
    }
}
