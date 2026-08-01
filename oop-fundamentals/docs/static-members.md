# Static Members in Java

## 1. Introduction

The `static` keyword in Java declares members that belong to the class itself rather than to any specific instance. Static members are loaded once when the class is loaded and shared across all instances. They are fundamental for utility methods, constants, factory methods, singleton patterns, and class-level state management. This guide provides a comprehensive deep-dive into every aspect of static members in Java 21.

## 2. Learning Objectives

After completing this guide, you will be able to:

- Distinguish between static and instance members and their use cases
- Implement static variables for shared class-level state
- Create utility classes with static methods
- Use static blocks for complex initialization
- Understand static nested classes and their advantages
- Identify thread safety concerns with static mutable state
- Apply best practices for static member design

## 3. Prerequisites

- Basic Java class and object syntax
- Understanding of inheritance and polymorphism
- Familiarity with JVM class loading (helpful but not required)
- JDK 21+ for running examples

## 4. Why This Concept Exists

Without static members, you would need to:

1. Create an instance just to call a utility method (`Math.sqrt()`)
2. Duplicate constants across every class that uses them
3. Have no clean way to initialize shared state before any object is created
4. Lose the ability to enforce single instances (singletons)

Static members solve these problems by providing class-level state and behavior that exists independently of objects. They are the bridge between procedural and object-oriented programming within Java's OOP framework.

## 5. Problem Statement

Consider a banking application:

- **Interest rate** is the same for all accounts — duplicating it per account wastes memory
- **Tax calculation** doesn't need account state — creating an `Account` just to call `calculateTax()` is wasteful
- **Database connection** should be initialized once — doing it per object causes connection pool exhaustion

The core problem: **How do you share state and behavior across all instances without creating an object?**

## 6. Theory

### Static Variables (Class Variables)

A static variable is a single copy shared by all instances of a class. It is stored in the Metaspace (class metadata area), not in individual objects on the heap.

**Characteristics:**
- One copy per class, regardless of how many instances exist
- Initialized during class loading (before any instance is created)
- Accessible via `ClassName.variableName`
- Lifetime: from class loading to class unloading

### Static Methods (Class Methods)

A static method belongs to the class, not to any instance. It can only access static members directly — it has no `this` reference.

**Characteristics:**
- No implicit `this` parameter
- Cannot access instance variables or instance methods directly
- Can be overloaded but not overridden (method hiding)
- Callable via `ClassName.methodName()`

### Static Blocks (Static Initializers)

A static block is a block of code enclosed in `static { }` that executes once when the class is loaded. It is used for complex static variable initialization that cannot be done in a single expression.

**Characteristics:**
- Executed exactly once during class loading
- Runs in declaration order within the class
- Can throw checked exceptions (wrapped in `ExceptionInInitializerError`)
- Multiple static blocks execute sequentially

### Static Import

`import static` allows importing static members so they can be used without class name qualification:

```java
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
```

### Static Nested Classes

A static nested class is a class defined inside another class with the `static` modifier. Unlike inner classes, it does not have an implicit reference to the outer class instance.

## 7. Internal Working

### Class Loading and Static Initialization

When the JVM loads a class, the following steps occur:

1. **Loading**: The class loader reads the `.class` file bytecode
2. **Linking**: 
   - Verification: Bytecode is validated
   - Preparation: Static variables are allocated and initialized to default values (0, null, false)
   - Resolution: Symbolic references are resolved
3. **Initialization**: `<clinit>` method runs
   - Static variables are initialized in declaration order
   - Static blocks execute in declaration order
   - Parent class is initialized first

```java
public class Demo {
    static int a = 10;         // Step 1: a = 10
    static {
        System.out.println("Block 1: a = " + a);  // a = 10
    }
    static int b = a + 5;     // Step 2: b = 15
    static {
        System.out.println("Block 2: b = " + b);  // b = 15
    }
}
```

### `<clinit>` Method

The compiler generates a `<clinit>` (class initializer) method containing all static initialization code. This method:

- Is thread-safe (the JVM guarantees it is called at most once)
- Is called by the class loader
- Can throw exceptions, which propagate to any code that uses the class
- Is not called if the class is never used (lazy loading)

## 8. JVM Perspective

### Static Variable Storage

Static variables are stored in the **Method Area** (or **Metaspace** in Java 8+) of the JVM. They are part of the class metadata, not the heap.

