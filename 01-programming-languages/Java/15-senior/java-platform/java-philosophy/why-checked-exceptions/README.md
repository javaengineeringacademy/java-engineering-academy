# Why Checked Exceptions?

## Historical Context

Checked exceptions were a revolutionary concept when Java 1.0 was released in 1996. The idea was borrowed from Mesa programming language and refined by James Gosling.

### The Original Vision

```java
// The dream: explicit error handling
try {
    FileInputStream fis = new FileInputStream("config.txt");
    // Process file
} catch (FileNotFoundException e) {
    // Handle recoverable error
    System.out.println("Config file not found, using defaults");
}
```

The goal was to make error handling **mandatory and explicit**, preventing programmers from silently ignoring errors.

## The Argument For (Forced Error Handling)

### Explicit Error Contracts

```java
// Method signature tells you what can go wrong
public void processFile(String path) throws FileNotFoundException, 
                                           IOException {
    // Caller MUST handle these exceptions
}
```

**Benefits:**

1. **Documentation**: Exceptions are part of the API contract
2. **Compiler enforcement**: Cannot ignore errors
3. **Recovery**: Encourages graceful error handling
4. **Safety**: Critical systems must handle all failure modes

### Real-World Example

```java
// Banking system: must handle all error cases
public TransferResult transfer(Account from, Account to, BigDecimal amount) 
        throws InsufficientFundsException, 
               AccountLockedException,
               NetworkException {
    // All three errors are documented and must be handled
    try {
        // Transfer logic
    } catch (InsufficientFundsException e) {
        // Notify user
        return TransferResult.insufficientFunds();
    } catch (AccountLockedException e) {
        // Queue for later
        return TransferResult.locked();
    } catch (NetworkException e) {
        // Retry or fail
        throw e;
    }
}
```

## The Argument Against (Verbose Boilerplate)

### The Reality of Enterprise Java

```java
// Typical enterprise code
try {
    Class.forName("com.example.dao.UserDAO");
} catch (ClassNotFoundException e) {
    throw new RuntimeException(e); // Just wrapping
}

try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Restore flag
    throw new RuntimeException(e);
}

try {
    JAXBContext.newInstance(MyClass.class);
} catch (JAXBException e) {
    throw new RuntimeException(e);
}
```

### Problems

1. **Swallowed exceptions**: Developers ignore exceptions to reduce boilerplate
2. **Over-wrapping**: Catching and rethrowing as RuntimeException
3. **Verbosity**: 50% more code for error handling
4. **False security**: Many checked exceptions are unrecoverable

### The Anti-Patterns

```java
// Anti-pattern 1: Swallowed exception
try {
    riskyOperation();
} catch (Exception e) {
    // Do nothing - VERY BAD
}

// Anti-pattern 2: Catch-all
try {
    riskyOperation();
} catch (Throwable t) {
    // Too broad
}

// Anti-pattern 3: Over-wrapping
try {
    riskyOperation();
} catch (CheckedException e) {
    throw new RuntimeException(e); // Loses context
}
```

## How Other Languages Handle Errors

### Go: Explicit Error Returns

```go
result, err := doSomething()
if err != nil {
    return err
}
```

**Pros**: Explicit, no hidden control flow
**Cons**: Verbose, easy to forget error check

### Rust: Result Type

```rust
match do_something() {
    Ok(result) => println!("Success: {}", result),
    Err(e) => println!("Error: {}", e),
}
```

**Pros**: Compiler enforces handling, composable
**Cons**: Learning curve

### Kotlin: Nothing Type

```kotlin
fun doSomething(): Result<String> = runCatching {
    riskyOperation()
}.getOrElse { "default" }
```

**Pros**: Functional, concise
**Cons**: Different mental model

### Swift: Error Handling

```swift
do {
    let result = try riskyOperation()
} catch {
    print(error)
}
```

**Pros**: Similar to Java, but all exceptions are unchecked
**Cons**: Still verbose

## Java's Evolution

### The Trend Toward Unchecked Exceptions

```java
// Java 8+: Many new APIs use unchecked exceptions
Optional.empty(); // NoSuchElementException (unchecked)
Stream.collect(...); // Various unchecked exceptions
CompletableFuture.join(); // CompletionException (unchecked)
```

### Modern Java Patterns

```java
// Pattern 1: Optional for expected absence
public Optional<User> findUser(String id) {
    return Optional.ofNullable(userMap.get(id));
}

// Pattern 2: Result-like approach
public class Result<T> {
    private final T value;
    private final Exception error;
    // ...
}

// Pattern 3: Unchecked exceptions for unrecoverable errors
public void process(String data) {
    Objects.requireNonNull(data, "data must not be null");
    // NullPointerException is unchecked
}
```

### When to Use Checked vs Unchecked

**Use Checked When:**
- Caller can reasonably recover
- Error is part of normal operation
- Multiple failure modes need different handling

**Use Unchecked When:**
- Programming error (null, array index)
- Unrecoverable error
- Implementation detail

