# Iterator in Java Collections Framework

## 1. Introduction

The `Iterator` interface provides a standard way to traverse elements in a collection one at a time without exposing its underlying structure. It is a fundamental part of the Java Collections Framework and implements the Iterator design pattern. `Iterator` allows uniform traversal of all collection types.

```java
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String element = iterator.next();
    System.out.println(element);
}
```

## 2. Learning Objectives

- Understand the `Iterator` interface and its methods
- Learn about `ListIterator` for bidirectional traversal
- Understand the `Iterable` interface and for-each loop
- Recognize the Iterator pattern as an OOP design pattern
- Learn about fail-safe iterators and removing elements while iterating

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of collections (List, Set, Map)
- Familiarity with interfaces and generics
- Knowledge of design patterns (recommended)

## 4. Why This Concept Exists

The Iterator pattern was created to solve the problem of traversing different collection types without knowing their internal structure. Before iterators, code needed to be written differently for arrays, linked lists, and other structures. Iterators provide a uniform interface, promoting code reuse and separation of concerns.

## 5. Problem Statement

Different collection types (ArrayList, LinkedList, HashSet, etc.) have different internal structures. Without a common traversal mechanism, developers would need to write separate code for each collection type. The Iterator pattern provides a unified way to traverse any collection, regardless of its implementation.

## 6. Theory

### Iterator Interface Methods
- `boolean hasNext()`: Returns true if more elements exist
- `E next()`: Returns the next element and advances the iterator
- `default void remove()`: Removes the last element returned by `next()`
- `default void forEachRemaining(Consumer<? super E> action)`: Performs action for each remaining element

### ListIterator Interface
Extends `Iterator` with additional methods for bidirectional traversal:
- `boolean hasPrevious()`: Returns true if previous element exists
- `E previous()`: Returns the previous element
- `int nextIndex()`: Returns index of next element
- `int previousIndex()`: Returns index of previous element
- `void set(E e)`: Replaces last element with specified element
- `void add(E e)`: Inserts element at current position

### Iterable Interface
- `Iterator<T> iterator()`: Returns an iterator over elements
- Enables for-each loop syntax
- Implemented by all Collection classes

### Iterator Pattern (OOP Design Pattern)
- **Intent**: Provide a way to access elements of a collection sequentially without exposing underlying representation
- **Participants**: Iterator (interface), ConcreteIterator, Aggregate (collection), ConcreteAggregate
- **Benefits**: Single responsibility, open/closed principle, multiple traversals

## 7. Internal Working

### Iterator Implementation in ArrayList
```java
// Simplified ArrayList iterator
private class Itr implements Iterator<E> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned
    
    public boolean hasNext() {
        return cursor != size;
    }
    
    public E next() {
        checkForComodification();
        int i = cursor;
        if (i >= size)
            throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData;
        if (i >= elementData.length)
            throw new ConcurrentModificationException();
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }
    
    public void remove() {
        if (lastRet < 0)
            throw new IllegalStateException();
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
}
```

### ListIterator Implementation in LinkedList
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
        if (!hasNext())
            throw new NoSuchElementException();
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
        if (!hasPrevious())
            throw new NoSuchElementException();
        lastReturned = next = (next == null) ? last : next.prev;
        nextIndex--;
        return lastReturned.item;
    }
}
```

## 8. Syntax

```java
// Import
import java.util.Iterator;
import java.util.ListIterator;

// Creating Iterator
Iterator<E> iterator = collection.iterator();
ListIterator<E> listIterator = list.listIterator();
ListIterator<E> listIterator = list.listIterator(int index);

// Iterator methods
boolean hasNext = iterator.hasNext();
E element = iterator.next();
iterator.remove();
iterator.forEachRemaining(element -> System.out.println(element));

// ListIterator methods
boolean hasPrevious = listIterator.hasPrevious();
E previous = listIterator.previous();
int nextIndex = listIterator.nextIndex();
int previousIndex = listIterator.previousIndex();
listIterator.set(element);
listIterator.add(element);

// For-each loop (uses Iterator internally)
for (E element : collection) {
    System.out.println(element);
}
```

## 9. Easy Example

```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorBasic {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        names.add("Diana");
        
        // Using Iterator
        Iterator<String> iterator = names.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            System.out.println(name);
        }
        
        // Using for-each loop
        for (String name : names) {
            System.out.println(name);
        }
        
        // Removing elements with Iterator
        Iterator<String> removeIterator = names.iterator();
        while (removeIterator.hasNext()) {
            String name = removeIterator.next();
            if (name.startsWith("B")) {
                removeIterator.remove();
            }
        }
        
        System.out.println("After removal: " + names);
    }
}
```

## 10. Medium Example

```java
import java.util.LinkedList;
import java.util.ListIterator;

