# Polymorphism in Java

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

Polymorphism is one of the four pillars of object-oriented programming, derived from the Greek words "poly" (many) and "morph" (forms). It is the ability of objects to take on many forms — the same reference type can refer to different actual types, and the same method call can produce different behaviors depending on the object's runtime type. Java supports two forms of polymorphism: compile-time polymorphism (method overloading) resolved at compile time, and runtime polymorphism (method overriding) resolved at runtime through dynamic dispatch. Polymorphism enables flexible, extensible code that can work with new types without modification, and is the foundation for design patterns, framework APIs, and the SOLID principles.

---

## Learning Objectives

- Understand the difference between compile-time and runtime polymorphism
- Master method overriding and the rules that govern it
- Learn how dynamic dispatch works in the JVM
- Apply polymorphic thinking to write extensible, maintainable code

---

## Prerequisites

- [01-classes-and-objects/README.md](../01-classes-and-objects/README.md) — Classes, objects, methods
- [03-methods/README.md](../03-methods/README.md) — Method overloading basics
- [09-inheritance/README.md](../09-inheritance/README.md) — Inheritance, extends, method overriding

---

## Why This Concept Exists

### The Problem

Without polymorphism, code must check types explicitly and call specific methods:

```java
public class NotificationSender {
    public void sendEmail(EmailNotification email) { /* ... */ }
    public void sendSms(SmsNotification sms) { /* ... */ }
    public void sendPush(PushNotification push) { /* ... */ }
}

// Every new notification type requires modifying this class
```

This violates the Open/Closed Principle — the class must be modified every time a new notification type is added.

### The Solution

Polymorphism allows the same method call to work with any notification type:

```java
public class NotificationSender {
    public void send(Notification notification) {
        notification.send(); // Same call, different behavior
    }
}

// New types can be added without modifying NotificationSender
```

### Real-World Analogy

Think of a remote control. The same "power" button works on a TV, a stereo, or a DVD player. You don't need to know the specific device type — you just press the button, and each device responds according to its own implementation. Polymorphism works the same way: the same method call triggers different behavior based on the actual object type.

---

## Internal Working

### Compile-Time Polymorphism (Method Overloading)

Method overloading is resolved at compile time through a process called static binding (or early binding). The compiler matches the method signature (name + parameter types) to determine which method to call.

```java
class Calculator {
    int add(int a, int b) { return a + b; }
    double add(double a, double b) { return a + b; }
    int add(int a, int b, int c) { return a + b + c; }
}

// Compiler determines:
calculator.add(1, 2)       // Calls int add(int, int)
calculator.add(1.0, 2.0)   // Calls double add(double, double)
calculator.add(1, 2, 3)    // Calls int add(int, int, int)
```

### Runtime Polymorphism (Method Overriding)

Method overriding uses dynamic dispatch (or late binding). The JVM determines which method to call at runtime based on the actual object type, not the reference type.

```java
class Animal { void speak() { } }
class Dog extends Animal { void speak() { System.out.println("Woof"); } }
class Cat extends Animal { void speak() { System.out.println("Meow"); } }

Animal a = new Dog(); // Reference type: Animal, Actual type: Dog
a.speak();            // JVM calls Dog.speak() — dynamic dispatch
```

### The vtable (Virtual Method Table)

The JVM maintains a virtual method table (vtable) for each class that has overridden methods. When an overridden method is called, the JVM looks up the method in the vtable of the actual object's class.

```
Animal vtable:           Dog vtable:              Cat vtable:
┌──────────────────┐    ┌──────────────────┐    ┌──────────────────┐
│ Object.toString  │    │ Object.toString  │    │ Object.toString  │
│ Object.hashCode  │    │ Object.hashCode  │    │ Object.hashCode  │
│ Animal.speak     │──→ │ Dog.speak        │    │ Cat.speak        │
└──────────────────┘    └──────────────────┘    └──────────────────┘
```

### Bytecode for Dynamic Dispatch

```bytecode
aload_1                // Load object reference (actual type: Dog)
invokevirtual #N       // Call method — resolved at runtime via vtable
```

The `invokevirtual` instruction uses the object's actual class to look up the method, not the reference's declared type.

---

## Syntax

### 1. Method Overloading (Compile-Time)

```java
class Calculator {
    int add(int a, int b) { return a + b; }
    double add(double a, double b) { return a + b; }
}
```

### 2. Method Overriding (Runtime)

```java
class Animal {
    void speak() { System.out.println("..."); }
}

class Dog extends Animal {
    @Override
    void speak() { System.out.println("Woof!"); }
}
```

### 3. Upcasting (Implicit)

```java
Animal a = new Dog(); // Dog → Animal (automatic)
```

### 4. Downcasting (Explicit)

```java
Animal a = new Dog();
Dog d = (Dog) a; // Animal → Dog (requires cast)
```

### 5. Polymorphic Arrays

```java
Animal[] animals = { new Dog(), new Cat(), new Bird() };
for (Animal a : animals) {
    a.speak(); // Each calls its own version
}
```

---

## Easy Examples

### Example 1: Method Overloading

**Problem Statement:**
Create a `Printer` class with overloaded `print()` methods that can handle different data types (String, int, double, Object).

**Implementation:**

```java
package academy.javaengineering.oop.polymorphism;

public class Printer {

    public void print(String message) {
        System.out.println("[TEXT] " + message);
    }

    public void print(int number) {
        System.out.println("[INT] " + number);
    }

    public void print(double number) {
        System.out.printf("[DOUBLE] %.2f%n", number);
    }

    public void print(boolean flag) {
        System.out.println("[BOOL] " + (flag ? "YES" : "NO"));
    }

    public void print(String label, String value) {
        System.out.println("[LABEL] " + label + ": " + value);
    }

    public void print(Object obj) {
        System.out.println("[OBJECT] " + obj.getClass().getSimpleName() + " → " + obj);
    }

    public static void main(String[] args) {
        Printer printer = new Printer();

        printer.print("Hello, World!");
        printer.print(42);
        printer.print(3.14159);
        printer.print(true);
        printer.print("Name", "Alice");
        printer.print(new int[]{1, 2, 3});

        System.out.println("\n=== Varargs Overloading ===");
        printer.print("Single arg");
        printer.print("Arg1", "Arg2");
    }
}
```

