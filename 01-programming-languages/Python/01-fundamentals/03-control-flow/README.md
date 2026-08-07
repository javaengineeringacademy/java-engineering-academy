# Control Flow

To make decisions and repeat actions in your programs, you need control flow structures. Python offers conditionals, loops, comprehensions, and pattern matching that let you branch logic, iterate over sequences, transform data concisely, and match complex patterns.

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

## Production Checklist

### ✅ Before using control flow in production:

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

### ❌ Myth 1: `for` loops are always slower than comprehensions
**Reality:** Comprehensions are faster due to optimized bytecode, but the difference is minimal for small datasets.

### ❌ Myth 2: `while True` always creates an infinite loop
**Reality:** `while True` with proper break conditions is a common and valid pattern.

### ❌ Myth 3: `match`/`case` is just syntactic sugar for if/elif
**Reality:** Pattern matching can destructure data and bind variables, which if/elif cannot do.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Control program execution flow |
| Complexity | O(n) for loops, O(1) for conditionals |
| Thread Safe | Yes (control flow is atomic) |
| Best Alternative | Use functional approaches (map/filter) |
| When to Use | Iterating, branching, repeating logic |
| When to Avoid | Overly complex nested structures |
