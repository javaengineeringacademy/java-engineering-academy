# Comparator

## Scope

This folder focuses exclusively on the Comparator interface.
Examples, exercises, and quizzes in this folder cover only Comparator concepts.

## Why It Exists

Before Comparator, customizing sort order required:

1. **Modifying the class**: Had to change compareTo() to change sort order
2. **Wrapper classes**: Create wrapper classes just for sorting
3. **No runtime flexibility**: Sort order fixed at compile time
4. **Single ordering**: Could only have one natural ordering per class

Comparator solved this by separating sorting logic from the class itself.

## What It Is

Comparator is an interface for custom ordering. It defines a comparison between two objects, independent of the objects' own compareTo() method.

```java
public interface Comparator<T> {
    int compare(T o1, T o2);
}
```

The compare method returns:
- Negative integer: o1 < o2
- Zero: o1 == o2
- Positive integer: o1 > o2

## Internal Working

```
When you call list.sort(comparator):
1. Sort algorithm calls comparator.compare() for comparisons
2. Comparator defines the ordering logic
3. Multiple comparators can define different orderings

Example:
  Comparator<Student> byGrade = (s1, s2) -> s1.getGrade() - s2.getGrade();
  Comparator<Student> byName = (s1, s2) -> s1.getName().compareTo(s2.getName());
  
  list.sort(byGrade);   // Sort by grade
  list.sort(byName);    // Sort by name
```

### compare Contract

```
The compare method must be:
1. Antisymmetric: compare(a, b) == -compare(b, a)
2. Transitive: if compare(a,b) > 0 && compare(b,c) > 0, then compare(a,c) > 0
3. Reflexive: compare(a, a) == 0
```

## Constructors

Comparator is an interface — no constructors. Create instances:

```java
// Lambda implementation
Comparator<Student> byGrade = (s1, s2) -> s1.getGrade() - s2.getGrade();

// Method reference
Comparator<Student> byName = Comparator.comparing(Student::getName);

// Chained comparators
Comparator<Student> complex = Comparator
    .comparing(Student::getGrade)
    .thenComparing(Student::getName);

// Reverse order
Comparator<Student> reverse = byGrade.reversed();
```

## Methods

| Method | Description | Returns |
|--------|-------------|---------|
| `compare(T o1, T o2)` | Compares two objects | negative, 0, or positive |
| `reversed()` | Returns reversed comparator | Comparator |
| `thenComparing()` | Chains comparators | Comparator |
| `thenComparingInt()` | Chains with int key | Comparator |
| `thenComparingLong()` | Chains with long key | Comparator |
| `thenComparingDouble()` | Chains with double key | Comparator |

## Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| compare() | O(1) typically | O(1) |
| Comparator.comparing() | O(1) setup | O(1) |
| Collections.sort() | O(n log n) | O(n) |

## Thread Safety

Comparator implementations should be stateless for thread safety:

```java
// Thread-safe: no shared state
Comparator<Student> byGrade = Comparator.comparingInt(Student::getGrade);

// Not thread-safe: uses shared mutable state
class UnsafeComparator implements Comparator<Student> {
    private int count = 0;  // Shared mutable state!
    
    public int compare(Student s1, Student s2) {
        count++;  // Race condition
        return s1.getGrade() - s2.getGrade();
    }
}
```

## Memory Behavior

```
Comparator adds no memory overhead:
- No extra fields in implementing class
- No wrapper objects created
- Lambda comparators are instantiated once

Memory impact: 0 bytes per comparison
```

## Production Incidents

1. **NPE from null comparisons**: Comparator doesn't handle nulls
   - Result: NullPointerException during sort
   - Fix: Use Comparator.nullsFirst() or nullsLast()

2. **Inconsistent ordering**: Comparator not transitive
   - Result: Sort produces unpredictable results
   - Fix: Ensure transitivity in compare() logic

## Engineering Decision Framework

### When Should I Use This?

- Class needs multiple sort orders
- You don't own the class
- Sort order depends on context
- Need runtime flexibility

### When Should I NOT Use This?

- Class has single natural ordering
- Comparable is already implemented
- Need only one sort order

### What Are the Alternatives?

