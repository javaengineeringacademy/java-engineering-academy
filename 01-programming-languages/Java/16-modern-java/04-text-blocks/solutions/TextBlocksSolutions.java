package academy.javaengineering.modern.textblocks;

/**
 * Solutions for Text Blocks practice exercises.
 */
public class TextBlocksSolutions {

    // Exercise 1: JSON Template
    public static String createUserJson(String name, String email, int age, boolean active) {
        return """
                {
                    "name": "%s",
                    "email": "%s",
                    "age": %d,
                    "active": %s
                }
                """.formatted(name, email, age, active);
    }

    // Exercise 2: HTML Page
    public static String createHtmlPage(String title, String header, String content, String[] items) {
        StringBuilder listHtml = new StringBuilder();
        for (String item : items) {
            listHtml.append("                    <li>%s</li>\n".formatted(item));
        }

        return """
                <!DOCTYPE html>
                <html>
                    <head>
                        <title>%s</title>
                    </head>
                    <body>
                        <h1>%s</h1>
                        <p>%s</p>
                        <ul>
                %s
                        </ul>
                    </body>
                </html>
                """.formatted(title, header, content, listHtml);
    }

    // Exercise 3: SQL Query Builder
    public static String buildSelectQuery(String table, String[] columns, String where, String orderBy) {
        String columnList = String.join(", ", columns);
        String query = """
                SELECT %s
                FROM %s
                """.formatted(columnList, table);

        if (where != null && !where.isEmpty()) {
            query += "WHERE %s\n".formatted(where);
        }

        if (orderBy != null && !orderBy.isEmpty()) {
            query += "ORDER BY %s".formatted(orderBy);
        }

        return query;
    }

    // Exercise 4: Code Generator
    public static String generateJavaClass(String className, String fieldName, String fieldType) {
        String capitalizedField = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        return """
                public class %s {
                    private %s %s;

                    public %s(%s %s) {
                        this.%s = %s;
                    }

                    public %s get%s() {
                        return %s;
                    }

                    public void set%s(%s %s) {
                        this.%s = %s;
                    }
                }
                """.formatted(
                    className,
                    fieldType, fieldName,
                    className, fieldType, fieldName,
                    fieldName, fieldName,
                    fieldType, capitalizedField, fieldName,
                    capitalizedField, fieldType, fieldName,
                    fieldName, fieldName
                );
    }

    public static void main(String[] args) {
        // Test Exercise 1
        System.out.println("--- Exercise 1: JSON Template ---");
        System.out.println(createUserJson("John Doe", "john@example.com", 30, true));

        // Test Exercise 2
        System.out.println("\n--- Exercise 2: HTML Page ---");
        String[] hobbies = {"Reading", "Hiking", "Coding"};
        System.out.println(createHtmlPage(
            "John's Profile",
            "About John",
            "John is a software developer.",
            hobbies
        ));

        // Test Exercise 3
        System.out.println("\n--- Exercise 3: SQL Query Builder ---");
        System.out.println(buildSelectQuery(
            "users",
            new String[]{"id", "name", "email"},
            "active = true",
            "name ASC"
        ));

        // Test Exercise 4
        System.out.println("\n--- Exercise 4: Code Generator ---");
        System.out.println(generateJavaClass("Person", "name", "String"));
    }
}
