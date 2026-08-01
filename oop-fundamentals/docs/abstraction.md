# Abstraction in Object-Oriented Programming

## 1. Introduction

Abstraction is one of the four fundamental pillars of Object-Oriented Programming (OOP), alongside encapsulation, inheritance, and polymorphism. It is the process of hiding complex implementation details while exposing only the essential features and behaviors of an object to the outside world.

In Java, abstraction is achieved through two primary mechanisms:
- **Abstract Classes**: Partial implementation with abstract method declarations
- **Interfaces**: Pure contract definition (with default methods in Java 8+)

Abstraction enables developers to focus on *what* an object does rather than *how* it does it, leading to cleaner, more maintainable, and extensible code.

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Define abstraction and explain its role in OOP
- Distinguish between abstract classes and interfaces
- Implement abstract classes with abstract and concrete methods
- Design interfaces using Java 8+ features (default, static, private methods)
- Apply the Template Method design pattern
- Make informed decisions about when to use abstract classes vs interfaces
- Recognize common abstraction pitfalls and avoid them
- Apply abstraction principles in enterprise-grade applications

---

## 3. Prerequisites

Before studying abstraction, you should be familiar with:

- **Classes and Objects**: Creating and using Java classes
- **Inheritance**: Single and multilevel inheritance concepts
- **Polymorphism**: Method overriding and dynamic dispatch
- **Access Modifiers**: `public`, `protected`, `private`, and package-private
- **Basic OOP Design**: Understanding of encapsulation principles

---

## 4. Why This Concept Exists

Consider a payment processing system that supports credit cards, PayPal, and cryptocurrency. Each payment method has a different implementation, but the caller doesn't need to know the details:

```java
// Without abstraction - caller must know implementation details
if (paymentType.equals("CREDIT_CARD")) {
    // 50 lines of credit card processing
} else if (paymentType.equals("PAYPAL")) {
    // 40 lines of PayPal processing
} else if (paymentType.equals("CRYPTO")) {
    // 60 lines of crypto processing
}
```

With abstraction:

```java
// With abstraction - caller only needs to know the contract
PaymentProcessor processor = PaymentFactory.create(type);
processor.processPayment(amount); // Implementation hidden
```

**Abstraction exists to:**
1. Reduce complexity by hiding implementation details
2. Prevent tight coupling between components
3. Enable polymorphic behavior
4. Facilitate code reuse through shared abstractions
5. Enforce contracts that implementations must follow

---

## 5. Problem Statement

### The Problem

In real-world applications, multiple classes often share similar behaviors but implement them differently. Without abstraction:

1. **Code Duplication**: Common logic is repeated across similar classes
2. **Tight Coupling**: Callers depend on concrete implementations
3. **Rigid Architecture**: Adding new variants requires modifying existing code
4. **Inconsistent Interfaces**: No enforcement of method signatures

### Example Problem

Consider a graphics rendering system without abstraction:

```java
class Circle {
    public void draw() { /* circle drawing logic */ }
    public double calculateArea() { /* πr² */ }
}

class Rectangle {
    public void draw() { /* rectangle drawing logic */ }
    public double calculateArea() { /* width × height */ }
}

class Triangle {
    public void draw() { /* triangle drawing logic */ }
    public double calculateArea() { /* ½ × base × height */ }
}
```

**Issues:**
- No common interface to iterate over shapes uniformly
- Adding a new shape requires modifying all code that processes shapes
- No enforcement that all shapes implement required methods

---

## 6. Theory

### Abstraction Concepts

**Abstract Class:**
- Cannot be instantiated directly
- Can contain both abstract (unimplemented) and concrete (implemented) methods
- Can have instance variables with state
- Supports constructors
- Supports single inheritance only

**Interface:**
- Defines a contract that implementing classes must fulfill
- Java 8+: Supports default and static methods
- Java 9+: Supports private methods
- Supports multiple inheritance of type
- Cannot have instance variables (only `public static final` constants)

### Design Principles Behind Abstraction

1. **Liskov Substitution Principle (LSP)**: Subtypes must be substitutable for their base types
2. **Dependency Inversion Principle (DIP)**: High-level modules should depend on abstractions
3. **Interface Segregation Principle (ISP)**: Prefer specific interfaces over general-purpose ones

### Abstraction vs Encapsulation

| Aspect | Abstraction | Encapsulation |
|--------|-------------|---------------|
| **Focus** | Hiding complexity | Hiding data |
| **Achieved via** | Abstract classes, interfaces | Access modifiers, getters/setters |
| **Purpose** | Define *what* to do | Control *how* to access |
| **Level** | Design level | Implementation level |

---

## 7. Internal Working

### How Abstract Classes Work

When a class extends an abstract class, the JVM:

1. **Loads the abstract class**: All static members are initialized
2. **Allocates memory**: For both abstract and subclass fields
3. **Enforces implementation**: The subclass MUST implement all abstract methods
4. **Enables polymorphism**: The abstract type reference can point to any subclass

```java
abstract class Animal {
    abstract void makeSound();
    
    void sleep() {
        System.out.println("Sleeping...");
    }
}

class Dog extends Animal {
    @Override
    void makeSound() {
        System.out.println("Woof!");
    }
}

// Runtime behavior:
Animal animal = new Dog();     // Upcast - compiles as Animal
animal.makeSound();            // Outputs: Woof! (dynamic dispatch)
animal.sleep();                // Outputs: Sleeping... (inherited)
```

### How Interfaces Work

When a class implements an interface:

1. **Type Checking**: The class must provide implementations for all abstract methods
2. **Covariant Return Types**: Implementing methods can return subtypes
3. **Default Methods**: Optional to override; provide fallback behavior
4. **Static Methods**: Called on the interface, not on implementing classes

```java
interface Drawable {
    void draw();                      // Abstract - must implement
    
    default void clear() {            // Default - optional override
        System.out.println("Cleared");
    }
    
    static Drawable createDefault() { // Static - called on interface
        return new Drawable() {
            @Override
            public void draw() {
                System.out.println("Default drawing");
            }
        };
    }
}
```

---

## 8. JVM Perspective

### Abstract Class Execution

```
┌─────────────────────────────────────────┐
│           JVM Class Loader              │
│  Loads Animal.class and Dog.class       │
└─────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│           Memory Allocation             │
│  Dog object created on heap             │
│  Includes: Animal fields + Dog fields   │
│  + Animal method table + Dog overrides  │
└─────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│         Dynamic Method Dispatch         │
│  animal.makeSound() → Dog.makeSound()  │
│  animal.sleep() → Animal.sleep()       │
└─────────────────────────────────────────┘
```

### Interface Execution

