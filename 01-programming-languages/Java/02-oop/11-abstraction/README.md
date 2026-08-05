# Abstraction in Java

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

Abstraction is the process of hiding implementation details while exposing only the essential features of an object. In Java, abstraction is achieved through two mechanisms: abstract classes, which can contain both abstract methods (without implementation) and concrete methods (with implementation), and interfaces, which define a contract of methods that implementing classes must provide. Abstraction enables developers to work at a higher level of design, focusing on what an object does rather than how it does it. It is one of the four pillars of object-oriented programming and is fundamental to building scalable, maintainable, and loosely coupled systems.

---

## Learning Objectives

- Understand the difference between abstract classes and interfaces
- Learn when to use abstract classes versus interfaces
- Master abstract methods and their role in defining contracts
- Apply abstraction principles to create extensible, maintainable designs

---

## Prerequisites

- [01-classes-and-objects/README.md](../01-classes-and-objects/README.md) — Classes, objects, methods
- [08-encapsulation/README.md](../08-encapsulation/README.md) — Access modifiers
- [09-inheritance/README.md](../09-inheritance/README.md) — Inheritance, extends keyword
- [10-polymorphism/README.md](../10-polymorphism/README.md) — Method overriding, dynamic dispatch

---

## Why This Concept Exists

### The Problem

Without abstraction, code depends on concrete implementations:

```java
public class EmailSender {
    public void send(String to, String message) {
        // Email-specific code
    }
}

public class SmsSender {
    public void send(String to, String message) {
        // SMS-specific code
    }
}

public class NotificationService {
    private EmailSender emailSender; // Tightly coupled to EmailSender

    public void notify(String to, String message) {
        emailSender.send(to, message); // Only works with email
    }
}
```

Adding SMS support requires modifying `NotificationService`. There's no way to swap implementations without changing code.

### The Solution

Abstraction defines a contract that decouples the caller from the implementation:

```java
public interface MessageSender {
    void send(String to, String message);
}

public class EmailSender implements MessageSender {
    public void send(String to, String message) {
        // Email implementation
    }
}

public class SmsSender implements MessageSender {
    public void send(String to, String message) {
        // SMS implementation
    }
}

public class NotificationService {
    private MessageSender sender; // Works with ANY implementation

    public NotificationService(MessageSender sender) {
        this.sender = sender;
    }

    public void notify(String to, String message) {
        sender.send(to, message); // Polymorphic call
    }
}
```

### Real-World Analogy

Think of a power outlet. You don't need to know how electricity is generated — you just plug in your device. The outlet abstracts away the complexity of power generation, transmission, and distribution. Different power plants (coal, solar, nuclear) provide the same interface (electricity), and your devices work regardless of the source.

---

## Internal Working

### Abstract Classes at the JVM Level

An abstract class cannot be instantiated directly. When you create a subclass instance, the JVM allocates memory for all fields (including those from abstract parents) and executes the constructor chain.

```
Abstract Shape → Concrete Circle

Memory Layout:
┌─────────────────────────────┐
│ Object header (Circle)      │
│ Shape fields (if any)       │
│ Circle fields (radius)      │
└─────────────────────────────┘
```

### Interfaces at the JVM Level

Interface methods are dispatched using `invokeinterface` bytecode instruction, which is similar to `invokevirtual` but uses a different lookup mechanism because a class can implement multiple interfaces.

```bytecode
aload_1                    // Load object reference
invokeinterface #N         // Interface method dispatch
```

The JVM uses an interface method table (itable) instead of a vtable. The itable maps interface+method to the actual implementation.

### Default Methods and Inheritance

When a class implements an interface with default methods, the JVM includes the default implementation in the class's method table. If the class overrides the method, the override takes precedence.

---

## Syntax

### 1. Abstract Class

```java
public abstract class Shape {
    protected String color;

    public Shape(String color) {
        this.color = color;
    }

    public abstract double getArea(); // No implementation

    public String getColor() { // Concrete method
        return color;
    }
}
```

### 2. Abstract Method

```java
public abstract class Animal {
    public abstract void speak(); // Must be implemented by subclasses
}
```

### 3. Interface

```java
public interface Drawable {
    void draw(); // Abstract method (public by default)
}
```

### 4. Interface with Default Method

```java
public interface Loggable {
    default void log(String message) {
        System.out.println("[LOG] " + message);
    }
}
```

### 5. Abstract Class vs Interface

```java
// Abstract class: can have state, constructors, non-public members
public abstract class AbstractRepository {
    protected DataSource dataSource;
    protected AbstractRepository(DataSource ds) { this.dataSource = ds; }
    public abstract Entity findById(long id);
}

// Interface: contract only (pre-Java 8), static methods, default methods
public interface Repository<T> {
    T findById(long id);
    List<T> findAll();
    void save(T entity);
    void delete(long id);
}
```

---

## Easy Examples

### Example 1: Abstract Class with Shape Hierarchy

**Problem Statement:**
Create a shape hierarchy using an abstract class to define a common contract while allowing each shape to provide its own implementation of `getArea()` and `describe()`.

**Implementation:**

```java
package academy.javaengineering.oop.abstraction;

public abstract class Shape {
    protected String color;
    protected String name;

    public Shape(String color, String name) {
        this.color = color;
        this.name = name;
    }

    public abstract double getArea();
    public abstract double getPerimeter();

    public void describe() {
        System.out.printf("%s %s: Area=%.2f, Perimeter=%.2f%n",
            color, name, getArea(), getPerimeter());
    }

    public String getColor() { return color; }
    public String getName() { return name; }

    public boolean isLargerThan(Shape other) {
        return this.getArea() > other.getArea();
    }
}

class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color, "Circle");
        this.radius = radius;
    }

    @Override
    public double getArea() { return Math.PI * radius * radius; }

    @Override
    public double getPerimeter() { return 2 * Math.PI * radius; }

    public double getRadius() { return radius; }
}

class Rectangle extends Shape {
    private double width, height;

    public Rectangle(String color, double width, double height) {
        super(color, "Rectangle");
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() { return width * height; }

    @Override
    public double getPerimeter() { return 2 * (width + height); }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
}

class Triangle extends Shape {
    private double a, b, c;

    public Triangle(String color, double a, double b, double c) {
        super(color, "Triangle");
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

class ShapeDemo {
    public static void main(String[] args) {
        Shape circle = new Circle("Red", 5);
        Shape rectangle = new Rectangle("Blue", 4, 6);
        Shape triangle = new Triangle("Green", 3, 4, 5);

        circle.describe();
        rectangle.describe();
        triangle.describe();

        System.out.println("\n=== Comparing Shapes ===");
        System.out.println("Circle larger than Rectangle? " + circle.isLargerThan(rectangle));
        System.out.println("Rectangle larger than Triangle? " + rectangle.isLargerThan(triangle));

        System.out.println("\n=== Polymorphic Processing ===");
        Shape[] shapes = { circle, rectangle, triangle };
        double totalArea = 0;
        for (Shape shape : shapes) {
            totalArea += shape.getArea();
        }
        System.out.printf("Total area of all shapes: %.2f%n", totalArea);
    }
}
```

