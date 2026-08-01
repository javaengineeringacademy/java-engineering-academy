# Constructors

## Objective
Understand constructor patterns, chaining, and best practices for object initialization.

## Theory

### What is a Constructor?
A **constructor** is a special method that initializes a new object. It has the same name as the class and no return type.

### Types of Constructors

| Type | Syntax | Purpose |
|------|--------|---------|
| **No-arg** | `public ClassName() { }` | Default initialization |
| **Parameterized** | `public ClassName(Type param) { }` | Initialize with values |
| **Copy Constructor** | `public ClassName(ClassName other) { }` | Create copy of existing object |
| **Builder Pattern** | Static inner class | Complex objects with many optional fields |

## Constructor Rules
- Same name as class
- No return type (not even `void`)
- Can be overloaded
- If none defined → compiler provides default no-arg
- First statement can be `this()` or `super()`

## Constructor Chaining

```java
public class Person {
    private String name;
    private int age;
    private String address;

    // Primary constructor (most parameters)
    public Person(String name, int age, String address) {
        this.name = Objects.requireNonNull(name, "Name required");
        this.age = validateAge(age);
        this.address = address;
    }

    // Chaining to primary constructor
    public Person(String name, int age) {
        this(name, age, "Unknown");  // Calls primary with default
    }

    public Person(String name) {
        this(name, 0);  // Chains to two-arg, which chains to primary
    }

    private int validateAge(int age) {
        if (age < 0) throw new IllegalArgumentException("Age >= 0");
        return age;
    }
}
```

## Copy Constructor

```java
public class Person {
    private String name;
    private int age;

    // Copy constructor
    public Person(Person other) {
        this.name = other.name;
        this.age = other.age;
    }
}

// Usage
Person original = new Person("Alice", 30);
Person copy = new Person(original);  // Deep copy for immutable fields
```

## Copy Constructor vs Clone

| Aspect | Copy Constructor | Clone |
|--------|------------------|-------|
| Type Safety | Compile-time checked | Requires cast |
| Inheritance | Works with inheritance | Tricky with inheritance |
| Final Fields | Can copy final fields | Cannot set final fields |
| Exception Handling | Normal exceptions | Checked `CloneNotSupportedException` |

## Constructor Best Practices

| Practice | Reason |
|----------|--------|
| Validate parameters | Fail fast with clear messages |
| Use `final` for immutable fields | Ensures immutability |
| Chain to primary constructor | Single initialization logic |
| Use `Objects.requireNonNull()` | Null safety |
| Prefer factory methods for complex creation | Better naming, can return subtypes |

## Constructor Overloading vs Factory Methods

```java
// Overloading (limited)
public Person(String name, int age) { ... }
public Person(String name, int age, String address) { ... }

// Factory Methods (more flexible)
public static Person createAdult(String name, String email) {
    return new Person(name, 18, email);
}

public static Person createChild(String name, String email) {
    return new Person(name, 10, email);
}
```

## Introduction

Constructors are special methods that initialize objects when they are created. They run automatically when you use the `new` keyword and are responsible for setting up the object's initial state. Understanding constructors is critical for writing robust, well-encapsulated Java classes.

## Learning Objectives

- Understand the purpose and rules of constructors
- Create no-arg, parameterized, and copy constructors
- Implement constructor chaining using `this()` and `super()`
- Distinguish between constructors and factory methods
- Apply validation logic within constructors
- Use the Builder pattern for complex object creation

## Prerequisites

- Basic Java syntax and class definitions
- Understanding of access modifiers (public, private)
- Familiarity with the `this` keyword
- Basic knowledge of object creation with `new`

## Why This Concept Exists

Objects often need complex initialization that cannot be done with simple field assignments. Constructors ensure that:
- Objects are created in a valid state
- Required parameters are provided
- Validation happens before the object is used
- Immutable fields can be assigned exactly once
- Initialization logic is centralized and reusable

