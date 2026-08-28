# Java 21 LTS Deep Dive

## Virtual Threads (Why, How, When to Use)

### The Problem

Traditional threads are expensive:

```java
// Traditional threads
for (int i = 0; i < 10000; i++) {
    new Thread(() -> {
        // Each thread uses ~1MB stack space
        // Creating 10000 threads = ~10GB RAM
        blockingIOOperation();
    }).start();
}
```

### The Solution

Virtual threads are lightweight:

```java
// Virtual threads (Java 21)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10000; i++) {
        executor.submit(() -> {
            blockingIOOperation(); // Same code
        });
    }
}
// 10000 virtual threads use ~10MB RAM
```

### Why Virtual Threads?

1. **Scalability**: Millions of concurrent tasks
2. **Simplicity**: Same blocking code, different execution
3. **Resource Efficiency**: Use OS threads only when needed
4. **No Code Changes**: Existing blocking code works
5. **Reactive Benefits**: Without reactive complexity

### How They Work

```java
// Virtual thread creation
Thread.startVirtualThread(() -> {
    System.out.println("Running in virtual thread");
});

// Virtual thread factory
ThreadFactory factory = Thread.ofVirtual()
    .name("worker-", 0)
    .factory();

// Executor service
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

// Structured concurrency (preview)
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    scope.fork(() -> task1());
    scope.fork(() -> task2());
    scope.join();
}
```

### When to Use Virtual Threads

**Good Use Cases:**
```java
// I/O-bound tasks
// - HTTP requests
// - Database queries
// - File operations
// - External service calls

// High concurrency
// - Chat servers
// - WebSocket handlers
// - Long-polling endpoints
```

**Bad Use Cases:**
```java
// CPU-bound tasks (no benefit)
// - Mathematical calculations
// - Image processing
// - Data transformation

// Thread-local state heavy
// - Sessions with large thread-local data
```

### Pinning Problem

```java
// Virtual threads can be pinned (blocked on OS thread)
synchronized (lock) {
    // This pins the virtual thread!
    blockingOperation();
}

// Better: Use ReentrantLock instead
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    blockingOperation(); // Won't pin virtual thread
} finally {
    lock.unlock();
}
```

### Performance Comparison

```java
// Platform threads
ExecutorService platform = Executors.newFixedThreadPool(200);
// Limited by OS thread count

// Virtual threads
ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor();
// Limited by memory, not thread count

// Benchmark results:
// 10,000 concurrent tasks:
// Platform threads: 200 tasks/second
// Virtual threads: 10,000 tasks/second
```

---

## Record Patterns (Why)

### The Problem

Destructuring data required manual extraction:

```java
// Before record patterns
if (obj instanceof Point) {
    Point p = (Point) obj;
    int x = p.x();
    int y = p.y();
    // Use x and y
}
```

### The Solution

```java
// After record patterns
if (obj instanceof Point(int x, int y)) {
    // x and y are directly available
    System.out.println("Point at " + x + ", " + y);
}
```

### Why Record Patterns?

1. **Conciseness**: Less boilerplate code
2. **Readability**: Clear data structure
3. **Safety**: Compile-time checking
4. **Pattern Matching Integration**: Works with switch

### Nested Patterns

```java
// Nested record patterns
record Point(int x, int y) {}
record Line(Point start, Point end) {}

if (obj instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
    // Direct access to all components
    System.out.println("Line from " + x1 + "," + y1 + " to " + x2 + "," + y2);
}
```

### Pattern Variables

```java
// Pattern variables in conditions
if (obj instanceof Point(int x, int y) && x > 0 && y > 0) {
    // x and y are available
}

// Pattern variables in switch
String description = switch (shape) {
    case Circle(double r) -> "Circle with radius " + r;
    case Square(double s) -> "Square with side " + s;
    case null -> "Null shape";
};
```

### Exhaustiveness

```java
// Exhaustive switch with sealed types
sealed interface Shape permits Circle, Square {}
record Circle(double radius) implements Shape {}
record Square(double side) implements Shape {}

String describe(Shape shape) {
    return switch (shape) {
        case Circle(double r) -> "Circle: " + r;
        case Square(double s) -> "Square: " + s;
        // No default needed - exhaustive!
    };
}
```

---

## Pattern Matching Switch (Why)

### Before Java 21

