package collections.set.exercises;

import java.util.*;

/**
 * SET EXERCISES — HashSet, LinkedHashSet, TreeSet
 *
 * Complete each TODO. Run tests to verify your solutions.
 */
public class exercises {

    // =========================================================================
    // EXERCISE 1: HashSet — Set Operations
    // =========================================================================
    /**
     * Given two lists of integers, return a list containing elements that
     * are in the first list but NOT in the second list (set difference A - B).
     * Preserve the order of elements from the first list.
     *
     * Example: A=[1,2,3,4,5], B=[3,4,5,6,7] → [1,2]
     *
     * TODO: Implement this method
     */
    public static List<Integer> setDifference(List<Integer> a, List<Integer> b) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 2: LinkedHashSet — First Non-Repeating Character
    // =========================================================================
    /**
     * Given a string, return the first character that does not repeat.
     * Use a LinkedHashSet to maintain insertion order for tracking.
     * Return '\0' (null character) if all characters repeat.
     *
     * Example: "programming" → 'p'
     * Example: "aabbcc" → '\0'
     *
     * TODO: Implement this method
     */
    public static char firstNonRepeatingChar(String s) {
        // TODO: Your code here
        return '\0';
    }

    // =========================================================================
    // EXERCISE 3: TreeSet — Running Median
    // =========================================================================
    /**
     * Given a list of integers, compute the median after each insertion
     * using two TreeSet instances (or one TreeSet with a sorted structure).
     * Return a list of medians (as doubles).
     *
     * For odd count, median is the middle element.
     * For even count, median is the average of two middle elements.
     *
     * Example: [5, 15, 1, 3] → [5.0, 10.0, 5.0, 4.0]
     *
     * TODO: Implement this method
     */
    public static List<Double> runningMedian(List<Integer> nums) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 4: HashSet — Longest Consecutive Sequence
    // =========================================================================
    /**
     * Given an unsorted array of integers, find the length of the longest
     * consecutive elements sequence using a HashSet.
     * Must run in O(n) time.
     *
     * Example: [100, 4, 200, 1, 3, 2] → 4 (sequence: 1,2,3,4)
     *
     * TODO: Implement this method
     */
    public static int longestConsecutiveSequence(int[] nums) {
        // TODO: Your code here
        return 0;
    }

    // =========================================================================
    // EXERCISE 5: TreeSet — Subrange Query
    // =========================================================================
    /**
     * Given a TreeSet of integers and two bounds (lower, upper),
     * return a list of all elements in the TreeSet that fall within
     * the range [lower, upper] inclusive, in sorted order.
     * Use TreeSet's subSet method.
     *
     * Example: TreeSet={1,3,5,7,9,11}, lower=3, upper=9 → [3,5,7,9]
     *
     * TODO: Implement this method
     */
    public static List<Integer> subrangeQuery(TreeSet<Integer> set, int lower, int upper) {
        // TODO: Your code here
        return null;
    }
}
