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
Subject: Welcome!
Body: Welcome to our service
Email sent successfully!

Processing SMS notification...
[SMS] 10:30:00 to 1234567890: Your OTP is 123456
Sending SMS to 1234567890
Message: Your OTP is 123456
SMS sent successfully!

Processing PUSH notification...
[PUSH] 10:30:00 to device123: New message available
Sending push notification to device: token_abc
Priority: high
Payload: New message available
Push notification sent successfully!

Processing EMAIL notification...
[EMAIL] 10:30:00 to admin@example.com: Report ready
Sending email to admin@example.com
Subject: Monthly Report
Body: Report ready
Attaching file...
Email sent successfully!

=== Urgent Notifications Only ===
Processing SMS notification...
[SMS] 10:30:00 to 1234567890: Your OTP is 123456
...

Processing PUSH notification...
[PUSH] 10:30:00 to device123: New message available
...
```

**Best Practices**:
- Use abstract classes or interfaces to define the common type for dynamic binding
- Keep the common interface focused and cohesive
- Document the expected behavior of overridden methods
- Consider using the Template Method pattern for common algorithms

### Example 2: Shape Drawing System

**Problem Statement**: Create a shape drawing system that uses dynamic binding to draw different shapes through a common Shape interface, demonstrating polymorphic behavior in rendering.

**Implementation**:

```java
package academy.javaengineering.oop.dynamicbinding;

import java.util.ArrayList;
import java.util.List;

interface Drawable {
    void draw();
    String getDescription();
    double getArea();
}

class Circle implements Drawable {
    private double radius;
    private String color;

