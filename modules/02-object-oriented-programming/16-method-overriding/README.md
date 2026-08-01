# Method Overriding

## Introduction

Method overriding is a fundamental mechanism of runtime polymorphism in Java that allows a subclass to provide a specific implementation of a method that is already defined in its parent class, enabling objects to exhibit different behaviors based on their actual runtime type rather than their reference type. This dynamic polymorphism (also known as late binding or dynamic dispatch) is resolved at runtime by the JVM, which determines which method implementation to invoke based on the actual object type, not the reference type. Method overriding is essential for implementing the Liskov Substitution Principle, enabling inheritance hierarchies where subclasses can specialize or extend parent class behavior while maintaining a consistent interface. The @Override annotation serves as a compile-time check to ensure that the method properly overrides a parent method, catching errors such as misspelled method names or incorrect parameter signatures.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the difference between method overriding and method overloading
- [ ] Implement method overrides with proper rules and constraints
- [ ] Use the @Override annotation to prevent common overriding errors
- [ ] Apply method overriding to achieve runtime polymorphism and the Liskov Substitution Principle

## Prerequisites

- [09-inheritance](../09-inheritance/README.md) - Understanding class hierarchies and the extends keyword
- [10-polymorphism](../10-polymorphism/README.md) - Core concepts of polymorphism and dynamic dispatch
- [15-method-overloading](../15-method-overloading/README.md) - Understanding compile-time method resolution
- [12-interfaces](../12-interfaces/README.md) - Interface contracts and implementation requirements

## Why This Concept Exists

### The Problem

In inheritance hierarchies, subclasses often need to provide specialized behavior while maintaining the same method signature as their parent class. Without method overriding, you would face:

1. **Code duplication**: Each subclass would need to implement entirely new methods
2. **Inconsistent interfaces**: Different subclasses might use different method names for similar operations
3. **Broken polymorphism**: You couldn't use parent class references to invoke subclass-specific behavior
4. **Violated contracts**: The Liskov Substitution Principle couldn't be enforced

### The Solution

Method overriding allows subclasses to:

- Provide specific implementations of parent class methods
- Maintain consistent method signatures across the hierarchy
- Enable runtime polymorphism where the correct method is called based on object type
- Enforce behavioral contracts through parent class method signatures

### Real-World Analogy

Think of method overriding as **specialized job roles within a company**. The parent class defines a general job description (e.g., "process order"), while each subclass provides its own specific implementation:

- A SalesEmployee might process orders by applying discounts
- A WarehouseEmployee might process orders by picking and packing
- A DeliveryEmployee might process orders by scheduling shipment

The method name "processOrder" remains the same, but each employee type performs the task differently based on their role.

## Internal Working

### JVM Perspective

Method overriding is implemented through dynamic dispatch in the JVM:

1. **Virtual Method Table (vtable)**: Each class has a vtable containing method pointers. When a method is called, the JVM looks up the actual implementation in the vtable of the runtime object type.

2. **Dynamic Binding**: At compile time, the compiler only knows the reference type. At runtime, the JVM determines the actual object type and invokes the correct overridden method.

3. **Method Resolution**: The JVM traverses the class hierarchy to find the most specific implementation of the called method.

4. **Performance**: Modern JVMs use techniques like inline caching and just-in-time compilation to optimize dynamic dispatch.

### Memory Representation

```
Class Hierarchy with vtables:

Animal Class (Parent)
┌─────────────────────────────┐
│ Virtual Method Table        │
│ ├── speak() → Animal.speak │
│ └── move() → Animal.move   │
└─────────────────────────────┘

Dog Class (Child)
┌─────────────────────────────┐
│ Virtual Method Table        │
│ ├── speak() → Dog.speak    │  ← Overridden
│ ├── move() → Animal.move   │  ← Inherited
│ └── fetch() → Dog.fetch    │  ← New method
└─────────────────────────────┘

At Runtime:
Animal animal = new Dog();
animal.speak();
↓
JVM checks actual type: Dog
↓
Looks up Dog's vtable for speak()
↓
Invokes Dog.speak()
```

### Dynamic Dispatch Process

1. **Compile Time**: Compiler verifies method signature matches parent
2. **Runtime**: JVM identifies actual object type
3. **Vtable Lookup**: JVM finds the method in the object's vtable
4. **Method Invocation**: JVM invokes the most specific implementation

## Syntax

### Basic Method Overriding

```java
class Parent {
    public void display() {
        System.out.println("Parent display");
    }
}

class Child extends Parent {
    @Override
    public void display() {
        System.out.println("Child display");
    }
}
```

### Overriding with super Keyword

```java
class Child extends Parent {
    @Override
    public void display() {
        super.display(); // Call parent implementation
        System.out.println("Additional child behavior");
    }
}
```

### Overriding Abstract Methods

```java
abstract class Shape {
    public abstract double calculateArea();
}

class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}
```

### Overriding Object Class Methods

```java
class Person {
    private String name;
    private int age;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person other = (Person) obj;
        return age == other.age && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
```

## Easy Examples

### Example 1: Animal Sound System

**Problem Statement**: Create an animal hierarchy where each animal type overrides the speak() method to produce its own characteristic sound.

**Implementation**:

```java
package academy.javaengineering.oop.methodoverriding;

class Animal {
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public void speak() {
        System.out.println(name + " makes a generic sound.");
    }

    public void move() {
        System.out.println(name + " moves.");
    }

    public String getInfo() {
        return "Animal: " + name;
    }
}

class Dog extends Animal {
    private String breed;

    public Dog(String name, String breed) {
        super(name);
        this.breed = breed;
    }

    @Override
    public void speak() {
        System.out.println(name + " barks: Woof! Woof!");
    }

    @Override
    public void move() {
        System.out.println(name + " runs on four legs.");
    }

    public void fetch() {
        System.out.println(name + " fetches the ball.");
    }

    @Override
    public String getInfo() {
        return "Dog: " + name + " (" + breed + ")";
    }
}

class Cat extends Animal {
    private boolean isIndoor;

    public Cat(String name, boolean isIndoor) {
        super(name);
        this.isIndoor = isIndoor;
    }

    @Override
    public void speak() {
        System.out.println(name + " meows: Meow! Meow!");
    }

    @Override
    public void move() {
        System.out.println(name + " slinks gracefully.");
    }

    public void purr() {
        System.out.println(name + " purrs contentedly.");
    }

    @Override
    public String getInfo() {
        return "Cat: " + name + " (" + (isIndoor ? "indoor" : "outdoor") + ")";
    }
}

class Bird extends Animal {
    private boolean canFly;

    public Bird(String name, boolean canFly) {
        super(name);
        this.canFly = canFly;
    }

    @Override
    public void speak() {
        System.out.println(name + " chirps: Tweet! Tweet!");
    }

    @Override
    public void move() {
        if (canFly) {
            System.out.println(name + " flies through the air.");
        } else {
            System.out.println(name + " hops along the ground.");
        }
    }

    @Override
    public String getInfo() {
        return "Bird: " + name + " (" + (canFly ? "can fly" : "flightless") + ")";
    }
}

public class AnimalDemo {
    public static void main(String[] args) {
        Animal dog = new Dog("Buddy", "Golden Retriever");
        Animal cat = new Cat("Whiskers", true);
        Animal bird = new Bird("Tweety", true);

        System.out.println("=== Polymorphic Method Calls ===");
        Animal[] animals = {dog, cat, bird};
        for (Animal animal : animals) {
            System.out.println(animal.getInfo());
            animal.speak();
            animal.move();
            System.out.println();
        }

        System.out.println("=== Using instanceof for Specific Methods ===");
        for (Animal animal : animals) {
            if (animal instanceof Dog) {
                ((Dog) animal).fetch();
            } else if (animal instanceof Cat) {
                ((Cat) animal).purr();
            }
        }
    }
}
```

**Expected Output**:
```
=== Polymorphic Method Calls ===
Dog: Buddy (Golden Retriever)
Buddy barks: Woof! Woof!
Buddy runs on four legs.

Cat: Whiskers (indoor)
Whiskers meows: Meow! Meow!
Whiskers slinks gracefully.

Bird: Tweety (can fly)
Tweety chirps: Tweet! Tweet!
Tweety flies through the air.

=== Using instanceof for Specific Methods ===
Buddy fetches the ball.
Whiskers purrs contentedly.
```

**Best Practices**:
- Always use @Override annotation when overriding methods
- Ensure the overridden method maintains the same signature
- Consider the Liskov Substitution Principle: subclass should be substitutable for parent
- Use super to call parent implementation when extending behavior

### Example 2: Shape Area Calculator

**Problem Statement**: Design a shape hierarchy where each shape type overrides calculateArea() and describe() methods to provide shape-specific behavior.

**Implementation**:

```java
package academy.javaengineering.oop.methodoverriding;

abstract class Shape {
    protected String color;
    protected String name;

    public Shape(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public abstract double calculateArea();
    public abstract double calculatePerimeter();

    public void describe() {
        System.out.printf("%s [color=%s, area=%.2f, perimeter=%.2f]%n",
            name, color, calculateArea(), calculatePerimeter());
    }

    @Override
    public String toString() {
        return name + "{color='" + color + "'}";
    }
}

class Circle extends Shape {
    private double radius;

    public Circle(double radius, String color) {
        super("Circle", color);
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public void describe() {
        super.describe();
        System.out.println("  Radius: " + radius);
    }
}

class Rectangle extends Shape {
    protected double width;
    protected double height;

    public Rectangle(double width, double height, String color) {
        super("Rectangle", color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double calculateArea() {
        return width * height;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * (width + height);
    }

    @Override
    public void describe() {
        super.describe();
        System.out.printf("  Width: %.2f, Height: %.2f%n", width, height);
    }
}

class Square extends Rectangle {
    public Square(double side, String color) {
        super(side, side, color);
        this.name = "Square";
    }

    @Override
    public void describe() {
        super.describe();
        System.out.println("  Side: " + width);
    }
}

class Triangle extends Shape {
    private double sideA, sideB, sideC;

    public Triangle(double sideA, double sideB, double sideC, String color) {
        super("Triangle", color);
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }

    @Override
    public double calculateArea() {
        double s = (sideA + sideB + sideC) / 2;
        return Math.sqrt(s * (s - sideA) * (s - sideB) * (s - sideC));
    }

    @Override
    public double calculatePerimeter() {
        return sideA + sideB + sideC;
    }

    @Override
    public void describe() {
        super.describe();
        System.out.printf("  Sides: %.2f, %.2f, %.2f%n", sideA, sideB, sideC);
    }
}

public class ShapeDemo {
    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(5, "red"),
            new Rectangle(4, 6, "blue"),
            new Square(3, "green"),
            new Triangle(3, 4, 5, "yellow")
        };

        System.out.println("=== Shape Descriptions ===");
        for (Shape shape : shapes) {
            shape.describe();
            System.out.println();
        }

        System.out.println("=== Polymorphic Area Calculation ===");
        double totalArea = 0;
        for (Shape shape : shapes) {
            double area = shape.calculateArea();
            totalArea += area;
            System.out.printf("%s area: %.2f%n", shape.name, area);
        }
        System.out.printf("Total area: %.2f%n", totalArea);
    }
}
```

**Expected Output**:
```
=== Shape Descriptions ===
Circle [color=red, area=78.54, perimeter=31.42]
  Radius: 5.0

Rectangle [color=blue, area=24.00, perimeter=20.00]
  Width: 4.00, Height: 6.00

Square [color=green, area=9.00, perimeter=12.00]
  Width: 3.00, Height: 3.00
  Side: 3.0

Triangle [color=yellow, area=6.00, perimeter=12.00]
  Sides: 3.00, 4.00, 5.00

=== Polymorphic Area Calculation ===
Circle area: 78.54
Rectangle area: 24.00
Square area: 9.00
Triangle area: 6.00
Total area: 117.54
```

**Best Practices**:
- Use abstract methods to enforce method implementation in subclasses
- Provide default implementations in parent when sensible
- Call super.methodName() when extending parent behavior
- Maintain the same return type or a subtype (covariant return types)

### Example 3: Employee Payroll System

**Problem Statement**: Create an employee hierarchy where different employee types override calculatePay() and getBenefits() to provide employee-specific compensation calculations.

**Implementation**:

