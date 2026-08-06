package strings;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * StringFormatting - Demonstrates String.format, text blocks, and formatting
 *
 * Covers:
 * - String.format() with various specifiers
 * - Text blocks (Java 13+)
 * - Number and date formatting
 * - MessageFormat for localization
 */
public class StringFormatting {

    public static void main(String[] args) {
        System.out.println("=== String.format() Basics ===");
        formatBasics();

        System.out.println("\n=== Number Formatting ===");
        numberFormatting();

        System.out.println("\n=== Date and Time Formatting ===");
        dateTimeFormatting();

        System.out.println("\n=== Text Blocks (Java 13+) ===");
        textBlocks();

        System.out.println("\n=== MessageFormat ===");
        messageFormatDemo();
    }

    static void formatBasics() {
        // Basic format specifiers
        String name = "John";
        int age = 30;
        double salary = 75000.50;

        // %s - string
        String formatted = String.format("Name: %s", name);
        System.out.println(formatted);

        // %d - decimal integer
        formatted = String.format("Age: %d", age);
        System.out.println(formatted);

        // %f - floating point
        formatted = String.format("Salary: $%.2f", salary);
        System.out.println(formatted);

        // %c - character
        formatted = String.format("First letter: %c", 'J');
        System.out.println(formatted);

        // %b - boolean
        formatted = String.format("Is adult: %b", age >= 18);
        System.out.println(formatted);

        // %n - platform-specific newline
        System.out.println("Line 1%nLine 2");

        // Multiple arguments
        formatted = String.format("Name: %s, Age: %d, Salary: $%.2f", name, age, salary);
        System.out.println("Combined: " + formatted);
    }

    static void numberFormatting() {
        double number = 1234567.891;

        // General formatting
        System.out.println("Default: " + String.format("%f", number));
        System.out.println("2 decimals: " + String.format("%.2f", number));
        System.out.println("Width 20: " + String.format("%20f", number));
        System.out.println("Left-aligned: " + String.format("%-20f", number));
        System.out.println("Zero-padded: " + String.format("%020.2f", number));
        System.out.println("With commas: " + String.format("%,.2f", number));

        // Integer formatting
        int intNum = 42;
        System.out.println("Hex: " + String.format("%x", intNum));
        System.out.println("Octal: " + String.format("%o", intNum));
        System.out.println("Binary: " + String.format("%8s",
            Integer.toBinaryString(intNum)).replace(' ', '0'));

        // DecimalFormat for complex patterns
        DecimalFormat df = new DecimalFormat("#,##0.00");
        System.out.println("DecimalFormat: " + df.format(number));

        df = new DecimalFormat("000000.00");
        System.out.println("Padded: " + df.format(42.5));
    }

    static void dateTimeFormatting() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        // DateTimeFormatter
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm:ss a");
        System.out.println("Full: " + now.format(fullFormatter));

        DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        System.out.println("Short: " + today.format(shortFormatter));

        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        System.out.println("ISO: " + now.format(isoFormatter));

        // Using String.format with dates (limited)
        System.out.println("Year: " + String.format("%tY", now));
        System.out.println("Month: " + String.format("%tm", now));
        System.out.println("Day: " + String.format("%td", now));
    }

    static void textBlocks() {
        // Text blocks (Java 13+) - triple quotes
        String json = """
                {
                    "name": "John",
                    "age": 30,
                    "city": "New York"
                }
                """;
        System.out.println("JSON text block:\n" + json);

        // SQL query
        String sql = """
                SELECT id, name, email
                FROM users
                WHERE age > 18
                ORDER BY name
                """;
        System.out.println("SQL text block:\n" + sql);

        // HTML
        String html = """
                <html>
                    <body>
                        <h1>Hello</h1>
                        <p>Welcome to Java</p>
                    </body>
                </html>
                """;
        System.out.println("HTML text block:\n" + html);

        // String formatting with text blocks
        String name = "John";
        int age = 30;
        String templated = """
                Name: %s
                Age: %d
                """.formatted(name, age);
        System.out.println("Formatted text block:\n" + templated);
    }

    static void messageFormatDemo() {
        // MessageFormat for i18n
        String pattern = "Hello {0}, you have {1} new messages.";
        String message = MessageFormat.format(pattern, "John", 5);
        System.out.println("MessageFormat: " + message);

        // Number formatting in MessageFormat
        String pattern2 = "Total: {0, number, currency}";
        String message2 = MessageFormat.format(pattern2, 1234.56);
        System.out.println("Currency: " + message2);

        // ChoiceFormat
        String pattern3 = "You have {0,choice,0#no messages|1#one message|2#{0} messages}";
        String message3 = MessageFormat.format(pattern3, 5);
        System.out.println("Choice: " + message3);

        // Alternative syntax with MessageFormat
        Object[] args = {"John", 5, 1234.56};
        String fullMessage = MessageFormat.format(
            "Hello {0}, you have {1} messages. Total: {2, number, currency}",
            args
        );
        System.out.println("Full message: " + fullMessage);
    }
}