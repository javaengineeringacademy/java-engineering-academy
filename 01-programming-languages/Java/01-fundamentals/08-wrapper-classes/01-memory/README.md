# Wrapper Classes Memory Model

## Object Layout

Wrapper classes are objects on the heap with specific memory layout.

### Integer Memory Layout

```java
Integer boxed = 42;

// Memory layout:
// Stack: 8 bytes (reference)
// Heap: Integer object (~16 bytes)
//   - Object header: 12 bytes
//   - Value field: 4 bytes (int)
//   - Padding: 4 bytes (alignment)
// Total: ~24 bytes
```

### Cache Memory

```java
Integer a = 127;
Integer b = 127;

// Both reference same cached object:
// Cache: Integer[256] (for -128 to 127)
// Each cached Integer: ~16 bytes
// Total cache: ~4KB
```

### Boxing Overhead

```java
// Primitive: 4 bytes on stack
int primitive = 42;

// Boxed: 8 bytes reference + 16 bytes object = 24 bytes
Integer boxed = 42;

// In collections: Massive overhead
List<Integer> list = new ArrayList<>();
list.add(42);  // Creates new Integer object for each element!
```

### Autoboxing Memory

```java
Integer sum = 0;
for (int i = 0; i < 1000; i++) {
    sum += i;  // Creates new Integer object each iteration!
}

// Memory: 1000 Integer objects created and garbage collected
// Use int sum = 0; instead for better performance
```

### Wrapper Array Memory

```java
Integer[] arr = {1, 2, 3, 4, 5};

// Array object: 16 + 4 + 4 + 5×8 = 64 bytes
// Integer objects: 5 × 16 = 80 bytes
// Total: 144 bytes

// vs primitive array:
int[] primitiveArr = {1, 2, 3, 4, 5};
// Total: 16 + 4 + 4 + 20 = 44 bytes
```

### Map with Wrapper Keys

```java
Map<Integer, String> map = new HashMap<>();
map.put(1, "one");
map.put(2, "two");

// Each Integer key: ~16 bytes object + 8 bytes reference
// HashMap overhead: ~32 bytes per entry
```
