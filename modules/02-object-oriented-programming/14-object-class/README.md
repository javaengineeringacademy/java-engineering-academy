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
- Avoid using instanceof too frequently as it can indicate design issues

### Example 3: Object Class Methods in Collections

**Problem Statement**: Demonstrate how Object class methods affect the behavior of objects in Java collections like HashSet and HashMap.

**Implementation**:

```java
package academy.javaengineering.oop.objectclass;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Objects;

class Student {
    private String id;
    private String name;
    private double gpa;

    public Student(String id, String name, double gpa) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{id='" + id + "', name='" + name + "', gpa=" + gpa + "}";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getGpa() { return gpa; }
}

public class CollectionDemo {
    public static void main(String[] args) {
        // Without proper equals/hashCode
        System.out.println("=== Without Proper equals/hashCode ===");
        HashSet<Student> studentSet = new HashSet<>();
        Student student1 = new Student("S001", "Alice", 3.8);
        Student student2 = new Student("S001", "Alice", 3.8);

        studentSet.add(student1);
        studentSet.add(student2);

        System.out.println("Set size (should be 1): " + studentSet.size());
        System.out.println("Contains student1: " + studentSet.contains(student1));
        System.out.println("Contains student2: " + studentSet.contains(student2));

        // Using as HashMap key
        System.out.println("\n=== HashMap Usage ===");
        HashMap<Student, String> studentGrades = new HashMap<>();
        studentGrades.put(student1, "A");
        studentGrades.put(student2, "A+");

        System.out.println("Map size (should be 1): " + studentGrades.size());
        System.out.println("Value for student1: " + studentGrades.get(student1));
        System.out.println("Value for student2: " + studentGrades.get(student2));

        // Demonstrate the importance of consistent equals/hashCode
        System.out.println("\n=== Consistency Check ===");
        Student student3 = new Student("S002", "Bob", 3.5);
        System.out.println("student1.equals(student3): " + student1.equals(student3));
        System.out.println("student1.hashCode() == student3.hashCode(): " +
            (student1.hashCode() == student3.hashCode()));
    }
}
```

**Expected Output**:
```
=== Without Proper equals/hashCode ===
Set size (should be 1): 1
Contains student1: true
Contains student2: true

=== HashMap Usage ===
Map size (should be 1): 1
Value for student1: A+
Value for student2: A+

=== Consistency Check ===
student1.equals(student3): false
student1.hashCode() == student3.hashCode(): false
```

**Best Practices**:
- Always override equals() and hashCode() when objects will be used in collections
- Ensure equals() and hashCode() are consistent: if a.equals(b) then a.hashCode() == b.hashCode()
- Use immutable fields for hashCode() calculation when possible
- Consider using IDE-generated equals() and hashCode() methods

## Medium Examples

### Example 1: Deep vs Shallow Clone

**Problem Statement**: Implement a class that demonstrates the difference between shallow and deep cloning, showing how each affects object references and nested objects.

**Requirements**:

- Implement Cloneable interface
- Demonstrate shallow clone behavior
- Implement deep clone for complex objects
- Show the impact on nested object references

**Implementation**:

```java
package academy.javaengineering.oop.objectclass;

class Address implements Cloneable {
    private String city;
    private String country;

    public Address(String city, String country) {
        this.city = city;
        this.country = country;
    }

    @Override
    public Address clone() {
        try {
            return (Address) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Address address = (Address) obj;
        return Objects.equals(city, address.city) &&
               Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, country);
    }

    @Override
    public String toString() {
        return "Address{city='" + city + "', country='" + country + "'}";
    }

    public String getCity() { return city; }
    public String getCountry() { return country; }
    public void setCity(String city) { this.city = city; }
}

class Employee implements Cloneable {
    private String name;
    private Address address;
    private String[] skills;

    public Employee(String name, Address address, String[] skills) {
        this.name = name;
        this.address = address;
        this.skills = skills;
    }

    // Shallow clone
    @Override
    public Employee clone() {
        try {
            return (Employee) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // Deep clone
    public Employee deepClone() {
        try {
            Employee cloned = (Employee) super.clone();
            cloned.address = this.address.clone();
            cloned.skills = this.skills.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "Employee{name='" + name + "', address=" + address +
               ", skills=" + java.util.Arrays.toString(skills) + "}";
    }

    public String getName() { return name; }
    public Address getAddress() { return address; }
    public String[] getSkills() { return skills; }
    public void setName(String name) { this.name = name; }
}

public class CloneDemo {
    public static void main(String[] args) {
        System.out.println("=== Shallow Clone Demo ===");
        Address address = new Address("New York", "USA");
        String[] skills = {"Java", "Python", "SQL"};
        Employee original = new Employee("Alice", address, skills);

        Employee shallowClone = original.clone();

        System.out.println("Original: " + original);
        System.out.println("Shallow Clone: " + shallowClone);

        // Modify the clone's address
        shallowClone.getAddress().setCity("Boston");
        System.out.println("\nAfter modifying clone's address:");
        System.out.println("Original address: " + original.getAddress());
        System.out.println("Clone address: " + shallowClone.getAddress());
        System.out.println("Note: Both share the same Address object!");

        System.out.println("\n=== Deep Clone Demo ===");
        Address address2 = new Address("San Francisco", "USA");
        String[] skills2 = {"JavaScript", "React", "Node.js"};
        Employee original2 = new Employee("Bob", address2, skills2);

        Employee deepClone = original2.deepClone();

        System.out.println("Original: " + original2);
        System.out.println("Deep Clone: " + deepClone);

        // Modify the clone's address
        deepClone.getAddress().setCity("Los Angeles");
        System.out.println("\nAfter modifying clone's address:");
        System.out.println("Original address: " + original2.getAddress());
        System.out.println("Clone address: " + deepClone.getAddress());
        System.out.println("Note: They have separate Address objects!");
    }
}
```

**Expected Output**:
```
=== Shallow Clone Demo ===
Original: Employee{name='Alice', address=Address{city='New York', country='USA'}, skills=[Java, Python, SQL]}
Shallow Clone: Employee{name='Alice', address=Address{city='New York', country='USA'}, skills=[Java, Python, SQL]}

After modifying clone's address:
Original address: Address{city='Boston', country='USA'}
Clone address: Address{city='Boston', country='USA'}
Note: Both share the same Address object!

=== Deep Clone Demo ===
Original: Employee{name='Bob', address=Address{city='San Francisco', country='USA'}, skills=[JavaScript, React, Node.js]}
Deep Clone: Employee{name='Bob', address=Address{city='San Francisco', country='USA'}, skills=[JavaScript, React, Node.js]}

After modifying clone's address:
Original address: Address{city='San Francisco', country='USA'}
Clone address: Address{city='Los Angeles', country='USA'}
Note: They have separate Address objects!
```

**Code Walkthrough**:

1. **Shallow Clone**: Uses `super.clone()` which copies all fields, but object references still point to the same objects.

2. **Deep Clone**: Creates new copies of all mutable objects, ensuring the clone is completely independent.

3. **Impact**: With shallow clone, modifying a nested object affects both original and clone. With deep clone, changes are isolated.

**Alternative Solution**:

```java
// Using copy constructor instead of clone
class EmployeeCopy {
    private String name;
    private Address address;
    private String[] skills;

    // Copy constructor for deep copy
    public EmployeeCopy(EmployeeCopy other) {
        this.name = other.name;
        this.address = other.address.clone();
        this.skills = other.skills.clone();
    }

    // Regular constructor
    public EmployeeCopy(String name, Address address, String[] skills) {
        this.name = name;
        this.address = address;
        this.skills = skills;
    }
}

// This approach is often preferred over clone() as it's more explicit
```

### Example 2: finalize() Method and Resource Cleanup

**Problem Statement**: Demonstrate the proper use and limitations of the finalize() method, showing why it's deprecated and what alternatives exist.

**Requirements**:

- Show finalize() usage (deprecated)
- Demonstrate try-with-resources pattern
- Compare finalization timing with deterministic cleanup
- Show the impact on garbage collection

**Implementation**:

