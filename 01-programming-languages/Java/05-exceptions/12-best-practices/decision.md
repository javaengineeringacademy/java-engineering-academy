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
