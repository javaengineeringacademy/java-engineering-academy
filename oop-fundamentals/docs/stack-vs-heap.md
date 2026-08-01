# Stack vs Heap Memory in Java

## 1. Introduction

Memory management is a fundamental aspect of Java programming. Understanding how the JVM allocates and manages memory—specifically the difference between stack and heap—is critical for writing efficient, bug-free applications. The stack handles method execution and local variables with fast, automatic LIFO allocation, while the heap stores objects and supports dynamic, GC-managed lifetime. This guide covers every aspect you need to master, from low-level JVM internals to practical debugging strategies.

## 2. Learning Objectives

After completing this guide, you will be able to:

- Explain the role of stack and heap memory in the JVM
- Identify where primitives, objects, and references live
- Predict `StackOverflowError` and `OutOfMemoryError` causes and fixes
- Apply escape analysis to optimize object allocation
- Tune JVM flags for stack and heap sizing
- Debug memory-related issues using JVM tools
- Make informed decisions about object allocation in production systems

## 3. Prerequisites

- Basic Java syntax and object creation (`new` keyword)
- Familiarity with method calls and recursion
- Understanding of garbage collection at a high level
- Access to JDK 21+ for running examples

## 4. Why This Concept Exists

Java abstracts memory management away from the developer. Unlike C/C++, you never call `malloc` or `free`. The JVM automatically handles allocation and deallocation—but that abstraction has costs. Misunderstanding where objects live leads to:

- Unnecessary garbage collection pressure
- Memory leaks from unintentional object retention
- `StackOverflowError` from unbounded recursion
- `OutOfMemoryError` from uncontrolled heap growth

Understanding stack vs heap gives you control over performance characteristics that matter in production.

## 5. Problem Statement

Consider this scenario: A web application processes 10,000 requests per second. Each request allocates a `RequestContext` object with 20 fields. Without understanding memory allocation:

- Objects may be allocated on the heap when stack allocation is possible
- Long-lived references prevent GC from reclaiming memory
- Stack frames grow unnecessarily deep from nested calls

The core problem: **Where should data live for optimal performance, and how does the JVM decide?**

## 6. Theory

### Stack Memory

The stack is a **per-thread, LIFO (Last-In-First-Out) data structure**. Each thread gets its own stack. When a method is invoked, a new **stack frame** is pushed onto the stack. When the method returns, its frame is popped.

**Stack frame contents:**
- **Local variables**: Primitive values and object references
- **Operand stack**: Intermediate computation results
- **Frame data**: Return address, exception handler table
- **Reference to constant pool**: Resolved symbolic references

**Key properties:**
- Fixed size per thread (default ~1 MB, configurable via `-Xss`)
- Automatic allocation and deallocation (no GC needed)
- Extremely fast (pointer increment/decrement)
- Thread-local (no synchronization overhead)

### Heap Memory

The heap is a **shared memory region** used for dynamic object allocation. All threads share the same heap. Objects are allocated with `new` and reclaimed by the garbage collector when no references point to them.

**Heap generations:**
- **Young Generation**: Newly created objects. Subdivided into Eden and two Survivor spaces (S0, S1)
- **Old Generation**: Objects that survived multiple GC cycles
- **Metaspace** (Java 8+): Class metadata, method info, constant pool

**Key properties:**
- Large size (configurable via `-Xms`, `-Xmx`, often GBs)
- GC-managed (non-deterministic deallocation)
- Slower than stack (allocation + GC overhead)
- Shared across threads (requires synchronization)

## 7. Internal Working

### Stack Internals

When `methodA()` calls `methodB()`:

1. JVM pushes a new stack frame for `methodB` onto the current thread's stack
2. Parameters and local variables are copied into the frame
3. Execution begins at the first instruction of `methodB`
4. When `methodB` returns, its frame is popped, and execution resumes in `methodA`

```
Stack (grows downward in memory)
┌─────────────────────┐  High address
│  Frame: methodC()   │  ← Top of stack (current)
│  local: int z = 5   │
├─────────────────────┤
│  Frame: methodB()   │
│  local: int y = 3   │
├─────────────────────┤
│  Frame: methodA()   │
│  local: int x = 1   │
├─────────────────────┤
│  Frame: main()      │  ← Bottom of stack
│  local: args        │
└─────────────────────┘  Low address
```

