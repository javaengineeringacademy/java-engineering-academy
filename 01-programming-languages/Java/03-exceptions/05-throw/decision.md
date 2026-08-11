# Decision Guide: When to Use throw

## Purpose of throw

The `throw` statement transfers control to the nearest enclosing `catch` or `finally` block. It is the mechanism by which code signals that an exceptional condition has occurred. Every `throw` is a design decision: it determines what information reaches the caller, how recoverable the failure is, and what the caller must do in response.

---

## Checked vs Unchecked: When to Throw Which

### Decision Tree

```
Is the failure recoverable by the caller?
├── Yes → Checked exception (caller can try alternative path)
│   └── Does a standard exception fit?
│       ├── Yes → Throw standard exception
│       └── No → Create custom checked exception
└── No → Unchecked exception
    ├── Is it a programming bug? → RuntimeException subclass
    └── Is it an unrecoverable system failure? → Error subclass
```

### Comparison Table

| Factor | Checked Exception | Unchecked Exception |
|--------|-------------------|---------------------|
| Caller obligation | Must handle or declare | May handle or propagate |
| Recovery expected | Yes — caller has alternative path | No — typically bug or fatal |
| API contract | Part of method signature | Not part of method signature |
| Refactoring cost | Adding later is breaking change | Adding later is safe |
| Typical use | I/O, network, validation | Null checks, index bounds, state |

### When to Throw Checked

Use checked exceptions when the caller can reasonably be expected to recover. The caller must declare the exception in the method signature or catch it. This creates an explicit contract.

```java
public User authenticate(String token) throws AuthenticationException {
    // Caller can retry, redirect to login, or use cached session
}
```

Checked exceptions are appropriate when:
- The failure is external to the program (network, file system, database)
- There is a documented alternative path
- The caller needs to make a decision based on the failure
- The exception is part of the API contract

### When to Throw Unchecked

Use unchecked exceptions for conditions that represent programming errors or unrecoverable states. The caller should not need to write catch blocks for these.

```java
public void setAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative: " + age);
    }
}
```

Unchecked exceptions are appropriate when:
- The argument is null, empty, or out of range (caller bug)
- The object is in an inconsistent state (invariant violation)
- A required resource is unavailable (system failure)
- The exception would propagate to a framework handler anyway

---

## Custom Exceptions vs Standard Exceptions

### Decision Tree

```
Does a standard exception accurately describe the failure?
├── Yes → Use the standard exception
│   └── Add a descriptive message
└── No → Create custom exception
    ├── Is the failure domain-specific?
    │   ├── Yes → Custom exception with domain context
    │   └── No → Is there a programmatic handler needed?
    │       ├── Yes → Custom exception
    │       └── No → Use standard exception with message
    └── Will callers catch this separately?
        ├── Yes → Custom exception (separate catch clause)
        └── No → Standard exception may suffice
```

### When to Create Custom Exceptions

1. **Domain-specific failure semantics** — The exception carries business meaning beyond the technical failure
2. **Programmatic handling** — Callers will catch and handle this exception differently from others
3. **Exception hierarchy** — You need a base type for multiple related failure modes
4. **API clarity** — The exception name communicates intent in the method signature

```java
// Custom exception is justified here: domain-specific, catchable, meaningful
public class InsufficientFundsException extends RuntimeException {
    private final double balance;
    private final double attempted;
    // ...
}
```

### When to Use Standard Exceptions

1. **Argument validation** — `IllegalArgumentException`, `NullPointerException`, `IllegalStateException`
2. **Index bounds** — `IndexOutOfBoundsException`, `ArrayIndexOutOfBoundsException`
3. **Type mismatches** — `ClassCastException`, `UnsupportedOperationException`
4. **Concurrency** — `IllegalMonitorStateException`, `ConcurrentModificationException`

```java
// Standard exception is appropriate: argument validation
public void setRate(double rate) {
    if (rate < 0 || rate > 1) {
        throw new IllegalArgumentException("Rate must be between 0 and 1: " + rate);
    }
}
```

### Anti-pattern: Custom Exception for Everything

Creating custom exceptions for common argument validation adds boilerplate without value. A method that throws `InvalidEmailFormatException extends RuntimeException` is less clear than one that throws `IllegalArgumentException("Invalid email format: " + email)`.

---

## throw vs throws

### Decision Table

| Aspect | throw | throws |
|--------|-------|--------|
| What it does | Creates and throws an exception instance | Declares exceptions in method signature |
| When used | Inside a method body | In method declaration |
| Purpose | Signal an exceptional condition | Inform caller of possible exceptions |
| Required | When an exception must be thrown | For checked exceptions not caught locally |
| Compiler enforced | No — can throw any throwable | Yes — must declare checked exceptions |

### Decision Tree

```
Are you inside a method that needs to throw?
├── Yes → Is the exception checked?
│   ├── Yes → Does the method catch it?
│   │   ├── Yes → throw (inside catch block, rethrow)
│   │   └── No → throws (declare in signature)
│   └── No → throw (no declaration needed)
└── No → You are catching an existing throw
    └── Use try-catch
```

### Common Patterns

**Throw in validation:**
```java
public void process(Order order) {
    Objects.requireNonNull(order, "order cannot be null");
    // ...
}
```

**Throws in declaration:**
```java
public Connection connect(String url) throws SQLException {
    // ...
}
```

