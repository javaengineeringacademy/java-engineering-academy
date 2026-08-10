# Linear Search Memory Usage

## Memory Access Pattern

```
Linear search accesses elements sequentially:

List: [A] [B] [C] [D] [E]
       ↑   ↑   ↑   ↑   ↑
      0   1   2   3   4

Access order: 0 → 1 → 2 → 3 → 4
```

## Cache Behavior

### Spatial Locality

```
Sequential access provides excellent spatial locality:

CPU Cache Line (typically 64 bytes):
┌────────────────────────────────────────────┐
│ Element 0 │ Element 1 │ Element 2 │ ...    │
└────────────────────────────────────────────┘
             ↓
When accessing Element 0, Elements 1, 2, 3... 
are also loaded into cache (prefetching)
```

### Temporal Locality

```
Poor temporal locality:
- Each element accessed only once
- No repeated access to same element
- Cache eviction may occur for large lists
```

## Memory Overhead

### Per-Element Cost

```
For ArrayList<Integer>:
- Element reference: 8 bytes (64-bit JVM)
- Integer object: 16 bytes (header + value)
- Total per element: 24 bytes

For LinkedList<Integer>:
- Element reference: 8 bytes
- Integer object: 16 bytes
- Node overhead: 24 bytes (prev + next pointers + header)
- Total per element: 48 bytes
```

### Search Overhead

```
Linear search uses O(1) extra memory:
- Loop variable: 4 bytes (int i)
- Comparison result: 1 byte (boolean)
- Total: ~5 bytes constant overhead
```

## Comparison with Binary Search

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

### ArrayList (Array-based)

```
Memory layout:
[ref0][ref1][ref2][ref3][ref4]  ← contiguous

Cache behavior:
- Excellent spatial locality
- Prefetching works well
- Fewer cache misses
```

### Linked List

```
Memory layout:
Node0 → Node1 → Node2 → Node3  ← scattered

Cache behavior:
- Poor spatial locality
- Each node may be in different cache line
- Many cache misses
```

## Memory Access Patterns

### Best Case (Target at Start)

```
Access pattern:
[Target] [?] [?] [?] [?]
    ↑
   Found!

Memory accesses: 1
Cache lines loaded: 1
```

### Worst Case (Target at End)

```
Access pattern:
[?] [?] [?] [?] [Target]
 ↑   ↑   ↑   ↑      ↑
 1   2   3   4      5

Memory accesses: n
Cache lines loaded: n/elements_per_line
```

### Average Case

```
Expected accesses: n/2
Expected cache lines: n/(2 × elements_per_line)
```

## JVM Memory Considerations

### Object Alignment

```
Java objects are aligned to 8-byte boundaries:

Integer object:
┌────────────────────────────┐
│ Mark word (8 bytes)        │
│ Klass pointer (4 bytes)    │
│ Value (4 bytes)            │
│ Padding (4 bytes)          │
└────────────────────────────┘
Total: 16 bytes (aligned to 8)
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
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

// Use:
int[] array = {1, 2, 3, 4, 5};
// Saves 16 bytes per element (no Integer objects)
```

### 2. Use Appropriate Data Structure

```java
// For small lists (< 100):
ArrayList<T> list; // Good cache locality

// For frequent insertions/deletions:
LinkedList<T> list; // Better for modifications

// For single search:
T[] array; // Minimal overhead
```

### 3. Consider Memory Pooling

```java
// Reuse lists for repeated searches
private static final List<String> SEARCH_POOL = new ArrayList<>();

public static int search(String target) {
    return linearSearch(SEARCH_POOL, target);
}
```

## Memory Profiling

### Using JMH

```java
@Benchmark
public int linearSearchBenchmark(Blackhole bh) {
    List<Integer> list = IntStream.range(0, 1000).boxed().collect(Collectors.toList());
    return linearSearch(list, 999);
}
```

### Using VisualVM

```
Memory tab:
- Shows heap usage during search
- Identifies memory allocation patterns
- Tracks garbage collection
```

## Key Takeaways

1. **Sequential access** - Excellent cache performance for small lists
2. **O(1) overhead** - Minimal extra memory needed
3. **Array vs List** - Primitive arrays save memory
4. **Cache matters** - Spatial locality reduces cache misses
5. **No preprocessing** - No extra memory for sorting/indexing