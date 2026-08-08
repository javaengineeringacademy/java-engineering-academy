import java.util.*;
import java.util.function.Predicate;

/**
 * Iterator Exercises - 5 challenges
 *
 * Complete each method body. Each exercise uses the Iterator interface.
 * Run tests after completing to verify your solutions.
 */
public class IteratorExercises {

    /**
     * Exercise 1: Remove Elements Matching Predicate
     * Remove all elements from the list that match the given predicate.
     * Use iterator's remove() method for safe removal.
     *
     * Example: removeIf([1, 2, 3, 4, 5], x -> x % 2 == 0) → [1, 3, 5]
     */
    public static <T> void removeIf(List<T> list, Predicate<T> predicate) {
        // TODO: Implement using Iterator
    }

    /**
     * Exercise 2: Find Elements Between Two Values
     * Return elements that are strictly between lower and upper bounds (exclusive).
     * Use iterator to traverse the collection.
     *
     * Example: findBetween([1, 5, 3, 8, 2, 7], 2, 7) → [5, 3]
     */
    public static List<Integer> findBetween(Collection<Integer> collection, int lower, int upper) {
        // TODO: Implement using Iterator
        return new ArrayList<>();
    }

    /**
     * Exercise 3: Collect Every Nth Element
     * Return every nth element from the collection (1-indexed).
     *
     * Example: everyNth([1, 2, 3, 4, 5, 6, 7, 8, 9], 3) → [3, 6, 9]
     * Example: everyNth([1, 2, 3, 4, 5], 2) → [2, 4]
     */
    public static <T> List<T> everyNth(Collection<T> collection, int n) {
        // TODO: Implement using Iterator
        return new ArrayList<>();
    }

    /**
     * Exercise 4: Detect Cycle in List
     * Detect if a list contains a cycle (last element points back to an earlier element).
     * Treat the list as a circular structure for this exercise.
     *
     * Example: hasCycle([1, 2, 3, 4]) → false
     * Example: hasCycle([1, 2, 3, 1]) → true (element 1 appears twice)
     */
    public static <T> boolean hasCycle(List<T> list) {
        // TODO: Implement using Iterator
        return false;
    }

    /**
     * Exercise 5: Merge Two Sorted Iterators
     * Merge two sorted iterators into a single sorted iterator.
     *
     * Example: merge([1, 3, 5], [2, 4, 6]) → [1, 2, 3, 4, 5, 6]
     */
    public static Iterator<Integer> mergeSorted(Iterator<Integer> it1, Iterator<Integer> it2) {
        // TODO: Implement using Iterator
        return Collections.<Integer>emptyList().iterator();
    }
}
