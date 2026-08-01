# Object Lifecycle

## Introduction

The object lifecycle in Java encompasses the entire journey of an object from creation to destruction. Understanding this lifecycle is crucial for writing efficient, memory-safe applications and avoiding common pitfalls like memory leaks and resource exhaustion.

## Learning Objectives

- Understand the complete object lifecycle in Java
- Differentiate between object creation phases
- Implement proper cleanup patterns
- Recognize garbage collection behavior
- Apply best practices for resource management

## Prerequisites

- Basic Java syntax
- Understanding of classes and objects
- Familiarity with memory concepts (stack vs heap)
- Knowledge of exception handling

## Why This Concept Exists

Understanding the object lifecycle is essential because:
- **Memory management**: Objects consume heap memory that must be reclaimed
- **Resource cleanup**: Files, connections, and other resources must be released
- **Performance**: Improper lifecycle management causes memory leaks
- **Reliability**: Proper cleanup prevents resource exhaustion

## Problem Statement

Without understanding the object lifecycle:
- Memory leaks accumulate over time
- Resources like files and connections are never released
- Programs consume excessive memory
- Applications become unstable and crash

## Object Creation

```java
Person person = new Person("Alice", 30);
```

### Steps:
1. **Memory Allocation** - Heap space allocated
2. **Field Initialization** - Default values (0, null, false)
3. **Instance Initializers** - Run in declaration order
3. **Constructor Execution** - Body runs
3. **Reference Returned** - Assigned to variable

```java
Person p = new Person("Alice", 30);
// Stack: person (reference) → Heap: Person object
```

## Internal Working

### Creation Sequence
1. `new` keyword triggers allocation
2. JVM calculates object size (fields + header)
3. Memory zeroed out (default values)
4. Instance initializers executed
5. Constructor invoked
6. Reference returned

### Class Loading
- Classes loaded on first use
- Static initializers run once
- Class ready for instantiation

## JVM Perspective

- **Heap**: Where objects live
- **Metaspace**: Class metadata (replaced PermGen in Java 8)
- **Stack**: Local variables and method frames
- **GC Roots**: Starting points for garbage collection

### Memory Layout
```
Object Header (12 bytes)
├── Mark word (8 bytes) - GC state, hash code
└── Klass pointer (4 bytes) - class metadata

Instance Fields
├── Field 1 (4-8 bytes)
├── Field 2 (4-8 bytes)
└── ...

Padding (to 8-byte boundary)
```

## Memory Representation

```java
Person p = new Person("Alice", 30);
Person p2 = p;  // Both reference same object

// Stack:
// p → [ref] ─────┐
// p2 → [ref] ────┤
//                 ▼
// Heap: Person object
//   Header: {hash, gc-state, klass}
//   name: ref → String "Alice"
//   age: 30
```

## Syntax

### Object Creation
```java
ClassName obj = new ClassName(args);
```

### Reference Assignment
```java
ClassName obj2 = obj;  // Shallow copy of reference
```

### Null Reference
```java
ClassName obj = null;  // No object, just null reference
```

## Object Usage

```java
Person person = new Person("Alice", 30);
person.greet();          // Instance method
String name = person.getName();  // Getter
person.setAge(31);       // Setter
```

## Object Destruction

### Eligibility for GC
Object eligible when **no reachable references**:

```java
Person p1 = new Person("A");
Person p2 = p1;        // p2 references same object
p1 = null;             // Still reachable via p2
p2 = null;             // Now eligible for GC
```

### Reference Types
| Reference Type | GC Behavior |
|----------------|-------------|
| Strong | Never collected |
| Soft | Collected if memory low |
| Weak | Collected next GC |
| Phantom | Collected, then enqueued |

```java
import java.lang.ref.*;

SoftReference<String> soft = new SoftReference<>("data");
WeakReference<String> weak = new WeakReference<>("data");
PhantomReference<String> phantom = new PhantomReference<>("data", queue);
```

## Object Methods

### toString()
```java
@Override
public String toString() {
    return "Person{name='%s', age=%d}".formatted(name, age);
}
```

### equals() & hashCode()
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person that = (Person) o;
    return age == that.age && Objects.equals(name, that.name);
}

@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

### clone()
```java
@Override
protected Person clone() throws CloneNotSupportedException {
    return (Person) super.clone();  // Shallow copy
}
```

### finalize() (Deprecated)
```java
@Deprecated(since = "9", forRemoval = true)
@Override
protected void finalize() throws Throwable {
    try { /* cleanup */ } finally { super.finalize(); }
}
```

**Use try-with-resources or Cleaner instead.**

