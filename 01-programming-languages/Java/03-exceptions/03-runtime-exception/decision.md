# Decision Tree: Which RuntimeException to Throw

Use this decision tree when you need to throw a `RuntimeException` and are not sure which subtype to use.

## Start Here

**Is the problem caused by a null reference?**
- Yes → `NullPointerException`
- No → continue

**Is the problem caused by an invalid argument passed to a method?**
- Yes → `IllegalArgumentException`
- No → continue

**Is the problem caused by the object being in the wrong state for the operation?**
- Yes → `IllegalStateException`
- No → continue

**Is the problem caused by an invalid index (array, list, string)?**
- Yes → `IndexOutOfBoundsException` (or `ArrayIndexOutOfBoundsException` / `StringIndexOutOfBoundsException`)
- No → continue

**Is the problem caused by an illegal arithmetic operation (division by zero)?**
- Yes → `ArithmeticException`
- No → continue

**Is the problem caused by an invalid type cast?**
- Yes → `ClassCastException`
- No → continue

**Is the problem caused by a string that cannot be parsed to a number?**
- Yes → `NumberFormatException`
- No → continue

**Is the problem caused by modifying a collection while iterating over it?**
- Yes → `ConcurrentModificationException`
- No → continue

**Is the problem caused by calling a method that the implementation does not support?**
- Yes → `UnsupportedOperationException`
- No → continue

**None of the above match?**
- Use a custom `RuntimeException` subclass with a descriptive name and message.

## Examples

### IllegalArgumentException Example

```java
public void setAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age must be non-negative, got: " + age);
    }
    this.age = age;
}
```

### IllegalStateException Example

```java
public void process() {
    if (!started) {
        throw new IllegalStateException("Cannot process before start() is called");
    }
    // processing logic
}
```

### IndexOutOfBoundsException Example

```java
public String getEntry(String[] array, int index) {
    if (index < 0 || index >= array.length) {
        throw new IndexOutOfBoundsException(
            "Index " + index + " out of bounds for length " + array.length);
    }
    return array[index];
}
```

## Quick Reference

| Situation                              | Exception                            |
|----------------------------------------|--------------------------------------|
| Null dereference                       | `NullPointerException`               |
| Bad method argument                    | `IllegalArgumentException`           |
| Wrong object state                     | `IllegalStateException`              |
| Bad index                              | `IndexOutOfBoundsException`          |
| Division by zero                       | `ArithmeticException`               |
| Invalid cast                           | `ClassCastException`                 |
| Unparseable number                     | `NumberFormatException`              |
| Collection modified during iteration   | `ConcurrentModificationException`   |
| Unsupported operation                  | `UnsupportedOperationException`      |
| Domain-specific bug                    | Custom `RuntimeException` subclass   |

## Decision Flow Diagram

```
Problem detected
    │
    ├─ Null reference? ──────────── NullPointerException
    │
    ├─ Bad argument? ────────────── IllegalArgumentException
    │
    ├─ Wrong state? ─────────────── IllegalStateException
    │
    ├─ Bad index? ───────────────── IndexOutOfBoundsException
    │
    ├─ Arithmetic error? ────────── ArithmeticException
    │
    ├─ Invalid cast? ────────────── ClassCastException
    │
    ├─ Bad number string? ───────── NumberFormatException
    │
    ├─ Concurrent modification? ─── ConcurrentModificationException
    │
    ├─ Unsupported operation? ───── UnsupportedOperationException
    │
    └─ None match? ──────────────── Custom RuntimeException subclass
```

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Using standard RuntimeExceptions | No custom classes; universal understanding | May not carry domain-specific context |
| Creating custom RuntimeException | Domain-specific semantics; typed catching | More classes to maintain; callers may not catch |
| IllegalArgumentException over custom | Familiar to all Java developers | Cannot distinguish between different invalid argument scenarios |
| IllegalStateException over custom | Clear "wrong state" semantics | Cannot distinguish between different state violations |

## Common Code Review Comments

- "Use `IllegalArgumentException`, not a custom exception — this is a standard argument validation."
- "Why `NullPointerException`? You should validate with `Objects.requireNonNull()` instead."
- "This should be `IllegalStateException` — the object is in the wrong state, not the caller's fault."
- "Don't throw raw `RuntimeException` — use a specific subtype so callers can catch selectively."
- "Add the actual value to the message: `"Expected positive, got: " + value`."

## Common Production Mistakes

- **Throwing NullPointerException manually**: Use `Objects.requireNonNull()` — it's idiomatic and conveys intent without custom messages.
- **Using IllegalArgumentException for state errors**: The distinction matters — `IllegalArgumentException` means "you gave me bad input"; `IllegalStateException` means "I'm not ready for this operation."
- **Swallowing IllegalArgumentException in validation**: Catching it silently and continuing with default values masks bugs that should fail fast.
- **Missing the actual value in the message**: `"Age must be positive"` is less useful than `"Age must be positive, got: -1"` — always include the offending value.

## When to Escalate

- You are designing a domain exception hierarchy with multiple `RuntimeException` subtypes — the naming and structure should be reviewed.
- A method throws `IllegalArgumentException` but the validation is complex and involves multiple fields — consider a validation exception instead.
- You need to decide between `IllegalStateException` and a domain-specific exception — the architect should define the convention for the team.
