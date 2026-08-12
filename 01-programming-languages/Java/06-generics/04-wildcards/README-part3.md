                    list.set(j + 1, temp);
                }
            }
        }
    }
    
    // Complex recursive bound
    public static <T extends Comparable<T>> List<T> mergeSorted(
            List<T> list1, List<T> list2) {
        List<T> result = new java.util.ArrayList<>();
        int i = 0, j = 0;
        
        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i).compareTo(list2.get(j)) <= 0) {
                result.add(list1.get(i++));
            } else {
                result.add(list2.get(j++));
            }
        }
        
        while (i < list1.size()) result.add(list1.get(i++));
        while (j < list2.size()) result.add(list2.get(j++));
        
        return result;
    }
    
    public static void main(String[] args) {
        List<Integer> list1 = List.of(1, 3, 5);
        List<Integer> list2 = List.of(2, 4, 6);
        
        List<Integer> merged = mergeSorted(list1, list2);
        System.out.println("Merged: " + merged);  // [1, 2, 3, 4, 5, 6]
        
        List<Integer> unsorted = List.of(5, 3, 1, 4, 2);
        sort(unsorted);
        System.out.println("Sorted: " + unsorted);  // [1, 2, 3, 4, 5]
    }
}
```

---

## Enterprise Example

### Type-Safe Comparison Framework

```java
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class ComparisonUtils {
    
    private ComparisonUtils() {}
    
    public static <T extends Comparable<T>> Comparator<T> naturalOrder() {
        return Comparator.naturalOrder();
    }
    
    public static <T> Comparator<T> comparing(
            Function<? super T, ? extends Comparable<?>> keyExtractor) {
        return Comparator.comparing(keyExtractor);
    }
    
    public static <T extends Comparable<T>> boolean inRange(
            T value, T min, T max) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }
    
    public static <T extends Number & Comparable<T>> T clamp(
            T value, T min, T max) {
        if (value.compareTo(min) < 0) return min;
        if (value.compareTo(max) > 0) return max;
        return value;
    }
    
    public static <T extends Comparable<T>> T requireInRange(
            T value, T min, T max, String name) {
        if (!inRange(value, min, max)) {
            throw new IllegalArgumentException(
                String.format("%s must be between %s and %s, got %s",
                    name, min, max, value));
        }
        return value;
    }
    
    public static <T extends Comparable<T>> int compareNullsLast(
            T a, T b) {
        if (a == null && b == null) return 0;
        if (a == null) return 1;
        if (b == null) return -1;
        return a.compareTo(b);
    }
    
    public static <T extends Comparable<T>> List<T> sortedCopy(List<T> list) {
        return list.stream()
                   .sorted()
                   .toList();
    }
    
    public static <T extends Comparable<T>> boolean isUnique(List<T> list) {
        return list.size() == list.stream().distinct().count();
    }
    
    public static void main(String[] args) {
        System.out.println(inRange(5, 1, 10));     // true
        System.out.println(inRange(15, 1, 10));    // false
        
        System.out.println(clamp(15, 0, 10));      // 10
        System.out.println(clamp(-5, 0, 10));      // 0
        System.out.println(clamp(5, 0, 10));       // 5
        
        List<String> names = List.of("Charlie", "Alice", "Bob");
        System.out.println(sortedCopy(names));  // [Alice, Bob, Charlie]
        
        System.out.println(isUnique(List.of(1, 2, 3)));  // true
        System.out.println(isUnique(List.of(1, 2, 2)));  // false
    }
}
```

---

## Performance

### Bounded vs Unbounded

| Aspect | Bounded | Unbounded |
|--------|---------|-----------|
| Compile time | Slightly more (bound checking) | Less |
| Runtime | Identical (erased) | Identical (erased) |
| Method calls | Same (virtual dispatch) | Same |
| Memory | Identical | Identical |

### Multiple Bounds Cost

```java
// Multiple bounds add compile-time checks only
public static <T extends Number & Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;  // No runtime overhead
}
```

---

## Best Practices

1. **Use the most specific bound** — `<T extends Comparable<T>>` over `<T extends Object>`
2. **Keep bounds minimal** — Only require what you actually use
3. **Document bound rationale** — Why is this bound necessary?
4. **Prefer interfaces over classes** — More flexible for users
5. **Use recursive bounds** — When type must relate to itself

---

## Common Mistakes

### 1. Wrong Bound Order

```java
// WRONG - interface first
public static <T extends Comparable<T> & Number> T bad(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;  // Compile error!
}

