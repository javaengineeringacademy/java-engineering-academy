package academy.javaengineering.oop.practices;

/**
 * Practice: StringBuilder in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Using StringBuilder for efficient string concatenation
 * - append, insert, delete, reverse, replace operations
 * - Understanding mutability vs String immutability
 * - Building strings in loops efficiently
 * - Performance comparison concepts
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 02-stringbuilder ===\n");

        // Test Exercise 1: buildCommaSeparatedList
        System.out.println("Exercise 1 - buildCommaSeparatedList: "
            + ("apple,banana,cherry".equals(buildCommaSeparatedList(new String[]{"apple", "banana", "cherry"}))
            ? "PASS" : "FAIL"));

        // Test Exercise 2: insertAtPosition
        System.out.println("Exercise 2 - insertAtPosition: "
            + ("HeXXllo".equals(insertAtPosition("Hello", "XX", 2)) ? "PASS" : "FAIL"));

        // Test Exercise 3: removeVowels
        System.out.println("Exercise 3 - removeVowels: "
            + ("Hll Wrld".equals(removeVowels("Hello World")) ? "PASS" : "FAIL"));

        // Test Exercise 4: repeatString
        System.out.println("Exercise 4 - repeatString: "
            + ("abcabcabc".equals(repeatString("abc", 3)) ? "PASS" : "FAIL"));

        // Test Exercise 5: build Pascal's triangle row as a string
        System.out.println("Exercise 5 - buildPascalRow: "
            + ("1 2 1".equals(buildPascalRow(new int[]{1, 2, 1})) ? "PASS" : "FAIL"));
    }

    // TODO 1: Build a comma-separated string from an array of items
    // Do NOT use String.join - use StringBuilder
    // Example: ["apple", "banana", "cherry"] -> "apple,banana,cherry"
    static String buildCommaSeparatedList(String[] items) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 2: Insert a string into another string at the given position
    // Example: insertAtPosition("Hello", "XX", 2) -> "HeXXllo"
    static String insertAtPosition(String original, String toInsert, int position) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 3: Remove all vowels (a, e, i, o, u) from a string, case-insensitive
    // Example: "Hello World" -> "Hll Wrld"
    static String removeVowels(String input) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 4: Repeat a string n times using StringBuilder
    // Example: repeatString("abc", 3) -> "abcabcabc"
    static String repeatString(String input, int times) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 5: Build a space-separated string from an int array
    // Example: {1, 2, 1} -> "1 2 1"
    static String buildPascalRow(int[] values) {
        // YOUR CODE HERE
        return null;
    }
}
