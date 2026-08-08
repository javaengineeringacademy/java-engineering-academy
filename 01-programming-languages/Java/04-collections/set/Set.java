package academy.javaengineering.collections.set;

import java.util.Iterator;

/**
 * Set - Collection that contains no duplicate elements.
 *
 * Internal: Backed by hash table (HashSet), hash+linked list (LinkedHashSet),
 *           or red-black tree (TreeSet)
 * Hierarchy: Iterable<E> -> Collection<E> -> Set<E>
 *
 * Complexity: add O(1) hash / O(log n) tree, contains O(1) / O(log n), remove O(1) / O(log n)
 * Thread-safe: NO
 *
 * Key characteristics:
 *   - No duplicates: add() returns false if element exists
 *   - At most one null element (HashSet/LinkedHashSet)
 *   - Unordered (HashSet), insertion-ordered (LinkedHashSet), sorted (TreeSet)
 *   - Membership testing is primary operation
 *
 * Common implementations: HashSet, LinkedHashSet, TreeSet, EnumSet
 */
public interface Set<E> extends java.util.Collection<E> {

    /** Adds element if not present. Returns true if added. Time: O(1) hash, O(log n) tree */
    boolean add(E e);

    /** Removes element if present. Returns true if removed. Time: O(1) hash */
    boolean remove(Object o);

    /** Returns true if set contains object. Time: O(1) hash, O(log n) tree */
    boolean contains(Object o);

    /** Returns number of elements. Time: O(1) */
    int size();

    /** Returns true if empty. Time: O(1) */
    boolean isEmpty();

    /** Removes all elements. Time: O(n) */
    void clear();

    /** Returns iterator over elements. Time: O(1) */
    Iterator<E> iterator();

    /** Returns array containing all elements. Time: O(n) */
    Object[] toArray();

    /** Returns true if set contains all elements in collection. Time: O(m) where m = c.size() */
    boolean containsAll(java.util.Collection<?> c);

    /** Adds all elements from collection. Returns true if set changed. Time: O(m) */
    boolean addAll(java.util.Collection<? extends E> c);

    /** Retains only elements also in collection. Time: O(m) */
    boolean retainAll(java.util.Collection<?> c);

    /** Removes elements also in collection. Time: O(m) */
    boolean removeAll(java.util.Collection<?> c);
}
