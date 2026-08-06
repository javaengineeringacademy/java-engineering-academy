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

---

## Continue Reading

- [Part 2](README-part2.md)
- [Part 3](README-part3.md)
