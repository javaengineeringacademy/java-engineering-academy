}

List<Integer> ints = List.of(1, 2, 3);
double result = sum(ints);
```

**Compiler steps:**
1. **Capture wildcard** — `? extends Number` becomes fresh type `CAP#1`
2. **Type check** — Verify `List<Integer>` is compatible with `List<CAP#1>`
3. **Insert bounds** — `CAP#1 extends Number`
4. **Erase** — Replace `CAP#1` with `Number`

### Bytecode After Erasure

```java
// What the JVM sees
public static double sum(List list) {
    return list.stream()
               .mapToDouble(((Number) x -> x).doubleValue())
               .sum();
}
```

---

## JVM Perspective

### Wildcard in Bytecode

```bash
javap -v MyClass.class | grep "Signature"
# Shows wildcard info in Signature attribute
# But JVM doesn't use it for type checking
```

### Runtime Type Information

```java
List<? extends Number> list = List.of(1, 2, 3);
// At runtime: list is just List
// The ? extends Number is erased
System.out.println(list.getClass());  // java.util.Arrays$ArrayList
```

---

## Memory Representation

### Wildcards Don't Affect Memory

```java
List<String> strings = List.of("a", "b");
List<Integer> integers = List.of(1, 2);
List<? extends Number> numbers = integers;

// All have identical memory layout
// The wildcard exists only at compile time
```

---

## Syntax

### Unbounded Wildcard

```java
public static void print(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

public static boolean isEmpty(Collection<?> collection) {
    return collection.isEmpty();
}
```

### Upper Bounded Wildcard

```java
public static double sum(List<? extends Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}

public static <T extends Comparable<T>> T max(List<? extends T> list) {
    return list.stream().max(Comparable::compareTo).orElseThrow();
}
```

### Lower Bounded Wildcard

```java
public static void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
}

public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    dest.addAll(src);
}
```

### Multiple Bounds with Wildcards

```java
public static <T extends Number & Comparable<T>> T max(List<? extends T> list) {
    return list.stream().max(Comparable::compareTo).orElseThrow();
}
```

---

## Easy Example

### Basic Wildcard Usage

```java
import java.util.List;

public class WildcardBasics {
    
    public static void printList(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }
    
    public static double sum(List<? extends Number> list) {
        double total = 0;
        for (Number num : list) {
            total += num.doubleValue();
        }
        return total;
    }
    
    public static void addIntegers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }
    
    public static void main(String[] args) {
        // Unbounded wildcard
        List<String> names = List.of("Alice", "Bob");
        List<Integer> numbers = List.of(1, 2, 3);
        printList(names);   // OK
        printList(numbers); // OK
        
        // Upper bounded wildcard
        List<Integer> ints = List.of(1, 2, 3);

---

[📖 Continue to Part 2](README-part2.md)
 | [📖 Continue to Part 3](README-part3.md)
```
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

