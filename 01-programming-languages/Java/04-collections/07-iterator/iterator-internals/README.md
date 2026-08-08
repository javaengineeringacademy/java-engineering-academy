# Iterator Internals

## Overview

This topic dives deep into how iterators work internally in various Java collection implementations. Understanding iterator internals helps you write more efficient code and debug `ConcurrentModificationException` issues.

## Learning Objectives

- Understand how ArrayList iterator tracks position and detects modification
- Learn how LinkedList iterator traverses nodes
- Understand the `modCount` mechanism for fail-fast iterators
- Learn how CopyOnWriteArrayList creates snapshot iterators
- Recognize iterator performance characteristics

## ArrayList Iterator Internals

```java
// Simplified ArrayList iterator
private class Itr implements Iterator<E> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned
    int expectedModCount = modCount;

    public boolean hasNext() {
        return cursor != size;
    }

    public E next() {
        checkForComodification();
        int i = cursor;
        if (i >= size) throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData;
        if (i >= elementData.length) throw new ConcurrentModificationException();
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }

    public void remove() {
        if (lastRet < 0) throw new IllegalStateException();
        checkForComodification();
        try {
            ArrayList.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
            expectedModCount = modCount;
        } catch (IndexOutOfBoundsException ex) {
            throw new ConcurrentModificationException();
        }
    }

    final void checkForComodification() {
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
    }
}
```

## LinkedList Iterator Internals

```java
// Simplified LinkedList listIterator
private class ListItr implements ListIterator<E> {
    private Node<E> lastReturned;
    private Node<E> next;
    private int nextIndex;
    private int expectedModCount = modCount;

    public boolean hasNext() {
        return nextIndex < size;
    }

    public E next() {
        checkForComodification();
        if (!hasNext()) throw new NoSuchElementException();
        lastReturned = next;
        next = next.next;
        nextIndex++;
        return lastReturned.item;
    }

    public boolean hasPrevious() {
        return nextIndex > 0;
    }

    public E previous() {
        checkForComodification();
        if (!hasPrevious()) throw new NoSuchElementException();
        lastReturned = next = (next == null) ? last : next.prev;
        nextIndex--;
        return lastReturned.item;
    }
}
```

## CopyOnWriteArrayList Snapshot Iterator

```java
// CopyOnWriteArrayList creates a snapshot
private static class COWIterator<E> implements Iterator<E> {
    private final Object[] snapshot;
    private int cursor;

    COWIterator(Object[] elements, int initialCursor) {
        snapshot = elements; // Reference to current array
        cursor = initialCursor;
    }

    public boolean hasNext() {
        return cursor < snapshot.length;
    }

    public E next() {
        if (!hasNext()) throw new NoSuchElementException();
        return (E) snapshot[cursor++];
    }

    // No remove() method - iteration is read-only
}
```

## Key Takeaways

1. ArrayList iterator uses array indexing (O(1) per next)
2. LinkedList iterator follows node pointers (O(1) per next, but cache-unfriendly)
3. CopyOnWriteArrayList iterator works on a snapshot (consistent but may be stale)
4. `modCount` is the mechanism that enables fail-fast behavior
5. Iterator.remove() updates `expectedModCount` to avoid `ConcurrentModificationException`
