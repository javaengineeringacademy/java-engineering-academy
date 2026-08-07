# Variables & Types

When building software that handles diverse data types, you need a flexible way to manage values without rigid type declarations. Python's dynamic typing lets you assign any type to a variable and convert between types as needed, which speeds up development and reduces boilerplate.

## Overview

Python uses dynamic typing — variables are references to objects, not typed containers. The interpreter infers types at runtime.

## When to Use

- Storing data of any kind (numbers, text, collections)
- Passing values between functions
- Configuring application state

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Assignment & unpacking | `variables.py:1-18` | Multiple assignment, tuple unpacking |
| Built-in types | `variables.py:21-30` | Type inspection with `type()` |
| Type conversion | `variables.py:33-46` | `int()`, `float()`, `str()`, `bool()` |
| Naming conventions | `variables.py:52-58` | PEP 8 snake_case, constants |
| LEGB scope | `variables.py:61-74` | Global, enclosing, local scope |

## Common Mistakes

1. **Confusing `is` with `==`** — `is` checks identity, `==` checks equality
2. **Mutable default arguments** — `def f(x=[])` shares the list across calls
3. **Shadowing built-ins** — avoid names like `list`, `dict`, `int`
4. **Forgetting None is falsy** — `if x:` fails when `x` is `0` or `""`

## Interview Questions

1. What is the difference between `is` and `==`?
2. Explain Python's LEGB rule for variable scope.
3. Why are default mutable arguments dangerous?
4. What are mutable vs immutable types? Give examples.

## Production Checklist

### ✅ Before using variables in production:

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

### ❌ Myth 1: Python variables have fixed types
**Reality:** Python variables are references to objects, not typed containers. The object has a type, not the variable.

### ❌ Myth 2: `is` and `==` are interchangeable
**Reality:** `is` checks object identity (same memory address), `==` checks value equality.

### ❌ Myth 3: Global variables are always faster
**Reality:** Local variables are faster due to LOAD_FAST opcode vs LOAD_GLOBAL.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Store and reference data |
| Complexity | O(1) assignment and access |
| Thread Safe | Yes (assignment is atomic) |
| Best Alternative | Use constants for fixed values |
| When to Use | Storing any type of data |
| When to Avoid | Mutable defaults, shadowing built-ins |
