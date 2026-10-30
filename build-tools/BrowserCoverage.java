import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

/**
 * Exports TeaCoCo Java coverage against checked-in sources for SonarQube.
 * The TeaVM adapter changes formatting and native bridges. Only complete,
 * unchanged token lines are mapped: changed expressions receive no guessed hits.
 * The original JaCoCo XML and raw execution data remain available for auditing.
 * Run with the JDK: java build-tools/BrowserCoverage.java [--self-test].
 */
public class BrowserCoverage {
  private static final Pattern TOKEN = Pattern.compile(
      "//[^\\n]*|/\\*[\\s\\S]*?\\*/|\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'|[A-Za-z_$][\\w$]*|\\d+(?:\\.\\d+)?|\\S");

  record Token(String text, int line) {}
  record Range(int a, int endA, int b, int endB) {}
  record Match(int a, int b, int length) {}
  record Counts(boolean hit, int branches, int covered) {
    Counts combine(Counts other) {
      // Multiple adapted lines can occupy one original line. Requiring all
      // contributing lines hit is conservative; this is not a union of probes.
      return new Counts(hit && other.hit, branches + other.branches, covered + other.covered);
    }
  }

  static List<Token> tokens(String text) {
    List<Token> result = new ArrayList<>();
    Matcher matcher = TOKEN.matcher(text);
    int line = 1;
    int previous = 0;
    while (matcher.find()) {
      line += (int) text.substring(previous, matcher.start()).chars().filter(c -> c == '\n').count();
      String token = matcher.group();
      if (!token.startsWith("//") && !token.startsWith("/*")) result.add(new Token(token, line));
      line += (int) token.chars().filter(c -> c == '\n').count();
      previous = matcher.end();
    }
    return result;
  }

  static Match longest(List<Token> a, Map<String, List<Integer>> positions, Range range) {
    Map<Integer, Integer> previous = Map.of();
    Match best = new Match(range.a, range.b, 0);
    for (int i = range.a; i < range.endA; i++) {
      Map<Integer, Integer> current = new HashMap<>();
      for (int j : positions.getOrDefault(a.get(i).text, List.of())) {
        if (j < range.b) continue;
        if (j >= range.endB) break;
        int length = previous.getOrDefault(j - 1, 0) + 1;
        current.put(j, length);
        if (length > best.length) best = new Match(i - length + 1, j - length + 1, length);
      }
      previous = current;
    }
    return best;
  }

  static Map<Integer, Integer> lineMapping(String original, String adapted) {
    List<Token> old = tokens(original);
    List<Token> generated = tokens(adapted);
    Map<String, List<Integer>> positions = new HashMap<>();
    for (int i = 0; i < generated.size(); i++) {
      positions.computeIfAbsent(generated.get(i).text, key -> new ArrayList<>()).add(i);
    }
    Map<Integer, Integer> matches = new HashMap<>();
    Set<Integer> matchedOriginal = new HashSet<>();
    Deque<Range> pending = new ArrayDeque<>();
    pending.add(new Range(0, old.size(), 0, generated.size()));
    while (!pending.isEmpty()) {
      Range range = pending.removeLast();
      Match match = longest(old, positions, range);
      if (match.length == 0) continue;
      for (int i = 0; i < match.length; i++) {
        matches.put(match.b + i, old.get(match.a + i).line);
        matchedOriginal.add(match.a + i);
      }
      if (range.a < match.a && range.b < match.b) {
        pending.add(new Range(range.a, match.a, range.b, match.b));
      }
      if (match.a + match.length < range.endA && match.b + match.length < range.endB) {
        pending.add(new Range(match.a + match.length, range.endA, match.b + match.length, range.endB));
      }
    }
    Map<Integer, Set<Integer>> destinations = new HashMap<>();
    for (int i = 0; i < generated.size(); i++) {
      destinations.computeIfAbsent(generated.get(i).line, key -> new HashSet<>()).add(matches.getOrDefault(i, -1));
    }
    Map<Integer, Integer> mapped = new HashMap<>();
    Set<Integer> changedOriginalLines = new HashSet<>();
    for (int i = 0; i < old.size(); i++) {
      if (!matchedOriginal.contains(i)) changedOriginalLines.add(old.get(i).line);
    }
    destinations.forEach((line, targets) -> {
      if (targets.size() == 1 && !targets.contains(-1)) {
        int target = targets.iterator().next();
        if (!changedOriginalLines.contains(target)) mapped.put(line, target);
      }
    });
    return mapped;
  }

