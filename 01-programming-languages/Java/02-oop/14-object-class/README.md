# Object Class

## Introduction

The Object class is the root of the Java class hierarchy, serving as the ultimate superclass from which every class in Java directly or indirectly inherits. Every class in Java extends Object by default, even if no explicit `extends` clause is written, which means all objects in Java share a common set of methods that provide fundamental functionality for object comparison, string representation, hash code calculation, and runtime type information. Understanding the Object class is essential because its methods define the basic contract that all Java objects must fulfill, and properly overriding these methods is crucial for creating classes that work correctly with collections, frameworks, and other Java APIs that rely on these fundamental Object class behaviors.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the role of the Object class as the root of the Java class hierarchy
- [ ] Properly override equals(), hashCode(), toString(), and other Object class methods
- [ ] Use instanceof operator and getClass() method for runtime type checking
- [ ] Implement clone() and finalize() methods understanding their implications and alternatives

## Prerequisites

- [02-classes](../02-classes/README.md) - Understanding class declaration and structure
- [03-objects](../03-objects/README.md) - Object creation and memory allocation
- [06-this-keyword](../06-this-keyword/README.md) - Reference to current object instance
- [09-inheritance](../09-inheritance/README.md) - How classes inherit from parent classes

## Why This Concept Exists

### The Problem

In any object-oriented language, objects need basic common behaviors that allow them to:

1. **Compare equality**: Determine if two objects are logically equal
2. **Generate hash codes**: Work correctly in hash-based collections like HashMap and HashSet
3. **Provide string representation**: Convert objects to readable text for debugging and logging
4. **Support cloning**: Create copies of objects when needed
5. **Identify type**: Determine the runtime type of an object

Without a standardized base class providing these behaviors, each class would need to reimplement them independently, leading to inconsistencies and incompatibilities across different parts of the codebase and third-party libraries.

### The Solution

The Object class provides default implementations of these fundamental behaviors, and Java requires all classes to inherit from Object. This ensures that:

- Every Java object has a consistent set of basic methods
- Collections and frameworks can work with any object type
- Developers can override these methods to provide custom behavior while maintaining a common contract

### Real-World Analogy

Think of the Object class as the **DNA of all living organisms**. Just as every living thing shares common genetic material that provides basic life functions (metabolism, reproduction, cell structure), every Java object shares the Object class methods that provide basic object functions (comparison, hashing, type identification). Individual species (classes) can specialize and add their own unique features, but they all start with the same fundamental blueprint.

## Internal Working

### JVM Perspective

The Object class has special treatment in the JVM:

1. **Class Loading**: Object is one of the first classes loaded by the bootstrap class loader when the JVM starts.

2. **Method Table**: Every class has a method table (vtable) that includes all inherited methods from Object. This enables dynamic method dispatch for overridden methods.

3. **Memory Layout**: Every object instance includes a header with:
   - Mark word: Contains hash code, GC age, lock information
   - Klass pointer: Points to the class metadata

4. **Default Initialization**: When an object is created, the JVM ensures all Object methods are available before the constructor runs.

### Memory Representation

```
Object Instance Memory Layout:
┌─────────────────────────────────┐
│ Object Header                   │
│ ├── Mark Word (64 bits)         │
│ │   ├── Hash Code               │
│ │   ├── GC Age                  │
│ │   └── Lock Status             │
│ └── Klass Pointer (32/64 bits)  │
├─────────────────────────────────┤
│ Instance Data (class fields)    │
├─────────────────────────────────┤
│ Padding (for alignment)         │
└─────────────────────────────────┘
```

### Method Resolution Order

When a method is called on an object:

1. The JVM looks for the method in the current class
2. If not found, it searches up the inheritance hierarchy
3. Object methods are always at the top of the hierarchy
4. Overridden methods take precedence over parent implementations

## Syntax

### Overriding toString() Method

```java
@Override
public String toString() {
    return "ClassName{" +
        "field1=" + field1 +
        ", field2='" + field2 + '\'' +
        '}';
}
```

### Overriding equals() and hashCode()

```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    MyClass other = (MyClass) obj;
    return Objects.equals(field1, other.field1) &&
           Objects.equals(field2, other.field2);
}

@Override
public int hashCode() {
    return Objects.hash(field1, field2);
}
```

### Using instanceof Operator

```java
if (obj instanceof MyClass) {
    MyClass myObj = (MyClass) obj;
    // Use myObj
}

// Pattern matching (Java 16+)
if (obj instanceof MyClass myObj) {
    // Use myObj directly
}
```

### Implementing Cloneable Interface

```java
public class MyClass implements Cloneable {
    @Override
    public MyClass clone() {
        try {
            return (MyClass) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen
        }
    }
}
```

## Easy Examples

### Example 1: Person Class with Proper Object Methods

**Problem Statement**: Create a Person class that properly implements equals(), hashCode(), and toString() methods to work correctly with collections and provide meaningful string representation.

**Implementation**:

```java
package academy.javaengineering.oop.objectclass;

import java.util.Objects;

class Person {
    private String name;
    private int age;
    private String email;

    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return age == person.age &&
               Objects.equals(name, person.name) &&
               Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, email);
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", email='" + email + "'}";
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
}

public class PersonDemo {
    public static void main(String[] args) {
        Person person1 = new Person("Alice", 30, "alice@example.com");
        Person person2 = new Person("Alice", 30, "alice@example.com");
        Person person3 = new Person("Bob", 25, "bob@example.com");

        // Test equals()
        System.out.println("=== Testing equals() ===");
        System.out.println("person1 == person2: " + (person1 == person2));
        System.out.println("person1.equals(person2): " + person1.equals(person2));
        System.out.println("person1.equals(person3): " + person1.equals(person3));

        // Test toString()
        System.out.println("\n=== Testing toString() ===");
        System.out.println("person1: " + person1);
        System.out.println("person2: " + person2);

        // Test hashCode() for collection usage
        System.out.println("\n=== Testing hashCode() ===");
        System.out.println("person1.hashCode(): " + person1.hashCode());
        System.out.println("person2.hashCode(): " + person2.hashCode());
        System.out.println("Hash codes equal: " + (person1.hashCode() == person2.hashCode()));
    }
}
```

**Expected Output**:
```
=== Testing equals() ===
person1 == person2: false
person1.equals(person2): true
person1.equals(person3): false

=== Testing toString() ===
person1: Person{name='Alice', age=30, email='alice@example.com'}
person2: Person{name='Alice', age=30, email='alice@example.com'}

=== Testing hashCode() ===
person1.hashCode(): 123456
person2.hashCode(): 123456
Hash codes equal: true
```

**Best Practices**:
- Always override equals() and hashCode() together
- Use Objects.hash() for generating hash codes
- Check for null and type compatibility in equals()
- Provide a meaningful toString() for debugging

### Example 2: Using instanceof for Type Checking

**Problem Statement**: Create a system that processes different types of objects and performs type-specific operations using instanceof checks and the getClass() method.

**Implementation**:

```java
package academy.javaengineering.oop.objectclass;

abstract class Shape {
    protected String color;

    public Shape(String color) {
        this.color = color;
    }

    public abstract double calculateArea();
    public abstract String getShapeType();

    @Override
    public String toString() {
        return getShapeType() + "{color='" + color + "', area=" + calculateArea() + "}";
    }
}

class Circle extends Shape {
    private double radius;

    public Circle(double radius, String color) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public String getShapeType() {
        return "Circle";
    }

    public double getRadius() { return radius; }
}

class Rectangle extends Shape {
    private double width, height;

    public Rectangle(double width, double height, String color) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double calculateArea() {
        return width * height;
    }

    @Override
    public String getShapeType() {
        return "Rectangle";
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
}

class ShapeProcessor {
    public static void processShape(Shape shape) {
        System.out.println("Processing: " + shape);

        // Using instanceof with pattern matching (Java 16+)
        if (shape instanceof Circle circle) {
            System.out.println("  Circle radius: " + circle.getRadius());
            System.out.println("  Circumference: " + (2 * Math.PI * circle.getRadius()));
        } else if (shape instanceof Rectangle rect) {
            System.out.println("  Rectangle dimensions: " + rect.getWidth() + " x " + rect.getHeight());
            System.out.println("  Perimeter: " + (2 * (rect.getWidth() + rect.getHeight())));
        }

        // Using getClass() for exact type matching
        System.out.println("  Exact class: " + shape.getClass().getSimpleName());
        System.out.println("  Is Shape: " + (shape instanceof Shape));
    }

    public static boolean isSameType(Object obj1, Object obj2) {
        return obj1.getClass() == obj2.getClass();
    }
}

public class TypeCheckingDemo {
    public static void main(String[] args) {
        Shape circle = new Circle(5.0, "red");
        Shape rectangle = new Rectangle(4.0, 6.0, "blue");
        Shape anotherCircle = new Circle(3.0, "green");

        System.out.println("=== Processing Shapes ===");
        ShapeProcessor.processShape(circle);
        System.out.println();
        ShapeProcessor.processShape(rectangle);

        System.out.println("\n=== Type Comparison ===");
        System.out.println("circle and rectangle same type: " +
            ShapeProcessor.isSameType(circle, rectangle));
        System.out.println("circle and anotherCircle same type: " +
            ShapeProcessor.isSameType(circle, anotherCircle));

        // Demonstrate getClass() vs instanceof
        System.out.println("\n=== getClass() vs instanceof ===");
        Shape shape = new Circle(5.0, "red");
        System.out.println("shape instanceof Circle: " + (shape instanceof Circle));
        System.out.println("shape instanceof Shape: " + (shape instanceof Shape));
        System.out.println("shape.getClass() == Circle.class: " + (shape.getClass() == Circle.class));
        System.out.println("shape.getClass() == Shape.class: " + (shape.getClass() == Shape.class));
    }
}
```

**Expected Output**:
```
=== Processing Shapes ===
Processing: Circle{color='red', area=78.53981633974483}
  Circle radius: 5.0
  Circumference: 31.41592653589793
  Exact class: Circle
  Is Shape: true

Processing: Rectangle{color='blue', area=24.0}
  Rectangle dimensions: 4.0 x 6.0
  Perimeter: 20.0
  Exact class: Rectangle
  Is Shape: true

=== Type Comparison ===
circle and rectangle same type: false
circle and anotherCircle same type: true

=== getClass() vs instanceof ===
shape instanceof Circle: true
shape instanceof Shape: true
shape.getClass() == Circle.class: true
shape.getClass() == Shape.class: false
```

**Best Practices**:
- Use instanceof for checking if an object is an instance of a class or its subclasses
- Use getClass() when you need exact type matching (not subclass matching)
- Prefer pattern matching (Java 16+) for cleaner instanceof checks

---

## Continue Reading

- Part 2
- Part 3
- Part 4
