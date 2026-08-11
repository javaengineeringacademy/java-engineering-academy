# 07 - Try With Resources

## Scope

This topic covers Java's try-with-resources (TWR) statement, introduced in Java 7 (JSR 334). You will learn what TWR is, why it exists, how it differs from traditional `finally` cleanup, and the underlying bytecode mechanics that make it work. The material progresses from basic usage to advanced patterns including exception suppression, effective final variables (Java 9+), and production-grade resource management.

## Why It Exists

Managing external resources—files, database connections, network sockets—is one of the most error-prone tasks in Java. Before Java 7, developers had to write boilerplate `try-finally` blocks to guarantee cleanup:

```java
BufferedReader reader = null;
try {
    reader = new BufferedReader(new FileReader("data.txt"));
    String line = reader.readLine();
} finally {
    if (reader != null) {
        reader.close(); // can itself throw IOException
    }
}
```

This pattern has several problems:

1. **Verbosity** — The cleanup logic dwarfs the business logic.
2. **Close exceptions** — If `close()` throws, the original exception is lost.
3. **Null checks** — Every resource must be null-guarded.
4. **Ordering** — Resources opened last must be closed first; forgetting this causes subtle bugs.
5. **Forgetfulness** — Omitting the `finally` block entirely is a common bug that leads to resource leaks.

Try-with-resources addresses all of these issues in a single language construct.

## Design Rationale

The Java Language Specification defines TWR as syntactic sugar that the compiler lowers into `try-finally` blocks. The design goals were:

- **Zero-cost abstraction** — The compiled code should be equivalent to hand-written `finally` blocks.
- **Exception transparency** — If both the body and `close()` throw, the close exception is *suppressed* rather than swallowed.
- **Declarative resource management** — Resources are declared inline, making their lifetime explicit.
- **Backward compatibility** — Existing `Closeable` and `AutoCloseable` types work without modification.

## What Is Try-with-Resources

A try-with-resources statement declares one or more resources inside the `try` parenthesis. Each resource is automatically closed at the end of the statement, regardless of whether the body completes normally or throws an exception.

```java
try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
    String line = reader.readLine();
    System.out.println(line);
}
// reader.close() is called automatically here
```

### The AutoCloseable Interface

Any class that implements `AutoCloseable` can be used as a TWR resource:

```java
public interface AutoCloseable {
    void close() throws Exception;
}
```

`java.io.Closeable` extends `AutoCloseable` and narrows the exception to `IOException`:

```java
public interface Closeable extends AutoCloseable {
    void close() throws IOException;
}
```

**Key difference:** `AutoCloseable.close()` may throw *any* checked exception; `Closeable.close()` throws only `IOException`. If you implement a resource class, prefer `AutoCloseable` unless you specifically need `IOException` narrowing.

## TWR Execution Flow

```
┌─────────────────────────────────┐
│  try (Resource r = new ...)     │
│    │                            │
│    ▼                            │
│  ┌─────────────────────────┐    │
│  │  Execute try body       │    │
│  └──────────┬──────────────┘    │
│             │                   │
│        ┌────┴────┐              │
│        │Success? │              │
│        └────┬────┘              │
│       Yes   │   No              │
│     ┌───────┘   └──────┐       │
│     ▼                   ▼       │
│  ┌──────────┐  ┌──────────────┐ │
│  │ r.close()│  │ Catch block  │ │
│  │ (auto)   │  │ (if exists)  │ │
│  └────┬─────┘  └──────┬───────┘ │
│       │               │         │
│       ▼               ▼         │
│  ┌──────────────────────────┐   │
│  │  r.close() (auto)       │   │
│  │  If close throws:       │   │
│  │  suppress on primary    │   │
│  └──────────────────────────┘   │
└─────────────────────────────────┘
```

## Syntax and Semantics

### Resource Declaration

Resources are declared in the parentheses after `try`. Each resource is a local variable declaration terminated by a semicolon:

```java
try (FileInputStream fis = new FileInputStream("in.bin");
     FileOutputStream fos = new FileOutputStream("out.bin")) {
    // use fis and fos
}
```

The resource variable is **implicitly final** — you cannot reassign it inside the try body.

### Close Order

Resources are closed in **reverse declaration order** (last declared, first closed). This mirrors the stack discipline: the last resource opened is the first that should be released.

```java
try (ResourceA a = new ResourceA();    // opened first
     ResourceB b = new ResourceB()) {  // opened second
    // body
}
// b.close() called first
// a.close() called second
```

### Exception Suppression