```java
// Traditional switch
switch (obj.getClass().getSimpleName()) {
    case "Circle":
        Circle c = (Circle) obj;
        return "Circle: " + c.getRadius();
    case "Square":
        Square s = (Square) obj;
        return "Square: " + s.getSide();
    default:
        return "Unknown";
}
```

### After Java 21

```java
// Pattern matching switch
return switch (obj) {
    case Circle c -> "Circle: " + c.getRadius();
    case Square s -> "Square: " + s.getSide();
    case null -> "Null";
    default -> "Unknown";
};
```

### Why Pattern Matching Switch?

1. **Type Safety**: Compile-time type checking
2. **Readability**: Clear pattern matching
3. **Exhaustiveness**: Ensures all cases handled
4. **Integration**: Works with sealed classes and records

### Guarded Patterns

```java
// Guarded patterns
return switch (shape) {
    case Circle c && c.radius() > 10 -> "Large circle";
    case Circle c -> "Small circle";
    case Square s -> "Square: " + s.side();
    case null -> "Null";
};
```

### Null Handling

```java
// Null-safe switch
return switch (obj) {
    case null -> "Null value";
    case String s -> "String: " + s;
    case Integer i -> "Integer: " + i;
    default -> "Unknown";
};
```

### Multiple Patterns

```java
// Multiple patterns in one case
return switch (obj) {
    case Integer i && i > 0 -> "Positive integer";
    case Integer i && i < 0 -> "Negative integer";
    case Integer i -> "Zero";
    case String s -> "String: " + s;
    case null -> "Null";
};
```

---

## Sequenced Collections (Why)

### The Problem

No unified way to access first/last elements:

```java
// Before sequenced collections
List<String> list = List.of("a", "b", "c");
String first = list.get(0);
String last = list.get(list.size() - 1);

Deque<String> deque = ArrayDeque.of("a", "b", "c");
String first = deque.getFirst();
String last = deque.getLast();
```

### The Solution

```java
// After sequenced collections
SequencedCollection<String> list = List.of("a", "b", "c");
String first = list.getFirst();
String last = list.getLast();

// Reverse view
SequencedCollection<String> reversed = list.reversed();
```

### Why Sequenced Collections?

1. **Consistency**: Unified API for ordered collections
2. **Readability**: Clear method names
3. **Reversibility**: Built-in reverse view
4. **Deque Integration**: Works with Deque implementations

### Interface Hierarchy

```java
// Sequenced collection hierarchy
public interface SequencedCollection<E> extends Collection<E> {
    E getFirst();
    E getLast();
    void addFirst(E e);
    void addLast(E e);
    E removeFirst();
    E removeLast();
    SequencedCollection<E> reversed();
}

public interface SequencedMap<K, V> extends Map<K, V> {
    Map.Entry<K, V> firstEntry();
    Map.Entry<K, V> lastEntry();
    Map.Entry<K, V> pollFirstEntry();
    Map.Entry<K, V> pollLastEntry();
    SequencedMap<K, V> reversed();
}
```

### Implementation Support

```java
// All major collections implement SequencedCollection
SequencedCollection<String> arrayList = new ArrayList<>();
SequencedCollection<String> linkedList = new LinkedList<>();
SequencedCollection<String> treeSet = new TreeSet<>();
SequencedCollection<String> linkedHashSet = new LinkedHashSet<>();

// SequencedMap
SequencedMap<String, Integer> treeMap = new TreeMap<>();
SequencedMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
```

---

## String Templates (Preview)

### The Problem

String concatenation is verbose and error-prone:

```java
// Before string templates
String name = "John";
int age = 30;
String message = "Hello, " + name + "! You are " + age + " years old.";
```

### The Solution

```java
// After string templates (Java 21 preview)
String name = "John";
int age = 30;
String message = STR."Hello, \{name}! You are \{age} years old.";
```

### Why String Templates?

1. **Readability**: Clear interpolation syntax
2. **Type Safety**: Compile-time checking
3. **Performance**: Optimized concatenation
4. **Flexibility**: Custom processors

### Template Processors

