```java
public interface SimpleList<E> extends Iterable<E> {
    boolean add(E element);
    E get(int index);
    E set(int index, E element);
    E remove(int index);
    int size();
    boolean isEmpty();
    boolean contains(E element);
    void clear();
    SimpleIterator<E> iterator();
}
```

### Implementation

```java
public class ArrayList<E> implements SimpleList<E> {
    private Object[] elements;
    private int size;

    @Override
    public boolean add(E element) {
        ensureCapacity();
        elements[size++] = element;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }
}
```

---

## Easy Example

### SimpleList Interface

```java
public interface SimpleList<E> extends Iterable<E> {
    boolean add(E element);
    E get(int index);
    E set(int index, E element);
    E remove(int index);
    int size();
    boolean isEmpty();
    boolean contains(E element);
    void clear();
}

// Basic usage
SimpleList<String> list = new ArrayList<>();
list.add("hello");
list.add("world");
String first = list.get(0);  // Type-safe
```

---

## Medium Example

### ArrayList Implementation

```java
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> implements SimpleList<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public ArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Capacity: " + initialCapacity);
        }
        elements = new Object[initialCapacity];
    }

    @Override
    public boolean add(E element) {
        ensureCapacity();
        elements[size++] = element;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        checkIndex(index);
        E old = (E) elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        checkIndex(index);
        E old = (E) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E element) {
        for (int i = 0; i < size; i++) {
            if (java.util.Objects.equals(elements[i], element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;

---

[📖 Continue to Part 2](README-part2.md)
# 09 - Mini Project: Type-Safe Collection Framework (Part 2)

[📖 Back to Part 1](README.md)

---


---

[📖 Continue to Part 2](README-part2.md)
# 09 - Mini Project: Type-Safe Collection Framework (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(list.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}
```

---

## Performance

### ArrayList vs LinkedList

| Operation | ArrayList | LinkedList |
|-----------|-----------|------------|
| add(E) | O(1) amortized | O(1) |
| get(int) | O(1) | O(n) |
| set(int, E) | O(1) | O(n) |
| remove(int) | O(n) | O(n) |
| contains(E) | O(n) | O(n) |
| Memory | Compact | Node overhead |

### When to Use Each

- **ArrayList**: Random access, large datasets, memory efficiency
- **LinkedList**: Frequent insertions/deletions, no random access needed

---

## Best Practices

1. **Use `@SuppressWarnings("unchecked")`** — When casting from Object[]
2. **Check array bounds** — Always validate index parameters
3. **Null safety** — Use Objects.equals() for comparisons
4. **Iterator consistency** — Throw ConcurrentModificationException when needed
5. **Capacity management** — Grow arrays geometrically (2x)

---

## Common Mistakes

### 1. Forgetting to Check Bounds

```java
// BAD
public E get(int index) {
    return (E) elements[index];  // No bounds check
}

// GOOD
public E get(int index) {
    checkIndex(index);
    return (E) elements[index];
}
```

### 2. Not Clearing References

```java
// BAD
public E remove(int index) {
    E old = (E) elements[index];
    System.arraycopy(elements, index + 1, elements, index, size - index - 1);
    size--;
    return old;  // elements[size] still references removed element
}

// GOOD
public E remove(int index) {
    E old = (E) elements[index];
    System.arraycopy(elements, index + 1, elements, index, size - index - 1);
    elements[--size] = null;  // Clear reference for GC
    return old;
}
```

