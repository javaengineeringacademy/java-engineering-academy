# ArrayList Source Code Walkthrough

ArrayList is the most commonly used Collection in Java. Understanding its implementation helps write efficient code and avoid pitfalls.

## Internal Structure

### Object Array

```java
transient Object[] elementData;
private int size;
```

ArrayList is backed by an array. The `size` field tracks the number of elements.

### Default Capacity

```java
private static final int DEFAULT_CAPACITY = 10;
```

When created with no initial capacity, ArrayList starts with space for 10 elements.

## add() Implementation

### Entry Point

```java
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount
    elementData[size++] = e;
    return true;
}
```

### ensureCapacityInternal()

```java
private void ensureCapacityInternal(int minCapacity) {
    ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
}

private static int calculateCapacity(Object[] elementData, int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        return Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    return minCapacity;
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++; // For fail-fast iterators
    
    // Overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
```

### grow() Method

```java
private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1); // 1.5x
    
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    
    // Copy to new array
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

**Design Decision**: Growth factor of 1.5x balances memory usage and reallocation frequency.

### hugeCapacity()

```java
private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ?
        Integer.MAX_VALUE :
        MAX_ARRAY_SIZE;
}
```

## get() Implementation

### Bounds Check

```java
public E get(int index) {
    rangeCheck(index);
    return elementData(index);
}

private void rangeCheck(int index) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

@SuppressWarnings("unchecked")
E elementData(int index) {
    return (E) elementData[index];
}
```

**Note**: Direct array access is O(1).

## set() Implementation

```java
public E set(int index, E element) {
    rangeCheck(index);
    
    E oldValue = elementData(index);
    elementData[index] = element;
    return oldValue;
}
```

Simple replacement, O(1) operation.

## remove() Implementation

### Remove by Index

```java
public E remove(int index) {
    rangeCheck(index);
    
    modCount++;
    E oldValue = elementData(index);
    
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index, numMoved);
    
    elementData[--size] = null; // Clear for GC
    
    return oldValue;
}
```

**Complexity**: O(n) due to arraycopy.

### Remove by Object

```java
public boolean remove(Object o) {
    if (o == null) {
        for (int index = 0; index < size; index++)
            if (elementData[index] == null) {
                fastRemove(index);
                return true;
            }
    } else {
        for (int index = 0; index < size; index++)
            if (o.equals(elementData[index])) {
                fastRemove(index);
                return true;
            }
    }
    return false;
}

private void fastRemove(int index) {
    modCount++;
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index, numMoved);
    elementData[--size] = null;
}
```

**Note**: Two loops for null handling (avoid calling equals() on null).

## clear() Implementation

```java
public void clear() {
    modCount++;
    
    // Let GC do its work
    for (int i = 0; i < size; i++)
        elementData[i] = null;
    
    size = 0;
}
```

Explicit nulling for faster GC (optional in Java 9+).

## trimToSize() Implementation

```java
public void trimToSize() {
    modCount++;
    if (size < elementData.length) {
        elementData = (size == 0)
          ? EMPTY_ELEMENTDATA
          : Arrays.copyOf(elementData, size);
    }
}
```

**Use case**: Reduce memory after bulk operations.

## indexOf() Implementation

```java
public int indexOf(Object o) {
    if (o == null) {
        for (int i = 0; i < size; i++)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = 0; i < size; i++)
            if (o.equals(elementData[i]))
                return i;
    }
    return -1;
}
```

Linear search, O(n) complexity.

## Iterator Implementation

### Itr Class

```java
private class Itr implements Iterator<E> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned
    int expectedModCount = modCount;
    
    public boolean hasNext() {
        return cursor != size;
    }
    
    @SuppressWarnings("unchecked")
    public E next() {
        checkForComodification();
        int i = cursor;
        if (i >= size)
            throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData;
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }
    
    public void remove() {
        if (lastRet < 0)
            throw new IllegalStateException();
        checkForComodification();
        
        ArrayList.this.remove(lastRet);
        cursor = lastRet;
        lastRet = -1;
        expectedModCount = modCount;
    }
    
    final void checkForComodification() {
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
    }
}
```

**Fail-fast behavior**: Iterator throws `ConcurrentModificationException` if list is modified during iteration.

## SubList Implementation

### SubList Class

```java
private class SubList extends AbstractList<E> {
    private final AbstractList<E> parent;
    private final int parentOffset;
    private final int offset;
    int size;
    
