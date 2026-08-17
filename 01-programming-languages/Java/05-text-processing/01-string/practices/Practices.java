package academy.javaengineering.oop.practices;

/**
 * Practice: String in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - String immutability and == vs equals()
 * - Common String methods (substring, indexOf, contains, etc.)
 * - String comparison and searching
 * - String splitting and joining
 * - String formatting
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 01-string ===\n");

        // Test Exercise 1: reverseString
        System.out.println("Exercise 1 - reverseString: "
            + ("olleH".equals(reverseString("Hello")) ? "PASS" : "FAIL"));

        // Test Exercise 2: countOccurrences
        System.out.println("Exercise 2 - countOccurrences: "
            + (countOccurrences("banana", 'a') == 3 ? "PASS" : "FAIL"));

        // Test Exercise 3: isPalindrome
        System.out.println("Exercise 3 - isPalindrome: "
            + (isPalindrome("racecar") && !isPalindrome("hello") ? "PASS" : "FAIL"));

        // Test Exercise 4: capitalizeWords
        System.out.println("Exercise 4 - capitalizeWords: "
            + ("Hello World Java".equals(capitalizeWords("hello world java")) ? "PASS" : "FAIL"));

        // Test Exercise 5: extractEmailDomain
        System.out.println("Exercise 5 - extractEmailDomain: "
            + ("gmail.com".equals(extractEmailDomain("user@gmail.com")) ? "PASS" : "FAIL"));
    }

    // TODO 1: Reverse a string using StringBuilder or char manipulation
    // Example: "Hello" -> "olleH"
    static String reverseString(String input) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 2: Count how many times a character appears in a string
    // Example: countOccurrences("banana", 'a') -> 3
    static int countOccurrences(String input, char target) {
        // YOUR CODE HERE
        return 0;
    }

    // TODO 3: Check if a string is a palindrome (reads same forwards and backwards)
    // Ignore case. "racecar" -> true, "hello" -> false
    static boolean isPalindrome(String input) {
        // YOUR CODE HERE
        return false;
    }

    // TODO 4: Capitalize the first letter of each word in a space-separated string
    // Example: "hello world" -> "Hello World"
    static String capitalizeWords(String input) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 5: Extract the domain from an email address
    // Example: "user@gmail.com" -> "gmail.com"
    // Hint: use indexOf('@') and substring()
    static String extractEmailDomain(String email) {
        // YOUR CODE HERE
        return null;
    }
}
