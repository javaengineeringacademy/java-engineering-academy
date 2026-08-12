    private T value;
}

// Bytecode signature
// Signature: LNumberBox<TT;>;  where T:Ljava/lang/Number;
// The bound is preserved in the Signature attribute
```

### Runtime Verification

```java
// Bounds are checked at compile time AND runtime (for generic types)
Box<Number> box = new Box<>();
box.setValue(42);      // OK
box.setValue(3.14);    // OK

// But this fails at runtime (due to type erasure workarounds):
// box.setValue("hello");  // Compile error, but runtime would succeed
// The bound prevents this at compile time
```

---

## Memory Representation

### Bounded vs Unbounded

```java
Box<Number> bounded = new Box<>();
Box<Object> unbounded = new Box<>();
```

**Memory layout:**
```
Both have IDENTICAL layout:
┌─────────────────────────┐
│ Object header (16 bytes)│
│ value: Object reference │
└─────────────────────────┘

The bound affects:
- Which methods can be called (compile time)
- What types can be assigned (compile time)
- NOT memory layout or performance
```

---

## Syntax

### Basic Upper Bound

```java
public class ClassName<T extends BoundType> {
    // T is bounded by BoundType
}

public static <T extends BoundType> ReturnType method(T param) {
    // T is bounded by BoundType
}
```

### Multiple Bounds

```java
public class ClassName<T extends ClassA & InterfaceB & InterfaceC> {
    // T extends ClassA AND implements InterfaceB AND InterfaceC
}

// Order matters: class first, then interfaces
public static <T extends Number & Comparable<T> & Serializable> T method(T param) {
    // T is Number, Comparable, and Serializable
}
```

### Recursive Bound

```java
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// T must be comparable to itself
```

### Wildcard Bounds (Preview for Topic 05)

```java
// Upper bounded wildcard
public static double sum(List<? extends Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}
```

---

## Easy Example

### Basic Bounded Type

```java
public class BoundedBox<T extends Number> {
    private T value;

    public BoundedBox(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public double doubleValue() {
        return value.doubleValue();  // OK: Number has doubleValue()
    }

    public int intValue() {
        return value.intValue();  // OK: Number has intValue()
    }

    public static void main(String[] args) {
        BoundedBox<Integer> intBox = new BoundedBox<>(42);
        BoundedBox<Double> doubleBox = new BoundedBox<>(3.14);
        
        System.out.println(intBox.doubleValue());   // 42.0
        System.out.println(doubleBox.doubleValue()); // 3.14
        
        // BoundedBox<String> stringBox = new BoundedBox<>("hello");
        // Compile error: String does not extend Number
    }
}
```

---

## Medium Example

### Comparable Bounded Type

```java
import java.util.List;
import java.util.Objects;

public class SortableUtil {
    
    public static <T extends Comparable<T>> T max(List<T> list) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Empty list");
        }

---

[📖 Continue to Part 2](README-part2.md)
 | [📖 Continue to Part 3](README-part3.md)
```
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
