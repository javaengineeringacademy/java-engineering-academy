package academy.javaengineering.collections.list.arraylist;

import java.util.Arrays;
import java.util.Iterator;

/**
 * ArrayList - Dynamic array implementation of List.
 *
 * Internal: transient Object[] elementData
 * Growth: 1.5x capacity when full (elementData = Arrays.copyOf(elementData, newCapacity))
 *
 * Complexity: get O(1), add O(1)*, add(i) O(n), remove O(n), contains O(n), size O(1)
 * Thread-safe: NO (use Collections.synchronizedList or CopyOnWriteArrayList)
 *
 * Best for: Random access, iteration, append. Worst for: frequent insert/remove at middle.
 */
public class ArrayList<E> implements java.util.List<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = {};

    transient Object[] elementData;
    private int size;

    public ArrayList() {
        this.elementData = DEFAULT_CAPACITY == 0 ? EMPTY_ELEMENTDATA : new Object[DEFAULT_CAPACITY];
    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0)
            this.elementData = new Object[initialCapacity];
        else if (initialCapacity == 0)
            this.elementData = EMPTY_ELEMENTDATA;
        else
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
    }

    /** Appends element to end. Grows array by 1.5x if needed. Amortized O(1) */
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;
        return true;
    }

    /** Returns element at index. O(1) - direct array access */
    public E get(int index) {
        rangeCheck(index);
        return elementData(index);
    }

    /** Replaces element at index. Returns old value. O(1) */
    public E set(int index, E element) {
        rangeCheck(index);
        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    /** Inserts element at index, shifting elements right. O(n) */
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        ensureCapacityInternal(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }

    /** Removes element at index, shifting elements left. O(n) */
    public E remove(int index) {
        rangeCheck(index);
        E oldValue = elementData(index);
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        elementData[--size] = null;
        return oldValue;
    }

    /** Returns index of first occurrence, or -1. O(n) */
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i] == null) return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i])) return i;
        }
        return -1;
    }

    /** Returns true if list contains object. O(n) */
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    /** Removes all elements. O(n) */
    public void clear() {
        for (int i = 0; i < size; i++)
            elementData[i] = null;
        size = 0;
    }

    /** Returns array copy. O(n) */
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;
            public boolean hasNext() { return cursor != size; }
            @SuppressWarnings("unchecked")
            public E next() { return (E) elementData[cursor++]; }
        };
    }

    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == EMPTY_ELEMENTDATA)
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    private void grow(int minCapacity) {
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1); // 1.5x growth
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    @SuppressWarnings("unchecked")
    private E elementData(int index) { return (E) elementData[index]; }

    private void rangeCheck(int index) {
        if (index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
}
