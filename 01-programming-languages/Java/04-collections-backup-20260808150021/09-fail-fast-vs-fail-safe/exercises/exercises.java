package collections.failsafe.exercises;

import java.util.*;
import java.util.concurrent.*;

/**
 * FAIL-FAST VS FAIL-SAFE EXERCISES — ConcurrentModificationException
 *
 * Complete each TODO. Run tests to verify your solutions.
 */
public class exercises {

    // =========================================================================
    // EXERCISE 1: Fail-Fast — Safe Removal During Iteration
    // =========================================================================
    /**
     * Given a list of integers, remove all elements that are divisible by 3
     * using an Iterator. Do NOT throw ConcurrentModificationException.
     * Return the modified list.
     *
     * Example: [1,3,4,6,7,9] → [1,4,7]
     *
     * TODO: Implement this method
     */
    public static List<Integer> removeMultiplesOfThree(List<Integer> list) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 2: Fail-Safe — CopyOnWriteArrayList Modification
    // =========================================================================
    /**
     * Demonstrate the difference between fail-fast and fail-safe by:
     * 1. Creating a CopyOnWriteArrayList with elements [1,2,3,4,5]
     * 2. Starting an iterator
     * 3. Adding element 6 to the list during iteration
     * 4. Returning all elements visible to the iterator (should be [1,2,3,4,5])
     *
     * TODO: Implement this method
     */
    public static List<Integer> copyOnWriteIterationDemo(CopyOnWriteArrayList<Integer> list) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 3: ConcurrentModificationException Prevention
    // =========================================================================
    /**
     * Given a Map<String, Integer>, remove all entries where the value
     * is less than the given threshold. Use the entrySet iterator.
     * Do NOT throw ConcurrentModificationException.
     *
     * TODO: Implement this method
     */
    public static Map<String, Integer> removeBelowThreshold(Map<String, Integer> map,
                                                            int threshold) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 4: Fail-Safe — ConcurrentHashMap Safe Iteration
    // =========================================================================
    /**
     * Given a ConcurrentHashMap, safely add a new entry and iterate
     * over all entries. Return the list of all key-value pairs as strings
     * in format "key=value".
     *
     * TODO: Implement this method
     */
    public static List<String> safeMapIteration(ConcurrentHashMap<String, Integer> map,
                                                 String newKey, int newValue) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 5: ArrayList vs CopyOnWriteArrayList
    // =========================================================================
    /**
     * Given an ArrayList, create a CopyOnWriteArrayList with the same elements.
     * Then demonstrate that modifying the original ArrayList after creating
     * the copy does NOT affect the copy. Return [originalSize, copySize]
     * after modification.
     *
     * TODO: Implement this method
     */
    public static List<Integer> arrayListVsCopyOnWrite(ArrayList<Integer> original,
                                                        int elementToAdd) {
        // TODO: Your code here
        return null;
    }
}
