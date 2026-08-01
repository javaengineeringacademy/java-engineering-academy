# Static Block

## 1. Introduction

A static block (also called a static initializer) is a block of code executed exactly once when a class is loaded into memory by the JVM. It provides a mechanism for complex one-time initialization of static state that cannot be expressed with simple field initializers.

## 2. Learning Objectives

- Understand when and how static blocks execute
- Differentiate static blocks from instance initializers and constructors
- Apply multiple static blocks and understand execution order
- Handle exceptions in static initialization
- Diagnose class loading issues using JVM knowledge

## 3. Prerequisites

- Basic Java syntax and class structure
- Understanding of `static` fields and methods
- Familiarity with exception handling

## 4. Why This Concept Exists

Simple static field initialization (`private static final int VALUE = 42;`) works for constants, but many initialization scenarios require try-catch blocks, loops, conditional logic, or multi-step processing. Static blocks provide a clean syntax for this without the limitations of field initializers.

## 5. Problem Statement

Consider initializing a static map from a file:

```java
// This does NOT work with a field initializer
private static final Map<String, String> CONFIG = loadConfig(); // throws checked exception
```

A field initializer cannot contain try-catch for checked exceptions. A static block solves this:

```java
private static final Map<String, String> CONFIG;

static {
    try {
        CONFIG = loadConfig(); // Can use try-catch here
    } catch (IOException e) {
        throw new ExceptionInInitializerError(e);
    }
}
```

## 6. Theory

When the JVM loads a class, it performs initialization:
1. **Linking**: Verify bytecode, prepare static fields, resolve references
2. **Initialization**: Execute static blocks and static field initializers **in declaration order**
3. The initialization is **atomic** — the JVM holds an intrinsic lock on the `Class` object
4. If initialization throws an uncaught exception, the class enters an **erroneous state** and all subsequent access throws `ExceptionInInitializerError`
5. Static initialization is **guaranteed to be thread-safe** by the JVM specification

## 7. Internal Working

```
Class Loading Process:
┌──────────────────┐
│  Loading          │  Read .class bytecode into memory
├──────────────────┤
│  Linking          │
│  ├─ Verification   │  Check bytecode validity
│  ├─ Preparation    │  Allocate memory for static fields
│  └─ Resolution     │  Resolve symbolic references
├──────────────────┤
│  Initialization   │  Execute static blocks + field initializers
│  (Thread-safe)    │  in declaration order, top-to-bottom
└──────────────────┘
```

During preparation, all static fields are set to their default values (`0`, `null`, `false`). During initialization, the actual values are assigned by executing field initializers and static blocks in source order.

## 8. JVM Perspective

The JVM specification (§12.4.2) defines the initialization procedure:

1. Synchronize on the initialization lock (intrinsic lock on the `Class` object)
2. If already initialized, return
3. If initialization is in progress by the current thread, permit (reentrant)
4. If initialization is in progress by another thread, **block** until initialization completes
5. Mark the class as "in progress"
6. Release the lock and execute:
   - Static field initializers and static blocks **in textual order**
7. Acquire the lock and mark as "initialized"
8. Notify all waiting threads

Static blocks are compiled to the `<clinit>` method (class initializer), which the JVM calls during class initialization.

## 9. Memory Representation

```
Metaspace (JVM):
┌─────────────────────────────────────────┐
│  Class Metadata for MyClass             │
│  ├─ Field: VALUE (static, final)        │
│  │    → 42 (initialized by <clinit>)    │
│  ├─ Field: NAMES (static, final)        │
│  │    → [ArrayList] (from static block) │
│  ├─ Method: <clinit>()                  │
│  │    → Contains static block code      │
│  └─ Initialization state: INITIALIZED   │
└─────────────────────────────────────────┘

Heap:
┌─────────────────────────────────────────┐
│  MyClass instance                       │
│  ├─ instanceField: ...                  │
│  └─ reference → class metadata          │
└─────────────────────────────────────────┘
```

## 10. Architecture Diagram

