# Constructors in Java

## Overview
Constructors are special methods called when an object is created. They initialize the object's state.

## When to Use
- To initialize objects with default or custom values
- To enforce invariants at creation time
- To create copies of existing objects

## Code Example
See `src/main/java/academy/javaengineering/oop/constructors/ConstructorDemo.java`

```java
ConstructorDemo default = new ConstructorDemo();
ConstructorDemo custom = new ConstructorDemo("Alice", 30);
ConstructorDemo copy = new ConstructorDemo(custom);
```

## Common Mistakes
1. Defining a return type on constructors
2. Not chaining constructors (code duplication)
3. Exposing mutable objects in constructor parameters
4. Forgetting to create a copy constructor for immutable classes

## Interview Questions
1. What is constructor chaining and how does `this()` work?
2. What is the difference between a default constructor and a no-arg constructor?
3. Can a constructor be private? When would you do this?
4. What is a copy constructor and why is it important?
5. What happens if you don't define any constructor?
