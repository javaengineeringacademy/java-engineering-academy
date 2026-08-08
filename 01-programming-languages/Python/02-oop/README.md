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
- Use magic methods for custom object behavior

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Complex domains, state management, polymorphic behavior | Simple scripts, pure functions |
| When NOT to use | Don't use classes for everything; prefer functions for simple logic | Use dataclasses for data-only containers |
| Alternatives | Dataclasses, Protocols, named tuples | Module-level functions |
| Production Examples | Web frameworks, ORMs, plugin systems | CLI tools, data pipelines |
| Common Mistakes | Over-using inheritance, forgetting `__hash__` with `__eq__` | Prefer composition over inheritance |

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
- Use magic methods for custom object behavior

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

## Production Incidents

### Incident 1: MRO Causing Infinite Recursion

**Problem:** Application crashed with RecursionError during initialization
**Cause:** Diamond inheritance with incompatible `super()` calls in metaclass hierarchy
**Impact:** Service unavailable for 30 minutes during peak load
**Detection:** Production logs showed stack overflow errors
**Solution:**
```python
# BAD: Uncooperative multiple inheritance
class Base:
    def process(self):
        return "base"
class Left(Base):
    def process(self):
        return f"left -> {super().process()}"  # Calls Right, not Base
class Right(Base):
    def process(self):
        return f"right -> {super().process()}"  # Calls Base

# GOOD: Cooperative design with *args, **kwargs
class Base:
    def process(self, **kwargs):
        return "base"
```
**Prevention:** Design classes to be cooperative; accept `*args, **kwargs`; check MRO with `Class.__mro__`

### Incident 2: Mutable Class Attribute Shared Across Instances

**Problem:** All User instances had the same `permissions` list
**Cause:** `permissions = []` defined at class level, not instance level
**Impact:** Permission escalation vulnerability in production
**Detection:** Security audit found all users had admin permissions
**Solution:**
```python
class User:
    def __init__(self, name):
        self.name = name
        self.permissions = []  # Instance attribute, not class attribute
```
**Prevention:** Initialize mutable attributes in `__init__`; use `dataclasses` which handles this correctly

### Incident 3: Property Override Breaking Encapsulation

**Problem:** Subclass accidentally overrode parent's `@property` without setter
**Cause:** Child class assigned to property without defining setter
**Impact:** AttributeError in production when updating user profile
**Detection:** Error monitoring caught AttributeError exceptions
**Solution:**
```python
class Base:
    @property
    def name(self):
        return self._name
    @name.setter
    def name(self, value):
        self._name = value

class Child(Base):
    @property
    def name(self):
        return super().name.upper()  # Getter only, no setter
```
**Prevention:** Always define both getter and setter when overriding properties; test property access patterns

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

## Related Topics

- [03-advanced](../03-advanced/) - Decorators and context managers
- [11-design-patterns](../11-design-patterns/) - Pythonic design patterns
- [17-metaclasses](../17-metaclasses/) - Advanced class customization

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Model entities with data and behavior |
| Complexity | O(1) attribute access; O(m) MRO lookup (m = hierarchy depth) |
| Thread Safe | No (use locks for shared mutable instances) |
| Best Alternative | Dataclasses for data-only classes, Protocols for duck typing |
| When to Use | Complex domains with state, polymorphic behavior, frameworks |
| When to Avoid | Simple scripts, performance-critical inner loops |

---

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| MRO causing infinite recursion | `inspect.getmro(Class)` or `Class.__mro__` | Check MRO order; ensure `super()` calls are cooperative with `*args, **kwargs` |
| Mutable class attribute shared across instances | `inspect.getmembers()` on class vs instance | Initialize mutable attributes in `__init__`; use `@dataclass` which handles this correctly |
| Property override breaking encapsulation | Test property access patterns in subclass | Always define both getter and setter when overriding properties |
| `__hash__` missing after defining `__eq__` | Add `__hash__` or set `__hash__ = None` | Define both `__eq__` and `__hash__` for hashable classes; set `__hash__ = None` for unhashable |
| Diamond inheritance confusion | `D.__mro__` to visualize resolution order | Design cooperative classes; accept `*args, **kwargs`; check MRO at class definition time |

## Code Review Checklist

- [ ] Classes have `__repr__` defined for debugging
- [ ] `__eq__` and `__hash__` defined together or `__hash__ = None` set
- [ ] Mutable attributes initialized in `__init__`, not at class level
- [ ] Properties have both getter and setter when overriding parent properties
- [ ] MRO checked for multiple inheritance hierarchies
- [ ] Composition preferred over deep inheritance chains
- [ ] Abstract methods enforced via ABC or Protocol, not just docstrings

## Architecture Considerations

OOP provides the structural foundation for modeling complex domains. Class hierarchies enable polymorphic behavior, but depth must be balanced against simplicity. Composition over inheritance creates loosely coupled systems that are easier to test and modify. The choice between ABCs and Protocols determines whether interfaces are enforced or structural.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Composition over inheritance | Domain models, plugin systems | Flexible but requires explicit delegation |
| ABC enforcement | Framework APIs, plugin contracts | Compile-time safety but adds coupling |
| Protocol structural typing | Duck typing with type safety | No inheritance required but no runtime enforcement |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Mutable class attribute privilege escalation | Permission corruption across instances | Initialize mutable attributes in `__init__`; use `@dataclass` |
| Property setter bypass in subclass | Encapsulation violated | Test property access in inheritance hierarchies; use `@property` with setters |
| `__new__` returning wrong type | Type confusion in object creation | Validate `__new__` return types; ensure `super().__new__()` is called |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Python 3.7+ | `@dataclass` for data containers | Replace manual `__init__`/`__repr__`/`__eq__` with `@dataclass` |
| Python 3.12+ | `type` parameter syntax for generics | Use `class Stack[T]` instead of `TypeVar` boilerplate |
| Python 3.12+ | `@override` decorator | Add to subclass methods that override parent for static checking |

## Version Validation

| Feature | Python Version | Status |
|---------|---------------|--------|
| `@dataclass` | 3.7+ | Stable, preferred for data containers |
| `__init_subclass__` | 3.6+ | Stable, alternative to metaclasses for subclass hooks |
| `Protocol` (PEP 544) | 3.8+ | Stable, structural subtyping |
| `type` parameter syntax | 3.12+ | Stable, cleaner generic syntax |
