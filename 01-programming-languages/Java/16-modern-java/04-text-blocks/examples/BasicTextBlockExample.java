package academy.javaengineering.modern.textblocks;

/**
 * Basic text block examples.
 */
public class BasicTextBlockExample {

    public static void main(String[] args) {
        // Basic text block
        String multiLine = """
                This is a
                multi-line
                string
                """;
        System.out.println("Basic text block:\n" + multiLine);

        // Text block with indentation
        String withIndent = """
                Line 1
                    Line 2
                        Line 3
                Line 4
                """;
        System.out.println("With indentation:\n" + withIndent);

        // JSON example
        String json = """
                {
                    "name": "John",
                    "age": 30,
                    "city": "New York"
                }
                """;
        System.out.println("JSON:\n" + json);

        // HTML example
        String html = """
                <html>
                    <body>
                        <h1>Hello, World!</h1>
                        <p>This is a text block example.</p>
                    </body>
                </html>
                """;
        System.out.println("HTML:\n" + html);

        // SQL example
        String sql = """
                SELECT id, name, email
                FROM users
                WHERE active = true
                ORDER BY name
                """;
        System.out.println("SQL:\n" + sql);

        // Escape sequences
        String withEscapes = """
                Line with trailing spaces\\s
                Line with tab\\t
                Line with newline\\n
                """;
        System.out.println("With escapes:\n" + withEscapes);

        // Line continuation
        String longString = """
                This is a very long string that \
                continues on the next line \
                and then ends here.
                """;
        System.out.println("Line continuation:\n" + longString);

        // Formatted text block
        String name = "Alice";
        int age = 25;
        String formatted = """
                Name: %s
                Age: %d
                Status: Active
                """.formatted(name, age);
        System.out.println("Formatted:\n" + formatted);
    }
}
