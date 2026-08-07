# Inheritance

When you need to reuse code and build hierarchies of related classes, inheritance is a key technique. Python supports single, multiple inheritance, method resolution order (MRO), and composition that let you share behavior between classes and manage complex relationships.

## Overview

Inheritance enables code reuse by creating new classes from existing ones. Python supports single, multiple, and multilevel inheritance.

## When to Use

- Sharing behavior between related classes
- Implementing polymorphic interfaces
- Building class hierarchies (is-a relationship)
- Use composition (has-a) when inheritance doesn't fit

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Single inheritance | `inheritance.py:4-26` | super().__init__ |
| Method override | `inheritance.py:30-32` | Redefining parent method |
| Multiple inheritance | `inheritance.py:36-48` | Diamond pattern |
| MRO | `inheritance.py:52-65` | __mro__, C3 linearization |
| isinstance/issubclass | `inheritance.py:69-74` | Type checking |
| Composition | `inheritance.py:78-88` | has-a vs is-a |
| ABC interface | `inheritance.py:92-112` | Abstract base classes |

## Common Mistakes

1. **Deep inheritance chains** — prefer composition over inheritance
2. **Not calling super().__init__()** — parent won't be initialized
3. **Diamond problem** — Python resolves via MRO, but be explicit
4. **Using inheritance for code reuse** — use mixins or composition

## Interview Questions

1. What is MRO and how does Python resolve it?
2. What is the difference between `isinstance` and `type`?
3. When would you use composition over inheritance?
4. Explain the diamond problem and how Python handles it.

## Production Checklist

### ✅ Before using inheritance in production:

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

### ❌ Myth 1: Inheritance always promotes code reuse
**Reality:** Deep inheritance hierarchies create tight coupling; composition is often better.

### ❌ Myth 2: Multiple inheritance is always dangerous
**Reality:** Python's MRO (C3 linearization) safely resolves diamond problems.

### ❌ Myth 3: `super()` always calls the parent class
**Reality:** `super()` calls the next class in the MRO, which may not be the direct parent.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Create specialized classes from base classes |
| Complexity | O(1) for method resolution |
| Thread Safe | No (shared class state) |
| Best Alternative | Use composition for has-a relationships |
| When to Use | Is-a relationships, shared behavior |
| When to Avoid | Deep hierarchies, code reuse only |
