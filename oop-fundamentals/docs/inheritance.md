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

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    Inheritance Architecture             │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────┐       ┌──────────────┐               │
│  │  Superclass   │       │  Interface    │               │
│  │  (Base Class) │       │  (Contract)   │               │
│  └──────┬───────┘       └──────┬───────┘               │
│         │ extends              │ implements             │
│         ▼                      ▼                        │
│  ┌──────────────┐       ┌──────────────┐               │
│  │  Subclass A   │       │  Subclass B   │               │
│  │              │       │              │               │
│  └──────┬───────┘       └──────┬───────┘               │
│         │ extends              │ extends                │
│         ▼                      ▼                        │
│  ┌──────────────┐       ┌──────────────┐               │
│  │  SubSubClass  │       │  SubSubClass  │               │
│  │  (Level 3)    │       │  (Level 3)    │               │
│  └──────────────┘       └──────────────┘               │
│                                                         │
│  Memory: Subclass = Superclass fields + Own fields     │
└─────────────────────────────────────────────────────────┘
```

```java
// Architecture in code
public abstract class Shape {
    protected String color;
    protected double opacity;
    
    public abstract double area();
    public abstract double perimeter();
}

public abstract class Polygon extends Shape {
    protected int sides;
    
    public abstract double sideLength();
}

public class RegularPolygon extends Polygon {
    private final double length;
    
    public RegularPolygon(String color, double opacity, int sides, double length) {
        this.color = color;
        this.opacity = opacity;
        this.sides = sides;
        this.length = length;
    }
    
    @Override
    public double area() {
        return (sides * length * length) / (4 * Math.tan(Math.PI / sides));
    }
    
    @Override
    public double perimeter() {
        return sides * length;
    }
    
    @Override
    public double sideLength() {
        return length;
    }
}
```

## Flow Diagram

```
┌─────────────────────────────────────────────────────────┐
│              Method Resolution Flow                     │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  Method Call on Subclass Object                         │
│         │                                              │
│         ▼                                              │
│  ┌──────────────┐     Found                             │
│  │ Subclass has  │──────────────► Execute               │
│  │ the method?   │                                      │
│  └──────┬───────┘                                      │
│         │ No                                            │
│         ▼                                              │
│  ┌──────────────┐     Found                             │
│  │ Parent has   │──────────────► Execute               │
│  │ the method?  │                                      │
│  └──────┬───────┘                                      │
│         │ No                                            │
│         ▼                                              │
│  ┌──────────────┐     Found                             │
│  │ Grandparent? │──────────────► Execute               │
│  └──────┬───────┘                                      │
│         │ No                                            │
│         ▼                                              │
│  ┌──────────────┐                                       │
│  │ Object class │                                       │
│  └──────────────┘                                       │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│              Constructor Chain Flow                     │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  new SubClass(args)                                     │
│         │                                              │
│         ▼                                              │
│  ┌──────────────┐                                       │
│  │ SubClass()   │                                       │
│  │ calls super()│                                       │
│  └──────┬───────┘                                      │
│         │                                              │
│         ▼                                              │
│  ┌──────────────┐                                       │
│  │ Parent()     │                                       │
│  │ calls super()│                                       │
│  └──────┬───────┘                                      │
│         │                                              │
│         ▼                                              │
│  ┌──────────────┐                                       │
│  │ Object()     │                                       │
│  │ initializes  │                                       │
│  └──────┬───────┘                                      │
│         │                                              │
│         ▼                                              │
│  Parent initialization complete                         │
│         │                                              │
│         ▼                                              │
│  SubClass initialization complete                       │
└─────────────────────────────────────────────────────────┘
```

```java
public class FlowDemo {
    static {
        System.out.println("1. Static block of main class");
    }
    
    public static void main(String[] args) {
        System.out.println("2. Creating Child object:");
        new Child();
    }
}

class Parent {
    protected String name;
    
    {
        System.out.println("3. Instance block of Parent");
    }
    
    static {
        System.out.println("4. Static block of Parent");
    }
    
    public Parent() {
        System.out.println("5. Parent constructor");
        this.name = "Parent";
    }
}

class Child extends Parent {
    private int age;
    
    {
        System.out.println("6. Instance block of Child");
    }
    
    static {
        System.out.println("7. Static block of Child");
    }
    
    public Child() {
        super();
        System.out.println("8. Child constructor");
        this.age = 10;
    }
}

// Output:
// 1. Static block of main class
// 4. Static block of Parent
// 7. Static block of Child
// 2. Creating Child object:
// 3. Instance block of Parent
// 5. Parent constructor
// 6. Instance block of Child
// 8. Child constructor
```

## Time Complexity

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| Field access (inherited) | O(1) | Direct field access |
| Method call (inherited) | O(1) | Virtual method dispatch |
| Method call (overridden) | O(1) | Same as non-inherited |
| Constructor chaining | O(n) | n = depth of hierarchy |
| `instanceof` check | O(d) | d = depth of hierarchy |
| `super.method()` call | O(1) | Direct parent lookup |
| Reflection on hierarchy | O(n) | n = number of classes |

```java
public class ComplexityDemo {
    
    // O(1) - Direct inherited field access
    static class Base {
        protected int value = 42;
    }
    
    static class Derived extends Base {
        public int getValue() {
            return value; // O(1) - no traversal needed
        }
    }
    
    // O(d) - instanceof walks hierarchy
    static class Level1 extends Base {}
    static class Level2 extends Level1 {}
    static class Level3 extends Level2 {}
    static class Level4 extends Level3 {}
    static class Level5 extends Level4 {}
    
