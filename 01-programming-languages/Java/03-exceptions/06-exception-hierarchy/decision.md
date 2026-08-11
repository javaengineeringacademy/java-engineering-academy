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

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Deep exception hierarchy | Fine-grained catching; domain clarity | More classes; maintenance overhead |
| Flat exception hierarchy | Simple; easy to maintain | Cannot distinguish failure types at catch site |
| Separate checked/unchecked hierarchies | Clear semantic boundary | Two parallel hierarchies to maintain |
| Single hierarchy (all unchecked) | Simple; clean APIs | No compiler enforcement; callers may ignore |

## Common Code Review Comments

- "Your hierarchy is too deep — do you really need `PaymentDeclinedException` AND `PaymentFailedException`?"
- "These two exception classes have the same body — merge them."
- "Don't catch `Exception` — catch the specific type from the hierarchy."
- "Why is this exception checked? It's thrown from internal code."
- "Add a base exception for this domain so callers can catch broadly if needed."

## Common Production Mistakes

- **Inconsistent hierarchy design**: Some exceptions extend `Exception`, others `RuntimeException` in the same domain — callers cannot predict what to catch.
- **Too-deep hierarchy**: Five levels of exception inheritance — catch blocks become complex and brittle to hierarchy changes.
- **Catching at the wrong level**: Catching `DataAccessException` when the code should catch `DataOptimisticLockException` — hides concurrency bugs.
- **Not providing a base type for a domain**: Each exception is independent — callers that want to catch all domain exceptions must list every type.

## When to Escalate

- You are designing the exception hierarchy for a new domain — the structure should be reviewed for naming, depth, and checked vs unchecked decisions.
- A refactoring changes the hierarchy depth or base types — this affects all callers across the system.
- You are merging two systems with different exception hierarchies — the combined design needs architectural review.
