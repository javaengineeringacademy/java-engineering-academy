# Abstract Classes

## Introduction

Abstract classes are a fundamental pillar of object-oriented programming that bridge the gap between concrete classes and interfaces, providing a way to define common behavior while enforcing design contracts that subclasses must fulfill. An abstract class cannot be instantiated directly and may contain both abstract methods (without implementation) and concrete methods (with implementation), making it an ideal mechanism for defining templates that establish a common structure for a family of related classes while allowing each subclass to provide its own specific implementations.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the purpose and benefits of abstract classes compared to concrete classes and interfaces
- [ ] Implement abstract classes with both abstract and concrete methods to define class hierarchies
- [ ] Apply abstract classes to enforce design contracts while providing shared implementations
- [ ] Design effective class hierarchies using abstract classes as templates for related classes

## Prerequisites

- [09-inheritance](../09-inheritance/README.md) - Understanding inheritance hierarchies and the extends keyword
- [11-abstraction](../11-abstraction/README.md) - Core concepts of abstraction and hiding implementation details
- [12-interfaces](../12-interfaces/README.md) - Understanding interfaces and their role in defining contracts
- [05-methods](../05-methods/README.md) - Method declaration, definition, and invocation

## Why This Concept Exists

### The Problem

In real-world software development, you often encounter situations where multiple related classes share common behavior but also need to implement specific behaviors differently. Consider a system with different types of vehicles—all vehicles share some common characteristics (they all have a make, model, and can start/stop), but each type may have different specific behaviors. Without abstract classes, you face several challenges:

1. **Code duplication**: Common behavior must be duplicated across all vehicle classes
2. **No enforcement**: There is no way to ensure that all vehicle classes implement required methods
3. **Inconsistent interfaces**: Without a common template, developers might implement similar methods differently
4. **Design fragility**: Changes to common behavior require modifications across multiple classes

### The Solution

Abstract classes solve these problems by providing a template that:

- Defines the common structure and behavior that all subclasses should have
- Enforces implementation of specific methods through abstract method declarations
- Allows sharing of code through concrete method implementations
- Creates a clear contract that subclasses must fulfill while still providing flexibility

### Real-World Analogy

Think of an abstract class as a **blueprint for a house**. The blueprint defines the overall structure—there must be walls, a roof, doors, and windows—and provides some standard implementations like the foundation and framing. However, the blueprint leaves certain details for the builder to implement: the specific color of the walls, the type of flooring, or the style of fixtures. You cannot live in the blueprint itself (you cannot instantiate an abstract class), but you can use it to build actual houses (concrete subclasses) that all share the same basic structure while having their own unique features.

## Internal Working

### JVM Perspective

When the JVM encounters an abstract class, several important mechanisms come into play:

1. **Compilation Phase**: The compiler validates that abstract methods are declared with the `abstract` keyword and do not have a method body. It also ensures that concrete subclasses implement all abstract methods.

2. **Class Loading**: Abstract classes are loaded into the method area just like concrete classes. The JVM creates a Class object for them, but instantiation is prevented at the bytecode level.

3. **Method Resolution**: When a method is called on a subclass instance, the JVM performs dynamic method dispatch to find the correct implementation. For abstract methods, this means finding the concrete implementation in the subclass hierarchy.

4. **Memory Management**: Abstract classes can have instance variables, but since they cannot be instantiated directly, these variables only exist as part of subclass instances.

### Memory Representation

When a subclass of an abstract class is instantiated:

```
Stack Memory:
┌─────────────────────┐
│ reference variable   │ ──────┐
└─────────────────────┘        │
                               ▼
Heap Memory:            ┌─────────────────────────────┐
                        │ Subclass Instance            │
                        │ ┌─────────────────────────┐ │
                        │ │ Abstract class fields    │ │
                        │ │ - inherited fields       │ │
                        │ └─────────────────────────┘ │
                        │ ┌─────────────────────────┐ │
                        │ │ Subclass-specific fields │ │
                        │ └─────────────────────────┘ │
                        └─────────────────────────────┘
```

### Bytecode Representation

In bytecode, abstract classes and methods are marked with specific flags:

- The `ACC_ABSTRACT` flag is set for abstract classes
- Abstract methods have the `ACC_ABSTRACT` flag and no Code attribute
- The JVM enforces that abstract methods cannot be called directly

## Syntax

### Basic Abstract Class Declaration

```java
public abstract class AbstractClassName {
    // Instance variables
    private String fieldName;

    // Constructor (abstract classes can have constructors)
    public AbstractClassName(String fieldName) {
        this.fieldName = fieldName;
    }

    // Concrete method with implementation
    public void concreteMethod() {
        System.out.println("This method has an implementation");
    }

    // Abstract method without implementation
    public abstract void abstractMethod();

    // Abstract method with return type
    public abstract int calculateValue();
}
```

### Extending Abstract Class

```java
public class ConcreteClass extends AbstractClass {
    public ConcreteClass(String fieldName) {
        super(fieldName);
    }

    // Must implement all abstract methods
    @Override
    public void abstractMethod() {
        System.out.println("Implementation in concrete class");
    }

    @Override
    public int calculateValue() {
        return 42;
    }
}
```

### Abstract Class Implementing Interface

```java
public interface Drawable {
    void draw();
    void resize(double factor);
}

public abstract class AbstractShape implements Drawable {
    protected String color;

    public AbstractShape(String color) {
        this.color = color;
    }

    // Abstract class can provide partial implementation
    @Override
    public void resize(double factor) {
        System.out.println("Resizing by factor: " + factor);
    }

    // Subclass must implement draw() and calculateArea()
    public abstract double calculateArea();
}
```