## The Future of Error Handling

### Project Loom (Virtual Threads)

```java
// Virtual threads make blocking acceptable
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> {
        // Blocking I/O is fine now
        var data = readFromNetwork(); // Can throw IOException
        processData(data);
    });
}
```

### Records and Sealed Classes

```java
// Algebraic data types for error handling
sealed interface Result<T> permits Success, Failure {}
record Success<T>(T value) implements Result<T> {}
record Failure<T>(Exception error) implements Result<T> {}
```

### Pattern Matching

```java
// Cleaner exception handling
try {
    riskyOperation();
} catch (IOException e) when (e instanceof FileNotFoundException) {
    // Handle specific case
} catch (IOException e) {
    // Handle other IO errors
}
```

## Best Practices

### 1. Prefer Unchecked Exceptions for Programming Errors

```java
public void process(Order order) {
    Objects.requireNonNull(order, "order must not be null");
    // Don't throw checked exception for null check
}
```

### 2. Use Checked Exceptions for Recoverable Errors

```java
public User login(String username, String password) 
        throws AuthenticationException {
    // Caller can show login form again
}
```

### 3. Don't Over-Wrap

```java
// Bad
try {
    riskyOperation();
} catch (CheckedException e) {
    throw new RuntimeException(e); // Loses context
}

// Better
try {
    riskyOperation();
} catch (CheckedException e) {
    throw new ServiceException("Failed to process", e); // Meaningful
}
```

### 4. Document Exceptions

```java
/**
 * Process user registration.
 * 
 * @throws DuplicateEmailException if email already exists
 * @throws ValidationException if input is invalid
 */
public User register(String email, String password) 
        throws DuplicateEmailException, ValidationException {
    // Implementation
}
```

## Conclusion

Checked exceptions were a bold experiment that didn't quite work out as intended. While they forced error handling, they also created massive boilerplate and encouraged bad patterns. Modern Java is moving toward unchecked exceptions, Optional, and functional error handling patterns.

The lesson: Good intentions don't always lead to good design. Sometimes explicit is worse than implicit.

## Overview

Checked exceptions are Java's mechanism for enforcing compile-time error handling on recoverable errors. Introduced in Java 1.0, they force callers to either catch or declare exceptions using `throws`. The design philosophy: if a method can fail in a way the caller can recover from, the compiler should force the caller to acknowledge it. In practice, checked exceptions became controversial—creating massive boilerplate, encouraging swallowed exceptions, and leading many developers to wrap them in `RuntimeException`.

## Why This Concept Exists

Java 1.0 borrowed exception handling from Mesa (via C++) but made all exceptions checked. The motivation: prevent programmers from silently ignoring errors. At the time, error handling was ad-hoc—return codes, error flags, or nothing. Checked exceptions made error handling mandatory and explicit. The compiler became an enforcer of robustness. However, the designers underestimated the verbosity cost and the fact that many checked exceptions are unrecoverable (e.g., `ClassNotFoundException`).

## Internal Working

### Bytecode Verification of Checked Exceptions

```java
// The compiler tracks checked exceptions in method signatures
public void processFile(String path) throws FileNotFoundException, IOException {
    FileInputStream fis = new FileInputStream(path); // throws FileNotFoundException
    // ...
}

// Bytecode includes exception table:
// Exception table:
//   from    to  target type
//     0     8    11   Class java/io/FileNotFoundException
//     0    14    23   Class java/io/IOException
```

The JVM verifies that catch blocks handle declared exceptions or their subtypes. The `throws` clause is metadata—enforced at compile time, not runtime.

### How Exception Propagation Works

```java
// Exception creation: lightweight (no stack trace capture)
throw new RuntimeException("error"); // No stack trace

// Exception with stack trace (expensive)
throw new RuntimeException("error"); // Captures stack at throw site

// Stack trace is lazy (filled on first getStackTrace() call)
// This is why RuntimeException is "cheap" — no stack trace captured
```

### Exception Table in Bytecode

```
// Each method has an exception handler table
// Format: [start_pc, end_pc, handler_pc, catch_type]

// Java source:
try {
    riskyOperation();       // 0-8
} catch (IOException e) {  // handler at 11
    handleIO(e);
}

// Bytecode exception table:
// 0: invoke riskyOperation
// 8: goto 20
// 11: astore_1            // handler for IOException
// 12: invoke handleIO
// 20: return
```

## Examples

### Best Practice: Layered Exception Handling

```java
// Layer 1: Low-level (throw specific checked exceptions)
public class FileRepository {
    public User findById(String id) throws DataAccessException {
        try {
            return readFromFile(id);
        } catch (FileNotFoundException e) {
            throw new DataAccessException("User not found: " + id, e);
        } catch (IOException e) {
            throw new DataAccessException("IO error reading user: " + id, e);
        }
    }
}

// Layer 2: Service layer (convert to unchecked)
public class UserService {
    public User getUser(String id) {
        try {
            return fileRepository.findById(id);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get user", e);
        }
    }
}

// Layer 3: Controller (handle gracefully)
@RestController
public class UserController {
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUser(id));
        } catch (ServiceException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
```

