# Stack vs Heap

## Quick Comparison

| Aspect | Stack | Heap |
|--------|-------|------|
| **Purpose** | Method execution | Object storage |
| **Allocation** | LIFO (automatic) | Dynamic (GC) |
| **Size** | Small (~1MB/thread) | Large (GBs) |
| **Speed** | Very fast | Slower |
| **Lifetime** | Method scope | Until GC |
| **Thread Safety** | Thread-local | Shared |

## Stack

### Structure
```
Thread Stack
├── main()
│   ├─ Local: int x = 10
│   ├─ Local: String s = "hello"
│   └─ Calls foo()
├── foo()
│   ├─ Local: int y = 20
│   └─ Calls bar()
└── bar()
    └─ Local: String z = "world"
```

### Stack Frame Contents
| Component | Description |
|-----------|-------------|
| Local Variables | Parameters, local variables |
| Operand Stack | Intermediate computations |
| Frame Data | Return address, exception table |
| Method Reference | Constant pool reference |

### Stack Characteristics
- **Fixed size** per thread (`-Xss` default ~1MB)
- **LIFO** allocation/deallocation
- **Very fast** (pointer increment/decrement)
- **Thread-local** (no synchronization needed)

```java
void example() {
    int x = 10;           // Stack: primitive value
    Person p = new Person(); // Stack: reference p
                              // Heap: Person object
}
```

## Heap

### Structure
```
Heap
├── Young Generation
│   ├── Eden Space (new objects)
│   ├── Survivor Space S0
│   └── Survivor Space S1
├── Old Generation
│   └── Long-lived objects
└── Metaspace (Java 8+)
    └── Class metadata
```

### Object Layout
```
Object Header (12-16 bytes)
├── Mark Word (hash, lock, GC age)
├── Class Pointer (to Class object)
└── Array Length (if array)

Instance Fields
└── Field values (aligned to 8 bytes)

Padding (for alignment)
```

### Object Allocation
```java
Person p = new Person("Alice", 30);
// 1. Check TLAB (Thread Local Allocation Buffer)
// 2. If full → allocate in Eden
// 3. If Eden full → Minor GC
```

## Comparison Table

| Aspect | Stack | Heap |
|--------|-------|------|
| **Purpose** | Method execution & local variables | Object storage & dynamic allocation |
| **Allocation** | LIFO (automatic) | Dynamic (GC-managed) |
| **Size** | Small (~1 MB/thread default) | Large (multi-GB) |
| **Speed** | Very fast (pointer arithmetic) | Slower (allocation + GC overhead) |
| **Lifetime** | Method scope (automatic deallocation) | Until GC collects unreachable objects |
| **Thread Safety** | Thread-local (no synchronization) | Shared (requires synchronization) |
| **Fragmentation** | None | Possible (compacted by GC) |
| **Tuning Flags** | `-Xss` | `-Xms`, `-Xmx` |

### Where Things Live

| Scenario | Stack | Heap |
|----------|-------|------|
| `int x = 10;` | ✓ Value stored | |
| `Person p = new Person();` | ✓ Reference `p` | ✓ Object |
| `int[] arr = new int[100];` | ✓ Reference `arr` | ✓ Array |
| Method parameters | ✓ (copied) | ✓ (objects via reference) |
| Local variables | ✓ | |
| Instance fields | | ✓ |
| Static fields | | ✓ (Method Area / Metaspace) |
| String constants (`"hello"`) | | ✓ (String Pool in Heap) |
| Lambda captured variables | ✓ (if value types) | ✓ (if object references) |

### When to Prefer Stack vs Heap

| Scenario | Best Choice | Reason |
|----------|-------------|--------|
| Small, fixed-size data | Stack | Fast, no GC overhead |
| Large arrays | Heap | Stack frame too small |
| Short-lived objects | Heap (Eden) | Quickly GC-collected |
| Long-lived shared objects | Heap (Old Gen) | Survive multiple GC cycles |
| Thread-confined objects | Stack (via escape analysis) | No synchronization needed |

## Escape Analysis

```java
// JVM may optimize: allocate on stack if object doesn't escape
public void process() {
    User user = new User("Alice");  // May allocate on stack!
    user.setName("Bob");
    // user doesn't escape method
}
```

**JVM Flags:**
- `-XX:+DoEscapeAnalysis` (default on)
- `-XX:+EliminateAllocations` (scalar replacement)

## Memory Errors

| Error | Cause | Solution |
|-------|-------|----------|
| `StackOverflowError` | Deep recursion | Increase `-Xss` or fix recursion |
| `OutOfMemoryError: Java heap space` | Heap full | Increase `-Xmx`, fix leaks |
| `OutOfMemoryError: Metaspace` | Too many classes | Increase `-XX:MaxMetaspaceSize` |
| `OutOfMemoryError: Direct buffer` | Off-heap buffers | Increase `-XX:MaxDirectMemorySize` |

## Visualization

### Stack Memory
```
Stack (grows down)
┌─────────────────┐
│  Stack Frame    │  ← bar()
│  (local vars)   │
├─────────────────┤
│  Stack Frame    │  ← foo()
├─────────────────┤
│  Stack Frame    │  ← main()
├─────────────────┤
│  Stack Frame    │  ← JVM internal
└─────────────────┘
```

### Heap Memory
```
Heap
├── Young Gen (Eden + S0 + S1)
│   ├── Object A (new)
│   ├── Object B (survived 1 GC)
│   └── Object C (new)
├── Old Gen
│   ├── Object D (long-lived)
│   └── Object E (long-lived)
└── Metaspace
    └── Class metadata
```