When the try body throws an exception *and* `close()` also throws, the close exception is **suppressed** and attached to the primary exception:

```java
try (MyResource r = new MyResource()) {
    throw new RuntimeException("body failed");
}
// RuntimeException("body failed") is thrown
// MyResource's close exception is attached via addSuppressed()
```

You can retrieve suppressed exceptions:

```java
} catch (RuntimeException e) {
    for (Throwable suppressed : e.getSuppressed()) {
        System.out.println("Suppressed: " + suppressed);
    }
}
```

### Multiple Resources

You can declare multiple resources in a single try statement. Each is closed independently, and close exceptions from earlier resources do not prevent later resources from being closed.

### No Resources

A try-with-resources with no resources is legal but pointless — it behaves exactly like a normal try block.

## Effective Final Variables (Java 9+)

Java 9 allows you to reference an effectively final variable declared outside the try:

```java
BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
try (reader) {  // Java 9+ — reader is effectively final
    String line = reader.readLine();
}
```

This is useful when the resource creation is conditional or factory-based.

## TWR vs finally for Cleanup

| Aspect | try-with-resources | finally |
|--------|-------------------|---------|
| Boilerplate | Minimal | Verbose |
| Close exceptions | Suppressed, never lost | May mask original exception |
| Null guards | Not needed | Required |
| Ordering | Automatic reverse order | Manual |
| Readability | Declarative | Imperative |

**Rule of thumb:** Always prefer TWR for `AutoCloseable` resources. Use `finally` only when you need cleanup logic that is not tied to a closeable resource (e.g., releasing a lock, clearing a thread-local).

## Common Pitfalls

### 1. Non-Closeable Resources

TWR only works with `AutoCloseable`. You cannot use it with raw sockets, threads, or third-party types that lack `close()`. Wrap them in an adapter or use `finally`.

### 2. Close Exception Handling

If you need to handle the exception from `close()`, catch it explicitly:

```java
try (MyResource r = new MyResource()) {
    // body
} catch (Exception e) {
    // handles both body and close exceptions
}
```

### 3. Swapping Resources

Do not assign a new resource to the TWR variable inside the body — the variable is implicitly final.

### 4. Nested TWR vs Multi-Resource

```java
// Nested — each in its own scope
try (ResourceA a = new ResourceA()) {
    try (ResourceB b = new ResourceB()) {
        // both open
    }
    // only a open
}

// Multi-resource — single scope
try (ResourceA a = new ResourceA();
     ResourceB b = new ResourceB()) {
    // both open
}
// both closed here
```

Nested TWR is appropriate when the second resource depends on the first.

## Production Patterns

### Database Resources

```java
try (Connection conn = dataSource.getConnection();
     PreparedStatement ps = conn.prepareStatement("SELECT * FROM users");
     ResultSet rs = ps.executeQuery()) {
    while (rs.next()) {
        System.out.println(rs.getString("name"));
    }
}
```

### Stream Pipelines

```java
try (Stream<String> lines = Files.lines(Path.of("data.csv"))) {
    long count = lines
        .filter(line -> !line.isBlank())
        .count();
    System.out.println("Non-blank lines: " + count);
}
```

### Custom Resource with Cleanup Logic

```java
public class Transaction implements AutoCloseable {
    private final Connection connection;
    private boolean committed = false;

    public Transaction(Connection connection) {
        this.connection = connection;
    }

    public void commit() throws SQLException {
        connection.commit();
        committed = true;
    }

    @Override
    public void close() throws SQLException {
        if (!committed) {
            connection.rollback();
        }
        connection.close();
    }
}
```

### Leaking Resources Intentionally

Sometimes you want a resource to outlive the try block. Return it from a factory method called inside the try, or use a wrapper that nullifies `close()`:

```java
// Resource escapes — use with extreme caution
static Socket createSocket(String host) throws IOException {
    Socket socket = new Socket(host, 80);
    // No try-with-resources — caller is responsible
    return socket;
}
```

## Version History

| Version | Change |
|---------|--------|
| JDK 7 | Try-with-resources introduced (JSR 334) |
| JDK 7 | `AutoCloseable` interface added |
| JDK 7 | Exception suppression for close() failures |
| JDK 9 | Effectively final variables allowed in resource declaration |

## Summary

Try-with-resources eliminates entire classes of resource-management bugs by making cleanup automatic, ordering correct, and exception handling transparent. It is the idiomatic way to manage any `AutoCloseable` in modern Java. Use it everywhere a resource has a finite lifecycle.

---

**Next:** [03 - Internals](03-internals/README.md) — How the compiler implements TWR.
