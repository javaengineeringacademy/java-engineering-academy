# Object-Oriented Programming - Decision Guide

## Key Decisions in OOP Design

### Class vs Interface
- **Use concrete classes** when you need state, implementation, or concrete behavior
- **Use interfaces** when defining contracts, enabling polymorphism across unrelated types
- **Use abstract classes** when subclasses share common code but need different implementations

### Inheritance vs Composition
- **Favor composition over inheritance** (HAS-A over IS-A)
- **Use inheritance** when there is a clear "is-a" relationship and the subclass is a specialization
- **Use composition** when you want to reuse behavior without tight coupling

### Encapsulation Strategy
- **Private fields + getters/setters** for mutable data
- **Private final fields + getters only** for immutable objects
- **Package-private** when classes are tightly coupled within the same module

### Record vs Traditional Class
- **Records** for immutable data carriers (DTOs, value objects, tuples)
- **Traditional classes** when you need mutable state, complex logic, or inheritance hierarchies

### Sealed Classes
- Use sealed classes with pattern matching when the set of subtypes is fixed at compile time
- Combine with records for exhaustive switch expressions

### Enums
- **Simple enums** for fixed sets of constants
- **Enum with fields/methods** when each constant has behavior or associated data
- **EnumSet/EnumMap** for high-performance enum-keyed collections

### Inner Classes
- **Static nested classes** when the inner class doesn't need access to the outer instance
- **Non-static inner classes** when it needs access to outer instance state (e.g., iterators)
- **Local classes** rarely; prefer lambdas for one-off implementations
- **Anonymous classes** for inline implementations of interfaces

### Object Lifecycle
- Initialize with constructors; use factory methods for complex construction
- Override `equals()` and `hashCode()` together when objects are used in collections
- Implement `Cloneable` cautiously; prefer copy constructors or static factory methods