**Output:**
```
[TEXT] Hello, World!
[INT] 42
[DOUBLE] 3.14
[BOOL] YES
[LABEL] Name: Alice
[OBJECT] [I → [I@1a2b3c4d

=== Varargs Overloading ===
[TEXT] Single arg
[LABEL] Arg1: Arg2
```

**Best Practices:**
- Overload methods with similar semantics (different parameter types for the same operation)
- Avoid overloading with varargs as it can create ambiguity
- Use descriptive method names when overloading isn't intuitive

---

### Example 2: Method Overriding

**Problem Statement:**
Create a shape hierarchy where each shape provides its own implementation of `draw()` and `getArea()`.

**Implementation:**

```java
package academy.javaengineering.oop.polymorphism;

public class Shape {
    protected String color;

    public Shape(String color) {
        this.color = color;
    }

    public void draw() {
        System.out.println("Drawing a generic " + color + " shape");
    }

    public double getArea() {
        return 0;
    }

    public String getDescription() {
        return color + " shape with area " + String.format("%.2f", getArea());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + getDescription() + "]";
    }
}

class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a " + color + " circle with radius " + radius);
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    private double width, height;

    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a " + color + " rectangle " + width + "x" + height);
    }

    @Override
    public double getArea() {
        return width * height;
    }
}

class Triangle extends Shape {
    private double base, height;

    public Triangle(String color, double base, double height) {
        super(color);
        this.base = base;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a " + color + " triangle with base " + base + " and height " + height);
    }

    @Override
    public double getArea() {
        return 0.5 * base * height;
    }
}

class OverridingDemo {
    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle("Red", 5),
            new Rectangle("Blue", 4, 6),
            new Triangle("Green", 3, 8)
        };

        System.out.println("=== Drawing Shapes (Polymorphism) ===");
        for (Shape shape : shapes) {
            shape.draw(); // Each calls its own draw()
        }

        System.out.println("\n=== Shape Descriptions ===");
        for (Shape shape : shapes) {
            System.out.println(shape);
        }

        System.out.println("\n=== Total Area ===");
        double total = 0;
        for (Shape shape : shapes) {
            total += shape.getArea();
        }
        System.out.printf("Total area: %.2f%n", total);
    }
}
```

**Output:**
```
=== Drawing Shapes (Polymorphism) ===
Drawing a Red circle with radius 5.0
Drawing a Blue rectangle 4.0x6.0
Drawing a Green triangle with base 3.0 and height 8.0

=== Shape Descriptions ===
Circle[Red shape with area 78.54]
Rectangle[Blue shape with area 24.00]
Triangle[Green shape with area 12.00]

=== Total Area ===
Total area: 114.54
```

**Best Practices:**
- Use `@Override` annotation to catch signature mismatches
- Ensure overriding methods maintain the contract of the parent method
- Use polymorphic collections to process heterogeneous objects uniformly

---

### Example 3: Polymorphic Method Calls

**Problem Statement:**
Demonstrate how the same method call produces different results based on the actual object type.

**Implementation:**

```java
package academy.javaengineering.oop.polymorphism;

public class PaymentProcessor {

    public void processPayment(PaymentMethod method) {
        System.out.print("Processing payment via " + method.getMethodType() + ": ");
        method.charge();
        method.sendReceipt();
    }

    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();

        PaymentMethod[] methods = {
            new CreditCard("4111-1111-1111-1111"),
            new PayPal("user@example.com"),
            new BankTransfer("ACC-12345")
        };

        for (PaymentMethod method : methods) {
            processor.processPayment(method);
            System.out.println();
        }
    }
}

class PaymentMethod {
    protected String identifier;

    PaymentMethod(String identifier) {
        this.identifier = identifier;
    }

    public String getMethodType() {
        return "Generic";
    }

    public void charge() {
        System.out.println("Charging...");
    }

    public void sendReceipt() {
        System.out.println("Receipt sent");
    }
}

class CreditCard extends PaymentMethod {
    CreditCard(String number) {
        super(number);
    }

    @Override
    public String getMethodType() {
        return "Credit Card";
    }

    @Override
    public void charge() {
        System.out.println("Charging credit card " + maskNumber(identifier));
    }

    @Override
    public void sendReceipt() {
        System.out.println("Email receipt for credit card payment");
    }

    private String maskNumber(String number) {
        return "****-****-****-" + number.substring(number.length() - 4);
    }
}

class PayPal extends PaymentMethod {
    PayPal(String email) {
        super(email);
    }

    @Override
    public String getMethodType() {
        return "PayPal";
    }

    @Override
    public void charge() {
        System.out.println("Charging PayPal account: " + identifier);
    }

    @Override
    public void sendReceipt() {
        System.out.println("PayPal notification sent to " + identifier);
    }
}

class BankTransfer extends PaymentMethod {
    BankTransfer(String accountId) {
        super(accountId);
    }

    @Override
    public String getMethodType() {
        return "Bank Transfer";
    }

    @Override
    public void charge() {
        System.out.println("Initiating bank transfer from account: " + identifier);
    }

    @Override
    public void sendReceipt() {
        System.out.println("Bank transfer confirmation emailed");
    }
}
```

**Output:**
```
Processing payment via Credit Card: 
Charging credit card ****-****-****-1111
Email receipt for credit card payment

Processing payment via PayPal: 
Charging PayPal account: user@example.com
PayPal notification sent to user@example.com

Processing payment via Bank Transfer: 
Initiating bank transfer from account: ACC-12345
Bank transfer confirmation emailed
```

**Best Practices:**
- Program to interfaces (PaymentMethod), not implementations
- Let the JVM handle dispatch through dynamic binding
- Add new payment types without modifying PaymentProcessor

---

## Medium Examples

### Example 1: Polymorphic Collections with Filtering

**Problem Statement:**
Create an inventory system that stores different product types in a single collection and provides polymorphic operations.

**Requirements:**
- Store `Product`, `Electronics`, `Clothing`, and `Food` items
- Calculate total inventory value
- Apply category-specific discounts
- Filter and display products by type

**Implementation:**

