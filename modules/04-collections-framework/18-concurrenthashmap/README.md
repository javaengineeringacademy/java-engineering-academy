# ConcurrentHashMap

## Introduction

ConcurrentHashMap is a thread-safe implementation of Map that allows concurrent access without locking the entire map.

## Learning Objectives

- Create and use ConcurrentHashMap
- Understand thread-safe operations
- Learn about concurrent modifications
- Know when to use ConcurrentHashMap vs synchronized maps

## Prerequisites

- HashMap
- Basic threading concepts
- Synchronized collections

## Why This Matters

ConcurrentHashMap provides better performance than synchronized collections by allowing concurrent reads and segment-based locking for writes.

## Syntax

```java
// Creating ConcurrentHashMap
Map<K, V> map = new ConcurrentHashMap<>();
Map<K, V> map = new ConcurrentHashMap<>(initialCapacity);
Map<K, V> map = new ConcurrentHashMap<>(concurrencyLevel);

// Thread-safe operations
map.put(key, value);        // Atomic
map.get(key);               // Non-blocking
map.remove(key);            // Atomic
map.putIfAbsent(key, value); // Atomic
map.compute(key, remappingFunction); // Atomic
map.merge(key, value, remappingFunction); // Atomic
```

## Examples

```java
// Example 1: Basic ConcurrentHashMap
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("Alice", 30);
map.put("Bob", 25);

System.out.println(map.get("Alice"));  // 30

// Example 2: Thread-safe counting
public class ThreadSafeCounter {
    private final ConcurrentHashMap<String, AtomicInteger> counts = new ConcurrentHashMap<>();

    public void increment(String key) {
        counts.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public int getCount(String key) {
        return counts.getOrDefault(key, new AtomicInteger(0)).get();
    }
}

// Example 3: Parallel processing
ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();
IntStream.range(0, 1000).parallel().forEach(i ->
    map.put(i, "Value" + i)
);
System.out.println(map.size());  // 1000

// Example 4: Atomic operations
ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();
scores.put("Alice", 100);

// Atomic increment
scores.compute("Alice", (key, value) -> value + 10);
System.out.println(scores.get("Alice"));  // 110

// Atomic merge
scores.merge("Bob", 50, Integer::sum);
scores.merge("Bob", 30, Integer::sum);
System.out.println(scores.get("Bob"));  // 80
```

## Exercises

1. Create a thread-safe word frequency counter using ConcurrentHashMap.
2. Implement a thread-safe cache with expiration using ConcurrentHashMap.
3. Write a parallel processing example that aggregates data using ConcurrentHashMap.

## Interview Questions

- What is the difference between ConcurrentHashMap and Collections.synchronizedMap()?
- How does ConcurrentHashMap achieve thread safety?
- What are the atomic operations available in ConcurrentHashMap?

## Common Pitfalls

- Assuming all operations are atomic (size() is approximate)
- Not using atomic operations for compound actions
- Using ConcurrentHashMap when synchronization isn't needed

## Best Practices

- Use ConcurrentHashMap for concurrent access
- Use atomic operations (compute, merge, putIfAbsent) for compound actions
- Consider ReadWriteLock for read-heavy scenarios
- Use appropriate initial capacity and concurrency level

## Real World Applications

- Concurrent caching
- Thread-safe counters
- Parallel data processing
- Multi-threaded applications
- Web application session storage

## References

- [ConcurrentHashMap Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html)
- [Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)

## Summary

In this topic, you learned about ConcurrentHashMap and its thread-safe operations. It provides better performance than synchronized collections for concurrent access. Practice with the exercises before learning about Comparators.
