package academy.javaengineering.exceptions.questions;

/**
 * Question 2: Multiple catch blocks
 *
 * Task: Complete the method to handle two different exception types differently.
 * - NumberFormatException: return -1
 * - ArrayIndexOutOfBoundsException: return -2
 * - Otherwise: return the parsed value
 */
public class Question02_MultipleCatch {

    public static int parseOrIndex(String input, int index) {
        // TODO: Parse input as integer, access array[10]
        // Catch NumberFormatException and ArrayIndexOutOfBoundsException separately
        return 0;
    }

    public static void main(String[] args) {
        System.out.println("Test 1: " + parseOrIndex("42", 1));   // Expected: 42
        System.out.println("Test 2: " + parseOrIndex("abc", 1));  // Expected: -1
        System.out.println("Test 3: " + parseOrIndex("42", 15));  // Expected: -2
    }
}