```java
package academy.javaengineering.oop.polymorphism;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Product {
    protected String id;
    protected String name;
    protected double price;
    protected int quantity;

    public Product(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public abstract String getCategory();
    public abstract double getDiscountedPrice();
    public abstract String getDetails();

    public double getTotalValue() {
        return getDiscountedPrice() * quantity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    @Override
    public String toString() {
        return String.format("[%s] %s - $%.2f x %d = $%.2f (%s)",
            id, name, getDiscountedPrice(), quantity, getTotalValue(), getCategory());
    }
}

class Electronics extends Product {
    private String brand;
    private int warrantyMonths;

    public Electronics(String id, String name, double price, int quantity,
                       String brand, int warrantyMonths) {
        super(id, name, price, quantity);
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getCategory() { return "Electronics"; }

    @Override
    public double getDiscountedPrice() {
        return price * 0.9; // 10% electronics discount
    }

    @Override
    public String getDetails() {
        return String.format("%s by %s (%d-month warranty)", name, brand, warrantyMonths);
    }

    public String getBrand() { return brand; }
    public int getWarrantyMonths() { return warrantyMonths; }
}

class Clothing extends Product {
    private String size;
    private String material;

    public Clothing(String id, String name, double price, int quantity,
                    String size, String material) {
        super(id, name, price, quantity);
        this.size = size;
        this.material = material;
    }

    @Override
    public String getCategory() { return "Clothing"; }

    @Override
    public double getDiscountedPrice() {
        return price * 0.85; // 15% clothing discount
    }

    @Override
    public String getDetails() {
        return String.format("%s (Size: %s, Material: %s)", name, size, material);
    }

    public String getSize() { return size; }
    public String getMaterial() { return material; }
}

class FoodItem extends Product {
    private Date expirationDate;
    private boolean perishable;

    public FoodItem(String id, String name, double price, int quantity,
                    Date expirationDate, boolean perishable) {
        super(id, name, price, quantity);
        this.expirationDate = expirationDate;
        this.perishable = perishable;
    }

    @Override
    public String getCategory() { return "Food"; }

    @Override
    public double getDiscountedPrice() {
        long daysUntilExpiry = (expirationDate.getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
        if (daysUntilExpiry < 3) return price * 0.5; // 50% off near expiry
        if (daysUntilExpiry < 7) return price * 0.8; // 20% off
        return price;
    }

    @Override
    public String getDetails() {
        return String.format("%s (Expires: %s, Perishable: %s)",
            name, expirationDate, perishable ? "Yes" : "No");
    }

    public Date getExpirationDate() { return expirationDate; }
    public boolean isPerishable() { return perishable; }
}

class Inventory {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public double calculateTotalValue() {
        return products.stream()
            .mapToDouble(Product::getTotalValue)
            .sum();
    }

    public Map<String, Double> getValueByCategory() {
        return products.stream()
            .collect(Collectors.groupingBy(
                Product::getCategory,
                Collectors.summingDouble(Product::getTotalValue)
            ));
    }

    public List<Product> getProductsByCategory(String category) {
        return products.stream()
            .filter(p -> p.getCategory().equals(category))
            .collect(Collectors.toList());
    }

    public void applyCategoryDiscount(String category, double discountPercent) {
        // This demonstrates compile-time polymorphism with overloaded method
        products.stream()
            .filter(p -> p.getCategory().equals(category))
            .forEach(p -> System.out.printf("Applied %.0f%% discount to %s%n",
                discountPercent, p.getName()));
    }

    public void displayInventory() {
        System.out.println("\n=== INVENTORY ===");
        products.forEach(System.out::println);
        System.out.printf("Total Value: $%.2f%n", calculateTotalValue());
    }

    public static void main(String[] args) {
        Inventory inventory = new Inventory();

        inventory.addProduct(new Electronics("E001", "Laptop", 999.99, 10, "Dell", 24));
        inventory.addProduct(new Electronics("E002", "Phone", 699.99, 25, "Apple", 12));
        inventory.addProduct(new Clothing("C001", "T-Shirt", 29.99, 100, "M", "Cotton"));
        inventory.addProduct(new Clothing("C002", "Jeans", 59.99, 50, "32", "Denim"));
        inventory.addProduct(new FoodItem("F001", "Milk", 3.99, 30,
            new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000L), true));
        inventory.addProduct(new FoodItem("F002", "Canned Beans", 1.99, 200,
            new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L), false));

        inventory.displayInventory();

        System.out.println("\n=== VALUE BY CATEGORY ===");
        inventory.getValueByCategory().forEach((cat, value) ->
            System.out.printf("  %s: $%.2f%n", cat, value));

        System.out.println("\n=== ELECTRONICS ===");
        inventory.getProductsByCategory("Electronics").forEach(System.out::println);
    }
}
```

**Output:**
```
=== INVENTORY ===
[E001] Laptop - $899.99 x 10 = $8999.90 (Electronics)
[E002] Phone - $629.99 x 25 = $15749.75 (Electronics)
[C001] T-Shirt - $25.49 x 100 = $2549.00 (Clothing)
[C002] Jeans - $50.99 x 50 = $2549.50 (Clothing)
[F001] Milk - $1.99 x 30 = $59.70 (Food)
[F002] Canned Beans - $1.99 x 200 = $398.00 (Food)
Total Value: $30305.85

=== VALUE BY CATEGORY ===
  Electronics: $24749.65
  Clothing: $5098.50
  Food: $457.70

=== ELECTRONICS ===
[E001] Laptop - $899.99 x 10 = $8999.90 (Electronics)
[E002] Phone - $629.99 x 25 = $15749.75 (Electronics)
```

**Alternative:**
Use the Strategy pattern for discount calculation instead of embedding it in each product class.

---

### Example 2: Compile-Time Polymorphism with Method Overloading

**Problem Statement:**
Create a `StringUtils` utility class that demonstrates different flavors of method overloading.

**Requirements:**
- Overload by number of parameters
- Overload by parameter types
- Overload with varargs
- Handle edge cases gracefully

**Implementation:**

