# Exception Chaining Decision Guide

## When to Use Exception Chaining

Use exception chaining when you need to preserve the causal chain of exceptions as they
propagate through different layers of your application.

---

## Decision Flowchart

```
Is the exception being thrown from a different layer than where it originated?
├── Yes → Chain the exception
│   ├── Wrap low-level exception in a higher-level exception
│   ├── Pass the original exception as the cause
│   └── Use constructor: new MyException("msg", originalException)
└── No → Do not chain
    └── Re-throw the original exception
```

---

## Key Decisions

### 1. Should I wrap the exception?

| Scenario | Action |
|----------|--------|
| Exception originates in the same layer | Re-throw as-is |
| Exception crosses a layer boundary | Wrap with higher-level exception |
| Exception is from a lower-level API | Translate to domain-specific exception |

### 2. Which exception type to use?

| Layer | Exception Type | Example |
|-------|---------------|---------|
| Infrastructure | `IOException`, `SQLException` | `java.io.IOException` |
| Data Access | `DataAccessException` | `com.example.DataAccessException` |
| Service | `ServiceException` | `com.example.ServiceException` |
| API | `UserNotFoundException` | `com.example.UserNotFoundException` |

### 3. When to use initCause() vs constructor?

- Use **constructor** `new MyException("msg", cause)` — most cases
- Use **initCause()** — when you cannot use a constructor (e.g., in a static factory method)

---

## Common Scenarios

### Scenario 1: Database exception translation

```java
try {
    dao.findById(id);
} catch (DataAccessException e) {
    throw new UserNotFoundException("User not found", e);
}
```

### Scenario 2: Network exception translation

```java
try {
    httpClient.send(request);
} catch (IOException e) {
    throw new CommunicationException("Failed to communicate with server", e);
}
```

### Scenario 3: Business logic exception wrapping

```java
try {
    processPayment(order);
} catch (PaymentException e) {
    throw new OrderProcessingException("Failed to process order", e);
}
```

### Scenario 4: Multiple exceptions

```java
List<Throwable> causes = new ArrayList<>();
for (Runnable task : tasks) {
    try {
        task.run();
    } catch (Exception e) {
        causes.add(e);
    }
}
if (!causes.isEmpty()) {
    throw new CompositeException("Multiple errors occurred", causes);
}
```

---

## Anti-Patterns to Avoid

| Anti-Pattern | Problem | Fix |
|--------------|---------|-----|
| Losing the cause | `throw new Exception("msg")` | Always pass the cause: `throw new Exception("msg", e)` |
| Double wrapping | Wrapping an already-wrapped exception | Only wrap at layer boundaries |
| Circular chains | `a.initCause(b); b.initCause(a);` | Never create circular references |
| Swallowing exceptions | `catch (Exception e) { }` | Always log or rethrow |

---

## Quick Reference

| Operation | Code |
|-----------|------|
| Chain with constructor | `new MyException("msg", cause)` |
| Chain with initCause | `ex.initCause(cause)` |
| Get cause | `ex.getCause()` |
| Get root cause | Traverse `getCause()` until null |
| Print full chain | `ex.printStackTrace()` |

---

## Rule of Thumb

> Wrap at layer boundaries, never lose the cause, and always preserve the root cause
> for debugging. Use exception translation to keep implementation details private.

---

## Next Steps

- Review `ExceptionChaining.java` for comprehensive examples
- Complete the exercises in `exercises/`
- Check the solutions in `solutions/`
