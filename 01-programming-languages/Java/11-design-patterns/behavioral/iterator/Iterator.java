package academy.javaengineering.patterns.behavioral.iterator;

/**
 * Iterator interface for traversing a collection.
 * Provides a uniform interface for accessing elements sequentially.
 *
 * @param <T> the type of elements being iterated
 */
public interface Iterator<T> {

    /**
     * Check if there are more elements to iterate.
     *
     * @return true if there are more elements
     */
    boolean hasNext();

    /**
     * Get the next element in the iteration.
     *
     * @return the next element
     */
    T next();

    /**
     * Reset the iterator to the beginning.
     */
    default void reset() {
        throw new UnsupportedOperationException("Reset not supported");
    }
}
