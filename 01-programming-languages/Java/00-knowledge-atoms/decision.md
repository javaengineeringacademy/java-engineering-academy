# Decision Guide: Knowledge Atoms

## When to Apply Knowledge Atom Concepts

### Autoboxing
- **Use autoboxing** when working with collections that require wrapper types (`List<Integer>`, `Map<String, Boolean>`)
- **Avoid autoboxing** in performance-critical loops — use primitive-specialized collections (Eclipse Collections, HPPC) instead
- **Use `==` for cache-range values** (-128 to 127) only when you are certain about caching behavior; otherwise always use `.equals()`

### Equals & HashCode
- **Override both** when your object will be used in `HashMap`, `HashSet`, or `LinkedHashMap`
- **Use `Objects.hash()`** for `hashCode()` and `Objects.equals()` for null-safe `equals()` comparisons
- **Consider `record` types** (Java 14+) for simple data carriers — they auto-generate `equals()`, `hashCode()`, and `toString()`

### Garbage Collection
- **Use G1 GC** (default) for general-purpose applications with heaps 4GB+
- **Use ZGC or Shenandoah** when latency requirements are sub-10ms (trading systems, real-time applications)
- **Set `-Xms` = `-Xmx`** to avoid resize pauses in production

### Immutability
- **Default to immutable objects** for value objects, DTOs, configuration, and cache keys
- **Use `record` types** for immutable data classes (Java 14+)
- **Use `List.copyOf()` / `Map.copyOf()`** for immutable collections (Java 10+)
- **Avoid immutability** for large mutable state with frequent updates (game worlds, large matrices)

### Java Memory Model
- **Use `volatile`** for simple state flags shared between threads
- **Use `synchronized`** for compound operations that must be atomic
- **Use `AtomicInteger` / `LongAdder`** for lock-free counters
- **Never publish `this`** in a constructor — use factory methods instead

### Pass by Value
- **Remember**: Java is always pass-by-value, even for objects
- **To modify an object** in a method, call methods on the passed reference (e.g., `list.add()`)
- **To "replace" an object**, return the new object and reassign at the call site

### Type Safety
- **Use generics** everywhere — avoid raw types
- **Use `instanceof`** before casting, or use pattern matching (Java 16+)
- **Use sealed classes** (Java 17+) to restrict class hierarchies and enable exhaustive switch expressions

## Trade-offs Summary

| Concept | With | Without |
|---------|------|---------|
| Autoboxing awareness | Better performance, fewer surprises | Potential NPE, slower loops |
| Correct equals/hashCode | Collections work correctly | Lost objects, broken contracts |
| GC tuning | Predictable latency, no pauses | Long stop-the-world pauses |
| Immutability | Thread safety, caching, security | Race conditions, defensive copies needed |
| JMM compliance | Correct concurrent code | Subtle visibility bugs |
| Type safety | Compile-time error catching | ClassCastException at runtime |

## Expert Recommendation

Apply these knowledge atoms as a checklist during code review:
1. Verify `equals()`/`hashCode()` are overridden together when needed
2. Check that immutable objects have all fields `final` and no setters
3. Ensure `volatile` is used for flags shared across threads
4. Confirm no autoboxing in performance-critical loops
5. Profile GC behavior under production-like load
