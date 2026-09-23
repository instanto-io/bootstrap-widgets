import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Makes the showcases' source maps usable from the published site, and fails when they
 * are not.
 *
 * <p>Run by Maven with the JDK's single-file launcher, so the build needs nothing else:
 *
 * <pre>
 *   java build-tools/SourceMaps.java gwt   OUTPUT_DIR [--required]
 *   java build-tools/SourceMaps.java teavm SCRIPT     [--required]
 * </pre>
 *
 * <p>{@code gwt} takes a compiled GWT module's output directory. GWT leaves each map in
 * {@code symbolMaps}; this copies it to where the script's {@code sourceMappingURL} points,
 * sets its {@code sourceRoot} to the sources {@code -saveSource} wrote, and checks that every
 * source it names is there. {@code teavm} checks the same of a TeaVM script's map.
 *
 * <p>Without {@code --required}, output that has not been compiled -- a plain {@code mvn
 * install} does not run the GWT compiler -- is skipped rather than failed.
 */
public final class SourceMaps {
  private static final Pattern SOURCE_MAP_URL = Pattern.compile("sourceMappingURL=(\\S+)");

  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      throw new IllegalArgumentException("Usage: SourceMaps gwt|teavm PATH [--required]");
    }
    boolean required = args.length > 2 && args[2].equals("--required");
    Path path = Path.of(args[1]).toAbsolutePath().normalize();
    switch (args[0]) {
      case "gwt" -> prepareGwt(path, required);
      case "teavm" -> verifyTeaVm(path, required);
      default -> throw new IllegalArgumentException("Unknown mode " + args[0]);
    }
  }

  static void prepareGwt(Path output, boolean required) throws IOException {
    List<Path> scripts = new ArrayList<>();
    if (Files.isDirectory(output)) {
      try (Stream<Path> files = Files.list(output)) {
        files.filter(f -> f.getFileName().toString().endsWith(".cache.js")).sorted().forEach(scripts::add);
      }
      Path deferred = output.resolve("deferredjs");
      if (Files.isDirectory(deferred)) {
        try (Stream<Path> files = Files.walk(deferred, 2)) {
          files.filter(f -> f.getFileName().toString().endsWith(".cache.js")).sorted().forEach(scripts::add);
        }
      }
    }
    if (scripts.isEmpty()) {
      skipOrFail(required, "No compiled GWT scripts under " + output);
      return;
    }
    Path centralMaps = output.resolve("symbolMaps");
    for (Path script : scripts) {
      Path relativeMap = Path.of(sourceMapUrl(script));
      Path targetMap = script.getParent().resolve(relativeMap).normalize();
      if (!Files.isRegularFile(targetMap)) {
        Path sourceMap = centralMaps.resolve(relativeMap.getFileName());
        if (!Files.isRegularFile(sourceMap)) {
          throw new IllegalStateException("Missing source map for " + script + ": " + sourceMap);
        }
        Files.createDirectories(targetMap.getParent());
        Files.copy(sourceMap, targetMap, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
      }
      @SuppressWarnings("unchecked")
      Map<String, Object> map = (Map<String, Object>) Json.parse(Files.readString(targetMap, StandardCharsets.UTF_8));
      String sourceRoot = targetMap.getParent().relativize(output.resolve("src")).toString().replace('\\', '/');
      map.put("sourceRoot", sourceRoot);
      Files.writeString(targetMap, Json.write(map), StandardCharsets.UTF_8);
      requireSources(map, targetMap.getParent().resolve(sourceRoot), "GWT source map", targetMap);
    }
    System.out.println("GWT " + output.getFileName() + ": " + scripts.size() + " scripts have resolvable maps and Java sources");
  }

  static void verifyTeaVm(Path script, boolean required) throws IOException {
    if (!Files.isRegularFile(script)) {
      skipOrFail(required, "No TeaVM script at " + script);
      return;
    }
    Path sourceMap = script.getParent().resolve(sourceMapUrl(script)).normalize();
    @SuppressWarnings("unchecked")
    Map<String, Object> map = (Map<String, Object>) Json.parse(Files.readString(sourceMap, StandardCharsets.UTF_8));
    Object root = map.getOrDefault("sourceRoot", "");
    int count = requireSources(map, sourceMap.getParent().resolve(String.valueOf(root)), "TeaVM source map", sourceMap);
    System.out.println("TeaVM " + script.getFileName() + ": " + count + " mapped Java sources are published");
  }

  private static int requireSources(Map<String, Object> map, Path root, String what, Path mapFile) {
    Object sources = map.get("sources");
    List<?> names = sources instanceof List<?> list ? list : List.of();
    long missing = names.stream().filter(s -> !Files.isRegularFile(root.resolve(String.valueOf(s)).normalize())).count();
    if (names.isEmpty() || missing > 0) {
      throw new IllegalStateException(what + " has " + missing + " unresolved of " + names.size() + " sources: " + mapFile);
    }
    return names.size();
  }

  private static String sourceMapUrl(Path script) throws IOException {
    byte[] bytes = Files.readAllBytes(script);
    int from = Math.max(0, bytes.length - 1024);
    String tail = new String(bytes, from, bytes.length - from, StandardCharsets.UTF_8);
    Matcher matcher = SOURCE_MAP_URL.matcher(tail);
    String url = null;
    while (matcher.find()) url = matcher.group(1);
    if (url == null) throw new IllegalStateException("No sourceMappingURL in " + script);
    return url;
  }

  private static void skipOrFail(boolean required, String message) {
    if (required) throw new IllegalStateException(message);
    System.out.println(message + "; skipping");
  }

  /** Enough JSON for source maps: objects keep their key order, numbers stay as written. */
  static final class Json {
    private final String text;
    private int at;

    private Json(String text) { this.text = text; }

    static Object parse(String text) {
      Json json = new Json(text);
      Object value = json.value();
      json.space();
      if (json.at != text.length()) throw json.error("Trailing content");
      return value;
    }

    static String write(Object value) {
      StringBuilder out = new StringBuilder();
      write(value, out);
      return out.toString();
    }

    private Object value() {
      space();
      if (at >= text.length()) throw error("Unexpected end");
      char c = text.charAt(at);
      if (c == '{') return object();
      if (c == '[') return array();
      if (c == '"') return string();
      if (text.startsWith("true", at)) { at += 4; return Boolean.TRUE; }
      if (text.startsWith("false", at)) { at += 5; return Boolean.FALSE; }
      if (text.startsWith("null", at)) { at += 4; return null; }
      return number();
    }

    private Map<String, Object> object() {
      Map<String, Object> map = new LinkedHashMap<>();
      at++;
      space();
      if (text.charAt(at) == '}') { at++; return map; }
      while (true) {
        space();
        String key = string();
        space();
        expect(':');
        map.put(key, value());
        space();
        if (text.charAt(at) == ',') { at++; continue; }
        expect('}');
        return map;
      }
    }

    private List<Object> array() {
      List<Object> list = new ArrayList<>();
      at++;
      space();
      if (text.charAt(at) == ']') { at++; return list; }
      while (true) {
        list.add(value());
        space();
        if (text.charAt(at) == ',') { at++; continue; }
        expect(']');
        return list;
      }
    }

    private String string() {
      expect('"');
      StringBuilder out = new StringBuilder();
      while (true) {
        char c = text.charAt(at++);
        if (c == '"') return out.toString();
        if (c != '\\') { out.append(c); continue; }
        char e = text.charAt(at++);
        switch (e) {
          case 'b' -> out.append('\b');
          case 'f' -> out.append('\f');
          case 'n' -> out.append('\n');
          case 'r' -> out.append('\r');
          case 't' -> out.append('\t');
          case 'u' -> { out.append((char) Integer.parseInt(text.substring(at, at + 4), 16)); at += 4; }
          default -> out.append(e);
        }
      }
    }

    private Object number() {
      int start = at;
      while (at < text.length() && "+-0123456789.eE".indexOf(text.charAt(at)) >= 0) at++;
      if (start == at) throw error("Unexpected character");
      return new JsonNumber(text.substring(start, at));
    }

    private void space() {
      while (at < text.length() && Character.isWhitespace(text.charAt(at))) at++;
    }

    private void expect(char c) {
      if (at >= text.length() || text.charAt(at) != c) throw error("Expected " + c);
      at++;
    }

    private IllegalArgumentException error(String message) {
      return new IllegalArgumentException(message + " at offset " + at);
    }

    private static void write(Object value, StringBuilder out) {
      if (value == null) out.append("null");
      else if (value instanceof String s) quote(s, out);
      else if (value instanceof Map<?, ?> map) {
        out.append('{');
        boolean comma = false;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
          if (comma) out.append(',');
          comma = true;
          quote(String.valueOf(entry.getKey()), out);
          out.append(':');
          write(entry.getValue(), out);
        }
        out.append('}');
      } else if (value instanceof List<?> list) {
        out.append('[');
        for (int i = 0; i < list.size(); i++) {
          if (i > 0) out.append(',');
          write(list.get(i), out);
        }
        out.append(']');
      } else out.append(value);
    }

    private static void quote(String s, StringBuilder out) {
      out.append('"');
      for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        switch (c) {
          case '"' -> out.append("\\\"");
          case '\\' -> out.append("\\\\");
          case '\n' -> out.append("\\n");
          case '\r' -> out.append("\\r");
          case '\t' -> out.append("\\t");
          default -> {
            if (c < 0x20) out.append(String.format("\\u%04x", (int) c));
            else out.append(c);
          }
        }
      }
      out.append('"');
    }
  }

  /** A number kept exactly as written, so rewriting a map does not reformat its values. */
  record JsonNumber(String text) {
    @Override public String toString() { return text; }
  }
}