```java
package academy.javaengineering.oop.polymorphism;

public class StringUtils {

    // Overloading by number of parameters
    public String repeat(String s) {
        return repeat(s, 2);
    }

    public String repeat(String s, int count) {
        if (s == null || count < 0) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    // Overloading by parameter type
    public String join(String separator, String s1, String s2) {
        return s1 + separator + s2;
    }

    public String join(String separator, int i1, int i2) {
        return i1 + separator + i2;
    }

    public String join(String separator, double d1, double d2) {
        return String.format("%.2f%s%.2f", d1, separator, d2);
    }

    // Varargs overloading
    public String join(String separator, String... parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < parts.length - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public String join(String separator, int... numbers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numbers.length; i++) {
            sb.append(numbers[i]);
            if (i < numbers.length - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    // Overloading with different parameter order
    public String pad(String s, int length, char padChar) {
        if (s == null) s = "";
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < length) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    public String pad(int number, int length, char padChar) {
        return pad(String.valueOf(number), length, padChar);
    }

    // Null-safe overloading
    public String defaultIfNull(String s) {
        return defaultIfNull(s, "");
    }

    public String defaultIfNull(String s, String defaultValue) {
        return s != null ? s : defaultValue;
    }

    public static void main(String[] args) {
        StringUtils utils = new StringUtils();

        System.out.println("=== Repeat Overloading ===");
        System.out.println("repeat(\"ha\"): " + utils.repeat("ha"));
        System.out.println("repeat(\"ha\", 3): " + utils.repeat("ha", 3));

        System.out.println("\n=== Join by Type ===");
        System.out.println("join(\" + \", \"a\", \"b\"): " + utils.join(" + ", "a", "b"));
        System.out.println("join(\" + \", 1, 2): " + utils.join(" + ", 1, 2));
        System.out.println("join(\" + \", 1.5, 2.5): " + utils.join(" + ", 1.5, 2.5));

        System.out.println("\n=== Join with Varargs ===");
        System.out.println("join(\",\", \"a\", \"b\", \"c\"): " + utils.join(",", "a", "b", "c"));
        System.out.println("join(\",\", 1, 2, 3): " + utils.join(",", 1, 2, 3));

        System.out.println("\n=== Pad Overloading ===");
        System.out.println("pad(\"hi\", 5, '0'): " + utils.pad("hi", 5, '0'));
        System.out.println("pad(42, 5, '0'): " + utils.pad(42, 5, '0'));

        System.out.println("\n=== Null Safe ===");
        System.out.println("defaultIfNull(null): [" + utils.defaultIfNull(null) + "]");
        System.out.println("defaultIfNull(\"hello\"): [" + utils.defaultIfNull("hello") + "]");
        System.out.println("defaultIfNull(null, \"N/A\"): [" + utils.defaultIfNull(null, "N/A") + "]");
    }
}
```

**Output:**
```
=== Repeat Overloading ===
repeat("ha"): haha
repeat("ha", 3): hahaha

=== Join by Type ===
join(" + ", "a", "b"): a + b
join(" + ", 1, 2): 1 + 2
join(" + ", 1.5, 2.5): 1.50 + 2.50

=== Join with Varargs ===
join(",", "a", "b", "c"): a,b,c
join(",", 1, 2, 3): 1,2,3

=== Pad Overloading ===
pad("hi", 5, '0'): hi000
pad(42, 5, '0'): 00042

=== Null Safe ===
defaultIfNull(null): []
defaultIfNull("hello"): [hello]
defaultIfNull(null, "N/A"): [N/A]
```

**Alternative:**
Use `@SafeVarargs` annotation for varargs methods that don't create heap pollution.

---

### Example 3: Interface Polymorphism

**Problem Statement:**
Demonstrate how interfaces enable polymorphism across unrelated class hierarchies.

**Implementation:**

```java
package academy.javaengineering.oop.polymorphism;

import java.util.*;

public interface Playable {
    String getTitle();
    int getDuration(); // seconds
    void play();
    void pause();
    void stop();

    default String getFormattedDuration() {
        int minutes = getDuration() / 60;
        int seconds = getDuration() % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}

interface Streamable extends Playable {
    void addToQueue();
    void share(String platform);
}

interface Downloadable extends Playable {
    void download(String path);
    boolean isDownloaded();
}

class Song implements Streamable {
    private final String title;
    private final String artist;
    private final int duration;
    private boolean playing;

    public Song(String title, String artist, int duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.playing = false;
    }

    @Override public String getTitle() { return title + " - " + artist; }
    @Override public int getDuration() { return duration; }

    @Override
    public void play() {
        playing = true;
        System.out.println("♫ Playing: " + getTitle());
    }

    @Override
    public void pause() {
        playing = false;
        System.out.println("⏸ Paused: " + getTitle());
    }

    @Override
    public void stop() {
        playing = false;
        System.out.println("⏹ Stopped: " + getTitle());
    }

    @Override
    public void addToQueue() {
        System.out.println("Added to queue: " + getTitle());
    }

    @Override
    public void share(String platform) {
        System.out.println("Sharing '" + getTitle() + "' on " + platform);
    }
}

class Podcast implements Streamable {
    private final String title;
    private final String host;
    private final int duration;
    private boolean playing;

    public Podcast(String title, String host, int duration) {
        this.title = title;
        this.host = host;
        this.duration = duration;
        this.playing = false;
    }

    @Override public String getTitle() { return title + " with " + host; }
    @Override public int getDuration() { return duration; }

    @Override
    public void play() {
        playing = true;
        System.out.println("▶ Playing podcast: " + getTitle());
    }

    @Override
    public void pause() { playing = false; System.out.println("⏸ Paused podcast"); }
    @Override
    public void stop() { playing = false; System.out.println("⏹ Stopped podcast"); }
    @Override
    public void addToQueue() { System.out.println("Podcast added to queue"); }
    @Override
    public void share(String platform) { System.out.println("Sharing podcast on " + platform); }
}

class Audiobook implements Downloadable {
    private final String title;
    private final String narrator;
    private final int duration;
    private boolean downloaded;

    public Audiobook(String title, String narrator, int duration) {
        this.title = title;
        this.narrator = narrator;
        this.duration = duration;
        this.downloaded = false;
    }

    @Override public String getTitle() { return title + " by " + narrator; }
    @Override public int getDuration() { return duration; }
    @Override public boolean isDownloaded() { return downloaded; }

    @Override
    public void play() {
        System.out.println("📖 Listening to: " + getTitle());
    }

    @Override
    public void pause() { System.out.println("⏸ Paused audiobook"); }
    @Override
    public void stop() { System.out.println("⏹ Stopped audiobook"); }

    @Override
    public void download(String path) {
        downloaded = true;
        System.out.println("Downloaded to " + path + ": " + getTitle());
    }
}

class MediaPlayer {
    public void playAll(Playable... items) {
        System.out.println("=== Playing All Media ===");
        int totalDuration = 0;
        for (Playable item : items) {
            item.play();
            totalDuration += item.getDuration();
            System.out.printf("  Duration: %s%n", item.getFormattedDuration());
        }
        System.out.printf("Total duration: %d:%02d%n",
            totalDuration / 60, totalDuration % 60);
    }

    public void shareAll(String platform, Streamable... items) {
        System.out.println("\n=== Sharing on " + platform + " ===");
        for (Streamable item : items) {
            item.share(platform);
        }
    }

    public static void main(String[] args) {
        MediaPlayer player = new MediaPlayer();

        Song song1 = new Song("Bohemian Rhapsody", "Queen", 354);
        Song song2 = new Song("Hotel California", "Eagles", 391);
        Podcast podcast = new Podcast("Tech Talk", "John Doe", 2400);
        Audiobook audiobook = new Audiobook("Clean Code", "Robert Martin", 36000);

        // Interface polymorphism — same method, different types
        player.playAll(song1, song2, podcast, audiobook);

        // Streamable-specific operations
        player.shareAll("Twitter", song1, podcast);

        // Downloadable-specific operations
        System.out.println("\n=== Downloads ===");
        audiobook.download("/downloads/audiobooks/");
        System.out.println("Downloaded? " + audiobook.isDownloaded());

        // Polymorphic collection
        System.out.println("\n=== All Playable Items ===");
        List<Playable> allItems = Arrays.asList(song1, song2, podcast, audiobook);
        allItems.forEach(item ->
            System.out.printf("  %s [%s]%n", item.getTitle(), item.getFormattedDuration()));
    }
}
```

