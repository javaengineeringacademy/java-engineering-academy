# Linear Search Internals

## How Linear Search Works

```
Linear search examines each element sequentially until finding the target or reaching the end.

Algorithm:
1. Start at index 0
2. Compare current element with target
3. If match found, return index
4. If no match, move to next element
5. If end reached, return -1
```

## Step-by-Step Example

```
List: ["Java", "Python", "C++", "Go"]
Target: "Python"

Step 1: Compare "Java" with "Python" → No match
Step 2: Compare "Python" with "Python" → Match found at index 1
Return 1
```

## Pseudocode

```
function linearSearch(list, target):
    for i from 0 to list.size - 1:
        if list[i] equals target:
            return i
    return -1
```

## Time Complexity

```
Best Case:    O(1) - target is first element
Worst Case:   O(n) - target is last element or not present
Average Case: O(n) - target is somewhere in middle

Space Complexity: O(1) - no extra space needed
```

## Comparison with Other Searches

```
┌──────────────────┬─────────────┬─────────────┬─────────────┐
│ Algorithm        │ Best        │ Average     │ Worst       │
├──────────────────┼─────────────┼─────────────┼─────────────┤
│ Linear Search    │ O(1)        │ O(n)        │ O(n)        │
│ Binary Search    │ O(1)        │ O(log n)    │ O(log n)    │
│ Jump Search      │ O(1)        │ O(√n)       │ O(√n)       │
│ Interpolation    │ O(1)        │ O(log log n)│ O(n)        │
└──────────────────┴─────────────┴─────────────┴─────────────┘
```

## When Linear Search is Optimal

1. **Small lists** (< 100 elements) - overhead of binary search not worth it
2. **Unsorted data** - binary search requires sorted data
3. **Single search** - one-time search, no preprocessing
4. **Linked lists** - no random access for binary search
5. **Searching in streams** - data arrives sequentially

## Implementation Details

### Iterative Implementation

```java
public static <T> int linearSearch(List<T> list, T target) {
    for (int i = 0; i < list.size(); i++) {
        if (list.get(i).equals(target)) {
            return i;
        }
    }
    return -1;
}
```

### Recursive Implementation

```java
public static <T> int linearSearchRecursive(List<T> list, T target, int index) {
    if (index >= list.size()) return -1;
    if (list.get(index).equals(target)) return index;
    return linearSearchRecursive(list, target, index + 1);
}
```

## Optimizations

### Early Termination

```java
// Stop as soon as target is found
for (int i = 0; i < list.size(); i++) {
    if (list.get(i).equals(target)) return i;
    // Optional: add other conditions to skip
}
```

### Sentinel Search

```java
// Add target at end to avoid bounds checking
list.add(target);
int i = 0;
while (!list.get(i).equals(target)) i++;
list.remove(list.size() - 1); // remove sentinel
return i < list.size() - 1 ? i : -1;
```

### Transposition Search

```java
// Move found element closer to front
for (int i = 0; i < list.size(); i++) {
    if (list.get(i).equals(target)) {
        if (i > 0) {
            Collections.swap(list, i, i - 1);
            return i - 1;
        }
        return i;
    }
}
return -1;
```

## Memory Access Pattern

```
Linear search accesses memory sequentially:
Index: 0 → 1 → 2 → 3 → ... → n-1

Cache behavior:
- Good spatial locality (sequential access)
- Prefetching works well
- Cache-friendly for small arrays
- May cause cache misses for large arrays (cache line size)

CPU branch prediction:
- Predictable pattern (always increment)
- Branch predictor learns quickly
- Minimal branch mispredictions
```

## Thread Safety

Linear search is **stateless** - no shared mutable state:
- Safe for concurrent reads
- Can be parallelized (split list, search in parallel)
- No synchronization needed for read-only access

## Key Implementation Details

1. **Generic implementation** - Works with any Comparable type
2. **Null handling** - equals() may throw NullPointerException
3. **Equality semantics** - Uses equals(), not ==
4. **Return value** - First occurrence index, or -1 if not found
5. **Stable** - Returns first occurrence if duplicates exist