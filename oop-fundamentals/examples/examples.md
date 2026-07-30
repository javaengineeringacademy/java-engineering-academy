# OOP Examples - Sprint 2

---

## 1. Classes and Objects

### Basic Class with Encapsulation
```java
public final class Person {
    private final String name;
    private int age;
    private final String email;

    public Person(String name, int age, String email) {
        this.name = Objects.requireNonNull(name, "Name required");
        this.age = validateAge(age);
        this.email = Objects.requireNonNull(email, "Email required");
    }

    private int validateAge(int age) {
        if (age < 0) throw new IllegalArgumentException("Age >= 0");
        return age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }

    public void setAge(int age) { this.age = validateAge(age); }

    @Override
    public String toString() {
        return "Person{name='%s', age=%d, email='%s'}".formatted(name, age, email);
    }
}
```

---

## 2. Constructors

### Constructor Chaining
```java
public class Person {
    private String name;
    private int age;
    private String address;

    // Primary constructor
    public Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    // Chaining to primary
    public Person(String name, int age) {
        this(name, age, "Unknown");
    }

    public Person(String name) {
        this(name, 0);
    }
}
```

### Copy Constructor
```java
public Person(Person other) {
    this.name = other.name;
    this.age = other.age;
    this.address = other.address;
}
```

---

## 3. Methods

### Method Overloading
```java
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
    public int add(int... numbers) { return Arrays.stream(numbers).sum(); }
}
```

### Method Overriding
```java
class Animal {
    public void makeSound() { System.out.println("Animal sound"); }
}

class Dog extends Animal {
    @Override
    public void makeSound() { System.out.println("Woof!"); }
}

class Cat extends Animal {
    @Override
    public void makeSound() { System.out.println("Meow!"); }
}
```

---

## 4. Encapsulation

### Bank Account Example
```java
public class BankAccount {
    private BigDecimal balance;

    public BankAccount(BigDecimal initialBalance) {
        this.balance = Objects.requireNonNull(initialBalance);
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance >= 0");
        }
    }

    public BigDecimal getBalance() { return balance; }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount > 0");
        }
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount > 0");
        }
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance = balance.subtract(amount);
    }
}
```

---

## 5. Inheritance

### Shape Hierarchy
```java
public abstract class Shape {
    protected String color;

    protected Shape(String color) {
        this.color = Objects.requireNonNull(color);
    }

    public abstract double area();
    public abstract double perimeter();

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}

final class Circle extends Shape {
    private final double radius;

    public Circle(String color, double radius) {
        super(color);
        if (radius < 0) throw new IllegalArgumentException("Radius >= 0");
        this.radius = radius;
    }

    @Override public double area() { return Math.PI * radius * radius; }
    @Override public double perimeter() { return 2 * Math.PI * radius; }
}

final class Rectangle extends Shape {
    private final double width, height;

    public Rectangle(String color, double width, double height) {
        super(color);
        if (width < 0 || height < 0) throw new IllegalArgumentException("Dimensions >= 0");
        this.width = width; this.height = height;
    }

    @Override public double area() { return width * height; }
    @Override public double perimeter() { return 2 * (width + height); }
}
```

### Using super()
```java
class Child extends Parent {
    public Child() {
        super(); // Call parent no-arg constructor
    }

    @Override
    public void method() {
        super.method(); // Call parent implementation
        // child-specific logic
    }
}
```

---

## 6. Polymorphism

### Runtime Polymorphism
```java
Animal a = new Dog();  // Upcasting
a.makeSound(); // Calls Dog.makeSound()

// Downcasting with check
if (animal instanceof Dog dog) {
    dog.bark();
}

// Pattern matching (Java 16+)
if (animal instanceof Dog dog) {
    dog.bark();
}
```

### Method Dispatch
```
Reference Type: Animal          Object Type: Dog
animal.makeSound() ──────────▶ Dog.makeSound()  (Runtime)
```

---

## 7. Abstraction

### Abstract Class
```java
public abstract class Shape {
    protected String color;

    public Shape(String color) { this.color = color; }

    public abstract double area();
    public abstract double perimeter();

    public String getColor() { return color; } // Concrete
}
```

### Interface
```java
public interface Payable {
    void pay(BigDecimal amount);

    default void printReceipt() {
        System.out.println("Receipt printed");
    }

    static BigDecimal calculateTax(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.18));
    }
}
```

### Implementing Interface
```java
final class CreditCardPayment implements Payable {
    private final String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = mask(cardNumber);
    }

    @Override
    public void pay(BigDecimal amount) {
        System.out.println("Charging $" + amount + " to card " + cardNumber);
    }
}
```

---

## 7. Interfaces (Modern)

### Default & Static Methods
```java
public interface Payable {
    void pay(BigDecimal amount);  // Abstract

    default void printReceipt() {  // Default (Java 8+)
        System.out.println("Receipt printed");
    }

    static BigDecimal calculateTax(BigDecimal amount) {  // Static
        return amount.multiply(BigDecimal.valueOf(0.18));
    }
}
```

### Functional Interface
```java
@FunctionalInterface
interface Operation {
    int apply(int a, int b);
}

// Lambda usage
Operation add = (a, b) -> a + b;
Operation multiply = (a, b) -> a * b;
```

---

## 8. Object Class Methods

### equals() & hashCode()
```java
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money that = (Money) o;
        return amount.compareTo(that.amount) == 0 && currency == that.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
```

