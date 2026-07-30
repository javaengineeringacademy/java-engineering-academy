# Sprint 2 Solutions - Object Oriented Programming

---

## ✅ Exercise 1: Class Design - Person

```java
package com.javaacademy.sprint2.exercises;

public final class Person {
    private final String name;
    private int age;
    private final String email;

    public Person(String name, int age, String email) {
        this.name = name;
        this.age = validateAge(age);
        this.email = email;
    }

    private int validateAge(int age) {
        if (age < 0) throw new IllegalArgumentException("Age must be >= 0");
        return age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }

    public void setAge(int age) {
        this.age = validateAge(age);
    }

    @Override
    public String toString() {
        return "Person{name='%s', age=%d, email='%s'}".formatted(name, age, email);
    }
}
```

---

## ✅ Exercise 2: Immutable Point

```java
package com.javaacademy.sprint2.exercises;

public record Point(double x, double y) {
    public Point {
        if (Double.isNaN(x) || Double.isNaN(y)) {
            throw new IllegalArgumentException("Coordinates cannot be NaN");
        }
    }

    public double distanceTo(Point other) {
        double dx = this.x - other.x();
        double dy = this.y - other.y();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
```

---

## ✅ Exercise 3: Counter Class

```java
package com.javaacademy.sprint2.exercises;

public class Counter {
    private int count;

    public Counter() { this(0); }

    public Counter(int initial) {
        if (initial < 0) throw new IllegalArgumentException("Initial >= 0");
        this.count = initial;
    }

    public void increment() { count++; }
    public void decrement() { if (count > 0) count--; }
    public void reset() { count = 0; }
    public int getCount() { return count; }
}
```

---

## ✅ Exercise 4: Book Class

```java
package com.javaacademy.sprint2.exercises;

import java.util.Objects;

public final class Book {
    private final String title;
    private final String author;
    private final String isbn;
    private final int year;
    private final BigDecimal price;

    public Book(String title, String author, String isbn, int year, BigDecimal price) {
        this.title = Objects.requireNonNull(title, "Title required");
        this.author = Objects.requireNonNull(author, "Author required");
        this.isbn = validateIsbn(isbn);
        this.year = validateYear(year);
        this.price = Objects.requireNonNull(price, "Price required");
        if (price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Price >= 0");
    }

    private String validateIsbn(String isbn) {
        String clean = isbn.replaceAll("[-\\s]", "");
        if (clean.length() != 10 && clean.length() != 13) {
            throw new IllegalArgumentException("ISBN must be 10 or 13 digits");
        }
        return clean;
    }

    private int validateYear(int year) {
        if (year < 1000 || year > 2100) throw new IllegalArgumentException("Invalid year");
        return year;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getYear() { return year; }
    public BigDecimal getPrice() { return price; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() { return Objects.hash(isbn); }

    @Override
    public String toString() {
        return "Book{title='%s', author='%s', isbn='%s', year=%d, price=%s}".formatted(title, author, isbn, year, price);
    }
}
```

---

## ✅ Exercise 5: Shape Hierarchy

```java
package com.javaacademy.sprint2.exercises;

import java.util.Objects;

public abstract class Shape {
    private String color;

    protected Shape(String color) {
        this.color = Objects.requireNonNull(color, "Color required");
    }

    public abstract double area();
    public abstract double perimeter();

    public String getColor() { return color; }
    public void setColor(String color) { this.color = Objects.requireNonNull(color); }

    @Override
    public String toString() {
        return "Shape{color='%s', area=%.2f, perimeter=%.2f}".formatted(color, area(), perimeter());
    }
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
    public double getRadius() { return radius; }

    @Override public String toString() {
        return "Circle{" + super.toString() + ", radius=" + radius + "}";
    }
}

final class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(String color, double width, double height) {
        super(color);
        if (width < 0 || height < 0) throw new IllegalArgumentException("Dimensions >= 0");
        this.width = width;
        this.height = height;
    }

    @Override public double area() { return width * height; }
    @Override public double perimeter() { return 2 * (width + height); }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}

final class Triangle extends Shape {
    private final double a, b, c;

    public Triangle(String color, double a, double b, double c) {
        super(color);
        if (a <= 0 || b <= 0 || c <= 0) throw new IllegalArgumentException("Sides > 0");
        if (a + b <= c || a + c <= b || b + c <= a) throw new IllegalArgumentException("Invalid triangle");
        this.a = a; this.b = b; this.c = c;
    }

    @Override public double area() {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
    @Override public double perimeter() { return a + b + c; }
}
```

---

## ✅ Exercise 6: Payable Interface

