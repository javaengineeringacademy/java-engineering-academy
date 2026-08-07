/**
 * TextBlocksAdvancedDemo.java
 *
 * Demonstrates advanced text block features in Java 15+.
 * Includes advanced formatting, JSON/HTML templates, and text processing.
 *
 * Compile with: javac TextBlocksAdvancedDemo.java
 * Run with: java TextBlocksAdvancedDemo
 *
 * Expected Output:
 * === Advanced Text Blocks (Java 15+) ===
 *
 * --- 1. Text Blocks with Formatting ---
 * Name: Alice
 * Age: 30
 * Salary: $75000.50
 *
 * --- 2. Text Blocks with JSON ---
 * {
 *     "name": "John",
 *     "age": 30,
 *     "city": "New York",
 *     "active": true
 * }
 *
 * --- 3. Text Blocks with HTML ---
 * <html>
 *     <head>
 *         <title>Hello</title>
 *     </head>
 *     <body>
 *         <h1>Hello World</h1>
 *         <p>Welcome to Java</p>
 *     </body>
 * </html>
 *
 * --- 4. Text Blocks with SQL ---
 * SELECT
 *     id,
 *     name,
 *     email
 * FROM
 *     users
 * WHERE
 *     active = true
 * ORDER BY
 *     name
 *
 * --- 5. Text Blocks with Indentation ---
 * {
 *     "users": [
 *         {
 *             "name": "Alice",
 *             "age": 30
 *         },
 *         {
 *             "name": "Bob",
 *             "age": 25
 *         }
 *     ]
 * }
 *
 * --- 6. Text Blocks with Escape Sequences ---
 * Quote: He said "Hello" to me
 * Backslash: Path: C:\Users\test
 * Unicode: Greek letter: α (alpha)
 *
 * --- 7. Text Blocks with Line Continuation ---
 * This is a very long line that continues on the same line.
 *
 * --- 8. Text Blocks as Method Parameters ---
 * === Method Call with Text Block ===
 * Dear Alice,
 *
 * Your order #12345 has been shipped.
 * Total: $99.99
 *
 * Thank you for your business!
 * Best regards,
 * The Team
 */
public class TextBlocksAdvancedDemo {

    // =====================================================
    // 1. Text Blocks with Formatting
    // =====================================================
    // Text blocks can be used with String.format for dynamic content

    static void textBlocksWithFormatting() {
        System.out.println("--- 1. Text Blocks with Formatting ---");

        String name = "Alice";
        int age = 30;
        double salary = 75000.50;

        // Text block with formatting
        String report = String.format("""
                Name: %s
                Age: %d
                Salary: $%.2f
                """, name, age, salary);

        System.out.println(report);
    }

    // =====================================================
    // 2. Text Blocks with JSON
    // =====================================================
    // Text blocks make JSON templates much more readable

    static void textBlocksWithJSON() {
        System.out.println("--- 2. Text Blocks with JSON ---");

        String name = "John";
        int age = 30;
        String city = "New York";
        boolean active = true;

        // JSON template using text block
        String json = """
                {
                    "name": "%s",
                    "age": %d,
                    "city": "%s",
                    "active": %b
                }
                """.formatted(name, age, city, active);

        System.out.println(json);
    }

    // =====================================================
    // 3. Text Blocks with HTML
    // =====================================================
    // Text blocks are perfect for HTML templates

    static void textBlocksWithHTML() {
        System.out.println("--- 3. Text Blocks with HTML ---");

        String title = "Hello";
        String heading = "Hello World";
        String paragraph = "Welcome to Java";

        // HTML template using text block
        String html = """
                <html>
                    <head>
                        <title>%s</title>
                    </head>
                    <body>
                        <h1>%s</h1>
                        <p>%s</p>
                    </body>
                </html>
                """.formatted(title, heading, paragraph);

        System.out.println(html);
    }

    // =====================================================
    // 4. Text Blocks with SQL
    // =====================================================
    // Text blocks make SQL queries much more readable

    static void textBlocksWithSQL() {
        System.out.println("--- 4. Text Blocks with SQL ---");

        // SQL query using text block
        String sql = """
                SELECT
                    id,
                    name,
                    email
                FROM
                    users
                WHERE
                    active = true
                ORDER BY
                    name
                """;

        System.out.println(sql);
    }

    // =====================================================
    // 5. Text Blocks with Indentation
    // =====================================================
    // Indentation is stripped based on the closing """

    static void textBlocksWithIndentation() {
        System.out.println("--- 5. Text Blocks with Indentation ---");

        // JSON with nested structures
        String json = """
                {
                    "users": [
                        {
                            "name": "Alice",
                            "age": 30
                        },
                        {
                            "name": "Bob",
                            "age": 25
                        }
                    ]
                }
                """;

        System.out.println(json);
    }

    // =====================================================
    // 6. Text Blocks with Escape Sequences
    // =====================================================
    // Escape sequences work in text blocks

    static void textBlocksWithEscapeSequences() {
        System.out.println("--- 6. Text Blocks with Escape Sequences ---");

        // Quote in text block
        String withQuotes = """
                Quote: He said "Hello" to me
                """;
        System.out.println(withQuotes);

        // Backslash in text block
        String withBackslash = """
                Backslash: Path: C:\\Users\\test
                """;
        System.out.println(withBackslash);

        // Unicode escape
        String withUnicode = """
                Unicode: Greek letter: \\u03B1 (alpha)
                """;
        System.out.println(withUnicode);
    }

    // =====================================================
    // 7. Text Blocks with Line Continuation
    // =====================================================
    // Line continuation with backslash

    static void textBlocksWithLineContinuation() {
        System.out.println("--- 7. Text Blocks with Line Continuation ---");

        // Line continuation with backslash
        String textBlock = """
                This is a very long line that continues \
                on the same line.
                """;

        System.out.println(textBlock);
    }

    // =====================================================
    // 8. Text Blocks as Method Parameters
    // =====================================================
    // Text blocks can be passed directly to methods

    static void printLetter(String letter) {
        System.out.println("=== Method Call with Text Block ===");
        System.out.println(letter);
    }

    static void textBlocksAsMethodParameters() {
        System.out.println("--- 8. Text Blocks as Method Parameters ---");

        String customerName = "Alice";
        String orderNumber = "12345";
        double total = 99.99;

        // Text block as method parameter
        String letter = """
                Dear %s,

                Your order #%s has been shipped.
                Total: $%.2f

                Thank you for your business!
                Best regards,
                The Team
                """.formatted(customerName, orderNumber, total);

        printLetter(letter);
    }

    // =====================================================
    // Main Method
    // =====================================================

    public static void main(String[] args) {
        System.out.println("=== Advanced Text Blocks (Java 15+) ===\n");

        textBlocksWithFormatting();
        textBlocksWithJSON();
        textBlocksWithHTML();
        textBlocksWithSQL();
        textBlocksWithIndentation();
        textBlocksWithEscapeSequences();
        textBlocksWithLineContinuation();
        textBlocksAsMethodParameters();

        System.out.println("\n=== Complete ===");
    }
}
