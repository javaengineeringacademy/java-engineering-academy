# TreeMap

## Introduction

TreeMap is a NavigableMap implementation based on a Red-Black tree. It maintains keys in sorted order.

## Learning Objectives

- Create and use TreeMap
- Understand natural ordering and custom comparators
- Learn NavigableMap methods
- Know when to use TreeMap vs HashMap

## Prerequisites

- Map Interface
- Comparable and Comparator interfaces

## Why This Matters

TreeMap provides sorted key access with O(log n) performance, essential for range queries and sorted key requirements.

## Syntax

```java
// Creating TreeMap
Map<K, V> map = new TreeMap<>();              // Natural ordering
Map<K, V> map = new TreeMap<>(comparator);    // Custom ordering
Map<K, V> map = new TreeMap<>(map);           // From another map

// Standard Map operations
map.put(key, value);        // O(log n)
map.get(key);               // O(log n)
map.remove(key);            // O(log n)

// NavigableMap methods
map.firstKey();             // Smallest key
map.lastKey();              // Largest key
map.lowerKey(key);          // Greatest key less than
map.higherKey(key);         // Smallest key greater than
map.floorKey(key);          // Greatest key less than or equal
map.ceilingKey(key);        // Smallest key greater than or equal
map.headMap(toKey);         // Keys less than
map.tailMap(fromKey);       // Keys greater than or equal
map.subMap(from, to);       // Range of keys
```

## Examples

```java
// Example 1: Natural ordering
TreeMap<String, Integer> ages = new TreeMap<>();
ages.put("Charlie", 35);
ages.put("Alice", 30);
ages.put("Bob", 25);

System.out.println(ages);  // {Alice=30, Bob=25, Charlie=35} - sorted by key
System.out.println(ages.firstKey());  // Alice
System.out.println(ages.lastKey());   // Charlie

// Example 2: Custom comparator
TreeMap<String, Integer> reverseMap = new TreeMap<>(Comparator.reverseOrder());
reverseMap.put("Charlie", 35);
reverseMap.put("Alice", 30);
reverseMap.put("Bob", 25);

System.out.println(reverseMap);  // {Charlie=35, Bob=25, Alice=30} - reverse sorted

// Example 3: NavigableMap operations
TreeMap<Integer, String> map = new TreeMap<>();
map.put(1, "One");
map.put(3, "Three");
map.put(5, "Five");
map.put(7, "Seven");

System.out.println(map.lowerEntry(5));     // 3=Three
System.out.println(map.higherEntry(5));    // 7=Seven
System.out.println(map.floorEntry(4));     // 3=Three
System.out.println(map.ceilingEntry(4));   // 5=Five

System.out.println(map.headMap(5));        // {1=One, 3=Three}
System.out.println(map.tailMap(5));        // {5=Five, 7=Seven}
System.out.println(map.subMap(2, 6));      // {3=Three, 5=Five}

// Example 4: Range queries
public List<String> getKeysInRange(TreeMap<String, Integer> map, String start, String end) {
    return new ArrayList<>(map.subMap(start, true, end, true).keySet());
}
```

## Exercises

1. Create a TreeMap of student names to GPAs sorted by GPA.
2. Implement a method that finds all keys within a given range.
3. Create a TreeMap that sorts strings by length, then alphabetically.

## Interview Questions

- What is the time complexity of get() and put()?
- What is the difference between headMap() and subMap()?
- How does TreeMap handle null keys?

## Common Pitfalls

- Assuming TreeMap allows null keys (it doesn't with natural ordering)
- Not implementing Comparable for custom keys
- Using TreeMap when HashMap would suffice

## Best Practices

- Use TreeMap when you need sorted keys
- Use HashMap when order doesn't matter (better performance)
- Implement Comparable for custom key types
- Consider performance overhead for large datasets

## Real World Applications

- Sorted data display
- Range queries
- Time-based data storage
- Leaderboards
- Configuration management

## References

- [TreeMap Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html)
- [NavigableMap Interface](https://docs.oracle.com/javase/8/docs/api/java/util/NavigableMap.html)

## Summary

In this topic, you learned about TreeMap and its sorted key capabilities with O(log n) performance. It's essential for range queries and sorted key access. Practice with the exercises before learning about ConcurrentHashMap.
