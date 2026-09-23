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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.junit.After;
import org.junit.Test;

/** Compiler-level contracts for the UiBinder annotation processor. */
public class UiBinderProcessorTest {
  @Test
  public void generatesDefaultStringConstantsAndLookupProvider() throws Exception {
    Compilation compiled =
        compile(
            "package fixture; public interface Sample extends com.google.gwt.i18n.client.ConstantsWithLookup { @DefaultStringValue(\"Cannot be blank\") String blank(); }",
            validTemplate());
    assertTrue(compiled.diagnostics(), compiled.success);
    String source = compiled.generated("fixture/Sample_StringConstants.java");
    assertTrue(source.contains("return \"Cannot be blank\""));
    assertTrue(source.contains("case \"blank\": return blank()"));
    assertTrue(source.contains("MissingResourceException"));
    assertTrue(Files.exists(compiled.classes.resolve("META-INF/services/fixture.Sample")));
  }

  @Test
  public void preservesWhitespaceInsideNestedPreformattedCode() throws Exception {
    Compilation compiled =
        compile(
            sampleOwner("com.google.gwt.event.dom.client.ClickEvent"),
            validTemplate()
                .replace("</w:Panel>", "<pre><code>first\n  second</code></pre></w:Panel>"));
    assertTrue(compiled.diagnostics(), compiled.success);
    assertTrue(
        compiled
            .generated("fixture/Sample_BinderImpl.java")
            .contains("createTextNode(\"first\\n  second\")"));
  }

  @Test
  public void resolvesInheritedGenericSetterAgainstConcreteWidget() throws Exception {
    final Compilation compilation =
        compile(
            sampleOwner("com.google.gwt.event.dom.client.ClickEvent"),
            validTemplate().replace("enabled=\"true\"", "enabled=\"true\" value=\"250\""));
    assertTrue(compilation.diagnostics(), compilation.success);
    assertTrue(compilation.generated("fixture/Sample_BinderImpl.java").contains(".setValue(250);"));
  }

  private final List<Path> workspaces = new ArrayList<>();

  @After
  public void removeWorkspaces() throws IOException {
    for (final Path workspace : workspaces) {
      if (!Files.exists(workspace)) {
        continue;
      }
      try (java.util.stream.Stream<Path> files = Files.walk(workspace)) {
        files
            .sorted(Comparator.reverseOrder())
            .forEach(
                path -> {
                  try {
                    Files.delete(path);
                  } catch (final IOException problem) {
                    throw new IllegalStateException(problem);
                  }
                });
      }
    }
  }

  @Test
  public void generatesTypedWidgetTreeFieldsHandlersAndServiceProvider() throws Exception {
    final Compilation compilation =
        compile(sampleOwner("com.google.gwt.event.dom.client.ClickEvent"), validTemplate());

    assertTrue(compilation.diagnostics(), compilation.success);
    final String generated = compilation.generated("fixture/Sample_BinderImpl.java");
    assertContains(
        generated,
        "widgets.Panel panel1 = new widgets.Panel();",
        "widgets.Button button2 = new widgets.Button(widgets.Kind.PRIMARY);",
        "button2.setEnabled(true);",
        "button2.getElement().setId(\"save-button\");",
        "button2.addStyleName(\"btn\");",
        "button2.addStyleName(\"btn-primary\");",
        "button2.setText(\"Save\");",
        "owner.action = button2;",
        "button2.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler()",
        "owner.onAction(event);",
        "panel1.add(button2);",
        "return panel1;");

    final Path service = compilation.classes.resolve("META-INF/services/fixture.Sample$Binder");
    assertTrue("nested binder service descriptor was not generated", Files.isRegularFile(service));
    assertTrue(Files.readString(service).contains("fixture.Sample_BinderImpl"));
  }