### Heap Internals

When `new Person("Alice")` is executed:

1. JVM checks the thread's **TLAB** (Thread Local Allocation Buffer) in Eden space
2. If TLAB has room, the object is allocated there (fast path, no locking)
3. If TLAB is full, a new TLAB is requested or allocation happens in Eden directly
4. If Eden is full, a **Minor GC** is triggered to reclaim space
5. Objects surviving multiple Minor GCs are promoted to Old Generation

```
Heap Memory Layout
┌─────────────────────────────────────────┐
│           Young Generation              │
│  ┌───────────┬──────────┬──────────┐    │
│  │   Eden    │  S0      │   S1     │    │
│  │  (new)    │(survivor)│(survivor)│    │
│  │  80%      │  10%     │  10%     │    │
│  └───────────┴──────────┴──────────┘    │
├─────────────────────────────────────────┤
│           Old Generation                │
│  ┌─────────────────────────────────┐    │
│  │     Long-lived objects          │    │
│  └─────────────────────────────────┘    │
├─────────────────────────────────────────┤
│           Metaspace                     │
│  ┌─────────────────────────────────┐    │
│  │  Class metadata, strings, etc.  │    │
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘
```

## 8. JVM Perspective

### Stack in the JVM Specification

Each Java thread has a private JVM stack, created at thread start. Each invocation creates a new **stack frame** containing:

- **Local variable array**: Indexed from 0, sized at compile time
- **Operand stack**: LIFO stack for bytecode operations
- **Frame data**: Constant pool reference, return address

The JVM specification mandates that a `StackOverflowError` be thrown if stack depth exceeds the limit, and an `OutOfMemoryError` if a new frame cannot be allocated.

### Heap in the JVM Specification

The JVM manages a single heap shared by all threads. The specification requires:

- All objects and arrays are allocated on the heap
- The heap is garbage collected automatically
- Heap size is bounded by `-Xmx` (max) and `-Xms` (initial)

### Metaspace (Java 8+)

Replaced PermGen. Stores class metadata in native memory. Does not have a fixed upper bound by default but can be limited with `-XX:MaxMetaspaceSize`.

## 9. Memory Representation

### What Lives Where

| Data Type | Location | Size | Lifetime |
|-----------|----------|------|----------|
| `int x = 10` | Stack (local var) | 4 bytes | Method scope |
| `double d = 3.14` | Stack (local var) | 8 bytes | Method scope |
| `Object obj = new Object()` | Stack: reference; Heap: object | Reference: 4-8 bytes; Object: 12+ bytes | Until method returns (ref); Until GC (object) |
| `int[] arr = new int[100]` | Stack: reference; Heap: array | Reference: 4-8 bytes; Array: 416 bytes | Until method returns (ref); Until GC (array) |
| `static int count` | Metaspace (class variable) | 4 bytes | Class lifetime |
| `String s = "hello"` | Stack: reference; Heap: String object (in String Pool) | Varies | Until GC |

### Object Header on Heap

Every object on the heap has a header:

```
┌──────────────────────────────┐
│  Mark Word (8 bytes)         │  ← Hashcode, GC age, lock info
├──────────────────────────────┤
│  Class Pointer (4 bytes)     │  ← Points to Class object in Metaspace
├──────────────────────────────┤
│  Padding (4 bytes, optional) │  ← Align to 8-byte boundary
├──────────────────────────────┤
│  Instance Fields             │  ← Actual data
├──────────────────────────────┤
│  Padding                     │  ← Align to 8-byte boundary
└──────────────────────────────┘
```

