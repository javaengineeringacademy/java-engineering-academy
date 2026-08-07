# Magic Methods

When you want your custom objects to work seamlessly with Python's built-in operations like printing, comparison, and iteration, magic methods are essential. Dunder methods let you define how objects behave with built-in functions and operators.

## Overview

Magic methods (double underscore methods) let you define how objects behave with built-in operations like printing, comparison, arithmetic, and iteration.

## When to Use

- Making custom objects work with built-in functions
- Overloading operators for custom types
- Implementing container protocols
- Creating context managers

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| __str__/__repr__ | `magic_methods.py:5-18` | String representations |
| Comparison | `magic_methods.py:22-42` | __eq__, __lt__, __hash__ |
| total_ordering | `magic_methods.py:46-57` | Auto-generate comparisons |
| Arithmetic | `magic_methods.py:61-88` | __add__, __sub__, __mul__ |
| Container | `magic_methods.py:92-113` | __len__, __getitem__, __iter__ |
| Context manager | `magic_methods.py:117-130` | __enter__, __exit__ |
| Callable | `magic_methods.py:134-142` | __call__ |

## Common Mistakes

1. **Defining __eq__ without __hash__** — breaks set/dict usage
2. **Forgetting __repr__** — makes debugging painful
3. **Not returning NotImplemented** — for unsupported operations
4. **Overusing magic methods** — keep it simple

## Interview Questions

1. What is the difference between __str__ and __repr__?
2. How do you make a class iterable?
3. What does __slots__ do?
4. How do context managers work with __enter__ and __exit__?

## Production Checklist

### ✅ Before using magic methods in production:

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

### ❌ Myth 1: Magic methods are always called automatically
**Reality:** Magic methods are only called by special syntax (e.g., `+` calls `__add__`).

### ❌ Myth 2: `__str__` is always called instead of `__repr__`
**Reality:** `__repr__` is called by the interpreter; `__str__` is called by `print()` and `str()`.

### ❌ Myth 3: Implementing `__eq__` is enough for hashing
**Reality:** If `__eq__` is defined, `__hash__` should also be defined or set to None.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Customize object behavior with built-in operations |
| Complexity | O(1) for most magic methods |
| Thread Safe | Yes (magic methods are stateless) |
| Best Alternative | Use built-in functions when possible |
| When to Use | Making objects work with built-in syntax |
| When to Avoid | Overusing magic methods, breaking expectations |
