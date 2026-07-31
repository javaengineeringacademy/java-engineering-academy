# Inheritance

## Introduction

Inheritance is a fundamental OOP mechanism where a new class (subclass) derives from an existing class (superclass), inheriting its fields and methods. It establishes an IS-A relationship and enables code reuse.

## Learning Objectives

By the end of this topic, you will be able to:

- Understand the IS-A relationship and when to use inheritance
- Implement single and multilevel inheritance in Java
- Use the `super` keyword to access parent members
- Apply method overriding correctly
- Understand the `final` keyword for restriction
- Follow inheritance best practices

## Prerequisites

- Classes and Objects
- Encapsulation
- Methods
- Access Modifiers

## Why This Concept Exists

### The Problem

Without inheritance:

- Code duplication across similar classes
- No natural way to model hierarchies
- Changes require updating multiple classes
- No polymorphic behavior

### The Solution

Inheritance provides:

- **Code reuse**: Subclass inherits superclass behavior
- **Hierarchy**: Natural modeling of IS-A relationships
- **Polymorphism**: Treat different types uniformly
- **Maintainability**: Changes in superclass propagate to subclasses

### Real-World Analogy

Think of inheritance as a **family tree**:

- Children inherit traits from parents (genes = fields, behaviors = methods)
- Children can have unique traits (subclass-specific members)
- Children can modify inherited traits (method overriding)
- Each generation builds on the previous (multilevel inheritance)

## Internal Working

### How Inheritance Works

1. **Compilation**: Subclass bytecode includes superclass
2. **Memory**: Subclass object contains all superclass fields
3. **Method Resolution**: JVM looks for method in subclass first, then superclass
4. **Constructor Chain**: Subclass constructor calls superclass constructor

### Memory Layout

```
Object (superclass)
├── hashCode
├── class
└── ...

Animal (extends Object)
├── name
├── energy
└── Object fields

Dog (extends Animal)
├── breed
├── Animal fields
└── Object fields
```

## Syntax

### Basic Inheritance

```java
public class Animal {
    protected String name;
    
    public void eat() {
        System.out.println(name + " is eating");
    }
}

public class Dog extends Animal {
    private String breed;
    
    public void bark() {
        System.out.println(name + " says Woof!");
    }
}

// Usage
Dog dog = new Dog();
dog.name = "Rex";      // Inherited field
dog.eat();             // Inherited method
dog.bark();            // Subclass method
dog.breed = "Labrador"; // Subclass field
```

### Constructor Chaining

```java
public class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
        System.out.println("Animal constructor");
    }
}

public class Dog extends Animal {
    private String breed;
    
    public Dog(String name, String breed) {
        super(name); // Must be first statement
        this.breed = breed;
        System.out.println("Dog constructor");
    }
}

// Output when creating Dog:
// Animal constructor
// Dog constructor
```

## Easy Examples

### Example 1: Basic Animal Hierarchy

**Problem Statement**: Create an animal hierarchy with inheritance.

**Implementation**:

```java
// Base class
public class Animal {
    protected String name;
    protected int energy;
    
    public Animal(String name) {
        this.name = name;
        this.energy = 100;
    }
    
    public void eat() {
        energy += 10;
        System.out.println(name + " is eating. Energy: " + energy);
    }
    
    public void sleep() {
        energy += 20;
        System.out.println(name + " is sleeping. Energy: " + energy);
    }
    
    public String getName() {
        return name;
    }
    
    public int getEnergy() {
        return energy;
    }
}

// Subclass
public class Dog extends Animal {
    private String breed;
    
    public Dog(String name, String breed) {
        super(name); // Call parent constructor
        this.breed = breed;
    }
    
    public void bark() {
        System.out.println(name + " says: Woof! Woof!");
    }
    
    public void fetch(String item) {
        System.out.println(name + " fetches the " + item);
        energy -= 5;
    }
    
    public String getBreed() {
        return breed;
    }
}

// Another subclass
public class Cat extends Animal {
    private boolean isIndoor;
    
    public Cat(String name, boolean isIndoor) {
        super(name);
        this.isIndoor = isIndoor;
    }
    
    public void purr() {
        System.out.println(name + " purrs: Purr purr...");
    }
    
    public void meow() {
        System.out.println(name + " says: Meow!");
    }
}
```

