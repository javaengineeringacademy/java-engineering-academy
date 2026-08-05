# Aggregation in Java

## Overview
Aggregation is a "has-a" relationship where the contained objects can exist independently of the container.

## When to Use
- When objects have independent lifecycles
- For "part-of" relationships (e.g., department is part of university)
- When the container doesn't manage contained object creation

## Code Example
See `src/main/java/academy/javaengineering/oop/aggregation/` (Department.java, University.java)

```java
Department cs = new Department("CS", List.of("Java", "Python"));
University uni = new University("MIT");
uni.addDepartment(cs);  // CS exists independently
```

## Common Mistakes
1. Confusing aggregation with composition
2. Not using defensive copies
3. Creating tight coupling between aggregated objects
4. Not managing references properly

## Interview Questions
1. What is the difference between aggregation and composition?
2. When would you use aggregation vs composition?
3. How do you implement defensive copying?
4. What is the lifetime management in aggregation?
5. What design patterns use aggregation?