```
JVM Memory Layout
┌─────────────────────────────────────────┐
│              Metaspace                   │
│  ┌─────────────────────────────────┐    │
│  │  Class: Counter                 │    │
│  │  - static count: 0 → 5 → 12    │    │
│  │  - instance size: 0 bytes       │    │
│  │  - method table: [...]          │    │
│  └─────────────────────────────────┘    │
├─────────────────────────────────────────┤
│              Heap                        │
│  ┌─────────────┐ ┌─────────────┐        │
│  │ Counter #1  │ │ Counter #2  │        │
│  │ (no fields) │ │ (no fields) │        │
│  └─────────────┘ └─────────────┘        │
└─────────────────────────────────────────┘
```

### Static Method Invocation

Static methods are invoked using the `invokestatic` bytecode instruction, which is faster than `invokevirtual` (used for instance methods) because:

- No null check on receiver is needed
- No virtual method table lookup is required
- No polymorphic dispatch overhead

### Class Unloading and Static Variables

Static variables live as long as the class is loaded. In most applications, classes are never unloaded, so static variables live for the entire application lifetime. In web applications with custom class loaders (e.g., hot deployment), classes can be unloaded, and their static variables become eligible for GC.

## 9. Memory Representation

### Static vs Instance Memory Layout

```java
public class Counter {
    private static int count = 0;  // Metaspace
    private int id;                 // Heap (in each instance)

    public Counter() {
        count++;
        id = count;
    }
}
```

```
Metaspace: Counter.class
├── static count = 3 (after 3 instances created)
├── method table
└── constant pool

Heap:
├── Counter instance #1 { id = 1 }
├── Counter instance #2 { id = 2 }
└── Counter instance #3 { id = 3 }
```

### What Happens with Each `new Counter()`

| Action | Memory Effect |
|--------|---------------|
| `Counter c1 = new Counter()` | Metaspace: count=1; Heap: id=1 |
| `Counter c2 = new Counter()` | Metaspace: count=2; Heap: id=2 |
| `Counter c3 = new Counter()` | Metaspace: count=3; Heap: id=3 |
| `c1 = null` | Heap: #1 eligible for GC; Metaspace: count=3 (unchanged) |

### Static Final Constants

Static final fields (constants) are inlined by the compiler:

```java
public class Constants {
    public static final int MAX_SIZE = 100;  // Inlined at compile time
}

// This:
int x = Constants.MAX_SIZE;
// Becomes (in bytecode):
int x = 100;
```

## 10. Architecture Diagram

### Static Member Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Class Loader                      │
│  ┌──────────────────────────────────────────────┐   │
│  │  Loads: Counter.class                        │   │
│  │  Executes: <clinit> (static initializer)     │   │
│  └──────────────────┬───────────────────────────┘   │
│                     │                               │
│                     ▼                               │
│  ┌──────────────────────────────────────────────┐   │
│  │  Metaspace (Method Area)                     │   │
│  │  ┌──────────────────────────────────────┐    │   │
│  │  │  Class: Counter                      │    │   │
│  │  │  - Static field: count (int, value=3)│    │   │
│  │  │  - Method: increment()               │    │   │
│  │  │  - Method: getCount()                │    │   │
│  │  │  - Instance field: id (int, offset=0)│    │   │
│  │  └──────────────────────────────────────┘    │   │
│  └──────────────────────────────────────────────┘   │
│                                                     │
│  ┌──────────────────────────────────────────────┐   │
│  │  Heap                                        │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐    │   │
│  │  │ Counter  │ │ Counter  │ │ Counter  │    │   │
│  │  │ #1: {id} │ │ #2: {id} │ │ #3: {id} │    │   │
│  │  └──────────┘ └──────────┘ └──────────┘    │   │
│  └──────────────────────────────────────────────┘   │
│                                                     │
│  ┌──────────────────────────────────────────────┐   │
│  │  Thread Stacks                               │   │
│  │  Thread 1: counter (ref → Heap)              │   │
│  │  Thread 2: counter (ref → Heap)              │   │
│  └──────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
```

## 11. Flow Diagram

### Static Initialization Flow

```
Class First Used
       │
       ▼
┌─────────────┐
│ Class Loader │
│ loads .class│
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ Linking      │
│ - Verify     │
│ - Prepare    │
│ - Resolve    │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────┐
│ Initialization (<clinit>)           │
│                                     │
│ Parent class <clinit> (if needed)   │
│       │                             │
│       ▼                             │
│ static field1 = expr1               │
│ static { block1 }                   │
│ static field2 = expr2               │
│ static { block2 }                   │
│ ...                                 │
└──────┬──────────────────────────────┘
       │
       ▼
┌─────────────┐
│ Class Ready  │
│ Use freely   │
└─────────────┘
```

### Static Method Call Flow

```
Code: Counter.getCount()
       │
       ▼
┌──────────────────┐
│ invokestatic     │
│ (bytecode instr) │
└──────┬───────────┘
       │
       ▼