**Output**:
```java
Dog dog = new Dog("Rex", "Labrador");
dog.eat();        // Rex is eating. Energy: 110
dog.bark();       // Rex says: Woof! Woof!
dog.fetch("ball"); // Rex fetches the ball

Cat cat = new Cat("Whiskers", true);
cat.eat();        // Whiskers is eating. Energy: 110
cat.purr();       // Whiskers purrs: Purr purr...
```

**Complexity**: O(1) for all methods

**Best Practices**:
- Use `protected` for fields subclasses need access to
- Call `super()` as the first statement in constructors
- Prefer composition when IS-A relationship doesn't exist

## Medium Examples

### Example 2: Multilevel Inheritance

**Problem Statement**: Create a multilevel inheritance hierarchy.

**Implementation**:

```java
// Level 1: Base class
public class Person {
    protected String name;
    protected int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public void introduce() {
        System.out.println("Hi, I'm " + name + ", age " + age);
    }
    
    public String getName() {
        return name;
    }
}

// Level 2: Extends Person
public class Employee extends Person {
    protected int employeeId;
    protected String department;
    
    public Employee(String name, int age, int employeeId, String department) {
        super(name, age);
        this.employeeId = employeeId;
        this.department = department;
    }
    
    public void work() {
        System.out.println(name + " is working in " + department);
    }
    
    @Override
    public void introduce() {
        super.introduce();
        System.out.println("I work in " + department + " (ID: " + employeeId + ")");
    }
}

// Level 3: Extends Employee
public class Manager extends Employee {
    private int teamSize;
    
    public Manager(String name, int age, int employeeId, String department, int teamSize) {
        super(name, age, employeeId, department);
        this.teamSize = teamSize;
    }
    
    public void manage() {
        System.out.println(name + " manages a team of " + teamSize);
    }
    
    @Override
    public void work() {
        super.work();
        System.out.println(name + " also manages the team");
    }
    
    @Override
    public void introduce() {
        super.introduce();
        System.out.println("I'm a manager with " + teamSize + " team members");
    }
}
```

**Output**:
```java
Person person = new Person("Alice", 30);
person.introduce(); // Hi, I'm Alice, age 30

Employee emp = new Employee("Bob", 35, 1001, "Engineering");
emp.introduce();    // Hi, I'm Bob, age 35
                    // I work in Engineering (ID: 1001)

Manager mgr = new Manager("Charlie", 40, 1002, "Engineering", 5);
mgr.introduce();    // Hi, I'm Charlie, age 40
                    // I work in Engineering (ID: 1002)
                    // I'm a manager with 5 team members
mgr.manage();       // Charlie manages a team of 5
```

**Complexity**: O(1) for all methods

## Hard Examples

### Example 3: Template Method Pattern with Inheritance

**Problem Statement**: Implement the Template Method pattern using inheritance.

**Implementation**:

```java
// Abstract base class with template method
public abstract class DataProcessor {
    
    // Template method - final to prevent overriding
    public final void process() {
        System.out.println("Starting processing...");
        readData();
        processData();
        writeData();
        System.out.println("Processing complete!");
    }
    
    // Abstract methods - subclasses must implement
    protected abstract void readData();
    protected abstract void processData();
    protected abstract void writeData();
    
    // Hook methods - subclasses can override
    protected void beforeProcess() {
        System.out.println("Before processing...");
    }
    
    protected void afterProcess() {
        System.out.println("After processing...");
    }
}

// Concrete implementation 1
public class CsvProcessor extends DataProcessor {
    
    @Override
    protected void readData() {
        System.out.println("Reading CSV data...");
    }
    
    @Override
    protected void processData() {
        System.out.println("Processing CSV records...");
    }
    
    @Override
    protected void writeData() {
        System.out.println("Writing to CSV file...");
    }
}

// Concrete implementation 2
public class DatabaseProcessor extends DataProcessor {
    
    @Override
    protected void readData() {
        System.out.println("Reading from database...");
    }
    
    @Override
    protected void processData() {
        System.out.println("Processing SQL results...");
    }
    
    @Override
    protected void writeData() {
        System.out.println("Writing to database...");
    }
    
    @Override
    protected void beforeProcess() {
        super.beforeProcess();
        System.out.println("Establishing database connection...");
    }
    
    @Override
    protected void afterProcess() {
        super.afterProcess();
        System.out.println("Closing database connection...");
    }
}
```

