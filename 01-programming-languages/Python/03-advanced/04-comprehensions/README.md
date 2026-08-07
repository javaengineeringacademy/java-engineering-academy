# Comprehensions

When you need to create collections from iterables concisely and efficiently, comprehensions offer a readable syntax. Python's list, dict, set comprehensions and generator expressions transform and filter data with clear, expressive code.

## Overview

Comprehensions provide concise syntax for creating collections from iterables. They're faster and more readable than equivalent loops.

## When to Use

- Transforming collections
- Filtering data
- Creating dicts/sets from other data
- Replacing map/filter with clearer syntax

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| List comprehension | `comprehensions.py:5-19` | Filter, nested loops |
| Dict comprehension | `comprehensions.py:23-35` | Invert, filter |
| Set comprehension | `comprehensions.py:39-46` | Unique values |
| Generator expression | `comprehensions.py:50-53` | Lazy evaluation |
| Nested | `comprehensions.py:57-65` | Transpose, deep flatten |
| Walrus operator | `comprehensions.py:69-77` | := in comprehensions |
| Advanced patterns | `comprehensions.py:83-95` | chain, partition |

## Common Mistakes

1. **Overly complex comprehensions** — use a loop if > 2 conditions
2. **Using [] for large data** — use generator expression ()
3. **Side effects in comprehensions** — avoid print() or mutations
4. **Readability vs brevity** — prioritize clarity

## Interview Questions

1. What is the difference between a list comprehension and a generator expression?
2. How do you flatten a nested list with a comprehension?
3. When would you use map/filter over a comprehension?
4. What is the walrus operator and how does it help in comprehensions?

## Production Checklist

### ✅ Before using comprehensions in production:

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

### ❌ Myth 1: Comprehensions are always faster than loops
**Reality:** Comprehensions are faster due to optimized bytecode, but the difference is minimal.

### ❌ Myth 2: Nested comprehensions are always readable
**Reality:** Deeply nested comprehensions are hard to read; use regular loops instead.

### ❌ Myth 3: Generator expressions are always better
**Reality:** Lists are better when you need random access or multiple iterations.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Create collections concisely |
| Complexity | O(n) for creation |
| Thread Safe | Yes (comprehensions are stateless) |
| Best Alternative | Use map/filter for simple transformations |
| When to Use | Transforming, filtering, creating collections |
| When to Avoid | Complex logic, side effects, deep nesting |
