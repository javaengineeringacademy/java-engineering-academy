# Enums

## Introduction

Enums (enumerations) are special Java classes that represent a fixed set of constants. Unlike C/C++ enums, Java enums are full-fledged classes with fields, methods, and constructors.

## Learning Objectives

By the end of this topic, you will be able to:

- Understand why enums exist and when to use them
- Declare enums with fields, constructors, and methods
- Use enum constants with built-in methods
- Implement interfaces with enums
- Use enums in switch statements
- Apply enums for type-safe constants

## Prerequisites

- Classes and Objects
- Methods
- Constants

## Why This Concept Exists

### The Problem

Before enums, developers used:

```java
// Magic numbers - error prone
public static final int STATUS_ACTIVE = 1;
public static final int STATUS_INACTIVE = 2;
public static final int STATUS_DELETED = 3;

// Strings - runtime errors
public static final String COLOR_RED = "RED";
public static final String COLOR_GREEN = "GREEN";
```

### The Solution

Enums provide:

- **Type safety**: Compiler prevents invalid values
- **Readability**: Named constants instead of magic numbers
- **Rich features**: Fields, methods, constructors
- **Thread safety**: Constants are singletons
- **Serialization**: Automatic handling

### Real-World Analogy

Think of enums as a **menu at a restaurant**:

- The menu has a fixed set of items (constants)
- Each item has properties (price, description)
- You can only order items on the menu (type safety)
- The menu can't change at runtime (fixed set)

## Problem Statement

Before Java 5, developers faced several challenges with constants:

1. **Type Safety Issues**: No compile-time checking for invalid values
2. **Maintenance Problems**: Constants scattered across classes
3. **Namespace Pollution**: Constants polluting class namespaces
4. **No Behavior**: Cannot attach behavior to constants
5. **Switch Statement Limitations**: Limited support for constant-based switching

Consider this scenario: You need to represent colors in a drawing application. Without enums, you'd use magic numbers or strings, leading to runtime errors and poor maintainability.

## Theory

### Core Concepts

Enums are based on several fundamental concepts:

1. **Type Safety**: Enums provide compile-time type checking
2. **Singleton Pattern**: Each enum constant is a singleton instance
3. **Inheritance**: Enums implicitly extend `java.lang.Enum`
4. **Interface Implementation**: Enums can implement interfaces
5. **Abstract Methods**: Enums can have abstract methods implemented per constant

### Enum vs Constants Comparison

| Aspect | Enums | Constants |
|--------|-------|-----------|
| Type Safety | Compile-time checking | Runtime errors possible |
| Behavior | Can have methods and fields | Only static final fields |
| Namespace | Separate type | Pollutes class namespace |
| Iteration | Built-in `values()` method | Manual iteration required |
| Switch Support | Full support | Limited support |
| Serialization | Automatic handling | Manual implementation |

### Enum Features

1. **Fields**: Enums can have instance fields
2. **Constructors**: Enums can have constructors (private)
3. **Methods**: Enums can have instance and static methods
4. **Abstract Methods**: Each constant can implement abstract methods
5. **Interfaces**: Enums can implement interfaces

## JVM Perspective

### Bytecode Generation

Enums are compiled to regular classes with special characteristics:

```java
// Your enum
public enum Color {
    RED, GREEN, BLUE;
}

// Compiled bytecode (simplified):
public final class Color extends java.lang.Enum<Color> {
    public static final Color RED;
    public static final Color GREEN;
    public static final Color BLUE;
    
    static {
        RED = new Color("RED", 0);
        GREEN = new Color("GREEN", 1);
        BLUE = new Color("BLUE", 2);
    }
    
    private Color(String name, int ordinal) {
        super(name, ordinal);
    }
    
    public static Color[] values() {
        // Returns array of all constants
    }
    
    public static Color valueOf(String name) {
        // Returns constant by name
    }
}
```

### Runtime Behavior

