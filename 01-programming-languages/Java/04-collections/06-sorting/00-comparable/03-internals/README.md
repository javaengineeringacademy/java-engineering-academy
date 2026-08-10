# Comparable Internal Details

## How Comparable Works Internally

### The Comparable Interface
The `Comparable<T>` interface defines a single method:
```java
public int compareTo(T o);
```

### Comparison Logic
- Returns negative integer if `this < o`
- Returns zero if `this == o`
- Returns positive integer if `this > o`

### Internal Sorting Algorithm
When you call `Collections.sort()` on a list of Comparable objects:
1. Java uses **TimSort** (a hybrid of Merge Sort and Insertion Sort)
2. TimSort is a stable, adaptive sorting algorithm
3. It exploits existing order in the data for better performance

### Memory Behavior
- **In-place sorting**: TimSort uses O(n) additional memory
- **Stability**: Equal elements maintain their relative order
- **Adaptive**: Performs better on partially sorted data

### Performance Characteristics
- **Best case**: O(n) - already sorted
- **Average case**: O(n log n)
- **Worst case**: O(n log n)
- **Space complexity**: O(n)

### Implementation Details
TimSort works by:
1. Dividing the array into small chunks called "runs"
2. Each run is sorted using Insertion Sort
3. Runs are then merged using a modified Merge Sort
4. The algorithm maintains a stack of pending runs to be merged

### Example Implementation
```java
public class ComparableExample implements Comparable<ComparableExample> {
    private int value;
    
    public int compareTo(ComparableExample other) {
        return this.value - other.value; // Natural ordering
    }
}
```

### Common Pitfalls
1. **Null values**: Comparable implementations should handle nulls
2. **Overflow**: Be careful with integer subtraction in compareTo
3. **Consistency**: compareTo should be consistent with equals
4. **Transitivity**: Must be transitive (if a < b and b < c, then a < c)