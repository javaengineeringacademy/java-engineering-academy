# Binary Search

## Scope

This folder focuses exclusively on binary search algorithm.
Examples, exercises, and quizzes in this folder cover only binary search concepts.

## Why It Exists

Before binary search, finding elements in sorted data required:

1. **Linear scan**: O(n) even though data is sorted
2. **No exploitation of order**: Didn't use sorted property
3. **Slow for large datasets**: Performance degraded linearly
4. **Redundant comparisons**: Checked every element

Binary search solves this by eliminating half the remaining elements at each step.

## What It Is

Binary search finds elements in sorted arrays by repeatedly dividing the search interval in half.

```java
public static int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}
```

## Internal Working

```
Search for value 7 in sorted array [1, 3, 5, 7, 9]:

Step 1: left=0, right=4, mid=2 → arr[2]=5 < 7 → left=3
Step 2: left=3, right=4, mid=3 → arr[3]=7 == 7 → Return 3

Search for value 4 in sorted array [1, 3, 5, 7, 9]:

Step 1: left=0, right=4, mid=2 → arr[2]=5 > 4 → right=1
Step 2: left=0, right=1, mid=0 → arr[0]=1 < 4 → left=1
Step 3: left=1, right=1, mid=1 → arr[1]=3 < 4 → left=2
left > right → Return -1 (not found)
```

### Binary Search Pattern

```
Binary search eliminates half at each step:
┌─────────────────────────────────────┐
│  1  3  5  7  9  11  13  15  17  19 │
└─────────────────────────────────────┘
         ▲
         │
      mid=9 → 9 > 7 → search left half

┌──────────────┐
│  1  3  5  7  │
└──────────────┘
      ▲
      │
   mid=3 → 3 < 7 → search right half

┌──────────────┐
│      5  7    │
└──────────────┘
      ▲
      │
   mid=5 → 5 < 7 → search right half

┌──────────────┐
│         7    │
└──────────────┘
      ▲
      │
   mid=7 → Found!
```

## Constructors

Binary search is a function, not a class:

```java
// Iterative implementation
public static <T extends Comparable<T>> int binarySearch(T[] arr, T target) {
    int left = 0, right = arr.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        int cmp = arr[mid].compareTo(target);
        if (cmp == 0) return mid;
        else if (cmp < 0) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}

// Recursive implementation
public static <T extends Comparable<T>> int binarySearchRecursive(T[] arr, T target, int left, int right) {
    if (left > right) return -1;
    int mid = left + (right - left) / 2;
    int cmp = arr[mid].compareTo(target);
    if (cmp == 0) return mid;
    else if (cmp < 0) return binarySearchRecursive(arr, target, mid + 1, right);
    else return binarySearchRecursive(arr, target, left, mid - 1);
}
```

## Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| binarySearch(array, target) | Find element in sorted array | O(log n) |
| binarySearch(list, target) | Find element in sorted list | O(log n) |

## Complexity Table

| Case | Time | Space |
|------|------|-------|
| Best (middle element) | O(1) | O(1) iterative, O(log n) recursive |
| Average | O(log n) | O(1) iterative, O(log n) recursive |
| Worst (not found) | O(log n) | O(1) iterative, O(log n) recursive |
| Total | O(log n) | O(1) iterative |

## Thread Safety

Binary search is inherently thread-safe for read-only operations:

```java
// Thread-safe: no shared state
public static <T> int binarySearch(T[] arr, T target) {
    int left = 0, right = arr.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid].equals(target)) return mid;
        else if (((Comparable) arr[mid]).compareTo(target) < 0) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}
```

## Memory Behavior

```
Binary search memory usage:
- Input array: O(n) — already exists
- Local variables: O(1) — left, right, mid
- Total extra memory: O(1) iterative, O(log n) recursive

No auxiliary data structures created.
```

## Production Incidents

1. **Integer overflow in mid calculation**: `(left + right) / 2` overflows
   - Result: Incorrect mid index, infinite loop
   - Fix: Use `left + (right - left) / 2`

2. **Array not sorted**: Binary search on unsorted array
   - Result: Incorrect results
   - Fix: Sort array first or use linear search

3. **Off-by-one errors**: Wrong loop condition or mid adjustment
   - Result: Infinite loop or missed elements
   - Fix: Test with edge cases

4. **Recursive stack overflow**: Very large arrays
   - Result: StackOverflowError
   - Fix: Use iterative implementation

## Engineering Decision Framework

### When Should I Use This?

- Large sorted datasets (> 1000 elements)
- Frequent searches on same data
- Performance-critical code
- Data is sorted or can be sorted

### When Should I NOT Use This?