1. **Class Loading**: Enums are loaded when first referenced
2. **Singleton Guarantee**: Each constant is a single instance
3. **Thread Safety**: Static initialization is thread-safe
4. **Serialization**: Special handling to maintain singleton property

## Architecture Diagram

### Enum Class Structure

```
java.lang.Enum<E>
└── YourEnum
    ├── Constant 1 (static final)
    ├── Constant 2 (static final)
    ├── Constant N (static final)
    ├── Fields
    ├── Constructors (private)
    └── Methods
```

### Enum in Type Hierarchy

```
Object
└── java.lang.Enum<E>
    └── YourEnum
        ├── implements Interface1
        └── implements Interface2
```

### Memory Architecture

```
PermGen/Metaspace (Class Metadata)
├── Enum class definition
├── Constant pool
└── Method definitions

Heap (Instance Data)
├── Constant 1 instance
├── Constant 2 instance
└── Constant N instance
```

## Flow Diagram

### Enum Lifecycle

```
┌─────────────────┐
│ Define Enum      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Compile to       │
│ .class file      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Load Class       │
│ (static init)    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Create Constants │
│ (singletons)     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Use Constants    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Garbage Collect  │
│ (never for enums)│
└─────────────────┘
```

### Enum Usage Flow

```
Use Enum Constant
    ↓
Check Type Safety
    ↓ (valid)
Access Fields/Methods
    ↓
Compare with == or equals()
    ↓
Use in Switch Statement
```

## Syntax

### How Enums Work

1. **Compilation**: Enums are compiled to regular classes
2. **Constants**: Each enum constant is a public static final field
3. **Class structure**: Enums implicitly extend `java.lang.Enum`
4. **Singleton**: Each constant is a singleton instance

### Bytecode Representation

```java
// Your enum
public enum Color {
    RED, GREEN, BLUE;
}

// Compiled to (simplified):
public final class Color extends Enum<Color> {
    public static final Color RED = new Color("RED", 0);
    public static final Color GREEN = new Color("GREEN", 1);
    public static final Color BLUE = new Color("BLUE", 2);
    
    private Color(String name, int ordinal) {
        super(name, ordinal);
    }
}
```

## Memory Representation

Enum constants are stored in the PermGen/Metaspace area (not heap). Each constant is a singleton, so `==` comparison works.

## Theory

### Enum Internals

1. **Class Definition**: Enums are compiled to final classes extending `java.lang.Enum`
2. **Constants**: Each constant is a `public static final` field of the enum type
3. **Constructor**: Invoked once per constant during static initialization
4. **Values Array**: The compiler generates a `values()` method returning all constants
5. **Ordinal**: Each constant has an implicit ordinal (position) starting from 0

### Enum vs Constants Comparison

| Aspect | Enums | Constants |
|--------|-------|-----------|
| Type Safety | Compile-time checking | Runtime errors possible |
| Behavior | Can have methods and fields | Only static final fields |
| Namespace | Separate type | Pollutes class namespace |
| Iteration | Built-in `values()` method | Manual iteration required |
| Switch Support | Full support | Limited support |
| Serialization | Automatic handling | Manual implementation |

## Syntax

### Basic Enum

```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
}
```

### Enum with Fields and Constructor

```java
public enum Planet {
    MERCURY(3.303e+23, 2.4397e6),
    VENUS(4.869e+24, 6.0518e6),
    EARTH(5.976e+24, 6.37814e6);
    
    private final double mass;   // in kilograms
    private final double radius; // in meters
    
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }
    
    public double surfaceGravity() {
        final double G = 6.67300E-11;
        return G * mass / (radius * radius);
    }
}
```

### Enum with Methods

```java
public enum Operation {
    PLUS {
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS {
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES {
        @Override
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE {
        @Override
        public double apply(double x, double y) {
            return x / y;
        }
    };
    
    public abstract double apply(double x, double y);
}
```

## Easy Examples

### Example 1: Basic Day of Week

