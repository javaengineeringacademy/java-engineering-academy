# Introduction to Generics

## Introduction

Generics enable you to create classes, interfaces, and methods that work with any data type while providing compile-time type safety.

## Learning Objectives

- Understand the purpose of generics
- Learn about type parameters
- Recognize the benefits of generic code
- Understand type safety at compile time

## Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Understanding of inheritance and interfaces

## Why This Matters

Generics eliminate the need for type casting, catch type errors at compile time, and enable writing reusable, type-safe code.

## Syntax

```java
// Generic class
public class Box<T> {
    private T content;

    public void set(T content) {
        this.content = content;
    }

    public T get() {
        return content;
    }
}

// Using generic class
Box<String> stringBox = new Box<>();
Box<Integer> intBox = new Box<>();
```

## Examples

```java
// Example 1: Without generics (old way)
public class OldBox {
    private Object content;

    public void set(Object content) {
        this.content = content;
    }

    public Object get() {
        return content;
    }
}

OldBox box = new OldBox();
box.set("Hello");
String str = (String) box.get();  // Requires casting

// Example 2: With generics (new way)
Box<String> box = new Box<>();
box.set("Hello");
String str = box.get();  // No casting needed

// Example 3: Multiple type parameters
public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
}

Pair<String, Integer> pair = new Pair<>("age", 30);
```

## Exercises

1. Create a generic Box class that can hold any type of object.
2. Create a generic Pair class with two type parameters.
3. What happens if you try to add a different type to a generic collection?

## Interview Questions

- What problem do generics solve?
- What is type erasure?
- Can you use primitives as type parameters?

## Common Pitfalls

- Using raw types (Box instead of Box<String>)
- Assuming generic types are checked at runtime
- Not understanding type erasure

## Best Practices

- Always use parameterized types (List<String> instead of List)
- Use meaningful type parameter names (T, E, K, V)
- Document type parameter constraints

## Real World Applications

- Collections framework (List<E>, Map<K,V>)
- Utility classes
- API design
- Framework development

## References

- [Java Generics Tutorial](https://docs.oracle.com/javase/tutorial/java/generics/)
- [Generics FAQ](https://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeParameters.html)

## Summary

In this topic, you learned the purpose and benefits of generics in Java. Generics provide type safety and eliminate casting. Practice with the exercises before creating generic classes.