**Output:**
```
Red Circle: Area=78.54, Perimeter=31.42
Blue Rectangle: Area=24.00, Perimeter=20.00
Green Triangle: Area=6.00, Perimeter=12.00

=== Comparing Shapes ===
Circle larger than Rectangle? true
Rectangle larger than Triangle? true

=== Polymorphic Processing ===
Total area of all shapes: 108.54
```

**Best Practices:**
- Use abstract classes when subclasses share state or common behavior
- Define abstract methods for operations that must be implemented differently
- Provide concrete methods in abstract classes for shared utility logic

---

### Example 2: Interface for Pluggable Behavior

**Problem Statement:**
Create a notification system using interfaces so that notification strategies can be swapped without changing the core logic.

**Implementation:**

```java
package academy.javaengineering.oop.abstraction;

public interface NotificationChannel {
    void send(String recipient, String message);
    String getChannelType();
    default boolean isUrgent() { return false; }
}

class EmailChannel implements NotificationChannel {
    private String smtpServer;

    public EmailChannel(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    @Override
    public void send(String recipient, String message) {
        System.out.printf("[EMAIL via %s] To: %s%n  Message: %s%n",
            smtpServer, recipient, message);
    }

    @Override
    public String getChannelType() { return "Email"; }
}

class SmsChannel implements NotificationChannel {
    private String apiKey;

    public SmsChannel(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void send(String recipient, String message) {
        System.out.printf("[SMS] To: %s (API: %s...)%n  Message: %s%n",
            recipient, apiKey.substring(0, 8), message);
    }

    @Override
    public String getChannelType() { return "SMS"; }

    @Override
    public boolean isUrgent() { return true; }
}

class PushChannel implements NotificationChannel {
    private String platform;

    public PushChannel(String platform) {
        this.platform = platform;
    }

    @Override
    public void send(String recipient, String message) {
        System.out.printf("[PUSH %s] Device: %s%n  Message: %s%n",
            platform, recipient, message);
    }

    @Override
    public String getChannelType() { return "Push (" + platform + ")"; }
}

class NotificationService {
    private final java.util.List<NotificationChannel> channels;

    public NotificationService() {
        this.channels = new java.util.ArrayList<>();
    }

    public void addChannel(NotificationChannel channel) {
        channels.add(channel);
    }

    public void notifyAll(String recipient, String message) {
        System.out.println("=== Sending Notification ===");
        for (NotificationChannel channel : channels) {
            channel.send(recipient, message);
        }
    }

    public void notifyUrgent(String recipient, String message) {
        System.out.println("=== Sending Urgent Notification ===");
        for (NotificationChannel channel : channels) {
            if (channel.isUrgent()) {
                channel.send(recipient, message);
            }
        }
    }

    public void listChannels() {
        System.out.println("Active channels:");
        channels.forEach(ch ->
            System.out.println("  - " + ch.getChannelType()));
    }
}

class NotificationDemo {
    public static void main(String[] args) {
        NotificationService service = new NotificationService();
        service.addChannel(new EmailChannel("smtp.company.com"));
        service.addChannel(new SmsChannel("sk-1234567890abcdef"));
        service.addChannel(new PushChannel("iOS"));

        service.listChannels();
        System.out.println();

        service.notifyAll("user@example.com", "Your order has been shipped!");
        System.out.println();
        service.notifyUrgent("user@example.com", "Security alert: unusual login detected");
    }
}
```

**Output:**
```
Active channels:
  - Email
  - SMS
  - Push (iOS)

=== Sending Notification ===
[EMAIL via smtp.company.com] To: user@example.com
  Message: Your order has been shipped!
[SMS] To: user@exa... (API: sk-123456...)
  Message: Your order has been shipped!
[PUSH iOS] Device: user@example.com
  Message: Your order has been shipped!

=== Sending Urgent Notification ===
[SMS] To: user@exa... (API: sk-123456...)
  Message: Security alert: unusual login detected
```

**Best Practices:**
- Use interfaces for cross-cutting concerns (logging, notification, validation)
- Use default methods for optional behavior
- Keep interfaces focused (Interface Segregation Principle)

---

### Example 3: Template Method Pattern

**Problem Statement:**
Use an abstract class with the Template Method pattern to define a skeleton algorithm while letting subclasses implement specific steps.

**Implementation:**

```java
package academy.javaengineering.oop.abstraction;

public abstract class DataExporter {
    // Template method — defines the algorithm skeleton
    public final void export(String data) {
        validate(data);
        String processed = processData(data);
        String formatted = formatData(processed);
        save(formatted);
        logExport();
    }

    protected abstract void validate(String data);
    protected abstract String processData(String data);
    protected abstract String formatData(String data);
    protected abstract void save(String data);

    // Hook method — optional override
    protected void logExport() {
        System.out.println("Export completed");
    }
}

class CsvExporter extends DataExporter {
    @Override
    protected void validate(String data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("CSV data cannot be empty");
        }
        System.out.println("[CSV] Validating data format...");
    }

    @Override
    protected String processData(String data) {
        System.out.println("[CSV] Processing rows...");
        return data.trim();
    }

    @Override
    protected String formatData(String data) {
        System.out.println("[CSV] Formatting as CSV...");
        return "col1,col2,col3\n" + data;
    }

    @Override
    protected void save(String data) {
        System.out.println("[CSV] Saving to file.csv");
        System.out.println("Content:\n" + data);
    }
}

class JsonExporter extends DataExporter {
    @Override
    protected void validate(String data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("JSON data cannot be empty");
        }
        System.out.println("[JSON] Validating JSON structure...");
    }

    @Override
    protected String processData(String data) {
        System.out.println("[JSON] Parsing and changing...");
        return data.trim();
    }

    @Override
    protected String formatData(String data) {
        System.out.println("[JSON] Formatting as JSON...");
        return "{ \"data\": \"" + data + "\" }";
    }

    @Override
    protected void save(String data) {
        System.out.println("[JSON] Saving to file.json");
        System.out.println("Content: " + data);
    }

    @Override
    protected void logExport() {
        System.out.println("[JSON] Export logged to audit trail");
    }
}

class TemplateMethodDemo {
    public static void main(String[] args) {
        String rawData = "Alice,30,Engineer\nBob,25,Designer";

        System.out.println("=== CSV Export ===");
        DataExporter csvExporter = new CsvExporter();
        csvExporter.export(rawData);

        System.out.println("\n=== JSON Export ===");
        DataExporter jsonExporter = new JsonExporter();
        jsonExporter.export(rawData);
    }
}
```

