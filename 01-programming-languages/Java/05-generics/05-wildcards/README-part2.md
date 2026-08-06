# 05 - Wildcards (Part 2)

[📖 Back to Part 1](README.md)
 | [📖 Continue to Part 3](README-part3.md)

---

        List<Double> doubles = List.of(1.0, 2.0, 3.0);
        System.out.println(sum(ints));     // 6.0
        System.out.println(sum(doubles));  // 6.0
        
        // Lower bounded wildcard
        java.util.List<Integer> intList = new java.util.ArrayList<>();
        java.util.List<Number> numList = new java.util.ArrayList<>();
        addIntegers(intList);  // OK
        addIntegers(numList);  // OK
    }
}
```

---

## Medium Example

### PECS Principle in Action

```java
import java.util.ArrayList;
import java.util.List;

public class pecsExample {
    
    // Producer Extends: reading from src
    public static <T> List<T> copy(List<? extends T> src) {
        List<T> dest = new ArrayList<>();
        for (T item : src) {
            dest.add(item);
        }
        return dest;
    }
    
    // Consumer Super: writing to dest
    public static <T> void fill(List<? super T> dest, T value, int count) {
        for (int i = 0; i < count; i++) {
            dest.add(value);
        }
    }
    
    // Both: reading from src, writing to dest
    public static <T> void transfer(
            List<? super T> dest, 
            List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }
    
    // Complex PECS example
    public static <T extends Comparable<T>> T findMin(List<? extends T> list) {
        T min = list.get(0);
        for (T item : list) {
            if (item.compareTo(min) < 0) {
                min = item;
            }
        }
        return min;
    }
    
    public static void main(String[] args) {
        // copy (Producer Extends)
        List<Integer> ints = List.of(1, 2, 3);
        List<Number> copied = copy(ints);
        System.out.println(copied);  // [1, 2, 3]
        
        // fill (Consumer Super)
        List<Object> objects = new ArrayList<>();
        fill(objects, "hello", 3);
        System.out.println(objects);  // [hello, hello, hello]
        
        // transfer (Both)
        List<Number> dest = new ArrayList<>();
        List<Integer> src = List.of(10, 20, 30);
        transfer(dest, src);
        System.out.println(dest);  // [10, 20, 30]
        
        // findMin
        List<Double> doubles = List.of(3.14, 2.71, 1.41);
        System.out.println(findMin(doubles));  // 1.41
    }
}
```

---

## Hard Example

### Wildcard Capture and Complex Patterns

```java
import java.util.ArrayList;
import java.util.List;

public class AdvancedWildcards {
    
    // Wildcard capture
    public static void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }
    
    private static <T> void swapHelper(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    // Complex wildcard with multiple bounds
    public static <T extends Number & Comparable<T>> T max(List<? extends T> list) {
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }
    
    // Wildcard capture with return type
    public static List<?> filter(List<?> list, java.util.function.Predicate<Object> predicate) {
        List<Object> result = new ArrayList<>();
        for (Object item : list) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
    
    // Nested wildcards
    public static <T> void copyAll(
            List<? super T> dest, 
            List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }
    
    // Complex nested generic with wildcards
    public static <T> List<T> flatten(List<List<? extends T>> lists) {
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists) {
            result.addAll(list);
        }
        return result;
    }
    
    public static void main(String[] args) {
        // swap
        List<String> strings = new ArrayList<>(List.of("a", "b", "c"));
        swap(strings, 0, 2);
        System.out.println(strings);  // [c, b, a]
        
        // max with multiple bounds
        List<Integer> nums = List.of(3, 1, 4, 1, 5, 9);
        System.out.println(max(nums));  // 9
        
        // filter
        List<Number> numbers = List.of(1, 2.0, 3L, 4.0f);
        List<?> evens = filter(numbers, n -> {
            if (n instanceof Integer i) return i % 2 == 0;
            if (n instanceof Double d) return d % 2 == 0;
            return false;
        });
        System.out.println(evens);  // [2.0, 4.0]
        
        // flatten
        List<List<Integer>> nested = List.of(
            List.of(1, 2),
            List.of(3, 4),
            List.of(5, 6)
        );
        List<Integer> flat = flatten(nested);
        System.out.println(flat);  // [1, 2, 3, 4, 5, 6]
    }
}
```

---

## Enterprise Example

### Type-Safe Collection Utilities

```java
import java.util.*;
import java.util.function.*;

public final class CollectionUtils {
    
    private CollectionUtils() {}
    
    // Producer Extends: read-only access
    public static <T> Optional<T> findFirst(
            List<? extends T> source, 
            Predicate<? super T> predicate) {
        return source.stream().filter(predicate).findFirst();
    }
    