```
┌─────────────────────────────────────────┐
│         Interface Method Resolution     │
│  1. Check implementing class methods    │
│  2. Check default methods in interface  │
│  3. Check Object methods                │
└─────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│         TableSwitch Instruction         │
│  itable[0] → Drawable.draw()           │
│  itable[1] → Drawable.clear()          │
└─────────────────────────────────────────┘
```

### Bytecode Representation

```java
// Abstract class bytecode (partial)
public abstract class Animal {
    // ACC_ABSTRACT flag set
    public abstract void makeSound();
}

// Interface bytecode (partial)
public interface Drawable {
    // ACC_INTERFACE flag set
    public abstract void draw();
}
```

---

## 9. Memory Representation

### Abstract Class Object Memory Layout

```
┌─────────────────────────────────────────┐
│         Dog Object (Heap)               │
├─────────────────────────────────────────┤
│  Header (12-16 bytes)                   │
│  ├─ Mark Word (8 bytes)                 │
│  └─ Klass Pointer (4 bytes)             │
├─────────────────────────────────────────┤
│  Animal Fields                          │
│  ├─ color (String reference)            │
│  └─ weight (double, 8 bytes)            │
├─────────────────────────────────────────┤
│  Dog-Specific Fields                    │
│  └─ breed (String reference)            │
├─────────────────────────────────────────┤
│  Method Pointers (vtable/itable)        │
│  ├─ makeSound() → Dog.makeSound()      │
│  └─ sleep() → Animal.sleep()           │
└─────────────────────────────────────────┘
```

### Interface Method Table (itable)

```
┌─────────────────────────────────────────┐
│         Interface Method Table          │
├─────────────────────────────────────────┤
│  Index 0: Drawable.draw()              │
│  Index 1: Serializable.serialize()     │
│  Index 2: Comparable.compareTo()       │
└─────────────────────────────────────────┘

When calling via interface:
  Drawable d = new MyClass();
  d.draw();  // Looks up itable[0]
```

---

## 10. Syntax

### Abstract Class Syntax

```java
// Abstract class declaration
public abstract class ClassName {
    // Instance variables (state)
    private String field;
    protected int count;
    
    // Constructor (abstract classes can have constructors)
    public ClassName(String field) {
        this.field = field;
    }
    
    // Abstract method (no body - must be implemented)
    public abstract ReturnType methodName(Params params);
    
    // Concrete method (has implementation)
    public void concreteMethod() {
        System.out.println("Implementation provided");
    }
    
    // Static method
    public static void staticMethod() {
        System.out.println("Cannot be overridden");
    }
}

// Subclass must implement all abstract methods
public class SubClass extends ClassName {
    public SubClass(String field) {
        super(field);
    }
    
    @Override
    public ReturnType methodName(Params params) {
        // Must implement
        return result;
    }
}
```

### Interface Syntax

```java
// Interface declaration
public interface InterfaceName {
    // Constants (implicitly public static final)
    int MAX_VALUE = 100;
    
    // Abstract method (implicitly public abstract)
    void abstractMethod(Params params);
    
    // Default method (Java 8+)
    default void defaultMethod(Params params) {
        // Optional implementation
    }
    
    // Static method (Java 8+)
    static void staticMethod(Params params) {
        // Utility method
    }
    
    // Private method (Java 9+)
    private void privateMethod() {
        // Helper for default methods
    }
    
    // Private static method (Java 9+)
    private static void privateStaticMethod() {
        // Shared private utility
    }
}

// Implementing class
public class Implementation implements InterfaceName {
    @Override
    public void abstractMethod(Params params) {
        // Must implement
    }
    
    // Optional: override default method
    @Override
    public void defaultMethod(Params params) {
        InterfaceName.super.defaultMethod(params); // Call default
    }
}
```

### Multiple Interface Implementation

```java
public interface Readable {
    String read();
}

public interface Writable {
    void write(String data);
}

// Class can implement multiple interfaces
public class FileHandler implements Readable, Writable {
    @Override
    public String read() {
        return "File content";
    }
    
    @Override
    public void write(String data) {
        System.out.println("Writing: " + data);
    }
}
```

---

## 11. Easy Example

### Basic Abstract Class

```java
public abstract class Vehicle {
    private String name;
    
    public Vehicle(String name) {
        this.name = name;
    }
    
    // Abstract method - must be implemented
    public abstract void start();
    
    // Concrete method - shared implementation
    public void stop() {
        System.out.println(name + " stopped.");
    }
    
    public String getName() {
        return name;
    }
}

public class Car extends Vehicle {
    public Car(String name) {
        super(name);
    }
    
    @Override
    public void start() {
        System.out.println(getName() + " engine starts with key.");
    }
}

public class ElectricCar extends Vehicle {
    public ElectricCar(String name) {
        super(name);
    }
    
    @Override
    public void start() {
        System.out.println(getName() + " powers on silently.");
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        Vehicle car = new Car("Tesla");
        Vehicle electric = new ElectricCar("Nissan Leaf");
        
        car.start();     // Tesla engine starts with key.
        electric.start(); // Nissan Leaf powers on silently.
        
        car.stop();      // Tesla stopped.
        electric.stop(); // Nissan Leaf stopped.
    }
}
```

---

## 12. Medium Example

### Payment Processing System

```java
// Abstract class for shared payment logic
public abstract class PaymentProcessor {
    protected String transactionId;
    protected BigDecimal amount;
    
    public PaymentProcessor(BigDecimal amount) {
        this.amount = Objects.requireNonNull(amount);
        this.transactionId = UUID.randomUUID().toString();
    }
    
    // Template method - defines the algorithm
    public final PaymentResult process() {
        validate();
        PaymentResult result = executePayment();
        sendNotification(result);
        return result;
    }
    
    // Abstract methods - subclasses implement differently
    protected abstract void validate();
    protected abstract PaymentResult executePayment();
    
    // Concrete method - shared behavior
    protected void sendNotification(PaymentResult result) {
        System.out.println("Notification: Payment " + result.getStatus());
    }
    
    public String getTransactionId() {
        return transactionId;
    }
}

// Interface for fee calculation
public interface FeeCalculable {
    BigDecimal calculateFee(BigDecimal amount);
    
    default BigDecimal applyDiscount(BigDecimal amount, double percent) {
        return amount.multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(percent / 100)));
    }
}

// Concrete implementations
public class CreditCardProcessor extends PaymentProcessor implements FeeCalculable {
    private String cardNumber;
    
    public CreditCardProcessor(BigDecimal amount, String cardNumber) {
        super(amount);
        this.cardNumber = cardNumber;
    }
    
    @Override
    protected void validate() {
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new InvalidPaymentException("Invalid card number");
        }
        System.out.println("Card validated: " + maskCardNumber());
    }
    
    @Override
    protected PaymentResult executePayment() {
        BigDecimal fee = calculateFee(amount);
        BigDecimal total = amount.add(fee);
        System.out.println("Processing credit card: $" + total);
        return new PaymentResult(transactionId, "SUCCESS", total);
    }
    
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.029)); // 2.9% fee
    }
    
    private String maskCardNumber() {
        return "****-****-****-" + cardNumber.substring(12);
    }
}

public class PayPalProcessor extends PaymentProcessor implements FeeCalculable {
    private String email;
    
    public PayPalProcessor(BigDecimal amount, String email) {
        super(amount);
        this.email = email;
    }
    
    @Override
    protected void validate() {
        if (email == null || !email.contains("@")) {
            throw new InvalidPaymentException("Invalid email");
        }
        System.out.println("PayPal account verified: " + email);
    }
    
    @Override
    protected PaymentResult executePayment() {
        BigDecimal fee = calculateFee(amount);
        BigDecimal total = amount.add(fee);
        System.out.println("Processing PayPal: $" + total);
        return new PaymentResult(transactionId, "SUCCESS", total);
    }
    
    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.035)); // 3.5% fee
    }
}

// Usage
public class PaymentService {
    public PaymentResult processPayment(PaymentProcessor processor) {
        return processor.process();
    }
}

// Client code
PaymentProcessor creditCard = new CreditCardProcessor(
    new BigDecimal("100.00"), "4111111111111111"
);
PaymentProcessor paypal = new PayPalProcessor(
    new BigDecimal("100.00"), "user@example.com"
);

PaymentService service = new PaymentService();
service.processPayment(creditCard);
service.processPayment(paypal);
```

