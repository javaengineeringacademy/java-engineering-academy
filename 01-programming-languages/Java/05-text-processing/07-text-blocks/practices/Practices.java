package academy.javaengineering.oop.practices;

/**
 * Practice: Text Blocks in Java (Java 15+)
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Creating multi-line text blocks with triple quotes
 * - Indentation stripping rules
 * - Line continuation with backslash
 * - Using text blocks for JSON, HTML, SQL
 * - Comparing text blocks to string concatenation
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 07-text-blocks ===\n");

        // Test Exercise 1: buildJsonTemplate
        String json = buildJsonTemplate("Alice", 30);
        System.out.println("Exercise 1 - buildJsonTemplate: "
            + (json.contains("Alice") && json.contains("30") && json.contains("\"name\"") ? "PASS" : "FAIL"));

        // Test Exercise 2: buildHtmlParagraph
        String html = buildHtmlParagraph("Hello, World!");
        System.out.println("Exercise 2 - buildHtmlParagraph: "
            + (html.contains("<p>") && html.contains("Hello, World!") && html.contains("</p>") ? "PASS" : "FAIL"));

        // Test Exercise 3: buildSqlQuery
        String sql = buildSqlQuery("users", "name", "active");
        System.out.println("Exercise 3 - buildSqlQuery: "
            + (sql.contains("users") && sql.contains("name") && sql.contains("active") ? "PASS" : "FAIL"));

        // Test Exercise 4: formatTable
        String table = formatTable(new String[]{"Name", "Age"}, new String[][]{{"Alice", "30"}, {"Bob", "25"}});
        System.out.println("Exercise 4 - formatTable: "
            + (table.contains("Alice") && table.contains("Bob") && table.contains("Name") ? "PASS" : "FAIL"));

        // Test Exercise 5: noTrailingWhitespace check
        String clean = stripTrailingNewline("Hello\n");
        System.out.println("Exercise 5 - stripTrailingNewline: "
            + ("Hello".equals(clean) ? "PASS" : "FAIL"));
    }

    // TODO 1: Build a JSON string template using a text block
    // The JSON should look like:
    // {
    //     "name": "<name>",
    //     "age": <age>
    // }
    // Use String.format() or concatenation to insert name and age
    static String buildJsonTemplate(String name, int age) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 2: Wrap text in HTML <p> tags using a text block
    // Return something like:
    // <p>
    //     Hello, World!
    // </p>
    static String buildHtmlParagraph(String text) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 3: Build a SQL SELECT query using a text block
    // SELECT <column> FROM <table> WHERE <column> = true
    static String buildSqlQuery(String table, String column, String filterColumn) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 4: Format a simple table with headers and rows
    // Use StringBuilder to build a pipe-separated table
    // Example output:
    // Name | Age
    // Alice | 30
    // Bob | 25
    static String formatTable(String[] headers, String[][] rows) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 5: Strip a trailing newline (\n) from a string
    // "Hello\n" -> "Hello"
    // "Hello" -> "Hello"
    static String stripTrailingNewline(String input) {
        // YOUR CODE HERE
        return null;
    }
}
