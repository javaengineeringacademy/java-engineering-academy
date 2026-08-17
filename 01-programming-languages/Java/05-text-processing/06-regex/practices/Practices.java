package academy.javaengineering.oop.practices;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Practice: Regular Expressions in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Compiling and reusing Pattern objects
 * - Using Matcher.find() and Matcher.matches()
 * - Capturing groups for data extraction
 * - Greedy vs lazy quantifiers
 * - Common validation patterns (email, phone)
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 06-regex ===\n");

        // Test Exercise 1: validateEmail
        System.out.println("Exercise 1 - validateEmail: "
            + (validateEmail("user@gmail.com") && !validateEmail("invalid") && !validateEmail("@no.com")
            ? "PASS" : "FAIL"));

        // Test Exercise 2: extractNumbers
        System.out.println("Exercise 2 - extractNumbers: "
            + ("123".equals(extractFirstNumber("abc123def")) ? "PASS" : "FAIL"));

        // Test Exercise 3: countWords
        System.out.println("Exercise 3 - countWords: "
            + (countWords("Hello World Java") == 3 ? "PASS" : "FAIL"));

        // Test Exercise 4: extractGroup
        System.out.println("Exercise 4 - extractGroup: "
            + ("2024".equals(extractDatePart("(\\d{4})-(\\d{2})-(\\d{2})", "2024-01-15", 1)) ? "PASS" : "FAIL"));

        // Test Exercise 5: replacePattern
        System.out.println("Exercise 5 - replacePattern: "
            + ("I have 3 cats".equals(replacePattern("I have 5 cats and 2 dogs", "\\d+", "3")) ? "PASS" : "FAIL"));
    }

    // TODO 1: Validate an email address using regex
    // Pattern: ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$
    // Use Pattern.compile() and cache the pattern
    static final Pattern EMAIL_PATTERN = null; // Initialize the compiled pattern here

    static boolean validateEmail(String email) {
        // YOUR CODE HERE
        return false;
    }

    // TODO 2: Extract the first number from a string using regex
    // Example: "abc123def" -> "123"
    // Use Matcher.find() and Matcher.group()
    static String extractFirstNumber(String input) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 3: Count the number of words in a string
    // Words are separated by whitespace. Use \\S+ pattern.
    static int countWords(String input) {
        // YOUR CODE HERE
        return 0;
    }

    // TODO 4: Extract a specific capturing group from a regex match
    // Given a regex pattern, input, and group number, return the matched group
    // Example: extractGroup("(\\d{4})-(\\d{2})-(\\d{2})", "2024-01-15", 1) -> "2024"
    static String extractDatePart(String pattern, String input, int group) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 5: Replace all matches of a regex pattern with a replacement string
    // Example: replacePattern("I have 5 cats and 2 dogs", "\\d+", "3") -> "I have 3 cats and 3 dogs"
    static String replacePattern(String input, String regex, String replacement) {
        // YOUR CODE HERE
        return null;
    }
}
