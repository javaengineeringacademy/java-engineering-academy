# Arrays Memory Model

## Heap Allocation

Arrays are always allocated on the heap, even when they contain primitives.

### Single-Dimensional Array Memory

```java
int[] arr = new int[100];

// Memory allocation:
// Object header: 16 bytes
// Length field: 4 bytes
// Padding: 4 bytes
// Elements: 100 × 4 bytes = 400 bytes
// Total: 424 bytes on heap
```

### Multi-Dimensional Array Memory

```java
int[][] matrix = new int[3][4];

// Memory layout:
// matrix reference: 8 bytes (on stack)
// matrix object: 16 + 4 + 4 + 3×8 = 48 bytes (on heap)
// matrix[0]: 16 + 4 + 4 + 4×4 = 40 bytes (on heap)
// matrix[1]: 16 + 4 + 4 + 4×4 = 40 bytes (on heap)
// matrix[2]: 16 + 4 + 4 + 4×4 = 40 bytes (on heap)
// Total: 8 + 48 + 120 = 176 bytes
```

### Jagged Array Memory

```java
int[][] jagged = new int[3][];
jagged[0] = new int[2];
jagged[1] = new int[5];
jagged[2] = new int[1];

// Each row can have different length
// Less memory waste than rectangular arrays
```

### Array Reference vs Object

```java
int[] arr = {1, 2, 3};

// Stack: arr (8 bytes reference on 64-bit JVM)
// Heap: array object (16 + 4 + 4 + 3×4 = 32 bytes)
```

### Autoboxing Array Memory

```java
Integer[] boxed = {1, 2, 3, 4, 5};

// Heap layout:
// Array object: 16 + 4 + 4 + 5×8 = 64 bytes
// Integer objects: 5 × 16 = 80 bytes (on heap)
// Total: 144 bytes

// vs primitive array:
int[] primitive = {1, 2, 3, 4, 5};
// Total: 16 + 4 + 4 + 20 = 44 bytes
```

### Array Copy Memory

```java
int[] original = {1, 2, 3, 4, 5};
int[] copy = Arrays.copyOf(original, original.length);

// Two separate array objects on heap
// Original: 44 bytes
// Copy: 44 bytes
// Total: 88 bytes
```

### Memory-Efficient Patterns

```java
// Use byte[] for flags (1 byte vs 4 bytes for int[])
boolean[] flags = new boolean[1000]; // ~1000 bytes
int[] intFlags = new int[1000];       // ~4000 bytes

// Use arrays of primitives for large datasets
double[] measurements = new double[1_000_000]; // ~8MB
Double[] boxedMeasurements = new Double[1_000_000]; // ~16MB + object overhead
```
