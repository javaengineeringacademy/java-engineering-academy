package academy.javaengineering.text.examples;

/**
 * Text Blocks Examples - Practical demonstrations of Text Blocks usage.
 * 
 * WHY TEXT BLOCKS WERE INTRODUCED:
 * - Multi-line strings were painful to write
 * - String concatenation for SQL/JSON was error-prone
 * - Escaping quotes was tedious
 * 
 * JAVA 15+: Text blocks are a preview feature
 * JAVA 15: Text blocks are final
 * 
 * TRADE-OFFS:
 * - Pros: Readable, no escaping, preserves formatting
 * - Cons: Fixed indentation, not for single-line strings
 */
public class TextBlocksExamples {

    public static void main(String[] args) {
        System.out.println("=== Text Blocks Examples ===\n");

        // Example 1: Basic Text Block
        example1_BasicTextBlock();

        // Example 2: JSON/HTML
        example2_JSON_HTML();

        // Example 3: SQL Queries
        example3_SQL();

        // Example 4: Indentation Handling
        example4_Indentation();
    }

    /**
     * WHY: Text blocks eliminate string concatenation.
     * 
     * ENGINEERING DECISION: Use text blocks for multi-line content.
     */
    private static void example1_BasicTextBlock() {
        System.out.println("--- Example 1: Basic Text Block ---");

        String textBlock = """
                This is a text block.
                It can span multiple lines.
                No need for \\n characters.
                """;

        System.out.println(textBlock);
    }

    /**
     * WHY: Text blocks are perfect for JSON and HTML.
     * 
     * ENGINEERING DECISION: Use text blocks for configuration files.
     */
    private static void example2_JSON_HTML() {
        System.out.println("\n--- Example 2: JSON/HTML ---");

        String json = """
                {
                    "name": "John",
                    "age": 30,
                    "city": "New York"
                }
                """;

        String html = """
                <html>
                    <body>
                        <h1>Hello World</h1>
                    </body>
                </html>
                """;

        System.out.println("JSON:");
        System.out.println(json);
        System.out.println("HTML:");
        System.out.println(html);
    }

    /**
     * WHY: Text blocks simplify SQL queries.
     * 
     * ENGINEERING DECISION: Use text blocks for complex SQL.
     */
    private static void example3_SQL() {
        System.out.println("\n--- Example 3: SQL Queries ---");

        String sql = """
                SELECT u.id, u.name, u.email
                FROM users u
                WHERE u.age > 18
                AND u.status = 'ACTIVE'
                ORDER BY u.name
                """;

        System.out.println("SQL Query:");
        System.out.println(sql);
    }

    /**
     * WHY: Text blocks handle indentation automatically.
     * 
     * INTERNAL: JVM strips common leading whitespace.
     * Use \s to preserve spaces, \ to trim whitespace.
     */
    private static void example4_Indentation() {
        System.out.println("\n--- Example 4: Indentation Handling ---");

        String indented = """
                    Indented text
                    More text
                    """;

        String withSpaces = """
                Line 1\s
                Line 2
                """;

        System.out.println("Indented:");
        System.out.println(indented);
        System.out.println("With explicit spaces:");
        System.out.println(withSpaces);
    }
}