    // O(1) - Method resolution cached after first call
    static class Animal {
        public void speak() { System.out.println("..."); }
    }
    
    static class Dog extends Animal {
        @Override
        public void speak() { System.out.println("Woof!"); }
    }
    
    public static void main(String[] args) {
        // Field access: O(1)
        Derived d = new Derived();
        int v = d.getValue(); // Single field lookup
        
        // instanceof: O(depth)
        Level5 l5 = new Level5();
        if (l5 instanceof Level1) { // JVM walks up chain
            System.out.println("Is a Level1");
        }
        
        // Method call: O(1) after JIT optimization
        Dog dog = new Dog();
        dog.speak(); // JVM caches vtable slot
    }
}
```

## Space Complexity

| Component | Space | Description |
|-----------|-------|-------------|
| Per-class overhead | ~16 bytes | Class metadata, vtable pointer |
| Per-instance fields | Sum of all fields | From class + all ancestors |
| Method metadata | Shared per class | Not duplicated per instance |
| Vtable entry | ~8 bytes per method | Virtual dispatch table |

```java
public class SpaceAnalysis {
    
    // Minimal class: ~16 bytes overhead (object header)
    static class Empty {}
    
    // Additional field overhead
    static class WithFields {
        int x;          // +4 bytes
        long y;         // +8 bytes
        Object ref;     // +4 bytes (reference)
        // Total: 16 + 16 = 32 bytes minimum
    }
    
    // Inheritance: parent fields included
    static class Parent {
        int a;          // 4 bytes
        long b;         // 8 bytes
        String c;       // 4 bytes (reference)
    }
    
    static class Child extends Parent {
        int d;          // 4 bytes
        double e;       // 8 bytes
        // Total: 16 (header) + 4 + 8 + 4 + 4 + 8 = 44 bytes
        // (padded to 48 for alignment)
    }
    
    // Demonstrating space is not duplicated for methods
    interface Swimmable {
        void swim();
    }
    
    interface Flyable {
        void fly();
    }
    
    // Methods are shared - only fields are per-instance
    static class Duck implements Swimmable, Flyable {
        // No extra space for implementing interfaces
        // Method code is shared in class metadata
    }
    
    public static void main(String[] args) {
        Empty e = new Empty();
        WithFields wf = new WithFields();
        Parent p = new Parent();
        Child c = new Child();
        
        // JVM estimates (actual values depend on JVM implementation):
        // Empty: 16 bytes
        // WithFields: 32 bytes
        // Parent: 40 bytes
        // Child: 48 bytes
        
        System.out.println("Empty: " + Runtime.getRuntime().totalMemory());
        
        // Force GC to get accurate memory
        System.gc();
        
        // Use JOL (Java Object Layout) for precise measurements:
        // -XX:+PrintFlagsFinal shows compressed object references
        // jol-core: ClassLayout.parseInstance(obj).toPrintable()
    }
}
```

## Thread Safety

| Scenario | Thread-Safe? | Solution |
|----------|-------------|----------|
| Inherited immutable fields | Yes | No synchronization needed |
| Mutable inherited fields | No | Use `synchronized` or `volatile` |
| Method overriding | Depends | Must document thread safety |
| Constructor chaining | No | Avoid in multi-threaded init |

```java
public class ThreadSafetyDemo {
    
    // Thread-safe: immutable inheritance
    static abstract class ImmutableBase {
        protected final String id;
        protected final long timestamp;
        
        protected ImmutableBase(String id) {
            this.id = id;
            this.timestamp = System.nanoTime();
        }
        
        public String getId() { return id; }
        public long getTimestamp() { return timestamp; }
    }
    
    static class ImmutableChild extends ImmutableBase {
        private final String label;
        
        public ImmutableChild(String id, String label) {
            super(id);
            this.label = label;
        }
        
        public String getLabel() { return label; }
    }
    
    // NOT thread-safe: mutable inherited state
    static class UnsafeBase {
        protected int counter = 0;
        
        public void increment() {
            counter++; // Race condition!
        }
    }
    
    static class UnsafeChild extends UnsafeBase {
        public void add(int amount) {
            counter += amount; // Race condition!
        }
    }
    
    // Thread-safe solution: synchronized
    static class SafeBase {
        protected int counter = 0;
        
        public synchronized void increment() {
            counter++;
        }
        
        public synchronized int getCounter() {
            return counter;
        }
    }
    
    static class SafeChild extends SafeBase {
        // Inherits synchronized methods - thread safe
        public synchronized void add(int amount) {
            for (int i = 0; i < amount; i++) {
                increment(); // Calls synchronized parent method
            }
        }
    }
    
    // ReadWriteLock for concurrent reads
    static class ConcurrentBase {
        protected final java.util.concurrent.locks.ReadWriteLock lock =
            new java.util.concurrent.locks.ReentrantReadWriteLock();
        protected volatile int value = 0;
        
        public int getValue() {
            lock.readLock().lock();
            try {
                return value;
            } finally {
                lock.readLock().unlock();
            }
        }
    }
    
