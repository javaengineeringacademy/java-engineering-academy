# Collections Utilities in Java Collections Framework

## 1. Introduction

The `Collections` class provides static utility methods for operating on collections. These methods include sorting, reversing, shuffling, and creating unmodifiable or synchronized wrappers. The `Collections` class is a member of the Java Collections Framework and provides convenient methods for common collection operations.

```java
List<Integer> numbers = new ArrayList<>(List.of(3, 1, 4, 1, 5, 9));
Collections.sort(numbers);
Collections.reverse(numbers);
Collections.shuffle(numbers);
```

## 2. Learning Objectives

- Understand the `Collections` utility class and its methods
- Learn about unmodifiable and synchronized wrappers
- Master sorting, reversing, and shuffling operations
- Understand frequency and disjoint operations
- Learn about empty and singleton factory methods

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of collections (List, Set, Map)
- Familiarity with generics
- Basic understanding of thread safety (recommended)

## 4. Why This Concept Exists

The `Collections` class provides convenient utility methods that are commonly needed when working with collections. Instead of implementing sorting, reversing, or shuffling algorithms manually, developers can use these optimized, tested methods. They also provide wrappers for thread safety and immutability.

## 5. Problem Statement

Common collection operations like sorting, reversing, and shuffling require manual implementation without utility classes. Additionally, creating thread-safe or unmodifiable collections requires boilerplate code. The `Collections` class solves these problems by providing ready-made, optimized methods.

## 6. Theory

### Key Methods

#### Unmodifiable Wrappers
- `Collections.unmodifiableList(List<? extends T> list)`: Returns unmodifiable view
- `Collections.unmodifiableSet(Set<? extends T> set)`: Returns unmodifiable view
- `Collections.unmodifiableMap(Map<? extends K, ? extends V> map)`: Returns unmodifiable view

#### Synchronized Wrappers
- `Collections.synchronizedList(List<T> list)`: Returns synchronized wrapper
- `Collections.synchronizedSet(Set<T> set)`: Returns synchronized wrapper
- `Collections.synchronizedMap(Map<K, V> map)`: Returns synchronized wrapper

#### Sorting and Ordering
- `Collections.sort(List<T> list)`: Sorts list in natural order
- `Collections.sort(List<T> list, Comparator<? super T> c)`: Sorts with comparator
- `Collections.reverse(List<?> list)`: Reverses order
- `Collections.shuffle(List<?> list)`: Randomizes order
- `Collections.shuffle(List<?> list, Random rnd)`: Randomizes with seed

#### Searching
- `Collections.binarySearch(List<? extends Comparable<? super T>> list, T key)`: Binary search
- `Collections.binarySearch(List<? extends T> list, T key, Comparator<? super T> c)`: Binary search with comparator

#### Frequency and Disjoint
- `Collections.frequency(Collection<?> c, Object o)`: Counts occurrences
- `Collections.disjoint(Collection<?> c1, Collection<?> c2)`: Checks if collections have no common elements

#### Empty and Singleton
- `Collections.emptyList()`: Returns empty immutable list
- `Collections.emptySet()`: Returns empty immutable set
- `Collections.emptyMap()`: Returns empty immutable map
- `Collections.singletonList(T o)`: Returns immutable list with one element
- `Collections.singleton(T o)`: Returns immutable set with one element
- `Collections.singletonMap(K key, V value)`: Returns immutable map with one entry

## 7. Internal Working

### Unmodifiable Wrapper Implementation
```java
// Simplified unmodifiable list wrapper
private static class UnmodifiableList<E> implements List<E> {
    private final List<? extends E> list;
    
    UnmodifiableList(List<? extends E> list) {
        this.list = list;
    }
    
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }
    
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }
    
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }
    
    public E get(int index) {
        return list.get(index);  // Delegate to original
    }
    
    public int size() {
        return list.size();  // Delegate to original
    }
}
```

### Synchronized Wrapper Implementation
```java
// Simplified synchronized list wrapper
private static class SynchronizedList<E> implements List<E> {
    private final List<E> list;
    
    SynchronizedList(List<E> list) {
        this.list = list;
    }
    
    public synchronized boolean add(E e) {
        return list.add(e);
    }
    
    public synchronized E get(int index) {
        return list.get(index);
    }
    
    public synchronized E set(int index, E element) {
        return list.set(index, element);
    }
    
    public synchronized Iterator<E> iterator() {
        return list.iterator();  // Not synchronized!
    }
}
```

### Sort Implementation
```java
// Simplified sort using TimSort
public static <T extends Comparable<? super T>> void sort(List<T> list) {
    Object[] a = list.toArray();
    Arrays.sort(a);  // Uses TimSort algorithm
    ListIterator<T> i = list.listIterator();
    for (int j = 0; j < a.length; j++) {
        i.next();
        i.set((T) a[j]);
    }
}
```

## 8. Syntax

