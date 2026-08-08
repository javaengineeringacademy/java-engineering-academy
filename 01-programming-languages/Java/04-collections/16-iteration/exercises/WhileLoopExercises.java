import java.util.*;

/**
 * While Loop Exercises - 5 challenges
 *
 * Complete each method body. Each exercise uses a while loop.
 * Run tests after completing to verify your solutions.
 */
public class WhileLoopExercises {

    /**
     * Exercise 1: Read Until Sentinel
     * Sum all integers from the array until (but not including) the first occurrence
     * of the sentinel value. If sentinel is not found, sum all elements.
     *
     * Example: sumUntilSentinel([1, 2, 3, -1, 4, 5], -1) → 6 (1+2+3)
     * Example: sumUntilSentinel([1, 2, 3, 4, 5], -1) → 15
     */
    public static int sumUntilSentinel(int[] array, int sentinel) {
        // TODO: Implement using while loop
        return 0;
    }

    /**
     * Exercise 2: Binary Search
     * Implement binary search on a sorted array. Return the index of target,
     * or -1 if not found.
     *
     * Example: binarySearch([1, 3, 5, 7, 9, 11], 7) → 3
     * Example: binarySearch([1, 3, 5, 7, 9, 11], 6) → -1
     */
    public static int binarySearch(int[] sortedArray, int target) {
        // TODO: Implement using while loop
        return -1;
    }

    /**
     * Exercise 3: Flatten Nested List
     * Flatten a list of lists into a single list.
     *
     * Example: flatten([[1, 2], [3, 4], [5]]) → [1, 2, 3, 4, 5]
     */
    public static <T> List<T> flatten(List<List<T>> nestedList) {
        // TODO: Implement using while loop
        return new ArrayList<>();
    }

    /**
     * Exercise 4: Run-Length Decoding
     * Decode a run-length encoded string. Format: "3a2b1c" → "aaabbc"
     *
     * Example: decode("3a2b1c") → "aaabbc"
     * Example: decode("2ab") → "aab" (number before a group of characters)
     */
    public static String decode(String encoded) {
        // TODO: Implement using while loop
        return "";
    }

    /**
     * Exercise 5: Pascal's Triangle Row
     * Return the nth row of Pascal's Triangle (0-indexed).
     *
     * Example: pascalRow(0) → [1]
     * Example: pascalRow(1) → [1, 1]
     * Example: pascalRow(4) → [1, 4, 6, 4, 1]
     */
    public static List<Long> pascalRow(int n) {
        // TODO: Implement using while loop
        return new ArrayList<>();
    }
}
