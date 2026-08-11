# Decision Guide: Checked vs Unchecked Exceptions

Use this guide to decide whether a condition should be represented by a checked
or unchecked exception.

---

## Quick Decision Tree

```
Is this a programming error (bug)?
├── YES → Use unchecked exception
│         (NullPointerException, IllegalArgumentException, etc.)
└── NO
    ├── Can the caller reasonably recover?
    │   ├── YES → Use checked exception
    │   └── NO  → Use unchecked exception
    └── Is it an external/system failure?
        ├── YES → Use checked exception (IOException, SQLException)
        └── NO  → Use unchecked exception
```

---

## When to Use Unchecked

| Scenario                              | Example Exception                  |
|---------------------------------------|-------------------------------------|
| Null reference dereference            | `NullPointerException`             |
| Invalid method argument               | `IllegalArgumentException`        |
| Wrong object state for operation      | `IllegalStateException`            |
| Array/list index out of bounds        | `IndexOutOfBoundsException`        |
| Failed type cast                      | `ClassCastException`               |
| Number parsing failure                | `NumberFormatException`            |
| Arithmetic error (division by zero)   | `ArithmeticException`              |
| Collection modified during iteration  | `ConcurrentModificationException`  |
| Domain rule violation                 | Custom `DomainException`           |

---

## When to Use Checked

| Scenario                              | Example Exception                  |
|---------------------------------------|-------------------------------------|
| File not found or I/O failure         | `IOException`                      |
| Database access failure               | `SQLException`                     |
| Network unavailable                   | `SocketException`                  |
| XML/JSON parsing failure              | `ParseException`                   |
| Class not found                       | `ClassNotFoundException`           |
| Thread interruption                   | `InterruptedException`            |

---

## Design Principles

1. **Fail fast.** Validate inputs at method boundaries using unchecked
   exceptions. Let invalid states be caught immediately rather than propagated.

2. **Recoverable = checked.** If a caller has a meaningful recovery strategy
   (retry, prompt user, use fallback), a checked exception forces them to handle
   it.

3. **Bug = unchecked.** If the condition indicates the code itself is wrong,
   an unchecked exception signals "fix the code" rather than "handle the error."

4. **Minimal API surface.** Unchecked exceptions do not appear in method
   signatures, keeping APIs clean.

5. **Avoid mixing.** Do not mix checked and unchecked exceptions for the same
   condition. Choose one strategy and be consistent.

---

## Common Anti-Patterns

| Anti-Pattern                                      | Problem                                    |
|---------------------------------------------------|--------------------------------------------|
| Catching `Exception` broadly                      | Swallows unchecked bugs silently           |
| Using exceptions for control flow                 | Slow, hides bugs, unclear intent           |
| Declaring unchecked in `throws`                   | Clutters API, unusual convention           |
| Wrapping every exception in `RuntimeException`    | Loses original exception type and context  |
| Catching `Error`                                  | JVM errors are rarely recoverable          |

---

## Checklist Before Choosing

- [ ] Does the condition represent a bug in the code?
- [ ] Can the caller recover without changing program logic?
- [ ] Is the failure caused by external resources (file, network, DB)?
- [ ] Would catching this exception improve reliability?
- [ ] Is the exception specific enough to be useful?

Answer these questions and refer to the decision tree above to make your choice.
