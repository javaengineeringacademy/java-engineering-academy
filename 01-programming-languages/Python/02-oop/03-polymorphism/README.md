# Polymorphism

When you need to write functions that work with multiple types and customize object behavior, polymorphism is essential. Python uses duck typing, method overriding, and operator overloading to let objects of different types be used through a uniform interface.

## Overview

Polymorphism allows objects of different types to be used through a uniform interface. Python uses duck typing — behavior matters, not type.

## When to Use

- Writing functions that work with multiple types
- Building plugin/extension systems
- Overloading operators for custom types
- Creating polymorphic collections

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Duck typing | `polymorphism.py:4-22` | No shared base required |
| Method overriding | `polymorphism.py:26-48` | Shape.area() |
| Built-in polymorphism | `polymorphism.py:52-56` | len() on any type |
| Operator overloading | `polymorphism.py:60-77` | __add__, __mul__ |
| Polymorphic collections | `polymorphism.py:81-99` | Notification broadcast |

## Common Mistakes

1. **Checking types instead of behavior** — use duck typing, not isinstance
2. **Not implementing __repr__** — makes debugging hard
3. **Overusing isinstance** — breaks polymorphism
4. **Forgetting NotImplemented** — return it from unsupported operations

## Interview Questions

1. What is duck typing?
2. How does operator overloading work in Python?
3. What is the difference between polymorphism and inheritance?
4. How do you make a class iterable?

## Production Checklist

### ✅ Before using polymorphism in production:

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

### ❌ Myth 1: Polymorphism requires inheritance
**Reality:** Python's duck typing means any object with the right methods works, regardless of inheritance.

### ❌ Myth 2: Operator overloading makes code harder to read
**Reality:** Well-designed operator overloading makes code more intuitive (e.g., `+` for addition).

### ❌ Myth 3: isinstance() is always necessary for type checking
**Reality:** Duck typing is preferred; isinstance() breaks polymorphism.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Write code that works with multiple types |
| Complexity | O(1) for method dispatch |
| Thread Safe | Yes (method calls are atomic) |
| Best Alternative | Use protocols for structural typing |
| When to Use | Writing generic functions, operator overloading |
| When to Avoid | Overusing isinstance(), breaking duck typing |
