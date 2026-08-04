# Inheritance

Inheritance allows a class to derive properties and behavior from another class, establishing an "is-a" relationship between types.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Inheritance](#basic-inheritance)
3. [The super Keyword](#the-super-keyword)
4. [Method Overriding](#method-overriding)
5. [Covariant Return Types](#covariant-return-types)
6. [Abstract Classes](#abstract-classes)
7. [Inheritance Hierarchies](#inheritance-hierarchies)
8. [Final Classes and Methods](#final-classes-and-methods)
9. [Best Practices](#best-practices)
10. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Inheritance?

Inheritance creates a new class (child/subclass) from an existing class (parent/superclass), inheriting its fields and methods.

```
        ┌──────────────┐
        │    Animal     │  ← Superclass
        ├──────────────┤
        │ - name        │
        │ - sound       │
        ├──────────────┤
        │ + speak()     │
        └──────┬───────┘
               │
      ┌────────┴────────┐
      │                 │
┌─────┴──────┐   ┌─────┴──────┐
│    Dog     │   │    Cat     │  ← Subclasses
├────────────┤   ├────────────┤
│ - breed    │   │ - indoor   │
├────────────┤   ├────────────┤
│ + fetch()  │   │ + purr()   │
└────────────┘   └────────────┘
```

### Benefits

- **Code Reuse** - inherit behavior without rewriting
- **Polymorphism** - treat subclasses as their parent type
- **Hierarchy** - model real-world relationships naturally
- **Extensibility** - add specialized behavior in subclasses

---

## Basic Inheritance

### Extending a Class

```java
public class Animal {
    protected String name;
    protected int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void speak() {
        System.out.println(name + " makes a sound");
    }

    public String getInfo() {
        return name + " (age " + age + ")";
    }
}

public class Dog extends Animal {
    private String breed;

    public Dog(String name, int age, String breed) {
        super(name, age);  // Call parent constructor
        this.breed = breed;
    }

    // Dog-specific method
    public void fetch(String item) {
        System.out.println(name + " fetches the " + item);
    }

    // Override parent method
    @Override
    public void speak() {
        System.out.println(name + " barks");
    }

    public String getBreed() {
        return breed;
    }
}

// Usage
Dog rex = new Dog("Rex", 5, "German Shepherd");
rex.speak();        // "Rex barks"
rex.fetch("ball");  // "Rex fetches the ball"
System.out.println(rex.getInfo());  // "Rex (age 5)"
```

### Constructor Chaining

```java
public class Vehicle {
    protected String make;
    protected String model;
    protected int year;

    public Vehicle(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
        System.out.println("Vehicle constructor called");
    }
}

public class Car extends Vehicle {
    private int doors;

    public Car(String make, String model, int year, int doors) {
        super(make, model, year);  // Must be first statement
        this.doors = doors;
        System.out.println("Car constructor called");
    }
}

// When creating a Car:
// 1. Car constructor is called
// 2. super() calls Vehicle constructor
// 3. Vehicle fields initialized
// 4. Car fields initialized
Car car = new Car("Toyota", "Camry", 2024, 4);
// Output:
// Vehicle constructor called
// Car constructor called
```

---

## The super Keyword

### Accessing Parent Methods

```java
public class Rectangle {
    protected double width;
    protected double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double area() {
        return width * height;
    }

    public String describe() {
        return "Rectangle: " + width + "x" + height;
    }
}

public class Square extends Rectangle {
    public Square(double side) {
        super(side, side);  // Pass same value for width and height
    }

    @Override
    public String describe() {
        return "Square: " + width + "x" + width;
    }

    // Add functionality while preserving parent behavior
    public double perimeter() {
        return 4 * width;
    }
}
```

### Accessing Parent Fields

```java
public class Person {
    protected String name;
    protected int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

public class Employee extends Person {
    private String employeeId;

    public Employee(String name, int age, String employeeId) {
        super(name, age);
        this.employeeId = employeeId;
    }

    public void celebrateBirthday() {
        super.age++;  // Access parent's field directly
        System.out.println(name + " is now " + super.age);
    }
}
```

### Calling Parent Constructor Conditionally

```java
public class Account {
    protected final String accountId;
    protected double balance;

    public Account(String accountId, double initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
    }
}

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accountId, double initialBalance, double rate) {
        super(accountId, initialBalance);
        this.interestRate = rate;
    }

    // Factory method for conditional initialization
    public static SavingsAccount create(String accountId, double balance, double rate) {
        if (balance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        return new SavingsAccount(accountId, balance, rate);
    }
}
```

---

## Method Overriding

### Basic Overriding

```java
public class Shape {
    protected String color;

    public Shape(String color) {
        this.color = color;
    }

    public double area() {
        return 0.0;
    }

    public void draw() {
        System.out.println("Drawing a " + color + " shape");
    }
}

public class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a " + color + " circle with radius " + radius);
    }
}

public class Rectangle extends Shape {
    private double width, height;

    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a " + color + " rectangle " + width + "x" + height);
    }
}

// Polymorphic usage
List<Shape> shapes = List.of(
    new Circle("red", 5),
    new Rectangle("blue", 4, 6),
    new Shape("green")
);

shapes.forEach(s -> {
    s.draw();
    System.out.println("Area: " + s.area());
});
```

### @Override Annotation

```java
public class Parent {
    public void doSomething() {
        System.out.println("Parent doing something");
    }
}

public class Child extends Parent {
    @Override  // Compiler verifies this actually overrides a parent method
    public void doSomething() {
        System.out.println("Child doing something");
    }

    @Override  // ERROR - no parent method with this signature
    public void doSomethingElse() {  // Compile error if @Override is used
        System.out.println("This doesn't override anything");
    }
}
```

### Calling Overridden Method with super

```java
public class Logger {
    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}

public class FileLogger extends Logger {
    private Path logFile;

    public FileLogger(Path logFile) {
        this.logFile = logFile;
    }

    @Override
    public void log(String message) {
        super.log(message);  // Call parent's implementation first
        try {
            Files.writeString(logFile, message + "\n", 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}
```

---

## Covariant Return Types

```java
public class Animal {
    public Animal create() {
        return new Animal();
    }

    public Animal clone() {
        return new Animal();
    }
}

public class Dog extends Animal {
    @Override
    public Dog create() {  // Return type is Dog, not Animal
        return new Dog();
    }

    @Override
    public Dog clone() {  // Covariant return type
        return new Dog();
    }
}

public class Puppy extends Dog {
    @Override
    public Puppy create() {  // Even more specific return type
        return new Puppy();
    }
}

// Usage
Dog dog = new Dog();
Dog puppy = dog.create();  // Returns Dog, not Animal
```

### Practical Example

```java
public class Builder<T> {
    protected T product;

    public Builder<T> setProduct(T product) {
        this.product = product;
        return this;
    }

    public T build() {
        return product;
    }
}

public class StringBuilder extends Builder<String> {
    private StringBuilder buffer = new StringBuilder();

    @Override
    public StringBuilder setProduct(String product) {
        buffer = new StringBuilder(product);
        return this;
    }

    public StringBuilder append(String text) {
        buffer.append(text);
        return this;
    }

    @Override
    public String build() {
        return buffer.toString();
    }
}
```

---

## Abstract Classes

### Basic Abstract Class

```java
public abstract class Vehicle {
    protected String make;
    protected String model;
    protected int year;

    public Vehicle(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }

    // Abstract method - must be implemented by subclasses
    public abstract double calculateFuelEfficiency();

    // Concrete method - inherited as-is
    public String getInfo() {
        return year + " " + make + " " + model;
    }

    // Template method pattern
    public final void start() {
        System.out.println("Performing pre-start checks...");
        engineCheck();
        System.out.println("Starting " + getInfo());
    }

    protected abstract void engineCheck();
}

public class ElectricCar extends Vehicle {
    private double batteryCapacity;

    public ElectricCar(String make, String model, int year, double batteryCapacity) {
        super(make, model, year);
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public double calculateFuelEfficiency() {
        return batteryCapacity * 4.5;  // miles per kWh equivalent
    }

    @Override
    protected void engineCheck() {
        System.out.println("Checking battery level...");
    }
}

public class GasCar extends Vehicle {
    private double mpg;

    public GasCar(String make, String model, int year, double mpg) {
        super(make, model, year);
        this.mpg = mpg;
    }

    @Override
    public double calculateFuelEfficiency() {
        return mpg;
    }

    @Override
    protected void engineCheck() {
        System.out.println("Checking oil level...");
    }
}
```

### Abstract Class vs Interface

```java
// Abstract class - can have state and constructors
public abstract class AbstractList<E> {
    protected int size;
    
    public AbstractList() {
        this.size = 0;
    }
    
    public abstract void add(E element);
    public abstract E get(int index);
    
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
}

// Interface - contract only (before Java 8)
public interface List<E> {
    void add(E element);
    E get(int index);
    int size();
    boolean isEmpty();
}
```

---

## Inheritance Hierarchies

### Deep Hierarchy Example

```java
// Level 1: Root
public abstract class Shape {
    protected String color;
    
    public Shape(String color) { this.color = color; }
    public abstract double area();
    public abstract double perimeter();
}

// Level 2: Broad categories
public abstract class Polygon extends Shape {
    protected int sides;
    
    public Polygon(String color, int sides) {
        super(color);
        this.sides = sides;
    }
    
    public int getSides() { return sides; }
}

public abstract class Ellipse extends Shape {
    protected double semiMajor, semiMinor;
    
    public Ellipse(String color, double semiMajor, double semiMinor) {
        super(color);
        this.semiMajor = semiMajor;
        this.semiMinor = semiMinor;
    }
}

// Level 3: Specific shapes
public class Rectangle extends Polygon {
    protected double width, height;
    
    public Rectangle(String color, double width, double height) {
        super(color, 4);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() { return width * height; }
    
    @Override
    public double perimeter() { return 2 * (width + height); }
}

public class Circle extends Ellipse {
    private double radius;
    
    public Circle(String color, double radius) {
        super(color, radius, radius);
        this.radius = radius;
    }
    
    @Override
    public double area() { return Math.PI * radius * radius; }
    
    @Override
    public double perimeter() { return 2 * Math.PI * radius; }
}

// Level 4: Specialized
public class Square extends Rectangle {
    public Square(String color, double side) {
        super(color, side, side);
    }
}
```

### Checking Types

```java
Shape shape = new Square("red", 5);

// instanceof check
if (shape instanceof Square) {
    Square square = (Square) shape;
    // Use square-specific methods
}

// Pattern matching (Java 16+)
if (shape instanceof Square square) {
    // Use square directly without casting
}

// Multi-pattern (Java 21+)
if (shape instanceof Circle c && c.getRadius() > 10) {
    System.out.println("Large circle");
}
```

---

## Final Classes and Methods

### Final Class (Cannot be Extended)

```java
public final class String {
    // Cannot create a subclass of String
}

public final class Math {
    // Utility class that cannot be extended
    private Math() {}
}

// This would cause a compile error:
// class MyString extends String { }
```

### Final Method (Cannot be Overridden)

```java
public class Parent {
    public final void criticalMethod() {
        // This method cannot be overridden
        System.out.println("Critical behavior");
    }
    
    public void overridableMethod() {
        System.out.println("Can be overridden");
    }
}

public class Child extends Parent {
    // ERROR: Cannot override final method
    // @Override
    // public void criticalMethod() { }
    
    @Override
    public void overridableMethod() {
        System.out.println("Overridden behavior");
    }
}
```

---

## Best Practices

### Favor Composition Over Inheritance

```java
// BAD: Inheritance for code reuse
public class Stack extends ArrayList<Object> {
    public void push(Object item) { add(item); }
    public Object pop() { 
        Object last = get(size() - 1);
        remove(size() - 1);
        return last;
    }
}

// GOOD: Composition
public class Stack<T> {
    private final List<T> items = new ArrayList<>();
    
    public void push(T item) { items.add(item); }
    public T pop() { 
        return items.remove(items.size() - 1);
    }
    public int size() { return items.size(); }
}
```

### Design for Extension

```java
// Design methods to be overridable when appropriate
public class Service {
    // Final - don't override infrastructure methods
    public final void process() {
        validate();
        execute();
        log();
    }
    
    // Protected - can be overridden by subclasses
    protected void validate() {
        // Default validation
    }
    
    protected void execute() {
        // Must be implemented by subclass
    }
    
    // Hook method - optional override
    protected void log() {
        System.out.println("Processing complete");
    }
}
```

### Use Liskov Substitution Principle

```java
// GOOD: Substitutable
public class Rectangle {
    protected double width, height;
    
    public void setWidth(double w) { this.width = w; }
    public void setHeight(double h) { this.height = h; }
    public double area() { return width * height; }
}

public class Square extends Rectangle {
    @Override
    public void setWidth(double w) {
        this.width = w;
        this.height = w;  // Maintains square invariant
    }
    
    @Override
    public void setHeight(double h) {
        this.width = h;
        this.height = h;
    }
}
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **`extends`** | Establishes inheritance relationship |
| **`super`** | References parent class members |
| **`@Override`** | Indicates method override from parent |
| **Covariant Return** | Overridden methods can return subclass types |
| **`abstract`** | Defines incomplete classes requiring implementation |
| **`final class`** | Prevents inheritance |
| **`final method`** | Prevents overriding |
| **Constructor Chaining** | Parent constructor called before child |
| **LSP** | Subtypes must be substitutable for base types |
| **Composition > Inheritance** | Prefer "has-a" over "is-a" for reuse |
