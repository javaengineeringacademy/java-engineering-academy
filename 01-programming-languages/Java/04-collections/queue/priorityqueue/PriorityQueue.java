package academy.javaengineering.collections.queue.priorityqueue;

/**
 * PriorityQueue - Heap-based priority queue.
 *
 * Internal: Object[] queue (binary min-heap)
 * Heap property: parent <= children (min-heap for natural ordering)
 * Growth: 2x when full (Arrays.copyOf)
 *
 * Complexity: offer O(log n), poll O(log n), peek O(1), contains O(n)
 * Thread-safe: NO
 *
 * Elements ordered by natural ordering or Comparator.
 * Head is always the least element.
 * Does NOT permit null elements.
 * O(log n) for add/poll, O(1) for peek, O(n) for contains/remove(Object).
 */
public class PriorityQueue<E> extends java.util.AbstractQueue<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    transient Object[] queue;
    private int size = 0;
    private final java.util.Comparator<? super E> comparator;

    public PriorityQueue() { this.comparator = null; }

    public PriorityQueue(int initialCapacity) {
        this.comparator = null;
        this.queue = new Object[initialCapacity];
    }

    public PriorityQueue(java.util.Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.queue = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    /** Adds element to heap. O(log n) - bubble up */
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        int i = size;
        size = i + 1;
        if (i == 0) queue[0] = e;
        else siftUp(i, e);
        return true;
    }

    /** Removes and returns head (least element). O(log n) - bubble down */
    public E poll() {
        if (size == 0) return null;
        int s = --size;
        E result = (E) queue[0];
        E x = (E) queue[s];
        queue[s] = null;
        if (s != 0) siftDown(0, x);
        return result;
    }

    /** Returns head without removing. O(1) */
    public E peek() { return size == 0 ? null : (E) queue[0]; }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    /** Finds parent index. O(1) */
    private int parent(int pos) { return (pos - 1) >> 1; }

    /** Finds left child index. O(1) */
    private int left(int pos) { return (pos << 1) + 1; }

    /** Finds right child index. O(1) */
    private int right(int pos) { return (pos << 1) + 2; }

    /** Bubble up: restore heap property upward. O(log n) */
    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = parent(k);
            Object e = queue[parent];
            if (comparator == null)
                { if (((Comparable<? super E>) x).compareTo((E) e) >= 0) break; }
            else
                { if (comparator.compare(x, (E) e) >= 0) break; }
            queue[k] = e;
            k = parent;
        }
        queue[k] = x;
    }

    /** Bubble down: restore heap property downward. O(log n) */
    private void siftDown(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = left(k);
            Object c = queue[child];
            int rightChild = right(k);
            if (rightChild < size) {
                Object rc = queue[rightChild];
                if (comparator == null) {
                    if (((Comparable<? super E>) c).compareTo((E) rc) > 0) { c = rc; child = rightChild; }
                } else {
                    if (comparator.compare((E) c, (E) rc) > 0) { c = rc; child = rightChild; }
                }
            }
            if (comparator == null) {
                if (((Comparable<? super E>) x).compareTo((E) c) <= 0) break;
            } else {
                if (comparator.compare(x, (E) c) <= 0) break;
            }
            queue[k] = c;
            k = child;
        }
        queue[k] = x;
    }
}
