# Comparable

## Scope

This folder focuses exclusively on the Comparable interface.
Examples, exercises, and quizzes in this folder cover only Comparable concepts.

## Why It Exists

Before Comparable, sorting objects required external logic:

1. **Scattered sorting code**: Each developer wrote their own sort logic
2. **Inconsistent ordering**: Same objects sorted differently in different places
3. **No natural order**: No way to define a default sort for a class
4. **API friction**: Collections.sort() had no way to sort custom objects automatically

Comparable solved this by letting a class define its own natural ordering.

## What It Is

Comparable is an interface that defines natural ordering for a class. A class implements Comparable to indicate its instances can be compared to other instances of the same type.

```java
public interface Comparable<T> {
    int compareTo(T o);
}
```

The compareTo method returns:
- Negative integer: `this` < `o`
- Zero: `this` == `o`
- Positive integer: `this` > `o`

## Internal Working

```
When you call Collections.sort(list):
1. List uses compareTo() method of elements
2. Comparison-based sort (TimSort in Java 7+)
3. Each comparison calls compareTo()

Example:
  Student s1 = new Student("Alice", 90);
  Student s2 = new Student("Bob", 85);
  s1.compareTo(s2);  // Returns positive (90 > 85)
```

### compareTo Contract

```
The compareTo method must be:
1. Reflexive: x.compareTo(x) == 0
2. Antisymmetric: x.compareTo(y) == -y.compareTo(x)
3. Transitive: if x.compareTo(y) > 0 && y.compareTo(z) > 0, then x.compareTo(z) > 0
4. Consistent with equals: x.compareTo(y) == 0 iff x.equals(y)
```

## Constructors

Comparable is an interface — no constructors. Implement it in your class:

```java
public class Student implements Comparable<Student> {
    private String name;
    private int grade;
    
    @Override
    public int compareTo(Student other) {
        return this.grade - other.grade;  // Ascending by grade
    }
}
```

## Methods

| Method | Description | Returns |
|--------|-------------|---------|
| `compareTo(T o)` | Compares this object with another | negative, 0, or positive |

## Complexity Table

| Operation | Time | Space |
|-----------|------|-------|
| compareTo() | O(1) typically | O(1) |
| Collections.sort() | O(n log n) | O(n) |
| Arrays.sort() | O(n log n) | O(log n) |

## Thread Safety

Comparable itself has no thread-safety concerns. However:
- If compareTo() accesses shared mutable state, it's not thread-safe
- Collections.sort() is not thread-safe — use synchronization

```java
// Not thread-safe
List<Student> students = getStudents();
Collections.sort(students);  // Requires external synchronization

// Thread-safe options
synchronized (students) {
    Collections.sort(students);
}
```

## Memory Behavior

```
Comparable adds no memory overhead:
- No extra fields in implementing class
- No wrapper objects created
- Just adds compareTo() method to class

Memory impact: 0 bytes per instance
```

## Production Incidents

1. **Inconsistent compareTo/equals**: Class where compareTo() returns 0 but equals() returns false
   - Result: TreeSet behaves unpredictably, duplicates allowed
   - Fix: Always ensure compareTo() is consistent with equals()

2. **ClassCastException in sort**: Comparable objects mixed with non-Comparable
   - Result: Runtime exception during sort
   - Fix: Ensure all objects in collection implement Comparable

## Engineering Decision Framework

### When Should I Use This?

- Class has a single, obvious natural ordering
- You want Collections.sort() to work without providing a Comparator
- Ordering is inherent to the domain (Student by grade, Employee by ID)

### When Should I NOT Use This?

- Class needs multiple sort orders (by name, by grade, by date)
- Ordering depends on context (different sorting in different views)
- You don't own the class (can't modify to implement interface)

### What Are the Alternatives?

| Approach | Use When |
|----------|----------|
| Comparable | Single natural ordering needed |
| Comparator | Multiple orderings needed |
| Comparator.comparing() | Functional style, lambdas |

### Common Code Review Comments