---

## 13. Hard Example

### Plugin System with Multiple Abstractions

```java
// Core abstraction layer
public interface Plugin {
    String getName();
    String getVersion();
    void initialize(PluginContext context);
    void shutdown();
}

public interface PluginContext {
    Logger getLogger();
    Configuration getConfig();
    <T> T getService(Class<T> serviceClass);
}

// Plugin lifecycle management
public abstract class AbstractPlugin implements Plugin {
    protected final String name;
    protected final String version;
    protected PluginContext context;
    protected volatile boolean initialized = false;
    
    protected AbstractPlugin(String name, String version) {
        this.name = Objects.requireNonNull(name);
        this.version = Objects.requireNonNull(version);
    }
    
    @Override
    public final void initialize(PluginContext context) {
        if (initialized) {
            throw new IllegalStateException("Plugin already initialized");
        }
        this.context = Objects.requireNonNull(context);
        onInitialize();
        initialized = true;
        context.getLogger().info(name + " initialized");
    }
    
    @Override
    public final void shutdown() {
        if (!initialized) {
            return;
        }
        onShutdown();
        initialized = false;
        context.getLogger().info(name + " shut down");
    }
    
    // Template methods for subclasses
    protected abstract void onInitialize();
    protected abstract void onShutdown();
    
    // Final hook for validation
    protected final void validateState() {
        if (!initialized) {
            throw new IllegalStateException("Plugin not initialized");
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getVersion() {
        return version;
    }
}

// Data processing abstraction
public interface DataProcessor<T, R> {
    R process(T input);
    boolean supports(Class<?> inputType);
    
    default Optional<R> processSafely(T input) {
        try {
            return Optional.of(process(input));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

// Concrete plugin implementation
public class AnalyticsPlugin extends AbstractPlugin {
    private final Map<String, DataProcessor<?, ?>> processors = new ConcurrentHashMap<>();
    
    public AnalyticsPlugin() {
        super("Analytics", "2.1.0");
    }
    
    @Override
    protected void onInitialize() {
        // Register processors
        processors.put("user", new UserDataProcessor());
        processors.put("order", new OrderDataProcessor());
    }
    
    @Override
    protected void onShutdown() {
        processors.clear();
    }
    
    public <T, R> R processData(String type, T input) {
        validateState();
        DataProcessor<T, R> processor = (DataProcessor<T, R>) processors.get(type);
        if (processor == null) {
            throw new IllegalArgumentException("Unknown data type: " + type);
        }
        return processor.process(input);
    }
    
    private static class UserDataProcessor implements DataProcessor<UserData, AnalyticsResult> {
        @Override
        public AnalyticsResult process(UserData input) {
            // Complex analytics processing
            return new AnalyticsResult(input.getUserId(), calculateMetrics(input));
        }
        
        @Override
        public boolean supports(Class<?> inputType) {
            return UserData.class.isAssignableFrom(inputType);
        }
        
        private Map<String, Double> calculateMetrics(UserData data) {
            return Map.of(
                "sessionDuration", data.getSessionDuration(),
                "pageViews", (double) data.getPageViews()
            );
        }
    }
    
    private static class OrderDataProcessor implements DataProcessor<OrderData, AnalyticsResult> {
        @Override
        public AnalyticsResult process(OrderData input) {
            return new AnalyticsResult(input.getOrderId(), calculateOrderMetrics(input));
        }
        
        @Override
        public boolean supports(Class<?> inputType) {
            return OrderData.class.isAssignableFrom(inputType);
        }
        
        private Map<String, Double> calculateOrderMetrics(OrderData data) {
            return Map.of(
                "totalAmount", data.getTotalAmount().doubleValue(),
                "itemCount", (double) data.getItems().size()
            );
        }
    }
}

// Plugin manager with dependency injection
public class PluginManager {
    private final Map<String, Plugin> plugins = new LinkedHashMap<>();
    private final PluginContext context;
    
    public PluginManager(PluginContext context) {
        this.context = context;
    }
    
    public void loadPlugin(Plugin plugin) {
        plugin.initialize(context);
        plugins.put(plugin.getName(), plugin);
    }
    
    public void unloadPlugin(String name) {
        Plugin plugin = plugins.remove(name);
        if (plugin != null) {
            plugin.shutdown();
        }
    }
    
    public Optional<Plugin> getPlugin(String name) {
        return Optional.ofNullable(plugins.get(name));
    }
    
    public List<Plugin> getActivePlugins() {
        return List.copyOf(plugins.values());
    }
    
    public void shutdownAll() {
        plugins.values().forEach(Plugin::shutdown);
        plugins.clear();
    }
}

// Usage
PluginContext context = new DefaultPluginContext(logger, config, serviceRegistry);
PluginManager manager = new PluginManager(context);

AnalyticsPlugin analytics = new AnalyticsPlugin();
manager.loadPlugin(analytics);

AnalyticsResult result = analytics.processData("user", userData);
```

---

## 14. Enterprise Example

### Banking System with Strategy Pattern

