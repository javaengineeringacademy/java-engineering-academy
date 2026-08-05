# Composition in Java

## Overview
Composition is a "has-a" relationship where objects contain other objects as fields. The contained objects are created and managed by the container.

## When to Use
- When an object is made up of other objects
- To achieve code reuse without inheritance
- When the contained object lifecycle is managed by the container

## Code Example
See `src/main/java/academy/javaengineering/oop/composition/` (Engine.java, Car.java)

```java
Engine engine = new Engine("V6", 300);
Car car = new Car("Toyota", "Camry", 2023, engine);
car.start();  // Engine starts with the car
```

## Common Mistakes
1. Using inheritance when composition is better ("favor composition over inheritance")
2. Creating circular compositions
3. Not managing lifecycle properly
4. Exposing internal composed objects

## Interview Questions
1. What is the difference between composition and inheritance?
2. When would you choose composition over inheritance?
3. What is the "has-a" vs "is-a" relationship?
4. How does composition help with testability?
5. What is dependency injection and how does it relate to composition?
