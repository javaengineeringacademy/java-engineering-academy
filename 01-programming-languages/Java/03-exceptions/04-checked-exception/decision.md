# Decision Guide: Checked Exceptions

## When to Use Checked Exceptions

Use checked exceptions when all of the following are true:

1. The failure is **external** to the application (I/O, network, database).
2. The failure is **recoverable** — the caller can retry, use a fallback, or report it.
3. You want the **compiler** to enforce that callers handle the failure.
4. The failure is **expected occasionally** in normal operation.

## When NOT to Use Checked Exceptions

| Scenario | Use Instead |
|---|---|
| Programming error (null, bad argument) | `IllegalArgumentException`, `NullPointerException` |
| Internal invariant violation | Unchecked exception or assertion |
| Failure that should never happen in correct code | `IllegalStateException` |
| Cross-layer boundary where checked exceptions are noisy | Wrap in unchecked exception |
| The caller cannot reasonably recover | Unchecked exception (let it propagate) |

## Decision Checklist

```
1. Is the failure caused by the caller's code?
   → YES: Use unchecked (programming error)
   → NO: Continue

2. Is the failure caused by an external system (file, network, DB)?
   → YES: Use checked (recoverable external failure)
   → NO: Continue

3. Can the caller reasonably recover or decide what to do?
   → YES: Use checked
   → NO: Use unchecked (let it propagate as an unexpected error)

4. Are you designing a public API?
   → YES: Be specific about which checked exceptions are thrown
   → NO: Consider whether checked exceptions add value at this layer

5. Will every caller need to handle this exception?
   → YES: Consider unchecked with documentation
   → NO: Use checked to force explicit handling
```

## Quick Reference

| Failure Type | Recommended Exception Type |
|---|---|
| File not found | Checked (`FileNotFoundException`) |
| Network timeout | Checked (`java.net.SocketTimeoutException`) |
| Database query failed | Checked (`SQLException`) — wrap in domain exception |
| Null argument | Unchecked (`NullPointerException`) |
| Invalid state | Unchecked (`IllegalStateException`) |
| Thread interrupted | Checked (`InterruptedException`) — requires special handling |
| Class not found at runtime | Checked (`ClassNotFoundException`) |
| XML parsing error | Checked (`SAXException`, `ParserConfigurationException`) |
| Reflection failure | Checked (`ReflectiveOperationException`) |
| Serialization failure | Checked (`java.io.InvalidClassException`) |

## Real-World Scenarios

### Scenario 1: File Processing Service
**Decision: Use checked exceptions.**
- File I/O failures are external and expected.
- Callers can retry, skip, or use a fallback.
- The compiler ensures callers handle failures.

### Scenario 2: User Input Validation
**Decision: Use unchecked exceptions.**
- Invalid input is a programming error (the UI layer should validate).
- Callers cannot "recover" — they must fix the bug.
- Checked exceptions would clutter the API.

### Scenario 3: Database Access Layer
**Decision: Use checked exceptions, but wrap at the boundary.**
- `SQLException` is checked — external failure, recoverable.
- Wrap in a domain-specific unchecked exception (`DataAccessException`)
  to avoid leaking JDBC details through the service layer.

### Scenario 4: Configuration Loading
**Decision: Use unchecked exceptions.**
- Configuration errors are typically fatal and unrecoverable.
- Using checked exceptions forces every caller to handle them.
- Use `IllegalStateException` or a domain unchecked exception.

### Scenario 5: Third-Party Library Integration
**Decision: Follow the library's convention.**
- If the library uses checked exceptions, respect that convention.
- Wrap at the boundary if the checked exceptions leak implementation details.
- Document the checked exceptions in your API if you propagate them.

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Checked exception for external failure | Compiler forces handling; documents recovery options | Boilerplate; callers may wrap in unchecked anyway |
| Wrapping checked in unchecked at boundary | Cleaner downstream APIs; domain-focused exceptions | Callers may not handle; loses checked contract |
| Declaring `throws Exception` on all methods | Simple signature | Hides failure types; callers catch everything broadly |
| Using unchecked for all external failures | No boilerplate | Callers may forget to handle; silent failures |

## Common Code Review Comments

- "This `IOException` should be caught and wrapped in a domain exception at the service boundary."
- "Don't let `SQLException` leak through the service layer — translate it to `DataAccessException`."
- "Why does this method declare `throws Exception`? Narrow it to the specific types."
- "This checked exception is thrown from one place but caught nowhere — consider unchecked."
- "Don't catch checked exceptions just to rethrow as `RuntimeException` — that defeats the purpose."

## Common Production Mistakes

- **Swallowing checked exceptions in catch blocks**: A `FileNotFoundException` is caught and logged but the method continues with null — downstream NPEs are now harder to debug.
- **Wrapping every checked exception in RuntimeException at the boundary**: The entire service layer becomes unchecked — callers have no compile-time guidance on handling.
- **Changing checked to unchecked in a minor release**: Callers that relied on the `throws` clause now silently ignore failures — this is a breaking API change.
- **Not logging at the catch site**: Checked exceptions caught and rethrown without logging — the original stack trace is lost in production logs.

## When to Escalate

- You are designing a public API and deciding which methods throw checked exceptions — this is a permanent contract.
- A team is debating whether to switch from checked to unchecked for an existing exception hierarchy — this affects all consumers.
- You are integrating with a library that uses checked exceptions differently from your convention — the boundary design needs review.
