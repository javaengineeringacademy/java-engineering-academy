# Type Safety

## Overview

Type safety is a property of programming languages that ensures operations are performed on compatible types. Java achieves type safety through compile-time checking, runtime verification, and generics.

---

## Compile-Time vs Runtime Type Checking

### Compile-Time Type Checking

Java's compiler enforces type safety before execution.

```java
// Compiler catches type errors
String name = "Hello";
int number = name;  // Compile error: incompatible types

// Method signature enforcement
public int add(int a, int b) { return a + b; }
add("1", "2");  // Compile error: String cannot be converted to int
```

### Runtime Type Checking

Some type checks happen at runtime, especially with casting and generics.

```java
// Runtime check with casting
Object obj = "Hello";
String str = (String) obj;  // OK at runtime
Integer num = (Integer) obj;  // ClassCastException at runtime

// Generic type erasure causes runtime checks
List<String> list = new ArrayList<>();
list.add("Hello");
Object obj = list.get(0);
String s = (String) obj;  // Runtime cast needed due to erasure
```

---

## Generics and Type Erasure

### Generics Provide Compile-Time Safety

```java
// Compile-time type safety
List<String> strings = new ArrayList<>();
strings.add("Hello");
strings.add(42);  // Compile error: Integer cannot be converted to String

// Wildcards for flexibility
public void printList(List<? extends Number> list) {
    for (Number n : list) {
        System.out.println(n);
    }
}
```

### Type Erasure Removes Generics at Runtime

```java
// At compile time
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();

// At runtime (after type erasure)
List strings = new ArrayList();  // Raw type
List integers = new ArrayList();  // Raw type

// This means:
// 1. Cannot create generic arrays: new T[]  // Compile error
// 2. Cannot use instanceof with generics: obj instanceof List<String>  // Compile error
// 3. Cannot create instances of type parameters: new T()  // Compile error
```

### Workarounds for Type Erasure

```java
// Pass Class object to preserve type information
public <T> T createInstance(Class<T> clazz) throws Exception {
    return clazz.getDeclaredConstructor().newInstance();
}

// Use TypeToken for complex generic types
public abstract class TypeToken<T> {
    private final Type type;
    
    protected TypeToken() {
        Type superClass = getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }
    
    public Type getType() { return type; }
}

// Usage
TypeToken<List<String>> token = new TypeToken<List<String>>() {};
Type type = token.getType();  // List<String>
```

---

## Casting and ClassCastException

### Safe Casting with instanceof

```java
public void process(Object obj) {
    if (obj instanceof String) {
        String str = (String) obj;  // Safe cast
        System.out.println(str.length());
    } else if (obj instanceof Integer) {
        Integer num = (Integer) obj;  // Safe cast
        System.out.println(num * 2);
    }
}
```

### Pattern Matching (Java 16+)

```java
// Simplified instanceof with pattern matching
public void process(Object obj) {
    if (obj instanceof String str) {
        System.out.println(str.length());  // No explicit cast needed
    } else if (obj instanceof Integer num) {
        System.out.println(num * 2);
    }
}
```

### Common ClassCastException Scenarios

```java
// 1. Unsafe cast
Object obj = "Hello";
Integer num = (Integer) obj;  // ClassCastException

// 2. Generic type erasure
List<String> strings = List.of("a", "b", "c");
Object first = strings.get(0);
Integer wrong = (Integer) first;  // ClassCastException

// 3. Collection type mismatch
List<Integer> numbers = new ArrayList<>();
numbers.add(42);
List raw = numbers;  // Raw type
List<String> strings = raw;  // Unchecked warning
String s = strings.get(0);  // ClassCastException at runtime
```

---

## instanceof Operator

### Basic Usage

```java
Object obj = "Hello";

if (obj instanceof String) {
    String str = (String) obj;
    System.out.println(str.toUpperCase());
}

if (obj instanceof Number) {
    System.out.println("It's a number");
}
```

### Pattern Matching (Java 16+)

```java
// Variable is automatically cast after instanceof
if (obj instanceof String str) {
    System.out.println(str.toUpperCase());
}

// With null check
if (obj instanceof String str && str.length() > 5) {
    System.out.println("Long string: " + str);
}

// With else
if (obj instanceof String str) {
    System.out.println("String: " + str);
} else {
    System.out.println("Not a string");
}
```

### Sealed Classes and instanceof (Java 17+)

```java
sealed interface Shape permits Circle, Rectangle, Triangle {}

record Circle(double radius) implements Shape {}
record Rectangle(double width, double height) implements Shape {}
record Triangle(double base, double height) implements Shape {}

public double calculateArea(Shape shape) {
    return switch (shape) {
        case Circle c -> Math.PI * c.radius() * c.radius();
        case Rectangle r -> r.width() * r.height();
        case Triangle t -> 0.5 * t.base() * t.height();
    };
}
```

---

## Summary

| Concept | Details |
|---------|---------|
| **Compile-time** | Catches type errors before execution |
| **Runtime** | Verifies casts, generic type checks |
| **Type Erasure** | Generics removed at runtime, raw types used |
| **ClassCastException** | Thrown when cast fails at runtime |
| **instanceof** | Checks type before casting |
| **Pattern Matching** | Simplifies instanceof + cast |
| **Best Practice** | Use generics, check instanceof before casting |