┌──────────────────┐
│ Check: Class     │
│ loaded?          │
├── YES → Execute  │
│    method        │
└── NO  → Load     │
    class first,   │
    then execute   │
└──────────────────┘
```

## 12. Syntax

### Static Variables

```java
public class Employee {
    // Static variable (class-level)
    private static int employeeCount = 0;
    public static final String COMPANY = "Acme Corp";
    private static final AtomicInteger nextId = new AtomicInteger(0);

    // Instance variable (object-level)
    private int id;
    private String name;

    public Employee(String name) {
        this.id = nextId.incrementAndGet();
        this.name = name;
        employeeCount++;
    }
}
```

### Static Methods

```java
public class StringUtils {
    // Utility method - no instance needed
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    // Overloaded static methods
    public static String repeat(String s, int count) {
        return s.repeat(count);
    }

    public static String repeat(char c, int count) {
        return String.valueOf(c).repeat(count);
    }
}
```

### Static Blocks

```java
public class AppConfig {
    private static final Map<String, String> config;
    private static final Logger logger;

    static {
        // Complex initialization
        config = new HashMap<>();
        try (InputStream in = AppConfig.class.getResourceAsStream("/app.properties")) {
            Properties props = new Properties();
            props.load(in);
            props.forEach((k, v) -> config.put((String) k, (String) v));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        logger = Logger.getLogger(AppConfig.class.getName());
    }

    // Multiple static blocks execute in order
    static {
        logger.info("Config loaded: " + config.size() + " entries");
    }
}
```

### Static Import

```java
import static java.lang.Math.*;
import static java.util.Objects.requireNonNull;

public class Geometry {
    public double circleArea(double radius) {
        return PI * pow(radius, 2);  // No Math. prefix needed
    }

    public Point midpoint(Point a, Point b) {
        requireNonNull(a, "Point a");  // No Objects. prefix
        requireNonNull(b, "Point b");
        return new Point((a.x + b.x) / 2, (a.y + b.y) / 2);
    }
}
```

### Static Nested Classes

```java
public class Tree {
    private Node root;

    // Static nested class - no reference to outer instance
    private static class Node {
        int value;
        Node left, right;

        Node(int value) {
            this.value = value;
        }
    }

    // Inner class (non-static) - has reference to outer
    private class Iterator {
        // Can access Tree.this.root
    }
}
```

## 13. Easy Example

```java
public class MathConstants {
    // Static constants
    public static final double PI = 3.14159265358979;
    public static final double E = 2.71828182845904;
    public static final double SQRT2 = 1.41421356237309;

    // Static utility method
    public static double circleArea(double radius) {
        return PI * radius * radius;
    }

    public static void main(String[] args) {
        // Access without creating an instance
        System.out.println("PI = " + MathConstants.PI);
        System.out.println("Area = " + MathConstants.circleArea(5.0));
    }
}
```

## 14. Medium Example

```java
public class BankAccount {
    // Shared across all accounts
    private static double interestRate = 0.05;
    private static int totalAccounts = 0;
    private static final List<BankAccount> allAccounts = new ArrayList<>();

    // Instance-specific
    private final String accountId;
    private double balance;

    public BankAccount(double initialBalance) {
        this.accountId = "ACC-" + (++totalAccounts);
        this.balance = initialBalance;
        allAccounts.add(this);
    }

    // Static method - operates on class-level state
    public static void setInterestRate(double rate) {
        if (rate < 0 || rate > 1) {
            throw new IllegalArgumentException("Rate must be 0-1");
        }
        interestRate = rate;
    }

    // Static method - returns class-level state
    public static double getInterestRate() {
        return interestRate;
    }

    // Static method - queries all accounts
    public static double getTotalDeposits() {
        return allAccounts.stream()
            .mapToDouble(account -> account.balance)
            .sum();
    }

    // Instance method - uses static state
    public void applyInterest() {
        balance += balance * interestRate;
    }

    // Instance method
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
    }

    @Override
    public String toString() {
        return accountId + ": $" + String.format("%.2f", balance);
    }

    public static void main(String[] args) {
        BankAccount acc1 = new BankAccount(1000);
        BankAccount acc2 = new BankAccount(2000);

        System.out.println("Initial rate: " + BankAccount.getInterestRate());
        System.out.println("Total deposits: $" + BankAccount.getTotalDeposits());

        BankAccount.setInterestRate(0.08);
        acc1.applyInterest();
        acc2.applyInterest();

        System.out.println("After interest:");
        System.out.println(acc1);
        System.out.println(acc2);
        System.out.println("Total: $" + BankAccount.getTotalDeposits());
    }
}
```

## 15. Hard Example

```java
public class Cache<K, V> {
    // Thread-safe static cache for all Cache instances
    private static final ConcurrentHashMap<String, Cache<?, ?>> instances = new ConcurrentHashMap<>();
    private static final AtomicInteger cacheCount = new AtomicInteger(0);