**Output:**
```
=== CSV Export ===
[CSV] Validating data format...
[CSV] Processing rows...
[CSV] Formatting as CSV...
[CSV] Saving to file.csv
Content:
col1,col2,col3
Alice,30,Engineer
Bob,25,Designer
Export completed

=== JSON Export ===
[JSON] Validating JSON structure...
[JSON] Parsing and changing...
[JSON] Formatting as JSON...
[JSON] Saving to file.json
Content: { "data": "Alice,30,Engineer\nBob,25,Designer" }
[JSON] Export logged to audit trail
```

**Best Practices:**
- Make template methods `final` to prevent subclasses from altering the algorithm
- Use hook methods for optional steps with sensible defaults
- Document which methods are extension points

---

## Medium Examples

### Example 1: Abstract Factory Pattern

**Problem Statement:**
Create a UI toolkit abstraction that produces different widget families (Material, Swing, JavaFX) without coupling to specific implementations.

**Requirements:**
- Define abstract widget types (Button, TextBox, CheckBox)
- Create factory interfaces for each widget family
- Support runtime switching between widget families

**Implementation:**

```java
package academy.javaengineering.oop.abstraction;

// Abstract product types
interface Button {
    void render();
    void onClick(Runnable action);
}

interface TextBox {
    void render();
    void setText(String text);
    String getText();
}

interface CheckBox {
    void render();
    void setChecked(boolean checked);
    boolean isChecked();
}

// Abstract factory
interface WidgetFactory {
    Button createButton(String label);
    TextBox createTextBox(String placeholder);
    CheckBox createCheckBox(String label);
}

// Material Design implementation
class MaterialButton implements Button {
    private final String label;

    MaterialButton(String label) { this.label = label; }

    @Override
    public void render() {
        System.out.println("[Material Button] " + label + " (rounded, elevation)");
    }

    @Override
    public void onClick(Runnable action) {
        System.out.println("[Material Button] Ripple effect...");
        action.run();
    }
}

class MaterialTextBox implements TextBox {
    private String text;
    private final String placeholder;

    MaterialTextBox(String placeholder) { this.placeholder = placeholder; }

    @Override
    public void render() {
        System.out.println("[Material TextBox] Placeholder: " + placeholder);
    }

    @Override
    public void setText(String text) { this.text = text; }

    @Override
    public String getText() { return text; }
}

class MaterialCheckBox implements CheckBox {
    private boolean checked;
    private final String label;

    MaterialCheckBox(String label) { this.label = label; }

    @Override
    public void render() {
        System.out.println("[Material CheckBox] " + label + " (checked: " + checked + ")");
    }

    @Override
    public void setChecked(boolean checked) { this.checked = checked; }

    @Override
    public boolean isChecked() { return checked; }
}

class MaterialWidgetFactory implements WidgetFactory {
    @Override
    public Button createButton(String label) { return new MaterialButton(label); }

    @Override
    public TextBox createTextBox(String placeholder) { return new MaterialTextBox(placeholder); }

    @Override
    public CheckBox createCheckBox(String label) { return new MaterialCheckBox(label); }
}

// Swing implementation
class SwingButton implements Button {
    private final String label;

    SwingButton(String label) { this.label = label; }

    @Override
    public void render() {
        System.out.println("[Swing JButton] " + label + " (metal look)");
    }

    @Override
    public void onClick(Runnable action) {
        System.out.println("[Swing JButton] Action performed...");
        action.run();
    }
}

class SwingTextBox implements TextBox {
    private String text;
    private final String placeholder;

    SwingTextBox(String placeholder) { this.placeholder = placeholder; }

    @Override
    public void render() {
        System.out.println("[Swing JTextField] Placeholder: " + placeholder);
    }

    @Override
    public void setText(String text) { this.text = text; }

    @Override
    public String getText() { return text; }
}

class SwingCheckBox implements CheckBox {
    private boolean checked;
    private final String label;

    SwingCheckBox(String label) { this.label = label; }

    @Override
    public void render() {
        System.out.println("[Swing JCheckBox] " + label + " (checked: " + checked + ")");
    }

    @Override
    public void setChecked(boolean checked) { this.checked = checked; }

    @Override
    public boolean isChecked() { return checked; }
}

class SwingWidgetFactory implements WidgetFactory {
    @Override
    public Button createButton(String label) { return new SwingButton(label); }

    @Override
    public TextBox createTextBox(String placeholder) { return new SwingTextBox(placeholder); }

    @Override
    public CheckBox createCheckBox(String label) { return new SwingCheckBox(label); }
}

// Client code works with abstractions
class LoginForm {
    private final Button loginButton;
    private final TextBox usernameField;
    private final TextBox passwordField;
    private final CheckBox rememberMe;

    LoginForm(WidgetFactory factory) {
        usernameField = factory.createTextBox("Enter username");
        passwordField = factory.createTextBox("Enter password");
        rememberMe = factory.createCheckBox("Remember me");
        loginButton = factory.createButton("Login");
    }

    public void render() {
        System.out.println("\n--- Login Form ---");
        usernameField.render();
        passwordField.render();
        rememberMe.render();
        loginButton.render();
    }

    public void simulateLogin() {
        usernameField.setText("alice");
        passwordField.setText("secret");
        rememberMe.setChecked(true);

        loginButton.onClick(() -> {
            System.out.println("Username: " + usernameField.getText());
            System.out.println("Remember: " + rememberMe.isChecked());
            System.out.println("Login successful!");
        });
    }
}

class AbstractFactoryDemo {
    public static void main(String[] args) {
        System.out.println("=== Material Design UI ===");
        WidgetFactory materialFactory = new MaterialWidgetFactory();
        LoginForm materialLogin = new LoginForm(materialFactory);
        materialLogin.render();
        materialLogin.simulateLogin();

        System.out.println("\n=== Swing UI ===");
        WidgetFactory swingFactory = new SwingWidgetFactory();
        LoginForm swingLogin = new LoginForm(swingFactory);
        swingLogin.render();
        swingLogin.simulateLogin();
    }
}
```

