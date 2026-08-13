# throw — Memory Behavior

## throw Statement Memory Allocation

When a `throw` statement executes, the JVM performs these allocations:

```
Stack Frame (current method)
┌──────────────────────────────────┐
│ Local variables                  │
│ Operand stack                    │
│ Return address                   │
└──────────────┬───────────────────┘
               │ throw exception
               ▼
         Exception Object
         (heap allocated)
```

## Exception Object Allocation

```java
throw new RuntimeException("error");
// 1. new → allocates RuntimeException on heap
// 2. RuntimeException(String) → constructor runs
// 3. throw → copies object reference to JVM exception handler
```

The exception object is always a heap allocation. Stack frames are unwound, but the exception object persists until the catch block completes.

## Stack Unwinding

```
Method C: throw new Exception("error")
  Frame removed ←──────────────────┐
Method B: calling method C         │
  Frame removed ←──────────────────┤
Method A: calling method B         │
  Frame removed ←──────────────────┤
  Catch block found                │
  Exception reference stored       │
  Execution resumes                │
```

Each frame removal:
- Restores the previous frame pointer
- Pops local variables and operand stack
- Does NOT zero out memory (JIT may optimize)

## JIT Compilation of throw

The JIT compiler optimizes `throw` statements:

- **Common exception path**: HotSpot compiles `new Exception()` as a fast allocation
- **Uncommon trap**: If exception is rarely thrown, JIT may inline the happy path and deoptimize on throw
- **Stack trace generation**: `fillInStackTrace()` is expensive; JIT may skip it for uncommon throws

```java
// JIT optimization: if this path is cold
if (error) {
    throw new IllegalArgumentException("rarely happens"); // uncommon trap
}
```

## Performance Impact

| Operation | Cost | Notes |
|-----------|------|-------|
| `new Exception()` | ~100ns | Heap allocation + constructor |
| `fillInStackTrace()` | ~1-10μs | Walks entire stack; most expensive |
| Stack unwinding | ~10-100ns | Per frame removed |
| Catch block lookup | ~1ns | Table-based in bytecode |

## Key Insight

`throw` is not a function call — it transfers control to the JVM's exception handling mechanism. The exception object escapes to the heap, making it visible to the garbage collector after the catch block completes.
