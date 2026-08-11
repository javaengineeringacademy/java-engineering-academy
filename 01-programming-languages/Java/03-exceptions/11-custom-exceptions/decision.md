# Custom Exceptions: Decision Guide

## Checked vs Unchecked

```
Is the error recoverable by the caller?
├── Yes → Checked (extends Exception)
│   └── Can caller reasonably handle it?
│       ├── Yes → Checked
│       └── No  → Unchecked
└── No  → Unchecked (extends RuntimeException)
```

## When to Create a Custom Exception

| Scenario | Custom Exception? | Why |
|----------|-------------------|-----|
| User not found | Yes | Domain-specific, callers need typed handling |
| Invalid input | No | Use `IllegalArgumentException` |
| Payment declined | Yes | Carries payment-specific data |
| Null argument | No | Use `NullPointerException` |
| Network timeout | No | Use `SocketTimeoutException` |
| Business rule violation | Yes | Expresses domain constraint |
| API contract error | Yes | Documents public API errors |

## Naming Checklist

- [ ] Suffix with `Exception`
- [ ] Descriptive, not generic
- [ ] Not `Error` suffix (reserved for JVM issues)

## Constructors Checklist

- [ ] `MyException(String message)`
- [ ] `MyException(String message, Throwable cause)`
- [ ] `MyException(Throwable cause)`
- [ ] Domain-specific fields constructor (if needed)
- [ ] `serialVersionUID` (for checked exceptions)

## Hierarchy Depth

| Project Size | Recommended Depth |
|--------------|-------------------|
| Small (< 5 exceptions) | Flat |
| Medium (5-15 exceptions) | 2 levels |
| Large (> 15 exceptions) | 3 levels max |

## Red Flags

1. Creating an exception for every tiny error variation
2. Catching custom exceptions too broadly
3. Using exceptions for normal flow control
4. Losing the original cause when wrapping
5. Missing `serialVersionUID` on checked exceptions

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Custom exception with domain data | Rich context for callers; typed catching | More classes; serialization concerns |
| Reusing standard exceptions | No custom classes; familiar to all | Cannot carry domain-specific data |
| Deep custom hierarchy | Fine-grained catching; clear semantics | Maintenance burden; fragile to changes |
| Flat custom hierarchy | Simple; few classes | Cannot distinguish failure subtypes |

## Common Code Review Comments

- "Why does this custom exception have no `serialVersionUID`? It's `Serializable`."
- "Add the three standard constructors — `message`, `cause`, and `message + cause`."
- "Don't add fields to a checked exception without considering serialization."
- "This custom exception is only used once — use `IllegalArgumentException` instead."
- "Name it `PaymentDeclinedException`, not `PaymentError` — suffix with `Exception`."

## Common Production Mistakes

- **Missing serialVersionUID**: Serializing a checked exception without `serialVersionUID` — deserialization fails on version mismatch in distributed systems.
- **Missing cause constructor**: No `MyException(String, Throwable)` constructor — callers cannot chain the original exception, losing the root cause.
- **Too many custom exceptions**: One exception per error code — catch blocks become unwieldy; consider grouping with a base type.
- **Using `Error` suffix**: `PaymentError` instead of `PaymentException` — `Error` implies JVM-level failure; misuse confuses callers.

## When to Escalate

- You are designing a custom exception hierarchy for a new service — the naming, constructors, and checked/unchecked decision should be reviewed.
- A custom exception needs to carry complex domain data (error codes, status codes) — the serialization strategy should be reviewed.
- You are creating an exception hierarchy shared across multiple teams — the contract needs agreement.