**Output:**
```
=== Material Design UI ---

--- Login Form ---
[Material TextBox] Placeholder: Enter username
[Material TextBox] Placeholder: Enter password
[Material CheckBox] Remember me (checked: false)
[Material Button] Login (rounded, elevation)
[Material Button] Ripple effect...
Username: alice
Remember: true
Login successful!

=== Swing UI ---

--- Login Form ---
[Swing JTextField] Placeholder: Enter username
[Swing JTextField] Placeholder: Enter password
[Swing JCheckBox] Remember me (checked: false)
[Swing JButton] Login (metal look)
[Swing JButton] Action performed...
Username: alice
Remember: true
Login successful!
```

**Alternative:**
Use dependency injection (Spring `@Autowired`) to inject factories, enabling runtime switching without modifying client code.

---

### Example 2: Abstract Repository Pattern

**Problem Statement:**
Create a generic data access abstraction that can work with different storage backends (in-memory, database, file).

**Requirements:**
- Abstract repository with common CRUD operations
- Specific implementations for different storage backends
- Type-safe generic operations

**Implementation:**

```java
package academy.javaengineering.oop.abstraction;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

// Abstract entity
abstract class BaseEntity {
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

// Abstract repository interface
interface Repository<T extends BaseEntity> {
    Optional<T> findById(Long id);
    List<T> findAll();
    T save(T entity);
    void delete(Long id);
    long count();
}

// In-memory implementation
class InMemoryRepository<T extends BaseEntity> implements Repository<T> {
    private final Map<Long, T> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(idGenerator.getAndIncrement());
        }
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }

    @Override
    public long count() {
        return store.size();
    }
}

// File-based implementation (simulated)
class FileRepository<T extends BaseEntity> implements Repository<T> {
    private final Map<Long, T> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final String filePath;

    public FileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<T> findById(Long id) {
        System.out.println("[FILE] Reading from " + filePath + " for id=" + id);
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<T> findAll() {
        System.out.println("[FILE] Reading all from " + filePath);
        return new ArrayList<>(store.values());
    }

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(idGenerator.getAndIncrement());
        }
        store.put(entity.getId(), entity);
        System.out.println("[FILE] Saved to " + filePath + ": " + entity.getClass().getSimpleName() + "#" + entity.getId());
        return entity;
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
        System.out.println("[FILE] Deleted from " + filePath + ": id=" + id);
    }

    @Override
    public long count() {
        return store.size();
    }
}

// Concrete entity
class User extends BaseEntity {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "User#" + getId() + "{name='" + name + "', email='" + email + "'}";
    }
}

// Service using abstract repository
class UserService {
    private final Repository<User> repository;

    public UserService(Repository<User> repository) {
        this.repository = repository;
    }

    public User register(String name, String email) {
        User user = new User(name, email);
        return repository.save(user);
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public void deleteUser(Long id) {
        repository.delete(id);
    }

    public long getRegisteredCount() {
        return repository.count();
    }
}

class RepositoryDemo {
    public static void main(String[] args) {
        System.out.println("=== In-Memory Repository ===");
        Repository<User> inMemoryRepo = new InMemoryRepository<>();
        UserService memoryService = new UserService(inMemoryRepo);

        memoryService.register("Alice", "alice@example.com");
        memoryService.register("Bob", "bob@example.com");
        System.out.println("Users: " + memoryService.getAllUsers());
        System.out.println("Count: " + memoryService.getRegisteredCount());

        System.out.println("\n=== File Repository ===");
        Repository<User> fileRepo = new FileRepository("/data/users.json");
        UserService fileService = new UserService(fileRepo);

        fileService.register("Charlie", "charlie@example.com");
        fileService.findById(1L).ifPresent(System.out::println);
    }
}
```

**Output:**
```
=== In-Memory Repository ===
Users: [User#1{name='Alice', email='alice@example.com'}, User#2{name='Bob', email='bob@example.com'}]
Count: 2

=== File Repository ===
[FILE] Saved to /data/users.json: User#1
[FILE] Reading from /data/users.json for id=1
User#1{name='Charlie', email='charlie@example.com'}
```

**Alternative:**
Use Spring Data JPA's `JpaRepository` interface, which provides a complete abstraction over JPA with built-in CRUD methods.

---

### Example 3: Strategy Pattern with Abstraction

**Problem Statement:**
Create a sorting system that can use different sorting algorithms interchangeably.

**Requirements:**
- Define a `SortStrategy` interface
- Implement BubbleSort, QuickSort, and MergeSort
- Allow runtime algorithm switching

**Implementation:**

```java
package academy.javaengineering.oop.abstraction;

import java.util.*;

public interface SortStrategy<T extends Comparable<T>> {
    void sort(List<T> list);
    String getAlgorithmName();
    default void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}

class BubbleSort<T extends Comparable<T>> implements SortStrategy<T> {
    @Override
    public void sort(List<T> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    swap(list, j, j + 1);
                }
            }
        }
    }

    @Override
    public String getAlgorithmName() { return "Bubble Sort"; }
}

class QuickSort<T extends Comparable<T>> implements SortStrategy<T> {
    @Override
    public void sort(List<T> list) {
        quickSort(list, 0, list.size() - 1);
    }

    private void quickSort(List<T> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    private int partition(List<T> list, int low, int high) {
        T pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(pivot) <= 0) {
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i + 1, high);
        return i + 1;
    }

    @Override
    public String getAlgorithmName() { return "Quick Sort"; }
}

class MergeSort<T extends Comparable<T>> implements SortStrategy<T> {
    @Override
    public void sort(List<T> list) {
        if (list.size() > 1) {
            mergeSort(list, 0, list.size() - 1);
        }
    }

    private void mergeSort(List<T> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    private void merge(List<T> list, int left, int mid, int right) {
        List<T> temp = new ArrayList<>();
        int i = left, j = mid + 1;

        while (i <= mid && j <= right) {
            if (list.get(i).compareTo(list.get(j)) <= 0) {
                temp.add(list.get(i++));
            } else {
                temp.add(list.get(j++));
            }
        }
        while (i <= mid) temp.add(list.get(i++));
        while (j <= right) temp.add(list.get(j++));

        for (int k = 0; k < temp.size(); k++) {
            list.set(left + k, temp.get(k));
        }
    }

    @Override
    public String getAlgorithmName() { return "Merge Sort"; }
}

class Sorter<T extends Comparable<T>> {
    private SortStrategy<T> strategy;

    public Sorter(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void sort(List<T> list) {
        System.out.println("Sorting with " + strategy.getAlgorithmName() + "...");
        long start = System.nanoTime();
        strategy.sort(list);
        long elapsed = System.nanoTime() - start;
        System.out.printf("  Completed in %d ns%n", elapsed);
    }

    public SortStrategy<T> getStrategy() { return strategy; }
}

class SortingDemo {
    public static void main(String[] args) {
        List<Integer> data = new ArrayList<>(Arrays.asList(64, 34, 25, 12, 22, 11, 90));
        System.out.println("Original: " + data);

        Sorter<Integer> sorter = new Sorter<>(new BubbleSort<>());
        sorter.sort(new ArrayList<>(data));
        System.out.println("Sorted: " + data);

        System.out.println();
        sorter.setStrategy(new QuickSort<>());
        sorter.sort(new ArrayList<>(data));
        System.out.println("Sorted: " + data);

        System.out.println();
        sorter.setStrategy(new MergeSort<>());
        sorter.sort(new ArrayList<>(data));
        System.out.println("Sorted: " + data);
    }
}
```

