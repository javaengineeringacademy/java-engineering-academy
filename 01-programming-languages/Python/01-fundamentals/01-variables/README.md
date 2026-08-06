# Variables & Types

Python's dynamic typing system with built-in type conversions.

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
