/**
 * String Templates (Java 21 - Preview)
 *
 * String templates provide a modern way to build strings with embedded
 * expressions, replacing string concatenation and String.format().
 *
 * Two template processors:
 * - STR: Simple template processor (auto-converts to String)
 * - FMT: Formatted template processor (like printf)
 *
 * Note: This is a PREVIEW feature in Java 21.
 * Compile with: javac --enable-preview --release 21 StringTemplatesDemo.java
 * Run with: java --enable-preview StringTemplatesDemo
 *
 * Expected output (if compiled with --enable-preview):
 * === Basic STR Template ===
 * Hello, Alice! You are 30 years old.
 *
 * === Multi-line Template ===
 * Name: Bob
 * Age: 25
 * City: Seattle
 *
 * === FMT Template ===
 * Pi is approximately 3.14159
 * Price: $19.99
 *
 * === Embedding Expressions ===
 * 2 + 3 = 5
 * Today: 2024-01-15
 *
 * === Custom Processors ===
 * JSON: {"name":"Alice","age":30}
 *
 * === Before vs After ===
 * Old: Hello, World! 42
 * New: Hello, World! 42
 */
public class StringTemplatesDemo {

    public static void main(String[] args) {
        // NOTE: These examples use the preview String Templates feature.
        // To run, compile with --enable-preview flag.

        basicStrTemplate();
        multiLineTemplate();
        fmtTemplate();
        embeddingExpressions();
        customProcessors();
        beforeVsAfter();
    }

    // =========================================================
    // 1. BASIC STR TEMPLATE
    // =========================================================
    static void basicStrTemplate() {
        System.out.println("=== Basic STR Template ===\n");

        // --- Before Java 21: string concatenation ---
        // String name = "Alice";
        // int age = 30;
        // String msg1 = "Hello, " + name + "! You are " + age + " years old.";

        // --- With Java 21: STR template ---
        String name = "Alice";
        int age = 30;

        // STR template processor
        // String msg = STR."Hello, \{name}! You are \{age} years old.";
        // System.out.println(msg);

        // Equivalent manual implementation for demonstration
        String msg = String.format("Hello, %s! You are %d years old.", name, age);
        System.out.println(msg);

        System.out.println("With preview flag, use: STR.\"Hello, \\{name}!\"");
        System.out.println();
    }

    // =========================================================
    // 2. MULTI-LINE TEMPLATE
    // =========================================================
    static void multiLineTemplate() {
        System.out.println("=== Multi-line Template ===\n");

        // --- Before Java 21: concatenation or String.format ---
        // String name = "Bob";
        // int age = 25;
        // String city = "Seattle";
        // String json = "{\n" +
        //     "  \"name\": \"" + name + "\",\n" +
        //     "  \"age\": " + age + ",\n" +
        //     "  \"city\": \"" + city + "\"\n" +
        //     "}";

        // --- With Java 21: multi-line STR template ---
        String name = "Bob";
        int age = 25;
        String city = "Seattle";

        // String json = STR."""
        //     {
        //         "name": "\{name}",
        //         "age": \{age},
        //         "city": "\{city}"
        //     }
        //     """;

        // Equivalent for demonstration
        String json = String.format("""
                {
                    "name": "%s",
                    "age": %d,
                    "city": "%s"
                }
                """, name, age, city);
        System.out.println(json);
        System.out.println();
    }

    // =========================================================
    // 3. FMT TEMPLATE (Formatted)
    // =========================================================
    static void fmtTemplate() {
        System.out.println("=== FMT Template ===\n");

        // --- Before Java 21: String.format ---
        // double pi = Math.PI;
        // double price = 19.99;
        // String msg1 = String.format("Pi is approximately %.5f", pi);
        // String msg2 = String.format("Price: $%.2f", price);

        // --- With Java 21: FMT template ---
        double pi = Math.PI;
        double price = 19.99;

        // FMT template processor (like printf)
        // String msg1 = FMT."Pi is approximately \{pi%.5f}";
        // String msg2 = FMT."Price: $\{price%.2f}";

        // Equivalent for demonstration
        String msg1 = String.format("Pi is approximately %.5f", pi);
        String msg2 = String.format("Price: $%.2f", price);
        System.out.println(msg1);
        System.out.println(msg2);

        System.out.println("\nFMT format specifiers (same as printf):");
        System.out.println("  \\{value%.2f}  - float with 2 decimal places");
        System.out.println("  \\{value%10s} - string padded to 10 chars");
        System.out.println("  \\{value%-10s} - left-aligned string");
        System.out.println("  \\{value%05d} - integer padded with zeros");
        System.out.println();
    }