**Output**:
```java
DataProcessor csv = new CsvProcessor();
csv.process();
// Starting processing...
// Reading CSV data...
// Processing CSV records...
// Writing to CSV file...
// Processing complete!

DataProcessor db = new DatabaseProcessor();
db.process();
// Starting processing...
// Before processing...
// Establishing database connection...
// Reading from database...
// Processing SQL results...
// Writing to database...
// After processing...
// Closing database connection...
// Processing complete!
```

**Complexity**: O(1) for template method execution

**Best Practices**:
- Make template methods `final` to prevent breaking the algorithm
- Use abstract methods for required steps
- Use hook methods for optional steps with defaults
- Document which methods are for extension

## Exercises

### Easy

1. **Vehicle Hierarchy**: Create a Vehicle class with Car and Motorcycle subclasses.

2. **Shape Hierarchy**: Create a Shape class with Circle and Rectangle subclasses that calculate area.

### Medium

3. **Employee System**: Extend the Person/Employee example to include Contractor and Manager.

4. **Library System**: Create Book hierarchy with EBook and PhysicalBook subclasses.

### Hard

5. **Game Characters**: Create a game character hierarchy with different abilities and inheritance chains.

6. **Payment System**: Implement a payment processing system using Template Method pattern.

## Interview Questions

### Beginner

1. **What is inheritance in Java?**
   Inheritance is a mechanism where a new class (subclass) derives from an existing class (superclass), inheriting its fields and methods. It establishes an IS-A relationship.

2. **What does the `extends` keyword do?**
   It establishes inheritance between two classes. The subclass extends the superclass.

3. **Can a class extend multiple classes?**
   No, Java supports single inheritance only. A class can extend one class but implement multiple interfaces.

### Intermediate

4. **What is method overriding?**
   Method overriding occurs when a subclass provides a specific implementation of a method that is already defined in its superclass. The method must have the same name, parameters, and return type.

5. **What is the `super` keyword used for?**
   `super` refers to the superclass. It's used to:
   - Call superclass constructors: `super(args)`
   - Access superclass methods: `super.method()`
   - Access superclass fields: `super.field`

6. **What is the difference between `==` and `.equals()`?**
   `==` compares references (same object in memory), `.equals()` compares content (logical equality).

### Senior

7. **How does Java resolve method calls at runtime?**
   Java uses dynamic method dispatch. The JVM looks for the method in the actual object's class first, then walks up the inheritance chain until found.

8. **What is the Liskov Substitution Principle?**
   Subclasses should be substitutable for their superclasses without breaking the program. If S is a subtype of T, then objects of type T can be replaced with objects of type S.

9. **What are the problems with inheritance?**
   - Tight coupling between parent and child
   - Fragile base class problem
   - Deep hierarchies can be complex
   - Cannot change parent behavior at runtime

### Architecture

10. **When should you use inheritance vs composition?**
    Use inheritance when there's a clear IS-A relationship and you want to reuse code. Use composition when you want to delegate behavior and have more flexibility.

11. **How does inheritance affect testing?**
    Inheritance can make testing harder because subclasses depend on parent implementation. Mocking becomes more complex. Consider composition for better testability.

12. **Can you change the superclass at runtime?**
    No, inheritance is a compile-time relationship. You cannot change what a class extends at runtime. This is why composition is often preferred for flexibility.