    static class ConcurrentChild extends ConcurrentBase {
        public void setValue(int newValue) {
            lock.writeLock().lock();
            try {
                this.value = newValue;
            } finally {
                lock.writeLock().unlock();
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        SafeChild safe = new SafeChild();
        UnsafeChild unsafe = new UnsafeChild();
        
        // Test thread-safe version
        Thread[] safeThreads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            safeThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    safe.increment();
                }
            });
            safeThreads[i].start();
        }
        for (Thread t : safeThreads) t.join();
        System.out.println("Safe counter: " + safe.getCounter()); // Always 10000
        
        // Test thread-unsafe version
        Thread[] unsafeThreads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    unsafe.increment();
                }
            });
            unsafeThreads[i].start();
        }
        for (Thread t : unsafeThreads) t.join();
        System.out.println("Unsafe counter: " + unsafe.getCounter()); // Unpredictable
    }
}
```

## Comparison Table

| Feature | Inheritance | Composition | Interface | Mixin (Java 21) |
|---------|------------|-------------|-----------|-----------------|
| IS-A relationship | Yes | No | Yes (capability) | Partial |
| Code reuse | Yes | Yes | No | Partial |
| Runtime flexibility | No | Yes | Yes | Yes |
| Tight coupling | Yes | No | No | Moderate |
| Multiple types | No | Yes (many) | Yes (many) | Yes |
| State sharing | Yes | No | No | No |
| Constructor chaining | Yes | No | No | No |
| `super` access | Yes | No | No | No |
| `final` restriction | Yes | No | No | Limited |
| Testing ease | Harder | Easier | Easier | Moderate |

```java
// Inheritance approach
abstract class Vehicle {
    protected String make;
    protected int year;
    
    public abstract double calculateMpg();
    
    public String getInfo() {
        return year + " " + make;
    }
}

class Car extends Vehicle {
    private int doors;
    
    @Override
    public double calculateMpg() { return 30.0; }
}

// Composition approach
interface Drivable {
    double calculateMpg();
    String getInfo();
}

class Engine {
    public String describe() { return "Internal Combustion"; }
}

class ComposedCar implements Drivable {
    private final Engine engine;
    private final String make;
    private final int year;
    
    public ComposedCar(Engine engine, String make, int year) {
        this.engine = engine;
        this.make = make;
        this.year = year;
    }
    
    @Override
    public double calculateMpg() { return 30.0; }
    
    @Override
    public String getInfo() { return year + " " + make; }
}

// Interface approach
interface Printable {
    default String format() {
        return toString();
    }
}

interface Loggable {
    default String toLog() {
        return getClass().getSimpleName() + ": " + hashCode();
    }
}

class Document implements Printable, Loggable {
    private final String content;
    
    public Document(String content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return "Document{" + content + "}";
    }
}

// Java 21 Feature: Sealed interfaces for controlled hierarchy
sealed interface Shape permits Circle, Rectangle, Triangle {
    double area();
}

record Circle(double radius) implements Shape {
    @Override
    public double area() { return Math.PI * radius * radius; }
}

record Rectangle(double width, double height) implements Shape {
    @Override
    public double area() { return width * height; }
}

record Triangle(double base, double height) implements Shape {
    @Override
    public double area() { return 0.5 * base * height; }
}
```

## Decision Tree

```
┌─────────────────────────────────────────────────────────────┐
│              Should You Use Inheritance?                     │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Does a clear IS-A relationship exist?                      │
│  │                                                          │
│  ├── YES ──► Will the hierarchy change frequently?          │
│  │           │                                              │
│  │           ├── YES ──► Use COMPOSITION                     │
│  │           │                                              │
│  │           └── NO ───► Do you need polymorphism?          │
│  │                       │                                  │
│  │                       ├── YES ──► Use INHERITANCE        │
│  │                       │                                  │
│  │                       └── NO ───► Use COMPOSITION        │
│  │                                                          │
│  └── NO ───► Do you need to share state?                    │
│              │                                              │
│              ├── YES ──► Use COMPOSITION                    │
│              │                                              │
│              └── NO ───► Do you need multiple type          │
│                          relationships?                     │
│                          │                                  │
│                          ├── YES ──► Use INTERFACES          │
│                          │                                  │
│                          └── NO ───► Use COMPOSITION         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

```java
public class DecisionTreeDemo {
    
    // SCENARIO 1: IS-A exists, stable hierarchy → Inheritance
    // "Dog IS-A Animal" and animals don't change often
    static class Animal {
        protected String name;
        public void eat() { System.out.println(name + " eating"); }
    }
    
    static class Dog extends Animal {
        public void bark() { System.out.println("Woof!"); }
    }
    
    // SCENARIO 2: No clear IS-A, need flexibility → Composition
    // "Car has-an Engine" not "Car IS-A Engine"
    static class Engine {
        public void start() { System.out.println("Engine started"); }
    }
    
    static class Car {
        private final Engine engine = new Engine(); // Composition
        
        public void start() {
            engine.start(); // Delegate
        }
    }
    
    // SCENARIO 3: Need multiple types → Interfaces
    // Dog can be both Pet and ServiceAnimal
    interface Pet {
        void play();
    }
    
    interface ServiceAnimal {
        void assist();
    }
    
    static class GuideDog extends Animal implements Pet, ServiceAnimal {
        @Override
        public void play() { System.out.println("Dog playing"); }
        
        @Override
        public void assist() { System.out.println("Dog guiding"); }
    }
    
    // SCENARIO 4: Share state but want flexibility → Abstract class + Composition
    abstract class Logger {
        protected final String prefix;
        
        protected Logger(String prefix) {
            this.prefix = prefix;
        }
        
        public abstract void log(String message);
    }
    
    static class ConsoleLogger extends Logger {
        public ConsoleLogger() { super("[CONSOLE]"); }
        
        @Override
        public void log(String message) {
            System.out.println(prefix + " " + message);
        }
    }
    
    // DECISION HELPER
    enum Relationship { IS_A, HAS_A, CAN_DO }
    
    static String recommend(Relationship rel, boolean stable, boolean polymorphic) {
        return switch (rel) {
            case IS_A -> stable && polymorphic ? "INHERITANCE" : "COMPOSITION";
            case HAS_A -> "COMPOSITION";
            case CAN_DO -> "INTERFACE";
        };
    }
    
    public static void main(String[] args) {
        // Test decisions
        System.out.println(recommend(Relationship.IS_A, true, true));   // INHERITANCE
        System.out.println(recommend(Relationship.IS_A, false, true));  // COMPOSITION
        System.out.println(recommend(Relationship.HAS_A, true, false)); // COMPOSITION
        System.out.println(recommend(Relationship.CAN_DO, true, true)); // INTERFACE
        
        // Test implementations
        Dog dog = new Dog();
        dog.name = "Rex";
        dog.eat();
        dog.bark();
        
        Car car = new Car();
        car.start();
    }
}
```

