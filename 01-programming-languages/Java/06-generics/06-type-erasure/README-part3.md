# 06 - Type Erasure (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

```

---

## Comparison Table

| Feature | Generic Code | After Erasure |
|---------|--------------|---------------|
| Type parameters | `<T>` | `Object` or bound |
| Method signatures | `T get()` | `Object get()` |
| Field types | `T value` | `Object value` |
| Casts | None needed | Compiler-inserted |
| Bridge methods | Not present | Added by compiler |
| instanceof | Can't use with `T` | Can use with raw type |
| Arrays | Can't create generic | Can create raw arrays |

---

## Decision Tree

```
Do you need runtime type information?
├── No → Use generics normally (erasure is fine)
└── Yes → What type of runtime info?
    ├── Class type → Use Class<T> token
    ├── Parameterized type → Use TypeToken or reflection
    ├── Array creation → Use Array.newInstance()
    └── Instance creation → Use Class<T>.newInstance()
```

---

## Interview Questions

### Q1: What is type erasure in Java?

**A:** Type erasure is the process where the compiler removes all generic type information at compile time, replacing type parameters with their bounds (or `Object`). This ensures backward compatibility but means generic types are not available at runtime.

### Q2: Why does Java use type erasure?

**A:** For backward compatibility with pre-Java 5 code. Raw types (`List`) and parameterized types (`List<String>`) produce identical bytecode, allowing old code to work with new generic code.

### Q3: What can't you do because of type erasure?

**A:** Cannot use `instanceof` with parameterized types, create generic arrays, use `new T()`, `T.class`, or overload methods with different generic signatures.

### Q4: How can you get runtime type information?

**A:** Use `Class<T>` tokens, `TypeToken<T>` pattern, or reflection APIs like `getGenericSuperclass()`, `getGenericType()`, etc.

### Q5: Do generics have runtime overhead?

**A:** No. Type erasure means generic code produces identical bytecode to raw types. The only overhead is compile-time type checking and bridge method generation, which has negligible runtime cost.

---

## Exercises

### Exercise 1: Type Erasure Demonstration

Create a program that demonstrates type erasure by:
1. Comparing `getClass()` of different generic types
2. Using reflection to inspect erased types
3. Showing bridge method generation

### Exercise 2: TypeToken Implementation

Implement a `TypeToken<T>` class that captures generic type information at runtime.

### Exercise 3: Generic Array Creation

Create a utility method to create generic arrays using reflection.

---

## Assignments

### Assignment 1: Type-Safe Reflection Utility

Create a `TypeSafeReflection` utility class that:
1. Safely gets generic type parameters
2. Creates instances of generic types
3. Accesses fields with type safety
4. Handles type erasure gracefully

### Assignment 2: Generic Builder with Runtime Type

Create a generic builder that:
1. Uses type tokens for runtime type info
2. Validates types at build time
3. Supports complex generic types
4. Handles type erasure properly

---

## Mini Project

### Type-Safe Serialization Framework

Build a serialization framework that:
1. Uses type tokens to preserve generic type information
2. Serializes/deserializes generic types correctly
3. Handles type erasure gracefully
4. Supports complex nested generic types

**Key classes:**
- `TypeToken<T>` — captures generic type info
- `TypeSafeSerializer` — serializes with type safety
- `TypeSafeDeserializer` — deserializes with type safety

---

## Summary

Type erasure is a fundamental aspect of Java generics:

1. **Compile-time feature** — Generic types erased at compile time
2. **Backward compatibility** — Allows pre-Java 5 code to work
3. **No runtime overhead** — Identical performance to raw types
4. **Limitations** — Cannot use instanceof, create arrays, etc.
5. **Workarounds** — Type tokens, reflection, class parameters

Understanding type erasure is essential for writing effective generic code and avoiding common pitfalls.

---

## References

- [Oracle - Type Erasure](https://docs.oracle.com/en/java/javase/21/java/generics/erasure.html)
- [Java Language Specification §4.6 - Type Erasure](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.6)
- [Effective Java - Item 33: Use tokens to pass class literals at runtime](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Angelika Langer - Type Erasure FAQ](https://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeErasure.html)
```
