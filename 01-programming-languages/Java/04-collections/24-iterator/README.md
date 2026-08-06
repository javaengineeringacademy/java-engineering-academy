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


## 📑 Continue Reading

**Part 1** of 2 | Part 2

