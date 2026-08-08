# Object-Oriented Programming

## Why OOP Matters

Every complex application needs to organize code into manageable, reusable components. Object-oriented programming in Python provides a way to model real-world entities and bundle data with behavior using classes, inheritance, polymorphism, encapsulation, and abstraction. Without OOP, managing large codebases becomes chaotic and error-prone.

Without OOP principles, you'd end up with scattered functions and global state that's hard to debug and impossible to extend. That's why OOP exists — it provides the structural foundation for building maintainable, scalable applications where components can be reused, extended, and tested in isolation.

## What You'll Learn

By the end of this module, you'll be able to:

- Design and implement classes with proper OOP principles
- Use inheritance and polymorphism to build flexible hierarchies
- Control access to internal state with encapsulation
- Define abstract interfaces with the ABC module
- Leverage magic methods for custom object behavior

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | Classes | Defining classes, __init__, instance methods, dataclasses |
| 02 | Inheritance | Single/multiple inheritance, super(), MRO |
| 03 | Polymorphism | Method overriding, duck typing, abstract classes |
| 04 | Encapsulation | Private attributes, properties, name mangling |
| 05 | Abstraction | ABC module, abstract methods, interfaces |
| 06 | Magic Methods | __repr__, __str__, __eq__, __hash__, operator overloading |

## Prerequisites

- Python Fundamentals (01-fundamentals)

## Interview Questions

### Q1: What is the difference between `__str__` and `__repr__`?
**Answer:** `__str__` is for human-readable output (print, str()), `__repr__` is for unambiguous representation (repr(), interactive shell). Always implement `__repr__`.

### Q2: Explain method resolution order (MRO).
**Answer:** MRO determines method lookup order in inheritance. Python uses C3 linearization. Check with Class.__mro__. Super() follows MRO.

### Q3: What is the difference between class and instance attributes?
**Answer:** Class attributes are shared across all instances. Instance attributes are unique to each object. Instance attributes shadow class attributes.

### Q4: How do properties work internally?
**Answer:** Properties use descriptor protocol. @property creates a descriptor with __get__, __set__, __delete__ that intercepts attribute access.

### Q5: What is the diamond problem and how does Python solve it?
**Answer:** Diamond problem occurs with multiple inheritance. Python uses MRO (C3 linearization) to determine method lookup order.

## Learning Objectives

By the end of this module you will be able to:

- Design and implement classes with proper OOP principles
- Use inheritance and polymorphism to build flexible hierarchies
- Control access to internal state with encapsulation
- Define abstract interfaces with the ABC module
- Leverage magic methods for custom object behavior

## Quick Start

```bash
# Run any topic directly
python 01-classes/classes.py
python 02-inheritance/inheritance.py
python 03-polymorphism/polymorphism.py
python 04-encapsulation/encapsulation.py
python 05-abstraction/abstraction.py
python 06-magic-methods/magic_methods.py
```

## Production Checklist

### ✅ Before using OOP in production:

☐ I know the time/space complexity of class instantiation and MRO
☐ I know common mistakes (mutable class attributes, MRO issues, forgetting __hash__ with __eq__)
☐ I know alternatives (dataclasses, named tuples, modules-as-namespaces)
☐ I know limitations (multiple inheritance pitfalls, memory overhead per instance)
☐ I know how to debug it (__repr__, inspect module, debugger breakpoints)
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: Everything must be a class
**Reality:** Python supports functional programming; plain functions and modules often suffice. Use classes when you need state and behavior bundled together.

### ❌ Myth 2: Python has true private variables
**Reality:** Name mangling (_ClassName__attr) is a convention, not enforcement. True encapsulation relies on discipline and documentation.

### ❌ Myth 3: Inheritance is always the best way to reuse code
**Reality:** Composition is often preferred—avoid deep inheritance hierarchies. Use mixins or delegation to reduce coupling.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Model entities with data and behavior |
| Complexity | O(1) attribute access; O(m) MRO lookup (m = hierarchy depth) |
| Thread Safe | No (use locks for shared mutable instances) |
| Best Alternative | Dataclasses for data-only classes, Protocols for duck typing |
| When to Use | Complex domains with state, polymorphic behavior, frameworks |
| When to Avoid | Simple scripts, performance-critical inner loops |
