# SOLID Principles in Object-Oriented Programming

## 1. Introduction

SOLID is an acronym representing five fundamental principles of object-oriented design, introduced by Robert C. Martin (Uncle Bob). These principles help developers create systems that are:

- **Maintainable**: Easy to modify and extend
- **Testable**: Easy to unit test
- **Flexible**: Resilient to change
- **Scalable**: Can grow without major refactoring

The five principles are:

| Principle | Description |
|-----------|-------------|
| **S** - Single Responsibility | A class should have one, and only one, reason to change |
| **O** - Open/Closed | Open for extension, closed for modification |
| **L** - Liskov Substitution | Subtypes must be substitutable for their base types |
| **I** - Interface Segregation | Many specific interfaces are better than one general-purpose interface |
| **D** - Dependency Inversion | Depend on abstractions, not concretions |

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Define and explain each of the five SOLID principles
- Identify violations of SOLID principles in existing code
- Refactor code to comply with SOLID principles
- Apply SOLID principles when designing new systems
- Recognize the trade-offs and tensions between principles
- Use SOLID principles to guide design decisions in enterprise applications
- Understand how SOLID relates to design patterns and testability

---

## 3. Prerequisites

Before studying SOLID principles, you should be familiar with:

- **Object-Oriented Programming**: Classes, objects, inheritance, polymorphism
- **Java Fundamentals**: Syntax, collections, exception handling
- **UML Basics**: Class diagrams, relationships
- **Basic Design Patterns**: Singleton, Factory, Strategy (helpful but not required)
- **Unit Testing Concepts**: Why testability matters

---

## 4. Why This Concept Exists

### The Problem Without SOLID

Consider an e-commerce system designed without SOLID principles:

```java
// A "God class" that does everything
public class OrderManager {
    public void createOrder(Order order) {
        // Validation logic
        // Payment processing
        // Inventory updates
        // Email notifications
        // Database operations
        // Logging
        // Report generation
    }
    
    public void processPayment(Payment payment) { ... }
    public void sendEmail(String to, String body) { ... }
    public void updateInventory(Item item) { ... }
    public void generateReport() { ... }
    public void logActivity(String activity) { ... }
}
```

**Consequences:**
- **Hard to test**: Can't test payment without email
- **Hard to modify**: Changing email logic affects payments
- **Hard to understand**: 500+ lines doing everything
- **Hard to reuse**: Can't reuse email logic elsewhere
- **Fragile**: One change breaks multiple features

### SOLID Benefits

1. **Reduced Coupling**: Changes in one module don't ripple everywhere
2. **Improved Testability**: Easy to mock dependencies
3. **Better Readability**: Small, focused classes
4. **Easier Maintenance**: Changes are localized
5. **Enhanced Reusability**: Components can be used in multiple contexts
6. **Scalable Architecture**: New features added without modifying existing code

---

## 5. Problem Statement

### Real-World Scenario: Payment Processing System

A payment processing system that violates all five SOLID principles:

```java
// Violates SRP: Multiple responsibilities
// Violates OCP: Requires modification for new payment types
// Violates LSP: Subclasses may break expected behavior
// Violates ISP: Forces implementing unused methods
// Violates DIP: Depends on concrete classes
public class PaymentProcessor {
    private StripeGateway stripeGateway = new StripeGateway();
    private PayPalGateway payPalGateway = new PayPalGateway();
    
    public void processCreditCard(CreditCard card, BigDecimal amount) {
        // Validation logic
        // Fraud detection
        // Stripe processing
        // Database storage
        // Email notification
        // Receipt generation
        // Tax calculation
        // Currency conversion
    }
    
    public void processPayPal(String email, BigDecimal amount) {
        // Similar duplicated logic...
    }
    
    public void processCrypto(String walletAddress, BigDecimal amount) {
        // Even more duplicated logic...
    }
    
    public void sendEmail(String to, String subject, String body) { ... }
    public void saveToDatabase(Transaction tx) { ... }
    public void generateReceipt(Transaction tx) { ... }
    public void calculateTax(BigDecimal amount) { ... }
    public void convertCurrency(BigDecimal amount, String from, String to) { ... }
}
```

**Issues:**
1. **SRP Violation**: This class handles validation, processing, storage, notifications, and reporting
2. **OCP Violation**: Adding cryptocurrency requires modifying this class
3. **LSP Violation**: Different payment types have different validation rules
4. **ISP Violation**: All payment methods implement all interface methods
5. **DIP Violation**: Directly depends on StripeGateway and PayPalGateway

---

## 6. Theory

### The Five Principles

#### Single Responsibility Principle (SRP)

**Definition:** A class should have one, and only one, reason to change.

**Core Idea:** Each class should be responsible for a single piece of functionality. This makes the class easier to understand, test, and modify.

**Key Concept:** "Reason to change" refers to who the stakeholders are. If multiple stakeholders can request changes to the same class, it violates SRP.

#### Open/Closed Principle (OCP)

**Definition:** Software entities should be open for extension but closed for modification.

**Core Idea:** You should be able to add new functionality without changing existing code. This is achieved through abstraction and polymorphism.

**Key Concept:** "Open for extension" means the behavior can be extended. "Closed for modification" means the existing code doesn't need to change.

#### Liskov Substitution Principle (LSP)

**Definition:** Objects of a superclass should be replaceable with objects of its subclasses without affecting the correctness of the program.

**Core Idea:** Subclasses must honor the contract of their parent class. If a subclass changes the expected behavior, it violates LSP.

**Key Concept:** Behavioral subtyping - the subclass must preserve the semantics (behavior) of the parent class.

#### Interface Segregation Principle (ISP)

**Definition:** No client should be forced to depend on methods it does not use.

**Core Idea:** Create small, specific interfaces rather than large, general-purpose ones. Clients shouldn't implement methods they don't need.

**Key Concept:** "Fat" interfaces with many methods force implementers to carry dead code.

#### Dependency Inversion Principle (DIP)

**Definition:** High-level modules should not depend on low-level modules. Both should depend on abstractions. Abstractions should not depend on details. Details should depend on abstractions.

**Core Idea:** Depend on interfaces, not concrete implementations. This allows swapping implementations without changing the calling code.

**Key Concept:** Invert the dependency direction - high-level policy should not depend on low-level details.

---

## 7. Internal Working

### How SOLID Principles Interact

