# Checked Exceptions — Memory Behavior

## Class File Storage

Checked exceptions consume space in the class file:

```
Method: processData()
  throws IOException, SQLException

Class file:
  method_info → Exceptions_attribute:
    exception_index_table: [#5, #6]  // 4 bytes per exception
```

## Runtime Memory

| Component | Memory | Lifetime |
|-----------|--------|----------|
| Exception class metadata | Loaded once per class | ClassLoader lifetime |
| Exception object | ~100-500 bytes per instance | Until GC after catch |
| Stack trace | ~1-10KB per exception | Until GC after catch |
| Exception table entry | ~6 bytes per handler | Class file (static) |

## Compared to Unchecked

```
Checked:   Same runtime memory as unchecked
Unchecked: Same runtime memory as checked

The only difference is compile-time enforcement.
No memory overhead for the distinction.
```

## Reflection Cost

```java
Class<?>[] exs = method.getExceptionTypes();
// Allocates a new Class[] array each call
// Each Class reference is 8 bytes (64-bit JVM)
```

## Key Insight

Checked exceptions have zero runtime memory overhead compared to unchecked. The distinction exists only in the class file metadata and compiler checks.
