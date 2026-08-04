# Abstraction

Abstraction hides complex implementation details while exposing a simple interface. It focuses on what an object does rather than how it does it.

## Table of Contents

1. [Concepts](#concepts)
2. [Abstract Classes](#abstract-classes)
3. [Interfaces](#interfaces)
4. [Default Methods](#default-methods)
5. [Functional Interfaces](#functional-interfaces)
6. [Interface Design](#interface-design)
7. [Abstract Class vs Interface](#abstract-class-vs-interface)
8. [Best Practices](#best-practices)
9. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Abstraction?

Abstraction defines a contract (what) without specifying implementation (how).

```
┌─────────────────────────────────────────────┐
│              ABSTRACTION                    │
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │     Abstract Class / Interface      │   │
│  │     - Defines contract              │   │
│  │     - Specifies what to do          │   │
│  └─────────────────────────────────────┘   │
│                    │                        │
│       ┌────────────┼────────────┐          │
│       │            │            │          │
│       ▼            ▼            ▼          │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐     │
│  │Impl A   │ │Impl B   │ │Impl C   │     │
│  │(How)    │ │(How)    │ │(How)    │     │
│  └─────────┘ └─────────┘ └─────────┘     │
└─────────────────────────────────────────────┘
```

### Benefits

- **Loose Coupling** - code depends on abstractions, not implementations
- **Flexibility** - swap implementations without changing client code
- **Testability** - easy to mock abstractions for testing
- **Maintainability** - changes to implementation don't affect clients

---

## Abstract Classes

### Basic Abstract Class

```java
public abstract class Vehicle {
    protected String name;
    protected int speed;

    public Vehicle(String name) {
        this.name = name;
        this.speed = 0;
    }

    // Abstract method - no implementation
    public abstract void start();
    public abstract void stop();
    public abstract double calculateFuelEfficiency();

    // Concrete method - inherited as-is
    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public void accelerate(int amount) {
        speed += amount;
    }

    // Template method - defines algorithm skeleton
    public final void performTrip() {
        start();
        drive();
        stop();
    }

    protected abstract void drive();
}
```

### Abstract Class with State

```java
public abstract class AbstractRepository<T> {
    protected final List<T> items = new ArrayList<>();
    protected final List<RepositoryListener<T>> listeners = new ArrayList<>();

    public void add(T item) {
        items.add(item);
        notifyListeners("added", item);
    }

    public void remove(T item) {
        items.remove(item);
        notifyListeners("removed", item);
    }

    public List<T> findAll() {
        return List.copyOf(items);
    }

    // Abstract method - subclasses define how to find
    public abstract Optional<T> findById(Object id);

    // Hook method - subclasses can optionally override
    protected void notifyListeners(String action, T item) {
        listeners.forEach(l -> l.onChanged(action, item));
    }

    public void addListener(RepositoryListener<T> listener) {
        listeners.add(listener);
    }
}

public interface RepositoryListener<T> {
    void onChanged(String action, T item);
}
```

### Multiple Levels of Abstraction

```java
// Level 1: Most abstract
public abstract class Shape {
    protected String color;

    public Shape(String color) {
        this.color = color;
    }

    public abstract double area();
    public abstract double perimeter();
    public abstract void draw(GraphicsContext gc);
}

// Level 2: Intermediate abstraction
public abstract class Polygon extends Shape {
    protected int sides;

    public Polygon(String color, int sides) {
        super(color);
        this.sides = sides;
    }

    // Common polygon behavior
    public int getSides() {
        return sides;
    }
}

// Level 3: Concrete implementations
public class Triangle extends Polygon {
    private final double a, b, c;

    public Triangle(String color, double a, double b, double c) {
        super(color, 3);
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

    @Override
    public void draw(GraphicsContext gc) {
        // Triangle drawing implementation
    }
}
```

---

## Interfaces

### Basic Interface

```java
public interface Drawable {
    void draw(GraphicsContext gc);
    default double area() {
        return 0;
    }
}

public interface Resizable {
    void resize(double factor);
    double getScale();
}

// A class can implement multiple interfaces
public class Circle implements Drawable, Resizable {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.fillOval(0, 0, radius * 2, radius * 2);
    }

    @Override
    public void resize(double factor) {
        radius *= factor;
    }

    @Override
    public double getScale() {
        return radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}
```

### Interface Constants

```java
public interface Constants {
    // Public static final by default
    int MAX_RETRIES = 3;
    String DEFAULT_ENCODING = "UTF-8";
    double PI = 3.14159265358979;
}

// Usage
int retries = Constants.MAX_RETRIES;
```

### Interface with Multiple Methods

```java
public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    List<T> findAllById(Iterable<ID> ids);
    T save(T entity);
    List<T> saveAll(Iterable<T> entities);
    void deleteById(ID id);
    void delete(T entity);
    long count();
    boolean existsById(ID id);
}

// Implementation
public class UserRepository implements Repository<User, Long> {
    private final Map<Long, User> store = new HashMap<>();

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public User save(User user) {
        store.put(user.getId(), user);
        return user;
    }

    // ... other method implementations
}
```

---

## Default Methods

### Basic Default Methods

```java
public interface List<E> {
    int size();
    boolean isEmpty();
    E get(int index);

    // Default method - provides implementation
    default boolean contains(E element) {
        for (int i = 0; i < size(); i++) {
            if (element.equals(get(i))) {
                return true;
            }
        }
        return false;
    }

    default void addAll(List<E> other) {
        for (int i = 0; i < other.size(); i++) {
            add(other.get(i));
        }
    }

    default E getFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        return get(0);
    }

    default E getLast() {
        if (isEmpty()) throw new NoSuchElementException();
        return get(size() - 1);
    }
}
```

### Default Methods for API Evolution

```java
// Original interface
public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
}

// Adding new functionality without breaking existing implementations
public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);

    // New default method - doesn't break existing implementations
    default PaymentResult processWithRetry(PaymentRequest request, int maxRetries) {
        PaymentResult result = null;
        for (int i = 0; i <= maxRetries; i++) {
            result = process(request);
            if (result.isSuccessful()) break;
        }
        return result;
    }

    default boolean supportsRefund() {
        return true;  // Default behavior
    }
}
```

### Resolving Default Method Conflicts

```java
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

// Diamond problem - must resolve explicitly
public class C implements A, B {
    @Override
    public void hello() {
        // Option 1: Choose one
        A.super.hello();

        // Option 2: Combine both
        // A.super.hello();
        // B.super.hello();

        // Option 3: Completely new implementation
        // System.out.println("Hello from C");
    }
}

// Resolving with more specific interface
public interface SmartDevice {
    default void turnOn() {
        System.out.println("Smart device turning on");
    }
}

public interface Phone extends SmartDevice {
    @Override
    default void turnOn() {
        System.out.println("Phone turning on");
    }
}

public class Smartphone implements SmartDevice, Phone {
    // Phone's default wins - more specific
    // No override needed
}
```

### Default Methods with State

```java
public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);

    // Default method using interface methods
    default V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return value != null ? value : defaultValue;
    }

    default V computeIfAbsent(K key, java.util.function.Function<K, V> mapper) {
        V value = get(key);
        if (value == null) {
            value = mapper.apply(key);
            put(key, value);
        }
        return value;
    }

    default void clear() {
        // Default implementation - subclasses can override
        throw new UnsupportedOperationException();
    }
}
```

---

## Functional Interfaces

### Basic Functional Interface

```java
@FunctionalInterface
public interface Transformer<T, R> {
    R transform(T input);

    // Can have other methods if they're default or static
    default <V> Transformer<V, R> compose(Transformer<V, T> before) {
        return input -> transform(before.transform(input));
    }

    static <T> Transformer<T, T> identity() {
        return input -> input;
    }
}

// Usage
Transformer<String, Integer> length = String::length;
Transformer<String, String> upper = String::toUpperCase;

// Composition
Transformer<String, Integer> upperLength = length.compose(upper);
System.out.println(upperLength.transform("hello"));  // 5
```

### Common Functional Interfaces

```java
// Predicate - takes T, returns boolean
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);

    default Predicate<T> and(Predicate<? super T> other) {
        return t -> test(t) && other.test(t);
    }

    default Predicate<T> or(Predicate<? super T> other) {
        return t -> test(t) || other.test(t);
    }

    default Predicate<T> negate() {
        return t -> !test(t);
    }
}

// Function - takes T, returns R
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        return v -> apply(before.apply(v));
    }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return t -> after.apply(apply(t));
    }
}

// Consumer - takes T, returns void
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);

    default Consumer<T> andThen(Consumer<? super T> after) {
        return t -> { accept(t); after.accept(t); };
    }
}

// Supplier - takes nothing, returns T
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
```

### Custom Functional Interfaces

```java
@FunctionalInterface
public interface Validator<T> {
    ValidationResult validate(T value);

    default Validator<T> and(Validator<T> other) {
        return value -> {
            ValidationResult result = validate(value);
            return result.isValid() ? other.validate(value) : result;
        };
    }

    default Validator<T> or(Validator<T> other) {
        return value -> {
            ValidationResult result = validate(value);
            return result.isValid() ? result : other.validate(value);
        };
    }
}

public record ValidationResult(boolean isValid, String message) {
    public static ValidationResult valid() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult invalid(String message) {
        return new ValidationResult(false, message);
    }
}

// Usage
Validator<String> notNull = value -> 
    value != null ? ValidationResult.valid() : ValidationResult.invalid("Cannot be null");

Validator<String> minLength = value ->
    value != null && value.length() >= 3 ? ValidationResult.valid() : ValidationResult.invalid("Too short");

Validator<String> composite = notNull.and(minLength);
```

---

## Interface Design

### Segregated Interfaces

```java
// BAD - fat interface
public interface Worker {
    void work();
    void eat();
    void sleep();
}

// GOOD - segregated interfaces
public interface Workable {
    void work();
}

public interface Feedable {
    void eat();
}

public interface Sleepable {
    void sleep();
}

// Classes implement only what they need
public class Robot implements Workable {
    @Override
    public void work() {
        System.out.println("Robot working");
    }
    // Robots don't eat or sleep
}

public class Human implements Workable, Feedable, Sleepable {
    @Override
    public void work() { System.out.println("Human working"); }

    @Override
    public void eat() { System.out.println("Human eating"); }

    @Override
    public void sleep() { System.out.println("Human sleeping"); }
}
```

### Interface as Type

```java
// Use interface as parameter type
public class TaskRunner {
    public void execute(Runnable task) {
        task.run();
    }

    public <T> List<T> filter(List<T> items, Predicate<T> predicate) {
        return items.stream()
            .filter(predicate)
            .toList();
    }
}

// Client depends on abstraction
TaskRunner runner = new TaskRunner();
runner.execute(() -> System.out.println("Task executed"));
```

### Marker Interfaces

```java
// Interface with no methods - used for type checking
public interface Serializable {}
public interface Cloneable {}
public interface Remote {}

// Usage
public class DataProcessor {
    public void process(Object data) {
        if (data instanceof Serializable) {
            serialize(data);
        }
        if (data instanceof Cloneable) {
            clone(data);
        }
    }
}
```

---

## Abstract Class vs Interface

### Comparison

| Feature | Abstract Class | Interface |
|---------|---------------|-----------|
| **State** | Can have instance fields | Only constants (static final) |
| **Constructors** | Yes | No |
| **Methods** | Abstract and concrete | Abstract, default, static |
| **Access Modifiers** | Any | Public (pre-Java 9) |
| **Multiple Inheritance** | Single class only | Multiple interfaces |
| **Performance** | Slightly faster | Slightly slower (indirection) |

### When to Use Each

```java
// Use ABSTRACT CLASS when:
// - You need to share state or implementation
// - You want to define a template method
// - You need constructors
public abstract class AbstractList<E> {
    protected int size;
    protected E[] elementData;

    public AbstractList() {
        this.elementData = (E[]) new Object[10];
    }

    public abstract void add(E element);
    public abstract E get(int index);

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
}

// Use INTERFACE when:
// - You need multiple inheritance
// - You're defining a capability/contract
// - You want maximum flexibility
public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    T save(T entity);
    void deleteById(ID id);
}

// Use both together
public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {
    protected final List<T> items = new ArrayList<>();

    @Override
    public List<T> findAll() {
        return List.copyOf(items);
    }
}
```

---

## Best Practices

### Program to Interface

```java
// BAD - depends on concrete class
ArrayList<String> list = new ArrayList<>();

// GOOD - depends on interface
List<String> list = new ArrayList<>();

// BETTER - depends on minimal interface
Collection<String> collection = new ArrayList<>();
```

### Keep Interfaces Focused

```java
// BAD - doing too much
public interface UserService {
    User createUser(UserDto dto);
    User updateUser(Long id, UserDto dto);
    void deleteUser(Long id);
    List<User> searchUsers(String query);
    void sendEmail(User user, String message);
    void exportToCsv(List<User> users, OutputStream out);
}

// GOOD - focused interfaces
public interface UserCrudService {
    User createUser(UserDto dto);
    User updateUser(Long id, UserDto dto);
    void deleteUser(Long id);
}

public interface UserSearchService {
    List<User> searchUsers(String query);
}

public interface UserNotificationService {
    void sendEmail(User user, String message);
}

public interface UserExportService {
    void exportToCsv(List<User> users, OutputStream out);
}
```

### Use Default Methods for Evolution

```java
// Adding methods without breaking implementations
public interface Stream<T> {
    Iterator<T> iterator();

    // New default method
    default void forEach(Consumer<? super T> action) {
        for (T item : this) {
            action.accept(item);
        }
    }

    // New static method
    static <T> Stream<T> of(T... values) {
        return Arrays.stream(values);
    }
}
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Abstract Class** | Partial implementation, can hold state |
| **Interface** | Pure contract, multiple inheritance |
| **Default Method** | Interface method with implementation |
| **Functional Interface** | Single abstract method interface |
| **`@FunctionalInterface`** | Compile-time check for single method |
| **Interface Segregation** | Many small interfaces > one large |
| **Program to Interface** | Depend on abstractions |
| **Abstract Class** | When you need shared state/implementation |
| **Interface** | When you need multiple inheritance |
| **Marker Interface** | Empty interface for type identification |