    // Per-instance cache
    private final ConcurrentHashMap<K, V> store = new ConcurrentHashMap<>();
    private final String name;
    private final int maxSize;
    private final Duration ttl;
    private final ConcurrentHashMap<K, Long> expiryTimes = new ConcurrentHashMap<>();

    // Private constructor - force use of factory
    private Cache(String name, int maxSize, Duration ttl) {
        this.name = name;
        this.maxSize = maxSize;
        this.ttl = ttl;
    }

    // Static factory method (thread-safe singleton per name)
    public static <K, V> Cache<K, V> getInstance(String name, int maxSize, Duration ttl) {
        return (Cache<K, V>) instances.computeIfAbsent(name,
            n -> new Cache<>(n, maxSize, ttl));
    }

    // Static utility: list all cache instances
    public static Map<String, Cache<?, ?>> getAllInstances() {
        return Collections.unmodifiableMap(instances);
    }

    // Static utility: clear all caches
    public static void clearAll() {
        instances.values().forEach(Cache::clear);
    }

    // Static utility: get total memory usage
    public static long getTotalSize() {
        return instances.values().stream()
            .mapToLong(Cache::size)
            .sum();
    }

    // Instance methods
    public V get(K key) {
        Long expiry = expiryTimes.get(key);
        if (expiry != null && System.currentTimeMillis() > expiry) {
            store.remove(key);
            expiryTimes.remove(key);
            return null;
        }
        return store.get(key);
    }

    public void put(K key, V value) {
        if (store.size() >= maxSize) {
            evictOldest();
        }
        store.put(key, value);
        expiryTimes.put(key, System.currentTimeMillis() + ttl.toMillis());
    }

    public void clear() {
        store.clear();
        expiryTimes.clear();
    }

    public int size() {
        return store.size();
    }

    private void evictOldest() {
        Optional<Map.Entry<K, Long>> oldest = expiryTimes.entrySet().stream()
            .min(Map.Entry.comparingByValue());
        oldest.ifPresent(entry -> {
            store.remove(entry.getKey());
            expiryTimes.remove(entry.getKey());
        });
    }

    @Override
    public String toString() {
        return "Cache[" + name + "] size=" + store.size();
    }
}
```

## 16. Enterprise Example

```java
public class DatabaseConnectionPool {
    // Static singleton instance
    private static volatile DatabaseConnectionPool instance;
    private static final Object lock = new Object();

    // Configuration (static)
    private static final int DEFAULT_POOL_SIZE = 10;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Logger logger = Logger.getLogger(DatabaseConnectionPool.class.getName());

