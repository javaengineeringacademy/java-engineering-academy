# Module 06: Generics

> **Difficulty:** ⭐⭐⭐ Intermediate  
> **Reading:** 35 min | **Practice:** 45 min | **Total:** 80 min

## Overview

Without generics, every collection stores Objects and requires manual casting at retrieval — a source of runtime ClassCastException bugs that compilers can't catch. Generics let you write a single class or method that works with any type while catching type mismatches at compile time. Introduced in Java 5, they eliminated explicit casting and made APIs like the Collections Framework type-safe.

## Learning Objectives

- [ ] Write generic classes and methods that work with any type
- [ ] Restrict type parameters using bounded types (`extends`, `super`)
- [ ] Apply the PECS principle (Producer Extends, Consumer Super)
- [ ] Understand type erasure and its implications
- [ ] Design type-safe APIs that eliminate explicit casting
- [ ] Avoid common generic pitfalls (raw types, unchecked casts)

## Prerequisites

- Solid understanding of OOP (inheritance, polymorphism)
- Familiarity with collections framework (Module 04)
- Basic understanding of interfaces and abstract classes

## History

- **1995** — Java 1.0 used raw collections (no type safety) because generics were not yet available
- **1998** — Java 1.2 introduced Collections Framework with `Object`-based types
- **2004** — Java 5 introduced generics for compile-time type safety, eliminating explicit casting
- **2011** — Java 7 added diamond operator (`<>`) to reduce boilerplate
- **2014** — Java 8 improved type inference in lambdas and method references
- **2017** — Java 9 added `var` for local variable type inference
- **2023** — Java 21 continued type system refinements

## Production Notes

- **Where is it used?** In every Java application that uses collections, APIs, or type-safe code
- **Why is it useful?** Provides compile-time type safety, eliminates casting, enables code reuse
- **When should it be avoided?** Not applicable; generics are fundamental to modern Java
- **Alternative?** Raw types (legacy), `Object` casting (unsafe)

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

### Wildcards (PECS Principle)

```java
// Producer Extends - read-only
public double sum(List<? extends Number> list) {
    return list.stream()
        .mapToDouble(Number::doubleValue)
        .sum();
}

// Consumer Super - write-only
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

1. **Remove type parameters** — Replace all `T` with bounds or `Object`
2. **Add casts** — Insert casts where type information is lost
3. **Bridge methods** — Generate methods to maintain polymorphism

```
// Before erasure
public class Box<T> {
    private T value;
    public T getValue() { return value; }
    public void setValue(T value) { this.value = value; }
}

// After erasure
public class Box {
    private Object value;
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
    // Bridge method for covariance
    public void setValue(String value) { setValue((Object) value); }
}
```

### Runtime Type Information

```java
// Type erasure means you cannot do:
// if (obj instanceof List<String>) // Compile error
// new T() // Compile error
// new T[10] // Compile error

// But you can:
List<String> list = new ArrayList<>();
Class<?> clazz = list.getClass(); // Works - runtime type
ParameterizedType pt = (ParameterizedType) clazz.getGenericSuperclass();
Type[] types = pt.getActualTypeArguments(); // String.class
```

## Syntax

```java
// Generic class
class Box<T> {
    private T value;
    public T getValue() { return value; }
    public void setValue(T value) { this.value = value; }
}

// Generic method
public <T> List<T> asList(T a, T b) {
    return List.of(a, b);
}

// Bounded type parameter
public <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}

// Wildcard with upper bound
public void printNumbers(List<? extends Number> list) {
    list.forEach(System.out::println);
}

// Wildcard with lower bound
public void addIntegers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
}

// Diamond operator (Java 7+)
Box<String> box = new Box<>();

// Type inference (Java 8+)
List<String> list = List.of("a", "b", "c");
```

## Examples

### Easy: Generic Box
```java
public class Box<T> {
    private T value;
    
    public Box(T value) {
        this.value = value;
    }
    
    public T getValue() { return value; }
    public void setValue(T value) { this.value = value; }
    
    @Override
    public String toString() {
        return "Box[" + value + "]";
    }
}

public class BoxDemo {
    public static void main(String[] args) {
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);
        
        System.out.println(stringBox); // Box[Hello]
        System.out.println(intBox);    // Box[42]
        
        // stringBox.setValue(42); // Compile error!
    }
}
```

### Medium: Bounded Type Parameters
```java
public class MathUtils {
    public static <T extends Number & Comparable<T>> T max(List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(max) > 0) {
                max = list.get(i);
            }
        }
        return max;
    }
    
    public static void main(String[] args) {
        List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9);
        System.out.println("Max: " + max(numbers)); // 9
        
        List<Double> doubles = List.of(3.14, 2.71, 1.41);
        System.out.println("Max: " + max(doubles)); // 3.14
    }
}
```

### Hard: Wildcards and PECS
```java
import java.util.*;

