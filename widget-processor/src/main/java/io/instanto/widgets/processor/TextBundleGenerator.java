package io.instanto.widgets.processor;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;

/** Generates synchronous text-only ClientBundles, including nested UiBinder source bundles. */
final class TextBundleGenerator {
    private final ProcessingEnvironment env;

    TextBundleGenerator(ProcessingEnvironment env) {
        this.env = env;
    }

    void process(Element element) {
        if (!(element instanceof TypeElement type)) return;
        for (Element nested : type.getEnclosedElements()) {
            if (nested instanceof TypeElement) process(nested);
        }
        TypeElement bundle = env.getElementUtils().getTypeElement("com.google.gwt.resources.client.ClientBundle");
        if (bundle == null || type.equals(bundle) || type.getKind() != ElementKind.INTERFACE
                || !env.getTypeUtils().isSubtype(type.asType(), bundle.asType())) return;
        List<ExecutableElement> methods = new ArrayList<>();
        for (Element member : env.getElementUtils().getAllMembers(type)) {
            if (member instanceof ExecutableElement method && member.getKind() == ElementKind.METHOD
                    && method.getModifiers().contains(Modifier.ABSTRACT)) {
                if (!method.getReturnType().toString().equals("com.google.gwt.resources.client.TextResource")
                        || !method.getParameters().isEmpty()) return;
                methods.add(method);
            }
        }
        if (methods.isEmpty()) return;
        String binary = env.getElementUtils().getBinaryName(type).toString();
        String pkg = env.getElementUtils().getPackageOf(type).getQualifiedName().toString();
        String localName = binary.substring(pkg.isEmpty() ? 0 : pkg.length() + 1).replace('$', '_') + "_TextBundle";
        String generatedProvider = pkg.isEmpty() ? localName : pkg + "." + localName;
        // Explicit providers from module generation take precedence (for example JS locale bundles).
        try (var existing = env.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "",
                "META-INF/services/" + binary).openInputStream()) {
            if (!new String(existing.readAllBytes(), StandardCharsets.UTF_8).trim().equals(generatedProvider)) return;
        } catch (IOException absent) {
            // No provider has been generated yet.
        }
        try {
            generate(type, binary, methods);
        } catch (IOException | IllegalArgumentException failure) {
            env.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Cannot generate text bundle " + binary + ": " + failure.getMessage(), type);
        }
    }

    private void generate(TypeElement type, String binary, List<ExecutableElement> methods) throws IOException {
        String pkg = env.getElementUtils().getPackageOf(type).getQualifiedName().toString();
        String simple = binary.substring(pkg.isEmpty() ? 0 : pkg.length() + 1).replace('$', '_') + "_TextBundle";
        String qualified = pkg.isEmpty() ? simple : pkg + "." + simple;
        StringBuilder java = new StringBuilder("// Generated from ClientBundle text resources.\n");
        if (!pkg.isEmpty()) java.append("package ").append(pkg).append(";\n");
        java.append("public final class ").append(simple).append(" implements ")
                .append(type.getQualifiedName()).append(" {\n");
        for (ExecutableElement method : methods) {
            List<String> paths = sources(method);
            String resourcePackage = env.getElementUtils().getPackageOf(method).getQualifiedName().toString();
            StringBuilder text = new StringBuilder();
            for (String path : paths) text.append(read(resourcePackage, path));
            java.append("public com.google.gwt.resources.client.TextResource ").append(method.getSimpleName())
                    .append("() { return new com.google.gwt.resources.client.TextResource() {\n")
                    .append("public String getName() { return ").append(quote(method.getSimpleName().toString())).append("; }\n")
                    .append("public String getText() { StringBuilder text = new StringBuilder();\n");
            // Avoid the class-file UTF-8 constant limit, including multibyte template text.
            for (int offset = 0; offset < text.length(); offset += 4096) {
                java.append("text.append(").append(quote(text.substring(offset, Math.min(offset + 4096, text.length()))))
                        .append(");\n");
            }
            java.append("return text.toString(); } }; }\n");
        }
        java.append("}\n");
        try (Writer writer = env.getFiler().createSourceFile(qualified, type).openWriter()) {
            writer.write(java.toString());
        }
        try (Writer writer = env.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "",
                "META-INF/services/" + binary, type).openWriter()) {
            writer.write(qualified + "\n");
        }
    }

    private List<String> sources(ExecutableElement method) {
        for (AnnotationMirror annotation : method.getAnnotationMirrors()) {
            if (!annotation.getAnnotationType().toString().equals("com.google.gwt.resources.client.ClientBundle.Source")) continue;
            for (var entry : env.getElementUtils().getElementValuesWithDefaults(annotation).entrySet()) {
                if (!entry.getKey().getSimpleName().contentEquals("value")) continue;
                List<String> paths = new ArrayList<>();
                for (Object value : (List<?>) entry.getValue().getValue()) {
                    paths.add((String) ((AnnotationValue) value).getValue());
                }
                if (!paths.isEmpty()) return paths;
            }
        }
        throw new IllegalArgumentException(method.getSimpleName() + " requires @Source");
    }

    private String read(String pkg, String path) throws IOException {
        for (StandardLocation location : new StandardLocation[] {
                StandardLocation.SOURCE_PATH, StandardLocation.CLASS_PATH, StandardLocation.CLASS_OUTPUT}) {
            try (var input = env.getFiler().getResource(location, pkg, path).openInputStream()) {
                return new String(input.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException absent) {
                // Try the other compiler resource locations.
            }
        }
        throw new IOException("Missing @Source resource " + pkg + "/" + path);
    }

    private String quote(String text) {
        StringBuilder quoted = new StringBuilder("\"");
        for (char c : text.toCharArray()) {
            switch (c) {
                case '\\' -> quoted.append("\\\\");
                case '"' -> quoted.append("\\\"");
                case '\n' -> quoted.append("\\n");
                case '\r' -> quoted.append("\\r");
                case '\t' -> quoted.append("\\t");
                default -> {
                    if (c < 32) quoted.append(String.format("\\%03o", (int) c));
                    else quoted.append(c);
                }
            }
        }
        return quoted.append('"').toString();
    }
}
