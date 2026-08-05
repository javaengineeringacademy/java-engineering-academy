# Java Sorting: Comparable vs Comparator

## Overview

Java provides two primary interfaces for sorting objects: `Comparable` and `Comparator`. Understanding the difference between them is essential for writing clean, maintainable sorting code.

## Comparable Interface

### What is Comparable?

`Comparable<T>` defines the **natural ordering** of a class. It is implemented by the class itself and provides a single method: `compareTo(T o)`.

```java
public class Student implements Comparable<Student> {
    private String name;
    
    @Override
    public int compareTo(Student other) {
        return this.name.compareTo(other.name);
    }
}
```

### When to Use Comparable

- When there is one **obvious** natural ordering (e.g., numbers, dates, alphabetical)
- When you want sorting to work with `Collections.sort()` without providing a Comparator
- When the ordering is intrinsic to the class

### compareTo() Contract

The `compareTo()` method must follow these rules:

1. **Reflexive**: `x.compareTo(x) == 0`
2. **Antisymmetric**: If `x.compareTo(y) < 0`, then `y.compareTo(x) > 0`
3. **Transitive**: If `x.compareTo(y) < 0` and `y.compareTo(z) < 0`, then `x.compareTo(z) < 0`
4. **Consistent with equals** (recommended): `x.compareTo(y) == 0` if and only if `x.equals(y)`

### Return Values

- **Negative**: `this` comes before `other`
- **Zero**: `this` equals `other`
- **Positive**: `this` comes after `other`

## Comparator Interface

### What is Comparator?

`Comparator<T>` defines **custom ordering** for objects. It is a separate class that compares two objects and provides the `compare(T o1, T o2)` method.

```java
Comparator<Student> byName = Comparator.comparing(Student::getName);
students.sort(byName);
```

### When to Use Comparator

- When you need **multiple sorting strategies** for the same class
- When the natural ordering is not suitable for your use case
- When you want to sort objects you don't control (third-party classes)
- When you need **reversible** sorting (ascending/descending)

### Modern Comparator Patterns (Java 8+)

```java
// By name
Comparator.comparing(Student::getName)

// By age (descending)
Comparator.comparingInt(Student::getAge).reversed()

// By GPA then name
Comparator.comparingDouble(Student::getGpa)
    .thenComparing(Student::getName)

// Null-safe
Comparator.nullsLast(Comparator.comparing(Student::getName))
```

## Natural Ordering vs Custom Ordering

| Aspect | Comparable | Comparator |
|--------|-----------|------------|
| Location | Inside the class | Separate class/method |
| Number of orderings | One (natural) | Multiple |
| Modifies class | Yes | No |
| Usage | `Collections.sort(list)` | `Collections.sort(list, comparator)` |
| Flexibility | Low | High |

## Lambda Comparators

Java 8+ enables concise lambda comparators:

```java
// Sort by name length
students.sort((s1, s2) -> Integer.compare(
    s1.getName().length(), 
    s2.getName().length()
));

// Sort by GPA descending
students.sort((s1, s2) -> Double.compare(
    s2.getGpa(), 
    s1.getGpa()
));

// Sort by multiple fields
students.sort((s1, s2) -> {
    int compare = s1.getGrade().compareTo(s2.getGrade());
    return compare != 0 ? compare : s1.getName().compareTo(s2.getName());
});
```

## Sorting Collections

### List Sorting

```java
// Using Comparable
Collections.sort(students);

// Using Comparator
students.sort(Comparator.comparing(Student::getName));

// In-place sorting
students.sort(StudentComparator.byName());
```

### Set Sorting

```java
// TreeSet uses natural ordering
Set<Student> treeSet = new TreeSet<>();

// TreeSet with Comparator
Set<Student> sortedSet = new TreeSet<>(Comparator.comparing(Student::getName));
```

### Map Sorting

```java
// Sort map by value
Map<String, Double> sortedMap = map.entrySet()
    .stream()
    .sorted(Map.Entry.comparingByValue())
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (e1, e2) -> e1,
        LinkedHashMap::new
    ));

// Sort map by key (use TreeMap)
Map<String, Double> sortedByKey = new TreeMap<>(map);
```

## Sorting Arrays

