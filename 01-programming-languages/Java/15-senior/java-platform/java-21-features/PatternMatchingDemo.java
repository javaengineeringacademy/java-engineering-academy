package academy.javaengineering.senior.java21;

public class PatternMatchingDemo {

    public static void main(String[] args) {
        patternMatchingForInstanceof();
        patternMatchingForSwitch();
        guardedPatterns();
        nullPatterns();
    }

    // ==================== Pattern Matching for instanceof ====================

    static void patternMatchingForInstanceof() {
        System.out.println("=== Pattern Matching for instanceof ===\n");

        Object obj = "Hello, Java 21!";

        if (obj instanceof String s) {
            System.out.println("String length: " + s.length());
            System.out.println("Uppercase: " + s.toUpperCase());
        }

        Object num = 42;
        if (num instanceof Integer i && i > 0) {
            System.out.println("Positive integer: " + i);
        }

        record Point(int x, int y) {}

        Object point = new Point(3, 7);
        if (point instanceof Point(int x, int y)) {
            System.out.println("Point: (" + x + ", " + y + ")");
        }
    }

    // ==================== Pattern Matching for switch ====================

    static void patternMatchingForSwitch() {
        System.out.println("\n=== Pattern Matching for switch ===\n");

        System.out.println("describe(42) = " + describe(42));
        System.out.println("describe(3.14) = " + describe(3.14));
        System.out.println("describe(\"hello\") = " + describe("hello"));
        System.out.println("describe(true) = " + describe(true));
        System.out.println("describe(null) = " + describe(null));
    }

    static String describe(Object obj) {
        return switch (obj) {
            case Integer i when i > 0 -> "Positive integer: " + i;
            case Integer i when i < 0 -> "Negative integer: " + i;
            case Integer i -> "Zero";
            case Double d -> "Double: " + d;
            case String s && !s.isEmpty() -> "Non-empty string: \"" + s + "\"";
            case String s -> "Empty string";
            case Boolean b -> "Boolean: " + b;
            case null -> "Null";
            default -> "Unknown: " + obj.getClass().getSimpleName();
        };
    }

    // ==================== Guarded Patterns ====================

    static void guardedPatterns() {
        System.out.println("\n=== Guarded Patterns ===\n");

        record Range(int start, int end) {}

        Range range = new Range(10, 50);

        String description = switch (range) {
            case Range(int s, int e) when s == 0 && e == 100 -> "Full range";
            case Range(int s, int e) when s < e -> "Valid range [" + s + ", " + e + "]";
            case Range(int s, int e) -> "Invalid range [" + s + ", " + e + "]";
        };

        System.out.println(description);

        Object[] values = {1, "test", 3.14, null, List.of(1, 2, 3)};
        for (Object v : values) {
            System.out.println("  " + formatWithType(v));
        }
    }

    static String formatWithType(Object obj) {
        return switch (obj) {
            case Integer i && i % 2 == 0 -> "Even int: " + i;
            case Integer i -> "Odd int: " + i;
            case String s && s.length() > 3 -> "Long string: \"" + s + "\"";
            case String s -> "Short string: \"" + s + "\"";
            case Double d -> "Double: " + d;
            case null -> "null";
            default -> obj.toString();
        };
    }

    // ==================== Null Patterns ====================

    static void nullPatterns() {
        System.out.println("\n=== Null Patterns ===\n");

        String[] inputs = {"hello", "", null, "world"};

        for (String input : inputs) {
            String result = switch (input) {
                case null -> "Got null!";
                case String s && s.isEmpty() -> "Empty string";
                case String s -> "String: " + s;
            };
            System.out.println("  \"" + input + "\" -> " + result);
        }

        record Config(String name, int port) {}

        Config[] configs = {
            new Config("api", 8080),
            null,
            new Config("db", 5432)
        };

        for (Config config : configs) {
            String info = switch (config) {
                case null -> "No config";
                case Config(String n, int p) -> n + " on port " + p;
            };
            System.out.println("  " + info);
        }
    }
}