## 10. Architecture Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                        JVM Process                           │
│                                                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │  Thread 1   │  │  Thread 2   │  │  Thread N   │         │
│  │  ┌────────┐ │  │  ┌────────┐ │  │  ┌────────┐ │         │
│  │  │ Stack  │ │  │  │ Stack  │ │  │  │ Stack  │ │         │
│  │  │(1MB)   │ │  │  │(1MB)   │ │  │  │(1MB)   │ │         │
│  │  └────────┘ │  │  └────────┘ │  │  └────────┘ │         │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘         │
│         │                │                │                  │
│         └────────────────┼────────────────┘                  │
│                          │                                   │
│                    ┌─────▼──────┐                            │
│                    │   Shared   │                            │
│                    │    Heap    │                            │
│                    │  ┌──────┐  │                            │
│                    │  │Young │  │                            │
│                    │  │ Gen  │  │                            │
│                    │  ├──────┤  │                            │
│                    │  │ Old  │  │                            │
│                    │  │ Gen  │  │                            │
│                    │  ├──────┤  │                            │
│                    │  │Meta  │  │                            │
│                    │  │space │  │                            │
│                    │  └──────┘  │                            │
│                    └────────────┘                            │
└──────────────────────────────────────────────────────────────┘
```

## 11. Flow Diagram

### Object Allocation Flow

```
new Object()
     │
     ▼
┌─────────────────┐    Yes    ┌──────────────┐
│ TLAB has space? ├──────────►│ Allocate in  │
└────────┬────────┘           │ TLAB (fast)  │
         │ No                 └──────────────┘
         ▼
┌─────────────────┐    Yes    ┌──────────────┐
│ Eden has space? ├──────────►│ Allocate in  │
└────────┬────────┘           │ Eden         │
         │ No                 └──────────────┘
         ▼
┌─────────────────┐           ┌──────────────┐
│ Trigger Minor   ├──────────►│ GC + Allocate│
│ GC              │           └──────────────┘
└─────────────────┘
```

### Stack Frame Lifecycle

```
Method Called
     │
     ▼
┌─────────────────┐
│ Push new frame  │  ← Parameters, return address
│ onto stack      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Execute method  │  ← Use local vars, operand stack
│ body            │
└────────┬────────┘
         │
    ┌────┴────┐
    │ Return  │
    └────┬────┘
         │
         ▼
┌─────────────────┐
│ Pop frame from  │  ← Free local vars
│ stack           │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Resume caller   │
└─────────────────┘
```

## 12. Syntax

### Stack Allocation (Implicit)

```java
// Primitives: allocated on stack automatically
int x = 10;
double price = 99.99;
boolean flag = true;

// Method parameters: copied to stack frame
public int add(int a, int b) {
    return a + b;  // a and b are on the stack
}
```

### Heap Allocation (Explicit with `new`)

```java
// Objects: reference on stack, object on heap
Person person = new Person("Alice", 30);

// Arrays: reference on stack, array on heap
int[] numbers = new int[100];

// Nested objects: each `new` allocates on heap
List<String> list = new ArrayList<>();
list.add(new String("hello"));
```

### JVM Flags

```bash
# Stack size per thread (default: 512KB - 1MB depending on platform)
java -Xss512k MyApp

# Heap initial and max size
java -Xms256m -Xmx4g MyApp

# Metaspace limit
java -XX:MaxMetaspaceSize=256m MyApp

# Escape analysis (default: on)
java -XX:+DoEscapeAnalysis -XX:+EliminateAllocations MyApp
```

## 13. Easy Example

```java
public class StackHeapDemo {
    public static void main(String[] args) {
        int x = 10;                    // Stack: x holds 10
        int y = 20;                    // Stack: y holds 20
        int result = add(x, y);        // Stack: new frame for add()
        System.out.println(result);    // Stack: back in main()
    }

    static int add(int a, int b) {     // Stack: a=10, b=20
        int sum = a + b;               // Stack: sum=30
        return sum;                    // Stack: frame popped
    }
}
```

**Memory state:**
```
Stack:
├── main(): args → String[] (heap ref)
├── add(): a=10, b=20, sum=30  ← current frame
```

## 14. Medium Example

```java
public class ObjectLifecycle {
    public static void main(String[] args) {
        // Stack: reference 'person'
        // Heap: Person object with name="Alice", age=30
        Person person = new Person("Alice", 30);

        // Stack: reference 'names'
        // Heap: String[] array
        // Heap: 3 String objects (String Pool or heap)
        String[] names = {"Alice", "Bob", "Charlie"};

        // Stack: reference 'copy'
        // Heap: new Person object (deep copy)
        Person copy = new Person(person.getName(), person.getAge());

        // After this line:
        // - 'person' reference is removed from stack
        // - Person("Alice", 30) is eligible for GC (no more refs)
        person = null;

        // 'copy' still references a Person object on heap
    }
}