**Problem Statement**: Create an enum for days of the week with methods to check if it's a weekday.

**Implementation**:

```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
    
    public boolean isWeekday() {
        return this != SATURDAY && this != SUNDAY;
    }
    
    public Day nextDay() {
        return values()[(ordinal() + 1) % values().length];
    }
}
```

**Output**:
```java
Day today = Day.WEDNESDAY;
System.out.println(today + " is weekday: " + today.isWeekday()); // true
System.out.println("Next day: " + today.nextDay()); // THURSDAY

Day sunday = Day.SUNDAY;
System.out.println(sunday + " is weekday: " + sunday.isWeekday()); // false
```

**Complexity**: O(1) for all operations

**Best Practices**:
- Use enums for fixed sets of constants
- Add behavior to enum constants when appropriate
- Use `values()` to iterate over all constants

### Example 2: Enum with Fields

**Problem Statement**: Create an enum for HTTP status codes with code and message.

**Implementation**:

```java
public enum HttpStatus {
    OK(200, "Success"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized");
    
    private final int code;
    private final String message;
    
    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public boolean isSuccess() {
        return code >= 200 && code < 300;
    }
    
    public boolean isClientError() {
        return code >= 400 && code < 500;
    }
    
    public boolean isServerError() {
        return code >= 500 && code < 600;
    }
    
    public static HttpStatus fromCode(int code) {
        for (HttpStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
```

**Output**:
```java
HttpStatus ok = HttpStatus.OK;
System.out.println(ok.getCode() + ": " + ok.getMessage()); // 200: Success
System.out.println("Is success: " + ok.isSuccess()); // true

HttpStatus notFound = HttpStatus.NOT_FOUND;
System.out.println(notFound.getCode() + ": " + notFound.getMessage()); // 404: Not Found
System.out.println("Is client error: " + notFound.isClientError()); // true

HttpStatus status = HttpStatus.fromCode(500);
System.out.println("From code 500: " + status); // INTERNAL_SERVER_ERROR
```

### Example 3: Enum in Switch Statement

**Problem Statement**: Use an enum in a switch statement to determine season activities.

**Implementation**:

```java
public enum Season {
    SPRING, SUMMER, AUTUMN, WINTER;
    
    public String getActivity() {
        return switch (this) {
            case SPRING -> "Plant flowers";
            case SUMMER -> "Go swimming";
            case AUTUMN -> "Rake leaves";
            case WINTER -> "Build snowman";
        };
    }
}
```

**Output**:
```java
Season season = Season.SUMMER;
System.out.println(season + ": " + season.getActivity()); // SUMMER: Go swimming

// Traditional switch
switch (season) {
    case SPRING -> System.out.println("Spring activities");
    case SUMMER -> System.out.println("Summer activities");
    case AUTUMN -> System.out.println("Autumn activities");
    case WINTER -> System.out.println("Winter activities");
}
```

## Medium Examples

### Example 4: Enum implementing Interface

**Problem Statement**: Create an enum that implements an interface for different payment methods.

**Implementation**:

```java
public interface PaymentMethod {
    String getDisplayName();
    double getProcessingFee(double amount);
    boolean isAvailable();
}

public enum Payment implements PaymentMethod {
    CREDIT_CARD("Credit Card", 0.029, true) {
        @Override
        public boolean isAvailable() {
            return true;
        }
    },
    PAYPAL("PayPal", 0.034, true) {
        @Override
        public boolean isAvailable() {
            return true;
        }
    },
    BANK_TRANSFER("Bank Transfer", 0.01, true) {
        @Override
        public boolean isAvailable() {
            return true;
        }
    },
    CRYPTO("Cryptocurrency", 0.015, false) {
        @Override
        public boolean isAvailable() {
            return false; // Not yet implemented
        }
    };
    
    private final String displayName;
    private final double processingFeeRate;
    private final boolean defaultAvailability;
    
    Payment(String displayName, double processingFeeRate, boolean defaultAvailability) {
        this.displayName = displayName;
        this.processingFeeRate = processingFeeRate;
        this.defaultAvailability = defaultAvailability;
    }
    
    @Override
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public double getProcessingFee(double amount) {
        return amount * processingFeeRate;
    }
    
    @Override
    public boolean isAvailable() {
        return defaultAvailability;
    }
    
    public double calculateTotal(double amount) {
        return amount + getProcessingFee(amount);
    }
}
```

