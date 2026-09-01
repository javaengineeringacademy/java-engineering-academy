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

## History

- **1996** — Java 1.0 introduced pass-by-value semantics and basic type system
- **1998** — Java 1.2 added Collections Framework, introducing equals/hashCode contract importance
- **2004** — Java 5 introduced autoboxing, generics, and improved type safety
- **2011** — Java 7 introduced G1 garbage collector for better pause time management
- **2014** — Java 8 added lambdas, affecting immutable object patterns
- **2017** — Java 9 module system restricted reflective access to internals
- **2021** — Java 17 records auto-generate equals/hashCode, simplifying immutability
- **2023** — Java 21 virtual threads changed memory model considerations

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

## Internal Working

### Autoboxing Internals

```
int i = 42;
Integer boxed = i;        // Integer.valueOf(i) — cached for -128 to 127
int unboxed = boxed;       // boxed.intValue() — unboxing
```

- Integer cache: -128 to 127 (default, configurable with `-XX:AutoBoxCacheMax`)
- Each autoboxing creates a new object outside cache range
- JIT can optimize some autoboxing away

### Object Header Layout

```
┌─────────────────────────────────────┐
│         Object Header               │
├─────────────────────────────────────┤
│  Mark Word (64-bit)                 │
│  ┌─────────────────────────────┐    │
│  │ Hash code, GC age, lock     │    │
│  └─────────────────────────────┘    │
│  Klass Pointer                     │
│  ┌─────────────────────────────┐    │
│  │ Pointer to class metadata   │    │
│  └─────────────────────────────┘    │
│  [Padding for alignment]           │
└─────────────────────────────────────┘
```

### Pass-by-Value Mechanism

```
Primitive: value is copied
  int x = 10;
  modify(x);  // copy of 10 passed

Object: reference is copied
  List<String> list = new ArrayList<>();
  modify(list);  // copy of reference passed
```

## Syntax

```java
// Autoboxing
int primitive = 42;
Integer boxed = primitive;        // autoboxing
int unboxed = boxed;              // unboxing

// equals/hashCode
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return age == user.age && Objects.equals(name, user.name);
}

@Override
public int hashCode() {
    return Objects.hash(name, age);
}

// Immutability
public final class User {
    private final String name;
    private final int age;
    
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
}

// Thread-safe singleton
public class Singleton {
    private static volatile Singleton instance;
    private Singleton() {}
    
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

## Examples

### Easy: Autoboxing Performance
```java
public class AutoboxingDemo {
    public static void main(String[] args) {
        // Autoboxing in loop — creates 10 million Integer objects!
        long start = System.currentTimeMillis();
        Long sum = 0L;  // boxed — BAD
        for (int i = 0; i < 10_000_000; i++) {
            sum += i;  // autoboxing per iteration
        }
        System.out.println("Boxed: " + (System.currentTimeMillis() - start) + "ms");
        
        // Using primitive — no object creation
        start = System.currentTimeMillis();
        long sum2 = 0L;  // primitive — GOOD
        for (int i = 0; i < 10_000_000; i++) {
            sum2 += i;
        }
        System.out.println("Primitive: " + (System.currentTimeMillis() - start) + "ms");
    }
}
```

### Medium: Immutable Object
```java
import java.util.Objects;

public final class Money {
    private final String currency;
    private final long amount;  // use long for cents, not double
    
    public Money(String currency, long amount) {
        this.currency = Objects.requireNonNull(currency);
        this.amount = amount;
    }
    
    public String getCurrency() { return currency; }
    public long getAmount() { return amount; }
    
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Different currencies");
        }
        return new Money(currency, this.amount + other.amount);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amount == money.amount && currency.equals(money.currency);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(currency, amount);
    }
}
```

### Hard: Memory Model Visibility
```java
public class VisibilityDemo {
    private static volatile boolean running = true;  // volatile ensures visibility
    
    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            int count = 0;
            while (running) {  // without volatile, may loop forever
                count++;
            }
            System.out.println("Count: " + count);
        });
        
        worker.start();
        Thread.sleep(1000);
        running = false;  // without volatile, worker may never see this
        worker.join();
    }
}
```

### Enterprise: Record for Immutability
```java
public record User(String name, int age, String email) {
    // Records are automatically:
    // - final (cannot be extended)
    // - immutable (all fields final)
    // - equipped with equals(), hashCode(), toString()
    
    public User {
        if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
    }
    
