package academy.javaengineering.collections.list.vector;

/**
 * Vector - Synchronized (thread-safe) dynamic array.
 *
 * Internal: Object[] elementData (same as ArrayList)
 * Growth: 2x capacity (doubles, unlike ArrayList's 1.5x)
 *
 * Complexity: get O(1), add O(1)*, remove O(n), contains O(n)
 * Thread-safe: YES (all methods synchronized)
 *
 * Legacy: Prefer ArrayList + Collections.synchronizedList() or CopyOnWriteArrayList.
 * Slower than ArrayList due to synchronization overhead on every operation.
 */
public class Vector<E> extends java.util.ArrayList<E> {

    protected int elementCount;
    protected Object[] elementData;
    private int capacityIncrement;

    public Vector(int initialCapacity, int capacityIncrement) {
        super(initialCapacity);
        this.elementData = new Object[initialCapacity];
        this.capacityIncrement = capacityIncrement;
    }

    public Vector(int initialCapacity) {
        this(initialCapacity, 0);
    }

    public Vector() {
        this(10);
    }

    /** Adds element. synchronized. O(1)* */
    public synchronized boolean add(E e) {
        ensureCapacityHelper(elementCount + 1);
        elementData[elementCount++] = e;
        return true;
    }

    /** Returns element at index. synchronized. O(1) */
    public synchronized E get(int index) {
        if (index >= elementCount) throw new ArrayIndexOutOfBoundsException(index);
        return elementData(index);
    }

    /** Removes element at index. synchronized. O(n) */
    public synchronized E remove(int index) {
        if (index >= elementCount) throw new ArrayIndexOutOfBoundsException(index);
        E oldValue = elementData(index);
        int numMoved = elementCount - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        elementData[--elementCount] = null;
        return oldValue;
    }

    /** Returns current capacity. O(1) */
    public synchronized int capacity() {
        return elementData.length;
    }

    /** Trims capacity to current size. O(n) */
    public synchronized void trimToSize() {
        if (elementCount < elementData.length)
            elementData = java.util.Arrays.copyOf(elementData, elementCount);
    }

    private void ensureCapacityHelper(int minCapacity) {
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    private void grow(int minCapacity) {
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
            capacityIncrement : oldCapacity); // 2x growth if no increment
        if (newCapacity - minCapacity < 0) newCapacity = minCapacity;
        elementData = java.util.Arrays.copyOf(elementData, newCapacity);
    }

    @SuppressWarnings("unchecked")
    private E elementData(int index) { return (E) elementData[index]; }
}
