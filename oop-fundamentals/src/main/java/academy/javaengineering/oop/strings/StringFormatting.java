package academy.javaengineering.oop.strings;

/**
 * Demonstrates
The user said "retry" because the previous tool call failed? Actually they said "retry why it failed". The previous tool call succeeded (write a method that demonstrates string formatting with printf, format, and text blocks.
 */
public final class StringFormatting {

    private StringFormatting() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== String Formatting ===\n");

        // Basic formatting
        System.out.println("--- Basic Formatting ---");
        String name = "Alice";
        int age = 30;
        double score = 95.5;
        
        String formatted = String.format("Name: %s, Age: %d, Score: %.1f", name, age, score);
        System.out.println(formatted); // Name: Alice, Age: 30, Score: 95.5
        
        System.out.printf("Name: %s, Age: %d, Score: %.1f%n", name, age, score);

        // Width and alignment
        System.out.println("\n--- Width & Alignment ---");
        System.out.printf("|%10s|%n", "hello");    // Right-aligned (default)
        System.out.printf("|%-10s|%n", "hello");   // Left-aligned
        System.out.printf("|%10d|%n", 42);          // Right-aligned number
        System.out.printf("|%-10d|%n", 42);         // Left-aligned number

        // Zero padding
        System.out.println("\n--- Zero Padding ---");
        System.out.printf("ID: %05d%n", 42);      // 00042
        System.out.printf("ID: %05d%n", 1234);    // 01234
        System.out.printf("Hex: %04X%n", 255);    // 00FF

        // Precision for floating-point
        System.out.println("\n--- Precision ---");
        double pi = Math.PI;
        System.out.printf("PI = %.2f%n", pi);    // 3.14
        System.out.printf("PI = %.5f%n", pi);    // 3.14159
        System.out.printf("PI = %10.4f%n", pi);  // width 10, 4 decimals

        // Scientific notation
        System.out.println("\n--- Scientific Notation ---");
        double large = 1234567.89;
        double small = 0.000123;
        System.out.printf("Large: %e%n", large);  // 1.234568e+06
        System.out.printf("Small: %e%n", small);  // 1.230000e-04
        System.out.printf("Large: %.2E%n", large); // 1.23E+06 (uppercase E)

        // Grouping separators
        System.out.println("\n--- Grouping Separators ---");
        int million = 1_000_000;
        System.out.printf("%,d%n", million);       // 1,000,000
        System.out.printf("%,.2f%n", 1234567.89);  // 1,234,567.89

        // Date/Time formatting
        System.out.println("\n--- Date/Time ---");
        java.time.LocalDate date = java.time.LocalDate.of(2024, 1, 15);
        java.time.LocalTime time = java.time.LocalTime.of(14, 30, 45);
        java.time.LocalDateTime dt = java.time.LocalDateTime.of(date, time);
        
        System.out.printf("Date: %tF%n", date);           // 2024-01-15
        System.out.printf("Time: %tT%n", time);           // 14:30:45
        System.out.printf("DateTime: %tF %tT%n", dt, dt); // 2024-01-15 14:30:45
        System.out.printf("Month: %tB%n", date);          // January
        System.out.printf("Day: %tA%n", date);            // Monday

        // Argument index (reorder/reuse)
        System.out.println("\n--- Argument Index ---");
        System.out.printf("%1$s %2$s %1$s%n", "Hello", "World"); // Hello World Hello
        System.out.printf("%<s %<s%n", "Repeat");               // Repeat Repeat

        // Text Blocks (Java 15+)
        System.out.println("\n--- Text Blocks ---");
        String html = """
            <html>
                <body>
                    <h1>%s</h1>
                    <p>%s</p>
                </body>
            </html>
            """.formatted("Welcome", "Java Engineering Academy");
        System.out.println(html);

        String json = """
            {
                "name": "%s",
                "age": %d,
                "active": %b
            }
            """.formatted("Bob", 25, true);
        System.out.println(json);

        // Formatted with text block
        System.out.println("\n--- Formatted Text Block ---");
        String template = """
            User: %s
            Score: %d/100
            Grade: %c
            """;
        System.out.println(template.formatted("Charlie", 87, 'B'));

        // Expected output demonstrates all formatting options
    }
}