    public Circle(double radius, String color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void draw() {
        System.out.println("Drawing " + color + " circle with radius " + radius);
        // Simulate drawing with ASCII
        for (int i = 0; i < 5; i++) {
            System.out.println("  " + " ".repeat(5 - i) + "*".repeat(2 * i + 1));
        }
    }

    @Override
    public String getDescription() {
        return "Circle (r=" + radius + ", color=" + color + ")";
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    public double getRadius() { return radius; }
    public String getColor() { return color; }
}

class Rectangle implements Drawable {
    private double width;
    private double height;
    private String color;

    public Rectangle(double width, double height, String color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public void draw() {
        System.out.println("Drawing " + color + " rectangle " + width + "x" + height);
        // Simulate drawing with ASCII
        for (int i = 0; i < 3; i++) {
            System.out.println("  " + "*".repeat(10));
        }
    }

    @Override
    public String getDescription() {
        return "Rectangle (w=" + width + ", h=" + height + ", color=" + color + ")";
    }

    @Override
    public double getArea() {
        return width * height;
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getColor() { return color; }
}

class Triangle implements Drawable {
    private double base;
    private double height;
    private String color;

    public Triangle(double base, double height, String color) {
        this.base = base;
        this.height = height;
        this.color = color;
    }

    @Override
    public void draw() {
        System.out.println("Drawing " + color + " triangle (base=" + base + ", height=" + height + ")");
        // Simulate drawing with ASCII
        for (int i = 0; i < 5; i++) {
            System.out.println("  " + " ".repeat(5 - i) + "*");
        }
    }

    @Override
    public String getDescription() {
        return "Triangle (b=" + base + ", h=" + height + ", color=" + color + ")";
    }

    @Override
    public double getArea() {
        return 0.5 * base * height;
    }

    public double getBase() { return base; }
    public double getHeight() { return height; }
    public String getColor() { return color; }
}

class Canvas {
    private List<Drawable> shapes;
    private String title;

    public Canvas(String title) {
        this.title = title;
        this.shapes = new ArrayList<>();
    }

    public void addShape(Drawable shape) {
        shapes.add(shape);
    }

    public void drawAll() {
        System.out.println("=== Canvas: " + title + " ===");
        for (Drawable shape : shapes) {
            shape.draw(); // Dynamic binding selects correct draw() implementation
            System.out.println("Description: " + shape.getDescription());
            System.out.printf("Area: %.2f%n%n", shape.getArea());
        }
    }

    public double calculateTotalArea() {
        double total = 0;
        for (Drawable shape : shapes) {
            total += shape.getArea(); // Dynamic binding for area calculation
        }
        return total;
    }

    public void drawByType(Class<? extends Drawable> type) {
        System.out.println("=== Drawing " + type.getSimpleName() + "s only ===");
        for (Drawable shape : shapes) {
            if (type.isInstance(shape)) {
                shape.draw();
            }
        }
    }
}

public class ShapeDrawingDemo {
    public static void main(String[] args) {
        Canvas canvas = new Canvas("My Art Gallery");

        // Add shapes
        canvas.addShape(new Circle(5, "red"));
        canvas.addShape(new Rectangle(8, 4, "blue"));
        canvas.addShape(new Triangle(6, 4, "green"));
        canvas.addShape(new Circle(3, "yellow"));

        // Draw all shapes - dynamic binding in action
        canvas.drawAll();

        // Calculate total area
        System.out.printf("Total area of all shapes: %.2f%n", canvas.calculateTotalArea());

        // Draw only circles
        canvas.drawByType(Circle.class);
    }
}
```

**Expected Output**:
```
=== Canvas: My Art Gallery ===
Drawing red circle with radius 5.0
  *****
  *******
  *********
  ***********
  *************
Description: Circle (r=5.0, color=red)
Area: 78.54

Drawing blue rectangle 8.0x4.0
  **********
  **********
  **********
Description: Rectangle (w=8.0, h=4.0, color=blue)
Area: 32.00

Drawing green triangle (base=6.0, height=4.0)
  *
   *
    *
     *
      *
Description: Triangle (b=6.0, h=4.0, color=green)
Area: 12.00

Drawing yellow circle with radius 3.0
  *
  ***
  *****
Description: Circle (r=3.0, color=yellow)
Area: 28.27

Total area of all shapes: 150.81

=== Drawing Circles only ===
Drawing red circle with radius 5.0
  ...
Drawing yellow circle with radius 3.0
  ...
```

**Best Practices**:
- Program to an interface, not an implementation
- Use dynamic binding to enable flexible, extensible designs
- Keep the common interface focused on essential operations
- Document the contract that implementations must follow

## Medium Examples

### Example 1: Strategy Pattern with Dynamic Binding

**Problem Statement**: Implement a sorting system that uses dynamic binding to select different sorting algorithms at runtime, demonstrating how strategy pattern uses polymorphic behavior.

**Requirements**:

- Support multiple sorting algorithms (Bubble Sort, Quick Sort, Merge Sort)
- Allow runtime selection of sorting strategy
- Measure performance of different algorithms
- Support custom comparators

**Implementation**:

```java
package academy.javaengineering.oop.dynamicbinding;

import java.util.*;

interface SortStrategy {
    <T> void sort(List<T> list, Comparator<T> comparator);
    String getName();
    default <T> void sort(List<T> list) {
        sort(list, (a, b) -> ((Comparable<T>) a).compareTo(b));
    }
}

class BubbleSortStrategy implements SortStrategy {
    @Override
    public <T> void sort(List<T> list, Comparator<T> comparator) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Bubble Sort";
    }
}

class QuickSortStrategy implements SortStrategy {
    @Override
    public <T> void sort(List<T> list, Comparator<T> comparator) {
        quickSort(list, 0, list.size() - 1, comparator);
    }

    private <T> void quickSort(List<T> list, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int pivotIndex = partition(list, low, high, comparator);
            quickSort(list, low, pivotIndex - 1, comparator);
            quickSort(list, pivotIndex + 1, high, comparator);
        }
    }

    private <T> int partition(List<T> list, int low, int high, Comparator<T> comparator) {
        T pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) < 0) {
                i++;
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        T temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1;
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }
}

class MergeSortStrategy implements SortStrategy {
    @Override
    public <T> void sort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) return;

        int mid = list.size() / 2;
        List<T> left = new ArrayList<>(list.subList(0, mid));
        List<T> right = new ArrayList<>(list.subList(mid, list.size()));

        sort(left, comparator);
        sort(right, comparator);

        merge(list, left, right, comparator);
    }

    private <T> void merge(List<T> result, List<T> left, List<T> right, Comparator<T> comparator) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.set(k++, left.get(i++));
            } else {
                result.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            result.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            result.set(k++, right.get(j++));
        }
    }

    @Override
    public String getName() {
        return "Merge Sort";
    }
}

class Sorter {
    private SortStrategy strategy;