  @Test
  public void reportsUnknownWidgetAttributeAgainstTheOwningTemplate() throws Exception {
    final Compilation compilation =
        compile(
            sampleOwner("ClickEvent"),
            validTemplate().replace("enabled=\"true\"", "missing=\"true\""));

    assertFalse(compilation.success);
    assertTrue(
        compilation.diagnostics(),
        compilation.diagnostics().contains("Button has no setMissing for the missing attribute"));
  }

  @Test
  public void reportsUnsupportedUiHandlerEventAtCompileTime() throws Exception {
    final Compilation compilation = compile(sampleOwner("UnsupportedEvent"), validTemplate());

    assertFalse(compilation.success);
    assertTrue(
        compilation.diagnostics(),
        compilation
            .diagnostics()
            .contains("action has no method that takes a handler for fixture.UnsupportedEvent"));
  }

  @Test
  public void reportsMissingUiConstructorArgumentAtCompileTime() throws Exception {
    final Compilation compilation =
        compile(
            sampleOwner("com.google.gwt.event.dom.client.ClickEvent"),
            validTemplate().replace("kind=\"PRIMARY\" ", ""));

    assertFalse(compilation.success);
    assertTrue(
        compilation.diagnostics(),
        compilation
            .diagnostics()
            .contains("Button needs the kind attribute its @UiConstructor takes"));
  }

  @Test
  public void reportsUiHandlerWithoutMatchingFieldAtCompileTime() throws Exception {
    final String owner =
        sampleOwner("com.google.gwt.event.dom.client.ClickEvent")
            .replace(
                "@com.google.gwt.uibinder.client.UiHandler(\"action\")",
                "@com.google.gwt.uibinder.client.UiHandler(\"missing\")");
    final Compilation compilation = compile(owner, validTemplate());

    assertFalse(compilation.success);
    assertTrue(
        compilation.diagnostics(),
        compilation.diagnostics().contains("@UiHandler names missing, which no ui:field declares"));
  }

  @Test
  public void prefixesUiStyleClassesPerTemplateAndInjectsThem() throws Exception {
    final Compilation compilation =
        compile(sampleOwner("com.google.gwt.event.dom.client.ClickEvent"), styledTemplate());

    assertTrue(compilation.diagnostics(), compilation.success);
    final String generated = compilation.generated("fixture/Sample_BinderImpl.java");
    // Prefixed, because 21 of the showcase templates each declare their own .margin-fix
    // and they all end up on one page.
    assertContains(
        generated,
        "StyleInjector.inject(\".Sample-spacing { margin: 4px; } "
            + ".Sample-margin-fix { margin-bottom: 0 !important; }\");",
        "button2.addStyleName(\"btn\");",
        "button2.addStyleName(\"Sample-spacing\");",
        "button2.addStyleName(\"Sample-margin-fix\");");
  }

  @Test
  public void reportsStyleReferenceThatTheTemplateDoesNotDeclare() throws Exception {
    final Compilation compilation =
        compile(
            sampleOwner("com.google.gwt.event.dom.client.ClickEvent"),
            styledTemplate().replace("{style.margin-fix}", "{style.absent}"));

    assertFalse(compilation.success);
    assertTrue(
        compilation.diagnostics(),
        compilation
            .diagnostics()
            .contains("{style.absent} is not declared by this template's <ui:style>"));
  }

  @Test
  public void readsValuesThroughUiWithRatherThanEmittingTheExpression() throws Exception {
    final Compilation compilation =
        compile(
            sampleOwner("com.google.gwt.event.dom.client.ClickEvent"),
            "<ui:UiBinder xmlns:ui=\"urn:ui:com.google.gwt.uibinder\" "
                + "xmlns:w=\"urn:import:widgets\">\n"
                + "  <ui:with field=\"tokens\" type=\"tokens.Tokens\"/>\n"
                + "  <w:Panel>\n"
                + "    <w:Button ui:field=\"action\" kind=\"PRIMARY\" "
                + "text=\"{tokens.getHome}\"/>\n"
                + "  </w:Panel>\n"
                + "</ui:UiBinder>\n");

    assertTrue(compilation.diagnostics(), compilation.success);
    assertContains(
        compilation.generated("fixture/Sample_BinderImpl.java"),
        "button2.setText(tokens.Tokens.getHome());");
    assertFalse(
        compilation.generated("fixture/Sample_BinderImpl.java").contains("new tokens.Tokens()"));
  }

