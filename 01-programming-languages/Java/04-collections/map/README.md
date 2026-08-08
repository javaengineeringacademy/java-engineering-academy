# Map Interface

## 1. What Is It

The `Map` interface maps keys to values. It cannot contain duplicate keys and each key maps to at most one value. Map is NOT part of the Collection hierarchy.

## 2. Characteristics

| Characteristic | Description |
|----------------|-------------|
| Key-value pairs | Each entry is a key-value mapping |
| No duplicate keys | Each key appears at most once |
| One null key | Most implementations allow one null key |
| Multiple null values | Most implementations allow null values |
| Not a Collection | Separate hierarchy from Collection |

## 3. Map Contract

| Method | Description | Complexity |
|--------|-------------|------------|
| `put(K key, V value)` | Associates key with value | O(1) HashMap, O(log n) TreeMap |
| `get(Object key)` | Returns value for key | O(1) HashMap, O(log n) TreeMap |
| `remove(Object key)` | Removes key-value pair | O(1) HashMap, O(log n) TreeMap |
| `containsKey(Object key)` | Checks if key exists | O(1) HashMap, O(log n) TreeMap |
| `containsValue(Object value)` | Checks if value exists | O(n) |
| `size()` | Returns entry count | O(1) |
| `isEmpty()` | Checks if empty | O(1) |
| `clear()` | Removes all entries | O(n) |

### View Methods

| Method | Description |
|--------|-------------|
| `keySet()` | Set of all keys |
| `values()` | Collection of all values |
| `entrySet()` | Set of Map.Entry<K,V> |

### Default Methods (Java 8+)

| Method | Description |
|--------|-------------|
| `getOrDefault(key, default)` | Returns value or default |
| `putIfAbsent(key, value)` | Puts only if key absent |
| `remove(key, value)` | Removes only if key maps to value |
| `compute(key, mappingFunction)` | Computes new value |
| `computeIfAbsent(key, mappingFunction)` | Computes if key absent |
| `computeIfPresent(key, remappingFunction)` | Computes if key present |
| `merge(key, value, remappingFunction)` | Merges values |
| `forEach(action)` | Iterates over entries |

## 4. Implementations Overview

| Implementation | Structure | Ordering | Null Keys | Thread-Safe |
|---------------|-----------|----------|-----------|-------------|
| HashMap | Hash table | No order | One null key | No |
| LinkedHashMap | Hash + linked list | Insertion/access order | One null key | No |
| TreeMap | Red-black tree | Sorted by key | No | No |
| Hashtable | Hash table | No order | No null | Yes |
| ConcurrentHashMap | Hash table | No order | No null | Yes (fine-grained) |
| WeakHashMap | Weak references | No order | One null key | No |
| EnumMap | Array | Enum order | No | No |

## 5. Performance Comparison

| Operation | HashMap | LinkedHashMap | TreeMap | ConcurrentHashMap |
|-----------|---------|---------------|---------|-------------------|
| put | O(1) | O(1) | O(log n) | O(1) |
| get | O(1) | O(1) | O(log n) | O(1) |
| remove | O(1) | O(1) | O(log n) | O(1) |
| containsKey | O(1) | O(1) | O(log n) | O(1) |
| Iteration | O(n) | O(n) | O(n) | O(n) |

## 6. Map is NOT Part of Collection Hierarchy

```
Iterable<E>
└── Collection<E>     ← Map is NOT here
    ├── List<E>
    ├── Set<E>
    ├── Queue<E>
    └── Deque<E>

Map<K,V>              ← Separate hierarchy
├── HashMap
├── TreeMap
├── LinkedHashMap
└── ConcurrentHashMap
```

## 7. Common Mistakes

1. **Overriding hashCode/equals incorrectly**: Breaks Map behavior
2. **Using mutable objects as keys**: Key hash changes, entry lost
3. **Iterating over keySet() when entrySet() needed**: Extra lookup per entry
4. **Not handling null from get()**: Returns null if key absent

## 8. One-Minute Revision

- Maps keys to values, no duplicate keys
- HashMap: fastest, no order, one null key
- TreeMap: sorted keys, O(log n) operations
- LinkedHashMap: maintains insertion/access order
- ConcurrentHashMap: thread-safe, fine-grained locking
- Use entrySet() for efficient iteration over key-value pairs

## 9. References

- [Oracle Java Documentation - Map](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