    public Sorter(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public <T> void sort(List<T> list, Comparator<T> comparator) {
        System.out.println("Sorting with " + strategy.getName() + "...");
        long startTime = System.nanoTime();
        strategy.sort(list, comparator);
        long endTime = System.nanoTime();
        System.out.printf("Completed in %.4f ms%n", (endTime - startTime) / 1_000_000.0);
    }

    public <T> void sort(List<T> list) {
        sort(list, (a, b) -> ((Comparable<T>) a).compareTo(b));
    }

    public String getStrategyName() {
        return strategy.getName();
    }
}

public class SortStrategyDemo {
    public static void main(String[] args) {
        // Create test data
        List<Integer> numbers = new ArrayList<>(Arrays.asList(64, 34, 25, 12, 22, 11, 90));
        System.out.println("Original list: " + numbers);

        // Test different strategies
        SortStrategy[] strategies = {
            new BubbleSortStrategy(),
            new QuickSortStrategy(),
            new MergeSortStrategy()
        };

        for (SortStrategy strategy : strategies) {
            System.out.println("\n=== Using " + strategy.getName() + " ===");
            List<Integer> testList = new ArrayList<>(numbers);
            Sorter sorter = new Sorter(strategy);
            sorter.sort(testList);
            System.out.println("Sorted list: " + testList);
        }

        // Dynamic strategy selection
        System.out.println("\n=== Dynamic Strategy Selection ===");
        Sorter dynamicSorter = new Sorter(new BubbleSortStrategy());
        System.out.println("Initial strategy: " + dynamicSorter.getStrategyName());

        List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob", "David"));
        System.out.println("Names before: " + names);
        dynamicSorter.sort(names);
        System.out.println("Names after: " + names);

        // Switch strategy at runtime
        dynamicSorter.setStrategy(new QuickSortStrategy());
        System.out.println("\nSwitched to: " + dynamicSorter.getStrategyName());
        List<Integer> moreNumbers = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9));
        System.out.println("Numbers before: " + moreNumbers);
        dynamicSorter.sort(moreNumbers);
        System.out.println("Numbers after: " + moreNumbers);
    }
}
```

**Expected Output**:
```
Original list: [64, 34, 25, 12, 22, 11, 90]

=== Using Bubble Sort ===
Sorting with Bubble Sort...
Completed in 0.0120 ms
Sorted list: [11, 12, 22, 25, 34, 64, 90]

=== Using Quick Sort ===
Sorting with Quick Sort...
Completed in 0.0080 ms
Sorted list: [11, 12, 22, 25, 34, 64, 90]

=== Using Merge Sort ===
Sorting with Merge Sort...
Completed in 0.0090 ms
Sorted list: [11, 12, 22, 25, 34, 64, 90]

=== Dynamic Strategy Selection ===
Initial strategy: Bubble Sort
Names before: [Charlie, Alice, Bob, David]
Sorting with Bubble Sort...
Completed in 0.0050 ms
Names after: [Alice, Bob, Charlie, David]

Switched to: Quick Sort
Numbers before: [5, 2, 8, 1, 9]
Sorting with Quick Sort...
Completed in 0.0030 ms
Numbers after: [1, 2, 5, 8, 9]
```

**Code Walkthrough**:

1. **Strategy Interface**: Defines the common contract for all sorting algorithms.

2. **Concrete Strategies**: Each sorting algorithm implements the SortStrategy interface with its specific logic.

3. **Context Class**: The Sorter class uses a Strategy reference and delegates sorting to it.

4. **Dynamic Binding**: The sort() method on Sorter calls strategy.sort(), which resolves to the actual strategy implementation at runtime.

**Alternative Solution**:

```java
// Using functional interfaces for strategies
class FunctionalSorter {
    public static <T> void sort(List<T> list, Comparator<T> comparator,
                                 BiConsumer<List<T>, Comparator<T>> algorithm) {
        algorithm.accept(list, comparator);
    }

    // Usage
    sort(numbers, Comparator.naturalOrder(), (list, comp) -> {
        // Custom sorting logic
    });
}
```

## Hard Examples

### Example 1: Plugin System with Dynamic Loading

**Problem Statement**: Design a plugin system that uses dynamic binding to load and execute plugins at runtime, supporting plugin lifecycle management and inter-plugin communication.

**Requirements**:

- Plugin interface with lifecycle methods (initialize, execute, shutdown)
- Plugin manager for loading and managing plugins
- Event system for inter-plugin communication
- Plugin dependency management
- Configuration support

**Architecture**:

```
Plugin System Architecture
├── Plugin Interface
│   ├── initialize()
│   ├── execute()
│   └── shutdown()
├── Plugin Manager
│   ├── loadPlugin()
│   ├── enablePlugin()
│   ├── disablePlugin()
│   └── unloadPlugin()
├── Event Bus
│   ├── publish()
│   ├── subscribe()
│   └── unsubscribe()
└── Plugin Implementations
    ├── LoggerPlugin
    ├── AnalyticsPlugin
    └── SecurityPlugin