## Easy Examples

### Example 1: Basic Animal Hierarchy

**Problem Statement**: Create a system with different animals that share common characteristics but have specific behaviors. All animals can eat and sleep, but each animal type makes different sounds and moves differently.

**Implementation**:

```java
package academy.javaengineering.oop.abstractclasses;

abstract class Animal {
    protected String name;
    protected int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void eat() {
        System.out.println(name + " is eating.");
    }

    public void sleep() {
        System.out.println(name + " is sleeping.");
    }

    public String getInfo() {
        return name + " (age: " + age + ")";
    }

    public abstract void makeSound();
    public abstract void move();
}

class Dog extends Animal {
    private String breed;

    public Dog(String name, int age, String breed) {
        super(name, age);
        this.breed = breed;
    }

    @Override
    public void makeSound() {
        System.out.println(name + " says: Woof! Woof!");
    }

    @Override
    public void move() {
        System.out.println(name + " runs on four legs.");
    }

    public void fetch() {
        System.out.println(name + " fetches the ball.");
    }
}

class Bird extends Animal {
    private boolean canFly;

    public Bird(String name, int age, boolean canFly) {
        super(name, age);
        this.canFly = canFly;
    }

    @Override
    public void makeSound() {
        System.out.println(name + " says: Tweet! Tweet!");
    }

    @Override
    public void move() {
        if (canFly) {
            System.out.println(name + " flies in the sky.");
        } else {
            System.out.println(name + " walks on the ground.");
        }
    }
}

public class AnimalDemo {
    public static void main(String[] args) {
        Dog dog = new Dog("Buddy", 3, "Golden Retriever");
        Bird bird = new Bird("Tweety", 2, true);

        dog.eat();
        dog.sleep();
        System.out.println(dog.getInfo());
        dog.makeSound();
        dog.move();
        dog.fetch();

        System.out.println();

        bird.eat();
        bird.sleep();
        bird.makeSound();
        bird.move();
    }
}
```

**Expected Output**:
```
Buddy is eating.
Buddy is sleeping.
Buddy (age: 3)
Buddy says: Woof! Woof!
Buddy runs on four legs.
Buddy fetches the ball.

Tweety is eating.
Tweety is sleeping.
Tweety says: Tweet! Tweet!
Tweety flies in the sky.
```

**Best Practices**:
- Use abstract classes when subclasses share common state and behavior
- Make fields protected in abstract classes to allow subclass access
- Provide sensible default implementations in concrete methods when possible
- Keep abstract methods focused on what subclasses must implement

### Example 2: Payment Processing System

**Problem Statement**: Design a payment processing system where different payment methods share common processing steps but have different specific validation and processing logic.

**Implementation**:

```java
package academy.javaengineering.oop.abstractclasses;

import java.time.LocalDateTime;
import java.util.UUID;

abstract class PaymentProcessor {
    protected String transactionId;
    protected LocalDateTime transactionTime;
    protected double amount;

    public PaymentProcessor(double amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.transactionTime = LocalDateTime.now();
        this.amount = amount;
    }

    public final boolean processPayment() {
        System.out.println("Starting payment processing...");

        if (!validatePayment()) {
            System.out.println("Payment validation failed!");
            return false;
        }

        if (!checkLimits()) {
            System.out.println("Amount exceeds limits!");
            return false;
        }

        boolean success = executePayment();

        if (success) {
            sendConfirmation();
        }

        return success;
    }

    protected abstract boolean validatePayment();
    protected abstract boolean executePayment();

    protected boolean checkLimits() {
        return amount > 0 && amount <= 100000;
    }

    protected void sendConfirmation() {
        System.out.println("Payment confirmation sent for: " + transactionId);
    }

    public double getAmount() {
        return amount;
    }
}

class CreditCardPayment extends PaymentProcessor {
    private String cardNumber;
    private String cvv;

    public CreditCardPayment(double amount, String cardNumber, String cvv) {
        super(amount);
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

    @Override
    protected boolean validatePayment() {
        System.out.println("Validating credit card details...");
        return cardNumber != null && cardNumber.length() == 16
            && cvv != null && cvv.length() == 3;
    }

    @Override
    protected boolean executePayment() {
        System.out.println("Processing credit card payment of $" + amount);
        System.out.println("Card: ****" + cardNumber.substring(12));
        return true;
    }
}

class PayPalPayment extends PaymentProcessor {
    private String email;

    public PayPalPayment(double amount, String email) {
        super(amount);
        this.email = email;
    }

    @Override
    protected boolean validatePayment() {
        System.out.println("Authenticating PayPal account...");
        return email != null && email.contains("@");
    }

    @Override
    protected boolean executePayment() {
        System.out.println("Processing PayPal payment of $" + amount);
        System.out.println("Account: " + email);
        return true;
    }
}

public class PaymentDemo {
    public static void main(String[] args) {
        PaymentProcessor creditCard = new CreditCardPayment(150.00, "1234567890123456", "123");
        PaymentProcessor paypal = new PayPalPayment(75.50, "user@example.com");

        System.out.println("=== Credit Card Payment ===");
        boolean ccSuccess = creditCard.processPayment();
        System.out.println("Success: " + ccSuccess);

        System.out.println("\n=== PayPal Payment ===");
        boolean ppSuccess = paypal.processPayment();
        System.out.println("Success: " + ppSuccess);
    }
}
```