```java
// Core abstraction - Account types
public abstract class BankAccount {
    protected final String accountId;
    protected final String ownerName;
    protected BigDecimal balance;
    protected final List<Transaction> transactions = new ArrayList<>();
    protected final InterestStrategy interestStrategy;
    
    protected BankAccount(String accountId, String ownerName, 
                          BigDecimal initialBalance, InterestStrategy interestStrategy) {
        this.accountId = Objects.requireNonNull(accountId);
        this.ownerName = Objects.requireNonNull(ownerName);
        this.balance = Objects.requireNonNull(initialBalance);
        this.interestStrategy = Objects.requireNonNull(interestStrategy);
    }
    
    // Template method
    public final TransactionResult deposit(BigDecimal amount) {
        validateAmount(amount);
        balance = balance.add(amount);
        Transaction tx = new Transaction(TransactionType.DEPOSIT, amount, LocalDateTime.now());
        transactions.add(tx);
        onDepositHook(amount);
        return new TransactionResult(true, balance, tx.getId());
    }
    
    public final TransactionResult withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (!hasSufficientFunds(amount)) {
            return new TransactionResult(false, balance, "Insufficient funds");
        }
        balance = balance.subtract(amount);
        Transaction tx = new Transaction(TransactionType.WITHDRAWAL, amount, LocalDateTime.now());
        transactions.add(tx);
        onWithdrawalHook(amount);
        return new TransactionResult(true, balance, tx.getId());
    }
    
    public BigDecimal calculateInterest() {
        return interestStrategy.calculate(balance, getAnnualRate());
    }
    
    // Abstract methods - each account type implements differently
    protected abstract BigDecimal getAnnualRate();
    protected abstract void validateAmount(BigDecimal amount);
    protected abstract boolean hasSufficientFunds(BigDecimal amount);
    
    // Hooks for subclass customization
    protected void onDepositHook(BigDecimal amount) {}
    protected void onWithdrawalHook(BigDecimal amount) {}
    
    // Getters
    public String getAccountId() { return accountId; }
    public String getOwnerName() { return ownerName; }
    public BigDecimal getBalance() { return balance; }
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
}

// Interest calculation strategy
public interface InterestStrategy {
    BigDecimal calculate(BigDecimal balance, annualRate);
}

public class SimpleInterest implements InterestStrategy {
    @Override
    public BigDecimal calculate(BigDecimal balance, annualRate) {
        return balance.multiply(BigDecimal.valueOf(annualRate));
    }
}

public class CompoundInterest implements InterestStrategy {
    private final int compoundsPerYear;
    
    public CompoundInterest(int compoundsPerYear) {
        this.compoundsPerYear = compoundsPerYear;
    }
    
    @Override
    public BigDecimal calculate(BigDecimal balance, annualRate) {
        BigDecimal rate = BigDecimal.valueOf(annualRate / compoundsPerYear);
        return balance.multiply(rate);
    }
}

// Concrete account types
public class SavingsAccount extends BankAccount {
    private static final BigDecimal MIN_BALANCE = new BigDecimal("100.00");
    private static final BigDecimal ANNUAL_RATE = new BigDecimal("0.045"); // 4.5%
    
    public SavingsAccount(String accountId, String ownerName, BigDecimal initialBalance) {
        super(accountId, ownerName, initialBalance, new CompoundInterest(12));
    }
    
    @Override
    protected BigDecimal getAnnualRate() {
        return ANNUAL_RATE;
    }
    
    @Override
    protected void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
    
    @Override
    protected boolean hasSufficientFunds(BigDecimal amount) {
        return balance.subtract(amount).compareTo(MIN_BALANCE) >= 0;
    }
    
    @Override
    protected void onWithdrawalHook(BigDecimal amount) {
        if (balance.compareTo(MIN_BALANCE) < 0) {
            System.out.println("Warning: Balance below minimum");
        }
    }
}

public class CheckingAccount extends BankAccount {
    private BigDecimal overdraftLimit;
    
    public CheckingAccount(String accountId, String ownerName, 
                          BigDecimal initialBalance, BigDecimal overdraftLimit) {
        super(accountId, ownerName, initialBalance, new SimpleInterest());
        this.overdraftLimit = overdraftLimit;
    }
    
    @Override
    protected BigDecimal getAnnualRate() {
        return new BigDecimal("0.01"); // 1%
    }
    
    @Override
    protected void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount.compareTo(new BigDecimal("10000")) > 0) {
            throw new IllegalArgumentException("Daily limit exceeded");
        }
    }
    
    @Override
    protected boolean hasSufficientFunds(BigDecimal amount) {
        return balance.add(overdraftLimit).compareTo(amount) >= 0;
    }
}

// Service layer using abstractions
public class BankingService {
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final AuditLogger auditLogger;
    
    public BankingService(AccountRepository accountRepository,
                         NotificationService notificationService,
                         AuditLogger auditLogger) {
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
        this.auditLogger = auditLogger;
    }
    
    public TransactionResult transfer(String fromAccountId, String toAccountId, 
                                     BigDecimal amount) {
        BankAccount from = accountRepository.findById(fromAccountId)
            .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        BankAccount to = accountRepository.findById(toAccountId)
            .orElseThrow(() -> new AccountNotFoundException(toAccountId));
        
        TransactionResult withdrawResult = from.withdraw(amount);
        if (!withdrawResult.isSuccess()) {
            return withdrawResult;
        }
        
        TransactionResult depositResult = to.deposit(amount);
        if (!depositResult.isSuccess()) {
            // Rollback
            from.deposit(amount);
            return depositResult;
        }
        
        accountRepository.save(from);
        accountRepository.save(to);
        
        notificationService.notifyTransfer(fromAccountId, toAccountId, amount);
        auditLogger.logTransfer(fromAccountId, toAccountId, amount);
        
        return depositResult;
    }
    
    public BigDecimal calculateMonthlyInterest(String accountId) {
        BankAccount account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));
        return account.calculateInterest();
    }
}

// Usage
BankingService bankingService = new BankingService(
    accountRepository, notificationService, auditLogger
);

TransactionResult result = bankingService.transfer("ACC001", "ACC002", new BigDecimal("500.00"));
```

---

## 15. Performance

### Abstract Class vs Interface Performance

| Aspect | Abstract Class | Interface |
|--------|----------------|-----------|
| Method Dispatch | vtable (direct) | itable (indirect) |
| Memory Overhead | Lower | Slightly higher |
| JIT Optimization | Better inlining | Less optimization |
| First Call | Faster | Slower (resolution) |
| Subsequent Calls | Same | Same (cached) |

### Benchmarking Results

```java
// Typical performance comparison (JMH benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class AbstractionBenchmark {
    
    @Benchmark
    public void abstractClassMethodCall(Blackhole bh) {
        Shape circle = new Circle(5.0);
        for (int i = 0; i < 1000; i++) {
            bh.consume(circle.area());
        }
    }
    
    @Benchmark
    public void interfaceMethodCall(Blackhole bh) {
        Drawable drawable = new DrawableCircle(5.0);
        for (int i = 0; i < 1000; i++) {
            bh.consume(drawable.draw());
        }
    }
}
```

