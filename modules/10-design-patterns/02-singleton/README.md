# Singleton Pattern

## 1. Introduction

The Singleton Pattern is a creational design pattern that ensures a class has only one instance and provides a global point of access to that instance. It's one of the simplest and most widely used design patterns, but also one of the most controversial due to its potential for misuse.

The Singleton pattern is particularly useful for managing shared resources like database connections, thread pools, configuration settings, and logging services where having multiple instances would cause problems.

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Implement the Singleton pattern using multiple approaches in Java 21
- Understand thread safety considerations for Singletons
- Identify when Singleton is appropriate and when it's an anti-pattern
- Handle common pitfalls like reflection, serialization, and cloning
- Compare different Singleton implementations and their trade-offs
- Recognize Singleton usage in Java standard library and frameworks

---

## 3. Prerequisites

- Understanding of static fields and methods in Java
- Knowledge of multithreading basics (synchronized keyword, volatile)
- Familiarity with object lifecycle and garbage collection
- Understanding of Java serialization

---

## 4. Why This Concept Exists

The Singleton pattern exists because some objects should never be duplicated:

- **Database connections**: Creating multiple connections wastes resources
- **Configuration**: Application config should be consistent everywhere
- **Logging**: Multiple loggers could corrupt log files
- **Thread pools**: Managing multiple pools is inefficient
- **Caches**: Duplicate caches waste memory

Without Singleton, you'd need to pass these objects everywhere or use global variables, which have their own problems.

---

## 5. Problem Statement

Consider a database connection scenario:

```java
// BAD: Multiple connections created
public class DatabaseService {
    public void query(String sql) {
        Connection conn = new Connection("jdbc:mysql://localhost/db");
        // Each call creates a new connection
        // Wasteful and potentially dangerous
    }
}

// Calling this 1000 times creates 1000 connections
DatabaseService service = new DatabaseService();
for (int i = 0; i < 1000; i++) {
    service.query("SELECT * FROM users");
}
```

**Problems:**
1. Resource waste — each connection consumes memory and network resources
2. Inconsistency — different connections might see different states
3. Connection limit — databases have max connection limits
4. Performance — connection establishment is expensive

---

## 6. Theory

### 6.1 Singleton Characteristics

1. **Private constructor** — prevents instantiation from outside
2. **Static field** — holds the single instance
3. **Static method** — provides global access point
4. **Thread safety** — ensures only one instance in concurrent environments

### 6.2 Singleton vs. Static Class

| Feature | Singleton | Static Class |
|---------|-----------|--------------|
| Inheritance | Can implement interfaces | Cannot |
| Polymorphism | Yes | No |
| Lazy loading | Yes | No (eager) |
| State | Can hold state | Static state only |
| Testing | Mockable | Hard to mock |
| Memory | One instance | One per classloader |

### 6.3 Singleton in Java Standard Library

- `java.lang.Runtime` — Runtime environment
- `java.awt.Desktop` — Desktop operations
- `java.util.logging.Logger` — Logging

---

## 7. Internal Working

### 7.1 Lazy Initialization Flow

```
1. Client calls Singleton.getInstance()
2. Check if instance == null
   ├── null → Create new instance → Store in static field → Return
   └── not null → Return existing instance
```

### 7.2 Thread Safety Challenge

```
Thread A                    Thread B
────────                    ────────
Check instance == null
                            Check instance == null
Instance is null            Instance is null
Create instance A           Create instance B
Store A                     Store B
Return A                    Return B

Result: Two instances created! (Race condition)
```

---

## 8. JVM Perspective

### 8.1 Class Loading

- Singleton instance is tied to the classloader
- Different classloaders can create different instances
- Application server classloaders can break Singleton

### 8.2 Memory Layout

```
Method Area (PermGen/Metaspace):
┌─────────────────────────┐
│ Singleton Class         │
│ - instance: Singleton   │
│ + getInstance(): Static │
└─────────────────────────┘

Heap:
┌─────────────────────────┐
│ Singleton Instance      │
│ - field1                │
│ - field2                │
└─────────────────────────┘
```

### 8.3 Garbage Collection

- Singleton instance is reachable via static field
- Never garbage collected until class is unloaded
- Class unloading typically happens when classloader is garbage collected

---

## 9. Memory Representation

### 9.1 Singleton Memory Model

```
┌─────────────────────────────────────┐
│            ClassLoader              │
│                 ↓                   │
│    ┌─────────────────────────┐     │
│    │   Singleton Class       │     │
│    │   static instance ──────│─────│──→ Heap
│    └─────────────────────────┘     │
│                                     │
│    ┌─────────────────────────┐     │
│    │    Singleton Instance   │     │
│    │    (in Heap)            │     │
│    │    - data fields        │     │
│    └─────────────────────────┘     │
└─────────────────────────────────────┘
```

---

## 10. Syntax

### 10.1 Basic Singleton Structure