    // Connection pool (instance, but accessed via static method)
    private final BlockingQueue<Connection> pool;
    private final String url;
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);

    // Static factory method (double-checked locking)
    public static DatabaseConnectionPool getInstance(String url) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DatabaseConnectionPool(url, DEFAULT_POOL_SIZE);
                }
            }
        }
        return instance;
    }

    public static DatabaseConnectionPool getInstance(String url, int poolSize) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DatabaseConnectionPool(url, poolSize);
                }
            }
        }
        return instance;
    }

    // Static utility: shutdown all pools (for application cleanup)
    public static void shutdownAll() {
        instances.forEach(DatabaseConnectionPool::shutdown);
        instances.clear();
    }

    // Static utility: health check across all pools
    public static Map<String, Boolean> healthCheckAll() {
        Map<String, Boolean> results = new HashMap<>();
        instances.forEach((name, pool) ->
            results.put(name, pool.isHealthy()));
        return results;
    }

    // Registry of all pool instances
    private static final Map<String, DatabaseConnectionPool> instances = new ConcurrentHashMap<>();

    private DatabaseConnectionPool(String url, int poolSize) {
        this.url = url;
        this.pool = new LinkedBlockingQueue<>(poolSize);
        this.instances.put(url, this);

        // Initialize pool
        for (int i = 0; i < poolSize; i++) {
            pool.offer(createConnection());
        }

        logger.info("Connection pool initialized: " + url + " [" + poolSize + " connections]");
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create connection", e);
        }
    }

    public Connection getConnection() throws InterruptedException {
        if (isShutdown.get()) {
            throw new IllegalStateException("Pool is shutdown");
        }
        Connection conn = pool.poll(DEFAULT_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
        if (conn == null) {
            throw new RuntimeException("Connection timeout");
        }
        return conn;
    }

    public void returnConnection(Connection conn) {
        if (conn != null && !isShutdown.get()) {
            pool.offer(conn);
        }
    }

    public void shutdown() {
        if (isShutdown.compareAndSet(false, true)) {
            Connection conn;
            while ((conn = pool.poll()) != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
            instances.remove(url);
            logger.info("Connection pool shutdown: " + url);
        }
    }

    public boolean isHealthy() {
        return !isShutdown.get() && !pool.isEmpty();
    }
}
```

## 17. Performance

### Static Method Performance

| Operation | Latency | Notes |
|-----------|---------|-------|
| Static method call | ~1-2 ns | `invokestatic`, no polymorphism |
| Instance method call | ~2-5 ns | `invokevirtual`, vtable lookup |
| Interface method call | ~5-10 ns | `invokeinterface`, dynamic dispatch |

### Static Variable Access

| Operation | Latency | Notes |
|-----------|---------|-------|
| Static field read | ~1-2 ns | Direct access from Metaspace |
| Instance field read | ~1-2 ns | Direct access (after object pointer dereference) |
| Static final constant | ~0 ns | Inlined by compiler |

### When Static is Faster

1. **Utility methods**: No object creation needed
2. **Constants**: Inlined at compile time
3. **Factory methods**: Control object creation without instance overhead
4. **Mathematical operations**: `Math.sqrt()` vs creating a `Math` object

## 18. Time Complexity

| Operation | Complexity | Notes |
|-----------|------------|-------|
| Static method call | O(1) | Direct invocation |
| Static variable access | O(1) | Direct memory access |
| Static block execution | O(1) amortized | Runs once per class load |
| Static factory (singleton) | O(1) amortized | `computeIfAbsent` is O(1) |

## 19. Space Complexity

| Component | Space | Notes |
|-----------|-------|-------|
| Static variable | O(1) per class | Single copy regardless of instances |
| Static method | O(1) | Bytecode in Metaspace, shared |
| Static block | O(1) | Compiled into `<clinit>` |
| Static nested class | O(1) | Loaded separately, no outer reference |

**Memory savings example:**
```java
// Without static: each instance has its own copy
public class Bad {
    final double PI = 3.14159;  // 8 bytes × N instances
}

// With static: single copy shared by all
public class Good {
    static final double PI = 3.14159;  // 8 bytes total
}
```

## 20. Thread Safety

### Thread-Safe Static Variables

```java
// GOOD: AtomicInteger for counters
public class SafeCounter {
    private static final AtomicInteger count = new AtomicInteger(0);

    public static void increment() {
        count.incrementAndGet();  // Thread-safe
    }

    public static int getCount() {
        return count.get();
    }
}
```

### Thread-Unsafe Static Variables

```java
// BAD: Not thread-safe
public class UnsafeCounter {
    private static int count = 0;  // Shared, not synchronized

    public static void increment() {
        count++;  // Race condition: read-modify-write
    }
}

// FIX: Synchronize access
public class FixedCounter {
    private static int count = 0;

    public static synchronized void increment() {
        count++;  // Now thread-safe
    }
}
```

### Static Synchronized Methods

```java
public class ConnectionManager {
    private static final Set<String> activeConnections = new HashSet<>();

    // Lock is on the Class object (ConnectionManager.class)
    public static synchronized boolean addConnection(String id) {
        return activeConnections.add(id);
    }

    public static synchronized boolean removeConnection(String id) {
        return activeConnections.remove(id);
    }

    public static synchronized int getConnectionCount() {
        return activeConnections.size();
    }
}
```

### Thread-Safe Lazy Initialization

```java
public class ExpensiveResource {
    private static volatile ExpensiveResource instance;
    private static final Object lock = new Object();

    // Double-checked locking
    public static ExpensiveResource getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ExpensiveResource();
                }
            }
        }
        return instance;
    }

    // Or simply use holder pattern (preferred)
    private static class Holder {
        static final ExpensiveResource INSTANCE = new ExpensiveResource();
    }

    public static ExpensiveResource getInstanceHolder() {
        return Holder.INSTANCE;  // Thread-safe, lazy, no synchronization overhead
    }
}
```

## 21. Best Practices

1. **Use static methods for stateless utilities** — No object needed, no side effects
2. **Prefer static final constants over magic numbers** — Readable, maintainable, inlined
3. **Use static factory methods over constructors** — Named returns, caching, subtype control
4. **Avoid static mutable state when possible** — Hard to test, hard to reason about
5. **Use Atomic* classes for static counters** — Thread-safe without synchronization overhead
6. **Keep static blocks small and focused** — Complex initialization should be in a method
7. **Use static nested classes over inner classes** — When you don't need outer class reference
8. **Document thread safety of static members** — Users need to know if they're safe
9. **Use the holder pattern for lazy singletons** — Thread-safe without synchronization
10. **Test static methods with dependency injection** — Make static code testable

## 22. Common Mistakes

### Mistake 1: Accessing Instance Members in Static Context

```java
public class UserService {
    private String currentUser;  // Instance field

