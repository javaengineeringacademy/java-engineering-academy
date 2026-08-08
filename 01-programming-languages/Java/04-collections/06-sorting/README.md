# Sorting

## Scope

This folder covers all sorting approaches in Java.
Examples and exercises compare and combine Comparable, Comparator, Collections.sort(), and TimSort.

## 1. What Is Sorting

Sorting is the process of arranging elements in a specific order (ascending or descending). Java provides multiple ways to sort collections, using either natural ordering (Comparable) or custom ordering (Comparator).

## 2. Comparable vs Comparator

### Comparable (Natural Ordering)

```java
public class Student implements Comparable<Student> {
    private String name;
    private double gpa;

    @Override
    public int compareTo(Student other) {
        return Double.compare(this.gpa, other.gpa);
    }
}

// Usage
Collections.sort(students);  // Uses natural ordering
students.sort(null);          // Uses natural ordering
```

### Comparator (Custom Ordering)

```java
Comparator<Student> byName = Comparator.comparing(Student::getName);
Comparator<Student> byGpa = Comparator.comparingDouble(Student::getGpa);
Comparator<Student> byGpaDesc = byGpa.reversed();
Comparator<Student> compound = byGpa.thenComparing(byName);

// Usage
students.sort(byName);
Collections.sort(students, byGpaDesc);
```

### Comparison

| Aspect | Comparable | Comparator |
|--------|-----------|------------|
| Method | compareTo() | compare() |
| Location | In element class | Separate class |
| Ordering | Natural ordering | Custom ordering |
| Single | One per class | Multiple possible |
| Modifies class | Yes | No |

## 3. Collections.sort vs Arrays.sort

### Collections.sort

```java
List<String> list = new ArrayList<>(List.of("C", "A", "B"));
Collections.sort(list);  // [A, B, C]
list.sort(Comparator.reverseOrder());  // [C, B, A]
```

### Arrays.sort

```java
String[] array = {"C", "A", "B"};
Arrays.sort(array);  // [A, B, C]
Arrays.sort(array, Comparator.reverseOrder());  // [C, B, A]
```

### Comparison

| Aspect | Collections.sort | Arrays.sort |
|--------|-----------------|-------------|
| Input | List | Array |
| Primitive arrays | Not supported | Supported |
| Stability | Stable | Stable (objects) |
| Algorithm | TimSort | TimSort (objects), Dual-Pivot Quicksort (primitives) |

## 4. TimSort Overview

TimSort is a hybrid sorting algorithm derived from Merge Sort and Insertion Sort:

| Property | Value |
|----------|-------|
| Algorithm | Hybrid (Merge Sort + Insertion Sort) |
| Time Complexity | O(n log n) worst/average, O(n) best |
| Space Complexity | O(n) |
| Stable | Yes |
| Used in | Java Arrays.sort (objects), Python, Android |

### How TimSort Works

1. Divide array into small runs (32-64 elements)
2. Sort each run using Insertion Sort
3. Merge runs using Merge Sort
4. Exploit existing order in data

### Why TimSort is Efficient

- **Best case O(n)**: Already sorted data
- **Stable**: Preserves equal elements' order
- **Cache-friendly**: Good locality
- **Adaptive**: Fast for partially sorted data

## 5. Sorting Examples

### Basic Sorting

```java
// Natural order
List<Integer> numbers = new ArrayList<>(List.of(5, 3, 1, 4, 2));
Collections.sort(numbers);  // [1, 2, 3, 4, 5]

// Reverse order
numbers.sort(Comparator.reverseOrder());  // [5, 4, 3, 2, 1]
```

### Custom Sorting

```java
// Sort strings by length
List<String> names = List.of("Charlie", "Bob", "Alice", "Diana");
names.sort(Comparator.comparingInt(String::length));
// [Bob, Diana, Alice, Charlie]

// Sort by multiple criteria
List<Student> students = List.of(
    new Student("Alice", 3.8),
    new Student("Bob", 3.5),
    new Student("Charlie", 3.8)
);
students.sort(Comparator.comparingDouble(Student::getGpa)
                         .thenComparing(Student::getName));
```

### Map Sorting

```java
// Sort by key
Map<String, Integer> map = new TreeMap<>(unsortedMap);

// Sort by value
Map<String, Integer> sorted = map.entrySet().stream()
    .sorted(Map.Entry.comparingByValue())
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (a, b) -> a,
        LinkedHashMap::new
    ));
```

## 6. Common Mistakes

### 1. Inconsistent compareTo/equals

```java
// BAD - compareTo returns 0 but equals returns false
public int compareTo(Student other) {
    return Integer.compare(this.id, other.id);
}

// GOOD - keep compareTo consistent with equals
public int compareTo(Student other) {
    return Integer.compare(this.id, other.id);
}

@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Student)) return false;
    Student other = (Student) obj;
    return this.id == other.id;
}
```

### 2. Not Handling Nulls

```java
// BAD - NullPointerException
list.sort(Comparator.comparing(Student::getName));

// GOOD - handle nulls
list.sort(Comparator.comparing(Student::getName, 
    Comparator.nullsLast(Comparator.naturalOrder())));
```

### 3. Modifying Collection During Sort

```java
// BAD - undefined behavior
for (String s : list) {
    if (s.length() < 3) {
        list.remove(s);  // Modifying during sort
    }
}

// GOOD - filter first, then sort
list.removeIf(s -> s.length() < 3);
list.sort(Comparator.naturalOrder());
```

## 7. One-Minute Revision

- Comparable: natural ordering via compareTo()
- Comparator: custom ordering via compare()
- Collections.sort: sorts List
- Arrays.sort: sorts array
- TimSort: O(n log n) stable hybrid algorithm
- Keep compareTo consistent with equals
- Handle nulls in comparators

## 8. References

- [Oracle Java Documentation - Comparable](https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html)
- [Oracle Java Documentation - Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)
- [Effective Java - Item 12: Always override toString](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