public class BidirectionalTraversal {
    public static void main(String[] args) {
        LinkedList<Integer> numbers = new LinkedList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        
        // Forward traversal
        System.out.println("Forward:");
        ListIterator<Integer> forwardIterator = numbers.listIterator();
        while (forwardIterator.hasNext()) {
            int number = forwardIterator.next();
            System.out.print(number + " ");
        }
        System.out.println();
        
        // Backward traversal
        System.out.println("Backward:");
        ListIterator<Integer> backwardIterator = numbers.listIterator(numbers.size());
        while (backwardIterator.hasPrevious()) {
            int number = backwardIterator.previous();
            System.out.print(number + " ");
        }
        System.out.println();
        
        // Adding elements while iterating
        ListIterator<Integer> addIterator = numbers.listIterator();
        while (addIterator.hasNext()) {
            int number = addIterator.next();
            if (number == 3) {
                addIterator.add(25); // Add 25 before 3
            }
        }
        
        System.out.println("After adding: " + numbers);
        
        // Replacing elements
        ListIterator<Integer> replaceIterator = numbers.listIterator();
        while (replaceIterator.hasNext()) {
            int number = replaceIterator.next();
            if (number % 2 == 0) {
                replaceIterator.set(number * 10); // Replace even numbers
            }
        }
        
        System.out.println("After replacing: " + numbers);
    }
}
```

## 11. Hard Example

```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentModificationException;

public class SafeIterator {
    private final List<String> elements;
    private int expectedModCount;
    
    public SafeIterator(List<String> elements) {
        this.elements = elements;
        this.expectedModCount = elements.size();
    }
    
    // Thread-safe iterator with copy-on-write semantics
    public Iterator<String> safeIterator() {
        // Create a snapshot for safe iteration
        List<String> snapshot = new ArrayList<>(elements);
        return snapshot.iterator();
    }
    
    // Iterator with modification detection
    public Iterator<String> modCountIterator() {
        return new Iterator<String>() {
            private int currentModCount = expectedModCount;
            private int index = 0;
            
            @Override
            public boolean hasNext() {
                checkModification();
                return index < elements.size();
            }
            
            @Override
            public String next() {
                checkModification();
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                return elements.get(index++);
            }
            
            @Override
            public void remove() {
                checkModification();
                if (index <= 0) {
                    throw new IllegalStateException();
                }
                elements.remove(--index);
                currentModCount = expectedModCount;
            }
            
            private void checkModification() {
                if (currentModCount != expectedModCount) {
                    throw new ConcurrentModificationException(
                        "Collection modified during iteration"
                    );
                }
            }
        };
    }
    
    public void add(String element) {
        elements.add(element);
        expectedModCount++;
    }
    
    public void remove(String element) {
        elements.remove(element);
        expectedModCount++;
    }
    
