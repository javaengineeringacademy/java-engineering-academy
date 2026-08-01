# Type Erasure

## Introduction

Type erasure is the process where generic type information is removed at compile time, ensuring backward compatibility with pre-generics code.

## Learning Objectives

- Understand what type erasure is
- Know what information is erased
- Learn the implications of type erasure
- Understand what can and can't be done with generics at runtime

## Prerequisites

- Generic Classes
- Generic Methods
- Bounded Types

## Why This Matters

Understanding type erasure is crucial for debugging generic code and knowing the limitations of generics in Java.

## Syntax

```java
// Before erasure
public class Box<T> {
    private T content;
    public void set(T content) { this.content = content; }
    public T get() { return content; }
}

// After erasure (what the compiler generates)
public class Box {
    private Object content;
    public void set(Object content) { this.content = content; }
    public Object get() { return content; }
}
```

## Examples

```java
// Example 1: Type erasure in action
public class Box<T> {
    private T content;

    public void set(T content) {
        this.content = content;
    }

    public T get() {
        return content;
    }

    public void checkType() {
        // This won't work as expected
        // if (content instanceof T) { }  // Compile error
    }
}

// Example 2: What's erased
public class Container<T extends Number> {
    private T value;

    // Type parameter T is erased to Number
    // T becomes Number after erasure
    public double doubleValue() {
        return value.doubleValue();  // Works because T extends Number
    }
}

// Example 3: Type checking at runtime
public class GenericUtils {
    public static <T> boolean checkType(Object obj, Class<T> type) {
        return type.isInstance(obj);
    }

    public static void main(String[] args) {
        String str = "Hello";
        System.out.println(checkType(str, String.class));   // true
        System.out.println(checkType(str, Integer.class));  // false
    }
}

// Example 4: Instance checks with generics
public class NumberBox<T extends Number> {
    private T number;

    public boolean isInteger() {
        return number instanceof Integer;  // Works because of bound
    }
}
```

## Exercises

1. Create a class that demonstrates type erasure with multiple type parameters.
2. Write a method that checks the actual type of a generic object.
3. What happens when you try to create an array of a generic type?

## Interview Questions

- What is type erasure and why does Java use it?
- Can you use instanceof with generic types?
- What are the limitations imposed by type erasure?

## Common Pitfalls

- Trying to create arrays of generic types (new T[] doesn't work)
- Assuming generic type information is available at runtime
- Not understanding why some operations are restricted

## Best Practices

- Pass Class objects for runtime type checking
- Use bounded types when you need type-specific operations
- Understand the limitations before trying to work around them

## Real World Applications

- Understanding framework internals
- Debugging generic code
- Designing APIs that work with type erasure
- Runtime type checking

## References

- [Type Erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html)
- [Generics FAQ - Type Erasure](https://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeErasure.html)

## Summary

In this topic, you learned about type erasure and its implications for Java generics. Understanding type erasure helps you write better generic code and avoid common pitfalls. Practice with the exercises before learning best practices.
