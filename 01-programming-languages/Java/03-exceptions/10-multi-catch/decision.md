# When to Use Multi-Catch — Decision Guide

## Quick Decision Tree

```
Do multiple exceptions need the SAME handling?
├─ YES → Are the exceptions semantically related?
│        ├─ YES → Use multi-catch
│        └─ NO  → Consider separate catch blocks (clarity)
└─ NO  → Use separate catch blocks
```

## Use Multi-Catch When

- Multiple exception types require **identical** recovery logic.
- The exceptions are **related** (e.g., all I/O, all parsing, all network).
- You want to **reduce duplication** without losing type specificity.
- The catch block is **short** (logging, rethrowing, wrapping).

## Avoid Multi-Catch When

- Each exception needs **different handling**.
- You must call **type-specific methods** (`getSQLState()`, `getErrorCode()`).
- Catching together would **mislead** readers about the exception relationship.
- The group includes **unrelated** exceptions (`IOException | NullPointerException`).

## The Rule of Three

If you see three or more identical catch blocks, multi-catch is almost always
the right refactor. Two identical blocks may be fine as-is if they are simple.

## Red Flags

| Pattern | Problem |
|---------|---------|
| `catch (A \| B \| C e)` with `instanceof` checks inside | Should be separate blocks |
| Catching `Exception \| Error` together | Too broad — catches fatal errors |
| Multi-catch followed by silent `return` | Swallowing exceptions |
| Mixing checked and unrelated unchecked exceptions | Misleading grouping |

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Multi-catch for related exceptions | Reduced duplication; cleaner code | Cannot access type-specific methods on the caught exception |
| Separate catch blocks | Type-specific handling; full API access | Verbose; code duplication for identical logic |
| Multi-catch with instanceof inside | Single block, type-aware logic | Ugly; defeats the purpose of multi-catch |
| Catching too many types in one block | Concise | May hide unrelated bugs; harder to reason about |

## Common Code Review Comments

- "These two catch blocks do the same thing — use multi-catch."
- "You're catching `IOException | NullPointerException` together — these are unrelated."
- "If you need `instanceof` checks inside the catch, use separate catch blocks instead."
- "Don't catch `Exception | Error` — that's too broad; catch specific types."
- "Multi-catch is for when the handling is identical — if it's not, keep them separate."

## Common Production Mistakes

- **Grouping unrelated exceptions**: `catch (IOException | IllegalArgumentException e)` — if the handling differs, this hides bugs and makes debugging harder.
- **Using multi-catch when type-specific methods are needed**: `catch (SQLException | IOException e)` — then calling `e.getSQLState()` fails at runtime for `IOException`.
- **Silently swallowing multi-catch exceptions**: `catch (A | B e) { log.error("...", e); }` — if recovery differs for A and B, this is a bug.
- **Multi-catch hiding missing handling**: Compiler doesn't check exhaustive handling for unchecked types in multi-catch — you may miss a new subtype.

## When to Escalate

- You are designing exception handling for a pipeline that processes multiple exception types — the grouping strategy needs review.
- Multi-catch is used in a critical path where different exceptions require different recovery — the design needs architectural input.
- A team adopts inconsistent multi-catch conventions — the architect should establish guidelines.