```
┌─────────────────────────────────────────────┐
│              Class Loading                   │
├─────────────────────────────────────────────┤
│                                             │
│  Static Field     Static Block              │
│  Initializers     (<clinit>)                │
│       │               │                    │
│       └───────┬───────┘                    │
│               │                            │
│         Interleaved in                     │
│         declaration order                  │
│               │                            │
│               ▼                            │
│         Static State                       │
│         (shared across                     │
│          all instances)                    │
└─────────────────────────────────────────────┘
```

## 11. Flow Diagram

```
First use of class detected
    │
    ▼
Is class already initialized?
├── Yes → Return
└── No → Is initialization in progress?
    ├── Yes (current thread) → Continue (reentrant)
    └── Yes (other thread) → Block until complete
    │
    ▼
Acquire initialization lock
    │
    ▼
Execute static field initializers and static blocks
in top-to-bottom declaration order
    │
    ├─► static int X = 5;
    ├─► static { loadConfig(); }
    ├─► static final List<String> NAMES = initNames();
    │
    ▼
If exception thrown → Class enters erroneous state
    │
    ▼
Mark class as INITIALIZED
    │
    ▼
Release lock, notify waiting threads
```

## 12. Syntax

### Basic Static Block
```java
public class Config {
    private static final String DATABASE_URL;

    static {
        DATABASE_URL = "jdbc:postgresql://localhost:5432/mydb";
    }
}
```

### Multiple Static Blocks
```java
public class MultiInit {
    private static final Map<String, String> MAP;

    static {
        MAP = new HashMap<>();
        MAP.put("key1", "value1");
    }

    static {
        MAP.replaceAll((k, v) -> v.toUpperCase());
    }
}
```

### Static Block with Exception Handling
```java
public class Loader {
    private static final Properties PROPS;

    static {
        try {
            PROPS = new Properties();
            try (InputStream in = Loader.class.getResourceAsStream("/app.properties")) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
```

### Static Block vs Field Initialization
```java
public class Example {
    // Equivalent: static { VALUE = 42; }
    private static final int VALUE = 42;

    private static final List<String> NAMES;

    // Use static block when logic is needed
    static {
        NAMES = new ArrayList<>();
        NAMES.add("Alice");
        NAMES.add("Bob");
    }
}
```

## 13. Easy Example

```java
public class Greeting {
    private static final String MESSAGE;

    static {
        MESSAGE = "Hello, World!";
        System.out.println("Static block executed");
    }

    public static void print() {
        System.out.println(MESSAGE);
    }
}

public class Main {
    public static void main(String[] args) {
        Greeting.print();
        // Output:
        // Static block executed
        // Hello, World!
    }
}
```

## 14. Medium Example

```java
import java.util.Map;
import java.util.HashMap;

public class HttpStatus {
    private static final Map<Integer, String> DESCRIPTIONS;

    static {
        DESCRIPTIONS = new HashMap<>();
        DESCRIPTIONS.put(200, "OK");
        DESCRIPTIONS.put(201, "Created");
        DESCRIPTIONS.put(204, "No Content");
        DESCRIPTIONS.put(400, "Bad Request");
        DESCRIPTIONS.put(401, "Unauthorized");
        DESCRIPTIONS.put(403, "Forbidden");
        DESCRIPTIONS.put(404, "Not Found");
        DESCRIPTIONS.put(500, "Internal Server Error");
    }

    static {
        DESCRIPTIONS.replaceAll((k, v) -> v.toUpperCase());
    }

    public static String describe(int code) {
        return DESCRIPTIONS.getOrDefault(code, "UNKNOWN");
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println(HttpStatus.describe(200)); // OK
        System.out.println(HttpStatus.describe(404)); // NOT FOUND
    }
}
```

## 15. Hard Example