    public static void greet() {
        System.out.println("Hello, " + currentUser);  // Compile error!
    }
}

// FIX: Remove static or pass instance as parameter
public class UserService {
    private String currentUser;

    public void greet() {
        System.out.println("Hello, " + currentUser);  // OK
    }

    public static void greet(UserService service) {
        System.out.println("Hello, " + service.currentUser);  // OK
    }
}
```

### Mistake 2: Thread-Unsafe Static Mutable State

```java
// BAD: Race condition
public class Config {
    private static Map<String, String> settings = new HashMap<>();

    public static void set(String key, String value) {
        settings.put(key, value);  // Not thread-safe!
    }

    public static String get(String key) {
        return settings.get(key);  // Not thread-safe!
    }
}

// FIX: Use ConcurrentHashMap
public class Config {
    private static final ConcurrentHashMap<String, String> settings = new ConcurrentHashMap<>();

    public static void set(String key, String value) {
        settings.put(key, value);
    }

    public static String get(String key) {
        return settings.get(key);
    }
}
```

### Mistake 3: Static Methods That Should Be Instance Methods

```java
// BAD: Why static? It operates on instance state
public class Order {
    private BigDecimal total;

    public static boolean isValid(Order order) {
        return order.total.compareTo(BigDecimal.ZERO) > 0;
    }
}

// FIX: Make it an instance method
public class Order {
    private BigDecimal total;

    public boolean isValid() {
        return total.compareTo(BigDecimal.ZERO) > 0;
    }
}
```

### Mistake 4: Static Import Abuse

```java
// BAD: Reduces readability
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

public class Calculator {
    public int compute() {
        return add(1, 2);  // Which add? Confusing.
    }
}

// FIX: Import only what's needed frequently
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

public class Calculator {
    public double circleArea(double r) {
        return PI * sqrt(r);  // Clear and readable
    }
}
```

### Mistake 5: Static Initialization Order Issues

```java
// BAD: Forward reference in static initializer
public class Order {
    private static int count = anotherStatic();  // Compiles but value is 0

    private static int anotherStatic() {
        return count + 1;  // count is still 0 (default value)
    }
}

// FIX: Initialize in correct order
public class Order {
    private static int base = 10;  // First
    private static int count = base + 1;  // Then (count = 11)
}
```

## 23. Pitfalls

### Pitfall 1: Static State Leaks in Tests

```java
// BAD: Static state persists between tests
public class UserService {
    private static final List<User> users = new ArrayList<>();

    public static void addUser(User user) {
        users.add(user);
    }

    public static List<User> getUsers() {
        return users;
    }
}

// Tests interfere with each other
@Test void test1() { UserService.addUser(user1); }
@Test void test2() { UserService.addUser(user2); }
// test2 sees user1 from test1!

// FIX: Reset static state in @BeforeEach
@BeforeEach
void setUp() {
    UserService.getUsers().clear();
}
```

### Pitfall 2: Static Methods Prevent Mocking

```java
// BAD: Cannot mock static method in unit tests
public class EmailService {
    public static boolean sendEmail(String to, String body) {
        // Uses SMTP - cannot test without real server
        return SmtpClient.send(to, body);
    }
}

// FIX: Use instance method with dependency injection
public class EmailService {
    private final SmtpClient client;

    public EmailService(SmtpClient client) {
        this.client = client;
    }

    public boolean sendEmail(String to, String body) {
        return client.send(to, body);
    }
}

// Now you can mock SmtpClient in tests
```

### Pitfall 3: Static Nested Class vs Inner Class

```java
public class Outer {
    private String message = "Hello";

    // Static nested class - no reference to outer
    static class StaticNested {
        void print() {
            // System.out.println(message);  // Compile error!
        }
    }

    // Inner class - has reference to outer
    class Inner {
        void print() {
            System.out.println(message);  // OK
        }
    }
}
```

## 24. Debugging Tips

### Debugging Static Initialization

```java
public class DebugStatic {
    private static int value;

    static {
        System.out.println("Static block: value = " + value);
        value = 42;
        System.out.println("Static block: value = " + value);
    }

    public static void main(String[] args) {
        System.out.println("Main: value = " + DebugStatic.value);
    }
}
// Output:
// Static block: value = 0
// Static block: value = 42
// Main: value = 42
```

### Debugging Class Loading

```bash
# Trace class loading
java -verbose:class MyApp

# Print class loading details
java -XX:+TraceClassLoading -XX:+TraceClassUnloading MyApp
```

### Debugging Thread Safety

```java
public class ThreadDebug {
    private static int shared = 0;

