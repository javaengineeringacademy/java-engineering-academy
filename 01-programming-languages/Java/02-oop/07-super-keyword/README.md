# super keyword

## Introduction

The `super` keyword in Java is a reference variable that is used to refer to the immediate parent class object. It is primarily used to access parent class members (fields, methods, and constructors) that are hidden or overridden by the child class. Understanding `super` is essential for mastering inheritance and constructor chaining in Java.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the purpose and usage of the `super` keyword
- [ ] Access parent class fields and methods using `super`
- [ ] Call parent class constructors from child class constructors
- [ ] Implement proper constructor chaining in inheritance hierarchies
- [ ] Differentiate between `this` and `super` keywords
- [ ] Apply best practices when using `super` in real-world scenarios

## Prerequisites

- [06-this-keyword](../06-this-keyword/README.md) - Understanding of `this` keyword
- [09-inheritance](../09-inheritance/README.md) - Basic knowledge of inheritance
- [04-constructors](../04-constructors/README.md) - Understanding of constructors

## Why This Concept Exists

### The Problem

In inheritance hierarchies, child classes often need to:
1. Access parent class fields that are shadowed by child class fields
2. Call parent class constructors to initialize inherited fields
3. Invoke parent class methods that have been overridden

Without a mechanism to explicitly reference the parent class, these operations would be ambiguous or impossible.

### The Solution

The `super` keyword provides a clear and explicit way to:
1. Reference the parent class instance
2. Call parent class constructors using `super()`
3. Access parent class methods and fields directly

### Real-World Analogy

Think of `super` like calling your parent for advice. When you're in a situation where you need to access knowledge or resources from your family line (parent class), you use `super` to explicitly reach out to them. Just as you might say "My mother taught me this" (referencing parent's knowledge), you use `super.method()` to reference parent's implementation.

## Internal Working

### How super Works

#### JVM Perspective

When the JVM encounters the `super` keyword:
1. It creates a reference to the parent class portion of the current object
2. The reference points to the same object instance but provides access to parent class members
3. During compilation, `super` calls are resolved to direct method invocations on the parent class

#### Memory Representation

```
┌─────────────────────────────────────┐
│         Child Object (obj)          │
├─────────────────────────────────────┤
│  Child Class Portion                │
│  - childField                       │
│  - childMethods()                   │
├─────────────────────────────────────┤
│  Parent Class Portion (super ref)   │
│  - parentField                      │
│  - parentMethods()                  │
└─────────────────────────────────────┘
```

### Constructor Chaining with super

```
Object() → Parent() → Child()
         ↑            ↑
       super()      super()
```

When a child object is created:
1. `Object` constructor is called first (implicitly)
2. Parent constructor is called via `super()`
3. Child constructor executes

## Syntax

### Accessing Parent Fields
```java
super.fieldName
```

### Calling Parent Methods
```java
super.methodName()
```

### Calling Parent Constructor
```java
super()        // No-arg constructor
super(args)    // Parameterized constructor
```

## Easy Examples

### Example 1: Accessing Parent Field

**Problem Statement**: When a child class field shadows a parent class field, how do we access the parent's version?

**Implementation**:

```java
package academy.javaengineering.oop.superkeyword;

class Animal {
    String type = "Animal";
}

class Dog extends Animal {
    String type = "Dog";
    
    void displayType() {
        System.out.println("Child type: " + type);
        System.out.println("Parent type: " + super.type);
    }
}

public class SuperFieldDemo {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.displayType();
    }
}
```

**Output**:
```
Child type: Dog
Parent type: Animal
```

**Best Practices**:
- Use `super` when you need to access a shadowed parent field
- Consider renaming fields to avoid shadowing when possible

### Example 2: Calling Parent Method

**Problem Statement**: How do we invoke a parent class method that has been overridden?

**Implementation**:

```java
package academy.javaengineering.oop.superkeyword;

class Vehicle {
    void start() {
        System.out.println("Vehicle is starting...");
    }
}

class Car extends Vehicle {
    @Override
    void start() {
        System.out.println("Car is starting...");
        super.start();  // Call parent method
        System.out.println("Car is ready to go!");
    }
}

public class SuperMethodDemo {
    public static void main(String[] args) {
        Car car = new Car();
        car.start();
    }
}
```