class Person {
    private String name;    // Heap: reference to String
    private int age;        // Heap: 4 bytes in object

    Person(String name, int age) {
        this.name = name;   // Reference copied to field
        this.age = age;     // Value copied to field
    }

    String getName() { return name; }
    int getAge() { return age; }
}
```

## 15. Hard Example

```java
public class MemoryPressure {
    private static final int LARGE_SIZE = 10_000_000;

    public static void main(String[] args) {
        // Scenario 1: Stack overflow from recursion
        try {
            infiniteRecursion(0);
        } catch (StackOverflowError e) {
            System.out.println("Stack overflow at depth: " + e.getStackTrace().length);
        }

        // Scenario 2: Heap exhaustion from large allocation
        try {
            byte[] hugeArray = new byte[LARGE_SIZE * 100]; // ~1GB
        } catch (OutOfMemoryError e) {
            System.out.println("Heap space exhausted");
        }

        // Scenario 3: Escape analysis optimization
        int sum = computeSum();
        System.out.println("Sum: " + sum);
    }

    static void infiniteRecursion(int depth) {
        // Each call adds a stack frame (~40-100 bytes)
        infiniteRecursion(depth + 1);
    }

    static int computeSum() {
        // Object may be stack-allocated (escape analysis)
        Point p = new Point(3, 4);
        return p.x + p.y;
    }
}

class Point {
    int x;
    int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
```

## 16. Enterprise Example

```java
public class RequestProcessor {
    // Thread pool: each thread has its own stack
    private final ExecutorService executor =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public Response handleRequest(Request request) {
        // Request object: heap (shared across thread pool)
        // RequestContext: heap (created per request)
        // Primitive fields in context: heap (part of object)

        return executor.submit(() -> {
            // This lambda captures 'request' by reference
            // 'request' must live on heap (escaped to another thread)

            ValidationResult result = validate(request);
            if (!result.isValid()) {
                return Response.error(result.getErrors());
            }

            // Process: many short-lived objects on heap (Eden)
            ProcessedData data = process(request);
            return Response.ok(data);
        }).get();
    }

    private ValidationResult validate(Request request) {
        // Validation rules: mostly stack (primitives, local vars)
        boolean isValid = true;
        List<String> errors = new ArrayList<>();

        for (String field : request.getRequiredFields()) {
            if (request.getField(field) == null) {
                errors.add("Missing: " + field);
                isValid = false;
            }
        }

        return new ValidationResult(isValid, errors);  // Heap object
    }