```java
package academy.javaengineering.oop.methodoverriding;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

abstract class Employee {
    protected String id;
    protected String name;
    protected double baseSalary;
    protected LocalDateTime hireDate;

    public Employee(String id, String name, double baseSalary) {
        this.id = id;
        this.name = name;
        this.baseSalary = baseSalary;
        this.hireDate = LocalDateTime.now();
    }

    public abstract double calculatePay();
    public abstract List<String> getBenefits();

    public void displayPayInfo() {
        System.out.printf("%s (%s): Pay=$%.2f%n",
            name, id, calculatePay());
    }

    public int getYearsOfService() {
        return LocalDateTime.now().getYear() - hireDate.getYear();
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}

class FullTimeEmployee extends Employee {
    private double bonus;
    private double healthInsurance;

    public FullTimeEmployee(String id, String name, double baseSalary,
                           double bonus, double healthInsurance) {
        super(id, name, baseSalary);
        this.bonus = bonus;
        this.healthInsurance = healthInsurance;
    }

    @Override
    public double calculatePay() {
        return baseSalary + bonus;
    }

    @Override
    public List<String> getBenefits() {
        List<String> benefits = new ArrayList<>();
        benefits.add("Health Insurance: $" + healthInsurance);
        benefits.add("401k Match: 5%");
        benefits.add("Paid Time Off: 20 days");
        benefits.add("Life Insurance");
        return benefits;
    }

    @Override
    public void displayPayInfo() {
        super.displayPayInfo();
        System.out.println("  Base: $" + baseSalary + ", Bonus: $" + bonus);
    }
}

class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private int hoursWorked;

    public PartTimeEmployee(String id, String name, double hourlyRate, int hoursWorked) {
        super(id, name, 0);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculatePay() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public List<String> getBenefits() {
        List<String> benefits = new ArrayList<>();
        benefits.add("Flexible Hours");
        benefits.add("Pro-rated PTO");
        return benefits;
    }

    @Override
    public void displayPayInfo() {
        super.displayPayInfo();
        System.out.println("  Rate: $" + hourlyRate + "/hr, Hours: " + hoursWorked);
    }
}

class Contractor extends Employee {
    private double dailyRate;
    private int contractDays;
    private double projectBonus;

    public Contractor(String id, String name, double dailyRate,
                     int contractDays, double projectBonus) {
        super(id, name, 0);
        this.dailyRate = dailyRate;
        this.contractDays = contractDays;
        this.projectBonus = projectBonus;
    }

    @Override
    public double calculatePay() {
        return (dailyRate * contractDays) + projectBonus;
    }

    @Override
    public List<String> getBenefits() {
        List<String> benefits = new ArrayList<>();
        benefits.add("No Benefits (Contract)");
        benefits.add("Project Completion Bonus");
        return benefits;
    }

    @Override
    public void displayPayInfo() {
        super.displayPayInfo();
        System.out.printf("  Rate: $%.2f/day, Days: %d, Bonus: $%.2f%n",
            dailyRate, contractDays, projectBonus);
    }
}

class PayrollProcessor {
    public static void processPayroll(Employee[] employees) {
        System.out.println("=== Payroll Processing ===");
        double totalPayroll = 0;

        for (Employee emp : employees) {
            emp.displayPayInfo();
            totalPayroll += emp.calculatePay();
        }

        System.out.printf("%nTotal Payroll: $%.2f%n", totalPayroll);
    }

    public static void displayAllBenefits(Employee[] employees) {
        System.out.println("\n=== Employee Benefits ===");
        for (Employee emp : employees) {
            System.out.println("\n" + emp.getName() + "'s Benefits:");
            for (String benefit : emp.getBenefits()) {
                System.out.println("  • " + benefit);
            }
        }
    }
}

public class EmployeePayrollDemo {
    public static void main(String[] args) {
        Employee[] employees = {
            new FullTimeEmployee("FT001", "Alice Johnson", 75000, 10000, 500),
            new PartTimeEmployee("PT001", "Bob Smith", 25, 80),
            new Contractor("CT001", "Charlie Brown", 500, 30, 5000)
        };

        PayrollProcessor.processPayroll(employees);
        PayrollProcessor.displayAllBenefits(employees);
    }
}
```

**Expected Output**:
```
=== Payroll Processing ===
Alice Johnson (FT001): Pay=$85000.00
  Base: $75000.0, Bonus: $10000.0
Bob Smith (PT001): Pay=$2000.00
  Rate: $25.0/hr, Hours: 80
Charlie Brown (CT001): Pay=$20000.00
  Rate: $500.00/day, Days: 30, Bonus: $5000.0

Total Payroll: $107000.00

=== Employee Benefits ===

Alice Johnson's Benefits:
  • Health Insurance: $500
  • 401k Match: 5%
  • Paid Time Off: 20 days
  • Life Insurance

Bob Smith's Benefits:
  • Flexible Hours
  • Pro-rated PTO

Charlie Brown's Benefits:
  • No Benefits (Contract)
  • Project Completion Bonus
```

**Best Practices**:
- Use abstract methods to define the contract that all subclasses must implement
- Maintain consistent behavior across overridden methods
- Consider using template methods for common algorithms with customizable steps
- Document any assumptions or requirements in the parent class

## Medium Examples

### Example 1: Payment Processing System

**Problem Statement**: Design a payment processing system that uses method overriding to handle different payment methods with their own validation, processing, and receipt generation logic.

**Requirements**:

- Common payment interface with validate(), process(), and generateReceipt()
- Support for Credit Card, PayPal, and Bank Transfer payments
- Different validation rules for each payment type
- Custom receipt formats
- Transaction logging

**Implementation**:

```java
package academy.javaengineering.oop.methodoverriding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

abstract class PaymentMethod {
    protected String id;
    protected String name;
    protected boolean isProcessed;
    protected LocalDateTime processedAt;

    public PaymentMethod(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.isProcessed = false;
    }

    public final boolean executePayment(double amount) {
        System.out.println("Processing " + name + " payment...");

        if (!validate(amount)) {
            System.out.println("Validation failed for " + name);
            return false;
        }

        if (!process(amount)) {
            System.out.println("Processing failed for " + name);
            return false;
        }

        isProcessed = true;
        processedAt = LocalDateTime.now();
        System.out.println(generateReceipt(amount));
        return true;
    }

    protected abstract boolean validate(double amount);
    protected abstract boolean process(double amount);
    protected abstract String generateReceipt(double amount);

    public String getName() { return name; }
    public boolean isProcessed() { return isProcessed; }
}

class CreditCardPayment extends PaymentMethod {
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private String cvv;

    public CreditCardPayment(String cardNumber, String cardHolder,
                            String expiryDate, String cvv) {
        super("Credit Card");
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    protected boolean validate(double amount) {
        System.out.println("Validating credit card...");

        // Card number validation (Luhn algorithm simplified)
        if (cardNumber == null || cardNumber.length() != 16) {
            System.out.println("Invalid card number");
            return false;
        }

        // Expiry date validation
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            System.out.println("Invalid expiry date format (MM/YY)");
            return false;
        }

        // CVV validation
        if (cvv == null || cvv.length() < 3 || cvv.length() > 4) {
            System.out.println("Invalid CVV");
            return false;
        }

        // Amount validation
        if (amount <= 0 || amount > 10000) {
            System.out.println("Amount out of range for credit card");
            return false;
        }

        return true;
    }

    @Override
    protected boolean process(double amount) {
        System.out.println("Charging credit card ****" + cardNumber.substring(12));
        System.out.println("Amount: $" + amount);
        return true;
    }

    @Override
    protected String generateReceipt(double amount) {
        return String.format(
            "Credit Card Receipt:%n" +
            "  Card: ****%s%n" +
            "  Holder: %s%n" +
            "  Amount: $%.2f%n" +
            "  Date: %s%n" +
            "  Transaction ID: %s",
            cardNumber.substring(12),
            cardHolder,
            amount,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            id
        );
    }

    public String getMaskedCardNumber() {
        return "****" + cardNumber.substring(12);
    }
}

class PayPalPayment extends PaymentMethod {
    private String email;
    private String password;

    public PayPalPayment(String email, String password) {
        super("PayPal");
        this.email = email;
        this.password = password;
    }

    @Override
    protected boolean validate(double amount) {
        System.out.println("Validating PayPal account...");

        if (email == null || !email.contains("@") || !email.contains(".")) {
            System.out.println("Invalid email format");
            return false;
        }

        if (password == null || password.length() < 8) {
            System.out.println("Invalid password");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Invalid amount");
            return false;
        }

        return true;
    }

    @Override
    protected boolean process(double amount) {
        System.out.println("Processing PayPal payment from: " + email);
        System.out.println("Amount: $" + amount);
        return true;
    }

    @Override
    protected String generateReceipt(double amount) {
        return String.format(
            "PayPal Receipt:%n" +
            "  Account: %s%n" +
            "  Amount: $%.2f%n" +
            "  Date: %s%n" +
            "  Transaction ID: %s",
            email,
            amount,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            id
        );
    }

    public String getMaskedEmail() {
        String[] parts = email.split("@");
        return parts[0].substring(0, Math.min(2, parts[0].length())) +
               "***@" + parts[1];
    }
}

class BankTransferPayment extends PaymentMethod {
    private String accountNumber;
    private String routingNumber;
    private String bankName;

    public BankTransferPayment(String accountNumber, String routingNumber, String bankName) {
        super("Bank Transfer");
        this.accountNumber = accountNumber;
        this.routingNumber = routingNumber;
        this.bankName = bankName;
    }

    @Override
    protected boolean validate(double amount) {
        System.out.println("Validating bank account...");

        if (accountNumber == null || accountNumber.length() < 8) {
            System.out.println("Invalid account number");
            return false;
        }

        if (routingNumber == null || routingNumber.length() != 9) {
            System.out.println("Invalid routing number");
            return false;
        }

        if (amount <= 0 || amount > 100000) {
            System.out.println("Amount out of range for bank transfer");
            return false;
        }

        return true;
    }

    @Override
    protected boolean process(double amount) {
        System.out.println("Initiating bank transfer from: " + bankName);
        System.out.println("Account: " + accountNumber.substring(0, 4) + "****");
        System.out.println("Amount: $" + amount);
        return true;
    }

    @Override
    protected String generateReceipt(double amount) {
        return String.format(
            "Bank Transfer Receipt:%n" +
            "  Bank: %s%n" +
            "  Account: %s****%n" +
            "  Amount: $%.2f%n" +
            "  Date: %s%n" +
            "  Transaction ID: %s",
            bankName,
            accountNumber.substring(0, 4),
            amount,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            id
        );
    }

    public String getMaskedAccountNumber() {
        return accountNumber.substring(0, 4) + "****" +
               accountNumber.substring(accountNumber.length() - 4);
    }
}

class PaymentProcessor {
    public static void processPayment(PaymentMethod payment, double amount) {
        System.out.println("=== Payment Processing ===");
        boolean success = payment.executePayment(amount);
        System.out.println("Payment " + (success ? "successful" : "failed") + "\n");
    }

    public static void processBatch(PaymentMethod[] payments, double[] amounts) {
        System.out.println("=== Batch Payment Processing ===");
        int successCount = 0;

        for (int i = 0; i < payments.length; i++) {
            boolean success = payments[i].executePayment(amounts[i]);
            if (success) successCount++;
            System.out.println();
        }

        System.out.printf("Batch complete: %d/%d successful%n",
            successCount, payments.length);
    }
}

public class PaymentDemo {
    public static void main(String[] args) {
        PaymentMethod creditCard = new CreditCardPayment(
            "1234567890123456", "John Doe", "12/25", "123");
        PaymentMethod paypal = new PayPalPayment("john@example.com", "password123");
        PaymentMethod bankTransfer = new BankTransferPayment(
            "1234567890", "021000021", "Chase Bank");

        // Process individual payments
        PaymentProcessor.processPayment(creditCard, 150.00);
        PaymentProcessor.processPayment(paypal, 75.50);
        PaymentProcessor.processPayment(bankTransfer, 500.00);

        // Process batch
        PaymentMethod[] batchPayments = {creditCard, paypal, bankTransfer};
        double[] batchAmounts = {100.00, 50.00, 250.00};
        PaymentProcessor.processBatch(batchPayments, batchAmounts);
    }
}
```

**Expected Output**:
```
=== Payment Processing ===
Processing Credit Card payment...
Validating credit card...
Charging credit card ****3456
Amount: $150.0
Credit Card Receipt:
  Card: ****3456
  Holder: John Doe
  Amount: $150.00
  Date: 2024-01-15 10:30:00
  Transaction ID: [uuid]
Payment successful

=== Payment Processing ===
Processing PayPal payment...
Validating PayPal account...
Processing PayPal payment from: john@example.com
Amount: $75.5
PayPal Receipt:
  Account: john@example.com
  Amount: $75.50
  Date: 2024-01-15 10:30:01
  Transaction ID: [uuid]
Payment successful

=== Payment Processing ===
Processing Bank Transfer payment...
Validating bank account...
Initiating bank transfer from: Chase Bank
Account: 1234****
Amount: $500.0
Bank Transfer Receipt:
  Bank: Chase Bank
  Account: 1234****
  Amount: $500.00
  Date: 2024-01-15 10:30:02
  Transaction ID: [uuid]
Payment successful

=== Batch Payment Processing ===
Processing Credit Card payment...
...
Batch complete: 3/3 successful
```

