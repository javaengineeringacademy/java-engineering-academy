package academy.javaengineering.exceptions.questions;

/**
 * Question 3: Multi-catch with throw
 *
 * Task: Complete the method using multi-catch (|) to catch both exception types.
 * Throw a custom RuntimeException wrapping the original cause.
 */
public class Question03_MultiCatchWithThrow {

    public static int processInput(String value) {
        // TODO: Try to parse value as Integer
        // If successful, check that it is between 1 and 100
        // Use multi-catch to catch both NumberFormatException and IllegalArgumentException
        // Wrap in RuntimeException and throw
        return 0;
    }

    public static void main(String[] args) {
        try {
            System.out.println("Test 1: " + processInput("50"));   // Expected: 50
        } catch (RuntimeException e) {
            System.out.println("Test 1 failed: " + e.getMessage());
        }

        try {
            System.out.println("Test 2: " + processInput("abc"));  // Expected: exception
        } catch (RuntimeException e) {
            System.out.println("Test 2 caught: " + e.getClass().getSimpleName());
        }

        try {
            System.out.println("Test 3: " + processInput("200")); // Expected: exception
        } catch (RuntimeException e) {
            System.out.println("Test 3 caught: " + e.getClass().getSimpleName());
        }
    }
}