  @Test
  public void instantiatesUiWithForInstanceMethodsAndFieldsOnly() throws Exception {
    for (final String member : new String[] {"getLabel", "caption", "HOME"}) {
      final Compilation compilation =
          compile(
              sampleOwner("com.google.gwt.event.dom.client.ClickEvent"),
              "<ui:UiBinder xmlns:ui=\"urn:ui:com.google.gwt.uibinder\" "
                  + "xmlns:w=\"urn:import:widgets\">"
                  + "<ui:with field=\"tokens\" type=\"tokens.Tokens\"/>"
                  + "<w:Panel><w:Button ui:field=\"action\" kind=\"PRIMARY\" text=\"{tokens."
                  + member
                  + "}\"/></w:Panel></ui:UiBinder>");
      assertTrue(compilation.diagnostics(), compilation.success);
      final String generated = compilation.generated("fixture/Sample_BinderImpl.java");
      if (member.equals("HOME")) {
        assertContains(generated, "tokens.Tokens.HOME");
        assertFalse(generated.contains("new tokens.Tokens()"));
      } else {
        assertContains(
            generated,
            "final tokens.Tokens uiWith_tokens = new tokens.Tokens();",
            "uiWith_tokens." + (member.equals("getLabel") ? "getLabel()" : "caption"));
      }
    }
  }

  @Test
  public void refusesAnExpressionNoUiWithDeclares() throws Exception {
    final Compilation compilation =
        compile(
            sampleOwner("com.google.gwt.event.dom.client.ClickEvent"),
            validTemplate().replace(">Save</w:Button>", " text=\"{absent.thing}\"/>"));

    // The failure that matters: emitted as a literal it would compile and run,
    // and the value would quietly be the text of the expression.
    assertFalse(compilation.success);
    assertTrue(
        compilation.diagnostics(),
        compilation
            .diagnostics()
            .contains("no <ui:with> declares absent, which {absent.thing} reads from"));
  }

  @Test
  public void refusesAUiElementItDoesNotImplement() throws Exception {
    final Compilation compilation =
        compile(
            sampleOwner("com.google.gwt.event.dom.client.ClickEvent"),
            validTemplate()
                .replace(
                    "<w:Panel>", "<w:Panel>\n    <ui:msg description=\"greeting\">Hello</ui:msg>"));

    assertFalse(compilation.success);
    assertTrue(
        compilation.diagnostics(),
        compilation.diagnostics().contains("<ui:msg> is not supported by this generator"));
  }

  @Test
  public void assignsOnlyTheFieldsTheOwnerDeclares() throws Exception {
    // A ui:field names an element for the template's own use; it does not oblige
    // the owner to hold it, and assigning one it never declared will not compile.
    final Compilation compilation =
        compile(
            sampleOwner("com.google.gwt.event.dom.client.ClickEvent"),
            validTemplate().replace("<w:Panel>", "<w:Panel ui:field=\"unheld\">"));

    assertTrue(compilation.diagnostics(), compilation.success);
    final String generated = compilation.generated("fixture/Sample_BinderImpl.java");
    assertContains(generated, "owner.action = button2;");
    assertFalse(
        "assigned a field the owner does not declare\n" + generated,
        generated.contains("owner.unheld"));
  }

  @Test
  public void treatsBackslashSequencesInTemplateTextAsJavaEscapes() throws Exception {
    // Every code sample in the showcase spells its line breaks this way, and GWT
    // renders them as line breaks because the text becomes a Java string literal.
    final Compilation compilation =
        compile(
            sampleOwner("com.google.gwt.event.dom.client.ClickEvent"),
            validTemplate().replace(">Save<", ">first\\nsecond<"));

    assertTrue(compilation.diagnostics(), compilation.success);
    assertContains(
        compilation.generated("fixture/Sample_BinderImpl.java"),
        "button2.setText(\"first\\nsecond\");");
  }