**Output:**
```
Original: [64, 34, 25, 12, 22, 11, 90]
Sorting with Bubble Sort...
  Completed in 4500 ns
Sorted: [64, 34, 25, 12, 22, 11, 90]

Sorting with Quick Sort...
  Completed in 2100 ns
Sorted: [64, 34, 25, 12, 22, 11, 90]

Sorting with Merge Sort...
  Completed in 3200 ns
Sorted: [64, 34, 25, 12, 22, 11, 90]
```

**Alternative:**
Use `java.util.Comparator` for a simpler abstraction when the sorting strategy is just comparison logic.

---

## Hard Examples

### Example 1: Plugin Architecture with Abstract Classes

**Architecture:**
A plugin system where plugins are loaded dynamically, abstract classes define the plugin lifecycle, and interfaces provide cross-cutting concerns.

**Implementation:**

```java
package academy.javaengineering.oop.abstraction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Plugin lifecycle management
abstract class Plugin implements Comparable<Plugin> {
    protected String name;
    protected String version;
    protected PluginState state;
    protected final Map<String, Object> properties;

    protected Plugin(String name, String version) {
        this.name = name;
        this.version = version;
        this.state = PluginState.CREATED;
        this.properties = new ConcurrentHashMap<>();
    }

    // Lifecycle methods — subclasses implement specific behavior
    protected abstract void onActivate();
    protected abstract void onDeactivate();
    protected abstract void onConfigure(Map<String, String> config);

    // Template method for lifecycle
    public final void activate() {
        if (state != PluginState.CREATED && state != PluginState.DEACTIVATED) {
            throw new IllegalStateException("Cannot activate plugin in state: " + state);
        }
        System.out.println("Activating plugin: " + name + " v" + version);
        onActivate();
        state = PluginState.ACTIVE;
        System.out.println("Plugin activated: " + name);
    }

    public final void deactivate() {
        if (state != PluginState.ACTIVE) {
            throw new IllegalStateException("Cannot deactivate plugin in state: " + state);
        }
        System.out.println("Deactivating plugin: " + name);
        onDeactivate();
        state = PluginState.DEACTIVATED;
        System.out.println("Plugin deactivated: " + name);
    }

    public final void configure(Map<String, String> config) {
        onConfigure(config);
        properties.putAll(config);
    }

    // Template method for execution
    public abstract Object execute(Object input);

    // Hook methods
    protected void onError(Throwable t) {
        System.err.println("Plugin " + name + " error: " + t.getMessage());
    }

    public boolean isCompatible(String platformVersion) {
        return true; // Default: compatible with all versions
    }

    // Getters
    public String getName() { return name; }
    public String getVersion() { return version; }
    public PluginState getState() { return state; }
    public Map<String, Object> getProperties() { return Collections.unmodifiableMap(properties); }

    @Override
    public int compareTo(Plugin other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name + " v" + version + " [" + state + "]";
    }
}

enum PluginState {
    CREATED, ACTIVE, DEACTIVATED, ERROR
}

// Plugin manager
class PluginManager {
    private final Map<String, Plugin> plugins = new TreeMap<>();

    public void registerPlugin(Plugin plugin) {
        plugins.put(plugin.getName(), plugin);
        System.out.println("Registered plugin: " + plugin);
    }

    public void activateAll() {
        plugins.values().forEach(Plugin::activate);
    }

    public void deactivateAll() {
        plugins.values().forEach(Plugin::deactivate);
    }

    public Optional<Plugin> getPlugin(String name) {
        return Optional.ofNullable(plugins.get(name));
    }

    public List<Plugin> getActivePlugins() {
        return plugins.values().stream()
            .filter(p -> p.getState() == PluginState.ACTIVE)
            .toList();
    }

    public Object executePlugin(String name, Object input) {
        Plugin plugin = plugins.get(name);
        if (plugin == null) throw new IllegalArgumentException("Plugin not found: " + name);
        if (plugin.getState() != PluginState.ACTIVE) {
            throw new IllegalStateException("Plugin not active: " + name);
        }
        try {
            return plugin.execute(input);
        } catch (Exception e) {
            plugin.onError(e);
            throw e;
        }
    }
}

// Concrete plugins
class TextAnalyzerPlugin extends Plugin {
    private boolean lowercaseMode;

    TextAnalyzerPlugin() {
        super("TextAnalyzer", "1.0.0");
    }

    @Override
    protected void onActivate() {
        System.out.println("  Loading NLP models...");
    }

    @Override
    protected void onDeactivate() {
        System.out.println("  Releasing NLP resources...");
    }

    @Override
    protected void onConfigure(Map<String, String> config) {
        lowercaseMode = Boolean.parseBoolean(config.getOrDefault("lowercase", "false"));
    }

    @Override
    public Object execute(Object input) {
        String text = input.toString();
        if (lowercaseMode) text = text.toLowerCase();
        int wordCount = text.split("\\s+").length;
        int charCount = text.length();
        return Map.of("words", wordCount, "chars", charCount, "text", text);
    }
}

class EncryptionPlugin extends Plugin {
    private String algorithm;

    EncryptionPlugin() {
        super("Encryption", "2.1.0");
    }

    @Override
    protected void onActivate() {
        System.out.println("  Initializing encryption engine...");
    }

    @Override
    protected void onDeactivate() {
        System.out.println("  Clearing encryption keys...");
    }

    @Override
    protected void onConfigure(Map<String, String> config) {
        algorithm = config.getOrDefault("algorithm", "AES");
    }

    @Override
    public Object execute(Object input) {
        String text = input.toString();
        // Simulated encryption (not real crypto)
        String encrypted = Base64.getEncoder().encodeToString(text.getBytes());
        return Map.of("algorithm", algorithm, "encrypted", encrypted, "original", text);
    }

    @Override
    public boolean isCompatible(String platformVersion) {
        return Double.parseDouble(platformVersion) >= 2.0;
    }
}

class ValidationPlugin extends Plugin {
    ValidationPlugin() {
        super("Validation", "1.2.0");
    }

    @Override
    protected void onActivate() {
        System.out.println("  Loading validation rules...");
    }

    @Override
    protected void onDeactivate() {
        System.out.println("  Clearing validation cache...");
    }

    @Override
    protected void onConfigure(Map<String, String> config) {
        // No configuration needed
    }

    @Override
    public Object execute(Object input) {
        String text = input.toString();
        List<String> errors = new ArrayList<>();
        if (text.isEmpty()) errors.add("Empty input");
        if (text.length() > 1000) errors.add("Input too long");
        if (!text.matches("[a-zA-Z0-9 ]+")) errors.add("Invalid characters");
        return Map.of("valid", errors.isEmpty(), "errors", errors);
    }
}

class PluginArchitectureDemo {
    public static void main(String[] args) {
        PluginManager manager = new PluginManager();

        manager.registerPlugin(new TextAnalyzerPlugin());
        manager.registerPlugin(new EncryptionPlugin());
        manager.registerPlugin(new ValidationPlugin());

        System.out.println("\n=== Activating Plugins ===");
        manager.activateAll();

        System.out.println("\n=== Configuring Plugins ===");
        manager.getPlugin("TextAnalyzer").ifPresent(p ->
            p.configure(Map.of("lowercase", "true")));
        manager.getPlugin("Encryption").ifPresent(p ->
            p.configure(Map.of("algorithm", "RSA")));

        System.out.println("\n=== Executing Plugins ===");
        String input = "Hello World from Plugin Architecture";

        Object analysisResult = manager.executePlugin("TextAnalyzer", input);
        System.out.println("Analysis: " + analysisResult);

        Object validationResult = manager.executePlugin("Validation", input);
        System.out.println("Validation: " + validationResult);

        Object encryptionResult = manager.executePlugin("Encryption", input);
        System.out.println("Encryption: " + encryptionResult);

        System.out.println("\n=== Active Plugins ===");
        manager.getActivePlugins().forEach(System.out::println);

        System.out.println("\n=== Deactivating Plugins ===");
        manager.deactivateAll();
    }
}
```

