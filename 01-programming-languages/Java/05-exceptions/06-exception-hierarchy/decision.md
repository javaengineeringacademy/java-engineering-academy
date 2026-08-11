# Decision Framework: When to Use Which Exception Level

## 1. Decision Tree

```
Is the failure recoverable?
├── Yes → Can the caller handle it?
│   ├── Yes → Use CHECKED Exception
│   └── No → Use UNCHECKED Exception
└── No → Is it a programming bug?
    ├── Yes → Use RuntimeException
    └── No → Use Error
```

## 2. Checked vs Unchecked Decision

| Question | Answer | Recommendation |
|----------|--------|----------------|
| Can the caller reasonably recover? | Yes | Checked exception |
| Is it a programming error? | Yes | Unchecked exception (RuntimeException) |
| Does it indicate a JVM failure? | Yes | Error |
| Should the caller be forced to handle it? | Yes | Checked exception |
| Is it a systemic failure? | Yes | Error |

## 3. Custom Exception Decision

| Scenario | Exception Level |
|----------|----------------|
| Business logic violation | Checked exception |
| Invalid input | Unchecked (IllegalArgumentException) |
| Resource not found | Checked exception |
| Configuration error | Unchecked (IllegalStateException) |
| External service failure | Checked exception |
| Null pointer | Unchecked (NullPointerException) |

## 4. Catching Exceptions

- **Catch specific**: Always catch the most specific exception type first.
- **Catch general**: Catch `Exception` only as a last resort, never catch `Throwable`.
- **Don't swallow**: Always log or re-throw exceptions.
- **Don't catch Error**: Let JVM handle `Error` subclasses (e.g., `OutOfMemoryError`).

## 5. Exception Hierarchy Design

```
DomainException (checked)
├── UserException
├── PaymentException
├── OrderException
└── InventoryException

DomainRuntimeException (unchecked)
├── UserRuntimeException
├── PaymentRuntimeException
├── OrderRuntimeException
└── InventoryRuntimeException
```

## 6. Key Principles

1. **Separation of concerns**: Keep checked and unchecked exceptions separate.
2. **Self-documenting**: Exception names should describe the failure.
3. **Layered design**: Each layer can translate exceptions to its own types.
4. **Fail-fast**: Throw early, catch late.
5. **Preserve cause**: Always chain exceptions with the original cause.

## 7. Common Anti-Patterns

| Anti-Pattern | Problem | Solution |
|--------------|---------|----------|
| Catching `Exception` | Hides specific failures | Catch specific exceptions |
| Empty catch block | Swallows errors | Log or re-throw |
| Catching `Throwable` | Catches `Error` too | Catch `Exception` |
| Not chaining | Loses cause | Use `throw new X(cause)` |
| Catching `RuntimeException` | Hides bugs | Validate inputs first |
| Overusing checked | Boilerplate | Use unchecked where appropriate |