```
┌─────────────────────────────────────────────────────────────┐
│                    SOLID Ecosystem                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────┐     ┌─────────────┐     ┌─────────────┐  │
│  │    SRP      │────▶│    OCP      │────▶│    LSP      │  │
│  │ (Responsibility)│  │ (Extension) │    │(Substitution)│  │
│  └─────────────┘     └─────────────┘     └─────────────┘  │
│         │                   │                   │          │
│         ▼                   ▼                   ▼          │
│  ┌─────────────┐     ┌─────────────┐                       │
│  │    ISP      │◀────│    DIP      │                       │
│  │(Interfaces) │     │(Abstractions)│                       │
│  └─────────────┘     └─────────────┘                       │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Relationship Between Principles

1. **SRP enables OCP**: When classes have single responsibilities, it's easier to extend behavior
2. **OCP relies on DIP**: Extension through abstraction requires depending on interfaces
3. **LSP depends on proper design**: Substitutability requires careful interface design
4. **ISP supports DIP**: Focused interfaces make dependency inversion practical
5. **DIP supports OCP**: Abstractions enable modification without changing code

### Design Pattern Connections

| Principle | Related Patterns |
|-----------|------------------|
| SRP | Facade, Mediator, Command |
| OCP | Strategy, Template Method, Observer |
| LSP | Factory Method, Abstract Factory |
| ISP | Adapter, Decorator |
| DIP | Factory, Service Locator, Dependency Injection |

---

## 8. JVM Perspective

### Method Dispatch and SOLID

```
┌─────────────────────────────────────────────────────────────┐
│         Virtual Method Table (vtable)                       │
├─────────────────────────────────────────────────────────────┤
│  Class: Animal                                              │
│  ├─ makeSound() → Animal.makeSound()                       │
│  └─ sleep() → Animal.sleep()                               │
├─────────────────────────────────────────────────────────────┤
│  Class: Dog extends Animal                                  │
│  ├─ makeSound() → Dog.makeSound() (override)               │
│  └─ sleep() → Animal.sleep() (inherited)                   │
├─────────────────────────────────────────────────────────────┤
│  Class: Cat extends Animal                                  │
│  ├─ makeSound() → Cat.makeSound() (override)               │
│  └─ sleep() → Cat.sleep() (override)                       │
└─────────────────────────────────────────────────────────────┘

Runtime: Animal a = new Dog();
         a.makeSound(); // → Dog.makeSound() via vtable lookup
```

### Interface Method Table (itable)

```
┌─────────────────────────────────────────────────────────────┐
│         Interface Method Resolution                        │
├─────────────────────────────────────────────────────────────┤
│  Class: CreditCardPayment implements PaymentMethod          │
│  ├─ itable[0] → PaymentMethod.charge()                     │
│  ├─ itable[1] → PaymentMethod.validate()                   │
│  └─ itable[2] → PaymentMethod.refund()                     │
├─────────────────────────────────────────────────────────────┤
│  Class: PayPalPayment implements PaymentMethod              │
│  ├─ itable[0] → PaymentMethod.charge()                     │
│  ├─ itable[1] → PaymentMethod.validate()                   │
│  └─ itable[2] → PaymentMethod.refund()                     │
└─────────────────────────────────────────────────────────────┘

When calling via interface:
  PaymentMethod pm = new CreditCardPayment();
  pm.charge(); // → Looks up itable[0] → CreditCardPayment.charge()
```

### JIT Optimization for SOLID Code

```java
// Well-designed SOLID code enables better JIT optimization
public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
}

// Monomorphic call site - JIT can inline
PaymentProcessor processor = new StripeProcessor();
processor.process(request); // JIT may inline this

// Megamorphic call site - JIT uses vtable/itable
List<PaymentProcessor> processors = List.of(
    new StripeProcessor(),
    new PayPalProcessor(),
    new CryptoProcessor()
);
for (PaymentProcessor p : processors) {
    p.process(request); // Multiple implementations, less optimization
}
```

---

## 9. Memory Representation

### Object Layout with SOLID Design

```
┌─────────────────────────────────────────────────────────────┐
│         Well-Designed SOLID Object                         │
├─────────────────────────────────────────────────────────────┤
│  Header (12-16 bytes)                                       │
│  ├─ Mark Word (8 bytes) - GC info, hash code               │
│  └─ Klass Pointer (4 bytes) - class metadata               │
├─────────────────────────────────────────────────────────────┤
│  SRP: Single responsibility fields                         │
│  ├─ paymentId (String reference)                           │
│  └─ amount (BigDecimal reference)                          │
├─────────────────────────────────────────────────────────────┤
│  DIP: Abstraction references                               │
│  ├─ validator (PaymentValidator reference)                 │
│  ├─ gateway (PaymentGateway reference)                     │
│  └─ notifier (NotificationService reference)               │
├─────────────────────────────────────────────────────────────┤
│  Method pointers (vtable/itable)                           │
│  ├─ process() → StripeProcessor.process()                 │
│  └─ validate() → PaymentValidator.validate()              │
└─────────────────────────────────────────────────────────────┘
```

### Dependency Injection and Memory

```java
// DIP: Depends on abstractions
public class OrderService {
    private final PaymentProcessor processor;  // Interface reference
    private final OrderRepository repository;  // Interface reference
    
    // Constructor injection
    public OrderService(PaymentProcessor processor, OrderRepository repository) {
        this.processor = processor;  // Stores reference to implementation
        this.repository = repository;
    }
}

// Memory layout:
// OrderService object
// ├─ processor → StripeProcessor object (separate heap allocation)
// └─ repository → JdbcOrderRepository object (separate heap allocation)
```

---

## 10. Syntax

### SRP Implementation

```java
// BEFORE: Multiple responsibilities
public class UserManager {
    public void saveUser(User user) { ... }
    public void sendEmail(User user) { ... }
    public void generateReport() { ... }
}

// AFTER: Single responsibilities
public class UserRepository {
    public void save(User user) { ... }
    public User findById(Long id) { ... }
}

public class EmailService {
    public void sendWelcomeEmail(User user) { ... }
    public void sendPasswordReset(User user) { ... }
}

public class ReportGenerator {
    public Report generateUserReport(User user) { ... }
}
```

### OCP Implementation

```java
// BEFORE: Requires modification for new types
public class PaymentProcessor {
    public void process(String type, BigDecimal amount) {
        if (type.equals("CREDIT_CARD")) {
            // Credit card logic
        } else if (type.equals("PAYPAL")) {
            // PayPal logic
        }
        // Adding new type requires modifying this method
    }
}

// AFTER: Open for extension
public interface PaymentMethod {
    PaymentResult process(BigDecimal amount);
}

public class CreditCardPayment implements PaymentMethod {
    @Override
    public PaymentResult process(BigDecimal amount) {
        // Credit card logic
    }
}

public class PayPalPayment implements PaymentMethod {
    @Override
    public PaymentResult process(BigDecimal amount) {
        // PayPal logic
    }
}

// New payment types can be added without modifying existing code
public class CryptoPayment implements PaymentMethod {
    @Override
    public PaymentResult process(BigDecimal amount) {
        // Crypto logic
    }
}
```

### LSP Implementation

```java
// BEFORE: Violates LSP
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
        this.height = width; // Unexpected side effect!
    }
}

