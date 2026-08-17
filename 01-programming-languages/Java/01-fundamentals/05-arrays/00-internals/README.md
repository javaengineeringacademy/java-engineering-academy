# Arrays Internals

## How Java Stores Arrays

### Array Object Layout

Arrays are objects on the heap with a special header:

```
Array Object Layout:
┌──────────────────────────┐
│ Object Header (16 bytes) │ ← Mark word + class pointer
│ Length field (4 bytes)   │ ← Array length (fixed)
│ Padding (4 bytes)        │ ← Alignment to 8-byte boundary
│ Element 0                │ ← First element
│ Element 1                │ ← Second element
│ ...                      │
│ Element n-1              │ ← Last element
└──────────────────────────┘
```

### Array Allocation

```java
int[] arr = new int[10];

// Bytecode:
bipush 10      // Push array size
newarray T_INT // Allocate new int array on heap
astore_1       // Store reference in local variable 1
```

### Multi-Dimensional Arrays

```java
int[][] matrix = new int[3][4];

// This creates an array of arrays:
// matrix → [0] → int[4] on heap
//          [1] → int[4] on heap
//          [2] → int[4] on heap
```

### Array Bounds Checking

Java performs bounds checking on every access:

```java
int[] arr = {1, 2, 3};
int x = arr[5]; // Throws ArrayIndexOutOfBoundsException

// Bytecode includes bounds check:
// aload_1      // push array reference
// iconst_5     // push index
// iaload       // includes bounds check before access
```

### Array Copy Internals

```java
int[] copy = Arrays.copyOf(original, original.length);

// Native System.arraycopy() for performance:
// - Uses native memory copy (memcpy equivalent)
// - Much faster than manual loop copy
// - Handles overlapping regions correctly
```

### Array Memory Layout

```java
int[] arr = {1, 2, 3, 4, 5};

// Heap layout:
// Address 0x00: [Object Header - 16 bytes]
// Address 0x10: [Length - 4 bytes]
// Address 0x14: [Padding - 4 bytes]
// Address 0x18: [Element 0 - 4 bytes]
// Address 0x1C: [Element 1 - 4 bytes]
// Address 0x20: [Element 2 - 4 bytes]
// Address 0x24: [Element 3 - 4 bytes]
// Address 0x28: [Element 4 - 4 bytes]
// Total: 40 bytes
```

### Array Iteration Bytecode

```java
// For each loop
for (int val : arr) {
    process(val);
}

// Bytecode:
// aload_1              // push array
// invokevirtual length // get array length
// iconst_0             // i = 0
// goto L1
// L0: iaload           // load arr[i]
//     invokestatic process()
//     iinc 1           // i++
// L1: if_icmplt L0    // if i < length, continue
```
