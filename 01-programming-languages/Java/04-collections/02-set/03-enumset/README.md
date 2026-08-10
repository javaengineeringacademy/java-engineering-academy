# EnumSet

## Scope

This folder focuses exclusively on EnumSet.
Examples, exercises, and quizzes in this folder cover only EnumSet concepts.

## 1. Why It Exists

EnumSet was introduced in Java 5 to provide a high-performance Set implementation specifically for enum types. It uses a bit vector internally, making it extremely fast and memory-efficient compared to HashSet for enum constants.

## 2. What It Is

EnumSet is an abstract Set implementation for enum types. It uses a bit vector (long or long[] array) to represent the set, where each bit corresponds to an enum constant. All operations are O(1).

## 3. Internal Working

```java
// EnumSet uses bit vector internally
// For small enums: single long
// For large enums: long[] array

// Example: DayOfWeek enum
// MONDAY = 0, TUESDAY = 1, ..., SUNDAY = 6
// EnumSet<DayOfWeek> set with MONDAY, WEDNESDAY, FRIDAY
// Binary: 0010101 (bits 0, 2, 4 set)
```

### Bit Vector Representation

```
EnumSet for DayOfWeek with MONDAY, WEDNESDAY, FRIDAY:

Bit position: 6 5 4 3 2 1 0
Value:        0 0 1 0 1 0 1

Bit 0 (MONDAY) = 1
Bit 1 (TUESDAY) = 0
Bit 2 (WEDNESDAY) = 1
Bit 3 (THURSDAY) = 0
Bit 4 (FRIDAY) = 1
Bit 5 (SATURDAY) = 0
Bit 6 (SUNDAY) = 0
```

### Union Operation

```
Set A: MONDAY, WEDNESDAY, FRIDAY (0010101)
Set B: TUESDAY, WEDNESDAY (0000110)

A | B = 0010111 (MONDAY, TUESDAY, WEDNESDAY, FRIDAY)
```

## 4. Constructors (Factory Methods)

```java
EnumSet<DayOfWeek> all = EnumSet.allOf(DayOfWeek.class);
EnumSet<DayOfWeek> none = EnumSet.noneOf(DayOfWeek.class);
EnumSet<DayOfWeek> some = EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
EnumSet<DayOfWeek> range = EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
EnumSet<DayOfWeek> complement = EnumSet.complementOf(some);
```

## 5. Methods

### Set Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `add(E e)` | Adds element | O(1) |
| `remove(Object o)` | Removes element | O(1) |
| `contains(Object o)` | Checks membership | O(1) |
| `size()` | Element count | O(1) |
| `isEmpty()` | Checks if empty | O(1) |
| `clear()` | Removes all | O(1) |
| `iterator()` | Returns iterator | O(1) |

### EnumSet-Specific Methods

| Method | Description | Complexity |
|--------|-------------|------------|
| `allOf(Class)` | All enum constants | O(1) |
| `noneOf(Class)` | Empty set | O(1) |
| `of(E...)` | Set of specified elements | O(n) |
| `range(E, E)` | Range of enum constants | O(n) |
| `complementOf(EnumSet)` | Complement set | O(n) |

### Bulk Operations

| Method | Description | Complexity |
|--------|-------------|------------|
| `addAll(EnumSet)` | Union | O(n) |
| `retainAll(EnumSet)` | Intersection | O(n) |
| `removeAll(EnumSet)` | Difference | O(n) |

## 6. Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| add(E) | O(1) | O(1) |
| remove(Object) | O(1) | O(1) |
| contains(Object) | O(1) | O(1) |
| size() | O(1) | O(1) |
| isEmpty() | O(1) | O(1) |
| addAll(EnumSet) | O(n) | O(1) |
| retainAll(EnumSet) | O(n) | O(1) |
| removeAll(EnumSet) | O(n) | O(1) |
| iterator() | O(1) | O(1) |

## 7. Thread Safety

EnumSet is NOT thread-safe:

```java
// Option 1: Synchronized wrapper
Set<DayOfWeek> syncSet = Collections.synchronizedSet(EnumSet.noneOf(DayOfWeek.class));

// Option 2: Explicit synchronization
synchronized (enumSet) {
    // Access enumSet
}

// Option 3: ConcurrentHashMap.newKeySet() for concurrent enum set
Set<DayOfWeek> concurrentSet = ConcurrentHashMap.newKeySet(DayOfWeek.class);
```

## 8. Memory Behavior

### Memory Layout

```
EnumSet object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ element class (8 bytes)     │
│ universe (Enum[] reference) │──────┐
│ elements (long or long[])   │──┐   │
│ (padding 4 bytes)           │  │   │
└─────────────────────────────┘  │   │
                                 │   ▼
                          long[] array (for large enums)
                          ┌──────────────────┐
                          │ [0]: bit vector  │
                          │ [1]: bit vector  │
                          └──────────────────┘
```

### Memory Comparison

| Type | Per-Element | 7 Elements (DayOfWeek) |
|------|-------------|------------------------|
| EnumSet | 8 bytes (long) | 8 bytes |
| HashSet | ~40 bytes | ~280 bytes |
| LinkedHashSet | ~48 bytes | ~336 bytes |

## 9. Production Incidents

### Incident 1: NullPointerException with null Element

**Problem:** NullPointerException when adding null.
**Cause:** EnumSet does not allow null elements.
**Impact:** Application crash.
**Solution:** Never add null to EnumSet.
**Prevention:** Validate inputs before adding.

### Incident 2: ClassCastException with Non-Enum Type

**Problem:** ClassCastException when creating EnumSet.
**Cause:** Trying to create EnumSet for non-enum type.
**Impact:** Application crash.
**Solution:** Only use EnumSet with enum types.
**Prevention:** Use HashSet for non-enum types.

### Incident 3: Performance Degradation with Large Enums

**Problem:** Application slows with large enum.
**Cause:** EnumSet uses long[] array for enums with > 64 constants.
**Impact:** Increased memory usage.
**Solution:** Consider alternative for very large enums.
**Prevention:** Monitor memory usage.

## 10. Engineering Decision Framework

### When Should I Use This?
- Set of enum constants needed
- Maximum performance required
- Memory efficiency important
- Set operations (union, intersection) needed

### When Should I NOT Use This?
- **Non-enum elements needed**: Use HashSet
- **Null elements needed**: Use HashSet
- **Very large enums (> 64 constants)**: Memory grows with JumboEnumSet

### What Are the Alternatives?

| Alternative | When to Use | Trade-off |
|-------------|-------------|-----------|
| HashSet | Non-enum elements, null elements | Slower, more memory |
| LinkedHashSet | Insertion order with non-enum | Slower, insertion order |
| TreeSet | Sorted non-enum elements | O(log n), sorted |
| ConcurrentHashMap.newKeySet() | Concurrent enum set | Thread-safe |

### What Trade-offs Am I Making?
- **Speed vs Generality**: Enum-only, fastest vs general-purpose, slower
- **Memory vs Flexibility**: Very low memory vs more flexibility
- **Immutability vs Performance**: Mutable vs Set.of() (immutable, slower for enums)
- **Thread Safety**: Not thread-safe by default

### What Would I Choose in Production?
> Always use EnumSet for enum constants — it's the fastest Set implementation in Java. Use EnumSet.of() for small sets, EnumSet.range() for contiguous enums, EnumSet.complementOf() for "all except".

### Common Code Review Comments
- "This should be an EnumSet — you're using enum values as elements."
- "EnumSet is the fastest Set implementation — always use it for enums."
- "Consider using EnumSet.range() for contiguous enum values."
- "This EnumSet is being iterated concurrently — use Collections.synchronizedSet()."

### Common Production Mistakes

> Notice: EnumSet doesn't allow null elements — it will throw NullPointerException.

> Notice: EnumSet is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: EnumSet.of() is the most efficient factory method — use it instead of EnumSet.allOf() when possible.

