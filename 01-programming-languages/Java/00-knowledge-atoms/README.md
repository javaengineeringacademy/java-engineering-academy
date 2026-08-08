# Module 00: Knowledge Atoms

> **Difficulty:** ⭐ Beginner  
> **Reading:** 20 min | **Practice:** 30 min | **Total:** 50 min

## Overview
Java knowledge atoms are fundamental building blocks that every Java developer must understand. These concepts—autoboxing, equals/hashCode, garbage collection, immutability, Java memory model, pass-by-value, and type safety—form the foundation for writing correct, efficient, and maintainable Java code. Understanding these atoms prevents common bugs and enables better design decisions.

## Learning Objectives
- Understand how autoboxing works and its performance implications
- Implement equals() and hashCode() correctly for consistent behavior
- Explain garbage collection mechanisms and tuning options
- Design immutable objects for thread safety and caching
- Apply pass-by-value semantics correctly in Java
- Ensure type safety through proper use of generics and collections

## Prerequisites
- Basic Java syntax
- Understanding of objects and references

## Topics

| # | Topic | Duration | Difficulty | Description |
|---|-------|----------|------------|-------------|
| 01 | [Autoboxing](autoboxing/) | 30 min | Beginner | Automatic conversion between primitives and wrapper classes |
| 02 | [Equals & HashCode](equals-hashcode/) | 45 min | Intermediate | Consistent object comparison and hashing |
| 03 | [Garbage Collection](garbage-collection/) | 60 min | Advanced | JVM memory management and GC tuning |
| 04 | [Immutability](immutability/) | 45 min | Intermediate | Creating immutable objects for thread safety |
| 05 | [Java Memory Model](java-memory-model/) | 60 min | Advanced | Memory visibility and happens-before relationships |
| 06 | [Pass by Value](pass-by-value/) | 30 min | Beginner | How Java passes arguments to methods |
| 07 | [Type Safety](type-safety/) | 30 min | Beginner | Compile-time type checking and generics |

## Production Notes
- **Where is it used?** In every Java application as foundational concepts
- **Why is it useful?** Prevents common bugs, improves performance, ensures correctness
- **When should it be avoided?** These concepts are always relevant
- **Alternative?** None; these are core Java concepts

## Core Concepts

### Knowledge Atom Summary
| Atom | Key Concept | Common Pitfall |
|------|-------------|----------------|
| Autoboxing | Auto-conversion between primitives and wrappers | Performance overhead in loops |
| Equals & HashCode | Consistent comparison and hashing | Inconsistent implementations |
| Garbage Collection | Automatic memory management | Memory leaks from strong references |
| Immutability | Objects that cannot change after creation | Not making all fields final |
| Memory Model | Thread visibility rules | Missing volatile/synchronization |
| Pass by Value | Copies of references, not references to references | Confusing with C++ pointers |
| Type Safety | Compile-time type checking | Using raw types |

## Production Incidents

### Incident 1: Autoboxing Memory Leak in Production Cache

**Problem:** A caching system using `HashMap<Integer, Object>` experienced gradual memory growth until OutOfMemoryError, despite having eviction policies.
**Cause:** Repeated autoboxing of `int` keys created new `Integer` objects that were never collected because they were referenced by the cache's internal structure.
**Impact:** Production cache server crashed every 48 hours, causing service degradation for 10,000+ users.
**Detection:** Heap dumps showed millions of `Integer` objects in the old generation with identical values.
**Solution:** Replaced `HashMap<Integer, Object>` with `IntObjectHashMap` from fastutil library, eliminating autoboxing overhead.
**Prevention:** Use primitive-specialized collections for high-frequency operations; profile memory usage in staging environments.

### Incident 2: Broken HashSet Due to Inconsistent equals/hashCode

**Problem:** A user management system failed to detect duplicate users because `HashSet.contains()` returned false for objects with identical data.
**Cause:** The `User` class overrode `equals()` but not `hashCode()`, violating the contract that equal objects must have equal hash codes.
**Impact:** Duplicate user accounts were created, causing data integrity issues and billing errors.
**Detection:** Customer support reported duplicate accounts; unit tests revealed the bug.
**Solution:** Added `hashCode()` implementation using `Objects.hash()` for all fields used in `equals()`.
**Prevention:** Always override both `equals()` and `hashCode()` together; use IDE-generated implementations or Lombok's `@EqualsAndHashCode`.

### Incident 3: GC Pause Causing Timeout in Trading System

**Problem:** A high-frequency trading application experienced periodic 2-3 second pauses, causing missed trade executions.
**Cause:** Default Parallel GC was performing full garbage collections on a 32GB heap, causing long stop-the-world pauses.
**Impact:** Trading system missed $2M in potential profits during peak market hours.
**Detection:** JFR recordings showed GC pauses correlating with missed trade windows.
**Solution:** Switched to ZGC with `-XX:+UseZGC -Xmx32g -Xms32g`, reducing max pause times to under 10ms.
**Prevention:** Profile GC behavior under production-like load; choose GC algorithm based on latency requirements.

## Production Checklist

- [ ] Override both equals() and hashCode() together when needed
- [ ] Use primitive-specialized collections for performance-critical code
- [ ] Configure appropriate GC algorithm for latency requirements
- [ ] Make immutable objects truly immutable (all fields final, no setters)
- [ ] Use volatile for flags accessed across threads
- [ ] Profile autoboxing in hot loops
- [ ] Test object equality contracts thoroughly
- [ ] Monitor GC logs in production
- [ ] Use JFR for low-overhead production profiling
- [ ] Document immutability guarantees in API contracts

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Understands basic concepts; uses autoboxing without thinking; implements equals/hashCode from IDE |
| Intermediate | Knows performance implications; creates simple immutable objects; understands GC basics |
| Advanced | Tunes GC for specific workloads; designs thread-safe immutable classes; uses primitive collections |
| Expert | Implements custom memory management; optimizes JVM for specific hardware; teaches these concepts |

## Common Myths

1. **Myth**: Java is pass-by-reference like C++
   **Truth**: Java is always pass-by-value. Object references are passed by value, meaning you can reassign the parameter but not the original reference.

2. **Myth**: Immutable objects are always slower
   **Truth**: Immutable objects enable caching, thread safety without synchronization, and can be faster due to JIT optimizations.

3. **Myth**: Garbage collection eliminates all memory leaks
   **Truth**: Strong references to unused objects, static collections, and listeners can prevent garbage collection, causing memory leaks.

4. **Myth**: Autoboxing is free
   **Truth**: Autoboxing creates new wrapper objects on each conversion, causing performance overhead and increased GC pressure in loops.

5. **Myth**: equals() and hashCode() are optional
   **Truth**: Overriding one without the other breaks collections like HashSet and HashMap, causing subtle bugs.

## Related Topics

- [Fundamentals](../01-fundamentals/README.md)
- [Collections](../04-collections/README.md)
- [JVM Internals](../10-jvm-internals/README.md)

## Next

- [Fundamentals](../01-fundamentals/README.md)

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Core Java concepts |
| Complexity | Varies |
| Thread Safe | Depends on implementation |
| Ordered | N/A |
| Allows Null | Depends on context |
| Best Alternative | N/A |
| When to Use | Always |
| When to Avoid | Never |