## Object Lifecycle Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     OBJECT LIFECYCLE                         │
├─────────────────────────────────────────────────────────────┤
│  1. CLASS LOADING                                            │
│     ├── Static fields initialized                            │
│     ├── Static blocks executed                               │
│     └── Class ready for instantiation                        │
├─────────────────────────────────────────────────────────────┤
│  2. INSTANTIATION                                            │
│     ├── new Operator                                         │
│     ├── Memory allocated on Heap                             │
│     ├── Fields default initialized                           │
│     ├── Instance initializers run                            │
│     ├── Constructor executes                                 │
│     └── Reference returned                                   │
├─────────────────────────────────────────────────────────────┤
│  3. OBJECT LIVING                                            │
│     ├── Method calls                                         │
│     ├── Field access                                         │
│     ├── State mutations                                      │
│     └── Reference passing                                    │
├─────────────────────────────────────────────────────────────┤
│  4. ELIGIBILITY FOR GC                                       │
│     ├── No strong references                                 │
│     ├── Soft/Weak/Phantom refs only                         │
│     └── GC determines collection time                        │
├─────────────────────────────────────────────────────────────┤
│  5. FINALIZATION (DEPRECATED)                                │
│     ├── finalize() called (once)                             │
│     └── Object memory reclaimed                              │
└─────────────────────────────────────────────────────────────┘
```

## Easy Example

```java
public class SimpleObject {
    private String data;

    public SimpleObject(String data) {
        this.data = data;
        System.out.println("Created: " + data);
    }

    public void use() {
        System.out.println("Using: " + data);
    }

    @Override
    public void finalize() {
        System.out.println("Finalized: " + data);
    }
}

// Lifecycle
SimpleObject obj = new SimpleObject("test");  // Created
obj.use();                                     // Using
obj = null;                                    // Eligible for GC
System.gc();                                  // Suggest GC
```

## Medium Example

```java
public class ResourceManager implements AutoCloseable {
    private final String name;
    private boolean open = true;

    public ResourceManager(String name) {
        this.name = name;
        System.out.println(name + " opened");
    }

    public void doWork() {
        if (!open) throw new IllegalStateException(name + " is closed");
        System.out.println(name + " working...");
    }

    @Override
    public void close() {
        if (open) {
            System.out.println(name + " closed");
            open = false;
        }
    }
}

// Usage with try-with-resources
try (ResourceManager rm = new ResourceManager("DB")) {
    rm.doWork();
}  // Automatically closed
```

## Hard Example

```java
public class ObjectPool<T> {
    private final Supplier<T> factory;
    private final Consumer<T> destroyer;
    private final Queue<PooledObject<T>> pool = new ConcurrentLinkedQueue<>();
    private final AtomicInteger activeCount = new AtomicInteger(0);
    private final int maxSize;

    public ObjectPool(Supplier<T> factory, Consumer<T> destroyer, int maxSize) {
        this.factory = factory;
        this.destroyer = destroyer;
        this.maxSize = maxSize;
    }

    public PooledObject<T> borrow() {
        PooledObject<T> obj = pool.poll();
        if (obj == null && activeCount.get() < maxSize) {
            obj = new PooledObject<>(factory.get(), this);
            activeCount.incrementAndGet();
        }
        return obj;
    }

    void returnObject(PooledObject<T> obj) {
        if (activeCount.get() > 0) {
            pool.offer(obj);
        } else {
            destroyer.accept(obj.get());
        }
    }

    public static class PooledObject<T> {
        private final T object;
        private final ObjectPool<T> pool;

        PooledObject(T object, ObjectPool<T> pool) {
            this.object = object;
            this.pool = pool;
        }

        public T get() { return object; }

        public void returnToPool() {
            pool.returnObject(this);
        }
    }
}
```

## Enterprise Example

```java
@Service
public class ConnectionPoolManager implements DisposableBean {
    private final HikariDataSource dataSource;
    private final MetricsCollector metrics;

    public ConnectionPoolManager(DataSourceConfig config, MetricsCollector metrics) {
        this.metrics = metrics;
        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl(config.getUrl());
        this.dataSource.setUsername(config.getUsername());
        this.dataSource.setPassword(config.getPassword());
        this.dataSource.setMaximumPoolSize(config.getMaxPoolSize());

        metrics.registerGauge("pool.active", dataSource::getHikariPoolMXBean::getActiveConnections);
        metrics.registerGauge("pool.idle", dataSource::getHikariPoolMXBean::getIdleConnections);
    }

    @Override
    public void destroy() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            metrics.unregisterGauge("pool.active");
            metrics.unregisterGauge("pool.idle");
        }
    }
}
```

## Performance

- **Object creation cost**: ~20ns for small objects
- **GC overhead**: Minor GC ~1-10ms, Major GC ~100ms+
- **Reuse objects**: Use object pools for expensive objects
- **Avoid premature optimization**: Profile first

```java
// Bad: creating objects in hot loop
for (int i = 0; i < 1000000; i++) {
    String s = new String("test");  // Unnecessary allocation
}

// Better: reuse
String s = "test";
for (int i = 0; i < 1000000; i++) {
    // Use s directly
}
```

## Object Cleanup Patterns

### Try-with-resources (Preferred)
```java
try (FileInputStream fis = new FileInputStream("file.txt")) {
    // use fis
} // fis.close() automatically called
```

### Cleaner (Java 9+)
```java
import java.lang.ref.Cleaner;

class Resource {
    private static final Cleaner cleaner = Cleaner.create();
    private final Cleaner.Cleanable cleanable;