    public static void main(String[] args) {
        List<String> sharedList = new ArrayList<>();
        sharedList.add("A");
        sharedList.add("B");
        sharedList.add("C");
        
        SafeIterator safeIterator = new SafeIterator(sharedList);
        
        // Simulate concurrent modification
        Thread iteratorThread = new Thread(() -> {
            Iterator<String> iterator = safeIterator.safeIterator();
            while (iterator.hasNext()) {
                try {
                    String element = iterator.next();
                    System.out.println("Iterating: " + element);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        Thread modifierThread = new Thread(() -> {
            try {
                Thread.sleep(50);
                safeIterator.add("D");
                System.out.println("Added D");
                Thread.sleep(50);
                safeIterator.remove("B");
                System.out.println("Removed B");
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
        
        System.out.println("Final list: " + sharedList);
    }
}
```

## 12. Enterprise Example

```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DatabaseMigrationService {
    private final List<String> pendingMigrations;
    private final List<String> completedMigrations;
    private final List<String> failedMigrations;
    
    public DatabaseMigrationService() {
        this.pendingMigrations = new CopyOnWriteArrayList<>();
        this.completedMigrations = new ArrayList<>();
        this.failedMigrations = new ArrayList<>();
    }
    
    public void addMigration(String migrationScript) {
        pendingMigrations.add(migrationScript);
    }
    
    public void executeMigrations() {
        System.out.println("Starting database migrations...");
        
        // Use safe iterator for concurrent modifications
        Iterator<String> migrationIterator = pendingMigrations.iterator();
        
        while (migrationIterator.hasNext()) {
            String migration = migrationIterator.next();
            
            try {
                System.out.println("Executing: " + migration);
                boolean success = executeMigration(migration);
                
                if (success) {
                    completedMigrations.add(migration);
                    migrationIterator.remove();
                    System.out.println("Completed: " + migration);
                } else {
                    failedMigrations.add(migration);
                    System.out.println("Failed: " + migration);
                }
            } catch (Exception e) {
                failedMigrations.add(migration);
                System.out.println("Error executing " + migration + ": " + e.getMessage());
            }
        }
        
        System.out.println("\nMigration Summary:");
        System.out.println("Completed: " + completedMigrations.size());
        System.out.println("Failed: " + failedMigrations.size());
        System.out.println("Pending: " + pendingMigrations.size());
    }
    
    private boolean executeMigration(String migration) {
        // Simulate migration execution
        try {
            Thread.sleep(100);
            return Math.random() > 0.2; // 80% success rate
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    public List<String> getFailedMigrations() {
        return new ArrayList<>(failedMigrations);
    }
    
    public void retryFailedMigrations() {
        System.out.println("\nRetrying failed migrations...");
        
        // Create a copy to avoid ConcurrentModificationException
        List<String> failedCopy = new ArrayList<>(failedMigrations);
        failedMigrations.clear();
        
        for (String migration : failedCopy) {
            boolean success = executeMigration(migration);
            if (success) {
                completedMigrations.add(migration);
                System.out.println("Retry successful: " + migration);
            } else {
                failedMigrations.add(migration);
                System.out.println("Retry failed: " + migration);
            }
        }
    }
    
    public static void main(String[] args) {
        DatabaseMigrationService service = new DatabaseMigrationService();
        
        // Add migration scripts
        service.addMigration("001_create_users_table.sql");
        service.addMigration("002_add_email_column.sql");
        service.addMigration("003_create_orders_table.sql");
        service.addMigration("004_add_indexes.sql");
        service.addMigration("005_create_audit_log.sql");
        
        // Execute migrations
        service.executeMigrations();
        
        // Retry failed ones
        service.retryFailedMigrations();
    }
}
```

## 13. Performance

### Time Complexity
- **hasNext()**: O(1)
- **next()**: O(1) for ArrayList/LinkedList, O(1) amortized for others
- **remove()**: O(1) for ArrayList (amortized), O(1) for LinkedList
- **forEachRemaining()**: O(n) for remaining elements

### Memory Usage
- **Iterator**: Minimal overhead, stores only position information
- **ListIterator**: Slightly more overhead for bidirectional traversal
- **CopyOnWriteArrayList Iterator**: O(n) memory for snapshot

### Comparison
| Operation | ArrayList | LinkedList | CopyOnWriteArrayList |
|-----------|-----------|------------|---------------------|
| Iterator creation | O(1) | O(1) | O(n) |
| next() | O(1) | O(1) | O(1) |
| remove() | O(n) | O(1) | O(n) |
| Thread safety | No | No | Yes (snapshot) |
| Memory | Low | Medium | High (snapshot) |

## 14. Best Practices

```java
// 1. Use for-each loop when possible (cleaner syntax)
for (String element : list) {
    System.out.println(element);
}

// 2. Use Iterator.remove() for safe removal during iteration
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String element = iterator.next();
    if (condition) {
        iterator.remove();  // Safe
    }
}

// 3. Use ListIterator for bidirectional traversal
ListIterator<Integer> listIterator = list.listIterator(list.size());
while (listIterator.hasPrevious()) {
    int number = listIterator.previous();
    // Process in reverse
}

// 4. Use CopyOnWriteArrayList for concurrent iteration
CopyOnWriteArrayList<String> concurrentList = new CopyOnWriteArrayList<>();
Iterator<String> safeIterator = concurrentList.iterator();

// 5. Avoid modifying collection directly during iteration
// Bad
for (String element : list) {
    if (condition) {
        list.remove(element);  // ConcurrentModificationException
    }
}

// 6. Use forEachRemaining for batch processing
iterator.forEachRemaining(element -> processElement(element));

// 7. Check hasNext() before next() to avoid NoSuchElementException
if (iterator.hasNext()) {
    String element = iterator.next();
}
```

## 15. Common Mistakes

```java
// Mistake 1: Modifying collection during iteration
// Bad
for (String element : list) {
    if (element.equals("remove")) {
        list.remove(element);  // ConcurrentModificationException
    }
}
// Good
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    if (iterator.next().equals("remove")) {
        iterator.remove();  // Safe
    }
}

// Mistake 2: Using next() without checking hasNext()
// Bad
String element = iterator.next();  // May throw NoSuchElementException
// Good
if (iterator.hasNext()) {
    String element = iterator.next();
}

// Mistake 3: Not using iterator.remove() for removal
// Bad
for (String element : list) {
    if (condition) {
        list.remove(element);  // ConcurrentModificationException
    }
}
// Good
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    if (condition) {
        iterator.remove();  // Safe
    }
}

// Mistake 4: Using Iterator for List when ListIterator is needed
// Bad (can't go backward)
Iterator<Integer> iterator = list.iterator();
// Good (bidirectional)
ListIterator<Integer> listIterator = list.listIterator();

// Mistake 5: Creating multiple iterators and mixing them
// Bad
Iterator<String> it1 = list.iterator();
Iterator<String> it2 = list.iterator();
it1.next();
it2.next();  // May cause issues
```

## 16. Pitfalls

### ConcurrentModificationException
- **Cause**: Collection modified structurally while iterator is active
- **Prevention**: Use `iterator.remove()` or `CopyOnWriteArrayList`
- **Detection**: Iterator checks `modCount` before each operation

### Thread Safety Issues
- **Problem**: Standard iterators are not thread-safe
- **Solution**: Use `CopyOnWriteArrayList` or external synchronization
- **Example**: `Collections.synchronizedList()` requires manual synchronization for iteration

### Memory Leaks
- **Problem**: Holding references to iterator can prevent garbage collection
- **Solution**: Don't store iterators as fields; use them locally
- **Example**: Use iterators within method scope only

### Performance Considerations
- **Problem**: Frequent iterator creation has overhead
- **Solution**: Reuse iterators when possible (but carefully)
- **Example**: `forEachRemaining()` for batch processing

## 17. Interview Questions

### Q1: What is the difference between Iterator and ListIterator?
**Answer**: `Iterator` is for forward traversal only, while `ListIterator` supports bidirectional traversal, element replacement, and element insertion. `ListIterator` is specific to `List` interfaces.

### Q2: What is ConcurrentModificationException and how to avoid it?
**Answer**: It's thrown when a collection is modified structurally while being iterated. Avoid by using `iterator.remove()` instead of `collection.remove()`, or use thread-safe collections like `CopyOnWriteArrayList`.

### Q3: What is the difference between Iterator and for-each loop?
**Answer**: For-each loop is syntactic sugar for Iterator. It's cleaner but less flexible - you can't remove elements or access index. Use Iterator when you need removal or bidirectional traversal.

### Q4: When would you use ListIterator over Iterator?
**Answer**: When you need bidirectional traversal, element replacement, or insertion during iteration. Also when you need to know the current index.

### Q5: What is the Iterable interface and why is it important?
**Answer**: `Iterable<T>` requires implementing `iterator()` method. It enables for-each loop syntax and makes classes compatible with Java's collection framework. All collections implement it.

### Q6: How does CopyOnWriteArrayList handle iteration?
**Answer**: Creates a snapshot of the array at iterator creation time. Iteration is safe and doesn't throw ConcurrentModificationException, but modifications create new copies of the array.

### Q7: Can you iterate over a Map directly?
**Answer**: No, but you can iterate over `keySet()`, `values()`, or `entrySet()`. Or use `forEach()` method with lambda expression.

## 18. Exercises

### Exercise 1: Basic Iterator
Create an `ArrayList` of integers and use an `Iterator` to print only even numbers. Then use `Iterator.remove()` to remove all odd numbers.

### Exercise 2: Bidirectional Traversal
Create a `LinkedList` of strings and use `ListIterator` to:
1. Print all elements forward
2. Print all elements backward
3. Replace all elements with their uppercase versions

### Exercise 3: Custom Iterator
Create a custom collection class that implements `Iterable` and provides a custom iterator. The iterator should skip null elements.

### Exercise 4: Thread-Safe Iteration
Implement a thread-safe list that allows safe iteration even when other threads modify the collection. Use `CopyOnWriteArrayList` or manual synchronization.

## 19. Summary

- `Iterator` provides a standard way to traverse collections
- Supports forward traversal with `hasNext()` and `next()`
- `ListIterator` adds bidirectional traversal, replacement, and insertion
- `Iterable` interface enables for-each loop syntax
- Iterator pattern is a fundamental OOP design pattern
- Use `iterator.remove()` to avoid `ConcurrentModificationException`
- `CopyOnWriteArrayList` provides fail-safe iteration for concurrent access
- Standard iterators are not thread-safe; use synchronization or concurrent collections

## 20. References

### Official Documentation
- [Java Iterator Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Iterator.html)
- [Java ListIterator Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ListIterator.html)
- [Java Iterable Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Iterable.html)

### Books
- *Effective Java* by Joshua Bloch
- *Design Patterns: Elements of Reusable Object-Oriented Software* by Gang of Four
- *Java Concurrency in Practice* by Brian Goetz

### Online Resources
- [Baeldung - Iterator in Java](https://www.baeldung.com/java-iterator)
- [GeeksforGeeks - Iterator in Java](https://www.geeksforgeeks.org/iterator-in-java/)
- [Oracle - Collections Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/)

### Related Topics
- [List Interface](../05-list/README.md)
- [Set Interface](../06-set/README.md)
- [Fail-Fast vs Fail-Safe](../26-fail-fast-vs-fail-safe/README.md)
