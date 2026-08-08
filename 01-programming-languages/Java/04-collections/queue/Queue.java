package academy.javaengineering.collections.queue;

/**
 * Queue - Collection designed for holding elements prior to processing (FIFO).
 *
 * Internal: Backed by array (PriorityQueue/ArrayDeque) or linked nodes (LinkedList)
 * Hierarchy: Iterable<E> -> Collection<E> -> Queue<E>
 *
 * Complexity: offer O(1)*, poll O(log n) heap / O(1) array, peek O(1)
 * Thread-safe: NO (use BlockingQueue for concurrent)
 *
 * Key characteristics:
 *   - FIFO: First-In-First-Out ordering (except PriorityQueue)
 *   - Two sets of operations: throws exception vs returns null/false
 *   - offer/poll: return false/null on failure (preferred)
 *   - add/remove: throw exception on failure
 *
 * Exception-throwing:  add(e), remove(), element()
 * Failure-returning:   offer(e), poll(), peek()
 *
 * Common implementations: PriorityQueue, ArrayDeque, LinkedList
 */
public interface Queue<E> extends java.util.Collection<E> {

    /** Inserts element. Returns true if successful. Time: O(1) amortized */
    boolean offer(E e);

    /** Retrieves and removes head. Returns null if empty. Time: O(1) ArrayDeque, O(log n) PriorityQueue */
    E poll();

    /** Retrieves but does not remove head. Returns null if empty. Time: O(1) */
    E peek();

    /** Inserts element. Throws IllegalStateException if no space. Time: O(1) */
    boolean add(E e);

    /** Retrieves and removes head. Throws NoSuchElementException if empty. Time: O(1) */
    E remove();

    /** Retrieves but does not remove head. Throws NoSuchElementException if empty. Time: O(1) */
    E element();

    /** Returns number of elements. Time: O(1) */
    int size();

    /** Returns true if empty. Time: O(1) */
    boolean isEmpty();
}