    Resource() {
        cleanable = cleaner.register(this, () -> cleanup());
    }
    private void cleanup() { /* release resources */ }
}
```

### AutoCloseable Interface
```java
public class Resource implements AutoCloseable {
    @Override
    public void close() { /* release */ }
}

// Usage
try (Resource r = new Resource()) {
    // use
}
```

## Best Practices

1. Implement `AutoCloseable` for resources
2. Use try-with-resources for automatic cleanup
3. Prefer `Cleaner` over `finalize()`
4. Make objects immutable when possible
5. Use `WeakReference` for caches
6. Document thread safety characteristics

## Common Mistakes

1. **Forgetting to close resources** - leads to resource leaks
2. **Not implementing equals/hashCode** correctly
3. **Using finalize()** - deprecated and unreliable
4. **Holding references** longer than needed

## Pitfalls

- **Memory leaks** from static collections holding object references
- **Resource exhaustion** from unclosed connections/streams
- **Thread safety** issues with shared mutable state
- **Serialization** complications with transient fields

## Debugging Tips

1. Override `toString()` for better debugging output
2. Use `jmap -histo:live <pid>` to view live objects
3. Use `jhat` or VisualVM to analyze heap dumps
4. Add finalizers temporarily to track object lifecycle
5. Use `-XX:+PrintGCDetails` to monitor GC activity

## Comparison Table

| Cleanup Method | Pros | Cons | Recommended |
|----------------|------|------|-------------|
| try-with-resources | Automatic, reliable | Requires AutoCloseable | Yes |
| Cleaner | Non-blocking, safe | More complex setup | Yes |
| finalize() | Simple | Unreliable, deprecated | No |
| Manual close | Full control | Error-prone | Sometimes |

## Decision Tree

```
Need to clean up resources?
├── Implements AutoCloseable?
│   ├── Yes → Use try-with-resources
│   └── No → Consider Cleaner
├── Native resources?
│   ├── Yes → Cleaner + PhantomReference
│   └── No → AutoCloseable
└── Need deterministic cleanup?
    ├── Yes → Explicit close()
    └── No → GC will handle
```

## Interview Questions

1. **When is an object eligible for GC?**
   - No strong references

2. **Can we force GC?**
   - `System.gc()` suggests, not guarantees

3. **What is `finalize()`?**
   - Deprecated, use try-with-resources/Cleaner

4. **How to swap references safely?**
   - Use `AtomicReference` or synchronized block

5. **What are strong, soft, weak, and phantom references?**
   - Strong: normal reference; Soft: memory-sensitive; Weak: GC'd next; Phantom: post-mortem cleanup

6. **How does object cloning work?**
   - Shallow copy by default; override for deep copy

## Exercises

1. Create a `ResourceTracker` class that logs when objects are created and finalized
2. Implement a simple object pool with borrow/return semantics
3. Build a `WeakCache<K,V>` using `WeakHashMap`

## Assignments

1. Design a connection pool with proper lifecycle management
2. Implement a custom `Cleaner` for a class holding native resources
3. Create a memory leak detector that monitors object creation rates

## Mini Project

**Database Connection Manager**

Build a complete connection manager:

```java
public class ConnectionManager implements AutoCloseable {
    private final HikariDataSource dataSource;
    private final ScheduledExecutorService scheduler;
    private final MetricsCollector metrics;

    public ConnectionManager(Config config) {
        this.dataSource = createDataSource(config);
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.metrics = new MetricsCollector();

        startHealthCheck();
        startMetricsCollection();
    }

    public Connection getConnection() throws SQLException {
        metrics.recordConnectionRequest();
        return dataSource.getConnection();
    }

    private void startHealthCheck() {
        scheduler.scheduleAtFixedRate(() -> {
            try (Connection conn = getConnection()) {
                conn.isValid(5);
                metrics.recordHealthCheck(true);
            } catch (SQLException e) {
                metrics.recordHealthCheck(false);
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void close() {
        scheduler.shutdown();
        dataSource.close();
    }
}
```

Features to implement:
- Connection pooling with configurable limits
- Automatic health checks
- Metrics collection
- Graceful shutdown

## Summary

- Objects go through creation, usage, and destruction phases
- Garbage collection automatically reclaims unreachable objects
- Use try-with-resources or Cleaner for deterministic cleanup
- Avoid finalize() - it's deprecated and unreliable
- Implement AutoCloseable for resource management

## References

- [JLS - Object Lifecycle](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.6)
- [JLS - finalize()](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.6)
- [Effective Java Item 8: Clean Up](https://www.oracle.com/technical-resources/articles/java/effective-java.html)
- *Effective Java* (3rd Ed.) — Joshua Bloch — Items 8-9
- *Java Concurrency in Practice* — Brian Goetz
- [Java Memory Model](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html)
- [Garbage Collection in Java](https://docs.oracle.com/javase/10/gctuning/)
- [Java 9 Cleaner API](https://docs.oracle.com/javase/9/docs/api/java/lang/ref/Cleaner.html)