**Unit Tests:**

```java
package academy.javaengineering.oop.abstraction;

import org.junit.Test;
import static org.junit.Assert.*;

public class PluginTest {

    @Test
    public void testPluginActivation() {
        TextAnalyzerPlugin plugin = new TextAnalyzerPlugin();
        assertEquals(PluginState.CREATED, plugin.getState());
        plugin.activate();
        assertEquals(PluginState.ACTIVE, plugin.getState());
    }

    @Test
    public void testPluginExecution() {
        TextAnalyzerPlugin plugin = new TextAnalyzerPlugin();
        plugin.activate();
        Object result = plugin.execute("Hello World");
        assertNotNull(result);
        assertTrue(result instanceof Map);
    }

    @Test(expected = IllegalStateException.class)
    public void testDoubleActivation() {
        TextAnalyzerPlugin plugin = new TextAnalyzerPlugin();
        plugin.activate();
        plugin.activate(); // Should throw
    }
}
```

**Complexity:**
- Plugin registration: O(log n) for TreeMap
- Activation: O(1) per plugin
- Execution: Varies by plugin implementation
- Space: O(n) where n is number of plugins

**Best Practices:**
- Use abstract classes for shared lifecycle management
- Define clean interfaces for cross-cutting concerns
- Handle errors gracefully in plugin execution

---

### Example 2: Abstract Payment Processing Pipeline

**Architecture:**
A payment processing pipeline that uses abstraction to decouple validation, processing, and notification steps.

**Implementation:**