```java
// Import
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;

// Unmodifiable wrappers
List<String> unmodifiableList = Collections.unmodifiableList(list);
Set<Integer> unmodifiableSet = Collections.unmodifiableSet(set);
Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(map);

// Synchronized wrappers
List<String> synchronizedList = Collections.synchronizedList(list);
Set<Integer> synchronizedSet = Collections.synchronizedSet(set);
Map<String, Integer> synchronizedMap = Collections.synchronizedMap(map);

// Sorting and ordering
Collections.sort(list);
Collections.sort(list, Comparator.reverseOrder());
Collections.reverse(list);
Collections.shuffle(list);
Collections.shuffle(list, new Random());

// Searching
int index = Collections.binarySearch(sortedList, key);
int index = Collections.binarySearch(sortedList, key, comparator);

// Frequency and disjoint
int count = Collections.frequency(collection, element);
boolean disjoint = Collections.disjoint(collection1, collection2);

// Empty and singleton
List<String> emptyList = Collections.emptyList();
Set<Integer> emptySet = Collections.emptySet();
Map<String, Integer> emptyMap = Collections.emptyMap();
List<String> singleElementList = Collections.singletonList("element");
Set<Integer> singleElementSet = Collections.singleton(42);
Map<String, Integer> singleElementMap = Collections.singletonMap("key", 1);
```

## 9. Easy Example

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsBasic {
    public static void main(String[] args) {
        // Create a list
        List<Integer> numbers = new ArrayList<>();
        numbers.add(5);
        numbers.add(2);
        numbers.add(8);
        numbers.add(1);
        numbers.add(9);
        
        System.out.println("Original: " + numbers);
        
        // Sort
        Collections.sort(numbers);
        System.out.println("Sorted: " + numbers);
        
        // Reverse
        Collections.reverse(numbers);
        System.out.println("Reversed: " + numbers);
        
        // Shuffle
        Collections.shuffle(numbers);
        System.out.println("Shuffled: " + numbers);
        
        // Frequency
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Alice");
        names.add("Charlie");
        names.add("Alice");
        
        int aliceCount = Collections.frequency(names, "Alice");
        System.out.println("Alice count: " + aliceCount);  // 3
        
        // Disjoint
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        
        List<Integer> list2 = new ArrayList<>();
        list2.add(4);
        list2.add(5);
        list2.add(6);
        
        boolean disjoint = Collections.disjoint(list1, list2);
        System.out.println("Lists are disjoint: " + disjoint);  // true
    }
}
```

## 10. Medium Example

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

public class CollectionsAdvanced {
    public static void main(String[] args) {
        // Unmodifiable list
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        
        List<String> unmodifiableNames = Collections.unmodifiableList(names);
        System.out.println("Unmodifiable: " + unmodifiableNames);
        
        try {
            unmodifiableNames.add("David");  // Throws UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiable list");
        }
        
        // Synchronized list
        List<Integer> synchronizedNumbers = Collections.synchronizedList(new ArrayList<>());
        synchronizedNumbers.add(1);
        synchronizedNumbers.add(2);
        synchronizedNumbers.add(3);
        
        System.out.println("Synchronized: " + synchronizedNumbers);
        
        // Sort with comparator
        List<String> cities = new ArrayList<>();
        cities.add("New York");
        cities.add("Los Angeles");
        cities.add("Chicago");
        cities.add("Houston");
        
        // Sort by length
        cities.sort(Comparator.comparingInt(String::length));
        System.out.println("Sorted by length: " + cities);
        
        // Sort reverse
        cities.sort(Comparator.comparingInt(String::length).reversed());
        System.out.println("Sorted by length (reverse): " + cities);
        
        // Binary search (list must be sorted)
        List<Integer> sortedList = new ArrayList<>();
        sortedList.add(10);
        sortedList.add(20);
        sortedList.add(30);
        sortedList.add(40);
        sortedList.add(50);
        
        int index = Collections.binarySearch(sortedList, 30);
        System.out.println("Index of 30: " + index);  // 2
        
        // Empty and singleton collections
        List<String> emptyList = Collections.emptyList();
        System.out.println("Empty list: " + emptyList);
        System.out.println("Empty list size: " + emptyList.size());  // 0
        
        List<String> singleElement = Collections.singletonList("Only");
        System.out.println("Singleton list: " + singleElement);
        
        Map<String, Integer> singleEntry = Collections.singletonMap("key", 1);
        System.out.println("Singleton map: " + singleEntry);
    }
}
```

## 11. Hard Example

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class DataProcessor {
    private final List<String> data;
    private final Map<String, Integer> wordCount;
    
    public DataProcessor() {
        this.data = new ArrayList<>();
        this.wordCount = new HashMap<>();
    }
    
    public void loadData(String[] items) {
        for (String item : items) {
            data.add(item);
        }
    }
    
    public void processAndSort() {
        System.out.println("=== Processing Data ===");
        
        // Create unmodifiable view for safe processing
        List<String> immutableData = Collections.unmodifiableList(data);
        
        // Sort by natural order
        List<String> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData);
        System.out.println("Sorted: " + sortedData);
        
        // Sort by length
        sortedData.sort(Comparator.comparingInt(String::length));
        System.out.println("Sorted by length: " + sortedData);
        
        // Reverse
        Collections.reverse(sortedData);
        System.out.println("Reversed: " + sortedData);
        
        // Shuffle
        List<String> shuffledData = new ArrayList<>(data);
        Collections.shuffle(shuffledData);
        System.out.println("Shuffled: " + shuffledData);
    }
    
    public void countWords() {
        System.out.println("
=== Word Count ===");
        
        for (String word : data) {
            wordCount.merge(word, 1, Integer::sum);
        }
        
        // Sort by frequency
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(wordCount.entrySet());
        sortedEntries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    
    public void findDuplicates() {
        System.out.println("
=== Finding Duplicates ===");
        
        Set<String> unique = new HashSet<>();
        Set<String> duplicates = new HashSet<>();

## 📑 Continue Reading

**Part 1** of 2 | [Part 2](README-part2.md)