### Custom Exception Hierarchy

```java
// Base exception for the application
public abstract class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApplicationException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() { return errorCode; }
}

// Specific exceptions
public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String userId) {
        super("User not found: " + userId, ErrorCode.USER_NOT_FOUND, null);
    }
}

public class InsufficientFundsException extends ApplicationException {
    public InsufficientFundsException(BigDecimal deficit) {
        super("Insufficient funds: " + deficit, ErrorCode.INSUFFICIENT_FUNDS, null);
    }
}
```

### Result Type Pattern (Java Alternative)

```java
// Sealed interface for Result type
public sealed interface Result<T> permits Success, Failure {}
public record Success<T>(T value) implements Result<T> {}
public record Failure<T>(Exception error) implements Result<T> {}

// Usage: no checked exceptions needed
public Result<User> findUser(String id) {
    try {
        return new Success<>(repository.findById(id));
    } catch (DataAccessException e) {
        return new Failure<>(e);
    }
}

// Pattern matching to handle result
Result<User> result = findUser("123");
switch (result) {
    case Success(var user) -> processUser(user);
    case Failure(var error) -> log.error("Failed", error);
}
```

## Performance

### Exception Creation Cost

| Operation | Time | Allocation |
|-----------|------|------------|
| `new RuntimeException()` | ~100ns | 1 object + stack trace |
| `new RuntimeException()` with stack | ~5μs | 1 object + stack trace array |
| `throw` + `catch` | ~1μs | Method frame + exception object |
| Checked exception (normal path) | ~0ns | Nothing (JIT eliminates) |

### JIT Optimization: Exception Table Elimination

The JIT compiler can eliminate exception handlers when:
1. Exception is never thrown (dead code elimination)
2. Exception handler is empty
3. Method is hot and exception path is cold

```java
// Hot path: JIT eliminates exception handling overhead
public int process(List<String> list) {
    try {
        return list.stream()
            .mapToInt(Integer::parseInt)
            .sum();
    } catch (NumberFormatException e) {
        return 0; // Rarely reached, JIT may eliminate
    }
}
```

### Overhead Comparison

| Approach | Compile Time | Runtime (Normal) | Runtime (Error) |
|----------|-------------|-----------------|----------------|
| Checked exceptions | Compiler enforces | 0 overhead | ~5μs |
| Result type | None | 0 overhead | ~0.5μs (no stack trace) |
| Return codes | None | ~1ns check | ~1ns |
| Optional | None | ~5ns creation | N/A |

## Pitfalls

### 1. Swallowing Exceptions

```java
// BAD: Exception silently ignored
try {
    riskyOperation();
} catch (Exception e) {
    // Do nothing — BUG silently hidden
}

// GOOD: At minimum, log the exception
try {
    riskyOperation();
} catch (Exception e) {
    log.error("Operation failed", e);
}
```

### 2. Over-Wrapping Exceptions

```java
// BAD: Wrapping everything in RuntimeException
try {
    riskyOperation();
} catch (CheckedException e) {
    throw new RuntimeException(e); // Loses context
}

// GOOD: Create meaningful custom exception
try {
    riskyOperation();
} catch (CheckedException e) {
    throw new ServiceException("Failed to process order " + orderId, e);
}
```

### 3. Catching Throwable/Error

```java
// BAD: Catching everything
try {
    riskyOperation();
} catch (Throwable t) {
    // Catches OutOfMemoryError, StackOverflowError, etc.
}

// GOOD: Catch specific exceptions
try {
    riskyOperation();
} catch (IOException e) {
    handleIO(e);
} catch (SQLException e) {
    handleSQL(e);
}
```

### 4. Using Checked Exceptions for Programming Errors

```java
// BAD: NullPointerException is a programming error, not recoverable
public void process(Order order) throws NullPointerException {
    // NullPointerException should never be declared
}

// GOOD: Use unchecked exceptions for programming errors
public void process(Order order) {
    Objects.requireNonNull(order, "order must not be null");
}
```

### 5. Declaring Too Many Checked Exceptions

```java
// BAD: Method throws 5 checked exceptions
public void process() throws A, B, C, D, E {
    // Caller must handle all 5 — overwhelming
}

// GOOD: Use a facade exception
public void process() throws ProcessingException {
    // Internal exceptions wrapped in ProcessingException
}
```

## References

- [Java Language Specification: Exceptions](https://docs.oracle.com/javase/specs/jls/se17/html/jls-11.html)
- *Effective Java* by Joshua Bloch — Item 69: Use exceptions for exceptional conditions
- *Java Concurrency in Practice* by Brian Goetz
- [Oracle: Java Exception Handling](https://www.oracle.com/java/technologies/javase/exception-handling.html)
- [OpenJDK Source: Throwable.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Throwable.java)