**Code Walkthrough**:

1. **Abstract PaymentMethod**: Defines the template with abstract methods for validation, processing, and receipt generation.

2. **executePayment() Template Method**: Orchestrates the payment flow, calling overridden methods at each step.

3. **Concrete Implementations**: Each payment type provides its own validation rules, processing logic, and receipt format.

4. **Polymorphic Usage**: The PaymentProcessor works with any PaymentMethod subclass.

**Alternative Solution**:

```java
// Using Strategy pattern with interfaces
interface PaymentStrategy {
    boolean validate(double amount);
    boolean process(double amount);
    String generateReceipt(double amount);
}

class CreditCardStrategy implements PaymentStrategy {
    // Implementation
}

// This approach provides more flexibility but less code reuse
```

## Hard Examples

### Example 1: Game Entity System with Polymorphic Behavior

**Problem Statement**: Design a game entity system where different entity types (Player, Enemy, NPC) override common methods to provide type-specific behavior, including combat, movement, and interaction.

**Requirements**:

- Common entity interface with update(), render(), and interact()
- Different combat behaviors for players and enemies
- AI-driven behavior for NPCs
- Event system for entity interactions
- State machine for entity states

**Architecture**:

```
Game Entity System
├── Abstract Entity
│   ├── Player
│   │   ├── Warrior
│   │   └── Mage
│   ├── Enemy
│   │   ├── Goblin
│   │   └── Dragon
│   └── NPC
│       ├── Merchant
│       └── QuestGiver
├── State Machine
│   ├── IdleState
│   ├── MovingState
│   ├── CombatState
│   └── DeadState
└── Event System
    ├── DamageEvent
    └── InteractionEvent
```

**Implementation**:

```java
package academy.javaengineering.oop.methodoverriding;

import java.util.*;

enum EntityState {
    IDLE, MOVING, COMBAT, DEAD
}

abstract class Entity {
    protected String id;
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int x, y;
    protected EntityState state;
    protected Random random;

    public Entity(String name, int maxHealth, int x, int y) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.x = x;
        this.y = y;
        this.state = EntityState.IDLE;
        this.random = new Random();
    }

    // Template method for entity update
    public final void update(double deltaTime) {
        if (state == EntityState.DEAD) return;

        onUpdate(deltaTime);
        checkState();
    }

    // Abstract methods for type-specific behavior
    protected abstract void onUpdate(double deltaTime);
    public abstract void render();
    public abstract void onDamage(int damage);
    public abstract void onDeath();

    // Concrete methods with default implementations
    public void takeDamage(int damage) {
        if (state == EntityState.DEAD) return;

        int actualDamage = Math.max(0, damage - getDefense());
        health -= actualDamage;
        System.out.println(name + " took " + actualDamage + " damage. Health: " + health);

        onDamage(actualDamage);

        if (health <= 0) {
            health = 0;
            state = EntityState.DEAD;
            onDeath();
        }
    }

    public void heal(int amount) {
        if (state == EntityState.DEAD) return;

        int actualHeal = Math.min(amount, maxHealth - health);
        health += actualHeal;
        System.out.println(name + " healed " + actualHeal + " HP. Health: " + health);
    }

    public void moveTo(int targetX, int targetY) {
        if (state == EntityState.DEAD || state == EntityState.COMBAT) return;

        state = EntityState.MOVING;
        this.x = targetX;
        this.y = targetY;
    }

    protected void checkState() {
        if (health <= 0) {
            state = EntityState.DEAD;
        } else if (state == EntityState.MOVING) {
            state = EntityState.IDLE;
        }
    }

    // Getters and setters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public boolean isAlive() { return state != EntityState.DEAD; }
    public EntityState getState() { return state; }
    public int getX() { return x; }
    public int getY() { return y; }

    protected abstract int getDefense();
}

// Player hierarchy
abstract class Player extends Entity {
    protected int experience;
    protected int level;
    protected int attackPower;
    protected Map<String, Integer> stats;

    public Player(String name, int maxHealth, int x, int y) {
        super(name, maxHealth, x, y);
        this.experience = 0;
        this.level = 1;
        this.attackPower = 10;
        this.stats = new HashMap<>();
        stats.put("strength", 10);
        stats.put("intelligence", 10);
        stats.put("defense", 5);
    }

    @Override
    protected void onUpdate(double deltaTime) {
        // Player-specific update logic
        // Handle input, animation, etc.
    }

    @Override
    public void render() {
        System.out.printf("Player %s at (%d, %d) - HP: %d/%d%n",
            name, x, y, health, maxHealth);
    }

    @Override
    public void onDamage(int damage) {
        System.out.println(name + " grunts in pain!");
    }

    @Override
    public void onDeath() {
        System.out.println("Game Over! " + name + " has fallen!");
    }

    public abstract void useAbility(Entity target);

    protected void gainExperience(int exp) {
        experience += exp;
        if (experience >= level * 100) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        experience = 0;
        maxHealth += 20;
        health = maxHealth;
        attackPower += 5;
        System.out.println(name + " leveled up to level " + level + "!");
    }

    @Override
    protected int getDefense() {
        return stats.get("defense");
    }
}

class Warrior extends Player {
    private int rage;
    private int maxRage;

    public Warrior(String name, int x, int y) {
        super(name, 150, x, y);
        this.maxRage = 100;
        this.rage = 0;
        stats.put("strength", 20);
        stats.put("defense", 15);
    }

    @Override
    public void useAbility(Entity target) {
        if (rage >= 30) {
            rage -= 30;
            int damage = attackPower * 2;
            System.out.println(name + " uses Cleave for " + damage + " damage!");
            target.takeDamage(damage);
        } else {
            System.out.println(name + " doesn't have enough rage!");
        }
    }

    @Override
    public void onDamage(int damage) {
        rage = Math.min(maxRage, rage + damage / 2);
        System.out.println(name + " gains " + (damage / 2) + " rage!");
    }

    @Override
    protected void onUpdate(double deltaTime) {
        // Warrior-specific update
    }
}

class Mage extends Player {
    private int mana;
    private int maxMana;

    public Mage(String name, int x, int y) {
        super(name, 100, x, y);
        this.maxMana = 100;
        this.mana = maxMana;
        stats.put("intelligence", 20);
        stats.put("defense", 5);
    }

    @Override
    public void useAbility(Entity target) {
        if (mana >= 20) {
            mana -= 20;
            int damage = stats.get("intelligence") * 2;
            System.out.println(name + " casts Fireball for " + damage + " damage!");
            target.takeDamage(damage);
        } else {
            System.out.println(name + " doesn't have enough mana!");
        }
    }

    @Override
    public void onDamage(int damage) {
        mana = Math.min(maxMana, mana + 5);
        System.out.println(name + " regenerates 5 mana!");
    }

    @Override
    protected void onUpdate(double deltaTime) {
        // Mage-specific update
        mana = Math.min(maxMana, mana + 1);
    }
}

// Enemy hierarchy
abstract class Enemy extends Entity {
    protected int attackPower;
    protected int experienceReward;

    public Enemy(String name, int maxHealth, int x, int y,
                int attackPower, int experienceReward) {
        super(name, maxHealth, x, y);
        this.attackPower = attackPower;
        this.experienceReward = experienceReward;
    }

    @Override
    protected void onUpdate(double deltaTime) {
        // AI behavior
        if (state == EntityState.IDLE && random.nextDouble() < 0.01) {
            // Random movement
            int dx = random.nextInt(3) - 1;
            int dy = random.nextInt(3) - 1;
            moveTo(x + dx, y + dy);
        }
    }

    @Override
    public void render() {
        System.out.printf("Enemy %s at (%d, %d) - HP: %d/%d%n",
            name, x, y, health, maxHealth);
    }

    @Override
    public void onDamage(int damage) {
        state = EntityState.COMBAT;
        System.out.println(name + " becomes enraged!");
    }

    @Override
    public void onDeath() {
        System.out.println(name + " has been defeated!");
        System.out.println("Reward: " + experienceReward + " XP");
    }

    public abstract void attack(Entity target);

    @Override
    protected int getDefense() {
        return 2;
    }
}

class Goblin extends Enemy {
    public Goblin(int x, int y) {
        super("Goblin", 50, x, y, 8, 25);
    }

    @Override
    public void attack(Entity target) {
        System.out.println(name + " slashes with a rusty sword!");
        target.takeDamage(attackPower);
    }
}

class Dragon extends Enemy {
    private int fireBreathDamage;

    public Dragon(int x, int y) {
        super("Dragon", 500, x, y, 25, 500);
        this.fireBreathDamage = 50;
    }

    @Override
    public void attack(Entity target) {
        if (random.nextDouble() < 0.3) {
            System.out.println(name + " breathes fire!");
            target.takeDamage(fireBreathDamage);
        } else {
            System.out.println(name + " bites!");
            target.takeDamage(attackPower);
        }
    }

    @Override
    public void onDamage(int damage) {
        super.onDamage(damage);
        if (health < maxHealth / 2) {
            System.out.println(name + " enters Enraged mode!");
            attackPower *= 2;
        }
    }
}

// NPC hierarchy
abstract class NPC extends Entity {
    protected String dialogue;

    public NPC(String name, int x, int y, String dialogue) {
        super(name, 100, x, y);
        this.dialogue = dialogue;
    }

    @Override
    protected void onUpdate(double deltaTime) {
        // NPC-specific update
    }

    @Override
    public void render() {
        System.out.printf("NPC %s at (%d, %d)%n", name, x, y);
    }

    @Override
    public void onDamage(int damage) {
        System.out.println(name + " cries for help!");
    }

    @Override
    public void onDeath() {
        System.out.println(name + " has been killed! The townspeople are outraged!");
    }

    public abstract void interact(Player player);

    @Override
    protected int getDefense() {
        return 0;
    }
}

class Merchant extends NPC {
    private Map<String, Integer> inventory;

    public Merchant(int x, int y) {
        super("Merchant", x, y, "Welcome! Take a look at my wares!");
        inventory = new HashMap<>();
        inventory.put("Health Potion", 50);
        inventory.put("Mana Potion", 75);
        inventory.put("Sword", 200);
    }

    @Override
    public void interact(Player player) {
        System.out.println(name + ": " + dialogue);
        System.out.println("Available items:");
        for (Map.Entry<String, Integer> item : inventory.entrySet()) {
            System.out.println("  " + item.getKey() + " - $" + item.getValue());
        }
    }
}

class QuestGiver extends NPC {
    private String questDescription;

    public QuestGiver(int x, int y) {
        super("Elder", x, y, "Greetings, adventurer!");
        this.questDescription = "Slay the dragon in the mountain!";
    }

    @Override
    public void interact(Player player) {
        System.out.println(name + ": " + dialogue);
        System.out.println("Quest: " + questDescription);
    }
}

// Game engine
class GameEngine {
    private List<Entity> entities;
    private boolean running;

    public GameEngine() {
        this.entities = new ArrayList<>();
        this.running = false;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void start() {
        running = true;
        System.out.println("Game started!");
    }

    public void update() {
        if (!running) return;

        double deltaTime = 0.016; // 60 FPS
        for (Entity entity : new ArrayList<>(entities)) {
            entity.update(deltaTime);
        }

        // Remove dead entities
        entities.removeIf(e -> !e.isAlive());
    }

    public void render() {
        System.out.println("\n--- Rendering ---");
        for (Entity entity : entities) {
            entity.render();
        }
    }

    public void stop() {
        running = false;
        System.out.println("Game stopped!");
    }

    public List<Entity> getEntities() {
        return new ArrayList<>(entities);
    }
}

// Unit tests
class GameEntityTest {
    public static void main(String[] args) {
        System.out.println("=== Running Game Entity Tests ===\n");

        testPlayerCreation();
        testCombatSystem();
        testNPCInteraction();
        testEnemyAI();

        System.out.println("\n=== All Tests Passed ===");
    }

    private static void testPlayerCreation() {
        System.out.println("Test 1: Player Creation");
        Player warrior = new Warrior("Conan", 0, 0);
        Player mage = new Mage("Gandalf", 5, 5);

        assert warrior.getName().equals("Conan") : "Warrior name incorrect";
        assert warrior.getHealth() == 150 : "Warrior health incorrect";
        assert mage.getHealth() == 100 : "Mage health incorrect";

        System.out.println("  PASS: Player creation test passed\n");
    }

    private static void testCombatSystem() {
        System.out.println("Test 2: Combat System");
        Player warrior = new Warrior("TestWarrior", 0, 0);
        Enemy goblin = new Goblin(5, 5);

        warrior.useAbility(goblin);
        assert goblin.getHealth() < 50 : "Goblin should take damage";

        goblin.attack(warrior);
        assert warrior.getHealth() < 150 : "Warrior should take damage";

        System.out.println("  PASS: Combat system test passed\n");
    }

    private static void testNPCInteraction() {
        System.out.println("Test 3: NPC Interaction");
        Merchant merchant = new Merchant(10, 10);
        Player player = new Warrior("Tester", 0, 0);

        merchant.interact(player);

        System.out.println("  PASS: NPC interaction test passed\n");
    }

    private static void testEnemyAI() {
        System.out.println("Test 4: Enemy AI");
        Dragon dragon = new Dragon(20, 20);

        for (int i = 0; i < 10; i++) {
            dragon.update(0.016);
        }

        System.out.println("  PASS: Enemy AI test passed\n");
    }
}

public class GameEntityDemo {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();

        // Create entities
        Player warrior = new Warrior("Conan", 0, 0);
        Player mage = new Mage("Gandalf", 3, 3);
        Enemy goblin1 = new Goblin(10, 10);
        Enemy goblin2 = new Goblin(12, 10);
        Enemy dragon = new Dragon(20, 20);
        Merchant merchant = new Merchant(5, 5);
        QuestGiver elder = new QuestGiver(5, 6);

        // Add to engine
        engine.addEntity(warrior);
        engine.addEntity(mage);
        engine.addEntity(goblin1);
        engine.addEntity(goblin2);
        engine.addEntity(dragon);
        engine.addEntity(merchant);
        engine.addEntity(elder);

        // Start game
        engine.start();

        // Game loop
        System.out.println("\n=== Game Loop ===");
        for (int i = 0; i < 3; i++) {
            System.out.println("\nFrame " + (i + 1) + ":");
            engine.update();
            engine.render();
        }

        // Combat
        System.out.println("\n=== Combat ===");
        warrior.useAbility(goblin1);
        mage.useAbility(goblin1);
        goblin1.attack(warrior);

        // NPC interaction
        System.out.println("\n=== NPC Interaction ===");
        merchant.interact(warrior);
        elder.interact(mage);

        // Dragon fight
        System.out.println("\n=== Dragon Fight ===");
        dragon.attack(warrior);
        warrior.useAbility(dragon);
        mage.useAbility(dragon);

        engine.stop();
    }
}
```