**Expected Output**:
```
=== Credit Card Payment ===
Starting payment processing...
Validating credit card details...
Processing credit card payment of $150.0
Card: ****3456
Payment confirmation sent for: [uuid]
Success: true

=== PayPal Payment ===
Starting payment processing...
Authenticating PayPal account...
Processing PayPal payment of $75.5
Account: user@example.com
Payment confirmation sent for: [uuid]
Success: true
```

**Best Practices**:
- Use the Template Method pattern with abstract classes to define workflows
- Make the template method final to prevent subclasses from altering the workflow
- Keep abstract methods focused on what varies between implementations
- Provide default implementations for optional operations

## Medium Examples

### Example 1: Shape Hierarchy with Area Calculation

**Problem Statement**: Design a geometry system that supports different shapes (Circle, Rectangle, Triangle) with common shape operations but type-specific area and perimeter calculations.

**Requirements**:

- Common shape properties (color, filled status)
- Type-specific area and perimeter calculations
- Comparison support between shapes
- Serialization to string format

**Implementation**:

```java
package academy.javaengineering.oop.abstractclasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class Shape implements Comparable<Shape> {
    protected String color;
    protected boolean filled;

    public Shape(String color, boolean filled) {
        this.color = color;
        this.filled = filled;
    }

    public Shape() {
        this("white", false);
    }

    public abstract double calculateArea();
    public abstract double calculatePerimeter();
    public abstract String getShapeType();

    public void displayInfo() {
        System.out.printf("%s [color=%s, filled=%b, area=%.2f, perimeter=%.2f]%n",
            getShapeType(), color, filled, calculateArea(), calculatePerimeter());
    }

    @Override
    public int compareTo(Shape other) {
        return Double.compare(this.calculateArea(), other.calculateArea());
    }

    @Override
    public String toString() {
        return String.format("%s{color='%s', filled=%b}", getShapeType(), color, filled);
    }

    public String getColor() { return color; }
    public boolean isFilled() { return filled; }
}

class Circle extends Shape {
    private double radius;

    public Circle(double radius, String color, boolean filled) {
        super(color, filled);
        this.radius = radius;
    }

    public Circle(double radius) {
        this(radius, "white", false);
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
    public String getShapeType() {
        return "Circle";
    }

    public double getRadius() { return radius; }
}

class Rectangle extends Shape {
    protected double width;
    protected double height;

    public Rectangle(double width, double height, String color, boolean filled) {
        super(color, filled);
        this.width = width;
        this.height = height;
    }

    public Rectangle(double width, double height) {
        this(width, height, "white", false);
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
    public String getShapeType() {
        return "Rectangle";
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
}

class Square extends Rectangle {
    public Square(double side, String color, boolean filled) {
        super(side, side, color, filled);
    }

    public Square(double side) {
        this(side, "white", false);
    }

    @Override
    public String getShapeType() {
        return "Square";
    }
}

class Triangle extends Shape {
    private double sideA;
    private double sideB;
    private double sideC;

    public Triangle(double sideA, double sideB, double sideC, String color, boolean filled) {
        super(color, filled);
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
    public String getShapeType() {
        return "Triangle";
    }
}

class ShapeComparator {
    public static void sortByArea(List<Shape> shapes) {
        Collections.sort(shapes);
    }

    public static Shape findLargest(List<Shape> shapes) {
        if (shapes.isEmpty()) return null;
        return Collections.max(shapes);
    }

    public static double calculateTotalArea(List<Shape> shapes) {
        return shapes.stream()
            .mapToDouble(Shape::calculateArea)
            .sum();
    }
}

public class ShapeDemo {
    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(5, "red", true));
        shapes.add(new Rectangle(4, 6, "blue", false));
        shapes.add(new Square(3, "green", true));
        shapes.add(new Triangle(3, 4, 5, "yellow", true));

        System.out.println("=== All Shapes ===");
        for (Shape shape : shapes) {
            shape.displayInfo();
        }

        System.out.println("\n=== Sorted by Area ===");
        ShapeComparator.sortByArea(shapes);
        for (Shape shape : shapes) {
            System.out.printf("%s: %.2f%n", shape.getShapeType(), shape.calculateArea());
        }

        System.out.println("\n=== Statistics ===");
        Shape largest = ShapeComparator.findLargest(shapes);
        System.out.println("Largest shape: " + largest.getShapeType());
        System.out.printf("Total area: %.2f%n", ShapeComparator.calculateTotalArea(shapes));
    }
}
```

**Expected Output**:
```
=== All Shapes ===
Circle [color=red, filled=true, area=78.54, perimeter=31.42]
Rectangle [color=blue, filled=false, area=24.00, perimeter=20.00]
Square [color=green, filled=true, area=9.00, perimeter=12.00]
Triangle [color=yellow, filled=true, area=6.00, perimeter=12.00]

=== Sorted by Area ===
Triangle: 6.00
Square: 9.00
Rectangle: 24.00
Circle: 78.54

=== Statistics ===
Largest shape: Circle
Total area: 117.54
```

**Code Walkthrough**:

1. **Abstract Shape Class**: Defines the contract with abstract methods for area, perimeter, and shape type. Implements `Comparable` for sorting support.

2. **Concrete Implementations**: Each shape provides its own area and perimeter calculations based on its geometric properties.

3. **Inheritance Hierarchy**: Square extends Rectangle since a square is a special case of a rectangle.

4. **Polymorphic Usage**: The ShapeComparator works with any Shape subclass through polymorphism.

**Alternative Solution**:

```java
// Using interface with default methods
interface ShapeOperations {
    double calculateArea();
    double calculatePerimeter();

    default void displayInfo() {
        System.out.printf("Area: %.2f, Perimeter: %.2f%n",
            calculateArea(), calculatePerimeter());
    }

    default boolean isLargerThan(ShapeOperations other) {
        return this.calculateArea() > other.calculateArea();
    }
}

// This approach provides more flexibility but less code reuse
```

