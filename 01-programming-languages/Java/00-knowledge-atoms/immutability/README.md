# Immutability

## Overview

Immutability is the property of an object whose state cannot be modified after creation. Immutable objects are inherently thread-safe, can be safely shared, and provide numerous benefits for program correctness and performance.

---

## What Makes an Object Immutable

An object is immutable if its state cannot change after construction. This requires:

1. **No setter methods** that modify fields
2. **Final fields** for all state
3. **No references to mutable objects** (or defensive copies)
4. **No fields that can be modified indirectly**

```java
// Immutable class
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;      // BigDecimal is immutable
        this.currency = currency;  // Currency is immutable
    }

    public BigDecimal getAmount() {
        return amount;  // No setter, no modification possible
    }

    public Currency getCurrency() {
        return currency;
    }
}
```

---

## Benefits

### 1. Thread Safety

Immutable objects are inherently thread-safe. No synchronization needed.

```java
// Thread-safe without synchronization
public class User {
    private final String name;
    private final int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Multiple threads can safely read this object
    // No need for volatile, synchronized, or locks
}
```

### 2. Safe Sharing

Immutable objects can be freely shared between threads and objects.

```java
// Safe to share across threads
public class Cache {
    private static final Map<String, String> SHARED_MAP = new HashMap<>();
    
    public String get(String key) {
        // String is immutable, safe to use as key
        return SHARED_MAP.get(key);
    }
}
```

### 3. Caching

Immutable objects can be safely cached because their state never changes.

```java
// Safe to cache
public class Point {
    private final int x;
    private final int y;

    // Cache can safely store immutable objects
    private static final Map<Long, Point> CACHE = new HashMap<>();

    public static Point of(int x, int y) {
        long key = ((long) x << 32) | y;
        return CACHE.computeIfAbsent(key, k -> new Point(x, y));
    }
}
```

### 4. Security

Immutable objects cannot be tampered with, making them suitable for security-sensitive operations.

```java
// Secure: Cannot be modified after creation
public class Credentials {
    private final String username;
    private final char[] password;  // Use char[] for security

    public Credentials(String username, char[] password) {
        this.username = username;
        this.password = password.clone();  // Defensive copy
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password.clone();  // Return defensive copy
    }
}
```

### 5. Simplified Debugging

Immutable objects have consistent state, making debugging easier.

---

## How to Create Immutable Classes

### Step-by-Step Guide

```java
// Step 1: Make the class final
public final class Temperature {
    // Step 2: Make all fields private and final
    private final double celsius;
    private final String unit;

    // Step 3: Initialize all fields in constructor
    public Temperature(double value, String unit) {
        this.celsius = convertToCelsius(value, unit);
        this.unit = unit;
    }

    // Step 4: No setter methods
    // Only getter methods

    // Step 5: Return defensive copies of mutable objects
    public double getValue() {
        return convertFromCelsius(celsius, unit);
    }

    // Step 6: If class has mutable fields, create defensive copies
    private final List<String> history = new ArrayList<>();

    public List<String> getHistory() {
        return Collections.unmodifiableList(history);
    }

    // Helper methods
    private double convertToCelsius(double value, String unit) {
        if ("F".equals(unit)) {
            return (value - 32) * 5/9;
        }
        return value;
    }

    private double convertFromCelsius(double celsius, String unit) {
        if ("F".equals(unit)) {
            return celsius * 9/5 + 32;
        }
        return celsius;
    }
}
```

### Using Records (Java 14+)

Records are inherently immutable.

```java
// Records are immutable by design
public record Point(int x, int y) {
    // No need for constructor, getters, equals, hashCode, toString
    // All automatically generated and immutable
}

public record Person(String name, int age, List<String> hobbies) {
    // Defensive copy for mutable field
    public Person {
        hobbies = List.copyOf(hobbies);  // Create unmodifiable copy
    }
}
```

---

## String Immutability

`String` is the most commonly used immutable class in Java.

```java
String s1 = "Hello";
String s2 = s1.concat(" World");

// s1 is still "Hello" (immutable)
// s2 is a new String "Hello World"

// String pool optimization
String a = "Hello";
String b = "Hello";
System.out.println(a == b);  // true (same object in pool)

// String concatenation creates new objects
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i;  // Creates 1000 String objects!
}

// Better: Use StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

### String Methods Return New Strings

```java
String s = "Hello";
String upper = s.toUpperCase();  // New String "HELLO"
String replaced = s.replace('l', 'x');  // New String "Hexxo"
String trimmed = s.trim();  // New String (if modified)
```

---

## Immutable Collections

### Unmodifiable Collections

```java
// Create unmodifiable list
List<String> list = List.of("A", "B", "C");
list.add("D");  // Throws UnsupportedOperationException

// Create unmodifiable map
Map<String, Integer> map = Map.of("A", 1, "B", 2);
map.put("C", 3);  // Throws UnsupportedOperationException

// Wrap existing collection
List<String> mutableList = new ArrayList<>(Arrays.asList("A", "B", "C"));
List<String> unmodifiable = Collections.unmodifiableList(mutableList);
unmodifiable.add("D");  // Throws UnsupportedOperationException

// WARNING: Mutable list can still be modified!
mutableList.add("D");  // This works!
// unmodifiable list now contains "A", "B", "C", "D"
```

### Defensive Copies

```java
public class ShoppingCart {
    private final List<String> items;

    public ShoppingCart(List<String> items) {
        // Defensive copy in constructor
        this.items = new ArrayList<>(items);
    }

    public List<String> getItems() {
        // Defensive copy in getter
        return new ArrayList<>(items);
    }

    // Or use unmodifiable views
    public List<String> getItemsUnmodifiable() {
        return Collections.unmodifiableList(items);
    }
}
```

---

## When NOT to Use Immutability

### Performance Considerations

```java
// Immutability can cause excessive object creation
public class Matrix {
    private final double[][] data;

    // This creates a new Matrix object every time!
    public Matrix multiply(Matrix other) {
        double[][] result = new double[data.length][other.data[0].length];
        // ... multiplication logic
        return new Matrix(result);
    }
}

// For performance-critical code, mutable might be better
public class MutableMatrix {
    private double[][] data;

    public void multiplyInPlace(MutableMatrix other) {
        // Modifies this object, no new allocation
    }
}
```

### Large Mutable State

```java
// Large objects with frequent modifications
public class GameWorld {
    // Too many objects to make immutable
    private final List<GameObject> objects;
    private final Map<String, Entity> entities;
    
    // Updating would create massive copies
    public void update() {
        // Need to modify state frequently
    }
}
```

---

## Summary

| Aspect | Details |
|--------|---------|
| **Definition** | Object state cannot change after construction |
| **Benefits** | Thread safety, safe sharing, caching, security |
| **How to create** | Final class, final fields, no setters, defensive copies |
| **Java support** | String, records, Collections.unmodifiable*() |
| **Trade-offs** | Object creation overhead vs thread safety |
| **Best for** | Value objects, DTOs, constants, shared state |
