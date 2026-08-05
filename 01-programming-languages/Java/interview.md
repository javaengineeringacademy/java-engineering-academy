# Java Interview Questions

> 20 Java interview questions with detailed answers.

## Core Java

### 1. JDK vs JRE vs JVM

| Component | Description |
|-----------|-------------|
| JDK | Development Kit: compiler, debugger, tools |
| JRE | Runtime: JVM + class libraries |
| JVM | Virtual Machine: executes bytecode |

### 2. JVM Memory Model

| Area | Description |
|------|-------------|
| Heap | Object instances, garbage collected |
| Metaspace | Class metadata, native memory |
| Stack | Method frames, local variables |
| PC Register | Current bytecode instruction address |

### 3. OOP Pillars

1. **Encapsulation**: Hiding state via private fields and accessors
2. **Inheritance**: Code reuse through class hierarchy (extends)
3. **Polymorphism**: Same interface, different implementations
4. **Abstraction**: Hiding complexity, exposing essentials

### 4. Generics and Type Erasure

```java
// Compile-time type safety
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);

// Type erasure: generics removed at runtime
List<String> a = new ArrayList<>();
List<Integer> b = new ArrayList<>();
a.getClass() == b.getClass();  // true
```

### 5. == vs .equals()

```java
String a = new String("hello");
String b = new String("hello");
a == b;      // false (different objects)
a.equals(b); // true (same content)

String c = "hello";
String d = "hello";
c == d;      // true (string pool)
```

## Collections

### 6. HashMap vs ConcurrentHashMap

| Feature | HashMap | ConcurrentHashMap |
|---------|---------|-------------------|
| Thread Safe | No | Yes |
| Null Keys | One allowed | Not allowed |
| Locking | None | CAS + synchronized (Java 8+) |
| Performance | Single-threaded | Concurrent |

### 7. ArrayList vs LinkedList

| Operation | ArrayList | LinkedList |
|-----------|-----------|------------|
| get(index) | O(1) | O(n) |
| add(end) | O(1) amortized | O(1) |
| remove(index) | O(n) | O(n) |
| Memory | Compact | Extra pointers |

### 8. HashMap Internals

Java 8+ uses array of Node buckets. Hash maps to bucket index. Collisions stored as linked list, converted to red-black tree when bucket exceeds 8 entries. Resizes when size exceeds capacity times load factor (default 0.75).

## Concurrency

### 9. wait() vs sleep()

| Method | Releases Lock | Called On |
|--------|---------------|-----------|
| wait() | Yes | Any object |
| sleep() | No | Thread class |

### 10. synchronized vs ReentrantLock

| Feature | synchronized | ReentrantLock |
|---------|-------------|---------------|
| Unlock | Automatic | Manual unlock() |
| Timeout | No | tryLock(timeout) |
| Fairness | No | Configurable |
| Interruptible | No | lockInterruptibly |

### 11. Thread Lifecycle

```
NEW -> RUNNABLE -> BLOCKED/WAITING/TIMED_WAITING -> TERMINATED
```

### 12. CompletableFuture

```java
CompletableFuture.supplyAsync(() -> fetchData())
    .thenApply(data -> transform(data))
    .thenAccept(result -> process(result))
    .exceptionally(ex -> handleError(ex));
```

## JVM Internals

### 13. Class Loading Process

1. **Loading**: Read .class bytes into memory
2. **Linking**: Verify, prepare, resolve references
3. **Initialization**: Execute static blocks

### 14. Garbage Collection Algorithms

| Algorithm | Type | Use Case |
|-----------|------|----------|
| Serial | Single-threaded | Small apps |
| Parallel | Multi-threaded | Throughput |
| G1 | Region-based | Balanced (default) |
| ZGC | Concurrent | Ultra-low latency |

### 15. JIT Compilation

Interpreter runs bytecode directly. JIT compiles hot methods to native code. Tiered compilation: C1 (client) compiles quickly, C2 (server) applies aggressive optimizations.

## Java 8+ Features

### 16. Lambda Expressions

```java
// Before
list.sort(new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
});

// After
list.sort(Comparator.naturalOrder());
```

### 17. Stream API

```java
List<String> result = list.stream()
    .filter(s -> s.length() > 5)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());
```

### 18. Optional

```java
Optional.ofNullable(user)
    .map(User::getEmail)
    .ifPresent(this::sendEmail);
```

## Java 17+ Features

### 19. Records

```java
public record Point(double x, double y) {
    public Point {
        Objects.requireNonNull(x);
    }
}
```

### 20. Pattern Matching for instanceof

```java
// Before
if (obj instanceof String) {
    String s = (String) obj;
}

// After
if (obj instanceof String s) {
    // use s directly
}
```

## Behavioral Questions

### Design a URL Shortener

Key considerations: hash function, collision handling, redirect performance, analytics tracking, custom aliases.

### Rate Limiter Implementation

Token bucket, sliding window, or fixed window algorithms. Redis for distributed rate limiting.

### Thread-Safe Singleton

```java
public enum Singleton {
    INSTANCE;
    
    public void doSomething() { }
}
```

## References

- [Java Interview Questions - Baeldung](https://www.baeldung.com/java-interview-questions)
- [Effective Java - Joshua Bloch](https://www.oreilly.com/library/view/effective-java/9780134686097/)

---
**Prerequisites:** [Java core-concepts](core-concepts.md)
**Related:** [Java patterns](patterns.md) | [Java best-practices](best-practices.md)
**Next:** [Java hands-on-labs](hands-on-labs.md)