```java
package academy.javaengineering.oop.objectclass;

import java.lang.ref.Cleaner;

// Deprecated approach using finalize()
class DeprecatedResource {
    private String name;
    private boolean isOpen;

    public DeprecatedResource(String name) {
        this.name = name;
        this.isOpen = true;
        System.out.println("DeprecatedResource opened: " + name);
    }

    public void use() {
        if (!isOpen) {
            throw new IllegalStateException("Resource is closed");
        }
        System.out.println("Using DeprecatedResource: " + name);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (isOpen) {
                System.out.println("finalize() called - closing resource: " + name);
                close();
            }
        } finally {
            super.finalize();
        }
    }

    private void close() {
        isOpen = false;
        System.out.println("DeprecatedResource closed: " + name);
    }
}

// Modern approach using try-with-resources
class ModernResource implements AutoCloseable {
    private String name;
    private boolean isOpen;

    public ModernResource(String name) {
        this.name = name;
        this.isOpen = true;
        System.out.println("ModernResource opened: " + name);
    }

    public void use() {
        if (!isOpen) {
            throw new IllegalStateException("Resource is closed");
        }
        System.out.println("Using ModernResource: " + name);
    }

    @Override
    public void close() {
        if (isOpen) {
            isOpen = false;
            System.out.println("ModernResource closed: " + name);
        }
    }

    @Override
    public String toString() {
        return "ModernResource{name='" + name + "', isOpen=" + isOpen + "}";
    }
}

// Alternative using Cleaner (Java 9+)
class CleanerResource implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();
    private final String name;
    private final Cleaner.Cleanable cleanable;

    // Clean action that does not reference the object itself
    private static class CleanAction implements Runnable {
        private final String name;

        CleanAction(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("Cleaner action - cleaning resource: " + name);
        }
    }

    public CleanerResource(String name) {
        this.name = name;
        this.cleanable = cleaner.register(this, new CleanAction(name));
        System.out.println("CleanerResource opened: " + name);
    }

    public void use() {
        System.out.println("Using CleanerResource: " + name);
    }

    @Override
    public void close() {
        cleanable.clean();
    }
}

public class FinalizeDemo {
    public static void main(String[] args) {
        System.out.println("=== Deprecated finalize() Demo ===");
        demoDeprecatedFinalize();

        System.out.println("\n=== Modern try-with-resources Demo ===");
        demoModernResource();

        System.out.println("\n=== Cleaner API Demo ===");
        demoCleanerResource();

        System.out.println("\n=== Forcing Garbage Collection ===");
        forceGarbageCollection();
    }

    private static void demoDeprecatedFinalize() {
        DeprecatedResource resource = new DeprecatedResource("Deprecated");
        resource.use();
        resource = null; // Make eligible for GC

        System.out.println("Resource set to null, waiting for finalize()...");
        System.gc(); // Request GC (not guaranteed)
        try {
            Thread.sleep(1000); // Wait for finalization
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void demoModernResource() {
        try (ModernResource resource = new ModernResource("Modern")) {
            resource.use();
        } // Automatically closed here

        System.out.println("Resource closed deterministically");
    }

    private static void demoCleanerResource() {
        CleanerResource resource = new CleanerResource("Cleaner");
        resource.use();
        resource.close(); // Explicitly trigger cleanup

        System.out.println("Resource cleanup triggered");
    }

    private static void forceGarbageCollection() {
        System.out.println("Requesting garbage collection...");
        System.gc();

        Runtime runtime = Runtime.getRuntime();
        System.out.println("Free memory: " + runtime.freeMemory() / 1024 + " KB");
        System.out.println("Total memory: " + runtime.totalMemory() / 1024 + " KB");
        System.out.println("Max memory: " + runtime.maxMemory() / 1024 + " KB");
    }
}
```

**Expected Output**:
```
=== Deprecated finalize() Demo ===
DeprecatedResource opened: Deprecated
Using DeprecatedResource: Deprecated
Resource set to null, waiting for finalize()...
finalize() called - closing resource: Deprecated
DeprecatedResource closed: Deprecated

=== Modern try-with-resources Demo ===
ModernResource opened: Modern
Using ModernResource: Modern
ModernResource closed: Modern
Resource closed deterministically

=== Cleaner API Demo ===
CleanerResource opened: Cleaner
Using CleanerResource: Cleaner
Cleaner action - cleaning resource: Cleaner
Resource cleanup triggered

=== Forcing Garbage Collection ===
Requesting garbage collection...
Free memory: 12345 KB
Total memory: 65536 KB
Max memory: 262144 KB
```

