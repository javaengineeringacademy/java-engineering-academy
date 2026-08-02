# Map Interface

## Introduction

Map stores key-value pairs, providing efficient lookup by key. It's separate from the Collection interface.

## Learning Objectives

- Understand the Map interface and its properties
- Learn key-value pair operations
- Understand Map implementations
- Know when to use Map vs other collections

## Prerequisites

- Introduction to Collections Framework
- equals() and hashCode() methods

## Why This Matters

Maps are fundamental for key-value lookups, caching, counting, and many other operations requiring fast access by key.

## Syntax

```java
// Map interface methods
Map<K, V> map = new HashMap<>();
map.put(key, value);        // Add/replace entry
map.get(key);               // Get value by key
map.remove(key);            // Remove entry
map.containsKey(key);       // Check if key exists
map.containsValue(value);   // Check if value exists
map.size();                 // Get size
map.isEmpty();              // Check if empty
map.keySet();               // Get all keys
map.values();               // Get all values
map.entrySet();             // Get all key-value pairs
map.getOrDefault(key, default); // Get with default
map.putIfAbsent(key, value);    // Add only if absent
map.merge(key, value, remappingFunction); // Merge values
```

## Examples

```java
// Example 1: Basic Map operations
Map<String, Integer> ages = new HashMap<>();
ages.put("Alice", 30);
ages.put("Bob", 25);
ages.put("Charlie", 35);

System.out.println(ages.get("Alice"));  // 30
System.out.println(ages.containsKey("Bob"));  // true
System.out.println(ages.size());  // 3

// Example 2: Iterating over Map
for (Map.Entry<String, Integer> entry : ages.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// Or using forEach
ages.forEach((name, age) -> System.out.println(name + ": " + age));

// Example 3: Counting with Map
String[] words = {"apple", "banana", "apple", "cherry", "banana", "apple"};
Map<String, Integer> wordCount = new HashMap<>();

for (String word : words) {
    wordCount.merge(word, 1, Integer::sum);
}

System.out.println(wordCount);  // {apple=3, banana=2, cherry=1}
```

## Exercises

1. Create a Map of student names to grades and find the average grade.
2. Write a method that inverts a Map (swap keys and values).
3. Create a word frequency counter using Map.

## Interview Questions

- What is the difference between Map and Collection?
- Why must keys in a Map implement equals() and hashCode()?
- What are the different Map implementations?

## Common Pitfalls

- Not overriding equals() and hashCode() for custom key objects
- Assuming Map maintains insertion order (HashMap doesn't)
- Using Map when you don't need key-value pairs

## Best Practices

- Use HashMap for best performance when order doesn't matter
- Use LinkedHashMap when you need insertion order
- Use TreeMap when you need sorted keys
- Always implement equals() and hashCode() for custom keys

## Real World Applications

- Caching and memoization
- Counting occurrences
- Database record storage
- Configuration management
- JSON/XML data representation

## References

- [Map Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/Map.html)
- [Java Collections Tutorial](https://docs.oracle.com/en/java/javase/21/collections/interfaces/map.html)

## Summary

In this topic, you learned about the Map interface and its key-value pair operations. Maps are essential for many programming tasks. Practice with the exercises before learning about HashMap.
