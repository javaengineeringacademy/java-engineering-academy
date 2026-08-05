# Objects in Java

## Overview
Objects are runtime instances of classes. They occupy heap memory and are accessed through reference variables.

## When to Use
- Every time you need a runtime instance of a class
- When you need to maintain state across method calls
- When modeling entities with identity and behavior

## Code Example
See `src/main/java/academy/javaengineering/oop/objects/ObjectDemo.java`

```java
ObjectDemo obj = new ObjectDemo("hello");
ObjectDemo ref = obj; // Same object
ref.setData("world");
System.out.println(obj.getData()); // "world"
```

## Common Mistakes
1. Using `==` to compare object content (compares references, not values)
2. Forgetting objects are passed by value (reference is copied)
3. Not handling null references leading to NullPointerException
4. Creating circular references that prevent garbage collection

## Interview Questions
1. What is the difference between `==` and `.equals()`?
2. How does Java pass arguments to methods?
3. What is garbage collection and when does it occur?
4. What is a memory leak in Java and how can it happen?
5. What is the difference between heap and stack memory?
