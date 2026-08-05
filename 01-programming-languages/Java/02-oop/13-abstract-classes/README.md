# Abstract Classes in Java

## Overview
Abstract classes combine the features of interfaces and concrete classes. They can have both abstract methods (no body) and concrete methods (with body).

## When to Use
- When subclasses share common state and behavior
- When you need constructors in the type hierarchy
- When you want to provide default implementations

## Code Example
See `src/main/java/academy/javaengineering/oop/abstractclasses/` (Employee.java, Manager.java)

```java
Employee emp = new Manager("Alice", "M001", 100000, 5);
emp.calculateBonus();  // Abstract - implemented by Manager
emp.getDetails();      // Concrete - inherited from Employee
```

## Common Mistakes
1. Confusing abstract classes with interfaces
2. Not implementing all abstract methods
3. Making everything abstract (defeats the purpose)
4. Deep abstract class hierarchies

## Interview Questions
1. When would you choose an abstract class over an interface?
2. Can abstract classes have final methods?
3. What is the difference between abstract class and template method pattern?
4. Can abstract classes have instance variables?
5. What happens if a subclass doesn't implement all abstract methods?