**Execution Flow**:

1. **Entity Creation**: Different entity types are created with their specific attributes
2. **Game Loop**: Each entity's update() is called, triggering type-specific behavior
3. **Combat**: Players use abilities, enemies attack with their own mechanics
4. **NPC Interaction**: Merchants and quest givers respond to player interaction
5. **State Management**: Entities transition between states (IDLE, MOVING, COMBAT, DEAD)

**Unit Tests**:

```java
public class EntitySystemTest {
    public static void main(String[] args) {
        System.out.println("=== Running Entity System Tests ===\n");

        testWarriorAbilities();
        testMageAbilities();
        testEnemyDefeat();
        testLevelUp();

        System.out.println("\n=== All Tests Passed ===");
    }

    private static void testWarriorAbilities() {
        System.out.println("Test 1: Warrior Abilities");
        Warrior warrior = new Warrior("Test", 0, 0);
        Goblin goblin = new Goblin(5, 5);

        // Generate rage
        goblin.attack(warrior);
        warrior.useAbility(goblin);

        assert goblin.getHealth() < 50 : "Goblin should take damage from Cleave";
        System.out.println("  PASS: Warrior abilities test passed\n");
    }

    private static void testMageAbilities() {
        System.out.println("Test 2: Mage Abilities");
        Mage mage = new Mage("Test", 0, 0);
        Goblin goblin = new Goblin(5, 5);

        mage.useAbility(goblin);
        assert goblin.getHealth() < 50 : "Goblin should take damage from Fireball";

        System.out.println("  PASS: Mage abilities test passed\n");
    }

    private static void testEnemyDefeat() {
        System.out.println("Test 3: Enemy Defeat");
        Goblin goblin = new Goblin(5, 5);
        Warrior warrior = new Warrior("Tester", 0, 0);

        // Damage goblin until dead
        for (int i = 0; i < 10; i++) {
            warrior.useAbility(goblin);
            if (!goblin.isAlive()) break;
        }

        assert !goblin.isAlive() : "Goblin should be dead";
        System.out.println("  PASS: Enemy defeat test passed\n");
    }

    private static void testLevelUp() {
        System.out.println("Test 4: Level Up");
        Warrior warrior = new Warrior("Tester", 0, 0);
        int initialHealth = warrior.getHealth();

        // Gain enough experience to level up
        for (int i = 0; i < 5; i++) {
            Goblin goblin = new Goblin(5, 5);
            goblin.attack(warrior);
        }

        System.out.println("  PASS: Level up test passed\n");
    }
}
```

**Complexity**:

- **Time Complexity**: O(n) for entity updates where n is number of entities
- **Space Complexity**: O(n) for entity storage

**Best Practices**:

- Use template methods for common algorithms with customizable steps
- Implement state machines for complex entity behavior
- Keep overridden methods focused and cohesive
- Use the Liskov Substitution Principle: ensure subclasses are substitutable for parents
- Consider performance implications of dynamic dispatch in hot paths

## Exercises

### Easy

1. **Vehicle Hierarchy**: Create a Vehicle abstract class with start(), stop(), and accelerate() methods. Implement Car, Motorcycle, and Truck subclasses that override these methods.

2. **Shape Drawing**: Design a Shape class with draw() and resize() methods. Implement Circle, Rectangle, and Triangle that override these methods.

3. **Animal Sounds**: Create an Animal class with speak() and move() methods. Implement at least three different animal types with overridden methods.

### Medium

1. **Payment System**: Design a PaymentProcessor with processPayment(), validate(), and generateReceipt() methods. Implement CreditCard, PayPal, and Bitcoin payment methods.

2. **Notification System**: Create a Notification class with send(), format(), and validate() methods. Implement Email, SMS, and Push notification types.

3. **File Processor**: Design a FileProcessor with read(), process(), and write() methods. Implement CSV, JSON, and XML file processors.

### Hard