  @Test
  public void generatesNestedTextBundleWithBinaryServiceName() throws Exception {
    String owner =
        sampleOwner("com.google.gwt.event.dom.client.ClickEvent")
            .replace(
                "public class Sample {",
                "public class Sample {\n"
                    + "interface Source extends com.google.gwt.resources.client.ClientBundle {\n"
                    + "@com.google.gwt.resources.client.ClientBundle.Source(\"Sample.ui.xml\")\n"
                    + "com.google.gwt.resources.client.TextResource template(); }\n");
    Compilation compiled = compile(owner, validTemplate());
    assertTrue(compiled.diagnostics(), compiled.success);
    assertEquals(
        "fixture.Sample_Source_TextBundle\n",
        Files.readString(compiled.classes.resolve("META-INF/services/fixture.Sample$Source")));
    try (var loader =
        new java.net.URLClassLoader(new java.net.URL[] {compiled.classes.toUri().toURL()})) {
      Object bundle =
          loader.loadClass("fixture.Sample_Source_TextBundle").getConstructor().newInstance();
      Object text = bundle.getClass().getMethod("template").invoke(bundle);
      Class<?> resource = loader.loadClass("com.google.gwt.resources.client.TextResource");
      assertEquals(validTemplate(), resource.getMethod("getText").invoke(text));
      assertEquals("template", resource.getMethod("getName").invoke(text));
    }
  }

  @Test
  public void missingTextResourceFailsCompilationInsteadOfTheBrowser() throws Exception {
    Compilation compiled =
        compile(
            "package fixture; public interface Sample extends "
                + "com.google.gwt.resources.client.ClientBundle {\n"
                + "@com.google.gwt.resources.client.ClientBundle.Source(\"missing.xml\")\n"
                + "com.google.gwt.resources.client.TextResource template(); }",
            validTemplate());
    assertFalse(compiled.success);
    assertTrue(compiled.diagnostics(), compiled.diagnostics().contains("Missing @Source resource"));
  }