```java
// Using Comparable
Arrays.sort(students);

// Using Comparator
Arrays.sort(students, Comparator.comparing(Student::getName));

// Partial sort (first 5 elements)
Arrays.sort(students, 0, 5);

// Parallel sort (better for large arrays)
Arrays.parallelSort(students);
```

## Null Handling

Java provides built-in null handling for Comparators:

```java
// Nulls appear before non-null elements
Comparator.nullsFirst(comparator)

// Nulls appear after non-null elements
Comparator.nullsLast(comparator)

// Example
students.sort(Comparator.nullsLast(Comparator.comparing(Student::getName)));
```

### Null Handling Best Practices

1. **Always handle nulls explicitly** - don't assume non-null
2. **Use nullsFirst/nullsLast** - cleaner than manual null checks
3. **Consider Optional** - for new code, prefer `Optional<T>` over null
4. **Document null policy** - make it clear in your API

## Stream Sorting

```java
// Sort and collect
List<Student> sorted = students.stream()
    .sorted(Comparator.comparing(Student::getName))
    .collect(Collectors.toList());

// Sort and limit (top N)
List<Student> top3 = students.stream()
    .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
    .limit(3)
    .collect(Collectors.toList());

// Parallel sorting
List<Student> parallel = students.parallelStream()
    .sorted(Comparator.comparing(Student::getName))
    .collect(Collectors.toList());
```

## Performance Considerations

| Scenario | Recommendation |
|----------|----------------|
| Small lists (< 1000) | `Collections.sort()` or `Arrays.sort()` |
| Large lists (> 10000) | Consider parallel streams |
| Repeated sorting | Cache Comparator instances |
| Primitive data | Use `IntStream`, `DoubleStream` |
| Memory-sensitive | Use `Arrays.sort()` (in-place) |

### Time Complexity

- **TimSort** (Java's default): O(n log n) average and worst case
- **ParallelSort**: O(n log n) but uses multiple threads
- **Insertion sort** (small arrays): O(n²) but fast for small n

## Common Interview Questions

### 1. What is the difference between Comparable and Comparator?

**Answer**: `Comparable` defines natural ordering within the class (single ordering), while `Comparator` defines custom ordering externally (multiple orderings possible).

### 2. Can you sort a List without modifying the original?

**Answer**: Yes, create a copy first:

```java
List<Student> sorted = new ArrayList<>(students);
sorted.sort(Comparator.comparing(Student::getName));
```

Or use streams:

```java
List<Student> sorted = students.stream()
    .sorted(Comparator.comparing(Student::getName))
    .collect(Collectors.toList());
```

### 3. How do you handle null values in sorting?

**Answer**: Use `Comparator.nullsFirst()` or `Comparator.nullsLast()`:

```java
students.sort(Comparator.nullsLast(Comparator.comparing(Student::getName)));
```

### 4. How do you sort by multiple fields?

**Answer**: Use `thenComparing()`:

```java
students.sort(
    Comparator.comparing(Student::getGrade)
        .thenComparing(Student::getName)
        .thenComparingInt(Student::getAge)
);
```

### 5. What is the time complexity of sorting?

**Answer**: Java uses TimSort with O(n log n) time complexity. ParallelSort also has O(n log n) but uses multiple threads for better wall-clock time.

### 6. When would you use Comparable vs Comparator?

**Answer**: 
- Use `Comparable` for natural ordering (numbers, dates, alphabetical)
- Use `Comparator` for multiple sort strategies or when you can't modify the class

### 7. How do you sort a Map by value?

**Answer**: Convert to entry stream, sort, and collect to a new map:

```java
Map<String, Double> sorted = map.entrySet()
    .stream()
    .sorted(Map.Entry.comparingByValue())
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (e1, e2) -> e1,
        LinkedHashMap::new
    ));
```

### 8. What is the difference between sort() and Collections.sort()?

**Answer**: `List.sort()` is an instance method (Java 8+), while `Collections.sort()` is a static method. Both use TimSort internally.

## References

- [Oracle: Comparable vs Comparator](https://docs.oracle.com/javase/tutorial/collections/interfaces/order.html)
- [Baeldung: Guide to Java Comparable](https://www.baeldung.com/java-comparable-comparator)
- [Java API: Comparable](https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html)
- [Java API: Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)
