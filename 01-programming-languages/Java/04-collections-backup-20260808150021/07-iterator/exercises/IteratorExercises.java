package collections.iterator.exercises;

import java.util.*;

/**
 * ITERATOR EXERCISES — Advanced Iterator and ListIterator operations.
 *
 * Complete each TODO. Run tests to verify your solutions.
 */
public class IteratorExercises {

    // =========================================================================
    // EXERCISE 1: Merge Two Sorted Iterators
    // =========================================================================
    /**
     * Given two sorted Iterators, merge them into a single sorted
     * Iterator without loading all elements into memory.
     * Use lazy evaluation — elements are produced on demand.
     *
     * TODO: Implement this method
     */
    public static <T extends Comparable<? super T>> Iterator<T> mergeSorted(
            Iterator<T> a, Iterator<T> b) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 2: Chunked Iterator
    // =========================================================================
    /**
     * Given an Iterator and a chunk size, return a new Iterator that
     * yields sub-lists (chunks) of the specified size. The last chunk
     * may be smaller if elements are not evenly divisible.
     *
     * Example: [1,2,3,4,5], size=2 → [1,2], [3,4], [5]
     *
     * TODO: Implement this method
     */
    public static <T> Iterator<List<T>> chunked(Iterator<T> source, int size) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 3: Flattening Nested Iterators
    // =========================================================================
    /**
     * Given an Iterator of Iterators, return an Iterator that flattens
     * all elements into a single sequence.
     *
     * Example: [[1,2],[3],[4,5,6]] → [1,2,3,4,5,6]
     *
     * TODO: Implement this method
     */
    public static <T> Iterator<T> flatten(Iterator<? extends Iterator<T>> iterators) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 4: Filtering Iterator
    // =========================================================================
    /**
     * Given an Iterator and a Predicate, return a new Iterator that
     * only yields elements matching the predicate. Skip non-matching
     * elements lazily.
     *
     * TODO: Implement this method
     */
    public static <T> Iterator<T> filtered(Iterator<T> source, java.util.function.Predicate<T> predicate) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 5: Peeking Iterator
    // =========================================================================
    /**
     * Given an Iterator, implement a PeekingIterator that supports:
     *   - peek(): view the next element without advancing
     *   - next(): advance and return the element
     *   - hasNext(): check if more elements exist
     *
     * TODO: Implement the PeekingIterator inner class
     */
    public static class PeekingIterator<T> implements Iterator<T> {
        private final Iterator<T> source;
        private T peeked = null;
        private boolean hasPeeked = false;

        public PeekingIterator(Iterator<T> source) {
            this.source = source;
        }

        public T peek() {
            // TODO: Your code here
            return null;
        }

        @Override
        public boolean hasNext() {
            // TODO: Your code here
            return false;
        }

        @Override
        public T next() {
            // TODO: Your code here
            return null;
        }
    }
}