    private ProcessedData process(Request request) {
        // Heavy computation: mostly stack-based
        int count = 0;
        double sum = 0.0;

        for (double value : request.getValues()) {
            count++;
            sum += value;
        }

        double average = sum / count;  // Stack: primitive

        return new ProcessedData(count, sum, average);  // Heap
    }
}
```

## 17. Performance

### Stack Performance

| Operation | Latency | Notes |
|-----------|---------|-------|
| Push/pop frame | ~1 ns | Pointer arithmetic |
| Local variable access | ~1 ns | Direct memory access |
| Method call overhead | ~2-5 ns | Frame setup/teardown |

### Heap Performance

| Operation | Latency | Notes |
|-----------|---------|-------|
| Object allocation (TLAB) | ~10-20 ns | Fast path, no locking |
| Object allocation (Eden) | ~30-100 ns | May require TLAB refill |
| Minor GC | ~1-10 ms | Stops the world briefly |
| Full GC | ~100ms - seconds | Can cause long pauses |

### Optimization Strategies

1. **Prefer stack allocation**: Use primitives and escape analysis
2. **Minimize object creation**: Reuse objects, use object pools
3. **Reduce GC pressure**: Allocate short-lived objects, avoid premature promotion
4. **Tune TLAB size**: `-XX:TLABSize` for high-allocation-rate threads

## 18. Time Complexity

| Operation | Stack | Heap |
|-----------|-------|------|
| Allocation | O(1) | O(1) amortized (TLAB) |
| Deallocation | O(1) | O(n) during GC |
| Access | O(1) | O(1) |
| Resize | N/A (fixed) | N/A (GC handles) |

## 19. Space Complexity

### Stack Space

Each stack frame size is determined at compile time:
- **Local variables**: Sum of all local variable sizes + 1 reference (`this`)
- **Operand stack**: Maximum operand stack depth × slot size
- **Frame data**: Fixed overhead (~16-32 bytes)

Example calculation:
```java
public int calculate(int a, int b) {
    int x = a + b;      // 4 bytes
    double y = x * 2.5; // 8 bytes
    boolean z = x > 10; // 1 byte (but aligned to 4)
    return (int) y;     // No additional space
}
// Frame: this(4) + a(4) + b(4) + x(4) + y(8) + z(4) = ~32 bytes
```

### Heap Space

Each object:
- **Header**: 12-16 bytes
- **Fields**: Sum of field sizes (aligned to 8 bytes)
- **Padding**: Up to 7 bytes for alignment

Array overhead:
- **Header**: 16 bytes (includes array length)
- **Elements**: Length × element size

## 20. Thread Safety

### Stack: Inherently Thread-Safe

Each thread has its own stack. No two threads can access each other's stack frames. Local variables are thread-confined.

```java
public void unsafeCounter() {
    int count = 0;  // Thread-local, safe
    count++;
    // No synchronization needed
}
```

### Heap: Requires Synchronization

Objects on the heap are shared. Concurrent access requires synchronization:

```java
public class SharedCounter {
    private int count = 0;  // Heap: shared across threads

    // NOT thread-safe
    public void increment() {
        count++;  // Read-modify-write race condition
    }

    // Thread-safe with synchronization
    public synchronized void safeIncrement() {
        count++;
    }

    // Thread-safe with AtomicInteger (preferred)
    private final AtomicInteger atomicCount = new AtomicInteger(0);
    public void atomicIncrement() {
        atomicCount.incrementAndGet();
    }
}
```

### Escape Analysis and Thread Safety

If an object doesn't escape the current thread, the JVM can allocate it on the stack:

```java
public int processItems(List<Item> items) {
    int total = 0;
    for (Item item : items) {
        // Result object may be stack-allocated
        // (doesn't escape this method)
        Result result = compute(item);
        total += result.getValue();
    }
    return total;
}

Result compute(Item item) {
    // Returned value is scalar-replaced (no object created)
    return new Result(item.getPrice() * item.getQuantity());
}
```

## 21. Best Practices

1. **Use primitives for performance-critical code** — Avoids heap allocation and GC
2. **Prefer local variables** — Stack allocation is automatic and fast
3. **Minimize object references** — Reduces GC root scanning
4. **Reuse objects when possible** — Object pooling for high-frequency allocations
5. **Tune stack size for recursion** — Increase `-Xss` for deep recursion
6. **Monitor heap usage** — Use `-Xlog:gc*` to track GC activity
7. **Avoid creating objects in loops** — Move allocation outside hot paths
8. **Use escape analysis** — Let the JVM stack-allocate when possible
9. **Size TLAB appropriately** — For threads with high allocation rates
10. **Profile before optimizing** — Use JMH or async-profiler

## 22. Common Mistakes

### Mistake 1: Ignoring Stack Overflow Risk

```java
// BAD: Unbounded recursion
public int factorial(int n) {
    if (n == 0) return 1;
    return n * factorial(n - 1);  // Stack overflow for large n
}

// GOOD: Iterative approach
public int factorial(int n) {
    int result = 1;
    for (int i = 2; i <= n; i++) {
        result *= i;
    }
    return result;
}
```

### Mistake 2: Unintentional Object Retention

```java
// BAD: Static collection holds references forever
public class Cache {
    private static final Map<String, Object> cache = new HashMap<>();
    public void put(String key, Object value) {
        cache.put(key, value);  // Never freed until class unload
    }
}

// GOOD: Use WeakReference or bounded cache
public class BoundedCache {
    private static final Map<String, Object> cache =
        new LinkedHashMap<>(100, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
                return size() > 100;  // Evict oldest
            }
        };
}
```

### Mistake 3: Unnecessary Boxing

```java
// BAD: Boxing/unboxing creates heap objects
List<Integer> numbers = new ArrayList<>();
for (int i = 0; i < 1000000; i++) {
    numbers.add(i);  // Auto-boxed: new Integer(i) on heap
}

