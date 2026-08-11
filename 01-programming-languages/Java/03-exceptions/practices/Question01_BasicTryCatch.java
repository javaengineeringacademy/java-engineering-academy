package academy.javaengineering.exceptions.questions;

/**
 * Question 1: Basic try-catch
 *
 * Task: Complete the method to catch ArithmeticException when dividing by zero.
 * Return the result of division, or -1 if division fails.
 */
public class Question01_BasicTryCatch {

    public static int safeDivide(int a, int b) {
        // TODO: Use try-catch to handle ArithmeticException
        // Return the result if successful, -1 if exception occurs
        return 0;
    }

    public static void main(String[] args) {
        System.out.println("Test 1: " + safeDivide(10, 3));   // Expected: 3
        System.out.println("Test 2: " + safeDivide(10, 0));   // Expected: -1
        System.out.println("Test 3: " + safeDivide(0, 5));    // Expected: 0
    }
}
