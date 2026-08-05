# Classes in Java

## Overview
A class is a blueprint for creating objects. It defines the state (fields) and behavior (methods) that objects of that type will have.

## When to Use
- To model real-world entities (Person, Order, Product)
- To encapsulate data and behavior together
- To create reusable code structures

## Code Example
See `src/main/java/academy/javaengineering/oop/classes/Person.java`

```java
Person person = new Person("Alice", 30, "alice@example.com");
System.out.println(person.getName()); // Alice
person.setAge(31);
```

## Common Mistakes
1. Making all fields public instead of using getters/setters
2. Forgetting to override `equals()` and `hashCode()`
3. Not validating input in setters
4. Creating classes that do too much (violation of SRP)

## Interview Questions
1. What is the difference between a class and an object?
2. What are access modifiers and why use private fields?
3. What is the purpose of `toString()`, `equals()`, and `hashCode()`?
4. What is a static nested class vs a non-static nested class?
5. Can a class extend multiple classes in Java?
