package academy.javaengineering.collections.set.treeset;

import java.util.TreeMap;

/**
 * TreeSet - Red-black tree backed sorted Set.
 *
 * Internal: TreeMap<E, PRESENT> (Red-Black balanced BST)
 * Order: Elements in natural ordering or by Comparator
 *
 * Complexity: add O(log n), remove O(log n), contains O(log n), iteration O(n)
 * Thread-safe: NO
 *
 * Best for: Sorted iteration, range queries (subSet, headSet, tailSet).
 * Elements must be Comparable or provide a Comparator.
 */
public class TreeSet<E> extends java.util.AbstractSet<E> {

    private transient java.util.NavigableMap<E, Object> m;
    private static final Object PRESENT = new Object();

    public TreeSet() { m = new TreeMap<>(); }

    public TreeSet(java.util.Comparator<? super E> comparator) { m = new TreeMap<>(comparator); }

    /** Adds element in sorted position. O(log n) */
    public boolean add(E e) { return m.put(e, PRESENT) == null; }

    /** Removes element. O(log n) */
    public boolean remove(Object o) { return m.remove(o) == PRESENT; }

    /** Returns true if element exists. O(log n) */
    public boolean contains(Object o) { return m.containsKey(o); }

    public int size() { return m.size(); }
    public boolean isEmpty() { return m.isEmpty(); }
    public void clear() { m.clear(); }

    /** Returns lowest element. O(1) */
    public E first() { return m.firstKey(); }

    /** Returns highest element. O(1) */
    public E last() { return m.lastKey(); }

    /** Returns view of elements strictly less than toElement. O(log n) */
    public java.util.SortedSet<E> headSet(E toElement) {
        return new java.util.TreeSet<>(m.headMap(toElement, true));
    }

    /** Returns view of elements from fromElement to toElement. O(log n) */
    public java.util.SortedSet<E> subSet(E fromElement, E toElement) {
        return new java.util.TreeSet<>(m.subMap(fromElement, true, toElement, true));
    }

    /** Returns view of elements greater than or equal to fromElement. O(log n) */
    public java.util.SortedSet<E> tailSet(E fromElement) {
        return new java.util.TreeSet<>(m.tailMap(fromElement, true));
    }

    public java.util.Iterator<E> iterator() { return m.keySet().iterator(); }
}