**Results (typical):**
- Abstract class method call: ~15-25 ns
- Interface method call: ~20-35 ns
- Direct method call: ~5-10 ns

### Optimization Strategies

1. **Use abstract classes for performance-critical paths**: Slightly faster dispatch
2. **Prefer interfaces for API design**: Better flexibility despite minor overhead
3. **Enable JIT inlining**: Keep class hierarchies shallow
4. **Profile before optimizing**: Differences are often negligible

---

## 16. Best Practices

### Abstract Classes

```java
// DO: Use abstract classes when you need shared state
public abstract class AbstractRepository<T> {
    private final DataSource dataSource;
    private final Class<T> entityClass;
    
    protected AbstractRepository(DataSource dataSource, Class<T> entityClass) {
        this.dataSource = dataSource;
        this.entityClass = entityClass;
    }
    
    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public abstract T findById(long id);
    public abstract List<T> findAll();
    public abstract T save(T entity);
}

// DON'T: Use abstract classes just for code sharing
public abstract class Logger {
    // If there's no shared state, prefer interface
}
```

### Interfaces

```java
// DO: Keep interfaces focused (Interface Segregation)
public interface Readable {
    String read();
}

public interface Writable {
    void write(String data);
}

// DO: Use default methods for optional behavior
public interface Cacheable {
    Object get(Object key);
    void put(Object key, Object value);
    
    default void refresh(Object key) {
        // Optional refresh behavior
    }
}

// DON'T: Put implementation in interfaces
public interface BadPractice {
    default void doSomething() {
        // Complex implementation in interface - avoid
        List<String> items = fetchData(); // Bad
        processItems(items); // Bad
    }
}
```

### Naming Conventions

```java
// Prefix abstract classes with "Abstract" or "Base"
public abstract class AbstractValidator<T> {}
public abstract class BaseRepository<T> {}

// Use adjectives for interfaces
public interface Serializable {}
public interface Cloneable {}
public interface AutoCloseable {}

// Use nouns for interfaces representing capabilities
public interface Readable {}
public interface Appendable {}
public interface Iterable<T> {}
```

---

## 17. Common Mistakes

### Mistake 1: Abstract Class with No Abstract Methods

```java
// WRONG: If no abstract methods, consider if abstract is needed
public abstract class PointlessAbstract {
    public void doSomething() {
        System.out.println("No reason to be abstract");
    }
}

// RIGHT: Either add abstract methods or make it concrete
public abstract class UsefulAbstract {
    public abstract void validate();
    
    public void process() {
        validate();
        // Common logic
    }
}
```

### Mistake 2: Violating Liskov Substitution

```java
// WRONG: Subclass changes behavior unexpectedly
public abstract class Rectangle {
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
        this.height = width; // Unexpected behavior!
    }
}

// RIGHT: Use composition or separate interfaces
public interface Shape {
    int getArea();
}

public record Rectangle(int width, int height) implements Shape {
    @Override
    public int getArea() { return width * height; }
}

public record Square(int side) implements Shape {
    @Override
    public int getArea() { return side * side; }
}
```

### Mistake 3: Fat Interfaces

```java
// WRONG: Interface with too many methods
public interface UserService {
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
    User findById(long id);
    List<User> findAll();
    List<User> findByEmail(String email);
    void sendEmail(User user, String message);
    void generateReport();
    void backupData();
    void authenticate(String username, String password);
}

// RIGHT: Segregated interfaces
public interface UserManagement {
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
}

public interface UserQuery {
    User findById(long id);
    List<User> findAll();
    List<User> findByEmail(String email);
}

public interface UserNotification {
    void sendEmail(User user, String message);
}
```

### Mistake 4: Using Abstract Class Instead of Interface

```java
// WRONG: Abstract class with no state
public abstract class Printable {
    public abstract void print();
}

// RIGHT: Interface for pure behavior contract
public interface Printable {
    void print();
}
```

---

## 18. Pitfalls

### Pitfall 1: Diamond Problem

```java
// Problem: Multiple inheritance ambiguity
public interface A {
    default void hello() {
        System.out.println("Hello from A");
    }
}

public interface B {
    default void hello() {
        System.out.println("Hello from B");
    }
}

// Compiler error: C inherits unrelated defaults
public class C implements A, B {
    // Must override to resolve ambiguity
    @Override
    public void hello() {
        A.super.hello(); // Explicit choice
    }
}
```

### Pitfall 2: Interface Evolution Breaking Changes

```java
// Version 1
public interface Repository {
    void save(Object entity);
}

// Version 2 - Adding new method breaks all implementations
public interface Repository {
    void save(Object entity);
    void saveWithAudit(Object entity); // Breaking change!
}

// Solution: Use default methods for evolution
public interface Repository {
    void save(Object entity);
    
    default void saveWithAudit(Object entity) {
        save(entity); // Default falls back to existing method
    }
}
```

### Pitfall 3: Abstract Class Constructor Complexity

```java
// Problem: Complex constructor chains
public abstract class BaseComponent {
    private final String id;
    private final Configuration config;
    
    public BaseComponent(String id, Configuration config) {
        this.id = id;
        this.config = config;
    }
}

public class ConcreteComponent extends BaseComponent {
    private final Logger logger;
    
    // Must call super with all parameters
    public ConcreteComponent(String id, Configuration config, Logger logger) {
        super(id, config); // Tight coupling
        this.logger = logger;
    }
}

// Solution: Use builder pattern or interfaces
public interface Component {
    String getId();
    void initialize();
}
```

### Pitfall 4: Overusing Abstraction

```java
// Problem: Unnecessary abstraction layers
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

// Solution: Keep it simple when possible
public class SimpleGreeter {
    public void greet() {
        System.out.println("Hello");
    }
}
```

---

## 19. Debugging Tips

### Common Issues and Solutions

```java
// Issue 1: AbstractMethodError at runtime
// Cause: Missing implementation of abstract method
public abstract class Base {
    public abstract void doWork();
}

public class Child extends Base {
    // Forgot to implement doWork()
}

// Debug: Check if all abstract methods are implemented
// IDE: Use "Add unimplemented methods" feature

// Issue 2: ClassCastException with interfaces
// Cause: Wrong type checking
public interface Flyable {
    void fly();
}

public interface Swimmable {
    void swim();
}

public class Duck implements Flyable, Swimmable {
    public void fly() { System.out.println("Flying"); }
    public void swim() { System.out.println("Swimming"); }
}

// WRONG:
Object duck = new Duck();
// ((Flyable) duck).fly(); // Works
// ((Swimmable) duck).swim(); // Works
// ((Swimmable) duck).fly(); // ClassCastException!

// RIGHT:
Duck duck = new Duck();
Flyable flyer = duck;      // Explicit type
Swimmable swimmer = duck;  // Explicit type
```