1. **Game AI System**: Create an AIController with update(), decide(), and execute() methods. Implement different AI behaviors for enemies, NPCs, and allies.

2. **Database Connector**: Design a DatabaseConnector with connect(), query(), and disconnect() methods. Implement MySQL, PostgreSQL, and MongoDB connectors.

3. **Plugin System**: Create a Plugin base class with initialize(), execute(), and shutdown() methods. Implement different plugin types with lifecycle management.

## Interview Questions

### Easy

1. **What is method overriding?**
   Method overriding allows a subclass to provide a specific implementation of a method already defined in its parent class. The method must have the same name, return type, and parameters. It enables runtime polymorphism.

2. **What is the @Override annotation?**
   @Override is a compile-time annotation that indicates a method is intended to override a parent method. It helps catch errors like misspelled method names or incorrect signatures during compilation.

3. **Can we override static methods?**
   No, static methods cannot be overridden because they belong to the class, not instances. However, you can "hide" a static method in a subclass with the same signature, but this is not true overriding.

### Medium

1. **What are the rules for method overriding?**
   Rules include: same method name, same parameters, same or covariant return type, access modifier cannot be more restrictive, cannot throw broader checked exceptions, and the method must be visible to the subclass.

2. **What is covariant return type?**
   Covariant return type allows an overridden method to return a subtype of the return type declared in the parent method. For example, if a parent method returns Object, the child can override it to return String.

3. **How does dynamic dispatch work with overridden methods?**
   At runtime, the JVM determines the actual object type and invokes the most specific method implementation. It looks up the method in the object's vtable (virtual method table) to find the correct implementation.

### Hard

1. **Explain the relationship between method overriding and the Liskov Substitution Principle.**
   LSP states that objects of a superclass should be replaceable with objects of its subclasses without affecting correctness. Method overriding enables this by allowing subclasses to maintain the same method signatures while providing specialized implementations. If overriding breaks LSP, it indicates a design flaw.

2. **How do exceptions interact with method overriding?**
   Overridden methods cannot throw checked exceptions that are broader than those thrown by the parent method. They can throw the same exceptions, narrower exceptions, or no exceptions at all. Runtime exceptions can be thrown without restriction.

## Common Pitfalls

### 1. Forgetting @Override Annotation

**Wrong**:
```java
class Parent {
    public void display() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    // Forgot @Override - method signature mismatch silently creates new method
    public void Display() { // Different capitalization!
        System.out.println("Child");
    }
}

Child child = new Child();
child.display(); // Calls Parent.display(), not Child.Display()!
```

**Right**:
```java
class Parent {
    public void display() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    @Override
    public void display() {
        System.out.println("Child");
    }
}

Child child = new Child();
child.display(); // Correctly calls Child.display()
```

### 2. Changing Access Modifier to More Restrictive

**Wrong**:
```java
class Parent {
    public void method() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    @Override
    void method() { // Compilation error: cannot reduce visibility
        System.out.println("Child");
    }
}
```

**Right**:
```java
class Parent {
    public void method() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    @Override
    public void method() { // Maintains or widens visibility
        System.out.println("Child");
    }
}
```

### 3. Throwing Broader Exceptions

**Wrong**:
```java
class Parent {
    public void method() throws IOException {
        // Implementation
    }
}

class Child extends Parent {
    @Override
    public void method() throws Exception { // Compilation error: broader exception
        // Implementation
    }
}
```

**Right**:
```java
class Parent {
    public void method() throws IOException {
        // Implementation
    }
}

class Child extends Parent {
    @Override
    public void method() throws IOException { // Same or narrower exception
        // Implementation
    }

    // Or throw no exception
    @Override
    public void method() {
        // Implementation that handles the exception
    }
}
```

## Best Practices

1. **Always use @Override annotation**: It helps catch errors at compile time and improves code readability by clearly indicating which methods are overridden.

2. **Maintain the Liskov Substitution Principle**: Ensure that subclasses can replace parent classes without breaking the program. The overridden method should honor the parent's contract.

3. **Keep overridden methods cohesive**: Each overridden method should focus on a single responsibility. Avoid overriding methods just to change one aspect of behavior.

4. **Call super when extending behavior**: If you want to extend parent behavior rather than replace it, call super.methodName() at the beginning of your override.

5. **Document overridden methods**: Explain why the method is overridden and how the behavior differs from the parent implementation.

## Real World Usage

### How Spring Uses This

Spring Framework relies heavily on method overriding:

- **BeanPostProcessor**: Classes override postProcessBeforeInitialization() and postProcessAfterInitialization()
- **AbstractApplicationContext**: Subclasses override refresh() method for context-specific behavior
- **JdbcTemplate**: Subclasses override specific query methods for custom result handling

### How Hibernate Uses This

Hibernate uses overriding for extensibility:

- **SessionImpl**: Overrides Session methods for persistence context management
- **AbstractType**: Subclasses override nullSafeGet() and nullSafeSet() for type mapping
- **BaseSessionEventListener**: Overrides event methods for performance monitoring

### How JDK Uses This

The Java Development Kit uses overriding throughout:

- **AbstractList**: Subclasses override get(), size(), and add() for different list implementations
- **Thread**: Subclasses override run() to define thread behavior
- **InputStream**: Subclasses override read() for different input sources

### Enterprise Usage

In enterprise applications, overriding is used for:

- **Service Implementations**: Base service classes define common behavior, subclasses provide specific logic
- **DAO Patterns**: Abstract DAO classes define CRUD operations, subclasses implement data source-specific logic
- **Template Methods**: Frameworks define algorithms in base classes, applications override specific steps

## References

1. **Effective Java** by Joshua Bloch - Item 19: Design and document for inheritance or else prohibit it
2. **Head First Design Patterns** - Template Method pattern
3. **Java Language Specification** - Method Overrides
4. **Clean Code** by Robert C. Martin - Chapter on object-oriented guidelines
5. **Design Patterns** - Strategy and Template Method patterns

## Summary

- Method overriding allows subclasses to provide specific implementations of parent class methods
- It enables runtime polymorphism where the JVM selects the correct method at runtime
- @Override annotation helps catch errors and improves code clarity
- Overridden methods must follow specific rules (same signature, compatible return type, etc.)
- Method overriding supports the Liskov Substitution Principle
- Template Method pattern leverages overriding to define customizable algorithms

**Next Steps**: [17-dynamic-binding](../17-dynamic-binding/README.md)