**Output**:
```
Car is starting...
Vehicle is starting...
Car is ready to go!
```

**Best Practices**:
- Use `super.method()` when you want to extend rather than replace parent behavior
- Place `super.method()` call at the beginning if you want parent initialization first

### Example 3: Calling Parent Constructor

**Problem Statement**: How do we ensure parent class initialization when creating a child object?

**Implementation**:

```java
package academy.javaengineering.oop.superkeyword;

class Person {
    String name;
    
    Person(String name) {
        this.name = name;
        System.out.println("Person constructor called for: " + name);
    }
}

class Student extends Person {
    int studentId;
    
    Student(String name, int studentId) {
        super(name);  // Must be first statement
        this.studentId = studentId;
        System.out.println("Student constructor called with ID: " + studentId);
    }
}

public class SuperConstructorDemo {
    public static void main(String[] args) {
        Student student = new Student("Alice", 1001);
    }
}
```

**Output**:
```
Person constructor called for: Alice
Student constructor called with ID: 1001
```

**Best Practices**:
- Always call `super()` as the first statement in child constructor
- Match the `super()` call with available parent constructors

## Medium Examples

### Example 4: Constructor Chaining in Multi-Level Inheritance

**Problem Statement**: How does constructor chaining work in a three-level inheritance hierarchy?

**Implementation**:

```java
package academy.javaengineering.oop.superkeyword;

class Animal {
    String species;
    
    Animal(String species) {
        this.species = species;
        System.out.println("Animal constructor: " + species);
    }
}

class Mammal extends Animal {
    boolean isWarmBlooded;
    
    Mammal(String species, boolean isWarmBlooded) {
        super(species);
        this.isWarmBlooded = isWarmBlooded;
        System.out.println("Mammal constructor: warmBlooded=" + isWarmBlooded);
    }
}

class Dog extends Mammal {
    String breed;
    
    Dog(String breed) {
        super("Dog", true);
        this.breed = breed;
        System.out.println("Dog constructor: " + breed);
    }
    
    void display() {
        System.out.println("Breed: " + breed);
        System.out.println("Species: " + super.species);
        System.out.println("Warm Blooded: " + super.isWarmBlooded);
    }
}

public class ConstructorChainingDemo {
    public static void main(String[] args) {
        Dog dog = new Dog("Golden Retriever");
        System.out.println("---");
        dog.display();
    }
}
```

**Output**:
```
Animal constructor: Dog
Mammal constructor: warmBlooded=true
Dog constructor: Golden Retriever
---
Breed: Golden Retriever
Species: Dog
Warm Blooded: true
```

### Example 5: super with Method Overriding and Extension

**Problem Statement**: How do we properly extend parent class behavior while maintaining the original functionality?

**Implementation**:

```java
package academy.javaengineering.oop.superkeyword;

class Calculator {
    int add(int a, int b) {
        System.out.println("Base calculator adding");
        return a + b;
    }
    
    int multiply(int a, int b) {
        System.out.println("Base calculator multiplying");
        return a * b;
    }
}

class ScientificCalculator extends Calculator {
    @Override
    int add(int a, int b) {
        System.out.println("Scientific calculator adding");
        int result = super.add(a, b);  // Reuse parent logic
        System.out.println("Adding logging...");
        return result;
    }
    
    @Override
    int multiply(int a, int b) {
        System.out.println("Scientific calculator multiplying");
        return super.multiply(a, b);  // Delegate to parent
    }
    
    int power(int base, int exponent) {
        int result = 1;
        for (int i = 0; i < exponent; i++) {
            result = multiply(result, base);  // Using overridden method
        }
        return result;
    }
}

public class MethodExtensionDemo {
    public static void main(String[] args) {
        ScientificCalculator calc = new ScientificCalculator();
        System.out.println("Sum: " + calc.add(5, 3));
        System.out.println("---");
        System.out.println("Power: " + calc.power(2, 10));
    }
}
```

**Output**:
```
Scientific calculator adding
Base calculator adding
Adding logging...
Sum: 8
---
Scientical calculator multiplying
Base calculator multiplying
...
Power: 1024
```

