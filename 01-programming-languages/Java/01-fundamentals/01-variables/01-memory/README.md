# Variables and Types Memory Model

## Stack vs Heap

### Stack Memory
- Stores local variables and method parameters
- Fast allocation and deallocation (LIFO order)
- Each thread has its own stack
- Automatically cleaned up when method returns

```
Method Stack Frame:
┌─────────────────────┐
│ localVar1 (int)     │ ← 4 bytes on stack
│ localVar2 (double)  │ ← 8 bytes on stack
│ objRef (reference)  │ ← 8 bytes on 64-bit JVM (points to heap)
└─────────────────────┘
```

### Heap Memory
- Stores all objects and instance variables
- Shared across the entire application
- Managed by Garbage Collector
- Slower access than stack

```
Heap:
┌─────────────────────────────────────┐
│ Object: String "Hello"              │ ← 40+ bytes (object header + data)
│ Object: Person {name, age}          │ ← 16+ bytes + field sizes
│ Object: int[] {1, 2, 3}            │ ← 16+ bytes + 4 bytes per element
└─────────────────────────────────────┘
```

## Memory Usage by Type

### Primitive Types (Stack)

| Type | Bytes | Binary Representation |
|------|-------|----------------------|
| byte | 1 | 8 bits signed |
| short | 2 | 16 bits signed |
| int | 4 | 32 bits signed |
| long | 8 | 64 bits signed |
| float | 4 | 32-bit IEEE 754 |
| double | 8 | 64-bit IEEE 754 |
| boolean | 1* | JVM-dependent |
| char | 2 | 16-bit Unicode |

*Note: boolean size varies by JVM implementation; conceptually 1 bit.

### Reference Types (Stack + Heap)

```
Reference on Stack: 8 bytes (64-bit JVM)
     │
     ▼
Object on Heap: Variable size
┌──────────────────┐
│ Object Header    │ ← 12-16 bytes (mark word + class pointer)
│ Instance Fields  │ ← Size depends on field types
│ Padding          │ ← Aligned to 8-byte boundary
└──────────────────┘
```

## Autoboxing Memory Impact

```java
// Primitive: 4 bytes on stack
int primitive = 42;

// Boxed: 8 bytes reference + 16+ bytes object on heap
Integer boxed = 42;

// In collections: Massive overhead
List<Integer> list = new ArrayList<>();
list.add(42);  // Creates new Integer object for each element!
```

## String Memory

```java
String s = "Hello";
// String object: ~40 bytes (header + hash + count + offset + char[])
// Character data: ~10 bytes (2 bytes per char)
// Total: ~50 bytes

// String pool saves memory for repeated literals
String s2 = "Hello";  // Reuses existing, no new allocation
```

## Array Memory Layout

```java
int[] arr = {1, 2, 3, 4, 5};
// Heap layout:
// Object header: 16 bytes
// Length field: 4 bytes
// Elements: 5 × 4 bytes = 20 bytes
// Padding: 4 bytes (to align to 8)
// Total: 44 bytes
```

## Best Practices for Memory Efficiency

1. Use primitives over wrappers when null is not needed
2. Prefer `StringBuilder` over string concatenation in loops
3. Use `int` over `long` when values fit in 32 bits
4. Consider `byte[]` over `Byte[]` for large arrays
5. Reuse objects when possible (object pooling)
6. Be aware of autoboxing overhead in tight loops