**Code Walkthrough**:

1. **Deprecated finalize()**: Runs during garbage collection, timing is unpredictable, can cause resurrection of objects.

2. **try-with-resources**: Deterministic cleanup, guaranteed to run, cleaner code with AutoCloseable interface.

3. **Cleaner API**: Non-deterministic like finalize() but safer because the clean action cannot reference the object.

**Alternative Solution**:

```java
// Using PhantomReference for cleanup (most advanced)
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

class PhantomRefResource {
    private static final ReferenceQueue<PhantomRefResource> queue = new ReferenceQueue<>();
    private static final Map<PhantomReference<PhantomRefResource>, String> refs = new HashMap<>();

    private final String name;

    static {
        Thread cleaner = new Thread(() -> {
            while (true) {
                try {
                    PhantomReference<PhantomRefResource> ref = (PhantomReference<PhantomRefResource>) queue.remove();
                    String name = refs.remove(ref);
                    System.out.println("Cleaning up: " + name);
                    ref.clear();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        cleaner.setDaemon(true);
        cleaner.start();
    }

    public PhantomRefResource(String name) {
        this.name = name;
        PhantomReference<PhantomRefResource> ref = new PhantomReference<>(this, queue);
        refs.put(ref, name);
    }
}
```

## Hard Examples

### Example 1: Custom Object Cache with equals/hashCode Optimization

**Problem Statement**: Design a high-performance object cache that uses custom equals() and hashCode() implementations for efficient storage and retrieval, with support for different equality strategies.

**Requirements**:

- Support multiple equality strategies (by ID, by content, by reference)
- Implement efficient hashCode() for hash-based collections
- Support LRU eviction policy
- Thread-safe operations
- Memory-efficient storage

**Architecture**:

```
Object Cache System
├── CacheEntry (stores cached objects)
├── EqualityStrategy (interface for equality comparison)
│   ├── ReferenceEquality
│   ├── FieldEquality
│   └── ContentEquality
├── LRUCache (main cache implementation)
└── CacheStatistics (performance metrics)
```

**Implementation**:

```java
package academy.javaengineering.oop.objectclass;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

interface EqualityStrategy<T> {
    boolean equals(T obj1, T obj2);
    int hashCode(T obj);
}

class ReferenceEquality<T> implements EqualityStrategy<T> {
    @Override
    public boolean equals(T obj1, T obj2) {
        return obj1 == obj2;
    }

    @Override
    public int hashCode(T obj) {
        return System.identityHashCode(obj);
    }
}

class FieldEquality<T> implements EqualityStrategy<T> {
    private final String[] fields;

    public FieldEquality(String... fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(T obj1, T obj2) {
        // Simplified - in real implementation, use reflection
        return obj1.equals(obj2);
    }

    @Override
    public int hashCode(T obj) {
        // Simplified - in real implementation, use reflection
        return obj.hashCode();
    }
}

class CacheEntry<T> {
    private final T value;
    private final long creationTime;
    private long lastAccessTime;
    private int accessCount;

    public CacheEntry(T value) {
        this.value = value;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessTime = creationTime;
        this.accessCount = 0;
    }

    public T getValue() { return value; }
    public long getCreationTime() { return creationTime; }
    public long getLastAccessTime() { return lastAccessTime; }
    public int getAccessCount() { return accessCount; }

    public void recordAccess() {
        this.lastAccessTime = System.currentTimeMillis();
        this.accessCount++;
    }

    public boolean isExpired(long maxAgeMs) {
        return System.currentTimeMillis() - creationTime > maxAgeMs;
    }
}

class CacheStatistics {
    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();
    private final AtomicLong evictions = new AtomicLong();

    public void recordHit() { hits.incrementAndGet(); }
    public void recordMiss() { misses.incrementAndGet(); }
    public void recordEviction() { evictions.incrementAndGet(); }

    public double getHitRate() {
        long total = hits.get() + misses.get();
        return total == 0 ? 0.0 : (double) hits.get() / total;
    }

    @Override
    public String toString() {
        return String.format("CacheStats{hits=%d, misses=%d, evictions=%d, hitRate=%.2f%%}",
            hits.get(), misses.get(), evictions.get(), getHitRate() * 100);
    }
}

class LRUCache<K, V> {
    private final int maxSize;
    private final long maxAgeMs;
    private final EqualityStrategy<K> keyStrategy;
    private final EqualityStrategy<V> valueStrategy;
    private final LinkedHashMap<K, CacheEntry<V>> cache;
    private final CacheStatistics stats;

    public LRUCache(int maxSize, long maxAgeMs,
                    EqualityStrategy<K> keyStrategy,
                    EqualityStrategy<V> valueStrategy) {
        this.maxSize = maxSize;
        this.maxAgeMs = maxAgeMs;
        this.keyStrategy = keyStrategy;
        this.valueStrategy = valueStrategy;
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, CacheEntry<V>> eldest) {
                if (size() > maxSize) {
                    stats.recordEviction();
                    return true;
                }
                return false;
            }
        };
        this.stats = new CacheStatistics();
    }

    public synchronized V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            stats.recordMiss();
            return null;
        }

        if (entry.isExpired(maxAgeMs)) {
            cache.remove(key);
            stats.recordEviction();
            stats.recordMiss();
            return null;
        }

        entry.recordAccess();
        stats.recordHit();
        return entry.getValue();
    }

    public synchronized void put(K key, V value) {
        // Remove expired entries
        cache.entrySet().removeIf(e -> e.getValue().isExpired(maxAgeMs));

        // If at capacity, remove oldest
        if (cache.size() >= maxSize) {
            Iterator<K> iterator = cache.keySet().iterator();
            if (iterator.hasNext()) {
                K oldest = iterator.next();
                iterator.remove();
                stats.recordEviction();
            }
        }

        cache.put(key, new CacheEntry<>(value));
    }

    public synchronized V remove(K key) {
        CacheEntry<V> entry = cache.remove(key);
        return entry != null ? entry.getValue() : null;
    }

    public synchronized void clear() {
        cache.clear();
    }

    public synchronized int size() {
        return cache.size();
    }

    public CacheStatistics getStats() {
        return stats;
    }

    public synchronized Map<K, V> getAll() {
        Map<K, V> result = new HashMap<>();
        for (Map.Entry<K, CacheEntry<V>> entry : cache.entrySet()) {
            if (!entry.getValue().isExpired(maxAgeMs)) {
                result.put(entry.getKey(), entry.getValue().getValue());
            }
        }
        return result;
    }
}

class CachedUser {
    private final String id;
    private final String name;
    private final String email;

    public CachedUser(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CachedUser user = (CachedUser) obj;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CachedUser{id='" + id + "', name='" + name + "', email='" + email + "'}";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}

public class ObjectCacheDemo {
    public static void main(String[] args) {
        System.out.println("=== Custom Object Cache Demo ===\n");

        // Create cache with 5 second max age
        LRUCache<String, CachedUser> userCache = new LRUCache<>(
            1000, 5000,
            new ReferenceEquality<>(),
            new FieldEquality<>("id")
        );

        // Add some users
        System.out.println("Adding users to cache:");
        userCache.put("U001", new CachedUser("U001", "Alice", "alice@example.com"));
        userCache.put("U002", new CachedUser("U002", "Bob", "bob@example.com"));
        userCache.put("U003", new CachedUser("U003", "Charlie", "charlie@example.com"));

        System.out.println("Cache size: " + userCache.size());

        // Retrieve users
        System.out.println("\nRetrieving users:");
        CachedUser user1 = userCache.get("U001");
        System.out.println("U001: " + user1);

        CachedUser user2 = userCache.get("U002");
        System.out.println("U002: " + user2);

        // Non-existent user
        CachedUser user999 = userCache.get("U999");
        System.out.println("U999: " + user999);

        // Test LRU eviction
        System.out.println("\nTesting LRU eviction:");
        for (int i = 4; i <= 11; i++) {
            userCache.put("U" + String.format("%03d", i),
                new CachedUser("U" + String.format("%03d", i),
                    "User" + i, "user" + i + "@example.com"));
        }

        System.out.println("Cache size after adding 8 more: " + userCache.size());
        System.out.println("U001 still in cache: " + (userCache.get("U001") != null));

        // Print statistics
        System.out.println("\nCache Statistics:");
        System.out.println(userCache.getStats());

        // Test expiration
        System.out.println("\n=== Testing Expiration ===");
        System.out.println("Waiting for entries to expire...");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        CachedUser expiredUser = userCache.get("U003");
        System.out.println("U003 after expiration: " + expiredUser);
        System.out.println("Cache size after expiration: " + userCache.size());
        System.out.println(userCache.getStats());
    }
}
```