```java
package academy.javaengineering.oop.abstraction;

import java.util.*;

// Value objects
record Money(double amount, String currency) {
    Money {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        if (currency == null || currency.length() != 3) throw new IllegalArgumentException("Invalid currency");
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency)) throw new IllegalArgumentException("Currency mismatch");
        return new Money(amount + other.amount, currency);
    }
}

record PaymentRequest(String orderId, Money amount, String payerId, String payeeId) {}

record PaymentResult(boolean success, String transactionId, String message) {
    static PaymentResult success(String transactionId) {
        return new PaymentResult(true, transactionId, "Payment successful");
    }

    static PaymentResult failure(String message) {
        return new PaymentResult(false, null, message);
    }
}

// Abstract pipeline step
interface PaymentStep {
    String getStepName();
    PaymentResult process(PaymentRequest request);
}

// Concrete steps
class FraudCheckStep implements PaymentStep {
    private static final double MAX_SINGLE_TRANSACTION = 10000;

    @Override
    public String getStepName() { return "Fraud Check"; }

    @Override
    public PaymentResult process(PaymentRequest request) {
        System.out.println("  [FraudCheck] Checking amount: " + request.amount());
        if (request.amount().amount() > MAX_SINGLE_TRANSACTION) {
            return PaymentResult.failure("Transaction exceeds maximum limit");
        }
        return PaymentResult.success("FC-" + System.currentTimeMillis());
    }
}

class BalanceCheckStep implements PaymentStep {
    private final Map<String, Double> balances = new HashMap<>();

    public void setBalance(String accountId, double balance) {
        balances.put(accountId, balance);
    }

    @Override
    public String getStepName() { return "Balance Check"; }

    @Override
    public PaymentResult process(PaymentRequest request) {
        double balance = balances.getOrDefault(request.payerId(), 0.0);
        System.out.println("  [BalanceCheck] Account " + request.payerId() +
            " balance: " + balance + ", required: " + request.amount().amount());
        if (balance < request.amount().amount()) {
            return PaymentResult.failure("Insufficient balance");
        }
        balances.put(request.payerId(), balance - request.amount().amount());
        return PaymentResult.success("BC-" + System.currentTimeMillis());
    }
}

class PaymentProcessingStep implements PaymentStep {
    @Override
    public String getStepName() { return "Payment Processing"; }

    @Override
    public PaymentResult process(PaymentRequest request) {
        String txnId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("  [Payment] Processing " + request.amount() +
            " from " + request.payerId() + " to " + request.payeeId());
        return PaymentResult.success(txnId);
    }
}

class AuditLogStep implements PaymentStep {
    private final List<String> auditLog = new ArrayList<>();

    @Override
    public String getStepName() { return "Audit Log"; }

    @Override
    public PaymentResult process(PaymentRequest request) {
        String entry = String.format("[%s] Payment: %s from %s to %s",
            java.time.Instant.now(), request.amount(), request.payerId(), request.payeeId());
        auditLog.add(entry);
        System.out.println("  [Audit] Logged: " + entry);
        return PaymentResult.success("AUD-" + System.currentTimeMillis());
    }

    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }
}

// Pipeline orchestrator
class PaymentPipeline {
    private final List<PaymentStep> steps;

    PaymentPipeline(List<PaymentStep> steps) {
        this.steps = new ArrayList<>(steps);
    }

    public PaymentResult execute(PaymentRequest request) {
        System.out.println("\n=== Processing Payment: " + request.orderId() + " ===");
        for (PaymentStep step : steps) {
            System.out.println("Executing step: " + step.getStepName());
            PaymentResult result = step.process(request);
            if (!result.success()) {
                System.out.println("Pipeline failed at step: " + step.getStepName());
                return result;
            }
        }
        System.out.println("Payment completed successfully!");
        return PaymentResult.success("COMPLETE-" + request.orderId());
    }
}

class PaymentPipelineDemo {
    public static void main(String[] args) {
        // Setup steps
        BalanceCheckStep balanceCheck = new BalanceCheckStep();
        balanceCheck.setBalance("ACC-001", 5000);
        balanceCheck.setBalance("ACC-002", 100);

        List<PaymentStep> steps = List.of(
            new FraudCheckStep(),
            balanceCheck,
            new PaymentProcessingStep(),
            new AuditLogStep()
        );

        PaymentPipeline pipeline = new PaymentPipeline(steps);

        // Test 1: Successful payment
        PaymentRequest request1 = new PaymentRequest(
            "ORD-001", new Money(250, "USD"), "ACC-001", "ACC-002");
        PaymentResult result1 = pipeline.execute(request1);
        System.out.println("Result: " + result1);

        // Test 2: Insufficient balance
        PaymentRequest request2 = new PaymentRequest(
            "ORD-002", new Money(500, "USD"), "ACC-002", "ACC-001");
        PaymentResult result2 = pipeline.execute(request2);
        System.out.println("Result: " + result2);

        // Test 3: Exceeds limit
        PaymentRequest request3 = new PaymentRequest(
            "ORD-003", new Money(50000, "USD"), "ACC-001", "ACC-002");
        PaymentResult result3 = pipeline.execute(request3);
        System.out.println("Result: " + result3);
    }
}
```

**Unit Tests:**

```java
package academy.javaengineering.oop.abstraction;

import org.junit.Test;
import static org.junit.Assert.*;

public class PaymentPipelineTest {

    @Test
    public void testSuccessfulPayment() {
        BalanceCheckStep balanceCheck = new BalanceCheckStep();
        balanceCheck.setBalance("ACC-001", 1000);

        PaymentPipeline pipeline = new PaymentPipeline(List.of(
            new FraudCheckStep(),
            balanceCheck,
            new PaymentProcessingStep()
        ));

        PaymentRequest request = new PaymentRequest(
            "ORD-001", new Money(100, "USD"), "ACC-001", "ACC-002");
        PaymentResult result = pipeline.execute(request);

        assertTrue(result.success());
        assertNotNull(result.transactionId());
    }

    @Test
    public void testInsufficientBalance() {
        BalanceCheckStep balanceCheck = new BalanceCheckStep();
        balanceCheck.setBalance("ACC-001", 50);

        PaymentPipeline pipeline = new PaymentPipeline(List.of(
            new FraudCheckStep(),
            balanceCheck
        ));

        PaymentRequest request = new PaymentRequest(
            "ORD-001", new Money(100, "USD"), "ACC-001", "ACC-002");
        PaymentResult result = pipeline.execute(request);

        assertFalse(result.success());
        assertEquals("Insufficient balance", result.message());
    }

    @Test
    public void testFraudCheckFailure() {
        PaymentPipeline pipeline = new PaymentPipeline(List.of(
            new FraudCheckStep()
        ));

        PaymentRequest request = new PaymentRequest(
            "ORD-001", new Money(50000, "USD"), "ACC-001", "ACC-002");
        PaymentResult result = pipeline.execute(request);

        assertFalse(result.success());
    }
}
```

**Complexity:**
- Pipeline execution: O(n) where n is number of steps
- Each step: O(1) amortized
- Space: O(m) for audit log where m is number of logged transactions

**Best Practices:**
- Use abstract classes for shared pipeline infrastructure
- Define interfaces for individual steps
- Make steps composable and independently testable

---

## Exercises

### Easy

1. **Animal Sounds:**
   Create an abstract `Animal` class with abstract `speak()` and `move()` methods. Implement `Dog`, `Cat`, and `Bird`.

2. **Calculator Interface:**
   Create a `Calculator` interface with `add()`, `subtract()`, `multiply()`, and `divide()` methods. Implement `BasicCalculator` and `ScientificCalculator`.

3. **Shape Area:**
   Create an abstract `Shape` class with abstract `getArea()` method. Implement `Circle` and `Rectangle` classes.

### Medium

4. **Payment Gateway:**
   Create a `PaymentGateway` abstract class with `authorize()`, `capture()`, and `refund()` methods. Implement `StripeGateway` and `PayPalGateway`.

5. **Notification System:**
   Create a `Notifier` interface with `send()` method and a `NotificationTemplate` abstract class with `format()` and `send()` template methods.

6. **Data Parser:**
   Create an abstract `DataParser` class with template method `parse()` and abstract methods `validate()`, `extract()`, and `transform()`. Implement `CsvParser` and `JsonParser`.

### Hard

