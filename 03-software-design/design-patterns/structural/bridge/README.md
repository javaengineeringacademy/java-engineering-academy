# Bridge Pattern

The Bridge pattern separates an object's abstraction from its implementation so they can vary independently. It decouples hierarchy from hierarchy.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Bridge](#basic-bridge)
3. [Abstraction/Implementation](#abstractionimplementation)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Bridge?

Bridge separates two hierarchies - abstraction and implementation - so they can evolve independently.

```
        Abstraction                    Implementor
        ┌─────────┐                    ┌─────────┐
        │ Shape   │────has-a───────────│ Color   │
        └────┬────┘                    └────┬────┘
             │                              │
       ┌─────┴─────┐                ┌───────┴───────┐
       │           │                │               │
    Circle    Rectangle          Red             Blue
```

### When to Use

- Avoiding permanent binding between abstraction and implementation
- Both abstraction and implementation need to vary
- You want to share implementation across objects
- You need to combine multiple abstractions with multiple implementations

---

## Basic Bridge

### Shape and Color Bridge

```java
// Implementation interface
public interface Color {
    String fill();
}

// Concrete implementations
public class Red implements Color {
    @Override
    public String fill() { return "Red"; }
}

public class Blue implements Color {
    @Override
    public String fill() { return "Blue"; }
}

public class Green implements Color {
    @Override
    public String fill() { return "Green"; }
}

// Abstraction
public abstract class Shape {
    protected Color color;

    protected Shape(Color color) {
        this.color = color;
    }

    public abstract void draw();
    public abstract double area();
}

// Refined abstractions
public class Circle extends Shape {
    private double radius;

    public Circle(Color color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.println("Drawing " + color.fill() + " circle with radius " + radius);
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

public class Rectangle extends Shape {
    private double width, height;

    public Rectangle(Color color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("Drawing " + color.fill() + " rectangle " + width + "x" + height);
    }

    @Override
    public double area() {
        return width * height;
    }
}

// Usage - combine any shape with any color
Shape redCircle = new Circle(new Red(), 5);
Shape blueRect = new Rectangle(new Blue(), 4, 6);
Shape greenCircle = new Circle(new Green(), 3);

redCircle.draw();   // "Drawing Red circle with radius 5.0"
blueRect.draw();    // "Drawing Blue rectangle 4.0x6.0"
```

---

## Abstraction/Implementation

### Notification Bridge

```java
// Implementation
public interface MessageSender {
    void send(String title, String message);
}

// Concrete implementations
public class EmailSender implements MessageSender {
    @Override
    public void send(String title, String message) {
        System.out.println("Email - " + title + ": " + message);
    }
}

public class SmsSender implements MessageSender {
    @Override
    public void send(String title, String message) {
        System.out.println("SMS - " + title + ": " + message);
    }
}

public class PushSender implements MessageSender {
    @Override
    public void send(String title, String message) {
        System.out.println("Push - " + title + ": " + message);
    }
}

// Abstraction
public abstract class Notification {
    protected MessageSender sender;

    protected Notification(MessageSender sender) {
        this.sender = sender;
    }

    public abstract void notify(String message);
}

// Refined abstractions
public class AlertNotification extends Notification {
    public AlertNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void notify(String message) {
        sender.send("ALERT", message);
    }
}

public class InfoNotification extends Notification {
    public InfoNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void notify(String message) {
        sender.send("INFO", message);
    }
}

// Usage - any notification type with any sender
Notification emailAlert = new AlertNotification(new EmailSender());
Notification smsInfo = new InfoNotification(new SmsSender());

emailAlert.notify("Server down!");    // "Email - ALERT: Server down!"
smsInfo.notify("Update available");   // "SMS - INFO: Update available"
```

---

## Best Practices

### Do

```java
// 1. Keep hierarchies independent
// Shape hierarchy and Color hierarchy change independently

// 2. Use bridge when you have multiple orthogonal dimensions
// Shape (circle, rectangle) x Color (red, blue, green)

// 3. Inject implementation through constructor
public Shape(Color color) {
    this.color = color;
}
```

### Don't

```java
// 1. Don't use when hierarchies are not independent
// If changes to abstraction always require changes to implementation

// 2. Don't over-engineer
// Simple inheritance may suffice for simple cases
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Bridge** | Separates abstraction from implementation |
| **Two Hierarchies** | Abstraction and implementation vary independently |
| **Composition** | Abstraction has-a implementation |
| **Flexibility** | Combine any abstraction with any implementation |
| **Extensibility** | Add new abstractions or implementations easily |
| **Decoupling** | Changes in one hierarchy don't affect the other |
