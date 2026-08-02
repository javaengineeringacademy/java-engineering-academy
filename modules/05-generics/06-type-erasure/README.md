# 06 - Type Erasure

## Table of Contents

1. [Introduction](#introduction)
2. [Learning Objectives](#learning-objectives)
3. [Prerequisites](#prerequisites)
4. [Why This Concept Exists](#why-this-concept-exists)
5. [Problem Statement](#problem-statement)
6. [Theory](#theory)
7. [Internal Working](#internal-working)
8. [JVM Perspective](#jvm-perspective)
9. [Memory Representation](#memory-representation)
10. [Syntax](#syntax)
11. [Easy Example](#easy-example)
12. [Medium Example](#medium-example)
13. [Hard Example](#hard-example)
14. [Enterprise Example](#enterprise-example)
15. [Performance](#performance)
16. [Best Practices](#best-practices)
17. [Common Mistakes](#common-mistakes)
18. [Pitfalls](#pitfalls)
19. [Debugging Tips](#debugging-tips)
20. [Comparison Table](#comparison-table)
21. [Decision Tree](#decision-tree)
22. [Interview Questions](#interview-questions)
23. [Exercises](#exercises)
24. [Assignments](#assignments)
25. [Mini Project](#mini-project)
26. [Summary](#summary)
27. [References](#references)

---

## Introduction

**Type erasure** is the process by which the Java compiler removes all generic type information at compile time, replacing type parameters with their bounds (or `Object` if unbounded). This ensures backward compatibility with pre-Java 5 code but means generic type information is not available at runtime.

Understanding type erasure is crucial for avoiding pitfalls, debugging generic code, and designing APIs that work within Java's type system constraints.

---

## Learning Objectives

By the end of this topic, you will be able to:

- Explain how type erasure works in Java
- Identify the limitations imposed by type erasure
- Work around type erasure restrictions
- Use reflection to access erased type information
- Debug generic code using type erasure knowledge
- Design APIs that account for type erasure

---

## Prerequisites

- Generic classes and methods (Topics 02-03)
- Bounded types (Topic 04)
- Wildcards (Topic 05)
- Basic understanding of bytecode and JVM
- Reflection basics (helpful but not required)

---

## Why This Concept Exists

### Backward Compatibility

```java
// Pre-Java 5 code (still compiles!)
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);  // Manual cast

// Post-Java 5 code
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);  // No cast needed

// Both produce IDENTICAL bytecode after type erasure
// This was intentional for backward compatibility
```

### The Problem Type Erasure Solves

```java
// If generics were reified (type info at runtime):
String[] strings = new String[10];
Number[] numbers = new Number[10];
// These are different types at runtime

// With generics:
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();
// At runtime: both are ArrayList (same type!)
// This allows pre-Java 5 code to work with generic code
```

---

## Problem Statement

Understand and work within the constraints of type erasure:

1. Cannot use `instanceof` with parameterized types
2. Cannot create generic arrays
3. Cannot use `new T()` or `T.class`
4. Cannot overload methods with different generic signatures
5. Must handle runtime type information through other means

---

## Theory

### How Type Erasure Works

**Rule 1: Unbounded type parameters erase to `Object`**

```java
// Source
public class Box<T> {
    private T value;
    public T get() { return value; }
    public void set(T v) { value = v; }
}

// After erasure
public class Box {
    private Object value;
    public Object get() { return value; }
    public void set(Object v) { value = v; }
}
```

**Rule 2: Bounded type parameters erase to their first bound**

```java
// Source
public class NumberBox<T extends Number> {
    private T value;
    public double doubleValue() { return value.doubleValue(); }
}

// After erasure
public class NumberBox {
    private Number value;
    public double doubleValue() { return value.doubleValue(); }
}
```

**Rule 3: Compiler inserts casts for type safety**

```java
// Source
Box<String> box = new Box<>();
box.set("hello");
String s = box.get();

// After erasure
Box box = new Box<>();
box.set("hello");
String s = (String) box.get();  // Cast inserted
```

**Rule 4: Bridge methods maintain polymorphism**

```java
// Source
public class StringBox extends Box<String> {
    @Override
    public void set(String value) { super.set(value); }
}

// After erasure
public class StringBox extends Box {
    @Override
    public void set(String value) { super.set(value); }
    
    // Bridge method added by compiler
    public void set(Object value) { set((String) value); }
}
```

### Type Erasure Limitations

```java
// 1. Cannot use instanceof with parameterized types
// if (obj instanceof List<String>) { }  // Compile error!

// 2. Cannot create generic arrays
// List<String>[] arrays = new List<String>[10];  // Compile error!

// 3. Cannot use new T()
// public class Box<T> { T value = new T(); }  // Compile error!

// 4. Cannot use T.class
// Class<T> clazz = T.class;  // Compile error!

// 5. Cannot overload with different generic signatures
// public void process(List<String> list) { }
// public void process(List<Integer> list) { }  // Compile error!
```

---

## Internal Working

### Compiler Transformation Steps

```java
// Source code
public class Container<T extends Comparable<T>> {
    private T value;
    
    public Container(T value) {
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
    
    public int compareTo(Container<T> other) {
        return value.compareTo(other.value);
    }
}

// Usage
Container<String> container = new Container<>("hello");
String v = container.getValue();
int result = container.compareTo(new Container<>("world"));
```

**Compiler steps:**

1. **Type erasure** — Replace `T` with `Comparable`
2. **Cast insertion** — Add `(Comparable)` cast where needed
3. **Bridge method generation** — Add synthetic methods for polymorphism

**After erasure:**

```java
public class Container {
    private Comparable value;
    
    public Container(Comparable value) {
        this.value = value;
    }
    
    public Comparable getValue() {
        return value;
    }
    
    public int compareTo(Container other) {
        return value.compareTo(other.value);
    }
    
    // Bridge method
    public int compareTo(Object other) {
        return compareTo((Container) other);
    }
}
```

### Wildcard Erasure

```java
// Upper bounded wildcard
List<? extends Number> list = List.of(1, 2, 3);
// After erasure: List (elements accessed as Number)

// Lower bounded wildcard
List<? super Integer> list = new ArrayList<>();
// After erasure: List (elements written as Object)

// Unbounded wildcard
List<?> list = List.of("a", "b");
// After erasure: List (elements accessed as Object)
```

---

## JVM Perspective

### Bytecode Structure

```java
Box<String> box = new Box<>();
box.set("hello");
String s = box.get();
```

**Bytecode (simplified):**
```
new Box
dup
invokespecial Box.<init>:()V
astore_1           // box = new Box()

aload_1
ldc "hello"
invokevirtual Box.set:(Ljava/lang/Object;)V  // set(Object), not set(String)

aload_1
invokevirtual Box.get:()Ljava/lang/Object;   // get() returns Object
checkcast java/lang/String                   // Cast to String
astore_2           // s = (String) box.get()
```

### Type Information in Bytecode

```bash
# Generic type info preserved in Signature attribute
javap -v Box.class | grep -A 3 "Signature"
# Signature: LBox<Ljava/lang/String;>;
# This is for tools (IDE, reflection), not JVM runtime
```

### Reflection and Type Erasure

```java
Box<String> box = new Box<>();

// Runtime type
Class<?> clazz = box.getClass();
System.out.println(clazz.getName());  // "Box" (not "Box<String>")

// Generic type info (if preserved in bytecode)
Type superclass = clazz.getGenericSuperclass();
if (superclass instanceof ParameterizedType pt) {
    Type[] typeArgs = pt.getActualTypeArguments();
    System.out.println(typeArgs[0]);  // "class java.lang.String"
}
```

---

## Memory Representation

### Generic Objects

```java
Box<String> stringBox = new Box<>();
Box<Integer> intBox = new Box<>();
Box<List<String>> listBox = new Box<>();
```

**Memory layout (all identical):**
```
┌─────────────────────────────┐
│ Object header (16 bytes)    │
├─────────────────────────────┤
│ value: Object reference     │
└─────────────────────────────┘

Total: 24 bytes (on 64-bit JVM with compressed oops)
```

### Array Erasure

```java
// Generic arrays are problematic
// String[] strings = new String[10];  // OK
// Box<String>[] boxes = new Box<String>[10];  // Compile error!

// Why? Arrays carry reified type info
// At runtime: strings knows it's String[]
// But Box<String>[] would need to be Box[] (erased)
// These are incompatible
```

---

## Syntax

### Type Erasure in Practice

```java
// Source
public class Box<T> {
    private T value;
    public T get() { return value; }
    public void set(T v) { value = v; }
}

// Bytecode equivalent
public class Box {
    private Object value;
    public Object get() { return value; }
    public void set(Object v) { value = v; }
    
    // Bridge method (for subclasses)
    public void set(String v) { set((Object) v); }
}
```

### Cast Insertion

```java
// Source
Box<String> box = new Box<>();
String s = box.get();

// Bytecode equivalent
Box box = new Box<>();
String s = (String) box.get();
```

### Bridge Methods

```java
// Source
public class StringBox extends Box<String> {
    @Override
    public void set(String value) { super.set(value); }
}

// Bytecode equivalent
public class StringBox extends Box {
    @Override
    public void set(String value) { super.set(value); }
    
    // Bridge method
    public void set(Object value) {
        set((String) value);
    }
}
```

---

## Easy Example

### Basic Type Erasure Demonstration

```java
import java.util.ArrayList;
import java.util.List;

public class TypeErasureBasic {
    
    public static void main(String[] args) {
        // Different generic types are same at runtime
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        
        System.out.println(strings.getClass() == integers.getClass());  // true
        System.out.println(strings.getClass().getName());  // java.util.ArrayList
        
        // Boxing/unboxing happens at compile time
        List<Integer> nums = new ArrayList<>();
        nums.add(42);  // Autoboxing: int → Integer
        int value = nums.get(0);  // Auto-unboxing: Integer → int
        
        // Type erasure in method signatures
        System.out.println("String list class: " + strings.getClass());
        System.out.println("Integer list class: " + integers.getClass());
    }
}
```

---

## Medium Example

### Type Erasure Implications

```java
import java.util.ArrayList;
import java.util.List;

public class TypeErasureImplications {
    
    // Cannot use instanceof with parameterized types
    public static void checkType(Object obj) {
        // WRONG: if (obj instanceof List<String>) { }
        
        // RIGHT: check raw type
        if (obj instanceof List<?>) {
            List<?> list = (List<?>) obj;
            System.out.println("It's a List of: " + 
                list.getClass().getTypeParameters()[0].getName());
        }
    }
    
    // Cannot create generic arrays
    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<T> type, int size) {
        return (T[]) java.lang.reflect.Array.newInstance(type, size);
    }
    
    // Cannot use new T()
    public static <T> T safeNew(Class<T> type) throws Exception {
        return type.getDeclaredConstructor().newInstance();
    }
    
    public static void main(String[] args) throws Exception {
        checkType(List.of("a", "b"));
        checkType(List.of(1, 2, 3));
        
        // Create array using reflection
        String[] strings = createArray(String.class, 10);
        System.out.println("Array length: " + strings.length);
        
        // Create instance using class token
        ArrayList<?> list = safeNew(ArrayList.class);
        System.out.println("Created: " + list.getClass());
    }
}
```

---

## Hard Example

### TypeToken Pattern for Runtime Type Information

```java
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class TypeToken<T> {
    private final Type type;
    
    protected TypeToken() {
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) superclass;
        type = pt.getActualTypeArguments()[0];
    }
    
    public Type getType() {
        return type;
    }
    
    @SuppressWarnings("unchecked")
    public Class<T> getRawType() {
        return (Class<T>) type;
    }
    
    // Usage
    public static void main(String[] args) {
        TypeToken<List<String>> token = new TypeToken<List<String>>() {};
        System.out.println("Type: " + token.getType());
        System.out.println("Raw: " + token.getRawType());
        
        // Type safety at runtime
        Type type = token.getType();
        if (type instanceof ParameterizedType pt) {
            Type[] typeArgs = pt.getActualTypeArguments();
            System.out.println("Type argument: " + typeArgs[0]);
        }
    }
}
```

---

## Enterprise Example

### Generic Type Information Utility

```java
import java.lang.reflect.*;
import java.util.*;

public final class TypeInfo {
    
    private TypeInfo() {}
    
    public static Type getSuperclassTypeParameter(
            Class<?> subclass, int index) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof ParameterizedType pt) {
            return pt.getActualTypeArguments()[index];
        }
        throw new IllegalArgumentException(
            subclass.getName() + " is not parameterized");
    }
    
    public static <T> Class<T> getCollectionElementType(
            Class<?> collectionClass) {
        Type type = getSuperclassTypeParameter(collectionClass, 0);
        if (type instanceof Class<?> clazz) {
            @SuppressWarnings("unchecked")
            Class<T> result = (Class<T>) clazz;
            return result;
        }
        throw new IllegalArgumentException(
            "Type parameter is not a simple class");
    }
    
    public static Type[] getMapTypeParameters(
            Class<?> mapClass) {
        Type type = getSuperclassTypeParameter(mapClass, 0);
        if (type instanceof ParameterizedType pt) {
            return pt.getActualTypeArguments();
        }
        throw new IllegalArgumentException(
            "Type parameter is not parameterized");
    }
    
    // Type-safe deserialization pattern
    public static <T> T deserialize(String json, Class<T> type) {
        // In real code, use Jackson or Gson
        // This demonstrates type token usage
        return null;  // Placeholder
    }
    
    public static void main(String[] args) {
        // Get type parameter of a subclass
        Type type = getSuperclassTypeParameter(
            StringList.class, 0);
        System.out.println("Element type: " + type);  // class java.lang.String
        
        // Type-safe list creation
        List<String> list = createTypedList(String.class);
        list.add("hello");
        System.out.println("List type: " + list.getClass());
    }
    
    static class StringList extends ArrayList<String> {}
    
    @SuppressWarnings("unchecked")
    private static <T> List<T> createTypedList(Class<T> elementType) {
        return new ArrayList<>();
    }
}
```

---

## Performance

### Type Erasure Overhead

| Aspect | Impact |
|--------|--------|
| Compile time | Slightly more (type checking) |
| Runtime | Zero overhead (erased) |
| Bytecode size | Slightly larger (bridge methods) |
| Method dispatch | Same as non-generic |
| JIT optimization | Identical to raw types |

### Bridge Method Cost

```java
// Bridge methods add one extra method call
// But JVM can inline them, so cost is negligible
public class StringBox extends Box {
    public void set(String value) { super.set(value); }
    public void set(Object value) { set((String) value); }  // Bridge
}
```

---

## Best Practices

1. **Understand erasure limitations** — Know what you can't do with generics
2. **Use type tokens for runtime type info** — `Class<T>` or `TypeToken<T>`
3. **Document generic types** — Javadoc `@param` and `@return` tags
4. **Suppress warnings properly** — `@SuppressWarnings("unchecked")` with justification
5. **Avoid raw types** — They bypass type safety
6. **Use `@SafeVarargs`** — For generic varargs methods

---

## Common Mistakes

### 1. Using instanceof with Parameterized Types

```java
// WRONG
List<String> list = List.of("a", "b");
if (list instanceof List<String>) { }  // Compile error!

// RIGHT
if (list instanceof List<?>) { }
```

### 2. Creating Generic Arrays

```java
// WRONG
// List<String>[] arrays = new List<String>[10];

// RIGHT
@SuppressWarnings("unchecked")
List<String>[] arrays = (List<String>[]) new List[10];
```

### 3. Using new T()

```java
// WRONG
public class Box<T> {
    private T value = new T();  // Compile error!
}

// RIGHT
public class Box<T> {
    private final T value;
    
    public Box(T value) {
        this.value = value;
    }
}
```

---

## Pitfalls

### 1. Type Erasure Surprise

```java
Box<String> stringBox = new Box<>();
Box<Integer> intBox = new Box<>();

// These are the SAME class at runtime!
System.out.println(stringBox.getClass() == intBox.getClass()); // true

// You cannot do:
// Box<String>.class  // Compile error
// if (box instanceof Box<String>) { }  // Compile error
```

### 2. Overloading with Generic Signatures

```java
// These have the SAME erasure - compile error!
public void process(List<String> list) { }
public void process(List<Integer> list) { }
// Both erase to: process(List)
```

### 3. Static Members

```java
// WRONG
public class Box<T> {
    private static T value;  // Compile error!
    // T is per-instance, but static is per-class
}

// RIGHT
public class Box<T> {
    private static int count;  // This is fine
    private T instanceValue;   // This is fine
}
```

---

## Debugging Tips

### 1. Check Erased Types

```bash
javac -d out src/Box.java
javap -v out/Box.class | grep "Signature"
# Shows generic type info in bytecode
```

### 2. Use Reflection to Inspect Types

```java
// Check generic type info
Field field = MyClass.class.getDeclaredField("list");
Type genericType = field.getGenericType();
if (genericType instanceof ParameterizedType pt) {
    System.out.println("Raw type: " + pt.getRawType());
    System.out.println("Type args: " + Arrays.toString(pt.getActualTypeArguments()));
}
```

### 3. Read Compiler Errors

```
Error: incompatible types: Object cannot be converted to String
// This means you're trying to use a raw type
// Add proper generic parameters
```

### 4. Use IDE Type Hints

```java
Box<> box = new Box<>();  // IDE shows inferred type
// IntelliJ: View > Tool Windows > Structure
// Eclipse: Open Declaration
```

### 5. Inspect Bytecode

```bash
javap -c -p Box.class | grep -A 5 "set\|get"
# Shows erased method signatures
```

---

## Comparison Table

| Feature | Generic Code | After Erasure |
|---------|--------------|---------------|
| Type parameters | `<T>` | `Object` or bound |
| Method signatures | `T get()` | `Object get()` |
| Field types | `T value` | `Object value` |
| Casts | None needed | Compiler-inserted |
| Bridge methods | Not present | Added by compiler |
| instanceof | Can't use with `T` | Can use with raw type |
| Arrays | Can't create generic | Can create raw arrays |

---

## Decision Tree

```
Do you need runtime type information?
├── No → Use generics normally (erasure is fine)
└── Yes → What type of runtime info?
    ├── Class type → Use Class<T> token
    ├── Parameterized type → Use TypeToken or reflection
    ├── Array creation → Use Array.newInstance()
    └── Instance creation → Use Class<T>.newInstance()
```

---

## Interview Questions

### Q1: What is type erasure in Java?

**A:** Type erasure is the process where the compiler removes all generic type information at compile time, replacing type parameters with their bounds (or `Object`). This ensures backward compatibility but means generic types are not available at runtime.

### Q2: Why does Java use type erasure?

**A:** For backward compatibility with pre-Java 5 code. Raw types (`List`) and parameterized types (`List<String>`) produce identical bytecode, allowing old code to work with new generic code.

### Q3: What can't you do because of type erasure?

**A:** Cannot use `instanceof` with parameterized types, create generic arrays, use `new T()`, `T.class`, or overload methods with different generic signatures.

### Q4: How can you get runtime type information?

**A:** Use `Class<T>` tokens, `TypeToken<T>` pattern, or reflection APIs like `getGenericSuperclass()`, `getGenericType()`, etc.

### Q5: Do generics have runtime overhead?

**A:** No. Type erasure means generic code produces identical bytecode to raw types. The only overhead is compile-time type checking and bridge method generation, which has negligible runtime cost.

---

## Exercises

### Exercise 1: Type Erasure Demonstration

Create a program that demonstrates type erasure by:
1. Comparing `getClass()` of different generic types
2. Using reflection to inspect erased types
3. Showing bridge method generation

### Exercise 2: TypeToken Implementation

Implement a `TypeToken<T>` class that captures generic type information at runtime.

### Exercise 3: Generic Array Creation

Create a utility method to create generic arrays using reflection.

---

## Assignments

### Assignment 1: Type-Safe Reflection Utility

Create a `TypeSafeReflection` utility class that:
1. Safely gets generic type parameters
2. Creates instances of generic types
3. Accesses fields with type safety
4. Handles type erasure gracefully

### Assignment 2: Generic Builder with Runtime Type

Create a generic builder that:
1. Uses type tokens for runtime type info
2. Validates types at build time
3. Supports complex generic types
4. Handles type erasure properly

---

## Mini Project

### Type-Safe Serialization Framework

Build a serialization framework that:
1. Uses type tokens to preserve generic type information
2. Serializes/deserializes generic types correctly
3. Handles type erasure gracefully
4. Supports complex nested generic types

**Key classes:**
- `TypeToken<T>` — captures generic type info
- `TypeSafeSerializer` — serializes with type safety
- `TypeSafeDeserializer` — deserializes with type safety

---

## Summary

Type erasure is a fundamental aspect of Java generics:

1. **Compile-time feature** — Generic types erased at compile time
2. **Backward compatibility** — Allows pre-Java 5 code to work
3. **No runtime overhead** — Identical performance to raw types
4. **Limitations** — Cannot use instanceof, create arrays, etc.
5. **Workarounds** — Type tokens, reflection, class parameters

Understanding type erasure is essential for writing effective generic code and avoiding common pitfalls.

---

## References

- [Oracle - Type Erasure](https://docs.oracle.com/en/java/javase/21/java/generics/erasure.html)
- [Java Language Specification §4.6 - Type Erasure](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.6)
- [Effective Java - Item 33: Use tokens to pass class literals at runtime](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Angelika Langer - Type Erasure FAQ](https://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeErasure.html)