**Output:**
```
=== Playing All Media ===
♫ Playing: Bohemian Rhapsody - Queen
  Duration: 5:54
♫ Playing: Hotel California - Eagles
  Duration: 6:31
▶ Playing podcast: Tech Talk with John Doe
  Duration: 40:00
📖 Listening to: Clean Code by Robert Martin
  Duration: 600:00
Total duration: 652:25

=== Sharing on Twitter ===
Sharing 'Bohemian Rhapsody - Queen' on Twitter
Sharing podcast on Twitter

=== Downloads ===
Downloaded to /downloads/audiobooks/: Clean Code by Robert Martin
Downloaded? true

=== All Playable Items ===
  Bohemian Rhapsody - Queen [5:54]
  Hotel California - Eagles [6:31]
  Tech Talk with John Doe [40:00]
  Clean Code by Robert Martin [600:00]
```

**Alternative:**
Use abstract classes for shared state and interfaces for shared behavior (the "abstract class + interface" combination pattern).

---

## Hard Examples

### Example 1: Dynamic Method Dispatch with Type Hierarchies

**Architecture:**
A logging framework that demonstrates complex polymorphic dispatch, including covariance, abstract methods, and the strategy pattern.

**Implementation:**

```java
package academy.javaengineering.oop.polymorphism;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class Logger {
    protected final String name;
    protected LogLevel minLevel;
    protected final List<LogEntry> entries;

    protected Logger(String name, LogLevel minLevel) {
        this.name = name;
        this.minLevel = minLevel;
        this.entries = new ArrayList<>();
    }

    public abstract void writeLog(LogEntry entry);

    public void log(LogLevel level, String message) {
        if (level.ordinal() >= minLevel.ordinal()) {
            LogEntry entry = new LogEntry(level, name, message, LocalDateTime.now());
            entries.add(entry);
            writeLog(entry);
        }
    }

    public void info(String message) { log(LogLevel.INFO, message); }
    public void warn(String message) { log(LogLevel.WARN, message); }
    public void error(String message) { log(LogLevel.ERROR, message); }

    public abstract String getFormatter(); // Covariant return type

    public List<LogEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public String getName() { return name; }
    public LogLevel getMinLevel() { return minLevel; }
    public void setMinLevel(LogLevel level) { this.minLevel = level; }
}

enum LogLevel {
    DEBUG, INFO, WARN, ERROR;

    public String getFormatted() {
        return String.format("[%s]", name());
    }
}

class LogEntry {
    private final LogLevel level;
    private final String loggerName;
    private final String message;
    private final LocalDateTime timestamp;

    public LogEntry(LogLevel level, String loggerName, String message, LocalDateTime timestamp) {
        this.level = level;
        this.loggerName = loggerName;
        this.message = message;
        this.timestamp = timestamp;
    }

    public LogLevel getLevel() { return level; }
    public String getLoggerName() { return loggerName; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

// Concrete loggers with different behaviors
class ConsoleLogger extends Logger {
    private final boolean useColors;

    public ConsoleLogger(String name, boolean useColors) {
        super(name, LogLevel.INFO);
        this.useColors = useColors;
    }

    @Override
    public void writeLog(LogEntry entry) {
        String formatted = formatEntry(entry);
        if (useColors) {
            formatted = colorize(formatted, entry.getLevel());
        }
        System.out.println(formatted);
    }

    private String formatEntry(LogEntry entry) {
        return String.format("%s %s [%s] %s",
            entry.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            entry.getLevel().getFormatted(),
            entry.getLoggerName(),
            entry.getMessage());
    }

    private String colorize(String text, LogLevel level) {
        String color = switch (level) {
            case DEBUG -> "\033[37m";  // White
            case INFO -> "\033[36m";   // Cyan
            case WARN -> "\033[33m";   // Yellow
            case ERROR -> "\033[31m";  // Red
        };
        return color + text + "\033[0m";
    }

    @Override
    public String getFormatter() { return "console-plain"; }
}

class FileLogger extends Logger {
    private final List<String> fileBuffer;
    private final String filePath;

    public FileLogger(String name, String filePath) {
        super(name, LogLevel.DEBUG);
        this.filePath = filePath;
        this.fileBuffer = new ArrayList<>();
    }

    @Override
    public void writeLog(LogEntry entry) {
        String line = String.format("%s|%s|%s|%s",
            entry.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            entry.getLevel(),
            entry.getLoggerName(),
            entry.getMessage());
        fileBuffer.add(line);
        // In production: write to actual file
    }

    public void flush() {
        System.out.println("Flushing " + fileBuffer.size() + " entries to " + filePath);
        fileBuffer.clear();
    }

    @Override
    public String getFormatter() { return "file-csv"; }

    public List<String> getFileBuffer() {
        return Collections.unmodifiableList(fileBuffer);
    }
}

class CompositeLogger extends Logger {
    private final List<Logger> loggers;

    public CompositeLogger(String name) {
        super(name, LogLevel.DEBUG);
        this.loggers = new ArrayList<>();
    }

    public void addLogger(Logger logger) {
        loggers.add(logger);
    }

    @Override
    public void writeLog(LogEntry entry) {
        for (Logger logger : loggers) {
            logger.writeLog(entry); // Polymorphic dispatch
        }
    }

    @Override
    public String getFormatter() { return "composite"; }

    @Override
    public List<LogEntry> getEntries() {
        List<LogEntry> all = new ArrayList<>();
        for (Logger logger : loggers) {
            all.addAll(logger.getEntries());
        }
        return Collections.unmodifiableList(all);
    }
}

class LoggerDemo {
    public static void main(String[] args) {
        // Create different loggers
        ConsoleLogger console = new ConsoleLogger("Console", true);
        FileLogger file = new FileLogger("File", "/var/log/app.log");

        // Composite logger dispatches to both
        CompositeLogger composite = new CompositeLogger("App");
        composite.addLogger(console);
        composite.addLogger(file);

        // Polymorphic method calls
        System.out.println("=== Direct Console Logger ===");
        console.info("Application started");
        console.warn("High memory usage");
        console.error("Connection failed");

        System.out.println("\n=== Direct File Logger ===");
        file.debug("Debug details");
        file.info("Processing request");
        file.flush();

        System.out.println("\n=== Composite Logger (Dispatches to All) ===");
        composite.info("User logged in");
        composite.warn("Rate limit approaching");

        // Polymorphic array
        System.out.println("\n=== Polymorphic Array Processing ===");
        Logger[] loggers = { console, file, composite };
        for (Logger logger : loggers) {
            System.out.printf("Logger: %s, Format: %s, Entries: %d%n",
                logger.getName(), logger.getFormatter(), logger.getEntries().size());
        }
    }
}
```