## Assignments

### Assignment 1: Shape Hierarchy (Beginner)

**Objective**: Create a shape hierarchy with area calculation.

```java
// Task: Implement this hierarchy
abstract class Shape {
    protected String color;
    
    public Shape(String color) { this.color = color; }
    
    public abstract double area();
    public abstract double perimeter();
    
    @Override
    public String toString() {
        return color + " " + getClass().getSimpleName() + 
               " [area=" + String.format("%.2f", area()) + "]";
    }
}

class Circle extends Shape {
    private double radius;
    
    // TODO: Implement constructor, area(), perimeter()
}

class Rectangle extends Shape {
    private double width, height;
    
    // TODO: Implement constructor, area(), perimeter()
}

class Square extends Rectangle {
    // TODO: Implement using Rectangle's fields
}

// Test your implementation
class ShapeTest {
    public static void main(String[] args) {
        Shape circle = new Circle("Red", 5.0);
        Shape rect = new Rectangle("Blue", 4.0, 6.0);
        Shape square = new Square("Green", 3.0);
        
        System.out.println(circle);   // Red Circle [area=78.54]
        System.out.println(rect);     // Blue Rectangle [area=24.00]
        System.out.println(square);   // Green Square [area=9.00]
    }
}
```

### Assignment 2: Employee Management (Intermediate)

**Objective**: Build an employee hierarchy with salary calculations.

```java
// Task: Complete this hierarchy
abstract class Employee {
    protected String name;
    protected int id;
    
    public Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }
    
    public abstract double calculateSalary();
    
    public void displayInfo() {
        System.out.printf("%s (ID: %d) - Salary: $%.2f%n", 
                         name, id, calculateSalary());
    }
}

class FullTimeEmployee extends Employee {
    private double annualSalary;
    
    // TODO: Implement
}

class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private int hoursPerWeek;
    
    // TODO: Implement
}

class Contractor extends Employee {
    private double dailyRate;
    private int contractDays;
    
    // TODO: Implement
}

// Bonus: Add a Manager class that extends FullTimeEmployee
// and adds a team bonus calculation
```

### Assignment 3: Plugin System (Advanced)

**Objective**: Create a plugin system using inheritance and interfaces.

```java
// Task: Implement a plugin system
interface Plugin {
    String getName();
    String getVersion();
    void execute();
}

abstract class BasePlugin implements Plugin {
    protected boolean enabled = true;
    protected final java.util.Map<String, Object> config = new java.util.HashMap<>();
    
    // TODO: Implement common plugin behavior
}

class ValidationPlugin extends BasePlugin {
    // TODO: Implement validation logic
}

class LoggingPlugin extends BasePlugin {
    // TODO: Implement logging logic
}

class PluginManager {
    private final java.util.List<BasePlugin> plugins = new java.util.ArrayList<>();
    
    // TODO: Implement plugin registration and execution
}
```

## Mini Project

### Project: Library Management System

**Objective**: Build a complete library system demonstrating inheritance principles.