// Usage breaks LSP
Rectangle rect = new Square();
rect.setWidth(5);
rect.setHeight(10);
assert rect.getArea() == 50; // FAILS! Actual: 100

// AFTER: LSP compliant
public interface Shape {
    int getArea();
}

public record Rectangle(int width, int height) implements Shape {
    @Override
    public int getArea() {
        return width * height;
    }
}

public record Square(int side) implements Shape {
    @Override
    public int getArea() {
        return side * side;
    }
}

// All shapes are substitutable
Shape shape = new Square(5);
assert shape.getArea() == 25; // Works correctly
```

### ISP Implementation

```java
// BEFORE: Fat interface
public interface Worker {
    void work();
    void eat();
    void sleep();
    void takeBreak();
}

// Robot doesn't need eat() or sleep()
public class Robot implements Worker {
    @Override
    public void work() { /* Robot works */ }
    
    @Override
    public void eat() { /* Robots don't eat - violates ISP */ }
    
    @Override
    public void sleep() { /* Robots don't sleep - violates ISP */ }
    
    @Override
    public void takeBreak() { /* Robots don't take breaks */ }
}

// AFTER: Segregated interfaces
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
    public void work() { /* Robot works */ }
    // No need to implement eat() or sleep()
}

public class Human implements Workable, Feedable, Sleepable {
    @Override
    public void work() { /* Human works */ }
    
    @Override
    public void eat() { /* Human eats */ }
    
    @Override
    public void sleep() { /* Human sleeps */ }
}
```

### DIP Implementation

```java
// BEFORE: Depends on concretions
public class OrderService {
    private MySQLDatabase database = new MySQLDatabase();
    private StripeGateway gateway = new StripeGateway();
    private SMTPMailService mailService = new SMTPMailService();
    
    public void placeOrder(Order order) {
        database.save(order);
        gateway.charge(order.getAmount());
        mailService.send(order.getEmail(), "Order placed");
    }
}

// AFTER: Depends on abstractions
public interface Database {
    void save(Order order);
    Optional<Order> findById(Long id);
}

public interface PaymentGateway {
    PaymentResult charge(BigDecimal amount);
    RefundResult refund(String transactionId);
}

public interface EmailService {
    void send(String to, String subject, String body);
}

public class OrderService {
    private final Database database;
    private final PaymentGateway gateway;
    private final EmailService emailService;
    
    // Constructor injection
    public OrderService(Database database, 
                       PaymentGateway gateway,
                       EmailService emailService) {
        this.database = database;
        this.gateway = gateway;
        this.emailService = emailService;
    }
    
    public void placeOrder(Order order) {
        database.save(order);
        PaymentResult result = gateway.charge(order.getAmount());
        if (result.isSuccess()) {
            emailService.send(order.getEmail(), "Order placed", "Thank you!");
        }
    }
}

// Implementations can be swapped
public class MongoDatabase implements Database {
    @Override
    public void save(Order order) { /* MongoDB logic */ }
    
    @Override
    public Optional<Order> findById(Long id) { /* MongoDB logic */ }
}

public class StripeGateway implements PaymentGateway {
    @Override
    public PaymentResult charge(BigDecimal amount) { /* Stripe logic */ }
    
    @Override
    public RefundResult refund(String transactionId) { /* Stripe logic */ }
}
```

---

## 11. Easy Example

### Single Responsibility Principle

```java
// Simple example: User management with SRP

// BEFORE (violates SRP):
public class UserManager {
    public void createUser(User user) {
        // Save to database
        // Send welcome email
        // Log activity
        // Generate user ID
    }
}

// AFTER (follows SRP):
public class UserRepository {
    public User save(User user) {
        System.out.println("Saving user to database: " + user.getName());
        return user;
    }
}

public class WelcomeEmailSender {
    public void sendWelcomeEmail(User user) {
        System.out.println("Sending welcome email to: " + user.getEmail());
    }
}

public class ActivityLogger {
    public void logUserCreation(User user) {
        System.out.println("User created: " + user.getName());
    }
}

public class UserIdGenerator {
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}

// Each class has ONE reason to change
public class UserService {
    private final UserRepository repository;
    private final WelcomeEmailSender emailSender;
    private final ActivityLogger logger;
    
    public UserService(UserRepository repository, 
                      WelcomeEmailSender emailSender,
                      ActivityLogger logger) {
        this.repository = repository;
        this.emailSender = emailSender;
        this.logger = logger;
    }
    
    public User createUser(String name, String email) {
        User user = new User(name, email);
        repository.save(user);
        emailSender.sendWelcomeEmail(user);
        logger.logUserCreation(user);
        return user;
    }
}
```

---

## 12. Medium Example

### Open/Closed Principle

```java
// Notification system following OCP

// Abstraction
public interface NotificationSender {
    boolean supports(NotificationType type);
    void send(Notification notification);
}

public enum NotificationType {
    EMAIL, SMS, PUSH, SLACK
}

// Concrete implementations
public class EmailSender implements NotificationSender {
    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.EMAIL;
    }
    
    @Override
    public void send(Notification notification) {
        System.out.println("Email sent to: " + notification.getRecipient());
    }
}

public class SmsSender implements NotificationSender {
    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.SMS;
    }
    
    @Override
    public void send(Notification notification) {
        System.out.println("SMS sent to: " + notification.getRecipient());
    }
}

public class PushSender implements NotificationSender {
    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.PUSH;
    }
    
    @Override
    public void send(Notification notification) {
        System.out.println("Push notification sent to: " + notification.getRecipient());
    }
}

// Notification service - open for extension, closed for modification
public class NotificationService {
    private final List<NotificationSender> senders;
    
    public NotificationService(List<NotificationSender> senders) {
        this.senders = senders;
    }
    
    public void sendNotification(Notification notification) {
        senders.stream()
            .filter(sender -> sender.supports(notification.getType()))
            .findFirst()
            .ifPresent(sender -> sender.send(notification));
    }
    
    // NEW senders can be added without modifying this class
}

// Usage
List<NotificationSender> senders = List.of(
    new EmailSender(),
    new SmsSender(),
    new PushSender()
);

NotificationService service = new NotificationService(senders);
service.sendNotification(new Notification(
    NotificationType.EMAIL, 
    "user@example.com", 
    "Hello!"
));
```

---

## 13. Hard Example

### Liskov Substitution Principle

```java
// Payment processing with LSP compliance

// BEFORE (violates LSP):
public class PaymentProcessor {
    public void process(Payment payment) {
        if (payment.getType() == PaymentType.CREDIT_CARD) {
            processCreditCard(payment);
        } else if (payment.getType() == PaymentType.PAYPAL) {
            processPayPal(payment);
        }
        // Violates OCP and potentially LSP
    }
    
    private void processCreditCard(Payment payment) {
        // Credit card specific logic
    }
    