```java
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicReference;

public class NativeLibraryLoader {
    private static final AtomicReference<LibraryState> STATE =
        new AtomicReference<>(LibraryState.NOT_LOADED);

    private static final String LIBRARY_NAME = "native-lib";

    enum LibraryState {
        NOT_LOADED, LOADING, LOADED, FAILED
    }

    static {
        if (!STATE.compareAndSet(LibraryState.NOT_LOADED, LibraryState.LOADING)) {
            throw new IllegalStateException("Library already loading or loaded");
        }
        try {
            Path tempDir = Files.createTempDirectory("nativelibs");
            Path libraryFile = tempDir.resolve(mapLibraryName(LIBRARY_NAME));

            try (InputStream in = NativeLibraryLoader.class
                    .getResourceAsStream("/native/" + getPlatformLibrary())) {
                if (in == null) {
                    throw new FileNotFoundException("Native library not found in resources");
                }
                Files.copy(in, libraryFile, StandardCopyOption.REPLACE_EXISTING);
            }

            System.load(libraryFile.toAbsolutePath().toString());
            STATE.set(LibraryState.LOADED);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                Files.deleteIfExists(libraryFile);
                Files.deleteIfExists(tempDir);
            }));
        } catch (Exception e) {
            STATE.set(LibraryState.FAILED);
            throw new ExceptionInInitializerError(e);
        }
    }

    private static String getPlatformLibrary() {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        if (os.contains("mac")) return "lib" + LIBRARY_NAME + ".dylib";
        if (os.contains("linux")) return "lib" + LIBRARY_NAME + ".so";
        if (os.contains("win")) return LIBRARY_NAME + ".dll";
        throw new UnsupportedOperationException("Unsupported OS: " + os);
    }

    private static String mapLibraryName(String name) {
        return System.mapLibraryName(name);
    }

    public static boolean isLoaded() {
        return STATE.get() == LibraryState.LOADED;
    }
}
```

## 16. Enterprise Example

```java
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ApplicationConfig {
    private static final Logger LOG = Logger.getLogger(ApplicationConfig.class.getName());
    private static final ExecutorService EXECUTOR;
    private static final ScheduledExecutorService SCHEDULER;
    private static final Config CONFIG;

    static {
        LOG.info("Initializing application configuration...");
        try {
            CONFIG = loadConfig();
            EXECUTOR = Executors.newFixedThreadPool(CONFIG.threadPoolSize());
            SCHEDULER = Executors.newScheduledThreadPool(2);
            LOG.info("Configuration loaded successfully");
        } catch (Exception e) {
            LOG.severe("Failed to load configuration: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    private static Config loadConfig() throws Exception {
        // Load from database, file, or environment
        String env = System.getenv().getOrDefault("APP_ENV", "development");
        return switch (env) {
            case "production" -> loadProductionConfig();
            case "staging" -> loadStagingConfig();
            default -> loadDevelopmentConfig();
        };
    }

    private static Config loadProductionConfig() { return new Config(20, "prod-db"); }
    private static Config loadStagingConfig() { return new Config(10, "staging-db"); }
    private static Config loadDevelopmentConfig() { return new Config(5, "dev-db"); }

    public static ExecutorService executor() { return EXECUTOR; }
    public static ScheduledExecutorService scheduler() { return SCHEDULER; }
    public static Config config() { return CONFIG; }
}

record Config(int threadPoolSize, String databaseName) {}
```

## 17. Performance

Static blocks execute once per class load. The cost is:
- **CPU**: One-time execution, negligible in most applications
- **Memory**: Objects created in static blocks persist for the application lifetime
- **Startup time**: Heavy static initialization (I/O, network) delays class loading

Optimization strategies:
- Lazy initialization for expensive resources
- Holder class pattern for lazy static state
- Avoid I/O or network calls in static blocks

## 18. Time Complexity

| Operation | Complexity |
|-----------|------------|
| Static block execution | O(n) where n = block logic |
| Field initialization | O(1) for primitives, O(n) for collections |
| Class loading trigger | O(1) per active use |
| Initialization guard check | O(1) with lock |

## 19. Space Complexity

Static state persists for the application lifetime:
- Objects in static fields: O(n) where n = object graph size
- No per-instance overhead
- Class metadata in Metaspace: O(m) where m = class complexity

## 20. Thread Safety

The JVM guarantees thread-safe class initialization:
- Only one thread executes `<clinit>` at a time
- Other threads block until initialization completes
- Double-checked locking is unnecessary for static initialization

```java
// Thread-safe without synchronization
public class Singleton {
    private static final Singleton INSTANCE;

    static {
        INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return INSTANCE;
    }
}
```

