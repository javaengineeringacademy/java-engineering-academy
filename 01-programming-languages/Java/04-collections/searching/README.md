# Searching

## 1. What Is Searching

Searching is the process of finding a specific element in a collection. Java provides multiple ways to search collections, each with different performance characteristics depending on the data structure.

## 2. Linear vs Binary Search

### Linear Search

```java
// O(n) - checks each element sequentially
int index = Collections.indexOf(list, target);
boolean contains = Collections.frequency(list, target) > 0;

// Manual implementation
for (int i = 0; i < list.size(); i++) {
    if (list.get(i).equals(target)) {
        return i;  // Found
    }
}
return -1;  // Not found
```

### Binary Search

```java
// O(log n) - requires sorted list
int index = Collections.binarySearch(sortedList, target);

// Manual implementation
int low = 0;
int high = list.size() - 1;
while (low <= high) {
    int mid = (low + high) >>> 1;
    Comparable midVal = list.get(mid);
    int cmp = midVal.compareTo(target);
    if (cmp < 0) low = mid + 1;
    else if (cmp > 0) high = mid - 1;
    else return mid;  // Found
}
return -(low + 1);  // Not found (insertion point)
```

### Comparison

| Aspect | Linear Search | Binary Search |
|--------|--------------|---------------|
| Time Complexity | O(n) | O(log n) |
| Requirement | None | Sorted collection |
| Space Complexity | O(1) | O(1) |
| Best for | Unsorted, small | Sorted, large |

## 3. Collections.indexOf and Collections.binarySearch

### Collections.indexOf

```java
List<String> list = List.of("Alice", "Bob", "Charlie", "Diana");
int index = Collections.indexOf(list, "Charlie");  // 2
int lastIndex = Collections.lastIndexOf(list, "Charlie");  // 2
```

### Collections.binarySearch

```java
List<String> sortedList = List.of("Alice", "Bob", "Charlie", "Diana");
int index = Collections.binarySearch(sortedList, "Charlie");  // 2
int index2 = Collections.binarySearch(sortedList, "Eve");  // -(insertion point) - 1
```

### With Comparator

```java
List<String> list = List.of("Charlie", "Bob", "Alice", "Diana");
Comparator<String> byLength = Comparator.comparingInt(String::length);
int index = Collections.binarySearch(list, "Bob", byLength);
```

## 4. Searching in Different Collections

| Collection | Linear | Binary | Contains |
|------------|--------|--------|----------|
| ArrayList | O(n) | O(log n) | O(n) |
| LinkedList | O(n) | O(n log n) | O(n) |
| HashSet | O(1) | N/A | O(1) |
| TreeSet | O(log n) | O(log n) | O(log n) |
| HashMap | O(1) | N/A | O(1) |
| TreeMap | O(log n) | O(log n) | O(log n) |

## 5. Searching Examples

### Basic Searching

```java
// Linear search
List<String> list = List.of("Alice", "Bob", "Charlie", "Diana");
int index = list.indexOf("Bob");  // 1
boolean contains = list.contains("Bob");  // true

// Binary search (list must be sorted)
List<String> sorted = new ArrayList<>(list);
Collections.sort(sorted);
int index2 = Collections.binarySearch(sorted, "Bob");  // 1
```

### Searching with Custom Comparator

```java
List<Student> students = List.of(
    new Student("Alice", 3.8),
    new Student("Bob", 3.5),
    new Student("Charlie", 3.9)
);

// Sort by GPA
students.sort(Comparator.comparingDouble(Student::getGpa));

// Search by GPA
Student target = new Student("", 3.5);
int index = Collections.binarySearch(students, target,
    Comparator.comparingDouble(Student::getGpa));
```

### Searching in Map

```java
Map<String, Integer> map = Map.of("Alice", 1, "Bob", 2, "Charlie", 3);

// Check if key exists
boolean hasKey = map.containsKey("Bob");  // true

// Check if value exists
boolean hasValue = map.containsValue(2);  // true

// Get with default
int value = map.getOrDefault("Eve", 0);  // 0
```

## 6. Common Mistakes

### 1. Binary Search on Unsorted List

```java
// BAD - undefined behavior
List<Integer> list = List.of(5, 3, 1, 4, 2);
int index = Collections.binarySearch(list, 3);  // Undefined

// GOOD - sort first
List<Integer> sorted = new ArrayList<>(list);
Collections.sort(sorted);
int index2 = Collections.binarySearch(sorted, 3);  // Correct
```

### 2. Using indexOf When ContainsKey Needed

```java
// BAD - O(n) search
if (list.indexOf(key) >= 0) {
    return map.get(key);
}

// GOOD - O(1) lookup
return map.getOrDefault(key, defaultValue);
```

### 3. Not Handling Negative Return from binarySearch

```java
// BAD - assumes found
int index = Collections.binarySearch(sortedList, target);
String element = sortedList.get(index);  // May throw IndexOutOfBoundsException

// GOOD - check return value
int index = Collections.binarySearch(sortedList, target);
if (index >= 0) {
    String element = sortedList.get(index);
} else {
    // Not found, insertion point is -(index + 1)
}
```

## 7. One-Minute Revision

- Linear search: O(n), no requirements
- Binary search: O(log n), requires sorted collection
- Collections.indexOf: linear search
- Collections.binarySearch: binary search (sorted required)
- Use HashSet/HashMap for O(1) membership testing
- Handle negative return from binarySearch
- Sort before binary search

## 8. References

- [Oracle Java Documentation - Collections](https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Effective Java - Item 54: Prefer interfaces to reflection](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
