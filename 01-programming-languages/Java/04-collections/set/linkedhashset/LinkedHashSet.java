package academy.javaengineering.collections.set.linkedhashset;

import java.util.HashMap;

/**
 * LinkedHashSet - Hash table + doubly-linked list maintaining insertion order.
 *
 * Internal: LinkedHashMap<E, PRESENT> (linked list preserves insertion order)
 * Order: Elements iterated in insertion order (order added)
 *
 * Complexity: add O(1), remove O(1), contains O(1), iteration O(n)
 * Thread-safe: NO
 *
 * Best for: When you need Set behavior with predictable iteration order.
 * Slightly slower than HashSet due to linked list maintenance overhead.
 */
public class LinkedHashSet<E> extends java.util.HashSet<E> {

    public LinkedHashSet() { super(); }

    public LinkedHashSet(int initialCapacity) { super(initialCapacity); }

    /** Uses LinkedHashMap internally to maintain insertion order */
    private static <K, V> java.util.HashMap<K, V> newLinkedHashMap(int cap) {
        return new java.util.LinkedHashMap<>(cap);
    }
}