    private void processPayPal(Payment payment) {
        // PayPal specific logic - may have different validation
    }
}

// AFTER (follows LSP):
public interface PaymentProcessor {
    PaymentResult process(Payment payment);
    boolean supports(PaymentType type);
    void validate(Payment payment);
}

public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public PaymentResult process(Payment payment) {
        validate(payment);
        // Credit card processing
        return PaymentResult.success();
    }
    
    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.CREDIT_CARD;
    }
    
    @Override
    public void validate(Payment payment) {
        if (payment.getCreditCardNumber() == null) {
            throw new InvalidPaymentException("Card number required");
        }
        // Credit card specific validation
    }
}

public class PayPalProcessor implements PaymentProcessor {
    @Override
    public PaymentResult process(Payment payment) {
        validate(payment);
        // PayPal processing
        return PaymentResult.success();
    }
    
    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.PAYPAL;
    }
    
    @Override
    public void validate(Payment payment) {
        if (payment.getPayPalEmail() == null) {
            throw new InvalidPaymentException("PayPal email required");
        }
        // PayPal specific validation - different rules
    }
}

// Usage: All processors are substitutable
public class PaymentService {
    private final Map<PaymentType, PaymentProcessor> processors;
    
    public PaymentService(List<PaymentProcessor> processorList) {
        this.processors = processorList.stream()
            .collect(Collectors.toMap(
                PaymentProcessor::supports,
                Function.identity()
            ));
    }
    
    public PaymentResult processPayment(Payment payment) {
        PaymentProcessor processor = processors.get(payment.getType());
        if (processor == null) {
            throw new UnsupportedPaymentTypeException(payment.getType());
        }
        // Any processor can be used here - LSP compliance
        return processor.process(payment);
    }
}
```

---

## 14. Enterprise Example

### Interface Segregation Principle

```java
// Enterprise reporting system with ISP

// BEFORE (violates ISP):
public interface ReportGenerator {
    Report generateSalesReport(DateRange range);
    Report generateInventoryReport(DateRange range);
    Report generateUserActivityReport(DateRange range);
    Report generateFinancialReport(DateRange range);
    void exportToPdf(Report report);
    void exportToExcel(Report report);
    void sendReportByEmail(Report report, String email);
    void scheduleReport(ReportConfig config);
}

// Sales module doesn't need inventory or user activity reports
public class SalesReportService implements ReportGenerator {
    // Forced to implement ALL methods, even irrelevant ones
    @Override
    public Report generateInventoryReport(DateRange range) {
        throw new UnsupportedOperationException("Not my responsibility");
    }
    
    @Override
    public Report generateUserActivityReport(DateRange range) {
        throw new UnsupportedOperationException("Not my responsibility");
    }
    // ... more useless implementations
}

// AFTER (follows ISP):
public interface SalesReportGenerator {
    Report generateSalesReport(DateRange range);
}

public interface InventoryReportGenerator {
    Report generateInventoryReport(DateRange range);
}

public interface UserActivityReportGenerator {
    Report generateUserActivityReport(DateRange range);
}

public interface ReportExporter {
    void exportToPdf(Report report);
    void exportToExcel(Report report);
}

public interface ReportDistributor {
    void sendReportByEmail(Report report, String email);
    void scheduleReport(ReportConfig config);
}

// Each module implements only what it needs
public class SalesReportService implements SalesReportGenerator {
    @Override
    public Report generateSalesReport(DateRange range) {
        // Only sales report logic
        return new Report("Sales Report", range);
    }
}

public class InventoryReportService implements InventoryReportGenerator {
    @Override
    public Report generateInventoryReport(DateRange range) {
        // Only inventory report logic
        return new Report("Inventory Report", range);
    }
}

// Compose functionality as needed
public class ReportManager {
    private final SalesReportGenerator salesReportGenerator;
    private final ReportExporter reportExporter;
    private final ReportDistributor reportDistributor;
    
    public ReportManager(SalesReportGenerator salesReportGenerator,
                        ReportExporter reportExporter,
                        ReportDistributor reportDistributor) {
        this.salesReportGenerator = salesReportGenerator;
        this.reportExporter = reportExporter;
        this.reportDistributor = reportDistributor;
    }
    
    public void generateAndSendSalesReport(DateRange range, String email) {
        Report report = salesReportGenerator.generateSalesReport(range);
        reportExporter.exportToPdf(report);
        reportDistributor.sendReportByEmail(report, email);
    }
}
```

---

## 15. Performance

### SOLID and Performance Trade-offs

| Principle | Performance Impact | Mitigation |
|-----------|-------------------|------------|
| SRP | Minimal - small classes load faster | Profile hot paths |
| OCP | Slight overhead from polymorphism | JIT inlining for monomorphic sites |
| LSP | No direct impact | Proper design prevents issues |
| ISP | Minimal - fewer methods to implement | Interface caching |
| DIP | Indirection overhead | Constructor injection, final classes |

### Benchmarking SOLID Code

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SolidBenchmark {
    
    // DIP: Depends on abstraction
    private PaymentProcessor processor;
    
    @Setup
    public void setup() {
        processor = new StripeProcessor(); // Concrete for benchmark
    }
    
    @Benchmark
    public void directCall() {
        // Direct method call - fastest
        processor.process(createPayment());
    }
    
    @Benchmark
    public void interfaceCall() {
        // Interface method call - slightly slower
        PaymentProcessor p = processor;
        p.process(createPayment());
    }
    
    @Benchmark
    public void abstractClassCall() {
        // Abstract class call - similar to direct
        AbstractPaymentProcessor p = new StripeAbstractProcessor();
        p.process(createPayment());
    }
}
```

**Typical Results:**
- Direct call: ~15 ns
- Interface call: ~20 ns (33% overhead)
- Abstract class call: ~16 ns (7% overhead)

**Optimization Strategies:**
1. Keep class hierarchies shallow
2. Use `final` classes for performance-critical code
3. Enable aggressive JIT optimization
4. Profile before optimizing

---

## 16. Best Practices

### SRP Best Practices

```java
// DO: Keep classes focused
public class EmailValidator {
    public boolean isValid(String email) {
        return email != null && email.contains("@");
    }
}

public class UserRepository {
    public User save(User user) {
        // Only database operations
    }
}

// DON'T: Create God classes
public class UserManager {
    public void saveUser(User user) { ... }
    public void sendEmail(User user) { ... }
    public void generateReport() { ... }
    public void backupData() { ... }
}
```

### OCP Best Practices

```java
// DO: Use abstraction for extension points
public interface DiscountStrategy {
    BigDecimal calculateDiscount(BigDecimal amount);
}

public class PercentageDiscount implements DiscountStrategy {
    private final double percentage;
    
    @Override
    public BigDecimal calculateDiscount(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(percentage / 100));
    }
}

// DON'T: Use type checking and modification
public class DiscountCalculator {
    public BigDecimal calculate(String type, BigDecimal amount) {
        if (type.equals("PERCENTAGE")) {
            return amount.multiply(BigDecimal.valueOf(0.1));
        } else if (type.equals("FLAT")) {
            return amount.subtract(BigDecimal.TEN);
        }
        // Adding new types requires modifying this method
    }
}
```

