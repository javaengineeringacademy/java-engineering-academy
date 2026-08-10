# Binary Search Internals

## How Binary Search Works

```
Binary search divides sorted array in half each iteration:

1. Compare target with middle element
2. If match, return index
3. If target < middle, search left half
4. If target > middle, search right half
5. Repeat until found or search space empty
```

## Step-by-Step Example

```
Sorted Array: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
Target: 7

Step 1: low=0, high=9, mid=4 → arr[4]=5 < 7 → search right
Step 2: low=5, high=9, mid=7 → arr[7]=8 > 7 → search left
Step 3: low=5, high=6, mid=5 → arr[5]=6 < 7 → search right
Step 4: low=6, high=6, mid=6 → arr[6]=7 = 7 → found!
Return 6
```

## Pseudocode

```
function binarySearch(array, target):
    low = 0
    high = array.length - 1
    
    while low <= high:
        mid = low + (high - low) / 2  // Avoid overflow
        if array[mid] == target:
            return mid
        else if array[mid] < target:
            low = mid + 1
        else:
            high = mid - 1
    
    return -1
```

## Time Complexity

```
Best Case:    O(1) - target is middle element
Worst Case:   O(log n) - target is at boundary or not present
Average Case: O(log n)

Space Complexity: O(1) iterative, O(log n) recursive (stack)
```

## Why mid = low + (high - low) / 2?

```
Naive: mid = (low + high) / 2
Problem: low + high can overflow for large arrays

Safe: mid = low + (high - low) / 2
Benefit: No overflow, same result

Example:
low = 1,000,000,000
high = 2,000,000,000
low + high = 3,000,000,000 > Integer.MAX_VALUE (overflow!)

low + (high - low) / 2 = 1,000,000,000 + 500,000,000 = 1,500,000,000
```

## Implementation Variants

### Iterative (Preferred)

```java
public static int binarySearch(int[] arr, int target) {
    int low = 0, high = arr.length - 1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) low = mid + 1;
        else high = mid - 1;
    }
    return -1;
}
```

### Recursive

```java
public static int binarySearchRecursive(int[] arr, int target, int low, int high) {
    if (low > high) return -1;
    int mid = low + (high - low) / 2;
    if (arr[mid] == target) return mid;
    else if (arr[mid] < target) return binarySearchRecursive(arr, target, mid + 1, high);
    else return binarySearchRecursive(arr, target, low, mid - 1);
}
```

## Handling Duplicates

### First Occurrence

```java
public static int findFirstOccurrence(int[] arr, int target) {
    int low = 0, high = arr.length - 1, result = -1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] == target) {
            result = mid;
            high = mid - 1; // Continue searching left
        } else if (arr[mid] < target) low = mid + 1;
        else high = mid - 1;
    }
    return result;
}
```

### Last Occurrence

```java
public static int findLastOccurrence(int[] arr, int target) {
    int low = 0, high = arr.length - 1, result = -1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] == target) {
            result = mid;
            low = mid + 1; // Continue searching right
        } else if (arr[mid] < target) low = mid + 1;
        else high = mid - 1;
    }
    return result;
}
```

## Search Space Visualization

```
Initial: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
          ↑                          ↑
         low                       high

Step 1: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
                      ↑
                     mid
        low = mid + 1 = 5

Step 2: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
                              ↑       ↑
                             mid    high
        high = mid - 1 = 6

Step 3: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
                          ↑   ↑
                         mid high
        low = mid + 1 = 6

Step 4: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
                          ↑
                         mid
        Found at index 6
```

## Memory Access Pattern

```
Binary search accesses memory non-sequentially:

Index access pattern: 4 → 7 → 5 → 6
                      (mid) (mid) (mid) (mid)

Cache behavior:
- Poor spatial locality (random access)
- May cause cache misses
- Each access may load new cache line
```

## Comparison with Linear Search

```
┌──────────────────┬─────────────┬─────────────┐
│ Aspect           │ Linear      │ Binary      │
├──────────────────┼─────────────┼─────────────┤
│ Time (sorted)    │ O(n)        │ O(log n)    │
│ Time (unsorted)  │ O(n)        │ O(n log n)  │
│ Space            │ O(1)        │ O(1)        │
│ Preprocessing    │ None        │ Sort needed │
│ Cache performance│ Good        │ Poor        │
│ Implementation   │ Simple      │ Moderate    │
└──────────────────┴─────────────┴─────────────┘
```

## Common Pitfalls

### 1. Integer Overflow

```java
// WRONG: Can overflow
int mid = (low + high) / 2;

// CORRECT: Safe from overflow
int mid = low + (high - low) / 2;
```

### 2. Off-by-One Errors

```java
// Use <= vs < carefully
while (low <= high)  // Include high
while (low < high)   // Exclude high
```

### 3. Not Handling Empty Arrays

```java
if (arr == null || arr.length == 0) return -1;
```

### 4. Using for Unsorted Data

```java
// Binary search requires sorted array!
// Sort first or use linear search
```

## Thread Safety

Binary search is **stateless**:
- Safe for concurrent reads
- Can be parallelized
- No synchronization needed for read-only access

## Key Implementation Details

1. **Sorted array required** - Binary search only works on sorted data
2. **Overflow prevention** - Use `low + (high - low) / 2`
3. **Mid calculation** - Integer division floors result
4. **Termination** - Loop exits when low > high
5. **Return value** - Index if found, -1 if not found