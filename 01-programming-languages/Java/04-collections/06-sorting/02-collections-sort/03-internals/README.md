# Collections.sort Internal Details

## How Collections.sort Works Internally

### The Collections.sort Method
```java
public static <T extends Comparable<? super T>> void sort(List<T> list)
public static <T> void sort(List<T> list, Comparator<? super T> c)
```

### Internal Implementation
1. **Convert to array**: List is converted to array for sorting
2. **TimSort**: Uses TimSort algorithm (hybrid of Merge Sort and Insertion Sort)
3. **Convert back**: Sorted array is copied back to list

### TimSort Algorithm Details
- **Stable sort**: Maintains relative order of equal elements
- **Adaptive**: Performs better on partially sorted data
- **Hybrid**: Combines Merge Sort and Insertion Sort

### Memory Behavior
- **Array conversion**: Creates temporary array of size n
- **Merge operations**: Additional O(n) memory for merging
- **Total memory**: O(n) additional memory

### Performance Characteristics
- **Best case**: O(n) - already sorted
- **Average case**: O(n log n)
- **Worst case**: O(n log n)
- **Space complexity**: O(n)

### Internal Steps
```java
// Simplified internal implementation
public static <T extends Comparable<? super T>> void sort(List<T> list) {
    Object[] a = list.toArray();  // Step 1: Convert to array
    Arrays.sort(a);               // Step 2: Sort array
    ListIterator<T> i = list.listIterator();
    for (int j = 0; j < a.length; j++) {  // Step 3: Copy back
        i.next();
        i.set((T) a[j]);
    }
}
```

### Comparison with Arrays.sort
- **Arrays.sort**: Uses Dual-Pivot Quicksort for primitives, TimSort for objects
- **Collections.sort**: Always uses TimSort
- **Memory**: Collections.sort uses more memory due to array conversion

### Stability Guarantees
- **Equal elements**: Maintain their relative order
- **Comparator sorting**: Stable if Comparator is consistent
- **Natural ordering**: Stable if Comparable is consistent with equals

### Thread Safety
- **Not thread-safe**: Collections.sort is not synchronized
- **Concurrent modification**: Throws ConcurrentModificationException
- **External synchronization**: Required for multi-threaded access

### Optimization Techniques
1. **Run detection**: Identifies existing sorted runs
2. **Merge policy**: Optimizes merge operations
3. **Insertion sort**: Uses for small subarrays
4. **Gallop mode**: Optimizes merging of similar runs