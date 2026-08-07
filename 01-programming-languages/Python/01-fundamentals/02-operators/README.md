# Operators

When writing expressions and making decisions in code, you need a variety of operators to manipulate data and control logic. Python provides arithmetic, comparison, logical, bitwise, and membership operators that let you perform calculations, evaluate conditions, combine boolean expressions, and test membership efficiently.

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

## Production Checklist

### ✅ Before using operators in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands thread safety

### Level 3: Deep Knowledge
- Knows internal implementation
- Understands edge cases

### Level 4: Expert
- Can optimize for specific use cases
- Can explain trade-offs

### Level 5: Master
- Can debug in production
- Can design custom implementations

## Common Myths

### ❌ Myth 1: `or` always returns True/False
**Reality:** `or` returns the first truthy operand or the last operand, not a boolean.

### ❌ Myth 2: `/` always returns float
**Reality:** `/` returns float in Python 3, but `//` floors toward negative infinity, not toward zero.

### ❌ Myth 3: Bitwise operators are only for low-level programming
**Reality:** Bitwise ops are useful for flags, permissions, and performance-critical code.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Perform operations on values |
| Complexity | O(1) for most operators |
| Thread Safe | Yes (operators are atomic) |
| Best Alternative | Use functions for complex operations |
| When to Use | Basic arithmetic, comparisons, logic |
| When to Avoid | Complex expressions without parentheses |