```java
package com.javaacademy.sprint2.exercises;

import java.math.BigDecimal;

public interface Payable {
    void pay(BigDecimal amount);

    default void printReceipt() {
        System.out.println("Receipt: Payment of " + amount + " processed");
    }

    static BigDecimal calculateTax(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.18)).setScale(2, RoundingMode.HALF_UP);
    }
}

final class CreditCardPayment implements Payable {
    private final String cardNumber;
    public CreditCardPayment(String cardNumber) { this.cardNumber = mask(cardNumber); }

    @Override public void pay(BigDecimal amount) {
        System.out.println("Charging $" + amount + " to card " + cardNumber);
    }
    private String mask(String n) { return "**** **** **** " + n.substring(n.length() - 4); }
}

final class CashPayment implements Payable {
    @Override public void pay(BigDecimal amount) {
        System.out.println("Cash payment: $" + amount);
    }
}

final class BankTransfer implements Payable {
    private final String accountNumber;
    public BankTransfer(String accountNumber) { this.accountNumber = accountNumber; }
    @Override public void pay(BigDecimal amount) {
        System.out.println("Bank transfer of $" + amount + " to account " + accountNumber);
    }
}
```

---

## ✅ Exercise 7: Bank Account Hierarchy

```java
package com.javaacademy.sprint2.exercises;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Account {
    private final String accountNumber;
    protected BigDecimal balance;

    protected Account(String accountNumber, BigDecimal openingBalance) {
        this.accountNumber = Objects.requireNonNull(accountNumber);
        this.balance = Objects.requireNonNull(openingBalance);
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount > 0");
        balance = balance.add(amount);
    }

    public abstract void withdraw(BigDecimal amount);

    public BigDecimal getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
}

final class SavingsAccount extends Account {
    private final BigDecimal interestRate;

    public SavingsAccount(String accountNumber, BigDecimal openingBalance, BigDecimal interestRate) {
        super(accountNumber, openingBalance);
        this.interestRate = Objects.requireNonNull(interestRate);
    }

    @Override public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient funds");
        balance = balance.subtract(amount);
    }

    public BigDecimal calculateInterest() {
        BigDecimal interest = balance.multiply(interestRate).setScale(2, RoundingMode.HALF_UP);
        balance = balance.add(interest);
        return interest;
    }
}

final class CurrentAccount extends Account {
    private final BigDecimal overdraftLimit;

    public CurrentAccount(String accountNumber, BigDecimal openingBalance, BigDecimal overdraftLimit) {
        super(accountNumber, openingBalance);
        this.overdraftLimit = Objects.requireNonNull(overdraftLimit);
    }

    @Override public void withdraw(BigDecimal amount) {
        BigDecimal available = balance.add(overdraftLimit);
        if (available.compareTo(amount) < 0) throw new IllegalArgumentException("Exceeds overdraft");
        balance = balance.subtract(amount);
    }
}

final class Customer {
    private final String customerId;
    private final String name;
    private final List<Account> accounts = new ArrayList<>();

    public Customer(String customerId, String name) {
        this.customerId = customerId; this.name = name;
    }

    public void addAccount(Account account) { accounts.add(account); }
    public List<Account> getAccounts() { return List.copyOf(accounts); }
}
```

---

## ✅ Exercise 8: Strategy Pattern - Payment Processing

```java
package com.javaacademy.sprint2.exercises;

import java.math.BigDecimal;

interface PaymentStrategy {
    void pay(BigDecimal amount);
}

record CreditCardPayment(String cardNumber) implements PaymentStrategy {
    @Override public void pay(BigDecimal amount) {
        System.out.println("Credit Card " + cardNumber.substring(cardNumber.length()-4) + ": $" + amount);
    }
}

record PayPalPayment(String email) implements PaymentStrategy {
    @Override public void pay(BigDecimal amount) {
        System.out.println("PayPal (" + email + "): $" + amount);
    }
}

record BitcoinPayment(String walletAddress) implements PaymentStrategy {
    @Override public void pay(BigDecimal amount) {
        System.out.println("Bitcoin " + walletAddress.substring(0,8) + "...: " + amount + " BTC equivalent");
    }
}

class ShoppingCart {
    private PaymentStrategy paymentStrategy;
    private BigDecimal total = BigDecimal.ZERO;

    public void setPaymentStrategy(PaymentStrategy strategy) { this.paymentStrategy = strategy; }
    public void addItem(BigDecimal price) { total = total.add(price); }
    public void checkout() {
        if (paymentStrategy == null) throw new IllegalStateException("No payment method");
        paymentStrategy.pay(total);
        total = BigDecimal.ZERO;
    }
}
```

---

## ✅ Exercise 9: Employee Management System