  private static List<Element> children(Element element, String tag) {
    List<Element> result = new ArrayList<>();
    for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
      if (node instanceof Element child && child.getTagName().equals(tag)) result.add(child);
    }
    return result;
  }

  private static DocumentBuilder builder() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    return factory.newDocumentBuilder();
  }

  private static void readReport(Path root, int generation,
      Map<String, Map<Integer, Counts>> files, List<String> unmapped) throws Exception {
    Path library = root.resolve("teavm/teavm-bootstrap" + generation);
    Path tests = root.resolve("teavm/teavm-bootstrap-widget-tests/bootstrap" + generation + "/target");
    Element report = builder().parse(tests.resolve("teavm-coverage.xml").toFile()).getDocumentElement();
    List<Path> shared;
    try (var modules = Files.list(root.resolve("gwt"))) {
      shared = modules.filter(p -> p.getFileName().toString().startsWith("gwt-bootstrap" + generation))
          .map(p -> p.resolve("src/main/java")).sorted().toList();
    }
    for (Element pkg : children(report, "package")) {
      for (Element source : children(pkg, "sourcefile")) {
        Path relative = Path.of(pkg.getAttribute("name"), source.getAttribute("name"));
        Path original = library.resolve("src/main/java").resolve(relative);
        if (!Files.isRegularFile(original)) {
          List<Path> candidates = shared.stream().map(p -> p.resolve(relative)).filter(Files::isRegularFile).toList();
          if (candidates.size() != 1) throw new IllegalStateException("Expected one original source for " + relative + ": " + candidates);
          original = candidates.get(0);
        }
        Path prepared = tests.resolve("coverage-sources").resolve(relative);
        Map<Integer, Integer> mapping = lineMapping(Files.readString(original), Files.readString(prepared));
        String path = root.relativize(original).toString().replace('\\', '/');
        Map<Integer, Counts> lines = files.computeIfAbsent(path, key -> new TreeMap<>());
        for (Element line : children(source, "line")) {
          int adaptedLine = Integer.parseInt(line.getAttribute("nr"));
          Integer number = mapping.get(adaptedLine);
          if (number == null) {
            unmapped.add(path + ": adapted line " + adaptedLine);
            continue;
          }
          int covered = Integer.parseInt(line.getAttribute("cb"));
          Counts counts = new Counts(Integer.parseInt(line.getAttribute("ci")) > 0,
              Integer.parseInt(line.getAttribute("mb")) + covered, covered);
          lines.merge(number, counts, Counts::combine);
        }
      }
    }
  }

  private static void export(Path root) throws Exception {
    Map<String, Map<Integer, Counts>> files = new TreeMap<>();
    List<String> unmapped = new ArrayList<>();
    for (int generation : new int[] {3, 5}) readReport(root, generation, files, unmapped);
    Document document = builder().newDocument();
    Element coverage = document.createElement("coverage");
    coverage.setAttribute("version", "1");
    document.appendChild(coverage);
    int hits = 0;
    for (var entry : files.entrySet()) {
      if (entry.getValue().isEmpty()) continue;
      Element file = document.createElement("file");
      file.setAttribute("path", root.resolve(entry.getKey()).toString());
      coverage.appendChild(file);
      for (var line : entry.getValue().entrySet()) {
        Counts counts = line.getValue();
        if (counts.hit) hits++;
        Element result = document.createElement("lineToCover");
        result.setAttribute("lineNumber", line.getKey().toString());
        result.setAttribute("covered", Boolean.toString(counts.hit));
        if (counts.branches > 0) {
          result.setAttribute("branchesToCover", Integer.toString(counts.branches));
          result.setAttribute("coveredBranches", Integer.toString(counts.covered));
        }
        file.appendChild(result);
      }
    }
    if (hits == 0) throw new IllegalStateException("No browser hits mapped to checked-in Java sources");
    Path target = Files.createDirectories(root.resolve("target"));
    TransformerFactory factory = TransformerFactory.newInstance();
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
    Transformer transformer = factory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.transform(new DOMSource(document), new StreamResult(target.resolve("browser-coverage.xml").toFile()));
    Files.write(target.resolve("browser-coverage-unmapped.txt"), unmapped);
    System.out.printf("Browser coverage: %d covered lines in %d source files; %d adapted lines left unmapped%n", hits, files.size(), unmapped.size());
  }

  private static void check(boolean condition) {
    if (!condition) throw new AssertionError("Coverage mapping regression");
  }

  private static void selfTest() {
    String original = "class Example {\n int n;\n void update() {\n  n++;\n }\n}\n";
    String adapted = "// generated\nclass Example {\n\n int n;\n\n void update() {\n  n++;\n }\n}\n";
    check(lineMapping(original, adapted).get(7) == 4);
    check(!lineMapping("return oldValue();", "return newValue();").containsKey(1));
    check(!lineMapping("return value() + additionalWork();", "return value();").containsKey(1));
    check(!lineMapping("int a = 1;\nint b = 2;", "int a = 1; int b = 2;").containsKey(1));
    check(lineMapping("String s = \"// literal\";\n/* comment\n */\nreturn s;",
        "// notice\nString s = \"// literal\";\nreturn s;").get(3) == 4);
    System.out.println("Coverage source mapping: 5 checks passed");
  }

  public static void main(String[] args) throws Exception {
    if (args.length == 1 && args[0].equals("--self-test")) selfTest();
    else export(Path.of("").toAbsolutePath().normalize());
  }
}
