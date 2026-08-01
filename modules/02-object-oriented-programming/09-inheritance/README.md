# Inheritance in Java

## Table of Contents
- [Introduction](#introduction)
- [Learning Objectives](#learning-objectives)
- [Prerequisites](#prerequisites)
- [Why This Concept Exists](#why-this-concept-exists)
- [Internal Working](#internal-working)
- [Syntax](#syntax)
- [Easy Examples](#easy-examples)
- [Medium Examples](#medium-examples)
- [Hard Examples](#hard-examples)
- [Exercises](#exercises)
- [Interview Questions](#interview-questions)
- [Common Pitfalls](#common-pitfalls)
- [Best Practices](#best-practices)
- [Real World Usage](#real-world-usage)
- [References](#references)
- [Summary](#summary)

---

## Introduction

Inheritance is a fundamental mechanism of object-oriented programming that allows a new class (subclass or derived class) to inherit fields and methods from an existing class (superclass or base class). In Java, inheritance establishes an "is-a" relationship between classes, enabling code reuse, polymorphism, and the creation of hierarchical taxonomies. Using the `extends` keyword, a subclass gains access to all non-private members of its superclass, and can override methods to provide specialized behavior while maintaining a consistent interface. Inheritance is one of the four pillars of OOP and is essential for building flexible, extensible, and maintainable Java applications.

---

## Learning Objectives

- Understand the `extends` keyword and how it establishes inheritance hierarchies
- Learn how method inheritance and overriding work in Java
- Master constructor chaining with `super()` and the order of initialization
- Apply `instanceof` for safe type checking and downcasting

---

## Prerequisites

- [01-classes-and-objects/README.md](../01-classes-and-objects/README.md) — Classes, objects, fields, methods
- [06-this-keyword/README.md](../06-this-keyword/README.md) — `this` and `super` references
- [08-encapsulation/README.md](../08-encapsulation/README.md) — Access modifiers and visibility

---

## Why This Concept Exists

### The Problem

Without inheritance, similar classes must duplicate common code:

```java
public class Car {
    String make;
    String model;
    int year;
    double speed;

    void start() { System.out.println("Car started"); }
    void accelerate() { speed += 10; }
    void brake() { speed = Math.max(0, speed - 10); }
    void displayInfo() { System.out.println(make + " " + model + " " + year); }
}

public class Truck {
    String make;
    String model;
    int year;
    double speed;
    double payloadCapacity;

    void start() { System.out.println("Truck started"); }
    void accelerate() { speed += 5; } // Trucks accelerate slower
    void brake() { speed = Math.max(0, speed - 8); }
    void displayInfo() { System.out.println(make + " " + model + " " + year); }
}
```

Both classes share `make`, `model`, `year`, `speed` and similar methods. Adding a new field means modifying every class.

### The Solution

Inheritance eliminates duplication by extracting common behavior into a superclass:

```java
public class Vehicle {
    String make;
    String model;
    int year;
    double speed;

    void start() { System.out.println("Vehicle started"); }
    void accelerate() { speed += 10; }
    void brake() { speed = Math.max(0, speed - 10); }
}

public class Car extends Vehicle {
    // Inherits all fields and methods from Vehicle
    // Can add car-specific behavior
}

public class Truck extends Vehicle {
    double payloadCapacity;

    @Override
    void accelerate() { speed += 5; } // Specialized behavior
}
```

### Real-World Analogy

Think of inheritance like a family tree. A child inherits traits (eye color, height) from their parents but can also develop unique characteristics. Similarly, a subclass inherits fields and methods from its superclass but can add new features or modify inherited behavior.

---

## Internal Working

### How Inheritance Works at the JVM Level

When a subclass is instantiated, the JVM allocates memory for all fields (both inherited and new). The constructor chain starts from the topmost superclass and works down.

#### Memory Layout

```
Object Layout for Truck extends Vehicle:
┌─────────────────────────────┐
│ Object header (class info)  │
│ Vehicle fields:             │
│   - make (String)           │
│   - model (String)          │
│   - year (int)              │
│   - speed (double)          │
│ Truck fields:               │
│   - payloadCapacity (double)│
└─────────────────────────────┘
```

#### Constructor Execution Order

```
new Truck("Ford", "F-150", 2024, 1000)
    │
    ▼
Object() constructor          ← First
    │
    ▼
Vehicle() constructor         ← Second
    │
    ▼
Truck() constructor           ← Last
```

#### Method Dispatch

Inherited methods are resolved at compile time (if not overridden) or at runtime (if overridden and called through a superclass reference):

```bytecode
// For non-overridden methods: compile-time binding
invokevirtual Vehicle.accelerate()  // Direct call

// For overridden methods: runtime dispatch
aload_1                            // Load object reference
invokevirtual Truck.accelerate()   // Resolved at runtime based on actual type
```

### The `super` Keyword

`super` refers to the immediate superclass and is used to:
1. Call superclass constructors: `super(args)`
2. Access superclass methods: `super.method()`
3. Access superclass fields: `super.field` (when shadowed)

---

## Syntax

### 1. Basic Inheritance

```java
public class ChildClass extends ParentClass {
    // Inherits all non-private members
    // Can add new fields and methods
    // Can override inherited methods
}
```

### 2. Calling Superclass Constructor

```java
public class ChildClass extends ParentClass {
    public ChildClass(String arg) {
        super(arg); // Must be first statement
        // Additional initialization
    }
}
```

### 3. Method Overriding

```java
public class Animal {
    public void speak() {
        System.out.println("...");
    }
}

public class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("Woof!");
    }
}
```

### 4. Accessing Superclass Members

```java
public class Child extends Parent {
    public void doSomething() {
        super.doSomething(); // Call parent implementation
        // Additional behavior
    }
}
```

---

## Easy Examples

### Example 1: Basic Animal Hierarchy

**Problem Statement:**
Model different animals that share common behavior (eating, sleeping) but have unique behaviors (speaking, moving).

**Implementation:**

```java
package academy.javaengineering.oop.inheritance;

public class Animal {
    protected String name;
    protected int age;
    protected double weight;

    public Animal(String name, int age, double weight) {
        this.name = name;
        this.age = age;
        this.weight = weight;
    }

    public void eat(String food) {
        System.out.println(name + " is eating " + food);
    }

    public void sleep() {
        System.out.println(name + " is sleeping");
    }

    public void speak() {
        System.out.println(name + " makes a sound");
    }

    public void displayInfo() {
        System.out.printf("Name: %s, Age: %d, Weight: %.1f kg%n", name, age, weight);
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public double getWeight() { return weight; }
}

class Dog extends Animal {
    private String breed;

    public Dog(String name, int age, double weight, String breed) {
        super(name, age, weight);
        this.breed = breed;
    }

    @Override
    public void speak() {
        System.out.println(name + " says: Woof!");
    }

    public void fetch(String item) {
        System.out.println(name + " fetches the " + item);
    }

    public String getBreed() { return breed; }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Breed: " + breed);
    }
}

class Cat extends Animal {
    private boolean isIndoor;

    public Cat(String name, int age, double weight, boolean isIndoor) {
        super(name, age, weight);
        this.isIndoor = isIndoor;
    }

    @Override
    public void speak() {
        System.out.println(name + " says: Meow!");
    }

    public void purr() {
        System.out.println(name + " purrs contentedly");
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Indoor: " + isIndoor);
    }
}

class AnimalDemo {
    public static void main(String[] args) {
        Dog dog = new Dog("Buddy", 3, 12.5, "Golden Retriever");
        Cat cat = new Cat("Whiskers", 5, 4.2, true);

        System.out.println("=== Dog ===");
        dog.displayInfo();
        dog.speak();
        dog.eat("kibble");
        dog.fetch("ball");

        System.out.println("\n=== Cat ===");
        cat.displayInfo();
        cat.speak();
        cat.eat("tuna");
        cat.purr();

        System.out.println("\n=== Polymorphism ===");
        Animal a1 = dog;  // Dog IS-A Animal
        Animal a2 = cat;  // Cat IS-A Animal
        a1.speak();        // Calls Dog's speak()
        a2.speak();        // Calls Cat's speak()
    }
}
```

**Output:**
```
=== Dog ===
Name: Buddy, Age: 3, Weight: 12.5 kg
Breed: Golden Retriever
Buddy says: Woof!
Buddy is eating kibble
Buddy fetches the ball

=== Cat ===
Name: Whiskers, Age: 5, Weight: 4.2 kg
Indoor: true
Whiskers says: Meow!
Whiskers is eating tuna
Whiskers purrs contentedly

=== Polymorphism ===
Buddy says: Woof!
Whiskers says: Meow!
```

**Best Practices:**
- Use `protected` fields for subclass access, `private` for encapsulation
- Call `super.displayInfo()` to reuse parent formatting
- Override `toString()` for debugging convenience

---

### Example 2: Constructor Chaining

**Problem Statement:**
Understand the order of constructor execution and how `super()` chains constructors from parent to child.

**Implementation:**

```java
package academy.javaengineering.oop.inheritance;

public class Person {
    String name;
    int age;

    public Person() {
        this("Unknown", 0);
        System.out.println("Person() no-arg constructor");
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("Person(String, int) constructor");
    }
}

class Employee extends Person {
    String employeeId;
    double salary;

    public Employee() {
        super();
        this.employeeId = "E000";
        this.salary = 0;
        System.out.println("Employee() no-arg constructor");
    }

    public Employee(String name, int age, String employeeId, double salary) {
        super(name, age);
        this.employeeId = employeeId;
        this.salary = salary;
        System.out.println("Employee(String, int, String, double) constructor");
    }

    @Override
    public String toString() {
        return "Employee{name='" + name + "', age=" + age +
               ", id='" + employeeId + "', salary=" + salary + "}";
    }
}

class Manager extends Employee {
    String department;

    public Manager() {
        super();
        this.department = "Unknown";
        System.out.println("Manager() no-arg constructor");
    }

    public Manager(String name, int age, String employeeId, double salary, String department) {
        super(name, age, employeeId, salary);
        this.department = department;
        System.out.println("Manager(String, int, String, double, String) constructor");
    }

    @Override
    public String toString() {
        return "Manager{name='" + name + "', age=" + age +
               ", id='" + employeeId + "', salary=" + salary +
               ", dept='" + department + "'}";
    }
}

class ConstructorDemo {
    public static void main(String[] args) {
        System.out.println("=== Creating Manager with all args ===");
        Manager m1 = new Manager("Alice", 35, "M001", 95000, "Engineering");
        System.out.println(m1);

        System.out.println("\n=== Creating Manager with no-arg ===");
        Manager m2 = new Manager();
        System.out.println(m2);
    }
}
```

**Output:**
```
=== Creating Manager with all args ===
Person(String, int) constructor
Employee(String, int, String, double) constructor
Manager(String, int, String, double, String) constructor
Manager{name='Alice', age=35, id='M001', salary=95000.0, dept='Engineering'}

=== Creating Manager with no-arg ===
Person(String, int) constructor
Employee() no-arg constructor
Manager() no-arg constructor
Manager{name='Unknown', age=0, id='E000', salary=0.0, dept='Unknown'}
```

**Best Practices:**
- Always ensure the superclass constructor is called (explicitly or implicitly)
- If the superclass has no no-arg constructor, the subclass must explicitly call `super(args)`
- Chain constructors using `this()` within the same class to avoid duplication

---

### Example 3: `instanceof` and Safe Downcasting

**Problem Statement:**
When working with polymorphic references, you often need to check the actual type before performing type-specific operations.

**Implementation:**

```java
package academy.javaengineering.oop.inheritance;

public abstract class Shape {
    protected String color;

    public Shape(String color) {
        this.color = color;
    }

    public abstract double getArea();
    public abstract double getPerimeter();

    public String getColor() { return color; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[color=" + color + "]";
    }
}

class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    public double getRadius() { return radius; }

    @Override
    public double getArea() { return Math.PI * radius * radius; }

    @Override
    public double getPerimeter() { return 2 * Math.PI * radius; }
}

class Rectangle extends Shape {
    private double width, height;

    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }

    @Override
    public double getArea() { return width * height; }

    @Override
    public double getPerimeter() { return 2 * (width + height); }
}

class Triangle extends Shape {
    private double a, b, c; // sides

    public Triangle(String color, double a, double b, double c) {
        super(color);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double getArea() {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    @Override
    public double getPerimeter() { return a + b + c; }
}

class InstanceofDemo {
    public static void processShape(Shape shape) {
        System.out.println("Processing: " + shape);
        System.out.printf("Area: %.2f, Perimeter: %.2f%n",
            shape.getArea(), shape.getPerimeter());

        // Type checking with instanceof
        if (shape instanceof Circle) {
            Circle circle = (Circle) shape; // Safe downcast
            System.out.println("Circle radius: " + circle.getRadius());
        } else if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            System.out.printf("Rectangle dimensions: %.1f x %.1f%n",
                rect.getWidth(), rect.getHeight());
        } else if (shape instanceof Triangle) {
            System.out.println("Triangle is a polygon with 3 sides");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle("Red", 5),
            new Rectangle("Blue", 4, 6),
            new Triangle("Green", 3, 4, 5)
        };

        for (Shape shape : shapes) {
            processShape(shape);
        }

        // instanceof with null
        Shape nullShape = null;
        System.out.println("null instanceof Shape: " + (nullShape instanceof Shape)); // false

        // Pattern matching (Java 16+)
        Shape s = new Circle("Yellow", 3);
        if (s instanceof Circle c) {
            System.out.println("Pattern match - Radius: " + c.getRadius());
        }
    }
}
```

**Output:**
```
Processing: Circle[color=Red]
Area: 78.54, Perimeter: 31.42
Circle radius: 5.0

Processing: Rectangle[color=Blue]
Area: 24.00, Perimeter: 20.00
Rectangle dimensions: 4.0 x 6.0

Processing: Triangle[color=Green]
Area: 6.00, Perimeter: 12.00
Triangle is a polygon with 3 sides

null instanceof Shape: false
Pattern match - Radius: 3.0
```

**Best Practices:**
- Use `instanceof` sparingly — prefer polymorphism over type checking
- Use pattern matching (Java 16+) for cleaner syntax
- Always check for null before downcasting

---

## Medium Examples

### Example 1: Method Overriding and `super`

**Problem Statement:**
Demonstrate proper method overriding, including when and how to call the superclass implementation using `super`.

**Requirements:**
- Create a `Vehicle` hierarchy with `Car`, `Truck`, and `ElectricVehicle`
- Each subclass customizes `start()`, `stop()`, and `maintenance()`
- Use `super` to chain behavior

**Implementation:**

```java
package academy.javaengineering.oop.inheritance;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    protected String make;
    protected String model;
    protected int year;
    protected double fuelLevel;
    protected boolean running;
    protected final List<String> maintenanceLog;

    public Vehicle(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.fuelLevel = 100.0;
        this.running = false;
        this.maintenanceLog = new ArrayList<>();
    }

    public void start() {
        if (fuelLevel <= 0) {
            System.out.println("Cannot start: No fuel");
            return;
        }
        running = true;
        System.out.println(year + " " + make + " " + model + " started");
    }

    public void stop() {
        running = false;
        System.out.println(year + " " + make + " " + model + " stopped");
    }

    public void refuel(double amount) {
        fuelLevel = Math.min(100, fuelLevel + amount);
        System.out.printf("Refueled to %.1f%%%n", fuelLevel);
    }

    public void performMaintenance() {
        maintenanceLog.add("General maintenance performed");
        System.out.println("Maintenance completed for " + make + " " + model);
    }

    public void displayStatus() {
        System.out.printf("[%s] %d %s %s | Fuel: %.1f%% | Running: %s%n",
            getClass().getSimpleName(), year, make, model, fuelLevel, running);
    }

    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getFuelLevel() { return fuelLevel; }
    public boolean isRunning() { return running; }
    public List<String> getMaintenanceLog() {
        return new ArrayList<>(maintenanceLog);
    }
}

class Car extends Vehicle {
    private int doors;

    public Car(String make, String model, int year, int doors) {
        super(make, model, year);
        this.doors = doors;
    }

    @Override
    public void start() {
        System.out.println("Checking car systems...");
        super.start(); // Call parent's start()
        System.out.println("Car ready to drive");
    }

    @Override
    public void performMaintenance() {
        super.performMaintenance(); // Parent's maintenance
        maintenanceLog.add("Oil change performed");
        maintenanceLog.add("Tire rotation performed");
        System.out.println("Car-specific maintenance completed");
    }

    public int getDoors() { return doors; }
}

class Truck extends Vehicle {
    private double payloadCapacity;

    public Truck(String make, String model, int year, double payloadCapacity) {
        super(make, model, year);
        this.payloadCapacity = payloadCapacity;
    }

    @Override
    public void start() {
        System.out.println("Initializing truck systems...");
        System.out.println("Checking air brakes...");
        super.start();
        System.out.println("Truck ready for cargo");
    }

    @Override
    public void stop() {
        System.out.println("Engaging parking brake...");
        super.stop();
        System.out.println("Truck secured");
    }

    @Override
    public void performMaintenance() {
        super.performMaintenance();
        maintenanceLog.add("Brake inspection performed");
        maintenanceLog.add("Payload capacity verified");
        System.out.println("Truck-specific maintenance completed");
    }

    public double getPayloadCapacity() { return payloadCapacity; }
}

class ElectricVehicle extends Car {
    private double batteryLevel;
    private final double maxRange;

    public ElectricVehicle(String make, String model, int year, int doors, double maxRange) {
        super(make, model, year, doors);
        this.batteryLevel = 100.0;
        this.maxRange = maxRange;
    }

    @Override
    public void start() {
        System.out.println("Activating electric powertrain...");
        // Don't call super.start() — EVs don't use fuel
        running = true;
        System.out.println(year + " " + make + " " + model + " started silently");
    }

    @Override
    public void refuel(double amount) {
        System.out.println("Cannot refuel an electric vehicle. Use charge() instead.");
    }

    public void charge(double amount) {
        batteryLevel = Math.min(100, batteryLevel + amount);
        System.out.printf("Battery charged to %.1f%%%n", batteryLevel);
    }

    @Override
    public void performMaintenance() {
        super.performMaintenance();
        maintenanceLog.add("Battery health check performed");
        maintenanceLog.add("Regenerative braking system checked");
        System.out.println("EV-specific maintenance completed");
    }

    @Override
    public void displayStatus() {
        super.displayStatus();
        System.out.printf("Battery: %.1f%% | Range: %.0f km%n", batteryLevel, maxRange * batteryLevel / 100);
    }

    public double getBatteryLevel() { return batteryLevel; }
    public double getMaxRange() { return maxRange; }
}

class VehicleDemo {
    public static void main(String[] args) {
        Car car = new Car("Toyota", "Camry", 2024, 4);
        Truck truck = new Truck("Ford", "F-150", 2024, 1000);
        ElectricVehicle ev = new ElectricVehicle("Tesla", "Model 3", 2024, 4, 500);

        System.out.println("=== Car ===");
        car.start();
        car.displayStatus();
        car.performMaintenance();
        System.out.println("Maintenance log: " + car.getMaintenanceLog());

        System.out.println("\n=== Truck ===");
        truck.start();
        truck.displayStatus();
        truck.stop();

        System.out.println("\n=== Electric Vehicle ===");
        ev.start();
        ev.displayStatus();
        ev.charge(20);
        ev.performMaintenance();
    }
}
```

**Output:**
```
=== Car ===
Checking car systems...
2024 Toyota Camry started
Car ready to drive
[Car] 2024 Toyota Camry | Fuel: 100.0% | Running: true
Maintenance completed for Toyota Camry
Car-specific maintenance completed
Maintenance log: [General maintenance performed, Oil change performed, Tire rotation performed]

=== Truck ===
Initializing truck systems...
Checking air brakes...
2024 Ford F-150 started
Truck ready for cargo
[Truck] 2024 Ford F-150 | Fuel: 100.0% | Running: true
Engaging parking brake...
2024 Ford F-150 stopped
Truck secured

=== Electric Vehicle ===
Activating electric powertrain...
2024 Tesla Model 3 started silently
[ElectricVehicle] 2024 Tesla Model 3 | Fuel: 100.0% | Running: true
Battery: 100.0% | Range: 500 km
Battery charged to 100.0%
Maintenance completed for Tesla Model 3
Car-specific maintenance completed
EV-specific maintenance completed
```

**Alternative:**
Use the Template Method pattern to define a skeleton algorithm in the superclass with abstract hooks for subclasses.

---

### Example 2: The Diamond Problem

**Problem Statement:**
Demonstrate the diamond problem in Java and how the language resolves it through single inheritance and interface defaults.

**Implementation:**

```java
package academy.javaengineering.oop.inheritance;

// The Diamond Problem with Interfaces
interface Flyable {
    default void move() {
        System.out.println("Flying through the air");
    }

    default void describe() {
        System.out.println("I can fly");
    }
}

interface Swimmable {
    default void move() {
        System.out.println("Swimming through water");
    }

    default void describe() {
        System.out.println("I can swim");
    }
}

interface Walkable {
    default void move() {
        System.out.println("Walking on ground");
    }

    default void describe() {
        System.out.println("I can walk");
    }
}

// Java resolves diamond problem: must override conflicting defaults
class Duck implements Flyable, Swimmable, Walkable {
    @Override
    public void move() {
        // Choose one or combine
        System.out.println("Duck waddles on land, swims in water, and flies short distances");
    }

    @Override
    public void describe() {
        System.out.println("I'm a duck - I can fly, swim, and walk!");
    }
}

class Penguin implements Swimmable, Walkable {
    @Override
    public void move() {
        System.out.println("Penguin waddles on land and swims expertly");
    }

    @Override
    public void describe() {
        System.out.println("I'm a penguin - I can swim and walk but can't fly");
    }
}

// Single class inheritance prevents diamond problem
abstract class Animal {
    String name;

    Animal(String name) {
        this.name = name;
    }

    abstract void speak();
}

// Java doesn't support multiple class inheritance
// class Cat extends Animal, Pet { } // COMPILE ERROR!

// Solution: extend one class, implement multiple interfaces
class Pet {
    String ownerName;

    Pet(String ownerName) {
        this.ownerName = ownerName;
    }

    void beFriendly() {
        System.out.println(name + " is being friendly with " + ownerName);
    }
}

class Cat extends Animal implements Walkable {
    private final Pet pet;

    Cat(String name, String ownerName) {
        super(name);
        this.pet = new Pet(ownerName);
    }

    @Override
    void speak() {
        System.out.println(name + " says: Meow!");
    }

    @Override
    public void move() {
        System.out.println(name + " walks gracefully");
    }

    // Delegate to Pet for pet-specific behavior
    void beFriendly() {
        pet.beFriendly();
    }
}

class DiamondProblemDemo {
    public static void main(String[] args) {
        System.out.println("=== Interface Diamond Resolution ===");
        Duck duck = new Duck("Donald");
        duck.move();
        duck.describe();

        Penguin penguin = new Penguin("Tux");
        penguin.move();
        penguin.describe();

        System.out.println("\n=== Single Inheritance + Interface ===");
        Cat cat = new Cat("Whiskers", "Alice");
        cat.speak();
        cat.move();
        cat.beFriendly();

        System.out.println("\n=== Polymorphic References ===");
        Animal animal = cat;     // Upcast to Animal
        Walkable walker = cat;   // Upcast to Walkable

        animal.speak();  // Calls Cat's speak()
        walker.move();   // Calls Cat's move()
    }
}
```

**Output:**
```
=== Interface Diamond Resolution ===
Duck waddles on land, swims in water, and flies short distances
I'm a duck - I can fly, swim, and walk!
Penguin waddles on land and swims expertly
I'm a penguin - I can swim and walk but can't fly

=== Single Inheritance + Interface ===
Whiskers says: Meow!
Whiskers walks gracefully
Whiskers is being friendly with Alice

=== Polymorphic References ===
Whiskers says: Meow!
Whiskers walks gracefully
```

**Alternative:**
Use the Adapter pattern to wrap one interface and present it as another, avoiding the diamond problem entirely.

---

### Example 3: Inheritance with `final`

**Problem Statement:**
Use `final` to prevent inheritance and method overriding where appropriate.

**Implementation:**

```java
package academy.javaengineering.oop.inheritance;

// Final class — cannot be extended
final class StringUtils {
    private StringUtils() {} // Prevent instantiation

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String capitalize(String s) {
        if (isNullOrEmpty(s)) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static String repeat(String s, int count) {
        if (isNullOrEmpty(s) || count <= 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(s);
        }
        return sb.toString();
    }
}

// Cannot do: class SpecialStringUtils extends StringUtils {} // COMPILE ERROR

// Final methods in non-final class
class Vehicle2 {
    String make;

    Vehicle2(String make) {
        this.make = make;
    }

    // Final method — cannot be overridden
    public final String getMake() {
        return make;
    }

    // Non-final method — can be overridden
    public void describe() {
        System.out.println("Vehicle: " + make);
    }
}

class Car2 extends Vehicle2 {
    int year;

    Car2(String make, int year) {
        super(make);
        this.year = year;
    }

    // Can override describe() but NOT getMake()
    @Override
    public void describe() {
        System.out.println("Car: " + make + " " + year);
    }

    // This would cause a compile error:
    // @Override
    // public String getMake() { return "Modified"; } // COMPILE ERROR: cannot override final method
}

class FinalDemo {
    public static void main(String[] args) {
        // StringUtils utility class
        System.out.println("=== StringUtils ===");
        System.out.println("isNullOrEmpty(null): " + StringUtils.isNullOrEmpty(null));
        System.out.println("isNullOrEmpty(\"\"): " + StringUtils.isNullOrEmpty(""));
        System.out.println("capitalize(\"hello\"): " + StringUtils.capitalize("hello"));
        System.out.println("repeat(\"ab\", 3): " + StringUtils.repeat("ab", 3));

        // Final method
        System.out.println("\n=== Final Method ===");
        Car2 car = new Car2("Toyota", 2024);
        System.out.println("getMake(): " + car.getMake()); // Inherited final method
        car.describe(); // Overridden method
    }
}
```

**Output:**
```
=== StringUtils ===
isNullOrEmpty(null): true
isNullOrEmpty(""): true
capitalize("hello"): Hello
repeat("ab", 3): ababab

=== Final Method ===
getMake(): Toyota
Car: Toyota 2024
```

**Best Practices:**
- Use `final` classes for utility classes and security-sensitive classes
- Use `final` methods when the implementation must not change (invariants)
- Consider `final` for immutable objects

---

## Hard Examples

### Example 1: Composition vs Inheritance Trade-offs

**Architecture:**
Design a rich text editor that demonstrates when to use inheritance (is-a) versus composition (has-a).

**Implementation:**

```java
package academy.javaengineering.oop.inheritance;

import java.util.*;

// Inheritance approach: TextElement hierarchy
abstract class TextElement {
    protected String content;

    TextElement(String content) {
        this.content = content;
    }

    abstract String render();

    public String getContent() { return content; }
}

class PlainText extends TextElement {
    PlainText(String content) {
        super(content);
    }

    @Override
    String render() {
        return content;
    }
}

class BoldText extends TextElement {
    BoldText(String content) {
        super(content);
    }

    @Override
    String render() {
        return "**" + content + "**";
    }
}

class ItalicText extends TextElement {
    ItalicText(String content) {
        super(content);
    }

    @Override
    String render() {
        return "*" + content + "*";
    }
}

// Composition approach: Decorator pattern
interface FormattedText {
    String render();
}

class SimpleText implements FormattedText {
    private final String text;

    SimpleText(String text) {
        this.text = text;
    }

    @Override
    public String render() {
        return text;
    }
}

// Decorators can be combined (composition)
abstract class TextDecorator implements FormattedText {
    protected final FormattedText delegate;

    TextDecorator(FormattedText delegate) {
        this.delegate = delegate;
    }
}

class BoldDecorator extends TextDecorator {
    BoldDecorator(FormattedText delegate) {
        super(delegate);
    }

    @Override
    public String render() {
        return "**" + delegate.render() + "**";
    }
}

class ItalicDecorator extends TextDecorator {
    ItalicDecorator(FormattedText delegate) {
        super(delegate);
    }

    @Override
    public String render() {
        return "*" + delegate.render() + "*";
    }
}

class UnderlineDecorator extends TextDecorator {
    UnderlineDecorator(FormattedText delegate) {
        super(delegate);
    }

    @Override
    public String render() {
        return "__" + delegate.render() + "__";
    }
}

// Document using composition
class Document {
    private final List<FormattedText> elements = new ArrayList<>();

    public void addElement(FormattedText element) {
        elements.add(element);
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for (FormattedText element : elements) {
            sb.append(element.render()).append(" ");
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        System.out.println("=== Inheritance Approach (Limited) ===");
        TextElement bold = new BoldText("Hello");
        TextElement italic = new ItalicText("World");
        System.out.println(bold.render() + " " + italic.render());
        // Cannot combine: no BoldItalicText class exists

        System.out.println("\n=== Composition Approach (Flexible) ===");
        Document doc = new Document();

        // Simple text
        doc.addElement(new SimpleText("Hello"));

        // Bold text
        doc.addElement(new BoldDecorator(new SimpleText("Beautiful")));

        // Bold + Italic (composition)
        doc.addElement(new BoldDecorator(new ItalicDecorator(new SimpleText("World"))));

        // Underline + Bold + Italic (deep composition)
        doc.addElement(new UnderlineDecorator(
            new BoldDecorator(
                new ItalicDecorator(new SimpleText("!!!")))));

        System.out.println(doc.render());

        System.out.println("\n=== Runtime Composition ===");
        String text = "Dynamic";
        FormattedText formatted = new SimpleText(text);

        boolean makeBold = true;
        boolean makeItalic = true;
        boolean makeUnderline = false;

        if (makeBold) formatted = new BoldDecorator(formatted);
        if (makeItalic) formatted = new ItalicDecorator(formatted);
        if (makeUnderline) formatted = new UnderlineDecorator(formatted);

        System.out.println(formatted.render());
    }
}
```

**Unit Tests:**

```java
package academy.javaengineering.oop.inheritance;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testPlainRendering() {
        FormattedText text = new SimpleText("Hello");
        assertEquals("Hello", text.render());
    }

    @Test
    public void testBoldDecorator() {
        FormattedText text = new BoldDecorator(new SimpleText("Hello"));
        assertEquals("**Hello**", text.render());
    }

    @Test
    public void testNestedDecorators() {
        FormattedText text = new BoldDecorator(new ItalicDecorator(new SimpleText("Hello")));
        assertEquals("**_Hello_**", text.render());
    }

    @Test
    public void testDocumentRendering() {
        Document doc = new Document();
        doc.addElement(new SimpleText("Hello"));
        doc.addElement(new BoldDecorator(new SimpleText("World")));
        assertEquals("Hello **World**", doc.render());
    }
}
```

**Complexity:**
- Inheritance: O(1) per class, but O(n) classes for n combinations
- Composition: O(1) per decorator, O(n) decorators for n decorations
- Memory: Inheritance creates one object per type; composition creates decorator chain

**Best Practices:**
- Use composition (Decorator pattern) when behaviors can be combined
- Use inheritance when there's a clear is-a relationship
- Favor "has-a" over "is-a" for flexibility

---

### Example 2: Fragile Base Class Problem

**Architecture:**
Demonstrate the fragile base class problem where changes to a superclass break subclasses, and how to design robust inheritance hierarchies.

**Implementation:**

```java
package academy.javaengineering.oop.inheritance;

import java.util.*;

// FRAGILE: Superclass changes break subclass
class Order {
    private final List<String> items = new ArrayList<>();

    public void addItem(String item) {
        items.add(item);
        // What if we add logging here later?
        processItem(item); // This call breaks subclasses that rely on addItem behavior
    }

    protected void processItem(String item) {
        System.out.println("Processing: " + item);
    }

    public int getItemCount() {
        return items.size();
    }

    public List<String> getItems() {
        return Collections.unmodifiableList(items);
    }
}

class AuditedOrder extends Order {
    private int processCount = 0;

    @Override
    protected void processItem(String item) {
        processCount++;
        System.out.println("AUDIT: Item #" + processCount + " - " + item);
    }

    public int getProcessCount() {
        return processCount;
    }
}

// ROBUST: Using composition to avoid fragile base class
interface OrderProcessor {
    void processItem(String item);
}

class SimpleOrderProcessor implements OrderProcessor {
    @Override
    public void processItem(String item) {
        System.out.println("Processing: " + item);
    }
}

class AuditedOrderProcessor implements OrderProcessor {
    private int processCount = 0;
    private final OrderProcessor delegate;

    AuditedOrderProcessor(OrderProcessor delegate) {
        this.delegate = delegate;
    }

    @Override
    public void processItem(String item) {
        processCount++;
        System.out.println("AUDIT: Item #" + processCount + " - " + item);
        delegate.processItem(item);
    }

    public int getProcessCount() {
        return processCount;
    }
}

class RobustOrder {
    private final List<String> items = new ArrayList<>();
    private final OrderProcessor processor;

    RobustOrder(OrderProcessor processor) {
        this.processor = processor;
    }

    public void addItem(String item) {
        items.add(item);
        processor.processItem(item);
    }

    public int getItemCount() {
        return items.size();
    }
}

class FragileBaseClassDemo {
    public static void main(String[] args) {
        System.out.println("=== Fragile Base Class ===");
        AuditedOrder fragileOrder = new AuditedOrder();
        fragileOrder.addItem("Widget");
        fragileOrder.addItem("Gadget");
        System.out.println("Items: " + fragileOrder.getItemCount());
        System.out.println("Processed: " + fragileOrder.getProcessCount());

        System.out.println("\n=== Robust Composition ===");
        AuditedOrderProcessor auditedProcessor = new AuditedOrderProcessor(new SimpleOrderProcessor());
        RobustOrder robustOrder = new RobustOrder(auditedProcessor);
        robustOrder.addItem("Widget");
        robustOrder.addItem("Gadget");
        System.out.println("Items: " + robustOrder.getItemCount());
        System.out.println("Processed: " + auditedProcessor.getProcessCount());
    }
}
```

**Unit Tests:**

```java
package academy.javaengineering.oop.inheritance;

import org.junit.Test;
import static org.junit.Assert.*;

public class RobustOrderTest {

    @Test
    public void testSimpleOrder() {
        RobustOrder order = new RobustOrder(new SimpleOrderProcessor());
        order.addItem("Item1");
        order.addItem("Item2");
        assertEquals(2, order.getItemCount());
    }

    @Test
    public void testAuditedOrder() {
        AuditedOrderProcessor processor = new AuditedOrderProcessor(new SimpleOrderProcessor());
        RobustOrder order = new RobustOrder(processor);
        order.addItem("Item1");
        assertEquals(1, processor.getProcessCount());
    }

    @Test
    public void testFragileOrder() {
        AuditedOrder order = new AuditedOrder();
        order.addItem("Item1");
        assertEquals(1, order.getProcessCount());
        assertEquals(1, order.getItemCount());
    }
}
```

**Complexity:**
- Both approaches: O(1) per addItem
- Composition: O(d) where d is decorator depth
- Inheritance: O(1) but fragile to superclass changes

**Best Practices:**
- Design inheritance hierarchies with the Open/Closed Principle in mind
- Use the Fragile Base Class checklist:
  1. Does the superclass call overridable methods?
  2. Do subclasses depend on the order of method calls?
  3. Are there invariants that subclasses might violate?
- Default to composition unless there's a clear is-a relationship

---

## Exercises

### Easy

1. **Shape Hierarchy:**
   Create a `Shape` base class with `area()` and `perimeter()` methods. Implement `Circle`, `Rectangle`, and `Square` subclasses.

2. **Animal Sounds:**
   Create an `Animal` class with a `speak()` method. Override it in `Dog`, `Cat`, and `Cow` classes to print different sounds.

3. **Constructor Chaining:**
   Create a `Person` → `Employee` → `Manager` hierarchy where each constructor calls the appropriate `super()` constructor.

### Medium

4. **Vehicle Fleet:**
   Create a `Vehicle` → `Car`, `Truck`, `Motorcycle` hierarchy. Each vehicle type has different fuel efficiency and maintenance schedules. Use `super` to chain method calls.

5. **Library System:**
   Create a `LibraryItem` base class with `checkout()` and `returnItem()` methods. Implement `Book`, `DVD`, and `Magazine` subclasses with different loan periods and fees.

6. **Shape Calculator:**
   Implement a `ShapeCalculator` that processes an array of `Shape` objects using polymorphism, calculating total area and perimeter.

### Hard

7. **Employee Payroll:**
   Create a `Employee` → `SalariedEmployee`, `HourlyEmployee`, `CommissionEmployee` hierarchy. Each has different `calculatePay()` logic. Include `BonusCalculator` interface for flexible bonus strategies.

8. **Custom Exception Hierarchy:**
   Design an exception hierarchy: `AppException` → `ValidationException`, `DatabaseException`, `NetworkException`. Each should carry relevant context data.

9. **Game Character System:**
   Create a `Character` base class with `attack()`, `defend()`, and `useSpecialAbility()` methods. Implement `Warrior`, `Mage`, and `Ranger` subclasses with different stats and abilities.

---

## Interview Questions

### Easy

1. **What is inheritance in Java?**
   Inheritance is a mechanism where a new class (subclass) inherits fields and methods from an existing class (superclass). It establishes an "is-a" relationship and enables code reuse and polymorphism.

2. **What does the `extends` keyword do?**
   `extends` establishes an inheritance relationship. `class Child extends Parent` means Child inherits all non-private members of Parent. Java supports single inheritance only (one superclass).

3. **What is the difference between `super` and `this`?**
   `this` refers to the current object instance. `super` refers to the immediate superclass. `this()` calls another constructor of the same class; `super()` calls the superclass constructor.

### Medium

4. **Why does Java not support multiple inheritance of classes?**
   To avoid the diamond problem — ambiguity when two parent classes have the same method. Java resolves this by allowing single class inheritance but multiple interface implementation, where default methods must be explicitly overridden.

5. **What is method overriding and how does it differ from method overloading?**
   Overriding: Same method signature in subclass, replaces parent implementation, resolved at runtime. Overloading: Same method name with different parameters in the same class, resolved at compile time.

6. **Can a subclass access private members of its superclass?**
   No. Private members are only accessible within the class they're declared. Subclasses access inherited state through protected or public methods (getters/setters).

### Hard

7. **What is the fragile base class problem and how do you prevent it?**
   The fragile base class problem occurs when changes to a superclass break subclasses. Prevention: avoid calling overridable methods from the superclass, document invariants, use composition over inheritance, and follow the Open/Closed Principle.

8. **Explain the Liskov Substitution Principle with an example.**
   LSP states that objects of a subclass should be replaceable with objects of the superclass without breaking behavior. Example: If `Square extends Rectangle`, calling `setWidth(5)` on a Square should also set height — violating LSP because Square's behavior differs from Rectangle's.

---

## Common Pitfalls

### Pitfall 1: Calling Overridable Methods in Constructors

**Wrong:**
```java
class Parent {
    Parent() {
        initialize(); // Calls overridable method
    }
    void initialize() { System.out.println("Parent init"); }
}

class Child extends Parent {
    String name;
    Child(String name) {
        super(); // Parent() calls initialize() — name is null!
        this.name = name;
    }
    @Override
    void initialize() {
        System.out.println("Name: " + name.toUpperCase()); // NPE!
    }
}
```

**Right:**
```java
class Parent {
    Parent() {
        // Don't call overridable methods in constructor
    }
}

class Child extends Parent {
    String name;
    Child(String name) {
        super();
        this.name = name;
        initialize(); // Call after fields are initialized
    }
    void initialize() {
        System.out.println("Name: " + name.toUpperCase()); // Safe
    }
}
```

### Pitfall 2: Using `instanceof` for Polymorphism

**Wrong:**
```java
class Shape { void draw() {} }
class Circle extends Shape { void drawCircle() {} }
class Square extends Shape { void drawSquare() {} }

void draw(Shape shape) {
    if (shape instanceof Circle) ((Circle) shape).drawCircle();
    else if (shape instanceof Square) ((Square) shape).drawSquare();
    // Violates Open/Closed Principle — must modify for every new shape
}
```

**Right:**
```java
class Shape { abstract void draw(); }
class Circle extends Shape { void draw() { /* draw circle */ } }
class Square extends Shape { void draw() { /* draw square */ } }

void draw(Shape shape) {
    shape.draw(); // Polymorphism handles dispatch
}
```

### Pitfall 3: Inheritance for Code Reuse Without is-a Relationship

**Wrong:**
```java
class Stack extends ArrayList<Object> { // "Stack is an ArrayList"? No!
    public void push(Object item) { add(item); }
    public Object pop() { return remove(size() - 1); }
}
// Exposes: add(index, element), remove(index), etc. — breaks stack abstraction
```

**Right:**
```java
class Stack<T> {
    private final List<T> elements = new ArrayList<>();

    public void push(T item) { elements.add(item); }
    public T pop() { return elements.remove(elements.size() - 1); }
    public boolean isEmpty() { return elements.isEmpty(); }
    public int size() { return elements.size(); }
}
// Composition: Stack has-a List, not is-a List
```

---

## Best Practices

1. **Favor Composition Over Inheritance:**
   Unless there's a clear "is-a" relationship, use composition. Inheritance creates tight coupling; composition provides flexibility.

2. **Design for Extension or Make Final:**
   If a class is designed for inheritance, document the contract (overridable methods, invariants). If not, make it `final`.

3. **Use `@Override` Annotation:**
   Always use `@Override` when overriding methods. It prevents signature mismatches and makes intent clear.

4. **Avoid Calling Overridable Methods from Constructors:**
   The superclass constructor runs before the subclass constructor, so overridden methods may reference uninitialized fields.

5. **Keep Inheritance Hierarchies Shallow:**
   Deep hierarchies (more than 3 levels) are hard to understand and maintain. Prefer composition or interfaces for complex designs.

---

## Real World Usage

### Spring Framework
- `JpaRepository` extends `CrudRepository` — inheritance for shared CRUD methods
- `AbstractController` provides base implementation for custom controllers
- `HibernateTemplate` extends `JdbcOperations` for Hibernate-specific operations

### Hibernate / JPA
- `@Entity` classes extend `Serializable` for session storage
- `Interceptor` interface has empty default methods that subclasses override
- `Type` hierarchy: `AbstractType` → `StringType`, `IntegerType`, etc.

### JDK Source Code
- `ArrayList` extends `AbstractList` implements `List` — inheritance for code reuse
- `HashMap` extends `AbstractMap` implements `Map`
- `IOException` hierarchy: `IOException` → `FileNotFoundException`, `SocketException`, etc.

### Enterprise Applications
- Exception hierarchies for layered architecture (ServiceException, RepositoryException)
- Abstract base DAO classes with common CRUD operations
- Template Method pattern in framework code (JUnit, TestNG)

---

## References

- [Java Language Specification — Inheritance](https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html#jls-8.4)
- [Effective Java, 3rd Edition — Item 18: Favor composition over inheritance](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Oracle Java Tutorials — Classes and Inheritance](https://docs.oracle.com/javase/tutorial/java/javaOO/index.html)
- [Baeldung — Inheritance in Java](https://www.baeldung.com/java-inheritance)
- [Refactoring.Guru — Inheritance](https://refactoring.guru/design-patterns/inheritance)

---

## Summary

Inheritance is a powerful mechanism for code reuse and polymorphism in Java. Key takeaways:

- **extends keyword:** Establishes is-a relationship, single inheritance only
- **Constructor chaining:** Superclass constructors run first, subclass constructors run last
- **Method overriding:** Runtime dispatch enables polymorphic behavior
- **instanceof:** Safe type checking before downcasting
- **Diamond problem:** Resolved through single inheritance + interface defaults
- **Fragile base class:** Avoid by not calling overridable methods in constructors

**Golden rule:** Favor composition over inheritance unless there's a clear is-a relationship.

---

**Navigation:**
- Previous: [08-encapsulation](../08-encapsulation/README.md)
- Next: [10-polymorphism](../10-polymorphism/README.md)
- [Back to OOP Module](../README.md)