### Debugging Techniques

```java
// 1. Use instanceof to check types
if (shape instanceof Circle circle) {  // Pattern matching (Java 16+)
    circle.draw();
}

// 2. Print class hierarchy
Class<?> clazz = obj.getClass();
while (clazz != null) {
    System.out.println(clazz.getName());
    clazz = clazz.getSuperclass();
}

// 3. Check interface implementation
Class<?>[] interfaces = obj.getClass().getInterfaces();
for (Class<?> iface : interfaces) {
    System.out.println("Implements: " + iface.getName());
}

// 4. Use debugger to inspect vtable/itable
// Set breakpoint and examine method dispatch
```

### IDE Debugging Features

1. **Eclipse/IntelliJ**: Debug → Evaluate Expression
2. **Step Over (F6)**: Execute current line
3. **Step Into (F5)**: Enter method call
4. **Step Out (F7)**: Exit current method
5. **Run to Cursor**: Execute until breakpoint

---

## 20. Comparison Table

### Abstract Class vs Interface vs Concrete Class

| Feature | Abstract Class | Interface | Concrete Class |
|---------|----------------|-----------|----------------|
| **Instantiation** | No | No | Yes |
| **Abstract Methods** | Required (optional) | Required (optional) | No |
| **Concrete Methods** | Yes | Java 8+ default | Yes |
| **Instance Variables** | Yes | No (constants only) | Yes |
| **Constructors** | Yes | No | Yes |
| **Inheritance** | Single | Multiple | Single |
| **Access Modifiers** | All | public (default) | All |
| **Static Methods** | Yes | Java 8+ | Yes |
| **Final Methods** | Yes | Java 9+ private | Yes |
| **Design Purpose** | Partial implementation | Pure contract | Full implementation |

### When to Use Each

| Scenario | Recommended |
|----------|-------------|
| Shared code + contract | Abstract class |
| Pure behavior contract | Interface |
| Multiple inheritance of type | Interface |
| Need constructors | Abstract class |
| Need instance state | Abstract class |
| API boundaries | Interface |
| Template Method pattern | Abstract class |
| Strategy pattern | Interface |
| Factory pattern | Interface |
| Multiple unrelated implementations | Interface |

### Java 8+ Interface Features

| Feature | Java 7 | Java 8 | Java 9 | Java 11 |
|---------|--------|--------|--------|---------|
| Abstract methods | Yes | Yes | Yes | Yes |
| Default methods | No | Yes | Yes | Yes |
| Static methods | No | Yes | Yes | Yes |
| Private methods | No | No | Yes | Yes |
| Private static | No | No | No | Yes |

---

## 21. Decision Tree

### Should You Use an Abstract Class or Interface?

```
START: Do you need shared state (instance variables)?
  │
  ├─ YES → Use Abstract Class
  │         └─ Do you need a constructor?
  │              ├─ YES → Abstract Class (required)
  │              └─ NO → Abstract Class (still appropriate)
  │
  └─ NO → Do you need multiple inheritance of type?
           │
           ├─ YES → Use Interface
           │         └─ Do you need default methods?
           │              ├─ YES → Interface with defaults
           │              └─ NO → Interface (pure contract)
           │
           └─ NO → Do you need to define a contract only?
                    │
                    ├─ YES → Use Interface
                    │         └─ Will implementations vary widely?
                    │              ├─ YES → Interface
                    │              └─ NO → Consider abstract class
                    │
                    └─ NO → Use Concrete Class
```

### Quick Decision Matrix

| Need | Abstract Class | Interface |
|------|----------------|-----------|
| Shared fields | ✓ | ✗ |
| Constructors | ✓ | ✗ |
| Multiple inheritance | ✗ | ✓ |
| Default implementations | ✓ | ✓ (Java 8+) |
| Static utility methods | ✓ | ✓ (Java 8+) |
| Strict contract | Partial | ✓ |
| API stability | Good | Better |

---

## 22. Interview Questions

### Basic Questions

**Q1: What is abstraction in OOP?**
**A:** Abstraction is the process of hiding complex implementation details while exposing only essential features. It allows developers to focus on *what* an object does rather than *how* it does it.

**Q2: What is the difference between abstract class and interface?**
**A:**
| Aspect | Abstract Class | Interface |
|--------|----------------|-----------|
| State | Can have instance variables | Only constants |
| Constructors | Yes | No |
| Inheritance | Single | Multiple |
| Methods | Abstract + concrete | Abstract + default/static |

**Q3: Can an abstract class have a constructor?**
**A:** Yes. Abstract classes can have constructors, which are called when subclass instances are created.

### Advanced Questions

**Q4: What is the Template Method pattern?**
**A:** A behavioral pattern where an abstract class defines the skeleton of an algorithm, deferring some steps to subclasses. The abstract class controls the flow while subclasses provide specific implementations.

```java
public abstract class DataProcessor {
    public final void process() {  // Template method
        readData();
        processData();
        writeData();
    }
    
    protected abstract void readData();
    protected abstract void processData();
    protected abstract void writeData();
}
```

**Q5: How do default methods in interfaces solve the diamond problem?**
**A:** When a class inherits conflicting default methods, the compiler requires the class to explicitly override the method and choose which implementation to use.

```java
public interface A {
    default void hello() { System.out.println("A"); }
}

public interface B {
    default void hello() { System.out.println("B"); }
}

public class C implements A, B {
    @Override
    public void hello() {
        A.super.hello(); // Must resolve ambiguity
    }
}
```

**Q6: What is the Liskov Substitution Principle and how does it relate to abstraction?**
**A:** LSP states that subtypes must be substitutable for their base types without altering program correctness. When creating abstractions, ensure that implementations can seamlessly replace the base type.

**Q7: When would you use an abstract class over an interface?**
**A:** Use abstract classes when you need:
- Shared state (instance variables)
- Constructors
- Non-public members
- Template Method pattern
- Partial implementation

### Coding Questions

**Q8: Implement a simple abstraction for different notification types.**
```java
public interface Notification {
    void send(String message);
    default void sendWithRetry(String message, int retries) {
        for (int i = 0; i < retries; i++) {
            send(message);
        }
    }
}

public class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

public class SMSNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}
```

---

## 23. Exercises

### Exercise 1: Shape Abstraction (Easy)

Create an abstract class `Shape` with abstract methods `area()` and `perimeter()`. Implement concrete classes `Circle`, `Rectangle`, and `Triangle`.

