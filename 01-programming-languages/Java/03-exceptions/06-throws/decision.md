# Decision Guide: The throws Declaration

## Purpose

The `throws` clause is a method-level contract that declares which checked exceptions a method may propagate to its caller. This guide helps engineers decide what to declare, when to translate exceptions, and how to avoid common mistakes that degrade API quality.

---

## Decision Tree: Should You Declare a Checked Exception?

**Does the method call code that throws a checked exception?**
- Yes → Can you handle it meaningfully at this layer?
  - Yes → Handle it; do not declare throws
  - No → Does the caller need to know about it?
    - Yes → Declare it in throws
    - No → Translate to an unchecked exception
- No → Do not declare any checked exception

**Are you dealing with a programming error (null arg, illegal state, bad index)?**
- Yes → Use RuntimeException; do not declare throws
- No → Continue above

---

## When to Declare Checked vs Unchecked Exceptions

| Criterion | Declare Checked (throws) | Do Not Declare |
|---|---|---|
| Caller can recover | Yes | — |
| Caller cannot recover | — | Yes (use RuntimeException) |
| External system failure (IO, DB, network) | Yes | — |
| Programming error (null, bad state) | — | Yes |
| Business rule violation (recoverable) | Yes | — |
| Business rule violation (bug) | — | Yes |
| Method is part of a public API | Yes, if checked | — |
| Method is internal implementation | Prefer translation | — |

### Rule of Thumb

Declare `throws` for checked exceptions the caller **must** handle or propagate. Do not declare `throws` for unchecked exceptions — doing so clutters the signature without adding value.

---

## Method Signature Design with throws

### Specific Exceptions Over Generic

```java
// GOOD — caller knows exactly what to handle
public String readConfig(String path) throws FileNotFoundException, IOException { }

// BAD — caller must catch Exception, loses type information
public String readConfig(String path) throws Exception { }
```

### Exception Hierarchy in throws

Declare the most specific type that covers the expected failures. If multiple unrelated exceptions are possible, list them separately.

```java
// Covers all IO subtypes — acceptable when callers treat them uniformly
public void process() throws IOException { }

// Covers specific subtypes — better when callers handle them differently
public void process() throws FileNotFoundException, SocketException { }
```

### Interface Stability

Adding a checked exception to a published interface method is a **breaking change** — all implementations and callers will fail to compile. Removing an exception from throws is binary compatible.

| Change | Binary Compatible? |
|---|---|
| Remove exception from throws | Yes |
| Add exception to throws | No |
| Narrow exception type in throws | Yes |
| Widen exception type in throws | No |

---

## When to Use throws Exception (and When Not To)

### Acceptable Uses

- Generic utility methods (retry, execute, run) where the exception type is parameterized
- Test helpers and test utilities
- Framework code that delegates to user-provided callbacks

```java
// Generic executor — exception type is determined by the Callable
public <T> T execute(Callable<T> task) throws Exception {
    return task.call();
}
```

### Unacceptable Uses

- Public API methods in production code
- Methods in library code consumed by other teams
- Any method where callers cannot meaningfully handle "any exception"

```java
// BAD — callers have no idea what to catch
public void processOrder(Order order) throws Exception { }

// GOOD — specific failure modes
public void processOrder(Order order) throws OrderValidationException, PaymentException { }
```

### Why throws Exception Is Harmful

| Problem | Explanation |
|---|---|
| Callers cannot handle selectively | Must catch `Exception`, which swallows everything |
| Hides the real failure modes | A method throwing `IOException` and `SQLException` looks the same as one throwing `Exception` |
| Prevents targeted recovery | Caller cannot retry on `IOException` while failing fast on `SQLException` |
| Makes code review harder | Reviewer cannot assess error handling without reading the implementation |

---

## Exception Translation Patterns

Exception translation catches a low-level exception and rethrows it as a domain-appropriate exception at the current layer. This is the primary mechanism for keeping layer boundaries clean.

### The Pattern

```
Layer N (low-level)          Layer N+1 (domain)              Layer N+2 (caller)
─────────────────          ──────────────────              ──────────────────
throws IOException    →    catches IOException             catches ServiceException
                           throws ServiceException
```

### When to Translate

| Situation | Action |
|---|---|
| Low-level exception is implementation-specific | Translate to domain exception |
| Caller should not depend on implementation details | Translate at the boundary |
| Multiple low-level exceptions map to one domain concept | Translate to a single domain exception |
| Low-level exception carries useful context | Preserve as the cause |

### When NOT to Translate

| Situation | Action |
|---|---|
| Caller can handle the low-level exception directly | Let it propagate |
| Low-level exception is already domain-appropriate | Do not wrap unnecessarily |
| Translation would lose important type information | Declare the specific type instead |

### Translation Example

```java
// Low-level: throws IOException
public String sendRequest(String url) throws IOException {
    // network call
}

// Mid-level: translates to domain exception
public OrderStatus getStatus(String orderId) {
    try {
        return sendRequest("/orders/" + orderId);
    } catch (IOException e) {
        throw new OrderException("Failed to get status", e);
    }
}
```

### Multi-Cause Translation

When a method attempts multiple operations and both fail, preserve both causes:

```java
public Data load(String primary, String fallback) {
    IOException primaryError = null;
    try {
        return readFrom(primary);
    } catch (IOException e) {
        primaryError = e;
    }
    try {
        return readFrom(fallback);
    } catch (IOException e) {
        DataAccessException ex = new DataAccessException("Both sources failed", e);
        ex.addSuppressed(primaryError);
        throw ex;
    }
}
```

---

## Common Code Review Comments for throws Declarations

