# Functions

When you need to organize code into reusable blocks, pass data between them, and extend behavior without modifying original code, functions are essential. Python's first-class functions support definitions, arguments, closures, and decorators that enable modular, maintainable, and flexible code.

## Overview

Functions are first-class objects in Python — they can be assigned to variables, passed as arguments, and returned from other functions.

## When to Use

- Code reuse and organization
- Encapsulating logic
- Creating decorators and higher-order functions
- Building APIs and libraries

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Basic function | `functions.py:5-9` | def, return |
| Default args | `functions.py:13-16` | title="Mr." |
| *args/**kwargs | `functions.py:20-25` | Flexible arguments |
| Keyword-only | `functions.py:29-32` | After * |
| Multiple return | `functions.py:37-40` | Tuple unpacking |
| First-class | `functions.py:45-49` | Passing functions |
| Lambda | `functions.py:53-57` | Anonymous functions |
| Closures | `functions.py:61-71` | nonlocal, captured state |
| Decorator | `functions.py:75-87` | @timer |
| Type hints | `functions.py:91-101` | typing module |

## Common Mistakes

1. **Mutable default arguments** — `def f(x=[])` shares the list across calls
2. **Using `return` in a generator** — use `yield` instead
3. **Forgetting `self`** in class methods
4. **Overusing lambdas** — use `def` for complex logic

## Interview Questions

1. What are *args and **kwargs?
2. Explain closures and the `nonlocal` keyword.
3. Why are mutable default arguments dangerous?
4. What is the difference between a generator and a regular function?

## Production Checklist

### ✅ Before using functions in production:

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

### ❌ Myth 1: Lambda functions are always faster
**Reality:** Lambdas have the same performance as regular functions; the difference is syntactic, not performance.

### ❌ Myth 2: `*args` and `**kwargs` are always interchangeable
**Reality:** `*args` passes positional arguments as a tuple, `**kwargs` passes keyword arguments as a dict.

### ❌ Myth 3: Default arguments are evaluated at call time
**Reality:** Default arguments are evaluated once at function definition time, which causes mutable default issues.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Encapsulate reusable logic |
| Complexity | O(1) call overhead |
| Thread Safe | Yes (functions are stateless) |
| Best Alternative | Use classes for stateful behavior |
| When to Use | Code reuse, organization, abstraction |
| When to Avoid | Mutable defaults, overusing lambdas |