    SubList(AbstractList<E> parent, int offset, int fromIndex, int toIndex) {
        this.parent = parent;
        this.offset = offset;
        this.fromIndex = fromIndex;
        this.toIndex = size = toIndex - fromIndex;
        this.modCount = ArrayList.this.modCount;
    }
    
    public E set(int index, E e) {
        rangeCheck(index);
        checkForComodification();
        E oldValue = ArrayList.this.elementData(offset + index);
        ArrayList.this.elementData[offset + index] = e;
        return oldValue;
    }
    
    public E get(int index) {
        rangeCheck(index);
        checkForComodification();
        return ArrayList.this.elementData(offset + index);
    }
}
```

**Note**: SubList is a view, not a copy. Changes affect the original list.

## Key Design Decisions

### 1. Growth Factor (1.5x)

```java
int newCapacity = oldCapacity + (oldCapacity >> 1);
```

- **Why not 2x?**: Avoids memory waste
- **Tradeoff**: More frequent reallocation vs memory usage

### 2. Null Elements Allowed

```java
// Can store null
list.add(null);
```

**Design decision**: Flexibility over performance.

### 3. Fail-Fast Iterators

```java
// Detect concurrent modification
modCount++;
```

**Tradeoff**: Safety vs performance overhead.

### 4. No Synchronization

- **Thread-unsafety by design**
- **Vector** for thread safety (legacy)
- **CopyOnWriteArrayList** for concurrent reads

### 5. System.arraycopy() for Bulk Operations

```java
System.arraycopy(elementData, index+1, elementData, index, numMoved);
```

**Why?**: Native implementation, much faster than loop.

## Performance Characteristics

| Operation | Average | Worst Case |
|-----------|---------|------------|
| add() | O(1) amortized | O(n) |
| get() | O(1) | O(1) |
| set() | O(1) | O(1) |
| remove() | O(n) | O(n) |
| contains() | O(n) | O(n) |
| indexOf() | O(n) | O(n) |

## Memory Usage

```java
// ArrayList with 1000 Integer objects
List<Integer> list = new ArrayList<>();
for (int i = 0; i < 1000; i++) {
    list.add(i);
}

// Memory usage:
// - Object header: 16 bytes
// - Reference to array: 8 bytes
// - Size field: 4 bytes
// - Padding: 4 bytes
// - Array object: 16 + 8*1000 bytes (with boxing overhead)
// Total: ~8KB + Integer objects
```

## Common Mistakes

### 1. Modifying During Iteration

```java
// Bad
for (String s : list) {
    if (s.equals("remove")) {
        list.remove(s); // ConcurrentModificationException
    }
}

// Good
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("remove")) {
        it.remove(); // Safe
    }
}

// Or
list.removeIf(s -> s.equals("remove")); // Java 8+
```

### 2. Using remove(int) Instead of remove(Object)

```java
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
list.remove(1); // Removes index 1 (value 2), not value 1
list.remove(Integer.valueOf(1)); // Removes value 1
```

### 3. Not Pre-sizing

```java
// Bad: Multiple reallocations
List<String> list = new ArrayList<>();
for (int i = 0; i < 10000; i++) {
    list.add("item" + i);
}

// Good: Pre-size
List<String> list = new ArrayList<>(10000);
for (int i = 0; i < 10000; i++) {
    list.add("item" + i);
}
```

### 4. SubList Not Independent

```java
List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
List<String> sub = list.subList(0, 2);
sub.clear(); // Also clears original list!
```

## Resources

- **Java ArrayList Official Docs**
- **OpenJDK Source**: `src/java.base/java/util/ArrayList.java`
- **"Effective Java"** by Joshua Bloch
- **"Java Collections"** by Naftalin & Wampler