```java
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Base class for all library items
abstract class LibraryItem {
    protected final String id;
    protected final String title;
    protected boolean available;
    
    protected LibraryItem(String id, String title) {
        this.id = id;
        this.title = title;
        this.available = true;
    }
    
    public abstract int getLoanDays();
    public abstract double getLateFee(int daysLate);
    
    public boolean isAvailable() { return available; }
    public void checkout() { available = false; }
    public void returnItem() { available = true; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", id, title, 
                           available ? "Available" : "Checked Out");
    }
}

// Book hierarchy
class Book extends LibraryItem {
    private final String author;
    private final String isbn;
    
    public Book(String id, String title, String author, String isbn) {
        super(id, title);
        this.author = author;
        this.isbn = isbn;
    }
    
    @Override
    public int getLoanDays() { return 14; }
    
    @Override
    public double getLateFee(int daysLate) {
        return daysLate * 0.50;
    }
    
    public String getAuthor() { return author; }
}

class ReferenceBook extends Book {
    public ReferenceBook(String id, String title, String author, String isbn) {
        super(id, title, author, isbn);
    }
    
    @Override
    public int getLoanDays() { return 0; } // Cannot be checked out
    
    @Override
    public double getLateFee(int daysLate) {
        return daysLate * 2.00; // Higher penalty
    }
}

// Media hierarchy
abstract class Media extends LibraryItem {
    protected final String format;
    
    protected Media(String id, String title, String format) {
        super(id, title);
        this.format = format;
    }
}

class DVD extends Media {
    private final String director;
    
    public DVD(String id, String title, String format, String director) {
        super(id, title, format);
        this.director = director;
    }
    
    @Override
    public int getLoanDays() { return 7; }
    
    @Override
    public double getLateFee(int daysLate) {
        return daysLate * 1.00;
    }
}

class Audiobook extends Media {
    private final String narrator;
    
    public Audiobook(String id, String title, String format, String narrator) {
        super(id, title, format);
        this.narrator = narrator;
    }
    
    @Override
    public int getLoanDays() { return 21; }
    
    @Override
    public double getLateFee(int daysLate) {
        return daysLate * 0.75;
    }
}

// Member hierarchy
abstract class Member {
    protected final String memberId;
    protected final String name;
    protected final List<LibraryItem> borrowedItems = new ArrayList<>();
    protected final int maxItems;
    
    protected Member(String memberId, String name, int maxItems) {
        this.memberId = memberId;
        this.name = name;
        this.maxItems = maxItems;
    }
    
    public boolean canBorrow() {
        return borrowedItems.size() < maxItems;
    }
    
    public void borrowItem(LibraryItem item) {
        if (canBorrow() && item.isAvailable()) {
            borrowedItems.add(item);
            item.checkout();
        }
    }
    
    public abstract double calculateOverdueFees();
    
    @Override
    public String toString() {
        return String.format("%s (%s) - Items: %d/%d", 
                           name, memberId, borrowedItems.size(), maxItems);
    }
}

class RegularMember extends Member {
    public RegularMember(String memberId, String name) {
        super(memberId, name, 5);
    }
    
    @Override
    public double calculateOverdueFees() {
        return borrowedItems.stream()
            .mapToDouble(item -> item.getLateFee(0))
            .sum();
    }
}

class PremiumMember extends Member {
    public PremiumMember(String memberId, String name) {
        super(memberId, name, 15);
    }
    
    @Override
    public double calculateOverdueFees() {
        return borrowedItems.stream()
            .mapToDouble(item -> item.getLateFee(0) * 0.5) // 50% discount
            .sum();
    }
}

// Library class
class Library {
    private final List<LibraryItem> items = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    
    public void addItem(LibraryItem item) { items.add(item); }
    public void addMember(Member member) { members.add(member); }
    
    public LibraryItem findItem(String id) {
        return items.stream()
            .filter(item -> item.id.equals(id))
            .findFirst()
            .orElse(null);
    }
    
    public void checkoutItem(String memberId, String itemId) {
        Member member = members.stream()
            .filter(m -> m.memberId.equals(memberId))
            .findFirst()
            .orElse(null);
        LibraryItem item = findItem(itemId);
        
        if (member != null && item != null) {
            member.borrowItem(item);
        }
    }
    
    public void displayCatalog() {
        System.out.println("=== Library Catalog ===");
        items.forEach(System.out::println);
    }
}

// Main class
public class LibrarySystem {
    public static void main(String[] args) {
        Library library = new Library();
        
        // Add items
        library.addItem(new Book("B001", "Java Patterns", "John Doe", "978-0-123"));
        library.addItem(new ReferenceBook("R001", "Java Reference", "Jane Smith", "978-0-456"));
        library.addItem(new DVD("D001", "Java Tutorial", "DVD", "Bob Wilson"));
        
        // Add members
        Member alice = new RegularMember("M001", "Alice");
        Member bob = new PremiumMember("M002", "Bob");
        library.addMember(alice);
        library.addMember(bob);
        
        // Display catalog
        library.displayCatalog();
        
        // Checkout
        library.checkoutItem("M001", "B001");
        System.out.println("\nAfter checkout:");
        System.out.println(alice);
    }
}
```

## Use Cases

### 1. GUI Component Hierarchy

```java
// Real-world: Java Swing/AWT style hierarchy
abstract class Component {
    protected int x, y, width, height;
    protected boolean visible = true;
    
    public abstract void paint(Graphics g);
    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && 
               py >= y && py <= y + height;
    }
}

class Container extends Component {
    private final java.util.List<Component> children = new java.util.ArrayList<>();
    
    public void add(Component c) { children.add(c); }
    
    @Override
    public void paint(Graphics g) {
        children.stream()
            .filter(Component::isVisible)
            .forEach(c -> c.paint(g));
    }
}

class Button extends Component {
    private final String label;
    private Runnable onClick;
    
    public Button(String label) { this.label = label; }
    
    @Override
    public void paint(Graphics g) {
        System.out.println("Drawing button: " + label);
    }
    
    public void click() {
        if (onClick != null) onClick.run();
    }
}

class TextBox extends Component {
    private String text = "";
    
    @Override
    public void paint(Graphics g) {
        System.out.println("Drawing textbox: " + text);
    }
    
    public void type(char c) { text += c; }
}
```

### 2. Payment Processing

```java
abstract class PaymentProcessor {
    protected String merchantId;
    protected java.math.BigDecimal totalProcessed = java.math.BigDecimal.ZERO;
    
    public abstract boolean processPayment(java.math.BigDecimal amount);
    public abstract boolean refund(String transactionId, java.math.BigDecimal amount);
    
    public java.math.BigDecimal getTotalProcessed() { return totalProcessed; }
}

class CreditCardProcessor extends PaymentProcessor {
    @Override
    public boolean processPayment(java.math.BigDecimal amount) {
        System.out.println("Processing credit card: " + amount);
        totalProcessed = totalProcessed.add(amount);
        return true;
    }
    
    @Override
    public boolean refund(String transactionId, java.math.BigDecimal amount) {
        System.out.println("Refunding credit card: " + amount);
        return true;
    }
}

class PayPalProcessor extends PaymentProcessor {
    @Override
    public boolean processPayment(java.math.BigDecimal amount) {
        System.out.println("Processing PayPal: " + amount);
        totalProcessed = totalProcessed.add(amount);
        return true;
    }
    
    @Override
    public boolean refund(String transactionId, java.math.BigDecimal amount) {
        System.out.println("Refunding PayPal: " + amount);
        return true;
    }
}
```

