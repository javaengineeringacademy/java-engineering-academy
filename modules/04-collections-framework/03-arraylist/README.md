# ArrayList

## 1. Introduction

ArrayList is the most widely used implementation of the `List` interface in Java. It uses a dynamic array internally, providing O(1) random access to elements and efficient iteration. Think of ArrayList as a resizable array that automatically grows and shrinks as you add or remove elements.

ArrayList is the default choice for most List use cases because arrays are the most efficient data structure for indexed access, and ArrayList adds the convenience of dynamic resizing. It provides the best balance of performance, memory efficiency, and ease of use for the majority of real-world scenarios.

The internal backing array (`elementData`) is allocated with some extra capacity beyond the current size. When the array fills up, a new array is created with 1.5x the previous capacity (default 10 → 15 → 22 → 33 → ...), and all elements are copied over. This amortized approach ensures that most `add()` operations are O(1) even though occasional resizing is O(n).

## 2. Learning Objectives

- Create and use ArrayList with generics
- Understand ArrayList's internal dynamic array mechanism
- Learn ArrayList performance characteristics (O(1) access, O(n) insertion/removal)
- Master common operations: add, get, set, remove, subList, sort
- Compare ArrayList vs LinkedList with concrete performance benchmarks
- Understand capacity vs size and initial capacity optimization
- Learn thread-safety considerations for ArrayList
- Recognize when ArrayList is NOT the right choice

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming (interfaces, inheritance)
- Module 03: Generics basics
- Basic understanding of arrays and their limitations
- Familiarity with the List interface

## 4. Why This Concept Exists

Arrays in Java are fixed-size: once created, you cannot add or remove elements. This is a significant limitation for real-world applications where data sizes are dynamic. Before ArrayList, developers had to manually:
1. Create arrays of estimated size
2. Track the current size separately
3. Create new arrays and copy elements when full
4. Handle null values for unused slots

ArrayList solves all these problems by:
- **Automatic resizing**: Grows dynamically as elements are added
- **Simplified API**: `add()`, `get()`, `remove()` without manual array management
- **Type safety**: Generic type parameter prevents ClassCastException
- **Integration**: Works with all Collection APIs (streams, iterators, algorithms)

## 5. Problem Statement

Consider building a shopping cart for an e-commerce application:
- Items are added as users browse
- Items can be removed at any time
- The cart must display items in order
- Users may have 1 item or 1000 items
- The cart must support quick access to calculate totals

A fixed-size array would fail because we don't know the cart size upfront. A LinkedList would work but provide slower random access for calculating totals. ArrayList provides the optimal solution: dynamic sizing with O(1) indexed access.

## 6. Theory

### Internal Structure

ArrayList maintains:
- `transient Object[] elementData`: The backing array
- `private int size`: Number of elements (not the array length)

### Resizing Mechanism

When `add()` is called and the array is full:
1. New capacity = old capacity + (old capacity >> 1) (1.5x)
2. A new array of the new capacity is created
3. `Arrays.copyOf()` copies all elements to the new array
4. The old array becomes eligible for garbage collection

### Growth Factor Analysis

| Initial | After 10 adds | After 100 adds | After 1000 adds |
|---------|---------------|----------------|-----------------|
| 10 | 15 | 169 | 1706 |

The 1.5x growth factor is a balance between:
- **Too small (1.1x)**: Frequent resizing, O(n) copies
- **Too large (2x)**: Wasted memory (up to 50% unused)
- **1.5x**: At most 33% wasted space, O(n/3) total copies for n elements

### Amortized Analysis

Adding n elements to an initially empty ArrayList:
- Total cost = n + n/2 + n/4 + ... + 1 ≈ 2n
- Amortized cost per add = O(1)

### modCount for Fail-Fast Iterators

ArrayList maintains a `modCount` field that increments on structural modifications. Iterators check this value to detect concurrent modification:
```java
final void checkForComodification() {
    if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
}
```

## 7. Internal Working

### The add() Operation

