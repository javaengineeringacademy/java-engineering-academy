# Decision Tree: Extend Exception or RuntimeException

## When to Use Exception (Checked)

Extend `Exception` when the caller can reasonably recover from the failure.

```
Is the failure recoverable?
+-- Yes --> Can the caller do something about it?
|   +-- Yes --> Extend Exception (checked)
|   +-- No  --> Consider RuntimeException
+-- No  --> Is it a programming error?
    +-- Yes --> Extend RuntimeException (unchecked)
    +-- No  --> Extend Error (or RuntimeException)
```

## Comparison Matrix

| Factor | Exception (Checked) | RuntimeException (Unchecked) |
|---|---|---|
| Compile-time enforcement | Yes | No |
| Caller must handle | Yes (catch or throws) | No |
| Recovery possible | Yes | Usually not |
| Example | IOException, SQLException | NullPointerException, IllegalArgumentException |
| API contract | Explicit | Implicit |
| Boilerplate | Higher | Lower |

## Concrete Rules

### Use Exception (checked) when:

1. **I/O or network operations** - the caller can retry, show a message, or fall back
2. **Business rule violations** - the caller can correct input or take alternative action
3. **External system failures** - the caller can notify the user or queue for retry
4. **Resource unavailability** - the caller can wait or use a different resource

```java
public class ValidationException extends Exception {
    private final String field;

    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
    }
}
```

### Use RuntimeException (unchecked) when:

1. **Programming errors** - null arguments, array bounds, illegal state
2. **Invariants broken** - the code has a bug, not the caller
3. **Unrecoverable conditions** - the only option is to fix the code
4. **Internal API misuse** - misuse of an internal method by other code in the same class

```java
public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
```

## Decision Questions

Before creating a custom exception, ask:

1. **Can the caller recover?** If yes, checked. If no, unchecked.
2. **Is this a bug or a condition?** Bug means unchecked. Condition means checked.
3. **Do I need a throws declaration?** If it must be in the API, checked.
4. **Will callers commonly catch it?** If yes, checked gives them the compiler assist.
5. **Is this an edge case in normal flow?** If yes, checked. If it is a programming error, unchecked.

## Gray Areas

Some exceptions fall in between:

| Scenario | Recommendation |
|---|---|
| Cache miss | Unchecked (programming error if cache should have it) |
| Cache miss (intentional fallback) | Checked |
| Configuration error at startup | Unchecked (fail fast) |
| Configuration error at runtime | Checked (caller can reload) |
| Rate limiting | Checked (caller can retry) |
| Null parameter | Unchecked (programming error) |

## Summary

- Checked exceptions: the caller can and should handle the failure.
- Unchecked exceptions: the code is broken, fix it.
- When in doubt, lean toward unchecked -- checked exceptions add API surface area that cannot be removed without breaking backward compatibility.
