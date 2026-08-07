# Encapsulation

When you need to protect internal state and validate data, encapsulation provides control over attribute access. Python uses naming conventions, name mangling, @property, and descriptors to manage access and maintain object integrity.

## Overview

Python uses naming conventions and name mangling for encapsulation. There's no true private access — it's "we're all consenting adults here."

## When to Use

- Protecting internal state from modification
- Validating data before setting attributes
- Creating read-only properties
- Optimizing memory with __slots__

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Name mangling | `encapsulation.py:4-30` | __pin, _balance |
| @property | `encapsulation.py:34-55` | Temperature with celsius/fahrenheit |
| __slots__ | `encapsulation.py:59-65` | Memory optimization |
| Read-only config | `encapsulation.py:69-83` | __getattr__, __setattr__ |
| Descriptors | `encapsulation.py:87-110` | Validated attribute |

## Common Mistakes

1. **Assuming _ means private** — it's just a convention
2. **Overusing name mangling** — makes testing harder
3. **Not using @property for validation** — allows invalid state
4. **Using __slots__ without planning** — prevents adding attributes

## Interview Questions

1. What is the difference between `_attr` and `__attr`?
2. How does @property work internally?
3. When would you use __slots__?
4. What is the descriptor protocol?

## Production Checklist

### ✅ Before using encapsulation in production:

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

### ❌ Myth 1: `_attr` makes attributes private
**Reality:** `_attr` is just a naming convention; Python has no true private access.

### ❌ Myth 2: `__slots__` always saves memory
**Reality:** `__slots__` saves memory by preventing `__dict__`, but prevents adding attributes.

### ❌ Myth 3: @property is always better than direct access
**Reality:** @property adds overhead; use it only when validation or computed values are needed.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Control access to object state |
| Complexity | O(1) for attribute access |
| Thread Safe | No (attribute access is not atomic) |
| Best Alternative | Use dataclasses with frozen=True |
| When to Use | Validating data, read-only properties |
| When to Avoid | Overusing name mangling, unnecessary validation |
