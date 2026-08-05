# Methods in Java

## Overview
Methods define the behavior of objects and classes. They can be instance-based (per object) or static (per class).

## When to Use
- Instance methods for behavior that depends on object state
- Static methods for utility functions and factory methods
- Overloading for flexible API design with different parameter types

## Code Example
See `src/main/java/academy/javaengineering/oop/methods/MethodDemo.java`

```java
MethodDemo obj = new MethodDemo("Alice");
obj.greet();           // Instance method
MethodDemo.add(5, 3);  // Static method
obj.greet("Hi");       // Overloaded method
```

## Common Mistakes
1. Overloading vs overriding confusion
2. Using static methods when instance methods are needed
3. Not using varargs when appropriate
4. Creating side effects in methods that should be pure

## Interview Questions
1. What is the difference between method overloading and overriding?
2. Can static methods be overridden? Why or why not?
3. What is a varargs parameter and what are its restrictions?
4. What is the difference between `final`, `finally`, and `finalize()`?
5. What is method chaining and how is it implemented?
