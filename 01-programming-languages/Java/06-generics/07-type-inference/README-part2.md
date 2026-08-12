### No Impact from Best Practices

Best practices are compile-time guidelines only:

```java
// These have identical memory layout
Box<String> good = new Box<>();
Box raw = new Box<>();

// Type safety is enforced at compile time, not runtime
```

---

## Syntax

### Naming Conventions

```java
// Single type parameter
public class Box<T> {
    private T value;
}

// Multiple type parameters
public class Pair<K, V> {
    private K key;
    private V value;
}

// Bounded type parameters
public class SortedList<T extends Comparable<T>> {
    private List<T> elements;
}
```

### Wildcard Usage

```java
// Producer: extends
public static <T> T getFirst(List<? extends T> list) {
    return list.get(0);
}

// Consumer: super
public static <T> void addAll(List<? super T> dest, List<? extends T> src) {
    dest.addAll(src);
}

// Unbounded
public static void printAll(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}
```

### Documentation

```java
/**
 * A generic container that holds a single value.
 *
 * @param <T> the type of the contained value
 */
public class Container<T> {
    private T value;

    /**
     * Creates a new container with the specified value.
     *
     * @param value the value to store
     */
    public Container(T value) {
        this.value = value;
    }

    /**
     * Gets the contained value.
     *
     * @return the contained value
     */
    public T getValue() {
        return value;
    }
}
```

---

## Easy Example

### Basic Best Practices

```java
import java.util.ArrayList;
import java.util.List;

public class BestPracticesBasics {

    // Good: Clear naming
    public static <T> List<T> asList(T a, T b) {
        List<T> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        return list;
    }

    // Good: Bounded type
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    // Good: Wildcard for read-only
    public static void printAll(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }

    public static void main(String[] args) {
        List<String> names = asList("Alice", "Bob");
        printAll(names);

        System.out.println(max(10, 20));        // 20
        System.out.println(max("hello", "world")); // world
    }
}
```

---

## Medium Example

### PECS in Practice

```java
import java.util.ArrayList;
import java.util.List;

public class PecsExample {

    // Producer Extends: read from source
    public static <T> T getFirst(List<? extends T> source) {
        return source.get(0);
    }

    // Consumer Super: write to destination
    public static <T> void addAll(List<? super T> dest, List<T> src) {
        dest.addAll(src);

---

[📖 Continue to Part 2](README-part2.md)
```
# 07 - Best Practices (Part 2)

[📖 Back to Part 1](README.md)

---


---

[📖 Continue to Part 2](README-part2.md)
# 07 - Best Practices (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---


---

## References

- [Effective Java - Chapter 6: Generics](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Oracle Generics Tutorial](https://docs.oracle.com/en/java/javase/21/java/generics/)
- [Google Java Style Guide - Generics](https://google.github.io/styleguide/javaguide.html)
- [Angelika Langer - Generics FAQ](https://www.angelikalanger.com/GenericsFAQ/)
