# When to Use Arrays

## Decision Guide

### Arrays vs Collections

| Use Arrays When | Use Collections When |
|-----------------|---------------------|
| Fixed size known at compile time | Dynamic sizing needed |
| Performance-critical access | Rich API needed (add, remove, contains) |
| Multi-dimensional data | Generic type safety |
| Low memory overhead required | Frequent insertions/deletions |
| Interfacing with legacy code | Need List, Set, or Map behavior |

### Array Type Selection

| Type | Use When | Memory |
|------|----------|--------|
| `int[]` | Most integer data | 4 bytes/element |
| `long[]` | Large integers | 8 bytes/element |
| `double[]` | Decimal calculations | 8 bytes/element |
| `boolean[]` | Flags, bitmasks | 1 byte/element |
| `byte[]` | Raw data, I/O | 1 byte/element |
| `char[]` | Character data (faster than String) | 2 bytes/element |
| `String[]` | Fixed text data | 8 bytes/element + objects |

### Multi-Dimensional Arrays

| Use When | Example |
|----------|---------|
| Grid/matrix data | `int[][] matrix = new int[3][3];` |
| Jagged arrays | `int[][] jagged = new int[3][];` |
| Image processing | `int[][] pixels = new int[width][height];` |
| Game boards | `char[][] board = new char[8][8];` |

### Array Operations Decision Tree

| Operation | Method | Time |
|-----------|--------|------|
| Access by index | `arr[i]` | O(1) |
| Search unsorted | `linearSearch()` | O(n) |
| Search sorted | `Arrays.binarySearch()` | O(log n) |
| Sort | `Arrays.sort()` | O(n log n) |
| Copy | `Arrays.copyOf()` | O(n) |
| Fill | `Arrays.fill()` | O(n) |

## Production Guidelines

### Defensive Copying
```java
public class Service {
    private final int[] data;

    public Service(int[] data) {
        // Defensive copy prevents external mutation
        this.data = Arrays.copyOf(data, data.length);
    }

    public int[] getData() {
        // Return copy to prevent internal mutation
        return Arrays.copyOf(data, data.length);
    }
}
```

### Null-Safe Array Operations
```java
public static boolean contains(int[] arr, int target) {
    if (arr == null) return false;
    for (int val : arr) {
        if (val == target) return true;
    }
    return false;
}
```

### Array Bounds Checking
```java
public static int safeGet(int[] arr, int index) {
    if (arr == null) throw new IllegalArgumentException("Array is null");
    if (index < 0 || index >= arr.length) {
        throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + arr.length);
    }
    return arr[index];
}
```
