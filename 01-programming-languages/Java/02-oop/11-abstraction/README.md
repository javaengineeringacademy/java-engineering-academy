# Abstraction in Java

## Overview
Abstraction hides implementation details and shows only functionality. Abstract classes and interfaces are the two main ways to achieve abstraction.

## When to Use
- When you want to define a contract without implementation
- When subclasses share common behavior but differ in specifics
- To achieve loose coupling between components

## Code Example
See `src/main/java/academy/javaengineering/oop/abstraction/` (Vehicle.java, Car.java, Motorcycle.java)

```java
Vehicle car = new Car("Toyota", "Camry", 2023, 4);
car.start();       // Polymorphic call
car.fuelEfficiency(); // Different per subclass
```

## Common Mistakes
1. Trying to instantiate abstract classes
2. Not implementing all abstract methods in subclasses
3. Using abstract classes when interfaces would be better
4. Over-abstracting (too many layers)

## Interview Questions
1. What is the difference between abstraction and encapsulation?
2. When would you use an abstract class vs an interface?
3. Can abstract classes have constructors?
4. What are concrete methods in abstract classes?
5. Can an abstract class extend another abstract class?
