package academy.javaengineering.strings;

public class StringFormatting {

    public static void main(String[] args) {
        demonstrateBasicFormatting();
        demonstrateNumericFormatting();
        demonstrateDateFormatting();
        demonstrateTextBlocks();
        demonstrateAdvancedFormatting();
    }

    private static void demonstrateBasicFormatting() {
        System.out.println("=== Basic Formatting ===");

        String name = "John";
        int age = 30;
        double salary = 75000.50;
        boolean active = true;

        String formatted = String.format("Name: %s, Age: %d, Salary: %.2f, Active: %b",
                name, age, salary, active);
        System.out.println(formatted);

        System.out.printf("Name: %s, Age: %d, Salary: %.2f, Active: %b%n",
                name, age, salary, active);

        System.out.printf("Character: %c%n", 'A');
        System.out.printf("Binary: %b%n", true);
        System.out.printf("Octal: %o%n", 255);
        System.out.printf("Hex: %x%n", 255);
    }

    private static void demonstrateNumericFormatting() {
        System.out.println("\n=== Numeric Formatting ===");

        int number = 1234567;
        System.out.printf("Default: %d%n", number);
        System.out.printf("With commas: %,d%n", number);
        System.out.printf("Zero-padded: %010d%n", number);
        System.out.printf("Left-justified: %-10d%n", number);
        System.out.printf("With sign: %+d%n", number);

        double pi = 3.141592653589793;
        System.out.printf("Default: %f%n", pi);
        System.out.printf("Two decimals: %.2f%n", pi);
        System.out.printf("Scientific: %e%n", pi);
        System.out.printf("General: %g%n", pi);

        System.out.printf("Width 10: %10.2f%n", pi);
        System.out.printf("Width 10 left: %-10.2f%n", pi);
    }

    private static void demonstrateDateFormatting() {
        System.out.println("\n=== Date Formatting ===");

        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        System.out.printf("Date/Time: %tF %<tT%n", now);
        System.out.printf("Date only: %tF%n", now);
        System.out.printf("Time only: %<tT%n", now);
        System.out.printf("Year: %<tY%n", now);
        System.out.printf("Month: %<tB%n", now);
        System.out.printf("Day: %<td%n", now);
        System.out.printf("Hour: %<tH%n", now);
        System.out.printf("Minute: %<tM%n", now);
        System.out.printf("Second: %<tS%n", now);

        java.util.Date date = new java.util.Date();
        System.out.printf("Legacy date: %tc%n", date);
    }

    private static void demonstrateTextBlocks() {
        System.out.println("\n=== Text Blocks ===");

        String json = """
                {
                    "name": "John",
                    "age": 30,
                    "email": "john@example.com"
                }
                """;
        System.out.println("JSON:\n" + json);

        String sql = """
                SELECT id, name, email
                FROM users
                WHERE active = true
                ORDER BY name
                """;
        System.out.println("SQL:\n" + sql);

        String html = """
                <html>
                    <body>
                        <h1>Hello World</h1>
                        <p>This is a paragraph.</p>
                    </body>
                </html>
                """;
        System.out.println("HTML:\n" + html);

        String multiLine = """
                Line 1
                Line 2
                Line 3
                """;
        System.out.println("Multi-line:\n" + multiLine);
    }

    private static void demonstrateAdvancedFormatting() {
        System.out.println("\n=== Advanced Formatting ===");

        System.out.printf("Reusable argument: %1$s %2$d %1$s%n", "test", 42);
        System.out.printf("Argument index: %2$s %1$d%n", 42, "Number:");
        System.out.printf("Literal percent: 100%%%n");

        System.out.printf("Padding with zeros: %05d%n", 42);
        System.out.printf("Padding with spaces: %5d%n", 42);
        System.out.printf("Left padding: %-5d%n", 42);

        System.out.printf("Hex: %x%n", 255);
        System.out.printf("Octal: %o%n", 255);
        System.out.printf("Binary: %s%n", Integer.toBinaryString(255));
    }
}