**Unit Tests:**

```java
package academy.javaengineering.oop.polymorphism;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class LoggerTest {

    private ConsoleLogger consoleLogger;
    private FileLogger fileLogger;

    @Before
    public void setUp() {
        consoleLogger = new ConsoleLogger("TestConsole", false);
        fileLogger = new FileLogger("TestFile", "/tmp/test.log");
    }

    @Test
    public void testConsoleLoggerCapturesEntries() {
        consoleLogger.info("Test message");
        assertEquals(1, consoleLogger.getEntries().size());
        assertEquals(LogLevel.INFO, consoleLogger.getEntries().get(0).getLevel());
    }

    @Test
    public void testFileLoggerCapturesEntries() {
        fileLogger.debug("Debug message");
        assertEquals(1, fileLogger.getEntries().size());
    }

    @Test
    public void testCompositeDispatchesToAll() {
        CompositeLogger composite = new CompositeLogger("TestComposite");
        composite.addLogger(consoleLogger);
        composite.addLogger(fileLogger);

        composite.info("Composite message");

        assertEquals(1, consoleLogger.getEntries().size());
        assertEquals(1, fileLogger.getEntries().size());
    }

    @Test
    public void testMinLevelFiltering() {
        consoleLogger.setMinLevel(LogLevel.WARN);
        consoleLogger.info("Should be filtered");
        consoleLogger.warn("Should be logged");

        assertEquals(1, consoleLogger.getEntries().size());
        assertEquals(LogLevel.WARN, consoleLogger.getEntries().get(0).getLevel());
    }
}
```

**Complexity:**
- `log()`: O(1) per logger, O(n) for composite (n = number of loggers)
- `writeLog()`: O(1) for console, O(1) amortized for file (buffer), O(n) for composite
- Space: O(m) per logger where m is number of entries

**Best Practices:**
- Use abstract classes for partial implementations, interfaces for contracts
- Composite pattern enables treating individual and composite objects uniformly
- Covariant return types allow subclasses to return more specific types

---

### Example 2: Strategy Pattern with Polymorphism

**Architecture:**
An e-commerce discount system where discount strategies are polymorphic and can be swapped at runtime.

**Implementation:**

