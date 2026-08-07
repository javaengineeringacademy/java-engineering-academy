# Abstraction

When you need to define interfaces that must be implemented and enforce contracts between components, abstraction is key. Python uses abstract base classes, interfaces, and plugin patterns to hide implementation details behind clean interfaces.

## Overview

Abstraction hides implementation details behind a clean interface. Python uses ABC (Abstract Base Classes) to define interfaces.

## When to Use

- Defining interfaces that must be implemented
- Building plugin/extension systems
- Enforcing contracts between components
- Creating framework APIs

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| ABC basics | `abstraction.py:7-24` | @abstractmethod |
| Concrete classes | `abstraction.py:28-44` | GasCar, ElectricCar |
| Abstract property | `abstraction.py:48-67` | Database interface |
| Virtual subclass | `abstraction.py:71-81` | Serializer.register() |
| Plugin system | `abstraction.py:85-110` | Registry pattern |

## Common Mistakes

1. **Forgetting @abstractmethod** — method won't be required
2. **Instantiating abstract class** — raises TypeError
3. **Overusing ABC** — use duck typing when possible
4. **Not calling super().__init__()** — parent won't initialize

## Interview Questions

1. What is an abstract base class?
2. How do you register a virtual subclass?
3. What is the difference between ABC and Protocol?
4. When would you use abstraction vs duck typing?

## Production Checklist

### ✅ Before using abstraction in production:

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

### ❌ Myth 1: Abstract classes can't have implementations
**Reality:** Abstract classes can have concrete methods alongside abstract methods.

### ❌ Myth 2: ABC is always better than duck typing
**Reality:** Duck typing is more flexible; ABC is better for strict interface enforcement.

### ❌ Myth 3: Abstract methods must be decorated
**Reality:** `@abstractmethod` ensures subclasses implement the method; without it, it's optional.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Define interfaces that must be implemented |
| Complexity | O(1) for method dispatch |
| Thread Safe | Yes (abstract methods are stateless) |
| Best Alternative | Use Protocol for structural typing |
| When to Use | Enforcing contracts, building frameworks |
| When to Avoid | Simple duck typing, overengineering |
