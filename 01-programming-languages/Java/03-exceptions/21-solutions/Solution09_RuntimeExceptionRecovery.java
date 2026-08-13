package academy.javaengineering.exceptions.solutions;

import java.util.InputMismatchException;

/**
 * Solution 9: RuntimeException recovery
 *
 * Attempt operation, recover from RuntimeException, return fallback.
 */
public class Solution09_RuntimeExceptionRecovery {

    public static int parseWithFallback(String input, int fallback) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse: " + input);
            return fallback;
        }
    }

    public static int divideWithFallback(int a, int b, int fallback) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            System.out.println("Division failed: " + a + " / " + b);
            return fallback;
        }
    }

    public static void main(String[] args) {
        System.out.println("Test 1: " + parseWithFallback("42", 0));       // 42
        System.out.println("Test 2: " + parseWithFallback("abc", 99));     // 99
        System.out.println("Test 3: " + divideWithFallback(10, 3, 0));     // 3
        System.out.println("Test 4: " + divideWithFallback(10, 0, -1));    // -1
    }
}