```java
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount
    elementData[size++] = e;
    return true;
}

private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;  // For fail-fast iterators
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1); // 1.5x
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

### The get() Operation

```java
public E get(int index) {
    rangeCheck(index);  // O(1) bounds check
    return elementData(index);  // O(1) array access
}

private void rangeCheck(int index) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

E elementData(int index) {
    return (E) elementData[index];  // Direct array access
}
```

### The remove() Operation

```java
public E remove(int index) {
    rangeCheck(index);
    modCount++;
    E oldValue = elementData(index);
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index, numMoved);
    elementData[--size] = null; // Help GC
    return oldValue;
}
```

## 8. JVM Perspective

### Memory Allocation

```java
List<String> list = new ArrayList<>();
// JVM allocates:
// - ArrayList object header: 12 bytes (mark word + klass pointer)
// - elementData reference: 8 bytes (pointer to backing array)
// - size field: 4 bytes
// - Padding to 8-byte boundary: 4 bytes
// Total ArrayList object: ~32 bytes

// When adding elements:
// - Backing array: 10 references × 8 bytes = 80 bytes (default capacity)
// - Each String reference in array: 8 bytes
```

### JIT Optimization

The JIT compiler optimizes ArrayList operations:
- **Inlining**: `get()` and `set()` methods are inlined for direct array access
- **Bounds check elimination**: JIT can eliminate redundant range checks
- **Escape analysis**: Small ArrayLists may be scalar-replaced
- **Loop unrolling**: Enhanced for loops over ArrayList are optimized

### Garbage Collection Impact

- Removed elements set to `null` to help GC
- Resizing creates garbage (old array)
- Large ArrayLists may be stored in Old Gen
- Weak references can be used for caching

## 9. Memory Representation

```
ArrayList<String> list = new ArrayList<>(4);
list.add("Hello");
list.add("World");
list.add("Java");

Memory layout:
┌───────────────────────────────┐
│ ArrayList object              │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ elementData ──────────────────────┐
│ size = 3 (4 bytes)            │      │
│ (padding 4 bytes)             │      │
└───────────────────────────────┘      │
                                       ▼
                               Object[] elementData
                               ┌──────────────────┐
                               │ [0] → "Hello"    │ (8 bytes ref)
                               │ [1] → "World"    │ (8 bytes ref)
                               │ [2] → "Java"     │ (8 bytes ref)
                               │ [3] → null       │ (8 bytes, unused)
                               └──────────────────┘
                               Capacity: 4, Size: 3