However, if the static block starts background threads or initializes non-thread-safe objects, additional synchronization may be needed.

## 21. Best Practices

1. **Prefer static field initializers for simple cases** — Use `private static final Type FIELD = value;` when no logic is needed.
2. **Reserve static blocks for complex logic** — Use them for try-catch, loops, or multi-step initialization.
3. **Wrap checked exceptions** — Always wrap in `ExceptionInInitializerError`; never let exceptions propagate raw.
4. **Avoid side effects** — Static blocks should only initialize static state; do not perform I/O or network calls unless unavoidable.
5. **Keep them short** — Long static blocks reduce readability; extract helper methods.
6. **Be aware of initialization order** — Static blocks run in declaration order; ensure dependencies are initialized before use.
7. **Avoid circular static dependencies** — If class A's static block references B, and B's static block references A, you get `ClassCircularityError` or deadlocks.
8. **Document the purpose** — Add comments explaining *why* a static block exists, not *what* it does.
9. **Consider using a factory method** — For complex initialization, a `static init()` method with explicit calling can improve clarity.
10. **Test static initialization** — Verify that the class loads correctly and that failure modes produce meaningful errors.

## 22. Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Throwing checked exception | Static blocks cannot throw checked exceptions | Wrap in `ExceptionInInitializerError` |
| Accessing instance members | No `this` reference exists in static context | Use static members only |
| Ignoring execution order | Multiple blocks run top-to-bottom; dependencies may not be initialized | Order blocks by dependency |
| Circular initialization | Class A loads B which loads A → deadlock or `ClassCircularityError` | Break the cycle with lazy initialization |
| Heavy I/O in static block | Delays class loading; hard to test; failure is permanent | Use lazy initialization or dependency injection |
| Catching and swallowing exceptions | Class enters erroneous state but failure is silent | Always rethrow wrapped in `ExceptionInInitializerError` |
| Relying on subclass loading order | Only parent classes guarantee load order | Do not assume child static blocks run before parent constructors |

## 23. Pitfalls

- **Static blocks cannot be called directly** — They execute automatically on class loading.
- **Exception handling is strict** — Checked exceptions must be wrapped in `ExceptionInInitializerError`. Unchecked exceptions propagate directly.
- **Initialization is permanent** — Once a class fails to initialize, it cannot be retried. The class remains in an erroneous state.
- **Order dependencies** — Static field initializers and blocks execute in declaration order. Reordering code can change behavior.
- **Testing difficulty** — Static blocks execute on first use, making unit testing harder. Consider using dependency injection for testability.

## 24. Debugging Tips

- Add `System.out.println` or logging at the start and end of static blocks to trace initialization order.
- Use `jshell` to test class loading: `jshell> Class.forName("com.example.MyClass")`
- Inspect class loading with JVM flags: `-verbose:class`
- Use `Thread.currentThread().getStackTrace()` inside static blocks to see the calling context.
- Monitor Metaspace usage with `jcmd <pid> VM.metaspace` to detect class loading issues.

## 25. Comparison Table

| Initializer | When It Runs | Scope | Use Case |
|-------------|--------------|-------|----------|
| Static block | Class loading (once) | Static fields | Complex static init |
| Static field init | Class loading (once) | Static fields | Simple static init |
| Instance initializer | Per instance creation | Instance fields | Shared pre-constructor logic |
| Constructor | Per instance creation | Instance fields | Full instance initialization |
| `main()` method | JVM entry point | N/A | Application entry point |

## 26. Decision Tree

```
Need to initialize static state?
├── Simple constant → Static field initializer
├── Requires try-catch → Static block
├── Requires loops or conditionals → Static block
├── Multiple steps → Multiple static blocks or single block with steps
└── Lazy initialization needed → Holder class or Supplier pattern

Static block throws exception?
├── Checked exception → Wrap in ExceptionInInitializerError
├── Unchecked exception → Let it propagate (class enters erroneous state)
└── Error → Class initialization fails permanently
```

## 27. Interview Questions

1. **When does a static block execute?**
   On first active use of the class: `new`, static method call, static field access, `Class.forName()`, or as the JVM entry point (`main()`).

