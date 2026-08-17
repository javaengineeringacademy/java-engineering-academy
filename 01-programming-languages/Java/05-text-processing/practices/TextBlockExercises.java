package academy.javaengineering.exercises;

/**
 * Exercises: Text Blocks (Java 15+)
 *
 * Complete the TODO sections below.
 * Requires Java 15 or later.
 */
public class TextBlockExercises {

    // TODO 1: Create a formatted SQL SELECT statement using text blocks
    public String buildSelectQuery(String table, String... columns) {
        // TODO: implement using text blocks and String.join
        return "";
    }

    // TODO 2: Create a JSON object string using text blocks
    public String buildJson(String name, int age, String city) {
        // TODO: implement using text blocks
        return "";
    }

    // TODO 3: Create an HTML template using text blocks
    public String buildHtmlPage(String title, String bodyContent) {
        // TODO: implement using text blocks
        return "";
    }

    // TODO 4: Strip common leading whitespace from a text block
    public String normalizeIndentation(String textBlock) {
        // TODO: implement using stripIndent()
        return "";
    }

    // TODO 5: Apply formatted() to a text block with placeholders
    public String formatTemplate(String template, String name, int value) {
        // TODO: implement using formatted()
        return "";
    }

    // TODO 6: Create a multi-line string with escaped characters
    public String buildEscapedString() {
        // TODO: implement using text blocks with escape sequences
        return "";
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        TextBlockExercises exercises = new TextBlockExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== TextBlockExercises Tests ===\n");

        // Test 1
        total++;
        String sql = exercises.buildSelectQuery("users", "id", "name", "email");
        if (sql.contains("SELECT") && sql.contains("FROM users") && sql.contains("id")) {
            System.out.println("Test 1 PASSED: buildSelectQuery");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: buildSelectQuery");
        }

        // Test 2
        total++;
        String json = exercises.buildJson("Alice", 30, "NYC");
        if (json.contains("name") && json.contains("Alice") && json.contains("30")) {
            System.out.println("Test 2 PASSED: buildJson");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: buildJson");
        }

        // Test 3
        total++;
        String html = exercises.buildHtmlPage("Test", "Hello");
        if (html.contains("Test") && html.contains("Hello")) {
            System.out.println("Test 3 PASSED: buildHtmlPage");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: buildHtmlPage");
        }

        // Test 4
        total++;
        String input = "    line1\n    line2\n    line3";
        String normalized = exercises.normalizeIndentation(input);
        if (normalized.contains("line1") && !normalized.contains("    line1")) {
            System.out.println("Test 4 PASSED: normalizeIndentation");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: normalizeIndentation");
        }

        // Test 5
        total++;
        String formatted = exercises.formatTemplate("Hello %s, value is %d", "Alice", 42);
        if (formatted.contains("Alice") && formatted.contains("42")) {
            System.out.println("Test 5 PASSED: formatTemplate");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: formatTemplate");
        }

        // Test 6
        total++;
        String escaped = exercises.buildEscapedString();
        if (escaped != null && !escaped.isEmpty()) {
            System.out.println("Test 6 PASSED: buildEscapedString");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: buildEscapedString");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
