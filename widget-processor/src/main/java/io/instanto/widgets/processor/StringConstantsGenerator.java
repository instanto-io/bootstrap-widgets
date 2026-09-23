package io.instanto.widgets.processor;

import java.io.IOException;
import java.io.Writer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;

/** Default-locale string constants; explicit application providers take precedence. */
final class StringConstantsGenerator {
  private final ProcessingEnvironment env;

  StringConstantsGenerator(ProcessingEnvironment env) {
    this.env = env;
  }

  void process(Element element) {
    if (!(element instanceof TypeElement type)) return;
    for (Element child : type.getEnclosedElements())
      if (child instanceof TypeElement) process(child);
    TypeElement base =
        env.getElementUtils().getTypeElement("com.google.gwt.i18n.client.ConstantsWithLookup");
    if (base == null
        || type.equals(base)
        || type.getKind() != ElementKind.INTERFACE
        || !env.getTypeUtils().isSubtype(type.asType(), base.asType())) return;
    String binary = env.getElementUtils().getBinaryName(type).toString();
    String pkg = env.getElementUtils().getPackageOf(type).getQualifiedName().toString();
    String simple =
        binary.substring(pkg.isEmpty() ? 0 : pkg.length() + 1).replace('$', '_')
            + "_StringConstants";
    String qualified = pkg.isEmpty() ? simple : pkg + "." + simple;
    try (var provider =
        env.getFiler()
            .getResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/services/" + binary)
            .openInputStream()) {
      if (!new String(provider.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8)
          .trim()
          .equals(qualified)) return;
    } catch (IOException absent) {
    }
    try {
      StringBuilder java = new StringBuilder();
      if (!pkg.isEmpty()) java.append("package ").append(pkg).append(";\n");
      java.append("public final class ")
          .append(simple)
          .append(" implements ")
          .append(type.getQualifiedName())
          .append(" {\n");
      StringBuilder cases = new StringBuilder();
      for (Element member : env.getElementUtils().getAllMembers(type)) {
        if (!(member instanceof ExecutableElement method)
            || !method.getModifiers().contains(Modifier.ABSTRACT)) continue;
        if (method.getSimpleName().contentEquals("getString") && method.getParameters().size() == 1)
          continue;
        if (!method.getParameters().isEmpty()
            || !method.getReturnType().toString().equals("java.lang.String"))
          throw new IllegalArgumentException(
              "Only no-argument string constants are supported: " + method);
        String value = null;
        for (AnnotationMirror annotation : method.getAnnotationMirrors()) {
          if (annotation
              .getAnnotationType()
              .toString()
              .equals("com.google.gwt.i18n.client.Constants.DefaultStringValue"))
            for (var entry :
                env.getElementUtils().getElementValuesWithDefaults(annotation).entrySet())
              if (entry.getKey().getSimpleName().contentEquals("value"))
                value = (String) entry.getValue().getValue();
        }
        if (value == null)
          throw new IllegalArgumentException(
              method + " requires @DefaultStringValue or an explicit provider");
        java.append("public String ")
            .append(method.getSimpleName())
            .append("(){return ")
            .append(quote(value))
            .append(";}\n");
        cases
            .append("case ")
            .append(quote(method.getSimpleName().toString()))
            .append(": return ")
            .append(method.getSimpleName())
            .append("();\n");
      }
      java.append("public String getString(String name){ if(name!=null) switch(name){")
          .append(cases)
          .append(
              "} throw new java.util.MissingResourceException(\"Unknown string" + " constant\", ")
          .append(quote(binary))
          .append(", name); }\n}\n");
      try (Writer writer = env.getFiler().createSourceFile(qualified, type).openWriter()) {
        writer.write(java.toString());
      }
      try (Writer writer =
          env.getFiler()
              .createResource(
                  StandardLocation.CLASS_OUTPUT, "", "META-INF/services/" + binary, type)
              .openWriter()) {
        writer.write(qualified + "\n");
      }
    } catch (IOException | IllegalArgumentException failure) {
      env.getMessager()
          .printMessage(
              Diagnostic.Kind.ERROR,
              "Cannot generate string constants: " + failure.getMessage(),
              type);
    }
  }

  private static String quote(String value) {
    return "\""
        + value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
        + "\"";
  }
}