1. "compareTo() must be consistent with equals()"
2. "Handle null comparisons — or document non-null contract"
3. "Consider overflow for int subtraction — use Integer.compare()"
4. "Prefer Comparator.comparing() for new code"

## Performance

| Operation | Comparable | Comparator |
|-----------|------------|------------|
| Setup | Implement interface | Create separate class/lambda |
| Maintenance | Changes to class affect sort | Independent of class |
| Flexibility | Single ordering | Multiple orderings |
| Performance | Same | Same |

## Debugging Tips

1. **compareTo returns wrong sign**: Check subtraction order
2. **ClassCastException**: Ensure all elements are Comparable
3. **Infinite loop**: Check transitivity — a<b, b<c, c<a creates cycle
4. **Unsorted result**: Check if compareTo() implementation is correct

## Code Review Checklist

- [ ] compareTo() is consistent with equals()
- [ ] Handles null comparisons (or documents non-null contract)
- [ ] Handles integer overflow in subtraction
- [ ] Transitivity is maintained
- [ ] compareTo() is reflexive (x.compareTo(x) == 0)

## Architecture Considerations

- Where it fits in system design: Domain model defines natural ordering
- Integration patterns: Works with Collections.sort(), TreeSet, TreeMap
- Scaling considerations: No impact — compareTo() is O(1)

## Security Considerations

- Comparable implementations should not have side effects
- compareTo() should not access external resources
- Ensure compareTo() terminates — no infinite loops

## Evolution & Modernization

| Version | Change |
|---------|--------|
| JDK 1.2 | Comparable interface introduced |
| JDK 1.8 | Default methods added (reversed(), thenComparing()) |

```java
// Modern: Use Comparator.comparing() instead
Comparator<Student> byGrade = Comparator.comparingInt(Student::getGrade);
```

## Version Validation

```java
// Test in Java 21
Student s1 = new Student("Alice", 90);
Student s2 = new Student("Bob", 85);
assert s1.compareTo(s2) > 0;
```

## Best Practices

1. Always be consistent with equals()
2. Use Integer.compare() instead of subtraction to avoid overflow
3. Document null-handling behavior
4. Consider using Comparator.comparing() for new code

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Inconsistent with equals | HashSet/TreeSet conflicts | Align compareTo() with equals() |
| Integer overflow | Subtraction wraps around | Use Integer.compare() |
| Missing null check | NullPointerException | Handle null or document contract |
| Non-transitive | Infinite loops in sort | Ensure a<b && b<c → a<c |

## Common Myths

1. "Comparable is always better than Comparator" — False. Comparator is better for multiple orderings.
2. "compareTo() must use subtraction" — False. Use Integer.compare() or manual logic.
3. "Comparable is thread-safe" — False. The method itself may be, but sort operations aren't.

## One-Minute Revision

```
Comparable defines natural ordering for a class.

Key points:
- int compareTo(T o) method
- Returns negative, 0, or positive
- Must be consistent with equals()
- Used by Collections.sort(), TreeSet, TreeMap
- Single ordering per class

When to use:
- Class has obvious natural order
- You own the class

When NOT to use:
- Multiple orderings needed
- You don't own the class
```

## Related Topics

| Topic | Relationship |
|-------|--------------|
| Comparator | Alternative for multiple orderings |
| Collections.sort() | Uses Comparable |
| TreeSet | Sorted by Comparable |
| TreeMap | Keys sorted by Comparable |

## Interview Questions

1. What is the difference between Comparable and Comparator?
2. What contract must compareTo() follow?
3. Why must compareTo() be consistent with equals()?
4. How do you handle null values in compareTo()?
5. What happens if compareTo() causes integer overflow?
6. Can you sort a list without Comparable or Comparator?
7. What is the time complexity of compareTo()?
8. How does Comparable differ in Java 8+?

## References

- Oracle Java SE Documentation: Comparable
- OpenJDK Source: java/lang/Comparable.java
- JLS Section 15.20.1: The Type-Comparison Operator instanceof
- Effective Java, Item 12: Always override toString