### 3. Data Access Layer

```java
abstract class Repository<T> {
    protected final java.util.Map<String, T> storage = new java.util.HashMap<>();
    
    public void save(String id, T entity) {
        storage.put(id, entity);
        onAfterSave(entity);
    }
    
    public T findById(String id) {
        return storage.get(id);
    }
    
    public java.util.List<T> findAll() {
        return new java.util.ArrayList<>(storage.values());
    }
    
    protected abstract void onAfterSave(T entity);
}

class UserRepository extends Repository<User> {
    private final java.util.List<String> auditLog = new java.util.ArrayList<>();
    
    @Override
    protected void onAfterSave(User user) {
        auditLog.add("Saved user: " + user.name() + " at " + java.time.Instant.now());
    }
    
    public User findByEmail(String email) {
        return storage.values().stream()
            .filter(u -> u.email().equals(email))
            .findFirst()
            .orElse(null);
    }
}

record User(String id, String name, String email) {}
```

## Error Handling

```java
// Exception hierarchy using inheritance
class AppException extends Exception {
    protected final String errorCode;
    
    public AppException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() { return errorCode; }
}

class ValidationException extends AppException {
    private final String field;
    
    public ValidationException(String message, String field) {
        super(message, "VALIDATION_ERROR");
        this.field = field;
    }
    
    public String getField() { return field; }
}

class NotFoundException extends AppException {
    public NotFoundException(String entityType, String id) {
        super(entityType + " not found: " + id, "NOT_FOUND");
    }
}

class DatabaseException extends AppException {
    public DatabaseException(String message, Throwable cause) {
        super(message, "DATABASE_ERROR");
        initCause(cause);
    }
}

// Service using exception hierarchy
class UserService {
    private final java.util.Map<String, User> users = new java.util.HashMap<>();
    
    public User createUser(String id, String name, String email) 
            throws ValidationException {
        if (id == null || id.isBlank()) {
            throw new ValidationException("ID cannot be blank", "id");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name cannot be blank", "name");
        }
        if (email == null || !email.contains("@")) {
            throw new ValidationException("Invalid email", "email");
        }
        
        User user = new User(id, name, email);
        users.put(id, user);
        return user;
    }
    
    public User getUser(String id) throws NotFoundException {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("User", id);
        }
        return user;
    }
}

class ErrorHandler {
    public static void handle(AppException e) {
        System.err.printf("[%s] %s%n", e.getErrorCode(), e.getMessage());
        
        if (e instanceof ValidationException ve) {
            System.err.println("  Field: " + ve.getField());
        } else if (e instanceof NotFoundException nfe) {
            System.err.println("  Resource not found");
        } else if (e instanceof DatabaseException dbe) {
            System.err.println("  Cause: " + dbe.getCause().getMessage());
        }
    }
    
    public static void main(String[] args) {
        UserService service = new UserService();
        
        try {
            service.createUser("", "John", "john@example.com");
        } catch (ValidationException e) {
            handle(e);
        }
        
        try {
            service.getUser("nonexistent");
        } catch (NotFoundException e) {
            handle(e);
        }
    }
}
```

## Testing

```java
// Test hierarchy
abstract class BaseTest {
    protected StringBuilder log = new StringBuilder();
    
    protected void log(String message) {
        log.append(message).append("\n");
    }
    
    public abstract void setup();
    public abstract void runTests();
    public abstract void teardown();
    
    public final void execute() {
        setup();
        runTests();
        teardown();
        System.out.println(log.toString());
    }
}

class InheritanceTest extends BaseTest {
    private Dog dog;
    private Cat cat;
    
    @Override
    public void setup() {
        log("Setting up test fixtures...");
        dog = new Dog("Rex", "Labrador");
        cat = new Cat("Whiskers", true);
    }
    
    @Override
    public void runTests() {
        log("Running inheritance tests...");
        
        testInheritedMethod();
        testSubclassMethod();
        testPolymorphism();
    }
    
    private void testInheritedMethod() {
        dog.eat();
        cat.eat();
        log("PASS: Inherited methods work correctly");
    }
    
    private void testSubclassMethod() {
        dog.bark();
        cat.purr();
        log("PASS: Subclass methods work correctly");
    }
    
    private void testPolymorphism() {
        java.util.List<Animal> animals = java.util.List.of(dog, cat);
        for (Animal animal : animals) {
            animal.eat(); // Polymorphic call
        }
        log("PASS: Polymorphism works correctly");
    }
    
    @Override
    public void teardown() {
        log("Cleaning up...");
        dog = null;
        cat = null;
    }
}

// Test runner
class TestRunner {
    public static void main(String[] args) {
        new InheritanceTest().execute();
    }
}
```

## Performance

| Aspect | Performance Impact | Optimization |
|--------|-------------------|--------------|
| Method dispatch | O(1) vtable lookup | JIT inlines hot paths |
| Constructor chaining | O(depth) calls | Avoid deep hierarchies |
| `instanceof` | O(depth) checks | Use `sealed` classes |
| Reflection | Slow | Cache results |
| Field access | O(1) | Use final for optimization |

