package academy.javaengineering.collections.queue.deque;

/**
 * Deque - Double-ended queue: insertion/removal at both ends.
 *
 * Internal: Backed by circular array (ArrayDeque) or doubly-linked nodes (LinkedList)
 * Hierarchy: Iterable<E> -> Collection<E> -> Queue<E> -> Deque<E>
 *
 * Complexity: addFirst/addLast O(1), removeFirst/removeLast O(1), peek O(1)
 * Thread-safe: NO
 *
 * Key characteristics:
 *   - Double-ended: Insert/remove from both head and tail
 *   - Can serve as both Queue (FIFO) and Stack (LIFO)
 *   - Preferred over Stack class for stack behavior
 *   - No capacity restrictions (ArrayDeque grows dynamically)
 *
 * Queue operations (FIFO): offer/poll/peek
 * Stack operations (LIFO): push/pop/peek
 * Deque-specific: addFirst/addLast/removeFirst/removeLast
 *
 * Common implementations: ArrayDeque (preferred), LinkedList
 */
public interface Deque<E> extends java.util.Queue<E> {

    /** Inserts element at front (head). Time: O(1) */
    void addFirst(E e);

    /** Inserts element at rear (tail). Time: O(1) */
    void addLast(E e);

    /** Inserts element at front. Returns false if full. Time: O(1) */
    boolean offerFirst(E e);

    /** Inserts element at rear. Returns false if full. Time: O(1) */
    boolean offerLast(E e);

    /** Pushes element onto stack (same as addFirst). Time: O(1) */
    void push(E e);

    /** Retrieves and removes first element. Throws exception if empty. Time: O(1) */
    E removeFirst();

    /** Retrieves and removes last element. Throws exception if empty. Time: O(1) */
    E removeLast();

    /** Retrieves and removes first element. Returns null if empty. Time: O(1) */
    E pollFirst();

    /** Retrieves and removes last element. Returns null if empty. Time: O(1) */
    E pollLast();

    /** Pops from stack (same as removeFirst). Time: O(1) */
    E pop();

    /** Retrieves but does not remove first element. Time: O(1) */
    E peekFirst();

    /** Retrieves but does not remove last element. Time: O(1) */
    E peekLast();

    /** Returns true if deque contains object. Time: O(n) */
    boolean contains(Object o);

    /** Returns number of elements. Time: O(1) */
    int size();
}
