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
