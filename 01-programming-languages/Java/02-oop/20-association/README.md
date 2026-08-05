# Association in Java

## Overview
Association is a relationship between two classes where objects of one class are linked to objects of another. It can be unidirectional or bidirectional.

## When to Use
- When objects need to reference each other
- For many-to-many relationships
- When objects have independent lifecycles

## Code Example
See `src/main/java/academy/javaengineering/oop/association/` (Teacher.java, Student.java)

```java
Teacher t = new Teacher("Dr. Smith", "Math");
Student s = new Student("Alice", "S001");
t.addStudent(s);  // Bidirectional
```

## Common Mistakes
1. Creating circular references without proper management
2. Not handling bidirectional consistency
3. Using strong references when weak references are needed
4. Not using appropriate collections

## Interview Questions
1. What is the difference between association, aggregation, and composition?
2. How do you implement bidirectional association?
3. What are the pros and cons of bidirectional vs unidirectional?
4. How does association differ from dependency?
5. What design patterns use association?
