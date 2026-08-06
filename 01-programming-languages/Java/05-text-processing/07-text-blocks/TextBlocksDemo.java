/**
 * TextBlocksDemo.java
 *
 * Demonstrates text blocks in Java 15+.
 * Compile with: javac --enable-preview --release 15 TextBlocksDemo.java
 * Run with: java --enable-preview TextBlocksDemo
 */
public class TextBlocksDemo {

    public static void main(String[] args) {
        System.out.println("=== Text Blocks Demo (Java 15+) ===\n");

        // 1. Basic text block
        basicTextBlock();

        // 2. Line continuation
        lineContinuation();

        // 3. Indentation stripping
        indentationStripping();

        // 4. Escape sequences
        escapeSequences();

        // 5. String formatting
        stringFormatting();

        // 6. Comparison with regular strings
        comparisonWithStrings();
    }

    private static void basicTextBlock() {
        System.out.println("--- 1. Basic Text Block ---");

        // Regular string
        String regular = "Line 1\nLine 2\nLine 3";

        // Text block
        String textBlock = """
                Line 1
                Line 2
                Line 3
                """;

        System.out.println("Regular string:");
        System.out.println(regular);
        System.out.println("\nText block:");
        System.out.println(textBlock);

        System.out.println();
    }

    private static void lineContinuation() {
        System.out.println("--- 2. Line Continuation ---");

        // Line continuation with backslash
        String textBlock = """
                This is a very long \
                line that continues \
                on the same line\
                .
                """;

        System.out.println("Line continuation:");
        System.out.println(textBlock);

        // Without backslash (newlines preserved)
        String withNewlines = """
                Line 1
                Line 2
                Line 3
                """;

        System.out.println("\nWithout backslash (newlines preserved):");
        System.out.println(withNewlines);

        System.out.println();
    }

    private static void indentationStripping() {
        System.out.println("--- 3. Indentation Stripping ---");

        // Text block with indentation
        String textBlock = """
                    {
                        "name": "John",
                        "age": 30
                    }
                """;

        System.out.println("Text block with indentation:");
        System.out.println(textBlock);

        // The indentation is stripped based on the closing """
        // The content becomes:
        // {
        //     "name": "John",
        //     "age": 30
        // }

        System.out.println("After stripping:");
        System.out.println("{" +
            "\n    \"name\": \"John\"," +
            "\n    \"age\": 30" +
            "\n}");

        System.out.println();
    }

    private static void escapeSequences() {
        System.out.println("--- 4. Escape Sequences ---");

        // Quote in text block
        String withQuotes = """
                He said "Hello" to me.
                """;
        System.out.println("With quotes: " + withQuotes);

        // Escaped quote
        String escapedQuote = """
                He said \\"Hello\\" to me.
                """;
        System.out.println("Escaped quote: " + escapedQuote);

        // Backslash
        String withBackslash = """
                Path: C:\\Users\\test
                """;
        System.out.println("With backslash: " + withBackslash);

        // Unicode escape
        String withUnicode = """
                Greek letter: \\u03B1 (alpha)
                """;
        System.out.println("With unicode: " + withUnicode);

        // Null character
        String withNull = """
                Text before\\0 text after
                """;
        System.out.println("With null: " + withNull);

        // Form feed
        String withFormFeed = """
                Line 1\\fLine 2
                """;
        System.out.println("With form feed: " + withFormFeed);

        System.out.println();
    }

    private static void stringFormatting() {
        System.out.println("--- 5. String Formatting ---");

        String name = "Alice";
        int age = 30;
        double salary = 75000.50;

        // Text block with formatting
        String report = String.format("""
                Employee Report
                ===============
                Name:   %s
                Age:    %d
                Salary: $%.2f
                """, name, age, salary);

        System.out.println("Formatted text block:");
        System.out.println(report);

        // Text block with concatenation
        String report2 = """
                Employee Report
                ===============
                Name:   """ + name + """
                
                Age:    """ + age + """
                
                Salary: $""" + salary;

        System.out.println("Concatenated text block:");
        System.out.println(report2);

        System.out.println();
    }

    private static void comparisonWithStrings() {
        System.out.println("--- 6. Comparison with Regular Strings ---");

        // Same content, different approaches
        String regular = "Line 1\nLine 2\nLine 3\n";
        String textBlock = """
                Line 1
                Line 2
                Line 3
                """;

        System.out.println("Regular string length: " + regular.length());
        System.out.println("Text block length: " + textBlock.length());
        System.out.println("Equal content: " + regular.equals(textBlock));

        // Text blocks are easier to read for multi-line content
        String json = """
                {
                    "name": "John",
                    "age": 30,
                    "city": "New York"
                }
                """;

        System.out.println("\nJSON example:");
        System.out.println(json);

        // SQL query example
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

        System.out.println("SQL example:");
        System.out.println(sql);

        System.out.println();
    }
}
