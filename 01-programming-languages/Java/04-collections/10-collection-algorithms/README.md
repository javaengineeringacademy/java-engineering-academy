# Collection Algorithms

## Overview

The `Collections` class provides static utility methods for working with collections, including searching, shuffling, reversing, and creating unmodifiable or synchronized views.

## Key Methods

### Searching
```java
int index = Collections.binarySearch(sortedList, key);
int max = Collections.max(collection);
int min = Collections.min(collection);
int freq = Collections.frequency(collection, object);
boolean disjoint = Collections.disjoint(col1, col2);
```

### Modifying
```java
Collections.reverse(list);
Collections.shuffle(list);
Collections.sort(list);
Collections.swap(list, i, j);
Collections.rotate(list, distance);
Collections.fill(list, object);
```

### Creating Views
```java
List<E> unmodifiable = Collections.unmodifiableList(list);
Set<E> unmodifiable = Collections.unmodifiableSet(set);
Map<K,V> unmodifiable = Collections.unmodifiableMap(map);
List<E> synchronizedList = Collections.synchronizedList(list);
Map<K,V> synchronizedMap = Collections.synchronizedMap(map);
```

### Factory Methods
```java
List<E> empty = Collections.emptyList();
Set<E> empty = Collections.emptySet();
Map<K,V> empty = Collections.emptyMap();
List<E> single = Collections.singletonList(element);
Set<E> single = Collections.singleton(element);
Map<K,V> single = Collections.singletonMap(key, value);
List<E> copies = Collections.nCopies(n, object);
```

## Best Practices

- Use `Collections.unmodifiable*` for defensive copying
- Use `Collections.synchronized*` for simple thread safety (prefer concurrent collections for complex operations)
- Use Java 8+ methods (`List.of()`, `List.copyOf()`) when available
- `binarySearch` requires a sorted list
- Consider Guava or Apache Commons for additional utilities