```java
public abstract class Shape {
    private final String name;
    
    protected Shape(String name) {
        this.name = name;
    }
    
    public abstract double area();
    public abstract double perimeter();
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name + " [area=" + String.format("%.2f", area()) + 
               ", perimeter=" + String.format("%.2f", perimeter()) + "]";
    }
}

public class Circle extends Shape {
    private final double radius;
    
    public Circle(double radius) {
        super("Circle");
        this.radius = radius;
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }
}

public class Rectangle extends Shape {
    private final double width;
    private final double height;
    
    public Rectangle(double width, double height) {
        super("Rectangle");
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() {
        return width * height;
    }
    
    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
}

public class Triangle extends Shape {
    private final double a, b, c;
    
    public Triangle(double a, double b, double c) {
        super("Triangle");
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public double area() {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
    
    @Override
    public double perimeter() {
        return a + b + c;
    }
}

// Test
Shape[] shapes = {
    new Circle(5),
    new Rectangle(4, 6),
    new Triangle(3, 4, 5)
};

for (Shape shape : shapes) {
    System.out.println(shape);
}
```

### Exercise 2: Payment Processor (Medium)

Implement a payment processing system using abstract classes and interfaces:

```java
public interface PaymentMethod {
    boolean validate();
    PaymentResult charge(BigDecimal amount);
    RefundResult refund(String transactionId, BigDecimal amount);
}

public abstract class AbstractPayment implements PaymentMethod {
    protected final String merchantId;
    protected final List<String> transactionHistory = new ArrayList<>();
    
    protected AbstractPayment(String merchantId) {
        this.merchantId = merchantId;
    }
    
    protected void logTransaction(String transactionId) {
        transactionHistory.add(transactionId);
    }
    
    public List<String> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }
}

public class CreditCardPayment extends AbstractPayment {
    private final String cardNumber;
    private final String cvv;
    
    public CreditCardPayment(String merchantId, String cardNumber, String cvv) {
        super(merchantId);
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }
    
    @Override
    public boolean validate() {
        return cardNumber != null && cardNumber.length() == 16
            && cvv != null && cvv.length() == 3;
    }
    
    @Override
    public PaymentResult charge(BigDecimal amount) {
        if (!validate()) {
            return PaymentResult.failure("Invalid card");
        }
        String txId = UUID.randomUUID().toString();
        logTransaction(txId);
        return PaymentResult.success(txId, amount);
    }
    
    @Override
    public RefundResult refund(String transactionId, BigDecimal amount) {
        return RefundResult.success(transactionId, amount);
    }
}
```

### Exercise 3: Plugin System (Hard)

Design a plugin system with abstraction:

```java
public interface Plugin {
    String getId();
    String getVersion();
    void onLoad(PluginContext context);
    void onUnload();
}

public abstract class AbstractPlugin implements Plugin {
    protected PluginContext context;
    protected boolean loaded = false;
    
    @Override
    public final void onLoad(PluginContext context) {
        this.context = context;
        onInitialize();
        loaded = true;
    }
    
    @Override
    public final void onUnload() {
        onCleanup();
        loaded = false;
    }
    
    protected abstract void onInitialize();
    protected abstract void onCleanup();
    
    protected void requireLoaded() {
        if (!loaded) {
            throw new IllegalStateException("Plugin not loaded");
        }
    }
}

public class AnalyticsPlugin extends AbstractPlugin {
    private final Map<String, MetricCollector> collectors = new HashMap<>();
    
    @Override
    public String getId() {
        return "analytics";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    protected void onInitialize() {
        collectors.put("pageviews", new PageViewCollector());
        collectors.put("clicks", new ClickCollector());
        context.registerService(MetricService.class, new MetricServiceImpl(collectors));
    }
    
    @Override
    protected void onCleanup() {
        collectors.clear();
    }
}
```

---

## 24. Assignments

### Assignment 1: Library Management System (Beginner)

**Objective:** Design an abstraction layer for a library system.

**Requirements:**
1. Create an abstract class `LibraryItem` with:
   - Common fields: `id`, `title`, `isAvailable`
   - Abstract methods: `calculateLateFee(int days)`, `getType()`
   - Concrete methods: `checkout()`, `returnItem()`

2. Create concrete classes:
   - `Book`: Has `author`, `isbn`, `pageCount`
   - `DVD`: Has `director`, `duration`, `rating`
   - `Magazine`: Has `issueNumber`, `publisher`

3. Each type has different late fee calculation logic.

**Expected Output:**
```java
LibraryItem book = new Book("B001", "Java Programming", true, "John Doe", "978-0123456789", 500);
LibraryItem dvd = new DVD("D001", "Inception", true, "Christopher Nolan", 148, "PG-13");

System.out.println(book.getType());      // Book
System.out.println(book.calculateLateFee(7)); // $3.50
System.out.println(dvd.getType());       // DVD
System.out.println(dvd.calculateLateFee(7)); // $7.00
```

### Assignment 2: E-commerce Discount System (Intermediate)

**Objective:** Implement a discount calculation system using interfaces.

**Requirements:**
1. Create interfaces:
   - `Discountable`: `calculateDiscount(BigDecimal price)`
   - `TieredDiscount`: extends `Discountable`, adds `getTier()`

2. Implement discount types:
   - `PercentageDiscount`: Fixed percentage
   - `FlatDiscount`: Fixed amount off
   - `BuyOneGetOneFree`: BOGO logic
   - `LoyaltyDiscount`: Tiered based on customer loyalty

3. Create a `DiscountCalculator` that applies the best discount.

### Assignment 3: Plugin Architecture (Advanced)

**Objective:** Design a plugin system with dependency injection.

**Requirements:**
1. Create plugin interfaces and abstract classes
2. Implement plugin lifecycle management (load, initialize, unload)
3. Add plugin dependency resolution
4. Create a plugin registry and discovery mechanism
5. Implement hot-reloading capability

**Deliverables:**
- Complete source code
- Unit tests
- Documentation
- Architecture diagram

---

## 25. Mini Project

### Project: Task Scheduler with Abstraction

**Objective:** Build a task scheduling system demonstrating abstraction principles.

**Architecture:**

```
┌─────────────────────────────────────────────────────────────┐
│                    TaskScheduler                            │
│  (Abstract class - template method)                         │
├─────────────────────────────────────────────────────────────┤
│  + schedule(Task task): ScheduleResult                      │
│  + cancel(String taskId): boolean                           │
│  + getSchedule(): List<ScheduledTask>                       │
├─────────────────────────────────────────────────────────────┤
│  # validateTask(Task task): void (abstract)                  │
│  # executeTask(Task task): TaskResult (abstract)            │
│  # onTaskComplete(Task task, TaskResult result): void       │
└─────────────────────────────────────────────────────────────┘
         │                      │                      │
         ▼                      ▼                      ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│ ImmediateScheduler│  │ DelayedScheduler│  │ RecurringScheduler│
│  (Concrete)      │  │  (Concrete)     │  │  (Concrete)       │
└─────────────────┘  └─────────────────┘  └─────────────────┘
```