> Notice: EnumSet is a bit-vector — it's the most memory-efficient Set implementation for enums.

## 11. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| NullPointerException | Debug logging | Check for null elements |
| ClassCastException | Debug logging | Check for enum type |
| Memory usage | Heap dump | Check bit vector size |

## 12. Code Review Checklist

- [ ] Using EnumSet for enum constants only
- [ ] No null elements added
- [ ] Thread safety handled
- [ ] Factory methods used (not constructors)
- [ ] Correct enum type specified

## 13. Architecture Considerations

### Where EnumSet Fits in System Design

| Layer | Use Case | Why EnumSet |
|-------|----------|-------------|
| Service Layer | Feature flags | Bit-vector O(1) operations |
| Configuration | Permission sets | Fast union/intersection |
| Event Processing | Event type filtering | Bitwise set operations |
| State Machine | State transitions | Fast membership check |
| API Gateway | Request type routing | O(1) contains |

### Integration Patterns

```
Client → API Gateway → EnumSet → Service → EnumSet → Client
                    ↓
            EnumSet → Flag Manager → EnumSet
```

### Scaling Considerations

| Scale | Recommendation |
|-------|----------------|
| < 64 constants | EnumSet with single long |
| 64 - 128 constants | EnumSet with long[] |
| > 128 constants | Consider HashSet |
| Non-enum types | Use HashSet |

### When to Replace EnumSet in Architecture

| Pattern | Replacement | Why |
|---------|-------------|-----|
| Non-enum elements | HashSet | General-purpose elements |
| Null elements needed | HashSet | EnumSet rejects nulls |
| Thread-safe set | ConcurrentHashMap.newKeySet() | Concurrent access |
| Sorted enums | TreeSet | Sorted iteration |

## 14. Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Memory exhaustion | OutOfMemoryError | Set max size |
| Null injection | NullPointerException | Validate inputs |

## 15. Evolution & Modernization

| Version | Change | Impact |
|---------|--------|--------|
| Java 5 | EnumSet introduced | High-performance enum set |
| Java 9 | Factory methods | Immutable enum sets |

## 16. Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| EnumSet | 5.0 | Stable |
| Factory methods | 5.0 | Stable |

## 17. Best Practices

1. Use factory methods, not constructors
2. Never add null elements
3. Use for enum constants only
4. Use complementOf for opposite sets
5. Use range for sequential enums

## 18. Common Mistakes

1. Using with non-enum types
2. Adding null elements
3. Using constructors instead of factory methods
4. Using HashSet for enum constants

## 19. Common Myths

### Myth 1: EnumSet is always faster than HashSet
**Reality:** For small enums yes, but for very large enums with many operations, HashSet may be competitive.

### Myth 2: EnumSet allows null
**Reality:** EnumSet throws NullPointerException for null.

### Myth 3: EnumSet is thread-safe
**Reality:** Not thread-safe. Use ConcurrentHashMap.newKeySet().

## 20. One-Minute Revision

- High-performance Set for enum types
- Uses bit vector (long or long[])
- O(1) for all operations
- No null elements allowed
- Factory methods: allOf, noneOf, of, range, complementOf
- Best for enum constant sets

## 21. Related Topics

| Topic | Relationship |
|-------|-------------|
| Enum | Element type requirement |
| HashSet | Alternative for non-enum |
| Bit vector | Internal implementation |
| Set operations | Union, intersection via bitwise |

## 22. Interview Questions

1. **How does EnumSet work internally?** — Uses bit vector (long or long[]). Each bit corresponds to an enum constant.

2. **What is the time complexity of EnumSet operations?** — O(1) for add/remove/contains.

3. **Does EnumSet allow null elements?** — No. Throws NullPointerException.

4. **What is the memory advantage of EnumSet?** — 8 bytes per enum constant vs ~40 bytes for HashSet.

5. **When should you use EnumSet?** — When working with sets of enum constants.

## 23. References

- [Oracle Java Documentation - EnumSet](https://docs.oracle.com/javase/8/docs/api/java/util/EnumSet.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