    public static <T> boolean allMatch(
            List<? extends T> source, 
            Predicate<? super T> predicate) {
        return source.stream().allMatch(predicate);
    }
    
    public static <T> boolean anyMatch(
            List<? extends T> source, 
            Predicate<? super T> predicate) {
        return source.stream().anyMatch(predicate);
    }
    
    // Consumer Super: write-only access
    public static <T> void addAll(
            List<? super T> dest, 
            Collection<? extends T> src) {
        dest.addAll(src);
    }
    
    public static <T> List<T> newFilledList(int size, T value) {
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(value);
        }
        return list;
    }
    
    // Both: read and write
    public static <T> List<T> filter(
            List<? extends T> source, 
            Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : source) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
    
    public static <T, R> List<R> map(
            List<? extends T> source, 
            Function<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>();
        for (T item : source) {
            result.add(mapper.apply(item));
        }
        return result;
    }
    
    // Complex wildcard usage
    public static <T extends Comparable<T>> List<T> sorted(
            List<? extends T> source) {
        return source.stream().sorted().toList();
    }
    
    public static <T> Map<Boolean, List<T>> partition(
            List<? extends T> source, 
            Predicate<? super T> predicate) {
        Map<Boolean, List<T>> result = new HashMap<>();
        result.put(true, new ArrayList<>());
        result.put(false, new ArrayList<>());
        for (T item : source) {
            result.get(predicate.test(item)).add(item);
        }
        return result;
    }
    
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // filter with wildcard
        List<Integer> evens = filter(numbers, n -> n % 2 == 0);
        System.out.println(evens);  // [2, 4, 6, 8, 10]
        
        // map with wildcard
        List<String> strings = map(numbers, n -> "Num: " + n);
        System.out.println(strings);  // [Num: 1, Num: 2, ...]
        
        // partition
        Map<Boolean, List<Integer>> partitioned = 
            partition(numbers, n -> n % 2 == 0);
        System.out.println("Evens: " + partitioned.get(true));
        System.out.println("Odds: " + partitioned.get(false));
        
        // findFirst
        Optional<Integer> first = findFirst(numbers, n -> n > 5);
        System.out.println(first.orElse(0));  // 6
    }
}
```

---

## Performance

### Wildcard Overhead

| Aspect | Impact |
|--------|--------|
| Compile time | Slightly more (wildcard checking) |
| Runtime | Zero (erased) |
| Method calls | Identical |
| Memory | Identical |

### PECS Efficiency

```java
// Wildcards allow efficient API design
// No unnecessary copying or casting
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (T item : src) {  // Efficient iteration
        dest.add(item);   // Direct add, no cast
    }
}
```

---

## Best Practices

1. **Apply PECS consistently** — Producer Extends, Consumer Super
2. **Prefer wildcards over type parameters** — When you don't need to name the type
3. **Use `? extends T` for read-only access** — When you only read from a collection
4. **Use `? super T` for write-only access** — When you only write to a collection
5. **Use `<?>` for truly unknown types** — When you only use Object methods
6. **Document wildcard usage** — Explain why wildcards are used

---

## Common Mistakes

### 1. Adding to Upper Bounded List

```java
// WRONG - can't add to ? extends
public static <T> void bad(List<? extends T> list, T item) {
    list.add(item);  // Compile error!
}

// RIGHT - use type parameter for writing
public static <T> void good(List<T> list, T item) {
    list.add(item);  // OK
}
```

### 2. Reading from Lower Bounded List

```java
// WRONG - can only read as Object
public static <T> T bad(List<? super T> list) {
    return list.get(0);  // Returns Object, not T
}

// RIGHT - use type parameter for reading
public static <T> T good(List<T> list) {
    return list.get(0);  // Returns T
}
```

### 3. Confusing Wildcards and Type Parameters

```java
// Type parameter: you CREATE or RETURN values of type T
public static <T> T identity(T value) { return value; }

// Wildcard: you only READ values of type T
public static boolean isNull(List<?> list) { return list.isEmpty(); }
```

---

## Pitfalls

### 1. Wildcard Capture Limitations

```java
// This doesn't work:
List<?> list = List.of(1, 2, 3);
list.set(0, 42);  // Compile error! Can't add to ?

// Workaround: wildcard capture
public static <T> void set(List<?> list, int index, T value) {
    setHelper(list, index, value);
}
private static <T> void setHelper(List<T> list, int index, T value) {
    list.set(index, value);
}
```

### 2. Nested Wildcards

```java
// Nested wildcards can be confusing
List<? extends List<?>> nested = List.of(List.of(1), List.of(2));
// What can you do with this?

// Better approach: use explicit type parameters
public static <T> List<T> flatten(List<List<? extends T>> lists) {
    List<T> result = new ArrayList<>();