// RIGHT - class first
public static <T extends Number & Comparable<T>> T good(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

### 2. Over-Constraining

```java
// TOO RESTRICTIVE - only works with Number
public static <T extends Number> boolean equals(T a, T b) {
    return a.equals(b);  // Works, but why restrict to Number?
}

// BETTER - works with any Comparable
public static <T extends Comparable<T>> boolean equals(T a, T b) {
    return a.compareTo(b) == 0;
}
```

### 3. Missing Bound

```java
// WRONG - can't call compareTo
public static <T> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;  // Compile error!
}

// RIGHT - add Comparable bound
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

---

## Pitfalls

### 1. Type Erasure with Bounds

```java
Box<Number> box = new Box<>();
box.setValue(42);  // OK: Integer is Number

// But you can't do this (compile error):
// box.setValue("hello");  // String is not Number
// This is enforced at compile time, not runtime!
```

### 2. Multiple Bounds and Erasure

```java
// After erasure, T becomes Number (first bound)
public static <T extends Number & Comparable<T>> T max(T a, T b) {
    // At runtime: a and b are Number, not T
    // The Comparable constraint is gone!
}
```

### 3. Wildcard Capture

```java
// This doesn't work as expected:
public static <T extends Number> void add(List<? extends Number> list, T item) {
    list.add(item);  // Compile error! Can't add to ? extends
}
```

---

## Debugging Tips

### 1. Check Bound Satisfaction

```java
// If you get "T is not within its bound"
// Check: does T satisfy ALL bounds?
public static <T extends Number & Comparable<T>> void method(T item) {
    // T must be Number AND Comparable<T>
    // If T doesn't satisfy both, you'll get a compile error
}
```

### 2. Read Error Messages

```
Error: type argument T is not within bounds of type-variable T
// T (the argument) doesn't extend the bound type
// Check your type parameter declaration
```

### 3. Use IDE Inspection

```java
// IntelliJ: Hover over type parameter to see bound
// Eclipse: Open Declaration
```

### 4. Verify with Reflection

```java
// Check generic type info
TypeVariable<?>[] typeParams = MyClass.class.getTypeParameters();
for (TypeVariable<?> param : typeParams) {
    System.out.println(param.getName() + " extends " + 
        Arrays.toString(param.getBounds()));
}
```

---

## Comparison Table

| Feature | Unbounded `<T>` | Bounded `<T extends X>` | Multiple Bounds `<T extends A & B>` |
|---------|-----------------|-------------------------|-------------------------------------|
| Flexibility | Maximum | Limited to X | Limited to A + B |
| Methods available | Object methods only | X's methods | A's + B's methods |
| Type safety | Basic | Enhanced | Most restrictive |
| Use case | Generic storage | Type-specific operations | Complex constraints |

---

## Decision Tree

```
Does your generic type need to call specific methods?
├── No → Use unbounded <T>
└── Yes → What methods?
    ├── compareTo → <T extends Comparable<T>>
    ├── doubleValue/intValue → <T extends Number>
    ├── toString → <T extends Object> (or just <T>)
    └── Multiple → <T extends A & B & C>
```

---

## Interview Questions

### Q1: What is a bounded type parameter?

**A:** A bounded type parameter restricts the types that can be used as generic arguments. Using `extends`, you specify an upper bound: `<T extends Number>` means T must be Number or a subclass of Number.

### Q2: What's the difference between `<T extends Comparable>` and `<T extends Comparable<T>>`?

**A:** `<T extends Comparable>` means T implements raw Comparable. `<T extends Comparable<T>>` means T implements Comparable<T> — it can be compared to itself. The latter is more type-safe and allows calling `compareTo(T)` without casting.

### Q3: Can you have multiple bounds?
```
# 04 - Bounded Type Parameters (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---


**A:** Yes, using `&`: `<T extends Number & Comparable<T> & Serializable>`. The class (if any) must come first, followed by interfaces.

### Q4: What is a recursive type bound?

**A:** A recursive bound is when a type parameter bounds itself: `<T extends Comparable<T>>`. This means T must be comparable to instances of its own type.

### Q5: How do bounds affect type erasure?

**A:** After erasure, the type parameter is replaced by its first bound. `<T extends Number & Comparable<T>>` erases to `Number`. Subsequent bounds are erased completely.

---

## Exercises

### Exercise 1: Bounded Sum

Write a generic method `sum(List<T> list)` that returns the sum of all elements. The type parameter should be bounded to `Number`.

### Exercise 2: Comparable Search

Write a generic method `binarySearch(T[] array, T target)` that performs binary search using `Comparable`.

### Exercise 3: Multiple Bounds

Write a generic method that finds the maximum of two values, where the type must be both `Number` and `Comparable`.

---

## Assignments

### Assignment 1: Type-Safe Validator

Create a `Validator<T>` class that:
- Accepts rules that implement `Predicate<T>`
- Validates values against all rules
- Provides meaningful error messages
- Works with any type that implements `Comparable`

### Assignment 2: Generic Range

Create a `Range<T extends Comparable<T>>` class that:
- Represents a range from min to max
- Supports `contains(T value)`
- Supports `overlaps(Range<T> other)`
- Supports `intersection(Range<T> other)`
- Implements `Comparable<Range<T>>`

---

## Mini Project

### Type-Safe Math Library

Build a generic math library that works with different numeric types:

1. `MathUtils<T extends Number & Comparable<T>>` class
2. Methods: `max`, `min`, `sum`, `average`, `clamp`
3. Support for `Integer`, `Long`, `Double`, `BigDecimal`
4. Type-safe conversion methods
5. Range operations

---

## Summary

Bounded type parameters are essential for writing generic code that requires specific capabilities:

1. **`extends` keyword** — Specifies upper bound
2. **Multiple bounds** — Use `&` for multiple constraints
3. **Recursive bounds** — `<T extends Comparable<T>>` for self-referencing types
4. **Compile-time safety** — Bounds are checked at compile time
5. **No runtime overhead** — Bounds are erased at compile time

Bounded types enable type-safe operations while maintaining generic flexibility.

---

## References

- [Oracle - Bounded Type Parameters](https://docs.oracle.com/en/java/javase/21/java/generics/bounded.html)
- [Java Language Specification §4.5 - Type Parameters](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.5)
- [Effective Java - Item 30: Use bounded wildcards to increase API flexibility](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Angelika Langer - Generics FAQ](https://www.angelikalanger.com/GenericsFAQ/)