### Example 2: Notification System

**Problem Statement**: Build a notification system that supports multiple channels (Email, SMS, Push) with common notification management but channel-specific delivery mechanisms.

**Requirements**:

- Common notification lifecycle (create, validate, send, track)
- Channel-specific formatting and delivery
- Retry mechanism for failed deliveries
- Notification history and tracking

**Implementation**:

```java
package academy.javaengineering.oop.abstractclasses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

enum NotificationStatus {
    CREATED, VALIDATING, SENDING, SENT, FAILED, RETRYING
}

abstract class Notification {
    protected String id;
    protected String recipient;
    protected String subject;
    protected String message;
    protected NotificationStatus status;
    protected LocalDateTime createdAt;
    protected LocalDateTime sentAt;
    protected int retryCount;
    protected List<String> deliveryLog;

    public Notification(String recipient, String subject, String message) {
        this.id = UUID.randomUUID().toString();
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.status = NotificationStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.deliveryLog = new ArrayList<>();
        this.retryCount = 0;
    }

    public final boolean send() {
        status = NotificationStatus.VALIDATING;
        log("Validating notification...");

        if (!validate()) {
            status = NotificationStatus.FAILED;
            log("Validation failed!");
            return false;
        }

        status = NotificationStatus.SENDING;
        log("Formatting message...");
        String formattedMessage = formatMessage();

        log("Sending notification...");
        boolean success = deliver(formattedMessage);

        if (success) {
            status = NotificationStatus.SENT;
            sentAt = LocalDateTime.now();
            log("Notification sent successfully!");
        } else {
            status = NotificationStatus.FAILED;
            log("Failed to send notification!");
            retry();
        }

        return success;
    }

    protected abstract boolean validate();
    protected abstract String formatMessage();
    protected abstract boolean deliver(String formattedMessage);

    protected void retry() {
        if (retryCount < 3) {
            retryCount++;
            status = NotificationStatus.RETRYING;
            log("Retrying... (attempt " + retryCount + ")");
            send();
        }
    }

    protected void log(String logMessage) {
        deliveryLog.add("[" + LocalDateTime.now() + "] " + logMessage);
    }

    public String getId() { return id; }
    public String getRecipient() { return recipient; }
    public NotificationStatus getStatus() { return status; }
}

class EmailNotification extends Notification {
    private List<String> ccRecipients;
    private boolean isHtml;

    public EmailNotification(String recipient, String subject, String message) {
        super(recipient, subject, message);
        this.ccRecipients = new ArrayList<>();
        this.isHtml = false;
    }

    public void addCcRecipient(String email) {
        ccRecipients.add(email);
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    @Override
    protected boolean validate() {
        return recipient != null && recipient.contains("@") && subject != null;
    }

    @Override
    protected String formatMessage() {
        StringBuilder sb = new StringBuilder();
        if (isHtml) {
            sb.append("<html><body>");
            sb.append("<h1>").append(subject).append("</h1>");
            sb.append("<p>").append(message).append("</p>");
            sb.append("</body></html>");
        } else {
            sb.append("Subject: ").append(subject).append("\n\n");
            sb.append(message);
        }
        return sb.toString();
    }

    @Override
    protected boolean deliver(String formattedMessage) {
        System.out.println("Sending email to: " + recipient);
        if (!ccRecipients.isEmpty()) {
            System.out.println("CC: " + String.join(", ", ccRecipients));
        }
        System.out.println("Message:\n" + formattedMessage);
        return true;
    }
}

class SMSNotification extends Notification {
    private int maxLength;

    public SMSNotification(String recipient, String message) {
        super(recipient, "SMS", message);
        this.maxLength = 160;
    }

    @Override
    protected boolean validate() {
        return recipient != null && recipient.matches("\\d{10}") && message != null;
    }

    @Override
    protected String formatMessage() {
        if (message.length() > maxLength) {
            return message.substring(0, maxLength - 3) + "...";
        }
        return message;
    }

    @Override
    protected boolean deliver(String formattedMessage) {
        System.out.println("Sending SMS to: " + recipient);
        System.out.println("Message (" + formattedMessage.length() + " chars): " + formattedMessage);
        return true;
    }
}

class PushNotification extends Notification {
    private String deviceToken;
    private String priority;

    public PushNotification(String recipient, String subject, String message, String deviceToken) {
        super(recipient, subject, message);
        this.deviceToken = deviceToken;
        this.priority = "high";
    }

    @Override
    protected boolean validate() {
        return deviceToken != null && !deviceToken.isEmpty();
    }

    @Override
    protected String formatMessage() {
        return String.format("{\"title\":\"%s\",\"body\":\"%s\",\"priority\":\"%s\"}",
            subject, message, priority);
    }

    @Override
    protected boolean deliver(String formattedMessage) {
        System.out.println("Sending push to device: " + deviceToken);
        System.out.println("Payload: " + formattedMessage);
        return true;
    }
}

class NotificationManager {
    private List<Notification> notifications;

    public NotificationManager() {
        this.notifications = new ArrayList<>();
    }

    public void sendNotification(Notification notification) {
        notifications.add(notification);
        notification.send();
    }

    public List<Notification> getNotificationsByStatus(NotificationStatus status) {
        List<Notification> result = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getStatus() == status) {
                result.add(notification);
            }
        }
        return result;
    }
}

public class NotificationDemo {
    public static void main(String[] args) {
        NotificationManager manager = new NotificationManager();

        System.out.println("=== Email Notification ===");
        EmailNotification email = new EmailNotification(
            "user@example.com", "Welcome!", "Thank you for joining us.");
        email.addCcRecipient("admin@example.com");
        email.setHtml(true);
        manager.sendNotification(email);

        System.out.println("\n=== SMS Notification ===");
        SMSNotification sms = new SMSNotification("1234567890", "Your OTP is 123456");
        manager.sendNotification(sms);

        System.out.println("\n=== Push Notification ===");
        PushNotification push = new PushNotification(
            "user123", "New Message", "You have a new message!", "device_token_abc");
        manager.sendNotification(push);
    }
}
```