```java
// Built-in processors
String json = STR."""
    {
      "name": "\{name}",


---

## Overview

Java 21 LTS (September 2023) is the most significant LTS since Java 8, introducing virtual threads (Project Loom), record patterns, pattern matching switch (finalized), sequenced collections, and string templates (preview). Virtual threads fundamentally change concurrency by enabling millions of lightweight threads on the JVM. This release positions Java as competitive with Go and Rust for high-concurrency workloads.

## Why This Concept Exists

Java 21 exists because concurrent programming was broken. Reactive programming (Project Reactor, RxJava) solved scalability but introduced massive complexity—callback chains, opaque stack traces, and a steep learning curve. Virtual threads bring the simplicity of blocking code with the scalability of reactive systems. Record patterns and pattern matching switch complete the algebraic data types story started with sealed classes.

## Internal Working

### Virtual Threads: Implementation

```
Virtual Thread (user thread)
  └─ Mapped to Carrier Thread (platform thread)
      └─ ForkJoinPool (default, sized to CPU cores)

When virtual thread blocks:
  1. JVM detects blocking operation (I/O, sleep, lock)
  2. JVM "unmounts" virtual thread from carrier
  3. Carrier thread takes another virtual thread
  4. When I/O completes, virtual thread is "remounted"
```

```java
// Virtual thread creation
Thread vt = Thread.ofVirtual()
    .name("worker-", 0)
    .start(() -> {
        // This runs on a carrier thread
        var response = httpClient.send(request, handler);
        // When blocking, carrier is freed for other virtual threads
    });

// Virtual thread executor
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    // Each task gets its own virtual thread
    executor.submit(task1);
    executor.submit(task2);
    // Executor closes when try block exits
}
```

### Pinning: The Hidden Problem

```java
// PINNED: Virtual thread blocked on synchronized
synchronized (lock) {
    blockingOperation(); // Virtual thread pinned to carrier
    // Carrier cannot take other virtual threads
}

// NOT PINNED: Use ReentrantLock instead
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    blockingOperation(); // Virtual thread can unmount
} finally {
    lock.unlock();
}
```

Pinning occurs when:
1. Inside `synchronized` block (monitor)
2. Inside native method that doesn't release carrier
3. During JNI calls

### Record Patterns: Deconstruction

```java
// Nested record pattern deconstruction
record Point(int x, int y) {}
record Line(Point start, Point end) {}

// Pattern matching deconstructs nested records
if (obj instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
    double distance = Math.sqrt(
        Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)
    );
}
```

### Pattern Matching Switch: Final Form

```java
// Exhaustive switch with sealed types and record patterns
public static String format(Shape shape) {
    return switch (shape) {
        case Circle(var r) when r > 100 -> "Large circle";
        case Circle(var r) -> "Circle(r=" + r + ")";
        case Square(var s) -> "Square(s=" + s + ")";
        case Triangle(var b, var h) -> "Triangle(b=" + b + ",h=" + h + ")";
        case null -> "Null shape";
    };
}
```

## Examples

### Virtual Threads: Complete Migration

```java
// BEFORE: Platform threads (Java 17)
ExecutorService executor = Executors.newFixedThreadPool(200);
List<Future<Order>> futures = orders.stream()
    .map(order -> executor.submit(() -> {
        var inventory = inventoryService.check(order); // blocks
        var pricing = pricingService.calculate(order); // blocks
        return order.withPricing(pricing);
    }))
    .collect(Collectors.toList());

// AFTER: Virtual threads (Java 21)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    List<Future<Order>> futures = orders.stream()
        .map(order -> executor.submit(() -> {
            var inventory = inventoryService.check(order); // blocks
            var pricing = pricingService.calculate(order); // blocks
            return order.withPricing(pricing);
        }))
        .collect(Collectors.toList());
}
// No code changes except executor creation!
```

### Record Patterns with Sealed Classes

```java
// Complete algebraic data type pattern
public sealed interface Expr permits Num, Add, Mul, Neg {}
public record Num(double value) implements Expr {}
public record Add(Expr left, Expr right) implements Expr {}
public record Mul(Expr left, Expr right) implements Expr {}
public record Neg(Expr operand) implements Expr {}

public static double eval(Expr expr) {
    return switch (expr) {
        case Num(var n) -> n;
        case Add(var l, var r) -> eval(l) + eval(r);
        case Mul(var l, var r) -> eval(l) * eval(r);
        case Neg(var e) -> -eval(e);
    };
}

// Nested pattern matching
public static boolean isConstant(Expr expr) {
    return switch (expr) {
        case Num(_) -> true;
        case Add(Num(_), Num(_)) -> true;
        case Mul(Num(_), Num(_)) -> true;
        case Neg(Num(_)) -> true;
        default -> false;
    };
}
```

### Sequenced Collections

```java
// Unified API for ordered collections
SequencedCollection<String> list = new ArrayList<>(List.of("a", "b", "c"));
String first = list.getFirst(); // "a"
String last = list.getLast();   // "c"