    public static void increment() {
        int local = shared;      // Read
        Thread.yield();          // Encourage context switch
        shared = local + 1;      // Write (may overwrite another thread's update)
    }

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = Thread.ofVirtual().start(() -> {
                for (int j = 0; j < 10000; j++) increment();
            });
        }
        for (Thread t : threads) t.join();
        System.out.println("Expected: 100000, Actual: " + shared);
    }
}
```

### JVM Diagnostic Commands

```bash
# List loaded classes
jcmd <pid> VM.classloader

# Print static field values
jcmd <pid> GC.class_stats

# Thread dump (see if threads are blocked on static synchronized)
jstack <pid>
```

## 25. Comparison Table

| Aspect | Static | Instance |
|--------|--------|----------|
| **Memory** | Metaspace (single copy per class) | Heap (per object) |
| **Access** | `ClassName.member` | `object.member` |
| **`this` reference** | Not available | Available |
| **Override** | No (hidden, not overridden) | Yes (polymorphic dispatch) |
| **Serialize** | Not serialized with object | Serialized with object |
| **Lifetime** | Class loading → unloading | Object creation → GC |
| **Inheritance** | Inherited but not overridden | Inherited and overridden |
| **Synchronization** | Lock on Class object | Lock on object instance |
| **Testing** | Harder to mock | Easy to mock |
| **Coupling** | Tighter (class-level) | Looser (instance-level) |
| **Memory per instance** | 0 bytes | Size of fields |
| **Thread Safety** | Shared state (requires care) | Thread-confined (safe) |

## 26. Decision Tree

```
Should this be static?
│
├── Does it need access to instance state?
│   ├── YES → Instance method/field
│   └── NO ↓
│
├── Is it a utility function (pure logic)?
│   ├── YES → Static method
│   └── NO ↓
│
├── Is it a constant (immutable value)?
│   ├── YES → Static final field
│   └── NO ↓
│
├── Is it shared across all instances?
│   ├── YES → Static field
│   └── NO ↓
│
├── Is it a factory method?
│   ├── YES → Static method
│   └── NO ↓
│
└── Does it need to exist before any instance?
    ├── YES → Static (block, method, or field)
    └── NO → Instance method/field
```

## 27. Interview Questions

### Basic

1. **What is the `static` keyword used for?**
   It declares class-level members (variables, methods, blocks, nested classes) that belong to the class itself, not to any specific instance. They can be accessed without creating an object.

2. **Can static methods access instance variables?**
   No. Static methods have no `this` reference. To access instance state, you must pass an instance explicitly or create one.

3. **When does a static block execute?**
   During class loading, which happens once before the class is first used. It executes in declaration order, after the parent class is initialized.

4. **Can static methods be overridden?**
   No. Static methods are hidden, not overridden. The method called depends on the reference type, not the actual object type (no polymorphism).

5. **Can a static method be overloaded?**
   Yes. Overloading is based on the parameter list. Static methods can be overloaded like any other method.

### Intermediate

6. **Why is the `main` method static?**
   The JVM needs to call `main` without instantiating the class. Making it static allows direct invocation by the JVM: `public static void main(String[] args)`.

7. **What is the difference between `static` and `final`?**
   `static` means the member belongs to the class. `final` means the value cannot be changed after initialization. They are independent: a field can be `static final`, `static` (non-final), `final` (non-static), or neither.

8. **What is the lifetime of a static variable?**
   From class loading to class unloading. In most applications, this means the entire application lifetime.

9. **Can static methods be synchronized?**
   Yes. The lock is on the `Class` object itself, so only one thread can execute any synchronized static method of that class at a time.

10. **What is the difference between a static nested class and an inner class?**
    A static nested class does not have an implicit reference to the outer class instance. An inner class does. Static nested classes are preferred when you don't need to access outer instance members.

### Advanced

11. **What happens if you try to access an instance variable from a static method?**
    A compile-time error: "non-static variable this cannot be referenced from a static context."

12. **How do static variables interact with class loading and the JVM?**
    Static variables are stored in the Metaspace. They are initialized during the `<clinit>` phase of class loading, triggered by the class loader. The JVM guarantees `<clinit>` is called at most once and is thread-safe.

13. **What is the Static Initialization Order in Java?**
    1. Parent class static blocks and static variables (in declaration order)
    2. Child class static blocks and static variables (in declaration order)
    Parent is always initialized before child.

14. **How do you implement a thread-safe singleton using static members?**
    Use the holder pattern:
    ```java
    private static class Holder {
        static final Singleton INSTANCE = new Singleton();
    }
    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }
    ```
    This is lazy, thread-safe, and has no synchronization overhead.

15. **What is a static import and when should you use it?**
    `static import` lets you access static members without class name qualification. Use it sparingly for constants (`PI`, `MAX_VALUE`) and frequently used utility methods (`sqrt`, `requireNonNull`) to improve readability.

## 28. Exercises

### Exercise 1: Counter Implementation

Implement a `Counter` class with:
- A static variable tracking total instances created
- A static method returning the total count
- An instance variable for the individual count
- An instance method to increment the individual count

```java
public class Counter {
    // Your code here
}
```

### Exercise 2: Static Factory Methods

Create a `Color` class with:
- Static factory methods: `red()`, `green()`, `blue()`
- Instance fields: `r`, `g`, `b`
- A `toString()` method

```java
public class Color {
    // Your code here
    public static Color red() { /* ... */ }
}
```

### Exercise 3: Static Initialization Order

Predict the output of this code without running it:

```java
public class Order {
    static int a = b;
    static int b = 10;

