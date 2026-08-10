# Binary Search Memory Usage

## Memory Access Pattern

```
Binary search accesses memory non-sequentially:

Sorted Array: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
               ↑           ↑       ↑       ↑
              0           4       6       7
              (access order: 4 → 7 → 5 → 6)
```

## Cache Behavior

### Spatial Locality

```
Poor spatial locality - random access pattern:

CPU Cache Line (64 bytes):
┌────────────────────────────────────────────┐
│ Element 0 │ Element 1 │ Element 2 │ ...    │
└────────────────────────────────────────────┘
             ↑
When accessing Element 4, Elements 0-3 and 5-7
may NOT be in cache (depending on access pattern)
```

### Temporal Locality

```
Good temporal locality:
- Same elements may be accessed multiple times
- Small search space fits in cache
- Cache lines reused across iterations
```

## Memory Overhead

### Per-Element Cost

```
For int[] array:
- Element: 4 bytes (primitive int)
- No object overhead per element
- Total per element: 4 bytes

For Integer[] array:
- Element reference: 8 bytes (64-bit JVM)
- Integer object: 16 bytes (header + value)
- Total per element: 24 bytes
```

### Search Overhead

```
Binary search uses O(1) extra memory:
- low variable: 4 bytes (int)
- high variable: 4 bytes (int)
- mid variable: 4 bytes (int)
- Total: 12 bytes constant overhead
```

## Comparison with Linear Search

```
┌──────────────────┬─────────────┬─────────────┐
│ Aspect           │ Linear      │ Binary      │
├──────────────────┼─────────────┼─────────────┤
│ Extra memory     │ O(1)        │ O(1)        │
│ Stack space      │ O(1)        │ O(1)        │
│ Cache misses     │ Few         │ More        │
│ Branch misses    │ Few         │ More        │
│ Preprocessing    │ None        │ Sort needed │
└──────────────────┴─────────────┴─────────────┘
```

## Array vs Linked List

### Array (Required for Binary Search)

```
Memory layout:
[1][2][3][4][5][6][7][8][9][10]  ← contiguous

Binary search works because:
- Random access by index: O(1)
- Middle element accessible: arr[mid]
- Contiguous memory: cache-friendly
```

### Linked List (Cannot Use Binary Search)

```
Memory layout:
Node1 → Node2 → Node3 → Node4  ← scattered

Binary search fails because:
- No random access by index
- Must traverse to middle: O(n)
- Scattered memory: cache-unfriendly
```

## Memory Access Patterns

### Best Case (Target at Middle)

```
Access pattern:
[1] [2] [3] [4] [5] [6] [7] [8] [9] [10]
                ↑
               mid
            Found!

Memory accesses: 1
Cache lines loaded: 1
```

### Worst Case (Target at Boundary)

```
Access pattern:
[1] [2] [3] [4] [5] [6] [7] [8] [9] [10]
↑                       ↑               ↑
0                       4               9
(access order: 4 → 7 → 5 → 6 → ...)

Memory accesses: log₂(n)
Cache lines loaded: log₂(n)
```

### Average Case

```
Expected accesses: log₂(n)
Expected cache lines: log₂(n)
```

## JVM Memory Considerations

### Integer Overflow Prevention

```java
// Naive mid calculation can overflow:
int mid = (low + high) / 2;  // low + high > Integer.MAX_VALUE

// Safe mid calculation:
int mid = low + (high - low) / 2;  // No overflow
```

### Array Header

```
Array object overhead:
┌────────────────────────────┐
│ Mark word (8 bytes)        │
│ Klass pointer (4 bytes)    │
│ Length (4 bytes)           │
└────────────────────────────┘
Total: 16 bytes header
```

## Memory Optimization Tips

### 1. Use Primitive Arrays

```java
// Instead of:
Integer[] array = {1, 2, 3, 4, 5};

// Use:
int[] array = {1, 2, 3, 4, 5};
// Saves 20 bytes per element (no Integer objects)
```

### 2. Pre-sort Data

```java
// Sort once, search many times
Arrays.sort(array);  // O(n log n)
binarySearch(array, target);  // O(log n) per search
```

### 3. Use Binary Search on Sorted Views

```java
// For frequent searches on same data
SortedSet<T> sortedSet = new TreeSet<>(list);
sortedSet.contains(target);  // O(log n)
```

## Memory Profiling

### Using JMH

```java
@Benchmark
public int binarySearchBenchmark(Blackhole bh) {
    int[] array = IntStream.range(0, 1000).toArray();
    return binarySearch(array, 999);
}
```

### Using VisualVM

```
Memory tab:
- Shows heap usage during search
- Identifies memory allocation patterns
- Tracks garbage collection
```

## Comparison Table

```
┌──────────────────┬─────────────┬─────────────┬─────────────┐
│ Algorithm        │ Time        │ Space       │ Cache       │
├──────────────────┼─────────────┼─────────────┼─────────────┤
│ Linear Search    │ O(n)        │ O(1)        │ Good        │
│ Binary Search    │ O(log n)    │ O(1)        │ Poor        │
│ Hash Search      │ O(1) avg    │ O(n)        │ Good        │
│ Tree Search      │ O(log n)    │ O(1)        │ Poor        │
└──────────────────┴─────────────┴─────────────┴─────────────┘
```

## Key Takeaways

1. **Non-sequential access** - May cause cache misses
2. **O(1) overhead** - Minimal extra memory needed
3. **Primitive arrays** - Save memory vs object arrays
4. **Sort once, search many** - Amortized cost
5. **Overflow prevention** - Use safe mid calculation