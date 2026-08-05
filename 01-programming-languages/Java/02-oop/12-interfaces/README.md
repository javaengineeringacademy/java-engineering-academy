# Interfaces in Java

## Overview
Interfaces define a contract that classes must follow. They support multiple inheritance and are key to achieving loose coupling.

## When to Use
- To define capabilities that unrelated classes can implement
- When you need multiple inheritance of type
- To achieve loose coupling and dependency inversion

## Code Example
See `src/main/java/academy/javaengineering/oop/interfaces/` (Printable.java, Book.java)

```java
Printable p = new Book("Java", "Author", 300);
p.format();            // Implemented method
p.printWithHeader();   // Default method
Printable.version();   // Static method
```

## Common Mistakes
1. Putting implementation in interfaces (before Java 8 defaults)
2. Not using interfaces for dependency injection
3. Creating fat interfaces (Interface Segregation Principle)
4. Confusing interfaces with abstract classes

## Interview Questions
1. What is the difference between an interface and an abstract class?
2. What are default and static methods in interfaces?
3. Can an interface extend multiple interfaces?
4. What is the diamond problem with interfaces?
5. What functional interfaces does Java provide?