2. **Can a static block throw a checked exception?**
   No. The JVM specification requires checked exceptions to be wrapped in `ExceptionInInitializerError`.

3. **Static block vs static field initialization — which runs first?**
   They run interleaved in declaration order. If a field is declared before a block, its initializer runs first.

4. **Can a static block access instance variables?**
   No. There is no `this` reference in a static context. Only static members are accessible.

5. **What happens if a static block throws an exception?**
   The class becomes unusable. Any subsequent attempt to use it throws `ExceptionInInitializerError`.

6. **How do you prevent static initialization order problems?**
   Use lazy initialization (e.g., `Supplier<T>`, holder class pattern), avoid circular dependencies, and keep static blocks minimal.

7. **What is the difference between a static block and a static factory method?**
   A static block runs automatically on class load; a factory method runs on demand. Use blocks for mandatory one-time setup; use factories for controlled object creation.

8. **Can a static block be synchronized?**
   No, but the JVM synchronizes the entire `<clinit>` method. You can synchronize on an explicit lock object within the block if needed.

9. **What is the `<clinit>` method?**
   The compiler-generated class initializer method that contains all static blocks and static field initializers. The JVM calls it during class initialization.

10. **How do you test a class with a static block?**
    Use `Class.forName()` with `initialize=false` to load without initializing, or use dependency injection to replace static dependencies.

## 28. Exercises

1. **Initialization order**: Create a class with multiple static blocks and static field initializers. Print statements in each to observe execution order.
2. **Exception handling**: Write a static block that loads a configuration file and wraps `IOException` in `ExceptionInInitializerError`.
3. **Thread safety test**: Create two threads that both trigger class loading. Verify that initialization runs exactly once.
4. **Holder class pattern**: Implement lazy initialization of a `Config` object using the static holder class idiom.
5. **Circular dependency**: Create classes A and B with circular static dependencies. Observe the `ClassCircularityError` or deadlock.

## 29. Assignments

1. **Database connection pool**: Implement a `ConnectionPool` class with a static block that initializes the pool from configuration. Handle connection failures gracefully.
2. **Platform detection**: Write a `Platform` class with a static block that detects the OS and architecture, initializing appropriate native libraries.
3. **Feature flags**: Implement a `FeatureFlags` class that loads feature toggles from a file in a static block, with fallback defaults.

## 30. Mini Project

**Application Bootstrap**

```java
public class ApplicationBootstrap {
    private static final Logger LOG = Logger.getLogger(ApplicationBootstrap.class.getName());
    private static final Config CONFIG;
    private static final ConnectionPool POOL;
    private static final CacheManager CACHE;

    static {
        LOG.info("Starting application bootstrap...");
        try {
            CONFIG = ConfigLoader.load("application.properties");
            POOL = ConnectionPool.create(CONFIG.databaseUrl(), CONFIG.poolSize());
            CACHE = CacheManager.create(CONFIG.cacheTtl());
            LOG.info("Bootstrap completed successfully");
        } catch (Exception e) {
            LOG.severe("Bootstrap failed: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void shutdown() {
        POOL.close();
        CACHE.shutdown();
        LOG.info("Application shutdown complete");
    }
}

public class Main {
    public static void main(String[] args) {
        // Static block executes here (first active use)
        var app = ApplicationBootstrap.getInstance();
        Runtime.getRuntime().addShutdownHook(new Thread(ApplicationBootstrap::shutdown));
    }
}
```

## 31. Summary

Static blocks provide a powerful mechanism for complex one-time initialization of static state. They execute in declaration order, are thread-safe by JVM specification, and integrate cleanly with the class loading process. Use them when field initializers are insufficient, but prefer lazy initialization for expensive resources to avoid startup delays and testing difficulties.

## 32. References

- [JLS §8.7 — Static Initializers](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.7)
- [JLS §12.4.1 — When a Class is Initialized](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.4.1)
- [JLS §12.4.2 — Detailed Initialization Procedure](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.4.2)
- [JEP 395 — Records (for comparison)](https://openjdk.org/jeps/395)
- [Baeldung — Java Static Block](https://www.baeldung.com/java-static-blocks)
- [Java SE 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
