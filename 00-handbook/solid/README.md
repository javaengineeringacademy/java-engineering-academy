# SOLID Principles

Comprehensive guide to the five SOLID design principles with detailed examples and practical applications.

---

## Table of Contents

1. [Overview](#overview)
2. [Single Responsibility Principle (SRP)](#single-responsibility-principle-srp)
3. [Open/Closed Principle (OCP)](#open-closed-principle-ocp)
4. [Liskov Substitution Principle (LSP)](#liskov-substitution-principle-lsp)
5. [Interface Segregation Principle (ISP)](#interface-segregation-principle-isp)
6. [Dependency Inversion Principle (DIP)](#dependency-inversion-principle-dip)
7. [Putting It All Together](#putting-it-all-together)
8. [Best Practices](#best-practices)
9. [Common Mistakes](#common-mistakes)
10. [Key Takeaways](#key-takeaways)

---

## Overview

SOLID is an acronym for five design principles intended to make software designs more understandable, flexible, and maintainable. These principles were introduced by Robert C. Martin (Uncle Bob).

### The Acronym

- **S** - Single Responsibility Principle
- **O** - Open/Closed Principle
- **L** - Liskov Substitution Principle
- **I** - Interface Segregation Principle
- **D** - Dependency Inversion Principle

### Why SOLID Matters

- **Maintainability**: Easier to modify and extend
- **Testability**: Easier to write unit tests
- **Flexibility**: Easier to change implementations
- **Readability**: Code is easier to understand
- **Scalability**: Systems can grow without breaking

---

## Single Responsibility Principle (SRP)

### Definition

"A class should have only one, and only one, reason to change."

### Key Concepts

- Each class should have one responsibility
- Each class should have one reason to change
- High cohesion within classes
- Low coupling between classes

### Violation Example

```java
public class User {
    private String name;
    private String email;
    
    // User data (responsibility 1)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    // Email validation (responsibility 2 - WRONG)
    public boolean isValidEmail() {
        return email != null && email.contains("@");
    }
    
    // Database operations (responsibility 3 - WRONG)
    public void saveToDatabase() {
        // Database logic
    }
    
    // Email sending (responsibility 4 - WRONG)
    public void sendWelcomeEmail() {
        // Email logic
    }
}
```

### Correct Implementation

```java
// Only handles user data
public class User {
    private String name;
    private String email;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

// Only handles validation
public class EmailValidator {
    public boolean isValid(String email) {
        return email != null && email.contains("@");
    }
}

// Only handles database operations
public class UserRepository {
    public void save(User user) {
        // Database logic
    }
}

// Only handles email sending
public class EmailService {
    public void sendWelcomeEmail(User user) {
        // Email logic
    }
}
```

### Benefits

- **Easier to understand**: Each class has one purpose
- **Easier to test**: One responsibility to test
- **Easier to maintain**: Changes are isolated
- **Easier to reuse**: Classes can be used in different contexts

---

## Open/Closed Principle (OCP)

### Definition

"Software entities should be open for extension, but closed for modification."

### Key Concepts

- Add new functionality without changing existing code
- Use abstraction and polymorphism
- New features should be additions, not changes
- Existing code should not need modification

### Violation Example

```java
public class DiscountCalculator {
    public double calculateDiscount(String customerType, double amount) {
        if (customerType.equals("regular")) {
            return amount * 0.05;
        } else if (customerType.equals("premium")) {
            return amount * 0.10;
        } else if (customerType.equals("vip")) {
            return amount * 0.15;
        }
        return 0;
    }
}
```

### Correct Implementation

```java
// Abstraction
public interface DiscountStrategy {
    double calculateDiscount(double amount);
}

// Extensions
public class RegularDiscount implements DiscountStrategy {
    public double calculateDiscount(double amount) {
        return amount * 0.05;
    }
}

public class PremiumDiscount implements DiscountStrategy {
    public double calculateDiscount(double amount) {
        return amount * 0.10;
    }
}

public class VipDiscount implements DiscountStrategy {
    public double calculateDiscount(double amount) {
        return amount * 0.15;
    }
}

// Calculator is open for extension, closed for modification
public class DiscountCalculator {
    public double calculateDiscount(DiscountStrategy strategy, double amount) {
        return strategy.calculateDiscount(amount);
    }
}
```

### Benefits

- **Extensibility**: Add new features without changing existing code
- **Stability**: Existing code remains unchanged
- **Flexibility**: New implementations can be added easily
- **Testability**: New code can be tested independently

---

## Liskov Substitution Principle (LSP)

### Definition

"Objects of a superclass should be replaceable with objects of its subclasses without breaking the application."

### Key Concepts

- Subtypes must be substitutable for their base types
- Behavioral compatibility between parent and child classes
- Preconditions cannot be strengthened in subclasses
- Postconditions cannot be weakened in subclasses

### Violation Example

```java
public class Rectangle {
    protected int width;
    protected int height;
    
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int getArea() { return width * height; }
}

public class Square extends Rectangle {
    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width; // Changes height too
    }
    
    @Override
    public void setHeight(int height) {
        this.width = height; // Changes width too
        this.height = height;
    }
}

// This breaks LSP
public class RectangleTest {
    void printArea(Rectangle r) {
        r.setWidth(5);
        r.setHeight(4);
        // Expected: 20, but Square gives 16
        System.out.println(r.getArea());
    }
}
```

### Correct Implementation

```java
// Abstraction
public interface Shape {
    int getArea();
}

// Implementations
public class Rectangle implements Shape {
    private int width;
    private int height;
    
    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getArea() { return width * height; }
}

public class Square implements Shape {
    private int side;
    
    public Square(int side) {
        this.side = side;
    }
    
    public int getArea() { return side * side; }
}

// Now Square can substitute Rectangle without breaking behavior
public class ShapeTest {
    void printArea(Shape shape) {
        System.out.println(shape.getArea());
    }
}
```

### Benefits

- **Reliability**: Subclasses don't break existing code
- **Polymorphism**: Proper use of inheritance
- **Testability**: Tests work with base types
- **Maintainability**: Changes in subclasses are safe

---

## Interface Segregation Principle (ISP)

### Definition

"Clients should not be forced to depend on interfaces they do not use."

### Key Concepts

- Many specific interfaces are better than one general-purpose interface
- Clients should only know about methods that are of interest to them
- Avoid fat interfaces
- Split large interfaces into smaller, more specific ones

### Violation Example

```java
public interface Worker {
    void work();
    void eat();
    void sleep();
}

public class Robot implements Worker {
    public void work() { /* working */ }
    public void eat() { /* robots don't eat */ }
    public void sleep() { /* robots don't sleep */ }
}
```

### Correct Implementation

```java
// Specific interfaces
public interface Workable {
    void work();
}

public interface Feedable {
    void eat();
}

public interface Sleepable {
    void sleep();
}

// Implementations use only what they need
public class Human implements Workable, Feedable, Sleepable {
    public void work() { /* working */ }
    public void eat() { /* eating */ }
    public void sleep() { /* sleeping */ }
}

public class Robot implements Workable {
    public void work() { /* working */ }
}
```

### Benefits

- **Flexibility**: Clients depend only on what they use
- **Maintainability**: Changes to unused interfaces don't affect clients
- **Testability**: Easier to mock interfaces
- **Reusability**: Smaller interfaces are more reusable

---

## Dependency Inversion Principle (DIP)

### Definition

"High-level modules should not depend on low-level modules. Both should depend on abstractions."

### Key Concepts

- Depend on abstractions, not concrete implementations
- High-level and low-level modules should depend on abstractions
- Abstractions should not depend on details
- Details should depend on abstractions

### Violation Example

```java
public class MySQLDatabase {
    public void save(String data) {
        // MySQL specific logic
    }
}

public class UserService {
    private MySQLDatabase database; // Direct dependency on low-level module
    
    public UserService() {
        this.database = new MySQLDatabase(); // Creates dependency
    }
    
    public void saveUser(String user) {
        database.save(user);
    }
}
```

### Correct Implementation

```java
// Abstraction
public interface Database {
    void save(String data);
}

// Low-level modules depend on abstraction
public class MySQLDatabase implements Database {
    public void save(String data) {
        // MySQL specific logic
    }
}

public class PostgreSQLDatabase implements Database {
    public void save(String data) {
        // PostgreSQL specific logic
    }
}

// High-level module depends on abstraction
public class UserService {
    private Database database; // Depends on abstraction
    
    public UserService(Database database) {
        this.database = database; // Injected dependency
    }
    
    public void saveUser(String user) {
        database.save(user);
    }
}

// Usage
Database db = new MySQLDatabase(); // or new PostgreSQLDatabase()
UserService service = new UserService(db);
```

### Benefits

- **Flexibility**: Easy to change implementations
- **Testability**: Easy to mock dependencies
- **Maintainability**: Changes in low-level modules don't affect high-level modules
- **Reusability**: High-level modules can work with different implementations

---

## Putting It All Together

### Complete Example

```java
// SRP: Each class has one responsibility
public class User {
    private String name;
    private String email;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

// OCP: Open for extension, closed for modification
public interface UserRepository {
    void save(User user);
    User findByEmail(String email);
}

public class MySQLUserRepository implements UserRepository {
    public void save(User user) { /* MySQL logic */ }
    public User findByEmail(String email) { /* MySQL logic */ }
}

public class PostgreSQLUserRepository implements UserRepository {
    public void save(User user) { /* PostgreSQL logic */ }
    public User findByEmail(String email) { /* PostgreSQL logic */ }
}

// LSP: Subtypes are substitutable
public interface Validator {
    boolean validate(User user);
}

public class EmailValidator implements Validator {
    public boolean validate(User user) {
        return user.getEmail() != null && user.getEmail().contains("@");
    }
}

public class NameValidator implements Validator {
    public boolean validate(User user) {
        return user.getName() != null && !user.getName().isEmpty();
    }
}

// ISP: Clients depend only on what they use
public interface UserReader {
    User findByEmail(String email);
}

public interface UserWriter {
    void save(User user);
}

public class ReadOnlyUserRepository implements UserReader {
    public User findByEmail(String email) { /* read only */ }
}

public class ReadWriteUserRepository implements UserReader, UserWriter {
    public User findByEmail(String email) { /* read */ }
    public void save(User user) { /* write */ }
}

// DIP: Depend on abstractions
public class UserService {
    private final UserReader userReader;
    private final UserWriter userWriter;
    private final List<Validator> validators;
    
    public UserService(UserReader userReader, UserWriter userWriter, List<Validator> validators) {
        this.userReader = userReader;
        this.userWriter = userWriter;
        this.validators = validators;
    }
    
    public void createUser(User user) {
        for (Validator validator : validators) {
            if (!validator.validate(user)) {
                throw new IllegalArgumentException("User validation failed");
            }
        }
        userWriter.save(user);
    }
    
    public User findUser(String email) {
        return userReader.findByEmail(email);
    }
}
```

---

## Best Practices

### Applying SOLID

1. **Start with SRP**: Ensure each class has one responsibility
2. **Apply OCP**: Use abstraction for extensibility
3. **Verify LSP**: Test that subclasses don't break behavior
4. **Practice ISP**: Create focused interfaces
5. **Implement DIP**: Depend on abstractions

### Common Patterns

1. **Strategy Pattern**: Implements OCP
2. **Factory Pattern**: Implements DIP
3. **Adapter Pattern**: Implements LSP
4. **Facade Pattern**: Implements ISP
5. **Decorator Pattern**: Implements OCP

### Code Reviews

1. **Check for SRP violations**: Multiple responsibilities in one class
2. **Verify OCP**: Can new features be added without modification?
3. **Test LSP**: Do subclasses maintain behavior?
4. **Review ISP**: Are interfaces focused?
5. **Verify DIP**: Do high-level modules depend on abstractions?

---

## Common Mistakes

### SRP Mistakes

1. **God classes**: Classes doing too much
2. **Mixed concerns**: Database, business logic, and UI in one class
3. **Multiple reasons to change**: Class changes for different reasons
4. **Low cohesion**: Unrelated methods in same class
5. **High coupling**: Classes dependent on many others

### OCP Mistakes

1. **Switch statements**: Adding new cases requires modification
2. **String comparisons**: Checking types with strings
3. **No abstraction**: Concrete classes instead of interfaces
4. **Rigid design**: Can't extend without modifying
5. **Feature envy**: Class using another class's data excessively

### LSP Mistakes

1. **Inheritance abuse**: Using inheritance for code reuse only
2. **Overriding behavior**: Changing expected behavior in subclasses
3. **Strengthening preconditions**: Adding restrictions in subclasses
4. **Weakening postconditions**: Returning different results in subclasses
5. **Type checking**: Using instanceof to check types

### ISP Mistakes

1. **Fat interfaces**: Too many methods in one interface
2. **Forced implementation**: Classes implementing unused methods
3. **Interface bloat**: Interfaces doing too much
4. **Client dependency**: Clients depending on unused methods
5. **No segregation**: One interface for all clients

### DIP Mistakes

1. **Concrete dependencies**: Depending on specific implementations
2. **No injection**: Creating dependencies in constructors
3. **Service locator**: Using service locators instead of injection
4. **Abstraction leak**: Exposing implementation details
5. **Tight coupling**: Classes directly dependent on each other

---

## Key Takeaways

1. **SRP**: One class, one responsibility
2. **OCP**: Open for extension, closed for modification
3. **LSP**: Subclasses must be substitutable
4. **ISP**: Many small interfaces are better than one large
5. **DIP**: Depend on abstractions, not implementations
6. **Balance**: Apply principles contextually
7. **Practice**: Principles become habits with practice
8. **Review**: Check for violations in code reviews

---

## Additional Resources

- [Engineering Principles](../engineering-principles/README.md) - Core principles overview
- [Clean Code](../clean-code/README.md) - Writing quality code
- [Design Patterns](../08-design-patterns/README.md) - Common patterns
- [Books](../books/README.md) - Recommended reading
- [FAQs](../faqs/README.md) - Common questions

---

*Last Updated: August 2026*