// GOOD: Use primitive-specialized collections
// Or use IntStream
IntStream.range(0, 1000000)
    .boxed()
    .collect(Collectors.toList());
```

### Mistake 4: Deep Stack from Callback Chains

```java
// BAD: Deep stack from nested callbacks
public void process() {
    serviceA.call(result -> {
        serviceB.call(result2 -> {
            serviceC.call(result3 -> {
                // Stack depth grows with each callback
            });
        });
    });
}
```

## 23. Pitfalls

### Pitfall 1: String Immutability Creates Hidden Allocations

```java
// Each concatenation creates new String objects on heap
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i;  // Creates 1000 intermediate String objects
}

// Use StringBuilder for heap efficiency
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
String result = sb.toString();  // Single allocation
```

### Pitfall 2: Lambda Captures Force Heap Allocation

```java
public void processData() {
    int threshold = 100;  // Primitive on stack

    // Lambda captures 'threshold' - forces heap allocation
    // because the lambda may outlive this method
    Runnable task = () -> System.out.println(threshold);
    executor.submit(task);  // 'threshold' is boxed and placed on heap
}
```

### Pitfall 3: Large Objects on Stack

```java
// BAD: Large local arrays on stack
public void process() {
    byte[] buffer = new byte[1024 * 1024];  // 1MB on stack → StackOverflow
}

// GOOD: Allocate on heap
public void process() {
    byte[] buffer = new byte[1024 * 1024];  // 1MB on heap (OK)
    // Or use ByteBuffer for off-heap
}
```

## 24. Debugging Tips

### Diagnosing StackOverflowError

```bash
# Increase stack size to get more diagnostic info
java -Xss4m MyApp

# Print stack trace on overflow
java -XX:+PrintFlagsFinal MyApp | grep StackOverflow
```

```java
public class StackDebug {
    public static void main(String[] args) {
        try {
            recurse(0);
        } catch (StackOverflowError e) {
            System.err.println("Stack depth: " + e.getStackTrace().length);
            e.printStackTrace();
        }
    }

    static void recurse(int depth) {
        System.err.println("Depth: " + depth);  // Track depth
        recurse(depth + 1);
    }
}
```

### Diagnosing OutOfMemoryError

```bash
# Generate heap dump on OOM
java -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/tmp/heapdump.hprof \
     MyApp

# Monitor heap usage
jstat -gc <pid> 1000

# Analyze heap dump
jhat /tmp/heapdump.hprof
# Or use Eclipse MAT / VisualVM
```

```java
public class HeapDebug {
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();
        int count = 0;
        try {
            while (true) {
                list.add(new byte[1024 * 1024]);  // 1MB chunks
                count++;
                System.out.println("Allocated: " + count + " MB");
            }
        } catch (OutOfMemoryError e) {
            System.out.println("OOM after " + count + " MB");
            list.clear();  // Free memory
        }
    }
}
```

### JVM Diagnostic Flags

```bash
# Verbose GC logging
java -Xlog:gc*:file=gc.log:time,uptime,level,tags MyApp

# Print memory layout
java -XX:+PrintCommandLineFlags MyApp