Without constructors, objects could be created in invalid or incomplete states, leading to bugs that are difficult to trace.

## Problem Statement

Consider initializing a `Person` object without constructors:
```java
Person p = new Person();
p.name = "Alice";
p.age = -5; // Invalid, but no one catches it
p.email = null; // May cause NPE later
```

There's no way to enforce required parameters or validate input at creation time. Constructors solve this by providing a controlled entry point for object creation.

## Internal Working

When `new Person("Alice", 30)` is called:

1. **Memory Allocation**: JVM allocates space on the heap for the Person object (including object header + field storage)
2. **Field Default Values**: All fields are set to default values (null, 0, false)
3. **Constructor Invocation**: The matching constructor is called with provided arguments
4. **this() Chain Resolution**: If the constructor calls `this()`, the chained constructor executes first
5. **super() Chain**: If `super()` is called (explicitly or implicitly), the parent constructor runs
6. **Field Assignment**: Constructor body executes, assigning validated values to fields
7. **Reference Return**: The initialized object reference is returned to the calling code

## JVM Perspective

- **Constructor Bytecode**: Each constructor compiles to a `<init>` method in the `.class` file
- **Implicit super()**: Java automatically inserts `super()` as the first line if not specified
- **Constructor Chaining**: `this()` calls are resolved at compile time, not runtime (static binding)
- **Object Header Initialization**: The JVM sets up the object header (mark word + klass pointer) before the constructor runs
- **Exception Handling**: If the constructor throws, the partially constructed object is garbage collected

## Memory Representation

```
Stack                          Heap                          Metaspace
─────────────────              ─────────────────             ─────────────────
person: 0x7f3a ──────────────▶ Object Header (12 bytes)
                                - name: 0x7f4b ───────────▶ "Alice"
                                - age: 30
                                - address: null (before ctor)
                                
After constructor:
person: 0x7f3a ──────────────▶ Object Header
                                - name: 0x7f4b ───────────▶ "Alice"
                                - age: 30
                                - address: 0x7f5c ────────▶ "123 Main St"

Constructor bytecode:
  invokespecial #1  // Person.<init>
```

## Syntax

```java
public class ClassName {
    private final Type field1;
    private Type field2;

    // No-arg constructor
    public ClassName() {
        this.field1 = defaultValue;
    }

    // Parameterized constructor
    public ClassName(Type field1, Type field2) {
        this.field1 = Objects.requireNonNull(field1);
        this.field2 = field2;
    }

    // Copy constructor
    public ClassName(ClassName other) {
        this.field1 = other.field1;  // For immutable fields
        this.field2 = other.field2;
    }
}

// Calling constructors
ClassName obj1 = new ClassName();
ClassName obj2 = new ClassName(value1, value2);
ClassName obj3 = new ClassName(obj2);
```

## Easy Example

```java
public class Book {
    private final String title;
    private final String author;

    public Book(String title, String author) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.author = Objects.requireNonNull(author, "Author cannot be null");
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
}

// Usage
Book book = new Book("1984", "George Orwell");
System.out.println(book.getTitle()); // 1984
```

## Medium Example

```java
public class Rectangle {
    private final double width;
    private final double height;
    private final String color;

    public Rectangle(double width, double height) {
        this(width, height, "BLACK");
    }

    public Rectangle(double width, double height, String color) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Dimensions must be positive");
        this.width = width;
        this.height = height;
        this.color = Objects.requireNonNull(color);
    }

    public double area() { return width * height; }
    public double perimeter() { return 2 * (width + height); }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getColor() { return color; }
}

// Usage
Rectangle r1 = new Rectangle(5, 3); // Default color BLACK
Rectangle r2 = new Rectangle(4, 6, "BLUE");
System.out.println(r2.area()); // 24.0
```

## Hard Example