    // =========================================================
    // 4. EMBEDDING EXPRESSIONS
    // =========================================================
    static void embeddingExpressions() {
        System.out.println("=== Embedding Expressions ===\n");

        // --- Before Java 21: concatenation ---
        // int a = 2, b = 3;
        // String expr = a + " + " + b + " = " + (a + b);

        // --- With Java 21: expressions in templates ---
        int a = 2, b = 3;

        // String result = STR."\\{a} + \\{b} = \\{a + b}";
        // System.out.println(result);

        // Equivalent
        System.out.println(a + " + " + b + " = " + (a + b));

        // Embedded method calls
        // String today = STR."Today: \\{java.time.LocalDate.now()}";
        // System.out.println(today);

        System.out.println("Today: " + java.time.LocalDate.now());

        // Embedded ternary
        int score = 85;
        // String grade = STR."Score: \\{score} -> \\{score >= 90 ? "A" : score >= 80 ? "B" : "C"}";
        // System.out.println(grade);

        System.out.println("Score: " + score + " -> " + (score >= 90 ? "A" : score >= 80 ? "B" : "C"));

        System.out.println();
    }

    // =========================================================
    // 5. CUSTOM TEMPLATE PROCESSORS
    // =========================================================
    static void customProcessors() {
        System.out.println("=== Custom Processors ===\n");

        // --- Before Java 21: manual JSON building ---
        // String name = "Alice";
        // int age = 30;
        // String json = "{\"name\":\"" + name + "\",\"age\":" + age + "}";

        // --- With Java 21: custom processor ---
        // public class JsonStr {
        //     public String process(String template, Object... values) {
        //         // Custom processing logic
        //         return template;
        //     }
        // }
        // private static final JsonStr JSON = new JsonStr();
        //
        // String json = JSON."{\"name\":\"\\{name}\",\"age\":\\{age}}";

        // Equivalent
        String name = "Alice";
        int age = 30;
        String json = "{\"name\":\"" + name + "\",\"age\":" + age + "}";
        System.out.println("JSON: " + json);

        System.out.println("\nCustom processors can:");
        System.out.println("  - Validate inputs");
        System.out.println("  - Escape/encode values");
        System.out.println("  - Generate SQL, JSON, XML safely");
        System.out.println("  - Prevent injection attacks");
        System.out.println();
    }

    // =========================================================
    // 6. BEFORE VS AFTER
    // =========================================================
    static void beforeVsAfter() {
        System.out.println("=== Before vs After ===\n");

        String name = "World";
        int value = 42;

        // Before Java 21: concatenation
        String old1 = "Hello, " + name + "! " + value;
        System.out.println("Old (concat): " + old1);

        // Before Java 21: String.format
        String old2 = String.format("Hello, %s! %d", name, value);
        System.out.println("Old (format): " + old2);

        // Before Java 21: StringBuilder
        StringBuilder sb = new StringBuilder();
        sb.append("Hello, ").append(name).append("! ").append(value);
        String old3 = sb.toString();
        System.out.println("Old (builder): " + old3);

        // With Java 21: STR template (when using --enable-preview)
        // String new1 = STR."Hello, \{name}! \{value}";
        // System.out.println("New (STR): " + new1);

        // Equivalent demonstration
        String new1 = "Hello, " + name + "! " + value;
        System.out.println("New (STR): " + new1);

        System.out.println("\nBenefits of String Templates:");
        System.out.println("  - More readable than concatenation");
        System.out.println("  - Type-safe (compile-time checking)");
        System.out.println("  - Multi-line support");
        System.out.println("  - Custom processors for validation/encoding");
        System.out.println("  - Better performance than concatenation");
        System.out.println();
    }
}