```java
package academy.javaengineering.oop.polymorphism;

import java.util.*;

public interface DiscountStrategy {
    double calculateDiscount(double price, ShoppingCart cart);
    String getDescription();
}

class NoDiscount implements DiscountStrategy {
    @Override
    public double calculateDiscount(double price, ShoppingCart cart) {
        return 0;
    }

    @Override
    public String getDescription() { return "No discount"; }
}

class PercentageDiscount implements DiscountStrategy {
    private final double percentage;

    PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double calculateDiscount(double price, ShoppingCart cart) {
        return price * (percentage / 100);
    }

    @Override
    public String getDescription() {
        return percentage + "% off";
    }
}

class FixedAmountDiscount implements DiscountStrategy {
    private final double amount;
    private final double minimumPurchase;

    FixedAmountDiscount(double amount, double minimumPurchase) {
        this.amount = amount;
        this.minimumPurchase = minimumPurchase;
    }

    @Override
    public double calculateDiscount(double price, ShoppingCart cart) {
        if (price >= minimumPurchase) {
            return Math.min(amount, price);
        }
        return 0;
    }

    @Override
    public String getDescription() {
        return "$" + String.format("%.2f", amount) + " off (min purchase: $" +
               String.format("%.2f", minimumPurchase) + ")";
    }
}

class BuyOneGetOneFree implements DiscountStrategy {
    @Override
    public double calculateDiscount(double price, ShoppingCart cart) {
        long itemCount = cart.getItems().stream()
            .filter(item -> item.getPrice() > 0)
            .count();
        if (itemCount >= 2) {
            double cheapest = cart.getItems().stream()
                .mapToDouble(CartItem::getPrice)
                .min()
                .orElse(0);
            return cheapest;
        }
        return 0;
    }

    @Override
    public String getDescription() { return "Buy one, get one free"; }
}

class TieredDiscount implements DiscountStrategy {
    private final double[] thresholds;
    private final double[] percentages;

    TieredDiscount(double[] thresholds, double[] percentages) {
        this.thresholds = thresholds;
        this.percentages = percentages;
    }

    @Override
    public double calculateDiscount(double price, ShoppingCart cart) {
        for (int i = thresholds.length - 1; i >= 0; i--) {
            if (price >= thresholds[i]) {
                return price * (percentages[i] / 100);
            }
        }
        return 0;
    }

    @Override
    public String getDescription() { return "Tiered discount"; }
}

class ShoppingCart {
    private final List<CartItem> items;
    private DiscountStrategy discountStrategy;

    public ShoppingCart() {
        this.items = new ArrayList<>();
        this.discountStrategy = new NoDiscount();
    }

    public void addItem(String name, double price, int quantity) {
        items.add(new CartItem(name, price, quantity));
    }

    public void setDiscountStrategy(DiscountStrategy strategy) {
        this.discountStrategy = strategy;
    }

    public double getSubtotal() {
        return items.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
    }

    public double getDiscount() {
        return discountStrategy.calculateDiscount(getSubtotal(), this);
    }

    public double getTotal() {
        return getSubtotal() - getDiscount();
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void display() {
        System.out.println("=== Shopping Cart ===");
        items.forEach(item ->
            System.out.printf("  %s: $%.2f x %d = $%.2f%n",
                item.getName(), item.getPrice(), item.getQuantity(),
                item.getPrice() * item.getQuantity()));
        System.out.printf("  Subtotal: $%.2f%n", getSubtotal());
        System.out.printf("  Discount: -$%.2f (%s)%n", getDiscount(), discountStrategy.getDescription());
        System.out.printf("  Total: $%.2f%n", getTotal());
    }
}

class CartItem {
    private final String name;
    private final double price;
    private final int quantity;

    CartItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}

class DiscountStrategyDemo {
    public static void main(String[] args) {
        // Test different discount strategies with the same cart
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Laptop", 999.99, 1);
        cart.addItem("Mouse", 29.99, 2);
        cart.addItem("Keyboard", 79.99, 1);

        DiscountStrategy[] strategies = {
            new NoDiscount(),
            new PercentageDiscount(10),
            new FixedAmountDiscount(50, 100),
            new BuyOneGetOneFree(),
            new TieredDiscount(
                new double[]{0, 500, 1000},
                new double[]{0, 5, 15}
            )
        };

        for (DiscountStrategy strategy : strategies) {
            cart.setDiscountStrategy(strategy);
            cart.display();
            System.out.println();
        }

        // Runtime strategy switching
        System.out.println("=== Runtime Strategy Switching ===");
        cart.setDiscountStrategy(new PercentageDiscount(20));
        System.out.printf("With 20%% off: $%.2f%n", cart.getTotal());

        cart.setDiscountStrategy(new FixedAmountDiscount(100, 500));
        System.out.printf("With $100 off: $%.2f%n", cart.getTotal());
    }
}
```

**Unit Tests:**

```java
package academy.javaengineering.oop.polymorphism;

import org.junit.Test;
import static org.junit.Assert.*;

public class DiscountStrategyTest {

    @Test
    public void testNoDiscount() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Item", 100, 1);
        cart.setDiscountStrategy(new NoDiscount());
        assertEquals(0, cart.getDiscount(), 0.001);
    }

    @Test
    public void testPercentageDiscount() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Item", 200, 1);
        cart.setDiscountStrategy(new PercentageDiscount(10));
        assertEquals(20, cart.getDiscount(), 0.001);
        assertEquals(180, cart.getTotal(), 0.001);
    }

    @Test
    public void testFixedDiscountWithMinimum() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Item", 50, 1);
        cart.setDiscountStrategy(new FixedAmountDiscount(20, 100));
        assertEquals(0, cart.getDiscount(), 0.001); // Below minimum

        cart.addItem("Item2", 100, 1);
        assertEquals(20, cart.getDiscount(), 0.001); // Above minimum
    }
}
```

**Complexity:**
- Strategy creation: O(1)
- Discount calculation: O(1) for most strategies, O(n) for BuyOneGetOneFree (n = items)
- Space: O(1) per strategy instance

**Best Practices:**
- Encapsulate varying behavior in strategy objects
- Make strategies stateless when possible (immutable)
- Use dependency injection to provide strategies

---

## Exercises

### Easy

1. **Method Overloading:**
   Create a `MathUtils` class with overloaded `max()` methods for `int`, `double`, and `int[]` parameters.

2. **Simple Override:**
   Create a `Vehicle` class with `start()` and `stop()` methods. Override them in `Car` and `Motorcycle` subclasses.

3. **Polymorphic Array:**
   Create an array of `Animal` objects (Dog, Cat, Bird) and call `speak()` on each using a loop.

### Medium

4. **Payment System:**
   Create a payment processing system with `PaymentMethod` interface and `CreditCard`, `DebitCard`, `CryptoCurrency` implementations. Process payments polymorphically.

5. **Shape Calculator:**
   Create a `Shape` abstract class with `Circle`, `Rectangle`, `Triangle` subclasses. Calculate total area and perimeter using polymorphic collections.

6. **Notification System:**
   Create a `Notification` interface with `EmailNotification`, `SMSNotification`, `PushNotification` implementations. Send notifications polymorphically.

### Hard

7. **Plugin Architecture:**
   Design a plugin system where plugins are loaded dynamically and invoked polymorphically. Include `Plugin` interface, `PluginManager`, and at least 3 plugin implementations.

8. **Expression Evaluator:**
   Create an expression tree with `Expression` interface and `Number`, `Add`, `Multiply` implementations. Evaluate expressions using polymorphic dispatch.

9. **Render Engine:**
   Create a rendering system with `Renderer` interface and `ConsoleRenderer`, `HTMLRenderer`, `JSONRenderer` implementations. Render the same data model using different renderers.

---

## Interview Questions

### Easy

1. **What is polymorphism in Java?**
   Polymorphism is the ability of objects to take on many forms. The same reference type can refer to different actual types, and the same method call produces different behavior based on the runtime type. Java supports compile-time (overloading) and runtime (overriding) polymorphism.

2. **What is the difference between method overloading and overriding?**
   Overloading: Same method name with different parameters in the same class. Resolved at compile time (static binding). Overriding: Same method signature in a subclass replaces the parent implementation. Resolved at runtime (dynamic dispatch).

