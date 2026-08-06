# Encapsulation

Access control, name mangling, @property, and descriptors.

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