```

**Implementation**:

```java
package academy.javaengineering.oop.dynamicbinding;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

interface Plugin {
    String getName();
    String getVersion();
    String[] getDependencies();
    void initialize(PluginContext context);
    Object execute(Object input);
    void shutdown();
    default boolean requiresConfiguration() { return false; }
    default void configure(Map<String, Object> config) {}
}

class PluginContext {
    private final Map<String, Object> attributes;
    private final EventPublisher eventPublisher;

    public PluginContext(EventPublisher eventPublisher) {
        this.attributes = new ConcurrentHashMap<>();
        this.eventPublisher = eventPublisher;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public EventPublisher getEventPublisher() {
        return eventPublisher;
    }
}

interface EventPublisher {
    void publish(String topic, Object data);
    void subscribe(String topic, EventSubscriber subscriber);
    void unsubscribe(String topic, EventSubscriber subscriber);
}

interface EventSubscriber {
    void onEvent(String topic, Object data);
}

class SimpleEventPublisher implements EventPublisher {
    private final Map<String, List<EventSubscriber>> subscribers;

    public SimpleEventPublisher() {
        this.subscribers = new ConcurrentHashMap<>();
    }

    @Override
    public void publish(String topic, Object data) {
        List<EventSubscriber> topicSubscribers = subscribers.get(topic);
        if (topicSubscribers != null) {
            for (EventSubscriber subscriber : topicSubscribers) {
                subscriber.onEvent(topic, data);
            }
        }
    }

    @Override
    public void subscribe(String topic, EventSubscriber subscriber) {
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(subscriber);
    }

    @Override
    public void unsubscribe(String topic, EventSubscriber subscriber) {
        List<EventSubscriber> topicSubscribers = subscribers.get(topic);
        if (topicSubscribers != null) {
            topicSubscribers.remove(subscriber);
        }
    }
}

class PluginInfo {
    private final Plugin plugin;
    private boolean enabled;
    private boolean initialized;
    private Map<String, Object> configuration;

    public PluginInfo(Plugin plugin) {
        this.plugin = plugin;
        this.enabled = false;
        this.initialized = false;
        this.configuration = new HashMap<>();
    }

    public Plugin getPlugin() { return plugin; }
    public boolean isEnabled() { return enabled; }
    public boolean isInitialized() { return initialized; }
    public Map<String, Object> getConfiguration() { return configuration; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setInitialized(boolean initialized) { this.initialized = initialized; }
    public void setConfiguration(Map<String, Object> configuration) { this.configuration = configuration; }
}

class PluginManager {
    private final Map<String, PluginInfo> plugins;
    private final PluginContext context;
    private final EventPublisher eventPublisher;

    public PluginManager() {
        this.plugins = new ConcurrentHashMap<>();
        this.eventPublisher = new SimpleEventPublisher();
        this.context = new PluginContext(eventPublisher);
    }

    public boolean loadPlugin(Plugin plugin) {
        String name = plugin.getName();

        if (plugins.containsKey(name)) {
            System.out.println("Plugin " + name + " is already loaded");
            return false;
        }

        // Check dependencies
        for (String dependency : plugin.getDependencies()) {
            if (!plugins.containsKey(dependency)) {
                System.out.println("Missing dependency: " + dependency + " for plugin " + name);
                return false;
            }
        }

        plugins.put(name, new PluginInfo(plugin));
        System.out.println("Loaded plugin: " + name + " v" + plugin.getVersion());
        return true;
    }

    public boolean enablePlugin(String name) {
        PluginInfo info = plugins.get(name);
        if (info == null) {
            System.out.println("Plugin not found: " + name);
            return false;
        }

        if (info.isEnabled()) {
            System.out.println("Plugin already enabled: " + name);
            return true;
        }

        // Initialize if needed
        if (!info.isInitialized()) {
            info.getPlugin().initialize(context);
            info.setInitialized(true);
        }

        info.setEnabled(true);
        System.out.println("Enabled plugin: " + name);
        eventPublisher.publish("plugin.enabled", name);
        return true;
    }

