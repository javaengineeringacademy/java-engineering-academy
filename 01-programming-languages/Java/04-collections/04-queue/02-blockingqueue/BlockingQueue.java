package academy.javaengineering.collections.queue.blockingqueue;

import java.util.concurrent.TimeUnit;

/**
 * BlockingQueue - Queue that blocks on add/remove operations.
 *
 * Internal: Implementation-specific (ArrayBlockingQueue = array,
 *           LinkedBlockingQueue = nodes, PriorityBlockingQueue = heap)
 *
 * Complexity: put O(1), take O(1)/O(log n), offer O(1), peek O(1)
 * Thread-safe: YES (designed for producer-consumer patterns)
 *
 * Four sets of operations:
 *   1. Throws exception:  add(e), remove(), element()
 *   2. Returns null/false: offer(e), poll(), peek()
 *   3. Blocks:             put(e), take()      <-- key difference
 *   4. Times out:          offer(e, time, unit), poll(time, unit)
 *
 * put(e)  - Blocks if queue is full until space available
 * take()  - Blocks if queue is empty until element available
 *
 * Common implementations: ArrayBlockingQueue, LinkedBlockingQueue,
 *                         PriorityBlockingQueue, SynchronousQueue
 */
public interface BlockingQueue<E> extends java.util.Queue<E> {

    /** Inserts element, blocks if queue is full until space available. */
    void put(E e) throws InterruptedException;

    /** Removes and returns head, blocks if queue is empty until available. */
    E take() throws InterruptedException;

    /** Inserts element. Returns false if queue is full (non-blocking). */
    boolean offer(E e);

    /** Removes and returns head. Returns null if empty (non-blocking). */
    E poll();

    /** Inserts element, blocks up to specified time. Returns false if timed out. */
    boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException;

    /** Removes head, blocks up to specified time. Returns null if timed out. */
    E poll(long timeout, TimeUnit unit) throws InterruptedException;

    /** Returns remaining capacity. */
    int remainingCapacity();

    /** Returns true if queue contains object. */
    boolean contains(Object o);

    /** Returns number of elements. */
    int size();
}