- Unsorted data (use linear search)
- Small datasets (< 100 elements)
- Data changes frequently (re-sorting overhead)
- Need to search by multiple criteria

### What Are the Alternatives?

| Algorithm | Time | Requirement | Use When |
|-----------|------|-------------|----------|
| Linear Search | O(n) | None | Small/unsorted data |
| Binary Search | O(log n) | Sorted | Large sorted data |
| Hash Lookup | O(1) | Hash table | Frequent lookups |
| Interpolation Search | O(log log n) | Uniform distribution | Uniform data |

### Common Code Review Comments

1. "Use left + (right - left) / 2 to avoid overflow"
2. "Ensure array is sorted before binary search"
3. "Consider using Arrays.binarySearch() or Collections.binarySearch()"
4. "Test with edge cases: empty array, single element, duplicates"

## Performance

| Operation | Linear Search | Binary Search |
|-----------|---------------|---------------|
| Best case | O(1) | O(1) |
| Average case | O(n) | O(log n) |
| Worst case | O(n) | O(log n) |
| Preparation | None | Sort required |
| Memory | O(1) | O(1) |

## Debugging Tips

1. **Infinite loop**: Check mid calculation and loop bounds
2. **Wrong index**: Verify array is sorted
3. **Integer overflow**: Use left + (right - left) / 2
4. **Element not found**: Check if duplicates exist (binary search finds any match)

## Code Review Checklist

- [ ] Array is sorted before binary search
- [ ] Mid calculation avoids overflow (left + (right - left) / 2)
- [ ] Loop condition is correct (left <= right)
- [ ] Mid adjustment is correct (left = mid + 1, right = mid - 1)
- [ ] Edge cases handled (empty array, single element)

## Architecture Considerations

- Where it fits in system design: Core search algorithm for sorted data
- Integration patterns: Used by Arrays.binarySearch(), Collections.binarySearch()
- Scaling considerations: O(log n) — scales well to billions of elements

## Security Considerations

- Binary search is safe — no data modification
- Ensure search terminates — no infinite loops
- Handle null inputs gracefully

## Evolution & Modernization

| Version | Change |
|---------|--------|
| JDK 1.0 | Arrays.binarySearch() introduced |
| JDK 1.2 | Collections.binarySearch() introduced |
| JDK 1.8 | Primitive-specific binarySearch methods |

```java
// Modern: Use built-in methods
int index = Arrays.binarySearch(array, target);
int index = Collections.binarySearch(list, target);
```

## Version Validation

```java
// Test in Java 21
int[] arr = {1, 3, 5, 7, 9};
assert binarySearch(arr, 7) == 3;
assert binarySearch(arr, 4) == -1;
```

## Best Practices

1. Ensure array is sorted before binary search
2. Use left + (right - left) / 2 to avoid overflow
3. Use built-in Arrays.binarySearch() when possible
4. Consider iterative over recursive for large arrays

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Unsorted array | Incorrect results | Sort first or use linear search |
| Integer overflow | Wrong mid index | Use left + (right - left) / 2 |
| Wrong loop condition | Infinite loop | Use left <= right |
| Wrong mid adjustment | Missed elements | left = mid + 1, right = mid - 1 |

## Common Myths

1. "Binary search is always faster" — False. For small data, linear search can be faster due to lower overhead.
2. "Binary search requires recursion" — False. Iterative version is often preferred.
3. "Binary search finds the first occurrence" — False. It finds any match; modifications needed for first/last occurrence.

## One-Minute Revision

```
Binary search finds elements in sorted arrays by halving search space.

Key points:
- Time: O(log n)
- Space: O(1) iterative
- Requires sorted data
- Eliminates half at each step
- Used by Arrays.binarySearch()

When to use:
- Large sorted datasets
- Frequent searches
- Performance-critical code

When NOT to use:
- Unsorted data
- Small datasets
- Data changes frequently
```

## Related Topics

| Topic | Relationship |
|-------|--------------|
| Linear Search | Simpler but slower for sorted data |
| Arrays.binarySearch() | Built-in implementation |
| Collections.binarySearch() | Built-in for lists |
| Interpolation Search | Faster for uniform data |

## Interview Questions

1. What is the time complexity of binary search?
2. Why does binary search require sorted data?
3. How do you avoid integer overflow in mid calculation?
4. What is the difference between iterative and recursive binary search?
5. How do you find the first occurrence of an element?
6. What is the space complexity of binary search?
7. When would you use linear search over binary search?
8. How does binary search work with duplicates?

## References

- Oracle Java SE Documentation: Arrays
- OpenJDK Source: java/util/Arrays.java
- Algorithms, 4th Edition (Sedgewick): Chapter 1.4
- Introduction to Algorithms (CLRS): Chapter 2.3