  private Compilation compile(final String owner, final String template) throws Exception {
    final Path workspace = Files.createTempDirectory("uibinder-processor-test-");
    workspaces.add(workspace);
    final Path sources = Files.createDirectories(workspace.resolve("src"));
    final Path generated = Files.createDirectories(workspace.resolve("generated"));
    final Path classes = Files.createDirectories(workspace.resolve("classes"));
    write(
        sources,
        "com/google/gwt/i18n/client/Constants.java",
        "package com.google.gwt.i18n.client; public interface Constants { @interface DefaultStringValue {String value();} }");
    write(
        sources,
        "com/google/gwt/i18n/client/ConstantsWithLookup.java",
        "package com.google.gwt.i18n.client; public interface ConstantsWithLookup extends Constants { String getString(String name); }");
    write(
        sources,
        "com/google/gwt/dom/client/Element.java",
        "package com.google.gwt.dom.client; public class Element { public void setAttribute(String a,String b){} public void appendChild(Object child){} }");
    write(
        sources,
        "com/google/gwt/dom/client/Document.java",
        "package com.google.gwt.dom.client; public class Document { public static Document get(){return new Document();} public Element createElement(String tag){return new Element();} public Object createTextNode(String text){return text;} }");

    write(
        sources,
        "com/google/gwt/resources/client/ClientBundle.java",
        "package com.google.gwt.resources.client; public interface ClientBundle {\n"
            + "@java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)\n"
            + "@interface Source { String[] value(); } }");
    write(
        sources,
        "com/google/gwt/resources/client/TextResource.java",
        "package com.google.gwt.resources.client; public interface TextResource {\n"
            + "String getText(); String getName(); }");

    write(
        sources,
        "com/google/gwt/uibinder/client/UiBinder.java",
        "package com.google.gwt.uibinder.client;\n"
            + "public interface UiBinder<R, O> { R createAndBindUi(O owner); }\n");
    write(sources, "com/google/gwt/uibinder/client/UiField.java", annotation("UiField", "FIELD"));
    write(
        sources,
        "com/google/gwt/uibinder/client/UiHandler.java",
        "package com.google.gwt.uibinder.client;\n"
            + "@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)\n"
            + "@java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)\n"
            + "public @interface UiHandler { String[] value(); }\n");
    write(
        sources,
        "com/google/gwt/uibinder/client/UiConstructor.java",
        annotation("UiConstructor", "CONSTRUCTOR"));
    write(
        sources,
        "com/google/gwt/dom/client/StyleInjector.java",
        "package com.google.gwt.dom.client;\n"
            + "public class StyleInjector { public static void inject(String css) {} }\n");
    write(
        sources,
        "com/google/gwt/event/dom/client/ClickEvent.java",
        "package com.google.gwt.event.dom.client; public class ClickEvent {}\n");
    write(
        sources,
        "com/google/gwt/event/dom/client/ClickHandler.java",
        "package com.google.gwt.event.dom.client;\n"
            + "public interface ClickHandler { void onClick(ClickEvent event); }\n");
    write(
        sources,
        "fixture/UnsupportedEvent.java",
        "package fixture; public class UnsupportedEvent {}\n");
    write(
        sources,
        "tokens/Tokens.java",
        "package tokens; public class Tokens {\n"
            + "  public static final String HOME = \"home\";\n"
            + "  public String caption = \"caption\";\n"
            + "  public String getLabel() { return caption; }\n"
            + "  public static String getHome() { return \"home\"; }\n"
            + "}\n");
    write(
        sources, "widgets/Kind.java", "package widgets; public enum Kind { PRIMARY, SECONDARY }\n");
    write(
        sources,
        "widgets/DomElement.java",
        "package widgets; public class DomElement { public void setId(String id) {} }\n");
    write(
        sources,
        "widgets/Panel.java",
        "package widgets; public class Panel {\n"
            + "  public com.google.gwt.dom.client.Element getElement(){return new com.google.gwt.dom.client.Element();}\n"
            + "  public void add(Object child) {}\n"
            + "}\n");
    write(
        sources,
        "widgets/BaseButton.java",
        "package widgets; public class BaseButton<T> {\n"
            + "  public void setValue(T value) {}\n"
            + "  public void setEnabled(boolean enabled) {}\n"
            + "  public void setText(String text) {}\n"
            + "  public void addStyleName(String style) {}\n"
            + "  public DomElement getElement() { return new DomElement(); }\n"
            + "}\n");
    write(
        sources,
        "widgets/Button.java",
        "package widgets;\n"
            + "public class Button extends BaseButton<Integer> {\n"
            + "  @com.google.gwt.uibinder.client.UiConstructor\n"
            + "  public Button(Kind kind) {}\n"
            + "  public void addClickHandler(com.google.gwt.event.dom.client.ClickHandler handler) {}\n"
            + "}\n");
    write(sources, "fixture/Sample.java", owner);
    write(sources, "fixture/Sample.ui.xml", template);

    final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    try (StandardJavaFileManager files =
        compiler.getStandardFileManager(diagnostics, Locale.ROOT, StandardCharsets.UTF_8)) {
      final List<Path> javaSources = new ArrayList<>();
      try (java.util.stream.Stream<Path> paths = Files.walk(sources)) {
        paths.filter(path -> path.toString().endsWith(".java")).forEach(javaSources::add);
      }
      final Iterable<? extends JavaFileObject> units =
          files.getJavaFileObjectsFromPaths(javaSources);
      final List<String> options =
          Arrays.asList(
              "--release", "17",
              "-classpath", System.getProperty("java.class.path"),
              "-sourcepath", sources.toString(),
              "-d", classes.toString(),
              "-s", generated.toString());
      final JavaCompiler.CompilationTask task =
          compiler.getTask(null, files, diagnostics, options, null, units);
      task.setProcessors(java.util.Collections.singletonList(new UiBinderProcessor()));
      return new Compilation(
          Boolean.TRUE.equals(task.call()), generated, classes, diagnostics.getDiagnostics());
    }
  }

