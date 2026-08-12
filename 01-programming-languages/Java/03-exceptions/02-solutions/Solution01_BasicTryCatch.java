package academy.javaengineering.exceptions.solutions;

/**
 * Solution 1: Basic try-catch
 *
 * Catch ArithmeticException when dividing by zero.
 */
public class Solution01_BasicTryCatch {

    public static int safeDivide(int a, int b) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println("Test 1: " + safeDivide(10, 3));   // 3
        System.out.println("Test 2: " + safeDivide(10, 0));   // -1
        System.out.println("Test 3: " + safeDivide(0, 5));    // 0
    }
}
