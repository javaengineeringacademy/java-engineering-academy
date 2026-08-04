# Composition

Composition builds complex objects by combining simpler ones. It creates "has-a" relationships instead of "is-a" inheritance, leading to more flexible and maintainable code.

## Table of Contents

1. [Concepts](#concepts)
2. [Composition Over Inheritance](#composition-over-inheritance)
3. [Has-A vs Is-A](#has-a-vs-is-a)
4. [Delegation](#delegation)
5. [Composite Pattern](#composite-pattern)
6. [Dependency Injection](#dependency-injection)
7. [Best Practices](#best-practices)
8. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Composition?

Composition creates objects by assembling other objects. The composed object manages the lifecycle and behavior of its parts.

```
┌──────────────────────────────────────┐
│           Car (Composite)            │
│                                      │
│  ┌─────────┐  ┌─────────┐          │
│  │ Engine  │  │  Wheel  │ x4       │
│  │  - type │  │  - size │          │
│  │  - power│  │  - pos  │          │
│  └─────────┘  └─────────┘          │
│                                      │
│  ┌─────────┐  ┌─────────┐          │
│  │  Door   │  │ Battery │          │
│  │  - side │  │ - volt  │          │
│  └─────────┘  └─────────┘          │
└──────────────────────────────────────┘
```

### Benefits

- **Flexibility** - swap components at runtime
- **Reusability** - components work in different contexts
- **Testability** - mock individual components
- **Loose Coupling** - components are independent

---

## Composition Over Inheritance

### Problem with Inheritance

```java
// BAD: Inheritance for code reuse
public class Stack extends ArrayList<Object> {
    public void push(Object item) { add(item); }
    public Object pop() {
        return remove(size() - 1);
    }
}

// Problems:
// 1. Exposes all ArrayList methods (get, remove, etc.)
// 2. Stack should only have push/pop/top/size
// 3. Changing ArrayList breaks Stack
// 4. Tight coupling to implementation
```

### Solution with Composition

```java
// GOOD: Composition
public class Stack<T> {
    private final List<T> items = new ArrayList<>();

    public void push(T item) {
        items.add(item);
    }

    public T pop() {
        if (items.isEmpty()) {
            throw new EmptyStackException();
        }
        return items.remove(items.size() - 1);
    }

    public T peek() {
        if (items.isEmpty()) {
            throw new EmptyStackException();
        }
        return items.get(items.size() - 1);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Only expose relevant methods
}
```

### Decorator Pattern (Composition)

```java
// Base interface
public interface DataSource {
    void writeData(String data);
    String readData();
}

// Core implementation
public class FileDataSource implements DataSource {
    private String filename;

    public FileDataSource(String filename) {
        this.filename = filename;
    }

    @Override
    public void writeData(String data) {
        // Write to file
    }

    @Override
    public String readData() {
        // Read from file
        return "";
    }
}

// Decorator - adds compression
public class CompressionDecorator implements DataSource {
    private final DataSource wrappee;

    public CompressionDecorator(DataSource source) {
        this.wrappee = source;
    }

    @Override
    public void writeData(String data) {
        String compressed = compress(data);
        wrappee.writeData(compressed);
    }

    @Override
    public String readData() {
        String data = wrappee.readData();
        return decompress(data);
    }

    private String compress(String data) { return data; }
    private String decompress(String data) { return data; }
}

// Decorator - adds encryption
public class EncryptionDecorator implements DataSource {
    private final DataSource wrappee;

    public EncryptionDecorator(DataSource source) {
        this.wrappee = source;
    }

    @Override
    public void writeData(String data) {
        String encrypted = encrypt(data);
        wrappee.writeData(encrypted);
    }

    @Override
    public String readData() {
        String data = wrappee.readData();
        return decrypt(data);
    }

    private String encrypt(String data) { return data; }
    private decrypt(String data) { return data; }
}

// Usage - compose behaviors dynamically
DataSource source = new EncryptionDecorator(
    new CompressionDecorator(
        new FileDataSource("data.txt")
    )
);
```

---

## Has-A vs Is-A

### Is-A (Inheritance)

```java
// Inheritance: Dog IS-A Animal
public class Animal {
    protected String name;
    protected int age;

    public void eat() { /* ... */ }
    public void sleep() { /* ... */ }
}

public class Dog extends Animal {
    private String breed;

    public void fetch() { /* ... */ }  // Dog-specific behavior
}

// Dog inherits from Animal - "is-a" relationship
Dog dog = new Dog();
dog.eat();     // Inherited
dog.fetch();   // Dog-specific
```

### Has-A (Composition)

```java
// Composition: Car HAS-A Engine
public class Engine {
    private int horsepower;
    private String fuelType;

    public void start() { /* ... */ }
    public void stop() { /* ... */ }
}

public class Car {
    private final Engine engine;  // Has-a Engine
    private final List<Wheel> wheels;  // Has-a Wheels
    private final Transmission transmission;  // Has-a Transmission

    public Car(Engine engine) {
        this.engine = engine;
        this.wheels = List.of(
            new Wheel("front-left"),
            new Wheel("front-right"),
            new Wheel("rear-left"),
            new Wheel("rear-right")
        );
        this.transmission = new AutomaticTransmission();
    }

    public void start() {
        engine.start();
        transmission.engage();
    }
}
```

### When to Use Each

```java
// USE INHERITANCE when:
// - There's a true "is-a" relationship
// - You want to inherit implementation
// - The hierarchy is stable
public class SavingsAccount extends BankAccount {
    private double interestRate;

    @Override
    public double calculateInterest() {
        return getBalance() * interestRate;
    }
}

// USE COMPOSITION when:
// - You want to reuse behavior without inheritance
// - You need to change behavior at runtime
// - The relationship is "has-a" or "uses-a"
public class Logger {
    private final List<LogAppender> appenders;

    public Logger(List<LogAppender> appenders) {
        this.appenders = appenders;
    }

    public void log(String message) {
        appenders.forEach(a -> a.append(message));
    }
}
```

---

## Delegation

### Basic Delegation

```java
// Interface
public interface Printer {
    void print(String document);
}

// Real implementation
public class RealPrinter implements Printer {
    @Override
    public void print(String document) {
        System.out.println("Printing: " + document);
    }
}

// Delegating class
public class PrinterProxy implements Printer {
    private final Printer delegate;

    public PrinterProxy(Printer delegate) {
        this.delegate = delegate;
    }

    @Override
    public void print(String document) {
        System.out.println("Proxy: Before printing");
        delegate.print(document);  // Delegate to real implementation
        System.out.println("Proxy: After printing");
    }
}
```

### Delegation with Logging

```java
public interface UserService {
    User createUser(CreateUserRequest request);
    User updateUser(Long id, UpdateUserRequest request);
    void deleteUser(Long id);
}

public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        User user = new User(request.name(), request.email());
        return repository.save(user);
    }

    @Override
    public User updateUser(Long id, UpdateUserRequest request) {
        User user = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found"));
        user.update(request);
        return repository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}

// Delegating decorator
public class LoggingUserService implements UserService {
    private final UserService delegate;
    private final Logger logger;

    public LoggingUserService(UserService delegate, Logger logger) {
        this.delegate = delegate;
        this.logger = logger;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        logger.info("Creating user: {}", request.name());
        User user = delegate.createUser(request);
        logger.info("User created: {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(Long id, UpdateUserRequest request) {
        logger.info("Updating user: {}", id);
        return delegate.updateUser(id, request);
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Deleting user: {}", id);
        delegate.deleteUser(id);
    }
}
```

### Forwarding vs Delegation

```java
// Forwarding: Just pass calls through
public class ForwardingList<E> implements List<E> {
    private final List<E> delegate;

    public ForwardingList(List<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int size() { return delegate.size(); }

    @Override
    public boolean isEmpty() { return delegate.isEmpty(); }

    @Override
    public boolean contains(Object o) { return delegate.contains(o); }

    // ... forward all methods
}

// Delegation: Modify behavior before/after forwarding
public class SynchronizedList<E> implements List<E> {
    private final List<E> delegate;
    private final Object lock;

    public SynchronizedList(List<E> delegate) {
        this.delegate = delegate;
        this.lock = new Object();
    }

    @Override
    public boolean add(E e) {
        synchronized (lock) {
            return delegate.add(e);
        }
    }

    @Override
    public E get(int index) {
        synchronized (lock) {
            return delegate.get(index);
        }
    }
}
```

---

## Composite Pattern

### Tree Structure

```java
// Component
public interface FileSystemItem {
    String getName();
    int getSize();
    void print(String indent);
}

// Leaf
public class File implements FileSystemItem {
    private final String name;
    private final int size;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getSize() { return size; }

    @Override
    public void print(String indent) {
        System.out.println(indent + "- " + name + " (" + size + " bytes)");
    }
}

// Composite
public class Directory implements FileSystemItem {
    private final String name;
    private final List<FileSystemItem> children = new ArrayList<>();

    public Directory(String name) {
        this.name = name;
    }

    public void add(FileSystemItem item) {
        children.add(item);
    }

    public void remove(FileSystemItem item) {
        children.remove(item);
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getSize() {
        return children.stream()
            .mapToInt(FileSystemItem::getSize)
            .sum();
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "+ " + name + " (" + getSize() + " bytes)");
        children.forEach(child -> child.print(indent + "  "));
    }
}

// Usage
Directory root = new Directory("root");
Directory src = new Directory("src");
src.add(new File("Main.java", 500));
src.add(new File("Utils.java", 300));

Directory test = new Directory("test");
test.add(new File("MainTest.java", 400));

root.add(src);
root.add(test);
root.add(new File("README.md", 200));

root.print("");
// + root (1400 bytes)
//   + src (800 bytes)
//     - Main.java (500 bytes)
//     - Utils.java (300 bytes)
//   + test (400 bytes)
//     - MainTest.java (400 bytes)
//   - README.md (200 bytes)
```

### Menu System

```java
// Component
public interface MenuComponent {
    String getName();
    double getPrice();
    void print();
}

// Leaf
public class MenuItem implements MenuComponent {
    private final String name;
    private final double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() { return name; }

    @Override
    public double getPrice() { return price; }

    @Override
    public void print() {
        System.out.println("  " + name + " - $" + price);
    }
}

// Composite
public class Menu implements MenuComponent {
    private final String name;
    private final List<MenuComponent> items = new ArrayList<>();

    public Menu(String name) {
        this.name = name;
    }

    public void add(MenuComponent item) {
        items.add(item);
    }

    @Override
    public String getName() { return name; }

    @Override
    public double getPrice() {
        return items.stream()
            .mapToDouble(MenuComponent::getPrice)
            .sum();
    }

    @Override
    public void print() {
        System.out.println(name + " - $" + getPrice());
        items.forEach(MenuComponent::print);
    }
}

// Build menu structure
Menu breakfast = new Menu("Breakfast");
breakfast.add(new MenuItem("Pancakes", 8.99));
breakfast.add(new MenuItem("Eggs", 6.99));

Menu lunch = new Menu("Lunch");
lunch.add(new MenuItem("Burger", 12.99));
lunch.add(new MenuItem("Salad", 9.99));

Menu allDay = new Menu("All Day Menu");
allDay.add(breakfast);
allDay.add(lunch);
allDay.add(new MenuItem("Coffee", 3.99));
```

---

## Dependency Injection

### Constructor Injection

```java
public class OrderService {
    private final OrderRepository orderRepo;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    // Dependencies injected through constructor
    public OrderService(
            OrderRepository orderRepo,
            PaymentService paymentService,
            NotificationService notificationService) {
        this.orderRepo = orderRepo;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    public Order createOrder(OrderRequest request) {
        Order order = new Order(request);
        paymentService.processPayment(order);
        orderRepo.save(order);
        notificationService.sendConfirmation(order);
        return order;
    }
}

// Creating dependencies
OrderRepository repo = new JdbcOrderRepository(dataSource);
PaymentService payment = new StripePaymentService(apiKey);
NotificationService notification = new EmailNotificationService(smtpConfig);

// Inject dependencies
OrderService service = new OrderService(repo, payment, notification);
```

### Field Injection with DI Framework

```java
// Spring-style injection
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private NotificationService notificationService;

    public Order createOrder(OrderRequest request) {
        // Use injected dependencies
    }
}

// Or constructor injection (preferred)
@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final PaymentService paymentService;

    @Autowired
    public OrderService(OrderRepository orderRepo, PaymentService paymentService) {
        this.orderRepo = orderRepo;
        this.paymentService = paymentService;
    }
}
```

### Interface-Based Injection

```java
// Depend on abstraction
public class NotificationService {
    private final NotificationSender sender;

    public NotificationService(NotificationSender sender) {
        this.sender = sender;
    }

    public void send(String message) {
        sender.send(message);
    }
}

// Different implementations
public interface NotificationSender {
    void send(String message);
}

public class EmailSender implements NotificationSender {
    @Override
    public void send(String message) {
        // Send email
    }
}

public class SmsSender implements NotificationSender {
    @Override
    public void send(String message) {
        // Send SMS
    }
}

// Swap implementations easily
NotificationService emailService = new NotificationService(new EmailSender());
NotificationService smsService = new NotificationService(new SmsSender());
```

---

## Best Practices

### Favor Composition

```java
// BAD: Inheritance for reuse
public class ExtendedArrayList extends ArrayList<String> {
    public void addAllUnique(Collection<String> items) {
        for (String item : items) {
            if (!contains(item)) {
                add(item);
            }
        }
    }
}

// GOOD: Composition
public class UniqueList {
    private final Set<String> items = new LinkedHashSet<>();

    public void addAll(Collection<String> newItems) {
        items.addAll(newItems);
    }

    public List<String> asList() {
        return new ArrayList<>(items);
    }

    public int size() { return items.size(); }
    public boolean contains(String item) { return items.contains(item); }
}
```

### Keep Components Small

```java
// GOOD: Small, focused components
public class UserManager {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserManager(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }
}

public class AuthenticationManager {
    private final TokenService tokens;
    private final SessionStore sessions;

    public AuthenticationManager(TokenService tokens, SessionStore sessions) {
        this.tokens = tokens;
        this.sessions = sessions;
    }
}

// Instead of one giant class doing everything
```

### Design for Composition

```java
// GOOD: Easy to compose
public class Pipeline<T> {
    private final List<Function<T, T>> stages = new ArrayList<>();

    public Pipeline<T> addStage(Function<T, T> stage) {
        stages.add(stage);
        return this;
    }

    public T execute(T input) {
        T result = input;
        for (Function<T, T> stage : stages) {
            result = stage.apply(result);
        }
        return result;
    }
}

// Usage - easy to compose
Pipeline<String> pipeline = new Pipeline<String>()
    .addStage(String::trim)
    .addStage(String::toLowerCase)
    .addStage(s -> s.replaceAll("\\s+", " "));

String result = pipeline.execute("  Hello   World  ");
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Composition** | Build objects from other objects |
| **Has-A** | Relationship type (Car has Engine) |
| **Is-A** | Relationship type (Dog is Animal) |
| **Delegation** | Forward requests to contained object |
| **Composite Pattern** | Treat individual and composite objects uniformly |
| **DI** | Inject dependencies, don't create them |
| **Loose Coupling** | Components depend on abstractions |
| **Flexibility** | Swap components at runtime |
| **Testability** | Mock components for testing |
| **Prefer Composition** | Over inheritance for code reuse |