```java
public class Singleton {
    // 1. Private static instance
    private static Singleton instance;

    // 2. Private constructor
    private Singleton() {
        // Initialize
    }

    // 3. Public static access method
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

### 10.2 Thread-Safe Singleton

```java
public class ThreadSafeSingleton {
    private static volatile ThreadSafeSingleton instance;

    private ThreadSafeSingleton() {}

    public static ThreadSafeSingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null) {
                    instance = new ThreadSafeSingleton();
                }
            }
        }
        return instance;
    }
}
```

---

## 11. Easy Example

### Basic Singleton Implementation

```java
public class AppConfig {
    private static AppConfig instance;
    private String appName;
    private String version;

    private AppConfig() {
        this.appName = "MyApp";
        this.version = "1.0.0";
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getAppName() {
        return appName;
    }

    public String getVersion() {
        return version;
    }
}

// Usage
AppConfig config = AppConfig.getInstance();
System.out.println(config.getAppName()); // "MyApp"
```

---

## 12. Medium Example

### Thread-Safe Singleton with Double-Checked Locking

```java
public class DatabaseManager {
    private static volatile DatabaseManager instance;
    private Connection connection;
    private String url;

    private DatabaseManager() {
        this.url = "jdbc:mysql://localhost:3306/mydb";
        initializeConnection();
    }

    private void initializeConnection() {
        try {
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void executeQuery(String sql) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Query failed", e);
        }
    }
}
```

---

## 13. Hard Example

### Singleton with Enumeration (Bill Pugh Solution)

```java
public enum Configuration {
    INSTANCE;

    private final Map<String, String> properties;
    private final List<String> logHistory;

    Configuration() {
        properties = new HashMap<>();
        logHistory = new ArrayList<>();
        loadDefaults();
    }

    private void loadDefaults() {
        properties.put("app.name", "EnterpriseApp");
        properties.put("app.version", "2.0.0");
        properties.put("db.pool.size", "10");
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
        logHistory.add("Set " + key + " = " + value);
    }

    public List<String> getLogHistory() {
        return Collections.unmodifiableList(logHistory);
    }
}

// Usage
Configuration config = Configuration.INSTANCE;
System.out.println(config.getProperty("app.name"));
config.setProperty("new.key", "new.value");
```

### Handling Reflection Attack

```java
public class SecureSingleton implements Serializable {
    private static volatile SecureSingleton instance;
    private static boolean instanceCreated = false;

    private SecureSingleton() {
        if (instanceCreated) {
            throw new IllegalStateException("Singleton already instantiated!");
        }
        instanceCreated = true;
    }

    public static SecureSingleton getInstance() {
        if (instance == null) {
            synchronized (SecureSingleton.class) {
                if (instance == null) {
                    instance = new SecureSingleton();
                }
            }
        }
        return instance;
    }

    // Prevent deserialization from creating new instance
    private Object readResolve() {
        return getInstance();
    }

    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }
}
```

---

## 14. Enterprise Example

### Thread-Safe Singleton with Connection Pool

```java
public class ConnectionPool implements Closeable {
    private static volatile ConnectionPool instance;
    private final BlockingQueue<Connection> pool;
    private final int poolSize;
    private final String url;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    private ConnectionPool(int poolSize, String url) {
        this.poolSize = poolSize;
        this.url = url;
        this.pool = new LinkedBlockingQueue<>(poolSize);
        initializePool();
    }