After adding 4th element (resize):
New capacity = 4 + (4 >> 1) = 6
Arrays.copyOf() creates new array of size 6
```

### String Objects in Memory

```
"Hello" String object (in String Pool or heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ hash (int, 4 bytes)         │
│ value reference (8 bytes) ──────→ char[] or byte[] (Java 9+)
└─────────────────────────────┘
```

## 10. Syntax

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

// ============================================
// CREATION
// ============================================
List<String> empty = new ArrayList<>();
List<String> withCapacity = new ArrayList<>(100);
List<String> fromCollection = new ArrayList<>(List.of("A", "B", "C"));
List<String> fromArray = new ArrayList<>(Arrays.asList("X", "Y", "Z"));
List<String> immutable = List.of("A", "B", "C"); // Java 9+
List<String> mutable = new ArrayList<>(List.of("A", "B", "C"));

// ============================================
// ADDING ELEMENTS
// ============================================
list.add("element");              // Append to end, returns true
list.add(0, "element");          // Insert at index
list.addAll(List.of("a", "b"));  // Add all from collection
list.addAll(0, List.of("a"));   // Add all at index

// ============================================
// ACCESSING ELEMENTS
// ============================================
String element = list.get(0);           // O(1) random access
int index = list.indexOf("element");    // O(n) search
int lastIndex = list.lastIndexOf("element"); // O(n) search from end
boolean has = list.contains("element"); // O(n) search

// ============================================
// REMOVING ELEMENTS
// ============================================
String removed = list.remove(0);        // Remove by index, O(n)
boolean success = list.remove("element"); // Remove by value, O(n)
list.removeIf(s -> s.startsWith("A")); // Conditional removal
list.clear();                           // Remove all

// ============================================
// REPLACING ELEMENTS
// ============================================
list.set(0, "new value");              // Replace at index

// ============================================
// SEARCHING
// ============================================
int idx = list.indexOf(obj);
int lastIdx = list.lastIndexOf(obj);
boolean contains = list.contains(obj);

// ============================================
// SORTING
// ============================================
Collections.sort(list);                    // Natural order
list.sort(Comparator.naturalOrder());     // Natural order
list.sort(Comparator.reverseOrder());     // Reverse order
list.sort(Comparator.comparing(String::length)); // Custom comparator

// ============================================
// SUBLIST (view, not copy)
// ============================================
List<String> sub = list.subList(0, 3);    // [0, 3)
sub.set(0, "modified"); // Modifies original!

// ============================================
// CONVERSIONS
// ============================================
String[] array = list.toArray(new String[0]);
Object[] objArray = list.toArray();

// ============================================
// IMMUTABLE VIEWS
// ============================================
List<String> unmodifiable = Collections.unmodifiableList(list);
List<String> copied = List.copyOf(list); // Truly immutable copy

// ============================================
// ITERATION
// ============================================
// Enhanced for loop
for (String s : list) {
    System.out.println(s);
}

// Iterator
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}

// ListIterator (bidirectional)
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) {
    System.out.println(lit.nextIndex() + ": " + lit.next());
}

// forEach with lambda
list.forEach(System.out::println);

// Stream
list.stream().filter(s -> s.length() > 3).forEach(System.out::println);
```

## 11. Easy Example

```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListBasics {
    public static void main(String[] args) {
        // Create and populate
        List<String> colors = new ArrayList<>();
        colors.add("Red");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Yellow");

        System.out.println("Colors: " + colors);
        System.out.println("Size: " + colors.size());

        // Access by index
        System.out.println("First: " + colors.get(0));
        System.out.println("Last: " + colors.get(colors.size() - 1));

        // Check if contains
        System.out.println("Contains Red: " + colors.contains("Red"));
        System.out.println("Index of Blue: " + colors.indexOf("Blue"));

        // Remove
        colors.remove("Yellow");
        colors.remove(0);
        System.out.println("After removal: " + colors);

        // Add at specific position
        colors.add(0, "Purple");
        System.out.println("After insert: " + colors);

        // Sort
        colors.sort(String::compareToIgnoreCase);
        System.out.println("Sorted: " + colors);

        // Iterate
        System.out.println("Iterating:");
        for (String color : colors) {
            System.out.println("  - " + color);
        }
    }
}
```

## 12. Medium Example

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Optional;

public class ArrayListOperations {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>(List.of(5, 2, 8, 1, 9, 3, 7, 4, 6));

        // Find second largest
        Optional<Integer> secondLargest = numbers.stream()
            .distinct()
            .sorted(Comparator.reverseOrder())
            .skip(1)
            .findFirst();
        System.out.println("Second largest: " + secondLargest.orElse(null));

        // Remove duplicates while maintaining order
        List<Integer> unique = new ArrayList<>();
        for (Integer num : numbers) {
            if (!unique.contains(num)) {
                unique.add(num);
            }
        }
        System.out.println("Unique (preserved order): " + unique);

        // Rotate list
        rotateLeft(numbers, 3);
        System.out.println("Rotated left by 3: " + numbers);

        // Interleave two lists
        List<String> list1 = new ArrayList<>(List.of("A", "B", "C"));
        List<String> list2 = new ArrayList<>(List.of("1", "2", "3"));
        List<String> interleaved = interleave(list1, list2);
        System.out.println("Interleaved: " + interleaved);

        // Chunk list
        List<List<Integer>> chunks = chunk(numbers, 3);
        System.out.println("Chunks: " + chunks);
    }

    static void rotateLeft(List<?> list, int positions) {
        int size = list.size();
        if (size == 0) return;
        positions = positions % size;
        @SuppressWarnings("unchecked")
        List<Object> mutable = (List<Object>) list;
        java.util.Collections.rotate(mutable, -positions);
    }

    static <T> List<T> interleave(List<T> a, List<T> b) {
        List<T> result = new ArrayList<>(a.size() + b.size());
        int i = 0, j = 0;
        while (i < a.size() && j < b.size()) {
            result.add(a.get(i++));
            result.add(b.get(j++));
        }
        while (i < a.size()) result.add(a.get(i++));
        while (j < b.size()) result.add(b.get(j++));
        return result;
    }

    static <T> List<List<T>> chunk(List<T> list, int size) {
        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            chunks.add(new ArrayList<>(list.subList(i, Math.min(i + size, list.size()))));
        }
        return chunks;
    }
}
```

## 13. Hard Example

```java
import java.util.*;
import java.util.function.Predicate;

