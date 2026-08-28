package academy.javaengineering.modern.textblocks;

/**
 * Advanced text block usage.
 */
public class AdvancedTextBlockExample {

    public static void main(String[] args) {
        // Text block as method parameter
        System.out.println("=== Text Block as Parameter ===");
        printCodeBlock("""
                public class MyClass {
                    public void myMethod() {
                        System.out.println("Hello!");
                    }
                }
                """);

        // Text block with formatting
        System.out.println("\n=== Formatted Text Blocks ===");
        String report = generateReport("Sales Report", "Q4 2023", 150000);
        System.out.println(report);

        // Text block with concatenation
        System.out.println("\n=== Concatenated Text Blocks ===");
        String fullMessage = """
                Dear %s,

                We are pleased to inform you that your order #%s has been shipped.

                Thank you for your business!

                Best regards,
                The Team
                """.formatted("John", "12345");
        System.out.println(fullMessage);

        // Text block with conditional content
        System.out.println("\n=== Conditional Text Block ===");
        boolean isAdmin = true;
        String adminSection = """
                %s
                - User Management
                - System Settings
                - Reports
                """.formatted(isAdmin ? "Admin Menu:" : "User Menu:");
        System.out.println(adminSection);

        // Text block with loop
        System.out.println("\n=== Text Block with Loop ===");
        String[] items = {"Apple", "Banana", "Cherry"};
        StringBuilder listBuilder = new StringBuilder();
        listBuilder.append("""
                Shopping List:
                """);
        for (int i = 0; i < items.length; i++) {
            listBuilder.append("  %d. %s%n".formatted(i + 1, items[i]));
        }
        System.out.println(listBuilder);

        // Text block with special characters
        System.out.println("\n=== Special Characters ===");
        String special = """
                Tab: \\t
                Newline: \\n
                Backslash: \\\\
                Quote: \\"\\"
                Unicode: \\u0041
                """;
        System.out.println(special);
    }

    static void printCodeBlock(String code) {
        System.out.println("--- Code Block ---");
        System.out.println(code);
        System.out.println("--- End ---");
    }

    static String generateReport(String title, String period, double value) {
        return """
                ================================
                |        %s          |
                ================================
                | Period: %s
                | Value: $%,.2f
                ================================
                """.formatted(title, period, value);
    }
}