# JFR for production profiling
java -XX:StartFlightRecording=filename=recording.jfr MyApp
```

## 25. Comparison Table

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
| **Error** | `StackOverflowError` | `OutOfMemoryError` |
| **Data Types** | Primitives, references | Objects, arrays |
| **Cache Locality** | Excellent (contiguous) | Poor (scattered) |
| **Deallocation** | Automatic (frame pop) | Non-deterministic (GC) |

## 26. Decision Tree

```
Need to store data?
│
├── Is it a primitive (int, double, boolean, etc.)?
│   ├── YES → Use as local variable → STACK
│   └── NO ↓
│
├── Is it a fixed-size collection?
│   ├── YES → Use array → HEAP (reference on stack)
│   └── NO ↓
│
├── Is it a temporary computation result?
│   ├── YES → Can it be expressed as primitives?
│   │   ├── YES → Use local variables → STACK
│   │   └── NO → Short-lived object → HEAP (Eden, fast GC)
│   └── NO ↓
│
├── Does it need to be shared across threads?
│   ├── YES → Long-lived? → HEAP (Old Gen)
│   └── NO ↓
│
└── Does it escape the current method?
    ├── YES → HEAP
    └── NO → May be stack-allocated (escape analysis)
```

## 27. Interview Questions

### Basic

1. **What is the difference between stack and heap memory?**
   Stack is per-thread, LIFO, used for method execution and local variables. Heap is shared, GC-managed, used for object allocation.

2. **Where are primitive variables stored?**
   On the stack as local variables, or on the heap as instance fields within objects.

3. **Where are object references stored?**
   The reference itself is on the stack (as a local variable) or on the heap (as a field). The object it points to is always on the heap.

4. **What is a stack frame?**
   A stack frame is a block of memory on the stack containing local variables, the operand stack, frame data, and a reference to the constant pool for a single method invocation.

5. **What causes a `StackOverflowError`?**
   Deep or infinite recursion that exceeds the thread's stack size limit.

### Intermediate

6. **Can the JVM allocate objects on the stack?**
   Yes, through escape analysis. If an object doesn't escape the current method, the JVM can perform scalar replacement and allocate its fields on the stack.

7. **What is a TLAB (Thread Local Allocation Buffer)?**
   A region of Eden space pre-allocated to each thread for fast, lock-free object allocation. When a thread needs to allocate an object, it first checks its TLAB.

8. **How does garbage collection affect heap memory?**
   GC reclaims memory by identifying and collecting objects that are no longer reachable. Minor GC collects young generation; Major/Full GC collects old generation.

9. **What is the Metaspace?**
   Replaced PermGen in Java 8+. Stores class metadata, method info, and constant pool in native memory. Not bounded by default but can be limited with `-XX:MaxMetaspaceSize`.

10. **How do you tune stack and heap sizes?**
    Stack: `-Xss<size>` (e.g., `-Xss2m`). Heap: `-Xms<initial>` and `-Xmx<max>` (e.g., `-Xms512m -Xmx4g`).

### Advanced

11. **Explain escape analysis and scalar replacement.**
    Escape analysis determines if an object's lifetime is confined to the current thread and method. Scalar replacement breaks the object into its constituent fields, allocating them on the stack instead of the heap.

12. **What happens to thread stack memory when a thread is created?**
    The JVM allocates a contiguous block of memory (default ~1MB) for the thread's stack. This memory is allocated in the process's virtual address space, not on the heap.

13. **How does the JVM handle stack overflow vs heap exhaustion?**
    Stack overflow throws `StackOverflowError` immediately (no recovery). Heap exhaustion attempts GC; if memory is still insufficient, throws `OutOfMemoryError`.

14. **What is object promotion and how does it work?**
    When an object survives multiple Minor GC cycles (default threshold: 15), it's promoted from Young Generation to Old Generation. The threshold is configurable via `-XX:MaxTenuringThreshold`.

15. **How does escape analysis interact with JIT compilation?**
    The JIT compiler performs escape analysis during compilation. If analysis determines an object doesn't escape, it can eliminate the allocation entirely (scalar replacement) or lock elision (removing synchronization for thread-confined locks).

## 28. Exercises

### Exercise 1: Stack Frame Analysis

Determine the maximum stack depth for this code with a 1MB stack (`-Xss1m`):

```java
public class DepthTest {
    static int count = 0;
    public static void main(String[] args) {
        try {
            recurse();
        } catch (StackOverflowError e) {
            System.out.println("Max depth: " + count);
        }
    }
    static void recurse() {
        count++;
        int a = 1, b = 2, c = 3;  // ~12 bytes
        recurse();
    }
}
```

**Answer:** Each frame ≈ 40 bytes (locals + overhead). 1MB / 40 ≈ 26,214 calls.

### Exercise 2: Object Lifecycle Tracking

Trace the allocation and deallocation of every object in:

```java
public class Trace {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add(new String("Alice"));
        names.add(new String("Bob"));
        names.clear();
        names = null;
    }
}
```

### Exercise 3: Escape Analysis Verification

Write code that demonstrates escape analysis by comparing allocation rates:

```java
@Benchmark
public int stackAllocated() {
    Point p = new Point(1, 2);
    return p.x + p.y;
}
```

Run with `-XX:+DoEscapeAnalysis` and `-XX:-DoEscapeAnalysis` and compare.

## 29. Assignments

### Assignment 1: Memory Profiling

Profile a simple application using `jstat` and `jmap`:
1. Create an application that allocates objects in a loop
2. Monitor heap usage with `jstat -gc <pid> 1000`
3. Generate a heap dump with `jmap -dump:live,format=b,file=heap.bin <pid>`
4. Analyze which objects consume the most memory

### Assignment 2: Stack Size Tuning

1. Write a recursive algorithm (e.g., Fibonacci)
2. Test with different `-Xss` values (256k, 512k, 1m, 2m)
3. Record maximum recursion depth for each setting
4. Document the relationship between stack size and recursion depth

### Assignment 3: Escape Analysis Experiment

1. Write a benchmark using JMH
2. Compare allocation performance with and without escape analysis
3. Measure the impact of object size on escape analysis effectiveness
4. Write a report on when escape analysis helps most

## 30. Mini Project

### Memory Allocation Analyzer

Build a Java agent that instruments bytecode to track stack and heap allocations:

```java
public class AllocationTracker implements ClassFileTransformer {
    private final Map<String, Long> allocations = new ConcurrentHashMap<>();