**Output**:
```java
double amount = 100.00;

for (Payment payment : Payment.values()) {
    if (payment.isAvailable()) {
        System.out.printf("%s: Total = $%.2f (fee: $%.2f)%n",
            payment.getDisplayName(),
            payment.calculateTotal(amount),
            payment.getProcessingFee(amount));
    }
}
// Credit Card: Total = $102.90 (fee: $2.90)
// PayPal: Total = $103.40 (fee: $3.40)
// Bank Transfer: Total = $101.00 (fee: $1.00)
```

### Example 5: Enum with Abstract Method

**Problem Statement**: Create an enum for different shapes with area calculation.

**Implementation**:

```java
public enum ShapeType {
    CIRCLE {
        @Override
        public double calculateArea(double... dimensions) {
            double radius = dimensions[0];
            return Math.PI * radius * radius;
        }
        
        @Override
        public String getDescription() {
            return "Circle (radius)";
        }
    },
    RECTANGLE {
        @Override
        public double calculateArea(double... dimensions) {
            double width = dimensions[0];
            double height = dimensions[1];
            return width * height;
        }
        
        @Override
        public String getDescription() {
            return "Rectangle (width, height)";
        }
    },
    TRIANGLE {
        @Override
        public double calculateArea(double... dimensions) {
            double base = dimensions[0];
            double height = dimensions[1];
            return 0.5 * base * height;
        }
        
        @Override
        public String getDescription() {
            return "Triangle (base, height)";
        }
    };
    
    public abstract double calculateArea(double... dimensions);
    public abstract String getDescription();
}
```

**Output**:
```java
System.out.println(ShapeType.CIRCLE.getDescription() + ": " + 
    ShapeType.CIRCLE.calculateArea(5.0)); // Circle (radius): 78.53981633974483

System.out.println(ShapeType.RECTANGLE.getDescription() + ": " + 
    ShapeType.RECTANGLE.calculateArea(4.0, 6.0)); // Rectangle (width, height): 24.0

System.out.println(ShapeType.TRIANGLE.getDescription() + ": " + 
    ShapeType.TRIANGLE.calculateArea(3.0, 4.0)); // Triangle (base, height): 6.0
```

## Enterprise Example

### Example 6: Order Management System

**Problem Statement**: Design a production-grade order management system using enums for status, priority, and payment methods.

**Implementation**:

```java
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public enum OrderStatus {
    CREATED(100, "Order created"),
    VALIDATING(200, "Validating order"),
    PROCESSING(300, "Processing payment"),
    SHIPPED(400, "Order shipped"),
    DELIVERED(500, "Order delivered"),
    CANCELLED(600, "Order cancelled"),
    REFUNDED(700, "Order refunded");

    private final int code;
    private final String description;

    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() { return code; }
    public String getDescription() { return description; }

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case CREATED -> next == VALIDATING || next == CANCELLED;
            case VALIDATING -> next == PROCESSING || next == CANCELLED;
            case PROCESSING -> next == SHIPPED || next == CANCELLED;
            case SHIPPED -> next == DELIVERED;
            case DELIVERED -> next == REFUNDED;
            default -> false;
        };
    }

    public static OrderStatus fromCode(int code) {
        for (OrderStatus status : values()) {
            if (status.code == code) return status;
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}

public enum PaymentMethod {
    CREDIT_CARD("CC", BigDecimal.valueOf(0.029)),
    DEBIT_CARD("DC", BigDecimal.valueOf(0.015)),
    BANK_TRANSFER("BT", BigDecimal.valueOf(0.01)),
    DIGITAL_WALLET("DW", BigDecimal.valueOf(0.02));

    private final String code;
    private final BigDecimal feeRate;

    PaymentMethod(String code, BigDecimal feeRate) {
        this.code = code;
        this.feeRate = feeRate;
    }

    public String getCode() { return code; }
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(feeRate);
    }
}

public enum Priority {
    LOW(1), MEDIUM(2), HIGH(3), CRITICAL(4);

    private final int level;

    Priority(int level) { this.level = level; }
    public int getLevel() { return level; }
    public boolean isHigherThan(Priority other) {
        return this.level > other.level;
    }
}
```

