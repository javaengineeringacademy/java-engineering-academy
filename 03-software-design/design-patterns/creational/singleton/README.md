# Singleton Pattern

The Singleton pattern ensures a class has only one instance and provides a global point of access to it. It's useful for shared resources like configuration, connection pools, or caches.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Singleton](#basic-singleton)
3. [Thread-Safe Singleton](#thread-safe-singleton)
4. [Double-Checked Locking](#double-checked-locking)
5. [Enum Singleton](#enum-singleton)
6. [Bill Pugh Singleton](#bill-pugh-singleton)
7. [Dependency Injection Alternative](#dependency-injection-alternative)
8. [Best Practices](#best-practices)
9. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is a Singleton?

A Singleton restricts a class to a single instance and provides global access to it.

```
┌──────────────────────────────────────────┐
│              Singleton                    │
├──────────────────────────────────────────┤
│ - static instance: Singleton             │
│ - data: int                              │
├──────────────────────────────────────────┤
│ - Singleton()  // private constructor    │
│ + static getInstance(): Singleton        │
│ + operation(): void                      │
└──────────────────────────────────────────┘
           │
           ▼
    Only one instance exists
```

### When to Use

- Database connection pools
- Configuration managers
- Thread pools
- Caches
- Loggers

### When NOT to Use

- Stateful classes
- Classes with many dependencies
- When DI container manages lifecycle
- When testing becomes difficult

---

## Basic Singleton

### Lazy Initialization

```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Private constructor prevents instantiation
    private DatabaseConnection() {
        connection = createConnection();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private Connection createConnection() {
        // Create database connection
        return null;
    }

    public Connection getConnection() {
        return connection;
    }
}

// Usage
DatabaseConnection db = DatabaseConnection.getInstance();
Connection conn = db.getConnection();
```

### Problem with Lazy Initialization

```java
// NOT thread-safe!
// Two threads could both see instance == null
// and create two instances

Thread 1: if (instance == null)  // true
Thread 2: if (instance == null)  // true
Thread 1: instance = new DatabaseConnection()
Thread 2: instance = new DatabaseConnection()  // Second instance!
```

---

## Thread-Safe Singleton

### Synchronized Method

```java
public class ThreadSafeSingleton {
    private static ThreadSafeSingleton instance;

    private ThreadSafeSingleton() {}

    // Synchronized - only one thread can execute at a time
    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }
}

// Problem: synchronized is expensive
// Every call to getInstance() pays synchronization cost
// even after instance is created
```

### Synchronized Block

```java
public class SyncBlockSingleton {
    private static SyncBlockSingleton instance;

    private SyncBlockSingleton() {}

    public static SyncBlockSingleton getInstance() {
        synchronized (SyncBlockSingleton.class) {
            if (instance == null) {
                instance = new SyncBlockSingleton();
            }
        }
        return instance;
    }
}

// Still has synchronization overhead on every access
```

---

## Double-Checked Locking

### Optimized Thread-Safe Singleton

```java
public class DoubleCheckedSingleton {
    // volatile prevents instruction reordering
    private static volatile DoubleCheckedSingleton instance;

    private DoubleCheckedSingleton() {}

    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) {                    // First check (no sync)
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) {            // Second check (with sync)
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }
}

// Benefits:
// - Thread-safe
// - Only synchronizes on first creation
// - Subsequent calls skip synchronization
// - volatile prevents partial object publication
```

### Why volatile?

```java
// Without volatile - instruction reordering can cause issues:
// Thread 1: instance = new DoubleCheckedSingleton()
// This actually does:
//   1. Allocate memory
//   2. Initialize object
//   3. Assign reference to instance
//
// JVM might reorder to:
//   1. Allocate memory
//   2. Assign reference to instance
//   3. Initialize object
//
// Thread 2 could see non-null instance but uninitialized object!
// volatile prevents this reordering
```

---

## Enum Singleton

### Simplest Thread-Safe Singleton

```java
public enum Singleton {
    INSTANCE;

    private int value;

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public void doSomething() {
        System.out.println("Singleton operation");
    }
}

// Usage
Singleton.INSTANCE.doSomething();
int val = Singleton.INSTANCE.getValue();
```

### Benefits of Enum Singleton

```java
// 1. Thread-safe by JVM guarantee
// 2. Serialization handled automatically
// 3. Reflection attack prevented
// 4. Simple and concise

public enum Configuration {
    INSTANCE;

    private final Map<String, String> settings = new HashMap<>();

    public String get(String key) {
        return settings.get(key);
    }

    public void set(String key, String value) {
        settings.put(key, value);
    }

    public static Configuration load(String filename) {
        // Load configuration from file
        Configuration config = INSTANCE;
        // ... parse and populate settings
        return config;
    }
}

// Usage
Configuration config = Configuration.load("app.properties");
String value = config.get("database.url");
```

---

## Bill Pugh Singleton

### Using Inner Static Class

```java
public class BillPughSingleton {
    private BillPughSingleton() {}

    // Inner class not loaded until getInstance() is called
    private static class Holder {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return Holder.INSTANCE;
    }
}

// Benefits:
// - Lazy initialization (Holder class loaded on first access)
// - Thread-safe (class loading is synchronized by JVM)
// - No synchronization overhead on subsequent calls
// - No volatile needed
```

### How it Works

```java
// 1. JVM loads BillPughSingleton class
//    - Holder class NOT loaded yet
//
// 2. First call to getInstance()
//    - JVM loads Holder class (thread-safe)
//    - Holder.INSTANCE initialized
//    - Returns instance
//
// 3. Subsequent calls
//    - Holder already loaded
//    - Returns existing instance
//    - No synchronization needed
```

---

## Dependency Injection Alternative

### Why Prefer DI Over Singleton

```java
// SINGLETON - Global state, hard to test
public class UserService {
    public void process() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        // Hard to mock for testing
    }
}

// DEPENDENCY INJECTION - No global state, easy to test
public class UserService {
    private final DatabaseConnection db;

    public UserService(DatabaseConnection db) {
        this.db = db;
    }

    public void process() {
        // Easy to inject mock for testing
    }
}

// With DI container
@Service
public class UserService {
    private final DatabaseConnection db;

    @Autowired
    public UserService(DatabaseConnection db) {
        this.db = db;
    }
}
```

### Application-Scoped Singleton with DI

```java
// Spring example - singleton scope by default
@Configuration
public class AppConfig {
    @Bean
    @Scope("singleton")
    public DatabaseConnection databaseConnection() {
        return new DatabaseConnection();
    }
}

// All beans that depend on DatabaseConnection get the same instance
@Service
public class UserService {
    private final DatabaseConnection db;

    public UserService(DatabaseConnection db) {
        this.db = db;  // Same instance as other services
    }
}
```

---

## Best Practices

### Do

```java
// 1. Use enum for simple singletons
public enum AppConfig {
    INSTANCE;
    // ...
}

// 2. Use Bill Pugh for lazy initialization
private static class Holder {
    private static final Singleton INSTANCE = new Singleton();
}

// 3. Prefer DI over Singleton pattern
@Service
public class MyService {
    private final Singleton singleton;
    public MyService(Singleton singleton) { this.singleton = singleton; }
}

// 4. Make constructor private
private Singleton() {}
```

### Don't

```java
// 1. Don't use basic lazy initialization (not thread-safe)
public static Singleton getInstance() {
    if (instance == null) instance = new Singleton();
    return instance;
}

// 2. Don't use synchronized method (expensive)
public static synchronized Singleton getInstance() { ... }

// 3. Don't use Singleton for stateful classes
public class BadSingleton {
    private List<String> items = new ArrayList<>();
    // Thread-safety issues with mutable state
}

// 4. Don't overuse Singleton
// Consider alternatives like DI, static utility methods, or application-scoped beans
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Singleton** | One instance, global access |
| **Lazy Initialization** | Create on first use |
| **Thread Safety** | Multiple approaches available |
| **Double-Checked** | Optimized synchronization |
| **Enum** | Simplest, JVM-guaranteed |
| **Bill Pugh** | Inner class lazy loading |
| **DI Alternative** | Prefer over Singleton pattern |
| **Global State** | Singleton creates global state |
| **Testability** | Singletons make testing harder |
| **Use Cases** | Configuration, connection pools, caches |
