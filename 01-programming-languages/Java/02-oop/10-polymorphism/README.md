# Polymorphism in Java

## Overview
Polymorphism allows objects of different types to be treated through a common interface. The correct method is called at runtime based on the actual object type.

## When to Use
- When you need to process different types uniformly
- To write flexible, extensible code
- To implement strategy and template patterns

## Code Example
See `src/main/java/academy/javaengineering/oop/polymorphism/` (Shape.java, Circle.java, Rectangle.java)

```java
Shape[] shapes = { new Circle("red", 5), new Rectangle("blue", 4, 6) };
for (Shape s : shapes) {
    System.out.println(s.area()); // Calls correct implementation
}
```

## Common Mistakes
1. Confusing overloading with overriding
2. Not considering Liskov Substitution Principle
3. Excessive downcasting (design smell)
4. Assuming static methods can be overridden

## Interview Questions
1. What is the difference between compile-time and runtime polymorphism?
2. What is dynamic method dispatch?
3. What is the Liskov Substitution Principle?
4. How does Java achieve runtime polymorphism?
5. What is covariant return types?