| Approach | Use When |
|----------|----------|
| Comparable | Single natural ordering |
| Comparator | Multiple orderings, runtime flexibility |
| Comparator.comparing() | Functional style, lambdas |

### Common Code Review Comments

1. "Handle nulls with Comparator.nullsFirst()"
2. "Use Comparator.comparing() instead of raw compare()"
3. "Make comparators stateless for thread safety"
4. "Document sort order (ascending/descending)"

## Performance

| Operation | Comparator | Comparable |
|-----------|------------|------------|
| Setup | Create comparator | Implement interface |
| Maintenance | Independent of class | Changes affect sort |
| Flexibility | Multiple orderings | Single ordering |
| Performance | Same | Same |

## Debugging Tips

1. **NPE during sort**: Add null handling with nullsFirst/nullsLast
2. **Wrong sort order**: Check compare() return sign
3. **ClassCastException**: Ensure all elements are comparable
4. **Infinite loop**: Check transitivity

## Code Review Checklist

- [ ] Comparator is stateless (no shared mutable state)
- [ ] Handles null values (or documents non-null contract)
- [ ] compare() is antisymmetric, transitive, reflexive
- [ ] Uses Comparator.comparing() for readability
- [ ] Document sort order (ascending/descending)

## Architecture Considerations

- Where it fits in system design: Sorting strategy pattern
- Integration patterns: Works with Collections.sort(), TreeSet, TreeMap
- Scaling considerations: No impact — compare() is O(1)

## Security Considerations

- Comparator implementations should not have side effects
- compare() should not access external resources
- Ensure compare() terminates — no infinite loops

## Evolution & Modernization

| Version | Change |
|---------|--------|
| JDK 1.2 | Comparator interface introduced |
| JDK 1.8 | Default methods (reversed(), thenComparing()) |
| JDK 11 | Comparator.comparingInt() with toIntByKey |

```java
// Modern: Method references and chaining
Comparator<Student> complex = Comparator
    .comparing(Student::getGrade)
    .reversed()
    .thenComparing(Student::getName);
```

## Version Validation

```java
// Test in Java 21
Comparator<Student> byGrade = Comparator.comparingInt(Student::getGrade);
Student s1 = new Student("Alice", 90);
Student s2 = new Student("Bob", 85);
assert byGrade.compare(s1, s2) > 0;
```

## Best Practices

1. Use Comparator.comparing() for readability
2. Handle nulls with nullsFirst() or nullsLast()
3. Make comparators stateless
4. Use method references when possible

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| NPE on nulls | NullPointerException | Use nullsFirst/nullsLast |
| Mutable state | Race conditions | Keep comparators stateless |
| Raw compare() | Less readable | Use Comparator.comparing() |
| Wrong sign | Reversed sort order | Check return value logic |

## Common Myths

1. "Comparator is always better than Comparable" — False. Comparable is simpler for single ordering.
2. "Comparator.comparing() is always better" — False. Raw compare() can be more efficient.
3. "Comparator is thread-safe" — False. Implementations with shared state are not.

## One-Minute Revision

```
Comparator defines custom ordering separate from the class.

Key points:
- int compare(T o1, T o2) method
- Returns negative, 0, or positive
- Multiple comparators per class
- Created via lambdas or method references

When to use:
- Multiple sort orders needed
- You don't own the class
- Runtime flexibility required

Modern style:
- Comparator.comparing(Student::getGrade)
- .thenComparing(Student::getName)
- .reversed()
```

## Related Topics

| Topic | Relationship |
|-------|--------------|
| Comparable | Alternative for natural ordering |
| Collections.sort() | Uses Comparator |
| TreeSet | Sorted by Comparator |
| TreeMap | Keys sorted by Comparator |

## Interview Questions

1. What is the difference between Comparable and Comparator?
2. How do you create a Comparator using lambdas?
3. What is Comparator.comparing()?
4. How do you handle nulls in Comparator?
5. What is the contract of compare()?
6. How do you chain multiple comparators?
7. What is the difference between compare() and compareTo()?
8. Is Comparator thread-safe?

## References

- Oracle Java SE Documentation: Comparator
- OpenJDK Source: java/util/Comparator.java
- JLS Section 15.20.1: The Type-Comparison Operator instanceof
- Effective Java, Item 14: Consider implementing Comparable
