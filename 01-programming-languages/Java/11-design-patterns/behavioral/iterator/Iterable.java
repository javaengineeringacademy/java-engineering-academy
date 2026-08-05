package academy.javaengineering.patterns.behavioral.iterator;

/**
 * Iterable interface for collections that can be iterated.
 * Provides a method to create an iterator for the collection.
 *
 * @param <T> the type of elements in the collection
 */
public interface Iterable<T> {

    /**
     * Create an iterator for this collection.
     *
     * @return a new iterator instance
     */
    Iterator<T> createIterator();

    /**
     * Get the number of elements in the collection.
     *
     * @return the size of the collection
     */
    int size();
}
