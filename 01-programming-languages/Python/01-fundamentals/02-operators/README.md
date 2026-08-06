# Operators

Arithmetic, comparison, logical, bitwise, and membership operators.

## Overview

Python provides rich operator support including short-circuit evaluation, chaining comparisons, and membership tests.

## When to Use

- Arithmetic: mathematical computations
- Comparison: conditionals, filtering, sorting
- Logical: combining boolean expressions
- Bitwise: flag manipulation, low-level operations
- Membership: checking containment in collections

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Arithmetic | `operators.py:5-13` | +, -, *, /, //, %, ** |
| Comparison chaining | `operators.py:17-19` | 1 < x < 10 |
| Logical short-circuit | `operators.py:23-28` | and, or, not |
| Bitwise ops | `operators.py:32-39` | &, \|, ^, ~, <<, >> |
| Identity | `operators.py:43-49` | is, is not |
| Membership | `operators.py:53-56` | in, not in |
| Precedence | `operators.py:67-80` | Full precedence table |

## Common Mistakes

1. **`/` vs `//`** — `/` returns float, `//` floors
2. **`is` for value comparison** — use `==` for values, `is` for None/singletons
3. **Ignoring precedence** — use parentheses for clarity
4. **Short-circuit gotchas** — `and`/`or` return the operand, not bool

## Interview Questions

1. What is the difference between `/` and `//`?
2. Explain short-circuit evaluation in `and`/`or`.
3. What does `or` return when the first operand is falsy?
4. Why should you use `is None` instead of `== None`?
