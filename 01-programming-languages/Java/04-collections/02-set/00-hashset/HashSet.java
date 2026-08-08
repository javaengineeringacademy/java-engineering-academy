package academy.javaengineering.collections.set.hashset;

import java.util.HashMap;

/**
 * HashSet - Hash table backed Set implementation.
 *
 * Internal: HashMap<E, PRESENT> (dummy value for all entries)
 * Hashing: Uses hashCode() for bucket placement, equals() for collision resolution
 *
 * Complexity: add O(1), remove O(1), contains O(1), iteration O(n + capacity)
 * Thread-safe: NO
 *
 * Best for: Fast membership testing, no ordering guarantees.
 * Worst for: Ordered iteration (use LinkedHashSet) or sorted (use TreeSet).
 */
public class HashSet<E> extends java.util.AbstractSet<E> {

    private transient HashMap<E, Object> map;
    private static final Object PRESENT = new Object();

    public HashSet() { map = new HashMap<>(); }

    public HashSet(int initialCapacity) { map = new HashMap<>(initialCapacity); }

    /** Adds element if not present. O(1) amortized */
    public boolean add(E e) { return map.put(e, PRESENT) == null; }

    /** Removes element. O(1) */
    public boolean remove(Object o) { return map.remove(o) == PRESENT; }

    /** Returns true if element exists. O(1) */
    public boolean contains(Object o) { return map.containsKey(o); }

    public int size() { return map.size(); }
    public boolean isEmpty() { return map.isEmpty(); }
    public void clear() { map.clear(); }

    public java.util.Iterator<E> iterator() { return map.keySet().iterator(); }
}