| Comment | Issue | Fix |
|---|---|---|
| "Do not declare `throws Exception`" | Generic exception hides failure modes | List specific exception types |
| "Remove `throws IllegalArgumentException`" | Unchecked exceptions do not need declaration | Remove the clause |
| "Translate `IOException` at the boundary" | Low-level exception leaks into domain layer | Catch and rethrow as domain exception |
| "Add `@throws` Javadoc for this" | Declared checked exception is undocumented | Add Javadoc entry |
| "This is a breaking change to the interface" | Checked exception added to published interface | Use unchecked exception or redesign |
| "Use the most specific type" | `throws IOException` when only `FileNotFoundException` is thrown | Narrow to `FileNotFoundException` |
| "Preserve the cause in translation" | `throw new ServiceException(e.getMessage())` loses cause | Use `new ServiceException("msg", e)` |
| "Restore interrupt status" | `InterruptedException` caught and ignored | Call `Thread.currentThread().interrupt()` |

---

## Common Production Mistakes

### 1. Declaring Only Unchecked Exceptions

```java
// UNNECESSARY — adds no value
public void validate(String input) throws IllegalArgumentException { }

// BETTER — just throw it
public void validate(String input) {
    if (input == null) throw new IllegalArgumentException("Input is null");
}
```

### 2. Throwing Too Broadly

```java
// BAD — caller has no idea what can go wrong
public void process(String input) throws Exception { }

// BETTER — specific exceptions
public void process(String input) throws IOException, DataException { }
```

### 3. Swallowing Checked Exceptions

```java
// BAD — loses checked status, no context
public User getUser(long id) {
    try {
        return repository.findById(id);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

// BETTER — domain-specific unchecked exception with context
public User getUser(long id) {
    try {
        return repository.findById(id);
    } catch (SQLException e) {
        throw new DataAccessException("Failed to get user: " + id, e);
    }
}
```

### 4. Ignoring InterruptedException

```java
// BAD — loses interrupt status
try { Thread.sleep(1000); } catch (InterruptedException e) { }

// GOOD — restore interrupt status
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    throw new ServiceException("Interrupted during wait", e);
}
```

### 5. Adding Checked Exceptions to Published Interfaces

```java
// BEFORE (published interface)
public interface PaymentService {
    void process(Payment payment) throws PaymentException;
}

// AFTER (breaking change — all implementations fail to compile)
public interface PaymentService {
    void process(Payment payment) throws PaymentException, FraudException;
}
```

Fix: Use an unchecked exception for the new failure mode, or introduce a new method.

### 6. Logging and Rethrowing the Same Exception

```java
// BAD — exception logged at every layer
catch (IOException e) {
    log.error("IO error", e);  // logged here
    throw new ServiceException("IO error", e);  // and again at the next layer
}

// BETTER — log once at the boundary
catch (IOException e) {
    throw new ServiceException("IO error", e);  // log at the handler, not here
}
```

---

## Comparison Tables

### throws Declaration Patterns

| Pattern | When to Use | Example |
|---|---|---|
| Declare and propagate | Caller must handle the checked exception | `throws IOException` |
| Catch, wrap, rethrow | Translate at layer boundary | `catch (IOException e) { throw new ServiceException(e); }` |
| Catch and handle | Fallback is available internally | `catch (IOException e) { useFallback(); }` |
| Declare generic | Framework/utility code only | `throws Exception` |

### Checked vs Unchecked in throws

| Aspect | Checked in throws | Unchecked in throws |
|---|---|---|
| Compiler enforced | Yes | No |
| Caller must handle | Yes (catch or declare) | No |
| Adds to method contract | Yes | Rarely useful |
| Binary compatible to remove | Yes | Yes |
| Recommended | Yes, for recoverable failures | No, unless documenting custom exceptions |

### Exception Translation Decisions

| Low-Level Exception | Domain Exception | Preserves Cause? |
|---|---|---|
| `IOException` | `DataAccessException` | Yes |
| `SQLException` | `RepositoryException` | Yes |
| `ParseException` | `ValidationException` | Yes |
| `InterruptedException` | `ServiceException` | Yes (also restores interrupt) |

---

## Decision Flow Diagram

```
Method throws checked exception
    |
    +-- Can this layer handle it?
    |   +-- Yes -> Handle it; no throws needed
    |   +-- No  -> Continue
    |
    +-- Should the caller handle it?
    |   +-- Yes -> Declare in throws
    |   +-- No  -> Translate to domain exception
    |
    +-- Is the exception already domain-appropriate?
    |   +-- Yes -> Declare in throws
    |   +-- No  -> Translate
    |
    +-- Is this a programming error?
        +-- Yes -> Use RuntimeException; no throws needed
        +-- No  -> Declare checked exception in throws
```

---

## When to Escalate

- You are adding a checked exception to a published interface — this is a breaking change requiring team review.
- You are designing a domain exception hierarchy — the naming and structure should be agreed upon by the team.
- You are deciding between checked and unchecked for a new domain concept — the convention should be established at the architectural level.
- You are implementing exception translation across microservice boundaries — the error contract needs agreement between service owners.

---

## Summary

| Concept | Key Point |
|---|---|
| throws Declaration | Declares which checked exceptions a method may propagate |
| Checked Exceptions | Must be declared — compiler enforced |
| Unchecked Exceptions | Optional to declare — rarely useful |
| Exception Translation | Catch low-level, rethrow as domain exceptions at boundaries |
| throws Exception | Acceptable only in generic utilities; avoid in production APIs |
| Interface Stability | Adding checked exceptions is a breaking change |
| Documentation | Every declared checked exception should have Javadoc |