### LSP Best Practices

```java
// DO: Ensure substitutability
public abstract class Shape {
    public abstract int getArea();
}

public class Circle extends Shape {
    private final int radius;
    
    @Override
    public int getArea() {
        return (int) (Math.PI * radius * radius);
    }
}

// DON'T: Change expected behavior in subclasses
public class Square extends Rectangle {
    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width; // Unexpected side effect!
    }
}
```

### ISP Best Practices

```java
// DO: Create focused interfaces
public interface Readable {
    String read();
}

public interface Writable {
    void write(String data);
}

public interface Closeable {
    void close() throws IOException;
}

// DON'T: Create fat interfaces
public interface FileOperations {
    String read();
    void write(String data);
    void delete();
    void copy(String destination);
    void move(String destination);
    void rename(String newName);
    void setPermissions(String permissions);
    // Too many methods!
}
```

### DIP Best Practices

```java
// DO: Depend on abstractions
public class OrderService {
    private final OrderRepository repository;
    private final PaymentProcessor paymentProcessor;
    
    public OrderService(OrderRepository repository, 
                       PaymentProcessor paymentProcessor) {
        this.repository = repository;
        this.paymentProcessor = paymentProcessor;
    }
}

// DON'T: Depend on concretions
public class OrderService {
    private final MySQLOrderRepository repository = new MySQLOrderRepository();
    private final StripePaymentProcessor paymentProcessor = new StripePaymentProcessor();
}
```

---

## 17. Common Mistakes

### Mistake 1: Over-Segregation (SRP)

```java
// WRONG: Too many small classes
public class UserFirstNameGetter {
    public String get(User user) {
        return user.getFirstName();
    }
}

public class UserLastNameGetter {
    public String get(User user) {
        return user.getLastName();
    }
}

public class UserEmailGetter {
    public String get(User user) {
        return user.getEmail();
    }
}

// RIGHT: Appropriate level of granularity
public class UserDto {
    private final String firstName;
    private final String lastName;
    private final String email;
    
    public UserDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}
```

### Mistake 2: Violating LSP with Exceptions

```java
// WRONG: Throwing unexpected exceptions
public class BasePaymentProcessor {
    public PaymentResult process(Payment payment) {
        // Base implementation
        return PaymentResult.success();
    }
}

public class RestrictedPaymentProcessor extends BasePaymentProcessor {
    @Override
    public PaymentResult process(Payment payment) {
        if (payment.getAmount().compareTo(BigDecimal.TEN) > 0) {
            throw new UnsupportedOperationException("Amount too high");
        }
        return super.process(payment);
    }
}

// RIGHT: Consistent behavior
public interface PaymentProcessor {
    PaymentResult process(Payment payment);
    boolean supports(Payment payment);
}

public class RestrictedPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentResult process(Payment payment) {
        if (!supports(payment)) {
            return PaymentResult.failure("Payment not supported");
        }
        // Process payment
        return PaymentResult.success();
    }
    
    @Override
    public boolean supports(Payment payment) {
        return payment.getAmount().compareTo(BigDecimal.TEN) <= 0;
    }
}
```

### Mistake 3: Interface Pollution (ISP)

```java
// WRONG: Interface with too many methods
public interface UserService {
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
    User findById(long id);
    List<User> findAll();
    void sendEmail(User user, String message);
    void generateReport(User user);
    void backupData();
    void authenticate(String username, String password);
}

// RIGHT: Segregated interfaces
public interface UserCrudService {
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
    User findById(long id);
    List<User> findAll();
}

public interface UserNotificationService {
    void sendEmail(User user, String message);
}

public interface UserReportingService {
    void generateReport(User user);
}
```

### Mistake 4: Concrete Dependencies (DIP)

```java
// WRONG: Direct dependency on concretions
public class OrderService {
    private MySQLDatabase database = new MySQLDatabase();
    private StripeGateway gateway = new StripeGateway();
}

// RIGHT: Depend on abstractions
public class OrderService {
    private final Database database;
    private final PaymentGateway gateway;
    
    public OrderService(Database database, PaymentGateway gateway) {
        this.database = database;
        this.gateway = gateway;
    }
}
```

### Mistake 5: Premature Abstraction

```java
// WRONG: Abstraction without clear need
public interface Greetable {
    void greet();
}

public abstract class AbstractGreeter implements Greetable {
    protected abstract String getGreeting();
    
    @Override
    public void greet() {
        System.out.println(getGreeting());
    }
}

public class SimpleGreeter extends AbstractGreeter {
    @Override
    protected String getGreeting() {
        return "Hello";
    }
}

// RIGHT: Keep it simple
public class SimpleGreeter {
    public void greet() {
        System.out.println("Hello");
    }
}
```

---

## 18. Pitfalls

### Pitfall 1: SOLID as Dogma

```java
// Problem: Applying SOLID everywhere, even where it hurts
public class ValueObject {
    private final int x;
    private final int y;
    
    public ValueObject(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Don't over-engineer simple value objects
}

// Solution: Apply SOLID where it provides value
// - High-level architecture: Always
// - Complex business logic: Usually
// - Simple data structures: Sometimes
- Utility classes: Rarely
```

### Pitfall 2: YAGNI vs SOLID

```java
// Problem: Over-designing for hypothetical future needs
public interface Repository<T> {
    void save(T entity);
    void findById(long id);
    List<T> findAll();
    void update(T entity);
    void delete(long id);
    List<T> findByCriteria(Criteria criteria);
    void bulkSave(List<T> entities);
    void bulkDelete(List<Long> ids);
    // Many methods that may never be used
}

// Solution: Start simple, refactor when needed
public interface UserRepository {
    void save(User user);
    User findById(long id);
}
```

### Pitfall 3: SOLID Without Context

```java
// Problem: Applying SOLID principles inappropriately
public class SimpleScript {
    // In a simple script, SOLID may be overkill
}

// Solution: Consider the context
// - Enterprise application: Apply SOLID rigorously
// - Prototype/MVP: Apply selectively
// - Simple utility: Keep it simple
```

### Pitfall 4: Ignoring Performance

```java
// Problem: Over-abstracting in performance-critical code
public interface DataProcessor {
    ProcessResult process(Data data);
}

public class DataProcessorImpl implements DataProcessor {
    @Override
    public ProcessResult process(Data data) {
        // Virtual method call overhead in hot loop
        // May hurt performance significantly
    }
}

// Solution: Profile and optimize hot paths
public final class FastDataProcessor {
    public ProcessResult process(Data data) {
        // Direct method call, can be inlined by JIT
    }
}
```