    private void initializePool() {
        for (int i = 0; i < poolSize; i++) {
            pool.offer(createConnection());
        }
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create connection", e);
        }
    }

    public static ConnectionPool getInstance(int poolSize, String url) {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool(poolSize, url);
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        if (closed.get()) {
            throw new IllegalStateException("Connection pool is closed");
        }
        try {
            return pool.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while getting connection", e);
        }
    }

    public void releaseConnection(Connection conn) {
        if (conn == null) return;
        try {
            if (closed.get()) {
                conn.close();
            } else {
                pool.offer(conn);
            }
        } catch (SQLException e) {
            // Log and ignore
        }
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            Connection conn;
            while ((conn = pool.poll()) != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // Log and ignore
                }
            }
        }
    }
}
```

---

## 15. Performance

### 15.1 Performance Metrics

| Implementation | Thread Safe | Lazy | Performance |
|---------------|-------------|------|-------------|
| Basic (no sync) | No | Yes | Fastest |
| Synchronized | Yes | Yes | Slow (all calls) |
| Double-checked | Yes | Yes | Fast (after init) |
| Bill Pugh | Yes | Yes | Fast |
| Enum | Yes | No | Fastest |

### 15.2 Benchmark Comparison

```java
// Simple benchmark
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SingletonBenchmark {

    @Benchmark
    public Singleton basicSingleton() {
        return BasicSingleton.getInstance();
    }

    @Benchmark
    public Singleton dclSingleton() {
        return DCLSingleton.getInstance();
    }

    @Benchmark
    public Singleton billPughSingleton() {
        return BillPughSingleton.getInstance();
    }
}
```

---

## 16. Best Practices

1. **Use enum for simple Singletons**: Thread-safe, reflection-proof, serializable
2. **Implement Serializable**: Add `readResolve()` method
3. **Guard against reflection**: Check in private constructor
4. **Consider dependency injection**: Singletons can hide dependencies
5. **Document thread safety**: Clearly state threading guarantees
6. **Avoid mutable state**: Make Singleton immutable if possible
7. **Consider lazy vs. eager**: Choose based on initialization cost
8. **Test thoroughly**: Singleton testing requires special care

---

## 17. Common Mistakes

1. **Forgetting synchronization**: Race conditions in multi-threaded environments
2. **Not handling serialization**: Deserialization creates new instances
3. **Ignoring reflection attacks**: Reflection can invoke private constructors
4. **Overusing Singleton**: Using it when not necessary
5. **Not implementing Serializable**: Can cause issues with distributed systems
6. **Ignoring classloader issues**: Application servers may create multiple instances

---

## 18. Pitfalls

- **Global state**: Singletons are essentially global variables
- **Hidden dependencies**: Makes code harder to test
- **Tight coupling**: Code depends on concrete Singleton class
- **Testing difficulty**: Hard to mock in unit tests
- **Memory leak**: Instance never garbage collected
- **Scalability issues**: Can become bottleneck in distributed systems
- **Classloader issues**: Different classloaders create different instances

---

## 19. Debugging Tips

1. **Use logging**: Add logs to track Singleton creation
2. **Check thread dumps**: Use `jstack` to detect contention
3. **Monitor memory**: Use VisualVM to track instance count
4. **Test serialization**: Verify `readResolve()` works
5. **Test reflection**: Ensure private constructor is protected
6. **Use dependency injection**: Consider alternatives to Singleton

---

## 20. Comparison Table

| Approach | Thread Safe | Lazy | Serializable | Reflection Safe | Complexity |
|----------|-------------|------|--------------|-----------------|------------|
| Basic | No | Yes | No | No | Low |
| Synchronized | Yes | Yes | No | No | Low |
| DCL | Yes | Yes | No | No | Medium |
| Bill Pugh | Yes | Yes | No | No | Medium |
| Enum | Yes | No | Yes | Yes | Low |
| Holder | Yes | Yes | No | No | Low |

---

## 21. Decision Tree

```
Need a Singleton?
├── Simple, no thread safety needed → Basic
├── Thread safety needed?
│   ├── Performance critical → DCL or Bill Pugh
│   └── Simple implementation → Synchronized
├── Need serialization? → Implement readResolve()
├── Need reflection protection? → Check in constructor
└── Consider alternatives?
    ├── Dependency injection → Use Spring @Singleton
    └── Testing concerns → Use interface + implementation
```

---

## 22. Interview Questions

### Q1: Implement a thread-safe Singleton in Java.
**Answer**: Use double-checked locking with volatile, or Bill Pugh solution with static inner class.

### Q2: How can you break Singleton pattern?
**Answer**: Reflection, serialization, multiple classloaders, and cloning (if not prevented).

### Q3: Is Singleton an anti-pattern?
**Answer**: It can be if overused. It introduces global state and hides dependencies. Use dependency injection when possible.

### Q4: What are Singleton alternatives?
**Answer**: Dependency injection (Spring @Singleton), Monostate pattern, or simply passing dependencies.

### Q5: Why use volatile with double-checked locking?
**Answer**: Prevents instruction reordering that could expose partially constructed object to other threads.

---

## 23. Exercises

### Exercise 1: Basic Singleton
Implement a Singleton for a logging service.

### Exercise 2: Thread-Safe Singleton
Implement a thread-safe Singleton using double-checked locking.

### Exercise 3: Serializable Singleton
Implement a Singleton that handles serialization correctly.

---

## 24. Assignments

1. **Assignment 1**: Create a Singleton configuration manager with lazy initialization
2. **Assignment 2**: Implement a Singleton that resists reflection attacks
3. **Assignment 3**: Create a Singleton with dependency injection support

---

## 25. Mini Project

### Task Tracker Singleton
Create a `TaskManager` Singleton that:
- Manages a list of tasks
- Is thread-safe
- Handles serialization
- Resists reflection
- Provides thread-safe operations

---

## 26. Summary

- Singleton ensures only one instance exists
- Multiple implementations with different trade-offs
- Thread safety is crucial in multi-threaded environments
- Handle serialization, reflection, and cloning
- Consider alternatives like dependency injection
- Use appropriately — not for every shared resource

---

## 27. References

1. Gamma, E., et al. (1994). *Design Patterns*, Chapter 3
2. Bloch, J. (2018). *Effective Java*, Item 3
3. Goetz, B. (2006). *Java Concurrency in Practice*
4. Martin, R. C. (2017). *Clean Architecture*
5. Refactoring Guru: https://refactoring.guru/design-patterns/singleton
6. Java Design Patterns: https://java-design-patterns.com/patterns/singleton/