**Core Abstractions:**

```java
public interface Task {
    String getId();
    String getName();
    TaskType getType();
    Runnable getExecutable();
    Duration getTimeout();
}

public interface TaskResult {
    String getTaskId();
    TaskStatus getStatus();
    Optional<Exception> getError();
    Duration getExecutionTime();
}

public interface Scheduler {
    ScheduleResult schedule(Task task);
    boolean cancel(String taskId);
    List<ScheduledTask> getSchedule();
    SchedulerStats getStats();
}

public abstract class AbstractScheduler implements Scheduler {
    protected final TaskRepository repository;
    protected final ExecutorService executor;
    protected final MetricsCollector metrics;
    
    protected AbstractScheduler(TaskRepository repository, 
                               ExecutorService executor,
                               MetricsCollector metrics) {
        this.repository = repository;
        this.executor = executor;
        this.metrics = metrics;
    }
    
    @Override
    public final ScheduleResult schedule(Task task) {
        validateTask(task);
        ScheduledTask scheduled = createScheduledTask(task);
        repository.save(scheduled);
        metrics.recordScheduled(task.getType());
        return ScheduleResult.success(scheduled.getId());
    }
    
    protected abstract void validateTask(Task task);
    protected abstract ScheduledTask createScheduledTask(Task task);
}

public class ImmediateScheduler extends AbstractScheduler {
    @Override
    protected void validateTask(Task task) {
        if (task.getTimeout().toSeconds() > 30) {
            throw new IllegalArgumentException("Immediate tasks must have short timeout");
        }
    }
    
    @Override
    protected ScheduledTask createScheduledTask(Task task) {
        CompletableFuture<TaskResult> future = CompletableFuture.supplyAsync(
            () -> executeTask(task), executor
        );
        return new ScheduledTask(task, ScheduleType.IMMEDIATE, future);
    }
    
    private TaskResult executeTask(Task task) {
        long start = System.currentTimeMillis();
        try {
            task.getExecutable().run();
            return TaskResult.success(task.getId(), 
                Duration.ofMillis(System.currentTimeMillis() - start));
        } catch (Exception e) {
            return TaskResult.failure(task.getId(), e);
        }
    }
}

public class DelayedScheduler extends AbstractScheduler {
    private final ScheduledExecutorService scheduler;
    
    @Override
    protected void validateTask(Task task) {
        // Additional validation for delayed tasks
    }
    
    @Override
    protected ScheduledTask createScheduledTask(Task task) {
        CompletableFuture<TaskResult> future = new CompletableFuture<>();
        scheduler.schedule(
            () -> future.complete(executeTask(task)),
            task.getDelay().toMillis(),
            TimeUnit.MILLISECONDS
        );
        return new ScheduledTask(task, ScheduleType.DELAYED, future);
    }
}

public class RecurringScheduler extends AbstractScheduler {
    private final Map<String, ScheduledFuture<?>> recurringTasks = new ConcurrentHashMap<>();
    
    @Override
    protected void validateTask(Task task) {
        if (task.getInterval() == null || task.getInterval().isZero()) {
            throw new IllegalArgumentException("Recurring tasks must have interval");
        }
    }
    
    @Override
    protected ScheduledTask createScheduledTask(Task task) {
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(
            () -> executeTask(task),
            0,
            task.getInterval().toMillis(),
            TimeUnit.MILLISECONDS
        );
        recurringTasks.put(task.getId(), future);
        return new ScheduledTask(task, ScheduleType.RECURRING, 
            CompletableFuture.completedFuture(null));
    }
}

// Client usage
public class TaskSchedulerDemo {
    public static void main(String[] args) {
        TaskRepository repository = new InMemoryTaskRepository();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        MetricsCollector metrics = new SimpleMetricsCollector();
        
        Scheduler immediate = new ImmediateScheduler(repository, executor, metrics);
        Scheduler delayed = new DelayedScheduler(repository, executor, metrics);
        Scheduler recurring = new RecurringScheduler(repository, executor, metrics);
        
        Task emailTask = new EmailTask("EMAIL001", "Welcome email", 
            "user@example.com", "Welcome!");
        
        ScheduleResult result = immediate.schedule(emailTask);
        System.out.println("Scheduled: " + result.getTaskId());
    }
}
```

**Features to Implement:**
1. Task prioritization
2. Dependency resolution between tasks
3. Retry mechanism with exponential backoff
4. Task timeout handling
5. Dashboard for monitoring scheduled tasks
6. REST API for task management

---

## 26. Summary

### Key Takeaways

1. **Abstraction Hides Complexity**: Focus on *what* not *how*
2. **Abstract Classes**: For shared state + contract (single inheritance)
3. **Interfaces**: For pure contracts (multiple inheritance of type)
4. **Java 8+ Enhanced Interfaces**: Default, static, private methods
5. **Template Method Pattern**: Abstract classes define algorithm skeleton
6. **Follow SOLID Principles**: Especially Liskov Substitution and Interface Segregation

### When to Use Abstraction

| Use Abstract Class When | Use Interface When |
|-------------------------|-------------------|
| Need shared state | Need multiple inheritance |
| Need constructors | API boundaries |
| Template Method pattern | Strategy/Command patterns |
| Partial implementation | Pure behavior contract |
| Tight cohesion | Loose coupling |

### Best Practices Summary

- Keep abstractions small and focused
- Prefer interfaces for API design
- Use abstract classes for shared implementation
- Follow naming conventions (Abstract*, *able, *able)
- Document abstract contracts clearly
- Consider performance implications in hot paths

---

## 27. References

### Official Java Documentation

- [Abstract Classes (Oracle)](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html)
- [Interfaces (Oracle)](https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html)
- [Default Methods (Oracle)](https://docs.oracle.com/javase/8/docs/technotes/guides/language/interfaces.html)

### Books

- *Effective Java* by Joshua Bloch - Item 20: Prefer interfaces to abstract classes
- *Clean Code* by Robert C. Martin - Chapter 11: Systems
- *Design Patterns* by Gang of Four - Template Method, Strategy, Observer
- *Head First Design Patterns* - Abstraction and Polymorphism

### Online Resources

- [Baeldung: Abstract Classes vs Interfaces](https://www.baeldung.com/java-abstract-class-vs-interface)
- [Oracle Java Tutorials: Interfaces and Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/)
- [Refactoring.Guru: Template Method](https://refactoring.guru/design-patterns/template-method)

### Related Topics

- [Encapsulation](./encapsulation.md)
- [Polymorphism](./polymorphism.md)
- [Inheritance](./inheritance.md)
- [SOLID Principles](./solid.md)

---

*Last updated: August 2026*
*Java version: 21*