public class AdvancedArrayList {
    public static void main(String[] args) {
        // Pattern 1: Custom ArrayList with capacity tracking
        System.out.println("=== Capacity-Aware ArrayList ===");
        TrackedArrayList<String> tracked = new TrackedArrayList<>(4);
        for (int i = 0; i < 10; i++) {
            tracked.add("Item" + i);
            System.out.printf("Added Item%d: size=%d, capacity=%d%n",
                i, tracked.size(), tracked.getCapacity());
        }

        // Pattern 2: ArrayList with batch operations
        System.out.println("\n=== Batch Operations ===");
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        batchRemove(numbers, n -> n % 2 == 0);
        System.out.println("After removing evens: " + numbers);

        // Pattern 3: ArrayList as a stack
        System.out.println("\n=== ArrayList as Stack ===");
        ArrayList<String> stack = new ArrayList<>();
        stack.push("First");
        stack.push("Second");
        stack.push("Third");
        System.out.println("Pop: " + stack.remove(stack.size() - 1));
        System.out.println("Peek: " + stack.get(stack.size() - 1));

        // Pattern 4: ArrayList as a queue (not recommended)
        System.out.println("\n=== ArrayList as Queue (bad practice) ===");
        ArrayList<String> queue = new ArrayList<>();
        queue.add("A");
        queue.add("B");
        queue.add("C");
        String dequeued = queue.remove(0); // O(n) - bad!
        System.out.println("Dequeued: " + dequeued);

        // Pattern 5: Thread-safe ArrayList operations
        System.out.println("\n=== Thread-Safe Operations ===");
        List<String> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        List<String> copyOnWrite = new java.util.concurrent.CopyOnWriteArrayList<>();

        // Pattern 6: ArrayList with subList views
        System.out.println("\n=== SubList Views ===");
        List<String> original = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        List<String> view = original.subList(1, 4);
        System.out.println("Original: " + original);
        System.out.println("View: " + view);
        view.set(0, "X");
        System.out.println("After modifying view: " + original);
    }

