# HashMap

## Introduction

HashMap is the most commonly used Map implementation. It provides O(1) average time for get and put operations using hashing.

## Learning Objectives

- Create and use HashMap
- Understand HashMap's performance characteristics
- Learn about hashing and collisions
- Know when to use HashMap vs alternatives

## Prerequisites

- Map Interface
- equals() and hashCode() methods

## Why This Matters

HashMap is the default choice for most Map use cases due to its O(1) performance for basic operations.

## Syntax

```java
// Creating HashMap
Map<K, V> map = new HashMap<>();           // Empty
Map<K, V> map = new HashMap<>(capacity);   // With initial capacity
Map<K, V> map = new HashMap<>(map);        // From another map

// Standard Map operations (all O(1) average)
map.put(key, value);        // Returns old value or null
map.get(key);               // Returns value or null
map.remove(key);            // Returns old value or null
map.containsKey(key);       // O(1)
map.containsValue(value);   // O(n)
map.size();                 // O(1)
```

## Examples

```java
// Example 1: Basic HashMap
Map<String, Integer> scores = new HashMap<>();
scores.put("Alice", 95);
scores.put("Bob", 87);
scores.put("Charlie", 92);

System.out.println(scores.get("Alice"));  // 95
System.out.println(scores.getOrDefault("David", 0));  // 0

// Example 2: Building a frequency map
public static Map<Character, Integer> charFrequency(String str) {
    Map<Character, Integer> freq = new HashMap<>();
    for (char c : str.toCharArray()) {
        freq.merge(c, 1, Integer::sum);
    }
    return freq;
}

// Example 3: Grouping elements
public static <T> Map<Object, List<T>> groupBy(List<T> list, Function<T, ?> classifier) {
    return list.stream().collect(Collectors.groupingBy(classifier));
}

// Example 4: HashMap with custom objects
class Employee {
    private int id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

Map<Employee, String> departments = new HashMap<>();
departments.put(new Employee(1, "Alice"), "Engineering");
departments.put(new Employee(2, "Bob"), "Marketing");
```

## Exercises

1. Create a HashMap to count word frequency in a sentence.
2. Implement a simple phonebook using HashMap.
3. Write a method that finds all duplicate values in a HashMap.

## Interview Questions

- What is the time complexity of get() and put()?
- How does HashMap handle collisions?
- What happens when the load factor is exceeded?

## Common Pitfalls

- Not overriding equals() and hashCode() for custom keys
- Using HashMap in multi-threaded environment (use ConcurrentHashMap)
- Not setting initial capacity for known sizes

## Best Practices

- Set initial capacity to avoid rehashing
- Always override equals() and hashCode() together
- Use ConcurrentHashMap for thread safety
- Consider LinkedHashMap when order matters

## Real World Applications

- Caching and memoization
- Counting and frequency analysis
- Database indexing
- Configuration storage
- Implementing caches

## References

- [HashMap Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)
- [Java Collections Tutorial](https://docs.oracle.com/javase/tutorial/collections/implementations/map.html)

## Summary

In this topic, you learned about HashMap and its O(1) performance for basic operations. It's the most commonly used Map implementation. Practice with the exercises before learning about LinkedHashMap.
