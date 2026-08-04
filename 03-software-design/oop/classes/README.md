# Classes and Objects

Classes and objects are the fundamental building blocks of object-oriented programming. A class is a blueprint; an object is an instance of that blueprint.

## Table of Contents

1. [Concepts](#concepts)
2. [Class Declaration](#class-declaration)
3. [Constructors](#constructors)
4. [Fields](#fields)
5. [Methods](#methods)
6. [Access Modifiers](#access-modifiers)
7. [Static Members](#static-members)
8. [Inner Classes](#inner-classes)
9. [Best Practices](#best-practices)
10. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is a Class?

A class defines the structure and behavior of objects. It encapsulates:
- **State** (fields/attributes) - data the object holds
- **Behavior** (methods/functions) - operations the object can perform

### What is an Object?

An object is a runtime instance of a class. It has:
- **Identity** - unique reference in memory
- **State** - current values of its fields
- **Behavior** - methods it can invoke

### The Anatomy of a Class

```
┌─────────────────────────────────────┐
│              ClassName              │
├─────────────────────────────────────┤
│  - private field: Type              │
│  - private field: Type              │
├─────────────────────────────────────┤
│  + public method(): ReturnType      │
│  + public method(param): void       │
├─────────────────────────────────────┤
│  # protected field: Type            │
└─────────────────────────────────────┘
```

---

## Class Declaration

### Basic Class

```java
public class Person {
    // Fields
    private String name;
    private int age;

    // Constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Methods
    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

### Creating Objects

```java
// Creating an instance
Person person = new Person("Alice", 30);

// Using the object
String name = person.getName();  // "Alice"
person.setAge(31);

// Reference assignment (not a copy)
Person anotherRef = person;
anotherRef.setAge(32);
System.out.println(person.getAge());  // 32 - same object
```

### Using Records (Java 16+)

```java
// Records reduce boilerplate for data carriers
public record Point(int x, int y) {}

// Usage
Point p = new Point(10, 20);
System.out.println(p.x());  // 10
System.out.println(p.y());  // 20

// Records are inherently immutable
// They auto-generate: constructor, getters, equals, hashCode, toString
```

---

## Constructors

### Default Constructor

```java
public class Config {
    private String host = "localhost";
    private int port = 8080;

    // Default constructor (if no other constructor is defined)
    public Config() {
        // Fields initialized to default values
    }
}
```

### Parameterized Constructor

```java
public class User {
    private final String username;
    private final String email;
    private final LocalDateTime createdAt;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    public User(String username, String email, LocalDateTime createdAt) {
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }
}
```

### Constructor Chaining with `this()`

```java
public class Product {
    private final String name;
    private final double price;
    private final int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(String name, double price) {
        this(name, price, 1);  // Chain to full constructor
    }

    public Product(String name) {
        this(name, 0.0);  // Chain to partial constructor
    }
}
```

### Copy Constructor

```java
public class Address {
    private final String street;
    private final String city;
    private final String zipCode;

    public Address(String street, String city, String zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    // Copy constructor - creates a new instance from existing
    public Address(Address other) {
        this.street = other.street;
        this.city = other.city;
        this.zipCode = other.zipCode;
    }
}
```

### Private Constructor (Utility Classes)

```java
public final class MathUtils {
    // Prevent instantiation
    private MathUtils() {
        throw new AssertionError("No instances");
    }

    public static int max(int a, int b) {
        return a > b ? a : b;
    }
}
```

---

## Fields

### Instance Fields

```java
public class Employee {
    // Instance fields - unique to each object
    private String name;
    private double salary;
    private boolean active = true;  // Default value
}
```

### Field Initialization

```java
public class Sensor {
    // Direct initialization
    private String id = UUID.randomUUID().toString();

    // Initialization block
    private List<Reading> readings;

    {
        readings = new ArrayList<>();
    }

    // Final fields must be initialized
    private final Instant createdAt = Instant.now();
}
```

### Constants

```java
public class AppConfig {
    // Static final - class-level constant
    public static final int MAX_CONNECTIONS = 100;
    public static final String APP_NAME = "MyApp";
    public static final double PI = 3.14159265358979;

    // Instance constant
    private final String environment;

    public AppConfig(String environment) {
        this.environment = environment;  // Must be set in constructor
    }
}
```

---

## Methods

### Instance Methods

```java
public class Calculator {
    private double result = 0;

    // Method that modifies state
    public Calculator add(double value) {
        result += value;
        return this;  // Enable method chaining
    }

    public Calculator subtract(double value) {
        result -= value;
        return this;
    }

    // Getter method
    public double getResult() {
        return result;
    }

    // Method with parameters and return
    public double calculatePercentage(double percent) {
        return result * (percent / 100);
    }
}

// Usage with method chaining
double result = new Calculator()
    .add(100)
    .subtract(20)
    .calculatePercentage(15);
```

### Method Overloading (Compile-time Polymorphism)

```java
public class Printer {
    // Same method name, different parameter lists
    public void print(String text) {
        System.out.println("Text: " + text);
    }

    public void print(int number) {
        System.out.println("Number: " + number);
    }

    public void print(String text, int copies) {
        for (int i = 0; i < copies; i++) {
            System.out.println("Copy " + (i + 1) + ": " + text);
        }
    }
}
```

---

## Access Modifiers

### Visibility Levels

```
┌──────────────┬───────┬─────────┬──────────┬───────────┐
│   Modifier   │ Class │ Package │ Subclass │  Global   │
├──────────────┼───────┼─────────┼──────────┼───────────┤
│ private      │  Yes  │   No    │    No    │    No     │
│ (default)    │  Yes  │   Yes   │    No    │    No     │
│ protected    │  Yes  │   Yes   │    Yes   │    No     │
│ public       │  Yes  │   Yes   │    Yes   │    Yes    │
└──────────────┴───────┴─────────┴──────────┴───────────┘
```

### Practical Example

```java
public class BankAccount {
    // Private - only accessible within this class
    private double balance;
    private List<String> transactionLog = new ArrayList<>();

    // Package-private - accessible within same package
    String accountNumber;

    // Protected - accessible in subclasses
    protected double interestRate = 0.02;

    // Public - accessible everywhere
    public String ownerName;

    // Public method provides controlled access to private data
    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
        transactionLog.add("Deposit: " + amount);
    }

    // Private helper method
    private void logTransaction(String message) {
        transactionLog.add(LocalDateTime.now() + ": " + message);
    }
}
```

---

## Static Members

### Static Fields (Shared Across All Instances)

```java
public class User {
    // Static field - shared by all instances
    private static int totalUsers = 0;
    private static final Map<String, User> registry = new HashMap<>();

    // Instance field - unique per instance
    private final String username;

    public User(String username) {
        this.username = username;
        totalUsers++;
        registry.put(username, this);
    }

    // Static method - can be called without an instance
    public static int getTotalUsers() {
        return totalUsers;
    }

    public static Optional<User> findByUsername(String username) {
        return Optional.ofNullable(registry.get(username));
    }
}

// Usage
User alice = new User("alice");
User bob = new User("bob");
System.out.println(User.getTotalUsers());  // 2
```

### Static Utility Methods

```java
public final class StringUtils {
    private StringUtils() {}

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String capitalize(String s) {
        if (isNullOrEmpty(s)) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String repeat(String s, int count) {
        return s.repeat(count);  // Java 11+
    }
}
```

### Static Factory Methods

```java
public class Color {
    private final int r, g, b;

    private Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    // Named static factory methods
    public static Color of(int r, int g, int b) {
        return new Color(r, g, b);
    }

    public static Color fromHex(String hex) {
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return new Color(r, g, b);
    }

    public static Color red() { return new Color(255, 0, 0); }
    public static Color green() { return new Color(0, 255, 0); }
    public static Color blue() { return new Color(0, 0, 255); }
}

// Usage
Color c1 = Color.of(255, 128, 0);
Color c2 = Color.fromHex("#FF8000");
Color c3 = Color.red();
```

---

## Inner Classes

### Non-Static Inner Class

```java
public class LinkedList<T> {
    private Node<T> head;

    // Inner class has access to outer class's private members
    private class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public void add(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = head;
        head = newNode;
    }
}
```

### Static Nested Class

```java
public class Tree {
    private Node root;

    // Static nested class - no access to outer instance
    private static class Node {
        int value;
        Node left, right;

        Node(int value) {
            this.value = value;
        }
    }

    public void insert(int value) {
        root = insertRec(root, value);
    }

    private Node insertRec(Node current, int value) {
        if (current == null) return new Node(value);
        if (value < current.value) {
            current.left = insertRec(current.left, value);
        } else if (value > current.value) {
            current.right = insertRec(current.right, value);
        }
        return current;
    }
}
```

### Local Classes

```java
public class Processor {
    public void process(List<String> items) {
        // Local class - defined inside a method
        class ItemProcessor {
            private int count = 0;

            void process(String item) {
                count++;
                System.out.println("Processed #" + count + ": " + item);
            }

            int getCount() { return count; }
        }

        ItemProcessor processor = new ItemProcessor();
        items.forEach(processor::process);
        System.out.println("Total: " + processor.getCount());
    }
}
```

### Anonymous Classes

```java
public class EventSystem {
    public interface EventHandler {
        void handle(String event);
    }

    public void registerHandler(EventHandler handler) {
        handler.handle("registered");
    }
}

// Usage with anonymous class
EventSystem system = new EventSystem();
system.registerHandler(new EventSystem.EventHandler() {
    @Override
    public void handle(String event) {
        System.out.println("Handling: " + event);
    }
});

// Equivalent lambda (Java 8+)
system.registerHandler(event -> System.out.println("Handling: " + event));
```

---

## Best Practices

### Do

```java
// 1. Use meaningful names
public class CustomerOrder {
    private final String orderId;
    private final List<OrderItem> items;
    private final Instant orderDate;
}

// 2. Keep classes focused (Single Responsibility)
// 3. Initialize fields in constructor or initializer blocks
// 4. Use final for immutable fields
// 5. Prefer static factory methods over constructors for clarity
```

### Don't

```java
// 1. Don't create god classes
public class GodClass {  // BAD - does too much
    // 100+ fields
    // 100+ methods
}

// 2. Don't use public fields (unless a record)
public class BadExample {
    public String name;  // BAD - no encapsulation
}

// 3. Don't forget to close resources
// 4. Don't use magic numbers
if (status == 3) { }  // BAD
if (status == STATUS_ACTIVE) { }  // GOOD
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Class** | Blueprint defining structure and behavior |
| **Object** | Runtime instance of a class |
| **Constructor** | Special method for object initialization |
| **Field** | Variable holding object state |
| **Method** | Function defining object behavior |
| **`private`** | Access limited to declaring class |
| **`protected`** | Access in subclasses and same package |
| **`public`** | Accessible everywhere |
| **`static`** | Belongs to class, not instances |
| **Records** | Immutable data carriers with auto-generated methods |
| **Inner Classes** | Classes defined within other classes |
