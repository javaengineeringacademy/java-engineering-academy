# Collections.indexOf Memory Usage

## Memory Access Pattern

```
Collections.indexOf accesses memory sequentially:

ArrayList:
[ref0][ref1][ref2][ref3][ref4]  ← contiguous
  ↑    ↑    ↑    ↑    ↑
 0    1    2    3    4

LinkedList:
Node0 → Node1 → Node2 → Node3  ← scattered
  ↑       ↑       ↑       ↑
 item    item    item    item
```

## Cache Behavior

### ArrayList (Good)

```
Excellent spatial locality:
- Elements stored contiguously
- Prefetching works well
- Few cache misses
- Cache line holds multiple elements
```

### LinkedList (Poor)

```
Poor spatial locality:
- Nodes scattered in memory
- Each node may be in different cache line
- Many cache misses
- No prefetching benefit
```

## Memory Overhead

### Per-Element Cost

```
ArrayList<Integer>:
- Element reference: 8 bytes (64-bit JVM)
- Integer object: 16 bytes (header + value)
- Total per element: 24 bytes

LinkedList<Integer>:
- Element reference: 8 bytes
- Integer object: 16 bytes
- Node overhead: 24 bytes (prev + next pointers + header)
- Total per element: 48 bytes
```

### Search Overhead

```
indexOf uses O(1) extra memory:
- Loop variable: 4 bytes (int i)
- Comparison result: 1 byte (boolean)
- Total: ~5 bytes constant overhead
```

## Comparison with Binary Search

```
┌──────────────────┬─────────────┬─────────────┐
│ Aspect           │ indexOf     │ binarySearch│
├──────────────────┼─────────────┼─────────────┤
│ Extra memory     │ O(1)        │ O(1)        │
│ Stack space      │ O(1)        │ O(1)        │
│ Cache misses     │ Few (ArrayList) │ More    │
│ Branch misses    │ Few         │ More        │
│ Preprocessing    │ None        │ Sort needed │
└──────────────────┴─────────────┴─────────────┘
```

## Memory Access Patterns

### Best Case (Target at Start)

```
ArrayList access:
[Target] [?] [?] [?] [?]
    ↑
   Found!

Memory accesses: 1
Cache lines loaded: 1
```

### Worst Case (Target at End)

```
ArrayList access:
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

### 1. Use HashSet for Frequent Searches

```java
// Instead of:
list.contains(target);  // O(n)

// Use:
Set<T> set = new HashSet<>(list);  // O(n) one-time
set.contains(target);  // O(1) per search
```

### 2. Use Binary Search on Sorted Lists

```java
// Instead of:
list.indexOf(target);  // O(n)

// Use:
Collections.sort(list);  // O(n log n) one-time
Collections.binarySearch(list, target);  // O(log n) per search
```

### 3. Use Primitive Arrays for Performance

```java
// Instead of:
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
list.indexOf(3);  // O(n)

// Use:
int[] array = {1, 2, 3, 4, 5};
linearSearch(array, 3);  // O(n) but faster
```

## Memory Profiling

### Using JMH

```java
@Benchmark
public int indexOfBenchmark(Blackhole bh) {
    List<Integer> list = IntStream.range(0, 1000).boxed().collect(Collectors.toList());
    return list.indexOf(999);
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
│ indexOf (ArrayList)│ O(n)      │ O(1)        │ Good        │
│ indexOf (LinkedList)│ O(n)    │ O(1)        │ Poor        │
│ binarySearch     │ O(log n)    │ O(1)        │ Poor        │
│ HashSet.contains │ O(1)        │ O(n)        │ Good        │
└──────────────────┴─────────────┴─────────────┴─────────────┘
```

## Key Takeaways

1. **Sequential access** - Good cache performance for ArrayList
2. **O(1) overhead** - Minimal extra memory needed
3. **ArrayList vs LinkedList** - ArrayList has better cache performance
4. **HashSet for frequent searches** - Amortized O(1) contains
5. **Binary search for sorted data** - O(log n) per search