### Example 6: super vs this in Constructor Overloading

**Problem Statement**: How do `super` and `this` work together in constructor overloading scenarios?

**Implementation**:

```java
package academy.javaengineering.oop.superkeyword;

class Employee {
    String name;
    double salary;
    
    Employee() {
        this("Unknown", 0.0);  // Calls parameterized constructor
        System.out.println("Default Employee constructor");
    }
    
    Employee(String name) {
        this(name, 30000.0);  // Calls two-param constructor
        System.out.println("Single-param Employee constructor");
    }
    
    Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
        System.out.println("Employee created: " + name);
    }
}

class Manager extends Employee {
    String department;
    
    Manager() {
        this("General", 50000.0);
        System.out.println("Default Manager constructor");
    }
    
    Manager(String department, double salary) {
        super("Manager", salary);  // Call parent constructor
        this.department = department;
        System.out.println("Manager of " + department);
    }
}

public class SuperThisDemo {
    public static void main(String[] args) {
        Manager mgr = new Manager();
        System.out.println("---");
        System.out.println("Name: " + mgr.name);
        System.out.println("Dept: " + mgr.department);
    }
}
```

**Output**:
```
Employee created: Manager
Manager of General
Default Manager constructor
---
Name: Manager
Dept: General
```

## Hard Examples

### Example 7: Abstract Class Constructor Chaining

**Problem Statement**: How does `super` work in abstract class hierarchies with multiple levels?

**Implementation**:

```java
package academy.javaengineering.oop.superkeyword;

abstract class Shape {
    String color;
    
    Shape(String color) {
        this.color = color;
        System.out.println("Shape constructor: " + color);
    }
    
    abstract double area();
    
    void displayColor() {
        System.out.println("Color: " + color);
    }
}

abstract class Rectangle extends Shape {
    double width, height;
    
    Rectangle(String color, double width, double height) {
        super(color);  // Must call abstract class constructor
        this.width = width;
        this.height = height;
        System.out.println("Rectangle initialized");
    }
    
    @Override
    double area() {
        return width * height;
    }
}

class ColoredRectangle extends Rectangle {
    String pattern;
    
    ColoredRectangle(String color, double width, double height, String pattern) {
        super(color, width, height);
        this.pattern = pattern;
        System.out.println("ColoredRectangle with pattern: " + pattern);
    }
    
    void display() {
        displayColor();  // Inherited method
        System.out.println("Pattern: " + pattern);
        System.out.println("Area: " + area());
    }
}

public class AbstractSuperDemo {
    public static void main(String[] args) {
        ColoredRectangle rect = new ColoredRectangle("Red", 10, 5, "Striped");
        System.out.println("---");
        rect.display();
    }
}
```

**Output**:
```
Shape constructor: Red
Rectangle initialized
ColoredRectangle with pattern: Striped
---
Color: Red
Pattern: Striped
Area: 50.0
```

### Example 8: Interface Default Method with super

**Problem Statement**: How do we use `super` to call interface default methods?

**Implementation**:

```java
package academy.javaengineering.oop.superkeyword;

interface Loggable {
    default void log(String message) {
        System.out.println("[LOG]: " + message);
    }
    
    default void error(String message) {
        System.out.println("[ERROR]: " + message);
    }
}

interface Auditable {
    default void log(String message) {
        System.out.println("[AUDIT]: " + message);
    }
}

class BaseService implements Loggable, Auditable {
    @Override
    public void log(String message) {
        Loggable.super.log(message);  // Call specific interface method
        System.out.println("[SERVICE]: " + message);
    }
    
    public void performOperation() {
        log("Operation started");
        error("Something went wrong");  // Inherited from Loggable
        log("Operation completed");
    }
}

public class InterfaceSuperDemo {
    public static void main(String[] args) {
        BaseService service = new BaseService();
        service.performOperation();
    }
}
```

**Output**:
```
[LOG]: Operation started
[SERVICE]: Operation started
[ERROR]: Something went wrong
[LOG]: Operation completed
[SERVICE]: Operation completed
```

### Example 9: Diamond Problem Resolution with super

**Problem Statement**: How does Java resolve the diamond problem when using `super` with multiple inheritance?

**Implementation**:

```java
package academy.javaengineering.oop.superkeyword;

interface Flyable {
    default void move() {
        System.out.println("Flying in the sky");
    }
    
    default void describe() {
        System.out.println("I can fly");
    }
}

interface Swimmable {
    default void move() {
        System.out.println("Swimming in water");
    }
    
    default void describe() {
        System.out.println("I can swim");
    }
}

class Bird implements Flyable {
    @Override
    public void move() {
        System.out.println("Bird is flying");
    }
}

class Duck extends Bird implements Swimmable {
    @Override
    public void move() {
        Flyable.super.move();  // Call Flyable's move
        Swimmable.super.move();  // Call Swimmable's move
        System.out.println("Duck does both!");
    }
    
    @Override
    public void describe() {
        System.out.println("Duck can fly AND swim");
    }
    
    void display() {
        describe();
        move();
    }
}

public class DiamondProblemDemo {
    public static void main(String[] args) {
        Duck duck = new Duck();
        duck.display();
    }
}
```

**Output**:
```
Duck can fly AND swim
Flying in the sky
Swimming in water
Duck does both!
```

## Exercises

### Easy

1. **Access Shadowed Field**: Create a `Parent` class with a field `value = 10` and a `Child` class with `value = 20`. Write a method in `Child` that prints both values using `super`.

2. **Call Parent Method**: Create a `Greeting` class with a method `sayHello()` that prints "Hello!". Create a `PoliteGreeting` subclass that overrides `sayHello()` to first call the parent version, then add ", how are you?".

3. **Constructor Call**: Create a `Book` class with a constructor that takes a `title`. Create a `TextBook` subclass that takes `title` and `subject`, calling the parent constructor appropriately.

### Medium

4. **Three-Level Inheritance**: Create `Animal` → `Mammal` → `Dog` hierarchy. Implement constructors at each level that print messages, demonstrating proper constructor chaining.

5. **Method Extension Pattern**: Create a `Database` class with `connect()` and `query()` methods. Create a `PostgreSQLDatabase` subclass that extends behavior by adding logging before and after calling parent methods.

6. **Multiple Interface super**: Create two interfaces with a default method `process()`. Create a class implementing both, using `super` to call each interface's implementation.

### Hard

7. **Abstract Class Chain**: Create `Vehicle` (abstract) → `Car` (abstract) → `ElectricCar` hierarchy with proper constructor chaining and abstract method implementations.

8. **Template Method with super**: Implement a Template Method design pattern where `super` is used to call hook methods from the parent class.

9. **Framework Base Class**: Create a framework-style base class with protected methods and fields. Create subclasses that properly use `super` to access and extend framework functionality.

## Interview Questions

### Beginner

1. **What is the purpose of the `super` keyword in Java?**
   
   Answer: `super` is a reference variable used to refer to the immediate parent class object. It's used to access parent class fields, methods, and constructors that are hidden or overridden by the child class.

2. **Can we use `super` in a static context?**
   
   Answer: No, `super` cannot be used in static contexts because it refers to an instance of the parent class, and static contexts don't have instance references.

3. **What happens if we don't call `super()` in a child constructor?**
   
   Answer: Java automatically inserts a no-argument `super()` call as the first statement if no `super()` or `this()` is explicitly written. If the parent has no no-arg constructor, a compilation error occurs.

### Intermediate

4. **What is the difference between `this()` and `super()`?**
   
   Answer: `this()` calls another constructor in the same class, while `super()` calls a constructor in the parent class. Both must be the first statement in a constructor, so they cannot be used together.

5. **How does `super` work with method overriding?**
   
   Answer: When you override a parent method, you can use `super.methodName()` to call the parent's implementation. This is useful when you want to extend rather than completely replace the parent behavior.

6. **Can `super` be used to access private members of the parent class?**
   
   Answer: No, `super` cannot access private members directly. However, if the parent class provides public or protected getter/setter methods, those can be called using `super.getterMethod()`.

### Senior

7. **How does `super` interact with the diamond problem in Java?**
   
   Answer: In the diamond problem with interfaces, `super` can be used to specify which interface's default method to call: `InterfaceName.super.methodName()`. This provides explicit control over which implementation is invoked.

