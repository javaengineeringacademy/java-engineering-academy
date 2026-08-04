# Decorator Pattern

The Decorator pattern attaches additional responsibilities to an object dynamically. Decorators provide a flexible alternative to subclassing for extending functionality.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Decorator](#basic-decorator)
3. [Stacking Decorators](#stacking-decorators)
4. [I/O Streams](#io-streams)
5. [Best Practices](#best-practices)
6. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Decorator?

Decorator wraps an object to add new behavior while keeping the same interface. Multiple decorators can be stacked to combine behaviors.

```
Client ──▶ Decorator ──▶ Component
              │
         ┌────┴────┐
         │ wrappee │
         └─────────┘
```

### When to Use

- Add responsibilities to objects dynamically
- Extend functionality without subclassing
- Stack multiple behaviors
- I/O stream processing

---

## Basic Decorator

### Coffee Shop

```java
// Component interface
public interface Coffee {
    double getCost();
    String getDescription();
}

// Concrete component
public class SimpleCoffee implements Coffee {
    @Override
    public double getCost() { return 2.00; }

    @Override
    public String getDescription() { return "Simple coffee"; }
}

// Base decorator
public abstract class CoffeeDecorator implements Coffee {
    protected final Coffee coffee;

    protected CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    @Override
    public double getCost() { return coffee.getCost(); }

    @Override
    public String getDescription() { return coffee.getDescription(); }
}

// Concrete decorators
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) { super(coffee); }

    @Override
    public double getCost() { return coffee.getCost() + 0.50; }

    @Override
    public String getDescription() { return coffee.getDescription() + ", milk"; }
}

public class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) { super(coffee); }

    @Override
    public double getCost() { return coffee.getCost() + 0.25; }

    @Override
    public String getDescription() { return coffee.getDescription() + ", sugar"; }
}

public class WhipCreamDecorator extends CoffeeDecorator {
    public WhipCreamDecorator(Coffee coffee) { super(coffee); }

    @Override
    public double getCost() { return coffee.getCost() + 0.75; }

    @Override
    public String getDescription() { return coffee.getDescription() + ", whip cream"; }
}

// Usage - stack decorators
Coffee coffee = new SimpleCoffee();
coffee = new MilkDecorator(coffee);
coffee = new SugarDecorator(coffee);
coffee = new WhipCreamDecorator(coffee);

System.out.println(coffee.getDescription());
// "Simple coffee, milk, sugar, whip cream"
System.out.println("$" + coffee.getCost());
// "$3.50"
```

---

## Stacking Decorators

### Notification Decorators

```java
public interface Notification {
    void send(String message);
}

public class BasicNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Basic: " + message);
    }
}

public abstract class NotificationDecorator implements Notification {
    protected final Notification notification;

    protected NotificationDecorator(Notification notification) {
        this.notification = notification;
    }
}

public class LoggingDecorator extends NotificationDecorator {
    public LoggingDecorator(Notification notification) { super(notification); }

    @Override
    public void send(String message) {
        System.out.println("LOG: Sending notification");
        notification.send(message);
        System.out.println("LOG: Notification sent");
    }
}

public class EncryptionDecorator extends NotificationDecorator {
    public EncryptionDecorator(Notification notification) { super(notification); }

    @Override
    public void send(String message) {
        String encrypted = encrypt(message);
        notification.send(encrypted);
    }

    private String encrypt(String message) {
        return Base64.getEncoder().encodeToString(message.getBytes());
    }
}

public class RetryDecorator extends NotificationDecorator {
    private final int maxRetries;

    public RetryDecorator(Notification notification, int maxRetries) {
        super(notification);
        this.maxRetries = maxRetries;
    }

    @Override
    public void send(String message) {
        for (int i = 0; i <= maxRetries; i++) {
            try {
                notification.send(message);
                return;
            } catch (Exception e) {
                System.out.println("Retry " + (i + 1) + "/" + maxRetries);
            }
        }
    }
}

// Usage - stack behaviors
Notification notification = new RetryDecorator(
    new EncryptionDecorator(
        new LoggingDecorator(
            new BasicNotification()
        )
    ), 3);

notification.send("Hello!");
// LOG: Sending notification
// [encrypted message sent]
// LOG: Notification sent
```

---

## I/O Streams

### Java I/O as Decorator

```java
// Java I/O uses decorator pattern extensively
InputStream is = new BufferedInputStream(      // Decorator: buffering
    new FileInputStream(                        // Component: file
        new File("data.txt")
    )
);

// Each decorator adds behavior
InputStream is2 = new DataInputStream(          // Decorator: data types
    new BufferedInputStream(                    // Decorator: buffering
        new FileInputStream(                    // Component: file
            new File("data.txt")
        )
    )
);

// Reading
int value = is2.readInt();  // Buffered + Data + File
```

---

## Best Practices

### Do

```java
// 1. Keep decorator interface same as component
public class Decorator implements Component {
    private final Component wrappee;

    @Override
    public void operation() {
        wrappee.operation();
    }
}

// 2. Use composition
public abstract class Decorator implements Component {
    protected final Component component;
    protected Decorator(Component component) {
        this.component = component;
    }
}
```

### Don't

```java
// 1. Don't add state to decorators (keep them stateless)
// Decorators should be stackable in any order

// 2. Don't change the core behavior
// Decorators add behavior, not replace it

// 3. Don't create too many decorators
// Consider composition or strategy pattern instead
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Decorator** | Attach responsibilities dynamically |
| **Same Interface** | Decorator implements same interface as component |
| **Composition** | Wraps component instead of inheriting |
| **Stackable** | Multiple decorators can be combined |
| **Flexible** | Add/remove behaviors at runtime |
| **I/O Streams** | Classic example of decorator pattern |
| **vs Inheritance** | More flexible than static subclassing |
