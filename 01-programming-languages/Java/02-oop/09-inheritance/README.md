# Inheritance in Java

## Overview
Inheritance allows a class (child/subclass) to inherit fields and methods from another class (parent/superclass), promoting code reuse.

## When to Use
- When classes share common behavior and state
- To establish an "is-a" relationship (Dog is an Animal)
- To enable polymorphism through method overriding

## Code Example
See `src/main/java/academy/javaengineering/oop/inheritance/` (Animal.java, Dog.java, Cat.java)

```java
Animal animal = new Dog("Rex", 5, "Shepherd");
animal.eat();  // Inherited method
((Dog) animal).fetch("ball"); // Cast for subclass method
```

## Common Mistakes
1. Using inheritance when composition is more appropriate
2. Breaking the Liskov Substitution Principle
3. Not calling `super()` in constructors
4. Creating deep inheritance hierarchies (prefer composition)

## Interview Questions
1. What is the difference between inheritance and composition?
2. What is the diamond problem and how does Java solve it?
3. What is method hiding vs method overriding?
4. Can you override private or static methods?
5. What is the `super` keyword used for?
