# LinkedHashMap

## Introduction

LinkedHashMap is a HashMap that maintains insertion order (or access order) using a doubly-linked list.

## Learning Objectives

- Create and use LinkedHashMap
- Understand insertion order vs access order
- Learn LRU cache implementation
- Know when to use LinkedHashMap vs HashMap

## Prerequisites

- HashMap
- Map Interface

## Why This Matters

LinkedHashMap provides the same O(1) performance as HashMap while maintaining order, making it useful for LRU caches and ordered iteration.

## Syntax

```java
// Creating LinkedHashMap
Map<K, V> map = new LinkedHashMap<>();                        // Insertion order
Map<K, V> map = new LinkedHashMap<>(initialCapacity);         // With capacity
Map<K, V> map = new LinkedHashMap<>(initialCapacity, loadFactor, accessOrder); // Access order

// Same operations as HashMap
map.put(key, value);
map.get(key);
map.remove(key);
map.containsKey(key);
```

## Examples

```java
// Example 1: Insertion order (default)
Map<String, Integer> map = new LinkedHashMap<>();
map.put("Charlie", 3);
map.put("Alice", 1);
map.put("Bob", 2);

System.out.println(map);  // {Charlie=3, Alice=1, Bob=2} - insertion order

// Example 2: Access order (for LRU)
Map<String, Integer> lruMap = new LinkedHashMap<>(16, 0.75f, true);
lruMap.put("A", 1);
lruMap.put("B", 2);
lruMap.put("C", 3);

lruMap.get("A");  // Access A, moves to end

System.out.println(lruMap);  // {B=2, C=3, A=1} - access order

// Example 3: LRU Cache implementation
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public LRUCache(int maxSize) {
        super(maxSize, 0.75f, true);  // accessOrder = true
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}

LRUCache<String, Integer> cache = new LRUCache<>(3);
cache.put("A", 1);
cache.put("B", 2);
cache.put("C", 3);
cache.get("A");  // Access A
cache.put("D", 4);  // Evicts B (least recently used)

System.out.println(cache.containsKey("B"));  // false
```

## Exercises

1. Create a LinkedHashMap that maintains strings in insertion order.
2. Implement an LRU cache with a maximum size of 10.
3. Write a method that returns the last accessed element from a LinkedHashMap.

## Interview Questions

- What is the difference between HashMap and LinkedHashMap?
- How does access order work in LinkedHashMap?
- How would you implement an LRU cache in Java?

## Common Pitfalls

- Not realizing LinkedHashMap has slightly more memory overhead
- Using access order when insertion order is needed
- Forgetting to override removeEldestEntry for LRU cache

## Best Practices

- Use LinkedHashMap when insertion order matters
- Use access order for LRU cache implementations
- Use HashMap when order doesn't matter (better performance)
- Consider memory overhead for large datasets

## Real World Applications

- LRU caches
- Maintaining insertion order for display
- Access history tracking
- Ordered configuration storage

## References

- [LinkedHashMap Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html)
- [Java Collections Tutorial](https://docs.oracle.com/javase/tutorial/collections/implementations/map.html)

## Summary

In this topic, you learned about LinkedHashMap and its ability to maintain order while providing O(1) performance. It's ideal for LRU caches and ordered iteration. Practice with the exercises before learning about TreeMap.
