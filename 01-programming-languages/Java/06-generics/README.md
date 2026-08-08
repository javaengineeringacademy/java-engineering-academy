# Module 05: Generics

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
| 01 | [Introduction](01-introduction/) | 1 hour | Beginner | What generics are and why they matter |
| 02 | [Generic Classes](02-generic-class/) | 2 hours | Beginner | Creating type-parameterized classes |
| 03 | [Generic Methods](03-generic-method/) | 2 hours | Intermediate | Writing methods with their own type parameters |
| 04 | [Bounded Types](04-bounded-types/) | 2 hours | Intermediate | Restricting types with `extends` and `super` |
| 05 | [Wildcards](05-wildcards/) | 2 hours | Intermediate | Unknown type parameters with `?` |
| 06 | [Type Erasure](06-type-erasure/) | 2 hours | Advanced | How generics are implemented at the JVM level |
| 07 | [Best Practices](07-best-practices/) | 1.5 hours | Intermediate | Guidelines for effective generic code |
| 08 | [Real-World](08-real-world/) | 2 hours | Advanced | Industry patterns and production code |
| 09 | [Mini Project](09-mini-project/) | 3 hours | Advanced | Hands-on application of all concepts |

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

## Production Notes
- **Where is it used?** In all Java applications that require type-safe collections, methods, and classes
- **Why is it useful?** Provides compile-time type safety, eliminates explicit casting, and enables code reuse across different data types
- **When should it be avoided?** For simple, single-type scenarios where raw types are sufficient; overuse can lead to complex type parameters and readability issues
- **Alternative?** Raw types (not recommended), method overloading, or Object with casting (less safe)

## Learning Path

```
Introduction → Generic Class → Generic Method → Bounded Types
      ↓                                              ↓
 Type Erasure ← Wildcards ←──────────────────────────┘
      ↓
Best Practices → Real-World → Mini Project
```

## Difficulty Progression

- **Beginner** (Topics 01-02): Basic concepts and syntax
- **Intermediate** (Topics 03-07): Advanced features and patterns
- **Advanced** (Topics 08-09): Real-world applications and projects

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

## Internal Working

### Type Erasure Process
1. **Compile time** — Compiler checks all generic type constraints
2. **Erasure** — Generic type parameters are replaced with their bounds (or `Object` if unbounded)
3. **Bridge methods** — Compiler generates bridge methods to preserve polymorphism
4. **Cast insertion** — Compiler inserts necessary casts in bytecode

### What Gets Erased
- Type parameters → `Object` or upper bound
- Generic type in `instanceof` → compile-time error (not reifiable)
- Generic array creation → compile-time error
- Exception type parameters → compile-time error

### What's Preserved
- `extends` bound in bytecode
- Generic signatures for reflection (`getGenericSuperclass()`)
- Bridge methods for covariant return types

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│              Generic Type Hierarchy                      │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │              Generic Class<T>                    │   │
│  │                  (Source Code)                   │   │
│  └──────────────────────┬──────────────────────────┘   │
│                         │                               │
│                    [Type Erasure]                       │
│                         │                               │
│  ┌──────────────────────▼──────────────────────────┐   │
│  │            Raw Class (JVM Bytecode)             │   │
│  │              Object (T → Object)                │   │
│  └─────────────────────────────────────────────────┘   │
│                                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │          Bounded Type<T extends X>              │   │
│  │              (Source Code)                       │   │
│  └──────────────────────┬──────────────────────────┘   │
│                         │                               │
│                    [Type Erasure]                       │
│                         │                               │
│  ┌──────────────────────▼──────────────────────────┐   │
│  │            Raw Class (JVM Bytecode)             │   │
│  │              X (T → upper bound)                │   │
│  └─────────────────────────────────────────────────┘   │
│                                                         │
└─────────────────────────────────────────────────────────┘
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

- **Total:** 18-20 hours
- **Per topic:** 1.5-3 hours
- **Mini project:** 3-4 hours

## Resources

