package academy.javaengineering.collections.list.copyonwritearraylist;

import java.util.Arrays;

/**
 * CopyOnWriteArrayList - Thread-safe variant of ArrayList.
 *
 * Internal: Object[] array (immutable snapshot per write)
 * Write: Copies entire array on every add/remove/set (Arrays.copyOf)
 * Read: Direct array access (no locking needed)
 *
 * Complexity: get O(1), add O(n), remove O(n), contains O(n), iteration O(n)
 * Thread-safe: YES (snapshot isolation - readers never block)
 *
 * Best for: Read-heavy scenarios with rare writes (event listeners, observer lists).
 * Worst for: Write-heavy scenarios (each write copies entire array).
 * Iterators use snapshot semantics - never throw ConcurrentModificationException.
 */
public class CopyOnWriteArrayList<E> implements java.util.List<E> {

    transient volatile Object[] array;

    public CopyOnWriteArrayList() { array = new Object[0]; }

    private synchronized void setArray(Object[] a) { array = a; }

    /** Appends element. Copies array. O(n) */
    public boolean add(E e) {
        synchronized (this) {
            Object[] elements = array;
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len + 1);
            newElements[len] = e;
            setArray(newElements);
            return true;
        }
    }

    /** Returns element at index. Reads from snapshot array. O(1) */
    public E get(int index) {
        return get(array, index);
    }

    @SuppressWarnings("unchecked")
    private E get(Object[] a, int index) { return (E) a[index]; }

    /** Replaces at index. Copies array. O(n) */
    public E set(int index, E element) {
        synchronized (this) {
            Object[] elements = array;
            @SuppressWarnings("unchecked")
            E oldValue = (E) elements[index];
            if (oldValue != element) {
                int len = elements.length;
                Object[] newElements = Arrays.copyOf(elements, len);
                newElements[index] = element;
                setArray(newElements);
            }
            return oldValue;
        }
    }

    /** Removes at index. Copies array. O(n) */
    public E remove(int index) {
        synchronized (this) {
            Object[] elements = array;
            int len = elements.length;
            @SuppressWarnings("unchecked")
            E oldValue = (E) elements[index];
            int numMoved = len - index - 1;
            if (numMoved == 0)
                setArray(Arrays.copyOf(elements, len - 1));
            else {
                Object[] newElements = new Object[len - 1];
                System.arraycopy(elements, 0, newElements, 0, index);
                System.arraycopy(elements, index + 1, newElements, index, numMoved);
                setArray(newElements);
            }
            return oldValue;
        }
    }

    public int size() { return array.length; }
    public boolean isEmpty() { return array.length == 0; }

    /** Iterator over snapshot array - never throws ConcurrentModificationException */
    public java.util.Iterator<E> iterator() {
        return new java.util.Iterator<E>() {
            final Object[] snapshot = array;
            int cursor = 0;
            public boolean hasNext() { return cursor < snapshot.length; }
            @SuppressWarnings("unchecked")
            public E next() { return (E) snapshot[cursor++]; }
        };
    }
}
