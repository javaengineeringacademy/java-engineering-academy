package academy.javaengineering.exceptions.questions;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Question 9: RuntimeException recovery
 *
 * Task: Complete the method to attempt an operation and recover from
 * RuntimeException by returning a fallback value. Log the failure.
 */
public class Question09_RuntimeExceptionRecovery {

    public static int parseWithFallback(String input, int fallback) {
        // TODO: Try to parse input as integer
        // If InputMismatchException or NumberFormatException occurs:
        //   - Print "Failed to parse: " + input
        //   - Return the fallback value
        return fallback;
    }

    public static int divideWithFallback(int a, int b, int fallback) {
        // TODO: Try to divide a by b
        // If ArithmeticException occurs:
        //   - Print "Division failed: " + a + " / " + b
        //   - Return the fallback value
        return fallback;
    }

    public static void main(String[] args) {
        System.out.println("Test 1: " + parseWithFallback("42", 0));       // Expected: 42
        System.out.println("Test 2: " + parseWithFallback("abc", 99));     // Expected: 99
        System.out.println("Test 3: " + divideWithFallback(10, 3, 0));     // Expected: 3
        System.out.println("Test 4: " + divideWithFallback(10, 0, -1));    // Expected: -1
    }
}
