# Exception Best Practices — Decision Guide

Use this decision tree when you encounter a situation that requires exception handling.

## Quick Reference

```
Is this a programming bug (null, bad argument, failed assertion)?
  YES → throw unchecked (IllegalArgumentException, IllegalStateException)
  NO  ↓

Is this an external, recoverable failure (file not found, network timeout)?
  YES → declare checked exception, let caller decide
  NO  ↓

Is this a business rule violation (insufficient funds, duplicate order)?
  YES → throw domain-specific unchecked exception with error code
  NO  ↓

Is this at an infrastructure boundary (servlet, scheduler entry point)?
  YES → catch broadest type, log, return generic error
  NO  ↓

Follow the specific type hierarchy below
```

## Exception Type Selection

| Situation | Exception Type | Checked? |
|-----------|---------------|----------|
| Null argument | NullPointerException | No |
| Invalid argument | IllegalArgumentException | No |
| Illegal state | IllegalStateException | No |
| Feature not supported | UnsupportedOperationException | No |
| File not found | FileNotFoundException | Yes |
| SQL failure | SQLException | Yes |
| I/O failure | IOException | Yes |
| Business rule violation | DomainException | No |
| Validation error | ValidationException | No |
| Not found | NotFoundException | No |
| Conflict/duplicate | ConflictException | No |

## When to Create a Custom Exception

- Multiple callers need to catch the same error type differently
- You need to carry domain-specific data (error codes, status codes)
- The exception represents a meaningful concept in your domain
- You want to distinguish between different failure modes of the same operation

## When NOT to Create a Custom Exception

- A standard exception (IllegalArgumentException, IllegalStateException) fits
- The exception only appears in one place with no callers catching it
- The custom type adds no information beyond its name

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Domain-specific exceptions | Typed catching; rich context; clear semantics | More classes; maintenance overhead |
| Using standard exceptions universally | Fewer classes; familiar to all developers | Cannot carry domain-specific data |
| Wrapping at boundaries | Clean layer APIs; hides implementation | Extra exception objects; potential for double-wrapping |
| Catching broad Exception at entry points | Catches everything; prevents thread death | Hides bugs; may swallow programming errors |

## Common Code Review Comments

- "Don't catch `Exception` here — you'll swallow unchecked bugs."
- "Log the exception at the point of catching, not at the point of re-throwing."
- "This exception has no message — add context so we can debug it in production."
- "Don't use exceptions for control flow — validate with `if` checks."
- "Your custom exception should extend `RuntimeException` if it's only thrown internally."

## Common Production Mistakes

- **Empty catch blocks**: `catch (Exception e) { }` — the failure is silently swallowed; the system continues in an undefined state.
- **Logging and rethrowing**: `log.error("...", e); throw e;` — duplicate log entries for the same exception; choose one.
- **Using exceptions for control flow**: Throwing and catching exceptions for expected conditions — destroys performance and hides real bugs.
- **Missing exception messages**: `throw new IllegalArgumentException()` with no message — debugging requires reading the source code.
- **Catching Throwable in application code**: Catches JVM errors that should propagate — masks fatal failures.

## When to Escalate

- You are establishing exception handling conventions for a new team or project — the architect should define the standards.
- A production incident reveals systemic exception handling issues — the architect should review the strategy.
- You are designing error responses for a public API — the error contract needs architectural review.
