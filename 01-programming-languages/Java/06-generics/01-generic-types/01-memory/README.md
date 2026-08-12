# 04 - Memory: Generic Type Memory Layout

## Scope

This topic covers the memory representation of generic objects on the JVM, including object headers, field sizes, reference tracking, and memory implications of type erasure.

## Why It Exists

Understanding generic type memory layout helps you:

- Estimate memory overhead of generic containers
- Understand why generics have no runtime cost
- Diagnose memory issues in type-heavy applications
- Make informed decisions about data structure choices

## Object Header (64-bit JVM)

Every Java object has a header:

| Component | Size | Content |
|-----------|------|---------|
| Mark word | 8 bytes | Lock state, hashCode, GC age |
| Klass pointer | 4 bytes (compressed) | Pointer to class metadata |
| **Total header** | **16 bytes** (with padding) | Aligned to 8-byte boundary |

On 64-bit JVMs with compressed oops (default on heaps < 32 GB), the header is 16 bytes.

## Generic Object Layout

A generic class like `Box<T>` has the same memory layout regardless of `T`:

```
Box<String> object:
┌──────────────────────────────────────────┐
│ Object Header          │ 16 bytes        │
├──────────────────────────────────────────┤
│ value (reference)      │ 4 bytes (ref)   │
├──────────────────────────────────────────┤
│ Padding                │ 4 bytes         │
├──────────────────────────────────────────┤
│ Total                  │ 24 bytes        │
└──────────────────────────────────────────┘
         │
         └──→ String object
                ├─ header (16 bytes)
                ├─ hash (4 bytes)
                ├─ value (4 bytes) → char[] or byte[]
                └─ Total: ~24 bytes + char[] array
```

```
Box<Integer> object:
┌──────────────────────────────────────────┐
│ Object Header          │ 16 bytes        │
├──────────────────────────────────────────┤
│ value (reference)      │ 4 bytes (ref)   │
├──────────────────────────────────────────┤
│ Padding                │ 4 bytes         │
├──────────────────────────────────────────┤
│ Total                  │ 24 bytes        │
└──────────────────────────────────────────┘
         │
         └──→ Integer object (cached)
                ├─ header (16 bytes)
                ├─ value (int) (4 bytes)
                └─ Total: 24 bytes
```

Notice: The `Box` object itself is identical in both cases. The only difference is what the `value` reference points to.

## Type Erasure and Memory

Because of type erasure, `Box<String>` and `Box<Integer>` are both just `Box` at runtime. This means:

1. **No memory overhead** for generic type parameters
2. **Same object layout** regardless of type argument
3. **Same bytecode** for all type instantiations
4. **Same JIT optimization** opportunities

## Reference Field Sizes

| Reference Target | Size (compressed) | Size (uncompressed) |
|------------------|-------------------|---------------------|
| Any object reference | 4 bytes | 8 bytes |
| Array reference | 4 bytes | 8 bytes |
| String reference | 4 bytes | 8 bytes |

With compressed oops (default on heaps < 32 GB), each reference is 4 bytes.

## Generic Collections Memory

A `List<String>` has the same memory layout as a `List<Integer>`:

```
ArrayList<String> object:
┌──────────────────────────────────────────┐
│ Object Header          │ 16 bytes        │
├──────────────────────────────────────────┤
│ elementData (ref)      │ 4 bytes         │
│ size (int)             │ 4 bytes         │
├──────────────────────────────────────────┤
│ Total                  │ 24 bytes        │
└──────────────────────────────────────────┘
         │
         └──→ Object[] array
                ├─ header (16 bytes)
                ├─ length (4 bytes)
                └─ elements (4 bytes each)
                   └─ Each element → String object
```

The `elementData` array always holds `Object` references (due to type erasure), so the array layout is identical regardless of the generic type parameter.

## Memory Cost Comparison

| Type | Object Size | Total with Element |
|------|-------------|-------------------|
| `Box<Boolean>` | 24 bytes | 24 + 16 (Boolean) = 40 bytes |
| `Box<Byte>` | 24 bytes | 24 + 16 (Byte) = 40 bytes |
| `Box<Character>` | 24 bytes | 24 + 16 (Character) = 40 bytes |
| `Box<Short>` | 24 bytes | 24 + 16 (Short) = 40 bytes |
| `Box<Integer>` | 24 bytes | 24 + 16 (Integer) = 40 bytes |
| `Box<Long>` | 24 bytes | 24 + 16 (Long) = 40 bytes |
| `Box<Float>` | 24 bytes | 24 + 16 (Float) = 40 bytes |
| `Box<Double>` | 24 bytes | 24 + 16 (Double) = 40 bytes |
| `Box<String>` | 24 bytes | 24 + 24 + char[] = variable |
| `Box<Object>` | 24 bytes | 24 + depends on contents |

## Autoboxing Memory Impact

When you use generics with primitives, Java autoboxes them:

```java
List<Integer> list = new ArrayList<>();
list.add(42);  // Autoboxed to Integer.valueOf(42)
```

This creates an `Integer` object for each primitive value, which has memory overhead:

| Primitive | Wrapper | Object Size | Overhead |
|-----------|---------|-------------|----------|
| `boolean` | `Boolean` | 16 bytes | 16 bytes |
| `byte` | `Byte` | 16 bytes | 16 bytes |
| `char` | `Character` | 16 bytes | 16 bytes |
| `short` | `Short` | 16 bytes | 16 bytes |
| `int` | `Integer` | 16 bytes | 16 bytes |
| `long` | `Long` | 16 bytes | 16 bytes |
| `float` | `Float` | 16 bytes | 16 bytes |
| `double` | `Double` | 16 bytes | 16 bytes |

For collections of primitives, consider using specialized libraries (e.g., Eclipse Collections, HPPC) that provide primitive-specific collections to avoid autoboxing overhead.

## Memory Leak Patterns

### Pattern 1: Accumulating Generic References

```java
// MEMORY LEAK: each Box holds its value in memory
List<Box<String>> boxes = new ArrayList<>();
while (processing) {
    boxes.add(new Box<>(largeString));  // accumulates forever
}
```

**Fix**: Use a bounded collection or clear periodically.

### Pattern 2: Generic References in Static Fields

```java
// BAD: static field holds generic reference forever
class Cache {
    static Box<LargeObject> cached;
}
```

**Fix**: Use WeakReference or clear static references when no longer needed.

## Production Patterns

### Pattern 1: Primitive-Specific Collections

```java
// AVOID: Autoboxing overhead
List<Integer> list = new ArrayList<>();
for (int i = 0; i < 1_000_000; i++) {
    list.add(i);  // Autoboxed to Integer
}

// BETTER: Use specialized collections
IntList list = new IntArrayList();
for (int i = 0; i < 1_000_000; i++) {
    list.add(i);  // No autoboxing
}
```

### Pattern 2: Array-Based Storage

```java
// AVOID: Object[] with casting
Object[] array = new Object[1000];
array[0] = "hello";
String s = (String) array[0];  // Cast required

// BETTER: Type-safe array (if type is known)
String[] array = new String[1000];
array[0] = "hello";
String s = array[0];  // No cast needed
```

## Summary

| Concept | Key Takeaway |
|---------|--------------|
| Object header | 16 bytes on 64-bit JVM with compressed oops |
| Generic type parameters | No memory overhead (type erasure) |
| Reference fields | 4 bytes each (compressed) |
| Autoboxing | Creates wrapper objects for primitives |
| Collections | Same layout regardless of type parameter |
| Memory leaks | Accumulating generic references in collections |
| Optimization | Use primitive-specific collections for hot paths |