    public boolean disablePlugin(String name) {
        PluginInfo info = plugins.get(name);
        if (info == null || !info.isEnabled()) {
            return false;
        }

        info.setEnabled(false);
        System.out.println("Disabled plugin: " + name);
        eventPublisher.publish("plugin.disabled", name);
        return true;
    }

    public boolean unloadPlugin(String name) {
        PluginInfo info = plugins.get(name);
        if (info == null) {
            return false;
        }

        if (info.isEnabled()) {
            disablePlugin(name);
        }

        if (info.isInitialized()) {
            info.getPlugin().shutdown();
        }

        plugins.remove(name);
        System.out.println("Unloaded plugin: " + name);
        return true;
    }

    public Object executePlugin(String name, Object input) {
        PluginInfo info = plugins.get(name);
        if (info == null || !info.isEnabled()) {
            System.out.println("Plugin not available: " + name);
            return null;
        }

        return info.getPlugin().execute(input);
    }

    public void configurePlugin(String name, Map<String, Object> config) {
        PluginInfo info = plugins.get(name);
        if (info != null && info.getPlugin().requiresConfiguration()) {
            info.getPlugin().configure(config);
            info.setConfiguration(config);
        }
    }

    public List<String> getLoadedPlugins() {
        return new ArrayList<>(plugins.keySet());
    }

    public List<String> getEnabledPlugins() {
        List<String> enabled = new ArrayList<>();
        for (Map.Entry<String, PluginInfo> entry : plugins.entrySet()) {
            if (entry.getValue().isEnabled()) {
                enabled.add(entry.getKey());
            }
        }
        return enabled;
    }
}

// Concrete plugin implementations
class LoggerPlugin implements Plugin {
    private List<String> logs;

    @Override
    public String getName() { return "Logger"; }

    @Override
    public String getVersion() { return "1.0.0"; }

    @Override
    public String[] getDependencies() { return new String[0]; }

    @Override
    public void initialize(PluginContext context) {
        logs = new ArrayList<>();
        System.out.println("Logger plugin initialized");
        context.getEventPublisher().subscribe("plugin.enabled", this::onPluginEvent);
        context.getEventPublisher().subscribe("plugin.disabled", this::onPluginEvent);
    }

    @Override
    public Object execute(Object input) {
        String logEntry = "LOG: " + input.toString();
        logs.add(logEntry);
        System.out.println(logEntry);
        return logEntry;
    }

    @Override
    public void shutdown() {
        System.out.println("Logger plugin shutting down. Total logs: " + logs.size());
        logs.clear();
    }

    private void onPluginEvent(String topic, Object data) {
        execute("Event received: " + topic + " - " + data);
    }
}

class AnalyticsPlugin implements Plugin {
    private Map<String, Integer> metrics;

    @Override
    public String getName() { return "Analytics"; }

    @Override
    public String getVersion() { return "1.0.0"; }

    @Override
    public String[] getDependencies() { return new String[]{"Logger"}; }

    @Override
    public void initialize(PluginContext context) {
        metrics = new HashMap<>();
        System.out.println("Analytics plugin initialized");
    }

    @Override
    public Object execute(Object input) {
        String key = input.toString();
        metrics.merge(key, 1, Integer::sum);
        System.out.println("Analytics: " + key + " count = " + metrics.get(key));
        return metrics.get(key);
    }

    @Override
    public void shutdown() {
        System.out.println("Analytics plugin shutting down. Metrics: " + metrics);
        metrics.clear();
    }

    @Override
    public boolean requiresConfiguration() { return true; }

    @Override
    public void configure(Map<String, Object> config) {
        System.out.println("Analytics configured with: " + config);
    }
}

class SecurityPlugin implements Plugin {
    private Set<String> blockedPatterns;

    @Override
    public String getName() { return "Security"; }

    @Override
    public String getVersion() { return "1.0.0"; }

    @Override
    public String[] getDependencies() { return new String[]{"Logger"}; }

    @Override
    public void initialize(PluginContext context) {
        blockedPatterns = new HashSet<>(Arrays.asList("DROP", "DELETE", "TRUNCATE"));
        System.out.println("Security plugin initialized");
    }

    @Override
    public Object execute(Object input) {
        String query = input.toString().toUpperCase();
        for (String pattern : blockedPatterns) {
            if (query.contains(pattern)) {
                System.out.println("Security: Blocked dangerous query: " + input);
                return false;
            }
        }
        System.out.println("Security: Query allowed: " + input);
        return true;
    }