### Scenario

13. **You have a deep inheritance hierarchy (5+ levels). How would you refactor it?**

14. **You need to add functionality to all classes in a hierarchy. How would you do it?**

15. **You're designing a plugin system. How would you use inheritance?

### Coding

16. **Implement a binary tree with Node class using inheritance for different node types.**

17. **Create a undo/redo system using inheritance for different command types.**

18. **Design a notification system with different notification types using inheritance.

### Production

19. **How would you handle inheritance in a multi-threaded environment?**

20. **How would you version a class hierarchy in a library?**

### Debugging

21. **Why am I getting "Infinite recursion" when calling a method?**

22. **How do I debug method resolution in a deep hierarchy?**

## Common Pitfalls

### 1. Deep Inheritance Hierarchies

**Problem**:
```java
class A { }
class B extends A { }
class C extends B { }
class D extends C { }
class E extends D { } // Too deep!
```

**Solution**:
```java
// Use composition instead
class Service {
    private final Logger logger;
    private final Validator validator;
    // Delegate to composed objects
}
```

### 2. Fragile Base Class

**Problem**:
```java
public class ArrayList<E> {
    public void add(E element) {
        // Implementation changes
    }
}

public class MyList<E> extends ArrayList<E> {
    private int addCount = 0;
    
    @Override
    public void add(E element) {
        addCount++;
        super.add(element);
    }
    
    public int getAddCount() {
        return addCount;
    }
}
// If ArrayList changes add() behavior, MyList breaks!
```

**Solution**:
```java
// Use composition instead of inheritance
public class MyList<E> {
    private final List<E> list = new ArrayList<>();
    private int addCount = 0;
    
    public void add(E element) {
        addCount++;
        list.add(element);
    }
}
```

### 3. Calling Super Constructor Last

**Wrong**:
```java
public class Child extends Parent {
    public Child() {
        // Some initialization
        super(); // ERROR: Must be first statement
    }
}
```

**Right**:
```java
public class Child extends Parent {
    public Child() {
        super(); // Must be first
        // Then child initialization
    }
}
```

## Best Practices

### 1. Prefer Composition Over Inheritance

Use inheritance only when there's a clear IS-A relationship. Otherwise, use composition.

### 2. Keep Hierarchies Shallow

Aim for 2-3 levels maximum. Deeper hierarchies are harder to understand and maintain.

### 3. Use `final` to Prevent Extension

If a class wasn't designed for inheritance, mark it `final`.

### 4. Document Inheritance Contracts

Document which methods are safe to override and what the contract is.

### 5. Test Subclasses Thoroughly

Test that subclasses work correctly with the parent implementation.

## Real World Usage

### JDK Examples

```java
// Java Collections Framework
public class ArrayList<E> extends AbstractList<E> implements List<E> {
    // Inherits from AbstractList, implements List interface
}

// Java I/O
public class BufferedReader extends Reader {
    // Inherits from Reader, adds buffering
}
```

### Spring Framework

```java
// Spring uses inheritance for template classes
public class JdbcTemplate extends JdbcAccessor implements JdbcOperations {
    // Inherits connection management, adds operations
}
```

### Design Patterns

```java
// Strategy Pattern often uses inheritance
public abstract class SortStrategy {
    public abstract void sort(int[] array);
}

public class BubbleSort extends SortStrategy {
    @Override
    public void sort(int[] array) {
        // Bubble sort implementation
    }
}
```

## Summary

Inheritance is a powerful mechanism for code reuse and hierarchy modeling. Key takeaways:

- **Purpose**: IS-A relationship, code reuse, polymorphism
- **Single inheritance**: Java supports only single class inheritance
- **super keyword**: Access parent members and constructors
- **Method overriding**: Provide specific implementations in subclasses
- **final keyword**: Restrict inheritance, overriding, and modification
- **Best practices**: Prefer composition, keep hierarchies shallow, document contracts

**Next Steps**: Learn about polymorphism for dynamic behavior, or encapsulation for data protection.
