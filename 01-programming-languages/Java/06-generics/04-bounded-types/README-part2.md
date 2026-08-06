# 04 - Bounded Type Parameters (Part 2)

[📖 Back to Part 1](README.md)
 | [📖 Continue to Part 3](README-part3.md)

---

        
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(max) > 0) {
                max = list.get(i);
            }
        }
        return max;
    }
    
    public static <T extends Comparable<T>> T min(List<T> list) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Empty list");
        }
        
        T min = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(min) < 0) {
                min = list.get(i);
            }
        }
        return min;
    }
    
    public static <T extends Comparable<T>> boolean isSorted(List<T> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1).compareTo(list.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) {
        List<String> names = List.of("Charlie", "Alice", "Bob");
        System.out.println("Max: " + max(names));  // Charlie
        System.out.println("Min: " + min(names));  // Alice
        
        List<Integer> sorted = List.of(1, 2, 3, 4, 5);
        List<Integer> unsorted = List.of(5, 3, 1, 4, 2);
        System.out.println("Sorted: " + isSorted(sorted));    // true
        System.out.println("Unsorted: " + isSorted(unsorted)); // false
    }
}
```

---

## Hard Example

### Multiple Bounds with Complex Constraints

```java
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class AdvancedBounds {
    
    // Multiple bounds: Number + Comparable + Serializable
    public static <T extends Number & Comparable<T> & Serializable> 
            T max(List<T> list) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Empty list");
        }
        
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(max) > 0) {
                max = list.get(i);
            }
        }
        return max;
    }
    
    // Recursive bound with additional constraint
    public static <T extends Comparable<T>> void sort(List<T> list) {
        // Bubble sort using Comparable
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    // Swap
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
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
