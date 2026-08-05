# Method Overriding in Java

## Overview
Method overriding allows a subclass to provide a specific implementation of a method already defined in its parent class.

## When to Use
- To customize behavior of inherited methods
- To achieve runtime polymorphism
- To implement template method pattern

## Code Example
See `src/main/java/academy/javaengineering/oop/methodoverriding/` (Animal.java, Dog.java)

```java
Animal animal = new Dog("Rex", "Shepherd");
animal.speak();  // Calls Dog's speak() at runtime
```

## Common Mistakes
1. Changing method signature (becomes overloading, not overriding)
2. Reducing access modifier visibility
3. Making overridden method throw new checked exceptions
4. Forgetting @Override annotation

## Interview Questions
1. What are the rules for method overriding in Java?
2. Can you override private or static methods?
3. What is covariant return type?
4. How does @Override annotation help?
5. What is the super keyword used for in overriding?
