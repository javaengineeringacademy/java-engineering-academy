# Fail-Fast vs Fail-Safe Iterators in Java Collections Framework

## 1. Introduction

Fail-fast and fail-safe iterators describe how collections handle concurrent modification during iteration. Fail-fast iterators immediately throw `ConcurrentModificationException` if the collection is modified structurally during iteration. Fail-safe iterators work on a copy of the collection, allowing modifications without exceptions.

```java
// Fail-fast: ArrayList
List<String> list = new ArrayList<>();
Iterator<String> iterator = list.iterator();
list.add("new");  // Structural modification
iterator.next();  // Throws ConcurrentModificationException

// Fail-safe: CopyOnWriteArrayList
List<String> concurrentList = new CopyOnWriteArrayList<>();
Iterator<String> safeIterator = concurrentList.iterator();
concurrentList.add("new");  // No exception
safeIterator.next();  // Works fine
```

## 2. Learning Objectives

- Understand the difference between fail-fast and fail-safe iterators
- Learn about `ConcurrentModificationException` and its causes
- Identify which collections use which iterator type
- Understand when to use each type
- Recognize thread safety implications

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of Iterator interface
- Familiarity with collections (List, Set, Map)
- Basic multithreading concepts (recommended)

## 4. Why This Concept Exists

Concurrent modification is a common problem in multithreaded applications. When one thread modifies a collection while another is iterating over it, data corruption can occur. Fail-fast iterators detect this and throw an exception immediately, while fail-safe iterators provide thread-safe iteration at the cost of consistency.

## 5. Problem Statement

In multithreaded environments, simultaneous modification and iteration of collections can lead to:
- **Data corruption**: Inconsistent state during iteration
- **ConcurrentModificationException**: Runtime errors
- **Undefined behavior**: Skipping or duplicate elements
- **Memory consistency issues**: Threads see different states

Understanding fail-fast vs fail-safe helps choose the right collection for the use case.

## 6. Theory

### Fail-Fast Iterators
- **Behavior**: Throw `ConcurrentModificationException` immediately on structural modification
- **Mechanism**: Use `modCount` field to detect modifications
- **Collections**: `ArrayList`, `LinkedList`, `HashMap`, `HashSet`, `TreeMap`, `TreeSet`
- **Thread safety**: Not thread-safe
- **Performance**: Low overhead

### Fail-Safe Iterators
- **Behavior**: Do not throw exceptions; iterate over a copy or snapshot
- **Mechanism**: Create defensive copies or use concurrent data structures
- **Collections**: `CopyOnWriteArrayList`, `CopyOnWriteArraySet`, `ConcurrentHashMap`, `ConcurrentLinkedQueue`
- **Thread safe**: Thread-safe
- **Performance**: Higher memory usage, lower performance for writes

### Structural Modification
- **Definition**: Changes that affect the number of elements or internal structure
- **Examples**: `add()`, `remove()`, `clear()`, `set()` (for some collections)
- **Non-structural**: `Iterator.remove()` is allowed in fail-fast iterators

## 7. Internal Working

### Fail-Fast Mechanism (modCount)
```java
// Simplified ArrayList implementation
public class ArrayList<E> {
    private int modCount = 0;  // Modification counter
    
    public boolean add(E e) {
        modCount++;  // Increment on structural modification
        // Add element
        return true;
    }
    
    public Iterator<E> iterator() {
        return new Itr();
    }
    
    private class Itr implements Iterator<E> {
        int expectedModCount = modCount;  // Capture at creation
        
        public E next() {
            checkForComodification();  // Check before each operation
            // Return next element
        }
        
        public void remove() {
            checkForComodification();
            // Remove element
            modCount = expectedModCount;  // Update expected count
        }
        
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
}
```

### Fail-Safe Mechanism (CopyOnWriteArrayList)
```java
// Simplified CopyOnWriteArrayList implementation
public class CopyOnWriteArrayList<E> {
    private transient volatile Object[] array;
    
    public Iterator<E> iterator() {
        return new COWIterator(array, 0);  // Snapshot of array
    }
    
    private static class COWIterator<E> implements Iterator<E> {
        private final Object[] snapshot;  // Copy of array
        private int cursor;
        
        COWIterator(Object[] elements, int initialCursor) {
            snapshot = elements;  // Store reference to snapshot
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
    
    public boolean add(E e) {
        synchronized (this) {
            Object[] elements = array;
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len + 1);
            newElements[len] = e;
            array = newElements;  // Replace array (atomic)
            return true;
        }
    }
}
```

### ConcurrentHashMap Mechanism
```java
// Simplified ConcurrentHashMap iteration
public class ConcurrentHashMap<K, V> {
    // Uses segments for partial locking
    // Iterator traverses without locking entire map
    // May see partially updated state (weakly consistent)
    
    public Iterator<Map.Entry<K, V>> iterator() {
        return new Traverser(table, 0, table.length, 0, table.length);
    }
    
    // Traverser reads without locking
    // May miss some updates during iteration
    // Never throws ConcurrentModificationException
}
```

## 8. Syntax

