# Module 05: Generics

## Overview

Generics enable you to write code that works with any object type while providing compile-time type safety. Introduced in Java 5, generics eliminate the need for explicit type casting and catch type mismatches at compile time rather than runtime.

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | Introduction | What generics are and why they matter |
| 02 | Generic Classes | Creating type-parameterized classes |
| 03 | Generic Methods | Writing methods with their own type parameters |
| 04 | Bounded Types | Restricting types with `extends` and `super` |
| 05 | Wildcards | Unknown type parameters with `?` |
| 06 | Type Erasure | How generics are implemented at the JVM level |
| 07 | Best Practices | Guidelines for effective generic code |
| 08 | Real-World | Industry patterns and production code |
| 09 | Mini Project | Hands-on application of all concepts |

## Learning Path

```
Introduction → Generic Class → Generic Method → Bounded Types
      ↓                                              ↓
 Type Erasure ← Wildcards ←──────────────────────────┘
      ↓
Best Practices → Real-World → Mini Project
```

## Prerequisites

- Solid understanding of OOP (inheritance, polymorphism)
- Familiarity with collections framework
- Basic understanding of interfaces and abstract classes

## Key Concepts

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

### Type Erasure

Generics are a compile-time feature. The JVM sees raw types:

```java
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();
// At runtime: both are ArrayList (raw type)
System.out.println(strings.getClass() == integers.getClass()); // true
```

## Module Structure

```
05-generics/
├── README.md
├── 01-introduction/
├── 02-generic-class/
├── 03-generic-method/
├── 04-bounded-types/
├── 05-wildcards/
├── 06-type-erasure/
├── 07-best-practices/
├── 08-real-world/
└── 09-mini-project/
```

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

## Estimated Time

- **Total:** 12-16 hours
- **Per topic:** 1.5-2 hours
- **Mini project:** 3-4 hours

## Resources

- [Oracle Generics Tutorial](https://docs.oracle.com/javase/tutorial/java/generics/)
- [Effective Java - Chapter on Generics](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Java Language Specification - Generics](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html)