    static <T> void batchRemove(List<T> list, Predicate<T> predicate) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                iterator.remove();
            }
        }
    }

    static class TrackedArrayList<E> extends ArrayList<E> {
        private int capacity;

        public TrackedArrayList(int initialCapacity) {
            super(initialCapacity);
            this.capacity = initialCapacity;
        }

        @Override
        public boolean add(E e) {
            boolean result = super.add(e);
            if (size() > capacity) {
                capacity = size();
            }
            return result;
        }

        public int getCapacity() {
            return capacity;
        }
    }
}
```

## 14. Enterprise Example

```java
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InventoryManagement {
    private final List<Product> products;
    private final List<StockMovement> movements;

    public InventoryManagement() {
        this.products = new CopyOnWriteArrayList<>();
        this.movements = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
        movements.add(new StockMovement(product.sku(), "ADD", product.quantity(), new Date()));
    }

    public Optional<Product> findBySku(String sku) {
        return products.stream()
            .filter(p -> p.sku().equals(sku))
            .findFirst();
    }

    public List<Product> getLowStockProducts(int threshold) {
        return products.stream()
            .filter(p -> p.quantity() < threshold)
            .sorted(Comparator.comparingInt(Product::quantity))
            .collect(Collectors.toList());
    }

    public Map<String, List<Product>> groupByCategory() {
        return products.stream()
            .collect(Collectors.groupingBy(Product::category));
    }

    public double getTotalInventoryValue() {
        return products.stream()
            .mapToDouble(p -> p.price() * p.quantity())
            .sum();
    }

    public List<Product> search(String query) {
        String lowerQuery = query.toLowerCase();
        return products.stream()
            .filter(p -> p.name().toLowerCase().contains(lowerQuery) ||
                         p.sku().toLowerCase().contains(lowerQuery))
            .collect(Collectors.toList());
    }

    public void updateStock(String sku, int quantityChange) {
        products.replaceAll(p -> {
            if (p.sku().equals(sku)) {
                int newQuantity = p.quantity() + quantityChange;
                movements.add(new StockMovement(sku, "UPDATE", quantityChange, new Date()));
                return new Product(p.sku(), p.name(), p.category(), p.price(), newQuantity);
            }
            return p;
        });
    }

    public List<StockMovement> getRecentMovements(int count) {
        int size = movements.size();
        return movements.subList(Math.max(0, size - count), size);
    }

    public static void main(String[] args) {
        InventoryManagement inventory = new InventoryManagement();

        inventory.addProduct(new Product("SKU001", "Laptop", "Electronics", 999.99, 50));
        inventory.addProduct(new Product("SKU002", "Mouse", "Electronics", 29.99, 200));
        inventory.addProduct(new Product("SKU003", "Desk", "Furniture", 199.99, 30));
        inventory.addProduct(new Product("SKU004", "Chair", "Furniture", 149.99, 45));
        inventory.addProduct(new Product("SKU005", "Monitor", "Electronics", 399.99, 5));

        System.out.println("=== Low Stock Products ===");
        inventory.getLowStockProducts(10).forEach(p ->
            System.out.printf("  %s: %d units%n", p.name(), p.quantity())
        );

        System.out.println("\n=== Products by Category ===");
        inventory.groupByCategory().forEach((category, prods) -> {
            System.out.println("  " + category + ":");
            prods.forEach(p -> System.out.println("    " + p.name()));
        });

        System.out.printf("%nTotal Inventory Value: $%.2f%n",
            inventory.getTotalInventoryValue());

        System.out.println("\n=== Search Results ===");
        inventory.search("monitor").forEach(p ->
            System.out.println("  " + p.name())
        );

        System.out.println("\n=== Recent Movements ===");
        inventory.getRecentMovements(3).forEach(m ->
            System.out.printf("  %s: %s %d units%n", m.sku(), m.type(), m.quantity())
        );
    }

    record Product(String sku, String name, String category, double price, int quantity) {}
    record StockMovement(String sku, String type, int quantity, Date timestamp) {}
}
```

## 15. Performance

### Time Complexity

| Operation | Time | Notes |
|-----------|------|-------|
| add(E) | O(1)* | Amortized, O(n) when resizing |
| add(int, E) | O(n) | Shifts elements right |
| get(int) | O(1) | Direct array access |
| set(int, E) | O(1) | Direct array access |
| remove(int) | O(n) | Shifts elements left |
| remove(Object) | O(n) | Search + shift |
| contains(Object) | O(n) | Linear search |
| indexOf(Object) | O(n) | Linear search |
| size() | O(1) | Field access |
| isEmpty() | O(1) | Field access |
| iterator() | O(1) | Creates iterator object |
| subList() | O(1) | Creates view |

*Amortized O(1) due to occasional O(n) resize

### ArrayList vs LinkedList

| Operation | ArrayList | LinkedList | Winner |
|-----------|-----------|------------|--------|
| get(index) | O(1) | O(n) | ArrayList |
| add(end) | O(1)* | O(1) | Tie |
| add(beginning) | O(n) | O(1) | LinkedList |
| add(middle) | O(n) | O(1)** | LinkedList |
| remove(end) | O(1) | O(1) | Tie |
| remove(beginning) | O(n) | O(1) | LinkedList |
| remove(middle) | O(n) | O(1)** | LinkedList |
| contains() | O(n) | O(n) | Tie |
| iteration | O(n) | O(n) | ArrayList (cache) |
| memory | Less | More | ArrayList |

**Assuming you already have a reference to the node

### Space Efficiency

ArrayList: 4 bytes per element (reference only)
LinkedList: 24 bytes per element (item + next + prev + object header)

For 1 million elements:
- ArrayList: ~4 MB
- LinkedList: ~24 MB

### Benchmark Example

```java
import java.util.*;

