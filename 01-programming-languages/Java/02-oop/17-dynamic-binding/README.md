# Dynamic Binding

## Introduction

Dynamic binding, also known as late binding or runtime polymorphism, is the mechanism by which the JVM determines which method implementation to invoke at runtime based on the actual object type rather than the reference type, enabling flexible and extensible code through method overriding and the ability to write programs that work with entire class hierarchies rather than specific concrete classes. This fundamental concept of object-oriented programming allows Java to support polymorphic behavior where a single method call can result in different implementations depending on the actual object being referenced, making it possible to write code that is both generic and type-safe. Dynamic binding is the foundation for many design patterns and is essential for creating maintainable, extensible applications that can evolve over time without requiring changes to existing client code.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand how dynamic binding works at the JVM level through virtual method tables
- [ ] Distinguish between dynamic binding (runtime) and static binding (compile-time)
- [ ] Apply dynamic binding to implement polymorphic behavior in class hierarchies
- [ ] Recognize the performance implications and optimization techniques for dynamic dispatch

## Prerequisites

- [10-polymorphism](../10-polymorphism/README.md) - Core concepts of polymorphism and its benefits
- [16-method-overriding](../16-method-overriding/README.md) - How subclasses override parent methods
- [09-inheritance](../09-inheritance/README.md) - Class hierarchies and inheritance relationships
- [05-methods](../05-methods/README.md) - Method declaration, invocation, and resolution

## Why This Concept Exists

### The Problem

Without dynamic binding, you would be forced to use the reference type to determine which method to call:

```java
// Without dynamic binding - broken polymorphism
class Animal {
    public void speak() {
        System.out.println("Animal speaks");
    }
}

class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("Dog barks");
    }
}

Animal animal = new Dog();
animal.speak(); // Without dynamic binding: prints "Animal speaks"
// This defeats the purpose of inheritance!
```

This approach has several critical issues:

1. **Broken polymorphism**: Objects cannot behave according to their actual type
2. **Rigid code**: Client code must know concrete types at compile time
3. **Extensibility problems**: Adding new types requires modifying existing code
4. **Violation of OCP**: The Open/Closed Principle cannot be followed

### The Solution

Dynamic binding solves these problems by:

- Determining the method to call based on the runtime object type
- Enabling true polymorphism where objects behave according to their actual class
- Allowing client code to work with abstract types while supporting new implementations
- Supporting the Liskov Substitution Principle

### Real-World Analogy

Think of dynamic binding as a **universal remote control**. The remote doesn't know what specific TV brand you have, but when you press the "power" button, it sends a signal that each TV brand interprets in its own way. The remote works with any TV because the action (pressing power) is the same, but the implementation (how each TV responds) varies. Similarly, dynamic binding allows code to call methods on objects without knowing their exact type, letting each class respond in its own way.

## Internal Working

### JVM Perspective

Dynamic binding is implemented through the Virtual Method Table (vtable) mechanism:

1. **Virtual Method Table (vtable)**: Each class has a table that maps method signatures to their implementations. When a method is called, the JVM looks up the implementation in the vtable of the actual object type.

2. **Method Resolution**: The JVM traverses the class hierarchy to find the most specific implementation of the called method.

3. **Performance Optimization**: Modern JVMs use inline caching and just-in-time compilation to minimize the overhead of dynamic dispatch.

4. **Final Methods**: Methods declared as `final` cannot be overridden and can be statically bound, providing performance benefits.

### Memory Representation

```
Class Hierarchy and vtables:

Animal Class:
┌─────────────────────────────────┐
│ Virtual Method Table            │
│ ├── speak() → Animal.speak     │
│ └── toString() → Object.toString│
└─────────────────────────────────┘

Dog Class (extends Animal):
┌─────────────────────────────────┐
│ Virtual Method Table            │
│ ├── speak() → Dog.speak        │  ← Overridden
│ ├── toString() → Object.toString│
│ └── fetch() → Dog.fetch        │  ← New method
└─────────────────────────────────┘

Cat Class (extends Animal):
┌─────────────────────────────────┐
│ Virtual Method Table            │
│ ├── speak() → Cat.speak        │  ← Overridden
│ └── toString() → Object.toString│
└─────────────────────────────────┘

At Runtime:
Animal ref = new Dog();
ref.speak();
↓
JVM checks actual type: Dog
↓
Looks up Dog's vtable for speak()
↓
Invokes Dog.speak()
```

### Dynamic Dispatch Algorithm

1. **Compile Time**: Compiler verifies method exists in reference type
2. **Runtime**: JVM identifies actual object type from object header
3. **Vtable Lookup**: JVM finds the method in the object's vtable
4. **Method Invocation**: JVM invokes the most specific implementation found

## Syntax

### Basic Dynamic Binding

```java
class Animal {
    public void speak() {
        System.out.println("Animal speaks");
    }
}

class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("Dog barks");
    }
}

// Dynamic binding in action
Animal animal = new Dog();
animal.speak(); // Output: Dog barks (resolved at runtime)
```

### Dynamic Binding with Parameters

