# Module 06: Generics

> **Difficulty:** ⭐⭐⭐ Intermediate  
> **Reading:** 30 min | **Practice:** 45 min | **Total:** 75 min

## Overview

Without generics, every collection stores Objects and requires manual casting at retrieval — a source of runtime ClassCastException bugs that compilers can't catch. Generics let you write a single class or method that works with any type while catching type mismatches at compile time. Introduced in Java 5, they eliminated explicit casting and made APIs like the Collections Framework type-safe.

## Why This Concept Exists

Without generics:
- Code must use Object and cast everywhere
- No compile-time type checking
- Runtime ClassCastException possible
- Code duplication for each type

With generics:
- Single codebase for all types
- Compile-time type safety
- No casting required
- Better code readability

## Learning Objectives

By the end of this module, you will be able to:

- Write generic classes and methods that work with any type while catching errors at compile time
- Restrict type parameters using bounded types to call specific methods on generic arguments
- Apply the PECS principle (Producer Extends, Consumer Super) to design flexible APIs
- Avoid common type erasure pitfalls that cause runtime surprises
- Design type-safe APIs that eliminate explicit casting in client code

## Topics

| # | Topic | Duration | Difficulty | Description |
|---|-------|----------|------------|-------------|
| 01 | [Generic Types](01-generic-types/) | 1 hour | Beginner | What generics are and why they matter |
| 02 | [Generic Methods](02-generic-methods/) | 2 hours | Beginner | Creating type-parameterized methods |
| 03 | [Bounded Type Parameters](03-bounded-type-parameters/) | 2 hours | Intermediate | Restricting types with `extends` and `super` |
| 04 | [Wildcards](04-wildcards/) | 2 hours | Intermediate | Unknown type parameters with `?` |
| 05 | [Type Erasure](05-type-erasure/) | 2 hours | Advanced | How generics are implemented at the JVM level |
| 06 | [Generics and Inheritance](06-generics-inheritance-subtypes/) | 1.5 hours | Intermediate | Subtyping rules with generics |
| 07 | [Type Inference](07-type-inference/) | 1.5 hours | Intermediate | Diamond operator and var |
| 08 | [Restrictions on Generics](08-restrictions-generics/) | 1 hour | Intermediate | Limitations and workarounds |
| 09 | [Best Practices](09-best-practices/) | 1.5 hours | Intermediate | Guidelines for effective generic code |
| 10 | [Raw Types](10-raw-types/) | 1 hour | Beginner | Legacy code and backward compatibility |
| 11 | [Erasure of Generic Types](11-erasure-generic-types/) | 2 hours | Advanced | Bytecode transformation details |
| 12 | [Erasure of Generic Methods](12-erasure-generic-methods/) | 2 hours | Advanced | Bridge methods and runtime behavior |

**Total Estimated Time: 18-20 hours**

## Prerequisites

- Solid understanding of OOP (inheritance, polymorphism)
- Familiarity with collections framework
- Basic understanding of interfaces and abstract classes

## History

- **1995** — Java 1.0 used raw collections (no type safety) because generics were not yet available, leading to runtime ClassCastException risks
- **1998** — Java 1.2 introduced Collections Framework with `Object`-based types to provide a unified collections architecture, but lacked compile-time type safety
- **2004** — Java 5 introduced generics to enable compile-time type safety, eliminating explicit casting and catching type errors at compile time
- **2004** — Java 5 added generic interfaces, methods, and bounded types to provide flexible, type-safe code reuse across different data types
- **2011** — Java 7 added diamond operator (`<>`) to reduce boilerplate by inferring generic type arguments from context
- **2014** — Java 8 improved type inference in lambdas and method references to simplify functional programming with generics
- **2016** — Java 9 added `var` for local variable type inference to reduce verbosity while maintaining type safety (indirectly related to generics)
- **2021** — Java 17 continued type system refinements to improve developer experience and catch more errors at compile time

## Learning Path

```
Generic Types → Generic Methods → Bounded Type Parameters
      ↓                                    ↓
 Type Erasure ← Wildcards ←────────────────┘
      ↓
Best Practices → Restrictions → Raw Types → Erasure Details
```

## Difficulty Progression

- **Beginner** (Topics 01-02, 10): Basic concepts and syntax
- **Intermediate** (Topics 03-04, 06-09): Advanced features and patterns
- **Advanced** (Topics 05, 11-12): JVM internals and bytecode transformation

## Quick Reference

| Feature | Syntax | Example |
|---------|--------|---------|
| Generic Class | `class Name<T>` | `class Box<T>` |
| Generic Method | `<T> T method(T param)` | `<T> List<T> asList(T a)` |
| Bounded Type | `<T extends Upper>` | `<T extends Comparable<T>>` |
| Wildcard | `<?>` | `List<?>` |
| Upper Bounded | `<? extends T>` | `List<? extends Number>` |
| Lower Bounded | `<? super T>` | `List<? super Integer>` |
| Type Erasure | Removed at compile time | `List<String>` → `List` |

## Core Concepts

### Type Safety at Compile Time

```java
// Without generics - runtime ClassCastException possible
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0); // Manual cast required

// With generics - compile-time error
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0); // No cast needed
list.add(42); // Compile-time error!
```

### Bounded Types

```java
// Upper bound - T must be Number or subclass
public <T extends Number> double sum(List<T> list) {
    return list.stream()
        .mapToDouble(Number::doubleValue)
        .sum();
}

// Multiple bounds
public <T extends Comparable<T> & Serializable> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}
```

### Wildcards

```java
// Upper bounded - read-only
public double sum(List<? extends Number> list) {
    return list.stream()
        .mapToDouble(Number::doubleValue)
        .sum();
}

// Lower bounded - write-only
public void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
}

// Unbounded - read-only Object
public void printList(List<?> list) {
    list.forEach(System.out::println);
}
```

### Type Erasure

Generics are a compile-time feature. The JVM sees raw types:

```java
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();
// At runtime: both are ArrayList (raw type)
System.out.println(strings.getClass() == integers.getClass()); // true
```

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Using raw types | Loses type safety | Always use `List<String>` not `List` |
| `new T()` | Compile error — type erasure | Pass `Class<T>` and use `clazz.getDeclaredConstructor()` |
| `List<String>` not assignable to `List<Object>` | Generics are invariant | Use wildcards: `List<?>` |
| Unchecked cast warning | Potential `ClassCastException` | Use `@SuppressWarnings("unchecked")` only when safe |
| `instanceof List<String>` | Compile error — type erasure | Use `instanceof List<?>` |

## Cross-References

- **Previous Module:** [05 - Text Processing](../05-text-processing/)
- **Next Module:** [07 - Functional Programming](../07-functional-programming/)
- **Related:** [02 - OOP](../02-oop/) — inheritance and polymorphism
- **Related:** [04 - Collections](../04-collections/) — parameterized collection types
- **Related:** [07 - Functional Programming](../07-functional-programming/) — generic functional interfaces

## Resources

- [Oracle Generics Tutorial](https://docs.oracle.com/en/java/javase/21/java/generics/)
- [Effective Java - Chapter on Generics](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Java Language Specification - Generics](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html)