    @Override
    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {
        // Use ASM to instrument object allocations
        // Count: new, newarray, anewarray, multianewarray
        // Track: allocation site (class + line number)
        return instrumentedBytecode;
    }

    public void printReport() {
        allocations.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(e -> System.out.printf("%s: %d allocations%n",
                e.getKey(), e.getValue()));
    }
}
```

**Features to implement:**
- Count allocations per call site
- Distinguish stack vs heap allocations (via escape analysis heuristics)
- Generate flame graphs of allocation hotspots
- Compare before/after optimization results

## 31. Summary

| Concept | Key Takeaway |
|---------|--------------|
| **Stack** | Per-thread, LIFO, fast, automatic, fixed size |
| **Heap** | Shared, GC-managed, dynamic, slower, large |
| **Primitives** | Always on stack (as locals) or heap (as fields) |
| **Objects** | Always on heap (reference on stack) |
| **Escape Analysis** | Can move heap objects to stack if they don't escape |
| **TLAB** | Per-thread allocation buffer for fast, lock-free allocation |
| **Tuning** | `-Xss` for stack, `-Xms`/`-Xmx` for heap |
| **Errors** | StackOverflowError (recursion) vs OOM (heap full) |
| **Thread Safety** | Stack is safe; heap requires synchronization |
| **Performance** | Stack ≈ 1ns; Heap allocation ≈ 10-100ns |

**Golden Rules:**
1. Use primitives for performance-critical paths
2. Minimize object creation in hot loops
3. Let escape analysis do its job (avoid unnecessary object escapes)
4. Tune stack size for recursive algorithms
5. Monitor heap usage in production

## 32. References

- [JVM Specification - §2.5.2 Run-Time Data Areas](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.5.2)
- [JVM Specification - §2.5.3 Frame](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.5.3)
- [JEP 122: Remove the Permanent Generation](https://openjdk.org/jeps/122)
- [Oracle: Java Performance - Escape Analysis](https://www.oracle.com/java/technologies/tuning-garbage-collection-101.html)
- [OpenJDK Wiki: Escape Analysis](https://wiki.openjdk.org/display/HotSpot/Escape+Analysis)
- [Effective Java, Item 61: Prefer primitives to boxed primitives](https://books.google.com/books?id=BIpKEttKoLYC)
- [Java Performance Companion](https://www.oreilly.com/library/view/java-performance-companion/9780133759693/)