```java
public class PerformanceDemo {
    
    // Benchmark: Inheritance vs Composition
    static abstract class ShapeBase {
        abstract double area();
    }
    
    static class CircleBase extends ShapeBase {
        double radius;
        @Override double area() { return Math.PI * radius * radius; }
    }
    
    interface ShapeInterface {
        double area();
    }
    
    static class CircleInterface implements ShapeInterface {
        double radius;
        @Override public double area() { return Math.PI * radius * radius; }
    }
    
    record CircleRecord(double radius) {
        double area() { return Math.PI * radius * radius; }
    }
    
    public static void main(String[] args) {
        final int WARMUP = 10_000;
        final int ITERATIONS = 1_000_000;
        
        // Warmup JIT
        for (int i = 0; i < WARMUP; i++) {
            var c1 = new CircleBase();
            c1.radius = 5.0;
            c1.area();
            
            var c2 = new CircleInterface();
            c2.radius = 5.0;
            c2.area();
            
            var c3 = new CircleRecord(5.0);
            c3.area();
        }
        
        // Benchmark inheritance
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            var c = new CircleBase();
            c.radius = 5.0;
            c.area();
        }
        long inheritanceTime = System.nanoTime() - start;
        
        // Benchmark interface
        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            var c = new CircleInterface();
            c.radius = 5.0;
            c.area();
        }
        long interfaceTime = System.nanoTime() - start;
        
        // Benchmark record
        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            var c = new CircleRecord(5.0);
            c.area();
        }
        long recordTime = System.nanoTime() - start;
        
        System.out.printf("Inheritance: %d ms%n", inheritanceTime / 1_000_000);
        System.out.printf("Interface:   %d ms%n", interfaceTime / 1_000_000);
        System.out.printf("Record:      %d ms%n", recordTime / 1_000_000);
    }
}
```

## Security Considerations

```java
// Security: Don't expose sensitive data through inheritance
class SecureBase {
    // BAD: Subclass can access password
    protected String password;
    
    // GOOD: Use private with getter
    private String encryptedPassword;
    
    public String getEncryptedPassword() {
        return encryptedPassword;
    }
    
    // GOOD: Final method prevents bypassing security
    public final boolean authenticate(String input) {
        return hash(input).equals(encryptedPassword);
    }
    
    private String hash(String input) {
        return java.util.Base64.getEncoder()
            .encodeToString(input.getBytes());
    }
}

class SecureUser extends SecureBase {
    private String role;
    
    // GOOD: Cannot override authenticate to bypass security
    // public boolean authenticate(String input) { return true; } // COMPILE ERROR
    
    // GOOD: Validate in constructor
    public SecureUser(String password, String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role required");
        }
        this.encryptedPassword = hash(password); // Uses parent's private method indirectly
        this.role = role;
    }
    
    // GOOD: Restrict who can modify role
    public void setRole(String newRole, SecureBase admin) {
        if (admin instanceof AdminUser) {
            this.role = newRole;
        }
    }
}

class AdminUser extends SecureUser {
    public AdminUser(String password) {
        super(password, "ADMIN");
    }
    
    // GOOD: Audit logging
    @Override
    public String toString() {
        return "AdminUser[***]"; // Don't expose sensitive info
    }
}

// Sealed classes prevent unauthorized extension (Java 17+)
sealed interface PaymentMethod permits CreditCard, DebitCard, BankTransfer {
    boolean validate();
}

record CreditCard(String number, String cvv) implements PaymentMethod {
    @Override
    public boolean validate() {
        return number != null && number.length() == 16;
    }
    
    @Override
    public String toString() {
        return "CreditCard[****" + number.substring(number.length() - 4) + "]";
    }
}

record DebitCard(String number) implements PaymentMethod {
    @Override
    public boolean validate() {
        return number != null && number.length() == 16;
    }
    
    @Override
    public String toString() {
        return "DebitCard[****" + number.substring(number.length() - 4) + "]";
    }
}

record BankTransfer(String routingNumber, String accountNumber) implements PaymentMethod {
    @Override
    public boolean validate() {
        return routingNumber != null && accountNumber != null;
    }
    
    @Override
    public String toString() {
        return "BankTransfer[***]";
    }
}

// Any other class trying to implement PaymentMethod will fail to compile
// This prevents malicious implementations
```

## Scalability

```java
// Scalable design: Plugin architecture using inheritance
interface ScalableComponent {
    String getName();
    void initialize();
    void shutdown();
    boolean isHealthy();
}

abstract class BaseComponent implements ScalableComponent {
    protected volatile boolean running = false;
    protected final java.util.concurrent.atomic.AtomicLong requestCount = 
        new java.util.concurrent.atomic.AtomicLong(0);
    
    @Override
    public void initialize() {
        running = true;
        System.out.println(getName() + " initialized");
    }
    
    @Override
    public void shutdown() {
        running = false;
        System.out.println(getName() + " shut down. Requests handled: " + 
                         requestCount.get());
    }
    
    @Override
    public boolean isHealthy() {
        return running;
    }
    
    protected void incrementRequests() {
        requestCount.incrementAndGet();
    }
}

// Horizontally scalable service
class LoadBalancedService extends BaseComponent {
    private final java.util.List<ServiceInstance> instances = 
        new java.util.concurrent.CopyOnWriteArrayList<>();
    
    @Override
    public String getName() { return "LoadBalancedService"; }
    
    public void addInstance(ServiceInstance instance) {
        instances.add(instance);
        instance.initialize();
    }
    
    public String handleRequest(String request) {
        incrementRequests();
        ServiceInstance instance = selectInstance();
        return instance.process(request);
    }
    
    private ServiceInstance selectInstance() {
        // Round-robin selection
        return instances.get((int) (requestCount.get() % instances.size()));
    }
    
    @Override
    public boolean isHealthy() {
        return running && instances.stream().allMatch(ServiceInstance::isHealthy);
    }
}

class ServiceInstance extends BaseComponent {
    private final String id;
    private final java.util.concurrent.BlockingQueue<String> queue = 
        new java.util.concurrent.LinkedBlockingQueue<>(1000);
    
    public ServiceInstance(String id) {
        this.id = id;
    }
    
    @Override
    public String getName() { return "Instance-" + id; }
    
    public String process(String request) {
        incrementRequests();
        return "Processed by " + id + ": " + request;
    }
}

// Auto-scaling manager
class AutoScaler {
    private final LoadBalancedService service;
    private final int minInstances;
    private final int maxInstances;
    
    public AutoScaler(LoadBalancedService service, int min, int max) {
        this.service = service;
        this.minInstances = min;
        this.maxInstances = max;
    }
    
    public void scale() {
        long requests = service.requestCount.get();
        int currentSize = service.instances.size();
        
        if (requests > currentSize * 100 && currentSize < maxInstances) {
            service.addInstance(new ServiceInstance(String.valueOf(currentSize + 1)));
            System.out.println("Scaled up to " + (currentSize + 1) + " instances");
        }
    }
}

class ScalabilityDemo {
    public static void main(String[] args) {
        LoadBalancedService service = new LoadBalancedService();
        service.initialize();
        
        // Add initial instances
        for (int i = 0; i < 3; i++) {
            service.addInstance(new ServiceInstance(String.valueOf(i)));
        }
        
        // Simulate load
        for (int i = 0; i < 10; i++) {
            System.out.println(service.handleRequest("Request-" + i));
        }
        
        service.shutdown();
    }
}
```

