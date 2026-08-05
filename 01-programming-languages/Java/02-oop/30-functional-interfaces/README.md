# Functional Interfaces in Java

## Overview
A functional interface has exactly one abstract method, enabling lambda expressions and method references.

## When to Use
- To enable lambda expressions and method references
- For callback mechanisms and event handling
- For stream operations and functional programming

## Code Example
See `src/main/java/academy/javaengineering/oop/functionalinterfaces/MyFunction.java`

```java
MyFunction<String, Integer> len = String::length;
len.apply("Hello");  // 5

MyFunction<String, Boolean> check = len.andThen(l -> l > 3);
check.apply("Hi");   // false
```

## Common Mistakes
1. Adding more than one abstract method
2. Not using @FunctionalInterface annotation
3. Confusing with regular interfaces
4. Not understanding SAM (Single Abstract Method) conversion

## Interview Questions
1. What is a functional interface?
2. What are the built-in functional interfaces?
3. What is SAM conversion?
4. How do lambdas relate to functional interfaces?
5. What is the difference between Predicate, Function, and Consumer?