    @Override
    public void shutdown() {
        System.out.println("Security plugin shutting down");
        blockedPatterns.clear();
    }
}

public class PluginSystemDemo {
    public static void main(String[] args) {
        PluginManager manager = new PluginManager();

        System.out.println("=== Loading Plugins ===");
        manager.loadPlugin(new LoggerPlugin());
        manager.loadPlugin(new AnalyticsPlugin());
        manager.loadPlugin(new SecurityPlugin());

        System.out.println("\n=== Enabling Plugins ===");
        manager.enablePlugin("Logger");
        manager.enablePlugin("Analytics");
        manager.enablePlugin("Security");

        System.out.println("\n=== Configuring Plugins ===");
        Map<String, Object> analyticsConfig = new HashMap<>();
        analyticsConfig.put("trackingEnabled", true);
        analyticsConfig.put("samplingRate", 0.1);
        manager.configurePlugin("Analytics", analyticsConfig);

        System.out.println("\n=== Executing Plugins ===");
        manager.executePlugin("Logger", "User logged in");
        manager.executePlugin("Analytics", "page_view");
        manager.executePlugin("Security", "SELECT * FROM users");
        manager.executePlugin("Security", "DROP TABLE users");

        System.out.println("\n=== Plugin Status ===");
        System.out.println("Loaded: " + manager.getLoadedPlugins());
        System.out.println("Enabled: " + manager.getEnabledPlugins());

        System.out.println("\n=== Disabling Plugins ===");
        manager.disablePlugin("Analytics");

        System.out.println("\n=== Unloading Plugins ===");
        manager.unloadPlugin("Security");
        manager.unloadPlugin("Analytics");
        manager.unloadPlugin("Logger");
    }
}
```

**Execution Flow**:

1. **Plugin Loading**: Plugins are loaded and registered with the manager
2. **Dependency Check**: Manager verifies all dependencies are satisfied
3. **Plugin Initialization**: Plugins are initialized with the context
4. **Plugin Execution**: Plugins process input through their execute() methods
5. **Event Communication**: Plugins communicate through the event bus
6. **Plugin Shutdown**: Plugins are shut down in reverse order

**Unit Tests**:

```java
public class PluginSystemTest {
    public static void main(String[] args) {
        System.out.println("=== Running Plugin System Tests ===\n");

        testPluginLoading();
        testPluginExecution();
        testPluginDependencies();
        testEventCommunication();

        System.out.println("\n=== All Tests Passed ===");
    }

    private static void testPluginLoading() {
        System.out.println("Test 1: Plugin Loading");
        PluginManager manager = new PluginManager();
        Plugin logger = new LoggerPlugin();

        assert manager.loadPlugin(logger) : "Should load plugin successfully";
        assert manager.getLoadedPlugins().contains("Logger") : "Plugin should be loaded";
        assert !manager.loadPlugin(logger) : "Should not load duplicate plugin";

        System.out.println("  PASS: Plugin loading test passed\n");
    }

    private static void testPluginExecution() {
        System.out.println("Test 2: Plugin Execution");
        PluginManager manager = new PluginManager();
        manager.loadPlugin(new LoggerPlugin());
        manager.enablePlugin("Logger");

        Object result = manager.executePlugin("Logger", "Test message");
        assert result != null : "Should return result";

        System.out.println("  PASS: Plugin execution test passed\n");
    }

    private static void testPluginDependencies() {
        System.out.println("Test 3: Plugin Dependencies");
        PluginManager manager = new PluginManager();

        // Analytics depends on Logger
        assert !manager.enablePlugin("Analytics") : "Should fail without dependency";
        manager.loadPlugin(new LoggerPlugin());
        manager.enablePlugin("Logger");
        assert manager.enablePlugin("Analytics") : "Should succeed with dependency";

        System.out.println("  PASS: Plugin dependencies test passed\n");
    }

