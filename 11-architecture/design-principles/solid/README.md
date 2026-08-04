# SOLID Principles

## Overview

SOLID is a mnemonic for five design principles that make object-oriented designs more understandable, flexible, and maintainable.

## S - Single Responsibility Principle

A class should have only one reason to change.

```java
// BAD - Multiple responsibilities
public class User {
    private String name;
    private String email;
    
    public void save() { /* database logic */ }
    public void sendEmail() { /* email logic */ }
    public String generateReport() { /* reporting logic */ }
}

// GOOD - Single responsibility
public class User {
    private String name;
    private String email;
}

public class UserRepository {
    public void save(User user) { /* database logic */ }
}

public class EmailService {
    public void sendEmail(User user) { /* email logic */ }
}
```

## O - Open/Closed Principle

Open for extension, closed for modification.

```java
// BAD - Requires modification for new types
public class AreaCalculator {
    public double calculate(Object shape) {
        if (shape instanceof Circle) {
            return Math.PI * ((Circle) shape).getRadius() * ((Circle) shape).getRadius();
        } else if (shape instanceof Rectangle) {
            return ((Rectangle) shape).getWidth() * ((Rectangle) shape).getHeight();
        }
        throw new IllegalArgumentException();
    }
}

// GOOD - Open for extension
public interface Shape {
    double area();
}

public class Circle implements Shape {
    private double radius;
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

public class Rectangle implements Shape {
    private double width, height;
    
    @Override
    public double area() {
        return width * height;
    }
}

public class AreaCalculator {
    public double calculate(Shape shape) {
        return shape.area();
    }
}
```

## L - Liskov Substitution Principle

Subtypes must be substitutable for their base types.

```java
// BAD - Violates LSP
public class Bird {
    public void fly() { /* flying logic */ }
}

public class Ostrich extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Ostriches can't fly");
    }
}

// GOOD - Proper abstraction
public interface Bird {
    void move();
}

public class FlyingBird implements Bird {
    @Override
    public void move() {
        fly();
    }
    
    private void fly() { /* flying logic */ }
}

public class Ostrich implements Bird {
    @Override
    public void move() {
        run();
    }
    
    private void run() { /* running logic */ }
}
```

## I - Interface Segregation Principle

Clients shouldn't depend on interfaces they don't use.

```java
// BAD - Fat interface
public interface Worker {
    void work();
    void eat();
    void sleep();
}

// GOOD - Segregated interfaces
public interface Workable {
    void work();
}

public interface Feedable {
    void eat();
}

public interface Sleepable {
    void sleep();
}

public class Robot implements Workable {
    @Override
    public void work() { /* work logic */ }
}

public class Human implements Workable, Feedable, Sleepable {
    @Override
    public void work() { /* work logic */ }
    
    @Override
    public void eat() { /* eat logic */ }
    
    @Override
    public void sleep() { /* sleep logic */ }
}
```

## D - Dependency Inversion Principle

High-level modules shouldn't depend on low-level modules. Both should depend on abstractions.

```java
// BAD - High-level depends on low-level
public class OrderService {
    private MySQLDatabase database = new MySQLDatabase();
    
    public void saveOrder(Order order) {
        database.insert(order);
    }
}

// GOOD - Both depend on abstraction
public interface Database {
    void insert(Object entity);
}

public class MySQLDatabase implements Database {
    @Override
    public void insert(Object entity) { /* MySQL logic */ }
}

public class MongoDatabase implements Database {
    @Override
    public void insert(Object entity) { /* MongoDB logic */ }
}

public class OrderService {
    private final Database database;
    
    public OrderService(Database database) {
        this.database = database;
    }
    
    public void saveOrder(Order order) {
        database.insert(order);
    }
}
```

## Best Practices

1. Apply principles gradually
2. Don't over-engineer
3. Balance pragmatism with purity
4. Use tests to verify compliance
5. Refactor when violations accumulate
6. Consider context and trade-offs
7. Document design decisions
8. Review code regularly
