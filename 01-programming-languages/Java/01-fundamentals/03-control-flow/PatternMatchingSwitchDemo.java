/**
 * Pattern Matching for switch (Java 21 - Final)
 *
 * Pattern matching for switch allows you to test and deconstruct values
 * directly in switch cases, replacing verbose if-else chains.
 *
 * Features:
 * - Type patterns: case String s -> ...
 * - Guarded patterns: case String s when s.length() > 5 -> ...
 * - Null handling: case null -> ...
 * - Dominance: more specific patterns before less specific
 *
 * Expected output:
 * === Basic Pattern Matching ===
 * Hello is a String of length 5
 * 42 is an Integer: doubled = 84
 * 3.14 is a Double: rounded = 3
 * true is a Boolean: negated = false
 *
 * === Guarded Patterns ===
 * "Java" is short (length 4)
 * "Programming" is long (length 11)
 *
 * === Null Handling ===
 * null is handled explicitly
 * "test" is a non-null String
 *
 * === Sealed Class Pattern ===
 * Circle with radius 5.0
 * Rectangle 10.0 x 20.0
 * Unknown shape
 */
public class PatternMatchingSwitchDemo {

    // Sealed interface for pattern matching
    sealed interface Shape permits Circle, Rectangle, Triangle {}
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
    record Triangle(double a, double b, double c) implements Shape {}

    public static void main(String[] args) {
        basicPatternMatching();
        guardedPatterns();
        nullHandling();
        sealedClassPatterns();
        complexExpressions();
    }

    // =========================================================
    // 1. BASIC PATTERN MATCHING
    // =========================================================
    static void basicPatternMatching() {
        System.out.println("=== Basic Pattern Matching ===\n");

        // --- Before Java 21: verbose instanceof + cast ---
        // Object obj = "Hello";
        // if (obj instanceof String) {
        //     String s = (String) obj;
        //     System.out.println(s + " is a String of length " + s.length());
        // } else if (obj instanceof Integer) {
        //     Integer i = (Integer) obj;
        //     System.out.println(i + " is an Integer: doubled = " + (i * 2));
        // }

        // --- With Java 21: pattern matching in switch ---
        Object[] values = {"Hello", 42, 3.14, true};

        for (Object obj : values) {
            String result = switch (obj) {
                case String s  -> s + " is a String of length " + s.length();
                case Integer i -> i + " is an Integer: doubled = " + (i * 2);
                case Double d  -> d + " is a Double: rounded = " + (int) Math.round(d);
                case Boolean b -> b + " is a Boolean: negated = " + (!b);
                default        -> "Unknown type: " + obj.getClass().getSimpleName();
            };
            System.out.println(result);
        }

        System.out.println();
    }

    // =========================================================
    // 2. GUARDED PATTERNS (when clauses)
    // =========================================================
    static void guardedPatterns() {
        System.out.println("=== Guarded Patterns ===\n");

        // --- Before Java 21: if-else chain ---
        // String word = "Programming";
        // if (word instanceof String) {
        //     if (word.length() <= 5) {
        //         System.out.println(word + " is short");
        //     } else {
        //         System.out.println(word + " is long");
        //     }
        // }

        // --- With Java 21: guarded patterns in switch ---
        String[] words = {"Java", "Programming", "Go", "TypeScript"};

        for (String word : words) {
            String category = switch (word) {
                case String s when s.length() <= 3  -> "\"" + s + "\" is very short (<=3 chars)";
                case String s when s.length() <= 5  -> "\"" + s + "\" is short (length " + s.length() + ")";
                case String s when s.length() <= 8  -> "\"" + s + "\" is medium (length " + s.length() + ")";
                case String s                       -> "\"" + s + "\" is long (length " + s.length() + ")";
            };
            System.out.println(category);
        }

        System.out.println();
    }

    // =========================================================
    // 3. NULL HANDLING
    // =========================================================
    static void nullHandling() {
        System.out.println("=== Null Handling ===\n");

        // --- Before Java 21: null checks before switch ---
        // Object obj = null;
        // if (obj == null) {
        //     System.out.println("null is handled explicitly");
        // } else {
        //     switch (obj) { ... }
        // }

        // --- With Java 21: case null in switch ---
        Object[] values = {null, "test", 123};

        for (Object obj : values) {
            String result = switch (obj) {
                case null      -> "null is handled explicitly";
                case String s  -> "String: " + s;
                case Integer i -> "Integer: " + i;
                default        -> "Other: " + obj;
            };
            System.out.println(result);
        }

        System.out.println();
    }

    // =========================================================
    // 4. SEALED CLASS PATTERNS
    // =========================================================
    static void sealedClassPatterns() {
        System.out.println("=== Sealed Class Pattern ===\n");

        Shape[] shapes = {
            new Circle(5.0),
            new Rectangle(10.0, 20.0),
            new Triangle(3.0, 4.0, 5.0)
        };

        for (Shape shape : shapes) {
            // --- Before Java 21: instanceof chains ---
            // if (shape instanceof Circle c) {
            //     System.out.printf("Circle with radius %.1f%n", c.radius());
            // } else if (shape instanceof Rectangle r) {
            //     System.out.printf("Rectangle %.1f x %.1f%n", r.width(), r.height());
            // }

            // --- With Java 21: exhaustive pattern matching ---
            String description = switch (shape) {
                case Circle c    -> String.format("Circle with radius %.1f", c.radius());
                case Rectangle r -> String.format("Rectangle %.1f x %.1f", r.width(), r.height());
                case Triangle t  -> String.format("Triangle %.1f, %.1f, %.1f", t.a(), t.b(), t.c());
                // No default needed - compiler knows all cases are covered!
            };
            System.out.println(description);
        }

        System.out.println();
    }

    // =========================================================
    // 5. COMPLEX EXPRESSIONS
    // =========================================================
    static void complexExpressions() {
        System.out.println("=== Complex Expressions ===\n");

        // Combining patterns with logical operators
        Object obj = "Hello World";

        String result = switch (obj) {
            case null                  -> "null";
            case Integer i when i > 0  -> "Positive integer: " + i;
            case Integer i             -> "Non-positive integer: " + i;
            case String s when s.contains(" ") -> "String with space: \"" + s + "\"";
            case String s              -> "String without space: \"" + s + "\"";
            default                    -> "Other";
        };
        System.out.println(result);

        // Pattern matching with expressions
        Object value = 42;
        boolean isPositiveInteger = switch (value) {
            case Integer i when i > 0 -> true;
            default -> false;
        };
        System.out.println("Is positive integer: " + isPositiveInteger);

        System.out.println();
    }
}
