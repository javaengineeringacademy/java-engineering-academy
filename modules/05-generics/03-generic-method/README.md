# Generic Methods

## Introduction

Generic methods define their own type parameters, independent of the class's type parameters.

## Learning Objectives

- Create generic methods
- Understand type inference
- Use generic methods with different types
- Apply generic methods in algorithms

## Prerequisites

- Generic Classes
- Method declarations
- Type inference

## Why This Matters

Generic methods allow you to create type-safe methods that work with any type, enabling flexible and reusable algorithms.

## Syntax

```java
// Generic method syntax
public <T> ReturnType methodName(T parameter) {
    // Method body
}

// Multiple type parameters
public <T, U> ReturnType methodName(T param1, U param2) {
    // Method body
}

// Bounded type parameters
public <T extends Comparable<T>> ReturnType methodName(T parameter) {
    // Method body
}
```

## Examples

```java
// Example 1: Basic generic method
public class Utility {
    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.print(element + " ");
        }
        System.out.println();
    }
}

Integer[] numbers = {1, 2, 3, 4, 5};
String[] names = {"Alice", "Bob", "Charlie"};

Utility.printArray(numbers);  // 1 2 3 4 5
Utility.printArray(names);    // Alice Bob Charlie

// Example 2: Generic method with return type
public static <T extends Comparable<T>> T findMax(T[] array) {
    T max = array[0];
    for (T element : array) {
        if (element.compareTo(max) > 0) {
            max = element;
        }
    }
    return max;
}

Integer[] nums = {3, 1, 4, 1, 5, 9};
Integer max = findMax(nums);  // 9

// Example 3: Generic method with multiple parameters
public static <T, U> Map<T, U> zipToMap(T[] keys, U[] values) {
    Map<T, U> map = new HashMap<>();
    for (int i = 0; i < keys.length; i++) {
        map.put(keys[i], values[i]);
    }
    return map;
}

String[] names = {"Alice", "Bob"};
Integer[] ages = {30, 25};
Map<String, Integer> personAges = zipToMap(names, ages);

// Example 4: Type inference with diamond operator
List<String> list = Utility.convert(Arrays.asList(1, 2, 3), String::valueOf);
```

## Exercises

1. Create a generic method that swaps two elements in an array.
2. Write a generic method that filters elements based on a condition.
3. Create a generic method that converts a List to an array and vice versa.

## Interview Questions

- What is type inference?
- Can you call a generic method without specifying type arguments?
- How do you create a generic static method?

## Common Pitfalls

- Not using type inference when possible
- Overcomplicating generic method signatures
- Not constraining types when necessary

## Best Practices

- Let the compiler infer types when possible
- Use bounded types for operations that require specific interfaces
- Keep generic methods focused and simple

## Real World Applications

- Utility methods
- Algorithm implementations
- Data conversion methods
- Collection manipulation methods

## References

- [Generic Methods Tutorial](https://docs.oracle.com/javase/tutorial/java/generics/methods.html)
- [Type Inference](https://docs.oracle.com/javase/tutorial/java/generics/genTypes.html)

## Summary

In this topic, you learned how to create and use generic methods. Generic methods enable type-safe, reusable algorithms. Practice with the exercises before learning about bounded types.