**Execution Flow**:

1. **Cache Initialization**: LRU cache is created with max size and age parameters
2. **Entry Addition**: Users are added to cache with automatic LRU eviction when full
3. **Entry Retrieval**: Cache hits and misses are tracked with statistics
4. **Eviction**: LRU policy removes least recently used entries when capacity is reached
5. **Expiration**: Entries older than maxAge are automatically removed

**Unit Tests**:

```java
public class CacheTest {
    public static void main(String[] args) {
        System.out.println("=== Running Cache Tests ===\n");

        testBasicOperations();
        testLRUEviction();
        testExpiration();
        testStatistics();

        System.out.println("\n=== All Tests Passed ===");
    }

    private static void testBasicOperations() {
        System.out.println("Test 1: Basic Operations");
        LRUCache<String, String> cache = new LRUCache<>(10, 60000,
            new ReferenceEquality<>(), new FieldEquality<>());

        cache.put("key1", "value1");
        assert "value1".equals(cache.get("key1")) : "Should retrieve value1";
        assert cache.size() == 1 : "Cache size should be 1";

        cache.remove("key1");
        assert cache.get("key1") == null : "Should return null after removal";

        System.out.println("  PASS: Basic operations test passed\n");
    }

    private static void testLRUEviction() {
        System.out.println("Test 2: LRU Eviction");
        LRUCache<String, String> cache = new LRUCache<>(3, 60000,
            new ReferenceEquality<>(), new FieldEquality<>());

        cache.put("k1", "v1");
        cache.put("k2", "v2");
        cache.put("k3", "v3");
        cache.get("k1"); // Access k1 to make it recently used
        cache.put("k4", "v4"); // Should evict k2 (least recently used)

        assert cache.get("k1") != null : "k1 should still be in cache";
        assert cache.get("k2") == null : "k2 should be evicted";
        assert cache.get("k4") != null : "k4 should be in cache";

        System.out.println("  PASS: LRU eviction test passed\n");
    }

    private static void testExpiration() {
        System.out.println("Test 3: Entry Expiration");
        LRUCache<String, String> cache = new LRUCache<>(10, 1000, // 1 second
            new ReferenceEquality<>(), new FieldEquality<>());

        cache.put("key", "value");
        assert "value".equals(cache.get("key")) : "Should retrieve before expiration";

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assert cache.get("key") == null : "Should return null after expiration";

        System.out.println("  PASS: Expiration test passed\n");
    }

    private static void testStatistics() {
        System.out.println("Test 4: Statistics Tracking");
        LRUCache<String, String> cache = new LRUCache<>(10, 60000,
            new ReferenceEquality<>(), new FieldEquality<>());

        cache.put("key", "value");
        cache.get("key"); // Hit
        cache.get("missing"); // Miss

        CacheStatistics stats = cache.getStats();
        assert stats.getHitRate() == 0.5 : "Hit rate should be 50%";

        System.out.println("  PASS: Statistics tracking test passed\n");
    }
}
```

**Complexity**:

- **Time Complexity**: O(1) for get and put operations (LinkedHashMap with access ordering)
- **Space Complexity**: O(n) where n is the number of entries in the cache

**Best Practices**:

- Override equals() and hashCode() consistently
- Use Objects.hash() for hashCode() generation
- Consider using immutable objects as cache keys
- Implement proper eviction policies for memory management
- Monitor cache statistics for performance tuning

## Exercises

### Easy

1. **Book Class**: Create a Book class with proper equals(), hashCode(), and toString() implementations. Test it with a HashSet.

2. **Point Class**: Implement a Point class with x and y coordinates. Override equals() to consider points equal if they have the same coordinates.

3. **Color Class**: Create a Color class with red, green, blue components. Implement equals() and hashCode() for use in HashMap.

### Medium

1. **Immutable Student**: Create an immutable Student class that properly implements equals(), hashCode(), and clone().