```java
public class DatabaseConfig {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final int maxConnections;
    private final Duration timeout;
    private final boolean useSsl;

    private DatabaseConfig(Builder builder) {
        this.host = Objects.requireNonNull(builder.host);
        this.port = builder.port;
        this.database = Objects.requireNonNull(builder.database);
        this.username = builder.username;
        this.password = builder.password;
        this.maxConnections = builder.maxConnections;
        this.timeout = builder.timeout;
        this.useSsl = builder.useSsl;
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getDatabase() { return database; }

    public static class Builder {
        private final String host;
        private final String database;
        private int port = 5432;
        private String username;
        private String password;
        private int maxConnections = 10;
        private Duration timeout = Duration.ofSeconds(30);
        private boolean useSsl = false;

        public Builder(String host, String database) {
            this.host = host;
            this.database = database;
        }

        public Builder port(int port) { this.port = port; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder maxConnections(int max) { this.maxConnections = max; return this; }
        public Builder timeout(Duration timeout) { this.timeout = timeout; return this; }
        public Builder useSsl(boolean useSsl) { this.useSsl = useSsl; return this; }

        public DatabaseConfig build() {
            return new DatabaseConfig(this);
        }
    }
}

// Usage
DatabaseConfig config = new DatabaseConfig.Builder("localhost", "mydb")
    .port(3306)
    .username("admin")
    .maxConnections(20)
    .useSsl(true)
    .build();
```

## Enterprise Example

```java
public final class UserRegistration {
    private final String userId;
    private final String email;
    private final String hashedPassword;
    private final String fullName;
    private final LocalDate registeredAt;
    private final Role role;
    private boolean active;

    public enum Role { ADMIN, USER, GUEST }

    public UserRegistration(String email, String password, String fullName, Role role) {
        this.userId = UUID.randomUUID().toString();
        this.email = validateEmail(email);
        this.hashedPassword = hashPassword(Objects.requireNonNull(password));
        this.fullName = Objects.requireNonNull(fullName);
        this.registeredAt = LocalDate.now();
        this.role = Objects.requireNonNull(role);
        this.active = true;
    }

    // Copy constructor for creating test fixtures
    public UserRegistration(UserRegistration other) {
        this.userId = other.userId;
        this.email = other.email;
        this.hashedPassword = other.hashedPassword;
        this.fullName = other.fullName;
        this.registeredAt = other.registeredAt;
        this.role = other.role;
        this.active = other.active;
    }

    private static String validateEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
        return email.toLowerCase();
    }

    private static String hashPassword(String password) {
        if (password.length() < 8) throw new IllegalArgumentException("Password too short");
        return Integer.toHexString(password.hashCode()); // Simplified
    }

    public void deactivate() { this.active = false; }
    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }
}

// Usage
UserRegistration admin = new UserRegistration("admin@example.com", "securePass123", "Admin User", UserRegistration.Role.ADMIN);
UserRegistration copy = new UserRegistration(admin);
```

## Performance

- **Constructor Cost**: Constructors involve memory allocation, field defaulting, and method invocation (~10-50ns for simple objects)
- **Validation Overhead**: Parameter validation adds negligible cost but prevents expensive runtime errors
- **Builder Pattern**: Adds object creation overhead (builder + final object) but improves readability for complex objects
- **Factory Methods**: No additional cost over constructors but enable caching and subtype returns
- **Defensive Copying**: Creating copies in copy constructors adds memory/GC overhead; avoid for performance-critical paths
- **Record Constructors**: Java records generate optimized canonical constructors with minimal overhead

## Pitfalls

- **Circular Constructor Chaining**: `this()` chains that form loops cause `StackOverflowError`
- **Using `this.field` in `this()` calls**: Fields are still default values when `this()` is called
- **Forgetting super()**: Parent fields remain uninitialized without explicit or implicit `super()`
- **Calling overridable methods from constructor**: Can lead to partially initialized objects being observed
- **Defensive Copy of Mutable Parameters**: Not copying mutable input objects can break encapsulation
- **Constructor Throwing**: If constructor throws, the object is never fully created but memory is still allocated until GC

## Debugging Tips

