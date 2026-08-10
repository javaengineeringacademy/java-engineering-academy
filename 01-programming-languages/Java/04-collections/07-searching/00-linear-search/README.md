# Linear Search

## Scope

This folder focuses exclusively on linear search algorithm.
Examples, exercises, and quizzes in this folder cover only linear search concepts.

## Why It Exists

Before efficient search algorithms, finding elements required:

1. **Manual iteration**: Developers wrote loops each time
2. **No standard approach**: Each search was custom code
3. **Error-prone**: Easy to miss elements or go out of bounds
4. **No complexity awareness**: No understanding of performance implications

Linear search is the simplest search algorithm — check each element until found.

## What It Is

Linear search (sequential search) examines each element in order until the target is found or the list ends.

```java
public static int linearSearch(int[] arr, int target) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == target) {
            return i;
        }
    }
    return -1;
}
```

## Internal Working

```
Search for value 3 in array [1, 3, 5, 7, 9]:

Step 1: Compare arr[0]=1 with 3 → No match
Step 2: Compare arr[1]=3 with 3 → Match found!
Return index 1

Search for value 4 in array [1, 3, 5, 7, 9]:

Step 1: Compare arr[0]=1 with 4 → No match
Step 2: Compare arr[1]=3 with 4 → No match
Step 3: Compare arr[2]=5 with 4 → No match
Step 4: Compare arr[3]=7 with 4 → No match
Step 5: Compare arr[4]=9 with 4 → No match
Return -1 (not found)
```

### Search Pattern

```
Linear search checks elements sequentially:
┌─────┬─────┬─────┬─────┬─────┐
│  1  │  3  │  5  │  7  │  9  │
└──┬──┴─────┴─────┴─────┴─────┘
   │
   ▼
Check arr[0] → Not 3
         │
         ▼
      Check arr[1] → Found 3!
```

## Constructors

Linear search is a function, not a class:

```java
// Simple implementation
public static <T> int linearSearch(T[] array, T target) {
    for (int i = 0; i < array.length; i++) {
        if (array[i].equals(target)) {
            return i;
        }
    }
    return -1;
}
```

## Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| linearSearch(array, target) | Find element in array | O(n) |
| linearSearch(list, target) | Find element in list | O(n) |

## Complexity Table

| Case | Time | Space |
|------|------|-------|
| Best (first element) | O(1) | O(1) |
| Average | O(n/2) ≈ O(n) | O(1) |
| Worst (last element or not found) | O(n) | O(1) |
| Total | O(n) | O(1) |

## Thread Safety

Linear search is inherently thread-safe:

```java
// Thread-safe: no shared state
public static <T> int linearSearch(T[] array, T target) {
    for (int i = 0; i < array.length; i++) {
        if (array[i].equals(target)) {
            return i;
        }
    }
    return -1;
}
```

## Memory Behavior

```
Linear search memory usage:
- Input array: O(n) — already exists
- Local variables: O(1) — index, target
- Total extra memory: O(1)

No auxiliary data structures created.
```

## Production Incidents

1. **Off-by-one error**: Loop goes to `i <= array.length`
   - Result: ArrayIndexOutOfBoundsException
   - Fix: Use `i < array.length`

2. **Null pointer exception**: Comparing null values
   - Result: NullPointerException
   - Fix: Check for null before comparison

3. **String comparison with ==**: Using == instead of equals()
   - Result: Reference comparison, not value comparison
   - Fix: Use `.equals()` for object comparison

## Engineering Decision Framework

### When Should I Use This?

- Small datasets (< 100 elements)
- Unsorted data
- Simple implementation needed
- Data is accessed sequentially anyway
- Searching for multiple criteria

### When Should I NOT Use This?

- Large datasets (> 1000 elements)
- Sorted data (use binary search)
- Frequent searches (build index)
- Performance-critical code

### What Are the Alternatives?

