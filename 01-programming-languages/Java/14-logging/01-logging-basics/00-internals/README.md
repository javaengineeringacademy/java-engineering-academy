# Internals: How Logging Works Under the Hood

## SLF4J Binding Mechanism

SLF4J uses a **static binding** mechanism at runtime:

1. Application code calls `LoggerFactory.getLogger()`
2. SLF4J searches the classpath for binding providers in order:
   - `SLF4JServiceProvider` (ServiceLoader pattern in SLF4J 2.x)
   - Legacy `StaticLoggerBinder` (SLF4J 1.x)
3. First found binding is used; warnings printed for multiple bindings

```
Classpath scan:
  logback-classic.jar → LogbackServiceProvider (FOUND → USE THIS)
  log4j-slf4j-impl.jar → (ignored)
```

## Logger Hierarchy

SLF4J/Logback use a **hierarchical logger** system:

```
root
├── com
│   ├── myapp
│   │   ├── service
│   │   │   ├── UserService
│   │   │   └── OrderService
│   │   └── dao
│   │       └── UserRepository
│   └── framework
└── org
    └── springframework
```

**Inheritance rules:**
- A logger inherits settings from its parent if not explicitly configured
- `com.myapp.service` inherits from `com.myapp` which inherits from `com` which inherits from `root`
- Explicit level settings on a child override the parent

## `isXxxEnabled()` Pattern

```java
// SLF4J checks the effective level of the logger
// This is O(1) - just comparing two integers
public boolean isDebugEnabled() {
    return logger.isDebugEnabled();  // checks against configured threshold
}
```

**How it works internally:**
- Each logger has an `effectiveLevel` (inherited from parent or set directly)
- When you call `logger.debug(...)`, SLF4J first checks `level.isGreaterOrEqual(effectiveLevel)`
- If the check fails, the message is never constructed (including parameterized)

## Logback Initialization

1. `LoggerFactory.getLogger()` triggers Logback initialization (first call)
2. Logback searches classpath for `logback.xml` or `logback-test.xml`
3. If not found, uses `Logback-Spring.xml` (Spring Boot)
4. Falls back to default configuration (INFO to console)
5. After initialization, `StatusManager` collects configuration warnings

## Thread Safety

```java
// Loggers are inherently thread-safe
// Multiple threads can call the same logger concurrently
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

// MDC is thread-local - each thread has its own context
MDC.put("requestId", UUID.randomUUID().toString());
// Only visible in the current thread
```

## Message Construction

```java
// SLF4J 2.x uses MessageFormatter internally
logger.debug("User {} logged in at {}", username, timestamp);

// Internally:
// 1. Check if DEBUG enabled
// 2. Create FormattingTuple: MessageFormatter.arrayFormat("User {} logged in at {}", args)
// 3. Return MessageFormat with interpolated values
// 4. Pass to Appender

// Key insight: parameters are NOT interpolated unless the level is enabled
```

## Performance at the Implementation Level

**Logback Appenders:**
- `ConsoleAppender` - Synchronized writes to System.out/System.err
- `FileAppender` - Buffered I/O with optional flushing strategies
- `RollingFileAppender` - Triggers file rotation based on policies
- `AsyncAppender` - Wraps another appender, uses a blocking queue

**Async internals:**
```
Logger thread → puts event in ArrayBlockingQueue → AsyncAppender worker thread → writes to underlying appender
```

This decouples the logging call from the actual I/O.