3. **Can you override a private or static method?**
   No. Private methods are not visible to subclasses. Static methods belong to the class, not instances, so they're hidden (not overridden) when redeclared in a subclass.

### Medium

4. **What is dynamic dispatch and how does it work?**
   Dynamic dispatch is the mechanism the JVM uses to determine which overridden method to call at runtime. It uses the actual object's class (not the reference type) to look up the method in the virtual method table (vtable). This is how runtime polymorphism works.

5. **What is the difference between upcasting and downcasting?**
   Upcasting: Converting a subclass reference to a superclass reference (implicit, safe). Downcasting: Converting a superclass reference to a subclass reference (explicit, requires cast, may throw `ClassCastException`).

6. **Can you override a method to return a more specific type (covariant return)?**
   Yes. Since Java 5, overriding methods can return a subtype of the parent method's return type. This is called covariant return types. Example: `Object.clone()` returns `Object`, but `ArrayList.clone()` returns `ArrayList`.

### Hard

7. **Explain the performance implications of dynamic dispatch.**
   Dynamic dispatch adds a small overhead compared to static binding because the JVM must look up the method at runtime. However, modern JVMs use inline caches and devirtualization to optimize this. The overhead is negligible in most applications and is the price paid for flexibility.

8. **What is the difference between `invokevirtual`, `invokeinterface`, `invokestatic`, and `invokespecial`?**
   `invokevirtual`: Dynamic dispatch for virtual methods (most common). `invokeinterface`: Dynamic dispatch through interface reference. `invokestatic`: Static binding for static methods. `invokespecial`: Static binding for constructors, private methods, and `super` calls.

---

## Common Pitfalls

### Pitfall 1: Calling Overridden Methods in Constructor

**Wrong:**
```java
class Parent {
    Parent() { print(); } // Calls overridden method
    void print() { System.out.println("Parent"); }
}

class Child extends Parent {
    int value = 10;
    Child() { super(); }
    @Override
    void print() { System.out.println("Value: " + value); } // value is 0!
}

new Child(); // Prints "Value: 0" — value not yet initialized
```

**Right:**
```java
class Parent {
    Parent() { /* Don't call overridable methods */ }
    final void init() { print(); } // final prevents overriding
    void print() { System.out.println("Parent"); }
}

class Child extends Parent {
    int value = 10;
    Child() { super(); init(); } // Called after field initialization
    @Override
    void print() { System.out.println("Value: " + value); } // value is 10
}
```

### Pitfall 2: Confusing Overloading with Overriding

**Wrong:**
```java
class Animal {
    void speak(String s) { } // Overloaded, not overridden
}

class Dog extends Animal {
    void speak(Object o) { } // This is overloading, not overriding!
    // @Override annotation would catch this error
}
```

**Right:**
```java
class Animal {
    void speak() { }
}

class Dog extends Animal {
    @Override
    void speak() { } // Correctly overridden
}
```

### Pitfall 3: Assuming Reference Type Determines Behavior

**Wrong:**
```java
Animal a = new Dog();
a.speak(); // Assuming this calls Animal.speak() because a is Animal type
```

**Right:**
```java
Animal a = new Dog();
a.speak(); // Calls Dog.speak() — dynamic dispatch uses actual type
```

---

## Best Practices

1. **Program to Interfaces, Not Implementations:**
   Use interface types for variables and parameters. This enables polymorphism and makes code flexible.

2. **Use `@Override` Annotation:**
   Always use `@Override` when overriding methods. It prevents signature mismatches and documents intent.

3. **Favor Polymorphism Over Type Checking:**
   Avoid `instanceof` checks followed by casting. Use polymorphic method calls instead.

4. **Keep Override Contracts:**
   Overridden methods should honor the superclass contract (Liskov Substitution Principle).

5. **Understand Static vs Dynamic Binding:**
   Static methods, private methods, and final methods use static binding. Virtual methods use dynamic binding. Know which is which.

---

## Real World Usage

### Spring Framework
- `@Controller` dispatch — same URL pattern, different controller methods based on request type
- `BeanPostProcessor` — polymorphic post-processing of all beans
- `HandlerInterceptor` — polymorphic request interception

### Hibernate / JPA
- `Type` hierarchy — polymorphic type resolution for entity fields
- `EventListener` interfaces — polymorphic event handling
- `Interceptor` — polymorphic persistence lifecycle hooks

### JDK Source Code
- `Collections.sort()` — works with any `Comparable` implementation
- `Stream` API — intermediate operations return `Stream<T>`, terminal operations polymorphically collect
- `Exception` hierarchy — `catch (Exception e)` catches all exception types polymorphically

### Enterprise Applications
- Payment processing — multiple payment gateways behind a common interface
- Notification systems — email, SMS, push notifications through polymorphic dispatch
- Data access — different database dialects behind a common repository interface

---

## References

- [Java Language Specification — Polymorphism](https://docs.oracle.com/javase/specs/jls/se17/html/jls-15.html#jls-15.12)
- [Effective Java, 3rd Edition — Item 19: Design and document for inheritance or else prohibit it](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Oracle Java Tutorials — Polymorphism](https://docs.oracle.com/en/java/javase/21/java/javaOO/polymorphism.html)
- [Baeldung — Polymorphism in Java](https://www.baeldung.com/java-polymorphism)
- [Refactoring.Guru — Polymorphism](https://refactoring.guru/design-patterns/polymorphism)

---

## Summary

Polymorphism is the mechanism that enables objects to take on many forms, providing flexibility and extensibility to Java applications. Key takeaways:

- **Compile-time polymorphism (Overloading):** Same method name, different parameters, resolved at compile time
- **Runtime polymorphism (Overriding):** Same method signature in subclass, resolved at runtime via dynamic dispatch
- **Upcasting:** Implicit, safe conversion from subclass to superclass reference
- **Downcasting:** Explicit conversion requiring cast, may throw `ClassCastException`
- **Dynamic dispatch:** JVM uses the actual object's class to find the method implementation

**Golden rule:** Program to interfaces, not implementations. Let polymorphism handle the dispatch.

---

**Navigation:**
- Previous: [09-inheritance](../09-inheritance/README.md)
- Next: [11-abstraction](../11-abstraction/README.md)
- [Back to OOP Module](../README.md)