```java
class Shape {
    public double area() {
        return 0;
    }
}

class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

// Method accepting parent type, works with any subclass
public static void printArea(Shape shape) {
    System.out.println("Area: " + shape.area());
}

Shape shape = new Circle(5);
printArea(shape); // Dynamic binding resolves to Circle.area()
```

### Dynamic Binding in Collections

```java
List<Animal> animals = new ArrayList<>();
animals.add(new Dog());
animals.add(new Cat());
animals.add(new Bird());

for (Animal animal : animals) {
    animal.speak(); // Each animal speaks according to its type
}
```

### Dynamic Binding with Interface

```java
interface Drawable {
    void draw();
}

class Circle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing circle");
    }
}

Drawable drawable = new Circle();
drawable.draw(); // Dynamic binding resolves to Circle.draw()
```

## Easy Examples

### Example 1: Notification System with Dynamic Dispatch

**Problem Statement**: Create a notification system where different notification types (Email, SMS, Push) are processed through a common interface, demonstrating how dynamic binding selects the appropriate implementation at runtime.

**Implementation**:

```java
package academy.javaengineering.oop.dynamicbinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class Notification {
    protected String recipient;
    protected String message;
    protected LocalDateTime timestamp;

    public Notification(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public abstract void send();
    public abstract String getType();
    public abstract boolean isUrgent();

    public void display() {
        System.out.printf("[%s] %s to %s: %s%n",
            getType(), timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
            recipient, message);
    }

    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
}

class EmailNotification extends Notification {
    private String subject;
    private boolean hasAttachment;

    public EmailNotification(String recipient, String message, String subject, boolean hasAttachment) {
        super(recipient, message);
        this.subject = subject;
        this.hasAttachment = hasAttachment;
    }

    @Override
    public void send() {
        System.out.println("Sending email to " + recipient);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + message);
        if (hasAttachment) {
            System.out.println("Attaching file...");
        }
        System.out.println("Email sent successfully!");
    }

    @Override
    public String getType() {
        return "EMAIL";
    }

    @Override
    public boolean isUrgent() {
        return false;
    }

    public String getSubject() { return subject; }
}

class SMSNotification extends Notification {
    private int maxCharacters;

    public SMSNotification(String recipient, String message, int maxCharacters) {
        super(recipient, message);
        this.maxCharacters = maxCharacters;
    }

    @Override
    public void send() {
        String smsMessage = message;
        if (message.length() > maxCharacters) {
            smsMessage = message.substring(0, maxCharacters - 3) + "...";
            System.out.println("Message truncated to " + maxCharacters + " characters");
        }
        System.out.println("Sending SMS to " + recipient);
        System.out.println("Message: " + smsMessage);
        System.out.println("SMS sent successfully!");
    }

    @Override
    public String getType() {
        return "SMS";
    }

    @Override
    public boolean isUrgent() {
        return true;
    }

    public int getMaxCharacters() { return maxCharacters; }
}

class PushNotification extends Notification {
    private String deviceToken;
    private String priority;

    public PushNotification(String recipient, String message, String deviceToken, String priority) {
        super(recipient, message);
        this.deviceToken = deviceToken;
        this.priority = priority;
    }

    @Override
    public void send() {
        System.out.println("Sending push notification to device: " + deviceToken);
        System.out.println("Priority: " + priority);
        System.out.println("Payload: " + message);
        System.out.println("Push notification sent successfully!");
    }

    @Override
    public String getType() {
        return "PUSH";
    }

    @Override
    public boolean isUrgent() {
        return "high".equals(priority);
    }

    public String getDeviceToken() { return deviceToken; }
}

class NotificationService {
    public void processNotification(Notification notification) {
        System.out.println("Processing " + notification.getType() + " notification...");
        notification.display();
        notification.send();
        System.out.println();
    }

    public void processBatch(Notification[] notifications) {
        System.out.println("=== Batch Processing ===");
        for (Notification notification : notifications) {
            processNotification(notification);
        }
    }

    public void sendUrgentOnly(Notification[] notifications) {
        System.out.println("=== Urgent Notifications Only ===");
        for (Notification notification : notifications) {
            if (notification.isUrgent()) {
                processNotification(notification);
            }
        }
    }
}

public class NotificationDemo {
    public static void main(String[] args) {
        NotificationService service = new NotificationService();

        Notification[] notifications = {
            new EmailNotification("user@example.com", "Welcome!", "Welcome to our service", false),
            new SMSNotification("1234567890", "Your OTP is 123456", 160),
            new PushNotification("device123", "New message available", "token_abc", "high"),
            new EmailNotification("admin@example.com", "Report ready", "Monthly Report", true)
        };

        // Process all notifications - dynamic binding selects correct implementation
        service.processBatch(notifications);

        // Only process urgent notifications
        service.sendUrgentOnly(notifications);
    }
}
```

**Expected Output**:
```
=== Batch Processing ===
Processing EMAIL notification...
[EMAIL] 10:30:00 to user@example.com: Welcome!
Sending email to user@example.com

---

## Continue Reading

- [Part 2](README-part2.md)
- [Part 3](README-part3.md)
- [Part 4](README-part4.md)