    private static void testEventCommunication() {
        System.out.println("Test 4: Event Communication");
        SimpleEventPublisher publisher = new SimpleEventPublisher();
        List<String> receivedEvents = new ArrayList<>();

        publisher.subscribe("test", (topic, data) -> receivedEvents.add(data.toString()));
        publisher.publish("test", "event1");
        publisher.publish("test", "event2");

        assert receivedEvents.size() == 2 : "Should receive 2 events";
        assert receivedEvents.contains("event1") : "Should contain event1";
        assert receivedEvents.contains("event2") : "Should contain event2";

        System.out.println("  PASS: Event communication test passed\n");
    }
}
```

**Complexity**:

- **Time Complexity**: O(1) for plugin lookup, O(n) for event propagation where n is subscribers
- **Space Complexity**: O(p) where p is number of plugins

**Best Practices**:

- Use dynamic binding to enable flexible plugin architectures
- Implement proper lifecycle management for plugins
- Use dependency injection to manage plugin dependencies
- Design clear interfaces with minimal coupling
- Document the plugin contract thoroughly

## Exercises

### Easy

1. **Shape Calculator**: Create a Shape class hierarchy with dynamic binding to calculate areas and perimeters of different shapes through a common interface.

2. **Animal Sounds**: Implement an Animal class hierarchy where different animals produce their sounds through dynamic dispatch.

3. **Payment Methods**: Design a PaymentMethod hierarchy where different payment types process transactions through dynamic binding.

### Medium

1. **Strategy Pattern**: Implement a strategy pattern for different sorting algorithms, using dynamic binding to select the algorithm at runtime.

2. **Template Method**: Create a template method pattern for data processing where subclasses override specific steps through dynamic binding.

3. **Plugin System**: Design a simple plugin system with lifecycle methods and dynamic loading/unloading.

### Hard

1. **Game Engine**: Create a game engine entity system with dynamic binding for entity updates, rendering, and AI behavior.

2. **Event System**: Implement an event-driven system where handlers are resolved through dynamic binding.

3. **Dependency Injection**: Design a dependency injection container that uses dynamic binding for type resolution.

## Interview Questions

### Easy

1. **What is dynamic binding?**
   Dynamic binding (late binding) is the mechanism where the JVM determines which method to invoke at runtime based on the actual object type rather than the reference type. It enables runtime polymorphism through method overriding.

2. **How does dynamic binding differ from static binding?**
   Static binding is resolved at compile time based on the reference type (used for static, private, and final methods). Dynamic binding is resolved at runtime based on the actual object type (used for overridden methods).

3. **What is a virtual method table (vtable)?**
   A vtable is a mechanism used by the JVM to implement dynamic binding. Each class has a vtable that maps method signatures to their implementations. When a method is called, the JVM looks up the implementation in the vtable of the actual object type.

### Medium

1. **When does dynamic binding occur in Java?**
   Dynamic binding occurs when an overridden method is called through a parent class reference. The JVM determines the actual object type and invokes the most specific implementation. It does not occur for static, private, or final methods.

2. **How do final methods affect dynamic binding?**
   Final methods cannot be overridden, so they can be statically bound at compile time. This provides a performance optimization because the JVM doesn't need to look up the method in the vtable at runtime.

3. **What are the performance implications of dynamic binding?**
   Dynamic binding has a small runtime overhead compared to static binding due to vtable lookup. However, modern JVMs optimize this through inline caching and JIT compilation, making the difference negligible in most applications.

### Hard

1. **Explain the complete method resolution process in Java.**
   Method resolution involves: 1) Compile-time: Compiler checks if method exists in reference type. 2) Runtime: JVM identifies actual object type. 3) Vtable lookup: JVM finds the most specific implementation in the class hierarchy. 4) Method invocation: JVM invokes the resolved method.

2. **How do interfaces interact with dynamic binding?**
   Interface method calls also use dynamic binding. When a method is called through an interface reference, the JVM looks up the implementation in the vtable of the actual object type. This allows for polymorphic behavior with interfaces as well.

## Common Pitfalls

### 1. Calling Overridden Methods in Constructor

**Wrong**:
```java
class Parent {
    public Parent() {
        display(); // Problem: calls overridden method before child is initialized
    }

    public void display() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    private int value;

    public Child(int value) {
        super();
        this.value = value;
    }

    @Override
    public void display() {
        System.out.println("Child: " + value); // value is 0 here!
    }
}

// This prints "Child: 0" not "Child: 10"
Child child = new Child(10);
```

**Right**:
```java
class Parent {
    public Parent() {
        // Don't call overridden methods in constructor
    }
}

class Child extends Parent {
    private int value;

    public Child(int value) {
        super();
        this.value = value;
        display(); // Safe to call after initialization
    }