- [Oracle Generics Tutorial](https://docs.oracle.com/en/java/javase/21/java/generics/)
- [Effective Java - Chapter on Generics](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Java Language Specification - Generics](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html)

## Performance Comparison

| Operation | Time Complexity | Space | Notes |
|-----------|----------------|-------|-------|
| Generic class instantiation | O(1) | Type parameter | Compile-time only |
| Type erasure | O(1) | None | Happens at compile time |
| Bounded type check | O(1) | None | Compile-time check |
| Wildcard capture | O(1) | None | Compile-time only |
| Generic method call | O(1) | Type inference | Compile-time only |

## Examples

### 1. Builder Pattern with Generics
```java
public class Builder<T> {
    private T value;
    
    public Builder<T> with(T value) {
        this.value = value;
        return this;
    }
    
    public T build() {
        return value;
    }
}
```

### 2. Generic Repository
```java
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(T entity);
}
```

### 3. Type-Safe Heterogeneous Container
```java
public class TypeSafeContainer {
    private final Map<Class<?>, Object> map = new HashMap<>();
    
    public <T> void put(Class<T> type, T value) {
        map.put(type, type.cast(value));
    }
    
    public <T> T get(Class<T> type) {
        return type.cast(map.get(type));
    }
}
```

## Interview Questions

### Q1: What is type erasure?
**Answer:** Type erasure removes generic type information at compile time, converting List<String> to List. This ensures backward compatibility with pre-generics code.

### Q2: Can you create a generic array?
**Answer:** No, you cannot create `new T[]` due to type erasure. Use `Array.newInstance()` or `Object[]` with casting.

### Q3: What is the PECS principle?
**Answer:** Producer Extends, Consumer Super. Use `? extends T` when producing data, `? super T` when consuming.

### Q4: Can you overload methods with different generic types?
**Answer:** No, due to type erasure both methods have the same signature at runtime.

### Q5: What is a reified type?
**Answer:** A type whose type information is available at runtime. Generics are not reified (erased), but arrays and primitives are.

## Best Practices

**Do's:**
- Use bounded types (`<T extends Comparable<T>>`) to constrain generic types
- Prefer `List<? extends T>` for read-only access (Producer Extends)
- Prefer `List<? super T>` for write access (Consumer Super)
- Use type wildcards for flexibility in method parameters
- Reuse compiled `Pattern` objects for regex

**Don'ts:**
- Don't use raw types — always parameterize
- Don't create `new T()` or `new T[]` — type erasure prevents this
- Don't use `List<Object>` when you mean `List<?>` or `List<String>`
- Don't ignore unchecked cast warnings — suppress with `@SuppressWarnings` only when justified
- Don't use wildcards in return types — use concrete generic types

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
- **External:** [Oracle Generics Tutorial](https://docs.oracle.com/en/java/javase/21/java/generics/)
- **External:** [Effective Java - Chapter on Generics](https://learning.oreilly.com/library/view/effective-java/9780134686097/)

---

**Note:** This module contains detailed documentation with 27 sections per topic, including theory, examples, best practices, interview questions, exercises, and assignments.

## Prerequisites

- [OOP](../02-oop/README.md)

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| ClassCastException at runtime | Stack trace + type erasure awareness | Identify line causing cast; check if generic type info was erased; verify raw types |
| Unchecked cast warning suppressed | Compiler warnings review | Search for `@SuppressWarnings("unchecked")`; verify type safety at call sites |
| Wildcard capture issues | Type inference analysis | Use helper methods for wildcard capture; verify PECS principle application |
| Generic array creation error | Refactor to `Object[]` or `Class<T>` | Replace `new T[]` with `Array.newInstance()` or `Object[]` with casting |
| Type mismatch in generic method | IDE type inference | Use IDE autocomplete to verify type inference; add explicit type arguments |

## Code Review Checklist

- [ ] No raw types — all generics parameterized
- [ ] Bounded types used (`<T extends Comparable<T>>`) where appropriate
- [ ] Wildcards used correctly (Producer Extends, Consumer Super)
- [ ] `@SuppressWarnings("unchecked")` only with documented justification
- [ ] No `new T()` or `new T[]` (type erasure prevents this)
- [ ] Generic methods have proper type inference
- [ ] PECS principle applied to collection method parameters

## Architecture Considerations

Generics enable type-safe APIs that scale across teams. At scale, well-designed generic APIs reduce runtime errors and improve developer productivity. For library/framework authors, generic type design is an architectural decision — bounded types constrain usage, wildcards enable flexibility, and type erasure determines what's possible at runtime.

In large codebases, generic type consistency prevents subtle bugs. For example, a `Repository<T, ID>` pattern must ensure type safety across all layers. For distributed systems, type erasure means serialization frameworks need explicit type tokens (`TypeToken`, `Class<T>`) to reconstruct generic types at deserialization.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Bounded type parameters | Type-safe algorithms | Pros: Compile-time safety, method access; Cons: Complexity, verbosity |
| Wildcard capture helper | Flexible API design | Pros: Handles complex variance; Cons: Harder to understand |
| Type-safe heterogeneous container | Mixed-type storage with type safety | Pros: Runtime type safety; Cons: Complexity, casting overhead |
| Generic factory method | Object creation with type inference | Pros: Clean API; Cons: Type inference complexity |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Unchecked cast leading to ClassCastException | Runtime crashes, data corruption | Never suppress unchecked warnings without verification; use `TypeToken` pattern |
| Type erasure bypassing runtime checks | Security bypass, invalid state | Validate types at runtime when security-critical; use `Class.cast()` |
| Raw type usage in security contexts | Type confusion, injection attacks | Enforce parameterized types in security-sensitive code |
| Generic type information leakage | Information disclosure via reflection | Avoid exposing generic type info in public APIs; use wildcards |
| Unsafe deserialization of generic types | Remote code execution | Validate type tokens; use `ObjectInputFilter` for deserialization |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0–1.4 | Raw types, no generics | Migrate all raw types to parameterized types |
| Java 5 | Generics, bounded types, wildcards | Adopt generics throughout; eliminate explicit casting |
| Java 7 | Diamond operator | Simplify generic constructor calls |
| Java 8 | Improved type inference in lambdas | Use method references; let compiler infer types |
| Java 9 | `var` for local variables | Use `var` for obvious generic types |
| Java 10 | Local variable type inference | Combine with generics for cleaner code |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Basic generics | Java 5 | Stable |
| Bounded types (`extends`, `super`) | Java 5 | Stable |
| Wildcards (`? extends`, `? super`) | Java 5 | Stable |
| Diamond operator (`<>`) | Java 7 | Stable |
| Type inference improvements | Java 8 | Stable |
| `var` with generic types | Java 10 | Stable |

## Production Incidents

### Incident 1: ClassCastException from Raw Type Usage

**Problem:** A data migration tool crashed with `ClassCastException` when processing records from legacy database.
**Cause:** Raw `ArrayList` was used instead of `ArrayList<Record>`; casting `Object` to `Record` failed at runtime.
**Impact:** Migration failed for 50,000 records; required manual intervention; delayed project by 2 days.
**Detection:** `ClassCastException` in stack trace; legacy code used raw types.
**Solution:** Added generic type parameter `ArrayList<Record>` and removed explicit casting.
**Prevention:** Never use raw types; enable compiler warnings for raw type usage; add type safety checks in code review.

### Incident 2: Unchecked Cast Warning Causing Runtime Error

**Problem:** A generic utility method threw `ClassCastException` intermittently when processing different data types.
**Cause:** Unchecked cast from `Object` to generic type `T` without proper type checking; compiler warning was suppressed.
**Impact:** Application crashed for specific input combinations; 10% of requests affected; customer complaints.
**Detection:** `ClassCastException` with no obvious cause; investigation revealed suppressed unchecked cast warning.
**Solution:** Added runtime type checking using `Class.cast()`; removed `@SuppressWarnings("unchecked")`.
**Prevention:** Never suppress unchecked cast warnings without understanding implications; use `TypeToken` pattern for runtime type information.

### Incident 3: Wildcard Capture Issue in API Design

**Problem:** A generic API method couldn't accept `List<Integer>` when `List<? extends Number>` was expected due to variance issues.
**Cause:** Developer misunderstood wildcard capture; tried to assign `List<Integer>` to `List<Number>` directly.
**Impact:** API design flawed; workaround required type casting; 20% of users couldn't use the API correctly.
**Detection:** Compilation errors reported by API users; design review revealed variance issue.
**Solution:** Used wildcard capture helper method to properly handle type inference.
**Prevention:** Understand PECS principle (Producer Extends, Consumer Super); test generic APIs with various type arguments.

## Production Checklist

- [ ] Never use raw types — always parameterize generic types
- [ ] Don't create `new T()` or `new T[]` — type erasure prevents this
- [ ] Use bounded types (`<T extends Comparable<T>>`) to constrain generic types
- [ ] Prefer `List<? extends T>` for read-only access (Producer Extends)
- [ ] Prefer `List<? super T>` for write access (Consumer Super)
- [ ] Don't use `List<Object>` when you mean `List<?>` or `List<String>`
- [ ] Don't ignore unchecked cast warnings — suppress only when justified
- [ ] Don't use wildcards in return types — use concrete generic types
- [ ] Use type tokens for runtime type information when needed
- [ ] Test generic code with multiple type arguments

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses basic generics; doesn't understand type erasure; creates raw types accidentally |
| Intermediate | Uses bounded types; understands type erasure; applies PECS principle |
| Advanced | Designs type-safe APIs; uses wildcard capture; implements generic utilities |
| Expert | Creates advanced generic patterns; understands compiler internals; teaches generics |

## Common Myths

1. **Myth**: Generics provide runtime type safety
   **Truth**: Generics are erased at compile time (type erasure); runtime type information is lost. Generics catch errors at compile time only.

2. **Myth**: `List<Integer>` is a subtype of `List<Number>`
   **Truth**: Java generics are invariant; `List<Integer>` is NOT a subtype of `List<Number>`. Use wildcards for variance.

3. **Myth**: You can create `new T()` with generics
   **Truth**: Type erasure means `T` becomes `Object` at runtime; you cannot instantiate generic types directly. Pass `Class<T>` and use reflection.

4. **Myth**: Wildcards make code more complex without benefit
   **Truth**: Wildcards enable flexible APIs that work with different type arguments while maintaining type safety.

5. **Myth**: Generic methods are always better than non-generic methods
   **Truth**: Non-generic methods are simpler and more readable when type flexibility isn't needed. Use generics only when necessary.

## Related Topics

- [Functional Programming](../07-functional-programming/README.md)
- [Type Safety](../00-knowledge-atoms/type-safety/README.md)

## Next

- [Functional Programming](../07-functional-programming/README.md)

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Type safety and code reuse |
| Complexity | O(1) (type erasure) |
| Thread Safe | Yes (no state) |
| Ordered | N/A |
| Allows Null | Yes |
| Best Alternative | Specific types (for performance) |
| When to Use | Generic algorithms |
| When to Avoid | Simple types |