public class ArrayListBenchmark {
    public static void main(String[] args) {
        int[] sizes = {1000, 10000, 100000, 1000000};

        for (int size : sizes) {
            System.out.println("\n=== Size: " + size + " ===");

            // ArrayList
            List<Integer> arrayList = new ArrayList<>();
            long start = System.nanoTime();
            for (int i = 0; i < size; i++) {
                arrayList.add(i);
            }
            long addTime = System.nanoTime() - start;

            start = System.nanoTime();
            for (int i = 0; i < size; i++) {
                arrayList.get(i);
            }
            long getTime = System.nanoTime() - start;

            System.out.printf("ArrayList: add=%d ms, get=%d ms%n",
                addTime / 1_000_000, getTime / 1_000_000);

            // LinkedList
            List<Integer> linkedList = new LinkedList<>();
            start = System.nanoTime();
            for (int i = 0; i < size; i++) {
                linkedList.add(i);
            }
            addTime = System.nanoTime() - start;

            start = System.nanoTime();
            for (int i = 0; i < size; i++) {
                linkedList.get(i);
            }
            getTime = System.nanoTime() - start;

            System.out.printf("LinkedList: add=%d ms, get=%d ms%n",
                addTime / 1_000_000, getTime / 1_000_000);
        }
    }
}
```

## 16. Best Practices

1. **Set initial capacity**: If you know the approximate size, specify it to avoid resizing
   ```java
   List<String> list = new ArrayList<>(expectedSize);
   ```

2. **Use trimmedToSize() pattern**: After bulk operations, create a trimmed copy
   ```java
   list = new ArrayList<>(list); // Trims to actual size
   ```

3. **Use removeIf()**: More efficient than iterator-based removal
   ```java
   list.removeIf(s -> s.isEmpty());
   ```

4. **Prefer subList for views**: Don't create copies for range operations
   ```java
   List<String> sub = list.subList(0, Math.min(10, list.size()));
   ```

5. **Use List.of() for immutable lists**: When data doesn't change
   ```java
   List<String> immutable = List.of("A", "B", "C");
   ```

6. **Thread safety**: Use `Collections.synchronizedList()` or `CopyOnWriteArrayList` for concurrent access

7. **Avoid ArrayList as queue**: Use `ArrayDeque` for FIFO operations

8. **Use enhanced for loop**: Cleaner than index-based loops when you don't need the index

## 17. Common Mistakes

```java
// Mistake 1: Not setting initial capacity for large lists
// Bad - causes multiple resizes
List<String> list = new ArrayList<>();
for (int i = 0; i < 100000; i++) {
    list.add("item" + i);
}

// Good - single allocation
List<String> list = new ArrayList<>(100000);
for (int i = 0; i < 100000; i++) {
    list.add("item" + i);
}

// Mistake 2: Removing during enhanced for loop
for (String s : list) {
    if (s.isEmpty()) {
        list.remove(s); // ConcurrentModificationException
    }
}