**Expected Output**:
```
=== Email Notification ===
Sending email to: user@example.com
CC: admin@example.com
Message:
Subject: Welcome!

Thank you for joining us.

=== SMS Notification ===
Sending SMS to: 1234567890
Message (22 chars): Your OTP is 123456

=== Push Notification ===
Sending push to device: device_token_abc
Payload: {"title":"New Message","body":"You have a new message!","priority":"high"}
```

**Code Walkthrough**:

1. **Abstract Notification Class**: Defines the template for all notifications with common fields and the `send()` template method.

2. **Template Method Pattern**: The `send()` method orchestrates the notification workflow, calling abstract methods for validation, formatting, and delivery.

3. **Channel-Specific Implementations**: Each notification type provides its own validation, formatting, and delivery logic.

4. **Concrete Methods**: Common operations like logging and retry mechanisms are implemented in the abstract class.

## Hard Examples

### Example 1: Game Engine Entity System

**Problem Statement**: Design a game engine entity system that supports different entity types (Player, Enemy, NPC) with common entity behaviors but type-specific AI, rendering, and physics interactions.

**Requirements**:

- Common entity lifecycle (initialize, update, render, destroy)
- Type-specific AI behavior
- Physics collision handling
- Component-based architecture
- Event system integration
- Performance optimization with object pooling

**Architecture**:

```
Game Engine
├── Entity System
│   ├── Abstract Entity (base class)
│   │   ├── Player Entity
│   │   ├── Enemy Entity
│   │   └── NPC Entity
│   ├── Component System
│   │   ├── Transform Component
│   │   ├── Physics Component
│   │   └── Render Component
│   └── Event System
│       ├── Collision Events
│       └── Damage Events
```

**Implementation**:

```java
package academy.javaengineering.oop.abstractclasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

interface Component {
    void initialize(Entity entity);
    void update(double deltaTime);
    void render();
    void destroy();
}

class TransformComponent implements Component {
    private double x, y;
    private double velocityX, velocityY;
    private Entity owner;

    public TransformComponent(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void initialize(Entity entity) {
        this.owner = entity;
    }

    @Override
    public void update(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
    }

    @Override
    public void render() {}

    @Override
    public void destroy() {
        owner = null;
    }

    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }
}

class PhysicsComponent implements Component {
    private double mass;
    private boolean isStatic;
    private Entity owner;
    private TransformComponent transform;

    public PhysicsComponent(double mass, boolean isStatic) {
        this.mass = mass;
        this.isStatic = isStatic;
    }

    @Override
    public void initialize(Entity entity) {
        this.owner = entity;
        this.transform = entity.getComponent(TransformComponent.class);
    }

    @Override
    public void update(double deltaTime) {
        if (!isStatic && transform != null) {
            // Apply gravity-like effect
            transform.setVelocity(
                transform.getX() * 0.99,
                transform.getY() + 0.1
            );
        }
    }

    @Override
    public void render() {}

    @Override
    public void destroy() {
        owner = null;
        transform = null;
    }

    public void applyForce(double fx, double fy) {
        if (!isStatic && transform != null) {
            transform.setVelocity(
                transform.getX() + fx / mass,
                transform.getY() + fy / mass
            );
        }
    }
}

class RenderComponent implements Component {
    private String spritePath;
    private int width, height;
    private boolean visible;
    private Entity owner;
    private TransformComponent transform;

    public RenderComponent(String spritePath, int width, int height) {
        this.spritePath = spritePath;
        this.width = width;
        this.height = height;
        this.visible = true;
    }

    @Override
    public void initialize(Entity entity) {
        this.owner = entity;
        this.transform = entity.getComponent(TransformComponent.class);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render() {
        if (visible && transform != null) {
            System.out.printf("Rendering %s at (%.1f, %.1f)%n",
                spritePath, transform.getX(), transform.getY());
        }
    }

    @Override
    public void destroy() {
        owner = null;
        transform = null;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}

class GameEvent {
    enum EventType {
        COLLISION, DAMAGE, SPAWN, DESTROY
    }

    private EventType type;
    private Entity source;
    private Entity target;
    private Map<String, Object> data;

    public GameEvent(EventType type, Entity source, Entity target) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.data = new HashMap<>();
    }

    public EventType getType() { return type; }
    public Entity getSource() { return source; }
    public Entity getTarget() { return target; }
    public Map<String, Object> getData() { return data; }
    public void setData(String key, Object value) { data.put(key, value); }
}

class EventBus {
    private static EventBus instance;
    private Map<GameEvent.EventType, List<EventListener>> listeners;

    private EventBus() {
        listeners = new HashMap<>();
    }

    public static synchronized EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public void subscribe(GameEvent.EventType type, EventListener listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public void publish(GameEvent event) {
        List<EventListener> eventListeners = listeners.get(event.getType());
        if (eventListeners != null) {
            for (EventListener listener : eventListeners) {
                listener.onEvent(event);
            }
        }
    }
}

interface EventListener {
    void onEvent(GameEvent event);
}

abstract class Entity {
    protected String id;
    protected String name;
    protected boolean active;
    protected int health;
    protected int maxHealth;
    protected Map<Class<? extends Component>, Component> components;

    public Entity(String name, int maxHealth) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.active = true;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.components = new HashMap<>();
    }

    public final void initialize() {
        System.out.println("Initializing entity: " + name);
        for (Component component : components.values()) {
            component.initialize(this);
        }
        onInitialize();
        System.out.println("Entity initialized: " + name);
    }

    public final void update(double deltaTime) {
        if (!active) return;
        for (Component component : components.values()) {
            component.update(deltaTime);
        }
        onUpdate(deltaTime);
    }

    public final void render() {
        if (!active) return;
        for (Component component : components.values()) {
            component.render();
        }
        onRender();
    }

    public final void destroy() {
        System.out.println("Destroying entity: " + name);
        for (Component component : components.values()) {
            component.destroy();
        }
        components.clear();
        onDestroy();
        active = false;
    }

    protected abstract void onInitialize();
    protected abstract void onUpdate(double deltaTime);
    protected abstract void onRender();
    protected abstract void onDestroy();
    public abstract void handleCollision(Entity other);

    public <T extends Component> T getComponent(Class<T> componentClass) {
        Component component = components.get(componentClass);
        return componentClass.cast(component);
    }

    public void addComponent(Component component) {
        components.put(component.getClass(), component);
    }

    public void takeDamage(int damage) {
        health -= damage;
        System.out.println(name + " took " + damage + " damage. Health: " + health);
        if (health <= 0) {
            health = 0;
            active = false;
        }
    }

    public String getName() { return name; }
    public boolean isActive() { return active; }
    public int getHealth() { return health; }
}

class Player extends Entity {
    private int experience;
    private int level;
    private int attackPower;

    public Player(String name) {
        super(name, 100);
        this.experience = 0;
        this.level = 1;
        this.attackPower = 10;

        addComponent(new TransformComponent(0, 0));
        addComponent(new PhysicsComponent(1.0, false));
        addComponent(new RenderComponent("player.png", 32, 32));
    }

    @Override
    protected void onInitialize() {
        System.out.println("Player " + name + " ready for action!");
    }

    @Override
    protected void onUpdate(double deltaTime) {}

    @Override
    protected void onRender() {}

    @Override
    protected void onDestroy() {
        System.out.println("Game Over! Player " + name + " has been defeated.");
    }

    @Override
    public void handleCollision(Entity other) {
        System.out.println(name + " collided with " + other.getName());
        if (other instanceof Enemy) {
            attack((Enemy) other);
        }
    }

    public void attack(Enemy enemy) {
        System.out.println(name + " attacks " + enemy.getName() + " for " + attackPower + " damage!");
        enemy.takeDamage(attackPower);
        gainExperience(10);
    }

    public void gainExperience(int exp) {
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
}

class Enemy extends Entity {
    private int attackPower;
    private Random random;

    public Enemy(String name, int maxHealth, int attackPower) {
        super(name, maxHealth);
        this.attackPower = attackPower;
        this.random = new Random();

        addComponent(new TransformComponent(10, 0));
        addComponent(new PhysicsComponent(1.0, false));
        addComponent(new RenderComponent("enemy.png", 32, 32));
    }

    @Override
    protected void onInitialize() {
        System.out.println("Enemy " + name + " spawned!");
    }

    @Override
    protected void onUpdate(double deltaTime) {
        TransformComponent transform = getComponent(TransformComponent.class);
        if (transform != null && random.nextDouble() < 0.01) {
            double dx = random.nextDouble() * 2 - 1;
            transform.translate(dx, 0);
        }
    }

    @Override
    protected void onRender() {}

    @Override
    protected void onDestroy() {
        System.out.println("Enemy " + name + " defeated!");
    }

    @Override
    public void handleCollision(Entity other) {
        System.out.println(name + " collided with " + other.getName());
        if (other instanceof Player) {
            System.out.println(name + " attacks Player for " + attackPower + " damage!");
            other.takeDamage(attackPower);
        }
    }
}

class GameEngine {
    private List<Entity> entities;
    private boolean running;

    public GameEngine() {
        this.entities = new ArrayList<>();
        this.running = false;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
        entity.initialize();
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        entity.destroy();
    }

    public void start() {
        running = true;
        System.out.println("Game started!");
    }

    public void update() {
        if (!running) return;
        double deltaTime = 0.016;
        for (Entity entity : new ArrayList<>(entities)) {
            if (entity.isActive()) {
                entity.update(deltaTime);
            }
        }
        entities.removeIf(e -> !e.isActive());
    }

    public void render() {
        if (!running) return;
        System.out.println("\n--- Rendering Frame ---");
        for (Entity entity : entities) {
            if (entity.isActive()) {
                entity.render();
            }
        }
    }

    public void stop() {
        running = false;
        System.out.println("Game stopped!");
    }
}

public class GameEngineDemo {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.start();

        Player player = new Player("Hero");
        Enemy enemy = new Enemy("Goblin", 30, 5);

        engine.addEntity(player);
        engine.addEntity(enemy);

        System.out.println("\n=== Game Loop ===");
        for (int i = 0; i < 3; i++) {
            System.out.println("\nFrame " + (i + 1) + ":");
            engine.update();
            engine.render();
        }

        System.out.println("\n=== Combat ===");
        player.attack(enemy);
        player.attack(enemy);
        player.attack(enemy);

        engine.stop();
    }
}
```

**Execution Flow**:

1. **Entity Creation**: Player and Enemy entities are created with their components
2. **Initialization**: Each entity initializes its components
3. **Game Loop**: The game engine updates all entities each frame
4. **Combat**: Player attacks enemy, reducing its health
5. **Destruction**: When health reaches zero, entities are destroyed

**Unit Tests**:

```java
public class EntitySystemTest {
    public static void main(String[] args) {
        System.out.println("=== Running Entity System Tests ===\n");

        testPlayerCreation();
        testEnemyCreation();
        testCombatSystem();
        testComponentSystem();

        System.out.println("\n=== All Tests Passed ===");
    }

    private static void testPlayerCreation() {
        System.out.println("Test 1: Player Creation");
        Player player = new Player("Hero");
        player.initialize();

        assert player.getName().equals("Hero") : "Player name should be Hero";
        assert player.getHealth() == 100 : "Player health should be 100";
        assert player.isActive() : "Player should be active";

        System.out.println("  PASS: Player creation test passed\n");
    }

    private static void testEnemyCreation() {
        System.out.println("Test 2: Enemy Creation");
        Enemy enemy = new Enemy("Goblin", 30, 5);
        enemy.initialize();

        assert enemy.getName().equals("Goblin") : "Enemy name should be Goblin";
        assert enemy.getHealth() == 30 : "Enemy health should be 30";

        System.out.println("  PASS: Enemy creation test passed\n");
    }

    private static void testCombatSystem() {
        System.out.println("Test 3: Combat System");
        Player player = new Player("Warrior");
        Enemy enemy = new Enemy("Orc", 30, 5);

        player.initialize();
        enemy.initialize();

        int initialHealth = enemy.getHealth();
        player.attack(enemy);

        assert enemy.getHealth() == initialHealth - 10 : "Enemy should take damage";

        System.out.println("  PASS: Combat system test passed\n");
    }

    private static void testComponentSystem() {
        System.out.println("Test 4: Component System");
        Player player = new Player("TestPlayer");
        player.initialize();

        TransformComponent transform = player.getComponent(TransformComponent.class);
        assert transform != null : "Player should have transform";

        transform.translate(5, 0);
        assert transform.getX() == 5.0 : "Transform X should be 5.0";

        PhysicsComponent physics = player.getComponent(PhysicsComponent.class);
        assert physics != null : "Player should have physics component";

        System.out.println("  PASS: Component system test passed\n");
    }
}
```

**Complexity**:

- **Time Complexity**: O(n) for entity updates where n is the number of entities
- **Space Complexity**: O(n) for entity storage, O(1) for component access

**Best Practices**:

- Use abstract classes to define common entity behavior while allowing type-specific implementation
- Implement the Template Method pattern for lifecycle management
- Use composition (components) alongside inheritance for flexible entity design
- Keep abstract methods focused on what each entity type must uniquely implement
- Consider using object pooling for frequently created/destroyed entities

## Exercises

### Easy

1. **Shape Calculator**: Create an abstract class `Shape` with abstract methods `calculateArea()` and `calculatePerimeter()`. Implement concrete classes `Circle`, `Rectangle`, and `Triangle`.

2. **Vehicle Hierarchy**: Design an abstract class `Vehicle` with abstract methods `start()`, `stop()`, and `accelerate()`. Create concrete classes `Car`, `Motorcycle`, and `Truck`.

3. **Employee System**: Create an abstract class `Employee` with abstract method `calculateSalary()`. Implement `FullTimeEmployee`, `PartTimeEmployee`, and `ContractEmployee` classes.

### Medium

1. **File Processor**: Design an abstract class `FileProcessor` with abstract methods `read()`, `process()`, and `write()`. Implement processors for CSV, JSON, and XML files.

2. **Game Character**: Create an abstract class `GameCharacter` with abstract methods `attack()`, `defend()`, and `useSpecialAbility()`. Implement `Warrior`, `Mage`, and `Archer` classes.

3. **Notification Service**: Design an abstract class `NotificationService` with abstract methods `send()`, `validate()`, and `track()`. Implement services for Email, SMS, and Push notifications.

### Hard

1. **Plugin System**: Design an abstract class `Plugin` with lifecycle methods. Implement a plugin manager that can load, initialize, and execute plugins dynamically.

2. **Database Migration**: Create an abstract class `Migration` with methods for `up()` and `down()`. Implement a migration runner that handles dependencies and rollbacks.

3. **Game AI System**: Design an abstract class `AIController` with methods for `think()`, `decide()`, and `act()`. Implement AI controllers for different enemy types with pathfinding and behavior trees.

## Interview Questions

### Easy

1. **What is an abstract class?**
   An abstract class is a class that cannot be instantiated and may contain both abstract methods (without implementation) and concrete methods (with implementation). It serves as a template for other classes to extend.

2. **Can an abstract class have a constructor?**
   Yes, abstract classes can have constructors. Although you cannot instantiate an abstract class directly, its constructor is called when a subclass is instantiated using `super()`.

3. **What is the difference between an abstract class and an interface?**
   Abstract classes can have constructors, instance variables, and both abstract and concrete methods. Interfaces (prior to Java 8) could only have abstract methods and constants. Classes can extend only one abstract class but implement multiple interfaces.

### Medium

1. **When should you use an abstract class instead of an interface?**
   Use an abstract class when you need to share code among closely related classes, when classes in the same hierarchy should have common state, or when you need to provide default implementations. Use interfaces when you need to define a contract that unrelated classes can implement.

2. **Can an abstract class implement an interface?**
   Yes, an abstract class can implement an interface. It can provide implementations for some methods while leaving others abstract, which must then be implemented by concrete subclasses.

