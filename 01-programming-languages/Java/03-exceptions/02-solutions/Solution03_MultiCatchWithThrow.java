package academy.javaengineering.exceptions.solutions;

/**
 * Solution 3: Multi-catch with throw
 *
 * Use multi-catch (|) to catch both exception types and wrap in RuntimeException.
 */
public class Solution03_MultiCatchWithThrow {

    public static int processInput(String value) {
        try {
            int num = Integer.parseInt(value);
            if (num < 1 || num > 100) {
                throw new IllegalArgumentException("Out of range: " + num);
            }
            return num;
        } catch (NumberFormatException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid input: " + value, e);
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Test 1: " + processInput("50"));   // 50
        } catch (RuntimeException e) {
            System.out.println("Test 1 failed: " + e.getMessage());
        }

        try {
            System.out.println("Test 2: " + processInput("abc"));  // exception
        } catch (RuntimeException e) {
            System.out.println("Test 2 caught: " + e.getClass().getSimpleName());
        }

        try {
            System.out.println("Test 3: " + processInput("200")); // exception
        } catch (RuntimeException e) {
            System.out.println("Test 3 caught: " + e.getClass().getSimpleName());
        }
    }
}