// Mistake 3: Using get() in a loop for LinkedList
// Bad - O(n^2) for LinkedList
for (int i = 0; i < linkedList.size(); i++) {
    process(linkedList.get(i)); // Each get is O(n)
}

// Mistake 4: Confusing subList view with copy
List<String> original = new ArrayList<>(List.of("A", "B", "C"));
List<String> sub = original.subList(0, 2);
sub.clear(); // Also clears original!

// Mistake 5: Using ArrayList for frequent insertions at beginning
// Use LinkedList or ArrayDeque instead
```

## 18. Pitfalls

### Resizing Overhead
- First 30 adds to an empty ArrayList: ~15 array copies
- Solution: Set initial capacity for known sizes

### ConcurrentModificationException
- Modifying list during enhanced for loop
- Solution: Use Iterator.remove() or removeIf()

### SubList is a View, Not a Copy
- Changes to subList affect the original list
- Original list changes affect the subList
- Solution: Create a copy if you need independence

### Thread Safety
- ArrayList is NOT thread-safe
- Concurrent access can cause data corruption or ConcurrentModificationException
- Solution: Use Collections.synchronizedList() or CopyOnWriteArrayList

### Null Elements
- ArrayList allows null elements
- This can cause NullPointerException in stream operations
- Solution: Filter nulls before processing

## 19. Debugging Tips

1. **Print size and capacity**: Override or extend ArrayList to track capacity
2. **Use debugger**: Inspect elementData array directly in IDE
3. **Check for nulls**: Use `list.contains(null)` to detect null elements
4. **Monitor modCount**: If getting ConcurrentModificationException, track modification count
5. **Use assertions**: Verify invariants like `assert list.size() >= 0`
6. **Profile memory**: Use JProfiler or VisualVM to check ArrayList memory usage
7. **Log operations**: Add logging around add/remove operations for debugging

## 20. Comparison Table

| Feature | ArrayList | LinkedList | Vector |
|---------|-----------|------------|--------|
| Backing structure | Dynamic array | Doubly-linked list | Dynamic array |
| Random access | O(1) | O(n) | O(1) |
| Add/remove end | O(1)* | O(1) | O(1)* |
| Add/remove begin | O(n) | O(1) | O(n) |
| Thread-safe | No | No | Yes (synchronized) |
| Memory | Less | More | More |
| Cache locality | Good | Poor | Good |
| Best for | Random access | Frequent insertion/removal | Legacy code |

## 21. Decision Tree

```
Need a List?
├── Yes → Need random access by index?
│   ├── Yes → ArrayList (default choice)
│   └── No → Need frequent insertions at beginning?
│       ├── Yes → LinkedList
│       └── No → ArrayList (still usually better)
├── No → Need thread safety?
│   ├── Yes → CopyOnWriteArrayList or synchronizedList
│   └── No → ArrayList
└── Need a queue/deque?
    └── Use ArrayDeque, not ArrayList