  private static String sampleOwner(final String event) {
    return "package fixture;\n"
        + "public class Sample {\n"
        + "  interface Binder extends com.google.gwt.uibinder.client.UiBinder<widgets.Panel, Sample> {}\n"
        + "  @com.google.gwt.uibinder.client.UiField widgets.Button action;\n"
        + "  @com.google.gwt.uibinder.client.UiHandler(\"action\")\n"
        + "  void onAction("
        + event
        + " event) {}\n"
        + "}\n";
  }

  private static String validTemplate() {
    return "<ui:UiBinder xmlns:ui=\"urn:ui:com.google.gwt.uibinder\" "
        + "xmlns:w=\"urn:import:widgets\">\n"
        + "  <w:Panel>\n"
        + "    <w:Button ui:field=\"action\" kind=\"PRIMARY\" enabled=\"true\" "
        + "id=\"save-button\" addStyleNames=\"btn btn-primary\">Save</w:Button>\n"
        + "  </w:Panel>\n"
        + "</ui:UiBinder>\n";
  }

  private static String styledTemplate() {
    return "<ui:UiBinder xmlns:ui=\"urn:ui:com.google.gwt.uibinder\" "
        + "xmlns:w=\"urn:import:widgets\">\n"
        + "  <ui:style>\n"
        + "    .spacing { margin: 4px; }\n"
        + "    .margin-fix { margin-bottom: 0 !important; }\n"
        + "  </ui:style>\n"
        + "  <w:Panel>\n"
        + "    <w:Button ui:field=\"action\" kind=\"PRIMARY\" "
        + "addStyleNames=\"btn {style.spacing} {style.margin-fix}\">Save</w:Button>\n"
        + "  </w:Panel>\n"
        + "</ui:UiBinder>\n";
  }

  private static String annotation(final String name, final String target) {
    return "package com.google.gwt.uibinder.client;\n"
        + "@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)\n"
        + "@java.lang.annotation.Target(java.lang.annotation.ElementType."
        + target
        + ")\n"
        + "public @interface "
        + name
        + " {}\n";
  }

  private static void write(final Path root, final String relative, final String content)
      throws IOException {
    final Path file = root.resolve(relative);
    Files.createDirectories(file.getParent());
    Files.writeString(file, content, StandardCharsets.UTF_8);
  }

  private static void assertContains(final String value, final String... fragments) {
    for (final String fragment : fragments) {
      assertTrue(
          "generated source did not contain: " + fragment + "\n" + value, value.contains(fragment));
    }
  }

  private static final class Compilation {
    private final boolean success;
    private final Path generated;
    private final Path classes;
    private final List<Diagnostic<? extends JavaFileObject>> messages;

    Compilation(
        final boolean success,
        final Path generated,
        final Path classes,
        final List<Diagnostic<? extends JavaFileObject>> messages) {
      this.success = success;
      this.generated = generated;
      this.classes = classes;
      this.messages = messages;
    }

    String generated(final String relative) throws IOException {
      return Files.readString(generated.resolve(relative), StandardCharsets.UTF_8);
    }

    String diagnostics() {
      final StringBuilder output = new StringBuilder();
      for (final Diagnostic<? extends JavaFileObject> diagnostic : messages) {
        output
            .append(diagnostic.getKind())
            .append(": ")
            .append(diagnostic.getMessage(Locale.ROOT))
            .append('\n');
      }
      return output.toString();
    }
  }
}