**Rethrow after catch:**
```java
try {
    delegate.process(request);
} catch (IOException e) {
    log.error("Failed processing request", e);
    throw new ProcessingException("Request failed", e);
}
```

---

## Exception Message Quality Guidelines

### Principles

1. **State what failed** — The exception type names the category; the message names the specific failure
2. **Include relevant values** — Parameter names, actual values, expected ranges
3. **Do not include stack trace in message** — The stack trace is already captured separately
4. **Do not log in the message** — Logging is the caller's responsibility
5. **Use complete sentences** — Messages appear in logs, monitoring dashboards, and error reports

### Good vs Bad Messages

| Bad | Good |
|-----|------|
| `"Error"` | `"Connection refused to database at localhost:5432"` |
| `"Invalid input"` | `"Price must be positive, got: -5.00"` |
| `"null"` | `"User ID cannot be null"` |
| `"Failed"` | `"Failed to parse config from /etc/app.yml: unexpected token at line 42"` |

### Message Construction Patterns

```java
// Pattern 1: Include the actual value
throw new IllegalArgumentException("Port must be between 1 and 65535, got: " + port);

// Pattern 2: Include the resource identifier
throw new FileNotFoundException("Config file not found: " + path);

// Pattern 3: Include expected vs actual
throw new IllegalStateException(
    "Expected state ACTIVE, got: " + currentState + " for order " + orderId);

// Pattern 4: Chained cause — message explains the wrapper, not the cause
throw new DataAccessException("Failed to persist order " + orderId, cause);
```

---

## Common Code Review Comments for throw

1. **"Use a more specific exception type."** — Throwing `Exception` or `RuntimeException` instead of a domain-appropriate type.

2. **"Include the failing value in the message."** — `"Invalid age"` should be `"Invalid age: -5"`.

3. **"Do not throw checked exceptions from utility methods unless the caller can recover."** — Utility methods that throw `IOException` force every caller to handle it, even when recovery is not possible.

4. **"Preserve the cause when rethrowing."** — `throw new ProcessingException(e)` loses the original stack trace. Use `throw new ProcessingException("message", e)`.

5. **"Do not throw in a constructor without good reason."** — If construction fails, the object never exists, which can confuse callers and complicate resource management.

6. **"Validate early, throw early."** — Do not let invalid state propagate through multiple operations before failing.

7. **"Exception messages should be constant strings or use parameterized formatting."** — Avoid string concatenation in hot paths if the exception is not thrown.

8. **"Do not use exceptions for control flow."** — Throwing and catching exceptions to branch logic is slower and less readable than conditionals.

---

## Common Production Mistakes

### 1. Throwing Inside a Loop

```java
// BAD: First error stops processing; caller cannot know which items failed
for (String name : names) {
    if (name == null) {
        throw new IllegalArgumentException("Name cannot be null");
    }
}

// BETTER: Collect all errors, throw once
List<String> errors = new ArrayList<>();
for (int i = 0; i < names.size(); i++) {
    if (names.get(i) == null) {
        errors.add("Name at index " + i + " cannot be null");
    }
}
if (!errors.isEmpty()) {
    throw new ValidationException("Validation failed: " + String.join("; ", errors));
}
```

### 2. Losing the Cause on Rethrow

```java
// BAD: Original stack trace lost
try {
    readFile(path);
} catch (IOException e) {
    throw new ConfigException("Failed to read config");
}

// CORRECT: Preserve cause
try {
    readFile(path);
} catch (IOException e) {
    throw new ConfigException("Failed to read config from " + path, e);
}
```

### 3. Throwing Generic Types in Public APIs

```java
// BAD: Caller cannot catch specifically
public void process() throws Exception { ... }

// BETTER: Declare the specific checked exception
public void process() throws ProcessingException { ... }
```

### 4. Swallowing Exceptions Then Throwing New Ones

```java
// BAD: Original context lost
try {
    parse(input);
} catch (ParseException e) {
    throw new RuntimeException("Parse failed");
}

// CORRECT: Chain the exception
try {
    parse(input);
} catch (ParseException e) {
    throw new RuntimeException("Parse failed for input: " + input, e);
}
```

### 5. Throwing in Finally Masks Original Exception

```java
// BAD: Original exception from try is lost
try {
    return fetchData();
} finally {
    closeConnection(); // throws IOException — masks the try exception
}

// CORRECT: Wrap finally logic in try-catch
try {
    return fetchData();
} finally {
    try { closeConnection(); } catch (IOException ignored) { }
}
```

### 6. Throwing Unchecked from API Boundary

```java
// BAD: Service layer throws unchecked; caller has no contract
public User findUser(String id) {
    if (id == null) throw new NullPointerException("id is null");
    // ...
}

// BETTER: Validate at entry, throw checked if recovery possible
public User findUser(String id) throws UserNotFoundException {
    Objects.requireNonNull(id, "id");
    // ...
}
```

### 7. Empty Exception Messages

```java
// BAD: No diagnostic information
throw new IllegalArgumentException();

// BETTER: Always include context
throw new IllegalArgumentException("Port cannot be null or empty");
```

---

## When to Escalate

- You are designing an exception hierarchy for a multi-module system — the interface contracts need review.
- You are deciding whether a third-party exception should be wrapped or propagated — the API boundary implications need discussion.
- You are adding a checked exception to a widely-used interface — this is a breaking change.
- You are considering throwing exceptions in a hot path for control flow — performance implications need measurement.
