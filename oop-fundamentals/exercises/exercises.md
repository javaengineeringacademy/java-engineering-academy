# Sprint 2 Exercises - Object Oriented Programming

---

## 🟢 Level 1: Beginner (Syntax Practice)

### Exercise 1: Class Design - Person
Create a `Person` class with:
- Private fields: `name` (String), `age` (int), `email` (String)
- Constructor with all fields
- Getters for all fields
- Setter for `age` only (with validation: age >= 0)
- `toString()` method

### Exercise 2: Immutable Point
Create an immutable `Point` record with:
- Fields: `x` (double), `y` (double)
- Compact constructor validating x,y are not NaN
- Method `distanceTo(Point other)` returning Euclidean distance

### Exercise 3: Counter Class
Create a `Counter` class with:
- Private `count` field (int)
- No-arg constructor (starts at 0)
- Constructor with initial value
- `increment()`, `decrement()`, `reset()` methods
- `getCount()` getter
- Prevent negative counts

### Exercise 4: Book Class
Create a `Book` class with:
- Fields: `title`, `author`, `isbn`, `year`, `price`
- Proper encapsulation with validation
- `equals()` and `hashCode()` based on ISBN
- `toString()` with all fields

---

## 🟡 Level 2: Intermediate (Combining Concepts)

### Exercise 5: Shape Hierarchy
Design an abstract `Shape` class with:
- Abstract methods: `area()`, `perimeter()`
- Concrete method: `getColor()`, `setColor()`
- Subclasses: `Circle`, `Rectangle`, `Triangle`
- Proper constructors with validation
- `toString()` showing all properties

### Exercise 6: Payable Interface
Create a `Payable` interface with:
- `pay(BigDecimal amount)` method
- Default method `printReceipt()`
- Static method `calculateTax(BigDecimal amount)`
- Implementations: `CreditCardPayment`, `CashPayment`, `BankTransfer`
- Each with specific behavior

### Exercise 7: Bank Account Hierarchy
Based on the Bank Management System:
- Abstract `Account` class with `deposit()`, abstract `withdraw()`, `getBalance()`
- `SavingsAccount`: no overdraft, interest calculation
- `CurrentAccount`: overdraft limit, different withdrawal logic
- `Customer` class with composition (has accounts)
- Proper `equals/hashCode` for Account

### Exercise 8: Strategy Pattern - Payment Processing
- Interface `PaymentStrategy` with `pay(amount)`
- Implementations: `CreditCard`, `PayPal`, `Bitcoin`
- `ShoppingCart` using strategy
- Runtime strategy switching

### Exercise 9: Employee Management System
- Abstract `Employee` with `calculateSalary()`
- Subclasses: `FullTimeEmployee`, `PartTimeEmployee`, `Contractor`
- `Department` class managing employees (composition)
- `equals/hashCode` for Employee based on employeeId

### Exercise 10: Immutable Data Classes
Create records for:
- `Money`: amount (BigDecimal), currency (Currency)
- `Address`: street, city, state, zipCode, country
- `Customer`: id, name, email, address
- Validation in compact constructors

---

## 🔴 Level 3: Advanced (Design & Architecture)

### Exercise 11: SOLID Violations & Fixes
Given this violating code:
```java
class UserManager {
    public void saveUser(User user) { ... }
    public void sendEmail(User user) { ... }
    public void generateReport() { ... }
    public void backupDatabase() { ... }
}
```
1. Identify SRP violations
2. Refactor into separate classes
3. Apply Dependency Inversion for email sending

### Exercise 12: Liskov Substitution Principle
```java
class Rectangle {
    protected int width, height;
    public void setWidth(int w) { width = w; }
    public void setHeight(int h) { height = h; }
}

class Square extends Rectangle {
    @Override public void setWidth(int w) { width = height = w; }
    @Override public void setHeight(int h) { width = height = h; }
}
```
1. Show LSP violation
2. Fix using composition or interface segregation

### Exercise 13: Builder Pattern
Create a `Computer` class with many-field class using Builder pattern:
- Required: cpu, ram, storage
- Optional: gpu, motherboard, psu, case, cooling
- Fluent API: `Computer.builder().cpu("i7").ram(32).build()`

### Exercise 14: Dependency Injection Container
Create a simple DI container:
- `register(Class<T> type, T instance)`
- `resolve(Class<T> type)`
- Support constructor injection
- Handle circular dependencies

### Exercise 15: Observer Pattern
Create event system for Bank Account:
- `AccountEventListener` interface
- Events: `DepositEvent`, `WithdrawalEvent`, `InterestEvent`
- `Account` notifies listeners
- Multiple listeners: `EmailNotifier`, `SmsNotifier`, `AuditLogger`

---

## 📋 Submission Guidelines

1. Create Java classes in appropriate packages
2. Include Javadoc for all public classes/methods
3. Follow Google Java Style (Checkstyle)
4. Write unit tests (JUnit 5) with 80%+ coverage
5. Run `mvn clean verify` before submitting

---

## 💡 Hints

| Exercise | Hint |
|----------|------|
| 1-4 | Use records for immutable data, classes for mutable |
| 5 | Use abstract class for shared color field |
| 6 | Default methods for common logic |
| 7 | Use composition: Customer HAS-A List<Account> |
| 8 | Strategy pattern = runtime algorithm swap |
| 9 | Abstract base + concrete subclasses |
| 10 | Records with compact constructors |
| 11 | SRP = one reason to change |
| 12 | LSP: Square can't substitute Rectangle |
| 13 | Builder for 4+ parameters |
| 14 | Map<Class, Object> + reflection |
| 15 | Observer = publish/subscribe |

---