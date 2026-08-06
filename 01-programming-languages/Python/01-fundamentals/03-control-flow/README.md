# Control Flow

Conditionals, loops, comprehensions, and pattern matching.

## Overview

Python provides `if`/`elif`/`else` for branching, `for`/`while` for iteration, and `match`/`case` (3.10+) for structural pattern matching.

## When to Use

- `if`/`elif`/`else` — branching logic
- `for` — iterating sequences, ranges, iterables
- `while` — repeating until a condition changes
- `match`/`case` — complex pattern matching, replacing long if/elif chains
- Comprehensions — concise data transformation

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| if/elif/else | `conditionals.py:5-18` | Grading example |
| Ternary | `conditionals.py:21-24` | Inline conditional |
| Truthy/Falsy | `conditionals.py:27-33` | Empty collections are falsy |
| match/case | `conditionals.py:37-51` | Command parsing |
| Guard clauses | `conditionals.py:55-64` | Early returns |
| For loop | `loops.py:4-11` | enumerate, zip |
| While loop | `loops.py:25-30` | Counter-based |
| Break/continue/else | `loops.py:34-42` | Prime check |
| Comprehensions | `loops.py:69-77` | List, dict, set |
| Generator expressions | `loops.py:81-82` | Lazy evaluation |

## Common Mistakes

1. **Modifying list while iterating** — use a copy or list comprehension
2. **Using `for` with `range(len())`** — use `enumerate()` instead
3. **Infinite while loops** — always ensure a break condition
4. **Mutating loop variable in `for`** — doesn't affect the iterable

## Interview Questions

1. What is the difference between `for` and `while`?
2. Explain the `else` clause on loops.
3. When would you use `match`/`case` over `if`/`elif`?
4. What are generator expressions vs list comprehensions?