    // Custom method
    public String summary() {
        return name + " (" + age + ")";
    }
}
```

## Performance Considerations

| Operation | Cost | Notes |
|-----------|------|-------|
| Autoboxing (cached) | ~1ns | Integer.valueOf() for -128 to 127 |
| Autoboxing (uncached) | ~10ns | New object creation |
| Object header | 16 bytes | Mark word + klass pointer |
| equals() comparison | ~100ns | Depends on field count |
| hashCode() calculation | ~50ns | Depends on field count |
| Immutability copy | O(n) | New object for each change |

## Best Practices

**Do's:**
- Use primitives for performance-critical code
- Override both equals() and hashCode() together
- Make immutable objects truly immutable (all fields final)
- Use volatile for flags shared across threads
- Use Records for immutable data classes (Java 16+)

**Don'ts:**
- Don't use autoboxing in hot loops
- Don't override equals() without hashCode()
- Don't expose mutable internal state
- Don't assume thread safety without synchronization
- Don't use String.intern() excessively

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Autoboxing in loop | Performance degradation | Use primitives |
| Override equals() only | Broken HashMap/HashSet | Always override hashCode() too |
| Mutable fields in immutable class | Thread safety issues | Make all fields final |
| Missing volatile | Thread visibility issues | Add volatile for shared flags |
| Using == for Integer comparison | Reference comparison | Use .equals() or intValue() |

## Interview Questions

### Q1: What is autoboxing and when is it a problem?
**Answer:** Autoboxing is automatic conversion between primitives and wrapper classes. It's a problem in loops where it creates millions of objects, increasing GC pressure. Use primitives for performance-critical code.

### Q2: What is the equals/hashCode contract?
**Answer:** If two objects are equal (equals() returns true), they must have the same hashCode(). Breaking this contract causes HashSet/HashMap to malfunction. Always override both together.

### Q3: How do you create an immutable class?
**Answer:** Make class final, all fields final and private, no setters, deep copy mutable fields in constructor and getters, override equals/hashCode. Records (Java 16+) are automatically immutable.

### Q4: What is pass-by-value in Java?
**Answer:** Java is always pass-by-value. For primitives, the value is copied. For objects, the reference is copied (not the object). You can modify the object through the reference but not reassign the original reference.

### Q5: What is the difference between == and .equals()?
**Answer:** == compares references (memory addresses). .equals() compares values. For wrapper classes, == compares references (may fail for values outside cache range).

### Q6: Why is String immutable?
**Answer:** Strings are stored in the string pool. Immutability enables string interning, security, thread safety, and class loading safety. It also allows strings to be used as HashMap keys safely.

### Q7: What is the Integer cache?
**Answer:** Java caches Integer objects for values -128 to 127. Autoboxing within this range returns the same object. Outside this range, new objects are created.

### Q8: What is the difference between immutable and final?
**Answer:** Final means the reference cannot be reassigned. Immutable means the object's state cannot be changed. A final reference to a mutable object can still have its state changed.

### Q9: What is the Memory Model and why does it matter?
**Answer:** The Java Memory Model defines how threads interact through memory. Without it, threads may see stale values from CPU caches. volatile and synchronized establish happens-before relationships.

### Q10: What is a record and when should you use it?
**Answer:** A record is a compact class for immutable data. Use for DTOs, value objects, and data carriers. It auto-generates constructor, accessors, equals, hashCode, toString.

## Cross-References

- **Next Module:** [01 - Fundamentals](../01-fundamentals/)
- **Related:** [04 - Collections](../04-collections/) — equals/hashCode in HashMap
- **Related:** [09 - Multithreading](../09-multithreading-&-concurrency/) — memory model, volatile
- **Related:** [10 - JVM Internals](../10-jvm-internals/) — GC internals, object layout
- **Related:** [16 - Modern Java](../16-modern-java/) — records, pattern matching

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Integer cache mismatch | IntelliJ Debugger | Inspect `Integer` values with `==` in watch expression; verify cache range (-128 to 127) |
| GC pause causing timeouts | JFR (Java Flight Recorder) | Enable JFR recording; analyze GC pause events and duration in JFR Viewer |
| Broken HashSet/HashMap | Unit tests + equals/hashCode contract | Write tests verifying `equals()` and `hashCode()` consistency for custom objects |
| Thread visibility issues | JMM happens-before analysis | Use `jconsole` or `jstack` to inspect thread states; verify `volatile` and `synchronized` usage |
| Autoboxing performance hotspots | VisualVM sampler | Profile CPU usage; identify autoboxing in hot loops via allocation profiling |

## Code Review Checklist

- [ ] Verify `equals()` and `hashCode()` are overridden together when needed
- [ ] Check that immutable objects have all fields `final` and no setters
- [ ] Ensure `volatile` is used for flags shared across threads
- [ ] Verify no autoboxing in performance-critical loops (use primitive collections)
- [ ] Confirm GC configuration matches latency requirements
- [ ] Check that `String.intern()` is not used excessively (memory leak risk)
- [ ] Verify `Comparable` implementation is consistent with `equals()`

## Architecture Considerations

Knowledge atoms form the foundational layer that every other Java module depends on. At scale, choices like garbage collector selection (G1 vs ZGC vs Shenandoah), memory model compliance, and immutability guarantees directly impact system reliability and performance. For distributed systems, understanding pass-by-value semantics prevents subtle serialization bugs, while proper equals/hashCode contracts ensure correct behavior in distributed caches and hash-based data structures.

When designing large-scale systems, immutability should be the default for value objects shared across threads or services. The Java Memory Model's happens-before relationships must be explicitly managed through synchronization, volatile fields, or concurrent utilities rather than relying on assumptions about JVM behavior.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Immutable value objects | DTOs, cache keys, configuration | Pros: Thread-safe, cacheable, hashable; Cons: Object creation overhead, new instance for changes |
| Primitive-specialized collections | High-frequency numeric processing | Pros: No autoboxing overhead, lower GC pressure; Cons: API fragmentation, third-party dependency |
| Defensive copying | Exposing mutable internal state | Pros: Prevents external mutation; Cons: Performance cost per copy |
| Thread-local caching | Per-thread computation results | Pros: No synchronization needed; Cons: Memory overhead, thread pool leaks |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Weak equals/hashCode enabling hash collision attacks | DoS via degraded HashMap performance | Override `hashCode()` with randomized salt; use `ConcurrentHashMap` with proper hash distribution |
| Mutable objects in security contexts | State tampering, authentication bypass | Make security-relevant objects immutable; validate state on construction |
| GC information leakage via timing attacks | Side-channel vulnerability | Use constant-time comparison for security checks; avoid timing-dependent code paths |
| Unsafe deserialization of equals/hashCode-dependent objects | Remote code execution | Implement `readObject()` validation; use `ObjectInputFilter` for deserialization filtering |
| Thread-unsafe immutable object construction | Partially constructed object visibility | Ensure all fields are `final` or published safely through synchronization |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0–1.4 | Raw collections, manual equals/hashCode | Migrate to generics (Java 5+); use IDE-generated equals/hashCode |
| Java 5–7 | Autoboxing introduced, basic GC | Review autoboxing in loops; profile GC behavior |
| Java 8 | Default methods in interfaces | Update equals/hashCode contracts for default method interactions |
| Java 9+ | Module system restricts reflective access | Use public APIs instead of reflection for equals/hashCode testing |
| Java 12–16 | Switch expressions, records | Use `record` for immutable data classes (auto-generates equals/hashCode) |
| Java 21 | Virtual threads, sequenced collections | Verify equals/hashCode correctness in virtual thread contexts |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Autoboxing/unboxing | Java 5 | Stable |
| Records (auto equals/hashCode) | Java 16 | Stable |
| `sealed` classes | Java 17 | Stable |
| Pattern matching for switch | Java 21 | Stable |
| Virtual threads | Java 21 | Stable |
| ZGC (production) | Java 21 | Stable |

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

### Incident 4: String.intern() Causing Memory Leak in Cache

**Problem:** A caching system using `String.intern()` for metadata strings experienced a memory leak, with the String Pool growing to 8GB over 3 days.
**Cause:** High-cardinality metadata strings were interned without limits, exhausting PermGen/Metaspace.
**Impact:** Application crashed with `OutOfMemoryError: Metaspace` every 3 days, requiring restarts.
**Detection:** Heap dumps showed millions of interned strings in the String Pool.
**Solution:** Replaced `String.intern()` with a `ConcurrentHashMap<String, String>` cache with eviction policy.
**Prevention:** Avoid `String.intern()` in application code; use explicit caches (Guava, Caffeine) with size limits.

### Incident 5: Pass-by-Value Misunderstanding in Serialization

**Problem:** A deep-copy utility using serialization was silently sharing mutable state between objects, causing race conditions.
**Cause:** Developer assumed Java was pass-by-reference and misunderstood how object references work.
**Impact:** 3 production race conditions in 2 weeks, including one that corrupted user data.
**Detection:** Thread dump analysis showed threads accessing the same object instance unexpectedly.
**Solution:** Implemented proper deep copy using `Cloneable` interface with recursive cloning.
**Prevention:** Train developers on Java's pass-by-value semantics; use immutable objects to avoid deep copy needs.

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