---

## 19. Debugging Tips

### Debugging SOLID Violations

```java
// Issue 1: Class doing too much (SRP violation)
public class UserManager {
    public void saveUser(User user) { ... }
    public void sendEmail(User user) { ... }
    public void generateReport() { ... }
    public void backupDatabase() { ... }
}

// Debug: Count responsibilities
// - If class has > 3 distinct responsibilities, split it
// - Check for multiple reasons to change
// - Look for God anti-pattern

// Issue 2: Switch statements (OCP violation)
public class PaymentProcessor {
    public void process(String type, BigDecimal amount) {
        switch (type) {
            case "CREDIT_CARD": ...
            case "PAYPAL": ...
            // Adding new types requires modification
        }
    }
}

// Debug: Look for switch/if-else chains
// - If adding new types requires modifying existing code, violates OCP
// - Use polymorphism instead

// Issue 3: Subclass behavior changes (LSP violation)
public class Rectangle {
    public void setWidth(int width) { ... }
    public void setHeight(int height) { ... }
}

public class Square extends Rectangle {
    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width; // Unexpected behavior!
    }
}

// Debug: Test substitutability
// - Create instance of subclass
// - Use as base type
// - Verify behavior matches expectations
```

### Debugging Techniques

```java
// 1. Check class responsibilities
public class ClassAnalyzer {
    public List<String> getResponsibilities(Class<?> clazz) {
        List<String> responsibilities = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        
        // Analyze method names for different concerns
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("save") || name.startsWith("delete")) {
                responsibilities.add("Data Access");
            } else if (name.startsWith("send") || name.startsWith("notify")) {
                responsibilities.add("Notification");
            } else if (name.startsWith("calculate") || name.startsWith("compute")) {
                responsibilities.add("Business Logic");
            }
        }
        
        return responsibilities;
    }
}

// 2. Test LSP compliance
public class LspTester {
    public <T> boolean isSubstitutable(Class<T> base, Class<? extends T> sub) {
        // Test that subclass can replace base class
        try {
            T baseInstance = base.getDeclaredConstructor().newInstance();
            T subInstance = sub.getDeclaredConstructor().newInstance();
            
            // Verify behavior is consistent
            return true; // Additional tests needed
        } catch (Exception e) {
            return false;
        }
    }
}

// 3. Measure coupling
public class CouplingAnalyzer {
    public int countDependencies(Class<?> clazz) {
        return clazz.getDeclaredFields().length +
               clazz.getDeclaredMethods().length;
    }
}
```

### IDE Debugging Features

1. **Eclipse/IntelliJ**: Analyze → Dependencies
2. **Class Hierarchy**: View → Type Hierarchy
3. **Call Hierarchy**: Right-click → Call Hierarchy
4. **Code Coverage**: Run with coverage to identify dead code
5. **Static Analysis**: SonarQube, PMD, FindBugs

---

## 20. Comparison Table

### SOLID Principles Overview

| Principle | Definition | Violation Example | Fix |
|-----------|------------|-------------------|-----|
| **SRP** | One reason to change | God class | Split into focused classes |
| **OCP** | Open for extension, closed for modification | Switch statements | Use polymorphism |
| **LSP** | Substitutability of subtypes | Square extends Rectangle | Use composition or interfaces |
| **ISP** | Many specific interfaces | Fat interface | Segregate interfaces |
| **DIP** | Depend on abstractions | Direct dependency | Use dependency injection |

### Before/After Comparison

| Aspect | Before SOLID | After SOLID |
|--------|--------------|-------------|
| **Class Size** | Large, monolithic | Small, focused |
| **Coupling** | Tight | Loose |
| **Testability** | Difficult | Easy |
| **Maintainability** | Hard | Easy |
| **Extensibility** | Requires modification | Extension only |
| **Readability** | Low | High |
| **Reusability** | Limited | High |

### Design Pattern Relationships

| Principle | Patterns That Help | Patterns That Require |
|-----------|-------------------|----------------------|
| SRP | Facade, Mediator | Command, Chain of Responsibility |
| OCP | Strategy, Template Method | Factory, Abstract Factory |
| LSP | Factory Method | Observer, Decorator |
| ISP | Adapter, Decorator | Proxy, Composite |
| DIP | Factory, Service Locator | Dependency Injection, MVC |

---

## 21. Decision Tree

### When to Apply Each Principle

```
START: Analyze the design problem
  │
  ├─ Is the class doing too much?
  │   ├─ YES → Apply SRP
  │   │         └─ Split into focused classes
  │   └─ NO → Continue
  │
  ├─ Do you need to add new types?
  │   ├─ YES → Apply OCP
  │   │         └─ Use abstraction and polymorphism
  │   └─ NO → Continue
  │
  ├─ Are subclasses substitutable?
  │   ├─ NO → Apply LSP
  │   │        └─ Ensure behavioral compatibility
  │   └─ YES → Continue
  │
  ├─ Are interfaces too large?
  │   ├─ YES → Apply ISP
  │   │         └─ Segregate into focused interfaces
  │   └─ NO → Continue
  │
  └─ Are you depending on concretions?
      ├─ YES → Apply DIP
      │         └─ Use dependency injection
      └─ NO → Design is SOLID
```

### Quick Decision Matrix

| Problem | Principle | Solution |
|---------|-----------|----------|
| Class has multiple reasons to change | SRP | Split class |
| Adding new types requires modifying existing code | OCP | Use polymorphism |
| Subclass changes parent behavior | LSP | Redesign hierarchy |
| Class implements unused methods | ISP | Segregate interfaces |
| Class depends on concrete implementations | DIP | Use interfaces |

---

## 22. Interview Questions

### Basic Questions

**Q1: What are the SOLID principles?**
**A:** SOLID is an acronym for five OOP design principles:
- **S**ingle Responsibility: One reason to change
- **O**pen/Closed: Open for extension, closed for modification
- **L**iskov Substitution: Subtypes must be substitutable
- **I**nterface Segregation: Many specific interfaces > one general
- **D**ependency Inversion: Depend on abstractions, not concretions

**Q2: Explain the Single Responsibility Principle.**
**A:** A class should have one, and only one, reason to change. This means each class should be responsible for a single piece of functionality. For example, `UserRepository` handles database operations, `EmailService` handles notifications, and `ReportGenerator` handles reporting.

**Q3: What is the Open/Closed Principle?**
**A:** Software entities should be open for extension but closed for modification. You should be able to add new functionality without changing existing code. This is achieved through abstraction and polymorphism.

### Advanced Questions

**Q4: How does Liskov Substitution Principle relate to polymorphism?**
**A:** LSP ensures that polymorphism works correctly. If a subclass changes the expected behavior of parent class methods, polymorphism breaks. The classic example is Square extending Rectangle - Square changes the behavior of setWidth/setHeight, violating LSP.