- Set breakpoints inside constructors to trace initialization order
- Use `this.getClass().getSimpleName()` in constructors to log which class is being created
- In stack traces, look for `<init>` methods to identify constructor-related issues
- Use IDE debugger's "Force Return" to skip constructor execution during debugging
- Check constructor chaining order by adding logging at each `this()` call
- Use `jstack` to diagnose `StackOverflowError` from circular constructor chains

## Comparison Table

| Feature | Constructor | Factory Method |
|---------|-------------|----------------|
| Syntax | `new ClassName()` | `ClassName.create()` |
| Return Type | Always the class type | Can return subtypes |
| Naming | Fixed (class name) | Descriptive (create, of, from) |
| Validation | In constructor body | Before object creation |
| Caching | Not possible | Can cache instances |
| Subtyping | Cannot return different types | Can return different implementations |
| Chaining | `this()` and `super()` | Regular method calls |

## Decision Tree

```
Do you need to initialize an object?
├── Simple object with required fields only
│   └── Use parameterized constructor
├── Object with many optional fields
│   └── Use Builder pattern
├── Need to create objects of different subtypes
│   └── Use factory methods
├── Need to create copies of existing objects
│   └── Use copy constructor
├── Need to enforce invariants at creation
│   └── Use constructor with validation
└── Need descriptive creation semantics
    └── Use static factory methods
```

## Interview Questions

1. **Can a constructor be private?**
   - Yes, for Singleton or Factory pattern

2. **Difference between constructor and method?**
   - Constructor: same name as class, no return type, creates object
   - Method: has return type, performs action

3. **Can constructor throw exception?**
   - Yes, for validation failures

4. **Can we call constructor from method?**
   - No, only from another constructor via `this()` or `super()`

5. **What is a copy constructor?**
   - A constructor that takes an instance of the same class and creates a new instance with the same field values

## Exercises

1. Create a `Time` class with constructors for hours:minutes:seconds. Add validation to ensure valid time values (0-23 for hours, 0-59 for minutes/seconds).
2. Create a `Matrix` class with a copy constructor that creates a deep copy of a 2D array. Include a method to print the matrix.
3. Implement a `Person` class with constructor chaining: Person(name), Person(name, age), Person(name, age, address).

## Assignments

1. **Configuration Loader**: Create a `Config` class with a Builder pattern. Fields: host, port, username, password, timeout, maxRetries. Validate that host is not null and port is between 1-65535.
2. **Immutable Student**: Create an immutable `Student` class with a copy constructor. Fields: id, name, gpa, enrollmentDate. Ensure the copy constructor performs defensive copying for mutable fields.

## Mini Project

Build a `BankAccount` system:
- Create a `BankAccount` class with a Builder pattern for optional fields
- Required: accountId, ownerName, initialBalance
- Optional: interestRate (default 0.01), overdraftLimit (default 0), accountType (CHECKING/SAVINGS)
- Add validation in the constructor (positive balance, valid account type)
- Implement a copy constructor for creating duplicate accounts
- Create a `BankAccountTest` class to demonstrate all constructor variants

## Summary

- Constructors initialize objects and run automatically with `new`
- Same name as class, no return type
- Types: no-arg, parameterized, copy, and builder pattern
- Constructor chaining with `this()` reduces code duplication
- Always validate parameters in constructors to fail fast
- Factory methods offer more flexibility than constructors for complex creation
- Never call overridable methods from constructors

## Related Topics

← [Classes](classes.md) | → [Methods](methods.md)

## References

- [Java Language Specification - Constructors](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.8)
- [Effective Java Item 1: Consider Static Factory Methods](https://www.oracle.com/technical-resources/articles/java/effectivejava.html)
- [Effective Java Item 17: Minimize Mutability](https://www.oracle.com/java/technologies/javase/effective-java.html)
- [Oracle Docs - Constructors](https://docs.oracle.com/javase/tutorial/java/javaOO/constructors.html)