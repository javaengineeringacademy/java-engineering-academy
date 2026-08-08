package collections.stream.exercises;

import java.util.*;
import java.util.stream.*;

/**
 * STREAM OPERATIONS EXERCISES — filter, map, reduce
 *
 * Complete each TODO. Run tests to verify your solutions.
 */
public class exercises {

    // =========================================================================
    // EXERCISE 1: Stream filter — Find Palindromes
    // =========================================================================
    /**
     * Given a list of strings, use Stream API to filter and return only
     * palindromes (strings that read the same forwards and backwards),
     * converted to uppercase, sorted alphabetically.
     *
     * Example: ["madam", "hello", "racecar", "world", "level"]
     *        → ["LEVEL", "MADAM", "RACECAR"]
     *
     * TODO: Implement this method
     */
    public static List<String> findPalindromes(List<String> words) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 2: Stream map — Transform and Flatten
    // =========================================================================
    /**
     * Given a list of sentences, use Stream to:
     * 1. Split each sentence into words
     * 2. Flatten all words into a single stream
     * 3. Map each word to its length
     * 4. Return a list of unique lengths sorted in descending order
     *
     * Example: ["hello world", "java stream"] → [5, 6, 4]
     *
     * TODO: Implement this method
     */
    public static List<Integer> uniqueWordLengths(List<String> sentences) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 3: Stream reduce — Factorial Calculation
    // =========================================================================
    /**
     * Given an integer n, use Stream.rangeClosed and reduce to calculate
     * n! (n factorial). Return the result as a long.
     *
     * Example: 5 → 120 (5*4*3*2*1)
     *
     * TODO: Implement this method
     */
    public static long factorial(int n) {
        // TODO: Your code here
        return 0;
    }

    // =========================================================================
    // EXERCISE 4: Stream groupingBy — Frequency Distribution
    // =========================================================================
    /**
     * Given a list of integers, use Stream to group them by:
     * - Even or Odd
     * Then for each group, compute the sum. Return a Map<String, Integer>
     * with keys "even" and "odd".
     *
     * Example: [1,2,3,4,5,6] → {even=12, odd=9}
     *
     * TODO: Implement this method
     */
    public static Map<String, Integer> evenOddSum(List<Integer> numbers) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 5: Stream collect — Top N by Score
    // =========================================================================
    /**
     * Given a Map<String, Integer> of student names and scores, use Stream
     * to find the top 3 students by score. Return a List of strings in format
     * "name:score" sorted by score descending.
     *
     * Example: {Alice=85, Bob=92, Charlie=78, Diana=95, Eve=88}
     *        → ["Diana:95", "Bob:92", "Eve:88"]
     *
     * TODO: Implement this method
     */
    public static List<String> topNScored(Map<String, Integer> scores, int n) {
        // TODO: Your code here
        return null;
    }
}