**Q5: Why is Interface Segregation Principle important?**
**A:** ISP prevents "fat" interfaces that force implementers to carry dead code. If an interface has methods that some implementers don't need, those implementers are forced to provide empty or throwing implementations, which is a code smell.

**Q6: How does Dependency Inversion Principle improve testability?**
**A:** DIP allows substituting real implementations with mocks during testing. If a class depends on an interface, you can create a mock implementation for testing without touching the actual database or external services.

**Q7: What are the trade-offs of applying SOLID principles?**
**A:** Over-applying SOLID can lead to:
- Excessive abstraction
- Increased complexity
- Performance overhead
- More files to manage
- Harder navigation

The key is applying SOLID where it provides value, not everywhere blindly.

### Coding Questions

**Q8: Refactor this code to follow SRP.**
```java
// Violates SRP
public class UserManager {
    public void saveUser(User user) {
        // Save to database
        // Send email
        // Log activity
    }
}
```

**Answer:**
```java
public class UserRepository {
    public void save(User user) {
        // Save to database only
    }
}

public class EmailService {
    public void sendWelcomeEmail(User user) {
        // Email logic only
    }
}

public class ActivityLogger {
    public void logUserCreation(User user) {
        // Logging only
    }
}
```

**Q9: Refactor this code to follow OCP.**
```java
// Violates OCP
public class PaymentProcessor {
    public void process(String type, BigDecimal amount) {
        if (type.equals("CREDIT_CARD")) {
            // Credit card logic
        } else if (type.equals("PAYPAL")) {
            // PayPal logic
        }
    }
}
```

**Answer:**
```java
public interface PaymentMethod {
    PaymentResult process(BigDecimal amount);
}

public class CreditCardPayment implements PaymentMethod {
    @Override
    public PaymentResult process(BigDecimal amount) {
        // Credit card logic
    }
}

public class PayPalPayment implements PaymentMethod {
    @Override
    public PaymentResult process(BigDecimal amount) {
        // PayPal logic
    }
}
```

---

## 23. Exercises

### Exercise 1: Identify SRP Violations (Easy)

Find and fix SRP violations in the following code:

```java
public class OrderProcessor {
    public void processOrder(Order order) {
        validateOrder(order);
        processPayment(order);
        updateInventory(order);
        sendConfirmation(order);
        generateReport(order);
    }
    
    private void validateOrder(Order order) { ... }
    private void processPayment(Order order) { ... }
    private void updateInventory(Order order) { ... }
    private void sendConfirmation(Order order) { ... }
    private void generateReport(Order order) { ... }
}
```

**Solution:**
```java
public class OrderValidator {
    public void validate(Order order) { ... }
}

public class PaymentProcessor {
    public void process(Order order) { ... }
}

public class InventoryUpdater {
    public void update(Order order) { ... }
}

public class ConfirmationSender {
    public void send(Order order) { ... }
}

public class ReportGenerator {
    public void generate(Order order) { ... }
}
```

### Exercise 2: Apply OCP (Medium)

Design a notification system that follows OCP:

```java
// Your implementation here
// Requirements:
// 1. Support EMAIL, SMS, PUSH notifications
// 2. Adding new notification types shouldn't require modifying existing code
// 3. Each notification type has different sending logic
```

### Exercise 3: LSP Compliance (Medium)

Fix the following LSP violation:

```java
public class Stack<T> {
    private List<T> elements = new ArrayList<>();
    
    public void push(T element) {
        elements.add(element);
    }
    
    public T pop() {
        return elements.remove(elements.size() - 1);
    }
    
    public void addAll(Collection<T> collection) {
        elements.addAll(collection);
    }
}

// Subclass violates LSP
public class BoundedStack<T> extends Stack<T> {
    private final int maxSize;
    
    @Override
    public void push(T element) {
        if (size() >= maxSize) {
            throw new StackOverflowException("Stack full");
        }
        super.push(element);
    }
    
    @Override
    public void addAll(Collection<T> collection) {
        if (size() + collection.size() > maxSize) {
            throw new StackOverflowException("Stack full");
        }
        super.addAll(collection);
    }
}
```

**Hint:** Consider using composition or interfaces.

### Exercise 4: Apply ISP (Hard)

Refactor the following interface to follow ISP:

```java
public interface Worker {
    void work();
    void eat();
    void sleep();
    void takeBreak();
    void attendMeeting();
    void writeReport();
}

public class Robot implements Worker {
    @Override
    public void work() { /* Robot works */ }
    
    @Override
    public void eat() { /* Robots don't eat */ }
    
    @Override
    public void sleep() { /* Robots don't sleep */ }
    
    @Override
    public void takeBreak() { /* Robots don't take breaks */ }
    
    @Override
    public void attendMeeting() { /* Robots don't attend meetings */ }
    
    @Override
    public void writeReport() { /* Robots don't write reports */ }
}
```

### Exercise 5: Complete SOLID Refactoring (Advanced)

Refactor the following "God class" to follow all SOLID principles:

```java
public class EcommerceManager {
    private MySQLDatabase database = new MySQLDatabase();
    private StripeGateway gateway = new StripeGateway();
    private SMTPMailService mailService = new SMTPMailService();
    
    public void createUser(User user) {
        // Validation
        // Database save
        // Email notification
        // Logging
    }
    
    public void processOrder(Order order) {
        // Validation
        // Payment processing
        // Inventory update
        // Email confirmation
        // Reporting
    }
    
    public void sendEmail(String to, String subject, String body) {
        // Email logic
    }
    
    public void generateReport(ReportType type) {
        // Report generation
    }
}
```

---

## 24. Assignments

### Assignment 1: Design a Library System (Beginner)

**Objective:** Design a library management system following SOLID principles.

**Requirements:**
1. **SRP**: Separate classes for books, members, loans, notifications
2. **OCP**: Support different item types (books, DVDs, magazines) without modification
3. **LSP**: All item types must be substitutable
4. **ISP**: Separate interfaces for checkout, return, search
5. **DIP**: Depend on abstractions for persistence and notifications

**Deliverables:**
- UML class diagram
- Java implementation
- Unit tests
- Documentation

### Assignment 2: E-commerce System (Intermediate)

**Objective:** Design an e-commerce system following SOLID principles.

**Requirements:**
1. **SRP**: Separate classes for products, orders, payments, inventory, notifications
2. **OCP**: Support multiple payment methods (credit card, PayPal, crypto)
3. **LSP**: All payment methods must be substitutable
4. **ISP**: Separate interfaces for different operations
5. **DIP**: Use dependency injection throughout

**Deliverables:**
- Complete source code
- Architecture documentation
- Test coverage report

### Assignment 3: Plugin Architecture (Advanced)

**Objective:** Design a plugin system following SOLID principles.