3. **What are the benefits of using abstract classes in large applications?**
   Abstract classes promote code reuse, enforce design contracts, provide a clear hierarchy, support the Template Method pattern, and make the codebase more maintainable by centralizing common behavior.

### Hard

1. **How do abstract classes relate to the SOLID principles?**
   Abstract classes support the Open/Closed Principle by allowing extension without modification. They support the Liskov Substitution Principle when properly designed. However, they can violate the Interface Segregation Principle if they become too large, and the Dependency Inversion Principle if concrete classes depend on concrete abstractions rather than interfaces.

2. **What are the performance implications of using abstract classes?**
   Abstract classes have minimal performance overhead. Method calls through abstract class references use dynamic dispatch (virtual method table lookup), which is slightly slower than static dispatch but highly optimized in modern JVMs. The main cost is in object creation when instantiating subclasses.

## Common Pitfalls

### 1. Trying to Instantiate an Abstract Class

**Wrong**:
```java
abstract class Animal {
    public abstract void makeSound();
}

// This will cause a compilation error
Animal animal = new Animal();
animal.makeSound();
```

**Right**:
```java
abstract class Animal {
    public abstract void makeSound();
}

class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }
}

// Create an instance of a concrete subclass
Animal animal = new Dog();
animal.makeSound();
```

### 2. Forgetting to Implement All Abstract Methods

**Wrong**:
```java
abstract class Shape {
    public abstract double calculateArea();
    public abstract double calculatePerimeter();
}

// Compilation error: missing implementations
class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    // Missing calculatePerimeter() implementation
}
```

**Right**:
```java
abstract class Shape {
    public abstract double calculateArea();
    public abstract double calculatePerimeter();
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

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }
}
```

### 3. Overusing Abstract Classes When Interfaces Would Be Better

**Wrong**:
```java
// Creating an abstract class just for defining a contract
abstract class Printable {
    public abstract void print();
    public abstract String format();
}

abstract class Loggable {
    public abstract void log();
    public abstract String getLogMessage();
}

// A class that needs multiple contracts cannot extend both
class Document extends Printable { // Cannot also extend Loggable
    // ...
}
```

**Right**:
```java
// Use interfaces for contracts
interface Printable {
    void print();
    String format();
}

interface Loggable {
    void log();
    String getLogMessage();
}

// A class can implement multiple interfaces
class Document implements Printable, Loggable {
    @Override
    public void print() {
        System.out.println("Printing document");
    }

    @Override
    public String format() {
        return "Formatted document";
    }

    @Override
    public void log() {
        System.out.println("Logging document");
    }

    @Override
    public String getLogMessage() {
        return "Document log entry";
    }
}
```

## Best Practices

1. **Use abstract classes when you need to share code among closely related classes**: Abstract classes are ideal when subclasses share common state and behavior. Use them to avoid code duplication while still allowing specialization.

2. **Keep abstract classes focused and cohesive**: Each abstract class should represent a single concept or responsibility. Avoid creating "god" abstract classes that try to do too much.

3. **Use the Template Method pattern**: Define the skeleton of an algorithm in an abstract class, letting subclasses implement specific steps. Make the template method final to prevent alteration.

4. **Provide clear documentation for abstract methods**: Document what subclasses are expected to implement and any contracts they must follow. This helps maintain consistency across implementations.

5. **Consider using interfaces for contracts**: When you need to define a contract without any shared state or implementation, interfaces are often a better choice than abstract classes.

## Real World Usage

### How Spring Uses This

Spring Framework extensively uses abstract classes to provide base implementations for various components:

- **AbstractController**: Base class for Spring MVC controllers with common handler mapping logic
- **AbstractTransactionalDataSourceSpringContextTests**: Base class for integration tests with transaction management
- **AbstractBeanFactory**: Provides common bean factory functionality that specific factories extend

### How Hibernate Uses This

Hibernate ORM uses abstract classes for entity mapping and persistence:

- **AbstractEntityType**: Base class for entity type implementations
- **AbstractComponentType**: Base class for component type mappings
- **AbstractSessionImpl**: Base class providing common session functionality

### How JDK Uses This

The Java Development Kit uses abstract classes throughout its core libraries:

- **AbstractList, AbstractSet, AbstractMap**: Base classes for collection implementations
- **AbstractStringBuilder**: Base class for StringBuilder and StringBuffer
- **AbstractMap.SimpleEntry**: Base implementation for map entries

### Enterprise Usage

In enterprise applications, abstract classes are commonly used for:

- **Base Entity Classes**: Common audit fields (createdAt, updatedAt, createdBy)
- **Service Templates**: Template Method pattern for business process workflows
- **DAO Patterns**: Abstract data access objects with common CRUD operations
- **Plugin Architectures**: Defining plugin lifecycle and extension points

## References

1. **Effective Java** by Joshua Bloch - Item 18: Prefer interfaces to abstract classes
2. **Head First Design Patterns** - Template Method pattern chapter
3. **Java SE Documentation** - Abstract Classes and Methods
4. **Design Patterns: Elements of Reusable Object-Oriented Software** - Template Method pattern
5. **Clean Code** by Robert C. Martin - Chapter on smell and heuristics related to class design

## Summary

- Abstract classes cannot be instantiated and serve as templates for other classes
- They can contain both abstract methods (no implementation) and concrete methods (with implementation)
- Abstract classes enforce design contracts while allowing code reuse through inheritance
- The Template Method pattern leverages abstract classes to define algorithm skeletons
- Use abstract classes when related classes share common state and behavior
- Consider interfaces when you need multiple inheritance of type without shared state

**Next Steps**: [14-object-class](../14-object-class/README.md)