    static {
        System.out.println("a=" + a + ", b=" + b);
    }

    public static void main(String[] args) {
        System.out.println("a=" + a + ", b=" + b);
    }
}
```

## 29. Assignments

### Assignment 1: Configuration Manager

Build a thread-safe configuration manager:
- Static method to load configuration from a file
- Static method to get/set configuration values
- Support for environment variable overrides
- Thread-safe access to configuration values

### Assignment 2: Object Pool

Implement an object pool using static members:
- Static pool of reusable objects
- Static method to acquire/release objects
- Thread-safe access
- Pool statistics (created, active, idle counts)

### Assignment 3: Registry Pattern

Create a service registry:
- Static method to register services by name
- Static method to look up services
- Static method to list all registered services
- Thread-safe registration and lookup

## 30. Mini Project

### Plugin System with Static Registration

Build a plugin system using static members:

```java
public abstract class Plugin {
    private static final Map<String, Supplier<Plugin>> registry = new ConcurrentHashMap<>();

    protected static void register(String name, Supplier<Plugin> factory) {
        registry.put(name, factory);
    }

    public static Plugin create(String name) {
        Supplier<Plugin> factory = registry.get(name);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown plugin: " + name);
        }
        return factory.get();
    }

    public static List<String> listPlugins() {
        return List.copyOf(registry.keySet());
    }

    public abstract void execute();
}

// Concrete plugins auto-register via static blocks
public class LoggingPlugin extends Plugin {
    static {
        register("logging", LoggingPlugin::new);
    }

    @Override
    public void execute() {
        System.out.println("Logging plugin executed");
    }
}
```

**Features to implement:**
- Plugin discovery via ServiceLoader
- Plugin lifecycle management (start, stop, reload)
- Plugin dependency resolution
- Configuration injection into plugins

## 31. Summary

| Concept | Key Takeaway |
|---------|--------------|
| **Static Variables** | Single copy per class, stored in Metaspace |
| **Static Methods** | No `this` reference, cannot access instance members directly |
| **Static Blocks** | Execute once during class loading, in declaration order |
| **Static Import** | Avoid class name prefix, use sparingly |
| **Static Nested Class** | No reference to outer instance, preferred over inner class |
| **Thread Safety** | Static mutable state requires synchronization |
| **Lifetime** | From class loading to unloading (usually application lifetime) |
| **Performance** | Slightly faster than instance methods (no polymorphism) |
| **Testing** | Static methods are harder to mock; prefer instance methods for testability |
| **Singletons** | Use holder pattern for lazy, thread-safe initialization |

**Golden Rules:**
1. Use static for stateless utilities and constants
2. Avoid static mutable state when possible
3. Use static factory methods over constructors when beneficial
4. Keep static blocks simple and focused
5. Document thread safety of static members
6. Prefer static nested classes over inner classes when no outer reference is needed
7. Use the holder pattern for lazy singletons
8. Test static code with dependency injection

## 32. References

- [JLS - §8.3.1.1 `static` Fields](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.3.1.1)
- [JLS - §8.4.3.2 `static` Methods](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.3.2)
- [JLS - §8.7 Static Initializers](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.7)
- [JLS - §8.5.1 `static` Member Type Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.5.1)
- [Effective Java, Item 19: Design and document for inheritance or else prohibit it](https://books.google.com/books?id=BIpKEttKoLYC)
- [Effective Java, Item 2: Consider a builder when faced with many constructor parameters](https://books.google.com/books?id=BIpKEttKoLYC)
- [Effective Java, Item 34: Use interfaces to define types](https://books.google.com/books?id=BIpKEttKoLYC)
- [Java Concurrency in Practice - Chapter 3: Sharing Objects](https://jcip.net/)