2. **Custom List**: Implement a custom ArrayList that properly handles equals() comparisons with other List implementations.

3. **Cache System**: Build a simple cache that uses equals() and hashCode() for efficient key-based storage.

### Hard

1. **Deep Clone Framework**: Create a framework that automatically generates deep clone methods for complex object graphs.

2. **Custom Equality**: Implement a flexible equality system that supports different comparison strategies for the same object type.

3. **Object Pool**: Design an object pool that uses equals() and hashCode() for efficient object reuse and lifecycle management.

## Interview Questions

### Easy

1. **What is the Object class in Java?**
   The Object class is the root of the Java class hierarchy. Every class in Java directly or indirectly extends Object. It provides fundamental methods like equals(), hashCode(), toString(), and clone().

2. **Why should you override equals() and hashCode() together?**
   Because hash-based collections use hashCode() to find buckets and equals() to determine equality. If you override equals() without hashCode(), objects that are equal might have different hash codes, causing them to be placed in different buckets in HashMap or HashSet.

3. **What is the difference between == and equals()?**
   == compares reference equality (whether two variables point to the same object), while equals() compares logical equality (whether two objects are considered equal based on their content).

### Medium

1. **When should you use getClass() vs instanceof in equals()?**
   Use instanceof when you want to allow subclasses to be equal to parent classes (Liskov Substitution Principle). Use getClass() when you want strict type matching and don't consider objects of different classes equal even if they have the same fields.

2. **What are the problems with the finalize() method?**
   finalize() is unpredictable (GC timing is not guaranteed), can cause resurrection of objects, has performance overhead, and can lead to resource leaks if exceptions occur. It's deprecated in Java 9+ in favor of try-with-resources and Cleaner API.

3. **How does the default toString() implementation work?**
   The default toString() in Object returns the class name followed by the hash code in hexadecimal (e.g., "com.example.MyClass@1a2b3c"). You should override it to provide meaningful information about the object's state.

### Hard

1. **Explain the contract between equals() and hashCode().**
   The contract requires: 1) Reflexivity: x.equals(x) must be true. 2) Symmetry: x.equals(y) must equal y.equals(x). 3) Transitivity: if x.equals(y) and y.equals(z), then x.equals(z). 4) Consistency: multiple calls return the same result. 5) Non-nullity: x.equals(null) must be false. For hashCode(): if x.equals(y), then x.hashCode() must equal y.hashCode(). If x.hashCode() == y.hashCode(), x.equals(y) is not required to be true.

2. **Design a system for custom equality strategies.**
   Create an EqualityStrategy interface with equals() and hashCode() methods. Implement different strategies for different use cases (reference equality, field equality, content equality). Use these strategies in collections and caches to support flexible equality semantics without changing the object's own equals() implementation.

## Common Pitfalls

### 1. Overriding equals() Without hashCode()

**Wrong**:
```java
class Person {
    String name;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person other = (Person) obj;
        return name != null ? name.equals(other.name) : other.name == null;
    }
    // Missing hashCode() override!
}

// This will fail in HashSet
HashSet<Person> set = new HashSet<>();
set.add(new Person("Alice"));
set.add(new Person("Alice"));
System.out.println(set.size()); // Prints 2 instead of expected 1
```

**Right**:
```java
class Person {
    String name;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person other = (Person) obj;
        return name != null ? name.equals(other.name) : other.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

// Now works correctly in HashSet
HashSet<Person> set = new HashSet<>();
set.add(new Person("Alice"));
set.add(new Person("Alice"));
System.out.println(set.size()); // Prints 1
```

### 2. Modifying Fields Used in hashCode()

**Wrong**:
```java
class Key {
    int value;

    public Key(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Key other = (Key) obj;
        return value == other.value;
    }
}

// Problem: changing value after putting in HashMap
HashMap<Key, String> map = new HashMap<>();
Key key = new Key(1);
map.put(key, "value1");
key.value = 2; // Modifies hashCode!
System.out.println(map.get(key)); // Returns null! Can't find the key
```

**Right**:
```java
// Option 1: Make the field final
class Key {
    final int value;

    public Key(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Key other = (Key) obj;
        return value == other.value;
    }
}

// Option 2: Use immutable objects as keys
final class ImmutableKey {
    private final int value;

    public ImmutableKey(int value) {
        this.value = value;
    }

    public int getValue() { return value; }

    @Override
    public int hashCode() { return value; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ImmutableKey other = (ImmutableKey) obj;
        return value == other.value;
    }
}
```

