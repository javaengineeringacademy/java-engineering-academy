```

### Method-Level Type Parameters

```java
public class Container<T> {
    // Class-level: T
    private T value;
    
    // Method-level: U (independent of T)
    public <U> U convert(Function<T, U> converter) {
        return converter.apply(value);
    }
}
```

---

## Memory Representation

### Generic Method Invocation

```java
public static <T> T identity(T value) {
    return value;
}

String s = identity("hello");  // T = String
Integer i = identity(42);      // T = Integer
```

**Stack frame:**
```
identity("hello"):
┌─────────────────────┐
│ value: reference ───┼──→ String "hello"
│ return: reference ──┼──→ String "hello" (same object)
└─────────────────────┘

identity(42):
┌─────────────────────┐
│ value: Integer ref ─┼──→ Integer 42 (boxed)
│ return: reference ──┼──→ Integer 42 (autoboxed)
└─────────────────────┘
```

---

## Syntax

### Basic Generic Method

```java
// Declaration
public static <T> ReturnType methodName(T parameter) {
    // method body
}

// Examples
public static <T> T identity(T value) { return value; }
public static <T> List<T> list(T... elements) { return Arrays.asList(elements); }
public static <T> void swap(T[] arr, int i, int j) {
    T temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}
```

### Bounded Generic Methods

```java
// Upper bounded
public static <T extends Number> double sum(List<T> numbers) { ... }

// Multiple bounds
public static <T extends Number & Comparable<T>> T max(List<T> numbers) { ... }

// Recursive bound
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

### Multiple Type Parameters

```java
public static <K, V> Map<K, V> of(K key, V value) {
    return Map.of(key, value);
}

public static <T1, T2, R> R combine(T1 a, T2 b, BiFunction<T1, T2, R> combiner) {
    return combiner.apply(a, b);
}
```

### Generic Methods in Non-Generic Classes

```java
public class StringUtils {
    // This class is NOT generic, but the method IS
    public static <T> List<T> asList(T... elements) {
        return Arrays.asList(elements);
    }
    
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }
}
```

---

## Easy Example

### Basic Generic Method

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenericMethodBasics {
    
    public static <T> T identity(T value) {
        return value;
    }
    
    public static <T> List<T> asList(T... elements) {
        return Arrays.asList(elements);
    }
    
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    public static void main(String[] args) {
        // Type inference
        String s = identity("hello");
        Integer i = identity(42);
        
        System.out.println(s);  // hello
        System.out.println(i);  // 42
        
        // Varargs
        List<String> strings = asList("a", "b", "c");
        List<Integer> numbers = asList(1, 2, 3);
        
        System.out.println(strings);  // [a, b, c]

---

[📖 Continue to Part 2](README-part2.md)
 | [📖 Continue to Part 3](README-part3.md)
```
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