```java
package com.javaacademy.sprint2.exercises;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Employee {
    private final String employeeId;
    private final String name;

    protected Employee(String employeeId, String name) {
        this.employeeId = Objects.requireNonNull(employeeId);
        this.name = Objects.requireNonNull(name);
    }

    public abstract BigDecimal calculateSalary();

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee that = (Employee) o;
        return employeeId.equals(that.employeeId);
    }

    @Override public int hashCode() { return Objects.hash(employeeId); }
}

final class FullTimeEmployee extends Employee {
    private final BigDecimal monthlySalary;

    public FullTimeEmployee(String id, String name, BigDecimal salary) {
        super(id, name);
        this.monthlySalary = salary;
    }
    @Override public BigDecimal calculateSalary() { return monthlySalary; }
}

final class PartTimeEmployee extends Employee {
    private final BigDecimal hourlyRate;
    private final int hoursPerMonth;

    public PartTimeEmployee(String id, String name, BigDecimal rate, int hours) {
        super(id, name);
        this.hourlyRate = rate; this.hoursPerMonth = hours;
    }
    @Override public BigDecimal calculateSalary() { return hourlyRate.multiply(BigDecimal.valueOf(hoursPerMonth)); }
}

final class Contractor extends Employee {
    private final BigDecimal projectFee;

    public Contractor(String id, String name, BigDecimal fee) {
        super(id, name);
        this.projectFee = fee;
    }
    @Override public BigDecimal calculateSalary() { return projectFee; }
}

class Department {
    private final String name;
    private final List<Employee> employees = new ArrayList<>();

    public Department(String name) { this.name = name; }
    public void addEmployee(Employee e) { employees.add(e); }
    public BigDecimal totalPayroll() {
        return employees.stream().map(Employee::calculateSalary).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

---

## ✅ Exercise 10: Immutable Data Classes

```java
package com.javaacademy.sprint2.exercises;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount, "Amount required");
        Objects.requireNonNull(currency, "Currency required");
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Amount >= 0");
    }

    public Money add(Money other) {
        if (!currency.equals(other.currency())) throw new IllegalArgumentException("Currency mismatch");
        return new Money(amount.add(other.amount), currency);
    }
}

public record Address(String street, String city, String state, String zipCode, String country) {
    public Address {
        Objects.requireNonNull(street); Objects.requireNonNull(city);
        Objects.requireNonNull(state); Objects.requireNonNull(zipCode);
        Objects.requireNonNull(country);
    }
}

public record Customer(String id, String name, String email, Address address) {
    public Customer {
        Objects.requireNonNull(id); Objects.requireNonNull(name);
        Objects.requireNonNull(email); Objects.requireNonNull(address);
        if (!email.contains("@")) throw new IllegalArgumentException("Invalid email");
    }
}
```

---

## ✅ Exercise 11: SOLID Violations & Fixes

**Violations:**
1. **SRP**: `UserManager` has 4 reasons to change (save, email, report, backup)
2. **OCP**: Adding new notification type requires modifying `UserManager`
3. **DIP**: Depends on concrete email implementation

**Refactored:**
```java
// Separate responsibilities
interface UserRepository { void save(User user); }
interface EmailService { void send(User user, String message); }
interface ReportGenerator { Report generate(User user); }
interface BackupService { void backup(); }

class UserManager {
    private final UserRepository repo;
    private final EmailService email;
    private final ReportGenerator reports;
    private final BackupService backup;

    public UserManager(UserRepository repo, EmailService email, ReportGenerator reports, BackupService backup) {
        this.repo = repo; this.email = email; this.reports = reports; this.backup = backup;
    }

    public void register(User user) {
        repo.save(user);
        email.send(user, "Welcome!");
    }
}
```

---

## ✅ Exercise 12: Liskov Substitution Principle

**Violation:** Square cannot substitute Rectangle
```java
Rectangle r = new Square();
r.setWidth(5);
r.setHeight(10);  // Square ignores height, sets both to 10!
assert r.getWidth() == 5;  // FAILS - width is 10
```

**Fix 1: Composition over Inheritance**
```java
interface Shape { double area(); double perimeter(); }

record Rectangle(double width, double height) implements Shape {
    public double area() { return width * height; }
    public double perimeter() { return 2 * (width + height); }
}

record Square(double side) implements Shape {
    public double area() { return side * side; }
    public double perimeter() { return 4 * side; }
}
```

**Fix 2: Interface Segregation**
```java
interface Resizable { void setWidth(double w); void setHeight(double h); }
interface Shape { double area(); }

class Rectangle implements Shape, Resizable { ... }
class Square implements Shape { ... }  // No Resizable
```

---

## ✅ Exercise 13: Builder Pattern

```java
package com.javaacademy.sprint2.exercises;

public final class Computer {
    private final String cpu;
    private final int ram;
    private final String storage;
    private final String gpu;
    private final String motherboard;
    private final String psu;
    private final String caseType;
    private final String cooling;

