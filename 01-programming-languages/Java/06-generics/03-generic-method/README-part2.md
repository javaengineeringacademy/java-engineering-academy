# 03 - Generic Methods (Part 2)

[📖 Back to Part 1](README.md)
 | [📖 Continue to Part 3](README-part3.md)

---

        System.out.println(numbers);  // [1, 2, 3]
        
```
        // Swap
        String[] arr = {"first", "second"};
        swap(arr, 0, 1);
        System.out.println(Arrays.toString(arr));  // [second, first]
    }
}
```

---

## Medium Example

### Bounded Generic Methods

```java
import java.util.List;
import java.util.Objects;

public class BoundedGenericMethods {
    
    public static <T extends Comparable<T>> T max(List<T> list) {
        Objects.requireNonNull(list, "List cannot be null");
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }
        
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            T current = list.get(i);
            if (current.compareTo(max) > 0) {
                max = current;
            }
        }
        return max;
    }
    
    public static <T extends Number> double sum(List<T> numbers) {
        return numbers.stream()
                      .mapToDouble(Number::doubleValue)
                      .sum();
    }
    
    public static <T extends Number & Comparable<T>> T clamp(
            T value, T min, T max) {
        if (value.compareTo(min) < 0) return min;
        if (value.compareTo(max) > 0) return max;
        return value;
    }
    
    public static void main(String[] args) {
        // max with different types
        List<String> names = List.of("Charlie", "Alice", "Bob");
        System.out.println(max(names));  // Charlie
        
        List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9);
        System.out.println(max(numbers));  // 9
        
        // sum
        List<Integer> ints = List.of(1, 2, 3, 4, 5);
        System.out.println(sum(ints));  // 15.0
        
        // clamp
        System.out.println(clamp(15, 0, 10));   // 10
        System.out.println(clamp(-5, 0, 10));    // 0
        System.out.println(clamp(5, 0, 10));     // 5
    }
}
```

---

## Hard Example

### Advanced Generic Method Patterns

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class AdvancedGenericMethods {
    
    // Method with both class-level and method-level type parameters
    public static <T, R> List<R> map(List<T> source, Function<T, R> mapper) {
        return source.stream().map(mapper).collect(Collectors.toList());
    }
    
    // Recursive generic method
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> T[] sort(T[] array) {
        Object[] temp = Arrays.copyOf(array, array.length);
        Arrays.sort(temp);
        return (T[]) temp;
    }
    
    // Generic method with complex bounds
    public static <T extends Comparable<T> & Serializable> T median(List<T> list) {
        List<T> sorted = list.stream()
                             .sorted()
                             .collect(Collectors.toList());
        int mid = sorted.size() / 2;
        return sorted.get(mid);
    }
    
    // Generic factory method
    public static <T> Supplier<T> memoize(Supplier<T> supplier) {
        T cached = null;
        boolean computed = false;
        return () -> {
            if (!computed) {
                cached = supplier.get();
                computed = true;
            }
            return cached;
        };
    }
    
    // Generic method with varargs and bounds
    @SafeVarargs
    public static <T extends Comparable<T>> List<T> sortedList(T... elements) {
        return Arrays.stream(elements)
                     .sorted()
                     .collect(Collectors.toList());
    }
    
    // Type-safe heterogeneous method
    public static <T> T checkedCast(Object obj, Class<T> type) {
        return type.cast(obj);
    }
    
    public static void main(String[] args) {
        // map
        List<String> words = List.of("hello", "world");
        List<Integer> lengths = map(words, String::length);
        System.out.println(lengths);  // [5, 5]
        
        // sortedList
        List<Integer> sorted = sortedList(5, 3, 1, 4, 2);
        System.out.println(sorted);  // [1, 2, 3, 4, 5]
        
        // median
        List<Integer> nums = List.of(7, 3, 1, 5, 9);
        System.out.println(median(nums));  // 5
        
        // checkedCast
        Object obj = "hello";
        String str = checkedCast(obj, String.class);
        System.out.println(str);  // hello
    }
}
```

---

## Enterprise Example

### Utility Class with Generic Methods

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public final class CollectionUtils {
    
    private CollectionUtils() {}
    
    public static <T> List<T> filter(List<T> source, Predicate<T> predicate) {
        return source.stream()
                     .filter(predicate)
                     .collect(Collectors.toList());
    }
    
    public static <T, R> Map<R, List<T>> groupBy(
            List<T> source, Function<T, R> keyExtractor) {
        return source.stream()
                     .collect(Collectors.groupingBy(keyExtractor));
    }
    
    public static <T, R> Map<T, R> toMap(
            List<T> source, Function<T, R> valueExtractor) {
        return source.stream()
                     .collect(Collectors.toMap(
                         Function.identity(), 
                         valueExtractor));
    }
    
    public static <T> Optional<T> findFirst(
            List<T> source, Predicate<T> predicate) {
        return source.stream()
                     .filter(predicate)
                     .findFirst();
    }
    
    public static <T> boolean anyMatch(
            List<T> source, Predicate<T> predicate) {
        return source.stream().anyMatch(predicate);
    }
    
    public static <T> boolean allMatch(
            List<T> source, Predicate<T> predicate) {
        return source.stream().allMatch(predicate);
    }
    
    @SafeVarargs
    public static <T> List<T> concat(List<T>... lists) {
        return Arrays.stream(lists)
                     .flatMap(Collection::stream)
                     .collect(Collectors.toList());
    }
    
    public static <T> Map<T, Integer> frequency(List<T> source) {
        return source.stream()
                     .collect(Collectors.toMap(
                         Function.identity(),
                         t -> 1,
                         Integer::sum));
    }
    
    public static <T> List<List<T>> partition(
            List<T> source, int size) {
        return IntStream.range(0, (source.size() + size - 1) / size)
                        .mapToObj(i -> source.subList(
                            i * size, 
                            Math.min((i + 1) * size, source.size())))
                        .collect(Collectors.toList());
    }
    
    public static void main(String[] args) {
        List<String> names = List.of(
            "Alice", "Bob", "Charlie", "David", "Eve");
        
        // filter
        List<String> longNames = filter(names, n -> n.length() > 3);
        System.out.println(longNames);  // [Alice, Charlie, David]
        
        // groupBy
        Map<Integer, List<String>> byLength = groupBy(names, String::length);
        System.out.println(byLength);  // {3=[Bob, Eve], 5=[Alice], 7=[Charlie, David]}
        
        // frequency
        List<String> fruits = List.of("apple", "banana", "apple", "cherry");
        Map<String, Integer> freq = frequency(fruits);
        System.out.println(freq);  // {apple=2, banana=1, cherry=1}
        
        // partition
        List<List<String>> partitions = partition(names, 2);
        System.out.println(partitions);  // [[Alice, Bob], [Charlie, David], [Eve]]
    }
}
```

