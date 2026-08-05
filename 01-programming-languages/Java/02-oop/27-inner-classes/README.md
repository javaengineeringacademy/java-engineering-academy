# Inner Classes in Java

## Overview
Inner classes are classes defined within other classes. They include non-static inner classes, static nested classes, local classes, and anonymous classes.

## When to Use
- Non-static inner class: when the inner class needs access to outer instance
- Static nested class: when the inner class doesn't need outer instance
- For logically grouping classes that belong together

## Code Example
See `src/main/java/academy/javaengineering/oop/innerclasses/Outer.java`

```java
Outer outer = new Outer("Hello");
Outer.Inner inner = outer.new Inner();
Outer.StaticNested nested = new Outer.StaticNested("World");
```

## Common Mistakes
1. Using non-static when static would work (memory leak risk)
2. Not understanding outer class reference in inner classes
3. Overusing anonymous classes (use lambdas instead)
4. Creating memory leaks with inner class references

## Interview Questions
1. What is the difference between inner and static nested classes?
2. How do inner classes access outer class members?
3. What are local and anonymous classes?
4. When would you use a static nested class?
5. How do inner classes affect serialization?