// Reverse view (no copy)
SequencedCollection<String> reversed = list.reversed();
// reversed = ["c", "b", "a"] — views original, no allocation

// Works with Deque implementations
SequencedDeque<String> deque = new ArrayDeque<>(List.of("a", "b", "c"));
deque.addFirst("z");
deque.addLast("d");
```

### String Templates (Preview)

```java
// Structured data with STR processor
String name = "John";
int age = 30;

// Built-in processor
String json = STR."""
        {
          "name": "\{name}",
          "age": \{age}
        }
        """;

// Custom processor for SQL injection prevention
String tableName = "users";
String query = SQL."""
        SELECT * FROM \{tableName} WHERE id = ?
        """;
```

## Performance

### Virtual Threads vs Platform Threads

| Metric | Platform (200 threads) | Virtual (100K threads) |
|--------|----------------------|----------------------|
| Throughput (req/s) | 200 | 15,000 |
| Memory usage | 200MB | 15MB |
| Context switch cost | ~1-10μs | ~0 (JVM managed) |
| P99 latency | 50ms | 12ms |
| Max concurrent I/O | 200 | 100,000+ |

### Pinning Impact

```java
// Synchronized block: PINS virtual thread
// ReentrantLock: DOES NOT PIN

// Benchmark: 1000 virtual threads, 10ms blocking I/O each
// Synchronized: 1000 * 10ms = 10s sequential (pinned)
// ReentrantLock: 1000 * 10ms / 8 cores ≈ 1.25s parallel
```

### Record Patterns: Zero Runtime Overhead

Pattern matching is a compile-time transformation. The generated bytecode is equivalent to manual `instanceof` checks and casts. No performance difference vs hand-written code.

## Pitfalls

### 1. Synchronized Blocks Pin Virtual Threads

```java
// BAD: synchronized blocks pin virtual threads
synchronized (this) {
    blockingIO(); // Virtual thread pinned to carrier
    // Entire carrier thread blocked
}

// GOOD: Use ReentrantLock
private final ReentrantLock lock = new ReentrantLock();

void process() {
    lock.lock();
    try {
        blockingIO(); // Virtual thread can unmount
    } finally {
        lock.unlock();
    }
}
```

### 2. ThreadLocal Overhead with Virtual Threads

```java
// BAD: Large ThreadLocal with millions of virtual threads
private static final ThreadLocal<byte[]> BUFFER =
    ThreadLocal.withInitial(() -> new byte[8192]); // 8KB * millions = GBs

// GOOD: Use ScopedValues (Java 21+) or pass data explicitly
private static final ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();

void process(User user) {
    ScopedValue.where(CURRENT_USER, user).run(() -> {
        // CURRENT_USER.get() available in this scope
        handleRequest();
    });
}
```

### 3. Assuming Virtual Threads Fix CPU-Bound Work

```java
// BAD: CPU-bound work on virtual threads (no benefit)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> {
        // This is CPU-bound, virtual threads don't help
        heavyComputation(); // Wastes carrier threads
    });
}

// GOOD: Use platform thread pool for CPU-bound work
try (var cpuExecutor = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors())) {
    cpuExecutor.submit(() -> heavyComputation());
}
```

### 4. String Templates (Preview) Instability

```java
// BAD: Using preview features in production
String json = STR."""
    {"name": "\{name}"}
""";
// May change in future Java versions

// GOOD: Use String.formatted() or MessageFormat
String json = String.format("{\"name\": \"%s\"}", name);
```

### 5. Ignoring Sequenced Collections API

```java
// BAD: Manual first/last access
String first = list.get(0);
String last = list.get(list.size() - 1);

// GOOD: Use SequencedCollection methods
String first = list.getFirst();
String last = list.getLast();
```

## References

- [Java 21 Release Notes](https://www.oracle.com/java/technologies/javase/21-relnote-articles.html)
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
- [JEP 440: Record Patterns](https://openjdk.org/jeps/440)
- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)
- [JEP 431: Sequenced Collections](https://openjdk.org/jeps/431)
- [JEP 430: String Templates (Preview)](https://openjdk.org/jeps/430)
- *Virtual Threads: Patterns and Practices* by Oracle
- [OpenJDK Source Code](https://github.com/openjdk/jdk)
