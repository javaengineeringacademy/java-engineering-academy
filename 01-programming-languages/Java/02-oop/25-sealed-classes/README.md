# Sealed Classes in Java

## Overview
Sealed classes restrict which other classes or interfaces may extend or implement them. They provide controlled inheritance hierarchies.

## When to Use
- When you want to limit which classes can extend your class
- For domain modeling with a fixed set of subtypes
- To enable exhaustive pattern matching in switch expressions

## Code Example
See `src/main/java/academy/javaengineering/oop/sealed/` (Shape.java, Circle.java, Rectangle.java)

```java
sealed class Shape permits Circle, Rectangle { }
final class Circle extends Shape { }
final class Rectangle extends Shape { }
```

## Common Mistakes
1. Forgetting to make permitted subclasses final/sealed/permits
2. Not using permits clause
3. Creating open hierarchies when sealed is needed
4. Not combining with pattern matching

## Interview Questions
1. What is a sealed class and how does it differ from final?
2. What modifiers can permitted subclasses have?
3. How do sealed classes enable pattern matching?
4. What are the benefits of sealed classes?
5. Can sealed classes implement interfaces?