```

## 22. Interview Questions

### Q1: How does ArrayList resize itself?
**A**: When the backing array is full, a new array is created with 1.5x the capacity (oldCapacity + oldCapacity >> 1). All elements are copied using Arrays.copyOf(). The old array becomes garbage.

### Q2: What is the time complexity of ArrayList operations?
**A**: get/set: O(1), add(end): O(1) amortized, add(index): O(n), remove: O(n), contains: O(n).

### Q3: Why is ArrayList preferred over LinkedList?
**A**: Better cache locality (contiguous memory), O(1) random access, less memory overhead (no node pointers), and JIT optimizations. LinkedList only wins for frequent insertions at the beginning.

### Q4: What happens when you remove an element from ArrayList?
**A**: Elements after the removed index are shifted left using System.arraycopy(). The last slot is set to null for GC. This is O(n) for the shift operation.

### Q5: How do you avoid ConcurrentModificationException?
**A**: Use Iterator.remove(), removeIf(), or create a copy before modifying. Don't modify the list during enhanced for loop.

### Q6: What is the difference between ArrayList and Vector?
**A**: ArrayList is not synchronized (faster in single-threaded), grows by 1.5x. Vector is synchronized (slower), grows by 2x. Use ArrayList in modern code.

### Q7: When would you use LinkedList over ArrayList?
**A**: Rarely. LinkedList is better when you have a Deque/Queue API requirement, or when you're doing many insertions/deletions at the beginning and you have a ListIterator. For most use cases, ArrayList is superior.

## 23. Exercises

### Exercise 1: Find Second Largest
```java
public static Integer findSecondLargest(List<Integer> list) {
    // Your code here
    // Use Stream API or manual iteration
    // Handle edge cases (empty list, single element, all same)
}
```

### Exercise 2: Rotate List
```java
public static <T> void rotate(List<T> list, int positions) {
    // Rotate list to the right by given positions
    // Example: [1,2,3,4,5] rotated by 2 → [4,5,1,2,3]
}
```

### Exercise 3: Remove Duplicates
```java
public static <T> List<T> removeDuplicates(List<T> list) {
    // Remove duplicates while preserving order
    // Don't use Set (which doesn't preserve order)
}
```

### Exercise 4: Chunk List
```java
public static <T> List<List<T>> chunk(List<T> list, int chunkSize) {
    // Split list into chunks of specified size
    // Example: [1,2,3,4,5] chunked by 2 → [[1,2], [3,4], [5]]
}
```

## 24. Assignments

### Assignment 1: Dynamic Array Implementation
Implement your own dynamic array class (like ArrayList) with:
- Dynamic resizing (2x growth)
- add(), get(), set(), remove() operations
- trimToSize() method
- Iterator implementation

### Assignment 2: ArrayList Performance Analysis
Write a benchmark comparing:
- ArrayList vs LinkedList for random access
- ArrayList with different initial capacities
- ArrayList vs LinkedList for insertions at different positions

### Assignment 3: Task List Manager
Build a task list application using ArrayList:
- Add, remove, mark complete, search tasks
- Sort by priority, due date, or name
- Export to file, import from file
- Undo/redo functionality

## 25. Mini Project

### File-Based Contact Manager

Build a contact management system using ArrayList:

```java
// Features:
// 1. Store contacts in ArrayList<Contact>
// 2. Add/remove/edit contacts
// 3. Search by name, phone, or email
// 4. Sort by name, phone, or creation date
// 5. Export to CSV, import from CSV
// 6. Duplicate detection
// 7. Recent contacts (last 10 accessed)
```

**Requirements:**
- Use ArrayList for main storage
- Implement Comparable for Contact sorting
- Use subList for pagination
- Handle file I/O with proper error handling
- Thread-safe for concurrent access

## 26. Summary

ArrayList is the most commonly used List implementation in Java:

- **Internal structure**: Dynamic array with automatic resizing
- **Performance**: O(1) random access, O(1) amortized add at end, O(n) for insertions/removals
- **Memory**: Efficient (4 bytes per element reference)
- **Best for**: Random access, iteration, most general-purpose list operations
- **Avoid for**: Frequent insertions at beginning (use LinkedList), thread safety (use CopyOnWriteArrayList)
- **Key optimization**: Set initial capacity for known sizes to avoid resizing

## 27. References

### Official Documentation
- [ArrayList JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)
- [List Interface](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)

### Books
- *Effective Java* by Joshua Bloch (Item 15-17)
- *Java Performance* by Scott Oaks

### Online Resources
- [Baeldung ArrayList Guide](https://www.baeldung.com/java-arraylist)
- [GeeksforGeeks ArrayList](https://www.geeksforgeeks.org/arraylist-in-java/)
- [OpenJDK ArrayList Source](https://hg.openjdk.java.net/jdk8/jdk8/jdk/file/tip/src/share/classes/java/util/ArrayList.java)

### Related Topics
- [LinkedList](../04-linkedlist/README.md)
- [Vector](../05-vector/README.md)
- [Iterator](../24-iterator/README.md)
