# Map Interface

## Overview

Map stores key-value pairs, providing efficient lookup by key. It's separate from the `Collection` interface and models the mathematical function abstraction. Each key maps to at most one value, and you can use a key to efficiently retrieve its associated value.

## Learning Objectives

- Understand the Map interface and its properties
- Learn key-value pair operations
- Understand Map implementations (HashMap, LinkedHashMap, TreeMap, ConcurrentHashMap)
- Know when to use Map vs other collections
- Master Map operations (put, get, containsKey, etc.)
- Understand equals() and hashCode() contract for keys

## Implementations

| Implementation | Underlying Structure | Order | Null Keys | Thread-Safe |
|----------------|---------------------|-------|-----------|-------------|
| `HashMap` | Hash table | None | Yes (1) | No |
| `LinkedHashMap` | Hash table + linked list | Insertion/Access | Yes (1) | No |
| `TreeMap` | Red-black tree | Sorted | No | No |
| `ConcurrentHashMap` | Hash table | None | No | Yes |

## Key Concepts

### Hashing Mechanism

When putting a key-value pair:
1. Compute `hashCode()` of the key
2. Find bucket: `hash & (capacity - 1)`
3. If bucket empty, create new entry
4. If bucket has entries, search for equal key using `equals()`
5. If found, replace value; if not, add new entry

### View Collections

```java
Set<String> keys = map.keySet();           // Set of keys
Collection<Integer> values = map.values(); // Collection of values
Set<Map.Entry<String, Integer>> entries = map.entrySet(); // Set of entries
```

### Modern Methods (Java 8+)

```java
map.merge(key, value, Integer::sum);        // Merge values
map.compute(key, (k, v) -> v + 1);         // Compute new value
map.computeIfAbsent(key, k -> new ArrayList<>()); // Compute if absent
map.getOrDefault(key, defaultValue);        // Get with default
map.forEach((k, v) -> System.out.println(k + ": " + v)); // Iterate
```

## When to Use Each

- **HashMap**: Default choice, fastest O(1) operations, no order needed
- **LinkedHashMap**: Need insertion/access order preserved
- **TreeMap**: Need sorted keys or NavigableMap operations
- **ConcurrentHashMap**: Thread-safe, high-concurrency scenarios

## Subtopics

- [HashMap](01-hashmap/)
- [LinkedHashMap](02-linkedhashmap/)
- [TreeMap](03-treemap/)
- [ConcurrentHashMap](04-concurrenthashmap/)