public class PecsDemo {
    // Producer extends - can read, not write
    public static double sum(List<? extends Number> list) {
        return list.stream()
            .mapToDouble(Number::doubleValue)
            .sum();
    }
    
    // Consumer super - can write, not read (as specific type)
    public static void addIntegers(List<? super Integer> list) {
        for (int i = 1; i <= 5; i++) {
            list.add(i);
        }
    }
    
    // Unbounded - read-only Object access
    public static void printAll(List<?> list) {
        list.forEach(System.out::println);
    }
    
    public static void main(String[] args) {
        List<Integer> integers = List.of(1, 2, 3, 4, 5);
        List<Double> doubles = List.of(1.1, 2.2, 3.3);
        
        System.out.println("Sum integers: " + sum(integers)); // 15.0
        System.out.println("Sum doubles: " + sum(doubles));   // 6.6
        
        List<Number> numbers = new ArrayList<>();
        addIntegers(numbers); // Can add Integer to List<Number>
        System.out.println("Numbers: " + numbers); // [1, 2, 3, 4, 5]
        
        printAll(integers);
        printAll(doubles);
    }
}
```

### Enterprise: Generic Repository Pattern
```java
import java.util.*;
import java.util.concurrent.*;

public class GenericRepository<T, ID> {
    private final Map<ID, T> store = new ConcurrentHashMap<>();
    
    public void save(ID id, T entity) {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(entity, "Entity cannot be null");
        store.put(id, entity);
    }
    
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(store.get(id));
    }
    
    public List<T> findAll() {
        return List.copyOf(store.values());
    }
    
    public void delete(ID id) {
        store.remove(id);
    }
    
    public boolean exists(ID id) {
        return store.containsKey(id);
    }
}

// Usage
public class UserRepository extends GenericRepository<User, Long> {
    // Inherits all CRUD operations for User entities
}