8. **Explain the memory model when `super` is used in constructor chaining.**
   
   Answer: When `super()` is called, a single object is being created. The `super` reference points to the parent portion of the same object. All constructors share the same object being constructed, with each adding its own initialization.

## Common Pitfalls

### 1. Using super() Not as First Statement

**Wrong**:
```java
class Child extends Parent {
    Child() {
        System.out.println("Child constructor");
        super();  // Compilation error!
    }
}
```

**Right**:
```java
class Child extends Parent {
    Child() {
        super();  // Must be first statement
        System.out.println("Child constructor");
    }
}
```

### 2. Calling super on Private Methods

**Wrong**:
```java
class Parent {
    private void secret() {
        System.out.println("Parent secret");
    }
}

class Child extends Parent {
    void callSecret() {
        super.secret();  // Compilation error!
    }
}
```

**Right**:
```java
class Parent {
    protected void secret() {
        System.out.println("Parent secret");
    }
}

class Child extends Parent {
    void callSecret() {
        super.secret();  // Works fine
    }
}
```

### 3. Confusing super with super() vs super.method()

**Wrong**:
```java
class Child extends Parent {
    Child() {
        super;  // Syntax error - can't use super as a reference like this
    }
    
    void doSomething() {
        super;  // Wrong usage
    }
}
```

**Right**:
```java
class Child extends Parent {
    Child() {
        super();  // Call parent constructor
    }
    
    void doSomething() {
        super.doSomething();  // Call parent method
    }
}
```

## Best Practices

1. **Always call super() first**: When creating child constructors, always call `super()` as the first statement to ensure proper initialization order.

2. **Use super to extend, not replace**: When overriding methods, prefer extending parent behavior with `super.method()` rather than completely rewriting logic.

3. **Avoid excessive shadowing**: Minimize field shadowing to reduce the need for `super.fieldName` access, which can make code harder to read.

4. **Document super usage**: When using `super` to call parent constructors or methods, add comments explaining why this delegation is necessary.

5. **Leverage super for framework extension**: When extending frameworks or libraries, use `super` to properly integrate with base class contracts and lifecycle methods.

## Real World Usage

### How Spring Uses This

In Spring Framework, `super` is commonly used when extending base classes:

```java
@Service
public class UserServiceImpl extends BaseService<User> {
    
    public UserServiceImpl(UserRepository repository) {
        super(repository);  // Call parent constructor
    }
    
    @Override
    public void process(User user) {
        super.process(user);  // Call parent logic
        // Additional Spring-specific processing
    }
}
```

### How Hibernate Uses This

Hibernate uses `super` in entity inheritance:

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseEntity {
    @Id
    private Long id;
    
    @PrePersist
    public void prePersist() {
        // Base audit logic
    }
}

@Entity
public class User extends BaseEntity {
    private String name;
    
    @PrePersist
    public void prePersist() {
        super.prePersist();  // Call base audit
        // User-specific logic
    }
}
```

### How JDK Uses This

The JDK extensively uses `super` in its collections framework:

```java
public class ArrayList<E> extends AbstractList<E> {
    @Override
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;
        return true;
    }
}

public abstract class AbstractList<E> {
    public boolean add(E e) {
        add(size(), e);
        return true;
    }
}
```

## Summary

- `super` provides explicit access to parent class members (fields, methods, constructors)
- `super()` must be the first statement in a constructor and is used for constructor chaining
- `super.method()` allows extending rather than replacing parent method behavior
- `super.fieldName` accesses shadowed parent fields
- `InterfaceName.super.method()` resolves diamond problem in interface inheritance
- Java automatically calls `super()` if not explicitly written (no-arg version)
- `super` cannot be used in static contexts or to access private members

## References

- [Oracle Java Tutorials - Using the Keyword](https://docs.oracle.com/en/java/javase/21/java/IandI/super.html)
- [Java Language Specification - Class Instance Creation](https://docs.oracle.com/javase/specs/jls/se17/html/jls-15.html#jls-15.9.3)
- [Effective Java by Joshua Bloch - Item 19: Design and document for inheritance](https://www.oreilly.com/library/view/effective-java/9780134686097/)

**Next**: [24-object-lifecycle](../24-object-lifecycle/README.md)
