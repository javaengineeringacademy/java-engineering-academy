package academy.javaengineering.exceptions.solutions;

/**
 * Solution 2: Multiple catch blocks
 *
 * Handle two different exception types differently.
 */
public class Solution02_MultipleCatch {

    public static int parseOrIndex(String input, int index) {
        int[] data = {10, 20, 30, 40, 50};
        try {
            int value = Integer.parseInt(input);
            return data[index];
        } catch (NumberFormatException e) {
            return -1;
        } catch (ArrayIndexOutOfBoundsException e) {
            return -2;
        }
    }

    public static void main(String[] args) {
        System.out.println("Test 1: " + parseOrIndex("42", 1));   // 20
        System.out.println("Test 2: " + parseOrIndex("abc", 1));  // -1
        System.out.println("Test 3: " + parseOrIndex("42", 15));  // -2
    }
}