### 3. Using finalize() for Resource Cleanup

**Wrong**:
```java
class DatabaseConnection {
    private Connection connection;

    public DatabaseConnection() {
        connection = DriverManager.getConnection("jdbc:mysql://localhost/db");
    }

    public void executeQuery(String query) {
        // Execute query
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (connection != null) {
                connection.close(); // Unreliable! May never be called
            }
        } finally {
            super.finalize();
        }
    }
}

// Problem: finalize() is not guaranteed to run promptly or at all
DatabaseConnection conn = new DatabaseConnection();
conn.executeQuery("SELECT * FROM users");
conn = null; // May not close connection for a long time!
```

**Right**:
```java
class DatabaseConnection implements AutoCloseable {
    private Connection connection;

    public DatabaseConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost/db");
    }

    public void executeQuery(String query) throws SQLException {
        // Execute query
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}

// Use try-with-resources for guaranteed cleanup
try (DatabaseConnection conn = new DatabaseConnection()) {
    conn.executeQuery("SELECT * FROM users");
} // Automatically closed here, even if exception occurs
```

## Best Practices

1. **Always override equals() and hashCode() together**: If you override one, override both. Use Objects.hash() for hashCode() and check all relevant fields in equals().

2. **Use immutable objects as keys**: Immutable objects cannot change their hashCode() after being inserted into collections, preventing hash table corruption.

3. **Override toString() for debugging**: Provide a meaningful toString() implementation that includes relevant field values for easier debugging and logging.

4. **Avoid using finalize()**: Use try-with-resources with AutoCloseable or the Cleaner API for deterministic resource cleanup.

5. **Consider using IDE-generated equals() and hashCode()**: Modern IDEs can generate correct implementations that follow the contract and handle edge cases.

## Real World Usage

### How Spring Uses This

Spring Framework uses Object class methods extensively:

- **Bean Identity**: Spring uses equals() and hashCode() for bean identity in ApplicationContext
- **Cache Keys**: Spring Cache uses objects as keys, requiring proper hashCode() implementations
- **Bean Definitions**: DefaultListableBeanFactory compares bean definitions using equals()

### How Hibernate Uses This

Hibernate ORM relies on Object methods for entity management:

- **Entity Identity**: Hibernate uses equals() and hashCode() for entity identity in the persistence context
- **Cache Integration**: Second-level cache uses equals() and hashCode() for cache key generation
- **Collection Management**: Hibernate collections use equals() for element comparison

### How JDK Uses This

The Java Development Kit implements Object methods in core classes:

- **String**: Overrides equals() for content comparison and hashCode() using polynomial hash
- **Integer, Long, etc**: Override equals() for value comparison
- **Collection Classes**: ArrayList, HashMap, etc. use equals() for element comparison

### Enterprise Usage

In enterprise applications, Object class methods are critical for:

- **Entity Management**: JPA entities must properly implement equals() and hashCode()
- **Session Management**: HTTP session objects use equals() for comparison
- **Cache Keys**: Distributed caches require consistent equals() and hashCode()
- **Data Transfer Objects**: DTOs used in APIs must have proper equality semantics

## References

1. **Effective Java** by Joshua Bloch - Item 10: Observe the general contract when overriding equals()
2. **Effective Java** by Joshua Bloch - Item 11: Always override hashCode() when you override equals()
3. **Java SE Documentation** - Object Class
4. **Clean Code** by Robert C. Martin - Chapter on equality
5. **Java Concurrency in Practice** - Discussion of equals() and hashCode() in concurrent contexts

## Summary

- The Object class is the root of the Java class hierarchy, inherited by all classes
- equals() determines logical equality; override it to compare object state
- hashCode() must be consistent with equals() for correct behavior in collections
- toString() should provide meaningful string representation for debugging
- instanceof checks type compatibility; getClass() checks exact type
- clone() creates copies; prefer copy constructors or static factory methods
- finalize() is deprecated; use try-with-resources or Cleaner API instead

**Next Steps**: [15-method-overloading](../15-method-overloading/README.md)