```java
// Import
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.Map;

// Fail-fast iterator (ArrayList)
List<String> arrayList = new ArrayList<>();
Iterator<String> failFastIterator = arrayList.iterator();
arrayList.add("new");  // Structural modification
failFastIterator.next();  // Throws ConcurrentModificationException

// Fail-safe iterator (CopyOnWriteArrayList)
List<String> cowList = new CopyOnWriteArrayList<>();
Iterator<String> failSafeIterator = cowList.iterator();
cowList.add("new");  // No exception
failSafeIterator.next();  // Works fine

// Fail-fast iterator (HashMap)
Map<String, Integer> hashMap = new HashMap<>();
Iterator<Map.Entry<String, Integer>> mapIterator = hashMap.entrySet().iterator();
hashMap.put("key", 1);  // Structural modification
mapIterator.next();  // Throws ConcurrentModificationException

// Fail-safe iterator (ConcurrentHashMap)
Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
Iterator<Map.Entry<String, Integer>> concurrentIterator = concurrentMap.entrySet().iterator();
concurrentMap.put("key", 1);  // No exception
concurrentIterator.next();  // Works fine
```

## 9. Easy Example

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class FailFastFailSafeBasic {
    public static void main(String[] args) {
        // Fail-fast example
        System.out.println("=== Fail-Fast Example ===");
        List<String> failFastList = new ArrayList<>();
        failFastList.add("A");
        failFastList.add("B");
        failFastList.add("C");
        
        Iterator<String> failFastIterator = failFastList.iterator();
        
        try {
            while (failFastIterator.hasNext()) {
                String element = failFastIterator.next();
                System.out.println("Element: " + element);
                
                if (element.equals("B")) {
                    failFastList.add("D");  // Structural modification
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
        }
        
        // Fail-safe example
        System.out.println("
=== Fail-Safe Example ===");
        List<String> failSafeList = new CopyOnWriteArrayList<>();
        failSafeList.add("A");
        failSafeList.add("B");
        failSafeList.add("C");
        
        Iterator<String> failSafeIterator = failSafeList.iterator();
        
        while (failSafeIterator.hasNext()) {
            String element = failSafeIterator.next();
            System.out.println("Element: " + element);
            
            if (element.equals("B")) {
                failSafeList.add("D");  // No exception
                System.out.println("Added D during iteration");
            }
        }
        
        System.out.println("Final list: " + failSafeList);
    }
}
```

## 10. Medium Example

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class MapIterationComparison {
    public static void main(String[] args) {
        // Fail-fast HashMap
        System.out.println("=== Fail-Fast HashMap ===");
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("Alice", 25);
        hashMap.put("Bob", 30);
        hashMap.put("Charlie", 35);
        
        Iterator<Map.Entry<String, Integer>> hashMapIterator = hashMap.entrySet().iterator();
        
        try {
            while (hashMapIterator.hasNext()) {
                Map.Entry<String, Integer> entry = hashMapIterator.next();
                System.out.println(entry.getKey() + ": " + entry.getValue());
                
                if (entry.getKey().equals("Bob")) {
                    hashMap.put("David", 40);  // Structural modification
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getSimpleName());
        }
        
        // Fail-safe ConcurrentHashMap
        System.out.println("
=== Fail-Safe ConcurrentHashMap ===");
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        concurrentMap.put("Alice", 25);
        concurrentMap.put("Bob", 30);
        concurrentMap.put("Charlie", 35);
        
        Iterator<Map.Entry<String, Integer>> concurrentMapIterator = concurrentMap.entrySet().iterator();
        
        while (concurrentMapIterator.hasNext()) {
            Map.Entry<String, Integer> entry = concurrentMapIterator.next();
            System.out.println(entry.getKey() + ": " + entry.getValue());
            
            if (entry.getKey().equals("Bob")) {
                concurrentMap.put("David", 40);  // No exception
                System.out.println("Added David during iteration");
            }
        }
        
        System.out.println("Final map: " + concurrentMap);
    }
}
```

## 11. Hard Example

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentModificationDemo {
    private final List<String> failFastList;
    private final List<String> failSafeList;
    private final AtomicInteger failFastCount;
    private final AtomicInteger failSafeCount;
    
    public ConcurrentModificationDemo() {
        this.failFastList = new ArrayList<>();
        this.failSafeList = new CopyOnWriteArrayList<>();
        this.failFastCount = new AtomicInteger(0);
        this.failSafeCount = new AtomicInteger(0);
    }
    
    public void demonstrateFailFast() {
        System.out.println("=== Fail-Fast Demonstration ===");
        
        // Initialize list
        for (int i = 0; i < 5; i++) {
            failFastList.add("Item" + i);
        }
        
        // Start iterator in one thread
        Thread iteratorThread = new Thread(() -> {
            try {
                Iterator<String> iterator = failFastList.iterator();
                while (iterator.hasNext()) {
                    String item = iterator.next();
                    System.out.println("Fail-Fast: " + item);
                    Thread.sleep(10);
                    failFastCount.incrementAndGet();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.out.println("Fail-Fast Exception: " + e.getClass().getSimpleName());
            }
        });
        
        // Start modifier in another thread
        Thread modifierThread = new Thread(() -> {
            try {
                Thread.sleep(20);  // Let iterator start
                failFastList.add("NewItem");
                System.out.println("Added new item to fail-fast list");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        iteratorThread.start();
        modifierThread.start();
        
        try {
            iteratorThread.join();
            modifierThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Fail-Fast items processed: " + failFastCount.get());
    }
    
    public void demonstrateFailSafe() {
        System.out.println("
=== Fail-Safe Demonstration ===");
        
        // Initialize list
        for (int i = 0; i < 5; i++) {
            failSafeList.add("Item" + i);
        }

## 📑 Continue Reading

**Part 1** of 3 | [Part 2](README-part2.md) | [Part 3](README-part3.md)