    private Computer(Builder b) {
        this.cpu = b.cpu; this.ram = b.ram; this.storage = b.storage;
        this.gpu = b.gpu; this.motherboard = b.motherboard;
        this.psu = b.psu; this.caseType = b.caseType; this.cooling = b.cooling;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String cpu; private int ram; private String storage;
        private String gpu; private String motherboard; private String psu;
        private String caseType; private String cooling;

        public Builder cpu(String cpu) { this.cpu = cpu; return this; }
        public Builder ram(int ram) { this.ram = ram; return this; }
        public Builder storage(String s) { this.storage = s; return this; }
        public Builder gpu(String g) { this.gpu = g; return this; }
        public Builder motherboard(String m) { this.motherboard = m; return this; }
        public Builder psu(String p) { this.psu = p; return this; }
        public Builder caseType(String c) { this.caseType = c; return this; }
        public Builder cooling(String c) { this.cooling = c; return this; }

        public Computer build() {
            if (cpu == null || ram <= 0 || storage == null) throw new IllegalStateException("Required fields missing");
            return new Computer(this);
        }
    }
}
```

---

## ✅ Exercise 14: Simple DI Container

```java
package com.javaacademy.sprint2.exercises;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public final class DIContainer {
    private final Map<Class<?>, Object> instances = new HashMap<>();
    private final Map<Class<?>, Class<?>> bindings = new HashMap<>();

    public <T> void bind(Class<T> type, Class<? extends T> impl) {
        bindings.put(type, impl);
    }

    public <T> void register(Class<T> type, T instance) {
        instances.put(type, instance);
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

// Usage:
DIContainer container = new DIContainer();
container.bind(PaymentProcessor.class, StripeProcessor.class);
container.register(EmailService.class, new MockEmailService());
PaymentProcessor processor = container.resolve(PaymentProcessor.class);
```

---

## ✅ Exercise 15: Observer Pattern

```java
package com.javaacademy.sprint2.exercises;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

interface AccountEventListener {
    void onDeposit(DepositEvent event);
    void onWithdrawal(WithdrawalEvent event);
    void onInterest(InterestEvent event);
}

record DepositEvent(String accountNumber, BigDecimal amount) {}
record WithdrawalEvent(String accountNumber, BigDecimal amount) {}
record InterestEvent(String accountNumber, BigDecimal interest) {}

class Account {
    private final String accountNumber;
    private BigDecimal balance;
    private final List<AccountEventListener> listeners = new ArrayList<>();

    public Account(String accountNumber, BigDecimal openingBalance) {
        this.accountNumber = accountNumber;
        this.balance = openingBalance;
    }

    public void addListener(AccountEventListener listener) { listeners.add(listener); }
    public void removeListener(AccountEventListener listener) { listeners.remove(listener); }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
        notifyListeners(new DepositEvent(accountNumber, amount));
    }

    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient funds");
        balance = balance.subtract(amount);
        notifyListeners(new WithdrawalEvent(accountNumber, amount));
    }

    private void notifyListeners(DepositEvent event) {
        for (AccountEventListener l : listeners) l.onDeposit(event);
    }
    private void notifyListeners(WithdrawalEvent event) {
        for (AccountEventListener l : listeners) l.onWithdrawal(event);
    }
    private void notifyListeners(InterestEvent event) {
        for (AccountEventListener l : listeners) l.onInterest(event);
    }
}

class EmailNotifier implements AccountEventListener {
    @Override public void onDeposit(DepositEvent e) { System.out.println("Email: Deposit " + e.amount() + " to " + e.accountNumber()); }
    @Override public void onWithdrawal(WithdrawalEvent e) { System.out.println("Email: Withdrawal " + e.amount() + " from " + e.accountNumber()); }
    @Override public void onInterest(InterestEvent e) { System.out.println("Email: Interest " + e.interest() + " on " + e.accountNumber()); }
}

class SmsNotifier implements AccountEventListener {
    @Override public void onDeposit(DepositEvent e) { System.out.println("SMS: Deposit " + e.amount() + " to " + e.accountNumber()); }
    @Override public void onWithdrawal(WithdrawalEvent e) { System.out.println("SMS: Withdrawal " + e.amount() + " from " + e.accountNumber()); }
    @Override public void onInterest(InterestEvent e) { System.out.println("SMS: Interest " + e.interest() + " on " + e.accountNumber()); }
}
```

---

## 📝 Common Mistakes to Avoid

| Mistake | Correct Approach |
|---------|------------------|
| `Square extends Rectangle` | Use composition or separate interfaces |
| Mutable fields in `hashCode()` | Use only immutable fields |
| `equals` without `hashCode` | Always override both together |
| Using `instanceof` + cast everywhere | Use polymorphism |
| God classes doing everything | Apply SRP, split responsibilities |
| Concrete dependency in constructor | Use interface + DI |
| `Square.setWidth()` changes height | Separate classes or composition |

---