| Algorithm | Time | Requirement | Use When |
|-----------|------|-------------|----------|
| Linear Search | O(n) | None | Small/unsorted data |
| Binary Search | O(log n) | Sorted | Large sorted data |
| Hash Lookup | O(1) | Hash table | Frequent lookups |

### Common Code Review Comments

1. "Consider using binary search for sorted data"
2. "Handle null values in search"
3. "Use enhanced for-loop for readability"
4. "Consider using indexOf() for lists"

## Performance

| Operation | Linear Search | Binary Search |
|-----------|---------------|---------------|
| Best case | O(1) | O(1) |
| Average case | O(n) | O(log n) |
| Worst case | O(n) | O(log n) |
| Preparation | None | Sort required |
| Memory | O(1) | O(1) |

## Debugging Tips

1. **Element not found**: Check if array is sorted — binary search requires sorted data
2. **Wrong index returned**: Check loop bounds
3. **NullPointerException**: Add null checks
4. **Infinite loop**: Ensure loop terminates (usually off-by-one error)

## Code Review Checklist

- [ ] Loop bounds are correct (0 to length-1)
- [ ] Null values handled
- [ ] Correct comparison method used (equals vs ==)
- [ ] Return -1 when not found
- [ ] Consider using built-in indexOf()

## Architecture Considerations

- Where it fits in system design: Simple search for small datasets
- Integration patterns: Used internally by List.indexOf(), List.contains()
- Scaling considerations: O(n) — use for small data only

## Security Considerations

- Linear search is safe — no data modification
- Ensure search terminates — no infinite loops
- Handle null inputs gracefully

## Evolution & Modernization

| Version | Change |
|---------|--------|
| JDK 1.0 | Arrays.asList().indexOf() uses linear search |
| JDK 1.2 | Collections.indexOf() uses linear search |
| JDK 8 | Stream.filter().findFirst() uses linear search |

```java
// Modern: Use stream API
int index = IntStream.range(0, array.length)
    .filter(i -> array[i] == target)
    .findFirst()
    .orElse(-1);
```

## Version Validation

```java
// Test in Java 21
int[] arr = {1, 3, 5, 7, 9};
assert linearSearch(arr, 3) == 1;
assert linearSearch(arr, 4) == -1;
```

## Best Practices

1. Use enhanced for-loop for readability
2. Handle null values
3. Consider using indexOf() for List
4. Use binary search for sorted data

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Off-by-one | ArrayIndexOutOfBoundsException | Use i < array.length |
| == comparison | Reference comparison | Use .equals() |
| No null check | NullPointerException | Add null check |
| Sorted data | Wasted O(n) time | Use binary search |

## Common Myths

1. "Linear search is always slow" — False. For small data, it's faster than binary search due to simplicity.
2. "Binary search is always better" — False. Binary search requires sorted data and has higher overhead.
3. "Linear search is deprecated" — False. It's still used for small datasets and simple lookups.

## One-Minute Revision

```
Linear search checks each element sequentially.

Key points:
- Time: O(n)
- Space: O(1)
- Works on unsorted data
- Simple implementation
- Used by List.indexOf()

When to use:
- Small datasets
- Unsorted data
- Simple implementation needed

When NOT to use:
- Large datasets
- Sorted data
- Frequent searches
```

## Related Topics

| Topic | Relationship |
|-------|--------------|
| Binary Search | More efficient for sorted data |
| indexOf() | Uses linear search internally |
| contains() | Uses linear search internally |

## Interview Questions

1. What is the time complexity of linear search?
2. When would you use linear search over binary search?
3. How do you handle null values in linear search?
4. What is the space complexity of linear search?
5. How do you implement linear search for a list?
6. What is the best case for linear search?
7. How do you search for multiple criteria?
8. What is the difference between linear search and binary search?

## References

- Oracle Java SE Documentation: Collections
- OpenJDK Source: java/util/AbstractList.java
- Algorithms, 4th Edition (Sedgewick): Chapter 1.1
- Introduction to Algorithms (CLRS): Chapter 2.1