### toString()
```java
@Override
public String toString() {
    return "Money{amount=%s, currency=%s}".formatted(amount, currency);
}
```

---

## 9. equals() and hashCode()

### Template
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyClass that = (MyClass) o;
    return primitive == that.primitive && 
           Objects.equals(ref, that.ref);
}

@Override
public int hashCode() {
    return Objects.hash(primitive, ref);
}
```

---

## 10. Composition & Aggregation

### Composition (Strong Has-A)
```java
class House {
    private final List<Room> rooms = new ArrayList<>();

    public House() {
        rooms.add(new Room("Kitchen"));
        rooms.add(new Room("Bedroom"));
    }
}
```

### Aggregation (Weak Has-A)
```java
class Department {
    private List<Employee> employees = new ArrayList<>(); // Employees exist independently
}
```

### Association
```java
class Student {
    private List<Course> courses = new ArrayList<>();
}

class Course {
    private List<Student> students = new ArrayList<>();
}
```

---

## 11. Dependency Injection

### Constructor Injection
```java
public class OrderService {
    private final PaymentProcessor processor;

    public OrderService(PaymentProcessor processor) {
        this.processor = Objects.requireNonNull(processor);
    }
}
```

### Simple DI Container
```java
public final class DIContainer {
    private final Map<Class<?>, Object> instances = new HashMap<>();
    private final Map<Class<?>, Class<?>> bindings = new HashMap<>();

    public <T> void bind(Class<T> type, Class<? extends T> impl) {
        bindings.put(type, impl);
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        if (instances.containsKey(type)) return (T) instances.get(type);
        Class<?> impl = bindings.get(type);
        if (impl == null) throw new IllegalStateException("No binding for " + type);
        return createInstance(impl);
    }

    private <T> T createInstance(Class<?> impl) {
        try {
            Constructor<?> ctor = impl.getDeclaredConstructors()[0];
            Object[] params = new Object[ctor.getParameterCount()];
            for (int i = 0; i < params.length; i++) {
                params[i] = resolve(ctor.getParameterTypes()[i]);
            }
            T instance = (T) ctor.newInstance(params);
            instances.put((Class<T>) impl, instance);
            return instance;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot create " + impl, e);
        }
    }
}
```

---

## 11. SOLID Examples

### SRP - Single Responsibility
```java
// Violation
class UserManager {
    void saveUser(User u) { }
    void sendEmail(User u) { }
    void generateReport() { }
    void backupDatabase() { }
}

// Fix
interface UserRepository { void save(User u); }
interface EmailService { void send(User u, String msg); }
interface ReportGenerator { Report generate(User u); }
interface BackupService { void backup(); }
```

### OCP - Open/Closed
```java
// Violation
if (type == A) { ... } else if (type == B) { ... }

// Fix: Add new types by creating new classes
interface PaymentMethod { void pay(BigDecimal amount); }
class CreditCard implements PaymentMethod { }
class PayPal implements PaymentMethod { }
```

### LSP - Liskov Substitution
```java
// Violation: Square cannot substitute Rectangle
Rectangle r = new Square();
r.setWidth(5);
r.setHeight(10); // Square sets both to 10!
assert r.getWidth() == 5; // FAILS

// Fix: Separate interfaces
interface Shape { double area(); }
record Rectangle(double w, double h) implements Shape { }
record Square(double side) implements Shape { }
```

### ISP - Interface Segregation
```java
// Violation: Fat interface
interface IShape { void draw(); void resize(); void rotate(); }

// Fix
interface Drawable { void draw(); }
interface Resizable { void resize(); }
interface Rotatable { void rotate(); }
```

### DIP - Dependency Inversion
```java
// Violation
class OrderService {
    private StripeProcessor processor = new StripeProcessor();
}

// Fix
class OrderService {
    private final PaymentProcessor processor;
    public OrderService(PaymentProcessor processor) {
        this.processor = processor;
    }
}
```

---

## 12. Records (Java 16+)

```java
public record Person(String name, int age, String email) {
    public Person {
        if (name == null || name.isBlank()) throw new IllegalArgumentException();
        if (age < 0) throw new IllegalArgumentException();
    }

    // Auto: constructor, getters, equals, hashCode, toString
}
```

---

## 13. Design Patterns

### Strategy Pattern
```java
interface PaymentStrategy { void pay(BigDecimal amount); }

class CreditCard implements PaymentStrategy { }
class PayPal implements PaymentStrategy { }
class Bitcoin implements PaymentStrategy { }

class ShoppingCart {
    private PaymentStrategy strategy;
    public void setStrategy(PaymentStrategy s) { strategy = s; }
    public void checkout() { strategy.pay(total); }
}
```

### Builder Pattern
```java
public class Computer {
    private final String cpu, ram, storage, gpu;

    private Computer(Builder b) {
        this.cpu = b.cpu; this.ram = b.ram; this.storage = b.storage;
        this.gpu = b.gpu; this.motherboard = b.motherboard;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String cpu; private int ram; private String storage;
        private String gpu; private String motherboard;

        public Builder cpu(String c) { cpu = c; return this; }
        public Builder ram(int r) { ram = r; return this; }
        public Builder storage(String s) { storage = s; return this; }

        public Computer build() {
            if (cpu == null || ram <= 0 || storage == null)
                throw new IllegalStateException("Required fields missing");
            return new Computer(this);
        }
    }
}
```

---

*These examples demonstrate production-quality OOP code following Java 21 best practices and Google Java Style.*