class User {
    private final Long id;
    private final String name;
    
    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Long getId() { return id; }
    public String getName() { return name; }
}
```

## Performance Considerations

| Operation | Cost | Notes |
|-----------|------|-------|
| Type checking | Compile-time | No runtime cost |
| Type erasure | Compile-time | No runtime cost |
| Boxed types | +16 bytes | Integer vs int |
| Casting | ~100ns | When erasure requires casts |
| Bridge methods | Minimal | Generated per generic class |

- **Generics have zero runtime cost** — all checking is compile-time
- **Boxed types add overhead** — use primitive streams when possible
- **Type erasure means no `new T()`** — pass `Class<T>` and use reflection

## Best Practices

**Do's:**
- Always use parameterized types (`List<String>` not `List`)
- Use bounded types when you need to call methods on type parameters
- Follow PECS: Producer Extends, Consumer Super
- Use `Class<T>` to pass type information at runtime
- Use `@SuppressWarnings("unchecked")` only when you can prove safety

**Don'ts:**
- Don't use raw types (loses type safety)
- Don't create `new T()` (type erasure prevents this)
- Don't use `instanceof List<String>` (type erasure prevents this)
- Don't use `List<String>` and `List<Integer>` interchangeably (generics are invariant)
- Don't ignore unchecked cast warnings

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Using raw types | Loses type safety | Always use `List<String>` not `List` |
| `new T()` | Compile error — type erasure | Pass `Class<T>` and use `clazz.getDeclaredConstructor()` |
| `List<String>` not assignable to `List<Object>` | Generics are invariant | Use wildcards: `List<?>` |
| Unchecked cast warning | Potential `ClassCastException` | Use `@SuppressWarnings("unchecked")` only when safe |
| `instanceof List<String>` | Compile error — type erasure | Use `instanceof List<?>` |

## Interview Questions

### Q1: What is type erasure in generics?
**Answer:** Type erasure is the compile-time process where all generic type parameters are removed. `List<String>` becomes `List` at runtime. This maintains backward compatibility with pre-generics code but means you cannot use type parameters at runtime (no `new T()`, no `instanceof T`).

### Q2: What is the PECS principle?
**Answer:** Producer Extends, Consumer Super. When a generic type produces data for you to read, use `? extends T`. When you need to write data to a generic type, use `? super T`. This ensures type safety while allowing flexibility.

### Q3: What is the difference between `<T extends Comparable<T>>` and `<T extends Comparable>`?
**Answer:** The first is a bounded type parameter — T must be Comparable to itself. The second is a raw type — loses type safety. Always use the parameterized form.

### Q4: Can you create an array of generic type?
**Answer:** No, because of type erasure. `new T[10]` is illegal. Workaround: `Array.newInstance(clazz, 10)` or use `ArrayList<T>` instead.

### Q5: What is a wildcard capture?
**Answer:** When the compiler captures the unknown type of a wildcard to perform operations. Example: `public static void swap(List<?> list, int i, int j)` uses a helper method with wildcard capture to swap elements.

### Q6: What is the difference between `List<?>` and `List<Object>`?
**Answer:** `List<?>` can hold any type (unknown type). `List<Object>` can only hold `Object` subtypes. `List<String>` is not assignable to `List<Object>` but is assignable to `List<?>`.

### Q7: Why can't you use `new T()`?
**Answer:** Type erasure means the JVM doesn't know what `T` is at runtime. You must pass `Class<T>` and use reflection: `clazz.getDeclaredConstructor().newInstance()`.

### Q8: What are bridge methods?
**Answer:** Synthetic methods generated by the compiler to maintain polymorphism after type erasure. When a subclass overrides a generic method, bridge methods ensure the correct method is called at runtime.

### Q9: What is reified type?
**Answer:** A type that is fully available at runtime. Primitive types and non-generic reference types are reified. Generic types are not reified due to type erasure. `Class<T>` is reified — you can use it to access type information at runtime.

### Q10: What is the difference between `<T extends Comparable<T>>` and `<T extends Comparable<?>>`?
**Answer:** The first requires T to be Comparable to itself (natural ordering). The second requires T to be Comparable to some unknown type. The first is more type-safe and commonly used.

## Cross-References

- **Previous Module:** [05 - Text Processing](../05-text-processing/)
- **Next Module:** [07 - Functional Programming](../07-functional-programming/)
- **Related:** [02 - OOP](../02-oop/) — inheritance and polymorphism
- **Related:** [04 - Collections](../04-collections/) — parameterized collection types
- **Related:** [07 - Functional Programming](../07-functional-programming/) — generic functional interfaces

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Unchecked cast warning | IntelliJ inspection | Use `@SuppressWarnings("unchecked")` with proof of safety |
| Type inference failure | Explicit type parameter | Use `Collections.<String>emptyList()` when compiler can't infer |
| Wildcard capture failure | Helper method | Extract wildcard capture to a separate method |
| Generic array creation error | ArrayList workaround | Use `ArrayList<T>` instead of `T[]` |
| ClassCastException at runtime | Check for raw types | Verify all generic types are parameterized |

## Code Review Checklist

- [ ] No raw types used
- [ ] Wildcards used correctly (PECS)
- [ ] Bounded types used when calling methods on type parameters
- [ ] Unchecked cast warnings justified
- [ ] `Class<T>` passed for runtime type operations
- [ ] Generic types parameterized correctly

## Architecture Considerations

Generics enable type-safe, reusable code that scales across teams. At scale, generic APIs become the contract that distributed systems depend on. For library authors, generic design determines API usability and evolution. For microservices, generic DTOs and repositories reduce boilerplate across services.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Generic repository | Data access layer | Pros: Type-safe CRUD, code reuse; Cons: Complex queries |
| Generic factory | Object creation | Pros: Type-safe creation; Cons: Requires Class<T> parameter |
| Generic builder | Builder pattern | Pros: Fluent API, type safety; Cons: Verbosity |
| Wildcard API | Flexible APIs | Pros: Accepts more types; Cons: More complex signatures |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Type confusion via raw types | ClassCastException, data corruption | Use parameterized types everywhere |
| Unchecked cast bypassing type safety | Runtime ClassCastException | Minimize unchecked casts; use `@SuppressWarnings` |
| Generic type injection | Security bypass | Validate generic type parameters |
| Reflection bypassing generics | Unsafe operations | Restrict reflection access |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0–1.4 | Raw types | Migrate to parameterized types |
| Java 5 | Generics introduced | Replace raw types with generics |
| Java 7 | Diamond operator | Use `new ArrayList<>()` |
| Java 8 | Type inference in lambdas | Use lambdas with generic types |
| Java 9 | `var` for local variables | Use `var` for obvious types |
| Java 21 | Type system refinements | Use latest features |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Generics | Java 5 | Stable |
| Diamond operator | Java 7 | Stable |
| Type inference | Java 8 | Stable |
| `var` | Java 10 | Stable |

## Production Incidents

### Incident 1: Raw Type Causing ClassCastException

**Problem:** A legacy system threw `ClassCastException` intermittently when processing orders, causing 5% of transactions to fail.
**Cause:** Code used raw `List` instead of `List<Order>`, allowing wrong types to be inserted.
**Impact:** 5% transaction failure rate; customer complaints; manual data correction required.
**Detection:** Exception logs showed ClassCastException in order processing.
**Solution:** Replaced raw types with parameterized types; added compile-time type checking.
**Prevention:** Use static analysis to flag raw types; enforce parameterized types in code review.

### Incident 2: Type Erasure Hiding Bug

**Problem:** A generic utility class failed to detect null values at compile time, causing NPEs in production.
**Cause:** Developer assumed `T` would be checked at runtime; type erasure prevented null checking.
**Impact:** NPEs in production; 2% of requests failed.
**Solution:** Added explicit null checks; used `Objects.requireNonNull()` for generic parameters.
**Prevention:** Understand type erasure; add null checks for generic parameters.

### Incident 3: Unchecked Cast Warning Ignored

**Problem:** A caching system returned wrong types due to ignored unchecked cast warning.
**Cause:** Developer suppressed warning without proving safety; cache stored mixed types.
**Impact:** Type mismatch in downstream code; 10% of requests returned incorrect data.
**Solution:** Fixed type safety; removed unchecked cast; used proper generic types.
**Prevention:** Never ignore unchecked cast warnings; prove safety before suppressing.

### Incident 4: Wildcard Misuse Causing API Incompatibility

**Problem:** A library upgrade broke all consumer code due to wildcard changes in method signatures.
**Cause:** Library changed `List<String>` to `List<?>` in public API, breaking downstream code.
**Impact:** 50+ consumer applications failed to compile; 2-day emergency fix.
**Detection:** Compilation errors across multiple projects after library upgrade.
**Solution:** Maintained backward compatibility; added new methods with wildcards; kept old signatures.
**Prevention:** Follow semantic versioning; avoid breaking API changes; use bounded wildcards carefully.

### Incident 5: Type Erasure Hiding Runtime Type Information

**Problem:** A serialization library failed to deserialize generic types correctly at runtime.
**Cause:** Type erasure removed generic type information; code assumed `List<String>` was available at runtime.
**Impact:** 30% of deserialization requests failed; data corruption in some cases.
**Detection:** `ClassCastException` at runtime when accessing deserialized collections.
**Solution:** Used `TypeToken` pattern to preserve type information; passed `Class<T>` parameters.
**Prevention:** Use `TypeReference` or `Class<T>` for generic type preservation; understand type erasure limitations.

## Production Checklist

- [ ] No raw types used
- [ ] Wildcards used correctly (PECS)
- [ ] Bounded types used when calling methods on type parameters
- [ ] Unchecked cast warnings justified
- [ ] `Class<T>` passed for runtime type operations
- [ ] Generic types parameterized correctly
- [ ] Null checks added for generic parameters
- [ ] Type erasure understood and accounted for

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses raw types; doesn't understand generics; casts everywhere |
| Intermediate | Uses parameterized types; understands basic wildcards; avoids raw types |
| Advanced | Uses PECS; understands type erasure; designs generic APIs |
| Expert | Designs generic libraries; contributes to type system; teaches generics |

## Common Myths

1. **Myth**: Generics add runtime overhead
   **Truth**: Generics are compile-time only. Type erasure removes all generic information, so there's zero runtime cost.

2. **Myth**: `List<String>` is a subtype of `List<Object>`
   **Truth**: Generics are invariant. `List<String>` is NOT assignable to `List<Object>`. Use `List<?>` for unknown types.

3. **Myth**: You can use `instanceof` with generic types
   **Truth**: Type erasure prevents `instanceof List<String>`. Use `instanceof List<?>` instead.

4. **Myth**: Wildcards make APIs more complex unnecessarily
   **Truth**: Wildcards enable flexible, type-safe APIs. Without them, you'd need to duplicate methods for different types.

5. **Myth**: Type erasure means generics are useless at runtime
   **Truth**: `Class<T>` preserves type information at runtime. Use it for reflection, deserialization, and type-safe factories.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Compile-time type safety, code reuse |
| Key feature | Type parameters (`<T>`) |
| Bounded types | `<T extends Number>` — restrict T to Number subtypes |
| Wildcards | `? extends T` (read), `? super T` (write) |
| PECS | Producer Extends, Consumer Super |
| Type erasure | Removed at compile time — no runtime cost |
| Common mistake | Raw types, `new T()`, `instanceof List<String>` |
| When to use | Collections, APIs, type-safe code |
| When to avoid | Never — generics are fundamental |
