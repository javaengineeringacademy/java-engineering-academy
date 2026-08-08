# Collections Utilities

## Overview

The `Collections` class provides static utility methods for operating on collections. These methods include unmodifiable wrappers, synchronized wrappers, and factory methods for creating empty or singleton collections.

## Unmodifiable Wrappers

```java
List<String> immutable = Collections.unmodifiableList(list);
Set<Integer> immutable = Collections.unmodifiableSet(set);
Map<String, Integer> immutable = Collections.unmodifiableMap(map);
// Attempting to modify throws UnsupportedOperationException
```

## Synchronized Wrappers

```java
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
Set<Integer> syncSet = Collections.synchronizedSet(new HashSet<>());
Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());
// All individual operations are synchronized
// For compound operations, use external synchronization
```

## Empty and Singleton Collections

```java
List<String> empty = Collections.emptyList();
Map<String, Integer> empty = Collections.emptyMap();
Set<Integer> empty = Collections.emptySet();

List<String> single = Collections.singletonList("only");
Map<String, Integer> single = Collections.singletonMap("key", 1);
Set<Integer> single = Collections.singleton(42);

List<String> copies = Collections.nCopies(5, "hello"); // ["hello", "hello", ...]
```

## Unmodifiable vs Immutable

| Feature | `Collections.unmodifiable*` | `List.of()` / `List.copyOf()` |
|---------|---------------------------|-------------------------------|
| Java version | 1.2+ | 9+ |
| Null elements | Allowed | Not allowed |
| Performance | View (no copy) | True immutable copy |
| Backing data | Changes visible | Independent copy |
