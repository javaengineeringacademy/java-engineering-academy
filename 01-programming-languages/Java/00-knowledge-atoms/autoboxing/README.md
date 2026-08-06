# Autoboxing and Unboxing

## Overview

Autoboxing is the automatic conversion between primitive types and their corresponding wrapper classes. Unboxing is the reverse process. This feature, introduced in Java 5, simplifies code but has important implications for performance and behavior.

---

## Primitive to Wrapper Conversion

### Wrapper Classes

| Primitive | Wrapper Class | Size (bytes) |
|-----------|---------------|--------------|
| byte | Byte | 1 |
| short | Short | 2 |
| int | Integer | 4 |
| long | Long | 8 |
| float | Float | 4 |
| double | Double | 8 |
| char | Character | 2 |
| boolean | Boolean | 1 |

### Autoboxing Examples

```java
// Autoboxing: primitive → wrapper
Integer num = 42;           // int → Integer
Double pi = 3.14;           // double → Double
Boolean flag = true;        // boolean → Boolean
Character ch = 'A';         // char → Character

// In collections
List<Integer> list = new ArrayList<>();
list.add(10);               // autoboxing int → Integer
list.add(20);               // autoboxing int → Integer

// In method calls
System.out.println(Integer.valueOf(42));  // autoboxing
```

### Unboxing Examples

```java
// Unboxing: wrapper → primitive
Integer num = 42;
int value = num;            // Integer → int

Double pi = 3.14;
double d = pi;              // Double → double

Boolean flag = true;
boolean b = flag;           // Boolean → boolean

// In arithmetic
Integer a = 10;
Integer b = 20;
int sum = a + b;            // unboxing both, then adding

// In comparisons
Integer x = 100;
if (x > 50) { ... }        // unboxing x
```

---

## Integer Cache (-128 to 127)

Java caches Integer objects for values -128 to 127.

```java
// Cache behavior
Integer a = 127;
Integer b = 127;
System.out.println(a == b);  // true (same cached object)

Integer c = 128;
Integer d = 128;
System.out.println(c == d);  // false (different objects)

// Always use equals() for wrapper comparison
Integer e = 128;
Integer f = 128;
System.out.println(e.equals(f));  // true (value equality)

// Configure cache size
// -XX:AutoBoxCacheMax=1000
```

### Cache Details

```java
// Integer cache range
private static class IntegerCache {
    static final int low = -128;
    static final int high = 127;
    static final Integer[] cache;
    
    static {
        cache = new Integer[(high - low) + 1];
        for (int i = low; i <= high; i++) {
            cache[i - low] = new Integer(i);
        }
    }
}

// Other caches:
// Byte: all values cached (-128 to 127)
// Character: 0 to 127
// Short: -128 to 127
// Long: -128 to 127
// Boolean: TRUE and FALSE
// Float: no cache
// Double: no cache
```

---

## Performance Implications

### Object Creation Overhead

```java
// BAD: Creates millions of Integer objects
public void badPractice() {
    Long sum = 0L;  // Autoboxing on every iteration
    for (int i = 0; i < 1000000; i++) {
        sum += i;  // Unbox sum, add, autobox result
    }
}

// GOOD: Use primitive for loops
public void goodPractice() {
    long sum = 0L;  // Primitive
    for (int i = 0; i < 1000000; i++) {
        sum += i;  // No boxing/unboxing
    }
}
```

### Collection Performance

```java
// BAD: Lots of autoboxing
List<Integer> list = new ArrayList<>();
for (int i = 0; i < 1000000; i++) {
    list.add(i);  // Autoboxing for each element
}

// BETTER: Use primitive-specialized collections (Eclipse Collections, HPPC)
IntList list = new IntArrayList();
for (int i = 0; i < 1000000; i++) {
    list.add(i);  // No autoboxing
}
```

### Method Overloading Ambiguity

```java
public class OverloadDemo {
    public static void process(int value) {
        System.out.println("int");
    }

    public static void process(Integer value) {
        System.out.println("Integer");
    }

    public static void main(String[] args) {
        process(42);     // Calls process(int)
        process(Integer.valueOf(42));  // Calls process(Integer)
    }
}
```

---

## Summary

| Aspect | Details |
|--------|---------|
| **Autoboxing** | Automatic conversion: primitive → wrapper |
| **Unboxing** | Automatic conversion: wrapper → primitive |
| **Integer Cache** | -128 to 127 (configurable) |
| **Performance** | Avoid in tight loops, use primitives |
| **Equality** | Use `.equals()` for wrappers, not `==` |
| **Collections** | Always boxed (use primitive-specialized collections for performance) |
