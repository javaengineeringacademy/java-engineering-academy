# Engineering Principles

Core software engineering principles that guide development, design, and architecture decisions for building maintainable, scalable, and high-quality software.

---

## Table of Contents

1. [Overview](#overview)
2. [KISS (Keep It Simple Stupid)](#kiss-keep-it-simple-stupid)
3. [DRY (Don't Repeat Yourself)](#dry-dont-repeat-yourself)
4. [YAGNI (You Aren't Gonna Need It)](#yagni-you-arent-gonna-need-it)
5. [SOLID Principles](#solid-principles)
6. [Separation of Concerns](#separation-of-concerns)
7. [Composition over Inheritance](#composition-over-inheritance)
8. [Other Important Principles](#other-important-principles)
9. [Best Practices](#best-practices)
10. [Common Mistakes](#common-mistakes)
11. [Key Takeaways](#key-takeaways)

---

## Overview

Engineering principles are fundamental truths that guide software development. They help developers make better decisions, write cleaner code, and build more maintainable systems.

### Why Principles Matter

- **Consistency**: Teams follow common guidelines
- **Quality**: Principles lead to better code
- **Maintainability**: Easier to update and fix
- **Scalability**: Systems grow without breaking
- **Communication**: Shared language and concepts

### How to Apply Principles

1. **Understand the principle**: Know why it exists
2. **Apply contextually**: Not all principles apply everywhere
3. **Balance principles**: Sometimes principles conflict
4. **Practice regularly**: Principles become habits
5. **Review and refine**: Continuously improve

---

## KISS (Keep It Simple Stupid)

### Definition

Keep It Simple, Stupid (KISS) principle states that most systems work best if they are kept simple rather than made complicated.

### Key Concepts

**Simplicity in Design**
- Simple solutions are easier to understand
- Simple code is easier to maintain
- Simple systems are easier to debug
- Simple designs are easier to extend

**Avoiding Over-Engineering**
- Don't build for hypothetical futures
- Avoid unnecessary complexity
- Use simple, proven solutions
- Refactor when needed, not before

### KISS in Practice

**Bad Example: Over-Engineered**
```java
public class UserManager {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final CacheService cacheService;
    
    public User createUser(String email, String password) {
        // Complex validation logic
        // Multiple service calls
        // Audit logging
        // Cache updates
        // Notification sending
        // ... 100 lines of code
    }
}
```

**Good Example: Simple**
```java
public class UserManager {
    private final UserRepository userRepository;
    
    public User createUser(String email, String password) {
        validateInput(email, password);
        User user = new User(email, password);
        return userRepository.save(user);
    }
    
    private void validateInput(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
}
```

### When to Apply KISS

- **Always**: Simplicity should be the default
- **Complex problems**: Break them into simple parts
- **Team code**: Others need to understand it
- **Maintenance**: Simple code is easier to fix

---

## DRY (Don't Repeat Yourself)

### Definition

Don't Repeat Yourself (DRY) principle states that every piece of knowledge must have a single, unambiguous, authoritative representation within a system.

### Key Concepts

**Identifying Duplication**
- Code duplication: Same logic in multiple places
- Knowledge duplication: Same information in multiple places
- Process duplication: Same steps in multiple places
- Data duplication: Same data in multiple places

**Abstraction Strategies**
- Extract methods for repeated code
- Create classes for repeated patterns
- Use templates for repeated structures
- Implement inheritance for shared behavior

### DRY in Practice

**Bad Example: Duplicated Code**
```java
public class OrderService {
    public void processOrder(Order order) {
        // Validate order
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have items");
        }
        if (order.getTotal() <= 0) {
            throw new IllegalArgumentException("Order total must be positive");
        }
        
        // Calculate discount
        double discount = 0;
        if (order.getCustomer().isPremium()) {
            discount = order.getTotal() * 0.1;
        }
        
        // Process payment
        double finalAmount = order.getTotal() - discount;
        paymentService.charge(order.getCustomer(), finalAmount);
    }
    
    public void processRefund(Refund refund) {
        // Same validation logic duplicated
        if (refund.getItems().isEmpty()) {
            throw new IllegalArgumentException("Refund must have items");
        }
        if (refund.getTotal() <= 0) {
            throw new IllegalArgumentException("Refund total must be positive");
        }
        
        // Same discount logic duplicated
        double discount = 0;
        if (refund.getCustomer().isPremium()) {
            discount = refund.getTotal() * 0.1;
        }
        
        // Process refund
        double finalAmount = refund.getTotal() - discount;
        paymentService.refund(refund.getCustomer(), finalAmount);
    }
}
```

**Good Example: DRY**
```java
public class OrderService {
    private final PaymentService paymentService;
    
    public void processOrder(Order order) {
        validateOrderItems(order);
        double finalAmount = calculateFinalAmount(order);
        paymentService.charge(order.getCustomer(), finalAmount);
    }
    
    public void processRefund(Refund refund) {
        validateRefundItems(refund);
        double finalAmount = calculateFinalAmount(refund);
        paymentService.refund(refund.getCustomer(), finalAmount);
    }
    
    private void validateOrderItems(Order order) {
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have items");
        }
        if (order.getTotal() <= 0) {
            throw new IllegalArgumentException("Order total must be positive");
        }
    }
    
    private void validateRefundItems(Refund refund) {
        if (refund.getItems().isEmpty()) {
            throw new IllegalArgumentException("Refund must have items");
        }
        if (refund.getTotal() <= 0) {
            throw new IllegalArgumentException("Refund total must be positive");
        }
    }
    
    private double calculateFinalAmount(TotalCalculator calculator) {
        double discount = 0;
        if (calculator.getCustomer().isPremium()) {
            discount = calculator.getTotal() * 0.1;
        }
        return calculator.getTotal() - discount;
    }
}
```

### When to Apply DRY

- **Repeated code**: Same logic in multiple places
- **Similar patterns**: Structures that repeat
- **Knowledge sharing**: Information used in multiple contexts
- **Process repetition**: Steps that repeat

---

## YAGNI (You Aren't Gonna Need It)

### Definition

You Aren't Gonna Need It (YAGNI) principle states that you should not add functionality until it is necessary.

### Key Concepts

**Avoiding Speculative Development**
- Don't build for hypothetical futures
- Don't add features "just in case"
- Don't over-architect for potential needs
- Focus on current requirements

**Benefits of YAGNI**
- Less code to write and maintain
- Faster delivery of current features
- Simpler design
- Reduced technical debt

### YAGNI in Practice

**Bad Example: Speculative Features**
```java
public class UserService {
    public User createUser(String email, String password) {
        // Current requirement: simple user creation
        User user = new User(email, password);
        
        // YAGNI: Adding features for hypothetical future needs
        user.setPreferences(new UserPreferences()); // Not needed yet
        user.setNotificationSettings(new NotificationSettings()); // Not needed yet
        user.setSecurityQuestions(new SecurityQuestions()); // Not needed yet
        user.setSocialConnections(new SocialConnections()); // Not needed yet
        
        return userRepository.save(user);
    }
}
```

**Good Example: YAGNI**
```java
public class UserService {
    public User createUser(String email, String password) {
        // Only implement what's needed now
        User user = new User(email, password);
        return userRepository.save(user);
    }
}
```

### When to Apply YAGNI

- **New features**: Only build what's required
- **Architecture**: Don't over-engineer
- **Optimization**: Don't optimize prematurely
- **Abstraction**: Don't abstract until you see the pattern

---

## SOLID Principles

### Overview

SOLID is an acronym for five design principles intended to make software designs more understandable, flexible, and maintainable.

### Single Responsibility Principle (SRP)

**Definition**: A class should have only one reason to change.

**Bad Example**
```java
class User {
    private String name;
    private String email;
    
    // User data
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    // Email validation (wrong place)
    public boolean isValidEmail() {
        return email != null && email.contains("@");
    }
    
    // Database operations (wrong place)
    public void saveToDatabase() {
        // Database logic
    }
    
    // Email sending (wrong place)
    public void sendWelcomeEmail() {
        // Email logic
    }
}
```

**Good Example**
```java
class User {
    private String name;
    private String email;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

class EmailValidator {
    public boolean isValid(String email) {
        return email != null && email.contains("@");
    }
}

class UserRepository {
    public void save(User user) {
        // Database logic
    }
}

class EmailService {
    public void sendWelcomeEmail(User user) {
        // Email logic
    }
}
```

### Open/Closed Principle (OCP)

**Definition**: Software entities should be open for extension, but closed for modification.

**Bad Example**
```java
class DiscountCalculator {
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

**Good Example**
```java
interface DiscountStrategy {
    double calculateDiscount(double amount);
}

class RegularDiscount implements DiscountStrategy {
    public double calculateDiscount(double amount) {
        return amount * 0.05;
    }
}

class PremiumDiscount implements DiscountStrategy {
    public double calculateDiscount(double amount) {
        return amount * 0.10;
    }
}

class VipDiscount implements DiscountStrategy {
    public double calculateDiscount(double amount) {
        return amount * 0.15;
    }
}

class DiscountCalculator {
    public double calculateDiscount(DiscountStrategy strategy, double amount) {
        return strategy.calculateDiscount(amount);
    }
}
```

### Liskov Substitution Principle (LSP)

**Definition**: Objects of a superclass should be replaceable with objects of its subclasses without breaking the application.

**Bad Example**
```java
class Rectangle {
    protected int width;
    protected int height;
    
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int getArea() { return width * height; }
}

class Square extends Rectangle {
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
void printArea(Rectangle r) {
    r.setWidth(5);
    r.setHeight(4);
    // Expected: 20, but Square gives 16
    System.out.println(r.getArea());
}
```

**Good Example**
```java
interface Shape {
    int getArea();
}

class Rectangle implements Shape {
    private int width;
    private int height;
    
    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getArea() { return width * height; }
}

class Square implements Shape {
    private int side;
    
    public Square(int side) {
        this.side = side;
    }
    
    public int getArea() { return side * side; }
}
```

### Interface Segregation Principle (ISP)

**Definition**: Clients should not be forced to depend on interfaces they do not use.

**Bad Example**
```java
interface Worker {
    void work();
    void eat();
    void sleep();
}

class Robot implements Worker {
    public void work() { /* working */ }
    public void eat() { /* robots don't eat */ }
    public void sleep() { /* robots don't sleep */ }
}
```

**Good Example**
```java
interface Workable {
    void work();
}

interface Feedable {
    void eat();
}

interface Sleepable {
    void sleep();
}

class Human implements Workable, Feedable, Sleepable {
    public void work() { /* working */ }
    public void eat() { /* eating */ }
    public void sleep() { /* sleeping */ }
}

class Robot implements Workable {
    public void work() { /* working */ }
}
```

### Dependency Inversion Principle (DIP)

**Definition**: High-level modules should not depend on low-level modules. Both should depend on abstractions.

**Bad Example**
```java
class MySQLDatabase {
    public void save(String data) {
        // MySQL specific logic
    }
}

class UserService {
    private MySQLDatabase database;
    
    public UserService() {
        this.database = new MySQLDatabase(); // Direct dependency
    }
    
    public void saveUser(String user) {
        database.save(user);
    }
}
```

**Good Example**
```java
interface Database {
    void save(String data);
}

class MySQLDatabase implements Database {
    public void save(String data) {
        // MySQL specific logic
    }
}

class PostgreSQLDatabase implements Database {
    public void save(String data) {
        // PostgreSQL specific logic
    }
}

class UserService {
    private Database database;
    
    public UserService(Database database) {
        this.database = database; // Depends on abstraction
    }
    
    public void saveUser(String user) {
        database.save(user);
    }
}
```

---

## Separation of Concerns

### Definition

Separation of Concerns (SoC) is a design principle for separating a computer program into distinct sections, where each section addresses a separate concern.

### Key Concepts

**Distinct Responsibilities**
- Each module has a single purpose
- Changes in one area don't affect others
- Easier to understand and maintain
- Better testability

**Layers of Concern**
- Presentation: User interface
- Business logic: Application rules
- Data access: Database operations
- Infrastructure: Technical concerns

### Separation of Concerns in Practice

**Bad Example: Mixed Concerns**
```java
public class UserController {
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        // Validation (presentation concern)
        if (userDto.getEmail() == null) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        
        // Business logic (business concern)
        User user = new User(userDto.getEmail(), userDto.getPassword());
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        
        // Data access (data concern)
        userRepository.save(user);
        
        // Email sending (infrastructure concern)
        emailService.sendWelcomeEmail(user);
        
        // Response (presentation concern)
        return ResponseEntity.ok(user);
    }
}
```

**Good Example: Separated Concerns**
```java
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
        User user = userService.createUser(userDto);
        return ResponseEntity.ok(user);
    }
}

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    public User createUser(UserDto userDto) {
        User user = mapToUser(userDto);
        validateUser(user);
        User savedUser = userRepository.save(user);
        emailService.sendWelcomeEmail(savedUser);
        return savedUser;
    }
    
    private void validateUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }
    }
}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}

@Service
public class EmailService {
    public void sendWelcomeEmail(User user) {
        // Email logic
    }
}
```

---

## Composition over Inheritance

### Definition

Composition over inheritance is a principle that suggests preferring object composition over class inheritance for code reuse.

### Key Concepts

**Inheritance Issues**
- Tight coupling between parent and child
- Fragile base class problem
- Limited to single inheritance in many languages
- Can lead to deep hierarchies

**Composition Benefits**
- Loose coupling between components
- More flexible code reuse
- Easier to test and mock
- Better runtime flexibility

### Composition vs. Inheritance

**Inheritance Example**
```java
class Animal {
    protected String name;
    
    public void eat() {
        System.out.println(name + " is eating");
    }
    
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
}

class Dog extends Animal {
    public void bark() {
        System.out.println(name + " is barking");
    }
}

class Cat extends Animal {
    public void meow() {
        System.out.println(name + " is meowing");
    }
}
```

**Composition Example**
```java
interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

class EatingBehavior implements Eatable {
    public void eat() {
        System.out.println("Eating");
    }
}

class SleepingBehavior implements Sleepable {
    public void sleep() {
        System.out.println("Sleeping");
    }
}

class Animal {
    private String name;
    private Eatable eatable;
    private Sleepable sleepable;
    
    public Animal(String name, Eatable eatable, Sleepable sleepable) {
        this.name = name;
        this.eatable = eatable;
        this.sleepable = sleepable;
    }
    
    public void eat() { eatable.eat(); }
    public void sleep() { sleepable.sleep(); }
}

class Dog extends Animal {
    private BarkingBehavior barkingBehavior;
    
    public Dog(String name) {
        super(name, new EatingBehavior(), new SleepingBehavior());
        this.barkingBehavior = new BarkingBehavior();
    }
    
    public void bark() { barkingBehavior.bark(); }
}
```

---

## Other Important Principles

### Law of Demeter

**Definition**: Only talk to your immediate friends; don't talk to strangers.

**Key Concept**: Avoid method chaining that reaches into other objects.

**Bad Example**
```java
// Violates Law of Demeter
customer.getOrder().getItems().get(0).getPrice();
```

**Good Example**
```java
// Follows Law of Demeter
customer.getFirstItemPrice();
```

### Principle of Least Surprise

**Definition**: A component of a system should behave in a way that most users will expect it to behave.

**Key Concept**: APIs should be intuitive and predictable.

**Example**
```java
// Bad: Surprising behavior
public int add(int a, int b) {
    return a - b; // Subtraction instead of addition
}

// Good: Expected behavior
public int add(int a, int b) {
    return a + b;
}
```

### Fail-Fast

**Definition**: A system should fail as soon as possible rather than processing invalid data.

**Key Concept**: Detect errors early and fail immediately.

**Example**
```java
// Bad: Fails late
public void processOrder(Order order) {
    // ... 100 lines of processing ...
    if (order.getTotal() <= 0) {
        throw new IllegalArgumentException("Invalid total");
    }
    // ... more processing ...
}

// Good: Fails fast
public void processOrder(Order order) {
    validateOrder(order); // Validates immediately
    // ... processing ...
}

private void validateOrder(Order order) {
    if (order == null) {
        throw new IllegalArgumentException("Order cannot be null");
    }
    if (order.getTotal() <= 0) {
        throw new IllegalArgumentException("Order total must be positive");
    }
}
```

---

## Best Practices

### Applying Principles

1. **Understand the why**: Know why the principle exists
2. **Apply contextually**: Not all principles apply everywhere
3. **Balance principles**: Sometimes principles conflict
4. **Practice regularly**: Principles become habits
5. **Review and refine**: Continuously improve

### Code Reviews

1. **Check for principle violations**: Identify issues early
2. **Discuss trade-offs**: Principles aren't absolute
3. **Suggest improvements**: Help team learn
4. **Document decisions**: Record why choices were made
5. **Learn from mistakes**: Improve together

### Team Practices

1. **Agree on principles**: Team alignment
2. **Share knowledge**: Teach and learn together
3. **Review regularly**: Check adherence
4. **Refactor when needed**: Improve existing code
5. **Celebrate improvements**: Acknowledge progress

---

## Common Mistakes

### Principle Mistakes

1. **Applying blindly**: Without considering context
2. **Over-engineering**: Making things too complex
3. **Ignoring trade-offs**: Principles aren't absolute
4. **Not practicing**: Knowing but not doing
5. **Dogmatic adherence**: Never deviating from principles

### Implementation Mistakes

1. **Premature abstraction**: Abstracting too early
2. **Ignoring simplicity**: Making things complex
3. **Over-designing**: Building for hypothetical futures
4. **Not refactoring**: Letting code rot
5. **Poor documentation**: Not recording decisions

### Team Mistakes

1. **Not aligning**: Team disagrees on principles
2. **Not sharing knowledge**: Principles stay with individuals
3. **Not reviewing**: Missing violations
4. **Not improving**: Stagnant practices
5. **Not celebrating**: Missing recognition

---

## Key Takeaways

1. **KISS**: Keep it simple; simplicity is key
2. **DRY**: Don't repeat yourself; abstract duplication
3. **YAGNI**: Don't build what you don't need yet
4. **SOLID**: Five principles for maintainable code
5. **Separation of Concerns**: Separate distinct responsibilities
6. **Composition over Inheritance**: Prefer composition for flexibility
7. **Balance principles**: Context matters; principles aren't absolute
8. **Practice regularly**: Principles become habits with practice

---

## Additional Resources

- [SOLID](../solid/README.md) - Detailed SOLID examples
- [DRY](../dry/README.md) - DRY principle deep dive
- [KISS](../kiss/README.md) - KISS principle deep dive
- [YAGNI](../yagni/README.md) - YAGNI principle deep dive
- [Clean Code](../clean-code/README.md) - Writing quality code
- [Books](../books/README.md) - Recommended reading

---

*Last Updated: August 2026*