**Output**:
```java
OrderStatus status = OrderStatus.CREATED;
System.out.println(status.getDescription()); // Order created
System.out.println(status.canTransitionTo(OrderStatus.PROCESSING)); // false
System.out.println(status.canTransitionTo(OrderStatus.VALIDATING)); // true

BigDecimal fee = PaymentMethod.CREDIT_CARD.calculateFee(new BigDecimal("1000"));
System.out.println("Fee: " + fee); // Fee: 29.00
```

## Hard Examples

### Example 7: Enum Singleton Pattern

**Problem Statement**: Implement a thread-safe singleton using enum.

**Implementation**:

```java
public enum DatabaseConnection {
    INSTANCE;
    
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/mydb";
    private final String username = "root";
    private final String password = "password";
    
    DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database connection", e);
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }
    
    public int executeUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // Log error
        }
    }
}
```

**Output**:
```java
// Thread-safe singleton
DatabaseConnection db = DatabaseConnection.INSTANCE;
Connection conn = db.getConnection();

// Same instance every time
DatabaseConnection db2 = DatabaseConnection.INSTANCE;
System.out.println(db == db2); // true
```

**Complexity**: O(1) for all operations

**Best Practices**:
- Enums are天然 thread-safe and serialization-safe
- Use enum singleton for guaranteed single instance
- Prefer over traditional singleton patterns

### Example 7: Enum State Machine

**Problem Statement**: Implement a state machine using enums.

**Implementation**:

```java
public enum OrderState {
    PENDING {
        @Override
        public OrderState next() {
            return CONFIRMED;
        }
        
        @Override
        public boolean canCancel() {
            return true;
        }
    },
    CONFIRMED {
        @Override
        public OrderState next() {
            return SHIPPED;
        }
        
        @Override
        public boolean canCancel() {
            return true;
        }
    },
    SHIPPED {
        @Override
        public OrderState next() {
            return DELIVERED;
        }
        
        @Override
        public boolean canCancel() {
            return false;
        }
    },
    DELIVERED {
        @Override
        public OrderState next() {
            return this; // Terminal state
        }
        
        @Override
        public boolean canCancel() {
            return false;
        }
    },
    CANCELLED {
        @Override
        public OrderState next() {
            return this; // Terminal state
        }
        
        @Override
        public boolean canCancel() {
            return false;
        }
    };
    
    public abstract OrderState next();
    public abstract boolean canCancel();
    
    public OrderState cancel() {
        if (canCancel()) {
            return CANCELLED;
        }
        throw new IllegalStateException("Cannot cancel from state: " + this);
    }
}

// Order class using enum state
public class Order {
    private final String id;
    private OrderState state;
    
    public Order(String id) {
        this.id = id;
        this.state = OrderState.PENDING;
    }
    
    public void processNext() {
        this.state = this.state.next();
        System.out.println("Order " + id + " moved to: " + state);
    }
    
    public void cancel() {
        this.state = this.state.cancel();
        System.out.println("Order " + id + " cancelled");
    }
    
    public OrderState getState() {
        return state;
    }
}
```