## Tools & Libraries

```java
// Using Lombok with inheritance
// @Getter, @Setter, @ToString automatically handle inherited fields

// Using MapStruct for inheritance mapping
// @Mapper(inheritanceStrategy = InheritanceStrategy.EXPLICIT)

// Using Jackson for polymorphic deserialization
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Dog.class, name = "dog"),
    @JsonSubTypes.Type(value = Cat.class, name = "cat")
})
abstract class JacksonAnimal {
    public String name;
}

class JacksonDog extends JacksonAnimal {
    public String breed;
}

class JacksonCat extends JacksonAnimal {
    public boolean isIndoor;
}

// Using records with inheritance (Java 16+)
interface Identifiable {
    String id();
}

record EmployeeRecord(String id, String name, double salary) implements Identifiable {}
record ManagerRecord(String id, String name, double salary, int teamSize) implements Identifiable {}

// Using sealed classes with pattern matching (Java 17+)
sealed interface Expr permits Num, Add, Mul {
    double eval();
}

record Num(double value) implements Expr {
    @Override public double eval() { return value; }
}

record Add(Expr left, Expr right) implements Expr {
    @Override public double eval() { return left.eval() + right.eval(); }
}

record Mul(Expr left, Expr right) implements Expr {
    @Override public double eval() { return left.eval() * right.eval(); }
}

class ExprDemo {
    static double evaluate(Expr expr) {
        return switch (expr) {
            case Num n -> n.value();
            case Add a -> evaluate(a.left()) + evaluate(a.right());
            case Mul m -> evaluate(m.left()) * evaluate(m.right());
        };
    }
    
    public static void main(String[] args) {
        Expr expr = new Add(new Num(2), new Mul(new Num(3), new Num(4)));
        System.out.println("Result: " + evaluate(expr)); // 14.0
    }
}
```

## References

```java
// Key Java Language Specifications for Inheritance:

// 1. JLS §8.1.4: Class Extends
// "The optional class Extends clause specifies the direct superclass of the 
//  current class declaration."

// 2. JLS §8.4.8: Inheritance, Overriding, and Hiding
// "A class C inherits from its direct superclass all concrete methods m of the 
//  superclass that are accessible to code in C and are not overridden."

// 3. JLS §15.12.2: Compile-Time Step 2: Determine Method to Invoke
// "The method to be invoked is the one that matches the method signature."

// 4. JLS §15.27.4: Type of a Switch Expression
// "A switch expression can use pattern matching with sealed hierarchies."

// Useful resources:
// - Effective Java, Joshua Bloch (Item 18-19: Inheritance)
// - Clean Code, Robert Martin (Chapter 9: Unit Tests)
// - Design Patterns, Gang of Four (Template Method pattern)
// - Java Concurrency in Practice, Brian Goetz

// JDK source code references:
// - java.util.AbstractList (Template Method)
// - java.io.FilterInputStream (Decorator pattern via inheritance)
// - java.lang.Number (abstract class for numeric types)

// Recommended reading order for mastering inheritance:
// 1. Start with "Effective Java" Items 18-19
// 2. Study JDK source code for ArrayList, AbstractList
// 3. Practice with Template Method pattern
// 4. Learn about sealed classes (Java 17+)
// 5. Study Liskov Substitution Principle
// 6. Explore composition alternatives

class ReferenceDemo {
    public static void main(String[] args) {
        // Demonstrate JDK inheritance examples
        
        // ArrayList inherits from AbstractList
        java.util.List<String> list = new java.util.ArrayList<>();
        list.add("Hello");
        System.out.println("ArrayList: " + list);
        
        // Number hierarchy
        Integer i = 42;
        Double d = 3.14;
        Number n = i; // Number is superclass
        System.out.println("Integer as Number: " + n.intValue());
        n = d;
        System.out.println("Double as Number: " + n.doubleValue());
        
        // Exception hierarchy
        try {
            throw new java.io.FileNotFoundException("test.txt");
        } catch (java.io.IOException e) {
            System.out.println("Caught IOException: " + e.getClass().getSimpleName());
        }
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
