# 09 - Checked Exceptions in Java

## Scope

This topic covers Java checked exceptions — exceptions that the compiler requires you to
either catch or declare in a `throws` clause. You will learn when to use them, how they
differ from unchecked exceptions, and how to design APIs that use them effectively.

---

## Why Checked Exceptions Exist

Java is one of the few mainstream languages that enforces exception handling at compile
time. The designers wanted to guarantee that recoverable error conditions are never
silently ignored. A checked exception forces the caller to acknowledge that a method can
fail and to decide, at the call site, how to handle that failure.

This is especially valuable for **external failures** — I/O errors, network timeouts,
database errors — where the caller is the only entity that knows the correct recovery
strategy.

---

## Design Rationale

| Goal | How Checked Exceptions Achieve It |
|---|---|
| Compiler-enforced handling | The compiler refuses to compile code that ignores a checked exception |
| Documentation | A `throws` clause is part of a method's public contract |
| Recovery awareness | Callers must explicitly decide: catch, rethrow, or propagate |
| API clarity | Distinguishes recoverable failures from programming errors |

---

## What Are Checked Exceptions

A checked exception is any class that:

1. Extends `java.lang.Exception` (directly or indirectly).
2. Does **not** extend `java.lang.RuntimeException`.

Because they extend `Exception` and not `RuntimeException`, the Java compiler enforces
the **catch-or-specify** requirement on every method that can throw one.

```java
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
```

---

## Characteristics

| Attribute | Value |
|---|---|
| Superclass | `java.lang.Exception` (not `RuntimeException`) |
| Compiler enforcement | Must catch or declare in `throws` clause |
| Typically represents | Recoverable external failures |
| Examples | `IOException`, `SQLException`, `InterruptedException` |
| Intention | Caller should handle or propagate |
| When ignored | Compiles only if caught or declared |

---

## Checked Exception Decision Flow

```
┌─────────────────────┐
│ Method can fail?    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐      No      ┌─────────────────────┐
│ Is it recoverable?  │─────────────►│ Use unchecked       │
└──────────┬──────────┘              │ (RuntimeException)  │
           │ Yes                     └─────────────────────┘
           ▼
┌─────────────────────┐      No      ┌─────────────────────┐
│ Is it a programming │─────────────►│ Use checked          │
│ error?              │              │ (extends Exception)  │
└──────────┬──────────┘              └─────────────────────┘
           │ Yes
           ▼
┌─────────────────────┐
│ Use unchecked       │
│ (IllegalArgumentException)│
└─────────────────────┘
```

## Common Checked Exceptions

| Exception | Package | Typical Use |
|---|---|---|
| `IOException` | `java.io` | General I/O failure (read, write, close) |
| `FileNotFoundException` | `java.io` | Specified file does not exist |
| `SQLException` | `java.sql` | Database access error |
| `InterruptedException` | `java.lang` | A thread was interrupted while sleeping/waiting |
| `ClassNotFoundException` | `java.lang` | A required class was not found at runtime |
| `NoSuchMethodException` | `java.lang` | A requested method does not exist via reflection |
| `ReflectiveOperationException` | `java.lang` | Base class for reflection-related checked exceptions |
| `ParserConfigurationException` | `javax.xml.parsers` | XML parser configuration error |
| `SAXException` | `org.xml.sax` | SAX parsing error |
| `TimeoutException` | `java.util.concurrent` | An operation timed out |

---

## Checked Exception Contract

### Catch-or-Specify Rule

Every method that can throw a checked exception must do one of two things:

1. **Catch** the exception using a `try-catch` block.
2. **Specify** the exception in its `throws` declaration, propagating it to the caller.

```java
// Option 1: Catch the exception
public void readFile(String path) {
    try {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine();
    } catch (IOException e) {
        System.err.println("Failed to read file: " + e.getMessage());
    }
}

// Option 2: Declare the exception
public void readFile(String path) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line = reader.readLine();
}
```

### The `throws` Declaration

A `throws` clause is part of a method's signature. It tells callers what checked
exceptions the method might throw so they can prepare.

```java
public Connection connect(String url) throws SQLException {
    return DriverManager.getConnection(url);
}
```

### Subclass Override Rule

When overriding a method, the subclass method can declare:
- The same checked exceptions as the parent.
- A subclass of those exceptions.
- No checked exceptions at all (narrowing the contract).
- It must **not** declare new or broader checked exceptions.

```java
class Base {
    void process() throws IOException { }
}

class Child extends Base {
    @Override
    void process() throws FileNotFoundException { } // Narrower — OK
}
```

---

## When to Use Checked Exceptions

### Use checked exceptions for recoverable external failures:

- **File I/O**: The file might not exist, disk might be full.
- **Network operations**: The server might be unreachable.
- **Database access**: The query might fail, connection might drop.
- **Parsing external input**: XML, JSON, or other formats might be malformed.

### Do NOT use checked exceptions for:

- Programming errors (use `IllegalArgumentException`, `NullPointerException`, etc.).
- Conditions that should never happen in correct code.
- Situations where the caller cannot reasonably recover.

```java
// GOOD: External failure — caller must decide what to do
public byte[] readBytes(Path path) throws IOException {
    return Files.readAllBytes(path);
}

// BAD: Programming error — should be unchecked
public void setName(String name) throws InvalidNameException {
    if (name == null) {
        throw new InvalidNameException("Name cannot be null");
    }
    this.name = name;
}
```

---

## API Design: Checked vs Unchecked

| Choose Checked When... | Choose Unchecked When... |
|---|---|
| The caller can reasonably recover | It is a programming error |
| The failure is external to the application | The failure indicates a bug |
| You want to force the caller to handle it | You want to keep the API surface clean |
| The failure is expected occasionally | The failure should never happen in correct code |

### Guiding Principles

1. **Default to unchecked** for internal errors and programming mistakes.
2. **Use checked** when the caller needs to decide between alternative recovery strategies.
3. **Don't overuse checked exceptions** — too many makes an API cumbersome.
4. **Consider wrapping** low-level checked exceptions in domain-specific exceptions.

---

## Common Pitfalls

### 1. Too Many Checked Exceptions

When every method throws a checked exception, callers end up with deep `try-catch` blocks
and lost signal. Use domain-specific exceptions instead.

```java
// AVOID: Every method declares IOException
public void process() throws IOException {
    readConfig();
    openConnection();
    fetchData();
    writeResults();
}
```

### 2. Catching and Swallowing

Never catch a checked exception and do nothing. At minimum, log it.

```java
// AVOID
try {
    readFile(path);
} catch (IOException e) {
    // silently ignored
}
```

### 3. Wrapping Everything in RuntimeException

Wrapping every checked exception in an unchecked exception defeats the purpose of
checked exceptions. Only do this when crossing a layer boundary where checked exceptions
are not appropriate.

```java
// SOMETIMES ACCEPTABLE: Crossing a layer boundary
try {
    return database.query(sql);
} catch (SQLException e) {
    throw new DataAccessException("Query failed", e);
}
```

### 4. Declaring `throws Exception`

Declaring `throws Exception` on a public API method hides the specific failures a caller
should handle. Be specific.

```java
// AVOID: Vague contract
public void connect() throws Exception;

// PREFER: Specific contract
public void connect() throws IOException, TimeoutException;
```

---

## Production Patterns

### Pattern 1: Try-With-Resources

Always use try-with-resources for `AutoCloseable` resources. It guarantees cleanup even
when exceptions occur.

```java
try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
    String line;
    while ((line = reader.readLine()) != null) {
        process(line);
    }
} catch (IOException e) {
    log.error("Failed to read file", e);
    throw new ProcessingException("Could not read input file", e);
}
```

### Pattern 2: Exception Translation

Catch low-level checked exceptions and translate them into domain-specific exceptions.

```java
public User findUser(long id) {
    try {
        return userDao.findById(id);
    } catch (SQLException e) {
        throw new DataAccessException("Failed to find user: " + id, e);
    }
}
```

### Pattern 3: Partial Failure Handling

Sometimes you want to continue processing even when one item fails.

```java
List<String> results = new ArrayList<>();
for (Path file : files) {
    try {
        results.add(Files.readString(file));
    } catch (IOException e) {
        warnings.add("Skipping " + file + ": " + e.getMessage());
    }
}
```

### Pattern 4: Declaring Exceptions in Interfaces

When designing interfaces, declare the minimal set of checked exceptions. Consider
whether the interface contract should include them at all.

```java
public interface Repository<T> {
    T findById(long id) throws DataAccessException;
    void save(T entity) throws DataAccessException;
}
```

---

## Summary

| Concept | Key Point |
|---|---|
| What | Exceptions extending `Exception` (not `RuntimeException`) |
| Compiler rule | Catch-or-specify: catch or declare in `throws` |
| When to use | External, recoverable failures (I/O, network, database) |
| API design | Use sparingly; prefer unchecked for programming errors |
| Production | Try-with-resources, exception translation, partial failure handling |
| Pitfalls | Too many declarations, swallowing, vague `throws Exception` |

---

## Next Steps

- Review `CheckedException.java` for hands-on examples.
- Complete the exercises in `exercises/CheckedExceptionExercises.java`.
- Compare with unchecked exceptions in the `05-unchecked-exception` topic.