**Requirements:**
1. **SRP**: Each plugin has single responsibility
2. **OCP**: New plugins added without modifying core
3. **LSP**: All plugins are substitutable
4. **ISP**: Small, focused plugin interfaces
5. **DIP**: Core depends on plugin abstractions

**Deliverables:**
- Plugin API design
- Core framework implementation
- Example plugins
- Plugin loader and manager
- Documentation

---

## 25. Mini Project

### Project: Task Management System

**Objective:** Build a task management system demonstrating all SOLID principles.

**Architecture:**

```
┌─────────────────────────────────────────────────────────────┐
│                    TaskManager (DIP)                         │
│  Depends on: TaskRepository, NotificationService,           │
│              ReportGenerator (all abstractions)              │
├─────────────────────────────────────────────────────────────┤
│  + createTask(Task task): TaskResult                        │
│  + assignTask(String taskId, String userId): TaskResult     │
│  + completeTask(String taskId): TaskResult                  │
│  + getTasksByUser(String userId): List<Task>                │
└─────────────────────────────────────────────────────────────┘
         │                    │                    │
         ▼                    ▼                    ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│   TaskRepository │  │ NotificationService│  │  ReportGenerator │
│   (Interface)    │  │   (Interface)    │  │   (Interface)    │
└─────────────────┘  └─────────────────┘  └─────────────────┘
         │                    │                    │
         ▼                    ▼                    ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│   MySQLTaskRepo  │  │  EmailNotifier   │  │  PDFReportGen   │
│   MongoTaskRepo  │  │  SMSNotifier     │  │  ExcelReportGen │
└─────────────────┘  └─────────────────┘  └─────────────────┘
```

**Core Abstractions:**

```java
// SRP: Each interface has single responsibility
public interface TaskRepository {
    void save(Task task);
    Optional<Task> findById(String id);
    List<Task> findByUserId(String userId);
    void update(Task task);
    void delete(String id);
}

public interface NotificationService {
    void notifyTaskAssigned(Task task, User assignee);
    void notifyTaskCompleted(Task task, User creator);
    void notifyTaskOverdue(Task task, User assignee);
}

public interface ReportGenerator {
    Report generateUserTasksReport(String userId, DateRange range);
    Report generateTeamReport(String teamId, DateRange range);
}

// OCP: New task types added without modification
public interface Task {
    String getId();
    String getTitle();
    TaskType getType();
    TaskStatus getStatus();
    void assign(User user);
    void complete();
}

public class BugTask implements Task {
    // Bug-specific behavior
}

public class FeatureTask implements Task {
    // Feature-specific behavior
}

public class ImprovementTask implements Task {
    // Improvement-specific behavior
}

// LSP: All task types are substitutable
public class TaskManager {
    public void processTask(Task task) {
        // All task types work here
        task.assign(user);
        task.complete();
    }
}

// ISP: Segregated interfaces
public interface Assignable {
    void assign(User user);
}

public interface Completable {
    void complete();
}

public interface Cancellable {
    void cancel();
}

// DIP: Depend on abstractions
public class TaskManager {
    private final TaskRepository repository;
    private final NotificationService notificationService;
    private final ReportGenerator reportGenerator;
    
    public TaskManager(TaskRepository repository,
                      NotificationService notificationService,
                      ReportGenerator reportGenerator) {
        this.repository = repository;
        this.notificationService = notificationService;
        this.reportGenerator = reportGenerator;
    }
}
```

**Features to Implement:**
1. Task CRUD operations
2. Task assignment and notifications
3. Task status tracking
4. User and team management
5. Reporting and analytics
6. Dashboard UI
7. REST API
8. Unit and integration tests

**Deliverables:**
- Complete source code with SOLID compliance
- Architecture documentation
- API documentation
- Test suite with >80% coverage
- Deployment guide

---

## 26. Summary

### Key Takeaways

1. **SRP**: Keep classes focused on single responsibility
2. **OCP**: Use abstraction for extension, not modification
3. **LSP**: Ensure subclasses honor parent contracts
4. **ISP**: Prefer specific interfaces over general ones
5. **DIP**: Depend on abstractions, not concretions

### SOLID Benefits Recap

| Benefit | Description |
|---------|-------------|
| **Maintainability** | Easy to modify and extend |
| **Testability** | Easy to unit test |
| **Flexibility** | Resilient to change |
| **Scalability** | Can grow without major refactoring |
| **Reusability** | Components can be used in multiple contexts |
| **Readability** | Small, focused classes are easier to understand |

### When to Apply SOLID

| Context | Approach |
|---------|----------|
| Enterprise application | Apply rigorously |
| Prototype/MVP | Apply selectively |
| Simple utility | Keep it simple |
| Performance-critical code | Profile and optimize |

### Common Anti-Patterns

| Anti-Pattern | Principle Violated | Fix |
|--------------|-------------------|-----|
| God Class | SRP | Split into focused classes |
| Switch Statements | OCP | Use polymorphism |
| Fragile Base Class | LSP | Use composition or interfaces |
| Fat Interface | ISP | Segregate interfaces |
| Concrete Dependency | DIP | Use dependency injection |

---

## 27. References

### Official Documentation

- [SOLID Principles (Wikipedia)](https://en.wikipedia.org/wiki/SOLID)
- [Object Mentor: SOLID Principles](https://web.archive.org/web/20150905124425/http://www.objectmentor.com/resources/articles/principles_and_patterns.pdf)

### Books

- *Clean Code* by Robert C. Martin - Chapters 11-17
- *Agile Software Development: Principles, Patterns, and Practices* by Robert C. Martin
- *The Single Responsibility Principle* by Robert C. Martin
- *Head First Design Patterns* by Eric Freeman - SOLID principles
- *Refactoring to Patterns* by Joshua Kerievsky

### Online Resources

- [Baeldung: SOLID Principles](https://www.baeldung.com/solid-principles)
- [DigitalOcean: SOLID Principles](https://www.digitalocean.com/community/conceptual-articles/s-o-l-i-d-the-first-five-principles-of-object-oriented-design)
- [Refactoring.Guru: SOLID](https://refactoring.guru/design-patterns/solid)
- [SourceMaking: SOLID](https://sourcemaking.com/design_principles/solid)

### Video Tutorials

- [Uncle Bob: SOLID Principles](https://www.youtube.com/watch?v=QMN1cTGDAMk)
- [Derek Banas: SOLID Principles](https://www.youtube.com/watch?v=HZoVAW9g9TI)
- [Traversy Media: SOLID Principles](https://www.youtube.com/watch?v=K-8qoYsMPhg)

### Related Topics

- [Design Patterns](./design-patterns.md)
- [Refactoring](./refactoring.md)
- [Test-Driven Development](./tdd.md)
- [Clean Architecture](./clean-architecture.md)

---

*Last updated: August 2026*
*Java version: 21*
