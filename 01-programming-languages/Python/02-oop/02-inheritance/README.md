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

## Production Incidents

### Incident 1: MRO Issue Causing Method Resolution Failure

**Problem:** `super()` calls skip expected classes in the hierarchy, causing methods to not execute in the expected order or not at all.

**Cause:** Incorrect method resolution order (MRO) due to complex inheritance hierarchies. `super()` follows the MRO, not the direct parent class, leading to unexpected behavior when classes are added or reordered.

**Impact:** Parent class initialization is skipped. Business logic that depends on method call order breaks silently. Bugs appear only when specific class combinations are used.

**Detection:** Print `ClassName.__mro__` to verify resolution order. Add logging at the start of `__init__` methods to trace execution order.

**Solution:** Keep inheritance hierarchies shallow. Explicitly call parent methods when MRO-based resolution is confusing:
```python
class Child(ParentA, ParentB):
    def __init__(self):
        super().__init__()  # follows MRO: Child → ParentA → ParentB
        ParentB.__init__(self)  # explicit call if needed
```

** Prevention:** Prefer composition over deep inheritance. Use `__mro__` inspection in tests to verify expected resolution order.

---

### Incident 2: Diamond Inheritance Problem

**Problem:** Multiple parent classes with a common ancestor cause duplicate initialization, leading to resource leaks or inconsistent state.

**Cause:** Class C inherits from both A and B, which both inherit from D. Without proper `super()` usage, D's `__init__` is called multiple times.

**Impact:** Database connections opened multiple times. File handles leaked. Memory consumption doubles. Inconsistent state from partial initialization.

**Detection:** Add logging in `__init__` to track how many times a base class is initialized. Use `id()` to check if objects are the same instance.

**Solution:** Use cooperative multiple inheritance with `super()` throughout the hierarchy:
```python
class D:
    def __init__(self):
        print("D init")

class A(D):
    def __init__(self):
        super().__init__()  # calls D
        print("A init")

class B(D):
    def __init__(self):
        super().__init__()  # calls D
        print("B init")

class C(A, B):
    def __init__(self):
        super().__init__()  # follows MRO: C → A → B → D (D called once)
```

** Prevention:** Use `@dataclass` with `frozen=True` for value objects. Apply composition over multiple inheritance for complex hierarchies.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Create specialized classes from base classes |
| Complexity | O(1) for method resolution |
| Thread Safe | No (shared class state) |
| Best Alternative | Use composition for has-a relationships |
| When to Use | Is-a relationships, shared behavior |
| When to Avoid | Deep hierarchies, code reuse only |