    @Override
    public void display() {
        System.out.println("Child: " + value);
    }
}

// Now prints "Child: 10"
Child child = new Child(10);
```

### 2. Assuming Static Methods Use Dynamic Binding

**Wrong**:
```java
class Parent {
    public static void display() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    public static void display() {
        System.out.println("Child");
    }
}

Parent ref = new Child();
ref.display(); // Prints "Parent" - static methods don't use dynamic binding!
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

Parent ref = new Child();
ref.display(); // Prints "Child" - instance methods use dynamic binding
```

### 3. Confusing Overloading with Overriding

**Wrong**:
```java
class Parent {
    public void display(String s) {
        System.out.println("Parent: " + s);
    }
}

class Child extends Parent {
    public void display(Integer i) { // This is overloading, not overriding!
        System.out.println("Child: " + i);
    }
}

Parent ref = new Child();
ref.display("test"); // Prints "Parent: test" - calls Parent's method
ref.display(5); // Compilation error: Parent doesn't have display(Integer)
```

**Right**:
```java
class Parent {
    public void display(String s) {
        System.out.println("Parent: " + s);
    }
}

class Child extends Parent {
    @Override
    public void display(String s) { // This is overriding
        System.out.println("Child: " + s);
    }

    // This is overloading (different parameter type)
    public void display(Integer i) {
        System.out.println("Child: " + i);
    }
}

Parent ref = new Child();
ref.display("test"); // Prints "Child: test" - uses dynamic binding
((Child) ref).display(5); // Prints "Child: 5" - overloaded method
```

## Best Practices

1. **Use dynamic binding to enable polymorphism**: Program to interfaces and let the JVM resolve the correct implementation at runtime. This makes code more flexible and extensible.

2. **Mark methods as final when they should not be overridden**: This allows static binding and prevents unintended behavior changes in subclasses.

3. **Be careful with overridden methods in constructors**: Avoid calling overridden methods in constructors as the subclass may not be fully initialized yet.

4. **Understand the performance implications**: Dynamic binding has a small overhead, but it's usually negligible. Profile before optimizing.

5. **Document the expected behavior of overridden methods**: Clearly specify what subclasses should do when overriding methods to maintain consistency.

## Real World Usage

### How Spring Uses This

Spring Framework extensively uses dynamic binding:

- **BeanPostProcessor**: Dynamic binding allows different processors to modify beans at different stages
- **Transaction Management**: Proxy-based dynamic binding enables transparent transaction handling
- **AOP (Aspect-Oriented Programming)**: Dynamic proxies use dynamic binding to invoke target methods

### How Hibernate Uses This

Hibernate ORM relies on dynamic binding for:

- **Lazy Loading**: Dynamic proxies use dynamic binding to load data on demand
- **Entity Interceptors**: Dynamic binding allows intercepting entity operations
- **Custom Type Handlers**: Dynamic binding resolves the correct type handler at runtime

### How JDK Uses This

The Java Development Kit uses dynamic binding throughout:

- **Collection Framework**: Different List implementations (ArrayList, LinkedList) respond differently to the same method calls
- **I/O Streams**: Different stream types (FileInputStream, ByteArrayInputStream) handle data differently
- **Thread Management**: Runnable and Callable implementations use dynamic binding for task execution

### Enterprise Usage

In enterprise applications, dynamic binding is used for:

- **Service Layer**: Different service implementations are selected at runtime based on configuration
- **Data Access**: Different database drivers are used through a common interface
- **Message Processing**: Different message handlers process different message types through dynamic binding

## References

1. **Effective Java** by Joshua Bloch - Item 19: Design and document for inheritance or else prohibit it
2. **Java Performance** by Scott Oaks - Dynamic method dispatch optimization
3. **Java Language Specification** - Run-Time Evaluation of Method Invocation
4. **Design Patterns** - Strategy, Template Method, and Factory Method patterns
5. **Inside the JVM** - Virtual method tables and dynamic binding implementation

## Summary

- Dynamic binding resolves method calls at runtime based on the actual object type
- It enables true polymorphism where objects behave according to their class
- Virtual method tables (vtables) are used by the JVM to implement dynamic binding
- Final, static, and private methods use static binding for performance
- Dynamic binding is the foundation for many design patterns
- Modern JVMs optimize dynamic dispatch through inline caching and JIT compilation

**Next Steps**: [18-static-binding](../18-static-binding/README.md)
