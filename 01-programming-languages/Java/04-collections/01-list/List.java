package academy.javaengineering.collections.list;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * List - Ordered collection that allows duplicates (index-based access).
 *
 * Internal: Backed by array (ArrayList) or nodes (LinkedList)
 * Hierarchy: Iterable<E> -> Collection<E> -> List<E>
 *
 * Complexity: get O(1) arr / O(n) link, add O(1)* / O(n), remove O(n)
 * Thread-safe: NO
 *
 * Key characteristics:
 *   - Ordered: Maintains insertion order
 *   - Indexed: 0-based random access via get(index)
 *   - Duplicates: Allows null and duplicate elements
 *   - Elements accessed by position, not value
 *
 * Common implementations: ArrayList, LinkedList, Vector
 */
public interface List<E> extends java.util.Collection<E> {

    /** Returns element at specified index. Time: O(1) ArrayList, O(n) LinkedList */
    E get(int index);

    /** Replaces element at index with value. Returns old element. Time: O(1) */
    E set(int index, E element);

    /** Inserts element at index, shifting subsequent elements right. Time: O(n) */
    void add(int index, E element);

    /** Appends element to end. Returns true. Time: O(1) amortized */
    boolean add(E e);

    /** Removes element at index, shifting subsequent elements left. Returns removed element. Time: O(n) */
    E remove(int index);

    /** Removes first occurrence of object. Returns true if removed. Time: O(n) */
    boolean remove(Object o);

    /** Returns index of first occurrence, or -1 if not found. Time: O(n) */
    int indexOf(Object o);

    /** Returns index of last occurrence, or -1. Time: O(n) */
    int lastIndexOf(Object o);

    /** Returns number of elements. Time: O(1) */
    int size();

    /** Returns true if empty. Time: O(1) */
    boolean isEmpty();

    /** Returns true if list contains object. Time: O(n) */
    boolean contains(Object o);

    /** Removes all elements. Time: O(n) */
    void clear();

    /** Returns array containing all elements. Time: O(n) */
    Object[] toArray();

    /** Returns list iterator starting at specified index. Time: O(n) */
    ListIterator<E> listIterator(int index);

    /** Returns view of portion [fromIndex, toIndex). Time: O(1) */
    java.util.List<E> subList(int fromIndex, int toIndex);

    /** Sorts list using comparator. Time: O(n log n) */
    void sort(java.util.Comparator<? super E> c);
}