**Output**:
```java
Order order = new Order("ORD-001");
order.processNext(); // Order ORD-001 moved to: CONFIRMED
order.processNext(); // Order ORD-001 moved to: SHIPPED
order.processNext(); // Order ORD-001 moved to: DELIVERED

Order order2 = new Order("ORD-002");
order2.cancel(); // Order ORD-002 cancelled
```

**Best Practices**:
- Use enums for state machines with fixed states
- Encapsulate state transitions in the enum
- Use methods to enforce valid transitions

## Performance

Enums have minimal performance overhead. They are singleton instances cached in the PermGen/Metaspace, so `==` comparisons are extremely fast. The `values()` method returns a new array each time (defensive copy), so avoid calling it in tight loops.

## Time Complexity

| Operation | Complexity | Notes |
|-----------|------------|-------|
| `valueOf(String)` | O(n) | Linear search through constants |
| `values()` | O(n) | Creates new array copy |
| `ordinal()` | O(1) | Direct field access |
| `==` comparison | O(1) | Reference comparison |
| `compareTo()` | O(1) | Integer comparison |

## Space Complexity

Enum instances are stored in Metaspace as class metadata. Each constant occupies approximately 16-32 bytes of heap memory for instance data. The `values()` array creates an O(n) copy on each invocation.

## Thread Safety

Enum constants are inherently thread-safe due to JVM guarantees:

1. **Class Loading**: Static initialization is thread-safe per JLS §12.4.2
2. **Singleton Guarantee**: Only one instance exists per constant
3. **Immutable**: Enum fields should be `final` for true immutability
4. **No Synchronization Needed**: `==` comparisons are atomic

```java
// Thread-safe singleton
public enum DatabaseConfig {
    INSTANCE;
    
    private final String url;
    
    DatabaseConfig() {
        this.url = System.getenv("DB_URL");
    }
}
```

## Exercises

### Easy

1. **Day of Week**: Create an enum for days with methods to check if it's a weekend.

2. **Season**: Create an enum for seasons with methods to get the next season.

### Medium

3. **Priority**: Create a Priority enum (LOW, MEDIUM, HIGH, CRITICAL) with methods to compare priorities.

4. **File Type**: Create a FileType enum (PDF, IMAGE, VIDEO, DOCUMENT) with methods to get file extensions.

### Hard

5. **State Machine**: Implement a vending machine state machine using enums.

6. **Strategy Pattern**: Use enums to implement the Strategy pattern for different sorting algorithms.

## Interview Questions

### Beginner

1. **What is an enum in Java?**
   An enum is a special data type that represents a fixed set of constants. It's a class that extends `java.lang.Enum` implicitly.

2. **Can enums have fields and methods?**
   Yes, Java enums can have fields, constructors, and methods, unlike C/C++ enums.

3. **Can enums implement interfaces?**
   Yes, enums can implement interfaces but cannot extend classes (they implicitly extend Enum).

### Intermediate

4. **What is the difference between `==` and `.equals()` for enums?**
   Both work for enums because each constant is a singleton. However, `==` is preferred for performance and null-safety.

5. **Can enums have abstract methods?**
   Yes, but each constant must implement the abstract method.

6. **How do you iterate over all enum values?**
   Use `MyEnum.values()` which returns an array of all constants.

### Senior

7. **How are enums implemented in bytecode?**
   Enums are compiled to final classes that extend `java.lang.Enum`. Each constant is a public static final field.

8. **Can enums be serialized?**
   Yes, Java provides special serialization support for enums to ensure singleton instances are maintained.

9. **What is the EnumSet and EnumMap?**
   Specialized Set and Map implementations optimized for enum keys, using bit vectors for performance.

### Architecture

10. **When would you use enums vs constants?**
    Use enums when you need:
    - Type safety
    - Fixed set of values
    - Associated behavior
    - Null safety
    - Better debugging

11. **How would you design a plugin system using enums?**
    Use an enum to represent plugin types, with each constant implementing the plugin interface.