7. **Plugin Framework:**
   Design a complete plugin framework with abstract `Plugin` class, `PluginManager`, `PluginLoader`, and lifecycle hooks (init, start, stop, destroy).

8. **Workflow Engine:**
   Create a workflow engine with abstract `WorkflowStep` classes, conditional branching, and compensation logic (saga pattern).

9. **Custom ORM:**
   Build an abstract ORM layer with `EntityManager`, `Query`, and `Repository` abstractions that can work with different database dialects.

---

## Interview Questions

### Easy

1. **What is abstraction in Java?**
   Abstraction is the process of hiding implementation details while exposing only the essential features. It's achieved through abstract classes (partial implementation) and interfaces (contract only).

2. **What is the difference between an abstract class and an interface?**
   Abstract class: Can have constructors, state (fields), concrete methods, and access modifiers. A class can extend only one abstract class. Interface: Cannot have constructors (pre-Java 8), only public fields, and a class can implement multiple interfaces.

3. **Can an abstract class have a constructor?**
   Yes. Abstract classes can have constructors, which are called when a subclass is instantiated. The constructor is used to initialize fields defined in the abstract class.

### Medium

4. **When should you use an abstract class versus an interface?**
   Use abstract class when subclasses share state or common behavior, need constructors, or require access modifiers other than public. Use interface when defining a contract that unrelated classes can implement, or when you need multiple inheritance of type.

5. **What is the Template Method pattern and how does it relate to abstraction?**
   Template Method defines an algorithm skeleton in an abstract class, with abstract methods for steps that subclasses implement. It uses abstraction to defer specific steps to subclasses while controlling the overall flow.

6. **Can an abstract class implement an interface?**
   Yes. An abstract class can implement an interface and provide partial implementation of its methods. Subclasses then only need to implement the remaining abstract methods.

### Hard

7. **How does the Interface Segregation Principle relate to abstraction?**
   ISP states that no client should be forced to depend on methods it doesn't use. This means creating small, focused interfaces rather than large, monolithic ones. It ensures abstractions are precise and don't impose unnecessary coupling.

8. **What are the trade-offs between abstract classes and interfaces in terms of flexibility?**
   Abstract classes provide more flexibility in implementation (state, constructors, access modifiers) but less flexibility in type hierarchy (single inheritance). Interfaces provide less implementation flexibility but more type flexibility (multiple inheritance). Java 8+ default methods narrow the gap but don't eliminate it.

---

## Common Pitfalls

### Pitfall 1: Using Abstract Class When Interface Would Be Better

**Wrong:**
```java
abstract class Serializable {
    abstract void serialize();
}

abstract class Deserializable {
    abstract void deserialize();
}

// Can't have a class extend both
```

**Right:**
```java
interface Serializable {
    void serialize();
}

interface Deserializable {
    void deserialize();
}

class DataObject implements Serializable, Deserializable {
    // Can implement both
}
```

### Pitfall 2: Putting Too Much Logic in Abstract Class

**Wrong:**
```java
abstract class Service {
    void process() {
        // 200 lines of logic
        validate();
        transform();
        save();
        notify();
    }
}
```

**Right:**
```java
abstract class Service {
    final void process() { // Template method
        validate();
        transform();
        save();
        notify();
    }

    protected abstract void validate();
    protected abstract void transform();
    protected abstract void save();
    protected void notify() { } // Optional hook
}
```

### Pitfall 3: Leaking Implementation Details in Abstractions

**Wrong:**
```java
interface UserRepository {
    void saveUsingHibernate(EntityManager em, Entity entity); // Leaks JPA details
}
```

**Right:**
```java
interface UserRepository {
    void save(Entity entity); // Clean abstraction
}
```

---

## Best Practices

1. **Choose the Right Abstraction Mechanism:**
   Use abstract classes for "is-a" relationships with shared state. Use interfaces for "can-do" contracts that can be implemented by unrelated classes.

2. **Keep Abstractions Small and Focused:**
   Follow the Interface Segregation Principle. Don't force implementors to depend on methods they don't use.

3. **Design for Extension or Make Final:**
   If an abstract class is designed for inheritance, document the contract. If not, make it final.

4. **Use Abstract Classes for Partial Implementation:**
   Provide default behavior in abstract classes to reduce boilerplate in subclasses.

5. **Favor Composition Over Inheritance:**
   Use interfaces with composition (Strategy, Decorator patterns) instead of deep inheritance hierarchies.

---

## Real World Usage

### Spring Framework
- `AbstractApplicationContext` — template method for bean lifecycle
- `JpaRepository` — abstract repository with CRUD operations
- `HandlerInterceptor` — abstract handler with pre/post processing hooks

### Hibernate / JPA
- `AbstractType` — abstract base for type mappings
- `EventListener` interfaces — contracts for persistence events
- `Interceptor` — abstract class for persistence lifecycle hooks

### JDK Source Code
- `AbstractList` — partial implementation of `List` interface
- `AbstractMap` — partial implementation of `Map` interface
- `Number` — abstract class for numeric wrapper types

### Enterprise Applications
- DAO pattern — abstract data access with concrete implementations
- Strategy pattern — interchangeable algorithms behind interfaces
- Template Method — framework code with customizable steps

---

## References

- [Java Language Specification — Abstract Classes](https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html#jls-8.1.1.1)
- [Effective Java, 3rd Edition — Item 20: Prefer interfaces to abstract classes](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Oracle Java Tutorials — Abstract Classes and Methods](https://docs.oracle.com/en/java/javase/21/java/IandI/abstract.html)
- [Baeldung — Abstraction in Java](https://www.baeldung.com/java-abstraction)
- [Refactoring.Guru — Abstraction](https://refactoring.guru/design-patterns/abstraction)

---

## Summary

Abstraction is the foundation of flexible, maintainable object-oriented design. Key takeaways:

- **Abstract classes:** Provide partial implementation, can have state and constructors, single inheritance
- **Interfaces:** Define contracts, enable multiple inheritance, default methods for optional behavior
- **When to use each:** Abstract class for shared state/behavior, interface for cross-cutting concerns
- **Template Method:** Define algorithm skeleton in abstract class, defer steps to subclasses
- **Strategy Pattern:** Use interfaces to swap algorithms at runtime

**Golden rule:** Abstraction should hide complexity, not create it. Keep abstractions small, focused, and well-documented.

---

**Navigation:**
- Previous: [10-polymorphism](../10-polymorphism/README.md)
- Next: [12-interfaces](../12-interfaces/README.md)
- [Back to OOP Module](../README.md)