---

## Performance

### Generic Method Overhead

| Aspect | Impact |
|--------|--------|
| Compile time | Minimal (type inference) |
| Runtime | Zero (type erasure) |
| Method dispatch | Same as non-generic |
| Inlining | JVM can inline generic methods |

### Type Inference Cost

```java
// Simple inference (cheap)
String s = identity("hello");

// Complex inference (still cheap at runtime)
// The compiler does the work, not the JVM
<T extends Comparable<T>> T max(T a, T b);
```

---

## Best Practices

1. **Place type parameters before return type** — `<T> T method()`, not `T <T> method()`
2. **Use bounded types when needed** — `<T extends Comparable<T>>`
3. **Let the compiler infer types** — Avoid explicit type arguments when possible
4. **Name type parameters meaningfully** — `E` for elements, `R` for return
5. **Prefer methods over classes** — When only one method needs generics
6. **Use `@SafeVarargs`** — For generic varargs methods

---

## Common Mistakes

### 1. Type Parameter Placement

```java
// WRONG
public T <T> identity(T value) { return value; }

// RIGHT
public <T> T identity(T value) { return value; }
```

### 2. Confusing Class and Method Type Parameters

```java
public class Box<T> {
    // Class-level: T
    private T value;
    
    // Method-level: U (independent)
    public <U> U convert(Function<T, U> fn) {
        return fn.apply(value);
    }
    
    // This would shadow class T (bad practice)
    public <T> T badMethod(T value) { return value; }
}
```

### 3. Using Raw Types in Generic Methods

```java
// WRONG
public static <T> List<T> bad(List raw) {
    return raw;  // Unchecked warning
}

// RIGHT
public static <T> List<T> good(List<T> raw) {
    return raw;
}
```

---

## Pitfalls

### 1. Type Erasure in Overloading

```java
// These have the SAME erasure - compile error!
public static <T> void process(List<T> list) { }
public static <T> void process(List<String> list) { }
// Erasure: both become process(List)
```

### 2. Generic Method in Generic Class

```java
public class Box<T> {
    // T is class-level
    private T value;
    
    // This T shadows class T
    public <T> T bad(T input) { return input; }
    
    // Use different name
    public <U> U convert(Function<T, U> fn) {
        return fn.apply(value);
    }
}
```

### 3. Varargs Heap Pollution

```java
// Potential heap pollution warning
public static <T> T[] bad(T... elements) {
    return elements;  // Warning: Possible heap pollution
}

// Safe varargs annotation
@SafeVarargs
public static <T> T[] safe(T... elements) {
    return elements;
}
```

---

## Debugging Tips

### 1. Check Type Inference

```java
// If type inference fails, provide explicit types
List<String> list = GenericMethodBasics.list("a", "b");
// vs
var list = GenericMethodBasics.list("a", "b");  // Inferred as List<String>
```

### 2. Read Compiler Errors

```
Error: incompatible types: String cannot be converted to Integer
// This tells you exactly which types are mismatched
// Check the generic method's type constraints
```