12. **Can you use enums for dependency injection?**
    Yes, you can use enums to register and retrieve implementations in a type-safe manner.

### Scenario

13. **You need to represent different user roles (ADMIN, USER, GUEST). How would you implement this?**

14. **You're building a payment system and need to support different payment methods. How would you use enums?**

15. **You have a configuration with multiple options. How would you model this with enums?**

### Coding

16. **Implement an enum for different log levels (DEBUG, INFO, WARN, ERROR) with methods to filter messages.**

17. **Create an enum for different file permissions (READ, WRITE, EXECUTE) with methods to combine permissions.**

18. **Design an enum for different network protocols (HTTP, HTTPS, FTP, SSH) with methods to get default ports.**

### Production

19. **How would you handle enum versioning in a distributed system?**

20. **What happens if you add a new enum constant in a new version of your library?**

### Debugging

21. **Why am I getting "No enum constant" error?**

22. **How do I convert a string to an enum constant?**

## Common Pitfalls

### 1. Using Enums as Flags Incorrectly

**Wrong**:
```java
public enum Color {
    RED, GREEN, BLUE;
}
// Can't combine: RED | GREEN
```

**Right** (for flags):
```java
@Flags
public enum Color {
    RED(1), GREEN(2), BLUE(4);
    private final int value;
    // Use EnumSet for combinations
}
```

### 2. Forgetting null Safety

**Wrong**:
```java
public enum Day {
    MONDAY, TUESDAY, ...;
    
    public static Day fromString(String s) {
        return Day.valueOf(s); // Throws NPE if s is null
    }
}
```

**Right**:
```java
public static Day fromString(String s) {
    if (s == null) {
        throw new IllegalArgumentException("Day cannot be null");
    }
    return Day.valueOf(s);
}
```

### 3. Not Handling Unknown Values

**Wrong**:
```java
public static Day fromString(String s) {
    return Day.valueOf(s); // Throws IllegalArgumentException
}
```

**Right**:
```java
public static Optional<Day> fromString(String s) {
    try {
        return Optional.of(Day.valueOf(s));
    } catch (IllegalArgumentException e) {
        return Optional.empty();
    }
}
```

## Best Practices

### 1. Use Enums for Fixed Sets

Use enums when you have a fixed set of related constants.

### 2. Add Behavior to Constants

Enums can have methods - use this to encapsulate behavior.

### 3. Use `values()` for Iteration

The `values()` method returns an array of all enum constants.

### 4. Prefer `==` Over `.equals()`

For enum comparison, `==` is faster and null-safe.

### 5. Use EnumSet/EnumMap

For collections of enums, use specialized `EnumSet` and `EnumMap` for performance.

## Real World Usage

### JDK Usage

The JDK uses enums extensively:

```java
// java.lang.Thread.State
public enum State {
    NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED;
}

// java.util.concurrent.TimeUnit
public enum TimeUnit {
    NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS;
}
```

### Spring Framework

```java
// Spring's TransactionDefinition
public interface TransactionDefinition {
    int PROPAGATION_REQUIRED = 0;
    int PROPAGATION_SUPPORTS = 1;
    // ... constants as int fields
}
```

### JPA

```java
// javax.persistence.FetchType
public enum FetchType {
    LAZY, EAGER;
}

// javax.persistence.CascadeType
public enum CascadeType {
    ALL, PERSIST, MERGE, REMOVE, REFRESH, DETACH;
}
```

## Summary

Enums are powerful Java classes for representing fixed sets of constants. Key takeaways:

- **Purpose**: Type-safe constants with behavior
- **Features**: Fields, constructors, methods, interfaces
- **Benefits**: Type safety, thread safety, serialization, readability
- **Best practices**: Use for fixed sets, add behavior, prefer `==`
- **Use cases**: State machines, configuration, type-safe constants
- **Performance**: Use EnumSet/EnumMap for collections

**Next Steps**: Learn about sealed classes for restricted hierarchies, or nested classes for better organization.
